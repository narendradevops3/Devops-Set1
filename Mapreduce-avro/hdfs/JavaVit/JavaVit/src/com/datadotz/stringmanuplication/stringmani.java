package com.datadotz.stringmanuplication;

class Stringmani {
	public static void main(String args[]) {
		String v1 = new String("gowtham");
		String v2 = new String("saravana");
		String v3 = new String("ram");
		System.out.println("string1" + v1);
		System.out.println("string2" + v2);
		System.out.println();
		System.out.println("str1 lowercase:" + v1.toLowerCase());
		System.out.println("str1 uppercase:" + v1.toUpperCase());
		System.out.println("str2 lowercase:" + v2.toLowerCase());
		System.out.println("str2 uppercase:" + v2.toUpperCase());
		System.out.println("str2 replace 'a' into '*':" + v1.replace('a', '*'));
		System.out.println("concatenation of str1andstr2:" + v1.concat(v2));
		System.out.println("Trim:" + (v1.trim()).concat(v2));
		System.out.println("str1&str2 equal:" + v1.equals(v2));
		System.out.println("str1&str2 equal ignorecase:"
				+ v1.equalsIgnoreCase(v2));
		
		
		
		
		System.out.println("strlength" + v1.length());
		System.out.println("Comparision string str1 and str2"
				+ v1.compareTo(v2));
		System.out.println("str1 in 2 position substring:" + v1.substring(2));
		System.out.println("str1 in 2 to 4th position:" + v1.substring(2, 4));
		System.out.println("str1  Index of a:" + v2.indexOf('a'));
		System.out.println("str1 Index of a in 2:" + v2.indexOf('a', 2));
	}
}
