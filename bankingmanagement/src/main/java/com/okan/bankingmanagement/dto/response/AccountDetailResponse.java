package com.okan.bankingmanagement.dto.response;

import java.util.Date;

import com.okan.bankingmanagement.domain.AccountType;
import com.okan.bankingmanagement.domain.Bank;
import com.okan.bankingmanagement.domain.User;

public class AccountDetailResponse {
	
	private Bank bank;
	private User user;
	private int account_number;
	private AccountType type;
	private double balance;
	private Date creation_date;
	private Date last_update_date;
	private boolean is_deleted;
	
	public boolean isIs_deleted() {
		return is_deleted;
	}
	public void setIs_deleted(boolean is_deleted) {
		this.is_deleted = is_deleted;
	}
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public int getUser() {
		return user.getId();
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getAccount_number() {
		return account_number;
	}
	public void setAccount_number(int account_number) {
		this.account_number = account_number;
	}
	public AccountType getType() {
		return type;
	}
	public void setType(AccountType type) {
		this.type = type;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public Date getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}
	public Date getLast_update_date() {
		return last_update_date;
	}
	public void setLast_update_date(Date last_update_date) {
		this.last_update_date = last_update_date;
	}
	
	

}
