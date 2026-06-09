package ams.cms.api.model;

import java.io.Serializable;

public class CumulativelimitResponse implements Serializable 
{
	private static final long serialVersionUID = 1L;
    
	private String strDailyCumulativeTxnLimit;
	
	private String strAvailableDailyCumulativeLimit;
    
	private String strCumulativeBalanceLimit;
	
	private String strMaxAssignDailyCumulativeLimit;

	public String getStrDailyCumulativeTxnLimit() {
		return strDailyCumulativeTxnLimit;
	}

	public String getStrAvailableDailyCumulativeLimit() {
		return strAvailableDailyCumulativeLimit;
	}

	public void setStrAvailableDailyCumulativeLimit(String strAvailableDailyCumulativeLimit) {
		this.strAvailableDailyCumulativeLimit = strAvailableDailyCumulativeLimit;
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

	public String getStrMaxAssignDailyCumulativeLimit() {
		return strMaxAssignDailyCumulativeLimit;
	}

	public void setStrMaxAssignDailyCumulativeLimit(String strMaxAssignDailyCumulativeLimit) {
		this.strMaxAssignDailyCumulativeLimit = strMaxAssignDailyCumulativeLimit;
	}

}
