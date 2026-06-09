package ams.cms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class TransactionRequest {
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	private String strAccountNumber;
	
	@Transient
	private String strChannel;
	
	@Transient
	private String strCurrency;
	
	@Transient
	private String strTxnAmount;
	
	@Transient
	private String strTxnType;
	
	@Transient
	private String pin;
	
	@Transient
	private String custId;
	
	@Transient
	private String participantid;
	
	

	

	public String getParticipantid() {
		return participantid;
	}

	public void setParticipantid(String participantid) {
		this.participantid = participantid;
	}

	public String getStrAccountNumber() {
		return strAccountNumber;
	}

	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}

	public String getStrChannel() {
		return strChannel;
	}

	public void setStrChannel(String strChannel) {
		this.strChannel = strChannel;
	}

	public String getStrCurrency() {
		return strCurrency;
	}

	public void setStrCurrency(String strCurrency) {
		this.strCurrency = strCurrency;
	}

	public String getStrTxnAmount() {
		return strTxnAmount;
	}

	public void setStrTxnAmount(String strTxnAmount) {
		this.strTxnAmount = strTxnAmount;
	}

	public String getStrTxnType() {
		return strTxnType;
	}

	public void setStrTxnType(String strTxnType) {
		this.strTxnType = strTxnType;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}
	
	
	
}
