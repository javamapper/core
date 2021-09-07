package com.javamapper.common;

import static com.javamapper.common.AppConstants.ERROR_LOG;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utility {
	
	private Utility() {
		
	}
	private static Logger logger = LoggerFactory.getLogger(Utility.class);
	

	public static Runnable getProducerBlockingQueue(BlockingQueue<Integer> queue,int initialValue,String logName,int sleepVal) {
		return () -> {
			try {
				int value=initialValue;
				while(true) {
					queue.put(value);
					logger.info("{} :- produced {}",logName,value);
					value++;
					Thread.sleep(sleepVal);
				}
			}catch(InterruptedException e) {
				logger.error(ERROR_LOG,ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			}
		};
	}
	
	public static Runnable getConsumerBlockingQueue(BlockingQueue<Integer> queue,String logName,int sleepVal) {
		return () -> {
			try {
				while (true) {
					int value = queue.take();
					logger.info("{} :- consumed {}",logName,value);
					Thread.sleep(sleepVal);
				}
			} catch (InterruptedException e) {
				logger.error(ERROR_LOG,ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			}
		};
	}
}
