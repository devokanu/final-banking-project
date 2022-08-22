package com.okan.bankingmanagement.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.okan.bankingmanagement.domain.Bank;
import com.okan.bankingmanagement.domain.User;

@Mapper
@Component
public interface BankMapper {
	
	public Bank findById(int id);

	public List<Bank> getBanks();
	int save(Bank bank);
	public Bank findByName(String name);
	
}
