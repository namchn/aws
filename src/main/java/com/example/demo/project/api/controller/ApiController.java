package com.example.demo.project.api.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.project.api.service.ApiService;
import com.example.demo.project.lotto.service.LotteriesService;
import com.example.demo.project.lotto.service.NumberGenerator;
import com.example.demo.project.lotto.service.RandomNumberGenerator;
import com.example.demo.project.pay.persistence.ItemRepository;
import com.example.frame.controller.InterfaceController;
import com.example.frame.dto.ResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api")
public class ApiController implements InterfaceController{

	@Autowired
	private ApiService apiService;
	
	@GetMapping({"/",""})
	public String payCheck() {
		log.info("api..");
		return "The api service  is up and running...";
	}
	
	//업비트 코인 자동매매 RSI 값 구하기 (Java)
			//https://herojoon-dev.tistory.com/156
			
			// https://docs.upbit.com/reference/%EB%B6%84minute-%EC%BA%94%EB%93%A4-1
			//HashMap<String, Object> result = apiService.get("https://api.upbit.com/v1/candles/minutes/1?market=KRW-BTC&count=1");
			//HashMap<String, Object> result = apiService.get("https://api.upbit.com/v1/market/all?isDetails=false");
	
	@GetMapping("/upbits")
	public ResponseEntity<?> upbitValue() {

		
		HashMap<String, Object> result = apiService.get("https://api.upbit.com/v1/ticker?markets=KRW-BTC");
		
		List<Object> list = new ArrayList<Object>();
		list.add(result.get("body"));
		list.add(result.get("statusCode"));
		list.add(result.get("header"));
		
		ResponseDTO<Object> response = ResponseDTO.<Object>builder().error("").data(list).build();
		// http status 200를 원한다면 ResponseEntity.ok()
		// http status를 404로 설정  ResponseEntity.badRequest()
		return ResponseEntity.status(200).body(response);
	}
	

	@GetMapping("/upbit")
	public ResponseEntity<?> upbit() {
		HashMap<String, Object> result = apiService.get("https://api.upbit.com/v1/ticker?markets=KRW-BTC");
		
		List<Object> list = new ArrayList<Object>();
		list.add(result.get("body"));
		//list.add(result.get("statusCode"));
		//list.add(result.get("header"));
		
		List<Object> list2 = (List<Object>) result.get("body");
		log.info("list2 : "+list2.toString());
		
		ResponseDTO<Object> response = ResponseDTO.<Object>builder().error("").data(list2).build();
		// http status 200를 원한다면 ResponseEntity.ok()
		// http status를 404로 설정  ResponseEntity.badRequest()
		return ResponseEntity.status(200).body(response);
	}
	
}
