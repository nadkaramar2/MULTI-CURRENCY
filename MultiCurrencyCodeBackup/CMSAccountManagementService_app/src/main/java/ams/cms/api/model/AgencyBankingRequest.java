package ams.cms.api.model;

import java.io.Serializable;

public class AgencyBankingRequest implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String participantId;
	private String cid;
	private String bid;
	private String strCustId;
	private String montraTxnId;
	private String agentAccountNo;
	
	private String customerFromAccountNo;
	private String agentToAccountNo;
	
	private String receipentAccountNo;
	private String txnAmount;
	private String txnType;
	
	private String secretCode;
	
	private String feeType;
	private String feeApplicableTo;
	private String fee;
	private String vat;
	
	private String amsTransactionId;
	private String confirmTxn;
	
	private String entityInfo;
	private String entityNumber;

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
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

	public String getAgentAccountNo() {
		return agentAccountNo;
	}

	public void setAgentAccountNo(String agentAccountNo) {
		this.agentAccountNo = agentAccountNo;
	}

	public String getReceipentAccountNo() {
		return receipentAccountNo;
	}

	public void setReceipentAccountNo(String receipentAccountNo) {
		this.receipentAccountNo = receipentAccountNo;
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

	public String getSecretCode() {
		return secretCode;
	}

	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getFeeApplicableTo() {
		return feeApplicableTo;
	}

	public void setFeeApplicableTo(String feeApplicableTo) {
		this.feeApplicableTo = feeApplicableTo;
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

	public String getAmsTransactionId() {
		return amsTransactionId;
	}

	public void setAmsTransactionId(String amsTransactionId) {
		this.amsTransactionId = amsTransactionId;
	}

	public String getConfirmTxn() {
		return confirmTxn;
	}

	public void setConfirmTxn(String confirmTxn) {
		this.confirmTxn = confirmTxn;
	}

	public String getCustomerFromAccountNo() {
		return customerFromAccountNo;
	}

	public void setCustomerFromAccountNo(String customerFromAccountNo) {
		this.customerFromAccountNo = customerFromAccountNo;
	}

	public String getAgentToAccountNo() {
		return agentToAccountNo;
	}

	public void setAgentToAccountNo(String agentToAccountNo) {
		this.agentToAccountNo = agentToAccountNo;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
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
