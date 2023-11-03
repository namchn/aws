package com.example.demo._config.cache;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RedisConfig {

  @Value("${spring.redis.host}")
  private String host;
	  
  @Value("${spring.redis.port}")
  private int port;
  
  @Value("${spring.redis.username}")
  private String username;

  @Value("${spring.redis.password}")
  private String password;

  //private RedisConnectionFactory redisConnectionFactory;
  
  //public RedisConfig() {
	  //this.redisConnectionFactory  = redisConnectionFactory();
	  //return redisConnectionFactory();
  //}

  @Bean
  public String  testRedis() {
	  return host;
  }
  
  
  //@Bean
  public CacheManager cacheManager() {
	  log.info("RedisConfig cacheManager excuted.");  
	  log.info("RedisConfig cacheManager excuted...  {}" ,new RedisConfig().testRedis());  
	  RedisCacheConfiguration redisConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
        .entryTtl(Duration.ofSeconds(3600));  // 캐쉬에 저장되는 기간 


    //return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConfig.redisConnectionFactory()).cacheDefaults(redisConfiguration).build();
    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory()).cacheDefaults(redisConfiguration).build();

  }
  /*   */

  /* 
  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    return redisTemplate;
  }
   */

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
	    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
	    redisStandaloneConfiguration.setHostName(host);
	    redisStandaloneConfiguration.setPort(port);
	    log.info("host :"+host);
	    redisStandaloneConfiguration.setUsername(username);
	    redisStandaloneConfiguration.setPassword(password);
	    
	    LettuceConnectionFactory lettuceConnectionFactory = 
				new LettuceConnectionFactory(redisStandaloneConfiguration);
			
		return lettuceConnectionFactory;
	  }
}