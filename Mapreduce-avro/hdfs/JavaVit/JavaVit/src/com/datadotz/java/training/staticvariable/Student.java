package com.datadotz.java.training.staticvariable;//static variable

public class Student {
	int rollno;
	String name;
	static String college = "VIT";

	Student(int r, String n) {
		rollno = r;
		name = n;
	}

	void display() {
		System.out.println(rollno + " " + name + " " + college);
	}

	public static void main(String args[]) {
		Student s1 = new Student(111, "Karan");
		Student s2 = new Student(222, "Aryan");

		s1.display();
		s2.display();
	}
}
