// -*- java -*-

package eem.gun;
import eem.misc.*;

import java.awt.geom.Point2D;
import java.awt.Graphics2D;
import java.awt.Color;

public class firingSolution {
	public double firingAngle;
	public Point2D.Double firingPosition;
	public Point2D.Double targetPosition;

	public firingSolution() {
		firingAngle = Double.NaN;
		firingPosition = null;
		targetPosition = null;
	}

	public firingSolution( Point2D.Double fP, double angle ) {
		firingAngle = angle;
		firingPosition = (Point2D.Double) fP.clone();
		targetPosition = null;
	}

	public firingSolution( Point2D.Double fP, Point2D.Double tP ) {
		firingAngle = math.angle2pt( fP, tP );
		firingPosition = (Point2D.Double) fP.clone();
		targetPosition = (Point2D.Double) tP.clone();
	}

	public void onPaint(Graphics2D g, double L) {
		if ( firingPosition == null ) {
			logger.error( "This should not happen: the firing solution does not have firingPosition" );
		} else {
			g.setColor( new Color(0xFF, 0x00, 0x00, 0xff) );
			graphics.drawRect( g, firingPosition, 20, 20 );
		}

		if ( firingAngle == Double.NaN ) {
			logger.error( "This should not happen: the firing solution does not have firingAngle" );
		} else {
			g.setColor( new Color(0xFF, 0x00, 0x00, 0xff) );
			double a = math.game_angles2cortesian( firingAngle );
			double dx = Math.cos( Math.toRadians(a) );
			double dy = Math.sin( Math.toDegrees(a) );
			Point2D.Double endP = new Point2D.Double( firingPosition.x + dx, firingPosition.y + dy );
			graphics.drawLine( g, firingPosition,  endP );
		}
	}

	public void onPaint(Graphics2D g ) {
		if ( firingPosition == null ) {
			logger.error( "This should not happen: the firing solution does not have firingPosition" );
		} else {
			g.setColor( new Color(0xFF, 0x00, 0x00, 0xff) );
			graphics.drawRect( g, firingPosition, 20, 20 );
		}

		if ( targetPosition != null ) {
			g.setColor( new Color(0xFF, 0x00, 0x00, 0xff) );
			graphics.drawRect( g, targetPosition, 20, 20 );
		}

		if ( firingAngle == Double.NaN ) {
			logger.error( "This should not happen: the firing solution does not have firingAngle" );
		} else {
			
			g.setColor( new Color(0xFF, 0x00, 0x00, 0xff) );
			if ( targetPosition != null ) {
				graphics.drawLine( g, firingPosition,  targetPosition );
			} else {
				double L = 30;
				double a = math.game_angles2cortesian( firingAngle );
				double dx = Math.cos( Math.toRadians(a) );
				double dy = Math.sin( Math.toDegrees(a) );
				Point2D.Double endP = new Point2D.Double( firingPosition.x + dx, firingPosition.y + dy );
				graphics.drawLine( g, firingPosition,  endP );
			}
		}
	}
}

