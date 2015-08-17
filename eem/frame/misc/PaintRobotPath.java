package eem.frame.misc;
// borrowed from wompi.misc.painter

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.HashMap;

public class PaintRobotPath 
{
	static HashMap<String, PathHelper> pathMap = new HashMap<String, PathHelper>();
	static long lastTime;
	
	public static void onPaint(Graphics2D g,String botName, long time, double xRobot,double yRobot, Color pathColor)
	{
		if (lastTime > time) pathMap.clear();  // new battle reset
		lastTime = time;
		
		PathHelper myPath = pathMap.get(botName);
		if (myPath == null)
		{
			myPath = new PathHelper();
			myPath.rName = botName;
			myPath.rPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 2000); 
			myPath.rPath.moveTo (xRobot, yRobot);
			myPath.rColor = pathColor;
			pathMap.put(botName, myPath);
		}
		
		if (time - myPath.rTime >= 5)  // thin out the path  
		{
			myPath.rPath.lineTo(xRobot,yRobot);
			myPath.rTime = time;
		}
		
		for (PathHelper helper : pathMap.values()) 
		{
			if ((time - helper.rTime) >= 30) continue;   // dead robots fade away after 30 turns
			g.setColor(helper.rColor);		
			g.draw(helper.rPath);				
		}
	}
}

class PathHelper
{
	GeneralPath rPath;
	String rName;
	Color rColor;
	long rTime;
}
