package ams.cms.txn.handler;

import ams.cms.config.TransactionConfig;
import ams.cms.model.CustomerIdCreation;
import ams.cms.util.ProcessResponse;

public interface AccountValidator
{
	TransactionConfig validateAccountStatus(TransactionConfig transactionConfig);
	
	ProcessResponse validateCustomerPIN(ProcessResponse processResponse, CustomerIdCreation customerIdCreation);
}
