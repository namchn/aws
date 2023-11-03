package com.example.demo.project.ip.block.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import com.example.frame.model.AbstractBasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ip_list")
public class IpListEntity extends AbstractBasicEntity {
  @Id
  @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String id; // 유저에게 고유하게 부여되는 id.

  @Column(nullable = false)
  private String ip; // 아이디로 사용할 유저네임. 이메일일 수도 그냥 문자열일 수도 있다.
  private String type; // 유저의 롤.   //일반 사용자 ,어드민
}
