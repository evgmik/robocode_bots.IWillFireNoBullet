// -*- java -*-
// (C) 2013 by Eugeniy Mikhailov, <evgmik@gmail.com>

package eem.radar;

import eem.core.*;
import eem.bot.*;
import eem.misc.*;

import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import robocode.*;
import robocode.util.*;
import robocode.Rules.*;

public class nullRadar extends baseRadar {
	protected fighterBot myBot;

	public nullRadar(fighterBot bot) {
		super(bot);
	}

	public String toString() {
		String str = "";
		str += "nullRadar has no stats";
		return str;
	}
}

