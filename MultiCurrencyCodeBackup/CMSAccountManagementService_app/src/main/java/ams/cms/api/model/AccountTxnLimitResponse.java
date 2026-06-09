package ams.cms.api.model;

import java.io.Serializable;

public class AccountTxnLimitResponse implements Serializable{

	private static final long serialVersionUID = 3980182342258712021L;
	
	private String strSingleTxnLimit;
	private String strPerDayAssignLimit;
	private String strMonthlyAssignLimit;
	private String strYearlyAssignLimit;
	
	private String strYearlyAvailableLimit;
	private String strPerDayAvailableLimit;
	private String strMonthlyAvailableLimit;
	
	private String strSingleTxnMaxLimit;	
	private String strPerDayMaxAssignedlimit;
	private String strMonthlyMaxAssignedLimit;
	private String strYearlyMaxAssignedLimit;
	
	public String getStrSingleTxnLimit() {
		return strSingleTxnLimit;
	}
	public void setStrSingleTxnLimit(String strSingleTxnLimit) {
		this.strSingleTxnLimit = strSingleTxnLimit;
	}
	public String getStrPerDayAssignLimit() {
		return strPerDayAssignLimit;
	}
	public void setStrPerDayAssignLimit(String strPerDayAssignLimit) {
		this.strPerDayAssignLimit = strPerDayAssignLimit;
	}
	public String getStrMonthlyAssignLimit() {
		return strMonthlyAssignLimit;
	}
	public void setStrMonthlyAssignLimit(String strMonthlyAssignLimit) {
		this.strMonthlyAssignLimit = strMonthlyAssignLimit;
	}
	public String getStrYearlyAssignLimit() {
		return strYearlyAssignLimit;
	}
	public void setStrYearlyAssignLimit(String strYearlyAssignLimit) {
		this.strYearlyAssignLimit = strYearlyAssignLimit;
	}
	public String getStrYearlyAvailableLimit() {
		return strYearlyAvailableLimit;
	}
	public void setStrYearlyAvailableLimit(String strYearlyAvailableLimit) {
		this.strYearlyAvailableLimit = strYearlyAvailableLimit;
	}
	public String getStrPerDayAvailableLimit() {
		return strPerDayAvailableLimit;
	}
	public void setStrPerDayAvailableLimit(String strPerDayAvailableLimit) {
		this.strPerDayAvailableLimit = strPerDayAvailableLimit;
	}
	public String getStrMonthlyAvailableLimit() {
		return strMonthlyAvailableLimit;
	}
	public void setStrMonthlyAvailableLimit(String strMonthlyAvailableLimit) {
		this.strMonthlyAvailableLimit = strMonthlyAvailableLimit;
	}
	public String getStrSingleTxnMaxLimit() {
		return strSingleTxnMaxLimit;
	}
	public void setStrSingleTxnMaxLimit(String strSingleTxnMaxLimit) {
		this.strSingleTxnMaxLimit = strSingleTxnMaxLimit;
	}
	public String getStrPerDayMaxAssignedlimit() {
		return strPerDayMaxAssignedlimit;
	}
	public void setStrPerDayMaxAssignedlimit(String strPerDayMaxAssignedlimit) {
		this.strPerDayMaxAssignedlimit = strPerDayMaxAssignedlimit;
	}
	public String getStrMonthlyMaxAssignedLimit() {
		return strMonthlyMaxAssignedLimit;
	}
	public void setStrMonthlyMaxAssignedLimit(String strMonthlyMaxAssignedLimit) {
		this.strMonthlyMaxAssignedLimit = strMonthlyMaxAssignedLimit;
	}
	public String getStrYearlyMaxAssignedLimit() {
		return strYearlyMaxAssignedLimit;
	}
	public void setStrYearlyMaxAssignedLimit(String strYearlyMaxAssignedLimit) {
		this.strYearlyMaxAssignedLimit = strYearlyMaxAssignedLimit;
	}
	
	
}