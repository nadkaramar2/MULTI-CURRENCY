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
@Table(name = "credit_card_transaction")
public class AccountCreditCardTransactionModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "participant_id")
	private String strParticipantID;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "account_number")
	private String strAccountNumber;
	
	@Column(name = "mcc_code")
	private String strMcc;
	
	@Column(name = "transaction_amount")
	private String strTransactionAmount;
	
	@Column(name = "amount_paid")
	private String strAmountPaid;
	
	@Column(name = "amount_paid_date")
	private String strAmountPaidDate;
	
	@Column(name = "is_paid")
	private String strIsPaid;
	
	@Column(name = "date_of_interest")
	private Date dateOfInterest;
	
	@Column(name = "txn_id")
	private String strTransactionId;
	
	@Column(name = "tran_type")
	private String strTransactionType;
	
	@Column(name = "tran_type_descr")
	private String strTranTypeDescription;
	
	@Column(name = "txn_date")	
	private Date txnDate;
	
	@Column(name = "txn_time")
	private String strTxnTime;
	
	@Transient
	private String strTxnDate;
	
	@Transient
	private String strDateOfInterest;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getStrParticipantID() {
		return strParticipantID;
	}

	public void setStrParticipantID(String strParticipantID) {
		this.strParticipantID = strParticipantID;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrAccountNumber() {
		return strAccountNumber;
	}

	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}

	public String getStrMcc() {
		return strMcc;
	}

	public void setStrMcc(String strMcc) {
		this.strMcc = strMcc;
	}

	public String getStrTransactionAmount() {
		return strTransactionAmount;
	}

	public void setStrTransactionAmount(String strTransactionAmount) {
		this.strTransactionAmount = strTransactionAmount;
	}

	public String getStrAmountPaid() {
		return strAmountPaid;
	}

	public void setStrAmountPaid(String strAmountPaid) {
		this.strAmountPaid = strAmountPaid;
	}

	public String getStrAmountPaidDate() {
		return strAmountPaidDate;
	}

	public void setStrAmountPaidDate(String strAmountPaidDate) {
		this.strAmountPaidDate = strAmountPaidDate;
	}

	public String getStrIsPaid() {
		return strIsPaid;
	}

	public void setStrIsPaid(String strIsPaid) {
		this.strIsPaid = strIsPaid;
	}

	public String getStrDateOfInterest() {
		return strDateOfInterest;
	}

	public void setStrDateOfInterest(String strDateOfInterest) {
		this.strDateOfInterest = strDateOfInterest;
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

	public String getStrTransactionId() {
		return strTransactionId;
	}

	public void setStrTransactionId(String strTransactionId) {
		this.strTransactionId = strTransactionId;
	}

	public String getStrTransactionType() {
		return strTransactionType;
	}

	public void setStrTransactionType(String strTransactionType) {
		this.strTransactionType = strTransactionType;
	}

	public String getStrTranTypeDescription() {
		return strTranTypeDescription;
	}

	public void setStrTranTypeDescription(String strTranTypeDescription) {
		this.strTranTypeDescription = strTranTypeDescription;
	}

	public Date getDateOfInterest() {
		return dateOfInterest;
	}

	public void setDateOfInterest(Date dateOfInterest) {
		this.dateOfInterest = dateOfInterest;
	}

	public Date getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}	
}



