package com.example.demo._security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.demo._config.injection.ConfigrationInjectionService;
import com.example.demo._config.injection.YmlAndConfigrationInjectionService;
import com.example.demo.project.user.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenProvider {

	@Autowired
	private ConfigrationInjectionService configrationInjectionService;
	

	@Autowired
	private YmlAndConfigrationInjectionService ymlAndConfigrationInjectionService;


	private String SECRET_KEY;
	
	//application-{}.yml 에서 가져온다.
	//public TokenProvider(@Value("${security.jwt.token.secret.key}") String key ) {
    public TokenProvider(@Value("${ymlconfig.jwt.token.secret.key}") String key ) {
        this.SECRET_KEY = key;
        //this.SECRET_KEY = configrationInjectionService.getJwtTokenSecretKey();
    }
	
	//private   String KEY = configrationInjectionService.getJwtTokenSecretKey();
	//private static final String SECRET_KEY = "";
	

	/*
	private String secretKey;
	private long validityInMilliseconds;
    public TokenProvider(@Value("${security.jwt.token.secret-key}") String secretKey, @Value("${security.jwt.token.expire-length}") long validityInMilliseconds  ) {
        //this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        //this.secretKey = secretKey;
		//this.validityInMilliseconds = validityInMilliseconds;
    }
	
	*/


  public String create(UserEntity userEntity) {

	log.info("user JWT creating.");  
    // 기한 지금으로부터 1시간으로 설정
    Date expiryDate = Date.from(
        Instant.now()
            .plus(60, ChronoUnit.MINUTES));// 분단위

  /*
  { // header
    "alg":"HS512"
  }.
  { // payload
    "sub":"40288093784915d201784916a40c0001",
    "iss": "demo app",
    "iat":1595733657,
    "exp":1596597657
  }.
  // SECRET_KEY를 이용해 서명한 부분
  Nn4d1MOVLZg79sfFACTIpCPKqWmpZMZQsbNrXdJJNWkRv50_l7bPLQPwhMobT4vBOG6Q3JYjhDrKFlBSaUxZOg
  */
    
    // JWT Token 생성
    return Jwts.builder()
        // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
        .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        // payload에 들어갈 내용
        .setSubject(userEntity.getId()) // sub
        .setIssuer("todo app") // iss
        //.setIssuer("demo app") // iss
        .setIssuedAt(new Date()) // iat
        .setExpiration(expiryDate) // exp
        .compact();
  }


  public String validateAndGetUserId(String token) {
	log.info("user JWT validating.");  
    // parseClaimsJws메서드가 Base 64로 디코딩 및 파싱.
    // 즉, 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용 해 서명 후, token의 서명 과 비교.
    // 위조되지 않았다면 페이로드(Claims) 리턴, 위조라면 예외를 날림
    // 그 중 우리는 userId가 필요하므로 getBody를 부른다.
    Claims claims = Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(token)
        .getBody();

    return claims.getSubject();
  }

  
  /* 
  public Date validateAndGetExpiration(String token) {
	    // parseClaimsJws메서드가 Base 64로 디코딩 및 파싱.
	    // 즉, 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용 해 서명 후, token의 서명 과 비교.
	    // 위조되지 않았다면 페이로드(Claims) 리턴, 위조라면 예외를 날림
	    // 그 중 우리는 Expiration가 필요하므로 getBody를 부른다.
	    Claims claims = Jwts.parser()
	        .setSigningKey(SECRET_KEY)
	        .parseClaimsJws(token)
	        .getBody();

	    return claims.getExpiration();
	  }
   */
  
  public String create(final Authentication authentication) {
	  log.info("Authentication user JWT creating.");  
	    ApplicationOAuth2User userPrincipalOAuth2User = (ApplicationOAuth2User) authentication.getPrincipal();
	    Date expiryDate = Date.from(
	        Instant.now()
	            .plus(1, ChronoUnit.HOURS));

	    return Jwts.builder()
	    	// header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
	        .signWith(SignatureAlgorithm.HS512, SECRET_KEY)		
	        .setSubject(userPrincipalOAuth2User.getName()) // id가 리턴됨.
	        .setIssuer("todo app") // iss
	        .setIssuedAt(new Date())
	        .setExpiration(expiryDate)
	        .compact();
	  }
  
}

