package com.datadotz.collection.linkedlist;

import java.util.LinkedList;

public class Linkedlistexample {
	public static void main(String[] args) {
	    //create an ArrayList object
	    //LinkedList linkedList = new LinkedList();
	    LinkedList LList = new LinkedList();
	    //Add elements to Arraylist
	    LList.add("1");
	    LList.add("2");
	    LList.add("3");
	   
	    /*
	      To add an element at the specified index of ArrayList use
	      void add(int index, Object obj) method.
	      This method inserts the specified element at the specified index in the
	      ArrayList.  
	    */
	    LList.add(1,"INSERTED ELEMENT");
	   
	    /*
	      Please note that add method DOES NOT overwrites the element previously
	      at the specified index in the list. It shifts the elements to right side
	      and increasing the list size by 1.
	    */
	 
	    System.out.println("Linked contains...");
	    //display elements of ArrayList
	    for(int index=0; index < LList.size(); index++)
	      System.out.println(LList.get(index));
	   
	  }
}
//public static void main(String[] args) {
//    LinkedList lList = new LinkedList();
//    lList.add("100");
//    lList.add("200");
//    lList.add("300");
//    lList.add("400");
//    lList.add("500");
//    System.out.println("First element of LinkedList is :" + lList.getFirst());
//    System.out.println("Last element of LinkedList is : " + lList.getLast());
// }


//How to search an element inside a linked list ?


//public static void main(String[] args) {
//    LinkedList lList = new LinkedList();
//    lList.add("1");
//    lList.add("2");
//    lList.add("3");
//    lList.add("4");
//    lList.add("5");
//    lList.add("2");
//    System.out.println("First index of 2 is:"+
//    lList.indexOf("2"));
//    System.out.println("Last index of 2 is:"+ 
//    lList.lastIndexOf("2"));
// }