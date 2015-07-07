package com.ilian.Utils;

public class CustomParser {
	static String[] str ;
	public static String[] splitBySymbol(char delimiter, String array) {
		str = array.toString().split(delimiter+"");
		for (int i=0; i < str.length; i++) {
			System.out.println(str[i]);
		}
		return str;
		
	}
	public static int[] toIntegerArray(String[] s) {
		int[] ints = new int[s.length];
		for (int i=0; i < s.length; i++) ints[i] = Integer.parseInt(s[i]);
		return ints;
	}

/* 01.07.2014 */	
/* testing purposes only */
	public static void main(String[] args) {
		String[] s = CustomParser.splitBySymbol(',', "1");
		String[] s2 = CustomParser.splitBySymbol(',', "1,2,3");
		int[] ints = CustomParser.toIntegerArray(s2);
		int sum=0;
		for (int i=0; i < ints.length; i++) {
			sum+= ints[i];
		}
		System.out.println(sum+" is the sum");
	}
}
