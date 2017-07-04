package com.crunchify.model;

public class PersonalInfo {
	private String recipientPhone;

	private String recipientName;

	private String recipientEmailAddress;

	public String getRecipientPhone() {
		return recipientPhone;
	}

	public void setRecipientPhone(String recipientPhone) {
		this.recipientPhone = recipientPhone;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getRecipientEmailAddress() {
		return recipientEmailAddress;
	}

	public void setRecipientEmailAddress(String recipientEmailAddress) {
		this.recipientEmailAddress = recipientEmailAddress;
	}

	@Override
	public String toString() {
		return "ClassPojo [recipientPhone = " + recipientPhone
				+ ", recipientName = " + recipientName
				+ ", recipientEmailAddress = " + recipientEmailAddress + "]";
	}
}
