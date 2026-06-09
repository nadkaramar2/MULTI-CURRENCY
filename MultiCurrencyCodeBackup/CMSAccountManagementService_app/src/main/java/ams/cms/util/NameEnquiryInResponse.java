package ams.cms.util;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import ams.cms.model.FundTransferResponse;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class NameEnquiryInResponse implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String code;
	private String status;
	private String message;
	private Boolean success;
	private FundTransferResponse data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public FundTransferResponse getData() {
		return data;
	}
	public void setData(FundTransferResponse data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "NameEnquiryInResponse [code=" + code + ", status=" + status + ", message=" + message + ", success="
				+ success + ", data=" + data + "]";
	}
}
