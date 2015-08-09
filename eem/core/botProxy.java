// -*- java -*-

package eem.core;

import eem.bot.*;
import eem.core.*;

public class botProxy implements proxyBotInterface {
	public CoreBot masterBot = null;
	// this is just a base class it does nothing

	public botProxy() {
	}

	public botProxy( CoreBot bot) {
	}

	public void setTurnRadarRight(double a) {
	}

	public void setAdjustRadarForGunTurn(boolean c) {
	}

	public double getRadarHeading() {
		return 0;
	}
}
