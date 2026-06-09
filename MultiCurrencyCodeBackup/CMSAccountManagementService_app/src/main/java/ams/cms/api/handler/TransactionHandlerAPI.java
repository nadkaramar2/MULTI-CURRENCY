package ams.cms.api.handler;

import ams.cms.config.TransactionConfig;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.TransactionTypeModel;

public interface TransactionHandlerAPI 
{
	void updateSenderAccountMaster(AccountTranMaster accountTranMaster, AccountCreation senderAccountInfo) throws Exception;
	
	void updateRecipientAccountMaster(AccountTranMaster accountTranMaster) throws Exception;
	
	void addSenderAccountStatement(AccountTranMaster accountTranMaster) throws Exception;
	
	void addRecipientAccountStatement(AccountTranMaster accountTranMaster) throws Exception;
	
	void addTransactionMasterData(AccountTranMaster accountTranMaster) throws Exception;
	
	void addCreditCardRelatedEntry(AccountTranMaster accountTranMaster) throws Exception;
	
	TransactionTypeModel getTransactionTypeObject(AccountTranMaster accountTranMaster) throws Exception;
	
	void updateAccountMasterSomeFields(AccountCreation accountCreation) throws Exception;
	
	//added by ankit  
	void reduceEarMarkBalance(AccountTranMaster accountTranMaster, AccountCreation fromAccountDetails);

	void addPreCredAmount(AccountTranMaster accountTranMaster, AccountCreation toAccountDetails);
	//added by ankit 
	
	void updateSenderLinkedGLAccountMaster(AccountTranMaster accountTranMaster, AccountCreation senderAccountInfo) throws Exception;
	
	TransactionConfig verifyCustomerPin(AccountCreation accountMaster) throws Exception;
}
