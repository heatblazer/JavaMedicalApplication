
package com.ilian.Quiz;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class QButton extends JButton implements java.io.Serializable {
	
	/*optional variables for the monitro */
	
	private static final long serialVersionUID = -5853982049470434019L;
	boolean isPressed = false; 
	public boolean isUsed = false;
	public boolean getUsed() { return isUsed; }
	public void useButton() {isUsed = true; }
	public void resetUses() { isUsed = false; }
	public void press() { isPressed = true; }
	public void release() { isPressed = false;}
	public boolean isPressed() { return isPressed; }
	private Dimension buttonSize;
	private Color color; 
	private String bText;
	private transient ActionListener bAction;
	private transient MouseListener mListener ;
	
	private boolean isSpecialCaseButton = false;
	
	/* needed for assigning it to QErrorFrame`s button */
	public ActionListener getQButtonActionListener() { return bAction; }
	
	public MouseListener getQButtonMouseListener() { return mListener; }
	
	private QButton() { /*no default accessible ctro */}
	/*special controll variables */
	private QButton self = this; 
	
	private void updateListeners() {
		if ( bAction != null) addActionListener(bAction);
		if ( mListener != null ) addMouseListener(mListener);
		
	}
	/* REFER TO OBJECT */
	public QButton getSelf() { return self; }
	/*==========================================*/
	public void setActionL(ActionListener a) { 
		bAction  = a;  
		updateListeners();
	}
	public void setMouseL(MouseListener m ) { 
		mListener = m; 
		updateListeners();
	}
	public void copyButton(QButton src) {
		self = src;
	}
	public void setColor(Color c) {
		self.setBackground(c);
	}
	private QButton(Builder b) {
		bText = b.bTextB;
		bAction = b.bActionB ;
		mListener = b.mListenerB;
		this.color = b.color;
		this.buttonSize = b.buttonSize; 
		this.isSpecialCaseButton = b.isSpecial;
		//make the text fit with line warp
		if ( this.color != null ) {
			this.setBackground(this.color);
			this.setContentAreaFilled(true);
			this.setRolloverEnabled(false);
			this.setBorderPainted(false);
			this.setOpaque(true);
			
		}
		if ( this.buttonSize != null ) this.setPreferredSize(this.buttonSize);
		
		if ( bAction != null ) addActionListener(bAction);
		if ( mListener != null ) addMouseListener(mListener);
		if (bText != null | !bText.equals("") ) setText(bText);
		
	}
	/* INNER BUILDER CLASSS */
	public static class Builder implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -9118708573200915833L;
		private boolean isSpecial = false;
		private Color color = null;
		private String bTextB;
		private Dimension buttonSize; 
		private ActionListener bActionB;
		private MouseListener mListenerB;
		public Builder setListener(MouseListener ml) {
			if ( ml != null ) mListenerB = ml;
			return this;
		}
		public Builder setAction(ActionListener al) {
			if ( al != null ) bActionB = al;
			return this;
		}
		public Builder setColor(Color c) {
			if ( c != null ) color = c;
			return this;
		}
		public Builder setButtonSize(int w, int h) {
			buttonSize = new Dimension(w, h);
			return this; 
		}
		public Builder setButtonText(String t) {
			if ( t != null || ! t.equals("") ) { bTextB = "<html><center>"+t+"</center></html>"; }
			return this;
		}
		public Builder setButton(String t, ActionListener al, MouseListener ml) {
			if ( t != null ) bTextB = t;
			if ( al != null ) bActionB = al;
			if ( ml != null ) mListenerB = ml; 
			return this;
		}
		public Builder setSpecialCase(boolean tf) { this.isSpecial = tf; return this; }
		public Builder() { }; 
		public QButton build() {
			return new QButton(this);
		}
	} /* END OF INNER CLASS */
	
	/*important update of toString() - return all important information */
	@Override
	public String toString() { 
		return "com.ilian.Quiz.QButton:[isUsed="+isUsed+"][Dimension:"+buttonSize+
				"][color:"+color+"][bText:"+bText+"][bAction:"+bAction+"][mListener:"+mListener
				+"][isSpecialCaseButton:"+isSpecialCaseButton+"]";
	}
	
}
		
		