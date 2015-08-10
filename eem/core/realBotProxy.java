// -*- java -*-

package eem.core;

import eem.bot.*;
import eem.misc.*;

public class realBotProxy extends botProxy {
	// this is proxy which gives access to main robocode AdvancedBot interfaces

	public realBotProxy() {
		super();
		proxyName = "realBotProxy";
	}

	public realBotProxy( CoreBot bot) {
		this();
		masterBot = bot;
	}


	// radar
	public void setTurnRadarRight(double a) {
		masterBot.setTurnRadarRight( a);
	}

	public void setAdjustRadarForGunTurn(boolean c) {
		masterBot.setAdjustRadarForGunTurn( c );
	}

	public double getRadarHeading() {
		return masterBot.getRadarHeading();
	}

	// body
	public void setTurnRight(double a){
		masterBot.setTurnRight(a);
	}

	public void setAhead(double d) {
		masterBot.setAhead(d);
	}
}
