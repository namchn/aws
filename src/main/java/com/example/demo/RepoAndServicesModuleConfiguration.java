/**
 * 
 */
package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo._utility.RegexValidator;
import com.example.demo.project.admin.persistence.JoinUserRepository;
import com.example.demo.project.pay.model.table.ItemEntity;
import com.example.demo.project.pay.persistence.ItemRepository;
import com.example.demo.project.pay.service.BasketService;
import com.example.demo.project.pay.service.impl.ItemServiceImpl;
import com.example.demo.project.todo.persistence.TodoRepository;
import com.example.demo.project.user.persistence.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 모든 서비스 등록.
 */

//@Data
@Getter
@AllArgsConstructor
@Service
public class  RepoAndServicesModuleConfiguration {

	@Autowired
	private ItemServiceImpl itemService;
	
	@Autowired
	private BasketService basketService;
	
	@Autowired
	private RegexValidator regexValidator;
	
	@Autowired
	private ItemRepository itemRepository;
	
	 /* admin  관련  */
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TodoRepository repository;
	
	@Autowired
	private JoinUserRepository joinUserRepository;
	
	
	
	
	
	
	
	/**
	 * 
	 */
	public RepoAndServicesModuleConfiguration() {
	}

}
