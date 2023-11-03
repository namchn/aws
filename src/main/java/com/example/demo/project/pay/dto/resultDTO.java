package com.example.demo.project.pay.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
public class resultDTO<T> {
	private String error;
	private List<T> data;
	private boolean success;
	 private String message;
}
