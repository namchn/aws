package com.example.demo._config.cache;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Configuration
@PropertySource(value = "/config.properties")
public class SchedulerCacheService {
		
	private String method;
	//private long validityInMilliseconds;

    public SchedulerCacheService(@Value("${properties.environment.config.execution.method}") String method ) {
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
		//@Scheduled(fixedRateString = "250000", initialDelayString = "30000") //30초후 250초마다 실행
		//public void run() {
			//String uuid = UUID.randomUUID().toString();
		    //log.info("Scheduler uuid : "+uuid);
		    //log.info("method : "+this.method);
		//}
		
		
		
		@Scheduled(fixedRateString = "3600000", initialDelayString = "30000") //30초후 3600초마다 실행
		@CacheEvict(value = "todoById", allEntries = true)
		public void emptyCacheTodoById() {
			//String uuid = UUID.randomUUID().toString();
		    //log.info("Scheduler uuid : "+uuid);
		    log.info("Scheduled cache deleted : "+"todoById"+" - period : 3600sec");
		}

		@Scheduled(fixedRateString = "3600000", initialDelayString = "30000") //30초후 3600초마다 실행
		@CacheEvict(value = "itemOne", allEntries = true)
		public void emptyCacheItemOne() {
			//String uuid = UUID.randomUUID().toString();
		    //log.info("Scheduler uuid : "+uuid);
		    log.info("Scheduled cache deleted : "+"itemOne"+" - period : 3600sec");
		}
		
		@Scheduled(fixedRateString = "3600000", initialDelayString = "30000") //30초후 3600초마다 실행
		@Caching(
			evict = {
					@CacheEvict( value = {"itemList"}, allEntries = true )  // allEntries = true
					,@CacheEvict(value = {"itemCount"}, allEntries = true ) //key = "#userId"
			}
		)
		//@CacheEvict(value = "itemList", allEntries = true)
		public void emptyCacheItemList() {
			//String uuid = UUID.randomUUID().toString();
		    //log.info("Scheduler uuid : "+uuid);
		    log.info("Scheduled cache deleted : "+"itemList"+" - period : 3600sec");
		}
		
}
