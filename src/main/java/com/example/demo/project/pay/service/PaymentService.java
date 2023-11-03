package com.example.demo.project.pay.service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo._security.TokenProvider;
import com.example.demo.project.pay.dto.ItemDTO;
import com.example.demo.project.pay.dto.resultDTO;
import com.example.demo.project.pay.model.table.ItemEntity;
import com.example.demo.project.pay.model.table.PaymentEntity;
import com.example.demo.project.pay.model.table.UserBasketEntity;
import com.example.demo.project.pay.persistence.BasketRepository;
import com.example.demo.project.pay.persistence.ItemRepository;
import com.example.demo.project.pay.persistence.PaymentRepository;
import com.example.demo.project.user.persistence.UserRepository;

@Slf4j
@Service
public class PaymentService {

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private BasketRepository basketRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@Autowired
	private ItemValidator itemValidator;
	
	@Autowired
	private BasketValidator basketValidator;
	
	//@Autowired
	//private ItemConfirmService itemConfirmService;
	
	//@Autowired
	//private ItemService itemService;
		
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Transactional
	public PaymentEntity update(final PaymentEntity PaymentEntity) {
		return paymentRepository.save(PaymentEntity);
	}
	
}
