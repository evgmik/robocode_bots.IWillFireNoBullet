// -*- java -*-

package eem.frame.dangermap;

import eem.frame.dangermap.*;
import eem.frame.misc.*;
import eem.frame.wave.*;
import eem.frame.bot.*;

import java.util.LinkedList;
import java.util.Collections;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class dangerCalc {
	// calculates dangers in a point from myBot (any fighterBot) point of view

	public static double calculateDangerFromEnemyWaves(long time, Point2D.Double dP, fighterBot myBot) {
		double dL = 0;
		for ( waveWithBullets eW : myBot.getEnemyWaves() ) {
			dL += eW.getDanger( time, dP );
		}
		return dL;
	}

	public static double calculateDangerFromEnemyBots(long time,  Point2D.Double dP, fighterBot myBot) {
		double dL = 0;
		for ( fighterBot eB : myBot.getEnemyBots() ) {
			dL += eB.getDanger( time, dP );
		}
		return dL;
	}

	public static double calculateDangerFromWall(long time, Point2D.Double dP, fighterBot myBot) {
		double dLWall = 1;
		double wallDangerRadius = 5;
		double dL = 0;
		double dist = physics.shortestDist2wall( dP );
		if ( dist <= physics.robotHalfSize ) {
			dL += dLWall;
		}
		//dL += dLWall*Math.exp( -(dist-physics.robotHalfSize)/wallDangerRadius );
		return dL;
	}
}

