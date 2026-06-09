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

@JsonAutoDetect
@Entity
@Table(name = "dormant_account_master")
public class DormantAccountMaster implements Serializable 
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id;
	
	@Column(name = "account_number")
	private String accountNumber;
	
	@Column(name = "account_opened_date")
	private Date accountOpenedDate;
	
	@Column(name = "account_dormant_date")
	private Date accountDormantDate;
	
	@Column(name = "account_balance")
	private String accountBalance;	
	
	@Column(name = "account_dormant_released_date")
	private Date accountDormantReleasedDate;
	
	@Column(name = "dormant_released_reason")
	private String dormantReleasedReason;
	
	@Column(name = "released_by")
	private String releasedBy;
	
	@Transient
	private String reason;	

	@Transient
	private String status;

	@Transient
	private String makerId;
	
	@Transient
	private String checkerId;
	
	@Transient
	private String accountType;
	
	@Transient
	private String strAccountHolderName;
	
	@Transient
	private String description;
	
	@Transient
	private String creationDate;
	
	@Transient
	private String lastTxnDate;
	
	@Transient
	private String closingBalance;
	
	@Transient
	private String dormantMarkedDate;
	
	public String getStrAccountHolderName() {
		return strAccountHolderName;
	}

	public void setStrAccountHolderName(String strAccountHolderName) {
		this.strAccountHolderName = strAccountHolderName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastTxnDate() {
		return lastTxnDate;
	}

	public void setLastTxnDate(String lastTxnDate) {
		this.lastTxnDate = lastTxnDate;
	}

	public String getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(String closingBalance) {
		this.closingBalance = closingBalance;
	}

	public String getDormantMarkedDate() {
		return dormantMarkedDate;
	}

	public void setDormantMarkedDate(String dormantMarkedDate) {
		this.dormantMarkedDate = dormantMarkedDate;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMakerId() {
		return makerId;
	}

	public void setMakerId(String makerId) {
		this.makerId = makerId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Date getAccountOpenedDate() {
		return accountOpenedDate;
	}

	public void setAccountOpenedDate(Date accountOpenedDate) {
		this.accountOpenedDate = accountOpenedDate;
	}

	public Date getAccountDormantDate() {
		return accountDormantDate;
	}

	public void setAccountDormantDate(Date accountDormantDate) {
		this.accountDormantDate = accountDormantDate;
	}

	public String getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}

	public Date getAccountDormantReleasedDate() {
		return accountDormantReleasedDate;
	}

	public void setAccountDormantReleasedDate(Date accountDormantReleasedDate) {
		this.accountDormantReleasedDate = accountDormantReleasedDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDormantReleasedReason() {
		return dormantReleasedReason;
	}

	public void setDormantReleasedReason(String dormantReleasedReason) {
		this.dormantReleasedReason = dormantReleasedReason;
	}

	public String getReleasedBy() {
		return releasedBy;
	}

	public void setReleasedBy(String releasedBy) {
		this.releasedBy = releasedBy;
	}
	public String getCheckerId() {
		return checkerId;
	}

	public void setCheckerId(String checkerId) {
		this.checkerId = checkerId;
	}
}
