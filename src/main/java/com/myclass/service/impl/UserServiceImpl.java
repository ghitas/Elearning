package com.myclass.service.impl;

import java.util.List;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myclass.dto.ChangePassword;
import com.myclass.entity.User;
import com.myclass.repository.UserRepository;
import com.myclass.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findById(String id) {
		return userRepository.findById(id);
	}

	public boolean insert(User entity) {
		try {
			String id = UUID.randomUUID().toString();
			entity.setId(id);
			String hashed = BCrypt.hashpw(entity.getPassword(), BCrypt.gensalt(12));
			entity.setPassword(hashed);
			userRepository.save(entity);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean update(User entity) {
		try {
			userRepository.save(entity);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void delete(String id) {
		userRepository.removeById(id);
	}

	public boolean updatePassword(ChangePassword entity) {
		try {
			User user = userRepository.findByEmail(entity.getEmail());
			user.setPassword(entity.getPassword());
			userRepository.save(user);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
