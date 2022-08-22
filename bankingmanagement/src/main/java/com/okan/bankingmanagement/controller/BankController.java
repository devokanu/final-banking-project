package com.okan.bankingmanagement.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okan.bankingmanagement.dto.request.AccountCreateRequest;
import com.okan.bankingmanagement.dto.request.BankCreateRequest;
import com.okan.bankingmanagement.dto.response.AccountDetailResponse;
import com.okan.bankingmanagement.dto.response.BankResponse;
import com.okan.bankingmanagement.dto.response.ErrorResponse;
import com.okan.bankingmanagement.exception.BankExistException;
import com.okan.bankingmanagement.exception.InvalidAccountTypeException;
import com.okan.bankingmanagement.exception.UnexpectedErrorException;
import com.okan.bankingmanagement.service.AccountService;
import com.okan.bankingmanagement.service.BankService;

@RestController
@RequestMapping("v1/bank")
public class BankController {

	private final BankService service;

	@Autowired
	public BankController(BankService service) {
		this.service = service;
	}
	
	@GetMapping()
	public ResponseEntity<Object> getBanks(){
		List<BankResponse> response = service.getBanks() ;
	     return ResponseEntity
	                    .status(HttpStatus.OK)
	                    .body(response);
	}
	
	@PostMapping(path = "/{create}", produces = { MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasAnyAuthority('user:CREATE_BANK')")
	public ResponseEntity<?> createBank(@RequestBody BankCreateRequest request) throws BankExistException
	{
		String message = "";
		try {
			BankResponse response = service.createBank(request.getName()) ;
			message = "Created Successfully";
		} catch (UnexpectedErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
	     return ResponseEntity
	                    .status(HttpStatus.CREATED)
	                    .body(message);
	     
	     
	    
	}
	
	
	
}
