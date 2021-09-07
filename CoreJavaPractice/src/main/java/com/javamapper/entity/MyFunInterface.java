package com.javamapper.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FunctionalInterface
public interface MyFunInterface <T,R>{
	R collectValue(T op1);
	static Logger logger = LoggerFactory.getLogger(MyFunInterface.class);
	default void printValue(T input,R output) {
		logger.info("{} : {}",input,output);
	}
}
