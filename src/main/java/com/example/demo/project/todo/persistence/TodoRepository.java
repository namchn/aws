package com.example.demo.project.todo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.project.todo.model.TodoEntity;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
  List<TodoEntity> findByUserId(String userId);  //userId 로 찾기

  //@Query("SELECT t FROM Todo t WHERE t.userId = ?1")
  //@Query("SELECT t FROM TodoEntity t WHERE t.userId = ?1 order by createdAt asc, id asc")
  //List<TodoEntity> findByUserIdQuery(String userId);
  
  @Query("SELECT t FROM TodoEntity t WHERE t.userId = :userId order by createdAt asc, id asc")
  List<TodoEntity> findByUserIdQuery(@Param("userId") String userId);
  

}
