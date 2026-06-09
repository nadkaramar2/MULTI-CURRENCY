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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ams.cms.util.JsonDateDeserializer;
import ams.cms.util.JsonDateSerializer;

@JsonAutoDetect
@Entity
@Table(name = "account_transaction_limit")
public class AccountTransactionLimitation implements Serializable
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
	
	@Column(name = "single_txn_limit")
	private String strSingleTxnLimit;
	
	@Column(name = "daily_txn_limit")
	private String strDailyTxnLimit;
	
	@Column(name = "monthly_txn_limit")
	private String strMonthlyTxnLimit;
	
	@Column(name = "yearly_txn_limit")
	private String strYearlyTxnLimit;
	
	@Column(name = "created_by")
	private String strCreatedBy;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
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

	public String getStrSingleTxnLimit() {
		return strSingleTxnLimit;
	}

	public void setStrSingleTxnLimit(String strSingleTxnLimit) {
		this.strSingleTxnLimit = strSingleTxnLimit;
	}

	public String getStrDailyTxnLimit() {
		return strDailyTxnLimit;
	}

	public void setStrDailyTxnLimit(String strDailyTxnLimit) {
		this.strDailyTxnLimit = strDailyTxnLimit;
	}

	public String getStrMonthlyTxnLimit() {
		return strMonthlyTxnLimit;
	}

	public void setStrMonthlyTxnLimit(String strMonthlyTxnLimit) {
		this.strMonthlyTxnLimit = strMonthlyTxnLimit;
	}

	public String getStrYearlyTxnLimit() {
		return strYearlyTxnLimit;
	}

	public void setStrYearlyTxnLimit(String strYearlyTxnLimit) {
		this.strYearlyTxnLimit = strYearlyTxnLimit;
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
