package ams.cms.dao;

import java.util.List;

import ams.cms.model.TransactionIdTable;

public interface TransactionIdDao extends GenericDao<TransactionIdTable>
{
	 List<TransactionIdTable> getTransactionIdList(String year, String julianDayFormat);
	 
	 int updateTransactionIdDetails(TransactionIdTable transactionIdTable);
	 
	 List<TransactionIdTable> getPreTransactionIdList(String year, String julianDate);
	 
	 void savePreTransactionIdDetails(TransactionIdTable transactionIdTable);

	 int updatePreTransactionIdDetails(TransactionIdTable transactionIdTable);
	 
	 String getNextTransactionId(String year, String julianDay, String newTxnSerialNumber);
}
