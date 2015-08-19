// -*- java -*-

package eem.frame.dangermap;

import eem.frame.dangermap.*;
import eem.frame.misc.*;
import eem.frame.wave.*;
import eem.frame.bot.*;

import java.util.LinkedList;
import java.util.Collections;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class dangerMap {
	public LinkedList<dangerPoint> dangerPoints;
	public long ticTime=0;
	public fighterBot myBot;

	public dangerMap(fighterBot myBot) {
		dangerPoints = new LinkedList<dangerPoint>();
		this.myBot = myBot;
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
		double dL;
		for( dangerPoint dP: dangerPoints ) {
			dP.calculateDanger( time, myBot );
		}
	}



	public void sortDangerPoints() {
		Collections.sort(dangerPoints);
	}

	public dangerPoint getSafestPoint() {
		sortDangerPoints();
		return dangerPoints.getFirst();
	}

	public void clearDangerPoints() {
		dangerPoints.clear();
	}


	public void onPaint(Graphics2D g) {
		for( dangerPoint dP: dangerPoints ) {
			dP.onPaint(g);
		}
	}
}
