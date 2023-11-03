package com.example.demo.project.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("page")
@Controller
//@RestController  //리소스의 템플릿을 사용하려면 이건 금지. ModelAndView 쓰면 해결됨 
public class PageController {

	@GetMapping({"/",""})
	public ModelAndView index(ModelAndView mav) {
		
		mav.addObject("home", "is");
		//mav.setViewName("index.html");
		mav.setViewName("index");
		
		//return "/index.html";
		return mav;
	}

	/*
	 * @GetMapping("/robots.txt") public String robotsTxt() { return
	 * "User-agent: *\n   Disallow: /md$ \n     Allow: /md \n"; }
	 */

}
