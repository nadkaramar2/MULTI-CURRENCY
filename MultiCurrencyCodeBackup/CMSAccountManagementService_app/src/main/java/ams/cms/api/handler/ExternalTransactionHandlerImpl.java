package ams.cms.api.handler;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.config.TransactionConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.TierAccountMaster;
import ams.cms.services.TierAccountMasterService;
import ams.cms.utility.Utils;

@Component
public class ExternalTransactionHandlerImpl implements ExternalTransactionHandler
{
	private AMSLogger amsLogger = AMSLogger.getInstance(ExternalTransactionHandlerImpl.class);
	
	@Autowired
	private TierAccountMasterService tierAccountMasterService;
	
	@Override
	public TransactionConfig getTierInfoByCustID(AccountCreation accountCreation) 
	{
		TransactionConfig transactionConfig = new TransactionConfig();
		try
		{
			 TierAccountMaster tierAccountMaster = new TierAccountMaster();
			 tierAccountMaster.setStrAccountNo(accountCreation.getStrAccountNumber());			 
			 
			 tierAccountMaster = tierAccountMasterService.getTierInfoByCustID(tierAccountMaster);
			 transactionConfig.setTierAccountMaster(tierAccountMaster);
			 
			 transactionConfig.setCode("S0000");	
			 transactionConfig.setMessage("success");
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}

	@Override
	public TransactionConfig validateDailyTxnTierLimits(TransactionConfig transactionConfig) {
		try
		{
			if(Utils.stringToDouble(transactionConfig.getStrDailyCumuTxnLimit()) < Utils.stringToDouble(transactionConfig.getTxnAmount()) )
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Daily Transaction Limit Breached");
			}
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
		}
		return transactionConfig;
	}

	@Override
	public TransactionConfig validateCummulativeTierLimit(TransactionConfig transactionConfig) 
	{
		try
		{
			AccountCreation accountCreation = transactionConfig.getAccountCreation();
			String toAccountAvailableBalance = accountCreation.getStrClosingBalance();
			
			double updatedBalance = Double.parseDouble(toAccountAvailableBalance) + Double.parseDouble(transactionConfig.getTxnAmount()) + accountCreation.getStrPreCredAmount() ;
			
			if(Utils.stringToDouble(transactionConfig.getTierAccountMaster().getStrCumulativeBalanceLimit()) < updatedBalance)
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Cummulative Balance Limit Breached.");
			}
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	
	@Override
	public TransactionConfig validateDailyLimits(TransactionConfig transactionConfig)
	{
		try
		{
			double txnAmount = Utils.stringToDouble(transactionConfig.getTxnAmount());			
			
			//Added for fee & vat start// 1000(txnAmount) + 100(fee) + 20(vat)
		    txnAmount = Utils.getUpdatedTxnAmount(transactionConfig);
		    //Added for fee & vat end
			
			TierAccountMaster tierAccountMaster = transactionConfig.getTierAccountMaster();
			if("tier1".equalsIgnoreCase(tierAccountMaster.getStrActiveTier()))
			{
				if(tierAccountMaster.getStrTier1DailyCumlimit() < txnAmount )
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage("Daily Transaction Limit Breached");
				}
				else 
				{
					if(tierAccountMaster.getStrAvailableTier1DailyCumlimit() < txnAmount )
					{
						transactionConfig.setCode("E0000");
						transactionConfig.setMessage("Daily Transaction Limit Breached");
					}
				}
			}
			else if ("tier2".equalsIgnoreCase(tierAccountMaster.getStrActiveTier()))
			{
				if(tierAccountMaster.getStrTier2DailyCumlimit() < txnAmount )
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage("Daily Transaction Limit Breached");
				}
				else
				{
					if(tierAccountMaster.getStrAvailableTier2DailyCumlimit() < txnAmount )
					{
						transactionConfig.setCode("E0000");
						transactionConfig.setMessage("Daily Transaction Limit Breached");
					}
				}
			}
			else
			{
				if(tierAccountMaster.getStrTier3DailyCumlimit() < txnAmount )
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage("Daily Transaction Limit Breached");
				}
				else
				{
					if(tierAccountMaster.getStrAvailableTier3DailyCumlimit() < txnAmount )
					{
						transactionConfig.setCode("E0000");
						transactionConfig.setMessage("Daily Transaction Limit Breached");
					}
				}
			}
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	
	@Override
	public TransactionConfig updateLimitValues(TransactionConfig transactionConfig) 
	{
		try
		{
			Double txnAmount = Utils.stringToDouble(transactionConfig.getTxnAmount());
			txnAmount = Utils.getUpdatedTxnAmount(transactionConfig);
			
			TierAccountMaster tierAccountMaster = transactionConfig.getTierAccountMaster();
			if("tier1".equalsIgnoreCase(tierAccountMaster.getStrActiveTier()))
			{
				double availbeTier1DailyLimit = tierAccountMaster.getStrAvailableTier1DailyCumlimit() - txnAmount;
				tierAccountMaster.setStrAvailableTier1DailyCumlimit(availbeTier1DailyLimit);
			}
			else if("tier2".equalsIgnoreCase(tierAccountMaster.getStrActiveTier()))
			{
				double availbeTier2DailyLimit = tierAccountMaster.getStrAvailableTier2DailyCumlimit() - txnAmount;
				tierAccountMaster.setStrAvailableTier2DailyCumlimit(availbeTier2DailyLimit);
			}
			else
			{
				double availbeTier3DailyLimit = tierAccountMaster.getStrAvailableTier3DailyCumlimit() - txnAmount;
				tierAccountMaster.setStrAvailableTier3DailyCumlimit(availbeTier3DailyLimit);
			}
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		amsLogger.writeInfoLog("Inside updateLimitValues transactionConfig::["+transactionConfig+"]");
		return transactionConfig;
	}
}
