package ams.cms.utility;

import java.util.HashMap;
import java.util.List;

import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.AgencyBankingResponse;
import ams.cms.api.model.TierAccountResponse;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.model.GLAccountTypeMaster;

public class AccountUtility 
{
	public static Double getTxnAmount(TransactionPostingConfig transactionPostingConfig) 
	{
		double result = 0d;
		try 
		{
			double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());			
			if (transactionPostingConfig.getFee() != null && transactionPostingConfig.getFee().trim().length() > 0) 
		    {
		    	txnAmount = txnAmount + Utils.stringToDouble(transactionPostingConfig.getFee().trim());
		    	if (transactionPostingConfig.getVat() != null && transactionPostingConfig.getVat().trim().length() > 0) 
		    	{
		    		txnAmount = txnAmount + Utils.stringToDouble(transactionPostingConfig.getVat().trim());
		    	}
		    }
			result = txnAmount;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	public static Double getUpdatedTxnAmount(String txnAmount, AccountResponse accountResponse, TierAccountResponse tierAccountResponse)   
	{
		TransactionPostingConfig transactionPostingConf = new TransactionPostingConfig();
		transactionPostingConf.setTxnAmount(txnAmount);
		
		if (accountResponse != null && accountResponse.getFee()!=null && accountResponse.getVat()!=null)
		{
			transactionPostingConf.setFee(accountResponse.getFee());
			transactionPostingConf.setVat(accountResponse.getVat());
		}
		else 
		{
			transactionPostingConf.setFee(tierAccountResponse.getFee());
			transactionPostingConf.setVat(tierAccountResponse.getVat());
		}
		
		return getTxnAmount(transactionPostingConf);
	}
	public static Double getUpdatedGLTxnAmount(String txnAmount, GLAccountTypeMaster linkedGLAccountTypeMaster) 
	{
		TransactionPostingConfig transactionPostingConf = new TransactionPostingConfig();
		transactionPostingConf.setTxnAmount(txnAmount);
		
		transactionPostingConf.setFee(linkedGLAccountTypeMaster.getFee());
		transactionPostingConf.setVat(linkedGLAccountTypeMaster.getVat());
		
		return getTxnAmount(transactionPostingConf);
	}
	public static Double getUpdatedBalance(String txnAmount, AccountResponse accountResponse) 
	{
		String updatedStrBalance =  Utils.getIncreaseAmount(accountResponse.getStrClosingBalance(), String.valueOf(txnAmount));
		return Utils.stringToDouble(updatedStrBalance);
	}
	public static String getUpdatedAvailableBalance(String availableBalance, String txnAmount, String fee, String vat) 
	{
		Double availableBal = 0d;
		try 
		{
			availableBal = Utils.stringToDouble(availableBalance);			
			if(txnAmount != null && txnAmount.trim().length()>0) 
			{
				availableBal = availableBal - Utils.stringToDouble(txnAmount.trim());
			}
			if(fee != null && fee.trim().length()>0) 
			{
				availableBal = availableBal - Utils.stringToDouble(fee.trim());				
			}
			if( vat != null && vat.trim().length()>0) 
			{
				availableBal = availableBal - Utils.stringToDouble(vat.trim());
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return Utils.decimalFormat.format(availableBal);
	}
	public static Double getFeeVatValue(AccountResponse accountResponse) 
	{
		return Utils.stringToDouble(accountResponse.getFee()) + Utils.stringToDouble(accountResponse.getVat());
	}
	public static AccountResponse getAccountResponseWithBalanceUpdation(AccountResponse accountResponse) 
	{
		try 
		{
			String updatedBalance = accountResponse.getStrClosingBalance();
			if (accountResponse.getStrPreCredAmount()!=null) 
			{
				updatedBalance = Utils.getIncreaseAmount(updatedBalance, String.valueOf(accountResponse.getStrPreCredAmount()));
			}
			if (accountResponse.getStrEarMarkAmount()!=null) 
			{
				updatedBalance = Utils.getReduceAmount(updatedBalance, String.valueOf(accountResponse.getStrEarMarkAmount()));
			}
			accountResponse.setStrClosingBalance(updatedBalance);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return accountResponse;
	}
	public static Double getAccountUpdatedBalance(AccountResponse accountResponse) 
	{
		Double result = 0d;
		try 
		{
			String accountBalance = accountResponse.getStrClosingBalance();
			result = Utils.stringToDouble(accountBalance);
			
			if (accountResponse.getStrPreCredAmount()!=null) 
			{
				accountBalance = Utils.getIncreaseAmount(accountBalance, String.valueOf(accountResponse.getStrPreCredAmount()));
			}
			if (accountResponse.getStrEarMarkAmount()!=null) 
			{
				accountBalance = Utils.getReduceAmount(accountBalance, String.valueOf(accountResponse.getStrEarMarkAmount()));
			}
			result = Utils.stringToDouble(accountBalance);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public static TierAccountResponse getTierAccountResponse(AccountResponse accountResponse) 
	{
		TierAccountResponse tierAccountMaster = new TierAccountResponse();
		try 
		{
			tierAccountMaster.setStrCustId(accountResponse.getStrCustId());
			tierAccountMaster.setStrAccountNumber(accountResponse.getStrAccountNumber());
			tierAccountMaster.setAccountAvailableBalance(accountResponse.getStrClosingBalance());
			
			tierAccountMaster.setStrActiveTier(accountResponse.getStrActiveTier());
			
			tierAccountMaster.setTier1CumBalance(accountResponse.getTier1CumBalance());
			tierAccountMaster.setTier2CumBalance(accountResponse.getTier2CumBalance());
			tierAccountMaster.setTier3CumBalance(accountResponse.getTier3CumBalance());
			
			tierAccountMaster.setStrTier1DailyCumlimit(accountResponse.getStrTier1DailyCumlimit());
			tierAccountMaster.setStrTier2DailyCumlimit(accountResponse.getStrTier2DailyCumlimit());
			tierAccountMaster.setStrTier3DailyCumlimit(accountResponse.getStrTier3DailyCumlimit());
			
			tierAccountMaster.setAvailableTier1DailyCumLimit(accountResponse.getAvailableTier1DailyCumLimit());
			tierAccountMaster.setAvailableTier2DailyCumLimit(accountResponse.getAvailableTier2DailyCumLimit());
			tierAccountMaster.setAvailableTier3DailyCumLimit(accountResponse.getAvailableTier3DailyCumLimit());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return tierAccountMaster;
	}
	
	public static GLAccountTypeMaster getGLAccountTypeMaster(AccountResponse senderAccountResponse) 
	{
		GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
		try 
		{
			glAccountTypeMaster.setStrGLAccountType(senderAccountResponse.getStrGLAccountType());
			glAccountTypeMaster.setStrAccountNumber(senderAccountResponse.getStrGLAccountNo());
			glAccountTypeMaster.setStrGLAccountDescription(senderAccountResponse.getStrGLAccountDescription());
			glAccountTypeMaster.setStrClosingBalance(senderAccountResponse.getStrGLAccountBalance());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return glAccountTypeMaster;
	} 
	
	public static boolean isNumeric(String strNum) 
	{
	    if (strNum == null) 
	    {
	        return false;
	    }
	    try 
	    {
	        Double.parseDouble(strNum);
	    } 
	    catch (NumberFormatException nfe) 
	    {
	        return false;
	    }
	    return true;
	}
	public static boolean isGreaterThanZero(String strNum) 
	{
		try 
		{
			double d = Double.parseDouble(strNum);
			System.out.println("d=["+d+"]");
			if (d > 0d) 
			{
				return true;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static HashMap<String, GLAccountTypeMaster> getFeeVatAndParkingGLResponseMap(List<AgencyBankingResponse> agencyBankingResponseList) 
	{
		HashMap<String, GLAccountTypeMaster> map = new HashMap<String, GLAccountTypeMaster>();
		try 
		{
			for(AgencyBankingResponse agencyBankingResponse : agencyBankingResponseList) 
			{
				String reserveFieldValue = agencyBankingResponse.getReceipentAccountNo();				
				if (reserveFieldValue != null && reserveFieldValue.trim().length() > 0) 
				{
					reserveFieldValue = reserveFieldValue.trim();
					
					GLAccountTypeMaster glAccountTypeMaster = getGLAccountTypeMasterObject(agencyBankingResponse);
					if ("fee_charged".equalsIgnoreCase(reserveFieldValue)) 
					{
						map.put("fee_gl", glAccountTypeMaster);
					}
					else if("vat_charged".equalsIgnoreCase(reserveFieldValue)) 
					{
						map.put("vat_gl", glAccountTypeMaster);
					}
					else 
					{
						map.put("parking_gl", glAccountTypeMaster);
					}
				}				
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return map;
	}
	
	public static AgencyBankingResponse getRecipentAgencyBankingResponse(List<AgencyBankingResponse> agencyBankingResponseList) 
	{
		AgencyBankingResponse agencyBankingResponse = null;
		for(AgencyBankingResponse agencyBankingResponseData: agencyBankingResponseList) 
		{
			if (agencyBankingResponseData.getReceipentAccountNo()!=null && agencyBankingResponseData.getReceipentAccountNo().trim().length() > 0) 
			{
				if (!("fee_charged".equalsIgnoreCase(agencyBankingResponseData.getReceipentAccountNo().trim()))) 
				{
					if (!("vat_charged".equalsIgnoreCase(agencyBankingResponseData.getReceipentAccountNo().trim()))) 
					{
						agencyBankingResponse = agencyBankingResponseData;
						break;
					}
				}
			}
		}
		return agencyBankingResponse;
	}
	
	public static HashMap<String, GLAccountTypeMaster> getFeeVatAndOtherGLResponseMap(List<AgencyBankingResponse> agencyBankingResponseList) 
	{
		HashMap<String, GLAccountTypeMaster> map = new HashMap<String, GLAccountTypeMaster>();
		try 
		{
			for(AgencyBankingResponse agencyBankingResponse : agencyBankingResponseList) 
			{
				String reserveFieldValue = agencyBankingResponse.getReceipentAccountNo();				
				if (reserveFieldValue != null && reserveFieldValue.trim().length() > 0) 
				{
					reserveFieldValue = reserveFieldValue.trim();
					
					GLAccountTypeMaster glAccountTypeMaster = getGLAccountTypeMasterObject(agencyBankingResponse);
					if ("fee_charged".equalsIgnoreCase(reserveFieldValue)) 
					{
						map.put("fee_gl", glAccountTypeMaster);
					}
					else if("vat_charged".equalsIgnoreCase(reserveFieldValue)) 
					{
						map.put("vat_gl", glAccountTypeMaster);
					}
					else 
					{
						//
					}
				}
				else 
				{
					GLAccountTypeMaster glAccountTypeMaster = getGLAccountTypeMasterObject(agencyBankingResponse);
					map.put("other_gl", glAccountTypeMaster);
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return map;
	}
	private static GLAccountTypeMaster getGLAccountTypeMasterObject(AgencyBankingResponse agencyBankingResponse) 
	{
		GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
		try 
		{
			glAccountTypeMaster.setStrGLAccountType(agencyBankingResponse.getGlAccountType());
			glAccountTypeMaster.setStrAccountNumber(agencyBankingResponse.getGlAccountNo());
			glAccountTypeMaster.setStrGLAccountDescription(agencyBankingResponse.getGlAccountDescription());
			glAccountTypeMaster.setStrClosingBalance(agencyBankingResponse.getGlAcccounBalance());
			glAccountTypeMaster.setTxnAmount(agencyBankingResponse.getTxnAmount());			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return glAccountTypeMaster;
	}
}
