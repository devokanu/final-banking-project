package com.okan.bankingmanagement.service;

import java.util.Date;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.okan.bankingmanagement.domain.Account;
import com.okan.bankingmanagement.domain.AccountType;
import com.okan.bankingmanagement.domain.Bank;
import com.okan.bankingmanagement.domain.User;
import com.okan.bankingmanagement.domain.UserPrincipal;
import com.okan.bankingmanagement.dto.response.AccountDetailResponse;
import com.okan.bankingmanagement.dto.response.BankResponse;
import com.okan.bankingmanagement.exception.DeletedAccountException;
import com.okan.bankingmanagement.exception.InvalidAccountTypeException;
import com.okan.bankingmanagement.exception.UnexpectedErrorException;
import com.okan.bankingmanagement.mybatis.AccountMapper;
import com.okan.bankingmanagement.mybatis.UserMapper;
import com.okan.bankingmanagement.repository.AccountRepository;
import com.okan.bankingmanagement.repository.BankRepository;
import com.okan.bankingmanagement.repository.UserRepository;
import com.okan.bankingmanagement.utility.UtilMapper;

@Service
public class AccountService {

	
	private final AccountRepository accountRepo;
	private final BankRepository bankRepo;
	private final UserRepository userRepo;
	private final ModelMapper mapper;
	private final UtilMapper utilMapper;

	@Autowired
    public AccountService(AccountRepository accountRepo, ModelMapper mapper, BankRepository bankRepo,
    		UserRepository userRepo, UtilMapper utilMapper) {
        this.accountRepo = accountRepo;
        this.bankRepo = bankRepo;
        this.userRepo = userRepo;
        this.mapper = mapper;
        this.utilMapper = utilMapper;
        
    }
	
	public AccountDetailResponse createAccount(int bankId, String type) throws InvalidAccountTypeException {
		
		if(!checkType(type)) {
			String errorMessage = String.format("Invalid account type: " +
                    "Expected TRY, USD or XAU but got %s", type);
			
			throw new InvalidAccountTypeException(errorMessage);
		}
		
		String currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		User user = userRepo.findUserByUsername(currentUser);
		Bank bank = bankRepo.getBank(bankId);
	
		
		Long number = (long) (Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);
		String num = number.toString();
		Account acc = new Account();
		
		acc.setAccount_number(Integer.valueOf(num));
		acc.setBalance(0);
		acc.setCreation_date(new Date(System.currentTimeMillis()));
		acc.setLast_update_date(acc.getCreation_date());
		acc.setIs_deleted(false);
		acc.setUser(user);
		acc.setBank(bank);
		acc.setType(AccountType.valueOf(type));
		
		
		AccountDetailResponse detail = mapper.map(accountRepo.createAccount(acc), AccountDetailResponse.class);
		return detail;
	}
	
	
    public Account getAccount(int id)  {

        Account account = accountRepo.getAccount(id);

        return account;
    }
    
    public AccountDetailResponse getAccountByAccountNumber(int id)  {
        

    	AccountDetailResponse account = mapper.map(accountRepo.getAccountByAccountNumber(id), AccountDetailResponse.class);

        return account;
    }
    
    public boolean deleteAccount(int id) throws DeletedAccountException, AccountNotFoundException, UnexpectedErrorException {
        
    	Account account = accountRepo.getAccountByAccountNumber(id);
    	account.setIs_deleted(true);
    	account.setLast_update_date(new Date(System.currentTimeMillis()));
    	boolean isSuccess = accountRepo.deleteAccount(account);

        return isSuccess;
    }

    
    
    
    public boolean checkType(String accountType) {
		if(accountType.equals("TRY")  || accountType.equals("USD") || accountType.equals("XAU")) {
			return true;
		}
		return false;
	}

	public List<AccountDetailResponse> getAccounts() {
		String currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		User user = userRepo.findUserByUsername(currentUser);
		List<AccountDetailResponse> accounts =utilMapper.detailAccount(accountRepo.getAccounts(user.getId()));
		return accounts;
	}
	
	
	
	
	
	
	
	
}
