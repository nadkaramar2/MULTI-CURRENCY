package ams.cms.txn.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.config.TransactionConfig;
import ams.cms.constants.TransactionType;
import ams.cms.logger.AMSLogger;
import ams.cms.model.GLAccountTypeMaster;

@Component
public class ControlAccountTxnHandlerImpl implements ControlAccountTxnHandler
{
	@Autowired
	GLAccountTxnHandler glAccountTxnHandler;
	
	@Override
	public TransactionConfig addTransferINControlAccountEntry(TransactionConfig transactionConfig)
	{
		try
		{
			
			//Added By Sunil Y , 2023-09-14 , change TransType
			String strAccountTransType = transactionConfig.getAccountTranType();
			
			transactionConfig.setCode("S0000");
			transactionConfig.setGlTranType("TIN");//Transfer IN
			transactionConfig.setGlTranMode(TransactionType.TransactionMode.CREDIT);
			
			transactionConfig = increaseControlGLBalance(transactionConfig);
			if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
			{
				
				//Added By Sunil Y , 2023-09-14 , change TransType
				if (transactionConfig.isReversedTxn()) 
				{
					transactionConfig.setAccountTranType("TIN");
				}
				transactionConfig = insertControlGLCreditEntryInStatement(transactionConfig);
				if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
				{
					transactionConfig.setGlTranType(transactionConfig.getAccountTranType());//Debit
					transactionConfig.setGlTranMode(TransactionType.TransactionMode.DEBIT);
					
					transactionConfig = decreaseControlGLBalance(transactionConfig);
					if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
					{
						if (transactionConfig.isReversedTxn()) 
						{
							transactionConfig.setAccountTranType(strAccountTransType);
						}
						transactionConfig = insertControlGLDebitEntryInStatement(transactionConfig);
					}
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return transactionConfig;
	}

	@Override
	public TransactionConfig addTransferOUTControlAccountEntry(TransactionConfig transactionConfig) 
	{
		try
		{
			transactionConfig.setCode("S0000");			
			transactionConfig.setGlTranType("TOT");//Transfer OUT
			
			transactionConfig = decreaseControlGLBalance(transactionConfig);
			if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
			{
				transactionConfig.setGlTranMode(TransactionType.TransactionMode.DEBIT);
				
				transactionConfig = insertControlGLDebitEntryInStatement(transactionConfig);
				if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
				{
					transactionConfig.setGlTranType(transactionConfig.getAccountTranType());//Credit					
					
					transactionConfig = increaseControlGLBalance(transactionConfig);
					if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
					{
						transactionConfig.setGlTranMode(TransactionType.TransactionMode.CREDIT);
						transactionConfig = insertControlGLCreditEntryInStatement(transactionConfig);
					}
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return transactionConfig;
	}
	
	@Override
	public TransactionConfig increaseControlGLBalance(TransactionConfig transactionConfig)
	{
		return glAccountTxnHandler.increaseGLAccountBalance(transactionConfig);
	}

	@Override
	public TransactionConfig decreaseControlGLBalance(TransactionConfig transactionConfig) 
	{
		return glAccountTxnHandler.decreaseGLAccountBalance(transactionConfig);
	}

	@Override
	public TransactionConfig insertControlGLCreditEntryInStatement(TransactionConfig transactionConfig) 
	{
		return glAccountTxnHandler.insertGLAccountCreditEntryInStatement(transactionConfig);
	}

	@Override
	public TransactionConfig insertControlGLDebitEntryInStatement(TransactionConfig transactionConfig) 
	{
		return glAccountTxnHandler.insertGLAccountDebitEntryInStatement(transactionConfig);
	}
}
