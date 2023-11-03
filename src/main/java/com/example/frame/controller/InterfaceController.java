/**
 * 2023-10-05 ncw
 */
package com.example.frame.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 
 */


@RestController
public interface InterfaceController {
	
	final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	//@GetMapping("/")
	//public ResponseEntity<?> home(); //	return ResponseEntity.ok().body("responseDTO");
	
}
