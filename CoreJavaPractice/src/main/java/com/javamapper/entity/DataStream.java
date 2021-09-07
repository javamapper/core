package com.javamapper.entity;

import static com.javamapper.common.AppConstants.ERROR_LOG;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataStream {
	private static Logger logger = LoggerFactory.getLogger(DataStream.class);
	private String dataReference;
	private boolean dataTransfer;
	
	public synchronized void send(String dataRef) {
		while(!dataTransfer) {
			try {
				wait();
			}catch (InterruptedException e) {
				logger.error(ERROR_LOG,ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			}
		}
		dataTransfer = false;
		this.dataReference = dataRef;
		notifyAll();
	}
	
	public synchronized String receive() {
		while(dataTransfer) {
			try {
				wait();
			}catch (InterruptedException e) {
				logger.error(ERROR_LOG,ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
			}
		}
        dataTransfer = true;
        notifyAll();
        return dataReference;
	}
}
