package ams.cms.scheduler.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class RevolvingCreditCardTxnMaster implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String strParticipantId;
	private String strAccountType;
	private String strAccountNumber;
	private String strMcc;
	private String strInterestRate;
	private String strTxnId;
	private String strTxnType;
	private Double strTxnAmount;
	private String strTranTypeDes;
	private Date TxnDate;
	private Time TxnTime;
	private String strRemaningGracePeriod;
	private Double strUpdateTxnAmount;
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
	public String getStrTxnType() {
		return strTxnType;
	}
	public void setStrTxnType(String strTxnType) {
		this.strTxnType = strTxnType;
	}
	public Double getStrTxnAmount() {
		return strTxnAmount;
	}
	public void setStrTxnAmount(Double strTxnAmount) {
		this.strTxnAmount = strTxnAmount;
	}
	public String getStrTranTypeDes() {
		return strTranTypeDes;
	}
	public void setStrTranTypeDes(String strTranTypeDes) {
		this.strTranTypeDes = strTranTypeDes;
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
	
}
