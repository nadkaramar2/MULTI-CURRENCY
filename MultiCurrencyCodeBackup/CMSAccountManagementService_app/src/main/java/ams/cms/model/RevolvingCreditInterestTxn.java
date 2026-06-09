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
@Table(name = "revolving_credit_interest_txn")
public class RevolvingCreditInterestTxn  implements Serializable{

	/**
	 * 
	 */
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
	
	@Column(name = "mcc_code")
	private String strMcc;
	
	@Column(name = "txn_id")
	private String strTxnId;
	
	@Column(name = "txn_amount")
	private String  strTxnAmount;
	
	@Column(name = "txn_date")
	private String  strTxnDate;
	
	@Column(name = "txn_time")
	private String  strTxnTime;
	
	@Column(name = "calculated_interest_amount")
	private String strCalculateInterestAmt;
	
	@Column(name = "calculated_gst_amount")
	private String strCalcualteGstAmt;
	
	@Column(name = "interest_calculated_date")
	private String strInterestCalDate;

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

	public String getStrCalculateInterestAmt() {
		return strCalculateInterestAmt;
	}

	public void setStrCalculateInterestAmt(String strCalculateInterestAmt) {
		this.strCalculateInterestAmt = strCalculateInterestAmt;
	}

	public String getStrCalcualteGstAmt() {
		return strCalcualteGstAmt;
	}

	public void setStrCalcualteGstAmt(String strCalcualteGstAmt) {
		this.strCalcualteGstAmt = strCalcualteGstAmt;
	}

	public String getStrInterestCalDate() {
		return strInterestCalDate;
	}

	public void setStrInterestCalDate(String strInterestCalDate) {
		this.strInterestCalDate = strInterestCalDate;
	}
}
