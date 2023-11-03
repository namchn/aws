package com.example.demo._config.injection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;



//@Profile("prod")
//value를 통해 값이 있는 위치를 명시해준다.
@Configuration
@PropertySource(value = "/config.properties")
//@ConfigurationProperties(prefix = "my")
@Getter
//@Setter
//@Service
@RequiredArgsConstructor
public class ConfigrationInjectionService {
	
	//public ConfigrationInjectionService(@Value("${value.test}") String test) {
		//this.test= test;
		//return test;
	//}

	//final 쓰면 가져 오질 못함.
	@Value("${properties.environment.config.execution.method}")
	private   String method;
	
	@Value("${properties.environment.config.execution.use_cookie}")
	private  boolean useCookie;

	@Value("${properties.environment.config.url.frontend}")
	private   String frontendUrl;
	
	@Value("${properties.environment.config.execution.valid_csrf}")
	private boolean useValidCSRF;
	
}
