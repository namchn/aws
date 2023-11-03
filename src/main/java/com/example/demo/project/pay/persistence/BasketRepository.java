package com.example.demo.project.pay.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.project.admin.model.JoinEntity;
import com.example.demo.project.pay.dto.ItemListDTO;
import com.example.demo.project.pay.model.join.BasketJoinItemEntity;
import com.example.demo.project.pay.model.table.ItemEntity;
import com.example.demo.project.pay.model.table.UsedItemEntity;
import com.example.demo.project.pay.model.table.UserBasketEntity;





@Repository
public interface BasketRepository extends JpaRepository<UserBasketEntity, String> {

	@Query("SELECT u FROM UserBasketEntity u where userId =:userId order by :orderby1 desc, :orderby2 asc ")
	List<UserBasketEntity> findByUserId(@Param("userId") String userId,
									@Param("orderby1") String orderby1,
									@Param("orderby2") String orderby2,
									Pageable pageable );

	@Query("SELECT COUNT(u) FROM UserBasketEntity u where userId =:userId order by :orderby1 asc, :orderby2 desc ")
	List<Long> findByUserIdCount(@Param("userId") String userId,
							@Param("orderby1") String orderby1,
							@Param("orderby2") String orderby2,
							Pageable pageable );
	
	
	
	@Query("SELECT u FROM UserBasketEntity u where userId =:userId and useYn='Y' order by :orderby1 desc, :orderby2 asc ")
	List<UserBasketEntity> findByUserIdAndUseY(@Param("userId") String userId,
												@Param("orderby1") String orderby1,
												@Param("orderby2") String orderby2,
												Pageable pageable );

	@Query("SELECT COUNT(u) FROM UserBasketEntity u where userId =:userId order by :orderby1 asc, :orderby2 desc ")
	List<Long> findByUserIdAndUseYCount(@Param("userId") String userId,
							@Param("orderby1") String orderby1,
							@Param("orderby2") String orderby2,
							Pageable pageable );
	
	
	  //@Query("SELECT t FROM TodoEntity t  left join  UserEntity u on t.user_id = u.id  WHERE t.userId = ?1")
	  @Query("SELECT new com.example.demo.project.pay.model.join.BasketJoinItemEntity( b.id, b.count, b.useYn, b.itemId, i.itemName, i.price, i.text, i.useYn, b.createdAt, b.updatedAt, i.createdAt, i.updatedAt) "
	  		+ "FROM UserBasketEntity b left outer  join ItemEntity i  on b.itemId = i.id where b.userId =:userId and b.useYn='Y' order by b.updatedAt desc")
	  List<BasketJoinItemEntity> findByUserIdAndUseYAndJoin(@Param("userId") String userId,
															Pageable pageable );
	  
	
	
	
	Optional<UserBasketEntity> findById(String id );
	
	
	@Query("SELECT u FROM UserBasketEntity u where  userId =:userId and itemId =:itemId and useYn ='Y' order by updatedAt  desc ")
	List<UserBasketEntity> findbyItemId(@Param("userId") String userId,
										@Param("itemId") String itemId	);
	
	
	@Query("SELECT u FROM UserBasketEntity u where  userId =:userId and id =:id and useYn ='Y' ")
	Optional<UserBasketEntity> findbyIdandUseY(@Param("userId") String userId,
												@Param("id") String id);
	
}

//
