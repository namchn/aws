package com.example.demo.project.pay.service;

/**
 * 
 */

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.example.demo.project.pay.dto.resultDTO;
import com.example.demo.project.pay.model.table.ItemEntity;
import com.example.demo.project.pay.model.table.UserBasketEntity;

/**
 * 
 */
public interface ItemServiceInterface {
	
	Optional<ItemEntity> one(String id);

	List<ItemEntity> retrieve(String itemName, int maxprice, int lowprice, String orderby1, String order1Type,
			String orderby2, String order2Type, Pageable pageable);

	List<Long> retrieveCount(String itemName, int maxprice, int lowprice, String orderby1, String order1Type,
			String orderby2, String order2Type, Pageable pageable);

	List<ItemEntity> findAll(String itemName, int maxprice, int lowprice, String orderby1, String order1Type,
			String orderby2, String order2Type, Pageable pageable);

	List<Long> findAllCount(String itemName, int maxprice, int lowprice, String orderby1, String order1Type,
			String orderby2, String order2Type, Pageable pageable);

	ItemEntity create(final ItemEntity itemEntity);

	ItemEntity update(final ItemEntity itemEntity);

	List<ItemEntity> delete(final ItemEntity itemEntity);

	resultDTO<?> itemAmountUpdate(final UserBasketEntity userBasketEntity);

	resultDTO<?> itemAmountRollback(final UserBasketEntity userBasketEntity, boolean rollback);

	resultDTO<?> itemIdCheck(final UserBasketEntity userBasketEntity);

	resultDTO<?> basketIdCheck(final UserBasketEntity userBasketEntity);
			
}
