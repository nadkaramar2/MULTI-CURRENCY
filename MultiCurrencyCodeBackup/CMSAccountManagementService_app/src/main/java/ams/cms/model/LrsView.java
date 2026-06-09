package ams.cms.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class LrsView {
	
	private boolean isChannelConfigured;
	private String financialYear;
	private String currentDate;
	private List<ChannelLrsDetails>  channelLrsDetails;
	private String lrsAssigendLimit;
	private String lrsLimitConsumed;
	private String tcsApplied;
	private String totalAmountLoaded;
	
	
	public boolean isChannelConfigured() {
		return isChannelConfigured;
	}
	public void setChannelConfigured(boolean isChannelConfigured) {
		this.isChannelConfigured = isChannelConfigured;
	}
	public String getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}
	public String getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	public List<ChannelLrsDetails> getChannelLrsDetails() {
		return channelLrsDetails;
	}
	public void setChannelLrsDetails(List<ChannelLrsDetails> channelLrsDetails) {
		this.channelLrsDetails = channelLrsDetails;
	}
	public String getLrsAssigendLimit() {
		return lrsAssigendLimit;
	}
	public void setLrsAssigendLimit(String lrsAssigendLimit) {
		this.lrsAssigendLimit = lrsAssigendLimit;
	}
	public String getLrsLimitConsumed() {
		return lrsLimitConsumed;
	}
	public void setLrsLimitConsumed(String lrsLimitConsumed) {
		this.lrsLimitConsumed = lrsLimitConsumed;
	}
	public String getTcsApplied() {
		return tcsApplied;
	}
	public void setTcsApplied(String tcsApplied) {
		this.tcsApplied = tcsApplied;
	}
	public String getTotalAmountLoaded() {
		return totalAmountLoaded;
	}
	public void setTotalAmountLoaded(String totalAmountLoaded) {
		this.totalAmountLoaded = totalAmountLoaded;
	}
	
	
	
	
	

}
