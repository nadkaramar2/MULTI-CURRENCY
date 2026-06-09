package ams.cms.model;

import java.io.Serializable;
import java.sql.Time;
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
@Table(name = "revolving_credit_card_txn_master")
public class RevolvingCreditCardTxnMaster implements Serializable {

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
	
	@Column(name = "interest_rate")
	private String  strInterestRate;
	
	@Column(name = "txn_id")
	private String strTxnId;
	
	@Column(name = "tran_type")
	private String strTranType;
	
	@Column(name = "txn_amount")
	private String strTxnAmount;
	
	@Column(name = "tran_type_descr")
	private String strTranTypeDes;
	
	@Column(name = "txn_date")
	private Date txnDate;
	
	@Transient
	private String strTxnDate;
	
	@Column(name = "txn_time")
	private Time txnTime;
	
	@Transient
	private String strTxnTime;
	
	@Column(name = "remaning_grace_period")
	private String strRemaningGracePeriod;
  
	@Column(name="updated_txn_amount")
	private Double strUpdateTxnAmount;
	
	@Transient
	private Double totalOutstandingInterest;	
	
	@Transient
	private Double totalCalGstAmount;
	
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

	public String getStrInterestRate() {
		return strInterestRate;
	}

	public void setStrInterestRate(String strInterestRate) {
		this.strInterestRate = strInterestRate;
	}

	public String getStrTxnId() {
		return strTxnId;
	}

	public void setStrTxnId(String strTxnId) {
		this.strTxnId = strTxnId;
	}

	public String getStrTranType() {
		return strTranType;
	}

	public void setStrTranType(String strTranType) {
		this.strTranType = strTranType;
	}

	public String getStrTxnAmount() {
		return strTxnAmount;
	}

	public void setStrTxnAmount(String strTxnAmount) {
		this.strTxnAmount = strTxnAmount;
	}

	public String getStrTranTypeDes() {
		return strTranTypeDes;
	}

	public void setStrTranTypeDes(String strTranTypeDes) {
		this.strTranTypeDes = strTranTypeDes;
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

	public String getStrRemaningGracePeriod() {
		return strRemaningGracePeriod;
	}

	public void setStrRemaningGracePeriod(String strRemaningGracePeriod) {
		this.strRemaningGracePeriod = strRemaningGracePeriod;
	}

	public Double getStrUpdateTxnAmount() {
		return strUpdateTxnAmount;
	}

	public void setStrUpdateTxnAmount(Double strUpdateTxnAmount) {
		this.strUpdateTxnAmount = strUpdateTxnAmount;
	}

	public Double getTotalOutstandingInterest() {
		return totalOutstandingInterest;
	}

	public void setTotalOutstandingInterest(Double totalOutstandingInterest) {
		this.totalOutstandingInterest = totalOutstandingInterest;
	}

	public Double getTotalCalGstAmount() {
		return totalCalGstAmount;
	}

	public void setTotalCalGstAmount(Double totalCalGstAmount) {
		this.totalCalGstAmount = totalCalGstAmount;
	}

	public Date getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}

	public Time getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(Time txnTime) {
		this.txnTime = txnTime;
	}
}
