package com.datadotz.abstraction;

abstract class Abstrac {

	abstract void run();

	{
		System.out.println("hai im first");
	}

	void print() {

	}

}

class Im extends Abstrac {

	void run() {
		System.out.println("hai im running");
	}

	public static void main(String args[]) {
		Im obj = new Im();
		obj.run();
	}

}
