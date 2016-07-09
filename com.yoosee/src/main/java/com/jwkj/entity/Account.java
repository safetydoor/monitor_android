package com.jwkj.entity;

public class Account {
	public String three_number;
	public String email;
	public String phone;
	public String sessionId;
	public String rCode1;
	public String rCode2;
	public String accessKey;
	public String countryCode;

	public Account() {

	}

	public Account(String three_number, String email, String phone,
			String sessionId, String code1, String code2, String countryCode) {
		this.three_number = three_number;
		this.email = email;
		this.phone = phone;
		this.sessionId = sessionId;
		this.rCode1 = code1;
		this.rCode2 = code2;
		this.countryCode = countryCode;
	}

}
