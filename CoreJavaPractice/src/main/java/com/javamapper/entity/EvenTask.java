package com.javamapper.entity;

import com.javamapper.common.SharedProcessor;

public class EvenTask extends Thread{
	int maxLimit;
	SharedProcessor sharedProcessor;
	
	public EvenTask(int maxLimit,SharedProcessor sharedProcessor) {
		this.maxLimit = maxLimit;
		this.sharedProcessor = sharedProcessor;
	}
	@Override
	public void run() {
		int minEven = 2;
		while(minEven<=maxLimit) {
			sharedProcessor.printEvenNumber(minEven);
			minEven = minEven + 2;
		}
	}

}
