package com.okan.bankingmanagement.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okan.bankingmanagement.dto.request.DepositCreateRequest;
import com.okan.bankingmanagement.dto.request.TransferCreateRequest;
import com.okan.bankingmanagement.dto.response.AccountDetailResponse;
import com.okan.bankingmanagement.dto.response.DepositResponse;
import com.okan.bankingmanagement.dto.response.ErrorResponse;
import com.okan.bankingmanagement.exception.AccountAuthorizationException;
import com.okan.bankingmanagement.exception.AccountNotFoundException;
import com.okan.bankingmanagement.exception.DeletedAccountException;
import com.okan.bankingmanagement.exception.ExceptionHandling;
import com.okan.bankingmanagement.exception.InsufficientFundsException;
import com.okan.bankingmanagement.exception.InvalidInputException;
import com.okan.bankingmanagement.exception.UnexpectedErrorException;
import com.okan.bankingmanagement.service.TransactionService;

@RestController
@RequestMapping("v1/transaction")
public class TransactionController extends ExceptionHandling {
	
	private final TransactionService transactionService;
	private static final Logger logger = Logger.getLogger(TransactionController.class);

	@Autowired
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	@PatchMapping(path = "/deposit/{accountNumber}", produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deposit(@RequestBody DepositCreateRequest request, @PathVariable int accountNumber) throws AccountAuthorizationException, AccountNotFoundException, DeletedAccountException, InvalidInputException {
       
            AccountDetailResponse account = transactionService.deposit(accountNumber, request.getAmount());
            
            ZoneId defaultZoneId = ZoneId.systemDefault();
    		Instant instant = account.getLast_update_date().toInstant();
    		ZonedDateTime zonedDateTime = instant.atZone(defaultZoneId);
    		
    		
    		logger.info(account.getAccount_number()+ "," + request.getAmount() +  ": deposited");
    		
            return ResponseEntity
                    .ok()
                    .lastModified(zonedDateTime)
                    .body(new DepositResponse(true,"Deposit Successfully", account.getBalance()));
        
    }
	
	@PatchMapping("/transfer/{senderAccountId}")
    public ResponseEntity<?> transfer(@RequestBody TransferCreateRequest request, @PathVariable int senderAccountId) throws AccountNotFoundException, DeletedAccountException, InvalidInputException,
    InsufficientFundsException, UnexpectedErrorException, AccountAuthorizationException {
       
            AccountDetailResponse account = transactionService.transfer(
            		senderAccountId,
                    request.getAmount(),
                    request.getReceiverAccountId());
            
            ZoneId defaultZoneId = ZoneId.systemDefault();
    		Instant instant = account.getLast_update_date().toInstant();
    		ZonedDateTime zonedDateTime = instant.atZone(defaultZoneId);
            
            return ResponseEntity
                    .ok()
                    .lastModified(zonedDateTime)
                    .body(account);
        
            
    }
	
	
	
	
	

}
