package ams.cms.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * Note -: same as that of AccountTransactionLimitation
 * created by ankit
 * */
@Entity
@Table(name="account_transaction_limit")
public class AccountTransactionLimit implements Serializable{

	private static final long serialVersionUID = 9115872746167118453L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "TEXT", name = "id", unique = true)

	private long strId;
	@Column(name = "participant_id")

	private String strParticipantId;
	@Column(name = "account_type", unique = true)

	private String strAccountType;
	@Column(name = "single_txn_limit", unique = true)

	private String strSingleTxnLimit;
	@Column(name = "daily_txn_limit", unique = true)

	private String strDailyTxnLimit;
	@Column(name = "monthly_txn_limit", unique = true)

	private String strMonthlyTxnLimit;
	@Column(name = "yearly_txn_limit", unique = true)

	private String strYearlyTxnLimit;
	@Column(name = "creation_date", unique = true)

	private Date strCreationDate;
	@Column(name = "created_by", unique = true)
	
	private String strCreatedBy;
	public long getStrId() {
		return strId;
	}
	public void setStrId(long strId) {
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
	public Date getStrCreationDate() {
		return strCreationDate;
	}
	public void setStrCreationDate(Date strCreationDate) {
		this.strCreationDate = strCreationDate;
	}
	public String getStrCreatedBy() {
		return strCreatedBy;
	}
	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}
}
