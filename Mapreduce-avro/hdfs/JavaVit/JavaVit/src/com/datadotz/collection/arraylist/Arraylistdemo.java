package com.datadotz.collection.arraylist;

import java.util.ArrayList;
import java.util.Iterator;

//public class Arraylistdemo {
//	// Example of ArrayList
//	public static void main(String args[]) {
//
//		ArrayList al = new ArrayList();
//		al.add("Ravi");
//		al.add("Vijay");
//		al.add("Ravi");
//		al.add("Ajay");
//		al.add("Ajay");
//
//		Iterator itr = al.iterator();
//		while (itr.hasNext()) {
//			System.out.println(itr.next());
//		}
//	}
//
//}

// Iterating the elements of Collection by for-each loop:

// public static void main(String args[]){
//
// ArrayList al=new ArrayList();
// al.add("Ravi");
// al.add("Vijay");
// al.add("Ravi");
// al.add("Ajay");
//
// for(Object obj:al)
// System.out.println(obj);
// }
//}

// Storing user-defined class objects:
//
//class Student {
//	int rollno;
//	String name;
//	int age;
//
//	Student(int rollno, String name, int age) {
//		this.rollno = rollno;
//		this.name = name;
//		this.age = age;
//	}
//}
//class Simple{  
//	 public static void main(String args[]){  
//	    
//	  Student s1=new Student(101,"Sonoo",23);  
//	  Student s2=new Student(102,"Ravi",21);  
//	  Student s3=new Student(103,"Hanumat",25);  
//	      
//	  ArrayList al=new ArrayList();  
//	  al.add(s1);  
//	  al.add(s2);  
//	  al.add(s3);  
//	    
//	  Iterator itr=al.iterator();  
//	  while(itr.hasNext()){  
//	    Student st=(Student)itr.next();  
//	    System.out.println(st.rollno+" "+st.name+" "+st.age);  
//	  }  
//	 }  
//	}  


//Example of addAll(Collection c) method:

//class Simple{  
//	 public static void main(String args[]){  
//	   
//	  ArrayList al=new ArrayList();  
//	  al.add("Ravi");  
//	  al.add("Vijay");  
//	  al.add("Ajay");  
//	    
//	  ArrayList al2=new ArrayList();  
//	  al2.add("Sonoo");  
//	  al2.add("Hanumat");  
//	    
//	  al.addAll(al2);    
//	  
//	  Iterator itr=al.iterator();  
//	  while(itr.hasNext()){  
//	   System.out.println(itr.next());  
//	  }  
//	 }  
//	} 


//Example of removeAll() method:

//class Simple{  
//	 public static void main(String args[]){  
//	   
//	  ArrayList al=new ArrayList();  
//	  al.add("Ravi");  
//	  al.add("Vijay");  
//	  al.add("Ajay");  
//	    
//	  ArrayList al2=new ArrayList();  
//	  al2.add("Ravi");  
//	  al2.add("Hanumat");  
//	    
//	  al.removeAll(al2);  
//	  
//	  System.out.println("iterating the elements after removing the elements of al2...");  
//	  Iterator itr=al.iterator();  
//	  while(itr.hasNext()){  
//	   System.out.println(itr.next());  
//	  }  
//	  
//	  }  
//	}

//Example of retainAll() method:

//class Simple{  
//	 public static void main(String args[]){  
//	   
//	  ArrayList al=new ArrayList();  
//	  al.add("Ravi");  
//	  al.add("Vijay");  
//	  al.add("Ajay");  
//	    
//	  ArrayList al2=new ArrayList();  
//	  al2.add("Ravi");  
//	  al2.add("Hanumat");  
//	    
//	  al.retainAll(al2);  
//	  
//	  System.out.println("iterating the elements after retaining the elements of al2...");  
//	  Iterator itr=al.iterator();  
//	  while(itr.hasNext()){  
//	   System.out.println(itr.next());  
//	  }  
//	 }  
//	}  