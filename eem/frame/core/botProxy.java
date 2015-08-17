// -*- java -*-

package eem.frame.core;

import eem.frame.bot.*;
import eem.frame.core.*;

public class botProxy implements proxyBotInterface {
	public CoreBot masterBot = null;
	// this is just a base class it does nothing
	
	protected String proxyName = "";

	public botProxy() {
	}

	public botProxy( CoreBot bot) {
	}

	public String getName() {
		return proxyName;
	}

	// radar
	public void setTurnRadarRight(double a) {
	}

	public void setAdjustRadarForGunTurn(boolean c) {
	}

	public double getRadarHeading() {
		return 0;
	}

	// body
	public void setTurnRight(double a){
	}

	public void setAhead(double d) {
	}
}
