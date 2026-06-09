package ams.cms.api.handler;

import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.TierAccountResponse;
import ams.cms.config.TransactionConfig;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.util.ProcessResponse;

public interface ThirdPartyTxnHandler 
{
	ProcessResponse getThirdPartyBankList(TransactionConfig transactionConfig);
	
	ProcessResponse getThirdPartyAccountName(TransactionConfig transactionConfig);
	
	void processCreditThirdPartyGLBalanceUpdate(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster gLAccountTypeMaster);
	
	void processDebitThirdPartyGLBalanceUpdate(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster gLAccountTypeMaster);

	void processCreditControlGLBalanceUpdate(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster gLAccountTypeMaster);
	
	void processDebitControlGLBalanceUpdate(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster gLAccountTypeMaster);
	
	void processControlGLTransferOUTStatementEntry(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster gLAccountTypeMaster);

	//Added by Sunil Y , Reversal Part For TierLimit And Cummultive Limit , 2023-09-15 Start
	void updateAccountLimits(AccountResponse accountResponse, TierAccountResponse tierAccountResponse, String txnAmount);
	//Added by Sunil Y , Reversal Part For TierLimit And Cummultive Limit , 2023-09-15 End    
}
