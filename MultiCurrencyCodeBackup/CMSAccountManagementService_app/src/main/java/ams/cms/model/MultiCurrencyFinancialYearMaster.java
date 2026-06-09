package ams.cms.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@Entity
@Table(name = "multi_currency_financial_year_master")
@JsonAutoDetect
public class MultiCurrencyFinancialYearMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "account_number")
	private String accountNumber;
	
	@Column(name = "participant_id")
	private String strParticipantId;
	
	@Column(name = "cust_id")
	private String custId;
	
	@Column(name = "account_type")
	private String accountType;
	
	@Column(name = "financial_year")
	private String financialYear;
	
	
	@Column(name = "created_date")
	private Date createdDate ;
	
	@Column(name = "total_lrs_consumed")
	private double totalLrsConsumed;
	
	@Column(name = "total_tcs_on")
	private double totalTcsOn;
	
	@Column(name = "lrs_limit")
	private double lrsLimit ;
	

	@Column(name = "available_lrs_limit")
	private double availableLrsLimit ;
	
	@Column(name = "total_excess_loading")
	private double totalExcessLoading ;
	

	@Column(name = "total_loaded")
	private double totalLoaded ;
	
	@Column(name = "channel_code")
	private String channelCode ;
	
	
	
	
	
	
	public String getStrID() {
		return strID;
	}
	public void setStrID(String strID) {
		this.strID = strID;
	}
	public double getTotalLrsConsumed() {
		return totalLrsConsumed;
	}
	public void setTotalLrsConsumed(double totalLrsConsumed) {
		this.totalLrsConsumed = totalLrsConsumed;
	}
	public double getTotalTcsOn() {
		return totalTcsOn;
	}
	public void setTotalTcsOn(double totalTcsOn) {
		this.totalTcsOn = totalTcsOn;
	}
	public double getLrsLimit() {
		return lrsLimit;
	}
	public void setLrsLimit(double lrsLimit) {
		this.lrsLimit = lrsLimit;
	}
	public double getAvailableLrsLimit() {
		return availableLrsLimit;
	}
	public void setAvailableLrsLimit(double availableLrsLimit) {
		this.availableLrsLimit = availableLrsLimit;
	}
	public double getTotalExcessLoading() {
		return totalExcessLoading;
	}
	public void setTotalExcessLoading(double totalExcessLoading) {
		this.totalExcessLoading = totalExcessLoading;
	}
	public double getTotalLoaded() {
		return totalLoaded;
	}
	public void setTotalLoaded(double totalLoaded) {
		this.totalLoaded = totalLoaded;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}
	
	public String getStrParticipantId() {
		return strParticipantId;
	}
	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	
	
	

}
