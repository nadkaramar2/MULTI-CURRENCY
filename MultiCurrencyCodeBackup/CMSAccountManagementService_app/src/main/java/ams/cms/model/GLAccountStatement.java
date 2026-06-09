package ams.cms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name = "gl_account_statement")
public class GLAccountStatement implements Serializable 
{
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "account_type")
	private String strGLAccountType;
	
	@Column(name = "account_number")
	private String strAccountNumber;
	
	@Column(name = "Ref")
	private String strRef;
	
	@Column(name = "tran_id")
	private String strTxnId;
	
	@Column(name = "amount")
	private String strAmount;
	
	@Column(name = "closing_balance")
	private String strClosingBalance;
	
	@Column(name = "transaction_date")
	private Date transactionDate;
	
	@Column(name = "tran_type")
	private String strTranType;
	
	@Column(name = "tran_mode")
	private String strTranMode;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private String strCreated_by;
	
	@Column(name = "currency_code")
	private String currencyCode;
	
	@Transient
	private String fromDate;
	
	@Transient
	private String toDate;
	
	@Transient
	private String strTransactionDate;
	
	@Transient
	private String strTxnDate;
	
	@Transient
	private String strTxnTime;
	
	@Transient
	private double feeAmount;
	
	@Transient
	private String feeGl;
	
	@Transient
	private String feeGLAccountNumber;
	
	@Transient
	private String gstType;
	
	@Transient
	private String gstGL;
	
	@Transient
	private String gstAccountNumber;
	
	@Transient
	private double gstPercentage;
	
	@Transient
	private String channelAccountNumber;

	public String getStrTxnDate() {
		return strTxnDate;
	}

	public void setStrTxnDate(String strTxnDate) {
		this.strTxnDate = strTxnDate;
	}

	public String getStrTxnTime() {
		return strTxnTime;
	}

	public void setStrTxnTime(String strTxnTime) {
		this.strTxnTime = strTxnTime;
	}

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getStrGLAccountType() {
		return strGLAccountType;
	}

	public void setStrGLAccountType(String strGLAccountType) {
		this.strGLAccountType = strGLAccountType;
	}

	public String getStrAccountNumber() {
		return strAccountNumber;
	}

	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}

	public String getStrRef() {
		return strRef;
	}

	public void setStrRef(String strRef) {
		this.strRef = strRef;
	}

	public String getStrTxnId() {
		return strTxnId;
	}

	public void setStrTxnId(String strTxnId) {
		this.strTxnId = strTxnId;
	}

	public String getStrAmount() {
		return strAmount;
	}

	public void setStrAmount(String strAmount) {
		this.strAmount = strAmount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getStrTranType() {
		return strTranType;
	}

	public void setStrTranType(String strTranType) {
		this.strTranType = strTranType;
	}

	public String getStrTranMode() {
		return strTranMode;
	}

	public void setStrTranMode(String strTranMode) {
		this.strTranMode = strTranMode;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getStrCreated_by() {
		return strCreated_by;
	}

	public void setStrCreated_by(String strCreated_by) {
		this.strCreated_by = strCreated_by;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getStrClosingBalance() {
		return strClosingBalance;
	}

	public void setStrClosingBalance(String strClosingBalance) {
		this.strClosingBalance = strClosingBalance;
	}

	public String getStrTransactionDate() {
		return strTransactionDate;
	}

	public void setStrTransactionDate(String strTransactionDate) {
		this.strTransactionDate = strTransactionDate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
}
