package com.okan.bankingmanagement.service;

import java.util.Date;

import javax.security.auth.login.AccountNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.okan.bankingmanagement.domain.Account;
import com.okan.bankingmanagement.domain.AccountType;
import com.okan.bankingmanagement.dto.response.AccountDetailResponse;
import com.okan.bankingmanagement.exception.DeletedAccountException;
import com.okan.bankingmanagement.exception.InsufficientFundsException;
import com.okan.bankingmanagement.exception.InvalidInputException;
import com.okan.bankingmanagement.exception.UnexpectedErrorException;
import com.okan.bankingmanagement.repository.AccountRepository;
import com.okan.bankingmanagement.utility.ExchangeAPIService;

@Service
public class TransactionService {
	private final AccountRepository accountRepo;
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ExchangeAPIService exchange;
	private final ModelMapper mapper;
	
	
	@Autowired
	public TransactionService(AccountRepository AccountRepo, KafkaTemplate<String, String> kafkaTemplate,
			ExchangeAPIService exchange, ModelMapper mapper) {
		this.accountRepo = AccountRepo;
		this.kafkaTemplate = kafkaTemplate;
		this.exchange = exchange;
		this.mapper = mapper;
	}
	
	private double USD_TRY;
    private double XAU_TRY;

	public AccountDetailResponse deposit(int accountNumber, double amount)
            throws AccountNotFoundException, DeletedAccountException, InvalidInputException, UnexpectedErrorException {
		
		Account account = accountRepo.getAccountByAccountNumber(accountNumber);
		Account accTemp = new Account();

        if(amount <= 0) {
            String errorMessage = String.format("Deposit amount is not valid: %s %s",
                    amount, account.getType());

            throw new InvalidInputException(errorMessage);
        }
        
        accTemp.setAccount_number(accountNumber);
        accTemp.setBalance(account.getBalance() + amount);
        accTemp.setLast_update_date(new Date(System.currentTimeMillis()));

        accountRepo.updateAccount(accTemp);

        String log = account.getId() + " , " + amount + " : " + "deposited";
        kafkaTemplate.send("logs", log);
        //kafkaTemplate.send("logs", deposit.toString());

        AccountDetailResponse accountResponse = mapper.map(accountRepo.getAccountByAccountNumber(accountNumber), AccountDetailResponse.class) ;
        
        // Return updated account info
        return accountResponse;
    }
	
	@Transactional
    public AccountDetailResponse transfer(int senderId, double amount, int receiverId)
            throws AccountNotFoundException, DeletedAccountException, InvalidInputException,
            InsufficientFundsException, UnexpectedErrorException {
		Account sender = accountRepo.getAccountByAccountNumber(senderId);
		Account receiver = accountRepo.getAccountByAccountNumber(receiverId);
		
		AccountType base = sender.getType();
		AccountType to = receiver.getType();

        // Check if amount is valid
        if(amount <= 0) {
            String errorMessage = String.format("Transfer amount is not valid: %s %s",
                    amount, base);

            throw new InvalidInputException(errorMessage);
        }

        if(sender.getBalance() < amount) {
            String errorMessage = String.format("Sender account does not have enough funds: %s %s",
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
        tempSender.setBalance(sender.getBalance() - amount);
        tempSender.setLast_update_date(updateDate);
        
        tempReceiver.setAccount_number(receiverId);
        tempReceiver.setBalance(receiver.getBalance() + (amount * exchangeRate));
        tempReceiver.setLast_update_date(updateDate);
        
        if(!base.equals(to)) {
        if(base.equals(AccountType.TRY)) tempSender.setBalance(sender.getBalance() - 3);
        if(base.equals(AccountType.USD)) tempSender.setBalance(sender.getBalance() - 1);
        }
      
        accountRepo.updateAccount(tempSender);
        accountRepo.updateAccount(tempReceiver);

        //kafkaTemplate.send("logs", transfer.toString());
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
