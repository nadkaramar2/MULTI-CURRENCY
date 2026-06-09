package ams.cms.api.model;

import java.io.Serializable;

public class AccountCreditCardInterestResponse  implements Serializable

{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	private String strTotalOutstandingInterest;

	public String getStrTotalOutstandingInterest() {
		return strTotalOutstandingInterest;
	}

	public void setStrTotalOutstandingInterest(String strTotalOutstandingInterest) {
		this.strTotalOutstandingInterest = strTotalOutstandingInterest;
	}
	
}
