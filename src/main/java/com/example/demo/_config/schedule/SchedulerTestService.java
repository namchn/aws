package com.example.demo._config.schedule;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Configuration
@PropertySource(value = "/config.properties")
public class SchedulerTestService {
		
	private String method;
	//private long validityInMilliseconds;

    public SchedulerTestService(@Value("${properties.environment.config.execution.method}") String method ) {
        //this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.method = method;
		//this.validityInMilliseconds = validityInMilliseconds;
    }
		
    
    	//https://dev-coco.tistory.com/176
    	// 매달 10일,20일 14시에 실행
    	//@Scheduled(cron = "0 0 14 10,20 * ?", zone = "Asia/Seoul")
		
    	//@Scheduled(fixedDelay = 90000) // 작업시간동안 딜레이  90초마다 실행
		//@Scheduled(fixedDelayString  = "90000") // 90초마다 실행
    	//@Scheduled(fixedRate = 50000, initialDelay = 30000)
		//@Scheduled(fixedRate = 90000, initialDelayString = "30000") //30초후 90초마다 실행
		@Scheduled(fixedRateString = "250000", initialDelayString = "30000") //30초후 250초마다 실행
		public void run() {
			String uuid = UUID.randomUUID().toString();
		    log.info("Scheduler uuid : "+uuid);
		    log.info("method : "+this.method);
		}

}
