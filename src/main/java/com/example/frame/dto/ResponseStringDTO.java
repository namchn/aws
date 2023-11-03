package com.example.frame.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseStringDTO<T> {
  private String error;
  private String data;
  private Long count;
  private Long totalcount;
  //private boolean success;
  private String message;
}
