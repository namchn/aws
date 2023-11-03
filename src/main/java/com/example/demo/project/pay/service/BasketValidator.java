package com.example.demo.project.pay.service;

import org.springframework.stereotype.Service;

import com.example.demo.project.pay.model.table.UserBasketEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BasketValidator {

	// 리팩토링 한 메서드
	void validate(final UserBasketEntity entity) {
	    if(entity == null) {
	      log.warn("userBasketEntity cannot be null.");
	      throw new RuntimeException("userBasketEntity cannot be null.");
	    }

	    if(entity.getUserId() == null) {
	      log.warn("Unknown UserId.");
	      throw new RuntimeException("Unknown UserId.");
	    }
	}
	
	void validateId(final UserBasketEntity entity) {
	    if(entity == null) {
	      log.warn("userBasketEntity cannot be null.");
	      throw new RuntimeException("userBasketEntity cannot be null.");
	    }

	    if(entity.getUserId() == null) {
	      log.warn("Unknown UserId.");
	      throw new RuntimeException("Unknown UserId.");
	    }
	    if(entity.getId() == null) {
	      log.warn("Unknown Id.");
	      throw new RuntimeException("Unknown Id.");
	    }
	}
	
}
