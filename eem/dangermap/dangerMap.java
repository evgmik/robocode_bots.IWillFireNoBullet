// -*- java -*-

package eem.dangermap;

import eem.dangermap.*;
import eem.misc.*;
import eem.wave.*;
import eem.bot.*;

import java.util.LinkedList;
import java.util.Collections;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class dangerMap {
	public LinkedList<dangerPoint> dangerPoints;
	public long ticTime=0;
	public fighterBot myBot;

	public dangerMap(fighterBot myBot) {
		dangerPoints = new LinkedList<dangerPoint>();
		this.myBot = myBot;
	}

	public void add( dangerPoint p ) {
		dangerPoints.add( p );
	}

	public void add( Point2D.Double p ) {
		double dangerLevel = 0;
		dangerPoints.add( new dangerPoint(p, dangerLevel) );
	}

	public void reCalculateDangerMap(long time) {
		ticTime = time;
		double dL;
		for( dangerPoint dP: dangerPoints ) {
			dL = calculateDangerForPoint( dP, time );
			dP.setDanger( dL );
		}
	}

	public double calculateDangerForPoint( dangerPoint dP, long time) {
		double dL = 0;
		dL += calculateDangerFromWall(time, dP);
		dL += calculateDangerFromEnemyBots(time, dP);
		dL += calculateDangerFromEnemyWaves(time, dP);
		return dL;
	}

	public double calculateDangerFromEnemyWaves(long time, dangerPoint dP) {
		double dL = 0;
		return dL;
	}

	public double calculateDangerFromEnemyBots(long time, dangerPoint dP) {
		double dLbot = 1.0; // enemy bot normalization
		double dRadius = 100; // effective dangerous radius of enemy Bot
		double dL = 0;
		double dist = 0;
		for ( fighterBot eB : myBot.getEnemyBots() ) {
			dist = dP.getPosition().distance( eB.getPositionClosestToTime( time ) ) ;
			dL += dLbot * Math.exp( - dist/dRadius );
		}
		return dL;
	}

	public double calculateDangerFromWall(long time, dangerPoint dP) {
		double dLWall = 1.0;
		double dL = 0;
		double dist = physics.shortestDist2wall( dP.getPosition() );
		if ( dist <= physics.robotHalfSize ) {
			dL = dLWall;
		}
		return dL;
	}

	public void sortDangerPoints() {
		Collections.sort(dangerPoints);
	}

	public dangerPoint getSafestPoint() {
		sortDangerPoints();
		return dangerPoints.getFirst();
	}

	public void clearDangerPoints() {
		dangerPoints.clear();
	}


	public void onPaint(Graphics2D g) {
		for( dangerPoint dP: dangerPoints ) {
			dP.onPaint(g);
		}
	}
}
