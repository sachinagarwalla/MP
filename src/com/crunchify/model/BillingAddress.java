package com.crunchify.model;

public class BillingAddress {
	private String subdivision;

	private String postalCode;

	private String line1;

	private String line2;

	private String country;

	private String city;

	public String getSubdivision() {
		return subdivision;
	}

	public void setSubdivision(String subdivision) {
		this.subdivision = subdivision;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "ClassPojo [subdivision = " + subdivision + ", postalCode = "
				+ postalCode + ", line1 = " + line1 + ", line2 = " + line2
				+ ", country = " + country + ", city = " + city + "]";
	}
}