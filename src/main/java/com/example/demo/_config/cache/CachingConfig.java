package com.example.demo._config.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;


@Configuration // @bean 으로 등록된 메서드는 재사용 한다. //component는 새로 생성함.
@EnableCaching // 캐싱 기능 사용
@Slf4j
public class CachingConfig {

	//@Value("${spring.redis.host}")
	//private String host;
	
	/*
	 * @Configuration 으로 만들어진 class는 
	 * 생성자를 새로 생성하지 않을 경우 @bean 으로 등록된 메서드의 결과를 재사용 한다.
	 */
	
	@Autowired  
	RedisConfig redisConfig;
	//RedisConfig redisConfig = new RedisConfig();
	
	@Autowired
	MemoryConfig memoryConfig;
	
	@SuppressWarnings("unused")
	@Bean
	public CacheManager cacheManager() {
		
		CacheManager cacheManager;
		//if(true) cacheManager = redisConfig.cacheManager(); 
		if(false) { cacheManager = redisConfig.cacheManager(); log.info("redisConfig used.");} //레디스 캐쉬 사용할 경우
		else { cacheManager = memoryConfig.cacheManager();  log.info("memoryConfig used.");} //메모리 캐쉬 사용할 경우
		
		return cacheManager;
		//return redisConfig.cacheManager(); 
		//return memoryConfig.cacheManager();  
	}
}