package ams.cms.api.service;

import java.util.List;

import ams.cms.api.model.TransactionIdTable;

//added by ankit
public interface TransactionIdTableService {
		//TransactionDetails
		public int saveTransactionIdDetails(TransactionIdTable transactionIdTable);
		
		public int updateTransactionIdDetails(TransactionIdTable transactionIdTable);
		
		public List<TransactionIdTable> getTransactionIdList(String year,String julianDate);
}
