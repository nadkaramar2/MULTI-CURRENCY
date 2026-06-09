package ams.cms.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class FundTransferResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String reference;
	private String status;
	private String information;
	private String amsTranId;
	private String amsAuthCode;
	
	private String accountName;
	private String accountNumber;
	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	public String getAmsTranId() {
		return amsTranId;
	}
	public void setAmsTranId(String amsTranId) {
		this.amsTranId = amsTranId;
	}
	public String getAmsAuthCode() {
		return amsAuthCode;
	}
	public void setAmsAuthCode(String amsAuthCode) {
		this.amsAuthCode = amsAuthCode;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
}
