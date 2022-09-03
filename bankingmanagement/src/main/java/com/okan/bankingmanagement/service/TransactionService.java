package com.okan.bankingmanagement.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.okan.bankingmanagement.domain.Account;
import com.okan.bankingmanagement.domain.AccountType;
import com.okan.bankingmanagement.domain.User;
import com.okan.bankingmanagement.dto.response.AccountDetailResponse;
import com.okan.bankingmanagement.exception.AccountAuthorizationException;
import com.okan.bankingmanagement.exception.AccountNotFoundException;
import com.okan.bankingmanagement.exception.DeletedAccountException;
import com.okan.bankingmanagement.exception.InsufficientFundsException;
import com.okan.bankingmanagement.exception.InvalidInputException;
import com.okan.bankingmanagement.exception.UnexpectedErrorException;
import com.okan.bankingmanagement.repository.AccountRepository;
import com.okan.bankingmanagement.repository.UserRepository;
import com.okan.bankingmanagement.utility.ExchangeAPIService;

@Service
public class TransactionService {
	private final AccountRepository accountRepo;
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ExchangeAPIService exchange;
	private final ModelMapper mapper;
	private final UserRepository userRepo;
	private static final Logger logger = Logger.getLogger(TransactionService.class);
	
	
	@Autowired
	public TransactionService(AccountRepository AccountRepo, KafkaTemplate<String, String> kafkaTemplate,
			ExchangeAPIService exchange, ModelMapper mapper, UserRepository userRepo) {
		this.accountRepo = AccountRepo;
		this.kafkaTemplate = kafkaTemplate;
		this.exchange = exchange;
		this.mapper = mapper;
		this.userRepo = userRepo;
	}
	
	private double USD_TRY;
    private double XAU_TRY;

	public AccountDetailResponse deposit(int accountNumber, double amount)
            throws AccountNotFoundException, DeletedAccountException, AccountAuthorizationException, InvalidInputException {
		
		String currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    	User user = userRepo.findUserByUsername(currentUser);
    	Account acc = accountRepo.getAccountByAccountNumber(accountNumber);
    	
    	if(acc == null) {
    		throw new AccountNotFoundException("Account not found: " + accountNumber);
    	}
    	if(user.getId() != acc.getUser().getId()) {
    		throw new AccountAuthorizationException();
    	}
    	if(acc.isIs_deleted()) {
    		throw new DeletedAccountException("This account has been deleted before: " + accountNumber);
    	}
    	
		Account accTemp = new Account();

        if(amount <= 0) {
            String errorMessage = String.format("Deposit amount is not valid: %s %s",
                    amount, acc.getType());

            throw new InvalidInputException(errorMessage);
        }
        
        accTemp.setAccount_number(accountNumber);
        accTemp.setBalance(acc.getBalance() + amount);
        accTemp.setLast_update_date(new Date(System.currentTimeMillis()));

        accountRepo.updateAccount(accTemp);

        String log = acc.getId() + " , " + amount + " : " + "deposited";
        kafkaTemplate.send("logs", log);
        

        AccountDetailResponse accountResponse = mapper.map(accountRepo.getAccountByAccountNumber(accountNumber), AccountDetailResponse.class) ;
        
        return accountResponse;
    }
	
	@Transactional
    public AccountDetailResponse transfer(int senderId, double amount, int receiverId)
            throws AccountNotFoundException, DeletedAccountException, InvalidInputException,
            InsufficientFundsException, UnexpectedErrorException, AccountAuthorizationException {
		Account sender = accountRepo.getAccountByAccountNumber(senderId);
		Account receiver = accountRepo.getAccountByAccountNumber(receiverId);
		
		String currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    	User user = userRepo.findUserByUsername(currentUser);
    	
		
		if(sender == null) {
    		throw new AccountNotFoundException("Account not found: " + senderId);
    	}
		if(receiver == null) {
    		throw new AccountNotFoundException("Account not found: " + receiverId);
    	}
    	if(user.getId() != sender.getUser().getId()) {
    		throw new AccountAuthorizationException();
    	}
    	if(sender.isIs_deleted()) {
    		throw new DeletedAccountException("This account has been deleted before: " + senderId);
    	}
    	if(receiver.isIs_deleted()) {
    		throw new DeletedAccountException("This account has been deleted before: " + receiverId);
    	}
		
		AccountType base = sender.getType();
		AccountType to = receiver.getType();

        if(amount <= 0) {
            String errorMessage = String.format("Transfer amount is not valid: %s %s",
                    amount, base);

            throw new InvalidInputException(errorMessage);
        }

        if(sender.getBalance() < amount) {
            String errorMessage = String.format("Insufficient Balance, Sender account does not have enough funds: %s %s",
            		sender.getBalance(), base);

            throw new InsufficientFundsException(errorMessage);
        }

        double exchangeRate = 1;
        if(!base.equals(to)) {
            
            updateExchangeRates();

            if(base.equals(AccountType.USD)) exchangeRate *= USD_TRY;
            if(base.equals(AccountType.XAU)) exchangeRate *= XAU_TRY;

            if(to.equals(AccountType.USD)) exchangeRate /= USD_TRY;
            if(to.equals(AccountType.XAU)) exchangeRate /= XAU_TRY;
        }

        Account tempSender = new Account();
        Account tempReceiver = new Account();
        Date updateDate = new Date(System.currentTimeMillis());
        
        tempSender.setAccount_number(senderId);
        tempSender.setBalance((sender.getBalance() - amount));
        tempSender.setLast_update_date(updateDate);
        
        tempReceiver.setAccount_number(receiverId);
        tempReceiver.setBalance((receiver.getBalance() + (amount * exchangeRate)));
        tempReceiver.setLast_update_date(updateDate);
        
        if(!base.equals(to)) {
        if(base.equals(AccountType.TRY)) tempSender.setBalance(tempSender.getBalance() - 3);
        if(base.equals(AccountType.USD)) tempSender.setBalance(tempSender.getBalance() - 1);
        }
      
        accountRepo.updateAccount(tempSender);
        accountRepo.updateAccount(tempReceiver);

        String log = amount + " , " + senderId + " to " + receiverId + " : transferred";
        kafkaTemplate.send("logs", log);

        AccountDetailResponse accountResponse = mapper.map(accountRepo.getAccountByAccountNumber(senderId), AccountDetailResponse.class) ;
        return accountResponse;
    }

    private void updateExchangeRates() throws UnexpectedErrorException {
        try {
            USD_TRY = exchange.getUsdTryExchangeRate();
        } catch (Exception e) {
            String errorMessage = String.format("Failed to get TRY/USD exchange data: %s", e);

            throw new UnexpectedErrorException(errorMessage);
        }

        try {
            XAU_TRY = exchange.getXauTryExchangeRate();
        } catch (Exception e) {
            String errorMessage = String.format("Failed to get TRY/XAU exchange data: %s", e);

            throw new UnexpectedErrorException(errorMessage);
        }
    }
	
	
	
	
	
	
}
