package com.javamapper.main;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class App {
	private static final Scanner scanner = new Scanner(System.in);
	final private static String PROPERTIES_FILE_PATH="programIndexList.properties";
	
	public static void main(String[] args) {
		
		Properties properties = new Properties();
		try (InputStream inputStream = App.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_PATH)) {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("--- Need to modify input values then either frame code to take input from "
				+ "user in repective class Or Update given values in class ---");
		System.out.println("Give your selection in number:");
		Set<Entry<Object, Object>> propValueSet = properties.entrySet();
		propValueSet.stream()
					.map(entry->String.valueOf(entry.getValue()))
					.sorted((str1,str2)->{
						String strArr1=str1.split(" ")[0];
						String strArr2=str2.split(" ")[0];
						return Integer.valueOf(strArr1.substring(0, strArr1.length()-1))-Integer.valueOf(strArr2.substring(0, strArr2.length()-1));
					})
					.forEach(System.out::println);
		String indexVal = scanner.nextLine();
		Optional<Entry<Object, Object>> propSelection = propValueSet.stream()
																	.filter(val->{
																		String[] strArr=((String)val.getValue()).split(" ");
																		return strArr[0].substring(0, strArr[0].length()-1).equals(indexVal);
																	}).findFirst();
		propSelection.ifPresent(propValEntry -> {
			String key = String.valueOf(propValEntry.getKey());
			String className = key.substring(0, key.lastIndexOf('.'));
			String methodName = key.substring(key.lastIndexOf('.') + 1);
			try {
				Object object = Class.forName(className).newInstance();
				Method declaredMethod = object.getClass().getDeclaredMethod(methodName);
				declaredMethod.invoke(object);
			} catch (InstantiationException 
					| IllegalAccessException 
					| ClassNotFoundException 
					| NoSuchMethodException
					| SecurityException 
					| IllegalArgumentException 
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		});
	}
}
