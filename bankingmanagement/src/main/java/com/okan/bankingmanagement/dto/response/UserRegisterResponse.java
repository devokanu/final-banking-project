package com.okan.bankingmanagement.dto.response;

import java.util.List;

import com.okan.bankingmanagement.domain.Account;

public class UserRegisterResponse {
	
	private int id;
	private String username;
	private String email;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	

}