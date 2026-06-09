package ams.cms.handler;

import ams.cms.config.TransactionPostingConfig;

public interface FeeTypeHandler 
{
	TransactionPostingConfig getFeeAndVatGLAccounts(TransactionPostingConfig transactionPostingConfig);
}
