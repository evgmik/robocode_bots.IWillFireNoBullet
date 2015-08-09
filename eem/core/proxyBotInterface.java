// -*- java -*-

package eem.core;

public interface proxyBotInterface {

	// this should give or null access to realbot commands

	public void setTurnRadarRight(double a);
	public void setAdjustRadarForGunTurn(boolean c);

	public double getRadarHeading();

}


