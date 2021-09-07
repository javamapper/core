package com.javamapper.practices;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.IntBinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javamapper.common.AppConstants;
import com.javamapper.entity.CSPF;
import com.javamapper.entity.ConsumerSupplier;
import com.javamapper.entity.MyFunInterface;

public class UtilFunctionPractice {
	private static Logger logger = LoggerFactory.getLogger(UtilFunctionPractice.class);
	/**
	 * Functional interface use.
	 */
	public void functionalInterfaceUse() {
		String val="134.90";
		Double valDouble=136.90;
		Long valLong=145l;
		// for BigDecimal
		MyFunInterface<Object, BigDecimal> bigDecimalC= obj ->{
			if(obj instanceof String) {
			return BigDecimal.valueOf(Double.valueOf(String.valueOf(obj)));
			}else if(obj instanceof Double) {
				return BigDecimal.valueOf((Double)obj);
			}else if(obj instanceof Long) {
				return BigDecimal.valueOf((Long)obj);
			}else {
				return null;
			}
		};
		// for Long
		MyFunInterface<Object, Long> longC = obj->{
			if(obj instanceof String) {
			return Long.valueOf(String.valueOf(obj));
			}else if(obj instanceof Long) {
				return (Long)obj;
			}else if(obj instanceof Integer){
				return Long.valueOf(""+obj);
			}else {
				return null;
			}
		};
		bigDecimalC.printValue(val, bigDecimalC.collectValue(val));
		
		logger.info("Double value from String: {} , Double value from Double: {}, Double value from Long: {}",bigDecimalC.collectValue(val),bigDecimalC.collectValue(valDouble),bigDecimalC.collectValue(valLong));
		
		logger.info("Double value from String: {},   Double value from Long: {}",longC.collectValue("1332"),longC.collectValue(4321));
	}

	/**
	 * Consumer use.
	 */
	public void consumerUse() {
		Consumer<List<String>> jobProcessOnList = obj -> {
			obj.add("Anil01");
			obj.add("Sumit01");
			obj.add("Raju01");
			obj.add("Kumar01");
		};
		Consumer<List<String>> processNext = jobProcessOnList.andThen(list -> {
			list.add("Anil02");
			list.add("Sumit02");
			list.add("Raju02");
			list.add("Kumar02");
		});

		List<String> strList = new ArrayList<>();
		 
		processNext.accept(strList);
		logger.info(AppConstants.VALUE_LOG,strList);
	}
	
	/**
	 * Supplier use.
	 */
	public void supplierUse() {
		Supplier<List<String>> filledDataBySupplier = ()->{
			List<String> obj=new ArrayList<>();
			obj.add("Anil01");
			obj.add("Sumit01");
			obj.add("Raju01");
			obj.add("Kumar01");
			return obj;
		};
		logger.info(AppConstants.VALUE_LOG,filledDataBySupplier.get());
	}
	
	/**
	 * Supplier and consumer use.
	 */
	public void consumerSupplierUse() {
		ConsumerSupplier<String,List<String>> getListOfString = str-> Arrays.asList(str.split(","));
		logger.info(AppConstants.VALUE_LOG,getListOfString.process("anil,amit,sunil,ajay,ravi").stream().filter(str->str.contains("n")).collect(Collectors.toList()));
	}
	
	public void consumerSupplierPredicateUse() {
		CSPF<String, List<String>,String> csp=(input,pre,post,filter)->{
			if(!pre.test(input)) {
				return Collections.emptyList();
			}
			List<String> returnList = Arrays.asList(input.split(","));
			if(!post.test(returnList)) {
				return Collections.emptyList();
			}
			return returnList.stream().filter(filter).collect(Collectors.toList());
		};
		List<String> result = csp.process("anil,amit,ankit,raju,kumar,ravi", 
										  str->str.contains(","), // input value predicate
										  list->list.contains("ravi"), // output value predicate
										  filter->filter.contains("n")); //filter in output value
		logger.info(AppConstants.VALUE_LOG,result);
	}
	
	/**
	 * Binary operator.
	 */
	public void binaryOperator() {
		IntBinaryOperator intBinaryOperator=(op1,op2)->op1+op2;
		logger.info("value {}",intBinaryOperator.applyAsInt(30, 80));
		BinaryOperator<BigDecimal> getProcessAmount=(op1,op2)-> op2.max(op1.divide(BigDecimal.TEN));
		BigDecimal op1=new BigDecimal("300");
		BigDecimal op2=new BigDecimal("20");
		BinaryOperator<BigDecimal> maxBinaryOp = BinaryOperator.maxBy((Comparator<BigDecimal>) (input1,input2)-> input1.compareTo(input2));
		/*** maxBinaryOp=(input1,input2)->{
			return input1.divide(input2);
		};*/
		logger.info("value {} {}",getProcessAmount.apply(op1, op2),maxBinaryOp.apply(op1,op2));
	}
}
