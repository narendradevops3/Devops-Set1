package com.datadotz.polymorphism;

class Mthdoverr{

   public void move(){
      System.out.println("Animals can move");
   }
}

class Dog extends Mthdoverr{

   public void move(){
     // invokes the super class method
      System.out.println("Dogs can walk and run");
   }
//   void move1(){
//	     // invokes the super class method
//	      System.out.println("Dogs can walk and run");
//	      move();
//	   }

}
 class TestDog{

   public static void main(String args[]){
   Dog b=new Dog();
	  // mthdoverr b = new Dog(); // Animal reference but Dog object
     // b.move(); //Runs the method in Dog class
	  b.move();

   }
}
