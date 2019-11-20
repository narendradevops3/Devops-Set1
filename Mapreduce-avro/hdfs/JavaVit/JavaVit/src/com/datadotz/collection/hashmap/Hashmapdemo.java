package com.datadotz.collection.hashmap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;



public class Hashmapdemo {
	
	/*public static void main(String args[]){  
		   
		  HashMap hm=new HashMap();  
		  
		  hm.put(100,"Amit");  
		  hm.put(101,"Vijay");  
		  hm.put(102,"Rahul");  
		  
		  Set set=hm.entrySet();  
		  Iterator itr=set.iterator();  
		  
		  while(itr.hasNext()){  
		   Map.Entry m=(Map.Entry)itr.next();  
		   System.out.println(m.getKey()+" "+m.getValue());  
		  }  
		 } */ 
	public static void main(String args[]){
		
		
	    HashMap<Integer,String> map = new HashMap();
		//MultiMap map = new MultiMap();
		map.put(100,"gowtham");
		map.put(102,"senthil");
		map.put(103,"saravana");
		map.put(104,"ravi");
		map.put(104,"ram");
		

		for (Integer key : map.keySet()) {
		    System.out.println("Key = " + key);
		}

		//iterating over values only
		for (String value : map.values()) {
		    System.out.println("Value = " + value);
		}
		}


}
