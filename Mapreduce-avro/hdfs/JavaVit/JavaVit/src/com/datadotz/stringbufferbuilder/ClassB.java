package com.datadotz.stringbufferbuilder;

public class ClassB {
	public static void main(String args[]) {
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 5000; i++)// time in milli sec
		{
			String result = "this example" + "by";// +"string";//concat using +
			String hai = "this example" + "by";
		}
		long endTime = System.currentTimeMillis();

		System.out.println("the" + "time" + "taken" + (endTime - startTime)
				+ "ms");// sub the start time and end time
	//--------------------------------------------------------------
		// StringBuilder
		long startTime1 = System.currentTimeMillis();
		for (int i = 0; i < 500000; i++) {
			StringBuffer result = new StringBuffer();

			result.append("the");// concat using append
			result.append("time");
			result.append("taken");

		}
		long endTime1 = System.currentTimeMillis();
		System.out.println("the" + "time" + "taken" + (endTime1 - startTime1)
				+ "ms");
		
	//---------------------------------------------------------------
		// StringBuffer
		long startTime2 = System.currentTimeMillis();
		for (int i = 0; i < 500000; i++) {

			StringBuilder hai = new StringBuilder();

			hai.append("the");
			hai.append("time");
			hai.append("taken");
		}
		long endTime2 = System.currentTimeMillis();
		System.out.println("the" + "time" + "taken" + (endTime2 - startTime2)
				+ "ms");
	}

}
