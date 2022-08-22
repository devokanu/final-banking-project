package com.okan.bankingmanagement.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.okan.bankingmanagement.domain.Account;

@Mapper
@Component
public interface AccountMapper {

	List<Account> getAccounts(int user_id);
	void save(Account account);
    Account findById(int id);
    Account findByAccountNumber(int accountNumber);
    boolean update(Account account);
    
}
