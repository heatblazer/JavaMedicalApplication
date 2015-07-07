package com.ilian.Quiz;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

import com.ilian.Utils.ConfigReader;
import com.ilian.Utils.QuizLog;




public class QuizSplashMenu extends JPanel {
	
	private boolean OK = false;
	private MainApp mainApp = null;
	
	private static int lnf=0; 

	private QuizSplashMenu self = this;
	private JButton start = new JButton("Започни теста");
	private JButton loadQuiz = new JButton("Зареди нов тест");
	private JButton editor = new JButton("Създай нов тест");
	private JButton restore = new JButton("Зареди запазен тест");
	private JButton about = new JButton("За нас");
	private JButton exit = new JButton("Изход");
	private JRadioButton gtk = new JRadioButton("GTK");
	private JRadioButton win32 = new JRadioButton("Win32");
	private JRadioButton java = new JRadioButton("Java(default)");
	private JRadioButton nimbus = new JRadioButton("Nimbus");
	
	private JPanel buttonsPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private JPanel buttonsPane2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private JPanel buttonsPane3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private JPanel buttonsPane5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private JPanel buttonsPane4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private JPanel buttonsPane6 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	private JPanel radioButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private JFrame frame = new JFrame();
	private static int lookAndFeel = 0; 
	
	public QuizSplashMenu() {
		super();
		frame.setSize(480, 300);
	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		nimbus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				nimbus.setSelected(true);
				gtk.setSelected(false);
				win32.setSelected(false);
				java.setSelected(false);
				lookAndFeel = 0;
			}
		});
		gtk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nimbus.setSelected(false);
				gtk.setSelected(true);
				win32.setSelected(false);
				java.setSelected(false);
				lookAndFeel = 1;
			}
		});
		win32.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nimbus.setSelected(false);
				gtk.setSelected(false);
				win32.setSelected(true);
				java.setSelected(false);
				lookAndFeel = 2;
			}
		});
		java.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nimbus.setSelected(false);
				gtk.setSelected(false);
				win32.setSelected(false);
				java.setSelected(true);
				lookAndFeel = 3;
			}
		});
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( OK ) { 
					//new MainApp();
					/*SINGLETON HERE */
		
					MainApp.getInstance();
					frame.dispose();
				}
				else System.out.println("ERROR INITING MAIN APP");
			}

		});
		restore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* TODO implement deserilization method if possible - low priority */
			}
		});
		loadQuiz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try { 
					QuizFileLoader.showOpenQuizDialog();
					OK = true;
				} catch (Exception eeee) { OK = false; }
				QuizLog.log("loadQuiz.addActionListener(...");
			}
		});
		
		editor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if ( frame != null ) frame.dispose();
				System.exit(0);
			}
			
		});
		restore.setEnabled(false);
		editor.setEnabled(false);
		buttonsPane.add(start);
		buttonsPane2.add(loadQuiz);
		buttonsPane5.add(restore);
		buttonsPane3.add(editor);
		buttonsPane4.add(exit);
		buttonsPane6.add(about);
	//	radioButtons.add(nimbus);
	//	radioButtons.add(gtk);
	//	radioButtons.add(win32);
   //	radioButtons.add(java);
	//	nimbus.setSelected(true);
		setSize(1200, 800);
		add(buttonsPane); 
		add(buttonsPane2); 
		add(buttonsPane3); 
		add(buttonsPane5);
		add(buttonsPane6);
		add(buttonsPane4);
		
		add(radioButtons);
		setLayout(new BoxLayout(self, BoxLayout.PAGE_AXIS));	
		frame.add(this);
		frame.setVisible(true);
	}
	
	
	static {
		
		new Thread(new Runnable() {
			public void run() {
				System.out.println("Splash menu static block thread");
				ConfigReader.readConfing();
				try {
			//		ConfigReader.readConfing();
					QuizLog.log("lnf is: "+ConfigReader.lookAndFeel);
					
				} catch (Exception redex) { }
			}
		}).start();
	} /* END OF STATIC BLOCK */
	
	public static String setLNF() {
		String lookAndFeel2 = "";
		if ( lookAndFeel == 0 ) lookAndFeel2 = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
		if ( lookAndFeel == 1 ) lookAndFeel2 = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
		if ( lookAndFeel == 2) lookAndFeel2 = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		if ( lookAndFeel == 3 ) lookAndFeel2 = "";
		return lookAndFeel2;
	}
	
	public static void main(String[] agrs) {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					try {
						UIManager.setLookAndFeel(setLNF());
						new QuizSplashMenu();
					} catch (Exception lnfex) { }
					
					
				}
			});
		} catch (Exception ex) { }
	}

}
