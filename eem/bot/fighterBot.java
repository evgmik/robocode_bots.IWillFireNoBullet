// -*- java -*-

package eem.bot;

import eem.IWillFireNoBullet;
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

public class fighterBot {
	protected InfoBot fBot;
	protected gameInfo _gameinfo;
	protected botsManager _botsmanager;
	protected wavesManager _wavesManager;

	public fighterBot( InfoBot fBot, gameInfo gInfo) {
		this.fBot = fBot;
		_gameinfo = gInfo;
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

	public LinkedList<wave> getEnemyWaves() {
		String fBotName = fBot.getName();
		LinkedList<wave> eWaves = new LinkedList<wave>();
		for ( wave w: _gameinfo._wavesManager.Waves ) {
			if ( !fBotName.equals( w.firedBot.getName() ) ) {
				eWaves.add( w );
			}
		}
		return eWaves;
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

	public void onPaint(Graphics2D g) {
		LinkedList<InfoBot> bots = getEnemyBots();
		firingSolution fS = null;
		for ( InfoBot b: bots ){
			fS = getFiringSolutionFor( b, fBot.getLastSeenTime() );
		}
		if ( fS != null )
			fS.onPaint( g );
	}

}
