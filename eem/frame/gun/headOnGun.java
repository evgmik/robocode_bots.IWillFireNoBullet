// -*- java -*-

package eem.frame.gun;
import eem.frame.bot.*;
import eem.frame.misc.*;

import java.util.LinkedList;
import java.awt.geom.Point2D;
import java.awt.Graphics2D;
import java.awt.Color;


public class headOnGun extends baseGun {
	public String gunName;

	public headOnGun() {
		gunName = "headOnGun";
	}

	public LinkedList<firingSolution> getFiringSolutions( InfoBot fBot, InfoBot tBot, long time, double bulletEnergy ) {
		LinkedList<firingSolution> fSolultions = new LinkedList<firingSolution>();
		Point2D.Double fP = fBot.getPositionClosestToTime( time );
		if (fP == null)
			return fSolultions;

		Point2D.Double tP = tBot.getPositionClosestToTime( time );
		if (tP == null)
			return fSolultions;

		firingSolution fS = new firingSolution( fP, tP, time, bulletEnergy );
		fSolultions.add(fS);
		return fSolultions;
	}

}

