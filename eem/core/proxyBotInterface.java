// -*- java -*-

package eem.core;

public interface proxyBotInterface {

	// this should give or null access to realbot commands

	// radar
	public void setTurnRadarRight(double a);
	public void setAdjustRadarForGunTurn(boolean c);
	public double getRadarHeading();

	// body
	public void setTurnRight(double a);
	public void setAhead(double d);




}


