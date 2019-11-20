package com.datadotz.exception;

public class Exceptionexample {
	public static void main(String args[]) {
		try {
			int data = 50 / 0;
			int c[] = { 2 };// int array c has length of 1
			c[42] = 99;
			// c[34]=12;

		} catch (ArithmeticException e) {
			System.out.println("Divide by 0:" + e);

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("array index oob" + e);
		}

		finally {
			System.out.println("i always executed");
		}
		System.out.println("rest of the code");
	}

}
