package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder // Builder클래스를 따로 개발하지 않고->빌드 패턴을 사용 -> 오브젝트 생성 (생성자 이용과 비슷)
@NoArgsConstructor //매개변수 없는 생성자 자동 생성
@AllArgsConstructor //모든 매개변수 받는 생성자 자동 생성 : 
@Data //getter setter 메서드 구현
@Entity
@Table(name = "Todo")
public class TodoEntity {
  @Id
  @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String id; // 이 오브젝트의 아이디
  private String userId; // 이 오브젝트를 생성한 유저의 아이디
  private String title; // Todo 타이틀 예) 운동 하기
  private boolean done; // true - todo를 완료한 경우(checked)
}
