package com.javamapper.common;

public class AppConstants {
	private AppConstants() {
		
	}
	public static final String ERROR_LOG = "ERROR {}";
	public static final String VALUE_LOG = "VALUE {}";
	public static final String REMOVE_INT = "remove-%d";
	public static final String WRITE_INT = "write-%d";
	public static final String CONTAIN_LOG = "contains {}:{}";
	public static final String READ_VALUE_LOG = "read value {}";
	public enum PAYMENT_TYPE{DEBIT,CREDIT}
}
