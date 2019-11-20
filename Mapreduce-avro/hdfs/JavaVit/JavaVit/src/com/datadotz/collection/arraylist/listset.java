package com.datadotz.collection.arraylist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class listset {
	public static void main(String args[]) {
		long startTime1 = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {

			ArrayList al = new ArrayList();
			al.add(i);

		}
		long endTime1 = System.currentTimeMillis();
		System.out.println("the" + "time" + "taken" + (endTime1 - startTime1)
				+ "ms");

		long startTime2 = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {

		LinkedList al = new LinkedList();
			
			al.add(i);

		}
		long endTime2 = System.currentTimeMillis();
		System.out.println("the" + "time" + "taken" + (endTime2 - startTime2)
				+ "ms");
	}
}