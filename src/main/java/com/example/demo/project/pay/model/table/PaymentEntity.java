package com.example.demo.project.pay.model.table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.project.pay.model.cacao.Amount;
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
@Table(name = "payment" )
public class PaymentEntity  extends AbstractBasicEntity{
	@Id
	@GeneratedValue(generator = "pay-uuid")
	@GenericGenerator(name = "pay-uuid", strategy = "uuid")
	private String id; // 소비물품에 고유하게 부여되는 id.

	//@Column(nullable = false, length = 400)
	private String aid;
	private String tid;
	private String cid;
	private String sid;
	private String partner_order_id;
	private String partner_user_id;
	private String payment_method_type;
	private String item_name;
	private String item_code;
	private String quantity;
	private String order_created_at;
	private String order_approved_at;
	private String payload;
	private String amount_total;
	private String amount_tax_free;
	private String amount_vat;
	private String amount_point;
	private String amount_discount;

	// @CreatedDate
	// private LocalDateTime createdAt;
	// @LastModifiedDate
	// private LocalDateTime updatedAt;
}
