package ams.cms.txn.handler;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.handler.ExternalTransactionHandler;
import ams.cms.config.CommonConstants;
import ams.cms.config.TransactionConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.utility.Utils;

@Component
public class TransactionValidatorImpl implements TransactionValidator 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(TransactionValidatorImpl.class);
	
	@Autowired
	private ExternalTransactionHandler externalTransactionHandler;
	
	@Override
	public TransactionConfig txnValidate(TransactionConfig transactionConfig) throws Exception 
	{
		amsLogger.writeInfoLog("Application Name::["+CommonConstants.applicationName+"]");
		transactionConfig.setCode("S0000");	
		
		AccountCreation accountCreation = transactionConfig.getAccountCreation();		
		
		if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
		{
			if(transactionConfig.getAccountTranType() != null && !transactionConfig.getAccountTranType().equalsIgnoreCase("DPT"))
			{
				if (!"C".equalsIgnoreCase(accountCreation.getAccountCategoryType())) 
				{
					TransactionConfig transactionConfigObj = externalTransactionHandler.getTierInfoByCustID(transactionConfig.getAccountCreation());
					
					transactionConfig.setTierAccountMaster(transactionConfigObj.getTierAccountMaster());
					transactionConfigObj.setTxnAmount(transactionConfig.getTxnAmount());
					
					transactionConfigObj = externalTransactionHandler.validateDailyLimits(transactionConfigObj);
					if (!"S0000".equalsIgnoreCase(transactionConfigObj.getCode())) 
					{
						transactionConfig.setCode("E0000");
						transactionConfig.setMessage(transactionConfigObj.getMessage());
					}
				}
			}
		}
		else
		{
			if(transactionConfig.getAccountTranType() != null && !transactionConfig.getAccountTranType().equalsIgnoreCase("DPT"))
			{
				transactionConfig = validateTxnLimits(transactionConfig);
			}
		}
		if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
		{
			if ("C".equalsIgnoreCase(accountCreation.getAccountCategoryType())) 
			{
				transactionConfig = validateCreditLimits(transactionConfig);
			}
			else
			{
				transactionConfig = validateBalance(transactionConfig);
			}
		}
		return transactionConfig;
	}
	
	private TransactionConfig validateTxnLimits(TransactionConfig transactionConfig)
	{
		try 
		{
			AccountCreation accountCreation = transactionConfig.getAccountCreation();		
			double txnAmount = Utils.stringToDouble(transactionConfig.getTxnAmount());
			
			//Added for fee & vat start// 1000(txnAmount) + 100(fee) + 20(vat)
		    txnAmount = Utils.getUpdatedTxnAmount(transactionConfig);
		    //Added for fee & vat end
			
			String availableDailyLimit = accountCreation.getStrAvailableDailyLimit();			
			if( Utils.stringToDouble(availableDailyLimit) < txnAmount )
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Daily Limit Amount Breached.");
				return transactionConfig; 
			}
			
			String availableMonthlyLimit = accountCreation.getStrAvailableMonthlyLimit();
			if(Utils.stringToDouble(availableMonthlyLimit) < txnAmount )
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Monthly Limit Amount Breached."); 
				return transactionConfig;
			}
			
			String availableYearlyLimit = accountCreation.getStrAvailableYearlyLimit();
			if(Utils.stringToDouble(availableYearlyLimit) < txnAmount )
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Yearly Limit Amount Breached.");
				return transactionConfig; 
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	
	private TransactionConfig validateCreditLimits(TransactionConfig transactionConfig) 
	{
		AccountCreation accountCreation = transactionConfig.getAccountCreation();
		double txnAmount = Utils.stringToDouble(transactionConfig.getTxnAmount());
		
		double strAblCreditLimit = Utils.stringToDouble(accountCreation.getStrAvailableCreditLimit());
		
		if(strAblCreditLimit < txnAmount) 
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Available Credit Limit balance Amount Exceeded."); 
		}
		return transactionConfig;
	}
	
	private TransactionConfig validateBalance(TransactionConfig transactionConfig)
	{
		AccountCreation accountCreation = transactionConfig.getAccountCreation();
		double txnAmount = Utils.stringToDouble(transactionConfig.getTxnAmount());
		
	    double availableBalance =  Utils.stringToDouble(accountCreation.getStrClosingBalance()) - accountCreation.getStrEarMarkAmount() ;//<-- added by sunil y , for withdraw pre_mark_Amt
		
	    //Added for fee & vat start// 1000(txnAmount) + 100(fee) + 20(vat)
	    txnAmount = Utils.getUpdatedTxnAmount(transactionConfig);
	    //Added for fee & vat end
	    
		if( availableBalance < txnAmount)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Insufficient Balance.");
		}
		return transactionConfig;
	}

	@Override
	public TransactionConfig validateCummulativeBalanceLimit(TransactionConfig transactionConfig) throws Exception
	{
		try
		{
			TransactionConfig transactionConfigObj = externalTransactionHandler.getTierInfoByCustID(transactionConfig.getAccountCreation());	
			transactionConfig.setTierAccountMaster(transactionConfigObj.getTierAccountMaster());
			
			transactionConfigObj.setAccountCreation(transactionConfig.getAccountCreation());
			transactionConfigObj.setTxnAmount(transactionConfig.getTxnAmount());
			
			transactionConfigObj.setStrPreCredAmount(transactionConfig.getStrPreCredAmount());
			
			transactionConfigObj = externalTransactionHandler.validateCummulativeTierLimit(transactionConfigObj);
			if (!"S0000".equalsIgnoreCase(transactionConfigObj.getCode())) 
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage(transactionConfig.getMessage());
			}
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal Server Error.");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}

	@Override
	public TransactionConfig validateAccount(TransactionConfig transactionConfig) throws Exception 
	{
		return null;
	}
}
