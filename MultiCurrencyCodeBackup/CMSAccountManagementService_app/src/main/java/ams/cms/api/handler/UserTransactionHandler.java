package ams.cms.api.handler;

import ams.cms.config.TransactionConfig;

public interface UserTransactionHandler
{
	TransactionConfig validatedTransaction(TransactionConfig transactionConfig);

	TransactionConfig validateTxnAmount(TransactionConfig transactionConfig);
	
	TransactionConfig validateDenominationNotes(TransactionConfig transactionConfig);
	
	TransactionConfig getAccountHolderName(TransactionConfig transactionConfig);
	
	TransactionConfig checkUserAccountTypeCategory(TransactionConfig transactionConfig);

	TransactionConfig verifyUserPIN(TransactionConfig transactionConfig);
	
	TransactionConfig sendUserOTP(TransactionConfig transactionConfig);
	
	//Added by Pankaj Pawar Start
	TransactionConfig getAccountInformation(TransactionConfig transactionConfig );

	TransactionConfig validateOtp(TransactionConfig transactionConfig );
	
	TransactionConfig sendEmail(TransactionConfig transactionConfig );
	
	TransactionConfig addTranMasterRequestEntry(TransactionConfig transactionConfig );
	
	TransactionConfig reduceAccountBalanceWithLimits(TransactionConfig transactionConfig ); ////reduceAccountBalanceWithLimits increaseAccountBalance
	
	TransactionConfig increaseAccountBalance(TransactionConfig transactionConfig);
	
	TransactionConfig addAgentAccountStatementData(TransactionConfig transactionConfig);
	
	TransactionConfig addAgentGLAccountStatementData(TransactionConfig transactionConfig);
	
	TransactionConfig addAccountStatementData(TransactionConfig transactionConfig);
	
	TransactionConfig addGLAccountStatementData(TransactionConfig transactionConfig);
	
	void performGlAccountTransaction(TransactionConfig transactionConfig);
	//Added by Pankaj Pawar End
	
	//Added by Sunny soni Start
	TransactionConfig saveDenominationMasterData(TransactionConfig transactionConfig) throws Exception;
	//Added by Sunny soni Soni
	
	TransactionConfig validateSecretCode(TransactionConfig transactionConfig) throws Exception;
}
