package ams.cms.txn.handler;

import ams.cms.config.TransactionConfig;

public interface AccountTxnEmailHandler 
{
	void sendCreditMailToCustomer(TransactionConfig transactionConfig);
	
	void sendDebitMailToCustomer(TransactionConfig transactionConfig);
	
	void sendThirdPartyDebitMailToCustomer(TransactionConfig transactionConfig);
	
	void sendReversedMailToCustomer(TransactionConfig transactionConfig);
}
