// -*- java -*-

package eem.dangermap;

import eem.misc.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class dangerPoint implements Comparable<dangerPoint> {
	public Point2D.Double position;
	public double dangerLevel;

	public dangerPoint() {
		position  = new Point2D.Double(0,0);
		dangerLevel = 0;
	}

	public dangerPoint( Point2D.Double p, double dL ) {
		position = (Point2D.Double) p.clone();
		dangerLevel = dL;
	}

	public double getDanger() {
		return dangerLevel;
	}

	public void setDanger(double dL) {
		dangerLevel = dL;
	}

	public Point2D.Double getPosition() {
		return position;
	}

	public void setPosition( Point2D.Double p ) {
		position = (Point2D.Double) p.clone();
	}

	public int compare(dangerPoint p1, dangerPoint p2) {
		double dL1 = p1.dangerLevel;
		double dL2 = p2.dangerLevel;
		if ( dL1 == dL2 ) return 0;
		if ( dL1 >  dL2 ) return 1;
		return -1;
	}

	public int compareTo( dangerPoint p2) {
		return compare( this, p2);
	}

	public void print() {
		logger.dbg("Point [" + position.x + ", " + position.y + "]" + " has danger level = " + dangerLevel);
	}

	public void onPaint(Graphics2D g) {
		Point2D.Double p;
		p = this.position;
		double dL = this.dangerLevel;
		g.setColor( graphics.dangerLevel2mapColor( dL ) );
		//g.setColor( new Color(0x00, 0x00, 0xaa, 0xff) );
		g.drawOval((int) p.x-5, (int) p.y-5, 10, 10);
		// put dot in the middle
		g.setColor( new Color(0x00, 0x00, 0xaa, 0xff) );
		g.drawOval((int) p.x-2, (int) p.y-2, 2, 2);
	}

}
