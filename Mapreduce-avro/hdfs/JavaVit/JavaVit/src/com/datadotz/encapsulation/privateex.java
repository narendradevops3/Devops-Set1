package com.datadotz.encapsulation;

class Privateex {
	
 private int x=56;
	protected void showDemo() {
	
		System.out.println("The Variable value is " +x);
	}

	private void testDemo() {
		 
		System.out.println("It cannot be accessed in another class");
	}
	
}

class AccessExam {

	public static void main(String[] args) {
		Privateex ad = new Privateex();
		//ad.testDemo(); // Private method cannot be used
		//Systesm.out.println(ad.x); // Private variable cannot be use
		ad.showDemo(); // run properly but no output
	}
}
