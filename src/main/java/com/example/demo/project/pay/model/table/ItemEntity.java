package com.example.demo.project.pay.model.table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.frame.model.AbstractBasicEntity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Data
@Entity // JPA에게 해당 클래스는 데이터베이스와 매핑할 객체다를 알려주는 것이다.
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
@Table(name = "item" )
public class ItemEntity  extends AbstractBasicEntity{
	@Id
	@GeneratedValue(generator = "item-uuid")
	@GenericGenerator(name = "item-uuid", strategy = "uuid")
	private String id; // 소비물품에 고유하게 부여되는 id.

	@Column(nullable = false, length = 400)
	private String itemName; // 소비물품.
	private String type; // 소비물품의 형태.
	private String category; // 소비물품의 분류.
	private int amount; // 소비물품의 갯수.
	private int price; // 소비물품의 가격.
	private String text; // 소비물품의 설명.
	private String useYn; // 소비물품의 사용 여부.

	// @CreatedDate
	// private LocalDateTime createdAt;
	// @LastModifiedDate
	// private LocalDateTime updatedAt;
}
