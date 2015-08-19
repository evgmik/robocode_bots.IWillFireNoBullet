// -*- java -*-

package eem.frame.dangermap;

import eem.frame.bot.*;
import eem.frame.misc.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class dangerPathPoint implements Comparable<dangerPathPoint> {
	public botStatPoint botStat;
	public double dangerLevel;

	public dangerPathPoint() {
		botStat  = new botStatPoint();
		dangerLevel = 0;
	}

	public dangerPathPoint( botStatPoint bSt, double dL ) {
		botStat = bSt;
		dangerLevel = dL;
	}

	public double getDanger() {
		return dangerLevel;
	}

	public double calculateDanger( fighterBot myBot ) {
		double dL = 0;
		long time = botStat.getTime();
		dL += dangerCalc.calculateDangerFromWall(time, botStat.getPosition(), myBot);
		dL += dangerCalc.calculateDangerFromEnemyBots(time, botStat.getPosition(), myBot);
		// FIXME: we do not need precursors from waves
		dL += dangerCalc.calculateDangerFromEnemyWaves(time, botStat.getPosition(), myBot);
		setDanger(dL);
		return dL;
	}
	
	public void setDanger(double dL) {
		dangerLevel = dL;
	}

	public Point2D.Double getPosition() {
		return botStat.getPosition();
	}

	public void setPosition( Point2D.Double p ) {
		botStat.setPosition( (Point2D.Double) p.clone() );
	}

	public int compare(dangerPathPoint p1, dangerPathPoint p2) {
		double dL1 = p1.dangerLevel;
		double dL2 = p2.dangerLevel;
		if ( dL1 == dL2 ) return 0;
		if ( dL1 >  dL2 ) return 1;
		return -1;
	}

	public int compareTo( dangerPathPoint p2) {
		return compare( this, p2);
	}

	public String toString() {
		String str = "";
		str += "Point " + botStat.format() + "\n" + " has danger level = " + dangerLevel;
		return str;
	}

	public void onPaint(Graphics2D g) {
		Point2D.Double p;
		p = this.botStat.getPosition();
		double dL = this.dangerLevel;
		g.setColor( graphics.dangerLevel2mapColor( dL ) );
		// circle surroundig point, representing its danger
		double pR = 5;
		graphics.drawCircle(g, p, pR);
		// put dot in the middle
		g.setColor( new Color(0x00, 0x00, 0xaa, 0xff) );
		double dR = 2;
		graphics.drawCircle(g, p, dR);
	}

}
