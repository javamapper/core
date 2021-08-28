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

import com.javamapper.entity.CSPF;
import com.javamapper.entity.ConsumerSupplier;
import com.javamapper.entity.MyFunInterface;

public class UtilFunctionPractice {

	/**
	 * Functional interface use.
	 */
	public void functionalInterfaceUse() {
		String val="134.90";
		Double valDouble=136.90;
		Long valLong=145l;
		// for BigDecimal
		MyFunInterface<Object, BigDecimal> bigDecimalC=(obj)->{
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
		MyFunInterface<Object, Long> longC=(obj)->{
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
		
		System.out.println("Double value from String:"+bigDecimalC.collectValue(val)+
				"  Double value from Double:"+bigDecimalC.collectValue(valDouble)+
				"  Double value from Long:"+bigDecimalC.collectValue(valLong));
		
		System.out.println("Double value from String:"+longC.collectValue("1332")+
				"  Double value from Long:"+longC.collectValue(4321));
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
		System.out.println(strList);
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
		System.out.println(filledDataBySupplier.get());
	}
	
	/**
	 * Supplier and consumer use.
	 */
	public void consumerSupplierUse() {
		ConsumerSupplier<String,List<String>> getListOfString = (str)->{
			return Arrays.asList(str.split(","));
		};
		System.out.println(getListOfString.process("anil,amit,sunil,ajay,ravi").stream().filter(str->str.contains("n")).collect(Collectors.toList()));
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
		System.out.println(result);
	}
	
	/**
	 * Binary operator.
	 */
	public void binaryOperator() {
		IntBinaryOperator intBinaryOperator=(op1,op2)->op1+op2;
		System.out.println(intBinaryOperator.applyAsInt(30, 80));
		BinaryOperator<BigDecimal> getProcessAmount=(op1,op2)->{
			return op2.max(op1.divide(BigDecimal.TEN));
		};
		BigDecimal op1=new BigDecimal("300");
		BigDecimal op2=new BigDecimal("20");
		BinaryOperator<BigDecimal> maxBinaryOp = BinaryOperator.maxBy((Comparator<BigDecimal>) (input1,input2)->{
			return input1.compareTo(input2);
		});
		/*maxBinaryOp=(input1,input2)->{
			return input1.divide(input2);
		};*/
		System.out.println(getProcessAmount.apply(op1, op2)+"  "+maxBinaryOp.apply(op1,op2));
	}
}
