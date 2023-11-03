package com.example.demo._config.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MemoryConfig {

	// 예)
	// @Value("${spring.redis.host}")
	// private String host;
	// @Value("${spring.redis.port}")
	// private int port;
	// @Value("${spring.redis.username}")
	// private String username;
	// @Value("${spring.redis.password}")
	//private String password;

	//@Bean
	//public String test() {
		//return password;
	//}

	/*
	 * @Configuration에 메소드를 @Bean 으로 등록하면 으로 호출할때 마다 결과를 재사용한다.
	 * @Bean 으로 등록하지 않은이유는 CachingConfig 에서도
	 * @Bean에서도 사용하기에 충돌하므로 사용하지 않는다.
	 */
	public CacheManager cacheManager() {
		ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
		log.info("cacheManager excuted.");
		// cacheManager.setAllowNullValues(false);
		// cacheManager.setCacheNames(List.of("members"));
		return cacheManager;
	}
  
}