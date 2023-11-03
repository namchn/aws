package com.example.demo._security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo._config.injection.ConfigrationInjectionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static com.example.demo._security.RedirectUrlCookieFilter.REDIRECT_URI_PARAM;

@Slf4j
@Component
@AllArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private ConfigrationInjectionService configrationInjectionService;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	//@Value("${environment.config.url.frontend.local}")
	//private static final String LOCAL_REDIRECT_URL = "http://localhost:3000";
    
	
	//private String LOCAL_REDIRECT_URL;

    //public OAuthSuccessHandler(@Value("${environment.config.url.frontend.local}") String key ) {
        //this.LOCAL_REDIRECT_URL = key;
    //}

    
	
	
	
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

	  String FRONTEND_REDIRECT_URL = configrationInjectionService.getFrontendUrl();
		
	  //log.info("provider auth DEV_REDIRECT_URL : " + DEV_REDIRECT_URL);
	  log.info("provider auth succeeded");
	  
	  //TokenProvider tokenProvider = new TokenProvider();
	  String token = tokenProvider.create(authentication);
	  
	  
	  //String subject = tokenProvider.validateAndGetUserId(token);
	  //final String username = (String) oAuth2User.getAttributes().get("login");
	  ApplicationOAuth2User userPrincipalOAuth2User = (ApplicationOAuth2User) authentication.getPrincipal();
	  final String subject =userPrincipalOAuth2User.getAttribute("login");
	  
	  
	  Optional<Cookie> oCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(REDIRECT_URI_PARAM)).findFirst();
	  Optional<String> redirectUri = oCookie.map(Cookie::getValue);

	  //response.getWriter().write(token);
	  //토큰을 리다이렉트
	  log.info("token {}", token);
	  //response.sendRedirect("http://localhost:3000/sociallogin?token="+token);
	  response.sendRedirect(redirectUri.orElseGet(() -> FRONTEND_REDIRECT_URL) + "/sociallogin?token=" + token+"&subject=" + subject);
  }

}