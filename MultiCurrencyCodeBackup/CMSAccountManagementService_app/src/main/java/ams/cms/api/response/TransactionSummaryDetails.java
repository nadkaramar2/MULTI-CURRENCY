package ams.cms.api.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class TransactionSummaryDetails implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String code;
	private Boolean success;
	private String message;
	private String transactioncount;
	private String balance;
	private List<TxnSummary> transactions;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTransactioncount() {
		return transactioncount;
	}

	public void setTransactioncount(String transactioncount) {
		this.transactioncount = transactioncount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public List<TxnSummary> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TxnSummary> transactions) {
		this.transactions = transactions;
	}

}
