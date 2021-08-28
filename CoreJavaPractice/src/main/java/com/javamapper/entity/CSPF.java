package com.javamapper.entity;

import java.util.function.Predicate;

@FunctionalInterface
public interface CSPF<I,R,F> {
	R process(I input,Predicate<I> inputPredicate,Predicate<R> outputPredicate,Predicate<F> filterPredicate);
}
