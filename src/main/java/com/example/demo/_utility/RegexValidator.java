package com.example.demo._utility;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RegexValidator {

	// 리팩토링 한 메서드
	public int toInt(int basic,String ext , String text) {
		int intNum = basic;
		if(text == null) { return intNum; }
		
		if(text.matches(ext) ) {
			intNum = Integer.parseInt(text);
		}else {
			log.warn("ExpValidator no validate.");
		    //throw new RuntimeException("ExpValidator no validate.");
		    return intNum;
		}
	    return intNum;
	}
}
