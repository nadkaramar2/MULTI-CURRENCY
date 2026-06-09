package ams.cms.api.response;

import java.util.List;

public class TransactionStatementDetails {

	private String transactionid;
	private String montratxnid;
	private String narration;
	private List<TxnDetails> transactiondetails;

	public String getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}

	public String getMontratxnid() {
		return montratxnid;
	}

	public void setMontratxnid(String montratxnid) {
		this.montratxnid = montratxnid;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public List<TxnDetails> getTransactiondetails() {
		return transactiondetails;
	}

	public void setTransactiondetails(List<TxnDetails> transactiondetails) {
		this.transactiondetails = transactiondetails;
	}

}
