package com.ilian.Utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ConfigReader {
	static String line ="";
	static String[] matches = new String[2];
	
	/*varst to set and check for */
	public static int isFirstBoot = 0;
	public static int lookAndFeel = 0;
	public static int isFullScreen = 0;
	public static int I = 3; // remove for debug of QTimer only!!!
	private enum Level { EASY, MEDIUM, HARD };
	static List<Integer> list = new ArrayList<Integer>();
	
	public static void readConfing() {
		try {
			int i=0;
			BufferedReader br = new BufferedReader(new FileReader(".qapp_config.cfg"));
			while ( (line = br.readLine()) != null ) { 
				matches = line.split(":");
				System.out.println("CFG READER: "+matches[1]);
				list.add(Integer.parseInt(matches[1]));
			}
			isFirstBoot = (int) list.get(0);
			lookAndFeel = (int) list.get(1);
			isFullScreen = (int) list.get(2);
			I = 		(int) list.get(3); 
			
			try { br.close(); return; } catch (Exception ex2) { }
		} catch (Exception e) { 
				QuizLog.log("File does not exists. Creating...");
				QuizLog.log("File does not exists. Creating .qapp_config.cfg");
			try {
				/* test block - BEGIN */
				BufferedWriter bw = new BufferedWriter(new FileWriter(".qapp_config.cfg"));
				bw.write("isFirstBoot:1\nlookAndFeel:0\nisFullScreen:0\nI:3\n");
				try { bw.close(); } catch (Exception eclose) { }
				
			}catch (Exception e1) { 
				QuizLog.log("NOT WRITABLE FILESYSTEM. CHECK PERMISSIONS OR CONTACT YOUR SYSTEM ADMINISTRATOR."); 
				}
		}
		lookAndFeel = (int)Integer.parseInt(matches[1]);
		
	}
}
