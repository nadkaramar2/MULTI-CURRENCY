package ams.cms.utility;

import java.io.Serializable;

public class AccountMasterResponse implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String strAccountHolderName;
	private String strAccountType;
	private String strAccountNo;
	private String strAccountStatus;
	private String strAvailableBalance;
	private String strCurrentrTier;
	
	private String strPOADocumentType;
	private String strPOADocumentName;
	private String strPOADocumentValue;
	
	private String strPOIDocumentType;
	private String strPOIDocumentName;
	private String strPOIDocumentValue;
	
	private String strTier1PassportPhotograph;
	private String strTier2PassportPhotograph;
	private String strBvnNo;
	
	public String getStrAccountHolderName() {
		return strAccountHolderName;
	}
	public void setStrAccountHolderName(String strAccountHolderName) {
		this.strAccountHolderName = strAccountHolderName;
	}
	public String getStrAccountType() {
		return strAccountType;
	}
	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}
	public String getStrAccountNo() {
		return strAccountNo;
	}
	public void setStrAccountNo(String strAccountNo) {
		this.strAccountNo = strAccountNo;
	}
	public String getStrAccountStatus() {
		return strAccountStatus;
	}
	public void setStrAccountStatus(String strAccountStatus) {
		this.strAccountStatus = strAccountStatus;
	}
	public String getStrAvailableBalance() {
		return strAvailableBalance;
	}
	public void setStrAvailableBalance(String strAvailableBalance) {
		this.strAvailableBalance = strAvailableBalance;
	}
	public String getStrCurrentrTier() {
		return strCurrentrTier;
	}
	public void setStrCurrentrTier(String strCurrentrTier) {
		this.strCurrentrTier = strCurrentrTier;
	}
	public String getStrPOADocumentType() {
		return strPOADocumentType;
	}
	public void setStrPOADocumentType(String strPOADocumentType) {
		this.strPOADocumentType = strPOADocumentType;
	}
	public String getStrPOADocumentName() {
		return strPOADocumentName;
	}
	public void setStrPOADocumentName(String strPOADocumentName) {
		this.strPOADocumentName = strPOADocumentName;
	}
	public String getStrPOADocumentValue() {
		return strPOADocumentValue;
	}
	public void setStrPOADocumentValue(String strPOADocumentValue) {
		this.strPOADocumentValue = strPOADocumentValue;
	}
	public String getStrPOIDocumentType() {
		return strPOIDocumentType;
	}
	public void setStrPOIDocumentType(String strPOIDocumentType) {
		this.strPOIDocumentType = strPOIDocumentType;
	}
	public String getStrPOIDocumentName() {
		return strPOIDocumentName;
	}
	public void setStrPOIDocumentName(String strPOIDocumentName) {
		this.strPOIDocumentName = strPOIDocumentName;
	}
	public String getStrPOIDocumentValue() {
		return strPOIDocumentValue;
	}
	public void setStrPOIDocumentValue(String strPOIDocumentValue) {
		this.strPOIDocumentValue = strPOIDocumentValue;
	}
	public String getStrTier1PassportPhotograph() {
		return strTier1PassportPhotograph;
	}
	public void setStrTier1PassportPhotograph(String strTier1PassportPhotograph) {
		this.strTier1PassportPhotograph = strTier1PassportPhotograph;
	}
	public String getStrTier2PassportPhotograph() {
		return strTier2PassportPhotograph;
	}
	public void setStrTier2PassportPhotograph(String strTier2PassportPhotograph) {
		this.strTier2PassportPhotograph = strTier2PassportPhotograph;
	}
	public String getStrBvnNo() {
		return strBvnNo;
	}
	public void setStrBvnNo(String strBvnNo) {
		this.strBvnNo = strBvnNo;
	}
}
