package com.okan.bankingmanagement.service;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.okan.bankingmanagement.domain.Account;
import com.okan.bankingmanagement.domain.AccountType;
import com.okan.bankingmanagement.domain.Bank;
import com.okan.bankingmanagement.domain.User;
import com.okan.bankingmanagement.dto.response.AccountDetailResponse;
import com.okan.bankingmanagement.dto.response.BankResponse;
import com.okan.bankingmanagement.exception.BankExistException;
import com.okan.bankingmanagement.exception.InvalidAccountTypeException;
import com.okan.bankingmanagement.exception.UnexpectedErrorException;
import com.okan.bankingmanagement.repository.BankRepository;

@Service
public class BankService {
	
	private final BankRepository bankRepo;
	private final ModelMapper mapper;
	
	public BankService(BankRepository bankRepo, ModelMapper mapper) {
		
		this.bankRepo = bankRepo;
		this.mapper = mapper;
	}
	
	public BankResponse getBank(int id) {
		BankResponse bank = mapper.map(bankRepo.getBank(id),BankResponse.class);
		return bank;
	}
	
	public List<BankResponse> getBanks(){
		List<BankResponse> banks = mapper.map(bankRepo.getBanks(), 
				new TypeToken<List<BankResponse>>() {}.getType() );
		return banks;
		
	}
	
	public BankResponse createBank(String name) throws BankExistException, UnexpectedErrorException  {
		
		Bank bank = bankRepo.getBank(name);
		if(bank != null) {
			String errorMessage = String.format("Given name Already Used: " +
                     name);
			throw new BankExistException(errorMessage);
		}
		
		Bank bankTemp = new Bank();
		bankTemp.setName(name);
		bankRepo.createBank(bankTemp);
		
		BankResponse detail = mapper.map(bankTemp, BankResponse.class);
		return detail;
	}
	
	

}
