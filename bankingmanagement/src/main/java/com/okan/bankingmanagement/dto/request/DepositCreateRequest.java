package com.okan.bankingmanagement.dto.request;

public class DepositCreateRequest {
	
	private int account_number;
	private double amount;
	
	public int getAccount_number() {
		return account_number;
	}
	public void setAccount_number(int account_number) {
		this.account_number = account_number;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
	

}
