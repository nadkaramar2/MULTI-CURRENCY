package ams.cms.api.handler;

import ams.cms.api.model.AccountResponse;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.util.ProcessResponse;

public interface CreditAccountTransactionHandler 
{
	void addCreditCardRelatedEntry(TransactionPostingConfig transactionPostingConfig);
	
	void updateSenderCreditAccountMaster(TransactionPostingConfig transactionPostingConfig);
	
	ProcessResponse validateCreditAccountLimits(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, AccountResponse accountResponse);
}
