package com.ilian.Quiz;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTextArea;
/*!TODO - POSSIBLE PITFALL BUG WHILE READING XML FILES WITH UNPROPER ENCODING 
 * THIS WILL NOT BE FIXED, SINCE EDITOR WILL OPUTPUT THE PROPER FORMAT/ENCODING
 * FIXED!!!
 * FONT IS MAKING AN ISSUE. REMOVED FONT
 */
public class QTextArea  extends JTextArea {
	/**
	 * 
	 */
	
	/*init defaults */ 
	private String text;
	private QTextArea self = this;
	public QTextArea getSelf() { return self; } 

	boolean isEditable = false;
	int rows ;
	int cols ;
	Dimension size = null;
	Font font ;
	Color backgroundColor ;
	Color foregroundColor ;
	boolean setLineWarp ;
	boolean setVisible = false;
	
	@Override
	public String toString() {
		return "com.ilian.Quiz.QTextArea:[text:"+text+"][isEditable:"+isEditable+"][rows:"+rows+
				"][cols:"+cols+"][size:"+size+"][font:"+font+"][backgroundColor:"+backgroundColor+
				"][foregroundColor:"+foregroundColor+"][setLineWarp:"+setLineWarp+
				"][setVisible:"+setVisible+"]";
	}
	/* public mutators */
	public void setQText(String s) {
		self.setText(s);
	}
	
	/*BUILDER CLASS */
	public static class Builder implements java.io.Serializable {
		private String bText;
		private boolean bisEditable = false; 
		private Dimension size; 
		private int brows = 20;
		private int bcols = 20;
		private boolean setVisible = true ; 
		private Font bfont = new Font("Veranda", Font.ITALIC, 22);
		
		private Color bbackgroundColor = Color.BLUE;
		private Color bforegroundColor = Color.gray;
		boolean bsetLineWarp = true;   
		public Builder setText(String t) { bText = t; return this; }
		public Builder setArea(boolean isEditable, boolean lwarp, int rows, int cols) {
			bisEditable = isEditable; brows = rows; bcols = cols; bsetLineWarp = lwarp; 
			return this;
		}
		public Builder setVisible(boolean tf) { setVisible = tf; return this; }
		public Builder setFont(Font f) { bfont = f; return this; }
		public Builder setColors(Color bg, Color fg) { this.bbackgroundColor = bg; this.bforegroundColor = fg; return this; }
		public Builder setSize(int w, int h) { this.size = new Dimension(w, h);  return this; }
		public Builder setRowsCols(int r,  int c) { brows = r; bcols = c;  return this; }
		public Builder setTextArea(String t, boolean isEditable, 
				int rows, int cols, Font f, Color bg, Color fg, boolean lwarp) {
			bText = t; bisEditable = isEditable; brows = rows; bcols = cols;
			bfont = f; bbackgroundColor = bg; bforegroundColor = fg;
			bsetLineWarp = lwarp;
			return this;
		}
		public QTextArea build() {
			return new QTextArea(this);
		}
		
	} // END OF INNER CLASS

	private QTextArea(Builder b) {
		super();
		this.text =  b.bText;
		this.size = b.size;
		this.cols = b.bcols; 
		this.rows = b.brows;
		this.backgroundColor = b.bbackgroundColor;
		this.foregroundColor = b.bforegroundColor;
		this.isEditable = b.bisEditable;
		this.setLineWarp = b.bsetLineWarp;
		this.setVisible = b.setVisible; 
		this.font = b.bfont;
		if ( this.size != null ) {
			this.setPreferredSize(size);
		}
		if ( ! this.setVisible ) {
			this.setVisible(false); 
		}

		this.setText(text);

		this.setFont(font);
		this.setFocusable(false);
		setLineWrap(true);
		setEditable(isEditable);
		/* set font bugs in windows... with default fonts*/
		
		setForeground(this.foregroundColor);
		setBackground(this.backgroundColor);
		
		setRows(rows); setColumns(cols);
		
	}

	/* Verified - working 27.12.2013 */
	
}
