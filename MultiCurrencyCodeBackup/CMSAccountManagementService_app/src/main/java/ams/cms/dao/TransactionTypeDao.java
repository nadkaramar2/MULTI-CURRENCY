package ams.cms.dao;

import java.util.List;

import ams.cms.model.TransactionTypeModel;

public interface TransactionTypeDao extends GenericDao<TransactionTypeModel>
{	
	TransactionTypeModel getTransactionTypeMaster(TransactionTypeModel transactionTypeModel);
	
	String getTranTypeByProcessinCode(String proceessingCode);
	
	TransactionTypeModel getGlAccountNoByTxnKeyword(TransactionTypeModel transactionTypeModel);
	
	TransactionTypeModel getTransactionTypeMasterBasedOnTxnType(TransactionTypeModel transactionTypeModel);
	
	List<TransactionTypeModel> getTransactionTypeData(TransactionTypeModel transactionTypeModel);
	
	boolean isTransactionTypeAlreadyExist(TransactionTypeModel transactionTypeModel);

	boolean isGLAccountTypeExist(TransactionTypeModel transactionTypeModel);
}
