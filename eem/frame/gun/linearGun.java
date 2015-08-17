// -*- java -*-

package eem.frame.gun;
import eem.frame.bot.*;
import eem.frame.misc.*;

import java.util.LinkedList;
import java.awt.geom.Point2D;
import java.awt.Graphics2D;
import java.awt.Color;


public class linearGun extends baseGun {
	public String gunName;

	public linearGun() {
		gunName = "linearGun";
	}

	public LinkedList<firingSolution> getFiringSolutions( InfoBot fBot, InfoBot tBot, long time, double bulletEnergy ) {
		LinkedList<firingSolution> fSolultions = new LinkedList<firingSolution>();
		botStatPoint fBStat = fBot.getStatClosestToTime( time );
		if (fBStat == null)
			return fSolultions;

		botStatPoint tBStat = tBot.getStatClosestToTime( time );
		if (tBStat == null)
			return fSolultions;

		// OK we know fire point and at least one target position
		Point2D.Double fPos = (Point2D.Double) fBStat.getPosition().clone();
		Point2D.Double tPos = (Point2D.Double) tBStat.getPosition().clone();
		Point2D.Double vTvec = (Point2D.Double) tBStat.getVelocity().clone();

		double bSpeed = physics.bulletSpeed( bulletEnergy );

		// where target will be at wave intersection according to linear predictor
		Point2D.Double tFPos = misc.linear_predictor( bSpeed, tPos, vTvec,  fPos);

		firingSolution fS = new firingSolution( fPos, tFPos, time, bulletEnergy );
		logger.noise("linear gun firingSolution: " + fS.toString());
		fSolultions.add(fS);
		return fSolultions;
	}
}

