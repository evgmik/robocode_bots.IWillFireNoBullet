// -*- java -*-

package eem.core;

import eem.bot.*;

public class nullProxy extends botProxy {
	// this is proxy which does absolutely nothing
	// same as it parent botProxy
	
	public nullProxy() {
		super();
		proxyName = "nullProxy";
	}
	public nullProxy( CoreBot b) {
		this();
		masterBot = b;
	}
}
