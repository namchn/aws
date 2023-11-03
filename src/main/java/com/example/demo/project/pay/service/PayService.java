package com.example.demo.project.pay.service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo._security.TokenProvider;
import com.example.demo.project.pay.dto.ItemDTO;
import com.example.demo.project.pay.dto.resultDTO;
import com.example.demo.project.pay.model.table.ItemEntity;
import com.example.demo.project.pay.model.table.UserBasketEntity;
import com.example.demo.project.pay.persistence.BasketRepository;
import com.example.demo.project.pay.persistence.ItemRepository;
import com.example.demo.project.pay.service.impl.ItemServiceImpl;
import com.example.demo.project.user.persistence.UserRepository;

@Slf4j
@Service
public class PayService {

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private BasketRepository basketRepository;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@Autowired
	private ItemValidator itemValidator;
	
	@Autowired
	private BasketValidator basketValidator;
	
	//@Autowired
	//private ItemConfirmService itemConfirmService;
	
	//@Autowired
	private ItemServiceImpl itemService;
		
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	
	
	public List<UserBasketEntity> findByUserId(String userId, String itemName, int maxprice, int lowprice, String orderby1, String order1Type, String orderby2, String order2Type, Pageable pageable) {
		log.info("Entity itemName : {} is retrieved.", itemName);
		//return itemRepository.getJoinItems(itemName, maxprice, lowprice, orderby1, order1Type, orderby2, order2Type, pageable);
		return basketRepository.findByUserId(userId, orderby1,  orderby2 , pageable);
	}
	public List<Long> findByUserIdCount(String userId, String itemName, int maxprice, int lowprice, String orderby1, String order1Type, String orderby2, String order2Type, Pageable pageable) {
		//log.info("Entity itemName : {} is retrieved.", itemName);
		//return itemRepository.getJoinItems(itemName, maxprice, lowprice, orderby1, order1Type, orderby2, order2Type, pageable);
		return basketRepository.findByUserIdCount(userId, orderby1,  orderby2 , pageable);
	}
	
	@Transactional
	public UserBasketEntity excute(final UserBasketEntity userBasketEntity) {
		basketValidator.validate(userBasketEntity);  //  엔티티가 유효한지 확인
	    
		// 재고 확인
		//resultDTO rDto= itemConfirmService.itmeCheck(userBasketEntity);
		
		//결제 관련 일 수행 
		if(true) {return userBasketEntity;}
		//
		
		//ItemId  체크 
		Optional<UserBasketEntity> optional = basketRepository.findById(userBasketEntity.getId());
		
		if (optional.isPresent()) { 
			 UserBasketEntity entity = optional.get();
			 entity.setCount(entity.getCount() + userBasketEntity.getCount()); 
			 basketRepository.save(entity);
			
			return entity;
		} else {
			UserBasketEntity savedEntity = basketRepository.save(userBasketEntity);

		    log.info("ItemEntity Id : {} is saved.", savedEntity.getId());

		    //return repository.findByUserIdQuery(entity.getUserId());
		    return savedEntity;
		}
			
			
	}

	@Transactional
	public UserBasketEntity update(final UserBasketEntity userBasketEntity) {
		// 엔티티가 유효한지 확인
		basketValidator.validateId(userBasketEntity);

		// 재고 확인
		itemService.itemIdCheck(userBasketEntity);
		
		// (2) 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다. 존재하지 않는 엔티티는 업데이트 할 수 없기 때문이다.
		final Optional<UserBasketEntity> original = basketRepository.findById(userBasketEntity.getId());

		/*
		// 재고 확인 
		Optional<ItemEntity> itemOriginal = itemRepository.findById(userBasketEntity.getItemId());
		if(itemOriginal.isPresent()) {
			final ItemEntity entity = itemOriginal.get(); 
			if(entity.getAmount() < userBasketEntity.getCount() || entity.getAmount() ==0) {
				//아이템이 재고 없음
				log.warn("ItemEntity is empty.");
			    throw new RuntimeException("ItemEntity is empty.");
			}
		}else {
			//아이템이 존재하지 않음
			log.warn("ItemEntity is null.");
		    throw new RuntimeException("ItemEntity is null.");
		}
		*/
		
		/*
		 * if(original.ifPresent()) { final TodoEntity todo = TodoEntity.get(); }
		 */
		
		if(original.isPresent()) { 
			final UserBasketEntity entity = original.get(); 
	      	// (3) 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌운다.
	    	entity.setId(userBasketEntity.getId());
	    	entity.setUserId(userBasketEntity.getUserId());
	    	entity.setItemId(userBasketEntity.getItemId());
	    	entity.setCount(userBasketEntity.getCount());
	    	entity.setUseYn(userBasketEntity.getUseYn());
	      
			//// 실제 삭제하지 않고 삭제표시만 할경우 시간표시
			// item.setDeletedAt(LocalDateTime.now());

			// (4) 데이터베이스에 새 값을 저장한다. id는 같다
	    	basketRepository.save(entity);
	    	return entity;
		}else {
			return null;
		}
		
		/*
	    original.ifPresent(entity -> {
	      // (3) 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌운다.
	    	entity.setId(userBasketEntity.getId());
	    	entity.setUserId(userBasketEntity.getUserId());
	    	entity.setItemId(userBasketEntity.getItemId());
	    	entity.setCount(userBasketEntity.getCount());
	    	entity.setUseYn(userBasketEntity.getUseYn());
	      
			//// 실제 삭제하지 않고 삭제표시만 할경우 시간표시
			// item.setDeletedAt(LocalDateTime.now());

			// (4) 데이터베이스에 새 값을 저장한다. id는 같다
	    	basketRepository.save(entity);
		});
	    */
		
	}

	  public List<UserBasketEntity> delete(final UserBasketEntity userBasketEntity) {
	    // (1) 저장 할 엔티티가 유효한지 확인한다. 이 메서드는 2.3.1 Create Todo에서 구현했다.
		// 엔티티가 유효한지 확인
		  basketValidator.validateId(userBasketEntity);

	    try {
	      // (2) 엔티티를 삭제한다.
	    	basketRepository.delete(userBasketEntity);
	    } catch(Exception e) {
	      // (3) exception 발생시 id와 exception을 로깅한다.
	      log.error("error deleting entity {}", userBasketEntity.getId(), e);

	      // (4) 컨트롤러로 exception을 날린다. 데이터베이스 내부 로직을 캡슐화 하기 위해 e를 리턴하지 않고 새 exception 오브젝트를 리턴한다.
	      throw new RuntimeException("error deleting entity " + userBasketEntity.getId());
	    }
	    // (5) 새 Todo리스트를 가져와 리턴한다.
	    //return retrieve(itemEntity.getUserId());
		//Optional<UserBasketEntity> optional =  basketRepository.findById(userBasketEntity.getId());

		PageRequest pageRequest = PageRequest.of(0, 20);

		return  basketRepository.findByUserId(  userBasketEntity.getUserId(),"createdAt",  "count" , pageRequest);
	    //if(optional.isPresent()) {
	    	//final UserBasketEntity todo =  optional.get();
	    	//return todo;
	    //}
	  }
}
