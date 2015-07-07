package com.ilian.Utils;

public class CustomAlgorithms {

	static int hmark = 0;
	static int lmark = 0;
	static int mark = 0;
	public static int deadZone = 60;
	private static int iterator = 0;
	public static int getHiMark() { return hmark; }
	public static boolean HWM(int i, int step) {
		mark = i+step;
		if ( mark > hmark ){
			hmark = mark;
			return true;
		} else {
			hmark = lmark;
			return false;
		}
	}
	

	public static boolean LWM(int i, int step) {
		mark = i+step;
		if ( mark < lmark ) {
			lmark = mark;
			return true;
		} else {
			lmark = hmark;
			return false; 
		}
		
	}
	
	public static int HWM2(int i, int step) {
		mark = i+step;
		if ( mark  > hmark ) {
			hmark = mark ;
			return hmark;
		} else {
			
			return hmark;
		}
		
	}
	
	public static int getInterator() { return iterator; }

	
}
