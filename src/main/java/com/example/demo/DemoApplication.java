package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableJpaAuditing //JPA Auditing(감시, 감사) 기능을 활성화하기 위한 어노테이션으로 createdDate, modifiedDate처럼 DB에 데이터가 저장되거나 수정될 때 언제, 누가 했는지를 자동으로 관리한다.
@EnableScheduling //스케쥴링
//@EnableCaching // 캐싱 기능 사용
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
