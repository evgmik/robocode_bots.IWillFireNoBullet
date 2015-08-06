// -*- java -*-

package eem.dangermap;

import eem.dangermap.*;
import eem.misc.*;
import eem.wave.*;
import eem.bot.*;

import java.util.LinkedList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class dangerMap {
	public LinkedList<dangerPoint> dangerPoints;
	public long ticTime=0;

	public dangerMap() {
		dangerPoints = new LinkedList<dangerPoint>();
	}

	public void add( dangerPoint p ) {
		dangerPoints.add( p );
	}

	public void add( Point2D.Double p ) {
		double dangerLevel = 0;
		dangerPoints.add( new dangerPoint(p, dangerLevel) );
	}

	public void calculateDanger(long time) {
		ticTime = time;
		for( dangerPoint dP: dangerPoints ) {
			calculateDangerFromWall(time, dP);
		}
	}

	public void calculateDangerFromWall(long time, dangerPoint dP) {
		double dangerFromWall = 1.0;
		if ( physics.shortestDist2wall( dP.getPosition() ) <= physics.robotHalfSize ) {
			dP.setDanger( dangerFromWall );
		}

	}

	public void onPaint(Graphics2D g) {
		for( dangerPoint dP: dangerPoints ) {
			dP.onPaint(g);
		}
	}
}
