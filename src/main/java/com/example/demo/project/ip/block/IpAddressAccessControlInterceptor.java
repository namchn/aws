package com.example.demo.project.ip.block;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class IpAddressAccessControlInterceptor implements HandlerInterceptor {

    private final IpAddressMatcherManager ipAddressMatcherManager;

    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //getClientIp
        String clientIpAddress = getClientIp(request);

        if (ipAddressMatcherManager.isNotAccessible(clientIpAddress)) {
            String requestURI = request.getRequestURI();
            log.warn("Forbidden access. request uri={}, client ip={}", requestURI, clientIpAddress);

            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            //response.setStatus(403) ;
            //response.sendRedirect("/error");
            return false;
        }
        return true;
    }
    
    

	public static String getClientIp(HttpServletRequest request) {
	    String clientIp = null;
	    boolean isIpInHeader = false;
	
	    List<String> headerList = new ArrayList<>();
	    headerList.add("X-Forwarded-For");
	    headerList.add("HTTP_CLIENT_IP");
	    headerList.add("HTTP_X_FORWARDED_FOR");
	    headerList.add("HTTP_X_FORWARDED");
	    headerList.add("HTTP_FORWARDED_FOR");
	    headerList.add("HTTP_FORWARDED");
	    headerList.add("Proxy-Client-IP");
	    headerList.add("WL-Proxy-Client-IP");
	    headerList.add("HTTP_VIA");
	    headerList.add("IPV6_ADR");
	
	    for (String header : headerList) {
	        clientIp = request.getHeader(header);
	        if (StringUtils.hasText(clientIp) && !clientIp.equals("unknown")) {
	            isIpInHeader = true;
	            break;
	        }
	    }
	
	    if (!isIpInHeader) {
	        clientIp = request.getRemoteAddr();
	    }
	    return clientIp;
	}


}