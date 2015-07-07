package com.ilian.Quiz;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import com.ilian.Utils.ConfigReader;

public class QTimer implements java.io.Serializable {
	
/*TODO 09.01.2014 
 * must add method with QQuestion, QADList and MainApp refs., for showing
 * a QErrorMessage. 
 * Must test if QErrorMessage reference is needed in this class.
 * The anonymous runnable will call QErrorMessage. TODO some tests.
 */
	
	/**
	 * 
	 */
	private static List<Timer> timers = new ArrayList<Timer>();
	private static int timerIndex = 0;
	private static List<QQuestion> questions = new ArrayList<QQuestion>();
	private static MainApp mainApp = null;
	private  QTimer self = this; 
	private static int STATIC_I = ConfigReader.I;
	private static int i = 0;
	public static void resetI() { i = 0; }
	public static volatile boolean anErrorOnTS = false;
	private static Runnable runner = null;
	private static int index;
	private static QQuestion questionRef = null;
	private static QADList qadlistRef = null;
	private static Timer t =  new Timer(1000, new ActionListener() {
	
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			if ( anErrorOnTS ) {
				System.out.println("THERE WAS AN ERROR ON THE SCREEN");
				//((Timer)e.getSource()).stop();
				t.stop();
			} else {
				System.out.println("i is :"+i+" now");
				System.out.println("Question ID: "+questionRef.getCustomID());
				
				if ( i > STATIC_I ) {
					/* TODO create the QErrorMessage 
					 * parametrized ethod 
					 * for now test with ErrorMessage.class
					 * 10.01.2014 - no updates just test
					 */
					System.out.println("TIMER:"+questionRef.getQTextArea().getText());
					if ( 
							QTimer.questionRef.getCustomID() == 2 	||
							QTimer.questionRef.getCustomID() == 3 	||
							QTimer.questionRef.getCustomID() == 4 
						) 
						{
						System.out.println("GREEN WARNING ");
						QTimer.questionRef.initError("GREEN TIMER", QTimer.mainApp, QTimer.qadlistRef, 
								QTimer.questionRef, Color.GREEN,
								new ActionListener() {
								public void actionPerformed(ActionEvent event1) {
									QTimer.resetI();
									QTimer.questionRef.enableAll();
									QTimer.questionRef.getQErrorMessage().closeQErrorFrame();
									QTimer.getTimerInstance().stop();
									QTimer.getTimerInstance().start();
									
								}
						});
						QTimer.questionRef.disableAll();
						QTimer.anErrorOnTS = true;
						i = 0;
					}
						/* add custom logic here */
					if ( questionRef.wasVisitedFromBackButton ) {
							System.out.println("QUESTION WAS VISITED FROM PREVIOUS QUIZ");
					} 
					if ( QTimer.questionRef.getCustomID() == 0 ||
						QTimer.questionRef.getCustomID() == 1 ) {/* FILL IN LATER */
							//Timer error update code here 
//TODO 08.04.2014 
						//	QTimer.questionRef.getQQuestionPanelByName("error").
						//	add(new QErrorMessage(
						//			"TIMER ERROR FRAME",
						//			QTimer.mainApp, QTimer.qadlistRef, null, QTimer.questionRef,
						//			Color.yellow, QTimer.questionRef.getForwardQButton().getActionListeners()[0], null, null)
						//	);
		// 17.04.2014 - reuse initError()
						//02.05.2014 - error fix since there is no
						// backward button in 0-st frame
						//check for null refs and add the proper qbutton
						System.out.println("YELLOW WARNING");
						QButton qb = null;
						try { 
							qb = QTimer.questionRef.getForwardQButton() == null ? 
							QTimer.questionRef.getBackwardQButton() : QTimer.questionRef.getForwardQButton();
						} catch (Exception ex) { }
						QTimer.questionRef.initError(
								"TIMER ERROR FRAME", 
								QTimer.mainApp, 
								QTimer.qadlistRef, QTimer.questionRef, 
								Color.yellow, 
								qb.getActionListeners()[0]);
						QTimer.questionRef.disableAll();
						QTimer.anErrorOnTS = true;
						i = 0 ;
						
					}
				} // ENDIF
				i++; 
			}
		}
	});

	public static Timer getTimerInstance() {
		return t;
	}
	
	
	public static void setQADListItems(MainApp mainapp, QADList qadlist, QQuestion qquestion ) {
		
		QTimer.mainApp = mainapp;
		QTimer.qadlistRef = qadlist;
		QTimer.questionRef = qquestion;

	}
	
	public static void initTimer(final MainApp jf, final QADList qlist, final QQuestion qq,
			final int index, final Runnable run) {
			questionRef = qq;
			qadlistRef = qlist;
			new Thread(run).start();
	}
	
	public static void addAllQuestions(ArrayList<QQuestion> qq) {
		for (int i=0; i < qq.size(); i++) {
			questions.add(qq.get(i));
		}
		System.out.println("All questions added for monitoring");
	}
	
}// END OF CLASS
