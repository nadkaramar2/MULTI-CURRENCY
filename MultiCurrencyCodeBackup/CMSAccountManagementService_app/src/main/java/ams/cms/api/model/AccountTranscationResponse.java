package ams.cms.api.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import ams.cms.model.AccountTxnResponse;
import ams.cms.model.TransactionInformation;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountTranscationResponse implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String strAccountType;
	
	private String strClosingBalance;
	
	private Double strTransactionAmount;
	
	private String strTransactionDate;
	
	private String strTransactionTime;
	
	private String strTransactionID;
	
	private String strTransactionType;
	
	private String strTransactionMode;
	
	private String montraTxnId;
	
	private String strIsFeeTxn;
	
	private String strIsVatTxn;
	
	private String strIsTxn;
	
	private String entityNumber;
	private String entityInfo;	
	
	private TransactionInformation transactionInformation;
	private AccountTxnResponse accountTxnResponse;

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrClosingBalance() {
		return strClosingBalance;
	}

	public void setStrClosingBalance(String strClosingBalance) {
		this.strClosingBalance = strClosingBalance;
	}

	public Double getStrTransactionAmount() {
		return strTransactionAmount;
	}

	public void setStrTransactionAmount(Double strTransactionAmount) {
		this.strTransactionAmount = strTransactionAmount;
	}

	public String getStrTransactionID() {
		return strTransactionID;
	}

	public void setStrTransactionID(String strTransactionID) {
		this.strTransactionID = strTransactionID;
	}

	public String getStrTransactionType() {
		return strTransactionType;
	}

	public void setStrTransactionType(String strTransactionType) {
		this.strTransactionType = strTransactionType;
	}


	public String getStrTransactionMode() {
		return strTransactionMode;
	}

	public void setStrTransactionMode(String strTransactionMode) {
		this.strTransactionMode = strTransactionMode;
	}

	public String getStrTransactionDate() {
		return strTransactionDate;
	}

	public void setStrTransactionDate(String strTransactionDate) {
		this.strTransactionDate = strTransactionDate;
	}

	public String getStrTransactionTime() {
		return strTransactionTime;
	}

	public void setStrTransactionTime(String strTransactionTime) {
		this.strTransactionTime = strTransactionTime;
	}

	public String getMontraTxnId() {
		return montraTxnId;
	}

	public void setMontraTxnId(String montraTxnId) {
		this.montraTxnId = montraTxnId;
	}

	public String getStrIsFeeTxn() {
		return strIsFeeTxn;
	}

	public void setStrIsFeeTxn(String strIsFeeTxn) {
		this.strIsFeeTxn = strIsFeeTxn;
	}

	public String getStrIsVatTxn() {
		return strIsVatTxn;
	}

	public void setStrIsVatTxn(String strIsVatTxn) {
		this.strIsVatTxn = strIsVatTxn;
	}

	public String getStrIsTxn() {
		return strIsTxn;
	}

	public void setStrIsTxn(String strIsTxn) {
		this.strIsTxn = strIsTxn;
	}

	public TransactionInformation getTransactionInformation() {
		return transactionInformation;
	}

	public void setTransactionInformation(TransactionInformation transactionInformation) {
		this.transactionInformation = transactionInformation;
	}

	public AccountTxnResponse getAccountTxnResponse() {
		return accountTxnResponse;
	}

	public void setAccountTxnResponse(AccountTxnResponse accountTxnResponse) {
		this.accountTxnResponse = accountTxnResponse;
	}

	public String getEntityNumber() {
		return entityNumber;
	}

	public void setEntityNumber(String entityNumber) {
		this.entityNumber = entityNumber;
	}

	public String getEntityInfo() {
		return entityInfo;
	}

	public void setEntityInfo(String entityInfo) {
		this.entityInfo = entityInfo;
	}
}
