package ams.cms.api.handler;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import ams.cms.api.model.AccountResponse;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.Utils;

@Component
public class CreditAccountTransactionHandlerImpl implements CreditAccountTransactionHandler
{
	private AMSLogger amsLogger = AMSLogger.getInstance(CreditAccountTransactionHandlerImpl.class);
	
	@Override
	public void addCreditCardRelatedEntry(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void updateSenderCreditAccountMaster(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}		
	}

	@Override
	public ProcessResponse validateCreditAccountLimits(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, AccountResponse accountResponse) 
	{
		try 
		{
			double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			
			double strAblCreditLimit = Utils.stringToDouble(accountResponse.getStrAvailableCreditLimit());
			
			if(strAblCreditLimit < txnAmount) 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Available Credit Limit balance Amount Exceeded."); 
			}		
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}

}
