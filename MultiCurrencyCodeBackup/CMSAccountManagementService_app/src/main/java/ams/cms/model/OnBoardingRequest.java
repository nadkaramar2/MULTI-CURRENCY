package ams.cms.model;

import java.io.Serializable;

public class OnBoardingRequest implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private CustomerInfo customerInfo;
	private String cid;
	private String bid;
	private String accountType;	

	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}
	public void setCustomerInfo(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
}
