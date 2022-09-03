package com.okan.bankingmanagement.dto.response;

public class AccountCreateResponse {
	
	private boolean success;
	private String message;
	private AccountDetailResponse account;
	
	
	
	public AccountCreateResponse(boolean success, String message, AccountDetailResponse account) {
		super();
		this.success = success;
		this.message = message;
		this.account = account;
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
	public AccountDetailResponse getAccountDetailResponse() {
		return account;
	}
	public void setAccountDetailResponse(AccountDetailResponse account) {
		this.account = account;
	}
	
	

}
