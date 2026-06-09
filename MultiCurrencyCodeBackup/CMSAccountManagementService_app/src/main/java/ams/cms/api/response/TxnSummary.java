package ams.cms.api.response;

public class TxnSummary {

	private String paymenttype;
	private String transactionstatus;
	private String txnid;
	private String amount;
	private String transactiontype;
	private String transactiondate;
	private String entityinfo;
	private String entitynumber;
	
	
	public String getPaymenttype() {
		return paymenttype;
	}
	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
	}
	public String getTransactionstatus() {
		return transactionstatus;
	}
	public void setTransactionstatus(String transactionstatus) {
		this.transactionstatus = transactionstatus;
	}
	public String getTxnid() {
		return txnid;
	}
	public void setTxnid(String txnid) {
		this.txnid = txnid;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTransactiontype() {
		return transactiontype;
	}
	public void setTransactiontype(String transactiontype) {
		this.transactiontype = transactiontype;
	}
	public String getTransactiondate() {
		return transactiondate;
	}
	public void setTransactiondate(String transactiondate) {
		this.transactiondate = transactiondate;
	}
	public String getEntityinfo() {
		return entityinfo;
	}
	public void setEntityinfo(String entityinfo) {
		this.entityinfo = entityinfo;
	}
	public String getEntitynumber() {
		return entitynumber;
	}
	public void setEntitynumber(String entitynumber) {
		this.entitynumber = entitynumber;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getFailurereason() {
		return failurereason;
	}
	public void setFailurereason(String failurereason) {
		this.failurereason = failurereason;
	}
	private String cid;
	private String failurereason;
	
}
