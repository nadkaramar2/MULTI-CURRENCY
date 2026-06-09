package ams.cms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonAutoDetect
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Entity
@Table(name = "load_money_txn_details")
public class LoadMoneyTxnDetails {
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id;
	
	@Column(name = "participant_id")
	private String  participantId;
	
	@Column(name = "lrs_limit")
	private Double lrsLimit;
	
	@Column(name = "account_type")
	private String accountType;
	
	@Column(name = "account_number")
	private String accountNumber;
	
	@Column(name = "fy_balance")
	private Double fyBalance;
	
	@Column(name = "load_amt")
	private Double loadAmt;
	
	@Column(name = "balanced_lrs")
	private Double balancedLRS;
	
	@Column(name = "total_lrs_consumed")
	private Double totalLrsConsumed;
	
	@Column(name = "total_excess_loading")
	private Double totalExcessLoading;
	
	@Column(name = "total_loaded")
	private Double totalLoaded;
	
	@Column(name = "tcs_on")
	private Double tcsOn;
	
	@Column(name = "total_tcs_on")
	private Double totalTcsOn;
	
	@Column(name = "tcs_calculated_amt")
	private Double tcsCalculatedAmt;
	
	@Column(name = "loaded_date")
	private Date loadedDate;
	
	@Column(name = "currency_code")
	private String currencyCode;

	public Date getLoadedDate() {
		return loadedDate;
	}

	public void setLoadedDate(Date loadedDate) {
		this.loadedDate = loadedDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getLrsLimit() {
		return lrsLimit;
	}

	public void setLrsLimit(Double lrsLimit) {
		this.lrsLimit = lrsLimit;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Double getFyBalance() {
		return fyBalance;
	}

	public void setFyBalance(Double fyBalance) {
		this.fyBalance = fyBalance;
	}

	public Double getLoadAmt() {
		return loadAmt;
	}

	public void setLoadAmt(Double loadAmt) {
		this.loadAmt = loadAmt;
	}


	public Double getBalancedLRS() {
		return balancedLRS;
	}

	public void setBalancedLRS(Double balancedLRS) {
		this.balancedLRS = balancedLRS;
	}

	public Double getTotalLrsConsumed() {
		return totalLrsConsumed;
	}

	public void setTotalLrsConsumed(Double totalLrsConsumed) {
		this.totalLrsConsumed = totalLrsConsumed;
	}

	public Double getTotalExcessLoading() {
		return totalExcessLoading;
	}

	public void setTotalExcessLoading(Double totalExcessLoading) {
		this.totalExcessLoading = totalExcessLoading;
	}

	public Double getTotalLoaded() {
		return totalLoaded;
	}

	public void setTotalLoaded(Double totalLoaded) {
		this.totalLoaded = totalLoaded;
	}

	public Double getTcsOn() {
		return tcsOn;
	}

	public void setTcsOn(Double tcsOn) {
		this.tcsOn = tcsOn;
	}

	public Double getTotalTcsOn() {
		return totalTcsOn;
	}

	public void setTotalTcsOn(Double totalTcsOn) {
		this.totalTcsOn = totalTcsOn;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public Double getTcsCalculatedAmt() {
		return tcsCalculatedAmt;
	}

	public void setTcsCalculatedAmt(Double tcsCalculatedAmt) {
		this.tcsCalculatedAmt = tcsCalculatedAmt;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
}
