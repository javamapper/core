package com.javamapper.practices;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class CollectionPractice {

	public void concurrentNavMapPractice() {
		ConcurrentNavigableMap<String,String> concurrentNavigableMap = new ConcurrentSkipListMap<>();
		concurrentNavigableMap.put("e001", "Anil");
		concurrentNavigableMap.put("e002", "Sumit");
		concurrentNavigableMap.put("e003", "Jon");
		concurrentNavigableMap.put("e004", "Rana");
		concurrentNavigableMap.put("e005", "Tom");
		concurrentNavigableMap.put("e006", "Amit");
		ConcurrentNavigableMap<String,String> headMap = concurrentNavigableMap.headMap("e003");
		System.out.println(headMap);
		ConcurrentNavigableMap<String,String> tailMap = concurrentNavigableMap.tailMap("e004");
		System.out.println(tailMap);
		ConcurrentNavigableMap<String,String> subMap = concurrentNavigableMap.subMap("e002","e005");
		System.out.println(subMap);
		String valueOfE010 = concurrentNavigableMap.getOrDefault("e004", "default-value"); // introduce in Java8
		System.out.println(valueOfE010);
	}

	public void hashMapExerciseCharCount() {
		String str = "Hello world";
		Map<Character, Integer> numMap = new LinkedHashMap<>();
		str.chars().mapToObj(c->Character.valueOf((char)c)).forEach(c -> numMap.put(c, numMap.get(c) == null ? 1 : numMap.get(c) + 1));
		Optional<Entry<Character, Integer>> max = numMap.entrySet().stream().max((x,y)->Integer.valueOf(x.getValue())-Integer.valueOf(y.getValue()));
		System.out.println(max.get());
		System.out.println(numMap);
	}
	
	public void hashMapExerciseWordCount() {
		String str = "Hello, good to see you again. Today is good day for us, we are connected here for a goal. Goal is near to destination. I am happy to see, goal to near to complete.";
		Map<String, Integer> numMap = new LinkedHashMap<>();
		Arrays.asList(str.split(" ")).stream()
									 .map(s->s.replaceAll("(\\w)([\\.,]$)", "$1"))
									 .forEach(st->numMap.put(st, numMap.get(st)==null?1:numMap.get(st)+1));
		Optional<Entry<String, Integer>> max = numMap.entrySet().stream().max((x,y)->Integer.valueOf(x.getValue())-Integer.valueOf(y.getValue()));
		System.out.println(max.get());
		System.out.println(numMap);
	}
}
