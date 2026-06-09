package ams.cms.api.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

import ams.cms.config.TransactionConfig;
import ams.cms.config.TransactionPostingConfig;

public class InflightTransactionMaster implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Date txnDate;
	private Time txnTime;
	private String txnType;
	private String transactionId;
	private String txnAmount;
	private String fee;
	private String vat;
	private String commission;
	private String feeApplicableTo;
	
	private String fromAccountNo;
	private String toAccountNo;
	
	private String fromLinkedGLAccountNumber;
	private String toLinkedGLAccountNumber;
	
	private String vatGLAccountNumber;
	private String feeGLAccountNumber;
	private String commisonGLAccountNumber;
	
	private String glAccountNumber;
	private String ctrlGlAccountNumber;
	
	private String authCode;
	private String strSrcTxnId;
	private String responseCode;
	private String participantId;
	
	private Boolean isFeeTypeExist = false;
	
	private HashMap<String, TransactionPostingConfig> dataMap = new HashMap<String, TransactionPostingConfig>();
	
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

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public String getCommission() {
		return commission;
	}

	public void setCommission(String commission) {
		this.commission = commission;
	}

	public String getFeeApplicableTo() {
		return feeApplicableTo;
	}

	public void setFeeApplicableTo(String feeApplicableTo) {
		this.feeApplicableTo = feeApplicableTo;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
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

	public String getFromLinkedGLAccountNumber() {
		return fromLinkedGLAccountNumber;
	}

	public void setFromLinkedGLAccountNumber(String fromLinkedGLAccountNumber) {
		this.fromLinkedGLAccountNumber = fromLinkedGLAccountNumber;
	}

	public String getToLinkedGLAccountNumber() {
		return toLinkedGLAccountNumber;
	}

	public void setToLinkedGLAccountNumber(String toLinkedGLAccountNumber) {
		this.toLinkedGLAccountNumber = toLinkedGLAccountNumber;
	}

	public String getVatGLAccountNumber() {
		return vatGLAccountNumber;
	}

	public void setVatGLAccountNumber(String vatGLAccountNumber) {
		this.vatGLAccountNumber = vatGLAccountNumber;
	}

	public String getFeeGLAccountNumber() {
		return feeGLAccountNumber;
	}

	public void setFeeGLAccountNumber(String feeGLAccountNumber) {
		this.feeGLAccountNumber = feeGLAccountNumber;
	}

	public String getCommisonGLAccountNumber() {
		return commisonGLAccountNumber;
	}

	public void setCommisonGLAccountNumber(String commisonGLAccountNumber) {
		this.commisonGLAccountNumber = commisonGLAccountNumber;
	}

	public HashMap<String, TransactionPostingConfig> getDataMap() {
		return dataMap;
	}

	public void setDataMap(HashMap<String, TransactionPostingConfig> dataMap) {
		this.dataMap = dataMap;
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

	public String getGlAccountNumber() {
		return glAccountNumber;
	}

	public void setGlAccountNumber(String glAccountNumber) {
		this.glAccountNumber = glAccountNumber;
	}

	public String getCtrlGlAccountNumber() {
		return ctrlGlAccountNumber;
	}

	public void setCtrlGlAccountNumber(String ctrlGlAccountNumber) {
		this.ctrlGlAccountNumber = ctrlGlAccountNumber;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
}
