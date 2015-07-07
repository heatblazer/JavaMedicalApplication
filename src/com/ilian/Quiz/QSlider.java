package com.ilian.Quiz;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;


public class QSlider extends JSlider  {
	/**
	 * 
	 */
	
	/* special layout / view class */
	/* *************************************************************************** */
	private static class MySliderUI extends BasicSliderUI {
		private static float[] fracs = {0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f };
		
		private LinearGradientPaint paint; 
		public MySliderUI(JSlider b) {
			super(b);
			
			// TODO Auto-generated constructor stub
		}
		
		/*
		@Override
		public void paintLabels(Graphics g) {
			
		} */
		
		/*
		@Override
		protected void paintHorizontalLabel(Graphics g, int value, Component label ){
			
		
		} */
		
		
		@Override
		public void paintTrack(Graphics g) {
			/*
			Graphics2D g2d = (Graphics2D) g;
			Rectangle t = trackRect;
			Point2D start = new Point2D.Float(t.x, t.y);
			Point2D end = new Point2D.Float(t.width, t.height);
			Color[] colors = {Color.magenta, Color.blue, Color.cyan,
					Color.green, Color.yellow, Color.red };
			paint = new LinearGradientPaint(start, end, fracs, colors);
			g2d.setPaint(paint);
			g2d.fillRect(t.x, t.y, t.width, t.height);
			*/
		}
		
		
		@Override
		public void paintThumb(Graphics g) {
			
			//Graphics2D g2d = (Graphics2D) g;
			//g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			//		RenderingHints.VALUE_ANTIALIAS_ON);
			//Rectangle t = thumbRect;
			//Rectangle t2 = trackRect;
			//g2d.setColor(Color.black);
			//int tw2 = t.width / 2;
			//int th2 = t.height + 20;
			//g2d.drawLine(t.x, t.y, t.x+t.width-1, t.y);
			//g2d.drawLine(t.x, t.y, t.x+(tw2*2), t.y+t.height+(t.height/2));
			//g2d.drawLine(t.x+t.width-1, t.y, t.x+tw2*2, t.y+t.height+(t.height/2));
			//g2d.fillRect(t.x, t.y, t.width*2, t.height*2);
			//
			//g2d.fillOval(t.x, t.y-20 , t.width*2, t.height*2);
			
		} 
		
	} // END INNER CUSTOMIZING CLASS 
	
	/* **************************************************************************************** */
	private  List<Integer> sliderValues = new ArrayList<Integer>(); 
	
	public void addSliderValue(final int i) { 
		new Thread(new Runnable() {
			
			public void run() {
			sliderValues.add(new Integer(i)); 
			try { Thread.currentThread().sleep(200); } catch (Exception ex) { } 
			}
		}).start();
	}
	
	/* REMOVE LATER  TEST FOO() */
	public void printValues() {
		for (int i=0; i < sliderValues.size(); i++) 
			System.out.println(">>>>>>>>>>>>>>>>>>>>>> index:"+i+" value:"+sliderValues.get(i));
	}
	
	private  long[] times = new long[2];
	private  int index = 0;
	private  long stime = 0;
	private  long etime = 0;
	private Hashtable<Integer, JLabel> table = 
			new Hashtable<Integer, JLabel>(); //table for text
	
	/* table over slider extra methods */ 
	private JSlider auxilarySlider = null;
	public Hashtable<Integer, JLabel> getQSliderTable() { return table; }
	public JSlider getAuxilarySlider() { return this.auxilarySlider; }
	private JPanel qsPanel = new JPanel();
	public JPanel getQSLiderPanel() { return qsPanel; }
	
	public void setStime(long st) { this.stime = st; }
	public void setEtime(long et) { this.etime = et; }
	public long getStime() { return this.stime; }
	public long getEtime() { return this.etime; }
	
	private int xpointer = 0;
	public void setXpointer(int x) { xpointer = x; }
	public int getXpointer() { return xpointer; }
	
	private int startPoint = 0;
	private int endPoint = 0;
	private int sizeX = 0;
	private int sizeY = 0;
	private transient ChangeListener changeL = null;
	private transient MouseListener mouseL = null;
	private transient MouseMotionListener motionL = null;
	
	public int answerCounter = 0; 
	private QSlider() { /*never instant it */ }
	private QSlider self = this; 
	public QSlider getSelf() { return self; }
	
	@Override
	public String toString() {
		return "com.ilian.Quiz.QSlider:[startPoint:"+startPoint+"][endPoint:"+endPoint+
				"][sizeX:"+sizeX+"][sizeY:"+sizeY+"][changeL:"+changeL+"][mouseL:"+mouseL+
				"][motionL:"+motionL+"][table:"+table+"]";
	}
	
	private void updateListeners() {
		if ( changeL != null ) {
			self.addChangeListener(changeL);
		}
		if ( mouseL != null ) {
			self.addMouseListener(mouseL);
		}
		if ( motionL != null ) {
			self.addMouseMotionListener(motionL);
		}
	}
	/*TODO getter for ChangeEvent action */
	
	public void setChangeListener(ChangeListener cl) {
		changeL = cl;
		updateListeners();
	}
	public void setMouseListener(MouseListener ml) {
		mouseL = ml;
		updateListeners();
	}
	public void setMotionListener(MouseMotionListener mml) {
		motionL = mml;
		updateListeners(); 
	}
	public static class Builder implements java.io.Serializable { /*BUILDER */
		/**
		 * 
		 */
		private boolean isAux = false;
		private int startPoint = 0;
		private int endPoint = 0;
		private int sizeX = 0;
		private int sizeY = 0;
		private Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
		private ChangeListener changeL = null;
		private MouseListener mouseL = null;
		private MouseMotionListener motionL = null;
		public Builder() { } /* do we need it? */
		public Builder setSizeXY(int x, int y) { this.sizeX = x; this.sizeY = y; return this; }
		public Builder setStartEndPoints(int a, int b) { this.startPoint = a; this.endPoint = b; return this; }
		public Builder addChangeL(ChangeListener cl ) { this.changeL = cl;  return this; }
		public Builder addMouseL(MouseListener ml) { this.mouseL = ml; return this; }
		public Builder addMotionL(MouseMotionListener mml ) { this.motionL = mml; return this; }
		public Builder addTable(String[] s) {
			for (int i=0; i < s.length; i++ ) {
				table.put((i*(this.sizeX/(s.length))), new JLabel(s[i]));
			}
			return this; 
		}
		@Deprecated
		public Builder setAuxilaryQSlider(boolean isAux) {
			this.isAux = isAux;
			return this;
		}
		public QSlider build() { return new QSlider(this) ; }
	} //END INNER BUILDER CLASS 

/* ******************************************************************************************** */
	
	private QSlider(Builder b) {
		super(JSlider.HORIZONTAL, b.startPoint, b.endPoint, 1);
	/* enable / disable custom look and feel */ 
		
		this.table = b.table.isEmpty() ? null : b.table; 
		this.setLabelTable(table);
		this.setPaintTicks(false);
		this.setPaintLabels(false);		
		this.startPoint = b.startPoint;
		this.endPoint = b.endPoint;
		this.sizeX = b.sizeX;
		this.sizeY = b.sizeY;
		this.setMinorTickSpacing(1);
		this.setMajorTickSpacing((this.table.size()-1));
		this.setOrientation(JSlider.HORIZONTAL);
		//this.setSize(this.sizeX, this.sizeY); 
		this.setValueIsAdjusting(true);		
		this.setPreferredSize(new Dimension(this.sizeX, this.sizeY));
		this.setFocusable(false);
		// aux slider
		/* added custom geometry for the full screen slider
		 * now properly paint major ticks 
		 * the formula is:
		 * b.startPoint is 0 and end of slider - (slider size / number of questions) 
		 */
		this.auxilarySlider = new JSlider(JSlider.HORIZONTAL, b.startPoint, 
				(b.endPoint - (b.endPoint/b.table.size())), 1);
		this.auxilarySlider.setUI(new MySliderUI(this));
		this.auxilarySlider.setLabelTable(table);
		
		this.auxilarySlider.setPaintTicks(true);
		this.auxilarySlider.setPaintLabels(true);
		this.auxilarySlider.setPreferredSize(new Dimension(b.sizeX, b.sizeY));
		this.auxilarySlider.setMinorTickSpacing(5);
		//this.auxilarySlider.setMajorTickSpacing(b.sizeX / b.table.size()-1);
		this.auxilarySlider.setMajorTickSpacing(b.sizeX / b.table.size()+1);
		
		
		this.changeL = ( b.changeL == null ) ? null : b.changeL;
		this.mouseL = ( b.mouseL == null ) ? null : b.mouseL ;
		this.motionL = ( b.motionL == null ) ? null : b.motionL; 
		if ( mouseL != null ) addMouseListener(mouseL);
		if ( changeL != null ) addChangeListener(changeL);
		if ( motionL != null ) addMouseMotionListener(motionL);
		
		
		
		// depricated 13.02.2014 
		//qsPanel.setLayout(new GridLayout(0,1));
		//this.qsPanel.add(this.auxilarySlider);
		//this.qsPanel.add(this);
		
	}
	
/* verified - working 27.12.2013 */	

	
}
