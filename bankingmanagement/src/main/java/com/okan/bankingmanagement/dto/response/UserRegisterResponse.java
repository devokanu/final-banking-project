package com.okan.bankingmanagement.dto.response;

import java.util.List;

import com.okan.bankingmanagement.domain.Account;

public class UserRegisterResponse {
	
	private boolean success;
	private String message;
	private UserResponse userResponse;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public UserResponse getUserResponse() {
		return userResponse;
	}
	public void setUserResponse(UserResponse userResponse) {
		this.userResponse = userResponse;
	}
	
	
	
	
	

}
