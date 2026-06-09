package ams.cms.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;


/**
 * @author Admin
 *
 */
public class AccountStatementResponse implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String strParticipantId;

	private String strAccountNumber;
	
	private String strAccountType;
	
	private String strClosingBalance;
	
	private String strTransactionAmount;
	
	private String strTransactionDate;
	
	private String strTransactionID;
	
	private String strTransactionType;
	
	private String strTransactionMode;
	
	private String currencyCode;
   
	String getStrParticipantId() {
		return strParticipantId;
	}

	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}

	public String getStrAccountNumber() {
		return strAccountNumber;
	}

	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrClosingBalance() {
		return strClosingBalance;
	}

	public void setStrClosingBalance(String strClosingBalance) {
		this.strClosingBalance = strClosingBalance;
	}

	public String getStrTransactionAmount() {
		return strTransactionAmount;
	}

	public void setStrTransactionAmount(String strTransactionAmount) {
		this.strTransactionAmount = strTransactionAmount;
	}

	public String getStrTransactionID() {
		return strTransactionID;
	}

	public void setStrTransactionID(String strTransactionID) {
		this.strTransactionID = strTransactionID;
	}

	public String getStrTransactionType() {
		return strTransactionType;
	}

	public void setStrTransactionType(String strTransactionType) {
		this.strTransactionType = strTransactionType;
	}

	public String getStrTransactionMode() {
		return strTransactionMode;
	}

	public void setStrTransactionMode(String strTransactionMode) {
		this.strTransactionMode = strTransactionMode;
	}

	public String getStrTransactionDate() {
		return strTransactionDate;
	}

	public void setStrTransactionDate(String strTransactionDate) {
		this.strTransactionDate = strTransactionDate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	
	

}
