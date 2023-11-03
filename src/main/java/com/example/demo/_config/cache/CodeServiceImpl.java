package com.example.demo._config.cache;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;


@Service
@CacheConfig(cacheNames = "code")
public class CodeServiceImpl  {

	public CodeServiceImpl() {
		// TODO Auto-generated constructor stub
	}

}
