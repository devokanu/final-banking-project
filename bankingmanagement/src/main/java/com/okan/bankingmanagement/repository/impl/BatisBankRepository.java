package com.okan.bankingmanagement.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.okan.bankingmanagement.domain.Bank;
import com.okan.bankingmanagement.mybatis.BankMapper;
import com.okan.bankingmanagement.repository.BankRepository;

@Repository
public class BatisBankRepository implements BankRepository{
	
	private final BankMapper bankMapper;
	
	@Autowired
	public BatisBankRepository(BankMapper bankMapper) {
		this.bankMapper = bankMapper;
	}

	@Override
	public Bank getBank(int id) {
		Bank bank = bankMapper.findById(id);
		return bank;
	}

	@Override
	public int createBank(Bank bank) {
		int us = bankMapper.save(bank);
		return us;
	}

	@Override
	public List<Bank> getBanks() {
		List<Bank> banks = bankMapper.getBanks();
		return banks;
	}

	@Override
	public Bank getBank(String name) {
		Bank bank = bankMapper.findByName(name);
		return bank;
	}

}
