package com.example.demo.project.pay.dto;


import com.example.demo.project.pay.model.table.UserBasketEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BasketDTO {
  private String id;
  private String userId; // 사용자고유.
  private String itemId; // 소비물품의 형태.
  private int count; // 소비물품의 가격.
  private String useYn; // 소비물품의 분류.

  public BasketDTO(final UserBasketEntity entity) {
    this.id = entity.getId();
    this.userId = entity.getUserId();
    this.itemId = entity.getItemId();
    this.count = entity.getCount();
    this.useYn = entity.getUseYn();
  }
  
  public static UserBasketEntity toEntity(final BasketDTO dto) {
    return UserBasketEntity.builder()
        .id(dto.getId())
        .userId(dto.getUserId())
        .itemId(dto.getItemId())
        .count(dto.getCount())
        .useYn(dto.getUseYn())
        .build();
  }
  
}
