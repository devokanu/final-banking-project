package com.okan.bankingmanagement.dto.response;

public class DepositResponse {
	
	private boolean success;
	private String message;
	private double balance;
	
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
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public DepositResponse(boolean success, String message, double balance) {
		this.success = success;
		this.message = message;
		this.balance = balance;
	}
	
	
	
	

}
