// -*- java -*-

package eem.bot;

import eem.core.*;
import eem.event.*;
import eem.bot.*;
import eem.radar.*;
import eem.wave.*;
import eem.gameInfo.*;
import eem.motion.*;
import eem.gun.*;
import eem.misc.*;

import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.*;
import java.lang.Integer;

import robocode.*;
import robocode.util.*;
import robocode.Rules.*;

/*
 * This is main simulator class which should handle situation as it perceived by a bot.
 * Use it for both enemy and our own, but set motion, radar, ... methods appropriately.
 * */

public class fighterBot implements waveListener, botListener {
	protected InfoBot fBot;
	protected gameInfo _gameinfo;
	protected baseRadar _radar;
	protected basicMotion _motion;

	public botProxy proxy;

	public LinkedList<waveWithBullets> enemyWaves = new LinkedList<waveWithBullets>();
	public LinkedList<waveWithBullets> myWaves    = new LinkedList<waveWithBullets>();

	public HashMap<String,fighterBot> enemyBots = new HashMap<String, fighterBot>();

	public fighterBot( InfoBot fBot, gameInfo gInfo) {
		this.fBot = fBot;
		_gameinfo = gInfo;
		_gameinfo._wavesManager.addWaveListener( this );
		_gameinfo._botsmanager.addBotListener( this );
		if ( isItMasterBotDriver() ) {
			// this bot is in charge of the master bot
			proxy = new realBotProxy( _gameinfo.getMasterBot() );
			_radar = new universalRadar( this );
			_motion = new dangerMapMotion( this );
			//_dangerMap.add( new Point2D.Double(125,125) );
			//_dangerMap.add( new Point2D.Double(5,5) );

		} else {
			// this bot is in charge of the enemy bot
			proxy = new nullProxy( _gameinfo.getMasterBot() );
			_radar = new nullRadar( this );
			_motion = new basicMotion( this );
		}
	}

	public boolean isItMasterBotDriver() {
		return  getName().equals( _gameinfo.getMasterBot().getName() );  
	}

	public String getName() {
		return fBot.getName();
	}

	public long getTime() {
		return _gameinfo.getTime();
	}

	public double getDanger( long time, Point2D.Double dP ){
		double dLbot = 1.0; // enemy bot normalization
		double dRadius = 100; // effective dangerous radius of enemy Bot
		double dL = 0;
		double dist = 0;
		dist = dP.distance( getPositionClosestToTime( time ) ) ;
		dL += dLbot * Math.exp( - dist/dRadius );
		return dL;
	}

	public Point2D.Double getPosition() {
		return fBot.getPosition();
	}

	public Point2D.Double getPositionClosestToTime( long time ) {
		return fBot.getPositionClosestToTime( time );
	}

	public botStatPoint getStatClosestToTime(long time) {
		return fBot.getStatClosestToTime( time );
	}

	public double getHeadingDegrees() {
		return fBot.getHeadingDegrees();
	}

	public int getNumEnemyAlive() {
		return _gameinfo.getNumEnemyAlive();
	}

	public gameInfo getGameInfo() {
		return _gameinfo;
	}

	public LinkedList<fighterBot> getEnemyBots() {
		return new LinkedList<fighterBot>(enemyBots.values());
	}

	public LinkedList<waveWithBullets> getEnemyWaves() {
		return enemyWaves;
	}

	public LinkedList<waveWithBullets> geMyWaves() {
		return myWaves;
	}

	public void initTic() {
	}

	public void manage() {
		_radar.manage();
		_motion.manage();
	}

	public LinkedList<firingSolution> getFiringSolutions( InfoBot tBot, long time, double bulletEnergy ) {
		// how this bot fires to the targer bot
		LinkedList<firingSolution> fSolutions = new LinkedList<firingSolution>();

		LinkedList<firingSolution> gunFSs = null;
		// FIXME: here should be loop over all available guns
		baseGun g = new headOnGun();
		gunFSs =  g.getFiringSolutions( fBot, tBot, time, bulletEnergy ) ;
		for ( firingSolution fS: gunFSs ) {
			fSolutions.add( fS);
		}
		
		return fSolutions;
	}

	public boolean isItMyWave(wave w) {
		return ( fBot.getName().equals( w.firedBot.getName() ) );
	}

	public void waveAdded(wave w) {
		if ( !isItMyWave(w) ) {
			// make with bullets from enemy Bot
			String enemyName =  w.firedBot.getName() ;
			fighterBot eBot = enemyBots.get( enemyName );
			logger.noise("bot " + fBot.getName() + " added enemy wave from " + enemyName );
			waveWithBullets wB = new waveWithBullets( w );
			LinkedList<firingSolution> fSolutions = eBot.getFiringSolutions( fBot, w.getFiredTime(), w.getBulletEnergy() );
			for ( firingSolution fS: fSolutions ) {
				wB.addFiringSolution(fS);
			}
			enemyWaves.add(wB);
		}
	}

	public void waveRemoved(wave w) {
		if ( !isItMyWave(w) ) {
			// going over enemy waves
			for ( waveWithBullets eW: enemyWaves ) {
				if ( eW.equals( w ) ) {
					enemyWaves.remove(eW);
					logger.noise(fBot.getName() + ": Enemy( " + eW.getFiredBot().getName() + ")  wave is removed");
					break;
				}
			}
		} else {
			// going over my waves
			for ( waveWithBullets mW: myWaves ) {
				if ( mW.equals( w ) ) {
					myWaves.remove(mW);
					logger.noise(fBot.getName() + ": my wave is removed");
					break;
				}
			}
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		_radar.onScannedRobot(e);
	}

	public void onRobotDeath(RobotDeathEvent e) {
		_radar.onRobotDeath(e);
	}

	public void onScannedRobot(InfoBot b) {
		if ( !getName().equals( b.getName() ) ) {
			logger.noise("Fighter bot " + getName() + " scanned " + b.getName() );
			enemyBots.put( b.getName(),  _gameinfo.liveBots.get( b.getName() ) );
		}
	}

	public void onRobotDeath(InfoBot b){
		enemyBots.remove( b.getName() ) ;
	}

	
	public void drawThisBot( Graphics2D g, long timeNow ) {
		double size = 40;
		Point2D.Double  p = fBot.getPositionClosestToTime( timeNow );
		if ( p != null ) {
			g.setColor(new Color(0x00, 0x00, 0xff, 0x80));
			graphics.drawSquare( g, p, size );
		}
	}

	public void drawEnemyBot( Graphics2D g, long timeNow, fighterBot eB ) {
		double size = 40;
		Point2D.Double  p = eB.fBot.getPositionClosestToTime( timeNow );
		if ( p != null ) {
			g.setColor(new Color(0xff, 0x00, 0x00, 0x80));
			graphics.drawSquare( g, p, size );
		}
	}
	public void onPaint( Graphics2D g, long timeNow ) {
		if ( isItMasterBotDriver() ) {
			// draw itself
			drawThisBot( g, timeNow );
			// draw enemy waves
			for ( waveWithBullets eW: enemyWaves ) {
				eW.onPaint( g, timeNow );
			}
			// draw known enemy bots
			for ( fighterBot eB: getEnemyBots() ) {
				drawEnemyBot( g, timeNow, eB );
			}
			// draw motion
			_motion.onPaint( g );
		}
	}

}
