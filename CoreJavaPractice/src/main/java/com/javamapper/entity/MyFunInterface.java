package com.javamapper.entity;

@FunctionalInterface
public interface MyFunInterface <T,R>{
	R collectValue(T op1);
	default void printValue(T input,R output) {
		System.out.println(input+":"+output);
	}
}
