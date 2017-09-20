package com.crunchify.model;

public class PostbackPOJO {

	private String transactionId;
	private String currency;
	private String amount;
	private String paymentSuccessful;
	private String paymentCode;
	private String paymentDate;

	public PostbackPOJO(String transactionId, String currency, String amount,
			String paymentSuccessful, String paymentCode, String paymentDate) {
		super();
		this.transactionId = transactionId;
		this.currency = currency;
		this.amount = amount;
		this.paymentSuccessful = paymentSuccessful;
		this.paymentCode = paymentCode;
		this.paymentDate = paymentDate;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPaymentSuccessful() {
		return paymentSuccessful;
	}

	public void setPaymentSuccessful(String paymentSuccessful) {
		this.paymentSuccessful = paymentSuccessful;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

}
