package com.datadotz.polymorphism;

// Demonstrate method overloading. 
class Mthdoverload {   
	 void test() {
		System.out.println("No parameters");
	}

	// Overload test for one integer parameter.
	void test(int a) {
		System.out.println("a: " + a);
	}

	// Overload test for two integer parameters.
	void test(int a, int b) {
		System.out.println("a and b: " + a + " " + b);
	}

}
class Overload {
	public static void main(String args[]) {
		Mthdoverload ob = new Mthdoverload();
		ob.test();
		ob.test(10);
		ob.test(10, 20);
		
	}
}