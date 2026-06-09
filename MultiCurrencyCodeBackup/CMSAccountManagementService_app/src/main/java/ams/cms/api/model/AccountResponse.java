package ams.cms.api.model;

import java.io.Serializable;

public class AccountResponse implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private String strID;	
	private String strCustId;	
	private String strAccountType;	
	private String strAccountNumber;	
	private String strEmailID;	
	private String strMobileNo;		
	private String strStatus;
	private String strAccounTypeStatus;
	
	private String strClosingBalance;		
	
	private Double strEarMarkAmount;	
	private Double strPreCredAmount;	
	
	private int strSingleTxnLimit;	
	private String strDailyTxnLimit;	
	private String strMonthlyTxnLimit;	
	private String strYearlyTxnLimit;	
	
	private String strAvailableDailyLimit;	
	private String strAvailableMonthlyLimit;	
	private String strAvailableYearlyLimit;	
	
	private String strAccountHolderName;	
	private String accountCategoryType;	
	
	private String strGLAccountType;	
	private String strGLAccountNo;	
	private String strGLAccountDescription;
	private String strGLAccountBalance;	
	
	private String strActiveTier;	
	private String tier1CumBalance;	
	private String tier2CumBalance;	
	private String tier3CumBalance;	
	private String strTier1DailyCumlimit;	
	private String strTier2DailyCumlimit;	
	private String strTier3DailyCumlimit;	
	private String availableTier1DailyCumLimit;	
	private String availableTier2DailyCumLimit;	
	private String availableTier3DailyCumLimit;
	
	private String strTotalOutstandingBal;	
	private String strCreditLimitAmount;	
	private String strAvailableCreditLimit;	
	private String strBillingCycleDate;
	
	private String feeType;
	private String vat;
	private String fee;
	private Boolean isFeeApplicable = false;
	private Boolean isFeeApplicableToRecipent = false;
	
	private String availableBalance;
	
	private String entityInfo;
	private String entityNumber;
	
	public String getStrID() {
		return strID;
	}
	public void setStrID(String strID) {
		this.strID = strID;
	}
	public String getStrCustId() {
		return strCustId;
	}
	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
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
	public String getStrEmailID() {
		return strEmailID;
	}
	public void setStrEmailID(String strEmailID) {
		this.strEmailID = strEmailID;
	}
	public String getStrMobileNo() {
		return strMobileNo;
	}
	public void setStrMobileNo(String strMobileNo) {
		this.strMobileNo = strMobileNo;
	}
	public String getStrStatus() {
		return strStatus;
	}
	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}
	public String getStrClosingBalance() {
		return strClosingBalance;
	}
	public void setStrClosingBalance(String strClosingBalance) {
		this.strClosingBalance = strClosingBalance;
	}
	public Double getStrEarMarkAmount() {
		return strEarMarkAmount;
	}
	public void setStrEarMarkAmount(Double strEarMarkAmount) {
		this.strEarMarkAmount = strEarMarkAmount;
	}
	public Double getStrPreCredAmount() {
		return strPreCredAmount;
	}
	public void setStrPreCredAmount(Double strPreCredAmount) {
		this.strPreCredAmount = strPreCredAmount;
	}
	public int getStrSingleTxnLimit() {
		return strSingleTxnLimit;
	}
	public void setStrSingleTxnLimit(int strSingleTxnLimit) {
		this.strSingleTxnLimit = strSingleTxnLimit;
	}
	public String getStrDailyTxnLimit() {
		return strDailyTxnLimit;
	}
	public void setStrDailyTxnLimit(String strDailyTxnLimit) {
		this.strDailyTxnLimit = strDailyTxnLimit;
	}
	public String getStrMonthlyTxnLimit() {
		return strMonthlyTxnLimit;
	}
	public void setStrMonthlyTxnLimit(String strMonthlyTxnLimit) {
		this.strMonthlyTxnLimit = strMonthlyTxnLimit;
	}
	public String getStrYearlyTxnLimit() {
		return strYearlyTxnLimit;
	}
	public void setStrYearlyTxnLimit(String strYearlyTxnLimit) {
		this.strYearlyTxnLimit = strYearlyTxnLimit;
	}
	public String getStrAvailableDailyLimit() {
		return strAvailableDailyLimit;
	}
	public void setStrAvailableDailyLimit(String strAvailableDailyLimit) {
		this.strAvailableDailyLimit = strAvailableDailyLimit;
	}
	public String getStrAvailableMonthlyLimit() {
		return strAvailableMonthlyLimit;
	}
	public void setStrAvailableMonthlyLimit(String strAvailableMonthlyLimit) {
		this.strAvailableMonthlyLimit = strAvailableMonthlyLimit;
	}
	public String getStrAvailableYearlyLimit() {
		return strAvailableYearlyLimit;
	}
	public void setStrAvailableYearlyLimit(String strAvailableYearlyLimit) {
		this.strAvailableYearlyLimit = strAvailableYearlyLimit;
	}
	public String getStrAccountHolderName() {
		return strAccountHolderName;
	}
	public void setStrAccountHolderName(String strAccountHolderName) {
		this.strAccountHolderName = strAccountHolderName;
	}
	public String getAccountCategoryType() {
		return accountCategoryType;
	}
	public void setAccountCategoryType(String accountCategoryType) {
		this.accountCategoryType = accountCategoryType;
	}
	public String getStrGLAccountType() {
		return strGLAccountType;
	}
	public void setStrGLAccountType(String strGLAccountType) {
		this.strGLAccountType = strGLAccountType;
	}
	public String getStrGLAccountNo() {
		return strGLAccountNo;
	}
	public void setStrGLAccountNo(String strGLAccountNo) {
		this.strGLAccountNo = strGLAccountNo;
	}
	public String getStrGLAccountDescription() {
		return strGLAccountDescription;
	}
	public void setStrGLAccountDescription(String strGLAccountDescription) {
		this.strGLAccountDescription = strGLAccountDescription;
	}
	public String getStrGLAccountBalance() {
		return strGLAccountBalance;
	}
	public void setStrGLAccountBalance(String strGLAccountBalance) {
		this.strGLAccountBalance = strGLAccountBalance;
	}
	public String getStrActiveTier() {
		return strActiveTier;
	}
	public void setStrActiveTier(String strActiveTier) {
		this.strActiveTier = strActiveTier;
	}
	public String getTier1CumBalance() {
		return tier1CumBalance;
	}
	public void setTier1CumBalance(String tier1CumBalance) {
		this.tier1CumBalance = tier1CumBalance;
	}
	public String getTier2CumBalance() {
		return tier2CumBalance;
	}
	public void setTier2CumBalance(String tier2CumBalance) {
		this.tier2CumBalance = tier2CumBalance;
	}
	public String getTier3CumBalance() {
		return tier3CumBalance;
	}
	public void setTier3CumBalance(String tier3CumBalance) {
		this.tier3CumBalance = tier3CumBalance;
	}
	public String getStrTier1DailyCumlimit() {
		return strTier1DailyCumlimit;
	}
	public void setStrTier1DailyCumlimit(String strTier1DailyCumlimit) {
		this.strTier1DailyCumlimit = strTier1DailyCumlimit;
	}
	public String getStrTier2DailyCumlimit() {
		return strTier2DailyCumlimit;
	}
	public void setStrTier2DailyCumlimit(String strTier2DailyCumlimit) {
		this.strTier2DailyCumlimit = strTier2DailyCumlimit;
	}
	public String getStrTier3DailyCumlimit() {
		return strTier3DailyCumlimit;
	}
	public void setStrTier3DailyCumlimit(String strTier3DailyCumlimit) {
		this.strTier3DailyCumlimit = strTier3DailyCumlimit;
	}
	public String getAvailableTier1DailyCumLimit() {
		return availableTier1DailyCumLimit;
	}
	public void setAvailableTier1DailyCumLimit(String availableTier1DailyCumLimit) {
		this.availableTier1DailyCumLimit = availableTier1DailyCumLimit;
	}
	public String getAvailableTier2DailyCumLimit() {
		return availableTier2DailyCumLimit;
	}
	public void setAvailableTier2DailyCumLimit(String availableTier2DailyCumLimit) {
		this.availableTier2DailyCumLimit = availableTier2DailyCumLimit;
	}
	public String getAvailableTier3DailyCumLimit() {
		return availableTier3DailyCumLimit;
	}
	public void setAvailableTier3DailyCumLimit(String availableTier3DailyCumLimit) {
		this.availableTier3DailyCumLimit = availableTier3DailyCumLimit;
	}
	public String getStrTotalOutstandingBal() {
		return strTotalOutstandingBal;
	}
	public void setStrTotalOutstandingBal(String strTotalOutstandingBal) {
		this.strTotalOutstandingBal = strTotalOutstandingBal;
	}
	public String getStrCreditLimitAmount() {
		return strCreditLimitAmount;
	}
	public void setStrCreditLimitAmount(String strCreditLimitAmount) {
		this.strCreditLimitAmount = strCreditLimitAmount;
	}
	public String getStrAvailableCreditLimit() {
		return strAvailableCreditLimit;
	}
	public void setStrAvailableCreditLimit(String strAvailableCreditLimit) {
		this.strAvailableCreditLimit = strAvailableCreditLimit;
	}
	public String getStrBillingCycleDate() {
		return strBillingCycleDate;
	}
	public void setStrBillingCycleDate(String strBillingCycleDate) {
		this.strBillingCycleDate = strBillingCycleDate;
	}
	public String getStrAccounTypeStatus() {
		return strAccounTypeStatus;
	}
	public void setStrAccounTypeStatus(String strAccounTypeStatus) {
		this.strAccounTypeStatus = strAccounTypeStatus;
	}
	
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getVat() {
		return vat;
	}
	public void setVat(String vat) {
		this.vat = vat;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public Boolean getIsFeeApplicable() {
		return isFeeApplicable;
	}
	public void setIsFeeApplicable(Boolean isFeeApplicable) {
		this.isFeeApplicable = isFeeApplicable;
	}
	public Boolean getIsFeeApplicableToRecipent() {
		return isFeeApplicableToRecipent;
	}
	public void setIsFeeApplicableToRecipent(Boolean isFeeApplicableToRecipent) {
		this.isFeeApplicableToRecipent = isFeeApplicableToRecipent;
	}
	public String getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}
	public String getEntityInfo() {
		return entityInfo;
	}
	public void setEntityInfo(String entityInfo) {
		this.entityInfo = entityInfo;
	}
	public String getEntityNumber() {
		return entityNumber;
	}
	public void setEntityNumber(String entityNumber) {
		this.entityNumber = entityNumber;
	}
}
