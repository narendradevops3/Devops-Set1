package com.datadotz.java.training.staticmethod;

public class Student {
	int rollno;
	String name;
	static String college = "SRM";

	static void change() {
		college = "VIT";
	}

	Student(int r, String n) {
		rollno = r;
		name = n;
	}

	void display() {
		System.out.println(rollno + " " + name + " " + college);
	}

	public static void main(String args[]) {
		Student.change();

		Student s1 = new Student(111, "Karan");
		Student s2 = new Student(222, "Aryan");
		Student s3 = new Student(333, "Sonoo");

		s1.display();
		s2.display();
		s3.display();
	}
}
