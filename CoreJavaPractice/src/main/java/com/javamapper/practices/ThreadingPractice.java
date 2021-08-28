package com.javamapper.practices;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.javamapper.entity.MessageQueue;

public class ThreadingPractice {

	public void basicThreadCreation() throws InterruptedException {
		Thread currentThread = Thread.currentThread();
		System.out.println("--*** job start thread:\t" + currentThread.getName());
		System.out.println("--- job in process ---" + currentThread.getName());
		Thread th = new Thread(() -> {
			Thread childTh = Thread.currentThread();
			try {
				currentThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("--&&& job start thread:\t" + childTh.getName());
			System.out.println("---### \t job in process ---");
			System.out.println("--&&& job end thread: \t" + childTh.getName());
		});
		th.setName("child-thread");
		th.start();

		System.out.println("--*** job end thread:\t" + currentThread.getName());
	}

	public void interThreadCommuncation() {

		MessageQueue<Integer> queue = new MessageQueue<Integer>(2);

		try {
			// Thread 1- Producer
			Thread producerThread = new Thread(() -> {
				queue.producer((str) -> Integer.valueOf(str), 0);
			});
			// Thread 2- Consumer
			Thread consumerThread = new Thread(() -> {
				queue.consumer();
			});
			producerThread.start();
			consumerThread.start();
			producerThread.join();
			consumerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void interThreadCommuncationUsingBlockingQueue() {

		BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(10);

		try {
			// Thread 1- Producer
			Thread producerThread = new Thread(() -> {
				try {
					int value=0;
					while(true) {
						queue.put(value);
						System.out.println("Producer :- produced "+value);
						value++;
						Thread.sleep(1000);
					}
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			});
			// Thread 2- Consumer
			Thread consumerThread1 = new Thread(() -> {
				try {
					while (true) {
						int value = queue.take();
						System.out.println("Consumer1 :- consumed "+value);
						Thread.sleep(2000); // What will be output here, 2000 for consumer but producer keeping 1000
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			 //SynchronousQueue<E>
			/*
			 * When we introduced thread-3 as another consumer then,
			 * We will find inconsistency in consumer end because here is two thread from consumer side try to enter into 
			 * Synchronous block/method 
			 */
			// Thread 3- Consumer
			/*Thread consumerThread2 = new Thread(() -> {
				try {
					while (true) {
						int value = queue.take();
						System.out.println("Consumer2 :- consumed "+value);
						Thread.sleep(2000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});*/
			producerThread.start();
			consumerThread1.start();
			//consumerThread2.start();
			producerThread.join();
			consumerThread1.join();
			//consumerThread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void interThreadCommThreadPoolBlockingQueue() {
		BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<Integer>();
		ExecutorService executor = Executors.newFixedThreadPool(2);
		
		// Executor executor2= Executors.newFixedThreadPool(2,
		// Executors.defaultThreadFactory());
		Runnable producer = () -> {
			int value = 0;
			try {
				while (true) {
					blockingQueue.put(++value);
					System.out.println("Producer produced:-" + value);
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		Runnable consumer = () -> {
			try {
				while (true) {
					Integer value = blockingQueue.take();
					System.out.println("Consumer consumed:-" + value);
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		executor.execute(producer);
		executor.execute(consumer);
		executor.shutdown();
	}
}
