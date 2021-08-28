package com.javamapper.entity;

@FunctionalInterface
public interface ConsumerSupplier <I,R>{
	R process(I input);
}
