// -*- java -*-

package eem.wave;

import eem.IWillFireNoBullet;
import eem.bot.*;
import eem.misc.*;

import robocode.Bullet;

import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.*;

public class  wavesManager {
	public IWillFireNoBullet myBot;
	public LinkedList<wave> Waves = new LinkedList<wave>();

	public wavesManager(IWillFireNoBullet bot) {
		myBot = bot;
	}

	public void initTic() {
	}

	public void add( InfoBot firedBot, long firedTime, double bulletEnergy )  {
		wave w = new wave( firedBot, firedTime, bulletEnergy );
		Waves.add(w);
	}

	public void cleanUpPassedWaves(LinkedList<InfoBot> listOfAliveBots, long timeNow) {
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
				logger.dbg("wave is inactive");
				wLIter.remove();
			}
		}
	}

	public void onPaint(Graphics2D g) {
		long timeNow = myBot.getTime();
		for ( wave w : Waves ) {
			w.onPaint(g, timeNow);
		}
	}
}

