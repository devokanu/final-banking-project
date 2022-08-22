package com.okan.bankingmanagement.repository;

import java.util.List;

import com.okan.bankingmanagement.domain.Account;
import com.okan.bankingmanagement.exception.InvalidAccountTypeException;


public interface AccountRepository {

	Account createAccount(Account account) throws InvalidAccountTypeException;

	Account updateAccount(Account account);

	Account getAccount(int id);
	
	Account getAccountByAccountNumber(int id);
	
	boolean deleteAccount(Account account);
	
	List<Account> getAccounts(int userId);

}
