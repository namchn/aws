package com.example.demo.project.pay.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo._security.TokenProvider;
import com.example.demo.project.pay.dto.resultDTO;
import com.example.demo.project.pay.model.table.ItemEntity;
import com.example.demo.project.pay.model.table.UserBasketEntity;
import com.example.demo.project.pay.persistence.BasketRepository;
import com.example.demo.project.pay.persistence.ItemRepository;
import com.example.demo.project.pay.service.ItemValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
//public class ItemService  implements  ItemServiceInterface {
public class ItemServiceImpl   {	

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private BasketRepository basketRepository;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@Autowired
	private ItemValidator itemValidator;
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	
	/*	
	@Caching(
			cacheable = {
					@Cacheable( value = {"todoList1", "todoList2"}, key = "#userId"),
					@Cacheable("todoList3")
			}
		)
	*/		
	//@CacheEvict(value = "itemList", allEntries = true)  //캐쉬반환할때는 반환값이 같은지 확인 다르다면 캐쉬를 삭제처리하는게 나을듯.
	//@CacheEvict(value = "itemOne", key = "#itemEntity.id")  //전체리스트 삭제 
	//@CachePut(value = "itemList", key = "itemList") //아이디별
	//@Cacheable(value = "itemOne", key = "#id")
	
	
	@Caching(
		cacheable = {
				@Cacheable( value = {"itemOne"}, key = "#id")
				//,@Cacheable("todoList3")
		}
	)
	public Optional<ItemEntity> one(String id) {
		log.info("Entity userId : {} is retrieved.", id);
		return itemRepository.findById(id);
	}
	
	//  "'KeyIs' + #kim?.getName():'Unknown' + #page + #list"
	
	
	@Caching(
		cacheable = {
				@Cacheable( value = {"itemList"}, key = "'itemName' + #itemName+'maxprice' + #maxprice+'lowprice' + #lowprice +'pageInt' + #pageInt +'sizeInt' + #sizeInt")
				//,@Cacheable("todoList3")
		}
	)
	public List<ItemEntity> retrieve(String itemName, int maxprice, int lowprice, String orderby1, String order1Type, String orderby2, String order2Type, int pageInt,int sizeInt, Pageable pageable) {

		log.info("Entity userId : {} is retrieved.", itemName);
		PageRequest pageRequest = PageRequest.of(pageInt, sizeInt);
		//return itemRepository.getJoinItems(itemName, maxprice, lowprice, orderby1, order1Type, orderby2, order2Type, pageable);
		return itemRepository.getItems( itemName,  maxprice, lowprice, orderby1,  orderby2 , pageRequest);
	}
	
	@Caching(
		cacheable = {
				@Cacheable( value = {"itemCount"}, key = "'itemName' + #itemName+'maxprice' + #maxprice+'lowprice' + #lowprice +'pageInt' + #pageInt +'sizeInt' + #sizeInt")
				//,@Cacheable("todoList3")
		}
	)
	public List<Long> retrieveCount(String itemName, int maxprice, int lowprice, String orderby1, String order1Type, String orderby2, String order2Type, int pageInt,int sizeInt, Pageable pageable) {
		PageRequest pageRequest = PageRequest.of(pageInt, sizeInt);
		log.info("Entity userId : {} is retrieved.", itemName);
		//return itemRepository.getJoinItems(itemName, maxprice, lowprice, orderby1, order1Type, orderby2, order2Type, pageable);
		return itemRepository.getItemsCount( itemName,  maxprice, lowprice, orderby1,  orderby2 , pageRequest);
	}
	
	@Caching(
			cacheable = {
					@Cacheable( value = {"itemList"}, key = "'itemName' + #itemName+'maxprice' + #maxprice+'lowprice' + #lowprice +'pageInt' + #pageInt +'sizeInt' + #sizeInt")
					//,@Cacheable("todoList3")
			}
		)
	public List<ItemEntity> findAll(String itemName, int maxprice, int lowprice, String orderby1, String order1Type, String orderby2, String order2Type, int pageInt,int sizeInt, Pageable pageable) {
		PageRequest pageRequest = PageRequest.of(pageInt, sizeInt);
		log.info("Entity userId : {} is retrieved.", itemName);
		//return itemRepository.getJoinItems(itemName, maxprice, lowprice, orderby1, order1Type, orderby2, order2Type, pageable);
		return itemRepository.findAll( orderby1,  orderby2 , pageRequest);
	}
	
	@Caching(
			cacheable = {
					@Cacheable( value = {"itemCount"}, key = "'itemName' + #itemName+'maxprice' + #maxprice+'lowprice' + #lowprice +'pageInt' + #pageInt +'sizeInt' + #sizeInt")
					//,@Cacheable("todoList3")
			}
		)
	public List<Long> findAllCount(String itemName, int maxprice, int lowprice, String orderby1, String order1Type, String orderby2, String order2Type, int pageInt,int sizeInt, Pageable pageable) {
		PageRequest pageRequest = PageRequest.of(pageInt, sizeInt);
		log.info("Entity userId : {} is retrieved.", itemName);
		//return itemRepository.getJoinItems(itemName, maxprice, lowprice, orderby1, order1Type, orderby2, order2Type, pageable);
		return itemRepository.findAllCount( orderby1,  orderby2 , pageRequest);
	}
	
	
	
	
	
	
	/*
	@Caching(
			cacheable = {
					@Cacheable( value = {"todoList1", "todoList2"}, key = "#userId"),
					@Cacheable(value = "todoList3", key = "#userId" )
			}
			put = {
					@CachePut( value = {"itemList"},  allEntries = true ),
					//@CachePut(value = "todoList3", key = "#userId" )
			}
		)
	*/
	
	@Transactional
	@Caching(
			evict = {
					@CacheEvict( value = {"itemList"},  allEntries = true )
					,@CacheEvict(value = {"itemCount"},  allEntries = true  )  //key = "#userId"
			}
		)
	//@CacheEvict(value = "itemList", allEntries = true)  //캐쉬반환할때는 반환값이 같은지 확인 다르다면 캐쉬를 삭제처리하는게 나을듯.
	//@CacheEvict(value = "itemOne", key = "#itemEntity.id")  //전체리스트 삭제 
	//@CachePut(value = "itemOne", key = "itemEntity.id") //아이디별
	public ItemEntity create(final ItemEntity itemEntity) {
		itemValidator.validateName(itemEntity);  //  엔티티가 유효한지 확인
	    
		ItemEntity savedEntity = itemRepository.save(itemEntity);

	    log.info("ItemEntity Id : {} is saved.", savedEntity.getId());

	    //return repository.findByUserIdQuery(entity.getUserId());
	    return savedEntity;
	}

	@Transactional
	//@CacheEvict(value = "itemList", allEntries = true)  //캐쉬반환할때는 반환값이 같은지 확인 다르다면 캐쉬를 삭제처리하는게 나을듯.
	//@CacheEvict(value = "itemList", key = "#itemEntity.id")  //전체리스트 삭제 
	@CachePut(value = "itemOne", key = "itemEntity.id") //아이디별
	public ItemEntity update(final ItemEntity itemEntity) {
		// 엔티티가 유효한지 확인
		itemValidator.validateId(itemEntity);

		// (2) 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다. 존재하지 않는 엔티티는 업데이트 할 수 없기 때문이다.
		final Optional<ItemEntity> original = itemRepository.findById(itemEntity.getId());

		
	    
	    // (3) 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌운다.
		//original.ifPresent(item -> { });
		if(original.isPresent()) { 
			final ItemEntity item = original.get(); 
	    	item.setItemName(itemEntity.getItemName()==null ? item.getItemName() : itemEntity.getItemName() );
	    	item.setType(itemEntity.getType()==null ? item.getType() : itemEntity.getType() );
	    	item.setCategory(itemEntity.getCategory()==null ? item.getCategory() : itemEntity.getCategory() );
	    	item.setAmount( !Objects.nonNull(itemEntity.getAmount()) ? item.getAmount() :  itemEntity.getAmount()  );
	    	item.setPrice( !Objects.nonNull(itemEntity.getPrice()) ? item.getPrice():  itemEntity.getPrice()   ); 	
	    	item.setUseYn(itemEntity.getUseYn()==null ? item.getUseYn() : itemEntity.getUseYn() );
	    	
			//// 실제 삭제하지 않고 삭제표시만 할경우 시간표시
			// item.setDeletedAt(LocalDateTime.now());

			// (4) 데이터베이스에 새 값을 저장한다. id는 같다
	    	ItemEntity savedEntity = itemRepository.save(item);
	    	return savedEntity;
		}else {
			log.warn("itemEntity id  non exist at DB");
			//return null;
		    throw new RuntimeException("itemEntity id  non exist at DB");
		}
	    
	}

	
	@Caching(
		evict = {@CacheEvict(value = {"itemOne"},  key = "#itemEntity.id"  )
				,@CacheEvict( value = {"itemList"},  allEntries = true )
				,@CacheEvict(value = {"itemCount"},  allEntries = true  )  //key = "#userId"
		}
	)
	public List<ItemEntity> delete(final ItemEntity itemEntity) {
	    // (1) 저장 할 엔티티가 유효한지 확인한다. 이 메서드는 2.3.1 Create Todo에서 구현했다.
		// 엔티티가 유효한지 확인
		itemValidator.validateId(itemEntity);

	    try {
	      // (2) 엔티티를 삭제한다.
	    	itemRepository.delete(itemEntity);
	    } catch(Exception e) {
	      // (3) exception 발생시 id와 exception을 로깅한다.
	      log.error("error deleting entity ", itemEntity.getId(), e);

	      // (4) 컨트롤러로 exception을 날린다. 데이터베이스 내부 로직을 캡슐화 하기 위해 e를 리턴하지 않고 새 exception 오브젝트를 리턴한다.
	      throw new RuntimeException("error deleting entity " + itemEntity.getId());
	    }
	    // (5) 새 Todo리스트를 가져와 리턴한다.
	    //return retrieve(itemEntity.getUserId());
	    

		PageRequest pageRequest = PageRequest.of(0, 20);

		return  itemRepository.findAll( "createdAt",  "price" , pageRequest);
		
	  }
	  
	  
	    @Transactional
	    @CacheEvict(value = "itemOne", key = "#userBasketEntity.itemId") 
		public resultDTO<?> itemAmountUpdate(final UserBasketEntity  userBasketEntity) {
			
			resultDTO dto = resultDTO.builder().success(true).error(null).build();
			
			String aws = "true";
			// 재고 확인 
			Optional<ItemEntity> itemOriginal = itemRepository.findByIdandUseY(userBasketEntity.getItemId());
			if(itemOriginal.isPresent()) {
				final ItemEntity entity = itemOriginal.get(); 
				
				if(entity.getAmount() < userBasketEntity.getCount() || entity.getAmount() == 0) {
					//아이템이 재고 없음
					aws = "ItemStorage is exceeded.";
					log.warn(aws);
				    //throw new RuntimeException("ItemEntity is empty.");
					dto = resultDTO.builder().success(false).error(aws).message("A").build();
				}else { //성공 
					entity.setAmount(entity.getAmount() - userBasketEntity.getCount() );
					update(entity);
				}
			}else {
				//아이템이 존재하지 않음
				aws= "Item is null.";
				log.warn(aws);
			    //throw new RuntimeException("ItemEntity is null.");
				dto = resultDTO.builder().success(false).error(aws).message("B").build();
			}
		return dto;
		}
	  
	    @Transactional
	    @CacheEvict(value = "itemOne", key = "#userBasketEntity.itemId") 
		public resultDTO<?> itemAmountRollback(final UserBasketEntity  userBasketEntity, boolean rollback) {
			
			resultDTO dto = resultDTO.builder().success(true).error(null).build();
			
			String aws = "true";
			// 재고 확인 
			Optional<ItemEntity> itemOriginal = itemRepository.findById(userBasketEntity.getItemId());
			if(itemOriginal.isPresent()) {
				final ItemEntity entity = itemOriginal.get(); 
				if(rollback) {
					entity.setAmount(entity.getAmount() + userBasketEntity.getCount() );
					update(entity);
				}
			}else {
				aws= "Item is null."; //아이템이 존재하지 않음
				log.warn(aws);
			    //throw new RuntimeException("ItemEntity is null.");
				dto = resultDTO.builder().success(false).error(aws).message("B").build();
			}
		return dto;
		} 
	  
		@Transactional
		
		public resultDTO<?> itemIdCheck(final UserBasketEntity userBasketEntity) {

			resultDTO dto = resultDTO.builder().success(true).error(null).build();
			
			String aws = "true";
			// 재고 확인 
			Optional<ItemEntity> itemOriginal = itemRepository.findByIdandUseY(userBasketEntity.getItemId());
			if(itemOriginal.isPresent()) {
				final ItemEntity entity = itemOriginal.get(); 
				if(entity.getAmount() < userBasketEntity.getCount() || entity.getAmount() == 0) {
					//아이템이 재고 없음
					aws = "ItemStorage is exceeded.";
					log.warn(aws);
				    //throw new RuntimeException("ItemEntity is empty.");
					dto = resultDTO.builder().success(false).error(aws).message("A").build();
				}
			}else {
				//아이템이 존재하지 않음
				aws= "Item is null.";
				log.warn(aws);
			    //throw new RuntimeException("ItemEntity is null.");
				dto = resultDTO.builder().success(false).error(aws).message("B").build();
			}
		return dto;
		}
	  

		
		////*** ///@Transactional
	    
		public resultDTO<?> basketIdCheck(final UserBasketEntity  userBasketEntity ) {
			
			resultDTO dto = resultDTO.builder().success(true).error(null).build();
			
			String aws = "true";
			// 재고 확인 
			Optional<UserBasketEntity> basketOriginal = basketRepository.findbyIdandUseY(userBasketEntity.getUserId(),userBasketEntity.getId());
			if(basketOriginal.isPresent()) {
				final UserBasketEntity basket = basketOriginal.get(); 
				//basket 하나에 하나의 물품이 들어간다.
				dto = itemIdCheck(basket );
				
//				if(entity.getAmount() < userBasketEntity.getCount() || entity.getAmount() == 0) {
//					//아이템이 재고 없음
//					aws = "ItemStorage is empty.";
//					log.warn(aws);
//				    //throw new RuntimeException("ItemEntity is empty.");
//					dto = resultDTO.builder().success(false).error(aws).message("A").build();
//				}
			}else {
				//아이템이 존재하지 않음
				aws= "Basket is null.";
				log.warn(aws);
			    //throw new RuntimeException("ItemEntity is null.");
				dto = resultDTO.builder().success(false).error(aws).message("C").build();
			}
		return dto;
		}
	  
}
