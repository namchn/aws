package com.example.frame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo._security.TokenProvider;
import com.example.demo.project.user.dto.UserDTO;
import com.example.demo.project.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/testor")
public class SampleController implements InterfaceController {

	//@Autowired
	//private UserService userService;
	
	public ResponseEntity<?> home() {
		log.info("~/testor/home");
		return ResponseEntity.ok().body("responseDTO");
	}	
	
	@PostMapping("/test1")
	public ResponseEntity<?> test1(@RequestBody UserDTO userDTO) {

		log.info("testor");

		return ResponseEntity.ok().body("responseUserDTO");
	}

		
}
