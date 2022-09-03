package com.okan.bankingmanagement.dto.response;

public class CreateBankResponse {
	
	private boolean success;
	private String message;
	private BankResponse bank;
	
	public CreateBankResponse(boolean success, String message, BankResponse bank) {
		super();
		this.success = success;
		this.message = message;
		this.bank = bank;
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

	public BankResponse getBank() {
		return bank;
	}

	public void setBank(BankResponse bank) {
		this.bank = bank;
	}
	
	
	
	

}
