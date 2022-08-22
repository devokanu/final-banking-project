package com.okan.bankingmanagement.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.okan.bankingmanagement.domain.User;
import com.okan.bankingmanagement.mybatis.UserMapper;
import com.okan.bankingmanagement.repository.UserRepository;

@Repository
public class BatisUserRepository implements UserRepository {

	private final UserMapper mapper;
	
	
	@Autowired
	public BatisUserRepository(UserMapper mapper) {
		this.mapper = mapper;
	}



	@Override
	public User getUser(int id) {
		User us = mapper.findById(id);
		return us;
	}



	@Override
	public int save(User user) {
		int us = mapper.save(user);
		return us;
	}



	@Override
	public User findUserByUsername(String username) {
		
		User us = mapper.getUser(new User(0,username,null));
		return us;
	}



	@Override
	public User findUserByEmail(String email) {
		User us = mapper.getUser(new User(0,null,email));
		return us;
	}



	@Override
	public int updateUser(User user) {
		int us = mapper.update(user);
		return us;
	}



	
}
