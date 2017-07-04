package com.crunchify.model;

public class Card {
	private String expiryYear;

	private String accountNumber;

	private String cardHolderName;

	private String expiryMonth;

	private BillingAddress billingAddress;

	private String brandName;

	private String brandId;

	public String getExpiryYear() {
		return expiryYear;
	}

	public void setExpiryYear(String expiryYear) {
		this.expiryYear = expiryYear;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getExpiryMonth() {
		return expiryMonth;
	}

	public void setExpiryMonth(String expiryMonth) {
		this.expiryMonth = expiryMonth;
	}

	public BillingAddress getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(BillingAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	@Override
	public String toString() {
		return "ClassPojo [expiryYear = " + expiryYear + ", accountNumber = "
				+ accountNumber + ", cardHolderName = " + cardHolderName
				+ ", expiryMonth = " + expiryMonth + ", billingAddress = "
				+ billingAddress + ", brandName = " + brandName
				+ ", brandId = " + brandId + "]";
	}
}