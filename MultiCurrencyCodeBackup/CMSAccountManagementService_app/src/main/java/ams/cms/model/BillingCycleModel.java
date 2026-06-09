package ams.cms.model;

import java.io.Serializable;

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
@Table(name = "billing_cycle_master")
public class BillingCycleModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int strId;
	
	@Column(name = "participant_id")
	private String strParticipantId;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "account_number")
	private String strAccountNumber;
	
	@Column(name = "card_number")
	private String strCardNumber;
	
	@Column(name = "card_type")
	private String strCardType;
	
	@Column(name="mcc_code")
	private String strMcc;
	
	@Column(name="outstanding_amount")
	private Double strOutstandingAmt;
	
	@Column(name="available_credit_limit")
	private Double strAvialbleCreditLimit;
	
	@Column(name="minimum_amount_due")
	private Double strMinimumAmountDue;
	
	@Column(name="last_billing_cycle_date")
	private String strLastBillingCycleDate;
	
	@Column(name="current_billing_date")
	private String strCurrentBillingCycleDate;
	
	@Transient
	private String strBillingCycleDate;
	

	public int getStrId() {
		return strId;
	}

	public void setStrId(int strId) {
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

	public String getStrCardNumber() {
		return strCardNumber;
	}

	public void setStrCardNumber(String strCardNumber) {
		this.strCardNumber = strCardNumber;
	}

	public String getStrCardType() {
		return strCardType;
	}

	public void setStrCardType(String strCardType) {
		this.strCardType = strCardType;
	}

	public String getStrMcc() {
		return strMcc;
	}

	public void setStrMcc(String strMcc) {
		this.strMcc = strMcc;
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

	public String getStrBillingCycleDate() {
		return strBillingCycleDate;
	}

	public void setStrBillingCycleDate(String strBillingCycleDate) {
		this.strBillingCycleDate = strBillingCycleDate;
	}

	public Double getStrOutstandingAmt() {
		return strOutstandingAmt;
	}

	public void setStrOutstandingAmt(Double strOutstandingAmt) {
		this.strOutstandingAmt = strOutstandingAmt;
	}

	public Double getStrAvialbleCreditLimit() {
		return strAvialbleCreditLimit;
	}

	public void setStrAvialbleCreditLimit(Double strAvialbleCreditLimit) {
		this.strAvialbleCreditLimit = strAvialbleCreditLimit;
	}

	public Double getStrMinimumAmountDue() {
		return strMinimumAmountDue;
	}

	public void setStrMinimumAmountDue(Double strMinimumAmountDue) {
		this.strMinimumAmountDue = strMinimumAmountDue;
	}

}
