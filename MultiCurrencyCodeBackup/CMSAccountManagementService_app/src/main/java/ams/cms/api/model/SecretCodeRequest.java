package ams.cms.api.model;

import java.io.Serializable;

public class SecretCodeRequest implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String txnid;
	private String custid;
	private String totp;
	
	public String getTxnid() {
		return txnid;
	}
	public void setTxnid(String txnid) {
		this.txnid = txnid;
	}
	public String getCustid() {
		return custid;
	}
	public void setCustid(String custid) {
		this.custid = custid;
	}
	public String getTotp() {
		return totp;
	}
	public void setTotp(String totp) {
		this.totp = totp;
	}	
}
