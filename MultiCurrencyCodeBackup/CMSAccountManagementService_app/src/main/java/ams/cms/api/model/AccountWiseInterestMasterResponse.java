package ams.cms.api.model;

import java.io.Serializable;

public class AccountWiseInterestMasterResponse  implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String strMcc;
	private String strTransactionAmount;
	private String strInterestCalculateDate;
    private String strCalculatInterest;
    
	public String getStrMcc() {
		return strMcc;
	}
	public void setStrMcc(String strMcc) {
		this.strMcc = strMcc;
	}
	public String getStrTransactionAmount() {
		return strTransactionAmount;
	}
	public void setStrTransactionAmount(String strTransactionAmount) {
		this.strTransactionAmount = strTransactionAmount;
	}
	public String getStrInterestCalculateDate() {
		return strInterestCalculateDate;
	}
	public void setStrInterestCalculateDate(String strInterestCalculateDate) {
		this.strInterestCalculateDate = strInterestCalculateDate;
	}
	public String getStrCalculatInterest() {
		return strCalculatInterest;
	}
	public void setStrCalculatInterest(String strCalculatInterest) {
		this.strCalculatInterest = strCalculatInterest;
	}
 
}
