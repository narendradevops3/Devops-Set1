package com.datadotz.collection.treemap;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
public class Treemapdemo {
	public static void main(String args[]) {

		TreeMap hm = new TreeMap();

		hm.put(100, "Amit");
		hm.put(102, "Ravi");
		hm.put(101, "Vijay");
		hm.put(103, "Rahul");

		Set set = hm.entrySet();
		Iterator itr = set.iterator();

		while (itr.hasNext()) {
			Map.Entry m = (Map.Entry) itr.next();
			System.out.println(m.getKey() + " " + m.getValue());
		}
	}
/*	public static void main(String[] args) {
	      // creating tree map 
	      TreeMap<Integer, String> treemap = new TreeMap<Integer, String>();
	      
	      // populating tree map
	      treemap.put(2, "two");
	      treemap.put(1, "one");
	      treemap.put(3, "three");
	      treemap.put(6, "six");
	      treemap.put(5, "five");
	      
	      // putting values in navigable map
	      NavigableMap nmap=treemap.descendingMap();
	      
	      System.out.println("Checking value");
	      System.out.println("Navigable map values: "+nmap);
	   }    
*/
}
