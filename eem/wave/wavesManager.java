// -*- java -*-

package eem.wave;

import eem.core.*;
import eem.event.*;
import eem.bot.*;
import eem.misc.*;

import robocode.Bullet;

import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.*;

public class  wavesManager {
	public CoreBot myBot;
	public LinkedList<wave> Waves = new LinkedList<wave>();

	public LinkedList<waveListener> waveListeners = new LinkedList<waveListener>();

	public wavesManager(CoreBot bot) {
		myBot = bot;
	}

	public void initTic(long timeNow) {
		remove ( getListOfPassedWaves( myBot._gameinfo._botsmanager.listOfAliveBots(), timeNow ) );
	}

	public void add( InfoBot firedBot, long firedTime, double bulletEnergy )  {
		wave w = new wave( firedBot, firedTime, bulletEnergy );
		Waves.add(w);
	}

	public void add( wave w )  {
		Waves.add( w );
		for ( waveListener l : waveListeners ) {
			l.waveAdded(w);
		}
	}

	public void remove(wave w) {
		Waves.remove( w );
		for ( waveListener l : waveListeners ) {
			l.waveRemoved(w);
		}
	}

	public void remove(LinkedList<wave> wavesToRemove) {
		for ( wave w : wavesToRemove ) {
			remove( w );
		}
	}

	public LinkedList<wave> getListOfPassedWaves(LinkedList<InfoBot> listOfAliveBots, long timeNow) {
		LinkedList<wave> passedWaves = new LinkedList<wave>();
		ListIterator<wave> wLIter;
		wLIter = Waves.listIterator();
		while (wLIter.hasNext()) {
			wave w = wLIter.next();
			boolean isWaveActive = false;
			for ( InfoBot bot : listOfAliveBots ) {
				if ( !w.isBehindBot( bot, timeNow ) ) {
					// the wave is still active
					isWaveActive = true;
					break;
				}
			}
			if ( !isWaveActive ) {
				//wLIter.remove();
				passedWaves.add( w );
			}
		}
		return( passedWaves );
	}

	public void addWaveListener(waveListener l) {
		waveListeners.add(l);
	}

	public void onPaint(Graphics2D g) {
		long timeNow = myBot.getTime();
		for ( wave w : Waves ) {
			w.onPaint(g, timeNow);
		}
	}
}

