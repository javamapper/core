package com.javamapper.entity;

import static com.javamapper.common.AppConstants.CONTAIN_LOG;
import static com.javamapper.common.AppConstants.ERROR_LOG;
import static com.javamapper.common.AppConstants.READ_VALUE_LOG;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileActionReadWrite {
	private static Logger logger = LoggerFactory.getLogger(FileActionReadWrite.class);
	Lock readLock;
	Lock writeLock;
	List<String> fileLines;
	public FileActionReadWrite(ReadWriteLock readWriteLock,List<String> fileLines) {
		this.readLock = readWriteLock.readLock();
		this.writeLock = readWriteLock.writeLock();
		this.fileLines = fileLines;
	}
	/**
	 * Write. In last 
	 */
	public void write(String line) {
		writeLock.lock();
		try {
			logger.info("write {}",line);
			fileLines.add(line);
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}finally {
			writeLock.unlock();
		}
	}

	/**
	 * Remove. In last
	 */
	public void remove() {
		writeLock.lock();
		try {
			int index = fileLines.size()-1;
			logger.info("remove value {}",fileLines.get(index));
			fileLines.remove(index);
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
		}finally {
			writeLock.unlock();
		}
	}

	/**
	 * Read. From last
	 */
	public String read(){
		readLock.lock();
		try {
			int index = fileLines.size()-1;
			logger.info(READ_VALUE_LOG,fileLines.get(index));
			TimeUnit.SECONDS.sleep(1);
			return fileLines.get(index);
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
			return "";
		}finally {
			readLock.unlock();
		}
	}

	/**
	 * Read all lines.
	 * @throws InterruptedException 
	 */
	public boolean contains(String line){
		//We can use tryLock here
		/***
		 * boolean isLockObtained = readLock.tryLock(1, TimeUnit.SECONDS);
			if(isLockObtained) {
				//perform task because lock obtained
			}*/
		readLock.lock();
		try {
			boolean contains = fileLines.contains(line);
			logger.info(CONTAIN_LOG,line,contains);
			TimeUnit.SECONDS.sleep(1);
			return contains;
		} catch (InterruptedException e) {
			logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
			Thread.currentThread().interrupt();
			return false;
		}finally {
			readLock.unlock();
		}
	}

}
