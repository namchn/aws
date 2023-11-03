package com.example.demo.project.todo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.frame.dto.ResponseDTO;
import com.example.frame.dto.TestRequestBodyDTO;

@RestController
@RequestMapping("test") //리소스
public class TestController {

  @GetMapping
  public String testController(final HttpServletResponse response) {
	//response.addHeader("Cache-Control", "no-store");
    return "Hello World!";
  }
  
  
  //@GetMapping
  @RequestMapping(value="/test/t1",method=RequestMethod.GET) //리소스
  public String testController2(final HttpServletResponse response) {
	response.addHeader("Cache-Control", "no-store");
    return "Hello World!!!!";
  }
  

  @GetMapping("/testGetMapping")
  public String testControllerWithPath() {
    return "Hello World! testGetMapping ";
  }

  @GetMapping("/{id}")
  public String testControllerWithPathVariables(@PathVariable(required = false) int id) {
    return "Hello World! ID " + id;
  }

  // /test경로는 이미 존재하므로 /test/testRequestParam으로 지정했다.
  @GetMapping("/testRequestParam")
  public String testControllerRequestParam(@RequestParam(required = false) int id) {
    return "Hello World! ID " + id;
  }

  // /test경로는 이미 존재하므로 /test/testRequestBody로 지정했다.
  @GetMapping("/testRequestBody")
  public ResponseDTO<String>  testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
    
	int id = testRequestBodyDTO.getId();
	String message = testRequestBodyDTO.getMessage();
	  
	List<String> list = new ArrayList<>();
	list.add(message);
    //list.add("Hello World! I'm ResponseDTO");
    ResponseDTO<String> response = ResponseDTO.<String>builder()
    										.data(list)
    										.build();
    //System.out.println(response);
    return response;
  }

  @GetMapping("/testResponseEntity")
  public ResponseEntity<?> testControllerResponseEntity() {
    List<String> list = new ArrayList<>();
    list.add("Hello World! I'm ResponseEntity. And you got 400!");
    ResponseDTO<String> response = ResponseDTO.<String>builder()
    										.error("")
    										.data(list)
    										.build();
    // http status 200를 원한다면
    // ResponseEntity.ok().body(response); 사용
    // http status를 400로 설정.
    //return ResponseEntity.badRequest().body(response);
    // http status를 404로 설정.
    return ResponseEntity.status(404).body(response);
  }

  // /test경로는 이미 존재하므로 /test/testPostRequestParam으로 지정했다.
  @PostMapping("/testPostRequestParam")
  public String testControllerPostRequestParam(@RequestParam(required = false) int id) {
    return "Hello Post World! ID " + id;
  }
  
  
  

}
