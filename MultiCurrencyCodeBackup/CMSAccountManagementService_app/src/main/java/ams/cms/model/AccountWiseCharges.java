package ams.cms.model;

import java.io.Serializable;

public class AccountWiseCharges implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String participantId;
	private String strAccountType;
	private String strAccountNumber;
	private String strCardType;
	private String strCardNumber;
	private String strChargeType;
	private String strAmount;
	private String strClosingBalance;
	private String strGLAccountNumber;
	private String strGLAccountType;
	private String strTranType;
	private String strTranMode;
	private String strTransactionId;
	private AccountCreation accountCreation;
	
	public AccountCreation getAccountCreation() {
		return accountCreation;
	}
	public void setAccountCreation(AccountCreation accountCreation) {
		this.accountCreation = accountCreation;
	}
	public String getStrAccountType() {
		return strAccountType;
	}
	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}
	public String getStrAccountNumber() {
		return strAccountNumber;
	}
	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}
	public String getStrCardType() {
		return strCardType;
	}
	public void setStrCardType(String strCardType) {
		this.strCardType = strCardType;
	}
	public String getStrCardNumber() {
		return strCardNumber;
	}
	public void setStrCardNumber(String strCardNumber) {
		this.strCardNumber = strCardNumber;
	}
	public String getStrChargeType() {
		return strChargeType;
	}
	public void setStrChargeType(String strChargeType) {
		this.strChargeType = strChargeType;
	}
	public String getStrAmount() {
		return strAmount;
	}
	public void setStrAmount(String strAmount) {
		this.strAmount = strAmount;
	}
	public String getStrClosingBalance() {
		return strClosingBalance;
	}
	public void setStrClosingBalance(String strClosingBalance) {
		this.strClosingBalance = strClosingBalance;
	}
	public String getStrGLAccountNumber() {
		return strGLAccountNumber;
	}
	public void setStrGLAccountNumber(String strGLAccountNumber) {
		this.strGLAccountNumber = strGLAccountNumber;
	}
	public String getStrGLAccountType() {
		return strGLAccountType;
	}
	public void setStrGLAccountType(String strGLAccountType) {
		this.strGLAccountType = strGLAccountType;
	}
	public String getStrTranType() {
		return strTranType;
	}
	public void setStrTranType(String strTranType) {
		this.strTranType = strTranType;
	}
	public String getStrTranMode() {
		return strTranMode;
	}
	public void setStrTranMode(String strTranMode) {
		this.strTranMode = strTranMode;
	}
	public String getStrTransactionId() {
		return strTransactionId;
	}
	public void setStrTransactionId(String strTransactionId) {
		this.strTransactionId = strTransactionId;
	}
	public String getParticipantId() {
		return participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
	
}
