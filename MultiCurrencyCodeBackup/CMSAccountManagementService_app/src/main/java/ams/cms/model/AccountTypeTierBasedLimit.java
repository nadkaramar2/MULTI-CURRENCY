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
@Table(name = "account_type_tier_based_limit")
public class AccountTypeTierBasedLimit implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "tier_type")
	private String strTierType;

	@Column(name = "daily_cumulative_txn_limit")
	private String strDailyCumulativeTxnLimit;

	@Column(name = "cumulative_balance_limit")
	private String strCumulativeBalanceLimit;
	
	@Column(name = "created_date")
	private Date strCreatedDate;
	
	@Column(name = "created_time")
	private Time strCreatedTime;
	
	@Column(name = "created_by")
	private String strCreatedBy;
	
	@Transient
	private String strAvailableDailyCumulativeLimit;
	
	@Transient
	private String strMaxAssignDailyCumulativeLimit;
	
	@Transient
	private String strAccountNo;
	
	@Transient
	private String strCustId;
	
	@Transient
	private String cid;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrTierType() {
		return strTierType;
	}

	public void setStrTierType(String strTierType) {
		this.strTierType = strTierType;
	}

	public String getStrDailyCumulativeTxnLimit() {
		return strDailyCumulativeTxnLimit;
	}

	public void setStrDailyCumulativeTxnLimit(String strDailyCumulativeTxnLimit) {
		this.strDailyCumulativeTxnLimit = strDailyCumulativeTxnLimit;
	}

	public String getStrCumulativeBalanceLimit() {
		return strCumulativeBalanceLimit;
	}

	public void setStrCumulativeBalanceLimit(String strCumulativeBalanceLimit) {
		this.strCumulativeBalanceLimit = strCumulativeBalanceLimit;
	}

	public Date getStrCreatedDate() {
		return strCreatedDate;
	}

	public void setStrCreatedDate(Date strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}

	public Time getStrCreatedTime() {
		return strCreatedTime;
	}

	public void setStrCreatedTime(Time strCreatedTime) {
		this.strCreatedTime = strCreatedTime;
	}

	public String getStrCreatedBy() {
		return strCreatedBy;
	}

	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}

	public String getStrAvailableDailyCumulativeLimit() {
		return strAvailableDailyCumulativeLimit;
	}

	public void setStrAvailableDailyCumulativeLimit(String strAvailableDailyCumulativeLimit) {
		this.strAvailableDailyCumulativeLimit = strAvailableDailyCumulativeLimit;
	}

	public String getStrMaxAssignDailyCumulativeLimit() {
		return strMaxAssignDailyCumulativeLimit;
	}

	public void setStrMaxAssignDailyCumulativeLimit(String strMaxAssignDailyCumulativeLimit) {
		this.strMaxAssignDailyCumulativeLimit = strMaxAssignDailyCumulativeLimit;
	}

	public String getStrAccountNo() {
		return strAccountNo;
	}

	public void setStrAccountNo(String strAccountNo) {
		this.strAccountNo = strAccountNo;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getStrCustId() {
		return strCustId;
	}

	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}

	
}
