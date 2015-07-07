package com.ilian.Quiz;

/*this class is no longer needed 
 * delete it 
 */

/* EXPERIMENTAL CLASS - DISABLE LATER */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import net.miginfocom.swing.MigLayout;
@Deprecated
public class QToggleButton  {
	private MigLayout miglayout;
	private static JFrame window = new JFrame();
	private static JTextArea textArea = new JTextArea();
	private static JToggleButton button1 = new JToggleButton("OPTION 1");
	private static JToggleButton button2 = new JToggleButton("OPTION 2");
	private static JToggleButton button3 = new JToggleButton("OPTION 3");
	private static JToggleButton button4 = new JToggleButton("OPTION 3");
	private static JToggleButton button5 = new JToggleButton("OPTION 3");
	private static List<JToggleButton> buttons = new ArrayList<JToggleButton>();
	private static String text; 
	
	static JPanel jp = new JPanel(new MigLayout());
	public static void showErrorMessage(String text, Color color, ActionListener action) {
		
		window.setSize(600,  200);
		textArea.setText(text);
		textArea.setBackground(color);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jp.setPreferredSize(new Dimension(200, 200));
		window.add(textArea);
		jp.add(button1);
		jp.add(button2, "gap unrelated");
		jp.add(button3);
		jp.add(button4, "wrap");
		jp.add(button5, "span, grow");
		
		button1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//button2.setSelected(false);
				//button3.setSelected(false);
				//button4.setSelected(false);
				//button5.setSelected(false);
			}
			
		});
		
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//button1.setSelected(false);
				//button3.setSelected(false);
				//button4.setSelected(false);
				//button5.setSelected(false);
			}
			
		});
		button3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//button1.setSelected(false);
				//button2.setSelected(false);
				//button4.setSelected(false);
				//button5.setSelected(false);
			}
			
		});
		button4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//button1.setSelected(false);
				//button2.setSelected(false);
				//button3.setSelected(false);
				//button5.setSelected(false);
			}
			
		});
		button5.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//button1.setSelected(false);
				//button2.setSelected(false);
				//button3.setSelected(false);
				//button4.setSelected(false);
			}
			
		});
		
		
		window.add(jp);
		window.setVisible(true);
	}
	
	public static void main(String[] args) {
		QToggleButton.showErrorMessage("sdsds",  Color.red,  null);
	}
	
}
