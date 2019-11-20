package com.datadotz.stringbuffer;

public class ClassA {
	public static void main(String args[]) {

		StringBuffer sb = new StringBuffer("Hello ");
		sb.append("Java");// now original string is changed
		sb.insert(1, "Java");// now original string is changed
		sb.replace(1, 3, "Java");
		sb.delete(1, 3);
		sb.reverse();

		System.out.println(sb);// prints Hello Java
	}

}
