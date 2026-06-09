package ams.cms.utility;

import java.io.Serializable;

public class SignInResponseParam implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String strAccountNumber;
	private String strAccountType;
	private String accountCategoryType;
	private String qrCodeImageUrl;
	private String availableBalance;
	private String isMultiCurrencySupport;
	private String description;
	
	public String getStrAccountNumber() {
		return strAccountNumber;
	}
	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}
	public String getStrAccountType() {
		return strAccountType;
	}
	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}
	public String getAccountCategoryType() {
		return accountCategoryType;
	}
	public void setAccountCategoryType(String accountCategoryType) {
		this.accountCategoryType = accountCategoryType;
	}
	public String getQrCodeImageUrl() {
		return qrCodeImageUrl;
	}
	public void setQrCodeImageUrl(String qrCodeImageUrl) {
		this.qrCodeImageUrl = qrCodeImageUrl;
	}
	public String getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}
	public String getIsMultiCurrencySupport() {
		return isMultiCurrencySupport;
	}
	public void setIsMultiCurrencySupport(String isMultiCurrencySupport) {
		this.isMultiCurrencySupport = isMultiCurrencySupport;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
