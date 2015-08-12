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
		double waveDangerRadius = 50;
		double dL = 0;
		double dist = dP.distance( firedPosition ) - getDistanceTraveledAtTime( time );
		for ( firingSolution fS : firingSolutions ) {
			dL += fS.getDanger( time, dP );
		}
		dL *= Math.exp( - Math.abs(dist)/waveDangerRadius );
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
