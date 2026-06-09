package ams.cms.txn.handler;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.handler.ExternalTransactionHandler;
import ams.cms.api.model.AccountMaster;
import ams.cms.config.CommonConstants;
import ams.cms.config.TransactionConfig;
import ams.cms.constants.TransactionType;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountStatement;
import ams.cms.model.AccountTypeMaster;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountStatementService;
import ams.cms.services.AccountTypeMasterService;
import ams.cms.services.TierAccountMasterService;
import ams.cms.utility.Utils;

@Component
public class AccountTransactionHandlerImpl implements AccountTransactionHandler
{
	private AMSLogger amsLogger = AMSLogger.getInstance(AccountTransactionHandlerImpl.class);
	
	@Autowired
	AccountStatementService accountStatementService;
	
	@Autowired
	AccountMasterService accountMasterService;
	
	@Autowired
	AccountTypeMasterService accountTypeMasterService;
	
	@Autowired
	ExternalTransactionHandler externalTransactionHandler;
	
	@Autowired
	TierAccountMasterService tierAccountMasterService;
	
	@Override
	public TransactionConfig creatingDebitAccountStatement(TransactionConfig transactionConfig) throws Exception 
	{
		AccountStatement accountStatement = new AccountStatement();		
		accountStatement.setStrAccountNumber(transactionConfig.getAccountNo());			
		accountStatement.setStrAccountType(transactionConfig.getAccountType());			
		accountStatement.setStrClosingBalance(transactionConfig.getAccountClosingBalance());			
		accountStatement.setStrTransactionAmount(transactionConfig.getTxnAmount());			
		accountStatement.setStrTransactionID(transactionConfig.getTxnId());			
		if (transactionConfig.getAccountTranType()!=null && transactionConfig.getAccountTranType().trim().length()>0) 
		{
			accountStatement.setStrTransactionType(transactionConfig.getAccountTranType().trim());		
		}
		else
		{
			accountStatement.setStrTransactionType("WDL");
		}
		if (transactionConfig.getAccountNaration()!=null && transactionConfig.getAccountNaration().trim().length() > 0)
		{
			accountStatement.setStrNaration(transactionConfig.getAccountNaration().trim());
		}
		else
		{
			accountStatement.setStrNaration("Amount Transfer Successfully.");
		}
		accountStatement.setStrTransactionMode(TransactionType.MODE.get(accountStatement.getStrTransactionType()));
		accountStatement.setStrIsGLType("N");
		accountStatementService.addAccountTransactionData(accountStatement);
		return transactionConfig;
	}

	@Override
	public TransactionConfig creatingCreditAccountStatement(TransactionConfig transactionConfig) throws Exception 
	{
		AccountStatement accountStatement = new AccountStatement();		
		accountStatement.setStrAccountNumber(transactionConfig.getAccountNo());			
		accountStatement.setStrAccountType(transactionConfig.getAccountType());			
		accountStatement.setStrClosingBalance(transactionConfig.getAccountClosingBalance());			
		accountStatement.setStrTransactionAmount(transactionConfig.getTxnAmount());			
		accountStatement.setStrTransactionID(transactionConfig.getTxnId());
		
		if (transactionConfig.getAccountTranType()!=null && transactionConfig.getAccountTranType().trim().length()>0) 
		{
			accountStatement.setStrTransactionType(transactionConfig.getAccountTranType().trim());		
		}
		else
		{
			accountStatement.setStrTransactionType("DPT");
		}
		if (transactionConfig.getAccountNaration()!=null && transactionConfig.getAccountNaration().trim().length() > 0)
		{
			accountStatement.setStrNaration(transactionConfig.getAccountNaration().trim());
		}
		else
		{
			accountStatement.setStrNaration("Amount Credited Successfully.");
		}		
		accountStatement.setStrTransactionMode(TransactionType.MODE.get(accountStatement.getStrTransactionType()));
		accountStatement.setStrIsGLType("N");
		accountStatementService.addAccountTransactionData(accountStatement);
		return transactionConfig;
	}

	@Override
	public TransactionConfig getReducingAccountBalance(TransactionConfig transactionConfig) throws Exception
	{
		amsLogger.writeInfoLog("Inside getReducingAccountBalance transactionConfig ::"+transactionConfig);
		
		Double txnAmount = Double.parseDouble(transactionConfig.getTxnAmount());
		Double closingBal = Double.parseDouble(transactionConfig.getAvailableAccountClosingBalance()) -  txnAmount;
		String updatedClosingBalance = Utils.decimalFormat.format(closingBal);
		
		AccountMaster accountMaster = transactionConfig.getAccountMaster();	
		accountMaster.setStrClosingBalance(updatedClosingBalance);
		
		amsLogger.writeInfoLog("Inside getReducingAccountBalance applicationName ::["+transactionConfig+"]");
		//Added Nigeria related information START
		if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
		{
			TransactionConfig tranConfig = new TransactionConfig();
			tranConfig.setTierAccountMaster(transactionConfig.getFromAccountTierAccountMaster());
			tranConfig.setTxnAmount(String.valueOf(txnAmount));
			
			tranConfig = externalTransactionHandler.updateLimitValues(tranConfig);
			amsLogger.writeInfoLog("Inside getReducingAccountBalance tranConfig ::["+tranConfig+"]");
			
			tierAccountMasterService.updateCummAvailableBalance(tranConfig.getTierAccountMaster());
		}
		//Added Nigeria related information End
		else
		{
			Double availableDailyAmount = Double.parseDouble(accountMaster.getStrAvailableDailyLimit()) - txnAmount;
			Double availableMonthlyAmount = Double.parseDouble(accountMaster.getStrAvailableMonthlyLimit()) - txnAmount;
			Double availableYearlyAmount = Double.parseDouble(accountMaster.getStrAvailableYearlyLimit()) - txnAmount;			
			
			accountMaster.setStrAvailableDailyLimit(Utils.decimalFormat.format(availableDailyAmount));			
			accountMaster.setStrAvailableMonthlyLimit(Utils.decimalFormat.format(availableMonthlyAmount));			
			accountMaster.setStrAvailableYearlyLimit(Utils.decimalFormat.format(availableYearlyAmount));
		}
		
		accountMasterService.updateAccountMasterFields(accountMaster);
		return transactionConfig;
	}

	@Override
	public TransactionConfig getIncreasingAccountBalance(TransactionConfig transactionConfig) throws Exception 
	{
		AccountMaster accountMaster = transactionConfig.getAccountMaster();
		Double closingBal = Double.parseDouble(transactionConfig.getAvailableAccountClosingBalance()) +  Double.parseDouble(transactionConfig.getTxnAmount());
		String updatedClosingBalance = Utils.decimalFormat.format(closingBal);
		accountMaster.setStrClosingBalance(updatedClosingBalance);
		accountMasterService.updateAccountMasterFields(accountMaster);
		return transactionConfig;
	}

	@Override
	public TransactionConfig getLinkedGLAccountTypeAndNo(TransactionConfig transactionConfig) throws Exception 
	{
		try
		{
			transactionConfig.setCode("S0000");
			AccountTypeMaster accountTypeMaster = transactionConfig.getAccountTypeMaster();
			List<AccountTypeMaster> glAccountTypeMasters = accountTypeMasterService.getGLAccontNoAndType(accountTypeMaster);
			if (glAccountTypeMasters!=null && glAccountTypeMasters.size() > 0) 
			{
				accountTypeMaster = glAccountTypeMasters.get(0);
				if (accountTypeMaster.getStrGLAccountType()!=null && accountTypeMaster.getStrGLAccountType().trim().length() > 0) 
				{
					transactionConfig.setAccountTypeMaster(accountTypeMaster);
				}
				else
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage("No Linked GL Account Found!.");
				}
			}
			else
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("No Linked GL Account Found!!.");
			}			
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
}
