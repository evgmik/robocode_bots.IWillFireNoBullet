// -*- java -*-
package eem.misc;

import java.awt.geom.Point2D;
import java.awt.Graphics2D;
import java.awt.Color;

public class graphics {
	public static void drawLine( Graphics2D g, Point2D.Double strtP, Point2D.Double endP ) {
		g.drawLine((int)strtP.x, (int)strtP.y, (int)endP.x, (int)endP.y);

	}

	public static void drawCircle( Graphics2D g, Point2D.Double p, double R ) {
		g.drawOval( (int)(p.x - R), (int)(p.y - R), (int)(2*R), (int)(2*R) );
	}

	public static void fillRect( Graphics2D g, Point2D.Double p, double width, double height ) {
		g.fillRect( (int)(p.x - width/2), (int)(p.y - height/2), (int)(width), (int)(height) );
	}

	public static void fillSquare( Graphics2D g, Point2D.Double p, double width ) {
		fillRect( g, p, width, width ) ;
	}

	public static void drawRect( Graphics2D g, Point2D.Double p, double width, double height ) {
		g.drawRect( (int)(p.x - width/2), (int)(p.y - height/2), (int)(width), (int)(height) );
	}

	public static void drawSquare( Graphics2D g, Point2D.Double p, double width ) {
		drawRect( g, p, width, width ) ;
	}

		public static void drawCircArc( Graphics2D g, Point2D.Double center, double radius, double startAngle, double stopAngle ) {
			g.drawArc( (int) (center.x - radius), (int) (center.y - radius), (int) (2*radius), (int) (2*radius), (int) startAngle, (int) (stopAngle - startAngle) );
		}

	public static Color dangerLevel2mapColor(double dLevel, int opacityTreshold) {
		int opacity = (int) Math.abs(dLevel/3.0); // 0 - 255 but good values below 100
		Color c;

		if (opacity > opacityTreshold) opacity = opacityTreshold;
		if (opacity < 0 ) opacity = 0;

		if ( dLevel >= 0 ) {
			// red
			c = new Color(0xff, 0x00, 0x00, opacity);
		} else {
			// green
			c = new Color(0x00, 0xff, 0x00, opacity);
		}
		return c;
	}

	public static Color dangerLevel2mapColor(double dLevel) {
		int opacityTreshold = 100;
		return dangerLevel2mapColor( dLevel, opacityTreshold);
	}

}
