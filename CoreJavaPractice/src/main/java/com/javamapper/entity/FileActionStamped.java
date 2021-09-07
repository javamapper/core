package com.javamapper.entity;

import static com.javamapper.common.AppConstants.CONTAIN_LOG;
import static com.javamapper.common.AppConstants.ERROR_LOG;
import static com.javamapper.common.AppConstants.READ_VALUE_LOG;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileActionStamped {
	private static Logger logger = LoggerFactory.getLogger(FileActionStamped.class);
	StampedLock stampedLock;
	List<String> fileLines;
	public FileActionStamped(StampedLock stampedLock,List<String> fileLines) {
		this.stampedLock = stampedLock;
		this.fileLines = fileLines;
	}
	/**
	 * Write. In last 
	 */
	public void write(String line) {
		long stamp = stampedLock.writeLock();
		try {
			logger.info("write {}",line);
			fileLines.add(line);
		}finally {
			stampedLock.unlock(stamp);
		}
	}

	/**
	 * Remove. In last
	 */
	public void remove() {
		//tryConvertToReadLock
		long stamp = stampedLock.writeLock();
		try {
			int index = fileLines.size()-1;
			logger.info("remove value {}",fileLines.get(index));
			fileLines.remove(index);
		}finally {
			stampedLock.unlock(stamp);
		}
	}

	/**
	 * Read. From last
	 */
	public String read(){
		long stamp = stampedLock.readLock();
		try {
			int index = fileLines.size()-1;
			logger.info(READ_VALUE_LOG,fileLines.get(index));
			return fileLines.get(index);
		}finally {
			stampedLock.unlock(stamp);
		}
	}

	public Optional<String> readOffWhenWriterOn(){
		long stamp = stampedLock.tryOptimisticRead();
		// This mode can be thought of as an extremely weak version of a read-lock, that can be broken by a writer at any time.
		if(!stampedLock.validate(stamp)) {
			stamp = stampedLock.readLock();
			try {
				int index = fileLines.size()-1;
				logger.info(READ_VALUE_LOG,fileLines.get(index));
				return Optional.of(fileLines.get(index));
			}finally {
				stampedLock.unlock(stamp);
			}
		}
		return Optional.ofNullable(null);
	}

	/**
	 * Read all lines.
	 * @throws InterruptedException 
	 */
	public boolean contains(String line){
		long stamp = stampedLock.readLock();
		//tryConvertToWriteLock
		try {
			boolean contains = fileLines.contains(line);
			logger.info(CONTAIN_LOG,line,contains);
			return contains;
		}finally {
			stampedLock.unlock(stamp);
		}
	}

	/**
	 * Contains off when writer on.
	 *
	 * @param line the line
	 * @return the optional
	 */
	public Optional<Boolean> containsOffWhenWriterOn(String line) {
		long stamp = stampedLock.tryOptimisticRead();
		//stampedLock.getReadLockCount()
		//stampedLock.isReadLocked()
		//stampedLock.isWriteLocked() 
		// This mode can be thought of as an extremely weak version of a read-lock, that can be broken by a writer at any time.
		logger.info(" -------- stamp:{} - readLock:{} - writeLock:{}",stamp,stampedLock.isReadLocked(),stampedLock.isWriteLocked());
		if(!stampedLock.validate(stamp)) {
			stamp = stampedLock.readLock();
			try {
				boolean contains = fileLines.contains(line);
				logger.info(CONTAIN_LOG,line,contains);
				TimeUnit.SECONDS.sleep(1);
				return Optional.of(contains);
			} catch (InterruptedException e) {
				logger.error(ERROR_LOG, ExceptionUtils.getStackTrace(e));
				Thread.currentThread().interrupt();
				return Optional.of(false);
			}finally {
				stampedLock.unlock(stamp);               
			}
		}
		return Optional.ofNullable(null);
	}
}
