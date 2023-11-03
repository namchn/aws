package com.example.demo.project.user.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo._config.injection.ConfigrationInjectionService;
import com.example.demo._security.TokenProvider;
import com.example.demo.project.user.dto.UserDTO;
import com.example.demo.project.user.model.UserEntity;
import com.example.demo.project.user.service.UserService;
import com.example.frame.dto.ResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

  @Autowired
  private UserService userService;
  
  @Autowired
  private ConfigrationInjectionService configrationInjectionService;
  
  @Autowired
  private TokenProvider tokenProvider;
  
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  
  /* Bean으로 작성시.
  @Autowired
  private PasswordEncoder passwordEncoder;
  */
  
  
  
  	//@Autowired
	//private UserMapperRepository userMapperRepository;

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
    try {
    	
    	/*
    	UserMEntity user = new UserMEntity();
    	user.setId(null);
    	user.setUsername(userDTO.getUsername());
    	user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    	user.setRole(null);
    	user.setTel(userDTO.getTel());
    			
    	
    	userMapperRepository.save(user);
    	//UserMEntity registeredUser = userMapperRepository.sel(user);
    	
        return ResponseEntity.ok().body("??");	
        */
    
    	//
    	String pattern = "^(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{4,25}$";//  /^(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{4,25}$/
        
    	if(!userDTO.getPassword().matches(pattern)) {
    		throw new RuntimeException("Invalid Password value..");
    	}
    	//
    	
    	
      if(userDTO == null || userDTO.getPassword() == null ) {
        throw new RuntimeException("Invalid Password value.");
      }
      // 요청을 이용해 저장할 유저 만들기
      UserEntity user = UserEntity.builder()
          .username(userDTO.getUsername())
          .password(passwordEncoder.encode(userDTO.getPassword()))
          .tel(userDTO.getTel()) //전번 추가
          .build();
      // 서비스를 이용해 리포지터리 에 유저 저장
      UserEntity registeredUser = userService.create(user);
      UserDTO responseUserDTO = UserDTO.builder()
          .id(registeredUser.getId())
          .username(registeredUser.getUsername())
          .build();

      // 유저 정보는 항상 하나이므로 리스트로 만들어야 하는 ResponseDTO를 사용하지 않고  UserDTO 리턴.
      return ResponseEntity.ok().body(responseUserDTO);
      
      
    } catch (Exception e) {
      ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
      return ResponseEntity
    		  //.status(403)
    		  .badRequest()
    		  .body(responseDTO);
    }
  }


  @PostMapping("/signin")
  public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO,
          HttpServletResponse httpServletResponse) {
    UserEntity user = userService.getByCredentials(
        userDTO.getUsername(),
        userDTO.getPassword(),
        passwordEncoder);

    if(user != null) {
      // 토큰 생성
      final String token = tokenProvider.create(user);
      final UserDTO responseUserDTO = UserDTO.builder()
          .username(user.getUsername())
          .id(user.getId())
          .token(token)
          .build();
      
      
      
      
      
      
      //쿠키로 저장시.
      ResponseCookie responseCookie = ResponseCookie.from("aToken",token)
              .httpOnly(true)
              .secure(true)
              .path("/")
              .maxAge(1 * 60 * 60) //1시간
              //.domain("yourdomain.net")
              .build();
      
      //Cookie cookie = new Cookie("platform","mobile");
      // expires in 7 days
      //cookie.setMaxAge(24 * 60 * 60);
      // optional properties
      //cookie.setSecure(true);
      //cookie.setHttpOnly(true);
      //cookie.setPath("/");
      // add cookie to response
      //httpServletResponse.addCookie(cookie);
      
      
      boolean isCookie = configrationInjectionService.isUseCookie();
  	  if(isCookie) {
  	      return ResponseEntity.ok()
  				    		  .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
  				    		  .body(responseUserDTO);
  	  }else {
  		  //쿠키로 안할경우 
  		  return ResponseEntity.ok().body(responseUserDTO);
  	  }
  	  ///
      
    } else {
      ResponseDTO responseDTO = ResponseDTO.builder()
          .error("Login failed.")
          .build();
      return ResponseEntity
          .badRequest()
          .body(responseDTO);
    }
  }
  
  
  
  @PostMapping("/signout")  //쿠키로 로그인 했을 경우. //localstorage로 인증할경우는 사용하지 않음.
  public ResponseEntity<?> signout(@RequestBody UserDTO userDTO,
		  HttpServletRequest req, HttpServletResponse httpServletResponse) {
	  
  
	// 쿠키로 jwt를 받을 때: 요청시 브라우저에서 쿠키를 받으면 세션이 자동으로 적용되므로 이를 무효화.
	HttpSession oldSession = req.getSession();
	oldSession.invalidate();
	// 로그인 과정에서 세션-쿠키를 사용하지 않을 경우 필요하지 않은 과정
    
    try {
      //쿠키로 저장시.
      ResponseCookie responseCookie = ResponseCookie.from("aToken",null)
              .httpOnly(true)
              .secure(true)
              .path("/")
              //.maxAge(1 * 60 * 60) //1시간
              .maxAge(0) //1시간
              //.domain("yourdomain.net")
              .build();
      
      //Cookie cookie = new Cookie("JSESSIONID",null);
      // expires in 7 days
      //cookie.setMaxAge(24 * 60 * 60);
      //cookie.setMaxAge(0);
      // optional properties
      //cookie.setSecure(true);
      //cookie.setHttpOnly(true);
      //cookie.setPath("/");
      // add cookie to response
      //httpServletResponse.addCookie(cookie);
      
      
      
      
      final UserDTO responseUserDTO = UserDTO.builder()
    		  								.username(userDTO.getUsername())
    		  								.build();

      //쿠키로 안할경우 
      // return ResponseEntity.ok().body(responseUserDTO);
      return ResponseEntity.ok()
    		  				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
    		  				.body(responseUserDTO);
      
    } catch(Exception e) {
      ResponseDTO responseDTO = ResponseDTO.builder()
          .error("Logout failed.")
          .build();
      return ResponseEntity
          .badRequest()
          .body(responseDTO);
    }
  }

}

