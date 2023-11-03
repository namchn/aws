package com.example.demo.project.pay.dto;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemListDTO  {
	private String id; // 소비물품.

	private String itemId; // 소비물품.
	private String name; // 소비물품이름.
	private String type; // 소비물품의 종류.
	private String userId; // 유저의 아이디
	private String count; // 소비물품의 갯수.
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	// @CreatedDate
	// private LocalDateTime createdAt;
	// @LastModifiedDate
	// private LocalDateTime updatedAt;
}
