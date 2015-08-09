// -*- java -*-

package eem.gameInfo;

import eem.core.*;
import eem.event.*;
import eem.radar.*;
import eem.motion.*;
import eem.bot.*;
import eem.wave.*;
import eem.misc.*;

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
	public radar _radar;

	public static HashMap<String,fighterBot> liveBots = new HashMap<String, fighterBot>();
	public static HashMap<String,fighterBot> deadBots = new HashMap<String, fighterBot>();;


	public gameInfo(CoreBot bot) {
		logger.noise( "----- Creating gameInfo -----" );
		setMasterBot(bot);
		_radar = new radar(myBot);
		_motion = new basicMotion(myBot);
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
		_radar.initBattle( b );
		_motion.initBattle( b );
	}

	public void initTic() {
		long timeNow = myBot.getTime();
		_radar.initTic();
		_botsmanager.initTic( timeNow );
		_wavesManager.initTic( timeNow );
	}

	public long getTime() {
		return myBot.getTime();
	}

	public void run() {
		_radar.manage();
		_motion.moveToPoint( new Point2D.Double(20, 20) );
		myBot.execute();
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		_radar.onScannedRobot(e);
		_botsmanager.onScannedRobot(e);
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
		logger.noise("Time: " + getTime() +" Scanned bot " + botName );
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
		_radar.onRobotDeath(e);
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
		_motion.onPaint(g);
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
