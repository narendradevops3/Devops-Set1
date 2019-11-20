package com.datadotz.inheritance;

public class Inhe {

	int salary = 50000;

}

class Pro extends Inhe {
	int bonus = 20000;

	public static void main(String args[]) {
		Pro p = new Pro();

		System.out.println("Programmer  is" + p.salary);
		System.out.println("Programmer bonus is" + p.bonus);

	}
}