package ams.cms.model;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class WalleToWalletTransfer {
	
private static final long serialVersionUID = 1L;
	
	@Transient
	private String fromCurrency;
	
	@Transient
	private String strChannel;
	
	@Transient
	private String toCurrency;
	
	@Transient
	private double strTxnAmount;
	
	@Transient
	private String strAccountNumber;
	
	@Transient
	private String pin;
	
	@Transient
	private String custId;

	public String getFromCurrency() {
		return fromCurrency;
	}

	public void setFromCurrency(String fromCurrency) {
		this.fromCurrency = fromCurrency;
	}

	public String getStrChannel() {
		return strChannel;
	}

	public void setStrChannel(String strChannel) {
		this.strChannel = strChannel;
	}

	public String getToCurrency() {
		return toCurrency;
	}

	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}

	public String getStrAccountNumber() {
		return strAccountNumber;
	}

	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}

	public double getStrTxnAmount() {
		return strTxnAmount;
	}

	public void setStrTxnAmount(double strTxnAmount) {
		this.strTxnAmount = strTxnAmount;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}
	
}
