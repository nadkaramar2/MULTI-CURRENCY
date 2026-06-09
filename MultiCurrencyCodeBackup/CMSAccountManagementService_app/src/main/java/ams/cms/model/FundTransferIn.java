package ams.cms.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import ams.cms.config.TransactionPostingConfig;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class FundTransferIn implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String success ;
	private String message;
	
	private String status;
	
	private String participantId;
	private String requestId;
	private String beneficiaryAccountNumber;
	private String beneficiaryAccountName;
	private String senderAccountNumber;
	private String senderAccountName;
	private String senderBankCode;
	private String senderBankName;
	private String Amount;
	private String Narration;
	private String SessionId;
	private String tranType;
	
	private String tranId;
	
	private String channelCode;
	private String accountNumber;
	
	private String montraId;
	private String cid;
	private String custId;
	
	private FundTransferResponse data;
	
	private boolean isReversedTxn = false;
	private TransactionPostingConfig transactionPostingConfig;
	private Boolean isFeeTypeExist = false;
	
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getTranId() {
		return tranId;
	}
	public void setTranId(String tranId) {
		this.tranId = tranId;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	public FundTransferResponse getData() {
		return data;
	}
	public void setData(FundTransferResponse data) {
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getBeneficiaryAccountNumber() {
		return beneficiaryAccountNumber;
	}
	public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
		this.beneficiaryAccountNumber = beneficiaryAccountNumber;
	}
	public String getBeneficiaryAccountName() {
		return beneficiaryAccountName;
	}
	public void setBeneficiaryAccountName(String beneficiaryAccountName) {
		this.beneficiaryAccountName = beneficiaryAccountName;
	}
	public String getSenderAccountNumber() {
		return senderAccountNumber;
	}
	public void setSenderAccountNumber(String senderAccountNumber) {
		this.senderAccountNumber = senderAccountNumber;
	}
	public String getSenderAccountName() {
		return senderAccountName;
	}
	public void setSenderAccountName(String senderAccountName) {
		this.senderAccountName = senderAccountName;
	}
	public String getSenderBankCode() {
		return senderBankCode;
	}
	public void setSenderBankCode(String senderBankCode) {
		this.senderBankCode = senderBankCode;
	}
	public String getSenderBankName() {
		return senderBankName;
	}
	public void setSenderBankName(String senderBankName) {
		this.senderBankName = senderBankName;
	}
	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
		Amount = amount;
	}
	public String getNarration() {
		return Narration;
	}
	public void setNarration(String narration) {
		Narration = narration;
	}
	public String getSessionId() {
		return SessionId;
	}
	public void setSessionId(String sessionId) {
		SessionId = sessionId;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getMontraId() {
		return montraId;
	}
	public void setMontraId(String montraId) {
		this.montraId = montraId;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}	
	
	public boolean isReversedTxn() {
		return isReversedTxn;
	}
	public void setReversedTxn(boolean isReversedTxn) {
		this.isReversedTxn = isReversedTxn;
	}
	
	public TransactionPostingConfig getTransactionPostingConfig() {
		return transactionPostingConfig;
	}
	public void setTransactionPostingConfig(TransactionPostingConfig transactionPostingConfig) {
		this.transactionPostingConfig = transactionPostingConfig;
	}
	public Boolean getIsFeeTypeExist() {
		return isFeeTypeExist;
	}
	public void setIsFeeTypeExist(Boolean isFeeTypeExist) {
		this.isFeeTypeExist = isFeeTypeExist;
	}
	public String getParticipantId() {
		return participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
	@Override
	public String toString() {
		return "FundTransferIn [code=" + code + ", success=" + success + ", message=" + message + ", status=" + status
				+ ", requestId=" + requestId + ", beneficiaryAccountNumber=" + beneficiaryAccountNumber
				+ ", beneficiaryAccountName=" + beneficiaryAccountName + ", senderAccountNumber=" + senderAccountNumber
				+ ", senderAccountName=" + senderAccountName + ", senderBankCode=" + senderBankCode
				+ ", senderBankName=" + senderBankName + ", Amount=" + Amount + ", Narration=" + Narration
				+ ", SessionId=" + SessionId + ", tranType=" + tranType + ", tranId=" + tranId + ", channelCode="
				+ channelCode + ", accountNumber=" + accountNumber + ", montraId=" + montraId + ", cid=" + cid
				+ ", custId=" + custId + ", data=" + data + ", isReversedTxn=" + isReversedTxn
				+ ", transactionPostingConfig=" + transactionPostingConfig + ", isFeeTypeExist=" + isFeeTypeExist + "]";
	}
}
