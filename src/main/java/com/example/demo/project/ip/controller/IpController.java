package com.example.demo.project.ip.controller;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.project.ip.block.IpAddressAccessControlInterceptor;
import com.example.demo.project.ip.block.IpAddressMatcherManager;
import com.example.demo.project.ip.block.repository.IpListEntity;
import com.example.frame.dto.ResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("ip")
public class IpController  {

    @Autowired  //알아서 빈을 찾아서 인스턴스 가져오는 것
    private IpAddressMatcherManager ipAddressMatcherManager;
    
    @Autowired  //알아서 빈을 찾아서 인스턴스 가져오는 것
    private IpAddressAccessControlInterceptor ipAddressAccessControlInterceptor;
  
    @GetMapping("/ban")  //이곳으로 접속할 경우 접근 제한 ip로 등록된다.  
    public ResponseEntity<?> ban(HttpServletRequest request
    							,HttpServletResponse response
    							) throws IOException, ParseException {
        
    	//getClientIp
        String clientIpAddress = ipAddressAccessControlInterceptor.getClientIp(request);
    	
        ipAddressMatcherManager.addNotAccessIp(clientIpAddress);
        
        log.info("add baned clientIpAddress :  {}",clientIpAddress);
    	
        IpListEntity ipEntity = IpListEntity.builder().ip(clientIpAddress).build();
		
		List<IpListEntity> dtos = new ArrayList<IpListEntity>();
		dtos.add(ipEntity);
		// list.add("Hello World! I'm ResponseEntity. And you got 400!");
		ResponseDTO<IpListEntity> res = ResponseDTO.<IpListEntity>builder()
															.error("")
															.data(dtos)
															.build();
		// ResponseDTO를 리턴한다.
		return ResponseEntity.ok().body(res);
    }
	
  
}
