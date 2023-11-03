package com.example.demo.project.mybatis.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Data
//@Entity
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "User" , uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class UserMEntity {
  @Id
  @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String id; // 유저에게 고유하게 부여되는 id.

  @Column(nullable = false)
  private String username; // 아이디로 사용할 유저네임. 이메일일 수도 그냥 문자열일 수도 있다.
  private String password; // 패스워드.
  private String tel; // 전화번호.
  private String role; // 유저의 롤.   //일반 사용자 ,어드민
  private String auth_provider; // example : facebook,github
  private LocalDateTime updatedAt;
}
