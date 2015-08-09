// -*- java -*-
// (C) 2013 by Eugeniy Mikhailov, <evgmik@gmail.com>

package eem.radar;

import eem.core.*;
import eem.bot.*;
import eem.misc.*;

import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import robocode.*;
import robocode.util.*;
import robocode.Rules.*;

public class baseRadar {
	protected fighterBot myBot;

	public baseRadar(fighterBot bot) {
		initBattle(bot);
	}

	public void initBattle(fighterBot b) {
		myBot = b;
	}

	public void initTic() {
	}

	public void manage() {
	}


	public void onRobotDeath(RobotDeathEvent e) {
	}

	public void onScannedRobot(ScannedRobotEvent e) {
	}

	public String toString() {
		String str = "";
		str += "nullRadar has no stats";
		return str;
	}
}

