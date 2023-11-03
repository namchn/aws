package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.filter.CorsFilter;

import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.OAuthSuccessHandler;
import com.example.demo.security.OAuthUserServiceImpl;
import com.example.demo.security.RedirectUrlCookieFilter;

import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;
  
  @Autowired
  private OAuthUserServiceImpl oAuthUserService; //  OAuthUserServiceImpl 추가

  @Autowired
  private OAuthSuccessHandler oAuthSuccessHandler; // Success Handler 추가

  @Autowired
  private RedirectUrlCookieFilter redirectUrlCookieFilter;
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // http 시큐리티 빌더
    http.cors() // WebMvcConfig에서 이미 설정했으므로 기본 cors 설정.
        .and()
        .csrf()// csrf는 현재 사용하지 않으므로 disable
        .disable()
        .httpBasic()// token을 사용하므로 basic 인증 disable
        .disable()
        .sessionManagement()  // session 기반이 아님을 선언
        //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        .and()
        .authorizeRequests() // /와 /auth/** 경로는 인증 안해도 됨.
        .antMatchers("/","/js/**", "/ip/**",  "/test/**", "/file/**" ,"/robots.txt", "/auth/**", "/oauth2/**").permitAll()
        .anyRequest() // /와 /auth/**이외의 모든 경로는 인증 해야됨.
        .authenticated()
        .and()///////////   이하 oauth2   관련////////////////////////////
        .oauth2Login() //oath2Login 설정
        .redirectionEndpoint()  //리다이렉트로 돌아오는 지점 ("/")
        .baseUri("/oauth2/callback/*") // 콜백시 이주소로 호출하면 "/" 으로 돌아온다.
        .and()
        .authorizationEndpoint()
        .baseUri("/auth/authorize") // OAuth 2.0 흐름 시작을 위한 엔드포인트 추가  요청: {백엔드}/auth/authorize{provider} => 로그인 성공시 돌아오는 곳: {백엔드}/oauth2/authorization/github
        .and()
        .userInfoEndpoint()
        .userService(oAuthUserService) // OAuthUserServiceImpl:가져온 유저정보로 유저생성.
    	.and()
    	.successHandler(oAuthSuccessHandler) // Success Handler : 가져온 유저정보로 토큰 생성.
    	.and()
        .exceptionHandling()
        .authenticationEntryPoint(new Http403ForbiddenEntryPoint()); // 인증에 실패한 요청의 응답을 설정.
    
    // filter 등록.
    // 매 요청마다
    // CorsFilter 실행한 후에
    // jwtAuthenticationFilter 실행한다.
    http.addFilterAfter(
        jwtAuthenticationFilter,
        CorsFilter.class
    );
    http.addFilterBefore( // Before
    	redirectUrlCookieFilter,
        OAuth2AuthorizationRequestRedirectFilter.class // 리디렉트가 되기 전에 필터를 실행해야 한다.
    );
  }
}

