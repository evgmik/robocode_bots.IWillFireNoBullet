// -*- java -*-

package eem.gameInfo;

import eem.IWillFireNoBullet;
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

public class gameInfo {
	public IWillFireNoBullet myBot;
	public basicMotion _motion;
	public botsManager _botsmanager;
	public wavesManager _wavesManager;
	public radar _radar;


	public gameInfo(IWillFireNoBullet bot) {
		this.myBot = bot;
		_radar = new radar(myBot);
		_motion = new basicMotion(myBot);
		_botsmanager = new botsManager(myBot);
		_wavesManager = new wavesManager(myBot);
	}

	public void initTic() {
		_radar.initTic();
		_botsmanager.initTic(myBot.getTime());
	}

	public void run() {
		_radar.manage();
		_motion.moveToPoint( new Point2D.Double(physics.BattleField.x/2, physics.BattleField.y/2) );
		myBot.execute();
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		_radar.onScannedRobot(e);
		_botsmanager.onScannedRobot(e);
	}

	public void onPaint( Graphics2D g ) {
		_motion.onPaint(g);
		_botsmanager.onPaint(g);
		_wavesManager.onPaint(g);
	}
}
