package com.ilian.Quiz;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
/* This will be disabled for now */
import java.util.List;

import javax.imageio.ImageIO;

import com.ilian.Utils.QuizLog;


public class QuizScreenCapture implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9167917016098489080L;
	private static BufferedImage[] screensarr = new BufferedImage[1024];
	private static File[] filesarr = new File[1024];
	private static int INDEX = 0;
	
	private static List<BufferedImage> screens = new ArrayList<BufferedImage>();
	private static List<File> files = new ArrayList<File>(); 
	private static int fileIndex = 0; 
	private static boolean isRunning = true;
	private static boolean isPressed = false;
	public static void setIsPressed(boolean tf) { isPressed = tf; }
	public static boolean getIsPressed() { return isPressed; }
	public static boolean getRunning() { return isRunning; }
	public static void setRunning(boolean tf) { isRunning = tf; }
	private static Toolkit tool = Toolkit.getDefaultToolkit();
	private static Dimension d = new Dimension();
	private static Rectangle rect = new Rectangle(d);
	private static Robot robot = null;
	private static void print() {
		if ( INDEX > 1024 ) {
			QuizLog.log("Array overflowed");
			return;
		}
		try {
			Toolkit tool = Toolkit.getDefaultToolkit();
			d = tool.getScreenSize();
			rect = new Rectangle(d);
			robot = new Robot();
			/* temporaly disabled for testing arrays */
			//screens.add(robot.createScreenCapture(rect));
			//files.add(new File(fileIndex+"screen"));
			//ImageIO.write(screens.get(screens.size()-1), "jpeg", files.get(files.size()-1));
			
			screensarr[INDEX] = robot.createScreenCapture(rect);
			filesarr[INDEX] = new File(fileIndex+"screen");
			INDEX++;
			fileIndex++;
		} catch (Exception ex) { }
	}
	
	public static void record() {
		new Thread(new Runnable() {
			public void run() {
		try {
			for (int i=0; i < 1024; i++) {
				ImageIO.write(screensarr[i],  "jpeg",  filesarr[i]); 
				try { Thread.currentThread().sleep(300); } catch (Exception fls) { }
				}
			} catch (Exception ex)  { } 
		}
		}).start();
	}
	
	public static void init() {
		new Thread(new Runnable() {
			public void run() {
				int i = 0;
				while ( i < 100 ) {
					if ( isPressed ) {
						print();
					}
					try { Thread.currentThread().sleep(10); } catch (Exception e) { }
				}
			}
		}).start();
	}
	
	
	public static void main(String[] args) {
		QuizScreenCapture.init();
	}
}
