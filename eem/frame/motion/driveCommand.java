// -*- java -*-

package eem.frame.motion;

public class driveCommand {
	// remember we are doing back as front behind the scene
	public double turnRightAngleDegrees = 0;
	public double moveAheadDist = 0;

	public driveCommand() {};

	public driveCommand( double angle, double dist ) {
		turnRightAngleDegrees = angle;
		moveAheadDist = dist;
	}

	public double getTurnRightAngle() {
		return turnRightAngleDegrees;
	}

	public double getMoveAheadDist() {
		return moveAheadDist;
	}
}
