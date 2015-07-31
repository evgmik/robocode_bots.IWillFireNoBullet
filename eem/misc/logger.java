// -*- java -*-
package eem.misc;
import java.io.File;
import robocode.RobocodeFileWriter;
import java.io.IOException;

public class logger {
	// logger staff
	// debug levels
	public final static int log_important=0;
	public final static int log_error=2;
	public final static int log_warning=3;
	public final static int log_profiler=11;
	public final static int log_routine=4;
	public final static int log_debuging=5;
	public final static int log_stats=5;
	public final static int log_noise=10;
	private static int verbosity_level=log_debuging; // current level, smaller is less noisy
	private static RobocodeFileWriter fileWriter = null;

	public logger (int vLevel) {
		this();
		verbosity_level = vLevel;
	}

	public logger (int vLevel, RobocodeFileWriter fileWriter ) {
		this();
		this.fileWriter = fileWriter;
		verbosity_level = vLevel;
	}

	public logger () {
	}

	public static void log_message(int level, String s) {
		if (level <= verbosity_level) {
			System.out.println(s);
		}
		//if (true) {
		if (level <= verbosity_level) {
			if ( fileWriter != null ) {
				try {
					s = s + "\n";
					fileWriter.write(s);
					fileWriter.flush();
				} catch (IOException ioe) {
					System.out.println("Trouble writing to the log file: " + ioe.getMessage());
				}
			} else {
				System.out.println("The log file writer does not exist");
			}
		}
	}

	public static void warning(String s) {
		log_message(log_warning, s);
	}
	public static void noise(String s) {
		log_message(log_noise, s);
	}

	public static void profiler(String s) {
		log_message(log_profiler, s);
	}

	public static void error(String s) {
		log_message(log_error, s);
	}

	public static void dbg(String s) {
		log_message(log_debuging, s);
	}

	public static void stats(String s) {
		log_message(log_stats, s);
	}

	public static void routine(String s) {
		log_message(log_routine, s);
	}

	public static void important(String s) {
		log_message(log_important, s);
	}

	public static String shortFormatDouble(double d) {
		String str;
		str =  String.format("%.2f", d );
		return str;
	}

	public static String hitRateFormat( int hC, int fC) {
		double hR = math.eventRate( hC, fC );
		// string formatting
		String hRstr = logger.shortFormatDouble( 100.0*hR ) + "%";
		hRstr = String.format("%8s", hRstr);
		String hCstr = String.format("%4d", hC);
		String fCstr = String.format("%-4d", fC);
		String strOut = "";
		String tmpStr = hCstr + "/" + fCstr + " = " + hRstr;
		strOut += String.format( "%16s", tmpStr );
		return strOut;
	}

}

