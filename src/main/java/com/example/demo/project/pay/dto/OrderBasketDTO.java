package com.example.demo.project.pay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderBasketDTO {
  private String id;
  private String userId; // 
  private String itemId; //
  private int count; // 
  private String basketUseYn; //
  
  private String itemName; //
  private String itemType; // 
  private String itemCategory; //
  private int itemAmount; // 
  private int itemPrice; // 
  private String itemUseYn; // 

}
