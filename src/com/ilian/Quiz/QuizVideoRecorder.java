package com.ilian.Quiz;

/* USING OPEN CV */

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.ilian.Utils.QuizLog;



/* started on 07.12.2013 */
/* TODO - work on cross platform camera module */ 
public class QuizVideoRecorder   {
	/**
	 * 
	 */

	private static int INDEX = 0;
	private static OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
	private static IplImage img = new IplImage();
	private static BufferedImage[] fotos = new BufferedImage[1024]; 
	
	public   static void saveImage(String s, IplImage img) {
		String pwd = System.getProperty("user.dir");
		System.out.println(pwd);
		try {
			File outfile = new File("capture"+INDEX);
			QuizLog.log("Writing to: "+pwd);
			ImageIO.write(img.getBufferedImage(), "jpg", outfile);
			INDEX++;
		} catch (Exception ex) { 
			QuizLog.log("ERROR IN WRITING FILE:"+ex.toString());
		}
	}
	/*static method should be synchronized else the frame grabber fails
	 * important NOTE!
	 */
	public  static synchronized void captureFrame()  {
			
		QuizLog.log("RUNNING CAMERA MODE");
		if ( grabber == null ) { 
				QuizLog.log("Error NO OPEN CV");
		} else {
				QuizLog.log("OPENCV OK");
			try {
				
				grabber.start();
				QuizLog.log("STARTED GRABBER");
				img = grabber.grab();
				File file = new File(INDEX+"");
				if ( img != null ) {
					QuizLog.log(img.toString());
					QuizLog.log(img.getBufferedImage().toString());
				}
				if ( file != null ) QuizLog.log(file.toString());
				ImageIO.write(img.getBufferedImage(), "jpg", file);
				QuizLog.log("Image saved to: "+file.getAbsolutePath());
				INDEX++;
			
			} catch (Exception ex) { 
				QuizLog.log("Error in camera:"+ex.toString());
			} 
			QuizLog.log("Stopping grabber()");
			try { grabber.stop(); } catch(Exception eee)  { }
		}

	}
	public static void main(String[] args) {
	
		for (int i=0; i < 10; i++) QuizVideoRecorder.captureFrame();
		
	}
		
}
