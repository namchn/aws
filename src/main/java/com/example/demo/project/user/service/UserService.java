package com.example.demo.project.user.service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.project.user.model.UserEntity;
import com.example.demo.project.user.persistence.UserRepository;

@Slf4j
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public UserEntity create(final UserEntity userEntity) {
		if (userEntity == null || userEntity.getUsername() == null) {
			log.warn("userEntity is null.");
			throw new RuntimeException("Invalid arguments");
		}
		final String username = userEntity.getUsername();
		if (userRepository.existsByUsername(username)) {
			log.warn("Username already exists {}", username);
			throw new RuntimeException("Username already exists");
		}

		return userRepository.save(userEntity);
	}
  
	@Transactional
	public List<UserEntity> retrieveAll() {
		return userRepository.findAll();
	}

	@Transactional
	public UserEntity getByCredentials(final String username, final String password, final PasswordEncoder encoder) {
		final UserEntity originalUser = userRepository.findByUsername(username);
		// return userRepository.findByUsernameAndPassword(username, password);

		// matches 메서드를 이용해 패스워드가 같은지 확인
		if (originalUser != null && encoder.matches(password, originalUser.getPassword())) {
			return originalUser;
		}
		return null;
	}
}
