package ams.cms.api.dao;

import java.util.List;

import ams.cms.api.model.TransactionIdTable;

//copy pasted by ankit
public interface TransactionIdTableDao {

public int saveTransactionIdDetails(TransactionIdTable transactionIdDetails);
	
	public List<TransactionIdTable> getTransactionIdTableList(String year,String julianDate);
	
	public int updateTransactionIdDetails(TransactionIdTable transactionIdDetails);
	
}
