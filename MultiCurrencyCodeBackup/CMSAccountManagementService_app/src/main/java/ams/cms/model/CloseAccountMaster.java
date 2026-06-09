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

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;


@JsonAutoDetect
@Entity
@DynamicUpdate
@Table(name = "account_close_master")
public class CloseAccountMaster implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;	
	
	@Column(name = "transaction_id")
	private String transactionId;
	
	@Column(name = "closure_req_date")
	private Date closureReqDate;
	
	@Column(name = "closure_req_time")
	private Time closureReqTime;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "account_no")
	private String accountNo;
	
	@Column(name = "account_holder_name")
	private String accountHolderName;
	
	@Column(name = "cust_id")
	private String strCustId;
	
	@Column(name = "current_account_balance")
	private String currentAccountBalance;
	
	@Column(name = "current_account_tier")
	private String currentAccountTier;
	
	@Column(name = "closure_initiated_by")
	private String closureInitiatedBy;
	
	@Column(name = "closure_reason")
	private String closureReason;
	
	@Column(name = "maker_user_id")
	private String makerUserId;
	
	@Column(name = "cheker_user_id")
	private String chekerUserId;
	
	@Column(name = "request_status")
	private String requestStatus;
	
	@Column(name = "reason_for_rejection")
	private String reasonForRejection;
	
	@Column(name = "account_closure_rejected_date")
	private Date accountClosureRejectedDate;
	
	@Column(name = "account_closure_rejected_time")
	private Time accountClosureRejectedTime;
	
	@Column(name = "account_closed_date")
	private Date accountClosedDate;
	
	@Column(name = "account_closed_time")
	private Time accountClosedTime;
	
	@Column(name = "balance_transfer_to")
	private String balanceTransferTo;
	
	@Column(name = "recipient_bank_id")
	private String recipientBankId;
	
	@Column(name = "recipient_account_type")
	private String recipientAccountType;
	
	@Column(name = "recipient_account_no")
	private String recipientAccountNo;
	
	@Column(name = "recipient_account_holder_name")
	private String recipientAccountHolderName;
	
	@Column(name = "transfer_amount")
	private Double transferAmount;
	
	@Transient
	private String strClosureReqDate;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Date getClosureReqDate() {
		return closureReqDate;
	}

	public void setClosureReqDate(Date closureReqDate) {
		this.closureReqDate = closureReqDate;
	}

	public Time getClosureReqTime() {
		return closureReqTime;
	}

	public void setClosureReqTime(Time closureReqTime) {
		this.closureReqTime = closureReqTime;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public String getStrCustId() {
		return strCustId;
	}

	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}

	public String getCurrentAccountBalance() {
		return currentAccountBalance;
	}

	public void setCurrentAccountBalance(String currentAccountBalance) {
		this.currentAccountBalance = currentAccountBalance;
	}

	public String getCurrentAccountTier() {
		return currentAccountTier;
	}

	public void setCurrentAccountTier(String currentAccountTier) {
		this.currentAccountTier = currentAccountTier;
	}

	public String getClosureInitiatedBy() {
		return closureInitiatedBy;
	}

	public void setClosureInitiatedBy(String closureInitiatedBy) {
		this.closureInitiatedBy = closureInitiatedBy;
	}

	public String getClosureReason() {
		return closureReason;
	}

	public void setClosureReason(String closureReason) {
		this.closureReason = closureReason;
	}

	public String getMakerUserId() {
		return makerUserId;
	}

	public void setMakerUserId(String makerUserId) {
		this.makerUserId = makerUserId;
	}

	public String getChekerUserId() {
		return chekerUserId;
	}

	public void setChekerUserId(String chekerUserId) {
		this.chekerUserId = chekerUserId;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getReasonForRejection() {
		return reasonForRejection;
	}

	public void setReasonForRejection(String reasonForRejection) {
		this.reasonForRejection = reasonForRejection;
	}

	public Date getAccountClosureRejectedDate() {
		return accountClosureRejectedDate;
	}

	public void setAccountClosureRejectedDate(Date accountClosureRejectedDate) {
		this.accountClosureRejectedDate = accountClosureRejectedDate;
	}

	public Time getAccountClosureRejectedTime() {
		return accountClosureRejectedTime;
	}

	public void setAccountClosureRejectedTime(Time accountClosureRejectedTime) {
		this.accountClosureRejectedTime = accountClosureRejectedTime;
	}

	public Date getAccountClosedDate() {
		return accountClosedDate;
	}

	public void setAccountClosedDate(Date accountClosedDate) {
		this.accountClosedDate = accountClosedDate;
	}

	public Time getAccountClosedTime() {
		return accountClosedTime;
	}

	public void setAccountClosedTime(Time accountClosedTime) {
		this.accountClosedTime = accountClosedTime;
	}

	public String getBalanceTransferTo() {
		return balanceTransferTo;
	}

	public void setBalanceTransferTo(String balanceTransferTo) {
		this.balanceTransferTo = balanceTransferTo;
	}

	public String getRecipientBankId() {
		return recipientBankId;
	}

	public void setRecipientBankId(String recipientBankId) {
		this.recipientBankId = recipientBankId;
	}

	public String getRecipientAccountType() {
		return recipientAccountType;
	}

	public void setRecipientAccountType(String recipientAccountType) {
		this.recipientAccountType = recipientAccountType;
	}

	public String getRecipientAccountNo() {
		return recipientAccountNo;
	}

	public void setRecipientAccountNo(String recipientAccountNo) {
		this.recipientAccountNo = recipientAccountNo;
	}

	public String getRecipientAccountHolderName() {
		return recipientAccountHolderName;
	}

	public void setRecipientAccountHolderName(String recipientAccountHolderName) {
		this.recipientAccountHolderName = recipientAccountHolderName;
	}

	public Double getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(Double transferAmount) {
		this.transferAmount = transferAmount;
	}

	public String getStrClosureReqDate() {
		return strClosureReqDate;
	}

	public void setStrClosureReqDate(String strClosureReqDate) {
		this.strClosureReqDate = strClosureReqDate;
	}

	@Override
	public String toString() 
	{
		return "CloseAccountMaster [strID=" + strID + ", transactionId=" + transactionId + ", closureReqDate="
				+ closureReqDate + ", closureReqTime=" + closureReqTime + ", strAccountType=" + strAccountType
				+ ", accountNo=" + accountNo + ", accountHolderName=" + accountHolderName + ", strCustId=" + strCustId
				+ ", currentAccountBalance=" + currentAccountBalance + ", currentAccountTier=" + currentAccountTier
				+ ", closureInitiatedBy=" + closureInitiatedBy + ", closureReason=" + closureReason + ", makerUserId="
				+ makerUserId + ", chekerUserId=" + chekerUserId + ", requestStatus=" + requestStatus
				+ ", reasonForRejection=" + reasonForRejection + ", accountClosureRejectedDate="
				+ accountClosureRejectedDate + ", accountClosureRejectedTime=" + accountClosureRejectedTime
				+ ", accountClosedDate=" + accountClosedDate + ", accountClosedTime=" + accountClosedTime
				+ ", balanceTransferTo=" + balanceTransferTo + ", recipientBankId=" + recipientBankId
				+ ", recipientAccountType=" + recipientAccountType + ", recipientAccountNo=" + recipientAccountNo
				+ ", recipientAccountHolderName=" + recipientAccountHolderName + ", transferAmount=" + transferAmount
				+ "]";
	}
}
