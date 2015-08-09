// -*- java -*-

package eem.event;

import eem.bot.*;


public interface botListener {

	public void onScannedRobot(InfoBot b);

	public void onBotDeath(InfoBot b);

}
