package ams.cms.util;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FundTransferStatusEnquiryResponse implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private Boolean success;
	
	private String message;	
	private String requestId;
	
	private FundTransferStatusResponse data;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public FundTransferStatusResponse getData() {
		return data;
	}

	public void setData(FundTransferStatusResponse data) {
		this.data = data;
	}
}
