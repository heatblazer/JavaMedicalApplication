package com.ilian.Quiz;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

import com.ilian.Utils.ConfigReader;
import com.ilian.Utils.CustomAlgorithms;
import com.ilian.Utils.QuizLog;


public class MainApp extends JFrame implements java.io.Serializable {
	
	/*TODO - 472 line */
	/**
	 * 
	 */
	/*variables for serialization and deserialization */
	private  QErrorMessage pauseWarn = new QErrorMessage();
	private static UIDefaults defaults = UIManager.getDefaults();
	
	/* for HWM and LWM checks */
	private  boolean a = true;
	private  boolean b = true; 
	
	
	public static boolean readFromSave = false; //deprecation
	public int globalIndex = 0; /* current question */
	public int glbalIndexQuiz = 0; /* current quiz */
	public void setCurrentQuestion(QQuestion qq) {
		self.current = qq;
	}
	public void updateScreen() {
		self.getContentPane().validate();
		self.repaint();
	}
	/*singleton */
	private static MainApp INSTANCE = new MainApp();
	public static MainApp getInstance() { 
		return INSTANCE;
	}
	
	private Robot robi; //for mouse clicks
	public boolean thereIsAnErrorFrameOnScreen = false;
	
	private Toolkit tk = Toolkit.getDefaultToolkit(); 
	private int WIDTH = 800;
	private int HEIGHT = 600;
	static boolean isRunning = false; 
	
	 ArrayList<QADList> quizes = new ArrayList<QADList>();
	List<QQuestion> questions = new ArrayList<QQuestion>();
	private static  boolean serialized = false;
	public void setQuizes(ArrayList<QADList> q) {
		quizes = q;
	}
	QQuestion current = null;
	public QQuestion getCurrent() { return current; }
	QADList qadlistref ; 
	

	private void fillQuestions() {
		new Thread(new Runnable() {
			
			public void run() {
				QuizLog.log("MainApp::fillQuestion() - populating optional list of questions");
				for (int i=0; i < quizes.size() ; i++) {
					for (int j =0; j < quizes.get(i).getQuestions(); j++ ) {
						questions.add( quizes.get(i).getQuestion(j));
					}
				}
				QuizLog.log("MainApp::fillQuestion() - done");
			}
		}).start();
	}
	
	public int quizesLength() { return quizes.size(); }
	public QADList getQuizes(int i) {  
		try {
			return (quizes.get(i)) == null ? null : quizes.get(i);
		} catch ( Exception ex ) {
			return null;
		}
		
	}
	private static int lnf = 0; 
	private static List<QuizItem> quizItems = new ArrayList<QuizItem>();
	/* INIT ALL */	
	/* 03.12.2013 - NOTE
	 * initQuizesVer2 must take an argument to signalize if the quiz is first or is last. 
	 * The parameter will set the quiz last frame NOT to have "po-preden vupros" button 
	 * so "ArrayOutOfBounds" exceptions won`t be trown
	 * TODO! 
	 */
	private void startMonitoring() {
		QuizLog.log("MainApp::startMonitoring() - init monitoring of QQUestions active state");
		new Thread(new Runnable() {
			public void run() {
				
				while ( true ) {
					for (int i=0; i < questions.size(); i++) {
						if ( questions.get(i).isActive ) {
							QuizLog.log("Question ID:"+questions.get(i).getID()+" is active");
						} else {
							QuizLog.log("Question ID:"+questions.get(i).getID()+" is inactive");
						}
					}
				}
			}
		}).start();		
	}
	/*init all items and serializa the quizes array */
	private   void init() {
			QuizLog.log("MainApp::init() - initing main program");
			synchronized (this) {
				quizItems = QuizXMLReader.getQItems() ;
			}
		
			for ( QuizItem Item : quizItems ) {
				 //TODO Deprecated since composition is lost
				System.out.println("Entering Item iterrator");
				System.out.println("Item is CANVAS: "+Item.isCanvas+"Item is SLIDER:"+Item.isSlider
						+"Item is Radio:"+Item.isRadio);
				if ( Item.isCanvas ) { 
					System.out.println("CANVAS DETECTED");
					System.out.println("Items are "+Item.getItemList("xycoords").toString());
					initQuizesVer2(self, Item.getExplanation(), Item.getQuestion(),
							Item.toArray(Item.getItemList("xycoords"), 
									Item.getItemArrays("xycoords"), false), null, 3, true);
				 
				} else {
				initQuizesVer2(self, Item.getExplanation(), Item.getQuestion(), 
						Item.toArray(), null, Item.typeOfAnswer(), false);
				}
			}
			/* populate the list with all the questions
			 * monitor all questions for activity, 
			 * 
			 */
			/* speciall section for monitoring the acitviti vars 
			 * added 07.01.2014 
			 */
			self.fillQuestions();
			QTimer.addAllQuestions((ArrayList<QQuestion>) questions);
			
//		self.startMonitoring();
		
		/* end monitor seciton */
		
		initQuizesVer2(self, "УСПЕЩНО ЗАВЪРШИХТЕ ПРОГРАМАТА", "", new String[] {"","",""}, null,0,false); 
		quizes.get(quizes.size()-1).setLast(true);
		quizes.get(quizes.size()-1).getQuestion(0).getBackwardQButton().addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent eeee) {
						WindowEvent e = new WindowEvent(self, WindowEvent.WINDOW_CLOSING);
						WindowEvent e1 = new WindowEvent(self, WindowEvent.WINDOW_CLOSED);
						
						for (WindowListener L: self.getWindowListeners() ) {
							L.windowClosing(e);
							L.windowClosed(e1);
						}
					}
				});
		quizes.get(quizes.size()-1).getQuestion(0).getBackwardQButton().setText("ИЗХОД");
		quizes.add(null);
		QuizLog.log("MainApp::init() - done initing main program");
	} /* END INIT ALL */
	
	/* Variable field */
	int indexG=-1;
	MainApp self = this;
	public static MainApp getSelf() { return INSTANCE; }
	public boolean getIsRunning() { return isRunning; }
	public void setRunning(boolean tf) { isRunning = tf; }
	
	/* better INIT QUIZES VERSION FROM 15.11.2013 */
	public void initQuizesVer2(final MainApp ma, String explanation, String question, 
			String[] answers, QADList optlist, int typeOfAnswer, boolean isCanvasActive) 
		
	{
		//quizes list add many quizes 
		QuizLog.log("MainApp::initQuizesVer2() - populate QADList array with QQuestion`s Builders");
		indexG++; 
		/*ELSE*/
		final int index = this.indexG; 
	
		/* TODO - work on QuizVIsitor and set logics */
		self.qadlistref = new QADList.Builder().addQuestion(new QQuestion.Builder()
				.setCustomId(0)
				.setButtons(null, 
				new QButton.Builder().setButtonSize(300, 100).setColor(new Color(35, 211, 109)).setButtonText("КЪМ ВЪПРОСА")
				.setListener(new MouseListener() {

					@Override
					public void mouseClicked(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void mouseEntered(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void mouseExited(MouseEvent arg0) {
						// TODO Auto-generated method stub	
					}
					@Override
					public void mousePressed(MouseEvent arg0) {
						// TODO Auto-generated method stub	
					}
					@Override
					public void mouseReleased(MouseEvent arg0) {
						// TODO Auto-generated method stub
					}
				})
				.setAction(
						new ActionListener() {
						public void actionPerformed(ActionEvent ev1) {
							/* TAKE FOTO */
							new Thread(new Runnable() {
								
								public void run() {
									QuizVideoRecorder.captureFrame();
								}
								}).start(); 
							
							if ( quizes.get(index) != null ) {
								
								quizes.get(index).updateForward(ma);
								
							} else {  
							}
						}
						}).build())
					
						.setQTextArea(new QTextArea.Builder().setTextArea(explanation
						, false, 10, 30, null ,Color.white, Color.BLUE, false).build())
						.setFirstLast(true,  false)
						.setQSlider(null).build())
		.addQuestion(new QQuestion.Builder()
				.setCustomId(1)
				.setButtons(  
				new QButton.Builder().setButtonText("ГОТОВ СЪМ ДА ОТГОВОРЯ")
				.setButtonSize(300, 100).setColor(new Color(255, 255,0))
				.setAction(
						new ActionListener() {
							public void actionPerformed(ActionEvent ev) {
								/* TAKE FOTO */
								new Thread(new Runnable() {
									
									public void run() {
									QuizVideoRecorder.captureFrame();
									}
									}).start(); 
									
								if ( quizes.get(index) != null ) {	
									quizes.get(index).updateForward(ma);
									quizes.get(index).getQuestion(2).getSlider().printValues();
									
								} else { }
							}
						}).build(),
				new QButton.Builder().setButtonText("НАЗАД КЪМ ПОЯСНЕНИЕТО")
				.setButtonSize(300,  100).setColor(new Color(0, 255, 0))
				.setAction(	new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {

								/* TAKE FOTO */
								new Thread(new Runnable() {
									
								public void run() {
									QuizVideoRecorder.captureFrame();
									}
								}).start(); 
								
								if ( quizes.get(index) != null ) {
									quizes.get(index).updateBackward(ma, false, true);
								} else { }
							}
				}).build())
				.setQTextArea(
				new QTextArea.Builder().setTextArea(question
						, false, 10, 30, null ,Color.white, Color.BLUE, false).build())
				.setFirstLast(false,  false).build())
		.addQuestion(new QQuestion.Builder()
		.setCustomId(2)
		.setTypeOfAnswer(typeOfAnswer)
		.setQCanvas(new QCanvas.Builder(isCanvasActive).setXCoords(answers[0])
				.setYCoords(answers[1]).setRectangles().build())
		.setQRadioButton(new QRadioButton.Builder().setRadioButtons(answers).build()) 
		.setQSlider(new QSlider.Builder().setSizeXY(tk.getScreenSize().width, 50).setStartEndPoints(0, 
				tk.getScreenSize().width)
				.addChangeL(new ChangeListener() {
					@Override
					/* SLIDER CODE BLOCK */ 
					public void stateChanged(ChangeEvent arg0) {
						/* important fix 14.02.2014
						 * if-else must check if the answerCounter is exactly 3 or else
						 * additional error messages wll be produced  
						 */
						
						quizes.get(index).getQuestion(2).getSlider().addSliderValue(
								quizes.get(index).getQuestion(2).getSlider().getValue() 
								);
						if ( quizes.get(index).getQuestion(2).getSlider().answerCounter == 3 &&
								!self.thereIsAnErrorFrameOnScreen ) {
								quizes.get(index).getQuestion(2).
									initError("DARK BLUE", self, quizes.get(index), 
											quizes.get(index).getQuestion(2), 
											new Color(139, 11, 224), null);
							
						}
					}
				}) 
				.addMotionL(new MouseMotionListener() {

					@Override
					public void mouseDragged(MouseEvent arg0) {
						// TODO Auto-generated method stub
						/* disable screen capture */
						//QuizScreenCapture.setIsPressed(true);
					
						quizes.get(index).getQuestion(2).getSlider().setValue(arg0.getX());
						/* added 14.02.2014 - Loveday huh :) We, code!
						 * HWM and LWM algorithms to track movement */
						a = CustomAlgorithms.HWM(quizes.get(index).getQuestion(2).getSlider().getValue(), -40);
						b = CustomAlgorithms.LWM(quizes.get(index).getQuestion(2).getSlider().getValue(), 40);
						
						if (  b ) {
							System.out.println("OK - MOVING LEFT [a:"+a+"][b:"+b+"]");	
								quizes.get(index).getQuestion(2).getSlider().answerCounter++;
								
						}
						if (  a  ) {
							System.out.println("OK - MOVING RIGHT [a:"+a+"][b:"+b+"]");
							quizes.get(index).getQuestion(2).getSlider().answerCounter++; 
						}
					}
					@Override
					public void mouseMoved(MouseEvent arg0) {
						// TODO Auto-generated method stub
						quizes.get(index).getQuestion(2).getSlider().setValue(arg0.getX());
						/*
						boolean a = CustomAlgorithms.HWM(quizes.get(index).getQuestion(2).getSlider().getValue(), -60); 
						boolean b = CustomAlgorithms.LWM(quizes.get(index).getQuestion(2).getSlider().getValue(), 60) ;
						if ( a )
						{	if ( CustomAlgorithms.LWM(quizes.get(index).getQuestion(2).getSlider().getValue(), 60) ) {
								System.out.println("OK _ INC AND DEC");
								quizes.get(index).getQuestion(2).getSlider().answerCounter++;
							}
							
						} */
					}		
				})
				
				.addMouseL(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						// TODO Auto-generated method stub
						/* disable screen capture */
					//	quizes.get(index).getQuestion(2).getSlider().setValue(arg0.getX());
						//QuizScreenCapture.setIsPressed(true); 
				//		MouseEvent me = new MouseEvent(quizes.get(index).getQuestion(2).getSlider(),
				//			0,0,0,100,1, index, false);
				//		for (MouseListener ml : quizes.get(index).getQuestion(2).getSlider().getMouseListeners()) {
				//			ml.mouseReleased(me);
				//		}
						
					}

					@Override
					public void mouseEntered(MouseEvent arg0) {
						/*disable screen capture */
						//QuizScreenCapture.init();
						//QuizScreenCapture.setIsPressed(false);
						/* OBJECT VARIABLE PER QQUESTION */
						/* TODO Add slider change listener from here */
					
					
					}
					@Override
					public void mouseExited(MouseEvent arg0) {
						/* TODO - OK 10.01.2014 */
						new Thread(new Runnable() {
							public void run() {
								try {
									Thread.currentThread().sleep(200); 
								} catch (Exception ex) { 
									
								} finally {
									quizes.get(index).getQuestion(2).getSlider().answerCounter++;
									Thread.currentThread().interrupt();
								}
							}
						}).start();
						
				//		MouseEvent me = new MouseEvent(quizes.get(index).getQuestion(2).getSlider(),
				//				0,0,0,100,1, index, false);
				//		for (MouseListener ml : quizes.get(index).getQuestion(2).getSlider().getMouseListeners()) {
				//			ml.mouseReleased(me);
				//		}
						
					}

					@Override
					public void mousePressed(MouseEvent arg0) {
						// TODO Auto-generated method stub
					//	QuizScreenCapture.setIsPressed(true);
						
						JSlider sourceSlider = (JSlider) arg0.getSource();
						BasicSliderUI ui = (BasicSliderUI) sourceSlider.getUI();
						int value = ui.valueForXPosition(arg0.getX());
						quizes.get(index).getQuestion(2).getSlider().setValue(value);
						
					}
					@Override
					public void mouseReleased(MouseEvent arg0) {
						// TODO Auto-generated method stub
						//disable screen capture
			
						//QuizScreenCapture.setIsPressed(false);
						quizes.get(index).getQuestion(2).getSlider().answerCounter++;

							
					}
				}).addTable(answers).build())
			
				.setButtons( 
				new QButton.Builder().setButtonText("ПРИКЛЮЧИХ С ОТГОВОРА")
				.setButtonSize(300,  100).setColor(new Color(255,0, 100))
				.setAction(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						
						/*TODO 10.01.2014 update to next next quiz
						 * 
						 */
						if ( quizes.get(index).getQuestion(2).wasVisitedFromBackButton ) {
							try {
								final QADList next_next = (quizes.get(index+2)) == null ? 
										null : quizes.get(index+2);
/*TODO change the error frame to error panel added to
 * the new program logic
 * 11.04.2014  */
								/* TODO - deprecate the block */
								// BEGIN
								quizes.get(index).getQuestion(2)
								.initError("YOU WILL BE REDICRECTED TO QADLIST+2", 
										self, quizes.get(index), 
										quizes.get(index).getQuestion(2), Color.CYAN, 
										new ActionListener() {
										/* update to quiz + 2 */
											@Override
											public void actionPerformed(
													ActionEvent arg0) {
												quizes.get(index).toNextQuiz(self,  next_next);
												quizes.get(index).getQuestion(2).getQErrorMessage().closeQErrorFrame();
											}
								});
							// END 
/* --------------------------------------------------------------------------------------------------- */
							} catch ( Exception ex) {
								//fill in later if needed 
							}
						} else if ( quizes.get(index) != null ) {
							quizes.get(index).updateForward(ma);
							QuizScreenCapture.record(); /* OK RECORD */
						} else { 
							/* FILL IN LATER */
						}
					}
				}).build(),
				new QButton.Builder().setButtonText("ИСКАМ ДА ВИДЯ ОТНОВО ВЪПРОСА")
				.setButtonSize(300,  100).setColor(new Color(255, 255, 0))
				.setAction(new ActionListener() {
/*TODO FIX LOGIC ====================================================================>*/
					public void actionPerformed(ActionEvent e) {
					
						if ( !quizes.get(index).getQuestion(2).wasVisitedFromBackButton ) {
							quizes.get(index).updateBackward(ma, false, true);				
						} else  { 
							QuizLog.log("Was visited from backx2, used: "+
									quizes.get(index).getQuestion(2).getBackwardQButton().getUsed() );
							quizes.get(index).updateBackward(ma, true, true);
							
						}
					}
				}).build()) /* SLIDER */ 
				.setQTextArea(new QTextArea.Builder().setTextArea("ИЗБЕРИ ЕДИН ОТГОВОРИТЕ КАТО НАТИСНЕШ..."
						, false, 10, 30, null ,Color.white, Color.BLUE, false).build())
								.setFirstLast(false,  false).setLayout(new BoxLayout(ma, BoxLayout.PAGE_AXIS))
				/*.setTextButtonsEtcLayout(new GridLayout(), 
						new GridLayout(), 
						new GridLayout())*/.build())
		.addQuestion(new QQuestion.Builder()
			.setCustomId(3)
			.setButtons( 
				new QButton.Builder().
				setButtonText("ЗАСЕГА НЯМА ДА КОРЕГИРАМ ОТГОВОРА СИ")
				.setButtonSize(300, 100).setColor(new Color(0,150,0))
				.setAction(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					QADList p = (quizes.get(index) != null ? quizes.get(index) : null) ; 
					QADList t = (quizes.get(index+1) != null ? quizes.get(index+1) : null );  
					if ( p.getLast() && t == null ) {
						/* WORK ON THIS FOR EXITING THE PROGRAM */ 
							p.toNextQuiz(self,  null);
						} else {
							p.toNextQuiz(self, t);
						}
					}
				}).build(), 
				/* КЪМ СЛЕДВАЩ КУИЗ --- */
				// > 
				new QButton.Builder().setButtonText("ИСКАМ ДА КОРЕГИРАМ ОТГОВОРА СИ")
				.setButtonSize(300, 100).setColor(new Color(100,100,100))
				.setAction(new ActionListener() {
					/*SWAP THE BUTTON WITH THE PREVIUOUS */
					public void actionPerformed(ActionEvent e) {
						if ( quizes.get(index) != null ) {
							quizes.get(index).updateForward(ma);
						} else { 
							//empty else???
						}
					}
				}).build())
				.setQTextArea(new QTextArea.Builder().setVisible(false)
						.setSize(200, 50)
						.build())
				.setFirstLast(false,  true)
				.build())
		/* ot 4ti vurpos tivame kum 5-ti ili preminavame na sledvasht quiz */
		.addQuestion(new QQuestion.Builder()
			.setCustomId(4)
			.setButtons(
					/* TODO rework it for updating to 
					 * QSLider aarea
					 */
				new QButton.Builder()
					.setButtonText("НА ПО-ПРЕДНИЯ ВЪПРОС").setAction(
						new ActionListener() {
/*TODO FIX LOGIC HERE ====================================> */
							public void actionPerformed(ActionEvent ev2) {
								System.out.println("YOU ARE IN QUIZ:"+quizes.get(index).getID());
							try {	
								quizes.get(index-1).getQuestion(2).wasVisitedFromBackButton = true;
								quizes.get(index-1).isVisitedFromPreviousQuiz = true;
								quizes.get(index).toPreviousQuiz(self, 
											quizes.get(index-1));
							
								} catch (Exception ex) {
	/* OK -tested 14.04.2014 */
									quizes.get(index).getQuestion(4)
									.initError("THE BLUE FRAME - activated on first question back quiz button",self, quizes.get(index), 
											quizes.get(index).getQuestion(4), 
											Color.blue, new ActionListener() {
/* WORK ON THIS 18.04.2014 */
												@Override
												public void actionPerformed(
														ActionEvent e) {
													// TODO Auto-generated method stub
													quizes.get(index).getQuestion(2).wasVisitedFromBackButton = true;
													quizes.get(index).toNextQuiz(self, 
															quizes.get(index+1));
													quizes.get(index).getQuestion(4).getQErrorMessage().closeQErrorFrame();
												}
									});
								}
							}
						})
						.setButtonSize(300, 100).setColor(new Color(100, 50, 0))
						.build() ,
				new QButton.Builder().setButtonText("НА ПРЕДИШНИЯ ВЪПРОС").setAction(
						new ActionListener() {
						public void actionPerformed(ActionEvent ev1) {
							self.quizes.get(index).updateBackward(self, false, true);
						}
						})
						.setButtonSize(300, 100).setColor(new Color(200,40, 12))
						.build())
					
						.setQTextArea(new QTextArea.Builder().setTextArea(""
						, false, 10, 30, null ,Color.white, Color.BLUE, false).build())
						.setFirstLast(false, true) /* it`s the last */
						.setQSlider(null).build())
				.build(); /* main builder end */ 
		if ( indexG == 0 ) {
			self.qadlistref.isFirst = true;
		}
		if ( optlist == null ) {
			self.quizes.add(self.qadlistref);
		} else { 
			self.quizes.add(optlist);
		} 
		
		QuizLog.log("MainApp::initQuizVer2() - constructing the program finished OK");
	} //END initQuizes() construction method
	
	public void setLookAndFeel(int i) {
		this.lnf = i; 
	}
	
	public MainApp() {
		super("QuizAppTest");
		QuizLog.log("MainApp::MainApp() - constructor called, a new thread will be started");
		
	
		//getContentPane().setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		//pack();
	
		/* toolkit for maximized widths and heights */
		int xsize = (int) tk.getScreenSize().getWidth();
		int ysize = (int) tk.getScreenSize().getHeight();
		
		//Add exit with ESC key and remove mouse closeable stuff... DONE!
	
		/* 15.01.2014
		 * disable mouse by setting transparency level to max
		 */
//		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
//		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
//		    cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
//		this.getContentPane().setCursor(blankCursor);
		// END SETTING MOUSE CURSOR
		
/* TODO 12.04.2014 - QuizSplashMenu - use the samo reader to
 * load a text file for "About" 
 */
		try {
			QuizLog.log("MainApp::MainApp() - trying to load an icon file from resources/res directory...");
			this.setIconImage(ImageIO.read(this.getClass().getResourceAsStream("/resources/res/test.png")));
			
		} catch ( Exception ex2 )  { 
			QuizLog.log("ERROR LOADING ICON FILE");
		}
		this.setLayout(new GridLayout());
		this.WIDTH = xsize;
		this.HEIGHT = ysize;
		
/* >>> SET SIZE   ------------------------------------------------------------------------------*/
	//	this.setSize(xsize, ysize); 
		this.setSize(800, 600); //deprecate for fullscreen
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setUndecorated(false);
		this.getContentPane().setBackground(Color.gray);
		try {
			QuizLog.log("************************OPEN CV try-catch*************************");
			QuizLog.log(this.getClass().getResourceAsStream("/opencv/bin/opencv_java246.dll").toString());
		} catch (Exception eeeee) {
			QuizLog.log("*********************** OPEN CV - THROW ERROR ****************************");
		}
		
		try {
			// --------------------------->
			// 08.05.2014 
			MainApp.loadJAR_DLL(MainApp.class.getResourceAsStream("/opencv/bin/java_246.dll").toString());
		} catch (Exception noOCV) {
			System.out.println("NO OPENCV DLL: "+noOCV.getMessage());
		}
		GraphicsDevice gd = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if ( ConfigReader.isFullScreen > 0 ) {
			System.out.println("FULLSCREEN SELECTED");
			self.setUndecorated(true);
			try {
				gd.setFullScreenWindow(self);
			} catch (Exception ex) {
				gd.setFullScreenWindow(null);
			}
		}
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Main Program (all frames) keyPressed: "+arg0.getKeyCode());

				QuizLog.log("Main Program (all frames) keyPressed: "+arg0.getKeyChar());
				if ( arg0.getKeyCode() == 27 ) {	
					WindowEvent e = new WindowEvent(self, WindowEvent.WINDOW_CLOSING);
					WindowEvent e1 = new WindowEvent(self, WindowEvent.WINDOW_CLOSED);
					
					for (WindowListener L: self.getWindowListeners() ) {
						L.windowClosing(e);
						L.windowClosed(e1);
						
					}
				}
				if ( arg0.getKeyCode() == 32 ) {
					/* SPACE */
				 QuizLog.log("MainApp::MainApp()- keyListener - PAUSE PRESSED");
					pauseWarn.init2("PAUSED",  self, null, current, Color.WHITE, 
							new ActionListener() {
						public void actionPerformed(ActionEvent ev) {
							
							pauseWarn.closeQErrorFrame();
							self.thereIsAnErrorFrameOnScreen = false;
							QTimer.anErrorOnTS = false;
						}
					}, null, null);
				}

				if ( arg0.getKeyCode() == 82 ) {
					/* press ' R ' */ 
					/* restore the main app to where it was */
					
					
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				QuizLog.log("Main Program ( all frames ) keyReleased: "+arg0.getKeyChar());

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				self.setVisible(false);
				self.dispose();
				try { 
					QuizLog.record("logfile.log"); 
				}
				catch (Exception ioex) { 
					System.err.println("CRITICAL ERROR - CAN`T WRITE LOG FILE");
				}
				finally { System.exit(0); }
				
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				isRunning = false;
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
				self.requestFocus();
				self.setFocusable(true);
				QTimer.initTimer(self, self.quizes.get(0), self.questions.get(0), 0, 
						new Runnable() {
							public void run() {
								if ( QTimer.getTimerInstance().isRunning() ) {
									QTimer.getTimerInstance().stop();
								} else {
									QTimer.getTimerInstance().start();
								}
							}
						});
				self.quizes.get(0).updateBackwardSpecial(self,  false,  true);
			}
		});
		self.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowGainedFocus(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowLostFocus(WindowEvent arg0) {
				// TODO Auto-generated method stub
				if ( self.isRunning ) {
					self.requestFocus();
					self.setFocusable(true);
				}
			}
		});
		
		self.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		QuizLog.log("MainApp::MainApp() - I will call init()");
		
		/* VERY IMPORTANT TRY-CATCH - MAIN() fails without it!!! */
		/* ADDED 07.07.2014 */ 
		try {
			self.init();
		} catch (Exception ex) { 
			/* print some errors here */
		}
		
		
		//self.add(quizes.get(0).getQuestion(0));
		/*serilization called here for the last question before save */
/*===========================================================================> */
		current = ( current == null ) ? quizes.get(0).getQuestion(0) : current;
		self.add(current);
		this.setVisible(true);
		this.requestFocus();
		this.setFocusable(true);
		new Thread(new Runnable() {
			public void run() {
			for (int i=0; i < questions.size(); i++) {
				QuizLog.log(questions.get(i).toString()+"");
				try {
					Thread.currentThread().sleep(100);
				} catch (Exception ex) { }
			}
			}
		}).start();
		}
		/* linux only command with ffmpeg - NO SOUND TODO - OpenCV - deprecate for now*/ 
		//self.initProcess("ffmpeg -f video4linux2  -s 320x240 -i /dev/video0  /home/ilian/Desktop/outfile.mpg");
		
	/* Static  - read for possible serialized files */
	
	/* Static block - read the cfg file first */
	
	static {
		new Thread(new Runnable() {
			public void run() {
				try {	
					ConfigReader.readConfing();
					QuizLog.log("MainApp.class - static block 1: set look and feel.");
				
					try {
						UIManager.setLookAndFeel(QuizSplashMenu.setLNF());
						
					} catch (ClassNotFoundException | InstantiationException
										| IllegalAccessException
										| UnsupportedLookAndFeelException e) { }
				 
					} catch (Exception ex) { }
			}
		}).start();
	} /* END OF STATIC BLOCK */
	
	private static String setLNF(int i) {
		String lookAndFeel = "";
		lnf = i;
		if ( lnf == 0) lookAndFeel = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
		if ( lnf == 1 ) lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
		if ( lnf == 2) lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		if ( lnf == 3 ) lookAndFeel = "";
		return lookAndFeel;
	}
	
	/* Depricate main for now - call it from QuizSplashMenu.class */
	/*
	public static void main(String[] args) {
		
		try {
			EventQueue.invokeAndWait(new Runnable() {
				@Override
				public void run() {		
					try {
						UIManager.setLookAndFeel(setLNF(ConfigReader.lookAndFeel));
						//com.sun.java.swing.plaf.windows.WindowsLookAndFeel
						//com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel
						//com.sun.java.swing.plaf.gtk.GTKLookAndFeel
						//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					} catch (ClassNotFoundException | InstantiationException
							| IllegalAccessException
							| UnsupportedLookAndFeelException e) {
		
						QuizLog.log(e.getMessage());
					} finally {
						
						}
				     }
				}
				}); 
		} catch (Exception eeee)  { 
			QuizLog.log(eeee.getMessage());
		}
	}  // END OF MAIN! */

	/*Process starter method for camera using ffmpeg - LINUX ONLY! */
	
	/* usable in Win32 too with specific commands */
	public void initProcess(String s) {
		try {
			final Process proc0 = Runtime.getRuntime().exec(s);
			try {
				new Thread(new Runnable() {

					@Override
					public void run() {
			
						try {
							proc0.waitFor();
							int exitval = proc0.exitValue();
							if ( exitval ==0 ) QuizLog.log("Process exited successfully");
							else {
								QuizLog.log("FAILED TO INIT CAMERA MODE. No device?");
							}
							return;
						} catch (Exception e) { 
							
							QuizLog.log(e.getMessage());
						}
					}
				}).start();
			} catch (Exception ex1) { 
				QuizLog.log(ex1.getMessage());
			}
		} catch (java.io.IOException ioex) { 
			QuizLog.log("(PROCESS FUNCTION CALLER) IO EXCEPTION IN CAMERA MODE");
			QuizLog.log(ioex.getMessage());
		}
	}
	
	// New added 08.05.2014 - create temp copy of DLL and read it 
	private static void loadJAR_DLL(String file) throws IOException {
		
		InputStream in = MainApp.class.getResourceAsStream(file);
		byte[] buffer = new byte[1024];
		int read = -1;
		File temp = File.createTempFile(file, "");
		FileOutputStream fos = new FileOutputStream(temp);
		String tDir = System.getProperty("java.io.tmpdir");
		System.out.println("TMP DIR: "+tDir);
		System.out.println(temp.getAbsolutePath());
		
		while ( (read = in.read(buffer)) != -1 ) { /* EOF*/
			fos.write(buffer, 0, read);
		}
		fos.close();
		in.close();
		System.load(temp.getAbsolutePath());
	}
	
	
} // END OF CLASS 


