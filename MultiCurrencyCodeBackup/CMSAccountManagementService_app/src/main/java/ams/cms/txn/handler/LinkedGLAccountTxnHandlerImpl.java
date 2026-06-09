package ams.cms.txn.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.config.TransactionConfig;
import ams.cms.logger.AMSLogger;

@Component
public class LinkedGLAccountTxnHandlerImpl implements LinkedGLAccountTxnHandler 
{
	@Autowired
	GLAccountTxnHandler glAccountTxnHandler;
	
	@Override
	public TransactionConfig increaseLinkedGLAccountBalance(TransactionConfig transactionConfig)
	{
		return glAccountTxnHandler.increaseGLAccountBalance(transactionConfig);
	}

	@Override
	public TransactionConfig decreaseLinkedGLAccountBalance(TransactionConfig transactionConfig) 
	{
		return glAccountTxnHandler.decreaseGLAccountBalance(transactionConfig);
	}

	@Override
	public TransactionConfig insertLinkedGLAccountCreditEntryInStatement(TransactionConfig transactionConfig)
	{
		return glAccountTxnHandler.insertGLAccountCreditEntryInStatement(transactionConfig);
	}

	@Override
	public TransactionConfig insertLinkedGLAccountDebitEntryInStatement(TransactionConfig transactionConfig) 
	{
		return glAccountTxnHandler.insertGLAccountDebitEntryInStatement(transactionConfig);
	}

}
