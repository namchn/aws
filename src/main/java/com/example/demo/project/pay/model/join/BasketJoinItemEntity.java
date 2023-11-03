package com.example.demo.project.pay.model.join;

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
public class BasketJoinItemEntity {
  private String id; // 바구니 아이디.
  private int count; // 물품갯수
  private String basketUseYn; // 방바구니 사용여부 
  private String itemId; // 아이템 아이디.
  private String itemName; //아이템 이름
  private int itemPrice; //아이템 가격
  private String text; // 소비물품의 설명.
  private String itemUseYn; // 소비물품의 사용여부.
  
  private LocalDateTime basketCreatedAt;
  private LocalDateTime basketUpdatedAt;
  private LocalDateTime itemCreatedAt;
  private LocalDateTime itemUpdatedAt;
}
