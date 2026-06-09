package ams.cms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name = "mcc_wise_interest")
public class MccWiseInterestModel implements Serializable
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
	
	@Column(name = "mcc_code")
	private String strMccCode;
	
	@Column(name = "interest_rate")
	private String strInterestRate;
	
	@Column(name = "grace_period")
	private String strGracePeriod;
	
	@Column(name = "payment_received_within")
	private String strPaymentReceivedWithinDays;
	
	@Column(name = "amount_due_percentage")
	private String strAmountDuePercentage;
	
	@Column(name = "late_payment_fees")
	private String strLatePaymentFees;
	
	@Column(name = "created_by")
	private String strCreatedBy;
	
	@Column(name = "created_date")
	private Date strCreatedDate;

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

	public String getStrMccCode() {
		return strMccCode;
	}

	public void setStrMccCode(String strMccCode) {
		this.strMccCode = strMccCode;
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

	public String getStrPaymentReceivedWithinDays() {
		return strPaymentReceivedWithinDays;
	}

	public void setStrPaymentReceivedWithinDays(String strPaymentReceivedWithinDays) {
		this.strPaymentReceivedWithinDays = strPaymentReceivedWithinDays;
	}

	public String getStrAmountDuePercentage() {
		return strAmountDuePercentage;
	}

	public void setStrAmountDuePercentage(String strAmountDuePercentage) {
		this.strAmountDuePercentage = strAmountDuePercentage;
	}

	public String getStrLatePaymentFees() {
		return strLatePaymentFees;
	}

	public void setStrLatePaymentFees(String strLatePaymentFees) {
		this.strLatePaymentFees = strLatePaymentFees;
	}

	public String getStrCreatedBy() {
		return strCreatedBy;
	}

	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}

	public Date getStrCreatedDate() {
		return strCreatedDate;
	}

	public void setStrCreatedDate(Date strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}
}
