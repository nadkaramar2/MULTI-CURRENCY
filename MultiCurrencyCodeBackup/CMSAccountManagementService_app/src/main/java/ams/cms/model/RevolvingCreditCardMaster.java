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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ams.cms.util.JsonDateDeserializer;
import ams.cms.util.JsonDateSerializer;

@JsonAutoDetect
@Entity
@Table(name = "revolving_credit_card_master")
public class RevolvingCreditCardMaster implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int strId;
	
	@Column(name = "participant_id")
	private String strParticipantID;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "grace_period_in_days")
	private String strGracePeriodInDays;
	
	@Column(name = "mad_rate")
	private String strMadRate;
	
	@Column(name = "interest_rate")
	private String  strInterestRate;
	
	@Column(name = "penulty_interest_rate")
	private String strPenultyInterestRate;
	
	@Column(name = "late_payment_fees")
	private String strLatePaymentFees;
	
	@Column(name = "billing_cycle_date")
	private String strBillingCycleDate;
	
	@Column(name = "deliquency_days")
	private String strDeliquencyDays;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "creation_date")
	private Date strDateOfCreation;
	
	@Column(name = "created_by")
	private String strCreatedBy;
	
	@Transient
	private String strAccounNumber;
	
	@Transient
	private String strBillingMonthAndDay;

	public int getStrId() {
		return strId;
	}

	public void setStrId(int strId) {
		this.strId = strId;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrGracePeriodInDays() {
		return strGracePeriodInDays;
	}

	public void setStrGracePeriodInDays(String strGracePeriodInDays) {
		this.strGracePeriodInDays = strGracePeriodInDays;
	}

	public String getStrMadRate() {
		return strMadRate;
	}

	public void setStrMadRate(String strMadRate) {
		this.strMadRate = strMadRate;
	}

	public String getStrInterestRate() {
		return strInterestRate;
	}

	public void setStrInterestRate(String strInterestRate) {
		this.strInterestRate = strInterestRate;
	}

	public String getStrPenultyInterestRate() {
		return strPenultyInterestRate;
	}

	public void setStrPenultyInterestRate(String strPenultyInterestRate) {
		this.strPenultyInterestRate = strPenultyInterestRate;
	}

	public String getStrBillingCycleDate() {
		return strBillingCycleDate;
	}

	public void setStrBillingCycleDate(String strBillingCycleDate) {
		this.strBillingCycleDate = strBillingCycleDate;
	}

	public String getStrDeliquencyDays() {
		return strDeliquencyDays;
	}

	public void setStrDeliquencyDays(String strDeliquencyDays) {
		this.strDeliquencyDays = strDeliquencyDays;
	}


	public String getStrCreatedBy() {
		return strCreatedBy;
	}

	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}

	public Date getStrDateOfCreation() {
		return strDateOfCreation;
	}

	public void setStrDateOfCreation(Date strDateOfCreation) {
		this.strDateOfCreation = strDateOfCreation;
	}

	public String getStrAccounNumber() {
		return strAccounNumber;
	}

	public void setStrAccounNumber(String strAccounNumber) {
		this.strAccounNumber = strAccounNumber;
	}

	public String getStrParticipantID() {
		return strParticipantID;
	}

	public void setStrParticipantID(String strParticipantID) {
		this.strParticipantID = strParticipantID;
	}

	public String getStrLatePaymentFees() {
		return strLatePaymentFees;
	}

	public void setStrLatePaymentFees(String strLatePaymentFees) {
		this.strLatePaymentFees = strLatePaymentFees;
	}

	public String getStrBillingMonthAndDay() {
		return strBillingMonthAndDay;
	}

	public void setStrBillingMonthAndDay(String strBillingMonthAndDay) {
		this.strBillingMonthAndDay = strBillingMonthAndDay;
	}
}
