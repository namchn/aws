package com.example.demo.project.lotto.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.project.lotto.service.LotteriesService;
import com.example.demo.project.lotto.service.NumberGenerator;
import com.example.demo.project.lotto.service.RandomNumberGenerator;
import com.example.demo.project.pay.persistence.ItemRepository;
import com.example.frame.controller.InterfaceController;
import com.example.frame.dto.ResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("lotto")
public class LottoController implements InterfaceController{

	//@Autowired
	//private ItemRepository itemRepository;
	
	@GetMapping("/")
	public String payCheck() {
		log.info("lotto..");
		return "The lotto service  is up and running...";
	}

	

	@GetMapping("/lotto")
	public ResponseEntity<?> rotto(@RequestParam(required = false) String type) {

		// random context
		NumberGenerator randomGenerator = new RandomNumberGenerator();
		LotteriesService lotteriesService = new LotteriesService(randomGenerator);

		//System.out.println(randomGenerator);
		
		//List<Lotteries> list = lotteriesService.create(6, 45);
		List<Integer> list = lotteriesService.create(6, 45, false);
		Collections.sort(list);
		
		if (type != null && !type.equals("1234")) {
			list = null;
		}

		// list.add("Hello World! I'm ResponseEntity. And you got 400!");
		ResponseDTO<Integer> response = ResponseDTO.<Integer>builder().error("").data(list).build();
		// http status 200를 원한다면
		// ResponseEntity.ok().body(response); 사용
		// http status를 400로 설정.
		// return ResponseEntity.badRequest().body(response);
		// http status를 404로 설정.
		return ResponseEntity.status(200).body(response);
	}

}
