package com.okan.bankingmanagement.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.security.auth.login.AccountNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private static final Logger logger = LogManager.getLogger(TransactionController.class);

	@Autowired
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	@PatchMapping(path = "/deposit", produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deposit(@RequestBody DepositCreateRequest request) {
        try {
            AccountDetailResponse account = transactionService.deposit(request.getAccount_number(), request.getAmount());
            
            ZoneId defaultZoneId = ZoneId.systemDefault();
    		Instant instant = account.getLast_update_date().toInstant();
    		ZonedDateTime zonedDateTime = instant.atZone(defaultZoneId);
    		
    		logger.debug("uuu debug sss");
    		logger.info(account.getAccount_number()+ "," + request.getAmount() +  ": deposited");
    		
            return ResponseEntity
                    .ok()
                    .lastModified(zonedDateTime)
                    .body(new DepositResponse(true,"Deposit Successfully", account.getBalance()));
        } catch (InvalidInputException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (AccountNotFoundException | DeletedAccountException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (UnexpectedErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
	
	@PatchMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferCreateRequest request) {
        try {
            AccountDetailResponse account = transactionService.transfer(
                    request.getSenderId(),
                    request.getAmount(),
                    request.getReceiverId());
            
            ZoneId defaultZoneId = ZoneId.systemDefault();
    		Instant instant = account.getLast_update_date().toInstant();
    		ZonedDateTime zonedDateTime = instant.atZone(defaultZoneId);
            
            return ResponseEntity
                    .ok()
                    .lastModified(zonedDateTime)
                    .body(account);
        } catch (InvalidInputException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (InsufficientFundsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (AccountNotFoundException | DeletedAccountException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (UnexpectedErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
	
	
	
	
	

}
