package ams.cms.utility;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class OtherBankDataObj implements Serializable 
{
	private static final long serialVersionUID = 1L;
	private	String recipientInstitutionCode;
	private	String recipientAccountNumber;
	private	String recipientAccountName;
	private	String recipientBvn;
	
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
	
	@Override
	public String toString() {
		return "OtherBankDataObj [recipientInstitutionCode=" + recipientInstitutionCode + ", recipientAccountNumber="
				+ recipientAccountNumber + ", recipientAccountName=" + recipientAccountName + ", recipientBvn="
				+ recipientBvn + "]";
	}	
}
