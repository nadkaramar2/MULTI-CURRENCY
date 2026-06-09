package ams.cms.utility;

import java.io.Serializable;

import ams.cms.api.model.MiddleWareBankRequestModel;

public class MiddleWareRequestModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String appId;
	private String auditId;
	private String channelCode;
	private String participantId;
	
	private String recipientAccountNumber;
	private String senderAccountNumber;
	private String recipientInstitutionCode;
	private String recipientBankCode;
	
	private String txnType;
	private String beneficiaryAccountNumber;
	private String beneficiaryBankCode;
	private String beneficiaryInstitutionCode;
	private MiddleWareBankRequestModel middleWareAppInfoData;
	
	private String cid;

	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAuditId() {
		return auditId;
	}
	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
	public String getRecipientAccountNumber() {
		return recipientAccountNumber;
	}
	public void setRecipientAccountNumber(String recipientAccountNumber) {
		this.recipientAccountNumber = recipientAccountNumber;
	}
	public String getSenderAccountNumber() {
		return senderAccountNumber;
	}
	public void setSenderAccountNumber(String senderAccountNumber) {
		this.senderAccountNumber = senderAccountNumber;
	}
	public String getRecipientInstitutionCode() {
		return recipientInstitutionCode;
	}
	public void setRecipientInstitutionCode(String recipientInstitutionCode) {
		this.recipientInstitutionCode = recipientInstitutionCode;
	}
	public String getRecipientBankCode() {
		return recipientBankCode;
	}
	public void setRecipientBankCode(String recipientBankCode) {
		this.recipientBankCode = recipientBankCode;
	}
	public String getParticipantId() {
		return participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
	public String getBeneficiaryAccountNumber() {
		return beneficiaryAccountNumber;
	}
	public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
		this.beneficiaryAccountNumber = beneficiaryAccountNumber;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getBeneficiaryBankCode() {
		return beneficiaryBankCode;
	}
	public void setBeneficiaryBankCode(String beneficiaryBankCode) {
		this.beneficiaryBankCode = beneficiaryBankCode;
	}
	public String getBeneficiaryInstitutionCode() {
		return beneficiaryInstitutionCode;
	}
	public void setBeneficiaryInstitutionCode(String beneficiaryInstitutionCode) {
		this.beneficiaryInstitutionCode = beneficiaryInstitutionCode;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public MiddleWareBankRequestModel getMiddleWareAppInfoData() {
		return middleWareAppInfoData;
	}
	public void setMiddleWareAppInfoData(MiddleWareBankRequestModel middleWareAppInfoData) {
		this.middleWareAppInfoData = middleWareAppInfoData;
	}	
}
