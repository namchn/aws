package com.example.demo.project.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

	@GetMapping("/")
	public String healthCheck() {
		return "The service is up and running...";
	}


	/*
	 * @GetMapping("/robots.txt") public String robotsTxt() { return
	 * "User-agent: *\n   Disallow: /md$ \n     Allow: /md \n"; }
	 */

}
