package com.ilian.Quiz;

import java.io.File;

import javax.swing.JFileChooser;

public class QuizFileLoader extends JFileChooser {
	static QuizFileLoader self = null;
	static String absPath = "";
	
	public QuizFileLoader() {
		super(); 
		this.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	}
	public static String showOpenQuizDialog() {
			self = new QuizFileLoader();
			int retVal = self.showOpenDialog(self);
				if ( retVal  == JFileChooser.APPROVE_OPTION ) {
					File file = self.getSelectedFile();
					absPath  =  file.getAbsolutePath();
					QuizXMLReader.readConfig(absPath);
				} else {
					System.exit(0);
				}
		return absPath; 
	}
}
