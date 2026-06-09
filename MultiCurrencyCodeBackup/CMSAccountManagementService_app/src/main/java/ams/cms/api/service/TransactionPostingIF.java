package ams.cms.api.service;

import ams.cms.api.model.AccountResponse;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.model.AccountStatement;

public interface TransactionPostingIF 
{
	void createTransactionPostingData(TransactionPostingConfig transactionPostingConfig);	
	
	void processClosedLoopTxnPosting(TransactionPostingConfig transactionPostingConfig);
	
	void removeExistingTransactionPosting(TransactionPostingConfig transactionPostingConfig);
	
	void processAgentDepositTxnPosting(TransactionPostingConfig transactionPostingConfig);
	
	void processAgentConfirmDepositTxnPosting(TransactionPostingConfig transactionPostingConfig);
	
	void processAgentConfirmWithdrawalTxnPosting(TransactionPostingConfig transactionPostingConfig);
	
	void processAgentReversedDepositTransaction(TransactionPostingConfig transactionPostingConfig);
	
	void processAgentReversedWithdrawalTransaction(TransactionPostingConfig transactionPostingConfig);
	
	void processLoadMoneytxnPostingData(TransactionPostingConfig transactionPostingConfig);
	
	void processAgentWithdrawalTxnPosting(TransactionPostingConfig transactionPostingConfig);	
	
	void processThirdPartyTransactionPosting(TransactionPostingConfig transactionPostingConfig);
	
	void processBillPayTxnPostingData(TransactionPostingConfig transactionPostingConfig);

	void addAccountTransactionData(TransactionPostingConfig transactionPostingConfig);

	AccountStatement getCreditAccountStatementInstance(TransactionPostingConfig transactionPostConf,AccountResponse accountResponse);
}
