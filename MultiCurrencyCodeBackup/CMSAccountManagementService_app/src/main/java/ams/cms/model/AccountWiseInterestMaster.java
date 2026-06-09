package ams.cms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class AccountWiseInterestMaster implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strId;
	
	@Column(name = "participant_id")
	private String strParticipantId;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "account_number")
	private String strAccountNumber;
	
	@Column(name = "mcc_code")
	private String strMcc;
	
	@Column(name = "transaction_id")
	private String strTransactionId;
	
	@Column(name = "transaction_amount")
	private String strTransactionAmount;
	
	@Column(name = "transaction_time")
	private String strTransactionTime;
	
	@Column(name = "interest_paid_amount")
	private String strInterestPaidAmount;
	
	@Column(name = "interest_paid_date")
	private String strInterestPaidDate;
	
	@Column(name = "interest_calculated_date")
	private String strInterestCalculateDate;
	
	@Column(name = "is_paid")
    private String strIsPaid;
	
	@Column(name = "calculated_interest")
    private String strCalculatInterest;
	
	@Column(name = "calculated_GST")
    private String strCalculatGst;
	
	@Column(name = "amount_paid")
    private String strAmountPaid;
	
	@Column(name = "amount_paid_date")
    private String strAmountPaidDate;
	
	@Column(name = "date_of_interest")
    private String strDateOfInterest;
	
	@Column(name = "txn_date")
    private String strTxnDate;
	
	@Column(name = "txn_time")
    private String strTxnTime;
	
	@Column(name = "interest_rate")
	private String strInterestRate;
	
	@Column(name = "grace_period")
    private String strGracePeriod;
	
	@Column(name = "payment_received_within")
    private String strPaymentReceivedWithin;
	
	@Column(name = "amount_due_percentage")
    private String strAmountDuePercentage;
	
	@Column(name = "created_date")
    private String strCreatedDate;
	
	@Column(name = "transaction_date")
    private String strTransactionDate;
	
	public String getStrId() {
		return strId;
	}
	public void setStrId(String strId) {
		this.strId = strId;
	}
	public String getStrParticipantId() {
		return strParticipantId;
	}
	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
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
	public String getStrTransactionId() {
		return strTransactionId;
	}
	public void setStrTransactionId(String strTransactionId) {
		this.strTransactionId = strTransactionId;
	}
	public String getStrTransactionAmount() {
		return strTransactionAmount;
	}
	public void setStrTransactionAmount(String strTransactionAmount) {
		this.strTransactionAmount = strTransactionAmount;
	}
	public String getStrTransactionTime() {
		return strTransactionTime;
	}
	public void setStrTransactionTime(String strTransactionTime) {
		this.strTransactionTime = strTransactionTime;
	}
	public String getStrInterestPaidAmount() {
		return strInterestPaidAmount;
	}
	public void setStrInterestPaidAmount(String strInterestPaidAmount) {
		this.strInterestPaidAmount = strInterestPaidAmount;
	}
	public String getStrInterestPaidDate() {
		return strInterestPaidDate;
	}
	public void setStrInterestPaidDate(String strInterestPaidDate) {
		this.strInterestPaidDate = strInterestPaidDate;
	}
	public String getStrInterestCalculateDate() {
		return strInterestCalculateDate;
	}
	public void setStrInterestCalculateDate(String strInterestCalculateDate) {
		this.strInterestCalculateDate = strInterestCalculateDate;
	}
	public String getStrIsPaid() {
		return strIsPaid;
	}
	public void setStrIsPaid(String strIsPaid) {
		this.strIsPaid = strIsPaid;
	}
	public String getStrCalculatInterest() {
		return strCalculatInterest;
	}
	public void setStrCalculatInterest(String strCalculatInterest) {
		this.strCalculatInterest = strCalculatInterest;
	}
	public String getStrCalculatGst() {
		return strCalculatGst;
	}
	public void setStrCalculatGst(String strCalculatGst) {
		this.strCalculatGst = strCalculatGst;
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
	public String getStrInterestRate() {
		return strInterestRate;
	}
	public void setStrInterestRate(String strInterestRate) {
		this.strInterestRate = strInterestRate;
	}
	public String getStrGracePeriod() {
		return strGracePeriod;
	}
	public void setStrGracePeriod(String strGracePeriod) {
		this.strGracePeriod = strGracePeriod;
	}
	public String getStrPaymentReceivedWithin() {
		return strPaymentReceivedWithin;
	}
	public void setStrPaymentReceivedWithin(String strPaymentReceivedWithin) {
		this.strPaymentReceivedWithin = strPaymentReceivedWithin;
	}
	public String getStrAmountDuePercentage() {
		return strAmountDuePercentage;
	}
	public void setStrAmountDuePercentage(String strAmountDuePercentage) {
		this.strAmountDuePercentage = strAmountDuePercentage;
	}
	public String getStrCreatedDate() {
		return strCreatedDate;
	}
	public void setStrCreatedDate(String strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}
	public String getStrTransactionDate() {
		return strTransactionDate;
	}
	public void setStrTransactionDate(String strTransactionDate) {
		this.strTransactionDate = strTransactionDate;
	} 

}
