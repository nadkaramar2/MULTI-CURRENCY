package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.TransactionTypeDao;
import ams.cms.model.TransactionTypeModel;
import ams.cms.services.TransactionTypeService;

@Transactional
@Service
public class TransactionTypeServiceImpl implements TransactionTypeService 
{
	@Autowired
	private TransactionTypeDao transactionTypeDao;
	
	@Override
	public TransactionTypeModel addTransactionTypelist(TransactionTypeModel transactionTypeModel) 
	{
		try 
		{
			transactionTypeDao.save(transactionTypeModel);
			return transactionTypeModel;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	   return null;
	}


	@Override
	public TransactionTypeModel getTransactionTypeMaster(TransactionTypeModel transactionTypeModel) throws Exception {
		return transactionTypeDao.getTransactionTypeMaster(transactionTypeModel);
	}

	@Override
	public String getTranTypeByProcessinCode(String proceessingCode) {
		return transactionTypeDao.getTranTypeByProcessinCode(proceessingCode);
	}
	
	@Override
	public TransactionTypeModel getGlAccountNoByTxnKeyword(TransactionTypeModel transactionTypeModel) {
		return transactionTypeDao.getGlAccountNoByTxnKeyword(transactionTypeModel);
	}

	//@Transactional
	@Override
	public TransactionTypeModel getTransactionTypeMasterBasedOnTxnType(TransactionTypeModel transactionTypeModel) throws Exception 
	{
		return transactionTypeDao.getTransactionTypeMasterBasedOnTxnType(transactionTypeModel);
	}
	
	@Override
	public List<TransactionTypeModel> getTransactionTypeData(TransactionTypeModel transactionTypeModel) 
	{
		return transactionTypeDao.getTransactionTypeData(transactionTypeModel);
	}
	

	@Override
	public boolean isTransactionTypeAlreadyExist(TransactionTypeModel transactionTypeModel) 
	{
		return transactionTypeDao.isTransactionTypeAlreadyExist(transactionTypeModel);
	}


	@Override
	public boolean isGLAccountTypeExist(TransactionTypeModel transactionTypeModel) 
	{
		return transactionTypeDao.isGLAccountTypeExist(transactionTypeModel);
	}
}
