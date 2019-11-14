package com.driver.hire_me;

public class CreditCard {
	
	private String cardNumber;
	private String expDate;
	private String securityCode;
	

	public CreditCard(String cardNumber, String expDate, String securityCode)
	{
		this.setCardNumber(cardNumber);
		this.setExpDate(expDate);
		this.setSecurityCode(securityCode);
		
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	
}
