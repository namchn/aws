package com.example.demo._config.injection;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.demo._config.injection.ConfigrationInjectionService;
import com.example.demo.project.user.model.UserEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;



@Slf4j
@Configuration
//@Service
@Getter
//application-{}.yml 에서 가져온다.
public class YmlAndConfigrationInjectionService {

//	public YmlConfigrationInjectionService(@Value("${ymlconfig.jwt.token.secret.key}") String key) {
//		this.SECRET_KEY = key;
//	    //this.secretKey = Base64.getEncoder().encodeToString(key.getBytes());
//		//this.SECRET_KEY = configrationInjectionService.getMethod();
//	}

	@Autowired
	private ConfigrationInjectionService configrationInjectionService;

	//@Value("${security.jwt.token.secret.key}")
	//@Bean
	@Value("${ymlconfig.jwt.token.secret.key}")
	private String SECRET_KEY;
	
	
	
	//@Value("${environment.config.execution.mode}")
	@Value("${ymlconfig.environment.execution.mode}")
	private String MODE;
	
}

