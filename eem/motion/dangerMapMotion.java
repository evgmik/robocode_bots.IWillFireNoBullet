// -*- java -*-

package eem.motion;

import eem.core.*;
import eem.dangermap.*;
import eem.bot.*;
import eem.misc.*;

import robocode.util.*;

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
		_dangerMap.reCalculateDangerMap( myBot.getTime() );
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
		double dL = _dangerMap.calculateDangerForPoint( destPoint, myBot.getTime() );
		destPoint.setDanger( dL );
		_dangerMap.reCalculateDangerMap( myBot.getTime() );
		dangerPoint dPnew = _dangerMap.getSafestPoint();
		if ( destPoint.compareTo( dPnew ) > 0 )
			destPoint = dPnew;
		moveToPoint( destPoint.getPosition() );
	}

	public void makeMove() {
		// for basic motion we do nothing
	}

	private void buildListOfPointsToTestForDanger() {
		double R = 100;
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
			_dangerMap.add( p );
		}
	}

	public void onPaint(Graphics2D g) {
		// mark destination point
		g.setColor(new Color(0x00, 0xff, 0x00, 0x80));
		graphics.drawCircle(g, destPoint.getPosition(), 10);


		_dangerMap.onPaint(g);
	}

}
