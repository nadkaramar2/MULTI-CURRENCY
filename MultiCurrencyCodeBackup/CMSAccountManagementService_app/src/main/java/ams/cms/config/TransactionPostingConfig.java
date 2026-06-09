package ams.cms.config;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.TierAccountResponse;
import ams.cms.model.GLAccountTypeMaster;

public class TransactionPostingConfig implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String txnId;
	private String txnAmount;
	
	private String txnType;
	private Date txnDate;
	private Time txnTime;	
	
	private String feeApplicableTo;
	private String feeType;
	private String vat;
	private String fee;
	private String commission;
	private String senderAccountCalculate;
	
	private String closingBalance;
	
	//private AccountCreation fromAccount;
	//private AccountCreation toAccount;
	
	private AccountResponse fromAccount;
	private AccountResponse toAccount;
	
	//private TierAccountMaster fromTierAccountMaster;
	//private TierAccountMaster toTierAccountMaster;
	private TierAccountResponse fromTierAccountMaster;
	private TierAccountResponse toTierAccountMaster;
	
	private GLAccountTypeMaster fromLinkGLAccount;
	private GLAccountTypeMaster toLinkGLAccount;
	
	private GLAccountTypeMaster feeGLAccount;
	private GLAccountTypeMaster vatGLAccount;
	private GLAccountTypeMaster commissionGLAccount;
	
	private GLAccountTypeMaster glAccountTypeMaster;
	private GLAccountTypeMaster ctrlGlAccountTypeMaster;
	
	private String authCode;
	private String strSrcTxnId;
	private String responseCode;
	private Boolean isFeeTypeExist = false;
	
	private String fromAccountNo;
	private String toAccountNo;
	private String accountNo;
	
	private String strReservefield1;
	private String strReservefield2;
	private String strReservefield3;
	
	private String accountHolderName;
	private String emailId;
	private String participantId;

	private HashMap<String, String> linkedGLMap = new HashMap<String, String>();
	
	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
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

	public String getCommission() {
		return commission;
	}

	public void setCommission(String commission) {
		this.commission = commission;
	}

	public String getSenderAccountCalculate() {
		return senderAccountCalculate;
	}

	public void setSenderAccountCalculate(String senderAccountCalculate) {
		this.senderAccountCalculate = senderAccountCalculate;
	}

	public AccountResponse getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(AccountResponse fromAccount) {
		this.fromAccount = fromAccount;
	}

	public AccountResponse getToAccount() {
		return toAccount;
	}

	public void setToAccount(AccountResponse toAccount) {
		this.toAccount = toAccount;
	}

	public GLAccountTypeMaster getFromLinkGLAccount() {
		return fromLinkGLAccount;
	}

	public void setFromLinkGLAccount(GLAccountTypeMaster fromLinkGLAccount) {
		this.fromLinkGLAccount = fromLinkGLAccount;
	}

	public GLAccountTypeMaster getToLinkGLAccount() {
		return toLinkGLAccount;
	}

	public void setToLinkGLAccount(GLAccountTypeMaster toLinkGLAccount) {
		this.toLinkGLAccount = toLinkGLAccount;
	}

	public GLAccountTypeMaster getFeeGLAccount() {
		return feeGLAccount;
	}

	public void setFeeGLAccount(GLAccountTypeMaster feeGLAccount) {
		this.feeGLAccount = feeGLAccount;
	}

	public GLAccountTypeMaster getVatGLAccount() {
		return vatGLAccount;
	}

	public void setVatGLAccount(GLAccountTypeMaster vatGLAccount) {
		this.vatGLAccount = vatGLAccount;
	}

	public GLAccountTypeMaster getCommissionGLAccount() {
		return commissionGLAccount;
	}

	public void setCommissionGLAccount(GLAccountTypeMaster commissionGLAccount) {
		this.commissionGLAccount = commissionGLAccount;
	}

	public GLAccountTypeMaster getGlAccountTypeMaster() {
		return glAccountTypeMaster;
	}

	public void setGlAccountTypeMaster(GLAccountTypeMaster glAccountTypeMaster) {
		this.glAccountTypeMaster = glAccountTypeMaster;
	}

	public String getFeeApplicableTo() {
		return feeApplicableTo;
	}

	public void setFeeApplicableTo(String feeApplicableTo) {
		this.feeApplicableTo = feeApplicableTo;
	}

	public Date getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}

	public Time getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(Time txnTime) {
		this.txnTime = txnTime;
	}

	public TierAccountResponse getFromTierAccountMaster() {
		return fromTierAccountMaster;
	}

	public void setFromTierAccountMaster(TierAccountResponse fromTierAccountMaster) {
		this.fromTierAccountMaster = fromTierAccountMaster;
	}

	public TierAccountResponse getToTierAccountMaster() {
		return toTierAccountMaster;
	}

	public void setToTierAccountMaster(TierAccountResponse toTierAccountMaster) {
		this.toTierAccountMaster = toTierAccountMaster;
	}

	public String getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(String closingBalance) {
		this.closingBalance = closingBalance;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getStrSrcTxnId() {
		return strSrcTxnId;
	}

	public void setStrSrcTxnId(String strSrcTxnId) {
		this.strSrcTxnId = strSrcTxnId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public Boolean getIsFeeTypeExist() {
		return isFeeTypeExist;
	}

	public void setIsFeeTypeExist(Boolean isFeeTypeExist) {
		this.isFeeTypeExist = isFeeTypeExist;
	}

	public HashMap<String, String> getLinkedGLMap() {
		return linkedGLMap;
	}

	public void setLinkedGLMap(HashMap<String, String> linkedGLMap) {
		this.linkedGLMap = linkedGLMap;
	}

	public GLAccountTypeMaster getCtrlGlAccountTypeMaster() {
		return ctrlGlAccountTypeMaster;
	}

	public void setCtrlGlAccountTypeMaster(GLAccountTypeMaster ctrlGlAccountTypeMaster) {
		this.ctrlGlAccountTypeMaster = ctrlGlAccountTypeMaster;
	}

	public String getFromAccountNo() {
		return fromAccountNo;
	}

	public void setFromAccountNo(String fromAccountNo) {
		this.fromAccountNo = fromAccountNo;
	}

	public String getToAccountNo() {
		return toAccountNo;
	}

	public void setToAccountNo(String toAccountNo) {
		this.toAccountNo = toAccountNo;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getStrReservefield1() {
		return strReservefield1;
	}

	public void setStrReservefield1(String strReservefield1) {
		this.strReservefield1 = strReservefield1;
	}

	public String getStrReservefield2() {
		return strReservefield2;
	}

	public void setStrReservefield2(String strReservefield2) {
		this.strReservefield2 = strReservefield2;
	}

	public String getStrReservefield3() {
		return strReservefield3;
	}

	public void setStrReservefield3(String strReservefield3) {
		this.strReservefield3 = strReservefield3;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
}
