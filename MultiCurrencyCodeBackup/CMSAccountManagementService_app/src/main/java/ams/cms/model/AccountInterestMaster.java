package ams.cms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name = "interest_account_wise")
public class AccountInterestMaster implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int strID;
	
	@Column(name = "participant_id")
	private String strParticipantID;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "account_number")
	private String strAccountNumber;
	
	@Column(name = "mcc_code")
	private String strMcc;
	
	@Column(name = "transaction_id")
	private String strTransactionID;
	
	@Column(name = "transaction_amount")
	private String strTransactionAmount;
	
	@Column(name = "transaction_date")
	private String strTransactionDate;
	
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
	private String strCalculateInterest;
	
	@Column(name = "calculated_GST")
	private String strCalculateGST;
	
	@Column(name = "created_date")
	private String strCreateDate;

	public int getStrID() {
		return strID;
	}

	public void setStrID(int strID) {
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

	public String getStrTransactionID() {
		return strTransactionID;
	}

	public void setStrTransactionID(String strTransactionID) {
		this.strTransactionID = strTransactionID;
	}

	public String getStrTransactionAmount() {
		return strTransactionAmount;
	}

	public void setStrTransactionAmount(String strTransactionAmount) {
		this.strTransactionAmount = strTransactionAmount;
	}

	public String getStrTransactionDate() {
		return strTransactionDate;
	}

	public void setStrTransactionDate(String strTransactionDate) {
		this.strTransactionDate = strTransactionDate;
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

	public String getStrCalculateInterest() {
		return strCalculateInterest;
	}

	public void setStrCalculateInterest(String strCalculateInterest) {
		this.strCalculateInterest = strCalculateInterest;
	}

	public String getStrCalculateGST() {
		return strCalculateGST;
	}

	public void setStrCalculateGST(String strCalculateGST) {
		this.strCalculateGST = strCalculateGST;
	}

	public String getStrCreateDate() {
		return strCreateDate;
	}

	public void setStrCreateDate(String strCreateDate) {
		this.strCreateDate = strCreateDate;
	}
}
