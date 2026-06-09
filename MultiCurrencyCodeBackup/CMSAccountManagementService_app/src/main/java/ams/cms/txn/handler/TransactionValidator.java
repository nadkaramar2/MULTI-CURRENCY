package ams.cms.txn.handler;

import ams.cms.config.TransactionConfig;

public interface TransactionValidator 
{
	TransactionConfig txnValidate(TransactionConfig transactionConfig) throws Exception;
		
	TransactionConfig validateCummulativeBalanceLimit(TransactionConfig transactionConfig) throws Exception;
	
	TransactionConfig validateAccount(TransactionConfig transactionConfig) throws Exception;
}
