package com.example.demo.project.admin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinEntity {
  private String id; // 유저에게 고유하게 부여되는 id.
  private String username; // 아이디로 사용할 유저네임. 이메일일 수도 그냥 문자열일 수도 있다.
  private String userId; // 패스워드.
  private String title; // 전화번호.
  private String authProvider; // example : facebook,github@CreatedDate
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime userCreatedAt;
  private LocalDateTime userUpdatedAt;
}
