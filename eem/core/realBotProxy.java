// -*- java -*-

package eem.core;

import eem.bot.*;
import eem.core.*;

public class realBotProxy extends botProxy {
	// this is proxy which gives access to main robocode AdvancedBot interfaces

	public realBotProxy( CoreBot bot) {
		masterBot = bot;
	}


	public void setTurnRadarRight(double a) {
		masterBot.setTurnRadarRight( a);
	}

	public void setAdjustRadarForGunTurn(boolean c) {
		masterBot.setAdjustRadarForGunTurn( c );
	}

	public double getRadarHeading() {
		return masterBot.getRadarHeading();
	}
}
