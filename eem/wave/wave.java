// -*- java -*-

package eem.wave;

import eem.IWillFireNoBullet;
import eem.bot.*;
import eem.misc.*;

import robocode.Bullet;
import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.*;

public class wave {
	public InfoBot firedBot = null;
	protected Point2D.Double firedPosition;
	protected long   firedTime;
	protected double bulletSpeed;
	protected double bulletEnergy;
	protected long timeUncertaintyLower = 0; // how sure we are about firing time
	protected Color waveColor = new Color(0xff, 0x00, 0x00, 0x80);
	protected Color waveUncertaintyColorLower = new Color(0x00, 0x00, 0xff, 0x80);


	public wave(InfoBot firedBot, long firedTime, double bulletEnergy) {
		this.firedBot = firedBot;
		this.firedTime = firedTime;
		this.bulletEnergy = bulletEnergy;
		this.bulletSpeed = physics.bulletSpeed( bulletEnergy );
		Point2D.Double fP = firedBot.getPositionAtTime( firedTime );
		if ( fP != null ) {
			this.firedPosition = (Point2D.Double) fP.clone();
			this.timeUncertaintyLower = 0;
		} else {
			// we do not know position of the bot at firing time
			// but we do know that exactly the time upper bound
			// which is firedTime since it is coming from onScannedRobot
			// so we will use last known (i.e. current) position
			//FIXME uncertainty logic
			this.firedPosition = (Point2D.Double) firedBot.getLast().getPosition().clone();
			this.timeUncertaintyLower = firedTime - firedBot.getPrev().getTime();
			logger.noise("timeUncertaintyLower = " + timeUncertaintyLower );
		}
	}

	public InfoBot getFiredBot() {
		return firedBot;
	}

	public long getFiredTime() {
		return firedTime;
	}

	public double getBulletEnergy() {
		return bulletEnergy;
	}

	public double getDistanceTraveledAtTime(long time) {
		double timeInFlight = time - firedTime;
		double distTraveled = timeInFlight * bulletSpeed;
		return distTraveled;
	}

	public boolean isBehindBot(InfoBot bot, long timeNow) {
		double distTraveled = getDistanceTraveledAtTime( timeNow );
		Point2D.Double botPos = bot.getPosition( timeNow );
		if ( botPos == null ) {
			// position is unknown, using last known
			botPos = bot.getLast().getPosition();
		}
		double distToBot = botPos.distance( firedPosition ); 
		if ( distTraveled > distToBot + physics.robotHalfSize )
			return true;
		else
			return false;
	}

	public boolean equals( wave w ) {
		boolean ret = true;
		if ( !this.getFiredBot().getName().equals( w.getFiredBot().getName() ) )
			return false;
		if ( this.getFiredTime() != w.getFiredTime() )
			return false;
		if ( this.getBulletEnergy() != w.getBulletEnergy() )
			return false;
		if ( this.firedPosition.x != w.firedPosition.x )
			return false;
		if ( this.firedPosition.y != w.firedPosition.y )
			return false;

		return true;

	}

	public void onPaint(Graphics2D g, long timeNow) {
		g.setColor(waveColor);

		// draw overall  wave
		double distTraveled = getDistanceTraveledAtTime( timeNow );
		graphics.drawCircle(g, firedPosition, distTraveled);
		if ( timeUncertaintyLower !=  0 ) {
			g.setColor(waveUncertaintyColorLower);
			graphics.drawCircle(g, firedBot.getPositionAtTime(firedTime - timeUncertaintyLower), distTraveled + bulletSpeed * timeUncertaintyLower);
		}
	}
}
