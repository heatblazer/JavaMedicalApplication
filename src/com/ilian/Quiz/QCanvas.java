package com.ilian.Quiz;


import com.ilian.Utils.CustomAlgorithms; //for the cusom parsers 
import com.ilian.Utils.CustomParser;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics; 
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter; 
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
/*
 * Mitata: Т.е. отсечката с т. А (2,3) и т. В (11,4) ще я представиш като y=((4-3)/(11-2))x + y0 (където правата пресича ординатата).
 */
public class QCanvas extends JPanel {
	// draw multiple squares on the canvas
	private int[] x;
	private int[] y;
	private QCanvas self = this;
	private static final int WIDTH = 20;
	private List<Rectangle> coordsList = new ArrayList<Rectangle>();
	int[] xcoords;
	int[] ycoords;
	
	int currentX = 0;
	int currentY = 0;
	boolean mouseIsMoving = false;
	int lineX;
	int lineY;
	/* private constructor */
	
	private void populateShapes() {
		
		
		
	}
	private void repaintShapes(int x, int y) {
		this.lineX = x; this.lineY = y;
		this.repaint(lineX, lineY, 5, 5);
	}
	private QCanvas() { };
	
	private QCanvas(Builder b) {
		this.setPreferredSize(new Dimension(50,50));
		this.x = b.xcoords;
		this.y = b.ycoords;
		this.xcoords = new int[1000];
		this.ycoords = new int[1000];
		
		this.rects = b.rects;
		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			/*
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				mouseIsMoving = true;
				xcoords[e.getX()] = e.getX();
				ycoords[e.getY()] = e.getY();
				currentX = e.getX();
				currentY = e.getY();
				coordsList.add(new Rectangle(e.getX(), e.getY(), 5,5));
				repaint(xcoords[e.getX()],ycoords[e.getY()], 5, 5); //added 11.07.2014
				//Debugging only! remove later
			} */ 
			//version 2
			public void mouseMoved(MouseEvent e) {
				mouseIsMoving = true;
			
				repaint(e.getX(), e.getY(), 25,25);
				
			}

		});
	}
// Inner class for rectangles 
	private Rectangle[] rects ;
	/* BUILDER ----------------------------------------- */
	public static class Builder {
		private int[] xcoords;
		private int[] ycoords;
		private Rectangle[] rects;
		boolean isCanvasActive = false;
		Builder(boolean a) { 
			/* unused for now */
			isCanvasActive = a;
		};
		QCanvas build() {
			if ( !isCanvasActive ) return null;
			else return new QCanvas(this);
		}
		Builder setRectangles() {
			this.rects = new Rectangle[xcoords.length];
			for (int i=0; i < rects.length; i++ ) {
				rects[i] = new Rectangle(xcoords[i], ycoords[i], 20,20);
				System.out.println("Rect:"+rects[i].x);
			}
			return this;
		}
		Builder setXYCoords(String[] s) {
			if (s.length != 2) return null; 
			try {
				//BUUUUUUUG!!!!
				xcoords =  CustomParser.toIntegerArray(
						CustomParser.splitBySymbol(',', s[0]));
				ycoords = CustomParser.toIntegerArray(
						CustomParser.splitBySymbol(',', s[1]));
				System.out.println("XCOORDS:"+xcoords.toString());
				System.out.println("YCOORDS:"+ycoords.toString());
				return this;
			} catch (Exception ex) {
				System.out.println("Exception in QCanvas Builder setXYCoords()");
				
				return null;
			}	
		}
		
		Builder setXCoords(String s) {
			xcoords = CustomParser.toIntegerArray(
					CustomParser.splitBySymbol(',', s));
			return this;
		}
		Builder setYCoords(String s) {
			ycoords = CustomParser.toIntegerArray(
					CustomParser.splitBySymbol(',', s));
		
			return this;
		}
		
	}
	/* ------------------------------------------------- */
	
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); 
		//add he graph custom logics below
		
		if ( mouseIsMoving ) {
			System.out.println("Mouse is moving");
			g.setColor(Color.black);
			
			//g.fillRect(xcoords[currentX], ycoords[currentY], 5,5); //is this paint?
		} else {
			
			for (int i=0; i < rects.length; i++) {
				g.setColor(Color.red);
				g.drawRect(rects[i].x, rects[i].y, 20, 20);
				g.setColor(Color.blue);
				g.fillRect(rects[i].x+1, rects[i].y+1, 19,19); 
			}
		}
	}
	
	
	/* UNIT TESTING ------------------------------ */
	
	  public static void main(String[] args) {
	        SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                createAndShowGUI(); 
	            }
	        });
	    }
	 /////////////////////////////////////////////////////////////////////////////////
	  /* 06.07.2014 - test passed -ok workign */
	    private static void createAndShowGUI() {
	        JFrame f = new JFrame("QCanvas test");
	        JPanel jp = new JPanel();
	       
	        String[] answers = {"1,10,20,30,90", "1,10,20,30,90"};
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	        jp.add(new QCanvas.Builder(true).setXCoords(answers[0])
	        		.setYCoords(answers[1])
	        		.setRectangles().build());
	        f.add(jp);
	        f.setPreferredSize(new Dimension(200,200));
	        f.pack();
	        f.setVisible(true);
	    } 
	}	

