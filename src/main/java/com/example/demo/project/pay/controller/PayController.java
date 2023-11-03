package com.example.demo.project.pay.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo._utility.RegexValidator;
import com.example.demo.project.admin.model.JoinEntity;
import com.example.demo.project.admin.persistence.JoinUserRepository;
import com.example.demo.project.pay.dto.BasketDTO;
import com.example.demo.project.pay.dto.ItemDTO;
import com.example.demo.project.pay.dto.ItemListDTO;
import com.example.demo.project.pay.dto.resultDTO;
import com.example.demo.project.pay.model.table.ItemEntity;
import com.example.demo.project.pay.model.table.UserBasketEntity;
import com.example.demo.project.pay.persistence.ItemRepository;
import com.example.demo.project.pay.service.PayService;
import com.example.demo.project.pay.service.impl.ItemServiceImpl;
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
@RequestMapping("pay")
public class PayController implements InterfaceController{

	
	@Autowired
	private ItemServiceImpl itemService;
	
	//@Autowired
	//private ItemConfirmService itemConfirmService;
	
	@Autowired
	private PayService payService;
	
	@Autowired
	private ItemRepository itemRepository;
	
	 /* admin  관련  */
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TodoRepository repository;
	@Autowired
	private JoinUserRepository joinUserRepository;
	
	@Autowired
	private RegexValidator regexValidator;
	
	
	/*
	@GetMapping({"/",""})
	public String payCheck() {
		log.info("payCheck..");
		return "The payCheck service  is up and running...";
	}
	*/
	
	
	@PostMapping({"/excute"})
	public ResponseEntity<?> payExcute( //@AuthenticationPrincipal String userId,
							@RequestBody BasketDTO basketDTO) {
		
		String userId = "ncw11111";
		
		
		UserBasketEntity userBasketEntity = UserBasketEntity.builder()
													        .userId(userId)
													        .id(basketDTO.getId())
													        .itemId(basketDTO.getItemId())
													        .count(basketDTO.getCount())
													        .useYn("Y")
													        .build();
		
		// 재고 확인
		resultDTO rDto = itemService.basketIdCheck(userBasketEntity);
		if(!rDto.isSuccess()) {
			ResponseDTO<resultDTO> response = ResponseDTO.<resultDTO>builder()
					.error(rDto.getError())
					.message(rDto.getMessage())
					.build();
			return ResponseEntity.ok().body(response);
		}

		UserBasketEntity BasketEntity  = payService.excute(userBasketEntity);
		
		
		
		return ResponseEntity.ok().body(BasketEntity);
	}


	
	
	////////////////
	
	@GetMapping("/join")
	  public ResponseEntity<?> adminJoin(@RequestParam(required = false) String pass) {
		  
		

		//List<String> list = new ArrayList<>();  
		List<JoinEntity> list =   null; //joinUserRepository.getJoinUser();
		//List<Object[]> list = null;
		
		
		if(!pass.equals("1234")) {
			list = null;
		}
		  
	    //list.add("Hello World! I'm ResponseEntity. And you got 400!");
	    ResponseDTO<JoinEntity> response = ResponseDTO.<JoinEntity>builder()
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
	
	
	@GetMapping("/joins")
	  public ResponseEntity<?> adminJoins(@RequestParam(required = false) String page,
				  							@RequestParam(required = false) String size) {
		
		/*
		if(page == null) {
			page = 0 ;
		}
		if(size == null) {
			size = 0 ;
		}
		*/
		int pageInt = regexValidator.toInt(0,"^[0-9]*$" ,page );
		int sizeInt = regexValidator.toInt(20,"^[0-9]*$" ,size );
		PageRequest pageRequest = PageRequest.of(pageInt, sizeInt);  
		
		//List<String> list = new ArrayList<>();  
		List<JoinEntity> list =  joinUserRepository.getJoinUsers(pageRequest);
		//List<Object[]> list = null;
		
		
	    //list.add("Hello World! I'm ResponseEntity. And you got 400!");
	    ResponseDTO<JoinEntity> response = ResponseDTO.<JoinEntity>builder()
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
	
	@GetMapping("/ulist")
	  public ResponseEntity<?> userList(@RequestParam(required = false) String page,
			  							  @RequestParam(required = false) String size) {
		  
		int pageInt = regexValidator.toInt(0,"^[0-9]*$" ,page );
		int sizeInt = regexValidator.toInt(20,"^[0-9]*$" ,size );
		PageRequest pageRequest = PageRequest.of(pageInt, sizeInt);

		//List<String> list = new ArrayList<>();  
		List<UserEntity> list =  joinUserRepository.findByUsernameGreaterThan(pageRequest);
		
		Page<UserEntity> p =   joinUserRepository.findByUsername(pageRequest);
		
		
		//List<Object[]> list = null;
		
		  
	    //list.add("Hello World! I'm ResponseEntity. And you got 400!");
	    ResponseDTO<UserEntity> response = ResponseDTO.<UserEntity>builder()
	    										.error(  Long.toString( p.getTotalElements() )  )
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
