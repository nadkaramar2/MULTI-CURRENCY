package ams.cms.utility;

import java.io.Serializable;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import ams.cms.model.AccountTranMaster;

public class TransactionProcessResponse implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private LinkedHashMap<String, String> requestJson;
	private HttpServletRequest request;
	
	private AccountTranMaster accountTranMaster;
	private String txnId;
	
	public LinkedHashMap<String, String> getRequestJson() {
		return requestJson;
	}
	public void setRequestJson(LinkedHashMap<String, String> requestJson) {
		this.requestJson = requestJson;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public AccountTranMaster getAccountTranMaster() {
		return accountTranMaster;
	}
	public void setAccountTranMaster(AccountTranMaster accountTranMaster) {
		this.accountTranMaster = accountTranMaster;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}	
}
