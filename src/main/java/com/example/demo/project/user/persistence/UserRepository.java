package com.example.demo.project.user.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.project.user.model.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

  List<UserEntity>	findAll();
  UserEntity findByUsername(String username);
  Boolean existsByUsername(String username);
  UserEntity findByUsernameAndPassword(String username, String password);

}


