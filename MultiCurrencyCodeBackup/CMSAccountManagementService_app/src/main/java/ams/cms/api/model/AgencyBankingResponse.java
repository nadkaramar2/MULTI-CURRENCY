package ams.cms.api.model;

import java.io.Serializable;

public class AgencyBankingResponse implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String participantId;
	
	private String strCustId;
	private String cid;
	private String amsTransactionId;
	private String txnAmount;
	private String txnType;
	private String responseCode;
	private String montraTxnId;
	private String authCode;
	private String processingCode;
	private String receipentAccountNo;
	private String agentAccountNo;
	private String agentEmailId;
	
	private String tranStatus;
	private String glAccountNo;
	private String glAccountType;
	private String glAcccounBalance;
	private String glAccountDescription;
	
	private String accountNo;
	private String availableBalance;
	private String emailId;
	private String strAccountHolderName;
	
	public String getStrCustId() {
		return strCustId;
	}
	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getAmsTransactionId() {
		return amsTransactionId;
	}
	public void setAmsTransactionId(String amsTransactionId) {
		this.amsTransactionId = amsTransactionId;
	}
	public String getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getMontraTxnId() {
		return montraTxnId;
	}
	public void setMontraTxnId(String montraTxnId) {
		this.montraTxnId = montraTxnId;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getProcessingCode() {
		return processingCode;
	}
	public void setProcessingCode(String processingCode) {
		this.processingCode = processingCode;
	}
	public String getReceipentAccountNo() {
		return receipentAccountNo;
	}
	public void setReceipentAccountNo(String receipentAccountNo) {
		this.receipentAccountNo = receipentAccountNo;
	}
	public String getAgentAccountNo() {
		return agentAccountNo;
	}
	public void setAgentAccountNo(String agentAccountNo) {
		this.agentAccountNo = agentAccountNo;
	}
	public String getAgentEmailId() {
		return agentEmailId;
	}
	public void setAgentEmailId(String agentEmailId) {
		this.agentEmailId = agentEmailId;
	}
	public String getTranStatus() {
		return tranStatus;
	}
	public void setTranStatus(String tranStatus) {
		this.tranStatus = tranStatus;
	}
	public String getParticipantId() {
		return participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
	public String getGlAccountNo() {
		return glAccountNo;
	}
	public void setGlAccountNo(String glAccountNo) {
		this.glAccountNo = glAccountNo;
	}
	public String getGlAccountType() {
		return glAccountType;
	}
	public void setGlAccountType(String glAccountType) {
		this.glAccountType = glAccountType;
	}
	public String getGlAcccounBalance() {
		return glAcccounBalance;
	}
	public void setGlAcccounBalance(String glAcccounBalance) {
		this.glAcccounBalance = glAcccounBalance;
	}
	public String getGlAccountDescription() {
		return glAccountDescription;
	}
	public void setGlAccountDescription(String glAccountDescription) {
		this.glAccountDescription = glAccountDescription;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getStrAccountHolderName() {
		return strAccountHolderName;
	}
	public void setStrAccountHolderName(String strAccountHolderName) {
		this.strAccountHolderName = strAccountHolderName;
	}
	public String getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	@Override
	public String toString() {
		return "AgencyBankingResponse [participantId=" + participantId + ", strCustId=" + strCustId + ", cid=" + cid
				+ ", amsTransactionId=" + amsTransactionId + ", txnAmount=" + txnAmount + ", txnType=" + txnType
				+ ", responseCode=" + responseCode + ", montraTxnId=" + montraTxnId + ", authCode=" + authCode
				+ ", processingCode=" + processingCode + ", receipentAccountNo=" + receipentAccountNo
				+ ", agentAccountNo=" + agentAccountNo + ", agentEmailId=" + agentEmailId + ", tranStatus=" + tranStatus
				+ ", glAccountNo=" + glAccountNo + ", glAccountType=" + glAccountType + ", glAcccounBalance="
				+ glAcccounBalance + ", glAccountDescription=" + glAccountDescription + ", accountNo=" + accountNo
				+ ", availableBalance=" + availableBalance + ", emailId=" + emailId + ", strAccountHolderName="
				+ strAccountHolderName + "]";
	}
}
