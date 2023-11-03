package com.example.demo.project.user.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.example.frame.model.AbstractBasicEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity // JPA에게 해당 클래스는 데이터베이스와 매핑할 객체다를 알려주는 것이다.
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
@Table(name = "User" , uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class UserEntity  extends AbstractBasicEntity{
  @Id
  @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String id; // 유저에게 고유하게 부여되는 id.

  @Column(name = "username", nullable = false, length = 400)
  private String username; // 아이디로 사용할 유저네임. 이메일일 수도 그냥 문자열일 수도 있다.
  private String password; // 패스워드.
  private String tel; // 전화번호.
  private String role; // 유저의 롤.   //일반 사용자 ,어드민
  private String authProvider; // example : facebook,github
  
  //@OneToMany(mappedBy = "user")
  //private List<UsedItemEntity> UsedItems = new ArrayList<>();
  
  //@CreatedDate
  //private LocalDateTime createdAt;
  //@LastModifiedDate
  //private LocalDateTime updatedAt;
}
