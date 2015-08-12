// -*- java -*-

package eem.wave;

import eem.core.*;
import eem.bot.*;
import eem.gun.*;
import eem.misc.*;

import robocode.Bullet;
import java.util.LinkedList;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class waveWithBullets extends wave {
	public LinkedList<firingSolution> firingSolutions = new LinkedList<firingSolution>();

	public waveWithBullets( wave w ) {
		super( w.getFiredBot(), w.getFiredTime(), w.getBulletEnergy() );
	}

	public void addFiringSolution( firingSolution fS ) {
		firingSolutions.add(fS);
	}

	public double getDanger( long time, Point2D.Double dP ) {
		double waveDangerRadius = 100;
		double waveDanger= 1.0;
		double dL = 0;
		double dist = dP.distance( firedPosition ) - getDistanceTraveledAtTime( time );
		for ( firingSolution fS : firingSolutions ) {
			dL += fS.getDanger( time, dP );
		}
		// The danger from the far wave drops exponential with distance
		// but close one is uniform, this should push bot perpendicularly
		// away from enemy bullet path.
		if ( Math.abs(dist) > waveDanger )
			dL *= waveDanger * Math.exp( - Math.abs(dist)/waveDangerRadius );
		return dL;
	}

	public void onPaint(Graphics2D g, long time) {
		super.onPaint( g, time );
		g.setColor(waveColor);

		// draw overall  wave
		for ( firingSolution fS : firingSolutions ) {
			fS.onPaint( g, time );
		}
	}
}
