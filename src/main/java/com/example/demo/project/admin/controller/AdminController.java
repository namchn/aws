package com.example.demo.project.admin.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo._utility.RegexValidator;
import com.example.demo.project.admin.model.JoinEntity;
import com.example.demo.project.admin.persistence.JoinUserRepository;
import com.example.demo.project.api.service.ApiService;
import com.example.demo.project.lotto.service.LotteriesService;
import com.example.demo.project.lotto.service.NumberGenerator;
import com.example.demo.project.lotto.service.RandomNumberGenerator;
import com.example.demo.project.pay.persistence.ItemRepository;
import com.example.demo.project.todo.dto.TodoDTO;
import com.example.demo.project.todo.model.TodoEntity;
import com.example.demo.project.todo.persistence.TodoRepository;
import com.example.demo.project.user.dto.UserDTO;
import com.example.demo.project.user.model.UserEntity;
import com.example.demo.project.user.persistence.UserRepository;
import com.example.demo.project.user.service.UserService;
import com.example.frame.controller.InterfaceController;
import com.example.frame.dto.ResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("admin")
public class AdminController implements InterfaceController{

	@Autowired
	private ApiService apiService;
	
	@Autowired
	private RegexValidator regexValidator;
	
	
	  /* admin  관련  */
		@Autowired
		private UserRepository userRepository;
		@Autowired
		private TodoRepository repository;
		@Autowired
		private JoinUserRepository joinUserRepository;

		@Autowired  //알아서 빈을 찾아서 인스턴스 가져오는 것
		private UserService service;

		  
		@GetMapping("/all")
	  public ResponseEntity<?> getUserList(	@AuthenticationPrincipal String userId	
			  						//,@RequestParam(required = false) String pass
			  							) {
		  
			//ncw 만 접근 가능 
			boolean adminYn= true;
			Optional<UserEntity> Optional= userRepository.findById(userId);
			if(Optional.isPresent()) {
				UserEntity ue = Optional.get();
				String admin = ue.getUsername();
				if(admin.equals("ncw") || admin.equals("ncw2") || admin.equals("ncw3") ) {
					adminYn= true;
				}else {
					adminYn= false;
				}
			}
			if(!adminYn) {
					ResponseDTO response = ResponseDTO.builder()
							.error("접근불가")
							.build();
					return ResponseEntity.ok().body(response);
			}
			//
			
		
		//List<String> list = new ArrayList<>();  
		List<UserEntity> list =  userRepository.findAll(); //전체 유저 
		
	    //list.add("Hello World! I'm ResponseEntity. And you got 400!");
	    ResponseDTO<UserEntity> response = ResponseDTO.<UserEntity>builder()
	    										.error("")
	    										.data(list)
	    										.build();
	    // http status 200를 원한다면
	    // ResponseEntity.ok().body(response); 사용
	    // http status를 400로 설정.
	    //return ResponseEntity.badRequest().body(response);
	    // http status를 404로 설정.
	    return ResponseEntity.status(200).body(response);
	  }
	 
		

		
		@PostMapping("/userList")
		public ResponseEntity<?> postUserList(@AuthenticationPrincipal String userId
											//,@RequestParam(required = false) String name
											) {

			//if (name != null) {				list = null;}

			// String temporaryUserId = "temporary-user"; // temporary user id.

			log.info("userId : {}" , userId );
			
			//ncw 만 접근 가능 
			boolean adminYn= true;
			Optional<UserEntity> Optional= userRepository.findById(userId);
			if(Optional.isPresent()) {
				UserEntity ue = Optional.get();
				String admin = ue.getUsername();
				if(admin.equals("ncw") || admin.equals("ncw2") || admin.equals("ncw3")) {
					adminYn= true;
				}else {
					adminYn= false;
				}
			}
			if(!adminYn) {
					ResponseDTO response = ResponseDTO.builder()
							.error("접근불가")
							.build();
					return ResponseEntity.ok().body(response);
			}
			//

			//List<UserEntity> list = service.retrieveAll();
			List<UserEntity> list = service.retrieveAll();
			
			
			// (5) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.
			List<UserDTO> dtos = list.stream().map(UserDTO::new).collect(Collectors.toList());

			// list.add("Hello World! I'm ResponseEntity. And you got 400!");
			ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder()
														//.error(userId)
														.data(dtos)
														.build();

			// (7) ResponseDTO를 리턴한다.
			return ResponseEntity.ok().body(response);
		}
		  
		
		
		
		@PostMapping("/todo")
	  public ResponseEntity<?> postAllTodo(@AuthenticationPrincipal String userId
			  							,@RequestBody Map<String,String> map
			  							//,@RequestParam(required = false) String name
			  							) {
			
			//
			boolean adminYn= true;
			
			String name = null;
			if(map.get("id") != null) {
				name =map.get("id") ; 
			}

			if(name == null) {
				adminYn =false;
			}
			
			Optional<UserEntity> Optional= userRepository.findById(userId);
			if(Optional.isPresent()) {
				UserEntity ue = Optional.get();
				String admin = ue.getUsername();
				if(admin.equals("ncw") || admin.equals("ncw2") || admin.equals("ncw3")) {
					adminYn= true;
				}else {
					adminYn= false;
				}
			}
			
			
			if(!adminYn) {
					ResponseDTO response = ResponseDTO.builder()
							.error("접근불가")
							.build();
					return ResponseEntity.ok().body(response);
			}
			//
			
		 List<TodoEntity> entities = repository.findByUserIdQuery(name);
		 List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

	    //list.add("Hello World! I'm ResponseEntity. And you got 400!");
	    ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
	    										//.error("")
	    										.data(dtos)
	    										.build();
	    // http status 200를 원한다면
	    // ResponseEntity.ok().body(response); 사용
	    // http status를 400로 설정.
	    //return ResponseEntity.badRequest().body(response);
	    // http status를 404로 설정.
	    return ResponseEntity.status(200).body(response);
	  }
	  
		
		
		  //@PostMapping("/join")
		  @GetMapping("/join")
		  public ResponseEntity<?> getJoin(@AuthenticationPrincipal String userId	
				  							//,@RequestBody Map<String,String> map
				  							//,@RequestParam(required = false) String pass
				  							,@RequestParam(required = false, defaultValue="0") String page
				  							,@RequestParam(required = false, defaultValue="20") String size
				  							) {
			  
			//if(pass =null) {list = null;}
			 
			/*
			String page = null;
			String size = null;
			if(map.get("page") != null && map.get("size") != null ) {
				page = map.get("page") ; 
				size = map.get("size") ; 
			} 
			*/ 
			int pageInt = regexValidator.toInt(0,"^[0-9]*$" ,page );
			int sizeInt = regexValidator.toInt(20,"^[0-9]*$" ,size );
			PageRequest pageRequest = PageRequest.of(pageInt-1, sizeInt>100 ? 100 : sizeInt );
			
			  
			//
			boolean adminYn= true;
			Optional<UserEntity> Optional= userRepository.findById(userId);
			if(Optional.isPresent()) {
				UserEntity ue = Optional.get();
				String admin = ue.getUsername();
				if(admin.equals("ncw") || admin.equals("ncw2") || admin.equals("ncw3")) {
					adminYn= true;
				}else {
					adminYn= false;
				}
			}
			if(!adminYn) {
					ResponseDTO response = ResponseDTO.builder()
							.error("접근불가")
							.build();
					return ResponseEntity.ok().body(response);
			}
			//

			//List<String> list = new ArrayList<>();  
			List<JoinEntity> list =  joinUserRepository.getJoinUser(pageRequest);
			long totalcount = (long)joinUserRepository.getJoinUserCount();
			//List<Object[]> list = null;
			
		    //list.add("Hello World! I'm ResponseEntity. And you got 400!");
		    ResponseDTO<JoinEntity> response = ResponseDTO.<JoinEntity>builder()
		    										.totalcount(totalcount)		    										.error("")
		    										.data(list)
		    										.build();
		    // http status 200를 원한다면
		    // ResponseEntity.ok().body(response); 사용
		    // http status를 400로 설정.
		    //return ResponseEntity.badRequest().body(response);
		    // http status를 404로 설정.
		    return ResponseEntity.status(200).body(response);
		  }
		
		  //@GetMapping("/ulist")
		  @PostMapping("/ulist")
		  public ResponseEntity<?> userList(@AuthenticationPrincipal String userId	
				  							,@RequestBody Map<String,String> map
				  							//,@RequestParam(required = false) int page
				  							//,@RequestParam(required = false) int size
				  							) {
			  

				//List<Object[]> list = null;
				
			// if(page == 1234) {list = null;}
				  
			String page = null;
			String size = null;
			if(map.get("page") != null && map.get("size") != null ) {
				page = map.get("page") ; 
				size = map.get("size") ; 
			}  
			int pageInt = regexValidator.toInt(0,"^[0-9]*$" ,page );
			int sizeInt = regexValidator.toInt(20,"^[0-9]*$" ,size );
			
			
			  
			PageRequest pageRequest = PageRequest.of(pageInt, sizeInt);

			//
			boolean adminYn= true;
			Optional<UserEntity> Optional= userRepository.findById(userId);
			if(Optional.isPresent()) {
				UserEntity ue = Optional.get();
				String admin = ue.getUsername();
				if(admin.equals("ncw") || admin.equals("ncw2") || admin.equals("ncw3")) {
					adminYn= true;
				}else {
					adminYn= false;
				}
			}
			if(!adminYn) {
					ResponseDTO response = ResponseDTO.builder()
							.error("접근불가")
							.build();
					return ResponseEntity.ok().body(response);
			}
			//
			
			//List<String> list = new ArrayList<>();  
			List<UserEntity> list =  joinUserRepository.findByUsernameGreaterThan(pageRequest);
			
			Page<UserEntity> p =   joinUserRepository.findByUsername(pageRequest);
			
			
		    //list.add("Hello World! I'm ResponseEntity. And you got 400!");
		    ResponseDTO<UserEntity> response = ResponseDTO.<UserEntity>builder()
		    										//.error("")
		    										.data(list)
		    										.build();
		    // http status 200를 원한다면
		    // ResponseEntity.ok().body(response); 사용
		    // http status를 400로 설정.
		    //return ResponseEntity.badRequest().body(response);
		    // http status를 404로 설정.
		    return ResponseEntity.status(200).body(response);
		  }
		
		
		
	

}
