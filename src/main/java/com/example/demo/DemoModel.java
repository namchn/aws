package com.example.demo;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/* */

@Builder
@RequiredArgsConstructor
public class DemoModel {
	
	@NonNull
	private String id;
	
}




/*
자르파일 이클립스에 설치 
\boot\repo> java -jar lombok-1.18.22.jar

톰캣 시작
\boot\workspace\demo> ./gradlew bootRun

*/