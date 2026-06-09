package ams.cms.txn.handler;

import ams.cms.config.TransactionConfig;

public interface GLAccountTxnHandler 
{
	TransactionConfig increaseGLAccountBalance(TransactionConfig transactionConfig);
	
	TransactionConfig decreaseGLAccountBalance(TransactionConfig transactionConfig);
	
	TransactionConfig insertGLAccountCreditEntryInStatement(TransactionConfig transactionConfig);
	
	TransactionConfig insertGLAccountDebitEntryInStatement(TransactionConfig transactionConfig);
}
