// -*- java -*-

package eem.frame.motion;

import eem.frame.core.*;
import eem.frame.motion.*;
import eem.frame.dangermap.*;
import eem.frame.bot.*;
import eem.frame.misc.*;

import robocode.util.*;
import robocode.Rules.*;

import java.util.*;
import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.awt.Color;

public class pathSimulator {

	public static driveCommand moveToPointDriveCommand( botStatPoint bPnt,  Point2D.Double destPnt ) {
		return moveToPointDriveCommand( bPnt.getPosition(), bPnt.getHeadingDegrees(), destPnt);
	}

	public static driveCommand moveToPointDriveCommand( Point2D.Double fromPnt, double headingDegrees,  Point2D.Double destPnt ) {
		double angle = math.shortest_arc( math.angle2pt( fromPnt, destPnt) - headingDegrees );
		double dist = fromPnt.distance(destPnt);
		if ( Math.abs(angle ) > 90 ) {
			if (angle > 90 ) {
				angle = angle - 180;
			} else {
				angle = angle + 180;
			}
			dist = -dist;
		}
		driveCommand _driveCommand = new driveCommand( angle, dist );
		return _driveCommand;
	}

	public static  botStatPoint nextBotStatPointWithDriveCommand( botStatPoint currentBotStatPnt, driveCommand drvCmnd ) {
		botStatPoint _nextBotStatPnt = new botStatPoint();
		//very regimental stat point replication

		Point2D.Double pos = currentBotStatPnt.getPosition();
		long tStamp = currentBotStatPnt.getTime();
		double speed = currentBotStatPnt.getSpeed();
		double dist = drvCmnd.getMoveAheadDist();
		double angle  = drvCmnd.getTurnRightAngle();

		// Looks like we  are first rotate, than accelerate
		double maxTurnRate = robocode.Rules.getTurnRate(speed); // speed dependent
		angle = math.putWithinRange( angle, -maxTurnRate, maxTurnRate);
		angle += currentBotStatPnt.getHeadingDegrees();

		if ( speed*dist >= 0 ) {
			//we are accelerating (speed = 0 ==> definitely accelerating)
			speed = speed + math.sign(dist) * robocode.Rules.ACCELERATION;
		} else {
			// slowing down
			speed = speed + math.sign(dist) * robocode.Rules.DECELERATION ;
		}
		speed = math.putWithinRange( speed, -robocode.Rules.MAX_VELOCITY, robocode.Rules.MAX_VELOCITY );
		//logger.dbg("time = " + tStamp);
			

		_nextBotStatPnt.setHeadingDegrees( angle );
		_nextBotStatPnt.setSpeed( speed );
		double angleRads = Math.toRadians( angle );
		pos.x = pos.x + Math.sin(angleRads) * speed;
		pos.y = pos.y + Math.cos(angleRads) * speed;
		_nextBotStatPnt.setPosition( pos );
		_nextBotStatPnt.setTime( tStamp + 1 );
		//logger.dbg("path point = " + _nextBotStatPnt.format());

		return _nextBotStatPnt;

	}

	public static  botStatPoint nextBotStatPointOnAWayToo( botStatPoint currentBotStatPnt, Point2D.Double destPnt ) {
		driveCommand _driveCmnd = moveToPointDriveCommand( currentBotStatPnt, destPnt);
		return nextBotStatPointWithDriveCommand( currentBotStatPnt, _driveCmnd);
	}

	public static LinkedList<botStatPoint> getPathTo( Point2D.Double destPnt, botStatPoint strtPnt, long maxSteps ) {
		LinkedList<botStatPoint> pathPoints = new LinkedList<botStatPoint>();
		driveCommand _dCmnd;
		botStatPoint prevPnt  = strtPnt;
		botStatPoint nextPnt;
		long cnt = 0;
		do {
			cnt++;
			nextPnt = nextBotStatPointOnAWayToo( prevPnt, destPnt );
			pathPoints.add( nextPnt);
			prevPnt = nextPnt;
		} while ( cnt <= maxSteps );
		return pathPoints;
	}


}

