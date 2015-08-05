// -*- java -*-

package eem.bot;

import eem.IWillFireNoBullet;
import eem.bot.*;
import eem.misc.*;

import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.*;
import java.lang.Integer;

import robocode.*;
import robocode.util.*;
import robocode.Rules.*;

public class InfoBot {
	protected String name = "";
	public LinkedList<botStatPoint> botStats;

	public InfoBot() {
		botStats = new LinkedList<botStatPoint>();
	}

	public InfoBot(String botName) {
		this();
		setName(botName);
	}

	public void initTic(long ticTime) {
	}

	public boolean hasLast() {
		int n = botStats.size();
		if ( n >= 1 ) {
			return true;
		} else {
			return false;
		}
	}

	public boolean hasPrev() {
		int n = botStats.size();
		if ( n >= 2 ) {
			return true;
		} else {
			return false;
		}
	}

	public botStatPoint getLast() {
		if ( hasLast() ) {
			return botStats.getLast();
		} else {
			return null;
		}
	}

	public botStatPoint getPrev() {
		int n = botStats.size();
		if ( hasPrev() ) {
			return botStats.get(n-2); // last is n-1 thus prev  is n-2
		} else {
			return null;
		}
	}

	public InfoBot update(Point2D.Double pos, long tStamp) {
		botStats.add( new botStatPoint(pos, tStamp) );
		return this;
	}

	public InfoBot update(botStatPoint statPnt) {
		String profName = "InfoBot.update for " + getName();
		profiler.start( profName );
		botStats.add(statPnt);
		profiler.stop( profName );
		return this;
	}

	public double getEnergy() {
		if ( hasLast() ) {
			return getLast().getEnergy();
		} else {
			return 0;
		}
	}

	public Point2D.Double getVelocity() {
		if ( hasLast() ) {
			return getLast().getVelocity();
		} else {
			return new Point2D.Double(0,0);
		}
	}

	public void setName(String n) {
		name = n;
	}

	public double getLastDistance(Point2D.Double p) {
		if ( hasLast() ) {
			return  getLast().getDistance(p);
		} else {
			return 1000000; // very large
		}
	}

	public double getX() {
		if ( hasLast() ) {
			return  getLast().getX();
		} else {
			return 0;
		}
	}

	public double getY() {
		if ( hasLast() ) {
			return  getLast().getY();
		} else {
			return 0;
		}
	}

	public Point2D.Double getPosition() {
		if ( hasLast() ) {
			return  getLast().getPosition();
		} else {
			return new Point2D.Double(0,0);
		}
	}

	public Point2D.Double getPosition(long time) {
		return getPositionAtTime( time );
	}

	public Point2D.Double getPrevTicPosition() {
		// Return position at previous known point
		// Watch out previous point may be many clicks away !
		Point2D.Double pos = getPositionAtTime( getLast().getTime() - 1 );
		if ( pos == null ) {
			// we do linear approximation to the past
			pos = (Point2D.Double) getPosition().clone(); // current position
			pos.x -= getVelocity().x;
			pos.y -= getVelocity().y;
		}
		return pos;
	}

	public Point2D.Double getPrevPosition() {
		// Return position at previous known point
		// Watch out previous point may be many clicks away !
		if ( hasPrev() ) {
			return  getPrev().getPosition();
		} else {
			return  null;
		}
	}

	public Point2D.Double getPositionAtTime(long time) {
		botStatPoint bS = getStatAtTime(time);
		if (bS == null) return null;
		return bS.getPosition();
	}

	public botStatPoint getStatAtTime(long time) {
		// return position at given time
		// the main use to find exactly previous time for 
		// enemy bullets targeting.
		// Thus we count from the end
		int N = botStats.size();
		botStatPoint bS = null;
		for ( int i=N-1; i >=0; i-- ) {
			bS = botStats.get(i);
			if ( bS.getTime() == time ) {
				return bS;
			}
			if ( bS.getTime() < time ) {
				// we started with lagest/oldest timeTic
				// and now passed the required time
				// everything else is before required time
				return null;
			}
		}
		return null; // no matched time found, but we should not be here at all
	}

	public long getLastSeenTime() {
		if ( hasLast() ) {
			return  getLast().getTime();
		} else {
			return  -1000; // far far ago
		}
	}


	public double energyDrop() {
		if ( hasPrev() ) {
			return  getPrev().getEnergy() - getLast().getEnergy();
		} else {
			return 0;
		}
	}


	public String getName() {
		return name;
	}


	public String format() {
		String str;
		String strL;
		String strP;
		if ( hasPrev() )  {
			strP ="Prev: " + getPrev().format();
		} else {
			strP = "Prev: unknown";
		}
		if ( hasLast() )  {
			strL = "Last: " + getLast().format();
		} else {
			strL = "Last: unknown";
		}
		int statSize = botStats.size();
		str = "Target bot name: " + getName() + "\n" + strL + "\n" + strP;
		str += "\n stats size " + statSize;
		return str;
	}


	public void drawLastKnownBotPosition(Graphics2D g) {
		if ( hasLast() ) {
			double size = 50;
			botStatPoint  bsLast = getLast();
			graphics.drawSquare( g, bsLast.getPosition(), size );
		}
	}

	public void drawBotPath(Graphics2D g) {
		botStatPoint  bsLast;
		botStatPoint  bsPrev;
		ListIterator<botStatPoint> bLIter = botStats.listIterator(botStats.size());
		if (bLIter.hasPrevious()) {
			bsLast = bLIter.previous();
		} else {
			return;
		}
		long roundStartTime = physics.getRoundStartTime( bsLast.getTime() );
		while (bLIter.hasPrevious()) {
			bsPrev = bLIter.previous();
			if ( bsPrev.getTime() < roundStartTime ) 
				return; // we see previous round point
			graphics.drawLine( g, bsLast.getPosition(), bsPrev.getPosition() );
			bsLast = bsPrev;
		}

	}

	public void onPaint(Graphics2D g) {
		g.setColor(new Color(0xff, 0xff, 0x00, 0x80));
		drawBotPath(g);
		drawLastKnownBotPosition(g);
	}

}

