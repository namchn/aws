/**
 * 
 */
package com.example.demo.project.ip.block.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 */
public interface IPRepository extends JpaRepository<IpListEntity, String> {
	// mapper를 호출하는 방식
	// 회원가입

	List<IpListEntity> findAll();
	Optional< IpListEntity> findByIp(String ip);

	// mapper를 호출하지 않고 여기서 쿼리까지 수행하는 방식
	// 아래같이 코드를 작성하면 mapper에 내용을 추가하지 않아도 됨
	// 사용하고자 하는 쿼리 종류(insert, select 등)에 맞춰 어노테이션을 붙여야함
	//@Insert("insert into member_table(member_email, member_password, member_name) values(#{member_email}, #{member_password}, #{member_name})")
	//void save(MemberMapperDTO memberMapperDTO);
	
}
