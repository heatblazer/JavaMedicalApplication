package com.ilian.Quiz;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.ilian.Utils.QuizLog;
public class QuizXMLReader  {
	
	/**
	 * 
	 */
	
	static final String CANVAS = "canvas";
	static final String RADIO = "radio";
	static final String SLIDER = "slider";
	
	static final String QUIZ = "quiz";
	static final String CONDITION = "condition";
	static final String EXPLANATION = "explanation";
	static final String QUESTION = "question";
	static final String ANSWER = "answer"; 
//	static final String[] ANSWERS = {"a1", "a2", "a3", "a4", "a5"}; 
	private static List<QuizItem> qitems = new ArrayList<QuizItem>(); 
	public static List<QuizItem> getQItems() { return qitems; }
	public static List<QuizItem> readConfig(String configFile) {
		List<QuizItem> items = new ArrayList<QuizItem>();
		System.out.println("readConfig("+configFile+")");
		try {
			QuizLog.log("I AM READING XML");
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream(configFile);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			QuizItem item = null;
			while ( eventReader.hasNext() ) {
				XMLEvent event = eventReader.nextEvent();
				if ( event.isStartElement() ) {
					StartElement startElement = event.asStartElement();
					if ( startElement.getName().getLocalPart() == (QUIZ) ) {
						item = new QuizItem();
						}
				}
				if ( event.isStartElement() ) {
					/*TODO 21.01.2014 - add logic if radio - radiobutton if slidr - qslider */
					if ( event.asStartElement().getName().getLocalPart().equals(RADIO)) {
						event = eventReader.nextEvent();
						QuizLog.log("QuizXMLReader: will init a radio button as answer option.");
						item.isRadio = true;
						item.isSlider = false;
						item.isCanvas = false;
						continue;
					}
					if ( event.asStartElement().getName().getLocalPart().equals(SLIDER)) {
						event = eventReader.nextEvent();
						System.out.println("QuizXMLReader: will init a slider as answer option.");
						item.isRadio = false;
						item.isSlider = true; 
						item.isCanvas = false;
						continue;
					}
					if ( event.asStartElement().getName().getLocalPart().equals(CANVAS) ) {
						event = eventReader.nextEvent();
						System.out.println("Will init test QCanvas as answer");
						item.isRadio = false;
						item.isSlider = false;
						item.isCanvas = true; 
						continue; 
					}
					if ( event.asStartElement().getName().getLocalPart().equals(EXPLANATION) ) {
						event = eventReader.nextEvent();
						item.setExplanation(event.asCharacters().getData());
						continue;
					}
					if ( event.asStartElement().getName().getLocalPart().equals(QUESTION)) {
						event = eventReader.nextEvent();
						item.setQuestion(event.asCharacters().getData());
						continue;
					}
					
					if ( event.asStartElement().getName().getLocalPart().equals("answer") ) {
						event = eventReader.nextEvent();
						item.addAnswer(event.asCharacters().getData());
						continue; 
					} 
					if ( event.asStartElement().getName().getLocalPart().equals("xy") ){
						System.out.println("OK - I READ XY COORDS");
						event = eventReader.nextEvent();
						item.addXYCoord(event.asCharacters().getData());
						continue; 
					}
					
					
				}
				
				if ( event.isEndElement() ) {
					EndElement endElement  = event.asEndElement();
					if ( endElement.getName().getLocalPart() == (QUIZ) ) {
//						items.add(item);
						qitems.add(item);
						QuizLog.log(qitems.get(0)+"");
					}
				}
			}
		} catch (FileNotFoundException fnfex) { 
			JOptionPane.showMessageDialog(null,"КРИТИЧНА ГРЕШКА!!!НЕ Е НАМЕРЕН XML ФАЙЛА! ТЕРМИНАЦИЯ");
			QuizLog.log(fnfex.toString());
			System.exit(1);
		} catch (XMLStreamException xmlstrmex) {
			JOptionPane.showMessageDialog(null,"КРИТИЧНА ГРЕШКА!!!ГРЕШКА ПРИ ПРОЧИТ НА XML ФАЙЛА! ПРАВИЛЕН ФОРМАТ?");
			QuizLog.log(xmlstrmex.toString()); 
			System.exit(1);
		}
		return qitems;
	}
	
}