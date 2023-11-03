package com.example.demo.project.pay.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo._config.injection.YmlAndConfigrationInjectionService;
import com.example.demo._security.TokenProvider;
import com.example.demo._utility.RegexValidator;
import com.example.demo._utility.ParameterGetter;
import com.example.demo.project.pay.dto.BasketDTO;
import com.example.demo.project.pay.dto.OrderBasketDTO;
import com.example.demo.project.pay.dto.resultDTO;
import com.example.demo.project.pay.model.cacao.ApproveResponse;
import com.example.demo.project.pay.model.cacao.ReadyResponse;
import com.example.demo.project.pay.model.table.ItemEntity;
import com.example.demo.project.pay.model.table.PaymentEntity;
import com.example.demo.project.pay.model.table.UserBasketEntity;
import com.example.demo.project.pay.service.BasketService;
import com.example.demo.project.pay.service.PaymentService;
import com.example.demo.project.pay.service.cacao.KakaoPayService;
import com.example.demo.project.pay.service.impl.ItemServiceImpl;
import com.example.frame.dto.ResponseDTO;
import com.example.frame.dto.ResponseStringDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
// 세션에 저장된 겂을 사용할때 쓰는 어노테이션, session에서 없으면 model까지 훑어서 찾아냄.
@SessionAttributes({"tid","orderBasket","totalAmount"}) 
//@RequestMapping("pay")
public class OrderController {
	
	@Autowired
	RegexValidator regexValidator;
	
	@Autowired
	ParameterGetter parameterGetter;
	
	@Autowired
	private  KakaoPayService kakaoPayService;
	@Autowired
	private ItemServiceImpl itemService;
	@Autowired
	private BasketService basketService;
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	

	@Autowired
	private YmlAndConfigrationInjectionService ymlAndConfigrationInjectionService;

	//final String ncwmode = ymlAndConfigrationInjectionService.getMODE();
	//값이 불러와지지 않음
	
	
	//초기값 설정 
	private	Map<String,String>  payMap = new HashMap<String,String>();
	
	//payMap.put("cid","TC0ONETIME"); //가맹 토큰
	
	
	public OrderController(@Value("${ymlconfig.environment.execution.mode}") String key 
						   ,@Value("${ymlconfig.environment.pay.order.cid}") String cid 
						   ,@Value("${ymlconfig.environment.pay.order.partner_user_id}") String partner_user_id 
						   ,@Value("${ymlconfig.environment.pay.order.approval_url}") String approval_url 
						   ,@Value("${ymlconfig.environment.pay.order.cancel_url}") String cancel_url 
						   ,@Value("${ymlconfig.environment.pay.order.fail_url}") String fail_url 
						   ,@Value("${ymlconfig.environment.pay.order.login_uri}") String login_uri 
						   ,@Value("${ymlconfig.environment.pay.order.basket_uri}") String basket_uri 
							) {
      
		
		
		//boolean dev = true;
		//dev = false;
		if(key.equals("local")) { //개발 
			//this.payMap.put("ncwmode",ncwmode); //ncwmode
			
			this.payMap.put("cid",cid); //가맹 토큰
			this.payMap.put("partner_user_id",partner_user_id); // 회사 이름 
			
			//this.payMap.put("approval_url", "http://localhost:3000/pay/completed");// 결제승인시 넘어갈 url
			this.payMap.put("approval_url", approval_url);// 결제승인시 넘어갈 url
			
			this.payMap.put("cancel_url",cancel_url); // 결제취소시 넘어갈 url
			this.payMap.put("fail_url", fail_url); // 결제 실패시 넘어갈 url
			//urlMap.put("cancel_url", "http://localhost:8080/pay/cancel"); // 결제취소시 넘어갈 url
			//urlMap.put("fail_url", "http://localhost:8080/pay/fail"); // 결제 실패시 넘어갈 url
			//urlMap.put("approval_url", "http://localhost:3000/pay/completed");// 결제승인시 넘어갈 url
			//urlMap.put("cancel_url", "http://localhost:3000/pay/cancel"); // 결제취소시 넘어갈 url
			//urlMap.put("fail_url", "http://localhost:3000/pay/fail"); // 결제 실패시 넘어갈 url
			this.payMap.put("tax_free_amount", "0"); // 세금 없는 갯수
			this.payMap.put("payment_method_type", "CARD"); // 결제 방법
			this.payMap.put("install_month", "0"); // 할부월 
			this.payMap.put("pay_ready_uri", "https://kapi.kakao.com/v1/payment/ready"); // 요청주소 
			
			this.payMap.put("login_uri", login_uri); // 요청주소 
			this.payMap.put("basket_uri", basket_uri); // 요청주소 
			
			
		}else { //운영
			this.payMap.put("cid",cid); //가맹 토큰
			this.payMap.put("partner_user_id",partner_user_id); // 회사 이름 
			
			//this.payMap.put("approval_url", "https://nc2030.com/pay/completed");// 결제승인시 넘어갈 url
			this.payMap.put("approval_url", approval_url);// 결제승인시 넘어갈 url
			this.payMap.put("cancel_url", cancel_url); // 결제취소시 넘어갈 url
			this.payMap.put("fail_url", fail_url); // 결제 실패시 넘어갈 url
			//urlMap.put("cancel_url", "http://localhost:8080/pay/cancel"); // 결제취소시 넘어갈 url
			//urlMap.put("fail_url", "http://localhost:8080/pay/fail"); // 결제 실패시 넘어갈 url
			//urlMap.put("approval_url", "http://localhost:3000/pay/completed");// 결제승인시 넘어갈 url
			//urlMap.put("cancel_url", "http://localhost:3000/pay/cancel"); // 결제취소시 넘어갈 url
			//urlMap.put("fail_url", "http://localhost:3000/pay/fail"); // 결제 실패시 넘어갈 url
			this.payMap.put("tax_free_amount", "0"); // 세금 없는 갯수
			this.payMap.put("payment_method_type", "CARD"); // 결제 방법
			this.payMap.put("install_month", "0"); // 할부월 
			this.payMap.put("pay_ready_uri", "https://kapi.kakao.com/v1/payment/ready"); // 요청주소 
			
			this.payMap.put("login_uri", login_uri); // 요청주소 
			this.payMap.put("basket_uri", basket_uri); // 요청주소 
			
		}
		
		
		//this.payMap = key;
      //this.SECRET_KEY = configrationInjectionService.getJwtTokenSecretKey();
  }
	

	// 카카오페이결제 요청   이거 요청은  로그인 인증이 필요할거 같은데...??
	
	/*
	@GetMapping("/pay1")
	//@GetMapping("/pays")
	// @ResponseBody String
	public ResponseEntity<?> payReady(  @AuthenticationPrincipal String userId,
										HttpServletRequest req,
										//,@RequestParam(name = "total_amount") int totalAmount, 
										@RequestParam(name = "id") String basketId
										//,@RequestParam(name = "token") String token
										,Model model
										) 
										{
										// Order orderBasket
	*/
	
	@GetMapping("/pay")
	//@GetMapping("/pays")
	// @ResponseBody String
	public ResponseEntity<?> payReady(  //@AuthenticationPrincipal String userId,
										HttpServletRequest req,
										//,@RequestParam(name = "total_amount") int totalAmount, 
										
										@RequestParam(name = "id") String basketId
										,@RequestParam(name = "token") String token
										
										//@RequestBody Map<String,String> map
										,Model model
										) 
										{
										// Order orderBasket
		parameterGetter.getAll(req);
		
		
		
		//String token = map.get("token");
		//String basketId = map.get("id");
		
		
		//초기값 설정 
		Map<String,String>  urlMap = new HashMap<String,String>();
		urlMap.put("cid",payMap.get("cid")); //가맹 토큰
		urlMap.put("partner_user_id",payMap.get("partner_user_id")); // 회사 이름 
		urlMap.put("approval_url", payMap.get("approval_url"));// 결제승인시 넘어갈 url
		urlMap.put("cancel_url", payMap.get("cancel_url")); // 결제취소시 넘어갈 url
		urlMap.put("fail_url", payMap.get("fail_url")); // 결제 실패시 넘어갈 url
		//urlMap.put("cancel_url", "http://localhost:8080/pay/cancel"); // 결제취소시 넘어갈 url
		//urlMap.put("fail_url", "http://localhost:8080/pay/fail"); // 결제 실패시 넘어갈 url
		
		urlMap.put("tax_free_amount", "0"); // 세금 없는 갯수
		urlMap.put("payment_method_type", "CARD"); // 결제 방법
		urlMap.put("install_month", "0"); // 할부월 
		
		urlMap.put("pay_ready_uri",payMap.get("pay_ready_uri")); // 요청주소 
		
		
		try {
		
		//토큰 검증 
		String userId = tokenProvider.validateAndGetUserId(token);
			
		// 이걸로 본인인지 확인
		log.info("userId : " + userId );	
		if(userId == null) {
			userId = "ncw11111";
		}
		//String userId = "ncw11111";
		
		
		// 기본적으로 결제는 로그인이 되어 있는 상테에서 하므로 로그인 @AuthenticationPrincipal 정보를 가져온다. 
		OrderBasketDTO orderBasket = basketService.findOrderBasketDTO(userId, basketId);
		if(orderBasket == null) {
			URI redirectUri = new URI( urlMap.get("cancel_url")  ); //"/pay/pay/cancel"
		    HttpHeaders httpHeaders = new HttpHeaders();
		    httpHeaders.setLocation(redirectUri);
		    return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
			//return "redirect:/pay/pay/cancel";
		}
		// 세션 정보를 redis 를 이용해서 가져 오는 것도 고려.
		//String orderBasket =  "세션에서 주문정보 가저오기.";
		
		
		
		// id   =  402880828b0fce5b018b0fcebef80000
		
		Long totalCount  = (long) orderBasket.getCount();
		Long totalPrice  = (long) orderBasket.getItemPrice();
		Long totalAmount =  totalCount * totalPrice ;
		log.info("주문정보:"+orderBasket);
		log.info("주문가격:"+totalAmount);
		urlMap.put("total_amount", String.valueOf(totalAmount)); // 회사 이름 
		
		// 카카오 결제 준비하기	- 결제요청 service 실행.
		ReadyResponse readyResponse = kakaoPayService.payReady( urlMap.get("pay_ready_uri") ,orderBasket , urlMap );
		// 요청처리후 받아온 결재고유 번호(tid)를 모델에 저장
		model.addAttribute("tid", readyResponse.getTid());
		log.info("결재고유 번호(tid): " + readyResponse.getTid());		
		log.info("readyResponse: {} " , readyResponse);		
		// Order정보를 모델에 저장
		model.addAttribute("orderBasket",orderBasket);
		

		
		
		HttpSession oldSession =  req.getSession();
		oldSession.invalidate();
		HttpSession session =  req.getSession(true);
		log.info("session1  :"+session.getId());	
		session.setMaxInactiveInterval(1800); //30분
		session.setAttribute("tid", readyResponse.getTid());
		session.setAttribute("orderBasket", orderBasket);
		session.setAttribute("totalAmount", String.valueOf(totalAmount));
		log.info("session1  :"+session.getId());	
		
		//직접 리다이렉트 하는 방법 
		//RedirectView redirectView = new RedirectView();
		//redirectView.setUrl(readyResponse.getNext_redirect_pc_url());
		//return redirectView;
		
		
		//if(true) {
			URI redirectUri = new URI( readyResponse.getNext_redirect_pc_url() ); //"/pay/pay/cancel"
		    HttpHeaders httpHeaders = new HttpHeaders();
		    httpHeaders.setLocation(redirectUri);
		    return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
		  //return readyResponse.getNext_redirect_pc_url();  //반환값이 String 일때
		//}
		
		
		//readyResponse.next_redirect_app_url //앱
		//readyResponse.next_redirect_mobile_url /모바일일
		//android_app_scheme	String	카카오페이 결제 화면으로 이동하는 Android 앱 스킴(Scheme)
		//ios_app_scheme	String	카카오페이 결제 화면으로 이동하는 iOS 앱 스킴
		//created_at	Datetime	결제 준비 요청 시간
		
		
		//return readyResponse; // 클라이언트에 보냄.(tid,next_redirect_pc_url이 담겨있음.)
		
		
		}catch( Exception e) {
			log.error(e.getMessage());
			URI redirectUri;
			try {
				// JWT expired at 2023-10-13T20:32:21Z. Current time: 2023-10-13T21:23:30Z, a difference of 3069047 milliseconds.  Allowed clock skew: 0 milliseconds.
				redirectUri = new URI( String.valueOf( payMap.get("login_uri"))  +"?messege="+e.getMessage().replace(" ", "")  );  //pay/cancel
			    HttpHeaders httpHeaders = new HttpHeaders();
			    httpHeaders.setLocation(redirectUri);
			    return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
			    
			} catch (URISyntaxException e1) {
				log.error(e1.getMessage());
				//e1.printStackTrace();
				/* */
				ResponseDTO responseDTO = ResponseDTO.builder()
											.error(e.getMessage())
											.build();
			    return ResponseEntity
			    		  //.status(403)
			    		  .badRequest()
			    		  .body(responseDTO);
			    
			} //"/pay/pay/cancel"
			
		  //return readyResponse.getNext_redirect_pc_url();  //반환값이 String 일때
		}
		
	}
	
		// 카카오페이결제 post 요청
		@PostMapping("/payready")
		public @ResponseBody ResponseEntity<?> payReady(@AuthenticationPrincipal String userId,
											@RequestBody BasketDTO basketDTO,
											HttpServletRequest req, 
											Model model) {
											// Order orderBasket
			//장바구니 
			// id   =  402880828b0fce5b018b0fcebef80000
			
			log.info("ncwmode:"+payMap.get("ncwmode")  );
			
			parameterGetter.getAll(req);
			
			// 기본값 설정 
			Map<String,String>  urlMap = new HashMap<String,String> ();
			urlMap.put("cid", payMap.get("cid")); //가맹 토큰
			urlMap.put("partner_user_id",payMap.get("partner_user_id")); // 회사 이름 
			urlMap.put("approval_url", payMap.get("approval_url"));// 결제승인시 넘어갈 url
			urlMap.put("cancel_url", payMap.get("cancel_url")); // 결제취소시 넘어갈 url
			urlMap.put("fail_url", payMap.get("fail_url")); // 결제 실패시 넘어갈 url
			//urlMap.put("cancel_url", "http://localhost:8080/pay/cancel"); // 결제취소시 넘어갈 url
			//urlMap.put("fail_url", "http://localhost:8080/pay/fail"); // 결제 실패시 넘어갈 url
			//urlMap.put("approval_url", "http://localhost:3000/pay/completed");// 결제승인시 넘어갈 url
			//urlMap.put("cancel_url", "http://localhost:3000/pay/cancel"); // 결제취소시 넘어갈 url
			//urlMap.put("fail_url", "http://localhost:3000/pay/fail"); // 결제 실패시 넘어갈 url
			urlMap.put("tax_free_amount", "0"); // 세금 없는 갯수
			urlMap.put("payment_method_type", "CARD"); // 결제 방법
			urlMap.put("install_month", "0"); // 할부월 
			
			urlMap.put("pay_ready_uri",payMap.get("pay_ready_uri")); // 요청주소 
			
			
			try {
			
			//@ResponseBody ReadyResponse
			// 이걸로 본인인지 확인
			log.info("userId : " + userId );	
			if(userId == null) {
				userId = "ncw11111";
			}
			//String userId = "ncw11111";
			
			
			// 기본적으로 결제는 로그인이 되어 있는 상테에서 하므로 로그인 @AuthenticationPrincipal 정보를 가져온다. 
			OrderBasketDTO orderBasket = basketService.findOrderBasketDTO(userId, basketDTO.getId());
			
			log.info("testing orderBasket :" + orderBasket );
			
			if(orderBasket == null ) {  
				//return "redirect:/pay/pay/cancel";
				//URI redirectUri = new URI( urlMap.get("cancel_url")  ); //"/pay/pay/cancel"
			    //HttpHeaders httpHeaders = new HttpHeaders();
			    //httpHeaders.setLocation(redirectUri);
			    //return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
			
			    log.error("orderBasket is null {}", "" );
				//e1.printStackTrace();
				/* */
				ResponseDTO responseDTO = ResponseDTO.builder()
											.message("너무많이 주문하지 마세요.")
											.error("orderBasket is null.")
											.build();
			    return ResponseEntity
			    		  //.status(403)
			    		  .badRequest() //400
			    		  .body(responseDTO);
			
			}
			
			if( orderBasket.getCount() >= 3 ) {  //3개이상은 자동 취소.
				log.error("장바구니 제한 갯수 초과 {}", orderBasket.getCount() );
				//e1.printStackTrace();
				/* */
				ResponseDTO responseDTO = ResponseDTO.builder()
											.message("너무많이 주문하지 마세요.")
											.error("exceeded order.")
											.build();
			    return ResponseEntity
			    		  //.status(403)
			    		  .badRequest() //400
			    		  .body(responseDTO);
			}
			
			// 세션 정보를 redis 를 이용해서 가져 오는 것도 고려.
			//String orderBasket =  "세션에서 주문정보 가저오기.";
			
			Long totalAmount =  (long) orderBasket.getCount() * (long) orderBasket.getItemPrice() ;
			urlMap.put("total_amount", String.valueOf(totalAmount)); // 회사 이름 
			log.info("주문정보:"+orderBasket);
			log.info("주문가격:"+totalAmount);
			
			// 카카오 결제 준비하기	- 결제요청 service 실행.
			
			ReadyResponse readyResponse = kakaoPayService.payReady(urlMap.get("pay_ready_uri") ,orderBasket ,urlMap);
			// 요청처리후 받아온 결재고유 번호(tid)를 모델에 저장
			model.addAttribute("tid", readyResponse.getTid());
			log.info("결재고유 번호(tid): " + readyResponse.getTid());		
			log.info("readyResponse: {} " , readyResponse);		
			// Order정보를 모델에 저장
			model.addAttribute("orderBasket",orderBasket);
			
			HttpSession oldSession =  req.getSession();
			oldSession.invalidate();
			HttpSession session =  req.getSession(true);
			log.info("session1  :"+session.getId());	
			session.setMaxInactiveInterval(1800); //30분
			session.setAttribute("tid", readyResponse.getTid());
			session.setAttribute("orderBasket", orderBasket);
			session.setAttribute("totalAmount", String.valueOf(totalAmount));
			
			
			
			//직접 리다이렉트 하는 방법 
			//RedirectView redirectView = new RedirectView();
			//redirectView.setUrl(readyResponse.getNext_redirect_pc_url());
			//return redirectView;
			
			//return readyResponse.getNext_redirect_pc_url();
			//readyResponse.next_redirect_app_url //앱
			//readyResponse.next_redirect_mobile_url /모바일일
			//android_app_scheme	String	카카오페이 결제 화면으로 이동하는 Android 앱 스킴(Scheme)
			//ios_app_scheme	String	카카오페이 결제 화면으로 이동하는 iOS 앱 스킴
			//created_at	Datetime	결제 준비 요청 시간
			
			
			//return readyResponse; // 클라이언트에 보냄.(tid,next_redirect_pc_url이 담겨있음.)
			
			String response = readyResponse.getNext_redirect_pc_url();
			List<String> dtos= new ArrayList();
			dtos.add(response);		
			
			ResponseDTO<String> responseDTO = ResponseDTO.<String>builder()
					.data(dtos)
					.message(String.valueOf(session.getId()))
					.build();
			return ResponseEntity.status(200).body(responseDTO);
			
			}catch( Exception e) {
				log.error(e.getMessage());
				URI redirectUri;
				try {
					// jwt 만료될 경우 어떻게 할 것인가?
					// JWT expired at 2023-10-13T20:32:21Z. Current time: 2023-10-13T21:23:30Z, a difference of 3069047 milliseconds.  Allowed clock skew: 0 milliseconds.
					redirectUri = new URI( String.valueOf( payMap.get("login_uri"))  +"?messege="+e.getMessage().replace(" ", "")  );  //pay/cancel
				    HttpHeaders httpHeaders = new HttpHeaders();
				    httpHeaders.setLocation(redirectUri);
				    return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
				    
				} catch (URISyntaxException e1) {
					log.error(e1.getMessage());
					//e1.printStackTrace();
					/* */
					ResponseDTO responseDTO = ResponseDTO.builder()
												.message("server error")
												.error(e.getMessage())
												.build();
				    return ResponseEntity
				    		  //.status(403)
				    		  .badRequest() //400 
				    		  .body(responseDTO);
				    
				} //"/pay/pay/cancel"
				
			  //return readyResponse.getNext_redirect_pc_url();  //반환값이 String 일때
			}
		}
		
	
	/* */
    // 결제승인요청
	@GetMapping("/pay/completed")
	public @ResponseBody ResponseEntity<?>  payCompleted(@RequestParam("pg_token") String pgToken
								//, @ModelAttribute("tid") String tid
								//, @ModelAttribute("totalAmount") String totalAmount
								//, @ModelAttribute("orderBasket") OrderBasketDTO orderBasket
								//, @ModelAttribute("order") String order
								,  Model model
								 ,HttpServletRequest req) {
									//Order order																							
		parameterGetter.getAll(req);
		
		
		HttpSession session =  req.getSession(false);
		log.info("session2  :"+session.getId());	
		if(session == null) log.info("session null");
		String tid = (String) session.getAttribute("tid");
		String totalAmount = (String) session.getAttribute("totalAmount");
		//orderBasket = (String) session.getAttribute("orderBasket");
		OrderBasketDTO orderBasket = (OrderBasketDTO) session.getAttribute("orderBasket");
		
		log.info("결제승인 요청을 인증하는 토큰: " + pgToken);
		log.info("주문정보: " + orderBasket);		
		log.info("결재고유 번호(tid): " + tid);
		
		
		//주문 완료후 세션 삭제 
		session.invalidate();
		
		
		//{1}db에서  처리
		UserBasketEntity updatingBasket = UserBasketEntity.builder()
				.id(orderBasket.getId())
				.itemId(orderBasket.getItemId())
				.userId(orderBasket.getUserId())
				//.count(5000)
				.count(orderBasket.getCount())
				.useYn("N")   // 이거 활성화 필요!!!!!!!!!
				.build();
		//db에서  상품갯수를 차감한다.
		resultDTO rDTO= itemService.itemAmountUpdate(updatingBasket);
		if(!rDTO.isSuccess()) {
			//return "redirect:/pay/pay?id="+orderBasket.getId()+"&error="+rDTO.getMessage();
			URI redirectUri;
			try {
				redirectUri = new URI( String.valueOf( payMap.get("basket_uri"))  + "?message="+"failed_itemAmount" );
				HttpHeaders httpHeaders = new HttpHeaders();
			    httpHeaders.setLocation(redirectUri);
			    return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
			} catch (URISyntaxException e) {
				log.error(e.getMessage());
				return null;
				//e.printStackTrace();
			} //"/pay/pay/cancel"
		    
		}
		//db에서  장바구니 미사용처리한다.
		basketService.update(updatingBasket);
		
		
		// 카카오 결재 요청하기
		ApproveResponse approveResponse = kakaoPayService.payApprove("https://kapi.kakao.com/v1/payment/approve",tid, pgToken,totalAmount,orderBasket ,  String.valueOf(payMap.get("partner_user_id")) ); // 회사 이름 
		//{2} 여기서 예외 처리 후 클라이언트에 처리가 되었음을 확인하여 준다.
		if(approveResponse == null) {
			resultDTO rDTOback= itemService.itemAmountRollback(updatingBasket,true);
			updatingBasket.setUseYn("Y");
			basketService.update(updatingBasket);
			
			//return "redirect:/pay/pay?id="+orderBasket.getId()+"&rollback="+rDTOback.isSuccess();
			URI redirectUri;
			try {
				redirectUri = new URI(  String.valueOf( payMap.get("basket_uri"))  +  "?message="+"failed_payment" );
				HttpHeaders httpHeaders = new HttpHeaders();
			    httpHeaders.setLocation(redirectUri);
			    return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
			} catch (URISyntaxException e) {
				log.error(e.getMessage());
				return null;
				//e.printStackTrace();
			} //"/pay/pay/cancel"
		    
		}
		
		
		log.info("approveResponse: " + approveResponse);
		// approveResponse: ApproveResponse(aid=A526d2ab359871eb5938, tid=T526d29039a0345de174, cid=TC0ONETIME, sid=null, partner_order_id=402880828b0fce5b018b0fcebef80000, partner_user_id=nc2030, payment_method_type=CARD, item_name=k2 소총, item_code=402880828b0f5eff018b0f5f3a8c0000, quantity=5, created_at=2023-10-12T01:51:29, approved_at=2023-10-12T01:51:58, payload=결제 승인 요청에 대해 저장하고 싶은 값, 최대 200자, amount=Amount(total=2305, tax_free=0, vat=210, point=0, discount=0))
		 
		//{3} 완료된 처리건에 대한 Payment 디비 처리  - 카카오 페이로 넘겨받은 결재정보값을 저장.
		PaymentEntity payed = PaymentEntity.builder()
										 .aid(approveResponse.getAid())
										 .tid(approveResponse.getTid())
										 .cid(approveResponse.getCid())
										 .sid(approveResponse.getSid())
										 .partner_order_id(approveResponse.getPartner_order_id())
										 .partner_user_id(approveResponse.getPartner_user_id())
										 .payment_method_type(approveResponse.getPayment_method_type())
										 .item_name(approveResponse.getItem_name())
										 .item_code(approveResponse.getItem_code())
										 .quantity(String.valueOf(approveResponse.getQuantity()))
										 .order_created_at(approveResponse.getCreated_at())
										 .order_approved_at(approveResponse.getApproved_at())
										 .payload(approveResponse.getPayload())
										 .amount_total(String.valueOf(approveResponse.getAmount().getTotal()))
										 .amount_tax_free(String.valueOf(approveResponse.getAmount().getTax_free()))
										 .amount_vat(String.valueOf(approveResponse.getAmount().getVat()))
										 .amount_point(String.valueOf(approveResponse.getAmount().getPoint()))
										 .amount_discount(String.valueOf(approveResponse.getAmount().getDiscount()))
										 .build();
		
		PaymentEntity savePayed = paymentService.update(payed);
		log.info("savePayed : {}",savePayed);
		
		
		// 5. payment 저장
		//	orderNo, payMathod, 주문명.
		// - 카카오 페이로 넘겨받은 결재정보값을 저장.
		
		/*
		Payment payment = Payment.builder() 
				.paymentClassName(approveResponse.getItem_name())
				.payMathod(approveResponse.getPayment_method_type())
				.payCode(tid)
				.build();
		
		orderService.saveOrder(order,payment);
		*/
		
		//return "redirect:/pays";
		//return "redirect:/pay/item/all";
		
		//return "redirect:/pay/pay/cancel";
		URI redirectUri;
		try {
			redirectUri = new URI(  String.valueOf( payMap.get("basket_uri"))  + "?message="+"success_payed" );
			HttpHeaders httpHeaders = new HttpHeaders();
		    httpHeaders.setLocation(redirectUri);
		    ResponseEntity responseEntity = new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
		    return responseEntity;
			
		} catch (URISyntaxException e) {
			log.error(e.getMessage());
			return null;
		} //"/pay/pay/cancel"
	    
	}
	
	
	
	
	/* */
    // 결제승인요청
	@PostMapping("/pay/completed")
	public  @ResponseBody ResponseEntity<?> payCompleted(@AuthenticationPrincipal String userId,
								//@RequestBody BasketDTO basketDTO,
								@RequestBody Map<String,String> map,
								//@RequestParam("pg_token") String pgToken,
								//@ModelAttribute("tid") String tid,
								//@ModelAttribute("totalAmount") String totalAmount,
								//@ModelAttribute("orderBasket") OrderBasketDTO orderBasket,
								
								HttpServletRequest req, 
								Model model) {
								
		//String pgToken = basketDTO.getId(); 	
		String pgToken = map.get("id"); 
		if(pgToken==null) {
		//if(1==1) {
			ResponseStringDTO response = ResponseStringDTO.builder()
					//.data(redirectUri.toString())
					.error("카카오에서 토근을 안보내줌.")
					.build();
			return  ResponseEntity.ok().body(response);
		}
		
		
		
								//@RequestParam("pg_token") String pgToken
								//, @ModelAttribute("order") String order
								//,  Model model
								//,HttpServletRequest req) {
									//Order order																							
		
		parameterGetter.getAll(req);
		
		
		HttpSession session =  req.getSession(false);
		log.info("session2  :"+session.getId());	
		if(session == null) log.info("session null");
		String tid = (String) session.getAttribute("tid");
		String totalAmount = (String) session.getAttribute("totalAmount");
		//orderBasket = (String) session.getAttribute("orderBasket");
		OrderBasketDTO orderBasket = (OrderBasketDTO) session.getAttribute("orderBasket");
		
		log.info("결제승인 요청을 인증하는 토큰: " + pgToken);
		log.info("주문정보: " + orderBasket);		
		log.info("결재고유 번호(tid): " + tid);
		
		
		//주문 완료후 세션 삭제 : Cannot create a session after the response has been committed ERROR
		// 때문에 세션 삭제를 해주지 못함.
		//session.invalidate();
		
		
		//{1}db에서  처리
		UserBasketEntity updatingBasket = UserBasketEntity.builder()
				.id(orderBasket.getId())
				.itemId(orderBasket.getItemId())
				.userId(orderBasket.getUserId())
				//.count(5000)
				.count(orderBasket.getCount())
				.useYn("N")   // 이거 활성화 필요!!!!!!!!!
				.build();
		//db에서  상품갯수를 차감한다.
		resultDTO rDTO= itemService.itemAmountUpdate(updatingBasket);
		if(!rDTO.isSuccess()) {
			//return "redirect:/pay/pay?id="+orderBasket.getId()+"&error="+rDTO.getMessage();
			URI redirectUri;
			try {
				redirectUri = new URI( String.valueOf( payMap.get("basket_uri"))  + "?message="+"failed_itemAmount" );
				HttpHeaders httpHeaders = new HttpHeaders();
			    httpHeaders.setLocation(redirectUri);
			    //return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
			    ResponseStringDTO response = ResponseStringDTO.builder().data(redirectUri.toString())
						.build();
				return  ResponseEntity.ok().body(response);
			} catch (URISyntaxException e) {
				log.error(e.getMessage());
				return null;
				//e.printStackTrace();
			} //"/pay/pay/cancel"
		    
		}
		//db에서  장바구니 미사용처리한다.
		basketService.update(updatingBasket);
		
		
		// 카카오 결재 요청하기
		ApproveResponse approveResponse = kakaoPayService.payApprove("https://kapi.kakao.com/v1/payment/approve",tid, pgToken,totalAmount,orderBasket ,  String.valueOf(payMap.get("partner_user_id")) ); // 회사 이름 
		//{2} 여기서 예외 처리 후 클라이언트에 처리가 되었음을 확인하여 준다.
		if(approveResponse == null) {
			resultDTO rDTOback= itemService.itemAmountRollback(updatingBasket,true);
			updatingBasket.setUseYn("Y");
			basketService.update(updatingBasket);
			
			//return "redirect:/pay/pay?id="+orderBasket.getId()+"&rollback="+rDTOback.isSuccess();
			URI redirectUri;
			try {
				redirectUri = new URI(  String.valueOf( payMap.get("basket_uri"))  +  "?message="+"failed_payment" );
				HttpHeaders httpHeaders = new HttpHeaders();
			    httpHeaders.setLocation(redirectUri);
			    //return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
			    ResponseStringDTO response = ResponseStringDTO.builder().data(redirectUri.toString())
						.build();
				return  ResponseEntity.ok().body(response);
			} catch (URISyntaxException e) {
				log.error(e.getMessage());
				return null;
				//e.printStackTrace();
			} //"/pay/pay/cancel"
		    
		}
		
		
		log.info("approveResponse: " + approveResponse);
		// approveResponse: ApproveResponse(aid=A526d2ab359871eb5938, tid=T526d29039a0345de174, cid=TC0ONETIME, sid=null, partner_order_id=402880828b0fce5b018b0fcebef80000, partner_user_id=nc2030, payment_method_type=CARD, item_name=k2 소총, item_code=402880828b0f5eff018b0f5f3a8c0000, quantity=5, created_at=2023-10-12T01:51:29, approved_at=2023-10-12T01:51:58, payload=결제 승인 요청에 대해 저장하고 싶은 값, 최대 200자, amount=Amount(total=2305, tax_free=0, vat=210, point=0, discount=0))
		 
		//{3} 완료된 처리건에 대한 Payment 디비 처리  - 카카오 페이로 넘겨받은 결재정보값을 저장.
		PaymentEntity payed = PaymentEntity.builder()
										 .aid(approveResponse.getAid())
										 .tid(approveResponse.getTid())
										 .cid(approveResponse.getCid())
										 .sid(approveResponse.getSid())
										 .partner_order_id(approveResponse.getPartner_order_id())
										 .partner_user_id(approveResponse.getPartner_user_id())
										 .payment_method_type(approveResponse.getPayment_method_type())
										 .item_name(approveResponse.getItem_name())
										 .item_code(approveResponse.getItem_code())
										 .quantity(String.valueOf(approveResponse.getQuantity()))
										 .order_created_at(approveResponse.getCreated_at())
										 .order_approved_at(approveResponse.getApproved_at())
										 .payload(approveResponse.getPayload())
										 .amount_total(String.valueOf(approveResponse.getAmount().getTotal()))
										 .amount_tax_free(String.valueOf(approveResponse.getAmount().getTax_free()))
										 .amount_vat(String.valueOf(approveResponse.getAmount().getVat()))
										 .amount_point(String.valueOf(approveResponse.getAmount().getPoint()))
										 .amount_discount(String.valueOf(approveResponse.getAmount().getDiscount()))
										 .build();
		
		PaymentEntity savePayed = paymentService.update(payed);
		log.info("savePayed : {}",savePayed);
		
		
		// 5. payment 저장
		//	orderNo, payMathod, 주문명.
		// - 카카오 페이로 넘겨받은 결재정보값을 저장.
		
		/*
		Payment payment = Payment.builder() 
				.paymentClassName(approveResponse.getItem_name())
				.payMathod(approveResponse.getPayment_method_type())
				.payCode(tid)
				.build();
		
		orderService.saveOrder(order,payment);
		*/
		
		//return "redirect:/pays";
		//return "redirect:/pay/item/all";
		
		//return "redirect:/pay/pay/cancel";
		URI redirectUri;
		try {
			redirectUri = new URI(  String.valueOf( payMap.get("basket_uri"))  + "?message="+"success_payed" );
			HttpHeaders httpHeaders = new HttpHeaders();
		    httpHeaders.setLocation(redirectUri);
		    ResponseEntity responseEntity = new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
		    //return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
		    ResponseStringDTO response = ResponseStringDTO.builder().data(redirectUri.toString())
					.build();
			return  ResponseEntity.ok().body(response);
		    //return responseEntity;
			
		} catch (URISyntaxException e) {
			log.error(e.getMessage());
			return null;
		} //"/pay/pay/cancel"
		//return "redirect:/pay/item/all";
	}
	
	
	
	// 결제 취소시 실행 url
	@GetMapping("/pay/cancel")
	public String payCancel(HttpServletRequest req) {
		
		//세션 삭제 
		HttpSession session =  req.getSession(false);
		if(session != null)session.invalidate();
		return "redirect:/pay/item/all";
	}
    
	// 결제 실패시 실행 url    	
	@GetMapping("/pay/fail")
	public String payFail(HttpServletRequest req) {
		//세션 삭제 
		HttpSession session =  req.getSession(false);
		if(session != null)session.invalidate();
		return "redirect:/pay/item/all";
	}
	
	
}