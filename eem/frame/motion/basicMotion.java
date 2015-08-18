// -*- java -*-

package eem.frame.motion;

import eem.frame.core.*;
import eem.frame.motion.*;
import eem.frame.bot.*;
import eem.frame.misc.*;

import robocode.util.*;

import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.awt.Color;



public class basicMotion {
	protected fighterBot myBot;

	public void initTic() {
	}

	public basicMotion() {
	}

	public basicMotion(fighterBot bot) {
		initBattle( bot );
	}

	public void initBattle(fighterBot b) {
		myBot = b;
	}

	public void moveToPoint( Point2D.Double pnt ) {
		driveCommand _driveCommand = pathSimulator.moveToPointDriveCommand( myBot.getPosition(), myBot.getHeadingDegrees(), pnt );
		setTurnRight(_driveCommand.turnRightAngleDegrees);
		setAhead (_driveCommand.moveAheadDist);
	}

	public void setTurnRight( double angle) {
		myBot.proxy.setTurnRight( angle );
	}

	public void setAhead( double dist ) {
		myBot.proxy.setAhead( dist);
	}

	public void manage() {
		// for basic motion we do nothing
	}

	public void makeMove() {
		// for basic motion we do nothing
	}

	public void onPaint(Graphics2D g) {
	}

}
