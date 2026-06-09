package ams.cms.scheduler.model;

import java.io.Serializable;
import java.util.Date;

public class AccountCreditCardStatement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String strParticipantId;
	private String strAccountHolderName;
	private String strAccountType;
	private String strAccountNumber;
	private String strMadRate;
	private String strEmailID;
	private String strLastBillingCycleDate;
	private Double strTotalOutstandingBal;
	private Double strAvailableCreditLimit;
	private String strCurrentBillingCycleDate;
   // private String strSwitch_txn_date;
	private Double strTransaction_amount;
	private String strIsRevolvingCredit;
	private String strIsCreditType;
	private Double strMinimumAmountDue;
	private String strCardType;
	private String strCardNumber;
	private String strMcc;
	private Double strclosingBalance;
	private String strTransactionMode;
	private String strTransactionType;
	
	private String strTransactionDate;
	private String strTransactionId;
	private String strTransactionDetails;
	private String strRewardsPoints;
	private String strInternationalAmount;
	private String strPayementDueDate;
	private String calculatedtDueDate;
	private String lastTotalAmountClearDate;
	
	private String strcalculatedpaymentDueDate;
	

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
	public Double getStrTotalOutstandingBal() {
		return strTotalOutstandingBal;
	}
	public void setStrTotalOutstandingBal(Double strTotalOutstandingBal) {
		this.strTotalOutstandingBal = strTotalOutstandingBal;
	}
	public Double getStrAvailableCreditLimit() {
		return strAvailableCreditLimit;
	}
	public void setStrAvailableCreditLimit(Double strAvailableCreditLimit) {
		this.strAvailableCreditLimit = strAvailableCreditLimit;
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
	
	public String getStrAccountHolderName() {
		return strAccountHolderName;
	}
	public void setStrAccountHolderName(String strAccountHolderName) {
		this.strAccountHolderName = strAccountHolderName;
	}
	public String getStrEmailID() {
		return strEmailID;
	}
	public void setStrEmailID(String strEmailID) {
		this.strEmailID = strEmailID;
	}

	public String getStrMadRate() {
		return strMadRate;
	}
	public void setStrMadRate(String strMadRate) {
		this.strMadRate = strMadRate;
	}
	public String getStrCardType() {
		return strCardType;
	}
	public void setStrCardType(String strCardType) {
		this.strCardType = strCardType;
	}
	public String getStrCardNumber() {
		return strCardNumber;
	}
	public void setStrCardNumber(String strCardNumber) {
		this.strCardNumber = strCardNumber;
	}
	public String getStrMcc() {
		return strMcc;
	}
	public void setStrMcc(String strMcc) {
		this.strMcc = strMcc;
	}
	
	public Double getStrMinimumAmountDue() {
		return strMinimumAmountDue;
	}
	public void setStrMinimumAmountDue(Double strMinimumAmountDue) {
		this.strMinimumAmountDue = strMinimumAmountDue;
	}
	
	public Double getStrclosingBalance() {
		return strclosingBalance;
	}
	public void setStrclosingBalance(Double strclosingBalance) {
		this.strclosingBalance = strclosingBalance;
	}
	public void setStrTransaction_amount(Double strTransaction_amount) {
		this.strTransaction_amount = strTransaction_amount;
	}
	public String getStrTransactionMode() {
		return strTransactionMode;
	}
	public void setStrTransactionMode(String strTransactionMode) {
		this.strTransactionMode = strTransactionMode;
	}
	public String getStrTransactionType() {
		return strTransactionType;
	}
	public void setStrTransactionType(String strTransactionType) {
		this.strTransactionType = strTransactionType;
	}
	public String getStrLastBillingCycleDate() {
		return strLastBillingCycleDate;
	}
	public void setStrLastBillingCycleDate(String strLastBillingCycleDate) {
		this.strLastBillingCycleDate = strLastBillingCycleDate;
	}
	public String getStrCurrentBillingCycleDate() {
		return strCurrentBillingCycleDate;
	}
	public void setStrCurrentBillingCycleDate(String strCurrentBillingCycleDate) {
		this.strCurrentBillingCycleDate = strCurrentBillingCycleDate;
	}
	public String getStrTransactionDate() {
		return strTransactionDate;
	}
	public void setStrTransactionDate(String strTransactionDate) {
		this.strTransactionDate = strTransactionDate;
	}
	public String getStrTransactionId() {
		return strTransactionId;
	}
	public void setStrTransactionId(String strTransactionId) {
		this.strTransactionId = strTransactionId;
	}
	public String getStrTransactionDetails() {
		return strTransactionDetails;
	}
	public void setStrTransactionDetails(String strTransactionDetails) {
		this.strTransactionDetails = strTransactionDetails;
	}
	public String getStrRewardsPoints() {
		return strRewardsPoints;
	}
	public void setStrRewardsPoints(String strRewardsPoints) {
		this.strRewardsPoints = strRewardsPoints;
	}
	public String getStrInternationalAmount() {
		return strInternationalAmount;
	}
	public void setStrInternationalAmount(String strInternationalAmount) {
		this.strInternationalAmount = strInternationalAmount;
	}
	public Double getStrTransaction_amount() {
		return strTransaction_amount;
	}
	public String getStrPayementDueDate() {
		return strPayementDueDate;
	}
	public void setStrPayementDueDate(String strPayementDueDate) {
		this.strPayementDueDate = strPayementDueDate;
	}
	public String getCalculatedtDueDate() {
		return calculatedtDueDate;
	}
	public void setCalculatedtDueDate(String calculatedtDueDate) {
		this.calculatedtDueDate = calculatedtDueDate;
	}
	public String getStrcalculatedpaymentDueDate() {
		return strcalculatedpaymentDueDate;
	}
	public void setStrcalculatedpaymentDueDate(String strcalculatedpaymentDueDate) {
		this.strcalculatedpaymentDueDate = strcalculatedpaymentDueDate;
	}
	public String getLastTotalAmountClearDate() {
		return lastTotalAmountClearDate;
	}
	public void setLastTotalAmountClearDate(String lastTotalAmountClearDate) {
		this.lastTotalAmountClearDate = lastTotalAmountClearDate;
	}
}
