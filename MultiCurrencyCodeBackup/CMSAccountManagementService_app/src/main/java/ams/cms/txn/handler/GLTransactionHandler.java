package ams.cms.txn.handler;

import ams.cms.config.TransactionConfig;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;

public interface GLTransactionHandler 
{
	void addAndUpdateGLAccount(TransactionConfig transactionConfig) throws Exception;
	
	GLAccountTypeMaster creatingDebitGLAccountTypeMaster(TransactionConfig transactionConfig) throws Exception;
	
	GLAccountTypeMaster creatingCreditGLAccountTypeMaster(TransactionConfig transactionConfig) throws Exception;
	
	GLAccountStatement creatingDebitGLAccountStatement(TransactionConfig transactionConfig) throws Exception;
	
	GLAccountStatement creatingCreditGLAccountStatement(TransactionConfig transactionConfig) throws Exception;
	
	GLAccountTypeMaster getReducingGLAccountBalance(TransactionConfig transactionConfig) throws Exception;
	
	GLAccountTypeMaster getIncreasingGLAccountBalance(TransactionConfig transactionConfig) throws Exception;
}
