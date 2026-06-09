package ams.cms.utility;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class NameEnquiryMiddleWareResponse implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String message;
	private boolean success;
	
	@JsonProperty("resp_code")
	private String respCode;
	
	@JsonProperty("resp_desc")
	private String respDesc;
	
	@JsonProperty("tranid")
	private String tranId;
	
	@JsonProperty("referencenumber")
	private String referenceNumber;
	
	@JsonProperty("obj")
	private Data obj;

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespDesc() {
		return respDesc;
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public Data getObj() {
		return obj;
	}

	public void setObj(Data obj) {
		this.obj = obj;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return "NameEnquiryMiddleWareResponse [code=" + code + ", message=" + message + ", success=" + success
				+ ", respCode=" + respCode + ", respDesc=" + respDesc + ", tranId=" + tranId + ", referenceNumber="
				+ referenceNumber + ", obj=" + obj + "]";
	}	
}
