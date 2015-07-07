package com.ilian.Quiz;

public class TimeMonitoring {
	
	private static long startTime = 0;
	private static long endTime = 0;
	
	public static void startTime() {
		startTime = System.currentTimeMillis();
	}
	public static void endTime() {
		endTime = System.currentTimeMillis();
	}
	
	public static long getDifference() {
		return endTime - startTime;
	}

}
