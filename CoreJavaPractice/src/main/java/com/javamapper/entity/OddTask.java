package com.javamapper.entity;

import com.javamapper.common.SharedProcessor;

public class OddTask extends Thread{

	int maxLimit;
	SharedProcessor sharedProcessor;
	
	public OddTask(int maxLimit,SharedProcessor sharedProcessor) {
		this.maxLimit = maxLimit;
		this.sharedProcessor = sharedProcessor;
	}
	@Override
	public void run() {
		int minOddNumber = 1;
		while(minOddNumber<=maxLimit) {
			sharedProcessor.printOddNumber(minOddNumber);
			minOddNumber = minOddNumber + 2;
		}
	}
}
