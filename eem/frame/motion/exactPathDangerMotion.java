// -*- java -*-

package eem.frame.motion;

import eem.frame.core.*;
import eem.frame.motion.*;
import eem.frame.dangermap.*;
import eem.frame.bot.*;
import eem.frame.misc.*;

import robocode.util.*;

import java.util.*;
import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.awt.Color;



public class exactPathDangerMotion extends basicMotion {
	protected fighterBot myBot;
	private double superDanger = 1e8;
	public dangerPoint destPoint = null;
	dangerPath path = new dangerPath();
	long minimalPathLength = 10;
	
	public void initTic() {
	}

	public exactPathDangerMotion() {
	}

	public exactPathDangerMotion(fighterBot bot) {
		myBot = bot;
		initBattle( myBot );
		destPoint = new dangerPoint( new Point2D.Double(0,0), superDanger);
	}

	public void manage() {
		// make set of points around bot to check for danger
		// here I check exact path simulator
		if ( path.size() >= 1 ) {
			// NOTE: this is for algorithm mistakes notifications
			if ( myBot.getPosition().distance( path.getFirst().getPosition() ) > 1 ) {
				logger.warning("--- Check path simulator! ---");
				logger.warning("path size " + path.size() );
				logger.warning("current  path point = " + myBot.getStatClosestToTime( myBot.getTime() ).format() );
				logger.warning("expected path point = " + path.getFirst().toString() );
			}
			// end of algorithm check

			path.removeFirst();
		}
		//choseNewPath();
		if ( path.size() == 0 ) {
			choseNewPath();
		}
		moveToPoint( destPoint.getPosition() );
		// end of exact check
	}

	public void choseNewPath() {
		choseNewPath( minimalPathLength );
	}

	public void choseNewPath( long pathLength ) {
		dangerPath pathTrial;
		pathLength = Math.max( minimalPathLength, pathLength );
		Point2D.Double myPos = (Point2D.Double) myBot.getPosition().clone();
		Point2D.Double pp;
		long nTrials = 150;
		path.setDanger(1e6); // crazy dangerous for initial sorting

		for ( long i = 0; i < nTrials; i++ ) {
			pp = new Point2D.Double(0,0);
			double a= 2*Math.PI * Math.random();
			double R = pathLength*robocode.Rules.MAX_VELOCITY * Math.random();
			pp.x = myPos.x + R*Math.cos( a ); 
			pp.y = myPos.y + R*Math.sin( a ); 
			pathTrial = new dangerPath( pathSimulator.getPathTo( pp, myBot.getStatClosestToTime( myBot.getTime() ), pathLength ) );
			pathTrial.calculateDanger( myBot );
			if ( path.getDanger() > pathTrial.getDanger() ) {
				//logger.dbg("Choosing new path with danger = " + pathTrial.getDanger()); 
				path = pathTrial;
				destPoint = new dangerPoint( pp, 0 );
			}
		}
	}

	public void makeMove() {
		// for basic motion we do nothing
	}

	public void onPaint(Graphics2D g) {
		// mark destination point
		g.setColor(new Color(0x00, 0xff, 0x00, 0x80));
		graphics.drawCircle(g, destPoint.getPosition(), 10);

		path.onPaint(g);


		// here I draw full danger map picture
		dangerMap _dangerMapFull = new dangerMap( myBot );
		// now I populate the map with dense points
		int Npx = 20;
		int Npy = 20;
		for( int i=0; i < Npx; i++ ) {
			for( int k=0; k < Npy; k++ ) {
				Point2D.Double p = new Point2D.Double( 0,0 );
				p.x = physics.BattleField.x / (Npx+1) * (i+1) ;
				p.y = physics.BattleField.y / (Npy+1) * (k+1) ;
				_dangerMapFull.add( p );
			}
		}
		_dangerMapFull.calculateDanger( myBot.getTime() );
		//_dangerMapFull.onPaint(g);
	}

}
