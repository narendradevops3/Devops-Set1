package com.datadotz.collection.queue;

import java.util.Iterator;
import java.util.PriorityQueue;

public class Queueexample {
	public static void main(String args[]) {
		PriorityQueue queue = new PriorityQueue();
		queue.add("Amit");
		queue.add("Vijay");
		queue.add("Karan");
		queue.add("Jai");
		queue.add("Rahul");

		System.out.println("head:" + queue.element());
		System.out.println("head:" + queue.peek());

		System.out.println("iterating the queue elements:");
		Iterator itr = queue.iterator();
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}

		queue.remove();
		queue.poll();

		System.out.println("after removing two elements:");
		Iterator itr2 = queue.iterator();
		while (itr2.hasNext()) {
			System.out.println(itr2.next());
		}

	}

}
