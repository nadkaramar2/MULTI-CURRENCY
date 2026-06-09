package ams.cms.scheduler.model;

import java.io.Serializable;
import java.sql.Date;

public class CreditCardHeaderModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String strParticipantId;
	private String strAccountHolderName;
	private String strAccountType;
	private String strAccountNumber;
	private String strMadRate;
	private String strEmailID;
	private Date strBillingCycleDate;
	private Double strTotalOutstandingBal;
	private Double strAvailableCreditLimit;
    private String strSwitch_txn_date;
	private Double strTransaction_amount;
	private String strIsRevolvingCredit;
	private String strIsCreditType;
	private String strAmountDue;
	private String strCardType;
	private String strCardNumber;
	private String strMcc;
	public String getStrParticipantId() {
		return strParticipantId;
	}
	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}
	public String getStrAccountHolderName() {
		return strAccountHolderName;
	}
	public void setStrAccountHolderName(String strAccountHolderName) {
		this.strAccountHolderName = strAccountHolderName;
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
	public String getStrMadRate() {
		return strMadRate;
	}
	public void setStrMadRate(String strMadRate) {
		this.strMadRate = strMadRate;
	}
	public String getStrEmailID() {
		return strEmailID;
	}
	public void setStrEmailID(String strEmailID) {
		this.strEmailID = strEmailID;
	}
	public Double getStrAvailableCreditLimit() {
		return strAvailableCreditLimit;
	}
	public void setStrAvailableCreditLimit(Double strAvailableCreditLimit) {
		this.strAvailableCreditLimit = strAvailableCreditLimit;
	}
	public String getStrSwitch_txn_date() {
		return strSwitch_txn_date;
	}
	public void setStrSwitch_txn_date(String strSwitch_txn_date) {
		this.strSwitch_txn_date = strSwitch_txn_date;
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
	public String getStrAmountDue() {
		return strAmountDue;
	}
	public void setStrAmountDue(String strAmountDue) {
		this.strAmountDue = strAmountDue;
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
	public Date getStrBillingCycleDate() {
		return strBillingCycleDate;
	}
	public void setStrBillingCycleDate(Date strBillingCycleDate) {
		this.strBillingCycleDate = strBillingCycleDate;
	}
	public Double getStrTotalOutstandingBal() {
		return strTotalOutstandingBal;
	}
	public void setStrTotalOutstandingBal(Double strTotalOutstandingBal) {
		this.strTotalOutstandingBal = strTotalOutstandingBal;
	}
	public Double getStrTransaction_amount() {
		return strTransaction_amount;
	}
	public void setStrTransaction_amount(Double strTransaction_amount) {
		this.strTransaction_amount = strTransaction_amount;
	}	
	
}
