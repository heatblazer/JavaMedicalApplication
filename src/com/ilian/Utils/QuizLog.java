package com.ilian.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuizLog  implements java.io.Serializable {

	/* TODO special logging - EVENTS ONLY ! */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7184540213123800400L;
	private static List<String> logText = new ArrayList<String>();
	private static BufferedWriter bwriter;
	private static java.io.PrintWriter pw ;
	public static void log(String s) { logText.add(s); }
	public static void record(final String fname) {
		new Thread(new Runnable() {
			
			public void run() {
				try {
					@SuppressWarnings("deprecation")
					String dat = new Date().toGMTString();
					File logDir = new File("logs");
					String line = "----------";
					if ( !logDir.exists() ) {
						QuizLog.log("no 'logs' dir found. Creating...");
						boolean result = logDir.mkdir();
						if ( result ) {
							QuizLog.log("directory created OK");
							File logfile = new File(logDir.getAbsolutePath(), dat+fname);
							pw = new java.io.PrintWriter(new BufferedWriter(new java.io.FileWriter(logfile)));
							for (int i=0; i < logText.size(); i++) {
								pw.println(logText.get(i));
							}
						} else {
							QuizLog.log("error in creating directory!");
						}
					} else {
						File logfile = new File(logDir.getAbsolutePath(), dat+fname);
						pw = new java.io.PrintWriter(new BufferedWriter(new java.io.FileWriter(logfile)));
						for (int i=0; i < logText.size(); i++) {
							pw.println(logText.get(i));
						}
					}
				} catch (Exception ioex) { }
				pw.close();
				}
			}).start();
	}
}
