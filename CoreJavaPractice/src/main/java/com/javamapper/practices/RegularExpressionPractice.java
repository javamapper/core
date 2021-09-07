package com.javamapper.practices;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegularExpressionPractice {
	private static Logger logger = LoggerFactory.getLogger(RegularExpressionPractice.class);
	
	public void regExGrouping() {
		String strr="ram34 gyyan65.4 eree56,000 git65    hlo67 h4333 9987rewr";
		if(logger.isInfoEnabled()) {
			logger.info("value : {}",strr.replaceAll("(?<charGrp>[a-z]*)(?<digitGrp>[0-9.,]*)","${digitGrp}${charGrp}"));// we can use "$2$1" in this expression
		}
		/** when using group with name then replacement expression should refer by name like ${name-group}
		// 1. Each group defined inside () and it is boundary of group expression
		// 2. We are using expression ?<group-name> for group with name.. It is not require to define name of group
		// 3. [a-z]* Character set using a-z of length>=0
		// 4. [0-9]* digit set using 0-9 of length>=0
		// 5. [0-9.,]* digit set using 0-9 or . or , of length>=0
		 * 
		 */
		if(logger.isInfoEnabled()) {
			logger.info(strr.replaceAll("([a-z]*)([0-9.,]*)","$2$1"));
		}
		// 6. group without name so replacement expression refer with number
		
		
		//Question updated :- replacement for string which have character set length=3 and digit length=2
		if(logger.isInfoEnabled()) {
			logger.info(strr.replaceAll("([^\\d\\W]{3})(\\d{2})(\\s+)","$2$1$3")); 
		}
		// I am using meta-character ... \w means [a-zA-Z_0-9]... This will allow digit in string for length-3 .. //34ram gyyan65.4 eree56,000 65git	67hlo 33h43 9987rewr
		// you can notice result of h4333. It will be correct either use [a-zA-Z] or 
		// [^\\d\\W] ^\d means non-digit & ^\W means ^^\w => \w .. Finally, \w & ^\d
		
		// This pattern I defined 3 group and arranged such that after digit \\s(whitespace character) will come otherwise it disturb other string
		//
		//same example and result by using set intersection ... \\w keeping [a-zA-Z_0-9] if I drop [0-9] & _ from /w set then we will get [a-zA-Z]
		if(logger.isInfoEnabled()) {
			logger.info(strr.replaceAll("([\\w&&[^\\d_]]{3})(\\d{2})(\\s+)","$2$1$3")); 
		}
		//
		if(logger.isInfoEnabled()) {
			logger.info("Hello, Anil".replaceAll("(\\w)([\\.,])(\\s+)", "$1$3"));
		}
		// Remove whitespace between word and , just like trim spaces which is between word and ,
		if(logger.isInfoEnabled()) {
			logger.info("hello  ,".replaceAll("(\\w)(\\s+)([\\,])", "$1$3"));
		}
		
		// A String start with number then push it back/last
		if(logger.isInfoEnabled()) {
			logger.info("9003Anil hello654 98Rana goo5442aa".replaceAll("(^|\\s+)([0-9]+)([a-zA-Z]+)(\\s+)","$1$3$2$4"));
		
			logger.info("tewtw@gm".replaceAll("([a-zA-Z]{7})(\\@)(\\w+)","$1")); 
		}
		// Word is first of line or last of line or in between independently. 
		//(^|\s)Hello($|\s)
		strr="Hello how are you?"; // "Anil, Hello buddy" "He said Hello"
		boolean matches = Pattern.matches("((^|\\s)Hello($|\\s))", strr);
		
		if(matches) {
			logger.info("matched");
		}
	}
}
