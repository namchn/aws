package com.example.demo.project.pay.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.example.demo.project.pay.model.join.BasketJoinItemEntity;
import com.example.demo.project.pay.model.table.ItemEntity;
import com.example.demo.project.pay.model.table.UserBasketEntity;
import com.example.demo.project.pay.persistence.ItemRepository;
import com.example.demo.project.pay.service.BasketService;
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
@RequestMapping("basket")
public class BasketController implements InterfaceController{

	
	@Autowired
	private ItemServiceImpl itemService;
	
	@Autowired
	private BasketService basketService;

	//@Autowired
	//private ItemConfirmService itemConfirmService;
	
	@Autowired
	private RegexValidator regexValidator;
	
	@Autowired
	private ItemRepository itemRepository;
	
	 /* admin  관련  */
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TodoRepository repository;
	
	@Autowired
	private JoinUserRepository joinUserRepository;
	
	@GetMapping({"/basket"})
	public String payCheck() {
		log.info("basketCheck..");
		return "The basketCheck service  is up and running...";
	}

	@GetMapping("/list")
	public ResponseEntity<?> list(  @AuthenticationPrincipal String userId,
								  @RequestParam(required = false) String page
								, @RequestParam(required = false) String size){

		//String userId = "ncw11111";
		//log.info("itemList..");
		if(userId == null) {
			userId = "ncw11111";
		}
		
		
		try {
			//if(page == null) { page = 0 ; } 
			//if(size == null) { size = 0 ; }
			int pageInt = regexValidator.toInt(0,"^[0-9]*$" ,page );
			int sizeInt = regexValidator.toInt(20,"^[0-9]*$" ,size );
			PageRequest pageRequest = PageRequest.of(pageInt>0? pageInt-1 :pageInt , sizeInt);

			// List<String> list = new ArrayList<>();
			// List<ItemListDTO> list = itemRepository.getItemList();
			//List<ItemEntity> list = itemRepository.getJoinItems( "", "createdAt",  "price" , pageRequest);
			
			List<BasketJoinItemEntity> list = basketService.findByUserIdAndUseYAndJoin(userId,"", 100000000, 0, "createdAt","asc","count","desc", pageRequest);
			//List<UserBasketEntity> list = basketService.findByUserIdAndUseY(userId,"", 100000000, 0, "createdAt","asc","count","desc", pageRequest);
			//List<BasketDTO> dtos = list.stream().map(BasketDTO::new).collect(Collectors.toList());

			//사용자 고유 아이디 널처리
			//for(BasketDTO dto : dtos) {
			//	dto.setUserId(null);	
			//}
			
			List<Long> count = basketService.findByUserIdAndUseYCount(userId,"", 100000000, 0, "createdAt","asc","count","desc", pageRequest);
			
			//if (pass != null && !pass.equals("1234")) {
			//	list = null;
			//}

			// list.add("Hello World! I'm ResponseEntity. And you got 400!");
			ResponseDTO<BasketJoinItemEntity> response = ResponseDTO.<BasketJoinItemEntity>builder()
															//.error("")
															.count(count.get(0))
															//.data(dtos)
															.data(list)
															.build();
			
			if(userId == null || userId.equals("anonymousUser")) {
				//throw new RuntimeException("jwtout.");
				response.setError("jwtout");
			}
			
			
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
	
	// "id": "?????????????????",
	@GetMapping("/one")
	public ResponseEntity<?> one(  @AuthenticationPrincipal String userId,
									@RequestParam(required = false) String id){

		//String userId = "ncw11111";
		//log.info("itemList..");
		if(userId == null) {
			userId = "ncw11111";
		}
		
		if(id == null) {
			log.warn("Id is empty.");
			throw new RuntimeException("Id is empty.");
		} 
		
		try {
			UserBasketEntity entity = basketService.findById(userId,id);
			
			BasketDTO responseDTO = new BasketDTO(entity);
			
		    // (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.
		    //List<ItemDTO> dtos = entities.stream().map(ItemDTO::new).collect(Collectors.toList());
			List<BasketDTO> dtos= new ArrayList();
			dtos.add(responseDTO);		
		    // (5) 변환된 TodoDTO리스트를 이용해ResponseDTO를 초기화한다.
		    ResponseDTO<BasketDTO> response = ResponseDTO.<BasketDTO>builder()
		    										.count(1L)
		    										.data(dtos)
		    										.build();
		    return ResponseEntity.ok().body(response);
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
	    "id": "??????????????",
	    "userId":"k9 자주포1",
	    "itemId":"????????????????",
	    "count": 2,
	    "useYn": "N"
	}
	*/
	@PostMapping("/create")
	public ResponseEntity<?> create(  @AuthenticationPrincipal String userId,
									@RequestBody BasketDTO basketDTO){

		//String userId = "ncw11111";
		//String userId = userId;
		if(userId == null) {
			userId = "ncw11111";
		}
		
		
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
			UserBasketEntity userBasketEntity = UserBasketEntity.builder()
									          .userId(userId)
									          .itemId(basketDTO.getId())
									          .count(basketDTO.getCount())
									          .useYn("Y")
									          .build();

			// 재고 확인
			resultDTO rDto = itemService.itemIdCheck(userBasketEntity);
			if(!rDto.isSuccess()) {
				ResponseDTO<resultDTO> response = ResponseDTO.<resultDTO>builder()
						.error(rDto.getError())
						.message(rDto.getMessage())
						.build();
				return ResponseEntity.ok().body(response);
			}
			
		    // 서비스를 이용해 리포지터리 에 유저 저장
			UserBasketEntity savedEntity = basketService.create(userBasketEntity);
		   
			
			
			//BasketDTO responseDTO = BasketDTO.builder().id(savedEntity.getId()).build();
		    BasketDTO responseDTO = new BasketDTO(savedEntity);
		  	
		    //사용자 고유 아이디 빼기 
		    responseDTO.setUserId(null);
		    
		    // (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.
		    //List<ItemDTO> dtos = entities.stream().map(ItemDTO::new).collect(Collectors.toList());
			List<BasketDTO> dtos= new ArrayList();
			dtos.add(responseDTO);		
		    // (5) 변환된 TodoDTO리스트를 이용해ResponseDTO를 초기화한다.
		    ResponseDTO<BasketDTO> response = ResponseDTO.<BasketDTO>builder()
		    										.count(1L)
		    										.data(dtos)
		    										.build();
		
		    // 아이템 정보는 항상 하나이므로 리스트로 만들어야 하는 ResponseDTO를 사용하지 않고  ItemDTO 리턴.
		    //return ResponseEntity.ok().body(responseDTO);
		    // (6) ResponseDTO를 리턴한다.
		    return ResponseEntity.ok().body(response);
		      
		      
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
		    "id": "???????????????",
		    "userId": "ncw11111",
		    "itemId": "???????????????",
		    "count": 5000
		}	
		*/
		
	  @PutMapping("/update")
	  public ResponseEntity<?> update( @AuthenticationPrincipal String userId,
			  						@RequestBody BasketDTO basketDTO) { 	
	    //String temporaryUserId = "temporary-user"; // temporary user id.
		//String userId = "ncw11111";
		  if(userId == null) {
				userId = "ncw11111";
			}
		  
		  
	    // (1) dto를 entity로 변환한다.
		//UserBasketEntity userBasketEntity = BasketDTO.toEntity(basketDTO);
		
		UserBasketEntity userBasketEntity = UserBasketEntity.builder()
				  .id(basketDTO.getId())
				  .userId(userId)
		          .itemId(basketDTO.getItemId())
		          .count(basketDTO.getCount())
		          .useYn(basketDTO.getUseYn())
		          .build();
	
	    // (2) id를 temporaryUserId로 초기화 한다. 여기는 4장 인증과 인가에서 수정 할 예정이다.
	    //entity.setUserId(userId);

		// 재고 확인
		resultDTO rDto = itemService.itemIdCheck(userBasketEntity);
		if(!rDto.isSuccess()) {
			ResponseDTO<resultDTO> response = ResponseDTO.<resultDTO>builder()
					.message(rDto.getMessage())
					.error(rDto.getError())
					.build();
			return ResponseEntity.ok().body(response);
		}
		
	    // (3) 서비스를 이용해 entity를 업데이트 한다.
		UserBasketEntity savedEntity = basketService.update(userBasketEntity);
		
		BasketDTO DTO = new BasketDTO(savedEntity);
		
		//사용자 고유 번호 숨김.
		DTO.setUserId(null);
	
	    // (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.
	    //List<ItemDTO> dtos = entities.stream().map(ItemDTO::new).collect(Collectors.toList());
	
		List<BasketDTO> dtos= new ArrayList();
		dtos.add(DTO);		
	    // (5) 변환된 TodoDTO리스트를 이용해ResponseDTO를 초기화한다.
	    ResponseDTO<BasketDTO> response = ResponseDTO.<BasketDTO>builder()
	    										.data(dtos)
	    										.count(1L)
	    										.build();
	
	    // (6) ResponseDTO를 리턴한다.
	    return ResponseEntity.ok().body(response);
	  }

	  @DeleteMapping("/delete")
	  public ResponseEntity<?> delete(  @AuthenticationPrincipal String userId,
			  							@RequestBody  BasketDTO basketDTO) { 
		  
		  
		  //본인인지 확인 필요 
		  //String userId = "ncw11111";
		  
	    try {
			// String temporaryUserId = "temporary-user"; // temporary user id.

			// (1) TodoEntity로 변환한다.
	    	UserBasketEntity entity = BasketDTO.toEntity(basketDTO);

			// (2) 임시 유저 아이디를 설정 해 준다. 이 부분은 4장 인증과 인가에서 수정 할 예정이다. 지금은 인증과 인가 기능이 없으므로 한
			// 유저(temporary-user)만 로그인 없이 사용 가능한 애플리케이션인 셈이다
			// entity.setUserId(userId);

			// (3) 서비스를 이용해 entity를 삭제 한다.
	    	List<UserBasketEntity> entities = basketService.delete(entity);
	    	
	    	List<BasketDTO> dtos =  entities.stream().map(BasketDTO::new).collect(Collectors.toList());
	    	PageRequest pageRequest = PageRequest.of(0, 20);
	    	List<Long> count = basketService.findByUserIdCount(userId, "", 10000000, 0, "createdAt","asc","count","desc", pageRequest);
			
			//ItemDTO DTO = new ItemDTO(userBasketEntity);
			//List<ItemDTO> dtos = new ArrayList();
			//dtos.add(DTO);
			// (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.

		    // (5) 변환된 TodoDTO리스트를 이용해ResponseDTO를 초기화한다.
		    ResponseDTO<BasketDTO> response = ResponseDTO.<BasketDTO>builder()
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
