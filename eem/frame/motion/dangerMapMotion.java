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



public class dangerMapMotion extends basicMotion {
	protected fighterBot myBot;
	public dangerMap _dangerMap;
	private double superDanger = 1e8;
	public dangerPoint destPoint = null;
	
	public void initTic() {
		_dangerMap.calculateDanger( myBot.getTime() );
	}

	public dangerMapMotion() {
	}

	public dangerMapMotion(fighterBot bot) {
		myBot = bot;
		initBattle( myBot );
		_dangerMap = new dangerMap( myBot );
		destPoint = new dangerPoint( new Point2D.Double(0,0), superDanger);
	}

	public void manage() {
		// make set of points around bot to check for danger
		_dangerMap.clearDangerPoints();
		buildListOfPointsToTestForDanger();
		double dL = destPoint.calculateDanger( myBot.getTime(), myBot );
		destPoint.setDanger( dL );
		_dangerMap.calculateDanger( myBot.getTime() );
		dangerPoint dPnew = _dangerMap.getSafestPoint();
		if ( destPoint.compareTo( dPnew ) > 0 ) {
			destPoint = dPnew;
		}
		moveToPoint( destPoint.getPosition() );
	}

	public void makeMove() {
		// for basic motion we do nothing
	}

	private void buildListOfPointsToTestForDanger() {
		double R = 40;
		int Np = 20;
		Point2D.Double myCoord = null;
		myCoord = myBot.getPosition();
		if ( myCoord == null ) {
			myCoord = new Point2D.Double( 0, 0 );
		}
		for( int i=0; i < Np; i++ ) {
			double a = 2*Math.PI/(Np-1) * i;
			Point2D.Double p = new Point2D.Double( R*Math.cos(a), R*Math.sin(a) );
			p.x = myCoord.x + p.x;
			p.y = myCoord.y + p.y;
			if ( physics.isItWithInBotReacheableSpace( p ) ) {
				// Repeat after me, I will never probe the point
				// where robot hits the wall or outside of the Battle field
				_dangerMap.add( p );
			}
		}
	}

	public void onPaint(Graphics2D g) {
		// mark destination point
		g.setColor(new Color(0x00, 0xff, 0x00, 0x80));
		graphics.drawCircle(g, destPoint.getPosition(), 10);


		_dangerMap.onPaint(g); // motion danger map

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
		_dangerMapFull.onPaint(g);
	}

}
