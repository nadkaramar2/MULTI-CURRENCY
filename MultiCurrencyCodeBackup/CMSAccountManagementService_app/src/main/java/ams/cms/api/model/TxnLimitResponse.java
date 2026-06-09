package ams.cms.api.model;

import java.io.Serializable;
//created by ankit
public class TxnLimitResponse implements Serializable{

	private static final long serialVersionUID = 2600377581494911958L;

	private String strSingleTxnLimit;
	private String strDailyTxnLimit;
	private String strMonthlyTxnLimit;
	private String strYearlyTxnLimit;

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
}
