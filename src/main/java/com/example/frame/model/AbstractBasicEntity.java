package com.example.frame.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Getter;

@EntityListeners(AuditingEntityListener.class) //엔티티를 DB에 적용하기 전, 이후에 커스텀 콜백을 요청할 수 있는 어노테이션이다.
@MappedSuperclass  // 부모 클래스(엔티티)에 필드를 선언하고 단순히 속성만 받아서 사용하고싶을 때 사용하는 방법
@Getter // 
public abstract class AbstractBasicEntity {
	
  
  @CreatedDate
  @Column(name = "created_at", updatable = false)  // 개발자에 의해 수정되면 안되기 때문에
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime createdAt;
  
  @LastModifiedDate
  //@Column(updatable = false)  // 개발자에 의해 수정되면 안되기 때문에
  @Column(name = "updated_at")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime updatedAt;
  
  //@Column(updatable = false)  // 개발자에 의해 수정되면 안되기 때문에
  @Column(name = "deleted_at")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime deletedAt;
}

