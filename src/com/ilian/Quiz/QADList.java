package com.ilian.Quiz;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.ilian.Utils.QuizLog;

/*TODO from 10.01.2014
 * add restarting i-variable from QTimer static class on every screen update.
 * Must be called after validate() and repaint(). 
 * Must be tested for syncrhonization issues (low priority)
 * 
 * 
 */
public class QADList  {
	
	
	static int _ID = 0;
	private int ID = 0;
	
	public int getID() { return ID; }
	
	private boolean isLast = false;
	
	public boolean isVisitedFromPreviousQuiz = false;
	public boolean isFirst = false;
	public boolean getLast() { return isLast; }
	public void setLast(boolean tf) { isLast = tf; }
	/*times vistited checker */
	int timesVisited = 0;
	public void nullVisits() { timesVisited = 0; }
	public void nullAll(int range) {
		for (int i=0; i < range; i++ ) {
			questions.get(i).getBackwardQButton().isUsed = false;
		}
		questions.get(4).getForwardQButton().isUsed = false;
		timesVisited = 0; 
		
	}
	public void increaseVisit() { timesVisited++; }
	public int getVisited() { return timesVisited; }
	private QADList self = this;
	private transient MainApp jf;
	private transient QErrorMessage qerror = null;
	ArrayList<QQuestion> questions = new ArrayList<QQuestion>();
	public int getQuestions() { return questions.size(); }
	
	
	int index = 0; 
	public QADList getSelf() { return self; }
	public QADList() {
		
	}
	
	public String toString() {
		return "Quiz ID:"+ID; 
	}
	
	/* INNER ADDER CLASS */
	public static class Builder implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 992383686755607956L;
		private int index = 0;
		
		ArrayList<QQuestion> questions = new ArrayList<QQuestion>();
		public Builder addQuestion(QQuestion q) {
			this.questions.add(q);
			return this;
		}
		public QADList build() {
			return new QADList(this);
			
		}
		
		
	}
	private QADList(Builder b) {
		ID = _ID;
		_ID++; 
		
		this.index = b.index; 
		for (int i=0; i < b.questions.size(); i++ ) {
			this.questions.add(b.questions.get(i));
		}
	}

	public QSlider getCurrentSlider() {
		return questions.get(index).getSlider();
	}
	
	
public void updateForward2(final MainApp jf) {
		
		jf.getContentPane().remove(questions.get(index));
		questions.get(index).isActive = false;
		questions.get(index).getQErrorMessage().closeQErrorFrame();
		self.moveForward();
		jf.getContentPane().add(questions.get(index));
		questions.get(index).isActive = true; 
		QTimer.resetI();
		QTimer.anErrorOnTS = false;
		QTimer.setQADListItems(jf,  self,  questions.get(index));
		QTimer.initTimer(jf, self, questions.get(index), index, 
				new Runnable() {
			public void run() {
				if ( QTimer.getTimerInstance().isRunning() ) {
					QTimer.getTimerInstance().restart();
			
				} else {
					QTimer.getTimerInstance().start();
				
				}
			}
		});
		/* update graphics */
	
}
	
	public void updateForward(final MainApp jf) {
		
		jf.getContentPane().remove(questions.get(index));
		questions.get(index).getQErrorMessage().closeQErrorFrame();
		questions.get(index).isActive = false;
		questions.get(index).enableAll();
		moveForward();
		jf.getContentPane().add(questions.get(index));
		questions.get(index).isActive = true; 
		
		/* update graphics */
		jf.setCurrentQuestion(questions.get(index));	
		jf.updateScreen();
	/*	QSerialize.serialize((QQuestion) questions.get(index), 
				"currentQuestion");
	*/	
		QTimer.anErrorOnTS = false;
		QTimer.getTimerInstance().stop();
		
		QTimer.setQADListItems(jf,  self,  questions.get(index));
		QTimer.resetI();
		QTimer.getTimerInstance().start();
	}
	
	public void updateToLast(MainApp jf, QQuestion endQuestion) {
		jf.getContentPane().remove(questions.get(index));
		moveForward();
		jf.getContentPane().add(endQuestion); 
		
	}
	
	/* method for manipulating the timer 
	 * using Thread and Runnable - not timer
	 * 
	 */
	/* OK - working */ 
	/* IMPORTANT - ADD NOTIFY QTimer 
	 * 
	 */
	@Deprecated
	public void updateBackwardFromQE(MainApp jf, QADList next) {
		try {
			jf.getContentPane().remove(questions.get(index));
			rewind();
			jf.globalIndex--;
			jf.getContentPane().add(questions.get(index));
			QTimer.anErrorOnTS = false;
			QTimer.resetI();
			QTimer.setQADListItems(jf, self, questions.get(index));
			
			
		} catch ( Exception ex) {
			QuizLog.log("updateBackwardFromQE()");
			QuizLog.log(ex.getMessage());
		}
	}
	
	public void updateBackwardSpecial(final MainApp jf, boolean isSpecialCase, boolean update) {
		try {
			jf.getContentPane().remove(questions.get(index));
			questions.get(index).isActive = false;
			rewind();
			jf.getContentPane().add(questions.get(index));
			questions.get(index).isActive = true;
			if (update) jf.updateScreen();
			QTimer.anErrorOnTS = false;
			QTimer.resetI();
			QTimer.setQADListItems(jf,  self, questions.get(index));
			
		} catch (Exception exx) { 
		
		}
	}
	public void updateBackward(final MainApp jf, boolean isSpecialCase, boolean update) {
		try {
			if ( !questions.get(index).getBackwardQButton().isUsed  )  {
					jf.getContentPane().remove(questions.get(index));
					questions.get(index).isActive = false;
					questions.get(index).getBackwardQButton().useButton();
					rewind(); 
					jf.getContentPane().add(questions.get(index));
					questions.get(index).isActive = true;
					if ( update ) jf.updateScreen();
					QTimer.anErrorOnTS = false;
					QTimer.setQADListItems(jf, self, questions.get(index));
					QTimer.resetI();

			} else {
				/* PRINT ERROR MESSAGE */
				/* added update to next quiz and to prev quiz works on first button still
				 * has to add to second button 
				 * TODO the 2nd parameter must be an INT to get the index and to copy the button
				 * or rework the logic to some AI to understend which button must be pressed and 
				 * which one - not 
				 * 
				 */
				/* TODO TEST  - MOVE IT TO MainApp class */ 
				/* 08.04.2014 - unnedeed since QErrorMessage is JPanel */
				if ( QTimer.getTimerInstance().isRunning() ) {
					QTimer.anErrorOnTS = false;
				}
				
				if ( isVisitedFromPreviousQuiz ) {
					/* IPORTANT FIX */
					/*TODO - CLIENT TESTING */
					jf.getContentPane().remove(questions.get(index));
					questions.get(index).isActive = false;
					questions.get(index).getBackwardQButton().useButton();
					rewind(); 
					jf.getContentPane().add(questions.get(index));
					questions.get(index).isActive = true;
				}
				
/*=======================================> TODO */
				/* added to 27.03.2014 - working */
				/*03.04.2014 - added some functionalyty to error frames
				 * now properly hides/unhides an error, then starts timers
				 * 
				 */
/*======================================> TODO */
				/*added 03.04.2014 - checks and error frames types 
				 * also review GUI logic per errors
				 */
				if ( !isSpecialCase && index  < 4) {
					QuizLog.log("QADList::updateBackward() : if no special case or frame index is < 4 - noraml red error.");
					/*
					questions.get(index).initError("ВНИМАНИЕ!Този бутон е ползван.",
							jf,  self, questions.get(index), Color.red, null);
					*/
					questions.get(index).initError("ВНИМАНИЕ! Този бутон е ползван.", 
							jf, self, questions.get(index), Color.red, 
							new ActionListener() {
								public void actionPerformed(ActionEvent e11) {
										questions.get(index).enableAll();
										questions.get(index).getQQuestionPanelByName("error").remove(
										questions.get(index).getQErrorMessage());
										
										self.updateForward(jf);
							}
					});
/* 07.04.2014 - WIP here */
					if ( questions.get(index).getSlider() != null ) {
						questions.get(index).getSlider().setVisible(false);
					}
					if ( questions.get(index).getQRadioButton() != null ) {
						questions.get(index).getQRadioButton().setVisible(false);;
					}
					questions.get(index).getQQuestionPanelByName("text").setVisible(false);
					/*string, MainApp, QADlist, QButton, QQuestion, Color, ActionListener 1, AL 2(optional)
					Runnable - task */
					jf.updateScreen();
				}
				/* unchanged */
				if ( index == 4 && questions.get(4).getBackwardQButton().isUsed ) {
					QuizLog.log("QADLIst::updateBackward() if back button is used - change back button");
					
					questions.get(4).getQTextArea().setText("SOME NEW TEXT");
					questions.get(4).getBackwardQButton().setColor(Color.pink);
					questions.get(4).getBackwardQButton().setText("SOME NEW TEXT");
					questions.get(4).getBackwardQButton().setActionL(	
						new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								try {
									QADList next = (jf.getQuizes(self.ID+1) == null) ? null : jf.getQuizes(self.ID+1);
									self.toNextQuiz(jf, next);
								
								} catch (Exception ex) { 
		
								}
							
							}
						});  // test 1	
					}
				
				if ( isSpecialCase && questions.get(2).getBackwardQButton().isUsed ) {
					QuizLog.log("QADList::updateBackward() : special case - light blue frame.");
/*TODO -- blue error update to next->next quiz */
					System.out.println("QADLIST+2 testing");
					questions.get(index).getQQuestionPanelByName("error").add(
							new QErrorMessage("QADLIST+2", jf, self, null, questions.get(index), Color.cyan,
							new ActionListener() {
						public void actionPerformed(ActionEvent e11) {
							
							questions.get(index).getForwardQButton().setVisible(true);
							questions.get(index).getBackwardQButton().setVisible(true);
							questions.get(index).getQQuestionPanelByName("text").setVisible(true);
							questions.get(index).getQQuestionPanelByName("error").remove(
									questions.get(index).getQErrorMessage());
							try {
								// try-catch needed here 
								QADList next_next = jf.getQuizes(self.ID+2) == null ? null : jf.getQuizes(self.ID+2);
								self.toNextQuiz(jf,  next_next);	
							} catch ( Exception ex ) {  
								QuizLog.log("QADList::updateBackward()->actionPeformed:"+ex.getMessage());
							}
						}
					}, null, null));	
					questions.get(index).getForwardQButton().setVisible(false);
					questions.get(index).getBackwardQButton().setVisible(false);
/* 07.04.2014 - WIP here */
					if ( questions.get(index).getSlider() != null ) {
						questions.get(index).getSlider().setVisible(false);
					}
					if ( questions.get(index).getQRadioButton() != null ) {
						questions.get(index).getQRadioButton().setVisible(false);;
					}
					questions.get(index).getQQuestionPanelByName("text").setVisible(false);
					/*string, MainApp, QADlist, QButton, QQuestion, Color, ActionListener 1, AL 2(optional)
					Runnable - task */
					jf.updateScreen();
					/* Deprecate code below */
					}
				}
		} catch (Exception ex1) {  
			QuizLog.log("QADList::updateBackward() - try-catch block error:"+ex1.getLocalizedMessage());
		}
		
		/* end update backward */
	}

	
	public void updateToQuestion(MainApp jf, int qindex) {
		try {
			if ( !questions.get(index).getBackwardQButton().getUsed() ) {
				jf.getContentPane().remove(questions.get(index));
				questions.get(index).isActive = false;
				jf.getContentPane().add(questions.get(qindex));
				questions.get(qindex).isActive = true;
				questions.get(qindex).wasVisitedFromBackButton = true;
				QTimer.anErrorOnTS = false;
			} else {
				
				/* PRINT ERROR MESSAGE */
				/* added update to next quiz and to prev quiz works on first button still
				 * has to add to second button 
				 * TODO the 2nd parameter must be an INT to get the index and to copy the button
				 * or rework the logic to some AI to understend which button must be pressed and 
				 * which one - not 
				 * 
				 */
				if ( QTimer.getTimerInstance().isRunning() ) {
					QTimer.anErrorOnTS = true;
				}
				questions.get(index).initError("ВНИМАНИЕ!Този бутон е ползван.",
						jf,  self, questions.get(index), Color.yellow, null);
			}
		} catch (Exception ex1) {  
			QuizLog.log(ex1.getMessage());
		}
	}

	
	public void toNextNextQuiz() { 
		/* do nothing for now??? */
	}
	
	public void toPreviousQuizToQuestion(MainApp jf, QADList prev) {	
		/* MainApp will check if wasVisitedFromBackButton 
		 * then will add warning message with specifin button 
		 * action 
		 */
		jf.getContentPane().remove(questions.get(index));
		prev.getQuestion(2).wasVisitedFromBackButton = true;
		/* 14.01.2014
		 * OK. nullify all button isUsed variables to button is not used */
		/* also add special case that previous question was visited from
		 * another frame 
		 */
		prev.nullAll(5);
		prev.updateBackward(jf, true, false);
		jf.glbalIndexQuiz--;
		
	/* 15.01.2014 - changed logic no more update to answer frame */
		//prev.updateForward(jf);
		//prev.updateForward(jf);
		jf.updateScreen();
	}
	
	
	public void toPreviousQuiz(MainApp jf, QADList prev)   {
		//Implement it TODO !!!
		if ( prev == null ) { 
			
			System.out.println("NULL REF");
		
		}
		else {
			prev.nullVisits(); 
			jf.getContentPane().remove(questions.get(index));
		//	prev.updateBackward(jf, true, false);
			prev.updateBackwardSpecial(jf, false, true);
			jf.updateScreen();
		
		}
	}
	
	
	public void toNextQuiz(MainApp jf, QADList next) {
		if ( next == null ) {
			/* ommit for now */
			
		} else {
			next.nullVisits(); 
			
			jf.getContentPane().remove(questions.get(index));
			
			next.updateBackwardSpecial(jf,  false,  true);
			//jf.getContentPane().add(self.getQuestion(0));

			
			//jf.getContentPane().add(next.getQuestion(next.getIndex()));
		//	jf.getContentPane().add(next.getQuestion(0));
		//	jf.getContentPane().revalidate();
		//	next.updateBackward(jf, false, true);
		//	QTimer.setQADListItems(jf, self, self.getQuestion(0));
			
		
		} //else important here 
	}
		
	/* INDEX IS PRIVATE VAR TO ALL HERE */

	/*optional method */
	public void moveForwardAndRepeat() {
		
		if ( index >= questions.size()-1 ) {
			index = 0;
		} else {
			index++;
		}
	}
	
	private void moveForward() {
		QuizLog.log("QADList::moveForward()");
		if ( index >= questions.size()-1 ) {
			index = questions.size()-1;
			
		} else { 
			index++; 
		}
	}
	
	private void rewind() {
		QuizLog.log("QADList::rewind()");
		if ( index == 4 ) {
			questions.get(index).getBackwardQButton().isUsed = true;
			questions.get(index).getForwardQButton().isUsed = true;
			questions.get(1).getBackwardQButton().resetUses();
			questions.get(2).getBackwardQButton().resetUses();
			questions.get(2).getSlider().answerCounter = 0;
			// new
			
		} /* deprecate to forbid access to last frame */
		if ( index == 3) { 
			/* TODO - confirm usage or just warp to next??? */
			questions.get(index).getBackwardQButton().isUsed = true; //update back button 
			questions.get(1).getBackwardQButton().resetUses();
			questions.get(2).getBackwardQButton().resetUses();
			questions.get(2).getSlider().answerCounter = 0;
			//new
		} 
		if ( index == 2 ) {
			questions.get(index).getBackwardQButton().isUsed = true; 
			questions.get(1).getBackwardQButton().resetUses();
			questions.get(2).getSlider().answerCounter = 0;
		} 
		if ( index == 1  ) {
			questions.get(index).getBackwardQButton().isUsed = true;
			questions.get(2).getSlider().answerCounter = 0;
		}
		index = 0;
		/*optional */
		questions.get(index).increaseVisit();
		
	}
	
	public int getIndex() { return index; }
	
	public QQuestion getQuestion(int i) {
		if ( questions.get(i) != null ) { return questions.get(i); }
		else return null;
	}
	
	
/* NEW foo() added 08.04.2014 - Error init with programmable interface */
	
	
}
