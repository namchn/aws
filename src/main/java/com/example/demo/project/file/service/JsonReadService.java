package com.example.demo.project.file.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
public class JsonReadService {
	
	private String query; 
	
	public JsonReadService(){
		this.query =JsonRead();
	}
	
	public  String JsonRead() {
		
		String str = "";
		try {
		/////
		JSONParser parser = new JSONParser();
		
	    
		ClassPathResource resource = new ClassPathResource("data/mine.json");
		//byte[] bdata = FileCopyUtils.copyToByteArray(resource.getInputStream());
		//path = resource.getPath();
		//str = new String(bdata, StandardCharsets.UTF_8); 
		BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8"));

        // br.readLine() 이 null 인지 검사할 때 한번 사용되므로 String 에 먼저 저장해둬야한다.
		String s = "";
		StringBuilder sb = new StringBuilder();
		while((s = br.readLine()) != null){
		    sb.append(s);
		}
		ObjectMapper om = new ObjectMapper();
		JsonNode jsonNode = om.readTree(sb.toString());
		//System.out.println(jsonNode);
        
		str  = jsonNode.get("query").toString();
		
		/*
		String path ="D://json/mine.json";
		FileReader reader = new FileReader(path);
		Object obj = parser.parse(reader);
		JSONObject jsonObject = (JSONObject) obj;
		
		str  = (String) jsonObject.get("query");
		*/
		
		//reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	
}
