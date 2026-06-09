package ams.cms.utility;

import java.io.Serializable;
import java.util.List;

public class MiddleWareBankResponse implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String status;
	
	private Boolean success;
	private String message;
	
	private List<BankMiddleWareData> data;

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

	public List<BankMiddleWareData> getData() {
		return data;
	}

	public void setData(List<BankMiddleWareData> data) {
		this.data = data;
	}

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

	@Override
	public String toString() {
		return "MiddleWareBankResponse [code=" + code + ", status=" + status + ", success=" + success + ", message="
				+ message + ", data=" + data + "]";
	}
}
