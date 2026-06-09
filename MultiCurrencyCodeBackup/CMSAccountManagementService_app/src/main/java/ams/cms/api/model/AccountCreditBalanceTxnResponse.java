package ams.cms.api.model;

import java.io.Serializable;
import java.util.Date;

public class AccountCreditBalanceTxnResponse implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String strMcc;
	
	private String strTransactionAmount;
	
	private String strTransactionType;
	
	private String strTxnDate;


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

	public String getStrTxnDate() {
		return strTxnDate;
	}

	public void setStrTxnDate(String strTxnDate) {
		this.strTxnDate = strTxnDate;
	}

	public String getStrTransactionType() {
		return strTransactionType;
	}

	public void setStrTransactionType(String strTransactionType) {
		this.strTransactionType = strTransactionType;
	}
   
}
