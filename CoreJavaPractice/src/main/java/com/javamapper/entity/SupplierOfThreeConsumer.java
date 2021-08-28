package com.javamapper.entity;

@FunctionalInterface
public interface SupplierOfThreeConsumer <I1,I2,I3,R>{
	 R process(I1 input1,I2 input2,I3 input3);
}
