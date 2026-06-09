package ams.cms.txn.handler;

import ams.cms.config.TransactionConfig;

public interface ControlAccountTxnHandler 
{
	TransactionConfig addTransferINControlAccountEntry(TransactionConfig transactionConfig);
	
	TransactionConfig addTransferOUTControlAccountEntry(TransactionConfig transactionConfig);
	
	TransactionConfig increaseControlGLBalance(TransactionConfig transactionConfig);
	
	TransactionConfig insertControlGLCreditEntryInStatement(TransactionConfig transactionConfig);
	
	TransactionConfig decreaseControlGLBalance(TransactionConfig transactionConfig);
	
	TransactionConfig insertControlGLDebitEntryInStatement(TransactionConfig transactionConfig);
}
