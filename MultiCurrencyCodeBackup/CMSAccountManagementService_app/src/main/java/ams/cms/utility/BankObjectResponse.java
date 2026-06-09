package ams.cms.utility;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankObjectResponse implements Serializable //Inbound and Outbound Obj
{
	private static final long serialVersionUID = 1L;
	
	private Boolean success;	
	private OtherBankSendMoneyResponse data;
	private String message;
	
	@JsonProperty("response_code")
	private String responseCode;
	
	@JsonProperty("response_message")
	private String responseMessage;
	
	@JsonProperty("response_time")
	private String responseTime;
	
	@JsonProperty("transaction_reference")
	private String transactionReference;
	
	@JsonProperty("institution_reference")
	private String institutionReference;
	
	@JsonProperty("flex_reply_code")
	private String flexReplyCode;
	
	@JsonProperty("flex_reply_msg")
	private String flexReplyMsg;
	
	@JsonProperty("db_error_msg")
	private String dbErrorMsg;

	@JsonProperty("status")
	private String status;
	
	@JsonProperty("reference")
	private String reference;
	
	@JsonProperty("information")
	private String information;
	
	@JsonProperty("amount")
	private String amount;
	
	@JsonProperty("fee")
	private String fee;
	
	@JsonProperty("appId")
	private String appId;
	
	@JsonProperty("auditId")
	private String auditId;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

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
	
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public String getTransactionReference() {
		return transactionReference;
	}

	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}

	public String getInstitutionReference() {
		return institutionReference;
	}

	public void setInstitutionReference(String institutionReference) {
		this.institutionReference = institutionReference;
	}

	public String getFlexReplyCode() {
		return flexReplyCode;
	}

	public void setFlexReplyCode(String flexReplyCode) {
		this.flexReplyCode = flexReplyCode;
	}

	public String getFlexReplyMsg() {
		return flexReplyMsg;
	}

	public void setFlexReplyMsg(String flexReplyMsg) {
		this.flexReplyMsg = flexReplyMsg;
	}

	public String getDbErrorMsg() {
		return dbErrorMsg;
	}

	public void setDbErrorMsg(String dbErrorMsg) {
		this.dbErrorMsg = dbErrorMsg;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public OtherBankSendMoneyResponse getData() {
		return data;
	}

	public void setData(OtherBankSendMoneyResponse data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "BankObjectResponse [success=" + success + ", data=" + data + ", message=" + message + ", responseCode="
				+ responseCode + ", responseMessage=" + responseMessage + ", responseTime=" + responseTime
				+ ", transactionReference=" + transactionReference + ", institutionReference=" + institutionReference
				+ ", flexReplyCode=" + flexReplyCode + ", flexReplyMsg=" + flexReplyMsg + ", dbErrorMsg=" + dbErrorMsg
				+ ", status=" + status + ", reference=" + reference + ", information=" + information + ", amount="
				+ amount + ", fee=" + fee + ", appId=" + appId + ", auditId=" + auditId + "]";
	}
}
