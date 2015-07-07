package com.ilian.Quiz;
/*variant of answer frame -
 * QRadioButton started at 21.01.2014 y.
 */

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton; 
public class QRadioButton extends JPanel {
	/*TODO */
	/*DELETEME AFTER TEST !*/
	JFrame f; // <-- delete it 
	static Toolkit toolkitRef = Toolkit.getDefaultToolkit();
	public int answerCount = 0; 
	JToggleButton[] radioButtons = null;
	String[] buttonText = null; 
	private ActionListener actionL = null;
	JPanel buttonPanel = new JPanel(new FlowLayout());
	private QRadioButton self = this;
	public QRadioButton getSelf() { return self; }
	
	/* INNER OPTIONAL CLASSES */
	public static class Builder {
		private ActionListener al;
		private String[] buttonText = null;
		private JToggleButton[] radioButtons = null;
	
		public Builder setRadioButtons(String[] t) {
			this.buttonText = new String[t.length];
			this.radioButtons = new JToggleButton[t.length];
			for (int i=0; i < t.length; i++) {
				this.buttonText[i] = t[i];
				this.radioButtons[i] = new JToggleButton();
				this.radioButtons[i].setText("<html><center><b>"+this.buttonText[i]
						+"</b></center></html>");
				this.radioButtons[i].setPreferredSize(new Dimension(
						((int)toolkitRef.getScreenSize().getWidth() / t.length)    ,
						(int) toolkitRef.getScreenSize().getHeight() / 10 
						));
				this.radioButtons[i].setSelected(false);
			}
			return this;
		}
		
		public Builder setActionL(ActionListener al) { this.al = al; return this; }
		public QRadioButton build() { return new QRadioButton(this); }
		
		public Builder() {
			/*TODO*/
			
		}
	} // END INNER BUILDER CLASS 
	
	/* END INNER CLASSES */
	
	private QRadioButton(Builder b) {
		super();
	
		radioButtons = b.radioButtons;
		if ( this.actionL == null ) {
		for (int i=0; i < radioButtons.length ; i++) {
			radioButtons[i].setFocusable(false);
			final int index = i;
			radioButtons[i].addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					// fixes the issue with unclicking 
					((JToggleButton)e.getSource()).setSelected(true);
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					radioButtons[index].setSelected(true);
					for (int i=0; i < radioButtons.length; i++) 
						if ( i != index )  {
							radioButtons[i].setSelected(false);
						}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					//radioButtons[index].setSelected(false);
				}
				
			});
			
			this.add(radioButtons[i]);
			}
		}
		radioButtons[0].setEnabled(true);
		this.setFocusable(false);
		buttonPanel.add(this);
	}
	
}
