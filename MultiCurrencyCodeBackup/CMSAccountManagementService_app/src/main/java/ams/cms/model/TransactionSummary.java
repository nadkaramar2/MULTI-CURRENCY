package ams.cms.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class TransactionSummary implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String strTransactionID;
	private String montraTxnId;
	private String netOff;
	private String strTransactionType;
	
	private String strTransactionDate;
	private String entityInfo;
	private String entityNumber;
	
	public String getStrTransactionID() {
		return strTransactionID;
	}
	public void setStrTransactionID(String strTransactionID) {
		this.strTransactionID = strTransactionID;
	}
	public String getMontraTxnId() {
		return montraTxnId;
	}
	public void setMontraTxnId(String montraTxnId) {
		this.montraTxnId = montraTxnId;
	}
	public String getNetOff() {
		return netOff;
	}
	public void setNetOff(String netOff) {
		this.netOff = netOff;
	}
	public String getStrTransactionType() {
		return strTransactionType;
	}
	public void setStrTransactionType(String strTransactionType) {
		this.strTransactionType = strTransactionType;
	}
	public String getEntityInfo() {
		return entityInfo;
	}
	public void setEntityInfo(String entityInfo) {
		this.entityInfo = entityInfo;
	}
	public String getEntityNumber() {
		return entityNumber;
	}
	public void setEntityNumber(String entityNumber) {
		this.entityNumber = entityNumber;
	}
	public String getStrTransactionDate() {
		return strTransactionDate;
	}
	public void setStrTransactionDate(String strTransactionDate) {
		this.strTransactionDate = strTransactionDate;
	}

}
