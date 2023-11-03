package com.example.demo.project.pay.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.RepoAndServicesModuleConfiguration;
import com.example.demo._utility.RegexValidator;
import com.example.demo.project.admin.persistence.JoinUserRepository;
import com.example.demo.project.pay.dto.ItemDTO;
import com.example.demo.project.pay.model.table.ItemEntity;
import com.example.demo.project.pay.persistence.ItemRepository;
import com.example.demo.project.pay.service.impl.ItemServiceImpl;
import com.example.demo.project.todo.dto.TodoDTO;
import com.example.demo.project.todo.persistence.TodoRepository;
import com.example.demo.project.user.persistence.UserRepository;
import com.example.frame.controller.InterfaceController;
import com.example.frame.dto.ResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("pay/item")
public class ItemController implements InterfaceController{

	/*
	 * @GetMapping("/robots.txt") public String robotsTxt() { return
	 * "User-agent: *\n   Disallow: /md$ \n     Allow: /md \n"; }
	 */
	
	@Autowired
	private RepoAndServicesModuleConfiguration serviceConfiguration;
	
	@Autowired
	private ItemServiceImpl itemService;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private RegexValidator regexValidator;
	
	
	 /* admin  관련  */
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TodoRepository repository;
	@Autowired
	private JoinUserRepository joinUserRepository;
	
	
	@GetMapping({"/",""})
	public String itemCheck() {
		log.info("itemCheck..");
		return "The itemCheck service  is up and running...";
	}

	
	@GetMapping("/one")
	public ResponseEntity<?> one(@RequestParam(required = false) String id
								){

		log.info("pay/item/one..");
		
		try {
			//if(page == null) { page = 0 ; } 
			

			// List<String> list = new ArrayList<>();
			// List<ItemListDTO> list = itemRepository.getItemList();
			//List<ItemEntity> list = itemRepository.getJoinItems( "", "createdAt",  "price" , pageRequest);
			
			
			List<ItemDTO> dtos= new ArrayList();
			
			
			Optional<ItemEntity> optional = serviceConfiguration.getItemService().one(id);
			
			//Optional<ItemEntity> optional = itemService.one(id);
			if(optional.isPresent()) {
				ItemEntity entity =  optional.get();
				ItemDTO DTO = new ItemDTO(entity);
				dtos.add(DTO);
			}
			
			//List<ItemDTO> dtos = list.stream().map(ItemDTO::new).collect(Collectors.toList());

			//List<Long> count =itemService.retrieveCount(name, maxpriceInt, minpriceInt, "createdAt","asc","price","desc", pageRequest);
			
			//if (pass != null && !pass.equals("1234")) {
			//	list = null;
			//}

			// list.add("Hello World! I'm ResponseEntity. And you got 400!");
			ResponseDTO<ItemDTO> response = ResponseDTO.<ItemDTO>builder()
															.count(1L)
															.data(dtos)
															.message("no problem.")
															.build();
			// http status 200를 원한다면
			// ResponseEntity.ok().body(response); 사용
			// http status를 400로 설정.
			// return ResponseEntity.badRequest().body(response);
			// http status를 404로 설정.
			return ResponseEntity.status(200).body(response);
		}catch( Exception e) {
			ResponseDTO responseDTO = ResponseDTO.builder()
										.error(e.getMessage())
										.build();
		      return ResponseEntity
		    		  //.status(403)
		    		  .badRequest()
		    		  .body(responseDTO);
		}
		
	}
	
	
	@GetMapping("/list")
	public ResponseEntity<?> list(@RequestParam(required = false) String page
								, @RequestParam(required = false) String size
								, @RequestParam(required = false, defaultValue="" ) String name
								, @RequestParam(required = false) String maxprice
								, @RequestParam(required = false) String minprice){

		log.info("pay/item/list..");
		
		int pageInt = regexValidator.toInt(1,"^[0-9]*$" ,page );
		int sizeInt = regexValidator.toInt(20,"^[0-9]*$" ,size );
		int maxpriceInt = regexValidator.toInt(100000000,"^[0-9]*$" ,maxprice );
		int minpriceInt = regexValidator.toInt(0,"^[0-9]*$" ,minprice );
		if(name == null) { name = "" ; }
		
		
		try {
			//if(page == null) { page = 0 ; } 
			
			 
			PageRequest pageRequest = PageRequest.of(pageInt-1, sizeInt);

			// List<String> list = new ArrayList<>();
			// List<ItemListDTO> list = itemRepository.getItemList();
			//List<ItemEntity> list = itemRepository.getJoinItems( "", "createdAt",  "price" , pageRequest);
			
			List<ItemEntity> list =itemService.retrieve(name, maxpriceInt, minpriceInt, "createdAt","asc","price","desc", pageInt-1,sizeInt,pageRequest);
			List<ItemDTO> dtos = list.stream().map(ItemDTO::new).collect(Collectors.toList());

			List<Long> count =itemService.retrieveCount(name, maxpriceInt, minpriceInt, "createdAt","asc","price","desc", pageInt-1,sizeInt, pageRequest);
			
			//if (pass != null && !pass.equals("1234")) {
			//	list = null;
			//}

			// list.add("Hello World! I'm ResponseEntity. And you got 400!");
			ResponseDTO<ItemDTO> response = ResponseDTO.<ItemDTO>builder()
															.error("")
															.count(count.get(0))
															.data(dtos)
															.message("no problem.")
															.build();
			// http status 200를 원한다면
			// ResponseEntity.ok().body(response); 사용
			// http status를 400로 설정.
			// return ResponseEntity.badRequest().body(response);
			// http status를 404로 설정.
			return ResponseEntity.status(200).body(response);
		}catch( Exception e) {
			ResponseDTO responseDTO = ResponseDTO.builder()
										.error(e.getMessage())
										.build();
		      return ResponseEntity
		    		  //.status(403)
		    		  .badRequest()
		    		  .body(responseDTO);
		}
		
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> all(@RequestParam(required = false) String page
								, @RequestParam(required = false) String size){

		//log.info("itemList..");
		
		try {
			int pageInt = regexValidator.toInt(0,"^[0-9]*$" ,page );
			int sizeInt = regexValidator.toInt(20,"^[0-9]*$" ,size );
			
			PageRequest pageRequest = PageRequest.of(pageInt-1, sizeInt);
			// List<String> list = new ArrayList<>();
			// List<ItemListDTO> list = itemRepository.getItemList();
			//List<ItemEntity> list = itemRepository.getJoinItems( "", "createdAt",  "price" , pageRequest);
			
			
			List<ItemEntity> list = itemService.findAll("", 10000000, 0, "createdAt","asc","price","desc",pageInt-1, sizeInt, pageRequest);
			List<ItemDTO> dtos = list.stream().map(ItemDTO::new).collect(Collectors.toList());

			List<Long> count = itemService.findAllCount("", 10000000, 0, "createdAt","asc","price","desc",pageInt-1, sizeInt, pageRequest);
			
			

			//if (pass != null && !pass.equals("1234")) {
			//	list = null;
			//}

			// list.add("Hello World! I'm ResponseEntity. And you got 400!");
			ResponseDTO<ItemDTO> response = ResponseDTO.<ItemDTO>builder()
															.error("")
															.count(count.get(0))
															.data(dtos)
															.build();
			// http status 200를 원한다면
			// ResponseEntity.ok().body(response); 사용
			// http status를 400로 설정.
			// return ResponseEntity.badRequest().body(response);
			// http status를 404로 설정.
			return ResponseEntity.status(200).body(response);
		}catch( Exception e) {
			ResponseDTO responseDTO = ResponseDTO.builder()
										.error(e.getMessage())
										.build();
		      return ResponseEntity
		    		  //.status(403)
		    		  .badRequest()
		    		  .body(responseDTO);
		}
		
	}
	
	
	
	
	
	/*
	{
	    "itemName":"k9 자주포",
	    "type":"A",
	    "category":"A",
	    "amount":"10",
	    "price":"1000"
	}
	*/
	
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody ItemDTO itemDTO){

		try {
			//if(page == null) { page = 0 ; } 
			//if(size == null) { size = 0 ; }
			 

			// List<String> list = new ArrayList<>();
			// List<ItemListDTO> list = itemRepository.getItemList();
			//List<ItemEntity> list = itemRepository.getJoinItems(pageRequest,"createdAt","price");
			
			
			
		      //if(itemDTO == null || itemDTO.getPassword() == null ) {
		      //  throw new RuntimeException("Invalid Password value.");
		      //}
			
			
		      // 요청을 이용해 저장할 유저 만들기
		      ItemEntity itemEntity = ItemEntity.builder()
		          .itemName(itemDTO.getItemName())
		          .type(itemDTO.getType())
		          .category(itemDTO.getCategory())
		          .amount(itemDTO.getAmount())
		          .price(itemDTO.getPrice())
		          .useYn("Y")
		          .build();
		      // 서비스를 이용해 리포지터리 에 유저 저장
		      ItemEntity savedEntity = itemService.create(itemEntity);
		      //ItemEntity registeredUser = itemService.create(itemEntity);
		      ItemDTO responseDTO = ItemDTO.builder()
		    		  					.id(savedEntity.getId())
		    		  					.build();
		      // 아이템 정보는 항상 하나이므로 리스트로 만들어야 하는 ResponseDTO를 사용하지 않고  ItemDTO 리턴.
		      return ResponseEntity.ok().body(responseDTO);
		}catch( Exception e) {
			ResponseDTO responseDTO = ResponseDTO.builder()
										.error(e.getMessage())
										.build();
			
		      return ResponseEntity
		    		  //.status(403)
		    		  .badRequest()
		    		  .body(responseDTO);
		}
		
	}
	
	
	  @PutMapping("/update")
	  public ResponseEntity<?> update(@RequestBody ItemDTO itemDTO) {  //@AuthenticationPrincipal String userId,
	    //String temporaryUserId = "temporary-user"; // temporary user id.

	    // (1) dto를 entity로 변환한다.
		ItemEntity itemEntity = ItemDTO.toEntity(itemDTO);

	    // (2) id를 temporaryUserId로 초기화 한다. 여기는 4장 인증과 인가에서 수정 할 예정이다.
	    //entity.setUserId(userId);

	    // (3) 서비스를 이용해 entity를 업데이트 한다.
		ItemEntity entity = itemService.update(itemEntity);
		
		ItemDTO DTO = new ItemDTO(entity);

	    // (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.
	    //List<ItemDTO> dtos = entities.stream().map(ItemDTO::new).collect(Collectors.toList());

		List<ItemDTO> dtos= new ArrayList();
		dtos.add(DTO);		
	    // (5) 변환된 TodoDTO리스트를 이용해ResponseDTO를 초기화한다.
	    ResponseDTO<ItemDTO> response = ResponseDTO.<ItemDTO>builder()
	    										.count(1L)
	    										.data(dtos)
	    										.build();

	    // (6) ResponseDTO를 리턴한다.
	    return ResponseEntity.ok().body(response);
	  }

	  @DeleteMapping("/delete")
	  public ResponseEntity<?> delete(@RequestBody  ItemDTO itemDTO) {  //@AuthenticationPrincipal String userId,
	    try {
			// String temporaryUserId = "temporary-user"; // temporary user id.

			// (1) TodoEntity로 변환한다.
			ItemEntity itemEntity = ItemDTO.toEntity(itemDTO);

			// (2) 임시 유저 아이디를 설정 해 준다. 이 부분은 4장 인증과 인가에서 수정 할 예정이다. 지금은 인증과 인가 기능이 없으므로 한
			// 유저(temporary-user)만 로그인 없이 사용 가능한 애플리케이션인 셈이다
			// entity.setUserId(userId);

			// (3) 서비스를 이용해 entity를 삭제 한다.
			List<ItemEntity> entities = itemService.delete(itemEntity);
			// (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.
			List<ItemDTO> dtos = entities.stream().map(ItemDTO::new).collect(Collectors.toList());


			PageRequest pageRequest = PageRequest.of(0, 20);
			List<Long> count = itemService.findAllCount("", 10000000, 0, "createdAt","asc","price","desc",0,20, pageRequest);
			
			
			//ItemDTO DTO = new ItemDTO(entity);
			//List<ItemDTO> dtos = new ArrayList();
			//dtos.add(DTO);

		    // (5) 변환된 TodoDTO리스트를 이용해ResponseDTO를 초기화한다.
		    ResponseDTO<ItemDTO> response = ResponseDTO.<ItemDTO>builder()
		    										.data(dtos)
		    										.count(count.get(0))
		    										.build();
		    
		    // (6) ResponseDTO를 리턴한다.
		    return ResponseEntity.ok().body(response);
	    } catch (Exception e) {
	    	// (8) 혹시 예외가 나는 경우 dto대신 error에 메시지를 넣어 리턴한다.
	    	String error = e.getMessage();
	    	ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
	    		  									.error(error)
	    		  									.build();
	    	return ResponseEntity.badRequest().body(response);
		}
	}
	  
}
