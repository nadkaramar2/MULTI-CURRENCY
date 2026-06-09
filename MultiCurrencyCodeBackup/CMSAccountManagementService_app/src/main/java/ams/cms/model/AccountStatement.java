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
@Table(name = "account_statement")
public class AccountStatement implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "participant_id")
	private String strParticipantId;
	
	@Column(name = "account_number")
	private String strAccountNumber;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "closing_balance")
	private String strClosingBalance;
	
	@Column(name = "is_gl_type")
	private String strIsGLType;
	
	@Column(name = "transaction_amount")
	private String strTransactionAmount;	
	
	@Column(name = "transaction_date")
	private Date transactionDate;
	
	@Column(name = "naration")
	private String strNaration;
	
	@Column(name = "transaction_id")
	private String strTransactionID;
	
	@Column(name = "txn_type")
	private String strTransactionType;
	
	@Column(name = "txn_mode")
	private String strTransactionMode;
	
	@Column(name = "entity_info")
	private String entityInfo;
	
	@Column(name = "entity_number")
	private String entityNumber;
	
	@Column(name = "currency_code")
	private String currencyCode;
	
	@Transient
	private Double transactionAmount;
	
	@Transient
	private String fromDate;
	
	@Transient
	private String toDate;
	
	@Transient
	private String strTransactionDetails;
	
	@Transient
	private String strTransactionDate;
	
	@Transient
	private String strTransactionTime;
	
	@Transient
	private String strTranMode;
	
	@Transient
	private String strAccountHolderName;
	
	@Transient
	private String strTranType;
	
	@Transient
	private String strTxnDate;
	
	@Transient
	private String strTxnTime;
	
	@Transient
	private String cid;
	
	@Transient
	private String strFromDate;
	
	@Transient
	private String strToDate;
	
	@Transient
	private String montraTxnId;
	
	@Transient
	private String pageSize; //LIMIT
	
	@Transient
	private String pageNumber; //This is for OFFSET value
	
	@Transient
	private String strTotalTransactionAmount;
	
	@Transient
	private String totalTransModeCount;
	
	@Transient
	private String strResponseCode;
	
	@Transient
	private String strReservefield2;
	
	@Transient
	private String strSrcTxnId;
	
	@Transient
	private String transactionID;

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getStrSrcTxnId() {
		return strSrcTxnId;
	}

	public void setStrSrcTxnId(String strSrcTxnId) {
		this.strSrcTxnId = strSrcTxnId;
	}

	public String getStrResponseCode() {
		return strResponseCode;
	}

	public void setStrResponseCode(String strResponseCode) {
		this.strResponseCode = strResponseCode;
	}

	public String getStrReservefield2() {
		return strReservefield2;
	}

	public void setStrReservefield2(String strReservefield2) {
		this.strReservefield2 = strReservefield2;
	}

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getStrParticipantId() {
		return strParticipantId;
	}

	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}

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

	public String getStrClosingBalance() {
		return strClosingBalance;
	}

	public void setStrClosingBalance(String strClosingBalance) {
		this.strClosingBalance = strClosingBalance;
	}

	public String getStrIsGLType() {
		return strIsGLType;
	}

	public void setStrIsGLType(String strIsGLType) {
		this.strIsGLType = strIsGLType;
	}

	public String getStrTransactionAmount() {
		return strTransactionAmount;
	}

	public void setStrTransactionAmount(String strTransactionAmount) {
		this.strTransactionAmount = strTransactionAmount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getStrNaration() {
		return strNaration;
	}

	public void setStrNaration(String strNaration) {
		this.strNaration = strNaration;
	}

	public String getStrTransactionID() {
		return strTransactionID;
	}

	public void setStrTransactionID(String strTransactionID) {
		this.strTransactionID = strTransactionID;
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

	public String getStrTransactionDetails() {
		return strTransactionDetails;
	}

	public void setStrTransactionDetails(String strTransactionDetails) {
		this.strTransactionDetails = strTransactionDetails;
	}

	public String getStrTransactionDate() {
		return strTransactionDate;
	}

	public void setStrTransactionDate(String strTransactionDate) {
		this.strTransactionDate = strTransactionDate;
	}

	public String getStrTranMode() {
		return strTranMode;
	}

	public void setStrTranMode(String strTranMode) {
		this.strTranMode = strTranMode;
	}

	public String getStrAccountHolderName() {
		return strAccountHolderName;
	}

	public void setStrAccountHolderName(String strAccountHolderName) {
		this.strAccountHolderName = strAccountHolderName;
	}

	public String getStrTranType() {
		return strTranType;
	}

	public void setStrTranType(String strTranType) {
		this.strTranType = strTranType;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

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

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getStrFromDate() {
		return strFromDate;
	}

	public void setStrFromDate(String strFromDate) {
		this.strFromDate = strFromDate;
	}

	public String getStrToDate() {
		return strToDate;
	}

	public void setStrToDate(String strToDate) {
		this.strToDate = strToDate;
	}

	public String getStrTransactionTime() {
		return strTransactionTime;
	}

	public void setStrTransactionTime(String strTransactionTime) {
		this.strTransactionTime = strTransactionTime;
	}

	public String getMontraTxnId() {
		return montraTxnId;
	}

	public void setMontraTxnId(String montraTxnId) {
		this.montraTxnId = montraTxnId;
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

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getStrTotalTransactionAmount() {
		return strTotalTransactionAmount;
	}

	public void setStrTotalTransactionAmount(String strTotalTransactionAmount) {
		this.strTotalTransactionAmount = strTotalTransactionAmount;
	}

	public String getTotalTransModeCount() {
		return totalTransModeCount;
	}

	public void setTotalTransModeCount(String totalTransModeCount) {
		this.totalTransModeCount = totalTransModeCount;
	}

	@Override
	public String toString() {
		return "AccountStatement [strParticipantId=" + strParticipantId + ", strAccountNumber=" + strAccountNumber
				+ ", strClosingBalance=" + strClosingBalance + ", strTransactionAmount=" + strTransactionAmount
				+ ", transactionDate=" + transactionDate + ", strTransactionID=" + strTransactionID + ", strTranType="
				+ strTranType + "]";
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
}
