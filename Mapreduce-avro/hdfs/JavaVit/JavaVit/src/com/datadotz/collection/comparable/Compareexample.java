package com.datadotz.collection.comparable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Compareexample implements Comparable {
	int rollno;
	String name;
	int age;

	Compareexample(int rollno, String name, int age) {
		this.rollno = rollno;
		this.name = name;
		this.age = age;
	}

	public int compareTo(Object obj) {
		Compareexample st = (Compareexample) obj;
		if (age == st.age)
			return 0;
		else if (age > st.age)
			return 1;
		else
			return -1;
	}
}
class Simple{  
public static void main(String args[]){  
  
ArrayList al=new ArrayList();  
al.add(new Compareexample(101,"Vijay",23));  
al.add(new Compareexample(106,"Ajay",27));  
al.add(new Compareexample(105,"Jai",21));  
  
Collections.sort(al);  
Iterator itr=al.iterator();  
while(itr.hasNext()){  
	Compareexample st=(Compareexample)itr.next();  
System.out.println(st.rollno+""+st.name+""+st.age);  
  }  
}
}