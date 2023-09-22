package com.example.demo.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 스프링 빈으로 등록
public class WebMvcConfig implements WebMvcConfigurer {

  private final long MAX_AGE_SECS = 3600;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // 모든 경로에 대하여
    registry.addMapping("/**")
        // Origin이 http:localhost:3000에 대해.
        .allowedOrigins("http://localhost:3000",
        		"http://app.nc2030.com",
        		"https://app.nc2030.com",
        		"http://TodoApp-nc-front-service.us-west-2.elasticbeanstalk.com",
        		"https://TodoApp-nc-front-service.us-west-2.elasticbeanstalk.com",
        		"http://222.237.213.72",
        		"https://222.237.213.72",
        		"222.237.213.72",
        		"http://todoapp-front-service.eba-cfudfi7t.ap-southeast-2.elasticbeanstalk.com",
        		"https://todoapp-front-service.eba-cfudfi7t.ap-southeast-2.elasticbeanstalk.com")
        // GET, POST, PUT, PATCH, DELETE, OPTIONS 메서드를 허용한다.
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(MAX_AGE_SECS);
  }

}
