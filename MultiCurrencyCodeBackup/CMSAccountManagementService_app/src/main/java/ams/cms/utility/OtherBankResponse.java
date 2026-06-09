package ams.cms.utility;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class OtherBankResponse implements Serializable //outbound Obj
{
	private static final long serialVersionUID = 1L;

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

	@Override
	public String toString() {
		return "OtherBankResponse [status=" + status + ", reference=" + reference + ", information=" + information
				+ ", amount=" + amount + ", fee=" + fee + ", appId=" + appId + ", auditId=" + auditId + "]";
	}
}
