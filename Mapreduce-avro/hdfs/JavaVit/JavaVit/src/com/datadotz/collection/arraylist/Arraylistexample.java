package com.datadotz.collection.arraylist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Arraylistexample {

	/*public static void main(String args[]) {

		ArrayList al = new ArrayList();
		al.add("Viru");
		al.add("Saurav");
		al.add("Mukesh");
		al.add("Tahir");

		Collections.sort(al);
		Iterator itr = al.iterator();
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
	}*/
	public static void main(String[] args) {
	      
	    // create an empty array list with an initial capacity
	    ArrayList<Integer> arrlist = new ArrayList<Integer>(5);

	    // use add() method to add elements in the list
	    arrlist.add(15);
	    arrlist.add(22);
	    arrlist.add(30);
	    arrlist.add(40);

	    // let us print all the elements available in list
	    for (Integer number : arrlist) {
	      System.out.println("Number = " + number);
	      
	    
	    } 
		
	    // retrieves element at 4th postion
	    int retval=arrlist.get(3);
		System.out.println("Retrieved element is = " + retval);	   
	  }
}
