// -*- java -*-
package eem.config;

import eem.frame.core.*;
import eem.frame.event.*;
import eem.frame.bot.*;
import eem.frame.radar.*;
import eem.frame.wave.*;
import eem.frame.gameInfo.*;
import eem.frame.motion.*;
import eem.frame.gun.*;
import eem.frame.misc.*;

public class fighterBotConfig {
	protected baseRadar _radar;
	protected basicMotion _motion;
	protected gunManager _gunManager;
	public botProxy proxy;

	public void setDrivers( fighterBot bot,  gameInfo gInfo, boolean isItMasterBotDriver ) {
		if ( isItMasterBotDriver ) {
			// this bot is in charge of the master bot
			proxy = new realBotProxy( gInfo.getMasterBot() );
			_radar = new universalRadar( bot );
			//_motion = new dangerMapMotion( bot );
			_motion = new exactPathDangerMotion( bot );
			_gunManager = new nullGunManager( bot );

		} else {
			// this bot is in charge of the enemy bot
			proxy = new nullProxy( gInfo.getMasterBot() );
			_radar = new nullRadar( bot );
			_motion = new basicMotion( bot );
			_gunManager = new enemyBotGunManager( bot );
		}
	}
}
