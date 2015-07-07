package com.ilian.Quiz;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import com.ilian.Utils.QuizLog;

public class ErrorMessage extends JFrame {
	
	private Color[] colors = {Color.red, Color.yellow, Color.white, Color.CYAN};
	private int colorIndex = 0; 
	private int looper = 50; 
	private int timesClicked = 0;
	
	private static int index = 0; 
	private static JButton ok = new JButton("РАЗБРАХ. ПРОДЪЛЖАВАМ");
	private static QADList qadlist = null; 
	private  JTextArea err = new JTextArea("ГРЕШКА. ТОЗИ БУТОН Е ПОЛЗВАН ЗА ВРЪЩАНЕ. ");
	private static ErrorMessage self ;
	private boolean blink = false; 
	private static MainApp jf = null;
	static final String errorMessage = "ГРЕШКА! НЕ МОЖЕТЕ ДА ПРЕГЛЕЖДАТЕ УСЛОВИЕТО ПОВЕЧЕ ОТ 2 ПЪТИ!";
	private static List<QQuestion> questionVisits = new ArrayList<QQuestion>();
	private static List<QADList> quizVisits = new ArrayList<QADList>();
	private static QADList currentQuiz = null;
	JPanel buttonPanel = new JPanel();
	JPanel textPanel = new JPanel(); 
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
				self.getContentPane().setBackground(colors[colorIndex]);
				self.getContentPane().revalidate();
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
					self.getContentPane().setBackground(Color.RED);
					self.getContentPane().revalidate();
					self.repaint();
					((Timer)e.getSource()).stop();
				}
			} 
		}).start();
	}
	private void setFrameText(String s) {
		self.err.setText(s);
	}
	
	public static void closeError() {
		self.dispose();
	}
	private ErrorMessage(ActionListener al, Color color, String text) { 
		super();
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds(350,  350,  350,  150);
		this.setResizable(false);
		err.setText(text);
		err.setForeground(Color.white);
		err.setColumns(25);
		err.setRows(2);
		err.setEditable(false);
		err.setFocusable(false); 
		err.setBorder(null);
		this.setAlwaysOnTop(true);
		this.setUndecorated(false);	
		this.getContentPane().setBackground(color);
		this.setLayout(new FlowLayout());
		if ( al == null ) {
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				/*CLOSE THE FRAME */
				timesClicked++; //if times > 3 for example, update to next frame
				System.out.println("ErrMsg:button: button is clicked "+timesClicked+" times");
				QuizLog.log("ErrMsg:button: button is clicked "+timesClicked+" times");
				try {
				WindowEvent evclosing = new WindowEvent(self, WindowEvent.WINDOW_CLOSING);
				WindowEvent evclosed = new WindowEvent(self, WindowEvent.WINDOW_CLOSED);
				
				for ( WindowListener L : self.getWindowListeners() ) {
					L.windowClosing(evclosing);
					L.windowClosed(evclosed);
				}
				
				
				} catch (Exception ex) { 
					QuizLog.log(ex.toString());
				} 
			}
			
		});
		} else {
			ok.addActionListener(al);
		}
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("ErrMsg: window is closed");
				QuizLog.log("ErrMsg: window is closed");
				self = null; 
				
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				self.jf.setEnabled(true);
				self.jf.setRunning(true);
				self.jf.requestFocus();
				self.setVisible(false);
				self.qadlist.updateForward(jf);
				
				System.out.println("ErrMsg: window is closing");
				QuizLog.log("ErrMsg: window is closing");
					/* update forward before disposing */
					/* TODO */
				self.dispose();
				
			
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
				self.jf.setRunning(false);
				self.jf.setEnabled(false);
				self.setAlwaysOnTop(true);
				//self.jf.transferFocusUpCycle();
				System.out.println("ErrMsg: window opened");
				QuizLog.log("ErrMsg: window opened");
			}
			
		});
		this.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				
				
			}
			
		});
		textPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		textPanel.setBackground(color);
		textPanel.add(err);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		buttonPanel.add(ok);
		buttonPanel.setBackground(Color.red);
		this.add(textPanel);
		this.add(buttonPanel);
		this.setVisible(true);
	}
	
	/* probably useless... */
	
	public static void showWarnings(MainApp jf, QADList quiz, ActionListener al, String text, Color color) {
	
		self.jf = jf;
		self.qadlist = quiz;
		self = new ErrorMessage(al, color, text);
		
		
	}
	
	public static void showReport() {
		System.out.println("Errors called: ");
		for (int i=0; i < questionVisits.size(); i++) {
			System.out.println("Q:"+questionVisits.get(i).toString());
			QuizLog.log("Q:"+questionVisits.get(i).toString());
		}
		for (int i=0; i < quizVisits.size(); i++ ) {
			System.out.println("QUIZ:"+quizVisits.get(i).toString());
			QuizLog.log("QUIZ:"+quizVisits.get(i).toString());
		}
	}
	
}
