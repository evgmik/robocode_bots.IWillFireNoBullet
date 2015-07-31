package eem;
import eem.radar.*;
import eem.misc.*;

import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Random;
import java.util.*;

import robocode.*;
import robocode.util.*;
import robocode.Rules.*;

/**
 * IWillFireNoBullet - very peaceful robot by Eugeniy E. Mikhailov
 */
public class IWillFireNoBullet extends AdvancedRobot
{
	public Rules game_rules;
	double BodyTurnRate = game_rules.MAX_TURN_RATE;
	public int robotHalfSize;

	private botVersion botVer;
	public radar _radar;

	public int numEnemyBotsAlive = 1; // we have at least one enemy in general
	public long initTicStartTime = 0;

	public Point2D.Double myCoord;
	public Point2D.Double BattleField;
	double absurdly_huge=1e6; // something huge
	double desiredBodyRotationDirection = 0; // our robot body desired angle



	public long ticTime;
	public int roundCnt = 0;
	int nonexisting_coord = -10000;
	public int totalNumOfEnemiesAtStart = 0;
	public static int roundsWon = 0;
	public static int roundsLost = 0;
	public static int  finishingPlacesStats[] = null;
	public static int  skippedTurnStats[] = null;
	public static int bulletFiredCnt = 0;
        public static int bulletHitCnt = 0;	
        public static int bulletHitByPredictedCnt = 0;	
	private static int numTicsWhenGunInColdState = 0;

	// logger staff
	private String logFileName = "IWillFireNoBullet.log";
	public int verbosity_level=logger.log_debuging; // current level, smaller is less noisy
	private static RobocodeFileWriter fileWriter = null;
	private boolean appendToLogFlag = false; // TODO: make use of it
	public logger _log = null;

	public IWillFireNoBullet() {
	}

	public void initBattle() {
		if ( fileWriter == null ) {
			try {
				fileWriter = new RobocodeFileWriter( this.getDataFile( logFileName ) );
				_log = new logger(verbosity_level, fileWriter);
			} catch (IOException ioe) {
				System.out.println("Trouble opening the logging file: " + ioe.getMessage());
				_log = new logger(verbosity_level);
			}
		}

		roundCnt = getRoundNum() + 1;
		logger.routine("=========== Round #" + (roundCnt) + "=============");
		BattleField = new Point2D.Double(getBattleFieldWidth(), getBattleFieldHeight());
		robotHalfSize = (int) getWidth()/2;

		physics.init(this); // BattleField must be set
		math.init(this); // BattleField must be set

		myCoord = new Point2D.Double( getX(), getY() );

		setColors(Color.red,Color.blue,Color.green);
		botVer = new botVersion();

		totalNumOfEnemiesAtStart = getOthers();
		if ( finishingPlacesStats == null ) {
			finishingPlacesStats = new int[totalNumOfEnemiesAtStart+1];
		}

		if ( skippedTurnStats == null ) {
			skippedTurnStats = new int[getNumRounds()];
		}

		_radar = new radar(this);

		// give ScannedRobotEvent almost the highest priority,
		// otherwise when I process bullet related events
		// I work with old enemy bots coordinates
		setEventPriority("ScannedRobotEvent", 98);
		initTicStartTime = System.nanoTime();
	}

	public void initTic() {

		numEnemyBotsAlive = getOthers();

		// gun, radar, and body are decoupled
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true); 

		setTicTime();

		logger.noise("----------- Bot version: " + botVer.getVersion() + "------- Tic # " + ticTime + " -------------");
		logger.noise("Game time: " + ticTime);
		logger.noise("Number of other bots = " + numEnemyBotsAlive);
		
		if ( numEnemyBotsAlive == 0 ) {
			//logger.dbg("Round is over");
			return;
		}

		myCoord.x = getX();
	       	myCoord.y = getY();

		
		profiler.start( "_motion.initTic" );
		profiler.stop( "_motion.initTic" );
		
		profiler.start( "_gun.initTic" );
		profiler.stop( "_gun.initTic" );
		
		_radar.initTic();

	}

	private void setTicTime() {
		ticTime = this.getTime();
	}

	public long getTime() {
		// Robocode start every round with zero
		// to keep our own time increasing along the battle
		// we add to this time 100000*round_number
		// this big enough to separate rounds 
		return ( super.getTime() + 100000*(getRoundNum()+1) ); 
	}

	public String fightType() {
		double survRatio = 1.0*numEnemyBotsAlive/totalNumOfEnemiesAtStart;
		if ( (numEnemyBotsAlive == 1) && (totalNumOfEnemiesAtStart == 1) )
			return "1on1";
		if ( (numEnemyBotsAlive == 1) && (totalNumOfEnemiesAtStart != 1) )
			return "melee1on1";
		if ( survRatio > 2./3. )
			return "melee";
		return "meleeMidle";
	}

	public double distTo(double x, double y) {
		double dx=x-myCoord.x;
		double dy=y-myCoord.y;

		return Math.sqrt(dx*dx + dy*dy);
	}

	public void run() {
		initBattle();

		while(true) {
			initTic() ;
			if ( getOthers() == 0 ) {
				//logger.dbg("Round is over");
				setAhead(0);
				setStop();
				execute();
				continue;
			}

			//FIXME
			//_radar.setNeedToTrackTarget( _gun.doesItNeedTrackedTarget() );
			_radar.manage();
			
			execute();
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		setTicTime();
		myCoord.x = getX();
	       	myCoord.y = getY();

		_radar.onScannedRobot(e);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
	}

	public void  onBulletHit(BulletHitEvent e) {
	}

	public void  onBulletMissed(BulletMissedEvent e) {
	}

	public void onRobotDeath(RobotDeathEvent e) {
	}


	public void onHitWall(HitWallEvent e) {
		setTicTime();
	}
		
	public void onSkippedTurn(SkippedTurnEvent e) {
	}
	
	public void onPaint(Graphics2D g) {
	}

	public void onWin(WinEvent  e) {
		setTicTime();
		//logger.dbg("onWin");
		roundsWon++;
		updateFinishingPlacesStats();
		winOrLoseRoundEnd();
	}

	public void onDeath(DeathEvent e ) {
		setTicTime();
		//logger.dbg("onDeath");
		roundsLost++;
		updateFinishingPlacesStats();
		winOrLoseRoundEnd();
	}

	public void onRoundEnded(RoundEndedEvent e) {
		setTicTime();
		// this methods is called before onDeath or onWin
		// so we should not output any valiable stats here
		// if I want to see it at the end
		//logger.dbg("onRoundEnded");
		//winOrLoseRoundEnd();
	}

	public void updateFinishingPlacesStats() {
		int myWinLosePlace = getOthers();
		finishingPlacesStats[myWinLosePlace]++;
	}

	public void winOrLoseRoundEnd() {
	}

}
