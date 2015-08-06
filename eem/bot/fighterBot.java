// -*- java -*-

package eem.bot;

import eem.IWillFireNoBullet;
import eem.bot.*;
import eem.wave.*;
import eem.gameInfo.*;
import eem.misc.*;

import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.*;
import java.lang.Integer;

import robocode.*;
import robocode.util.*;
import robocode.Rules.*;

public class fighterBot {
	protected InfoBot myBot;
	protected gameInfo _gameinfo;
	protected botsManager _botsmanager;
	protected wavesManager _wavesManager;

	public fighterBot( InfoBot fBot, gameInfo gInfo) {
		this.myBot = fBot;
		_gameinfo = gInfo;
	}

	public LinkedList<InfoBot> getEnemyBots() {
		String myName = myBot.getName();
		LinkedList<InfoBot> eBots = new LinkedList<InfoBot>();
		for ( InfoBot b: _gameinfo._botsmanager.listOfAliveBots() ) {
			if ( !myName.equals( b.getName() ) ) {
				eBots.add( b );
			}
		}
		return eBots;
	}

	public LinkedList<wave> getEnemyWaves() {
		String myName = myBot.getName();
		LinkedList<wave> eWaves = new LinkedList<wave>();
		for ( wave w: _gameinfo._wavesManager.Waves ) {
			if ( !myName.equals( w.firedBot.getName() ) ) {
				eWaves.add( w );
			}
		}
		return eWaves;
	}

	public void initTic() {
	}

	public void onPaint(Graphics2D g) {
	}

}
