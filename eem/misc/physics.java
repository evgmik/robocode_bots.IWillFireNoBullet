// -*- java -*-
package eem.misc;

import java.awt.geom.Point2D;
import java.util.*;

import robocode.*;
import robocode.util.*;
import robocode.Rules.*;
import robocode.BattleRules.*;

public class physics {
	public static int robotHalfSize = 0;
	public static double robotRadius = 0;
	public static Point2D.Double BattleField = new Point2D.Double(0,0);
	public static double gunCoolingRate = 0; 
	public static double minimalAllowedBulletEnergy = 0; 
	public static double maximalAllowedBulletEnergy = 0; 
	// using maxTurnsInRound to have ticTime growing Round independent
	// robocode itself reset Turn=getTime() to 0 every Round
	public static long maxTurnsInRound = 100000; // maximum # of Turns/Tics per round

	public static void init(AdvancedRobot myBot) {
		robotHalfSize = (int) myBot.getWidth()/2;
		robotRadius = robotHalfSize*Math.sqrt(2);
		BattleField = new Point2D.Double(myBot.getBattleFieldWidth(), myBot.getBattleFieldHeight());
		gunCoolingRate = myBot.getGunCoolingRate(); // this sits inside robocode.BattleRules
		minimalAllowedBulletEnergy = Rules.MIN_BULLET_POWER;
		maximalAllowedBulletEnergy = Rules.MAX_BULLET_POWER ;
	}

	public static long ticTimeFromTurnAndRound ( long Turn, long Round ) {
		return Turn + (Round+1)*maxTurnsInRound;
	}

	public static long getRoundStartTime(long ticTime) {
		return (long) Math.floor(ticTime/maxTurnsInRound)*maxTurnsInRound;
	}

	public static boolean isTimeInSameRound(long t1, long t2) {
		return ( getRoundStartTime(t1) == getRoundStartTime(t2) );
	}
	public static int gunCoolingTime( double heat ) {
		return (int) Math.ceil( heat/gunCoolingRate );
	}

	public static double bulletSpeed( double firePower ) {
		if ( firePower > maximalAllowedBulletEnergy ) {
			logger.warning("bulletSpeed(): Forbiden bullet energy requested: " + firePower + " > " + maximalAllowedBulletEnergy);
		}
		double bSpeed;
		//bSpeed = ( 20 - firePower * 3 ); // see wiki
		bSpeed = Rules.getBulletSpeed( firePower );
		logger.noise("bullet speed = " + bSpeed + " for firePower = " + firePower);
		return bSpeed;
	}

	public static double  bulletEnergy( double bulletSpeed ) {
		double bEnergy = ( 20 - bulletSpeed ) / 3;
		return bEnergy;
	}

	public static double  bulletDamageByEnergy( double bEnergy ) {
		double bDamage;
		//bDamage = 4*bEnergy + 2 * Math.max( bEnergy - 1 , 0 ); // see wiki
		bDamage = Rules.getBulletDamage(bEnergy);
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

	public static double dist2LeftOrRightWall( Point2D.Double p ) {
		double dLeft  = p.x; // left wall distance
		double dRight = BattleField.x - p.x; // right wall distance
		if ( ( dLeft <= 0 ) || ( dRight <= 0 ) ) {
			// point is outside of wall
			return 0;
		}
		return Math.min( dLeft, dRight);
	}

	public static double dist2BottomOrTopWall( Point2D.Double p ) {
		double dBottom = p.y; // bottom wall distance
		double dTop    = BattleField.y - p.y; // top wall distance
		if ( ( dTop <= 0 ) || ( dBottom <= 0 ) ) {
			// point is outside of wall
			return 0;
		}
		return Math.min( dBottom, dTop);
	}

	public static double shortestDist2wall( Point2D.Double p ) {
		return  Math.min( dist2LeftOrRightWall( p ), dist2BottomOrTopWall( p ) );
	}
}
