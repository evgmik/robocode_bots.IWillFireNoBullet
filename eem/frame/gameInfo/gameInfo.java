// -*- java -*-

package eem.frame.gameInfo;

import eem.frame.core.*;
import eem.frame.event.*;
import eem.frame.radar.*;
import eem.frame.motion.*;
import eem.frame.bot.*;
import eem.frame.wave.*;
import eem.frame.misc.*;

import robocode.*;
import robocode.util.*;
import robocode.Rules.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.*;

public class gameInfo implements botListener {
	public CoreBot myBot;
	public basicMotion _motion;
	public botsManager _botsmanager;
	public wavesManager _wavesManager;

	public static HashMap<String,fighterBot> liveBots = new HashMap<String, fighterBot>();
	public static HashMap<String,fighterBot> deadBots = new HashMap<String, fighterBot>();;


	public gameInfo(CoreBot bot) {
		logger.noise( "----- Creating gameInfo -----" );
		setMasterBot(bot);
		_wavesManager = new wavesManager(myBot);
		_botsmanager = new botsManager( myBot, this );
		_botsmanager.addBotListener( this );
	}

	public void setMasterBot( CoreBot b) {
		myBot = b;
	}

	public CoreBot getMasterBot() {
		return myBot;
	}

	public void initBattle( CoreBot b) {
		setMasterBot( b );
	}

	public void initTic() {
		long timeNow = myBot.getTime();
		_botsmanager.initTic( timeNow );
		_wavesManager.initTic( timeNow );
		for( fighterBot fb: liveBots.values() ) {
			fb.initTic();
		}
		_wavesManager.initTic( timeNow );
	}

	public long getTime() {
		return myBot.getTime();
	}

	public int getNumEnemyAlive() {
		return myBot.numEnemyBotsAlive;
	}

	public void run() {
		for (fighterBot b : liveBots.values()) {
			b.manage();
		}
		myBot.execute();
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		_botsmanager.onScannedRobot(e);
		for ( fighterBot b: liveBots.values() ) {
			b.onScannedRobot(e);
		}
	}

	public void onScannedRobot(InfoBot b) {
		// it would be nice it amont other listeners
		// but we create botListener with new fighterBot
		// so we reserve to use the function below
	}

	public void specialOnScannedRobot(InfoBot b) {
		// here we are getting results from _botsmanager
		// but this function is a hack since we cannot use
		// onScannedRobot(InfoBot b)
		String botName = b.getName();
		logger.noise("Time: " + getTime() + " Scanned bot " + botName );
		fighterBot knownBot = null;
	       	knownBot = liveBots.get(botName);
		if ( knownBot != null ) {
			// nothing to do
			return;
		}
	       	knownBot = deadBots.get(botName);
		if ( knownBot != null ) {
			// bot is known but among dead ones
			// ressurecting it
			logger.noise("game manager ressurecting " + botName );
			logger.noise("old ref " + deadBots.get( botName) );
			logger.noise("new ref " +  knownBot );
			liveBots.put( botName, knownBot );
			deadBots.remove( botName );
			return;
		}
		// this is newly discovered bot
		knownBot = new fighterBot( b, this );
		if ( knownBot == null ) {
			// should never happen
			logger.error("Something wery wrong! We should have got fighterBot for " + botName);
		}
		liveBots.put( botName, knownBot );
	}

	public void onRobotDeath(RobotDeathEvent e) {
		_botsmanager.onRobotDeath(e);
		for ( fighterBot b: liveBots.values() ) {
			b.onRobotDeath(e);
		}
	}

	public void onRobotDeath(InfoBot b){
		// here we are getting results from _botsmanager
		logger.noise( "gameInfo: bot " + b.getName() + " is dead" );
		String botName = b.getName();
		fighterBot dBot = liveBots.get(botName);
		deadBots.put( botName, dBot);
		liveBots.remove( botName );
		logger.noise( this.toString() );
	}

	public void onPaint( Graphics2D g ) {
		_botsmanager.onPaint(g);
		_wavesManager.onPaint(g);
		long timeNow = myBot.getTime();
		for ( fighterBot fB: liveBots.values() ) {
			fB.onPaint( g, timeNow );
		}
	}

	public String toString() {
		String str = "";
		str += "Game Info stats\n";
		str += " liveBots known = " + liveBots.size() + "\n";
		for (fighterBot b : liveBots.values()) {
			str += "  bot: " + b.getName() + "\n";
		}
		str += " deadBots known = " + deadBots.size() + "\n";
		for (fighterBot b : deadBots.values()) {
			str += "  bot: " + b.getName() + "\n";
		}
		str += _botsmanager.toString();
		return str;
	}
}
