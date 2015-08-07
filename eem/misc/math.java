// -*- java -*-
package eem.misc;

import java.awt.geom.Point2D;
import java.util.LinkedList;

import robocode.*;
import robocode.util.*;
import robocode.Rules.*;

public class math {

	public static void init(AdvancedRobot myBot) {
	}

	public static double sqr( double x ) {
		return x*x;
	}

	public static double gaussian( double dist, double ampl, double width ) {
		return ampl*Math.exp(-dist*dist/(width*width) );
	}

	public static double quadraticSolverMinPosRoot(double a, double b, double c) {
		// we are solving for time in ballistic calculation
		// and interested only in positive solutions
		// hopefully determinant is always >= 0 since we solve real problems
		double d = Math.sqrt(b*b - 4*a*c);
		double x1= (-b + d)/(2*a);
		double x2= (-b - d)/(2*a);

		double root=Math.min(x1,x2);
		if (root < 0) {
			// if min gave as wrong root max should do better
			root=Math.max(x1,x2);
		}

		return root;
	}

	public static double angle2pt( Point2D.Double from_pnt, Point2D.Double to_pnt) {
		double angle;
		double dx, dy;
		// angle from one point to another
		dx=to_pnt.x - from_pnt.x;
		dy=to_pnt.y - from_pnt.y;

		angle = Math.atan2(dy,dx);
		angle = cortesian2game_angles( Math.toDegrees(angle) );
		return angle;
	}

	public static double cortesian2game_angles(double angle) {
		angle=90-angle;
		return angle;
	}

	public static double shortest_arc( double angle ) {
		//dbg(dbg_noise, "angle received = " + angle);
		angle = angle % 360;
		if ( angle > 180 ) {
			angle = -(360 - angle);
		}
		if ( angle < -180 ) {
			angle = 360+angle;
		}
		//dbg(dbg_noise, "angle return = " + angle);
		return angle;
	}

	public static boolean isItOutOfBorders( Point2D.Double pnt, Point2D.Double brdr) {
		if ( pnt.x < 0 ) return true;
		if ( pnt.y < 0 ) return true;
		if ( pnt.x > brdr.x ) return true;
		if ( pnt.y > brdr.y ) return true;
		return false;
	}

	
	public static double eventRate(double nEvents, double nTotal) {
		return nEvents / Math.max( nTotal, 1 );
	}

	public static double perfRate(double nEvents, double nTotal) {
		// forgiveness is to boost trials which not yet sampled
		// otherwise any event which happened saturate not yet triggered 
		double forgiveness = 1;
		return (nEvents + forgiveness ) / ( nTotal + forgiveness );
	}

	public static int binNumByWeight( LinkedList<Double> weights) {
		// returns bin number probabilisticly according to its weight
		int n = 0;
		// first lets find sum of all weights
		double sum=0;
		for ( Double w : weights) {
			sum += w;
		}
		// now we are choosing probabilisticly
		double accumWeight=0;
		double rnd=Math.random();
		for ( Double w : weights) {
			accumWeight += w/sum;
			if ( rnd <= accumWeight ) {
				break;
			}
			n++;
		}
		if ( n >= weights.size() ) {
			logger.warning("Improbable happens: rnd == 1, last bin");
			n = weights.size() - 1;
		}
		//logger.dbg("weights = " + weights);
		//logger.dbg("n = " + n);
		return n;
	}

	public static int binNumByMaxWeight( LinkedList<Double> weights) {
		// returns bin number with highest weight
		int n = 0;
		double maxW = 0;
		int cnt = 0;
		for ( Double w : weights) {
			if ( w > maxW ) {
				maxW = w;
				n = cnt;
			}
			cnt++;
		}
		if ( n >= weights.size() ) {
			logger.warning("Improbable happens: rnd == 1, last bin");
			n = weights.size() - 1;
		}
		//logger.dbg("weights = " + weights);
		//logger.dbg("n = " + n);
		return n;
	}


	public static Point2D.Double putWithinBorders( Point2D.Double pnt, Point2D.Double brdr) {
		Point2D.Double npnt= new Point2D.Double( pnt.x, pnt.y );
		npnt.x = putWithinRange( npnt.x, 0, brdr.x);
		npnt.y = putWithinRange( npnt.y, 0, brdr.y);
		return npnt;
	}

	public static double putWithinRange( double x, double lBound, double uBound) {
		double val;
		val = x;
		if ( val < lBound ) {
			val = lBound;
		}
		if ( val > uBound ) {
			val =uBound;
		}
		return val;
	}

	public static int signNoZero( double n) {
		int val;
		val = sign(n);
		if (0 == val) {
			val = 1;
		}
		return val;
	}

	public static int sign( double n) {
		if (n==0) 
			return 0;
		if (n > 0 )
			return 1;
		else
			return -1;
	}

	public static Point2D.Double vecCrossesBorder(Point2D.Double start_pnt, double heading, Point2D.Double brdr) {
		Point2D.Double hit_pnt = (Point2D.Double) start_pnt.clone();
		heading = Math.PI/180*shortest_arc(heading*180/Math.PI);
		double vx = Math.sin(heading);
		double vy = Math.cos(heading);
		double dist=0;
		if (vx == 0) {
			// putting small fictional number to avoid division by 0
			vx = 1e-5;
		}
		if (vx > 0) {
			// moving right
			dist = brdr.x - start_pnt.x;
		} else {
			// moving left
			dist = 0 - start_pnt.x;
		}
		hit_pnt.x = start_pnt.x + dist;
		hit_pnt.y = start_pnt.y + dist*vy/vx;

		dist = 0;
		if ( hit_pnt.y > brdr.y ) {
			// above border
			dist =  brdr.y - hit_pnt.y;
		}
		if ( hit_pnt.y < 0 ) {
			// below border
			dist = 0 - hit_pnt.y;
		}
		if (vy == 0) {
			// putting small fictional number to avoid division by 0
			vy = 1e-5;
		}
		hit_pnt.y = hit_pnt.y + dist;
		hit_pnt.x = hit_pnt.x + dist/vy*vx;
		return hit_pnt;
	}

	public static double bin2gf( int gfBin, int numBins ) {
		return 2.0*gfBin/(numBins-1.0) - 1.0;
	}


}
