package com.ilian.Quiz;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SpringLayout;
import javax.swing.Timer;


/*import an ErrorMessage.class to QQuestion -
 * each QQuestion will have a separate ErrorMessage which will
 * regain control over the app. TODO logics.
 */

public class QQuestion extends JPanel {
	
	/*testing for activation/deactivation
	 * this fails from question since it spams thousands of 
	 * threads - rework it as a static modifier
	 * for monitoring everything in single thread 
	 * fixed at 11.01.2014 
	 * 
	 */
		

	/* public booleans for setting/unsetting 
	 * wasVisitedFromBackQuestion is used to determine if question[4] "po-preden"
	 * was used to return to here. If so - the forward button of question[2] will behave
	 * as follows:
	 *  inform the user that it will redirect to the next next  question, 
	 *  update to next next question QADList + 2 one
	 */
	public boolean wasVisitedFromBackButton = false;
	public boolean isActive = false;
	
	private int typeOfAnswer = 0; /* 0- none 
									1 - QSlider
									2 - QRadioButton 
									3 - canvas */
	private int customID = 0;
	public int getCustomID() { return customID; }
	
	private int ID=0;
	static int _ID = 0;
	
	public int getID() { return ID; }
	public String toString() {
		String qtastr = (qta == null) ? "none" : qta.toString();
		String rewb = (rew == null) ? "none" : rew.toString();
		String fwdb = (fwd == null) ? "none": fwd.toString();
		String qsld = (qslider == null) ? "none" : qslider.toString();
		
		return "com.ilian.Quiz.QQuestion:[wasVisitedFromBackButton:"+wasVisitedFromBackButton+
				"][isActive:"+isActive+"][typeOfAnswer:"+typeOfAnswer+"][customID:"+customID+
				"][_ID:"+_ID+"][ID:"+ID+"][numbersVisited:"+numbersVisited+"][isFirst:"+isFirst+
				"][isLast:"+isLast+"]\n{"+qtastr+"}\n{"+rewb+"}\n{"+fwdb+"}\n{"+qsld+"}";
	}
	private  MainApp mainApp ;
		
	private int numbersVisited = 0; 
	boolean isFirst = false;
	boolean isLast = false; 
	
	public QQuestion getSelf() { return this; }
	
	public boolean isFirst() { return isFirst; }
	public boolean isLast() { return isLast; }
	public void nullVisits() { numbersVisited = 0; }
	public void increaseVisit() { numbersVisited++; }
	public int getVisited() { return numbersVisited; }
	public QSlider getSlider() { return qslider; }
	
	public QRadioButton getQRadioButton() { return qradio; }
	
	public void copyButton(QButton old, QButton newb) {
		old = newb;
	}
	public QButton getForwardQButton() { return this.fwd; }
	public QButton getBackwardQButton() { return this.rew; }
	
	public QErrorMessage getQErrorMessage() { return errorMessage; }
	public QTextArea getQTextArea() { return qta; }
	
	//added 02.07.2014 
	public QCanvas getQCanvas() { return this.qcanvas; }
	/* crete a singleton here 
	 * important note:
	 * errorMessage must be created and never inited from methods
	 * pseudo singleton 
	 */
	
	private  QErrorMessage errorMessage 	= new QErrorMessage();
	private QADList listRef;
	private MainApp jf;
	
	/*02.01.2014 - working for now 
	 * confirmed logic 
	 */
	private  Timer timer = new Timer(5000, new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			((Timer)arg0.getSource()).stop();
			System.out.println("Timer stopped");
		}
		});
	
	
	public void startTimer() {
		if ( !timer.isRunning() ) {
			timer.start();
		} else {
			System.out.println("Timer is running");
		}
	}
	public void stopTimer() {
		timer.stop();
	}
	
	/* DONE - fixed logic - now initError() works with panels 12.04.2014 */
	
	public void initError(String text, MainApp jf, QADList list, QQuestion qq, Color color,
			ActionListener al) {
		self = qq;
		self.disableAll();
		self = qq;
		self.errorMessage = new QErrorMessage(text, jf, list, null, self, color, al, null, null);
		self.getQQuestionPanelByName("error").add(errorMessage);		
	}
	
	/*variables for timer */
	
	private QRadioButton qradio; /* deprecate since useage of JToggleButton */
								/* rewrote the logic of QRadioButton as for 11.06.2014 */
	
	private QButton fwd;
	private QButton rew;
	private QTextArea qta;
	private QSlider qslider ; 
	private QCanvas qcanvas; /* added 28.06.2014 */
	private QQuestion self = this;
	
	/* TODO replace later with MigLayout ! */
	private LayoutManager lm = new FlowLayout(); /*default manager if none present */
	
	
	private JPanel buttons = new JPanel(/* TODO layout for margin IMPORT MIG LAYOUT*/ );
	private JPanel text = new JPanel(); 
	private JPanel etc = new JPanel(new FlowLayout());
	private JPanel aux1 = new JPanel();
	private JPanel aux2 = new JPanel();
	
	
	private JPanel errorPane = new JPanel();
	public JPanel getQQuestionPanelByName(String t) {
		if ( t.equals("buttons")) return buttons;
		else if ( t.equals("text")) return text;
		else if ( t.equals("etc")) return etc;
		else if ( t.equals("error")) return errorPane; 
		else return null;
	}
	
	
	public static class Builder  { /* BUILDER*/
		/**
		 * 
		 */
	
		int customID = 0; 
		int typeOfAnswer = 0 ;
		boolean isFirst = false;
		boolean isLast = false;
		private QErrorMessage errMsg = null;
		private QErrorMessage warnMsg = null;
		private QSlider qslider = null;

		private QCanvas qcanvas = null;
		private QRadioButton qradio = null;
		private QButton fwd = null;
		private QButton rew = null;
		private QTextArea qta = null;
		private LayoutManager lm = null; 
		private LayoutManager buttonsLayout = null;
		private LayoutManager textLayout = null;
		private LayoutManager etcLayout = null;
		
		public Builder enableQError() { 
				errMsg = new QErrorMessage();
				warnMsg = new QErrorMessage() ; 
				return this;
		}
		
		public Builder setCustomId(int id) { this.customID = id; return this; }
		public Builder setButtons(QButton f, QButton b) { this.rew = b; this.fwd = f; return this; }
		public Builder setQTextArea(QTextArea qt) { this.qta = qt; return this; }
		public Builder setQSlider(QSlider qsl) { this.qslider = qsl; return this; }
		
		/* new 02.07.2014 * QCanvas */
		public Builder setQCanvas(QCanvas qcvs) { this.qcanvas = qcvs; return this; }
		
		public Builder setQRadioButton(QRadioButton qrb ) { this.qradio = qrb; return this; }
		public Builder setLayout(LayoutManager l) { this.lm = l; return this; }
		public Builder setFirstLast(boolean first, boolean last) { this.isFirst = first; this.isLast = last; return this; }
		public Builder setTypeOfAnswer(int i) { this.typeOfAnswer = i; return this; }
		public Builder setTextButtonsEtcLayout(LayoutManager t, LayoutManager b, LayoutManager e) {
			this.buttonsLayout = b; this.textLayout = t; this.etcLayout = e; return this; 
		}
		
		public Builder setQuestion(boolean isF, boolean isL, QButton f, QButton r, QTextArea qt,
				QSlider qsl, LayoutManager w ) {
			isFirst = isF; isLast=isL; fwd = f; rew = r; qta = qt; qslider = qsl; lm = w;
			return this;
		}
		public Builder(int type) {
			this.typeOfAnswer = type;
		}
	
		public Builder() { }
		public QQuestion build() {
			return new QQuestion(this);
		}
		
	} //END INNER CLASS

	
	
	
	private QQuestion() { /*never access it */ }
	
	private QQuestion(Builder b) {
		ID = _ID;
		_ID++; 
		this.typeOfAnswer = b.typeOfAnswer;
		this.isActive = false;
		this.customID = b.customID; 
	
		isFirst = b.isFirst;
		isLast = b.isLast;
		fwd = b.fwd;
		rew = b.rew;
		qta = b.qta;
		this.qradio = b.qradio == null ? null : b.qradio;
		this.qslider = b.qslider == null ? null : b.qslider;
		this.qcanvas = b.qcanvas == null ? null : b.qcanvas; // fixed bug - missing assignment from builder
	
		text.setFont(new Font("Arial", Font.ITALIC, 22));
		lm = b.lm; 
//		text.setBackground(Color.white);
//		etc.setBackground(Color.white);
//		buttons.setBackground(Color.white);
		buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		if ( b.errMsg != null ) this.errorMessage = b.errMsg;
		if ( qta != null ) 	text.add(qta);
		/* ADD QCanvas here ---------- */
		
		if ( qslider != null && ( this.typeOfAnswer == 1 ) ) {
			
		//	aux1.add(qslider.getAuxilarySlider());
		//	aux2.add(qslider);
			
			etc.add(qslider.getAuxilarySlider());
			etc.add(qslider);
		
		}
		
		if ( qradio != null && this.typeOfAnswer == 2 ) {
			etc.add(qradio);
			
		}
	
		if ( this.qcanvas != null && this.typeOfAnswer == 3 ) {
			text.removeAll();
			etc.setBorder(BorderFactory.createEtchedBorder());
			etc.setPreferredSize(new Dimension(400,400));
			qcanvas.setPreferredSize(new Dimension(400,400));
			etc.add(this.qcanvas);		
		}
	
		
		if ( rew != null ) {
			buttons.add(rew);
			JPanel tm = new JPanel();
			tm.setPreferredSize(new Dimension(100, 0));
			buttons.add(tm);
			
		}
		
		if ( fwd != null ) {
			buttons.add(fwd);
		}
		add(text); 
		add(errorPane);
	/* new listener - add or remove component and update */
		errorPane.addContainerListener(new ContainerListener() {

			@Override
			public void componentAdded(ContainerEvent e) {
				/* importnat listener - 
				 * keep track of the error message
				 * adding it or removing it
				 * 
				 */
				self.errorMessage = (QErrorMessage) e.getChild();
			}

			@Override
			public void componentRemoved(ContainerEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Removed error component");
				System.out.println(errorMessage.toString());
			}
			
		});
		add(etc); 
		add(buttons); 
		//SET DEFAULT LAYOUT UNCHANGABLE FOR NOW 
		//added on 04.12.2013
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
	}

/* New added 08.04.2014 - enable all but  error message, and disable error message
 * and enable all other components 
 */
	public void disableAll() {
		if ( qradio != null ) qradio.setVisible(false);
		if ( fwd != null ) fwd.setVisible(false);
		if ( rew != null ) rew.setVisible(false);
		if ( qta != null ) qta.setVisible(false);
		if ( qslider != null ) {
			qslider.setVisible(false); 
			qslider.getAuxilarySlider().setVisible(false);
		}
	}
	
	public void enableAll() {
		self.errorMessage.setVisible(false);
		if ( qradio != null ) qradio.setVisible(true);
		if ( fwd != null ) fwd.setVisible(true);
		if ( rew != null ) rew.setVisible(true);
		if ( qta != null ) qta.setVisible(true);
		if ( qslider != null ) {
			qslider.setVisible(true); 
			qslider.getAuxilarySlider().setVisible(true);
		}
	
	}

}
