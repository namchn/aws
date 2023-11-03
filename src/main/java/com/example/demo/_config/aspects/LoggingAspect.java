package com.example.demo._config.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

/**
 * 로깅 관련 AOP 구성
 *
 * @author : ncw
 * @fileName : LoggingAspect
 * @since : 2023/11/01
 */

@Aspect
@Component
@Slf4j
public class LoggingAspect {
	

    /**
     * Before: 대상 “메서드”가 실행되기 전에 Advice를 실행합니다.
     *
     * @param joinPoint
     */
    @Before("execution(* com.example.demo.project.todo.controller.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Before: " + joinPoint.getSignature().getName());
    }

    /**
     * After : 대상 “메서드”가 실행된 후에 Advice를 실행합니다.
     *
     * @param joinPoint
     */
    @After("execution(* com.example.demo.project.todo.controller.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        log.info("After: " + joinPoint.getSignature().getName());
    }

    /**
     * AfterReturning: 대상 “메서드”가 정상적으로 실행되고 반환된 후에 Advice를 실행합니다.
     *
     * @param joinPoint
     * @param result
     */
    @AfterReturning(pointcut = "execution(* com.example.demo.project.todo.controller.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("AfterReturning: " + joinPoint.getSignature().getName() + " result: " + result);
    }

    /**
     * AfterThrowing: 대상 “메서드에서 예외가 발생”했을 때 Advice를 실행합니다.
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "execution(* com.example.demo.project.todo.controller.*.*(..))", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.info("AfterThrowing: " + joinPoint.getSignature().getName() + " exception: " + e.getMessage());
    }

    /**
     * Around : 대상 “메서드” 실행 전, 후 또는 예외 발생 시에 Advice를 실행합니다.
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.example.demo.project.todo.controller.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Around before: " + joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        log.info("Around after: " + joinPoint.getSignature().getName());
        return result;
    }
	
	
    /**
     * After : 대상 “메서드” 실행 전, 후 또는 예외 발생 시에 Advice를 실행합니다.
     *
     * @param joinPoint
     * @return 
     */
    @After("execution(* com.example.demo.project.todo.controller.*.*(..))")
    public void logAfterTest(JoinPoint joinPoint) {
        log.info("After Test: " + joinPoint.getSignature().getName());
        log.info("After getSignature: " + joinPoint.getSignature());
       // log.info("After getArgs: " + joinPoint.getArgs());
        log.info("After getSourceLocation: " + joinPoint.getSourceLocation());
        //log.info("After getKind: " + joinPoint.getKind());
        log.info("After getStaticPart: " + joinPoint.getStaticPart());
        //log.info("After getThis: " + joinPoint.getThis());
        //log.info("After getTarget: " + joinPoint.getTarget());
        //log.info("After toString: " + joinPoint.toString());
        //log.info("After toShortString: " + joinPoint.toShortString());
        log.info("After toLongString: " + joinPoint.toLongString());
        
    }
    
    /**
     * Before : 대상 “메서드” 실행 전, 후 또는 예외 발생 시에 Advice를 실행합니다.
     *
     * @param joinPoint
     * @return 
     */
    @Before("bean(*Controller)")
    public void  beforeLog1(JoinPoint joinPoint) {
    	log.info("==이름이 Controller로 끝나는 모든 @Bean 호출 전 실행되는 로그 입니다.==");
    	log.info("Before toLongString: " + joinPoint.toLongString());
    }
    
    /**
     * After : 대상 “메서드” 실행 전, 후 또는 예외 발생 시에 Advice를 실행합니다.
     *
     * @param joinPoint
     * @return 
     */
    
    //@After("bean(Todo*)")
    //@After("execution(* com.example.demo.project.todo.service.*.*(..))")
    //@After("within(* com.example.demo.project.todo.service.*)") //클래스
    @After("@within(org.springframework.stereotype.Service)") //클래스 타입
    public void  afterLog2(JoinPoint joinPoint) {
    	log.info(" demo 패키지내의 모든 service 메소드 호출 후 실행되는 로그 입니다.");
    	log.info("After toLongString: " + joinPoint.toLongString());
    }
    
    /**
     * Around : 대상 “메서드” 실행 전, 후 또는 예외 발생 시에 Advice를 실행합니다.
     *
     * @param joinPoint
     * @return 
     */
    @Around("@within(org.springframework.stereotype.Service)") //클래스 타입
    public Object executionAspect(ProceedingJoinPoint joinPoint) throws Throwable
    {   
        StopWatch stopWatch = new StopWatch();
	    
        try { 
        	stopWatch.start();
            Object result = joinPoint.proceed();
			return result; // 실제 타겟 호출
		} finally {  
			stopWatch.stop();  
			
		    log.info("==demo 패키지내의 모든 service 메소드 호출 후 실행되는 시간측정 로그 입니다.==");
		    //log.debug("{} - Total time = {}s",  joinPoint.toLongString(),stopWatch.getTotalTimeSeconds());  
		    log.info("Around After Total time = {}s - {}",stopWatch.getTotalTimeSeconds(),  joinPoint.toLongString());  
		    
		    if(stopWatch.getTotalTimeSeconds() > 0.1) log.warn("0.1sec over :  Warn-After-Total-time = {}s - {}",stopWatch.getTotalTimeSeconds(),  joinPoint.toLongString());  
		 
		    log.info("Around After time-nano-sec: " + stopWatch.prettyPrint());
		}  
        
        //return result;
    }
	
	
}