package ams.cms.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import ams.cms.model.TransactionIdTable;

@Service
public interface TransactionIdService 
{
	List<TransactionIdTable> getTransactionIdList(String year, String julianDayFormat);

	void saveTransactionIdDetails(TransactionIdTable transactionIdTable);

	int updateTransactionIdDetails(TransactionIdTable transactionIdTable);
	
	List<TransactionIdTable> getPreTransactionIdList(String year, String julianDate);
	
	void savePreTransactionIdDetails(TransactionIdTable transactionIdTable);

	int updatePreTransactionIdDetails(TransactionIdTable transactionIdTable); 
	
	String getTransactionId();
	
	String getPreTransactionId();
	
	CompletableFuture<String> getNextTransactionId() throws InterruptedException;
	
	String getNewTransactionId();
}
