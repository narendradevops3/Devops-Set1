package com.datadotz.interfaces;

interface printable {
	void print();

//void display();

	void hai();
}

class Inter implements printable {
	public void print() {
		System.out.println("Hello");
	}

	public void hai() {
		System.out.println("Hello");
	}
	
	public void hello() {
		System.out.println("Hello");
	}

	public void other() {
		System.out.println("i m extra");
	}

	public static void main(String args[]) {
		Inter obj = new Inter();
		obj.print();
		obj.hai();
		obj.other();
		obj.hello();

	}
}
