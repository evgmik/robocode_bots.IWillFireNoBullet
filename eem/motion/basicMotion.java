// -*- java -*-

package eem.motion;

import eem.core.*;
import eem.misc.*;

import robocode.util.*;

import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.awt.Color;



public class basicMotion {
	protected CoreBot myBot;
	protected double battleFieldHeight = 0;
	protected double battleFieldWidth = 0;
	protected int robotHalfSize;
	protected double dangerForPointTouchingTheWall = 1e6; // humongous number
	protected double dangerLevelWall = 50;
	protected double dangerLevelCorner = 1e4;
	protected double safe_distance_from_wall;


	public void initTic() {
	}

	public basicMotion() {
	}

	public basicMotion(CoreBot bot) {
		myBot = bot;
		battleFieldWidth  = physics.BattleField.x;
		battleFieldHeight = physics.BattleField.y;

		robotHalfSize = physics.robotHalfSize;
		safe_distance_from_wall = robotHalfSize + 2;
	}

	public void moveToPoint( Point2D.Double pnt ) {
		double angle = math.shortest_arc( bearingTo(pnt) - myBot.getHeading() );
		double dist = myBot.myCoord.distance(pnt);
		if ( Math.abs(angle ) > 90 ) {
			if (angle > 90 ) {
				angle = angle - 180;
			} else {
				angle = angle + 180;
			}
			dist = -dist;
		}
		myBot.setTurnRight(angle);
		myBot.setAhead (dist);
	}

	public void makeMove() {
		// for basic motion we do nothing
	}

	public void onPaint(Graphics2D g) {
	}

	// --- Utils -----
	public double bearingTo( Point2D.Double  pt) {
		return math.shortest_arc(
			math.cortesian2game_angles( Math.atan2( pt.y-myBot.myCoord.y, pt.x-myBot.myCoord.x )*180/Math.PI )
			);
	}

}
