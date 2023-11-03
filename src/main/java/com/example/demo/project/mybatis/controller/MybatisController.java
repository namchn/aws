package com.example.demo.project.mybatis.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.project.lotto.service.LotteriesService;
import com.example.demo.project.lotto.service.NumberGenerator;
import com.example.demo.project.lotto.service.RandomNumberGenerator;
import com.example.demo.project.mybatis.repository.UserMEntity;
import com.example.demo.project.mybatis.repository.UserMapperRepository;
import com.example.demo.project.pay.persistence.ItemRepository;
import com.example.frame.controller.InterfaceController;
import com.example.frame.dto.ResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("mybatis")
public class MybatisController implements InterfaceController{

	@Autowired
	private UserMapperRepository userMapperRepository;
	
	@GetMapping({"/",""})
	public String payCheck() {
		log.info("mybatis..");
		return "The mybatis service  is up and running...";
	}

	
	@GetMapping("/mybatis")
	public ResponseEntity<?> mybatis( HttpServletRequest req , HttpServletResponse res  ) {
		//@ModelAttribute Model model ,
		//model.addAttribute("loginType", "session-login");
        //model.addAttribute("pageName", "세션 로그인");
		
		//(@SessionAttribute(name = "userId", required = false) Long userId)
		//HttpSession session = req.getSession(true);
		//System.out.println(session);
		//session.setAttribute("userId", user.getId());
		//session.setMaxInactiveInterval(1800);
		//session.invalidate();
		
		List<UserMEntity> list = userMapperRepository.sel();
		//List<Object> list =
		//List<Lotteries> list = lotteriesService.create(6, 45);
		//List<UserMEntity> list = new ArrayList<Object>();
		//list.add(u);
		// list.add("Hello World! I'm ResponseEntity. And you got 400!");
		ResponseDTO<UserMEntity> response = ResponseDTO.<UserMEntity>builder().error("").data(list).build();
		// http status 200를 원한다면
		// ResponseEntity.ok().body(response); 사용
		// http status를 400로 설정.
		// return ResponseEntity.badRequest().body(response);
		// http status를 404로 설정.
		return ResponseEntity.status(200).body(response);
	}

}
