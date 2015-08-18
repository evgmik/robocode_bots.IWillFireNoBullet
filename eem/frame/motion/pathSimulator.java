// -*- java -*-

package eem.frame.motion;

import eem.frame.core.*;
import eem.frame.motion.*;
import eem.frame.dangermap.*;
import eem.frame.bot.*;
import eem.frame.misc.*;

import robocode.util.*;

import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.awt.Color;

public class pathSimulator {
	public Point2D.Double endPoint;

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

}

