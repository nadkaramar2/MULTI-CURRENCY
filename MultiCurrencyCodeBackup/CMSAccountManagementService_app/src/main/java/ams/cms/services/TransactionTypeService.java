package ams.cms.services;


import java.util.List;

import ams.cms.model.TransactionTypeModel;

public interface TransactionTypeService 
{
	TransactionTypeModel getTransactionTypeMaster(TransactionTypeModel transactionTypeModel) throws Exception;

	TransactionTypeModel addTransactionTypelist(TransactionTypeModel transactionTypeModel);
	
	String getTranTypeByProcessinCode(String proceessingCode);
	
	TransactionTypeModel getGlAccountNoByTxnKeyword(TransactionTypeModel transactionTypeModel);
	
	TransactionTypeModel getTransactionTypeMasterBasedOnTxnType(TransactionTypeModel transactionTypeModel) throws Exception;
	
	List<TransactionTypeModel> getTransactionTypeData(TransactionTypeModel transactionTypeModel);
	
	boolean isTransactionTypeAlreadyExist(TransactionTypeModel transactionTypeModel);

	boolean isGLAccountTypeExist(TransactionTypeModel transactionTypeModel);
}
