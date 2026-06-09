package ams.cms.utility;

import java.io.Serializable;

public class NameEnquiryOtherBankObj implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private Boolean success;
	private	String message;
	
	private OtherBankDataObj data;

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

	public OtherBankDataObj getData() {
		return data;
	}

	public void setData(OtherBankDataObj data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "NameEnquiryOtherBankObj [success=" + success + ", message=" + message + ", data=" + data + "]";
	}
}
