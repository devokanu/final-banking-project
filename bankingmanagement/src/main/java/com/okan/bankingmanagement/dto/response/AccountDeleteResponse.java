package com.okan.bankingmanagement.dto.response;

public class AccountDeleteResponse {
	
	private boolean success;
	private String message;
	
	public AccountDeleteResponse(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	
	
	public AccountDeleteResponse() {
		
	}


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
	
	

}
