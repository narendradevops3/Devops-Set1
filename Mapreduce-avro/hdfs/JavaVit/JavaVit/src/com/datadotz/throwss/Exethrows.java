package com.datadotz.throwss;

public class Exethrows {
	static void demo() throws ArithmeticException {
		int a = 10;
		int b = 0;
		int c = a / b;
		System.out.println("Result" + c);
		/*
		 * try { throw new NullPointerException("divide by zero"); } catch
		 * (NullPointerException e) {
		 * System.out.println("caught inside demo proc:"); throw e;
		 */
		// throw new NullPointerException("demo");
	}

	// }

	public static void main(String args[]) {
		try {
			demo();
		} catch (ArithmeticException e) {
			System.out.println("Recaught:" + e);
		}
	}

}
