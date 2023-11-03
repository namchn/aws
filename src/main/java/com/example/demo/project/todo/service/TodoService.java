package com.example.demo.project.todo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.project.todo.model.TodoEntity;
import com.example.demo.project.todo.persistence.TodoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor //필요한 생성자 자동 생성
@Slf4j
public class TodoService {


  @Autowired
  private TodoRepository repository;

  @Autowired
  private TodoValidator todoValidator;
  
  public String testService() {
    // TodoEntity 생성
    TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
    // TodoEntity 저장
    repository.save(entity);
    // TodoEntity 검색
	TodoEntity savedEntity = repository.findById(entity.getId()).get();
	return savedEntity.getTitle();
  }

  
  //@CachePut(value = "todoList", key = "#entity.userId")
  @Caching(
		evict = {
				@CacheEvict( value = {"todoById"},  key = "#entity.userId" )  // allEntries = true
				//@CacheEvict(value = "todoList3", key = "#userId" )
		}
	)
  public List<TodoEntity> create(final TodoEntity entity) {
	
    // Validations
	//validate(entity);
	todoValidator.validate(entity);
    

    repository.save(entity);

    log.info("Entity Id : {} is saved.", entity.getId());

    //return repository.findByUserIdQuery(entity.getUserId());
    return retrieve(entity.getUserId());

  }

  /*
  // 리팩토링 한 메서드
  private void validate(final TodoEntity entity) {
    if(entity == null) {
      log.warn("Entity cannot be null.");
      throw new RuntimeException("Entity cannot be null.");
    }

    if(entity.getUserId() == null) {
      log.warn("Unknown user.");
      throw new RuntimeException("Unknown user.");
    }
  }
  */


  //@Cacheable(value = "todoList", key = "#userId")
  /* */
  @Caching(
		cacheable = {
				@Cacheable( value = {"todoById"}, key = "#userId")
				//,@Cacheable("todoList3")
		}
	)
  public List<TodoEntity> retrieve(final String userId) {
	  
	  log.info("Entity userId : {} is retrieved.", userId);  
	  //return repository.findByUserId(userId);
	  return repository.findByUserIdQuery(userId);
  }

  //@CachePut(value = "todoList", key = "#entity.userId")
  @Caching(
		put = {
				@CachePut( value = {"todoById"},  key = "#entity.userId"  )
				//,@CachePut(value = "todoList3", key = "#userId" )
		}
	)
  public List<TodoEntity> update(final TodoEntity entity) {
    // (1) 저장 할 엔티티가 유효한지 확인한다. 이 메서드는 2.3.1 Create Todo에서 구현했다.


	// Validations
	//validate(entity);  
	todoValidator.validate(entity);

    // (2) 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다. 존재하지 않는 엔티티는 업데이트 할 수 없기 때문이다.
    final Optional<TodoEntity> original = repository.findById(entity.getId());

    /*
    if(original.ifPresent()) {
    	final TodoEntity todo = original.get();
    } */
    original.ifPresent(todo -> {
      // (3) 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌운다.
      todo.setTitle(entity.getTitle());
      todo.setDone(entity.isDone());
      
      //// 실제 삭제하지 않고 삭제표시만 할경우 시간표시
      //todo.setDeletedAt(LocalDateTime.now());
      
      // (4) 데이터베이스에 새 값을 저장한다.  id는 같다
      repository.save(todo);
    });

    // 2.3.2 Retrieve Todo에서 만든 메서드를 이용해 유저의 모든 Todo 리스트를 리턴한다.
    return retrieve(entity.getUserId());
  }


  //@CacheEvict(value = "todoList", allEntries = true)  //캐쉬반환할때는 반환값이 같은지 확인 다르다면 캐쉬를 삭제처리하는게 나을듯.
  //@CachePut(value = "todoList", key = "#entity.userId")
  //@CacheEvict(value = "todoList", key = "#entity.userId")
  @Caching(
		evict = {
				@CacheEvict( value = {"todoById"},  key = "#entity.userId" )  // allEntries = true
				//@CacheEvict(value = "todoList3", key = "#userId" )
		}
	)
  public List<TodoEntity> delete(final TodoEntity entity) {
    // (1) 저장 할 엔티티가 유효한지 확인한다. 이 메서드는 2.3.1 Create Todo에서 구현했다.
    

	// Validations
	//validate(entity);  
	todoValidator.validate(entity);

    try {
      // (2) 엔티티를 삭제한다.
      repository.delete(entity);
    } catch(Exception e) {
      // (3) exception 발생시 id와 exception을 로깅한다.
      log.error("error deleting entity ", entity.getId(), e);

      // (4) 컨트롤러로 exception을 날린다. 데이터베이스 내부 로직을 캡슐화 하기 위해 e를 리턴하지 않고 새 exception 오브젝트를 리턴한다.
      throw new RuntimeException("error deleting entity " + entity.getId());
    }
    // (5) 새 Todo리스트를 가져와 리턴한다.
    return retrieve(entity.getUserId());
  }


}
