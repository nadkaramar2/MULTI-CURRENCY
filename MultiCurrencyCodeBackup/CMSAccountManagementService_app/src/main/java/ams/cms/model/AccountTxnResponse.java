package ams.cms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import ams.cms.api.model.AccountTranscationResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountTxnResponse implements Serializable {
	
	private static final long serialVersionUID = 1L; 	
	
	private TransactionSummary transactionSumary;
	 
	private List<AccountTranscationResponse> transactionDetails = new ArrayList<AccountTranscationResponse>();
	
	public void setTransactionSumary(TransactionSummary transactionSumary) {
		this.transactionSumary = transactionSumary;
	}

	public TransactionSummary getTransactionSumary() {
		return transactionSumary;
	}

	public List<AccountTranscationResponse> getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(List<AccountTranscationResponse> transactionDetails) {
		this.transactionDetails = transactionDetails;
	}	
}
