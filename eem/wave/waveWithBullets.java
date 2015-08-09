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

	public void onPaint(Graphics2D g, long timeNow) {
		super.onPaint( g, timeNow );
		g.setColor(waveColor);

		// draw overall  wave
		double bulletDistance = getDistanceTraveledAtTime( timeNow ) - 5;
		for ( firingSolution fS : firingSolutions ) {
			fS.onPaint( g, bulletDistance );
		}
	}
}
