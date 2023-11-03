package com.example.demo._config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.project.ip.block.IpAddressAccessControlInterceptor;

@Configuration // 스프링 빈으로 등록
public class WebMvcConfig implements WebMvcConfigurer {

  private final long MAX_AGE_SECS = 3600; //1시간

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // 모든 경로에 대하여
    registry.addMapping("/**")
        // cors 허용 url : Origin이 http:localhost:3000에 대해.
        .allowedOrigins(
        		"http://localhost:3000"
        		//"http://app.nc2030.com",
        		,"https://app.nc2030.com"
        		//"http://nc2030.com",
        		,"https://nc2030.com"
        		//,"http://TodoApp-nc-front-service.us-west-2.elasticbeanstalk.com"
        		,"https://TodoApp-nc-front-service.us-west-2.elasticbeanstalk.com"
        		)
        // GET, POST, PUT, PATCH, DELETE, OPTIONS 메서드를 허용한다.
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(MAX_AGE_SECS);
  }
  
  @Autowired
  private  IpAddressAccessControlInterceptor ipAddressAccessControlInterceptor;
  

  @Override
  public void addInterceptors(InterceptorRegistry registry) {	// 인터셉터 요청하는 주소
      registry.addInterceptor(ipAddressAccessControlInterceptor)
              .order(1)
      		  .addPathPatterns("/ip/**");  //접근 제어할 주소
              //.addPathPatterns("/url1", "/url2")
              //.excludePathPatterns("/resources/**", "/error/**");
  }

}
