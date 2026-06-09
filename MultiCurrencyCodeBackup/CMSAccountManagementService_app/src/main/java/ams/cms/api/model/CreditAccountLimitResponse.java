package ams.cms.api.model;

import java.io.Serializable;

public class CreditAccountLimitResponse implements Serializable
{
	private static final long serialVersionUID = -5199400654034170382L;
	private String strTxnLimit;
	private String strPerDayAssignedLimit;
	private String strPerDayAvailableLimit;
	private String strMonthlyAssignedLimit;
	private String strMonthlyAvailableLimit;
	public String getStrTxnLimit() {
		return strTxnLimit;
	}
	public void setStrTxnLimit(String strTxnLimit) {
		this.strTxnLimit = strTxnLimit;
	}
	public String getStrPerDayAssignedLimit() {
		return strPerDayAssignedLimit;
	}
	public void setStrPerDayAssignedLimit(String strPerDayAssignedLimit) {
		this.strPerDayAssignedLimit = strPerDayAssignedLimit;
	}
	public String getStrPerDayAvailableLimit() {
		return strPerDayAvailableLimit;
	}
	public void setStrPerDayAvailableLimit(String strPerDayAvailableLimit) {
		this.strPerDayAvailableLimit = strPerDayAvailableLimit;
	}
	public String getStrMonthlyAssignedLimit() {
		return strMonthlyAssignedLimit;
	}
	public void setStrMonthlyAssignedLimit(String strMonthlyAssignedLimit) {
		this.strMonthlyAssignedLimit = strMonthlyAssignedLimit;
	}
	public String getStrMonthlyAvailableLimit() {
		return strMonthlyAvailableLimit;
	}
	public void setStrMonthlyAvailableLimit(String strMonthlyAvailableLimit) {
		this.strMonthlyAvailableLimit = strMonthlyAvailableLimit;
	}

}
