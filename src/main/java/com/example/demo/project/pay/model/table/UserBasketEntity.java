package com.example.demo.project.pay.model.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name = "user_basket" )
public class UserBasketEntity  extends AbstractBasicEntity{
	@Id
	@GeneratedValue(generator = "basket-uuid")
	@GenericGenerator(name = "basket-uuid", strategy = "uuid")
	private String id; // 소비물품에 고유하게 부여되는 id.

	@Column(nullable = false, length = 400)
	//@ManyToOne(fetch = FetchType.LAZY) // 1
	//@JoinColumn(name = "userId") // 2
	private String userId; // 유저의 아이디
	
	//@ManyToOne(fetch = FetchType.LAZY) // 1
	//@JoinColumn(name = "userId") // 2
	private String itemId; // 소비물품.
	private int count; // 소비물품의 갯수.
	private String useYn; // 소비물품의 사용.

	// @CreatedDate
	// private LocalDateTime createdAt;
	// @LastModifiedDate
	// private LocalDateTime updatedAt;
}
