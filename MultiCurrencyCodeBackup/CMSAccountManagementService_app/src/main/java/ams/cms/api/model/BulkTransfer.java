package ams.cms.api.model;

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

@Entity
@Table(name = "bulk_transfer")
public class BulkTransfer implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int strId;
	
	@Column(name= "pre_transaction_id")
	private String strPreTransactionId;
	
	@Column(name = "transaction_id")
	private String strTransactionId;
	
	@Column(name = "bulk_request_date")
	private Date strBulkRequestDate;
	
	@Column(name = "bulk_request_time")
	private Time strBulkRequestTime;
	
	@Column(name= "from_account_no")
	private String strFromAccountNo;
	
	@Column(name= "from_account_type")
	private String strFromAccountType;
	
	@Column(name= "from_account_name")
	private String strFromAccountName;
	
	@Column(name= "to_account_no")
	private String strToAccountNo;
	
	@Column(name= "to_account_type")
	private String strToAccountType;
	
	@Column(name= "to_account_name")
	private String strToAccountName;
	
	@Column(name= "bulk_response_date")
	private Date strBulkResponseDate;
	
	@Column(name= "bulk_response_time")
	private Time strBulkResponseTime;
	
	@Column(name= "amount")
	private Double strAmount;
	
	@Column(name= "maker_id")
	private String strMakerId;
	
	@Column(name= "checker_id")
	private String strCheckerId;
	
	@Column(name= "status")
	private String strStatus;
	
	@Column(name= "rejected_reason")
	private String strRejectedReason;
	
	@Column(name= "is_verified")
	private String strIsVerified;
	
	@Column(name= "bulk_mode")
	private String strBulkMode;
	
	@Column(name= "narration")
	private String strNarration;
	
	@Transient
	private String bulkTransferAmount;

	public int getStrId() {
		return strId;
	}

	public void setStrId(int strId) {
		this.strId = strId;
	}

	public String getStrTransactionId() {
		return strTransactionId;
	}

	public void setStrTransactionId(String strTransactionId) {
		this.strTransactionId = strTransactionId;
	}

	public Date getStrBulkRequestDate() {
		return strBulkRequestDate;
	}

	public void setStrBulkRequestDate(Date strBulkRequestDate) {
		this.strBulkRequestDate = strBulkRequestDate;
	}

	public Time getStrBulkRequestTime() {
		return strBulkRequestTime;
	}

	public void setStrBulkRequestTime(Time strBulkRequestTime) {
		this.strBulkRequestTime = strBulkRequestTime;
	}

	public String getStrFromAccountNo() {
		return strFromAccountNo;
	}

	public void setStrFromAccountNo(String strFromAccountNo) {
		this.strFromAccountNo = strFromAccountNo;
	}

	public String getStrFromAccountType() {
		return strFromAccountType;
	}

	public void setStrFromAccountType(String strFromAccountType) {
		this.strFromAccountType = strFromAccountType;
	}

	public String getStrToAccountNo() {
		return strToAccountNo;
	}

	public void setStrToAccountNo(String strToAccountNo) {
		this.strToAccountNo = strToAccountNo;
	}

	public String getStrToAccountType() {
		return strToAccountType;
	}

	public void setStrToAccountType(String strToAccountType) {
		this.strToAccountType = strToAccountType;
	}

	public Date getStrBulkResponseDate() {
		return strBulkResponseDate;
	}

	public void setStrBulkResponseDate(Date strBulkResponseDate) {
		this.strBulkResponseDate = strBulkResponseDate;
	}

	public Time getStrBulkResponseTime() {
		return strBulkResponseTime;
	}

	public void setStrBulkResponseTime(Time strBulkResponseTime) {
		this.strBulkResponseTime = strBulkResponseTime;
	}

	public Double getStrAmount() {
		return strAmount;
	}

	public void setStrAmount(Double strAmount) {
		this.strAmount = strAmount;
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


	public String getStrStatus() {
		return strStatus;
	}

	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}

	public String getStrPreTransactionId() {
		return strPreTransactionId;
	}

	public void setStrPreTransactionId(String strPreTransactionId) {
		this.strPreTransactionId = strPreTransactionId;
	}

	public String getStrRejectedReason() {
		return strRejectedReason;
	}

	public void setStrRejectedReason(String strRejectedReason) {
		this.strRejectedReason = strRejectedReason;
	}

	public String getStrIsVerified() {
		return strIsVerified;
	}

	public void setStrIsVerified(String strIsVerified) {
		this.strIsVerified = strIsVerified;
	}

	public String getStrBulkMode() {
		return strBulkMode;
	}

	public void setStrBulkMode(String strBulkMode) {
		this.strBulkMode = strBulkMode;
	}

	public String getBulkTransferAmount() {
		return bulkTransferAmount;
	}

	public void setBulkTransferAmount(String bulkTransferAmount) {
		this.bulkTransferAmount = bulkTransferAmount;
	}

	public String getStrFromAccountName() {
		return strFromAccountName;
	}

	public void setStrFromAccountName(String strFromAccountName) {
		this.strFromAccountName = strFromAccountName;
	}

	public String getStrToAccountName() {
		return strToAccountName;
	}

	public void setStrToAccountName(String strToAccountName) {
		this.strToAccountName = strToAccountName;
	}

	public String getStrNarration() {
		return strNarration;
	}

	public void setStrNarration(String strNarration) {
		this.strNarration = strNarration;
	}

	@Override
	public String toString() {
		return "BulkTransfer [strId=" + strId + ", strTransactionId=" + strTransactionId + ", strBulkRequestDate="
				+ strBulkRequestDate + ", strBulkRequestTime=" + strBulkRequestTime + ", strFromAccountNo="
				+ strFromAccountNo + ", strFromAccountType=" + strFromAccountType + ", strFromAccountName="
				+ strFromAccountName + ", strToAccountNo=" + strToAccountNo + ", strToAccountType=" + strToAccountType
				+ ", strToAccountName=" + strToAccountName + ", strBulkResponseDate=" + strBulkResponseDate
				+ ", strBulkResponseTime=" + strBulkResponseTime + ", strAmount=" + strAmount + ", strMakerId="
				+ strMakerId + ", strCheckerId=" + strCheckerId + ", strStatus=" + strStatus + ", strPreTransactionId="
				+ strPreTransactionId + ", strRejectedReason=" + strRejectedReason + ", strIsVerified=" + strIsVerified
				+ ", strBulkMode=" + strBulkMode + ", strNarration=" + strNarration + ", bulkTransferAmount="
				+ bulkTransferAmount + "]";
	}
}
