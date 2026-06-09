package ams.cms.model;

import java.io.Serializable;

public class TxnDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String terminalId;
	private String mid;
	private String amount;
	private String rrn;
	private String maskCardNo;
	private String transactionDate;
	private String response;
	private String transactionType;
	private String authId;
	private String stan;

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getMaskCardNo() {
		return maskCardNo;
	}

	public void setMaskCardNo(String maskCardNo) {
		this.maskCardNo = maskCardNo;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getStan() {
		return stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

	
}
