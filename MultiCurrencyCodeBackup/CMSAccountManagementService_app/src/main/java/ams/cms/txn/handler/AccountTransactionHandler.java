package ams.cms.txn.handler;

import ams.cms.config.TransactionConfig;

public interface AccountTransactionHandler 
{
	TransactionConfig creatingDebitAccountStatement(TransactionConfig transactionConfig) throws Exception;
		
	TransactionConfig creatingCreditAccountStatement(TransactionConfig transactionConfig) throws Exception;
		
	TransactionConfig getReducingAccountBalance(TransactionConfig transactionConfig) throws Exception;
		
	TransactionConfig getIncreasingAccountBalance(TransactionConfig transactionConfig) throws Exception;
	
	TransactionConfig getLinkedGLAccountTypeAndNo(TransactionConfig transactionConfig) throws Exception;
}
