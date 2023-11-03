package com.example.demo.project.user.dto;



import com.example.demo.project.user.model.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
  private String token;
  private String username;
  private String password;
  private String tel; // 전화번호 추가
  private String id;
  private String authProvider;
  private String role;
  
  public UserDTO(final UserEntity entity) {
	    this.id = entity.getId();
	    this.username = entity.getUsername();
	    this.tel = entity.getTel();
	    this.authProvider = entity.getAuthProvider();
	    this.role = entity.getRole();
  }
	  
  public static UserEntity toEntity(final UserDTO dto) {
    return UserEntity.builder()
        .id(dto.getId())
        .username(dto.getUsername())
        .tel(dto.getTel())
        .authProvider(dto.getAuthProvider())
        .role(dto.getRole())
        .build();
  }
}
