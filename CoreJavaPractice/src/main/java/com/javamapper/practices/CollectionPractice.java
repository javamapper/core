package com.javamapper.practices;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionPractice {
	private static Logger logger = LoggerFactory.getLogger(CollectionPractice.class);
	public void concurrentNavMapPractice() {
		ConcurrentNavigableMap<String,String> concurrentNavigableMap = new ConcurrentSkipListMap<>();
		concurrentNavigableMap.put("e001", "Anil");
		concurrentNavigableMap.put("e002", "Sumit");
		concurrentNavigableMap.put("e003", "Jon");
		concurrentNavigableMap.put("e004", "Rana");
		concurrentNavigableMap.put("e005", "Tom");
		concurrentNavigableMap.put("e006", "Amit");
		ConcurrentNavigableMap<String,String> headMap = concurrentNavigableMap.headMap("e003");
		logger.info("{}",headMap);
		ConcurrentNavigableMap<String,String> tailMap = concurrentNavigableMap.tailMap("e004");
		logger.info("{}",tailMap);
		ConcurrentNavigableMap<String,String> subMap = concurrentNavigableMap.subMap("e002","e005");
		logger.info("{}",subMap);
		String valueOfE010 = concurrentNavigableMap.getOrDefault("e004", "default-value"); // introduce in Java8
		logger.info(valueOfE010);
	}

	public void hashMapExerciseCharCount() {
		String str = "Hello world";
		Map<Character, Integer> numMap = new LinkedHashMap<>();
		str.chars().mapToObj(c->Character.valueOf((char)c)).forEach(c -> numMap.put(c, numMap.get(c) == null ? 1 : numMap.get(c) + 1));
		Optional<Entry<Character, Integer>> max = numMap.entrySet().stream().max((x,y)->Integer.valueOf(x.getValue())-Integer.valueOf(y.getValue()));
		max.ifPresent(val->logger.info("{}",val));
		logger.info("{}",numMap);
	}
	
	public void hashMapExerciseWordCount() {
		String str = "Hello, good to see you again. Today is good day for us, we are connected here for a goal. Goal is near to destination. I am happy to see, goal to near to complete.";
		Map<String, Integer> numMap = new LinkedHashMap<>();
		Arrays.asList(str.split(" ")).stream()
									 .map(s->s.replaceAll("(\\w)([\\.,]$)", "$1"))
									 .forEach(st->numMap.put(st, numMap.get(st)==null?1:numMap.get(st)+1));
		Optional<Entry<String, Integer>> max = numMap.entrySet().stream().max((x,y)->x.getValue()-y.getValue());
		max.ifPresent(val->logger.info("{}",val));
		logger.info("{}",numMap);
	}
	
	public void excerisePrintFibonacci() {
		Stream.iterate(new int[] {0,1},n->new int[] {n[1],n[0]+n[1]})
			  .limit(20)
			  .map(n->n[0])
			  .forEach(val->logger.info("{}",val));
	}
	
	//1,3,5,7,9
	public void excerisePrintOddNumbers() {
		//1 Approach
		Stream.iterate(1,n->n+1)
			  .map(n->2*n-1)
			  .limit(20)
			  .forEach(val->logger.info("{}",val));
		//2 Approach
		Stream.iterate(1,n->n+1)
			  .filter(n->n%2!=0)
			  .limit(20)
			  .forEach(val->logger.info("{}",val));
	}
	
	
	public static void main(String[] args) {
		CollectionPractice tp = new CollectionPractice();
		tp.hashMapExerciseWordCount();
	}
}
