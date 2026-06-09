package ams.cms.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ams.cms.config.TransactionConfig;

@Entity
@Table(name = "journal_transfer")
public class JournalTransfer implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int strId;
	
	@Column(name = "txn_id")
	private String strTxnId;
	
	@Column(name = "from_account_type")
	private String strFromAccountType;
	
	@Column(name = "from_account_number")
	private String strFromAccountNumber;
	
	@Column(name = "from_account_name")
	private String strFromAccountName;
	
	@Column(name = "to_account_type")
	private String strToAccountType;
	
	@Column(name = "to_account_number")
	private String strToAccountNumber;
	
	@Column(name = "to_account_name")
	private String strToAccountName;
	
	@Column(name = "amout_to_transfer")
	private Double strAmoutToTransfer;
	
	@Column(name = "narration")
	private String strNarration;
	
	@Column(name = "txn_status")
	private String strTxnStatus;
	
	@Column(name = "txn_date")
	private Date strTxnDate;
	
	@Column(name = "txn_time")
	private Time strTxnTime;
	
	@Column(name = "reject_reason")
	private String strRejectReason;
	
	@Column(name = "maker_id")
	private String strMakerId;
	
	@Column(name = "checker_id")
	private String strCheckerId;
	
	@Column(name="txn_journal_transfer_type")
	private String txnJournalTransferType;
	
	@Transient
	private String responseCode;
	
	@Transient
	private String tranType;
	
	@Transient
	TransactionConfig transactionConfig;
	
	@Transient
	private String strIsLinkedGLAccount;
	
	@Transient
	private String responseMessage;
	
	@Transient
	private String loginUserCode;	
	
	@Transient
	private String fromDate;
	
	@Transient
	private String toDate;
	
	@Transient
	private String strTxnDte;
	
	
	@Transient
	private String fromLinkedGlType;
	
	@Transient
	private String toLinkedGlType;
	
	public int getStrId() {
		return strId;
	}
	public void setStrId(int strId) {
		this.strId = strId;
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
	public Double getStrAmoutToTransfer() {
		return strAmoutToTransfer;
	}
	public void setStrAmoutToTransfer(Double strAmoutToTransfer) {
		this.strAmoutToTransfer = strAmoutToTransfer;
	}
	public String getStrMakerId() {
		return strMakerId;
	}
	public void setStrMakerId(String strMakerId) {
		this.strMakerId = strMakerId;
	}
	public String getStrCheckerId() {
		return strCheckerId;
	}
	public void setStrCheckerId(String strCheckerId) {
		this.strCheckerId = strCheckerId;
	}
	public String getStrNarration() {
		return strNarration;
	}
	public void setStrNarration(String strNarration) {
		this.strNarration = strNarration;
	}
	public String getStrTxnStatus() {
		return strTxnStatus;
	}
	public void setStrTxnStatus(String strTxnStatus) {
		this.strTxnStatus = strTxnStatus;
	}
	public Date getStrTxnDate() {
		return strTxnDate;
	}
	public void setStrTxnDate(Date strTxnDate) {
		this.strTxnDate = strTxnDate;
	}
	public Time getStrTxnTime() {
		return strTxnTime;
	}
	public void setStrTxnTime(Time strTxnTime) {
		this.strTxnTime = strTxnTime;
	}
	public String getStrRejectReason() {
		return strRejectReason;
	}
	public void setStrRejectReason(String strRejectReason) {
		this.strRejectReason = strRejectReason;
	}
	public String getTxnJournalTransferType() {
		return txnJournalTransferType;
	}
	public void setTxnJournalTransferType(String txnJournalTransferType) {
		this.txnJournalTransferType = txnJournalTransferType;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	public TransactionConfig getTransactionConfig() {
		return transactionConfig;
	}
	public void setTransactionConfig(TransactionConfig transactionConfig) {
		this.transactionConfig = transactionConfig;
	}
	public String getStrIsLinkedGLAccount() {
		return strIsLinkedGLAccount;
	}
	public void setStrIsLinkedGLAccount(String strIsLinkedGLAccount) {
		this.strIsLinkedGLAccount = strIsLinkedGLAccount;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getLoginUserCode() {
		return loginUserCode;
	}
	public void setLoginUserCode(String loginUserCode) {
		this.loginUserCode = loginUserCode;
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
	public String getStrTxnDte() {
		return strTxnDte;
	}
	public void setStrTxnDte(String strTxnDte) {
		this.strTxnDte = strTxnDte;
	}
	
	public String getFromLinkedGlType() {
		return fromLinkedGlType;
	}
	public void setFromLinkedGlType(String fromLinkedGlType) {
		this.fromLinkedGlType = fromLinkedGlType;
	}
	public String getToLinkedGlType() {
		return toLinkedGlType;
	}
	public void setToLinkedGlType(String toLinkedGlType) {
		this.toLinkedGlType = toLinkedGlType;
	}
	@Override
	public String toString() {
		return "JournalTransfer [strId=" + strId + ", strTxnId=" + strTxnId + ", strFromAccountType="
				+ strFromAccountType + ", strFromAccountNumber=" + strFromAccountNumber + ", strFromAccountName="
				+ strFromAccountName + ", strToAccountType=" + strToAccountType + ", strToAccountNumber="
				+ strToAccountNumber + ", strToAccountName=" + strToAccountName + ", strAmoutToTransfer="
				+ strAmoutToTransfer + ", strNarration=" + strNarration + ", strTxnStatus=" + strTxnStatus
				+ ", strTxnDate=" + strTxnDate + ", strTxnTime=" + strTxnTime + ", strRejectReason=" + strRejectReason
				+ ", strMakerId=" + strMakerId + ", strCheckerId=" + strCheckerId + ", txnJournalTransferType="
				+ txnJournalTransferType + ", responseCode=" + responseCode + ", tranType=" + tranType
				+ ", transactionConfig=" + transactionConfig + ", strIsLinkedGLAccount=" + strIsLinkedGLAccount
				+ ", responseMessage=" + responseMessage + ", loginUserCode=" + loginUserCode + ", fromDate=" + fromDate
				+ ", toDate=" + toDate + ", strTxnDte=" + strTxnDte + "]";
	}
}