package ams.cms.model;

import java.io.Serializable;

public class TransactionDetails implements Serializable{
	
	private static final long serialVersionUID = 1L; 
	
	private String strAccountType;
	private String strClosingBalance;
	private String strTransactionAmount;
	private String strTransactionDate;
	private String strTransactionID;
	private String montraTxnId;
	private String strTransactionType;
	private String strTransactionMode;
	private String strIsFeeTxn;
	private String StrIsVatTxn;
	private String StrIstTxn;
	public String getStrAccountType() {
		return strAccountType;
	}
	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}
	public String getStrClosingBalance() {
		return strClosingBalance;
	}
	public void setStrClosingBalance(String strClosingBalance) {
		this.strClosingBalance = strClosingBalance;
	}
	public String getStrTransactionAmount() {
		return strTransactionAmount;
	}
	public void setStrTransactionAmount(String strTransactionAmount) {
		this.strTransactionAmount = strTransactionAmount;
	}
	public String getStrTransactionDate() {
		return strTransactionDate;
	}
	public void setStrTransactionDate(String strTransactionDate) {
		this.strTransactionDate = strTransactionDate;
	}
	public String getStrTransactionID() {
		return strTransactionID;
	}
	public void setStrTransactionID(String strTransactionID) {
		this.strTransactionID = strTransactionID;
	}
	public String getMontraTxnId() {
		return montraTxnId;
	}
	public void setMontraTxnId(String montraTxnId) {
		this.montraTxnId = montraTxnId;
	}
	public String getStrTransactionType() {
		return strTransactionType;
	}
	public void setStrTransactionType(String strTransactionType) {
		this.strTransactionType = strTransactionType;
	}
	public String getStrTransactionMode() {
		return strTransactionMode;
	}
	public void setStrTransactionMode(String strTransactionMode) {
		this.strTransactionMode = strTransactionMode;
	}
	public String getStrIsFeeTxn() {
		return strIsFeeTxn;
	}
	public void setStrIsFeeTxn(String strIsFeeTxn) {
		this.strIsFeeTxn = strIsFeeTxn;
	}
	public String getStrIsVatTxn() {
		return StrIsVatTxn;
	}
	public void setStrIsVatTxn(String strIsVatTxn) {
		StrIsVatTxn = strIsVatTxn;
	}
	public String getStrIstTxn() {
		return StrIstTxn;
	}
	public void setStrIstTxn(String strIstTxn) {
		StrIstTxn = strIstTxn;
	}
}
