package com.example.demo.project.pay.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.project.pay.dto.ItemListDTO;
import com.example.demo.project.pay.model.table.ItemEntity;
import com.example.demo.project.pay.model.table.UsedItemEntity;





@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, String> {

  //@Query("SELECT t FROM TodoEntity t  left join  UserEntity u on t.user_id = u.id  WHERE t.userId = ?1")
  //@Query("SELECT new com.example.demo.project.pay.dto.ItemListDTO( u.id, u.itemId,  i.name, i.type,  u.userId, u.count, u.createdAt, u.updatedAt ) "
  //		+ "FROM UsedItemEntity u  left outer  join ItemEntity i  on  i.id = u.itemId ")
  //List<ItemListDTO> getItemList(); //String userId
  
  //@Query("SELECT  u FROM UserEntity u order by username asc ")
  //List<UsedItemEntity> findByUsernameGreaterThan();	

	@Query("SELECT i FROM ItemEntity i "
			+ "where itemName Like %:itemName% "
			//+ "and price < 10000"
			+ "and price BETWEEN :minprice and :maxprice "
			+ "order by :orderby1 , :orderby2  ")
	List<ItemEntity> getItems(	//@Param("id") String id,
									@Param("itemName") String itemName,
									//@Param("orderby1") String type,
									//@Param("orderby1") String category,
									//@Param("orderby1") int amount,
									@Param("maxprice") int maxprice,
									@Param("minprice") int minprice,
									@Param("orderby1") String orderby1,
									//@Param("order1Type") String order1Type,
									@Param("orderby2") String orderby2,
									//@Param("order2Type") String order2Type,
									Pageable pageable );
	
	
	@Query("SELECT COUNT(i) FROM ItemEntity i "
			+ "where itemName Like %:itemName% "
			//+ "and price < 10000"
			+ "and price BETWEEN :minprice and :maxprice "
			+ "order by :orderby1 , :orderby2  ")
	List<Long> getItemsCount(	//@Param("id") String id,
									@Param("itemName") String itemName,
									//@Param("orderby1") String type,
									//@Param("orderby1") String category,
									//@Param("orderby1") int amount,
									@Param("maxprice") int maxprice,
									@Param("minprice") int minprice,
									@Param("orderby1") String orderby1,
									//@Param("order1Type") String order1Type,
									@Param("orderby2") String orderby2,
									//@Param("order2Type") String order2Type,
									Pageable pageable );
	
	
	@Query("SELECT i FROM ItemEntity i order by :orderby1 asc, :orderby2 desc ")
	List<ItemEntity> findAll(@Param("orderby1") String orderby1,
								@Param("orderby2") String orderby2,
								Pageable pageable );
	@Query("SELECT COUNT(i) FROM ItemEntity i order by :orderby1 asc, :orderby2 desc ")
	List<Long> findAllCount(@Param("orderby1") String orderby1,
								@Param("orderby2") String orderby2,
								Pageable pageable );
	
	
	Optional<ItemEntity> findById(String id);
	

	@Query("SELECT i FROM ItemEntity i where id =:id and useYn='Y' ")
	Optional<ItemEntity> findByIdandUseY(@Param("id") String id);
	
}

//
