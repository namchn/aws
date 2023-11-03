package com.example.demo.project.pay.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.project.pay.dto.resultDTO;
import com.example.demo.project.pay.model.table.ItemEntity;
import com.example.demo.project.pay.model.table.UserBasketEntity;
import com.example.demo.project.pay.persistence.BasketRepository;
import com.example.demo.project.pay.persistence.ItemRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConfirmService {
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private BasketRepository basketRepository;
	
	
}
