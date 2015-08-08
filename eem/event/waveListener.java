// -*- java -*-

package eem.event;

import eem.wave.*;

import robocode.Bullet;
import robocode.BulletHitEvent;


public interface waveListener {

	public void waveAdded(wave w);

	public void waveRemoved(wave w);

}
