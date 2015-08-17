// -*- java -*-

package eem.frame.event;

import eem.frame.bot.*;


public interface botListener {

	public void onScannedRobot(InfoBot b);

	public void onRobotDeath(InfoBot b);

}
