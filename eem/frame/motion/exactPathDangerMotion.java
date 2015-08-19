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
	LinkedList<botStatPoint> path = new LinkedList<botStatPoint>();
	
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
				logger.warning("expected path point = " + path.getFirst().format() );
			}
			// end of algorithm check

			path.removeFirst();
		}
		if ( path.size() == 0 ) {
			choseNewPath();
		}
		moveToPoint( destPoint.getPosition() );
		// end of exact check
	}

	public void choseNewPath() {
		Point2D.Double pp = (Point2D.Double) myBot.getPosition().clone();
		double a= 2*Math.PI * Math.random();
		double R = 100;
		pp.x += R*Math.sin( a ); 
		pp.y += R*Math.cos( a ); 
		long pathLength = 20;
		destPoint = new dangerPoint( pp, 0 );
		path = pathSimulator.getPathTo( pp, myBot.getStatClosestToTime( myBot.getTime() ), pathLength );
	}

	public void makeMove() {
		// for basic motion we do nothing
	}

	public boolean isItWithReacheableSpace( Point2D.Double p ) {
		double dist = physics.shortestDist2wall( p );
		if ( dist < physics.robotHalfSize )
			return false;
		return true;
	}

	public void onPaint(Graphics2D g) {
		// mark destination point
		g.setColor(new Color(0x00, 0xff, 0x00, 0x80));
		graphics.drawCircle(g, destPoint.getPosition(), 10);


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
		_dangerMapFull.reCalculateDangerMap( myBot.getTime() );
		_dangerMapFull.onPaint(g);
	}

}
