package com.example.demo.project.todo.service;

import org.springframework.stereotype.Service;

import com.example.demo.project.todo.model.TodoEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoValidator {

	// 리팩토링 한 메서드
	void validate(final TodoEntity entity) {
	    if(entity == null) {
	      log.warn("Entity cannot be null.");
	      throw new RuntimeException("Entity cannot be null.");
	    }

	    if(entity.getUserId() == null) {
	      log.warn("Unknown user.");
	      throw new RuntimeException("Unknown user.");
	    }
	}
	
}
