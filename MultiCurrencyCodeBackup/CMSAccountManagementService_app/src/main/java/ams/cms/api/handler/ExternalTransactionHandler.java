package ams.cms.api.handler;

import ams.cms.config.TransactionConfig;
import ams.cms.model.AccountCreation;

public interface ExternalTransactionHandler {
	
	TransactionConfig getTierInfoByCustID( AccountCreation accountCreation);
	
	TransactionConfig validateDailyTxnTierLimits(TransactionConfig transactionConfig);
	
	TransactionConfig validateCummulativeTierLimit(TransactionConfig transactionConfig);
	
	TransactionConfig updateLimitValues(TransactionConfig transactionConfig);
	
	TransactionConfig validateDailyLimits(TransactionConfig transactionConfig);
}
