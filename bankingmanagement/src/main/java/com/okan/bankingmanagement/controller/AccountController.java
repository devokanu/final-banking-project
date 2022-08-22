package com.okan.bankingmanagement.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.okan.bankingmanagement.domain.Account;
import com.okan.bankingmanagement.domain.AccountType;
import com.okan.bankingmanagement.domain.Bank;
import com.okan.bankingmanagement.domain.User;
import com.okan.bankingmanagement.domain.UserPrincipal;
import com.okan.bankingmanagement.dto.request.AccountCreateRequest;
import com.okan.bankingmanagement.dto.response.AccountDetailResponse;
import com.okan.bankingmanagement.dto.response.BankResponse;
import com.okan.bankingmanagement.dto.response.ErrorResponse;
import com.okan.bankingmanagement.exception.DeletedAccountException;
import com.okan.bankingmanagement.exception.InvalidAccountTypeException;
import com.okan.bankingmanagement.exception.UnexpectedErrorException;
import com.okan.bankingmanagement.repository.impl.BatisAccountRepository;
import com.okan.bankingmanagement.service.AccountService;

@RestController
@RequestMapping("v1/accounts")
public class AccountController {
	
	
	private AccountService service;
	
	@Autowired
	public AccountController(BatisAccountRepository accountRepository, ModelMapper mapper, AccountService service) {
		this.service = service;
	}

	@PostMapping(path = "/{create}", produces = { MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasAnyAuthority('user:CREATE_ACCOUNT')")
	public ResponseEntity<?> createAccount(@RequestBody AccountCreateRequest request) throws InvalidAccountTypeException
	{
		AccountDetailResponse response = service.createAccount(Integer.parseInt(request.getBank()) ,request.getType()) ;
	     return ResponseEntity
	                    .status(HttpStatus.CREATED)
	                    .body(response);
	    
	}
	
	@GetMapping()
	public ResponseEntity<Object> getAccount(){
		
		List<AccountDetailResponse> response = service.getAccounts() ;
	     return ResponseEntity
	                    .status(HttpStatus.OK)
	                    .body(response);
		
		
	}
	
	@GetMapping(path = "/{accountNumber}", produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> getDetailByAccountNumber(@PathVariable int accountNumber){
		
		
		AccountDetailResponse detail = service.getAccountByAccountNumber(accountNumber);
		
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Instant instant = detail.getLast_update_date().toInstant();
		ZonedDateTime zonedDateTime = instant.atZone(defaultZoneId);
		
		return ResponseEntity
		.ok()
		.lastModified(zonedDateTime)
        .body(detail);
	}
	
	@DeleteMapping("/{accountNumber}")
	@PreAuthorize("hasAnyAuthority('user:REMOVE_ACCOUNT')")
    public ResponseEntity<?> deleteAccount(@PathVariable int accountNumber) {
        boolean result;
        try {
            result = service.deleteAccount(accountNumber);
        } catch (AccountNotFoundException | DeletedAccountException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (UnexpectedErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
		}

	
	
}
