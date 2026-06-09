package ams.cms.utility;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OtherBankSendMoneyResponse implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String status;
	private String reference;
	private String information;
	private String amount;
	private String fee;
	private String appId;
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
	@Override
	public String toString() {
		return "OtherBankSendMoneyResponse [status=" + status + ", reference=" + reference + ", information="
				+ information + ", amount=" + amount + ", fee=" + fee + ", appId=" + appId + ", auditId=" + auditId
				+ "]";
	}
}
