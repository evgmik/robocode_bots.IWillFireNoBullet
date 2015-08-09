// -*- java -*-

package eem.bot;

import eem.core.*;
import eem.misc.*;

import robocode.*;
import robocode.Rules.*;
import java.awt.geom.Point2D;

import robocode.*;
import robocode.util.*;
import robocode.Rules.*;

public class botStatPoint {
	private Point2D.Double pos;
	private long tStamp;
	private Point2D.Double velocity;
	private double headingDegrees;
	private double energy;
	private double myBotGunHeat;
	private double dist2WallAhead;
	private double dist2myBot;
	private double latteralVelocity;

	public botStatPoint() {
		pos = new Point2D.Double(0,0);
		tStamp = 0;
		velocity = new  Point2D.Double(0,0);
		headingDegrees = 0;
		energy =0;
		myBotGunHeat =0;
		dist2WallAhead=0;
		dist2myBot = 0;
		latteralVelocity = 0;
	}

	public botStatPoint(AdvancedRobot bot, ScannedRobotEvent e ) {
		this();
		double speed;
		Point2D.Double myCoord = new Point2D.Double();
		myCoord.x = bot.getX();
	       	myCoord.y = bot.getY();
		double angle = Math.toRadians(bot.getHeading()+ e.getBearing());
		double distance = e.getDistance();
		dist2myBot = distance;
		pos = new Point2D.Double( (myCoord.x + Math.sin(angle) * distance),
				(myCoord.y + Math.cos(angle) * distance) );
		tStamp = bot.getTime();
		headingDegrees = e.getHeading();
		speed = e.getVelocity();
		velocity = new Point2D.Double( speed*Math.sin( Math.toRadians(headingDegrees) ), speed*Math.cos( Math.toRadians(headingDegrees) ) );
		//if ( speed < 0 ) {
			//headingDegrees = math.shortest_arc( headingDegrees + 180 );
			//speed = -speed;
		//}
		energy = e.getEnergy();
		myBotGunHeat = bot.getGunHeat();
		dist2WallAhead = distanceToWallAhead();

		double enemyAbsoluteBearing = e.getBearingRadians() + bot.getHeadingRadians();
		double enemyLateralVelocity = e.getVelocity() * Math.sin(e.getHeadingRadians() - enemyAbsoluteBearing);
		latteralVelocity = enemyLateralVelocity; // positive means: enemy is circling clockwise

		//logger.dbg("bot stat = " + this.format() );
	}

	public botStatPoint(CoreBot bot) {
		this();
		double speed;
		pos.x = bot.getX();
	       	pos.y = bot.getY();
		tStamp = bot.getTime();
		headingDegrees = bot.getHeading();
		speed = bot.getVelocity();
		velocity = new Point2D.Double( speed*Math.sin( Math.toRadians(headingDegrees) ), speed*Math.cos( Math.toRadians(headingDegrees) ) );
		//if ( speed < 0 ) {
			//headingDegrees = math.shortest_arc( headingDegrees + 180 );
		//}
		energy = bot.getEnergy();
		dist2WallAhead = distanceToWallAhead();
		//logger.dbg("bot stat = " + this.format() );
	}

	public botStatPoint(Point2D.Double p, long t ) {
		this();
		tStamp = t;
		pos = p;
	}

	public Double getDistanceToWallAhead() {
		return dist2WallAhead;
	}

	public Double getDistance(Point2D.Double p) {
		return p.distance(pos);
	}

	public String format() {
		String str = "";
		str += "tStamp = " + tStamp;
		str += ", ";
		str += "position = " + pos;
		str += ", ";
		str += "energy = " + energy;
		str += ", ";
	       	str += "velocity = " + velocity;
		str += ", ";
	       	str += "heading = " + headingDegrees;
		str += ", ";
		str += "distance to " + whichWallAhead() +" wall ahead = " + getDistanceToWallAhead();
		str += ", ";
		str += "distance to myBot = " + dist2myBot + ", lateral velocity  = " + latteralVelocity;
		return str;
	}

	public long getTime() {
		return tStamp;
	}

	public double getEnergy() {
		return energy;
	}
	public double getX() {
		return pos.x;
	}

	public double getY() {
		return pos.y;
	}
	
	public double getHeadingDegrees() {
		return headingDegrees;
	}

	public double getHeadingRadians() {
		return Math.toRadians( headingDegrees );
	}

	public double getSpeed() {
		return velocity.distance(0,0);	
	}

	public Point2D.Double getVelocity() {
		return (Point2D.Double) velocity.clone();
	}

	public Point2D.Double getPosition() {
		return (Point2D.Double) pos.clone();
	}

	public double getDistanceToMyBot() {
		return dist2myBot;
	}

	public double getLatteralVelocity() {
		return latteralVelocity;
	}

	public Double getMyBotGunHeat() {
		return myBotGunHeat;
	}

	public boolean arePointsOfPathSimilar(botStatPoint refPatStart, botStatPoint refPatCurrent, botStatPoint testPatStart) {
		profiler.start( "arePointsOfPathSimilar" );
		// essentially does this point matches refPatCurrent point.
		// compare how this stat point with respect to testPatStart
		// matches reference Start and refPatCurrent
		double dist2WallProximity = 80;
		double dist2WallDiff = 4;
		double maxMyBotGunHeatDist = 0.2;
		double maxSpeedDist = 0.5;
		double maxDistDist = 50;
		double maxLateralDist = 2;
		double maxAngleDist = 10; // 10 is maximum bot rotation per turn
		double spdT = this.getSpeed();
		double angT = this.getHeadingDegrees() - testPatStart.getHeadingDegrees();
		double dist2myBotT = this.getDistanceToMyBot();
		long   timeDiffT = this.getTime() - testPatStart.getTime();
		double dist2wallAheadT = this.getDistanceToWallAhead();
		double latteralVelocityT = this.latteralVelocity;

		double spdR = refPatCurrent.getSpeed();
		double angR = refPatCurrent.getHeadingDegrees() - refPatStart.getHeadingDegrees();
		double dist2myBotR = refPatCurrent.getDistanceToMyBot();
		long   timeDiffR = refPatCurrent.getTime() - refPatStart.getTime();
		double dist2wallAheadR = refPatCurrent.getDistanceToWallAhead();
		double latteralVelocityR = refPatCurrent.getLatteralVelocity();
		boolean areSimilar = true;

		SameOrDifferent: {
			if (
				( Math.abs( spdT - spdR ) > maxSpeedDist )
				|| ( Math.abs( math.shortest_arc( angT - angR) ) > maxAngleDist )
				//|| ( Math.abs( dist2myBotT - dist2myBotR) > maxDistDist )
				|| ( Math.abs( latteralVelocityT - latteralVelocityR) > maxLateralDist )
			   ) {
				areSimilar = false;
				break SameOrDifferent;
			}
			// now let's check that the timing is right
			if ( timeDiffT != timeDiffR ) {
				areSimilar = false;
				break SameOrDifferent;
			}

			if ( false ) { // disable heat comparison
			if ( Math.min( this.getMyBotGunHeat(), refPatCurrent.getMyBotGunHeat() ) < .5) {
				if ( Math.abs( this.getMyBotGunHeat()  - refPatCurrent.getMyBotGunHeat() ) > maxMyBotGunHeatDist ) {
					areSimilar = false;
					break SameOrDifferent;
				}
			}
			}

			if ( Math.min( dist2wallAheadR, dist2wallAheadT) < dist2WallProximity ) {
				if ( Math.abs( dist2wallAheadT - dist2wallAheadR ) > dist2WallDiff ) {
					areSimilar = false;
					break SameOrDifferent;
				}
			}
			//logger.dbg("---- The following two bot stats match each other");
			//logger.dbg( this.format() );
			//logger.dbg( refPatCurrent.format() );
		} // end of SameOrDifferent
		profiler.stop( "arePointsOfPathSimilar" );
		return areSimilar;
	}

	public String whichWallAhead(botStatPoint bStatPnt) {
		return physics.whichWallAhead( bStatPnt.getPosition(), bStatPnt.getSpeed(), bStatPnt.getHeadingRadians() );
	}

	public String whichWallAhead() {
		return whichWallAhead( this );
	}

	public double distanceToWallAhead(botStatPoint bStatPnt) {
		return physics.distanceToWallAhead( bStatPnt.getPosition(), bStatPnt.getSpeed(), bStatPnt.getHeadingRadians() );
	}

	public double distanceToWallAhead() {
		return distanceToWallAhead( this );
	}
}
