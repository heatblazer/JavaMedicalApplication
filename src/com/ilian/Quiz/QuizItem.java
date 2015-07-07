package com.ilian.Quiz;
import java.util.ArrayList;
import java.util.List;

import com.ilian.Utils.QuizLog;

public class QuizItem  {
	
	

	
	public boolean isSlider = false;
	public boolean isRadio = false;
	public boolean isCanvas = false;
	private String quiz;
	private String condition;
	private String explanation; 
	private String question;
	private String[] answersArray; 
	private String[] xycoordsArray; // added 08.07.2014
	private List<String> answers = new ArrayList<String>();
	private List<String> xycoords = new ArrayList<String>(); //for canvas`s XY coords 
	public void setAnswerTool(boolean sliderOrradioButton) {
		/* TODO implement to create slider or radiobutton */
		/*21.01.2014 */
	}
	public void setQuiz(String q) { quiz = q; }
	public String getQuiz() { return quiz; }
	
	public String[] getItemArrays(String s) {
		if ( s.equals("xycoords") ) return xycoordsArray;
		else if ( s.equals("answers") ) return answersArray;
		else return null;
	}
	public List<String> getItemList(String s) {
		if ( s.equals("xycoords") ) return xycoords;
		else if ( s.equals("answers") ) return answers;
		else return null;
	}
	
	
	public int typeOfAnswer() {
		int ret=0;
		if ( isSlider ) ret = 1;
		if ( isRadio ) ret = 2;
		if ( isCanvas) ret = 3; /* added 02.07.2014 to be tested */
		System.out.println("TYPE IS :"+ret);
		return ret;
	}
	
	public void setCondition(String c) { condition = c; }
	public String getCondition() { return condition; }
	
	public void setExplanation(String e) { explanation = e; }
	public String getExplanation() { return explanation; }
	
	public void setQuestion(String k) { question = k; }
	public String getQuestion() { return question; }
	
	public void addAnswer(String s) { answers.add(s); }
	public void addXYCoord(String s) { xycoords.add(s); }
	
	public String getAnswer(String s) { 
		if ( answers.contains(s) ) { return s; }
		return null;
	}
	public String getXYCoords(int i) {
		if ( xycoords.get(i) != null ) return xycoords.get(i);
		else return null;
	}
	public String getAnswer(int i) { 
		if ( answers.get(i) != null ) return answers.get(i);
		return null;
	}
	
	/* toArray is passed in initQuizesVer2 
	 * 1. create a new integer array by parseInt for xs and ys
	 * this method will be in QCanvas class
	 */
	
	/* new function universal to convert list to array ( specific list to specific array */
	public String[] toArray(final List<String> list, String[] arrayRef, boolean freeList) {
		arrayRef = new String[list.size()];
		for (int i=0; i < list.size(); i++) {
			arrayRef[i] =  list.get(i);
			if ( freeList ) list.remove(list.get(i));
		}
		
		return arrayRef;
	}
	
	public String[] toArray() {
		answersArray = new String[answers.size()];
		for (int i=0; i < answers.size(); i++) {
			answersArray[i] = answers.get(i);
			QuizLog.log(answersArray[i]);
		}
		return answersArray;
	}
	
	public String toString() {
		return "QuizItem[quiz="+quiz+",condition="+condition+",explanation="+explanation+
				",question="+question+"]";
	}
}
