package com.example.demo.project.pay.dto;


import com.example.demo.project.pay.model.table.ItemEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemDTO {
  private String id;
  private String itemName; // 소비물품.
  private String type; // 소비물품의 형태.
  private String category; // 소비물품의 분류.
  private int amount; // 소비물품의 갯수.
  private int price; // 소비물품의 가격.
  private String text; // 소비물품의 설명.
  private String useYn; // 소비물품의 분류.

  public ItemDTO(final ItemEntity entity) {
    this.id = entity.getId();
    this.itemName = entity.getItemName();
    this.type = entity.getType();
    this.category = entity.getCategory();
    this.amount = entity.getAmount();
    this.price = entity.getPrice();
    this.useYn = entity.getUseYn();
    this.text = entity.getText();
  }
  
  public static ItemEntity toEntity(final ItemDTO dto) {
    return ItemEntity.builder()
        .id(dto.getId())
        .itemName(dto.getItemName())
        .type(dto.getType())
        .category(dto.getCategory())
        .amount(dto.getAmount())
        .price(dto.getPrice())
        .useYn(dto.getUseYn())
        .text(dto.getText())
        .build();
  }
  
}
