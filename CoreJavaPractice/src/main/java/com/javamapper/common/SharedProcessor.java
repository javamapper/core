package com.javamapper.common;

import static com.javamapper.common.AppConstants.ERROR_LOG;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SharedProcessor {
	private static Logger logger = LoggerFactory.getLogger(SharedProcessor.class);
	boolean isOddPrint = false;
	
	/**
	 * Prints the odd number.
	 *
	 * @param number the number
	 */
	public synchronized void printOddNumber(int number) {
		while(isOddPrint) {
			try {
				wait();
			} catch (InterruptedException e) {
				logger.error(ERROR_LOG,ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			}
		}
		isOddPrint = true;
		Thread.currentThread().setName("Odd Thread ::");
		logger.info("{} - {}",Thread.currentThread().getName(), number);
		notifyAll();
	}
	
	/**
	 * Prints the even number.
	 *
	 * @param number the number
	 */
	public synchronized void printEvenNumber(int number) {
		while(!isOddPrint) {
			try {
				wait();
			} catch (InterruptedException e) {
				logger.error(ERROR_LOG,ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			}
		}
		isOddPrint = false;
		Thread.currentThread().setName("Even Thread ::");
		logger.info("{} - {}",Thread.currentThread().getName(), number);
		notifyAll();
	}
}
