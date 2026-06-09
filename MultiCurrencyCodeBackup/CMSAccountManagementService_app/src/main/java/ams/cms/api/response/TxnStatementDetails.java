package ams.cms.api.response;

import java.io.Serializable;
import java.util.List;

public class TxnStatementDetails implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private List<TransactionStatementDetails> transactions;
	
	public List<TransactionStatementDetails> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionStatementDetails> transactions) {
		this.transactions = transactions;
	}
}
