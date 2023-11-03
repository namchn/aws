package com.example.demo.project.pay.service.cacao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.demo.project.pay.dto.OrderBasketDTO;
import com.example.demo.project.pay.model.cacao.ApproveResponse;
import com.example.demo.project.pay.model.cacao.ReadyResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KakaoPayService {
	
	//@Autowired
	//private CartMapper cartMapper;
	
	public ReadyResponse payReady(String url, OrderBasketDTO orderBasket , Map urlMap) {
		
		
		//사용자 정보 
		/*
		User user =  (User)SessionUtils.getAttribute("LOGIN_USER");
		List<CartDto> carts = cartMapper.getCartByUserNo(user.getNo());
		
		String[] cartNames = new String[carts.size()];
		for(CartDto cart: carts) {
			for(int i=0; i< carts.size(); i++) {
				cartNames[i] = cart.getClassTitle();
			}
		}	
		String itemName = cartNames[0] + " 그외" + (carts.size()-1);
		log.info("강좌이름들:"+itemName);
		*/
		
		//주문번호 
		//String order_id = user.getNo() + itemName;
		String order_id ="orderNo1234";
		//order_id = orderBasket.getId();
		
        // 카카오가 요구한 결제요청request값을 담아줍니다. 
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("cid", String.valueOf(urlMap.get("cid")));
		parameters.add("partner_order_id", orderBasket.getId());
		parameters.add("partner_user_id", String.valueOf(urlMap.get("partner_user_id")));
		//parameters.add("item_name", "itemName");
		parameters.add("item_name", orderBasket.getItemName());
		//parameters.add("item_code", "itemCode");
		parameters.add("item_code", orderBasket.getItemId());
		//parameters.add("quantity", "2" );  //String.valueOf(carts.size())
		parameters.add("quantity", String.valueOf(orderBasket.getCount()) );
		parameters.add("total_amount", String.valueOf(urlMap.get("total_amount")) ); //
		parameters.add("tax_free_amount", String.valueOf(urlMap.get("tax_free_amount")));
		parameters.add("approval_url", String.valueOf(urlMap.get("approval_url")) ); // 결제승인시 넘어갈 url
		parameters.add("cancel_url", String.valueOf(urlMap.get("cancel_url")) ); // 결제취소시 넘어갈 url
		parameters.add("fail_url", String.valueOf(urlMap.get("fail_url")) ); // 결제 실패시 넘어갈 url
		parameters.add("payment_method_type",String.valueOf(urlMap.get("payment_method_type")));
		//parameters.put("install_month", String.valueOf(urlMap.get("install_month")));
		
		
		log.info("파트너주문아이디:"+ parameters.get("partner_order_id")) ;
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
		// 외부url요청 통로 열기.
		RestTemplate template = new RestTemplate();
		//String url = "https://kapi.kakao.com/v1/payment/ready";
        // template으로 값을 보내고 받아온 ReadyResponse값 readyResponse에 저장.
		ReadyResponse readyResponse = template.postForObject(url, requestEntity, ReadyResponse.class);
		log.info("결재준비 응답객체: " + readyResponse);
        // 받아온 값 return
		return readyResponse;
	}
	
	
	/* */
    // 결제 승인요청 메서드
	public ApproveResponse payApprove(String url, String tid, String pgToken ,String totalAmount ,OrderBasketDTO orderBasket, String partner_user_id) {
		
		
		/*
		//유저정보 
		User user =  (User)SessionUtils.getAttribute("LOGIN_USER");
		List<CartDto> carts = cartMapper.getCartByUserNo(user.getNo());
		// 주문명 만들기.
		String[] cartNames = new String[carts.size()];
		for(CartDto cart: carts) {
			for(int i=0; i< carts.size(); i++) {
				cartNames[i] = cart.getClassTitle();
			}
		}	
		String itemName = cartNames[0] + " 그외" + (carts.size()-1);
		*/
		
		
		//String order_id = user.getNo() + itemName;
		String order_id ="orderNo1234";
		
		// request값 담기.
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("cid", "TC0ONETIME");
		parameters.add("tid", tid);
		parameters.add("partner_order_id", orderBasket.getId()); // 주문명
		parameters.add("partner_user_id", partner_user_id);
		parameters.add("pg_token", pgToken);
		parameters.add("payload", "결제 승인 요청에 대해 저장하고 싶은 값, 최대 200자");
		//parameters.add("total_amount", "상품 총액, 결제 준비 API 요청과 일치해야 함");
		//!!!!!!  값을 가져온다 .
		parameters.add("total_amount", totalAmount);
		
		// 이슈  json 보내는 경우 와 x-www-form-urlencoded 인 경우 
		// Map 으로 보낼 경우  - >json 으로 요청한다
		// MultiValueMap 으로 보낼경우 - >x-www-form-urlencoded 으로 요청한다 
		
		// 반대로 요청을 받을때!!!는 @requestBody 는 json을 받으므로  ,
		// MultiValueMap를 쓰거나 @ModelAtrributes 를 사용해야
		// x-www-form-urlencoded 으로 요청을 받는다  
		
        // 하나의 map안에 header와 parameter값을 담아줌.
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
		
        // 외부url 통신
		RestTemplate template = new RestTemplate();
		//String url = "https://kapi.kakao.com/v1/payment/approve";
        // 보낼 외부 url, 요청 메시지(header,parameter), 처리후 값을 받아올 클래스. 
		
		try {
			ApproveResponse approveResponse = template.postForObject(url, requestEntity, ApproveResponse.class);
			log.info("결재승인 응답객체: " + approveResponse);
			return approveResponse;
		}catch(Exception e) {
			log.warn("approveResponse 400 Bad Request.");
			//400 Bad Request: "{"msg":"partner user id is different when you ordered!","code":-707}"
			log.warn(e.getMessage());
			//throw new RuntimeException("approveResponse 400 Bad Request.");
			return null;
		}
		
	}
	
	
	
	// header() 셋팅
	private HttpHeaders getHeaders() {
		
		String SERVICE_APP_ADMIN_KEY = "b9e34eec0fea1cdfaaea223ec6bb1f64"; //카카오에서 발급받은.
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK "+SERVICE_APP_ADMIN_KEY);
		headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		return headers;
	}
}