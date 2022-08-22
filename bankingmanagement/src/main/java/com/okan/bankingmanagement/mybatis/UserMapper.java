package com.okan.bankingmanagement.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.okan.bankingmanagement.domain.User;

@Mapper
@Component
public interface UserMapper {
	
	User findById(int id);
	int save(User user);
	User getUser(User keys);
	User findByName(String name);
	int update(User user);
	//User findUserByEmail(String email);

}
