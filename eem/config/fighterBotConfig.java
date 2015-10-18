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

import java.util.*;

public class fighterBotConfig {
	protected baseRadar _radar;
	protected basicMotion _motion;
	protected gunManager _gunManager;
	public botProxy proxy;
	public HashMap<String, LinkedList<baseGun>> masterBotGunMapForGameType = new HashMap<String, LinkedList<baseGun>>();
	public HashMap<String, LinkedList<baseGun>> enemyBotGunMapForGameType = new HashMap<String, LinkedList<baseGun>>();

	public fighterBotConfig() {
		LinkedList<baseGun> gunList;

		// ---- Master bot guns -----------
	       	gunList = new LinkedList<baseGun>();
		masterBotGunMapForGameType.put("default", gunList);
		gunList.add( new circularGun() );
		//gunList.add( new linearGun() );
		gunList.add( new headOnGun() );
		gunList.add( new randomGun() );
		gunList.add( new guessFactorGun() );
		gunList.add( new decayingGuessFactorGun() );
		gunList.add( new flipLastGuessFactorGun() );
	
	       	gunList = new LinkedList<baseGun>();
		masterBotGunMapForGameType.put("MasterBotAlreadyWon", gunList);

	       	gunList = new LinkedList<baseGun>();
		masterBotGunMapForGameType.put("1on1", gunList);
		gunList.add( new circularGun() );
		//gunList.add( new linearGun() );
		gunList.add( new headOnGun() );
		gunList.add( new randomGun() );
		gunList.add( new guessFactorGun() );
		gunList.add( new decayingGuessFactorGun() );
		gunList.add( new flipLastGuessFactorGun() );
	
	       	gunList = new LinkedList<baseGun>();
		masterBotGunMapForGameType.put("melee1on1", gunList);
		gunList.add( new circularGun() );
		//gunList.add( new linearGun() );
		gunList.add( new headOnGun() );
		gunList.add( new randomGun() );
		gunList.add( new guessFactorGun() );
		gunList.add( new decayingGuessFactorGun() );
		gunList.add( new flipLastGuessFactorGun() );
		
	       	gunList = new LinkedList<baseGun>();
		masterBotGunMapForGameType.put("meleeVeterans", gunList);
		gunList.add( new circularGun() );
		//gunList.add( new linearGun() );
		gunList.add( new headOnGun() );
		gunList.add( new randomGun() );
		gunList.add( new guessFactorGun() );
		gunList.add( new decayingGuessFactorGun() );
		gunList.add( new flipLastGuessFactorGun() );
		
	       	gunList = new LinkedList<baseGun>();
		masterBotGunMapForGameType.put("meleeSeasoned", gunList);
		gunList.add( new circularGun() );
		//gunList.add( new linearGun() );
		gunList.add( new headOnGun() );
		gunList.add( new randomGun() );
		gunList.add( new guessFactorGun() );
		gunList.add( new decayingGuessFactorGun() );
		gunList.add( new flipLastGuessFactorGun() );

	       	gunList = new LinkedList<baseGun>();
		masterBotGunMapForGameType.put("melee", gunList);
		gunList.add( new circularGun() );
		//gunList.add( new linearGun() );
		gunList.add( new headOnGun() );
		gunList.add( new randomGun() );
		gunList.add( new guessFactorGun() );
		gunList.add( new decayingGuessFactorGun() );
		gunList.add( new flipLastGuessFactorGun() );

		// ---- Enemy bot guns -----------
	       	gunList = new LinkedList<baseGun>();
		enemyBotGunMapForGameType.put("default", gunList);
		gunList.add( new headOnGun() );
		gunList.add( new circularGun() );
		gunList.add( new linearGun() );
		gunList.add( new unknownGun() );
	
	       	gunList = new LinkedList<baseGun>();
		enemyBotGunMapForGameType.put("MasterBotAlreadyWon", gunList);

	       	gunList = new LinkedList<baseGun>();
		enemyBotGunMapForGameType.put("1on1", gunList);
		gunList.add( new circularGun() );
		//gunList.add( new linearGun() );
		gunList.add( new headOnGun() );
		gunList.add( new guessFactorGun() );
		gunList.add( new decayingGuessFactorGun() );
		gunList.add( new flipLastGuessFactorGun() );
		//gunList.add( new safetyCorridorGun() );
		gunList.add( new unknownGun() );
		
	       	gunList = new LinkedList<baseGun>();
		enemyBotGunMapForGameType.put("melee1on1", gunList);
		gunList.add( new headOnGun() );
		gunList.add( new linearGun() );
		gunList.add( new circularGun() );
		//gunList.add( new guessFactorGun() );
		//gunList.add( new decayingGuessFactorGun() );
		//gunList.add( new flipLastGuessFactorGun() );
		gunList.add( new unknownGun() );
		
	       	gunList = new LinkedList<baseGun>();
		enemyBotGunMapForGameType.put("meleeVeterans", gunList);
		gunList.add( new headOnGun() );
		gunList.add( new linearGun() );
		gunList.add( new circularGun() );
		//gunList.add( new guessFactorGun() );
		//gunList.add( new decayingGuessFactorGun() );
		//gunList.add( new flipLastGuessFactorGun() );
		gunList.add( new unknownGun() );
		
	       	gunList = new LinkedList<baseGun>();
		enemyBotGunMapForGameType.put("meleeSeasoned", gunList);
		gunList.add( new headOnGun() );
		gunList.add( new linearGun() );
		//gunList.add( new circularGun() );
		//gunList.add( new guessFactorGun() );
		//gunList.add( new decayingGuessFactorGun() );
		//gunList.add( new flipLastGuessFactorGun() );
		gunList.add( new unknownGun() );
		
	       	gunList = new LinkedList<baseGun>();
		enemyBotGunMapForGameType.put("melee", gunList);
		gunList.add( new headOnGun() );
		//gunList.add( new linearGun() );
		//gunList.add( new circularGun() );
		//gunList.add( new guessFactorGun() );
		//gunList.add( new decayingGuessFactorGun() );
		//gunList.add( new flipLastGuessFactorGun() );
		gunList.add( new unknownGun() );
		
	}


	public void setDrivers( fighterBot bot,  gameInfo gInfo, boolean isItMasterBotDriver ) {
		if ( isItMasterBotDriver ) {
			// this bot is in charge of the master bot
			proxy = new realBotProxy( gInfo.getMasterBot() );
			_radar = new universalRadar( bot );
			//_motion = new dangerMapMotion( bot );
			_motion = new exactPathDangerMotion( bot );
			_gunManager = new nullGunManager( bot );
			_gunManager.setGunsMap( masterBotGunMapForGameType );
		} else {
			// this bot is in charge of the enemy bot
			proxy = new nullProxy( gInfo.getMasterBot() );
			_radar = new nullRadar( bot );
			_motion = new basicMotion( bot );
			_gunManager = new enemyBotGunManager( bot );
			_gunManager.setGunsMap( enemyBotGunMapForGameType );
		}
	}
}
