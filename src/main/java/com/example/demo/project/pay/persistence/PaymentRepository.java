package com.example.demo.project.pay.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.project.pay.model.table.PaymentEntity;





@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {

}

