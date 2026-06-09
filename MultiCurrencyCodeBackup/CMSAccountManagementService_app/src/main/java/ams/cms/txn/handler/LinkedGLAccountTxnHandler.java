package ams.cms.txn.handler;

import ams.cms.config.TransactionConfig;

public interface LinkedGLAccountTxnHandler 
{
	TransactionConfig increaseLinkedGLAccountBalance(TransactionConfig transactionConfig);
	
	TransactionConfig decreaseLinkedGLAccountBalance(TransactionConfig transactionConfig);
	
	TransactionConfig insertLinkedGLAccountCreditEntryInStatement(TransactionConfig transactionConfig);
	
	TransactionConfig insertLinkedGLAccountDebitEntryInStatement(TransactionConfig transactionConfig);
}
