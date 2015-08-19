// -*- java -*-

package eem.frame.dangermap;

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



public class dangerPath implements Comparable<dangerPath> {
	private LinkedList<dangerPathPoint> path = new LinkedList<dangerPathPoint>();
	private double dangerLevel = 0;

	public dangerPath(){};

	public dangerPath( LinkedList<botStatPoint> bStatsList){
		double dL = 0;
		path = new LinkedList<dangerPathPoint>();
		ListIterator<botStatPoint> iter = bStatsList.listIterator();
		botStatPoint  bSt;
		while (iter.hasNext()) {
			bSt = iter.next();
			path.add ( new dangerPathPoint( bSt, dL ) );
		}
	};

	public void add(dangerPathPoint dP) {
		path.add( dP );
		dangerLevel += dP.getDanger();
	}

	public int size() {
		return path.size();
	}

	public dangerPathPoint get(int i) {
		if ( i >= size() )
			return null;
		return path.get(i);
	}

	public double getDanger() {
		return dangerLevel;
	}

	public void setDanger( double dL) {
		dangerLevel = dL;
	}

	public dangerPathPoint removeFirst() {
		dangerPathPoint dP = path.removeFirst();
		dangerLevel -= dP.getDanger();
		return dP;
	}

	public dangerPathPoint getFirst() {
		dangerPathPoint dP = path.getFirst();
		return dP;
	}


	public int compare(dangerPath p1, dangerPath p2) {
		double dL1 = p1.getDanger();
		double dL2 = p2.getDanger();
		if ( dL1 == dL2 ) return 0;
		if ( dL1 >  dL2 ) return 1;
		return -1;
	}

	public int compareTo( dangerPath p2) {
		return compare( this, p2);
	}

	public double calculateDanger() {
		dangerPathPoint  dP;
		double dL = 0;
		ListIterator<dangerPathPoint> iter = path.listIterator();
		while (iter.hasNext()) {
			dP = iter.next();
			dL += dP.calculateDanger();
		}
		setDanger(dL);
		return dL;
	}

	public void print() {
		ListIterator<dangerPathPoint> iter = path.listIterator();
		dangerPathPoint oldP=null;
		dangerPathPoint  dP;
		while (iter.hasNext()) {
			dP = iter.next();
			logger.dbg( dP.toString() );
		}
		logger.dbg("Path danger = " + dangerLevel);
	}

	public void onPaint(Graphics2D g) {
		ListIterator<dangerPathPoint> iter = path.listIterator();
		dangerPathPoint oldP=null;
		dangerPathPoint  dP;
		while (iter.hasNext()) {
			dP = iter.next();

			// path between points
			g.setColor(Color.blue);
			if ( oldP != null ) {
				graphics.drawLine(g,  dP.getPosition(), oldP.getPosition());
			}
			oldP = dP;

			dP.onPaint(g);
		}
	}
}

