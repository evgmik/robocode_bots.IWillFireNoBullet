// -*- java -*-
package eem.frame.misc;

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

	public static double calculateMEA( double bulletSpeed ) {
			// Max escape angle in degree for a given bullet speed
			double maxBotSpeed = Rules.MAX_VELOCITY;
			double MEA = 180/Math.PI*Math.asin( maxBotSpeed/bulletSpeed );
			return MEA;
	}

	public static boolean isBotOutOfBorders( Point2D.Double pnt ) {
		if ( ( pnt.x < robotHalfSize ) || ( pnt.x > (BattleField.x - robotHalfSize) ) )
			return true;
		if ( ( pnt.y < robotHalfSize ) || ( pnt.y > (BattleField.y - robotHalfSize) ) )
			return true;
		return false;
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

	public static String whichWallAhead(Point2D.Double pos, double speed, double headingInRadians) {
		// due to round offs bot position might appear inside of walls
		// below give us some margin to account for it
		double wall_margin = 1;
		double x = pos.x;
		double y = pos.y;
		double huge = 1e100; // humongous number

		String wallName="";

		if ( Utils.isNear(speed, 0.0) ) {
			// we are not moving anywhere 
			//find the closest wall
			double dist = x;
			double dist_temp=0;
			wallName = "left";

			dist_temp = y;
			if ( dist_temp < dist ) { 
				dist = y;
				wallName = "bottom";
			}
			
			dist_temp = BattleField.x - x;
			if ( dist_temp < dist ) { 
				dist = dist_temp;
				wallName = "right";
			}
			dist_temp = BattleField.y - y;
			if ( dist_temp < dist ) { 
				dist = dist_temp;
				wallName = "top";
			}
			return wallName;
		}

		double vx = Math.sin( headingInRadians )*speed;
		double vy = Math.cos( headingInRadians )*speed;

		double time_to_sidewall, time_to_top_bottom_wall;

		if ( Utils.isNear(vx, 0.0) ) {
			// we are not moving in x direction
			time_to_sidewall = huge;
		} else {
			if ( vx < 0 ) {
				time_to_sidewall = -( x -  robotHalfSize + wall_margin )/vx;
			} else {
				time_to_sidewall =  ( BattleField.x - x - robotHalfSize + wall_margin )/vx;
			}
		}
		if ( Utils.isNear(vy, 0.0) ) {
			// we are not moving in y direction
			time_to_top_bottom_wall = huge;
		} else {
			if ( vy < 0 ) {
				time_to_top_bottom_wall = -( y -  robotHalfSize + wall_margin )/vy;
			} else {
				time_to_top_bottom_wall =  ( BattleField.y - y - robotHalfSize + wall_margin )/vy;
			}
		}

		if ( time_to_sidewall < time_to_top_bottom_wall) {
			// side walls are closer
			if ( vx<0 ) {
				wallName = "left";
			} else {
				wallName = "right";
			}
		} else {
			// top or bootom is closer
			if ( vy<0 ) {
				wallName = "bottom";
			} else {
				wallName = "top";
			}
		}
		return wallName;
	}

	public static double distanceToWallAhead( Point2D.Double pos, double speed, double headingInRadians ) {
		double dist=0;

		String wallName = whichWallAhead( pos, speed, headingInRadians);

		if ( wallName.equals("left") ) {
				dist = pos.x;
		}	
		if ( wallName.equals("right") ) {
				dist = BattleField.x - pos.x;
		}
		if ( wallName.equals("bottom") ) {
				dist = pos.y;
		}
		if ( wallName.equals("top") ) {
				dist = BattleField.y - pos.y;
		}
		dist = dist - robotHalfSize;
		dist = Math.max(dist,0);
		if (dist < 0) dist = 0 ;
		logger.noise("distance to closest wall ahead " + dist);
		return dist;
	}

	public static double stopDistance( double velocity ) {
		double speed = Math.abs(velocity);
		int dist =0;

		speed -= 2;
		while ( speed > 0 ) {
			dist += speed;
			speed -= 2;
		}
		return dist;
	}

}
