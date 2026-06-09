package ams.cms.api.model;

import java.io.Serializable;

public class SecretCodeResponse implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Boolean success;
	private String message;
	private Integer data;
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
	public Integer getData() {
		return data;
	}
	public void setData(Integer data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "SecretCodeResponse [success=" + success + ", message=" + message + ", data=" + data + "]";
	}
}
