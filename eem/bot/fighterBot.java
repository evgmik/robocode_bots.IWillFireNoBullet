// -*- java -*-

package eem.bot;

import eem.core.*;
import eem.event.*;
import eem.bot.*;
import eem.wave.*;
import eem.gameInfo.*;
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

public class fighterBot implements waveListener {
	protected InfoBot fBot;
	protected gameInfo _gameinfo;
	protected botsManager _botsmanager;
	protected wavesManager _wavesManager;

	public LinkedList<waveWithBullets> enemyWaves = new LinkedList<waveWithBullets>();
	public LinkedList<waveWithBullets> myWaves    = new LinkedList<waveWithBullets>();

	public fighterBot( InfoBot fBot, gameInfo gInfo) {
		this.fBot = fBot;
		_gameinfo = gInfo;
		_gameinfo._wavesManager.addWaveListener( this );
	}

	public LinkedList<InfoBot> getEnemyBots() {
		String fBotName = fBot.getName();
		LinkedList<InfoBot> eBots = new LinkedList<InfoBot>();
		for ( InfoBot b: _gameinfo._botsmanager.listOfAliveBots() ) {
			if ( !fBotName.equals( b.getName() ) ) {
				eBots.add( b );
			}
		}
		return eBots;
	}

	public LinkedList<waveWithBullets> getEnemyWaves() {
		return enemyWaves;
	}

	public LinkedList<waveWithBullets> geMyWaves() {
		return myWaves;
	}

	public void initTic() {
	}

	public firingSolution getFiringSolutionFor( InfoBot bot, long time ) {
		LinkedList<InfoBot> bots = getEnemyBots();
		
		firingSolution fS = null;
		for ( InfoBot b: bots ){
			fS = new firingSolution( b.getPosition(), fBot.getPosition());
		}
		return fS;
	}

	public boolean isItMyWave(wave w) {
		return ( fBot.getName().equals( w.firedBot.getName() ) );
	}

	public void waveAdded(wave w) {
		if ( !isItMyWave(w) ) {
			logger.noise("bot " + fBot.getName() + " added enemy wave from " + w.firedBot.getName() );
			waveWithBullets wB = new waveWithBullets( w );
			baseGun g = new headOnGun();
			LinkedList<firingSolution> fSolutions =  g.getFiringSolutions( w.firedBot, fBot, w.getFiredTime(), w.getBulletEnergy() );
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
					logger.dbg(fBot.getName() + ": Enemy( " + eW.getFiredBot().getName() + ")  wave is removed");
					break;
				}
			}
		} else {
			// going over my waves
			for ( waveWithBullets mW: myWaves ) {
				if ( mW.equals( w ) ) {
					myWaves.remove(mW);
					logger.dbg(fBot.getName() + ": my wave is removed");
					break;
				}
			}
		}
	}

	public void onPaint( Graphics2D g, long timeNow ) {
		LinkedList<InfoBot> bots = getEnemyBots();
		for ( waveWithBullets eW: enemyWaves ) {
			eW.onPaint( g, timeNow );
		}
	}

}
