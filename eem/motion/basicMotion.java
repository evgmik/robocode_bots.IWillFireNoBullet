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

	public void initTic() {
	}

	public basicMotion() {
	}

	public basicMotion(CoreBot bot) {
		myBot = bot;
	}

	public void moveToPoint( Point2D.Double pnt ) {
		double angle = math.shortest_arc( math.angle2pt( myBot.myCoord, pnt) - myBot.getHeading() );
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

}
