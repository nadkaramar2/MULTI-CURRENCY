package ams.cms.api.model;

import java.io.Serializable;

public class UserTransactionModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String userCustId;
	private String userAccountType;
	private String userAccountNumber;
	private String tranType;
	private String reqPIN;
	private String userTxnAmount;
	private String reqOTP;
	private String fromAccountType;
	private String fromAccountNo;
	private String toAccountType;
	private String toAccountNumber;
	
	private String accountStatus;	
	private String strStatus;	
	private String currentAvailableBalance;
	private String strAccountTypeCategory;
	private String strAccountHolderName;
	private String strMobileNo;
	private String strIsWithdrawAllowAtAgent;	
	private String strIsDepositAllowAtAgent;
	
	private String fromGLAccountType;	
	private String ToGLAccountType;	
	private String fromAccountBalance;	
	private String toAccountBalance;	
	private String fromAvailableDailyLimit;	
	private String toAvailableDailyLimit;	
	private String fromAvailableMonthlyLimit;	
	private String toAvailableMonthlyLimit;	
	private String fromAvailableYearlyLimit;	
	private String toAvailableYearlyLimit;
	
	private DenominationMaster denominationData;
	private String strToAccountHolderName;
	
	private String cid;
	private String strCustId;
	private String montraTxnId;
	private String toAccountNo;
	private String strTran_type;
	private String secretCode;
	
	public String getUserCustId() {
		return userCustId;
	}

	public void setUserCustId(String userCustId) {
		this.userCustId = userCustId;
	}

	public String getUserAccountType() {
		return userAccountType;
	}

	public void setUserAccountType(String userAccountType) {
		this.userAccountType = userAccountType;
	}

	public String getUserAccountNumber() {
		return userAccountNumber;
	}

	public void setUserAccountNumber(String userAccountNumber) {
		this.userAccountNumber = userAccountNumber;
	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public String getReqPIN() {
		return reqPIN;
	}

	public void setReqPIN(String reqPIN) {
		this.reqPIN = reqPIN;
	}

	public String getUserTxnAmount() {
		return userTxnAmount;
	}

	public void setUserTxnAmount(String userTxnAmount) {
		this.userTxnAmount = userTxnAmount;
	}

	public String getReqOTP() {
		return reqOTP;
	}

	public void setReqOTP(String reqOTP) {
		this.reqOTP = reqOTP;
	}

	public String getFromAccountType() {
		return fromAccountType;
	}

	public void setFromAccountType(String fromAccountType) {
		this.fromAccountType = fromAccountType;
	}

	public String getFromAccountNo() {
		return fromAccountNo;
	}

	public void setFromAccountNo(String fromAccountNo) {
		this.fromAccountNo = fromAccountNo;
	}

	public String getToAccountType() {
		return toAccountType;
	}

	public void setToAccountType(String toAccountType) {
		this.toAccountType = toAccountType;
	}

	public String getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}
	public String getStrStatus() {
		return strStatus;
	}

	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getCurrentAvailableBalance() {
		return currentAvailableBalance;
	}

	public void setCurrentAvailableBalance(String currentAvailableBalance) {
		this.currentAvailableBalance = currentAvailableBalance;
	}

	public String getStrAccountHolderName() {
		return strAccountHolderName;
	}

	public void setStrAccountHolderName(String strAccountHolderName) {
		this.strAccountHolderName = strAccountHolderName;
	}

	public String getStrAccountTypeCategory() {
		return strAccountTypeCategory;
	}

	public void setStrAccountTypeCategory(String strAccountTypeCategory) {
		this.strAccountTypeCategory = strAccountTypeCategory;
	}

	public String getStrMobileNo() {
		return strMobileNo;
	}

	public void setStrMobileNo(String strMobileNo) {
		this.strMobileNo = strMobileNo;
	}

	public String getStrIsWithdrawAllowAtAgent() {
		return strIsWithdrawAllowAtAgent;
	}

	public void setStrIsWithdrawAllowAtAgent(String strIsWithdrawAllowAtAgent) {
		this.strIsWithdrawAllowAtAgent = strIsWithdrawAllowAtAgent;
	}

	public String getStrIsDepositAllowAtAgent() {
		return strIsDepositAllowAtAgent;
	}

	public void setStrIsDepositAllowAtAgent(String strIsDepositAllowAtAgent) {
		this.strIsDepositAllowAtAgent = strIsDepositAllowAtAgent;
	}

	public String getFromGLAccountType() {
		return fromGLAccountType;
	}

	public void setFromGLAccountType(String fromGLAccountType) {
		this.fromGLAccountType = fromGLAccountType;
	}

	public String getToGLAccountType() {
		return ToGLAccountType;
	}

	public void setToGLAccountType(String toGLAccountType) {
		ToGLAccountType = toGLAccountType;
	}

	public String getFromAccountBalance() {
		return fromAccountBalance;
	}

	public void setFromAccountBalance(String fromAccountBalance) {
		this.fromAccountBalance = fromAccountBalance;
	}

	public String getToAccountBalance() {
		return toAccountBalance;
	}

	public void setToAccountBalance(String toAccountBalance) {
		this.toAccountBalance = toAccountBalance;
	}

	public String getFromAvailableDailyLimit() {
		return fromAvailableDailyLimit;
	}

	public void setFromAvailableDailyLimit(String fromAvailableDailyLimit) {
		this.fromAvailableDailyLimit = fromAvailableDailyLimit;
	}

	public String getToAvailableDailyLimit() {
		return toAvailableDailyLimit;
	}

	public void setToAvailableDailyLimit(String toAvailableDailyLimit) {
		this.toAvailableDailyLimit = toAvailableDailyLimit;
	}

	public String getFromAvailableMonthlyLimit() {
		return fromAvailableMonthlyLimit;
	}

	public void setFromAvailableMonthlyLimit(String fromAvailableMonthlyLimit) {
		this.fromAvailableMonthlyLimit = fromAvailableMonthlyLimit;
	}

	public String getToAvailableMonthlyLimit() {
		return toAvailableMonthlyLimit;
	}

	public void setToAvailableMonthlyLimit(String toAvailableMonthlyLimit) {
		this.toAvailableMonthlyLimit = toAvailableMonthlyLimit;
	}

	public String getFromAvailableYearlyLimit() {
		return fromAvailableYearlyLimit;
	}

	public void setFromAvailableYearlyLimit(String fromAvailableYearlyLimit) {
		this.fromAvailableYearlyLimit = fromAvailableYearlyLimit;
	}

	public String getToAvailableYearlyLimit() {
		return toAvailableYearlyLimit;
	}

	public void setToAvailableYearlyLimit(String toAvailableYearlyLimit) {
		this.toAvailableYearlyLimit = toAvailableYearlyLimit;
	}

	public DenominationMaster getDenominationData() {
		return denominationData;
	}

	public void setDenominationData(DenominationMaster denominationData) {
		this.denominationData = denominationData;
	}

	public String getStrToAccountHolderName() {
		return strToAccountHolderName;
	}

	public void setStrToAccountHolderName(String strToAccountHolderName) {
		this.strToAccountHolderName = strToAccountHolderName;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getStrCustId() {
		return strCustId;
	}

	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}

	public String getMontraTxnId() {
		return montraTxnId;
	}

	public void setMontraTxnId(String montraTxnId) {
		this.montraTxnId = montraTxnId;
	}

	public String getToAccountNo() {
		return toAccountNo;
	}

	public void setToAccountNo(String toAccountNo) {
		this.toAccountNo = toAccountNo;
	}

	public String getStrTran_type() {
		return strTran_type;
	}

	public void setStrTran_type(String strTran_type) {
		this.strTran_type = strTran_type;
	}

	public String getSecretCode() {
		return secretCode;
	}

	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}
}
