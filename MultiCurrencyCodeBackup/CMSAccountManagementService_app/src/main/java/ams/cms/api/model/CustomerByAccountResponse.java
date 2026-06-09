package ams.cms.api.model;

import java.io.Serializable;

public class CustomerByAccountResponse implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String strAccountNumber;
	private String strAccountType;
	private String strCustId;
	
	private String strAccountHolderName;
	
	private Double strEarMarkAmount;
	private String strPreCredAmount;
	private String strClosingBalance;
	private String strStatus;
	private String strLoadCount;
	private String strTotalOutstandingBal;
	private String strAvailableGracePeriod;
	private String strAvailableCreditLimit;
	private String strAvailableDailyLimit;
	private String strAvailableMonthlyLimit;
	private String strAvailableYearlyLimit;
	private String strAccountTypeCategory;
	private String strAllowLoadCash;
	private String strTStatus;
	private String strGLAccountType;
	private String strGLAccountNumber;
	private String strNubanType; 
	private String strAccountTypeCode;
	private String strActiveTier;
	private String tier1CummBalance;
	private String tier2CummBalance;
	private String tier3CummBalance;
	private String strAvailableTier1DailyCumlimit;
	private String strAvailableTier2DailyCumlimit;
	private String strAvailableTier3DailyCumlimit;
	
	private String transferAmount;
	
	private String strFromAccountNumber;
	private String strFromAccountType;
	
	private String strToAccountNumber;
	private String strToAccountType;
	
	private String glAccountBalance;
	private String glAccountAvailableBalance;
	private String glAccountNo;
	private String glAccountType;
	private String glAccountDescr;
	
	private String txnId;
	private String txnType;
	private String txnNarration;
	
	private String strFromAccountTran;
	private String strToAccountTran;
	private String responseCode;
	
	private String strAvailableBalance;
	
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
	public String getStrCustId() {
		return strCustId;
	}
	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}
	public Double getStrEarMarkAmount() {
		return strEarMarkAmount;
	}
	public void setStrEarMarkAmount(Double strEarMarkAmount) {
		this.strEarMarkAmount = strEarMarkAmount;
	}
	public String getStrPreCredAmount() {
		return strPreCredAmount;
	}
	public void setStrPreCredAmount(String strPreCredAmount) {
		this.strPreCredAmount = strPreCredAmount;
	}
	public String getStrClosingBalance() {
		return strClosingBalance;
	}
	public void setStrClosingBalance(String strClosingBalance) {
		this.strClosingBalance = strClosingBalance;
	}
	public String getStrStatus() {
		return strStatus;
	}
	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}
	public String getStrLoadCount() {
		return strLoadCount;
	}
	public void setStrLoadCount(String strLoadCount) {
		this.strLoadCount = strLoadCount;
	}
	public String getStrTotalOutstandingBal() {
		return strTotalOutstandingBal;
	}
	public void setStrTotalOutstandingBal(String strTotalOutstandingBal) {
		this.strTotalOutstandingBal = strTotalOutstandingBal;
	}
	public String getStrAvailableGracePeriod() {
		return strAvailableGracePeriod;
	}
	public void setStrAvailableGracePeriod(String strAvailableGracePeriod) {
		this.strAvailableGracePeriod = strAvailableGracePeriod;
	}
	public String getStrAvailableCreditLimit() {
		return strAvailableCreditLimit;
	}
	public void setStrAvailableCreditLimit(String strAvailableCreditLimit) {
		this.strAvailableCreditLimit = strAvailableCreditLimit;
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
	public String getStrAccountTypeCategory() {
		return strAccountTypeCategory;
	}
	public void setStrAccountTypeCategory(String strAccountTypeCategory) {
		this.strAccountTypeCategory = strAccountTypeCategory;
	}
	public String getStrAllowLoadCash() {
		return strAllowLoadCash;
	}
	public void setStrAllowLoadCash(String strAllowLoadCash) {
		this.strAllowLoadCash = strAllowLoadCash;
	}
	public String getStrTStatus() {
		return strTStatus;
	}
	public void setStrTStatus(String strTStatus) {
		this.strTStatus = strTStatus;
	}
	public String getStrGLAccountType() {
		return strGLAccountType;
	}
	public void setStrGLAccountType(String strGLAccountType) {
		this.strGLAccountType = strGLAccountType;
	}
	public String getStrGLAccountNumber() {
		return strGLAccountNumber;
	}
	public void setStrGLAccountNumber(String strGLAccountNumber) {
		this.strGLAccountNumber = strGLAccountNumber;
	}
	public String getStrNubanType() {
		return strNubanType;
	}
	public void setStrNubanType(String strNubanType) {
		this.strNubanType = strNubanType;
	}
	public String getStrAccountTypeCode() {
		return strAccountTypeCode;
	}
	public void setStrAccountTypeCode(String strAccountTypeCode) {
		this.strAccountTypeCode = strAccountTypeCode;
	}
	public String getStrActiveTier() {
		return strActiveTier;
	}
	public void setStrActiveTier(String strActiveTier) {
		this.strActiveTier = strActiveTier;
	}
	public String getTier1CummBalance() {
		return tier1CummBalance;
	}
	public void setTier1CummBalance(String tier1CummBalance) {
		this.tier1CummBalance = tier1CummBalance;
	}
	public String getTier2CummBalance() {
		return tier2CummBalance;
	}
	public void setTier2CummBalance(String tier2CummBalance) {
		this.tier2CummBalance = tier2CummBalance;
	}
	public String getTier3CummBalance() {
		return tier3CummBalance;
	}
	public void setTier3CummBalance(String tier3CummBalance) {
		this.tier3CummBalance = tier3CummBalance;
	}
	public String getStrAvailableTier1DailyCumlimit() {
		return strAvailableTier1DailyCumlimit;
	}
	public void setStrAvailableTier1DailyCumlimit(String strAvailableTier1DailyCumlimit) {
		this.strAvailableTier1DailyCumlimit = strAvailableTier1DailyCumlimit;
	}
	public String getStrAvailableTier2DailyCumlimit() {
		return strAvailableTier2DailyCumlimit;
	}
	public void setStrAvailableTier2DailyCumlimit(String strAvailableTier2DailyCumlimit) {
		this.strAvailableTier2DailyCumlimit = strAvailableTier2DailyCumlimit;
	}
	public String getStrAvailableTier3DailyCumlimit() {
		return strAvailableTier3DailyCumlimit;
	}
	public void setStrAvailableTier3DailyCumlimit(String strAvailableTier3DailyCumlimit) {
		this.strAvailableTier3DailyCumlimit = strAvailableTier3DailyCumlimit;
	}
	public String getTransferAmount() {
		return transferAmount;
	}
	public void setTransferAmount(String transferAmount) {
		this.transferAmount = transferAmount;
	}
	public String getStrFromAccountNumber() {
		return strFromAccountNumber;
	}
	public void setStrFromAccountNumber(String strFromAccountNumber) {
		this.strFromAccountNumber = strFromAccountNumber;
	}
	public String getStrFromAccountType() {
		return strFromAccountType;
	}
	public void setStrFromAccountType(String strFromAccountType) {
		this.strFromAccountType = strFromAccountType;
	}
	public String getStrToAccountNumber() {
		return strToAccountNumber;
	}
	public void setStrToAccountNumber(String strToAccountNumber) {
		this.strToAccountNumber = strToAccountNumber;
	}
	public String getStrToAccountType() {
		return strToAccountType;
	}
	public void setStrToAccountType(String strToAccountType) {
		this.strToAccountType = strToAccountType;
	}
	public String getGlAccountNo() {
		return glAccountNo;
	}
	public void setGlAccountNo(String glAccountNo) {
		this.glAccountNo = glAccountNo;
	}
	public String getGlAccountType() {
		return glAccountType;
	}
	public void setGlAccountType(String glAccountType) {
		this.glAccountType = glAccountType;
	}
	public String getGlAccountBalance() {
		return glAccountBalance;
	}
	public void setGlAccountBalance(String glAccountBalance) {
		this.glAccountBalance = glAccountBalance;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getTxnNarration() {
		return txnNarration;
	}
	public void setTxnNarration(String txnNarration) {
		this.txnNarration = txnNarration;
	}
	public String getStrFromAccountTran() {
		return strFromAccountTran;
	}
	public void setStrFromAccountTran(String strFromAccountTran) {
		this.strFromAccountTran = strFromAccountTran;
	}
	public String getStrToAccountTran() {
		return strToAccountTran;
	}
	public void setStrToAccountTran(String strToAccountTran) {
		this.strToAccountTran = strToAccountTran;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getStrAvailableBalance() {
		return strAvailableBalance;
	}
	public void setStrAvailableBalance(String strAvailableBalance) {
		this.strAvailableBalance = strAvailableBalance;
	}
	public String getGlAccountDescr() {
		return glAccountDescr;
	}
	public void setGlAccountDescr(String glAccountDescr) {
		this.glAccountDescr = glAccountDescr;
	}
	public String getGlAccountAvailableBalance() {
		return glAccountAvailableBalance;
	}
	public void setGlAccountAvailableBalance(String glAccountAvailableBalance) {
		this.glAccountAvailableBalance = glAccountAvailableBalance;
	}
	public String getStrAccountHolderName() {
		return strAccountHolderName;
	}
	public void setStrAccountHolderName(String strAccountHolderName) {
		this.strAccountHolderName = strAccountHolderName;
	}
	
	@Override
	public String toString() {
		return "CustomerByAccountResponse [strAccountNumber=" + strAccountNumber + ", strAccountType=" + strAccountType
				+ ", strCustId=" + strCustId + ", strAccountHolderName=" + strAccountHolderName + ", strEarMarkAmount="
				+ strEarMarkAmount + ", strPreCredAmount=" + strPreCredAmount + ", strClosingBalance="
				+ strClosingBalance + ", strStatus=" + strStatus + ", strLoadCount=" + strLoadCount
				+ ", strTotalOutstandingBal=" + strTotalOutstandingBal + ", strAvailableGracePeriod="
				+ strAvailableGracePeriod + ", strAvailableCreditLimit=" + strAvailableCreditLimit
				+ ", strAvailableDailyLimit=" + strAvailableDailyLimit + ", strAvailableMonthlyLimit="
				+ strAvailableMonthlyLimit + ", strAvailableYearlyLimit=" + strAvailableYearlyLimit
				+ ", strAccountTypeCategory=" + strAccountTypeCategory + ", strAllowLoadCash=" + strAllowLoadCash
				+ ", strTStatus=" + strTStatus + ", strGLAccountType=" + strGLAccountType + ", strGLAccountNumber="
				+ strGLAccountNumber + ", strNubanType=" + strNubanType + ", strAccountTypeCode=" + strAccountTypeCode
				+ ", strActiveTier=" + strActiveTier + ", tier1CummBalance=" + tier1CummBalance + ", tier2CummBalance="
				+ tier2CummBalance + ", tier3CummBalance=" + tier3CummBalance + ", strAvailableTier1DailyCumlimit="
				+ strAvailableTier1DailyCumlimit + ", strAvailableTier2DailyCumlimit=" + strAvailableTier2DailyCumlimit
				+ ", strAvailableTier3DailyCumlimit=" + strAvailableTier3DailyCumlimit + ", transferAmount="
				+ transferAmount + ", strFromAccountNumber=" + strFromAccountNumber + ", strFromAccountType="
				+ strFromAccountType + ", strToAccountNumber=" + strToAccountNumber + ", strToAccountType="
				+ strToAccountType + ", glAccountBalance=" + glAccountBalance + ", glAccountAvailableBalance="
				+ glAccountAvailableBalance + ", glAccountNo=" + glAccountNo + ", glAccountType=" + glAccountType
				+ ", glAccountDescr=" + glAccountDescr + ", txnId=" + txnId + ", txnType=" + txnType + ", txnNarration="
				+ txnNarration + ", strFromAccountTran=" + strFromAccountTran + ", strToAccountTran=" + strToAccountTran
				+ ", responseCode=" + responseCode + ", strAvailableBalance=" + strAvailableBalance + "]";
	}
}
