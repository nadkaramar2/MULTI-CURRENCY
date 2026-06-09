package ams.cms.model;

public class LrsLimitChecker {
	
	private String accounttype;
	private String channelName;
	private String lrsAssignedLimit;
	private String lrsLimitConsumed;
	private String tcsApplied;
	private String totalAmtLoaded;
	
	public String getAccounttype() {
		return accounttype;
	}
	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getLrsAssignedLimit() {
		return lrsAssignedLimit;
	}
	public void setLrsAssignedLimit(String lrsAssignedLimit) {
		this.lrsAssignedLimit = lrsAssignedLimit;
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
	public String getTotalAmtLoaded() {
		return totalAmtLoaded;
	}
	public void setTotalAmtLoaded(String totalAmtLoaded) {
		this.totalAmtLoaded = totalAmtLoaded;
	}
	
	

}
