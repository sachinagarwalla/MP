package com.crunchify.model;

public class PaymentData {
	private AuthenticationOptions authenticationOptions;

	private String walletId;

	private PersonalInfo personalInfo;

	private Card card;

	private ShippingAddress shippingAddress;

	public AuthenticationOptions getAuthenticationOptions() {
		return authenticationOptions;
	}

	public void setAuthenticationOptions(
			AuthenticationOptions authenticationOptions) {
		this.authenticationOptions = authenticationOptions;
	}

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public PersonalInfo getPersonalInfo() {
		return personalInfo;
	}

	public void setPersonalInfo(PersonalInfo personalInfo) {
		this.personalInfo = personalInfo;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public ShippingAddress getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(ShippingAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	@Override
	public String toString() {
		return "ClassPojo [authenticationOptions = " + authenticationOptions
				+ ", walletId = " + walletId + ", personalInfo = "
				+ personalInfo + ", card = " + card + ", shippingAddress = "
				+ shippingAddress + "]";
	}
}
