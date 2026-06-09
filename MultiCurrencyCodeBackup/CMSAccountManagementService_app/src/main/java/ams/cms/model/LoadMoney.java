package ams.cms.model;

import java.io.Serializable;

public class LoadMoney implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String  cid;
	private String  bid;
	private String  strCustId;
	private String  montraTxnId;
	
	private String  receipentAccountNo;
	private String  senderAccountNo;
	private String  txnAmt;
	private String  txnType;
	private String  feeType;
	private String  feeApplicableTo;
	private String  fee;
	private String  vat;
	
	private String entityInfo;
	private String entityNumber;
	private String secretCode;
	
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
	public String getReceipentAccountNo() {
		return receipentAccountNo;
	}
	public void setReceipentAccountNo(String receipentAccountNo) {
		this.receipentAccountNo = receipentAccountNo;
	}
	public String getTxnAmt() {
		return txnAmt;
	}
	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
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
	public String getSenderAccountNo() {
		return senderAccountNo;
	}
	public void setSenderAccountNo(String senderAccountNo) {
		this.senderAccountNo = senderAccountNo;
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
	public String getSecretCode() {
		return secretCode;
	}
	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}
}
