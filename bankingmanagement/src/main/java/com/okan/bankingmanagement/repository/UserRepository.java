package com.okan.bankingmanagement.repository;

import com.okan.bankingmanagement.domain.User;

public interface UserRepository {
	
	User getUser(int id);
	int save(User user);
	User findUserByUsername(String username);
	User findUserByEmail(String email);
	int updateUser(User user);

}
