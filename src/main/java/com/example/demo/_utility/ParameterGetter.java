package com.example.demo._utility;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ParameterGetter {

	public void getHeaders(HttpServletRequest req) {
		log.info("getHeaders ============= ");
		StringBuffer sb= new StringBuffer(); 
		Enumeration headerNames = req.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String name = (String)headerNames.nextElement();
            String value = req.getHeader(name);
            sb.append(name + " : " + value + "<br>");
            log.info(name + " : " + value + "<br>");
        }
	}
	public void getAttributes(HttpServletRequest request) {
		log.info("getAttributes ============= ");
		Enumeration<String> attrs = request.getAttributeNames();
		while (attrs.hasMoreElements()) {
		    String name = (String)attrs.nextElement();
		    Object value = request.getAttribute(name);
		    log.debug(name + " : " + value.toString() + "<br>");
		}
	}
	
	public void getParameters(HttpServletRequest request) {
		log.info("getParameters ============= ");
		Enumeration params = request.getParameterNames();
		while (params.hasMoreElements()) {
		    String name = (String) params.nextElement();
		    String value = request.getParameter(name);
		    log.debug(name + " : " + value+ "<br>");
		}
	}
	
	public void getCookies(HttpServletRequest request) {
		log.info("getCookies ============= ");
		Cookie cookies[] = request.getCookies();
		if(cookies != null) {
			for(int i = 0; i < cookies.length; i++) {
			    String name = cookies[i].getName();
			    String value = cookies[i].getValue();
			    log.debug(name + " : " + value+ "<br>");
			}
		}
	}
	
	public void getAll(HttpServletRequest request) {
		getHeaders(request);
		getCookies(request);
		getParameters(request);
		getAttributes(request);
	}
	
}
