package com.okan.bankingmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okan.bankingmanagement.domain.HttpResponse;
import com.okan.bankingmanagement.domain.User;
import com.okan.bankingmanagement.domain.UserPrincipal;
import com.okan.bankingmanagement.dto.request.UserLoginRequest;
import com.okan.bankingmanagement.dto.request.UserRegisterRequest;
import com.okan.bankingmanagement.dto.response.ErrorResponse;
import com.okan.bankingmanagement.dto.response.LoginResponse;
import com.okan.bankingmanagement.dto.response.UserActiveStatusResponse;
import com.okan.bankingmanagement.dto.response.UserRegisterResponse;
import com.okan.bankingmanagement.dto.response.UserResponse;
import com.okan.bankingmanagement.exception.EmailExistException;
import com.okan.bankingmanagement.exception.UnexpectedErrorException;
import com.okan.bankingmanagement.exception.UserNotFoundException;
import com.okan.bankingmanagement.exception.UsernameExistException;
import com.okan.bankingmanagement.service.UserService;
import com.okan.bankingmanagement.utility.JWTTokenProvider;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static com.okan.bankingmanagement.constant.SecurityConstant.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path= {"/","/users"})
@CrossOrigin("http://localhost:4200")
public class UserController {
	
	private final UserService service;
	private AuthenticationManager authenticationManager;
	private JWTTokenProvider jwtTokenProvider;
	
	
	
	@Autowired
	public UserController(UserService service, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider) {
		this.service = service;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping("/register")
	public ResponseEntity<UserRegisterResponse> register(@RequestBody UserRegisterRequest request) throws MessagingException, UserNotFoundException, UsernameExistException, EmailExistException{
		service.register(request.getUsername(),request.getEmail(), request.getPassword()) ;
		UserRegisterResponse response = new UserRegisterResponse();
		response.setSuccess(true);
		response.setMessage("Created Successfully");
		response.setUserResponse(new UserResponse(request.getUsername(),request.getEmail(),true));
	    
		return ResponseEntity
	                    .status(HttpStatus.CREATED)
	                    .body(response);
		
	}
	
	@PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = service.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        
        return new ResponseEntity<>(new LoginResponse(true,"Logged-In Successfully",jwtHeader.get("Jwt-Token").toString()), jwtHeader, OK);
    }
	
	private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }
	
	private void authenticate(String username, String password) {
		 Authentication authenticate =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		 System.out.println(authenticate);
    }
	
	@PatchMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ACTIVATE_DEACTIVATE_USER')")
	public ResponseEntity<?> activityUserStatus(@PathVariable int id){
		String message = "";
		
		try {
			UserResponse response = service.updateUser(id);
			message = response.getUsername() + " is " + response.isEnabled();
			
		} catch (UnexpectedErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
		
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(new UserActiveStatusResponse(true, message));
		
	}
	

}
