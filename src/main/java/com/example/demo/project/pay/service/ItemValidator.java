package com.example.demo.project.pay.service;

import org.springframework.stereotype.Service;

import com.example.demo.project.pay.model.table.ItemEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ItemValidator {

	// 리팩토링 한 메서드
	public void validateId(final ItemEntity entity) {
	    if(entity == null) {
	      log.warn("ItemEntity cannot be null.");
	      throw new RuntimeException("ItemEntity cannot be null.");
	    }

	    if(entity.getId() == null) {
	      log.warn("Unknown Id.");
	      throw new RuntimeException("Unknown Id.");
	    }
	}
	public void validateName(final ItemEntity entity) {
	    if(entity == null) {
	      log.warn("ItemEntity cannot be null.");
	      throw new RuntimeException("ItemEntity cannot be null.");
	    }

	    if(entity.getItemName() == null) {
	      log.warn("Unknown ItemName.");
	      throw new RuntimeException("Unknown ItemName.");
	    }
	}
	
	
}
