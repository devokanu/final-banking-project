package com.okan.bankingmanagement.dto.request;

public class TransferCreateRequest {
	
	private int receiverAccountId;
	private double amount;
	
	public int getReceiverAccountId() {
		return receiverAccountId;
	}
	public void setReceiverId(int receiverAccountId) {
		this.receiverAccountId = receiverAccountId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	

}
