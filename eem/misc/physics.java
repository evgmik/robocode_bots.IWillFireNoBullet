// -*- java -*-
package eem.misc;

import java.awt.geom.Point2D;
import java.util.*;

import robocode.*;
import robocode.util.*;
import robocode.Rules.*;

public class physics {
	public static int robotHalfSize = 0;
	public static double robotRadius = 0;
	public static Point2D.Double BattleField = new Point2D.Double(0,0);
	public static double coolingRate = 0.1; 
	public static double minimalAllowedBulletEnergy = 0.1; 
	// using maxTurnsInRound to have ticTime growing Round independent
	// robocode itself reset Turn=getTime() to 0 every Round
	public static long maxTurnsInRound = 100000; // maximum # of Turns/Tics per round

	public static void init(AdvancedRobot myBot) {
		robotHalfSize = (int) myBot.getWidth()/2;
		robotRadius = robotHalfSize*Math.sqrt(2);
		BattleField = new Point2D.Double(myBot.getBattleFieldWidth(), myBot.getBattleFieldHeight());
		coolingRate = myBot.getGunCoolingRate();
	}

	public static long ticTimeFromTurnAndRound ( long Turn, long Round ) {
		return Turn + (Round+1)*maxTurnsInRound;
	}

	public static long getRoundStartTime(long ticTime) {
		return (long) Math.floor(ticTime/maxTurnsInRound)*maxTurnsInRound;
	}


	public static int gunCoolingTime( double heat ) {
		return (int) Math.ceil( heat/coolingRate );
	}

	public static double bulletSpeed( double firePower ) {
		double bSpeed;
		bSpeed = ( 20 - firePower * 3 );
		logger.noise("bullet speed = " + bSpeed + " for firePower = " + firePower);
		return bSpeed;
	}

	public static double  bulletEnergy( double bulletSpeed ) {
		double bEnergy = ( 20 - bulletSpeed ) / 3;
		return bEnergy;
	}

	public static double  bulletDamageByEnergy( double bEnergy ) {
		double bDamage = 4*bEnergy + 2 * Math.max( bEnergy - 1 , 0 );
		return bDamage;
	}

	public static double minReqBulEnergyToKillTarget(double target_energy) {
		double tinyBity = 0.1; // in case if there were rounding in energy report
		target_energy = target_energy + tinyBity;
		// Bullet_damage = 4 * bullet_power + 2 * max(bullet_power - 1 , 0) see wiki
		double bPower = target_energy/4;
	       	if ( bPower > 1) {
			// Bullet_damage = 4 * bullet_power + 2 * (bullet_power - 1)
			bPower = (target_energy +2) / 6;
		}
		bPower = Math.max( bPower, minimalAllowedBulletEnergy);
		return bPower;
	}

}
