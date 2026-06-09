package ams.cms.txn.handler;

import ams.cms.config.TransactionConfig;

public interface AccountTxnMasterHandler 
{
	TransactionConfig insertTranMasterEntryWithSingleAccount(TransactionConfig transactionConfig);
	
	TransactionConfig insertTranMasterEntryWithMultipleAccount(TransactionConfig transactionConfig);
	
	TransactionConfig updateAccountTranMastersColumn(TransactionConfig transactionConfig);
}
