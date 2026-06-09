package ams.cms.utility;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import ams.cms.model.BankDetailsInfo;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Data implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<BankDetailsInfo> banks;
	private String status;
	
	private String accountHolderName;
	
	private String accountNumber;
	
	//private String accountNumber;
	private String accountName;
	private String kycLevel;
	private String currencyCode;
	private String customerNo;
	private String sessionID;
	private String channelCode;
	private String responseCode;	
	private String bankVerificationNumber;
	private String destinationInstitutionCode;
	
	private String category;
	private String code;
	private String bankCode;
	private String name;
	
	@JsonProperty("AccountClassType")
	private String accountClassType;
	
	private String accountNo;
	
	@JsonProperty("AccountStatus")
	private String accountStatus;
	
	@JsonProperty("BVN")
	private String bvn;
	
	private String recipientInstitutionCode;	
	private String recipientAccountNumber;	
	private String recipientAccountName;	
	private String recipientBvn;
	
	private String iv;
	private String phrase;
	private String salt;
	
	private String cid;
	private String custId;
	private String bid;
	
	public String getBvn() {
		return bvn;
	}

	public void setBvn(String bvn) {
		this.bvn = bvn;
	}

	public List<BankDetailsInfo> getBanks() {
		return banks;
	}

	public void setBanks(List<BankDetailsInfo> banks) {
		this.banks = banks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
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

	public void setDestinationInstitutionCode(String destinationInstitutionCode) {
		this.destinationInstitutionCode = destinationInstitutionCode;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAccountClassType() {
		return accountClassType;
	}

	public void setAccountClassType(String accountClassType) {
		this.accountClassType = accountClassType;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getRecipientInstitutionCode() {
		return recipientInstitutionCode;
	}

	public void setRecipientInstitutionCode(String recipientInstitutionCode) {
		this.recipientInstitutionCode = recipientInstitutionCode;
	}

	public String getRecipientAccountNumber() {
		return recipientAccountNumber;
	}

	public void setRecipientAccountNumber(String recipientAccountNumber) {
		this.recipientAccountNumber = recipientAccountNumber;
	}

	public String getRecipientAccountName() {
		return recipientAccountName;
	}

	public void setRecipientAccountName(String recipientAccountName) {
		this.recipientAccountName = recipientAccountName;
	}

	public String getRecipientBvn() {
		return recipientBvn;
	}

	public void setRecipientBvn(String recipientBvn) {
		this.recipientBvn = recipientBvn;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	@Override
	public String toString() {
		return "Data [banks=" + banks + ", status=" + status + ", accountHolderName=" + accountHolderName
				+ ", accountNumber=" + accountNumber + ", accountName=" + accountName + ", kycLevel=" + kycLevel
				+ ", currencyCode=" + currencyCode + ", customerNo=" + customerNo + ", sessionID=" + sessionID
				+ ", channelCode=" + channelCode + ", responseCode=" + responseCode + ", bankVerificationNumber="
				+ bankVerificationNumber + ", destinationInstitutionCode=" + destinationInstitutionCode + ", category="
				+ category + ", code=" + code + ", bankCode=" + bankCode + ", name=" + name + ", accountClassType="
				+ accountClassType + ", accountNo=" + accountNo + ", accountStatus=" + accountStatus + "]";
	}
}
