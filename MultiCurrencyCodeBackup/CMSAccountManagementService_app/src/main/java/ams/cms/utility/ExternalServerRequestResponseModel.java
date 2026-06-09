package ams.cms.utility;

import java.io.Serializable;

public class ExternalServerRequestResponseModel implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String code;
	private Data data;
	private String message;
	
	private boolean success;
	
	private String accountNumber;
	private String channelCode;
	private String destinationInstitutionCode;
	private String transactionId;
	
	private String responseCode;
	private String sessionID;
	private String accountName;
	private String bankVerificationNumber;
	private String kycLevel;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
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
	public void setDestinationInstitutionCode(String destinationInstitutionCode) {
		this.destinationInstitutionCode = destinationInstitutionCode;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
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
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "ExternalServerRequestResponseModel [code=" + code + ", data=" + data + ", message=" + message
				+ ", success=" + success + ", accountNumber=" + accountNumber + ", channelCode=" + channelCode
				+ ", destinationInstitutionCode=" + destinationInstitutionCode + ", transactionId=" + transactionId
				+ ", responseCode=" + responseCode + ", sessionID=" + sessionID + ", accountName=" + accountName
				+ ", bankVerificationNumber=" + bankVerificationNumber + ", kycLevel=" + kycLevel + "]";
	}
}
