package com.javamapper.entity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;

public class MessageQueue<T> {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	PrintWriter wr = new PrintWriter(System.out);
	private Queue<T> listQ;
	private boolean breakExec = false;
	private int size;

	public MessageQueue(int size) {
		this.listQ = new LinkedList<>();
		this.size = size;
	}

	public void producer(Function<String, T> convertion, T exitValue) {
		while (true) {
			synchronized (this) {
				try {
					while (listQ.size() >= size) {
						wait();
					}
					System.out.println("Waiting input to produce data...:");
					String readValue = br.readLine();
					T convertedValue = convertion.apply(readValue);
					listQ.add(convertedValue);
					System.out.println("Producer produced:- " + convertedValue);
					notify();
					if (convertedValue.equals(exitValue)) {
						breakExec = true;
						break;
					}
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public synchronized void consumer() {
		while (true) {
			synchronized (this) {
				try {
					if (breakExec) {
						break;
					}
					while (listQ.size() == 0) {
						wait();
					}
					T value = listQ.poll();
					System.out.println("Consumer consuming:- " + value);
					notify();
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
