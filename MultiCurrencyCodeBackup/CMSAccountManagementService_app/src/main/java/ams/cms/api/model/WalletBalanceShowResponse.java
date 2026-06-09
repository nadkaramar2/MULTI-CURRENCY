package ams.cms.api.model;

import java.io.Serializable;

public class WalletBalanceShowResponse implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String strAvailableBalance;
	private String strWalletAccountNumber;
	private String strPercentage;

	public String getStrAvailableBalance() 
	{
		return strAvailableBalance;
	}
	public void setStrAvailableBalance(String strAvailableBalance) 
	{
		this.strAvailableBalance = strAvailableBalance;
	}
	
	public String getStrWalletAccountNumber() 
	{
		return strWalletAccountNumber;
	}
	public void setStrWalletAccountNumber(String strWalletAccountNumber) 
	{
		this.strWalletAccountNumber = strWalletAccountNumber;
	}
	public String getStrPercentage() {
		return strPercentage;
	}
	public void setStrPercentage(String strPercentage) {
		this.strPercentage = strPercentage;
	}
	
}
