package ams.cms.api.handler;

import ams.cms.config.TransactionPostingConfig;
import ams.cms.util.ProcessResponse;

public interface AccountTxnHandler 
{
	ProcessResponse processClosedLoopTransaction(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, String fee, String vat);
	
	ProcessResponse processAgentDepositTransaction(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, String fee, String vat);
	
	void processAgentConfirmDepositTransaction(TransactionPostingConfig transactionPostingConfig);
	
	void processAgentConfirmWithdrawalTransaction(TransactionPostingConfig transactionPostingConfig, String fee, String vat);
	
	void processAgentReversedDepositTransaction(TransactionPostingConfig transactionPostingConfig);
	
	void processAgentReversedWithdrawalTransaction(TransactionPostingConfig transactionPostingConfig);
	
	ProcessResponse processLoadMoneyTransaction(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, String fee, String vat);
	
	ProcessResponse processAgentWithdrawalTxnPhaseI(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig);
	
	ProcessResponse processThirdPartyTxn(ProcessResponse processResponse,TransactionPostingConfig transactionPostingConfig, String fee, String vat);
	
	ProcessResponse processBillPayTransaction(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, String fee, String vat);
	
	void updateTransactionRequestLogData(TransactionPostingConfig transactionPostingConfig);
	
	ProcessResponse validateEntityInfoAndNumber(String decrypt, ProcessResponse processResponse);
}
