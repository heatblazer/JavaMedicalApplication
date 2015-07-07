package com.ilian.Quiz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.ilian.Utils.QuizLog;

public class QErrorMessage extends JPanel /*JFrame*/   {

	/**
	 * 
	 */
	

	private Color[] colors = {Color.yellow, Color.red, Color.white, Color.green, Color.blue };
	
	/* communicator variables */
	private int looper = 50;
	private int colorIndex = 0; 
	private static int errorNumberFrames = 0;
	public boolean isActive = false;
	private boolean used = false; 
	public boolean getUsed() { return used; }
	public void setUsed() { this.used = true; }

	private QErrorMessage self = this;
	public  QErrorMessage getSelf() { return self; }
	
	private  int timesUsed = 0;
	public int timesUsed() { return timesUsed; }
	
	private transient ActionListener timerActionPerformed = null;
	private transient Runnable task = null;
	
	/* explicit event starter for closing */
	@Deprecated
	public void closeQErrorFrame() {
		
			//JFrame
		/*try {
			WindowEvent evclosing = new WindowEvent(self, WindowEvent.WINDOW_CLOSING);
			WindowEvent evclosed = new WindowEvent(self, WindowEvent.WINDOW_CLOSED);
			
			for ( WindowListener L : self.getWindowListeners() ) {
				L.windowClosing(evclosing);
				L.windowClosed(evclosed);
			}
			QuizLog.log(this.toString()+" was used "+timesUsed);
			
			} catch (Exception ex) { 
				QuizLog.log(ex.toString());
			}
			*/
		this.isActive = false;
		this.used = true;
		this.timesUsed++;
		
		QTimer.anErrorOnTS = false;
		
	}
	
	/* special seciton for count down timer */
	

	private String text = null;
	private MainApp jf = null; /* set in constructor */ 
	private JTextArea err = new JTextArea("DEFAULT ERROR FRAME");
	private QADList currentQuiz = null;
	private QButton qb = null;
	private JButton button = null;
	private QQuestion currentQuestion = null;
	private JPanel textPanel = new JPanel(new FlowLayout());
	private JPanel buttonPanel = new JPanel(new FlowLayout());
	/*special constructing methods */
	
	
	public  QErrorMessage init2(String text, MainApp ma, QADList qadlist,  QQuestion qq, Color c, 
			ActionListener action1, ActionListener action2, Runnable task) {
		self = new QErrorMessage(text, ma, qadlist, null, qq, c, action1, action2, task);
		
		return self;
	
	}
	
	
	public QErrorMessage(String text, MainApp ma, QADList qadlist, QButton qb, QQuestion qq, Color c,
			ActionListener action1, ActionListener action2, Runnable task) {
		super();
		this.setFocusable(false); //important bugfix 07.04.2014 
		this.textPanel.setFocusable(false);
		this.buttonPanel.setFocusable(false);
		errorNumberFrames += 1; //depricated since no longer JFrame extension
		this.used = true;
		this.text = text;
		jf = ma;
		if ( this.text != null ) {
			err.setText(this.text); 
		} else {
			err.setText("DEFAULT TEXT");
		}
		err.setLineWrap(true);
		//self.getContentPane().setBackground(c); /JFrame
		self.setBackground(c);
		currentQuiz = qadlist;
		currentQuestion = qq;
		this.qb = qb;
		self.isActive = true; 
		self.jf.thereIsAnErrorFrameOnScreen  = true;
		QTimer.anErrorOnTS = true;
		button = new JButton("<html><center><b>РАЗБРАХ. ПРОДЪЛЖАВАМ.</b></center></html>");
		button.setPreferredSize(new Dimension(220,100));
		button.setBackground(Color.CYAN);
		QuizLog.log("Error Message from question ID#: "+qq.getID());
		QuizLog.log("Times used is: "+self.timesUsed);
		if ( action1 == null ) {
		button.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent ev1) {
					if ( currentQuestion.getQErrorMessage().getUsed() )  {
						QuizLog.log("Error was used - to next quiz");
						/* NEW 14.01.2014 as reported in bug file rep. */
						currentQuestion.getQErrorMessage().setUsed();
						currentQuestion.getQQuestionPanelByName("text").setVisible(true);
						currentQuestion.getQQuestionPanelByName("error").setVisible(false);
						currentQuestion.getBackwardQButton().setVisible(true);
						currentQuestion.getForwardQButton().setVisible(true);
						self.currentQuiz.updateForward(jf);
						self.closeQErrorFrame();
					} else {
						if ( currentQuestion.getCustomID() == 2 ) {
							jf.getQuizes(currentQuiz.getID()).toNextQuiz(jf, 
									jf.getQuizes(currentQuiz.getID()+1));
							currentQuestion.getQErrorMessage().setUsed();
							currentQuestion.getQQuestionPanelByName("text").setVisible(true);
							currentQuestion.getQQuestionPanelByName("error").setVisible(false);
							currentQuestion.getBackwardQButton().setVisible(true);
							currentQuestion.getForwardQButton().setVisible(true);
							self.closeQErrorFrame();
							
						}
					/*NOTE closeQErrorFrame() possible to be deprecated */
						if ( currentQuestion.getSlider() != null  ) {
							currentQuestion.getSlider().setVisible(true);
							currentQuestion.getBackwardQButton().setVisible(true);
							currentQuestion.getForwardQButton().setVisible(true);
							currentQuestion.getQQuestionPanelByName("text").setVisible(true);
							currentQuestion.getQQuestionPanelByName("error").setVisible(false);
							currentQuestion.getQErrorMessage().setUsed();
							currentQuiz.updateForward(jf);
							self.closeQErrorFrame(); 
							
						}
						if ( currentQuestion.getCustomID() == 1 ) {
							currentQuestion.getQErrorMessage().setUsed();
							currentQuestion.getQQuestionPanelByName("text").setVisible(true);
							currentQuestion.getQQuestionPanelByName("error").setVisible(false);
							currentQuestion.getBackwardQButton().setEnabled(true);
							currentQuestion.getForwardQButton().setEnabled(true);
							self.currentQuiz.updateForward(jf);
							self.closeQErrorFrame();
						}
						/* else all */
						currentQuestion.getQErrorMessage().setUsed();
					} // END IF 
				}
		});
		} /* if there is no specific action listener added */ 
		else {
			/* add specific action per button */ 
			button.addActionListener(action1);	
		}
		this.timerActionPerformed = action2;
		this.task = task;
		this.setPreferredSize(new Dimension(currentQuestion.getQTextArea().getWidth(),  
				currentQuestion.getQTextArea().getHeight()));
		this.setBounds(0,0,  currentQuestion.getQTextArea().getWidth(),  
				currentQuestion.getQTextArea().getHeight());
		//this.setResizable(false); //JFrame
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		err.setForeground(Color.black);
		err.setPreferredSize(new Dimension(200, 100));
		err.setColumns(25);
		err.setRows(6);
		err.setEditable(false);
		err.setFocusable(false); 
		err.setBorder(null);
		//Deprecated - old feb.2014 
		//this.setAlwaysOnTop(true); //JFrame
		//this.setUndecorated(true); //JFrame	
		this.setLayout(new FlowLayout());
		textPanel.add(err); 
		buttonPanel.add(button);
		this.add(textPanel);
		this.add(buttonPanel);
		this.setVisible(true);
		
}
	

	private  void countDown(final int times) {
		if ( this.timerActionPerformed == null ) {
		new Timer(times, new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				((Timer)ev.getSource()).stop();
				/* add some changes to error message */
				self.blinkFrame(30);
				err.setText("THIS WILL TERMINATE IN 20 SEC.");
			//	self.getContentPane().setBackground(Color.pink); //JFrame
				self.setBackground(Color.pink);
				new Timer(times, new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						((Timer)ev.getSource()).stop();
						/* add some changes to error message */
/* OK - working - 08.04.2014 ========================================*/
						if ( self.isActive ) {
							if ( self.task == null ) {
								synchronized ( this ) {
									/* if timer runs out  - go to last exit quiz */
									/* Depricated  - TODO add custom logic here */
									/*jf.getQuizes(currentQuiz.getID()).toNextQuiz(jf, 
										jf.getQuizes(jf.quizesLength()-1));
										*/
								self.button.doClick();
								//self.closeQErrorFrame();
								}
							} else {
								synchronized ( this ) {
									new Thread(self.task).start();
								}
							}
						}
					}
				}).start();
			}
		}).start();
		} else {
			new Timer(times, this.timerActionPerformed).start();
		}
	
	}
	
/* needed default constructor explicitly */ 	
	public QErrorMessage() { 
		/* construct the reference */
		self = this; 
	}
	
	
private void blinkFrame(final int times) {
		
		new Timer(times, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if ( self.looper > 0 ) {
				System.out.println("From second ErrorFrame(), event");
				self.err.setBackground(colors[colorIndex]);
				self.textPanel.setBackground(colors[colorIndex]);
				self.buttonPanel.setBackground(colors[colorIndex]);
				//self.getContentPane().setBackground(colors[colorIndex]); //JFrame
				self.setBackground(colors[colorIndex]);
				//self.getContentPane().revalidate(); //JFrame
				self.revalidate();
				self.repaint();
				if ( self.colorIndex < 3 ) {
					colorIndex++;
				} else { 
					colorIndex = 0; 
				}
				self.looper--;
				} else {
					looper = 30; 
					self.err.setBackground(Color.RED);
					self.textPanel.setBackground(Color.RED);
					self.buttonPanel.setBackground(Color.RED);
				//	self.getContentPane().setBackground(Color.RED); //JFrame
					self.setBackground(Color.RED);
				//	self.getContentPane().revalidate(); //JFrame
					self.revalidate();
					self.repaint();
					((Timer)e.getSource()).stop();
				}
			} 
		}).start();
	}
	
	
}// END OF CLASS 