// -*- java -*-

package eem.motion;

import eem.core.*;
import eem.dangermap.*;
import eem.bot.*;
import eem.misc.*;

import robocode.util.*;

import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.awt.Color;



public class dangerMapMotion extends basicMotion {
	protected fighterBot myBot;
	public dangerMap _dangerMap;

	public void initTic() {
		_dangerMap.calculateDanger( myBot.getTime() );
	}

	public dangerMapMotion() {
	}

	public dangerMapMotion(fighterBot bot) {
		initBattle( bot );
	}

	public void manage() {
		// for basic motion we do nothing
	}

	public void makeMove() {
		// for basic motion we do nothing
	}

	public void onPaint(Graphics2D g) {
		_dangerMap.onPaint(g);
	}

}
