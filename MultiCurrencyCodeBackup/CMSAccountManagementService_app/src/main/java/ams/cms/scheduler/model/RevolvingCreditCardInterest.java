package ams.cms.scheduler.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class RevolvingCreditCardInterest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String strParticipantId;
	private String strAccountType;
	private String strAccountNumber;
	private String strMcc;
	private String strTxnId;
	private String strTxnAmount;
	private Date TxnDate;
	private Time TxnTime;
	private Double strTotalOutstandingBal;
	private Double strTotalOutstandingInterest;
	private Double strCalculatedInterest;
	private Double strCalcualteGstAmount;
	private Double strTotalCalculatedGST;
	private String  strInterestRate;
	private String strIsRevolvingCredit;
	private String strIsCreditType;
	private Date paymentDueDate;
	private Date calculatedtDueDate;
	private Double totalOutstandingInterest;
	private Date gracePeriodStartDate; 
	private Double totalSumOutstandingInterest;
	private Date switchTxnDate;
	
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
	public String getStrTxnId() {
		return strTxnId;
	}
	public void setStrTxnId(String strTxnId) {
		this.strTxnId = strTxnId;
	}
	public String getStrTxnAmount() {
		return strTxnAmount;
	}
	public void setStrTxnAmount(String strTxnAmount) {
		this.strTxnAmount = strTxnAmount;
	}
	public Date getTxnDate() {
		return TxnDate;
	}
	public void setTxnDate(Date txnDate) {
		TxnDate = txnDate;
	}
	public Time getTxnTime() {
		return TxnTime;
	}
	public void setTxnTime(Time txnTime) {
		TxnTime = txnTime;
	}
	public Double getStrTotalOutstandingBal() {
		return strTotalOutstandingBal;
	}
	public void setStrTotalOutstandingBal(Double strTotalOutstandingBal) {
		this.strTotalOutstandingBal = strTotalOutstandingBal;
	}
	public Double getStrTotalOutstandingInterest() {
		return strTotalOutstandingInterest;
	}
	public void setStrTotalOutstandingInterest(Double strTotalOutstandingInterest) {
		this.strTotalOutstandingInterest = strTotalOutstandingInterest;
	}
	public String getStrInterestRate() {
		return strInterestRate;
	}
	public void setStrInterestRate(String strInterestRate) {
		this.strInterestRate = strInterestRate;
	}
	public String getStrIsRevolvingCredit() {
		return strIsRevolvingCredit;
	}
	public void setStrIsRevolvingCredit(String strIsRevolvingCredit) {
		this.strIsRevolvingCredit = strIsRevolvingCredit;
	}
	public String getStrIsCreditType() {
		return strIsCreditType;
	}
	public void setStrIsCreditType(String strIsCreditType) {
		this.strIsCreditType = strIsCreditType;
	}
	public Date getPaymentDueDate() {
		return paymentDueDate;
	}
	public void setPaymentDueDate(Date paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}
	public Double getStrCalcualteGstAmount() {
		return strCalcualteGstAmount;
	}
	public void setStrCalcualteGstAmount(Double strCalcualteGstAmount) {
		this.strCalcualteGstAmount = strCalcualteGstAmount;
	}
	public Double getStrTotalCalculatedGST() {
		return strTotalCalculatedGST;
	}
	public void setStrTotalCalculatedGST(Double strTotalCalculatedGST) {
		this.strTotalCalculatedGST = strTotalCalculatedGST;
	}
	public Double getStrCalculatedInterest() {
		return strCalculatedInterest;
	}
	public void setStrCalculatedInterest(Double strCalculatedInterest) {
		this.strCalculatedInterest = strCalculatedInterest;
	}
	public Date getCalculatedtDueDate() {
		return calculatedtDueDate;
	}
	public void setCalculatedtDueDate(Date calculatedtDueDate) {
		this.calculatedtDueDate = calculatedtDueDate;
	}
	public Double getTotalOutstandingInterest() {
		return totalOutstandingInterest;
	}
	public void setTotalOutstandingInterest(Double totalOutstandingInterest) {
		this.totalOutstandingInterest = totalOutstandingInterest;
	}
	public Date getGracePeriodStartDate() {
		return gracePeriodStartDate;
	}
	public void setGracePeriodStartDate(Date gracePeriodStartDate) {
		this.gracePeriodStartDate = gracePeriodStartDate;
	}
	public Double getTotalSumOutstandingInterest() {
		return totalSumOutstandingInterest;
	}
	public void setTotalSumOutstandingInterest(Double totalSumOutstandingInterest) {
		this.totalSumOutstandingInterest = totalSumOutstandingInterest;
	}
	public Date getSwitchTxnDate() {
		return switchTxnDate;
	}
	public void setSwitchTxnDate(Date switchTxnDate) {
		this.switchTxnDate = switchTxnDate;
	}
	 	
}
