package ams.cms.api.model;

import java.io.Serializable;

public class WalletBalanceResponse implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String strParticipantID;
	
	private String strAccountType;
	
	private String strAccountNumber;
	
	private String strWalletAccountNumber;
	
	private String strMccCode;

	private String strPercentage;
	
	private String strAvailableBalance;

	public String getStrParticipantID() {
		return strParticipantID;
	}

	public void setStrParticipantID(String strParticipantID) {
		this.strParticipantID = strParticipantID;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrAccountNumber() {
		return strAccountNumber;
	}

	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}

	public String getStrWalletAccountNumber() {
		return strWalletAccountNumber;
	}

	public void setStrWalletAccountNumber(String strWalletAccountNumber) {
		this.strWalletAccountNumber = strWalletAccountNumber;
	}

	public String getStrMccCode() {
		return strMccCode;
	}

	public void setStrMccCode(String strMccCode) {
		this.strMccCode = strMccCode;
	}

	public String getStrPercentage() {
		return strPercentage;
	}

	public void setStrPercentage(String strPercentage) {
		this.strPercentage = strPercentage;
	}

	public String getStrAvailableBalance() {
		return strAvailableBalance;
	}

	public void setStrAvailableBalance(String strAvailableBalance) {
		this.strAvailableBalance = strAvailableBalance;
	}	

}
