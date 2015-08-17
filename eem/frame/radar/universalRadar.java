// -*- java -*-
// (C) 2013 by Eugeniy Mikhailov, <evgmik@gmail.com>

package eem.frame.radar;

import eem.frame.core.*;
import eem.frame.bot.*;
import eem.frame.misc.*;

import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import robocode.*;
import robocode.util.*;
import robocode.Rules.*;

public class universalRadar extends baseRadar{
	protected int radarSpinDirection =1;
	protected static double radarMaxRotationAngle;
	protected LinkedList<String> scannedBots = new LinkedList<String>();
	protected String botToSearchFor = "";
	boolean needToTrackTarget = false;

	public universalRadar(fighterBot bot) {
		super(bot);
		initBattle(bot);
	}

	public void initBattle(fighterBot b) {
		myBot = b;
		radarMaxRotationAngle = Rules.RADAR_TURN_RATE ;
		radarSpinDirection = 1;
		needToTrackTarget = false;
		botToSearchFor = "";
		scannedBots = new LinkedList<String>();
	}

	public void initTic() {
		myBot.proxy.setAdjustRadarForGunTurn(true); // decouple gun and radar
	}

	public void setNeedToTrackTarget( boolean flag ) {
		needToTrackTarget = flag;
	}

	public void manage() {
		double angle = 0;
		if ( myBot.getNumEnemyAlive() == 0) {
			// we already won, no need to do anything
			logger.noise("radar: no enemies left");
			return;
		}

		if ( scannedBots.size() < myBot.getNumEnemyAlive() ) {
			// this should be done only once at the begining of the round
			// we have not seen all bots thus we need to do/keep sweeping
			// performing initial sweep
			logger.noise("radar: sweeping battle field 1st time");
			angle = radarSpinDirection*radarMaxRotationAngle;
			setTurnRadarRight(angle);
			return;
		}

		if ( needToTrackTarget ) {
			//if ( myBot._trgt.haveTarget ) {
			if ( false ) { //FIXME disabled logic for now, implement above _trgt.haveTarget
				//String bName = myBot._trgt.getName();
				//moveRadarToBot( bName );
			} else {
				refreshBotsPositions();
			}
			return;
		}

		// if nothing of above
		refreshBotsPositions();
	}

	public void refreshBotsPositions() {
		logger.noise("radar: refreshBotsPositions");
		String bName = scannedBots.getFirst();
		moveRadarToBot( bName );
	}

	public void moveRadarToBot( String bName ) {
		logger.noise("Spinning radar to bot " + bName);
		double angle = 0;
		long lastSeenDelay = myBot.getGameInfo().getTime() -  myBot.getGameInfo()._botsmanager.getBotByName( bName ).getLastSeenTime();
		if ( botToSearchFor.equals( bName ) && (lastSeenDelay > 1) ) {
			// we already set radar motion parameters
			angle = radarSpinDirection*radarMaxRotationAngle;
		} else {
			// new bot to search or we just saw this bot so its position
			// can be used for radar spin calculations
			botToSearchFor = bName;
			double radar_heading = myBot.proxy.getRadarHeading();
			double angleToLastBotPosition = math.angle2pt(myBot.getPosition(), myBot.getGameInfo()._botsmanager.getBotByName( bName ).getPosition() );
			angle= angleToLastBotPosition - radar_heading;
			angle = math.shortest_arc(angle);
			radarSpinDirection = math.signNoZero(angle);
			angle = Math.abs( angle );
				angle+=radarMaxRotationAngle/2; // we want to overshoot
			angle = radarSpinDirection*angle;
		}
		setTurnRadarRight(angle);
	}

	protected void setTurnRadarRight(double angle) {
		double angle2rotate = math.putWithinRange(angle, -radarMaxRotationAngle, radarMaxRotationAngle);
		logger.noise("radar: sweeping direction = " + radarSpinDirection);
		logger.noise("radar: rotation angle = " + angle2rotate);
		myBot.proxy.setTurnRadarRight(angle2rotate);
	}

	public void onRobotDeath(RobotDeathEvent e) {
		String dBotName = e.getName();
		scannedBots.remove( dBotName );
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		String scannedBotName = e.getName();
		for ( String bName : scannedBots ) {
			if ( bName.equals( scannedBotName ) ) {
				scannedBots.remove( bName );
				break;
			}
		}
		scannedBots.addLast( scannedBotName );
	}

	public String toString() {
		String str = "";
		str += "universalRadar stats\n";
		str += " bots known " + scannedBots.size() +  " out of " + myBot.getNumEnemyAlive() + " alive\n";
		for ( String bName : scannedBots ) {
			str += "  bot: " + bName + "\n";
		}
		return str;
	}
}

