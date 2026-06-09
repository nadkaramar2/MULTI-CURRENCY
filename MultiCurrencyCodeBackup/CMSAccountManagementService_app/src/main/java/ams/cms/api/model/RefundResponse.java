package ams.cms.api.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RefundResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	//@JsonFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Kolkata")
	private Date strDateOfTransaction;
	private Time strTimeOfTransaction;
	private String strTransactionType;
	private String strTxnId;
	private String strTxnAmount;
	private String strNarration;
	private String strFromAccountName;
	private String strFromAccountCustID;
	private String strFromAccountType;
	private String strFromAccountNumber;
	private String strToAccountType;
	private String strToAccountNumber;
	private String strToAccountName;
	private String strToAccountCustID;
	
	
	
	public Date getStrDateOfTransaction() {
		return strDateOfTransaction;
	}
	public void setStrDateOfTransaction(Date strDateOfTransaction) {
		this.strDateOfTransaction = strDateOfTransaction;
	}
	public Time getStrTimeOfTransaction() {
		return strTimeOfTransaction;
	}
	public void setStrTimeOfTransaction(Time strTimeOfTransaction) {
		this.strTimeOfTransaction = strTimeOfTransaction;
	}
	public String getStrTransactionType() {
		return strTransactionType;
	}
	public void setStrTransactionType(String strTransactionType) {
		this.strTransactionType = strTransactionType;
	}
	public String getStrFromAccountCustID() {
		return strFromAccountCustID;
	}
	public void setStrFromAccountCustID(String strFromAccountCustID) {
		this.strFromAccountCustID = strFromAccountCustID;
	}
	public String getStrTxnId() {
		return strTxnId;
	}
	public void setStrTxnId(String strTxnId) {
		this.strTxnId = strTxnId;
	}
	public String getStrFromAccountType() {
		return strFromAccountType;
	}
	public void setStrFromAccountType(String strFromAccountType) {
		this.strFromAccountType = strFromAccountType;
	}
	public String getStrFromAccountNumber() {
		return strFromAccountNumber;
	}
	public void setStrFromAccountNumber(String strFromAccountNumber) {
		this.strFromAccountNumber = strFromAccountNumber;
	}
	public String getStrFromAccountName() {
		return strFromAccountName;
	}
	public void setStrFromAccountName(String strFromAccountName) {
		this.strFromAccountName = strFromAccountName;
	}
	public String getStrTxnAmount() {
		return strTxnAmount;
	}
	public void setStrTxnAmount(String strTxnAmount) {
		this.strTxnAmount = strTxnAmount;
	}
	public String getStrNarration() {
		return strNarration;
	}
	public void setStrNarration(String strNarration) {
		this.strNarration = strNarration;
	}
	public String getStrToAccountType() {
		return strToAccountType;
	}
	public void setStrToAccountType(String strToAccountType) {
		this.strToAccountType = strToAccountType;
	}
	public String getStrToAccountNumber() {
		return strToAccountNumber;
	}
	public void setStrToAccountNumber(String strToAccountNumber) {
		this.strToAccountNumber = strToAccountNumber;
	}
	public String getStrToAccountName() {
		return strToAccountName;
	}
	public void setStrToAccountName(String strToAccountName) {
		this.strToAccountName = strToAccountName;
	}
	public String getStrToAccountCustID() {
		return strToAccountCustID;
	}
	public void setStrToAccountCustID(String strToAccountCustID) {
		this.strToAccountCustID = strToAccountCustID;
	}
}
