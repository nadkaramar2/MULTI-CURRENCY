package ams.cms.txn.handler;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.config.TransactionConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.services.GLAccountStatementService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.utility.Utils;

@Component
public class GLAccountTxnHandlerImpl implements GLAccountTxnHandler
{
	private AMSLogger amsLogger = AMSLogger.getInstance(GLAccountTxnHandlerImpl.class);
	
	@Autowired
	GLAccountTypeMasterService glAccountTypeMasterService;
	
	@Autowired
	GLAccountStatementService glAccountStatementService;

	@Override
	public TransactionConfig increaseGLAccountBalance(TransactionConfig transactionConfig) 
	{
		transactionConfig.setCode("S0000");		
		if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
		{
			GLAccountTypeMaster	glAccountTypeMaster = transactionConfig.getGlAccountTypeMaster();
			
			String closingBalance = glAccountTypeMaster.getStrClosingBalance();
			closingBalance = (closingBalance != null && Double.parseDouble(closingBalance) > 0) ? closingBalance : "0";
			
			Double closingBalanceInDouble = Double.parseDouble(glAccountTypeMaster.getStrClosingBalance());
			
			closingBalanceInDouble = closingBalanceInDouble + Double.parseDouble(transactionConfig.getTxnAmount());
			
			String updatedClosingBalance = Utils.decimalFormat.format(closingBalanceInDouble);
			amsLogger.writeInfoLog("updateGLAccountTypeMaster:: updatedClosingBalance=["+updatedClosingBalance+"]");
			glAccountTypeMaster.setStrClosingBalance(updatedClosingBalance);
			
			int count =	glAccountTypeMasterService.updateGLAccountTypeDetails(glAccountTypeMaster);
			if (count == 0) 
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Error while updating gl Account type!");
			}
		}
		return transactionConfig;
	}

	@Override
	public TransactionConfig decreaseGLAccountBalance(TransactionConfig transactionConfig) 
	{
		transactionConfig.setCode("S0000");		
		if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
		{
			GLAccountTypeMaster	glAccountTypeMaster = transactionConfig.getGlAccountTypeMaster();
			
			String closingBalance = glAccountTypeMaster.getStrClosingBalance();
			closingBalance = (closingBalance != null && Double.parseDouble(closingBalance) > 0) ? closingBalance : "0";
			
			Double closingBalanceInDouble = Double.parseDouble(glAccountTypeMaster.getStrClosingBalance());
			
			closingBalanceInDouble = closingBalanceInDouble - Double.parseDouble(transactionConfig.getTxnAmount());
			
			String updatedClosingBalance = Utils.decimalFormat.format(closingBalanceInDouble);
			amsLogger.writeInfoLog("updateGLAccountTypeMaster:: updatedClosingBalance=["+updatedClosingBalance+"]");
			glAccountTypeMaster.setStrClosingBalance(updatedClosingBalance);
			
			int count =	glAccountTypeMasterService.updateGLAccountTypeDetails(glAccountTypeMaster);
			if (count == 0) 
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Error while updating gl Account type!!");
			}
		}
		return transactionConfig;
	}

	@Override
	public TransactionConfig insertGLAccountCreditEntryInStatement(TransactionConfig transactionConfig)
	{
		try 
		{
			GLAccountTypeMaster	glAccountTypeMaster = transactionConfig.getGlAccountTypeMaster();
			
			GLAccountStatement gLAccountStatement = new GLAccountStatement();
			gLAccountStatement.setStrGLAccountType(glAccountTypeMaster.getStrGLAccountType());
			gLAccountStatement.setStrRef(glAccountTypeMaster.getStrGLAccountDescription());		
			gLAccountStatement.setStrAccountNumber(glAccountTypeMaster.getStrAccountNumber());		
			gLAccountStatement.setTransactionDate(Utils.getCurrentDate());		
			
			Double closingBalanceInDouble = Double.parseDouble(glAccountTypeMaster.getStrClosingBalance());
			gLAccountStatement.setStrClosingBalance(Utils.decimalFormat.format(closingBalanceInDouble));
			
			Double txnAmountInDouble = Double.parseDouble(transactionConfig.getTxnAmount());			
			gLAccountStatement.setStrAmount(Utils.decimalFormat.format(txnAmountInDouble));
			
			gLAccountStatement.setStrTxnId(transactionConfig.getTxnId());
			gLAccountStatement.setStrTranType(transactionConfig.getGlTranType());			
			gLAccountStatement.setStrTranMode(transactionConfig.getGlTranMode());
			
			gLAccountStatement = glAccountStatementService.addGlAccountStatementData(gLAccountStatement);
			if (gLAccountStatement == null)
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Error while Inserting gl Account Statement!");
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}

	@Override
	public TransactionConfig insertGLAccountDebitEntryInStatement(TransactionConfig transactionConfig)
	{
		try 
		{
			GLAccountTypeMaster	glAccountTypeMaster = transactionConfig.getGlAccountTypeMaster();
			
			GLAccountStatement gLAccountStatement = new GLAccountStatement();
			gLAccountStatement.setStrGLAccountType(glAccountTypeMaster.getStrGLAccountType());
			gLAccountStatement.setStrRef(glAccountTypeMaster.getStrGLAccountDescription());		
			gLAccountStatement.setStrAccountNumber(glAccountTypeMaster.getStrAccountNumber());		
			gLAccountStatement.setTransactionDate(Utils.getCurrentDate());		
			
			Double closingBalanceInDouble = Double.parseDouble(glAccountTypeMaster.getStrClosingBalance());
			gLAccountStatement.setStrClosingBalance(Utils.decimalFormat.format(closingBalanceInDouble));
			
			Double txnAmountInDouble = Double.parseDouble(transactionConfig.getTxnAmount());			
			gLAccountStatement.setStrAmount(Utils.decimalFormat.format(txnAmountInDouble));
			
			gLAccountStatement.setStrTxnId(transactionConfig.getTxnId());
			gLAccountStatement.setStrTranType(transactionConfig.getGlTranType());			
			gLAccountStatement.setStrTranMode(transactionConfig.getGlTranMode());
			gLAccountStatement = glAccountStatementService.addGlAccountStatementData(gLAccountStatement);
			if (gLAccountStatement == null)
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Error while Inserting gl Account Statement!!");
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}

	/*
	public TransactionConfig getGLAccountTypeMasterObj(TransactionConfig transactionConfig)
	{
		GLAccountTypeMaster	glAccountTypeMaster = transactionConfig.getGlAccountTypeMaster();
		try 
		{
			GLAccountTypeMaster glAccounTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(glAccountTypeMaster);
			if (glAccounTypeMaster!=null && glAccounTypeMaster.getStrID()!=null) 
			{
				transactionConfig.setGlAccountTypeMaster(glAccounTypeMaster);
			}
			else
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Error while getting gl Account type");
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	*/
}
