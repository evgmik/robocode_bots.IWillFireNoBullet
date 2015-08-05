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
	protected Color waveColor = new Color(0xff, 0x00, 0x00, 0x80);


	public wave(InfoBot firedBot, long firedTime, double bulletEnergy) {
		this.firedBot = firedBot;
		this.firedTime = firedTime;
		this.bulletSpeed = physics.bulletSpeed( bulletEnergy );
		this.firedPosition = (Point2D.Double) firedBot.getPositionAtTime( firedTime ).clone(); 
	}

	public double getDistanceTraveledAtTime(long time) {
		double timeInFlight = time - firedTime;
		double distTraveled = timeInFlight * bulletSpeed;
		return distTraveled;
	}

	public boolean isBehindBot(InfoBot bot, long timeNow) {
		double distTraveled = getDistanceTraveledAtTime( timeNow );
		Point2D.Double botPos = bot.getPosition( timeNow );
		double distToBot = botPos.distance( firedPosition ); 
		if ( distTraveled > distToBot + 2*physics.robotHalfSize )
			return true;
		else
			return false;
	}

	public void onPaint(Graphics2D g, long timeNow) {
		g.setColor(waveColor);

		// draw overall  wave
		double distTraveled = getDistanceTraveledAtTime( timeNow );
		graphics.drawCircle(g, firedPosition, distTraveled);
	}
}
