package ams.cms.api.handler;

import ams.cms.config.TransactionConfig;

public interface TemporaryCardBlockHandler 
{
	
	TransactionConfig validatedCardValues(TransactionConfig transactionConfig);

	TransactionConfig updateCardStatusBlock(TransactionConfig transactionConfig);
	
	TransactionConfig sendMail(TransactionConfig transactionConfig) ;
	
	TransactionConfig verifyUserPIN(TransactionConfig transactionConfig) ;
	
	TransactionConfig updateCardStatusUnBlock(TransactionConfig transactionConfig) ;
}
