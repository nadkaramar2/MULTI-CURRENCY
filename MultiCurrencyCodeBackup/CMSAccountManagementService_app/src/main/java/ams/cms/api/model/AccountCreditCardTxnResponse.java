package ams.cms.api.model;

import java.io.Serializable;

public class AccountCreditCardTxnResponse implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String strTotalOutstandingBal;

	public String getStrTotalOutstandingBal() {
		return strTotalOutstandingBal;
	}

	public void setStrTotalOutstandingBal(String strTotalOutstandingBal) {
		this.strTotalOutstandingBal = strTotalOutstandingBal;
	}


}
