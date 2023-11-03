
package com.example.demo._security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo._config.injection.ConfigrationInjectionService;
import com.example.demo._utility.ParameterGetter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private TokenProvider tokenProvider;
  
  @Autowired
  private ParameterGetter parameterGetter;
  
  @Autowired
  private ConfigrationInjectionService configrationInjectionService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      // 요청에서 토큰 가져오기.
      String token = parseBearerToken(request,response);
      log.info("Filter is running...");
      // 토큰 검사하기. JWT이므로 인가 서버에 요청 하지 않고도 검증 가능.
      if (token != null && !token.equalsIgnoreCase("null")) {
        // userId 가져오기. 위조 된 경우 예외 처리 된다.
        String userId = tokenProvider.validateAndGetUserId(token);
        
        /* 유효시간 초과 */
        /* 
        Date now = new Date();
        Date expiryDate = tokenProvider.validateAndGetExpiration(token);
        if(now.compareTo(expiryDate) > 0 ) {//날짜 지남.
        	log.info("Date expired : " + expiryDate );
        }
        */
        
        log.info("Authenticated user ID : " + userId );
        // 인증 완료; SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userId, // 인증된 사용자의 정보. 문자열이 아니어도 아무거나 넣을 수 있다. 보통 UserDetails라는 오브젝트를 넣는데, 우리는 안 만들었음.
            null, //
            AuthorityUtils.NO_AUTHORITIES
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
      }
    } catch (Exception ex) {
      logger.error("Could not set user authentication in security context", ex);
    }

    filterChain.doFilter(request, response);
  }

  private String parseBearerToken(HttpServletRequest request,HttpServletResponse response) {
	  boolean isCookie = configrationInjectionService.isUseCookie();
	  String method = configrationInjectionService.getMethod();
	  boolean ValidCSRF = configrationInjectionService.isUseValidCSRF();
	  
	  //개발 모드 일때 
	  if(method.equals("local")) {  //디버깅
		  parameterGetter.getAll(request);
	  }
		  
	  /*
	  if(false) {
		  //세션으로 수행 횟수 쿠키에 담기 
		  final String KEY = "controllerCount";
		  HttpSession session = ((HttpServletRequest) request).getSession(false);
	        if (session != null) {
	            Integer count = (Integer) session.getAttribute(KEY);
	            if (count == null) {
	                count = -1;
	            }
	            //쿠키생성 
	            session.setAttribute(KEY, count + 1);
	            Cookie cookie = new Cookie("controllerCount",String.valueOf(session.getAttribute(KEY)) );
		        cookie.setMaxAge(24 * 60 * 60);// expires in 7 days
		        // optional properties
		        cookie.setSecure(true);
		        cookie.setHttpOnly(true);
		        cookie.setPath("/");
		        // add cookie to response
		        response.addCookie(cookie);
	        }
	        
	  }
		*/
	
	  
	  //이건  토큰을 localstorage 에 저장할 때 2차로  csrf 취약점 추가검증 방법.
	  // 생각 해보니 쿠키를 사용하지 않으면 csrf 취약점은 존재할 수 없다. CSRF 공격(Cross Site Request Forgery)
	  //boolean flag = false;
	  //flag = true;
	  if(ValidCSRF) {	//프런트서버에서 credentials 사용시 사용가능 
		String csrfToken = null;
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				String name = cookies[i].getName();
				String value = cookies[i].getValue();
				
				if (name.equals("c.csrfToken") && !value.equals("")) {
					csrfToken = value;
					// 재사용이 불가능하도록 쿠키 만료 //다시 만들어 지기 떄문에 꼭필요하진 않지만 
					cookies[i].setPath("/");
					cookies[i].setValue("");
					cookies[i].setMaxAge(0);
	                response.addCookie(cookies[i]);
					break;
				}
			}
		}
		// Http 요청의 헤더를 파싱해 csrfToken 토큰을 리턴한다.
	    String headerToken = request.getHeader("c_csrfToken");
	    log.info("headerToken:"+headerToken);
	    log.info("csrfToken:"+csrfToken);
	    if (csrfToken ==null || !StringUtils.hasText(headerToken) || !csrfToken.equalsIgnoreCase(headerToken)){
	      return null;
	    }
	}  
	  
	  
	// 쿠키를 만들면 자동으로 세션 로그인이 되어 버리네. 쿠키를 가져오면 세션을 사용하게 됨. 확인필요.
	// authentication  가 기본적으로 세션을 사용하는 방식으로 보임.그래서 쿠키가 오면 세션을 사용. 확인필요.
	// Http 요청의 쿠키에 저장되어 있다면 토큰 값을 가져온다. (클라이언트 서버에 쿠키값이 만들어져 있어야 함.)
	// 백 서버에는 이곳과 user controller에 쿠키 설정이 되어 있으므로 프런트 서버만 설정하면 된다. 
	
	if(isCookie) {	//프런트서버에서 credentials 사용시 사용가능
	log.info("isCookie:"+"yes");
		
		String cToken = null;
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				String name = cookies[i].getName();
				String value = cookies[i].getValue();
				// log.debug(name + " : " + value+ "<br>");
				if (name.equals("aToken") && !value.equals("")) {
					cToken = value;
					break;
				}
			}
		}
		if(cToken != null) return cToken;
	}
	/**/
	  
    // Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴한다.
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}

