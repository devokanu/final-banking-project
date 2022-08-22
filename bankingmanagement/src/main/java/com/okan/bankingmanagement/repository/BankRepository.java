package com.okan.bankingmanagement.repository;

import java.util.List;

import com.okan.bankingmanagement.domain.Bank;

public interface BankRepository {

	Bank getBank(int id);
	int createBank(Bank bank);
	List<Bank> getBanks();
	Bank getBank(String name);
	
}
