package com.example.demo.project.admin.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.project.admin.model.JoinEntity;
import com.example.demo.project.user.model.UserEntity;




@Repository
public interface JoinUserRepository extends JpaRepository<UserEntity, String> {

  //@Query("SELECT t FROM TodoEntity t  left join  UserEntity u on t.user_id = u.id  WHERE t.userId = ?1")
  @Query("SELECT new com.example.demo.project.admin.model.JoinEntity( t.id, u.username, t.userId, t.title, u.authProvider, t.createdAt, t.updatedAt , u.createdAt, u.updatedAt) "
  		+ "FROM TodoEntity t left outer  join UserEntity u   on t.userId = u.id order by u.username asc")
  List<JoinEntity> getJoinUser(Pageable pageable); //String userId
  
  
  @Query("SELECT   COUNT(*) "
	  		+ "FROM TodoEntity t left outer  join UserEntity u   on t.userId = u.id order by u.username asc")
	  int getJoinUserCount(); //String userId
  
  @Query("SELECT  u FROM UserEntity u order by username asc ")
  List<UserEntity> findByUsernameGreaterThan( Pageable pageable);	
  
  @Query("SELECT  u FROM UserEntity u order by username asc ")
  Page<UserEntity> findByUsername( Pageable pageable);


  @Query("SELECT new com.example.demo.project.admin.model.JoinEntity( t.id, u.username, t.userId, t.title, u.authProvider, t.createdAt, t.updatedAt , u.createdAt, u.updatedAt) "
	  	+ "FROM UserEntity u left outer  join  TodoEntity t on  u.id = t.userId ")
  List<JoinEntity> getJoinUsers(Pageable pageable);

}

//
