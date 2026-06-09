package ams.cms.utility;

import java.io.Serializable;

public class NameEnquiryResponseModel implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	private String code;
	private Data data;
	private String message;
	private boolean success;
	
	
	private String appId;
	private String auditId;
	private String participantId;
	
	private String recipientAccountNumber;
	private String senderAccountNumber;
	private String recipientInstitutionCode;
	private String recipientBankCode;
	
	
	private String accountNumber;
	private String accountName;
	private String bankVerificationNumber;
	private String kycLevel;
	private String currencyCode;
	private String customerNo;
	private String sessionID;
	private String channelCode;
	private String destinationInstitutionCode;
	private String responseCode;
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBankVerificationNumber() {
		return bankVerificationNumber;
	}
	public void setBankVerificationNumber(String bankVerificationNumber) {
		this.bankVerificationNumber = bankVerificationNumber;
	}
	public String getKycLevel() {
		return kycLevel;
	}
	public void setKycLevel(String kycLevel) {
		this.kycLevel = kycLevel;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getDestinationInstitutionCode() {
		return destinationInstitutionCode;
	}
	public void setDestinationInstitutionCode(String destinationInstitutionCode) 
	{
		this.destinationInstitutionCode = destinationInstitutionCode;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAuditId() {
		return auditId;
	}
	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
	public String getParticipantId() {
		return participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
	public String getRecipientAccountNumber() {
		return recipientAccountNumber;
	}
	public void setRecipientAccountNumber(String recipientAccountNumber) {
		this.recipientAccountNumber = recipientAccountNumber;
	}
	public String getSenderAccountNumber() {
		return senderAccountNumber;
	}
	public void setSenderAccountNumber(String senderAccountNumber) {
		this.senderAccountNumber = senderAccountNumber;
	}
	public String getRecipientInstitutionCode() {
		return recipientInstitutionCode;
	}
	public void setRecipientInstitutionCode(String recipientInstitutionCode) {
		this.recipientInstitutionCode = recipientInstitutionCode;
	}
	public String getRecipientBankCode() {
		return recipientBankCode;
	}
	public void setRecipientBankCode(String recipientBankCode) {
		this.recipientBankCode = recipientBankCode;
	}
	
}
