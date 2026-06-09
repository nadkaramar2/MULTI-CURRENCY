package ams.cms.api.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class TxnStatementDetailsMaster implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String code;
	private Boolean success;
	private String message;
	private TxnStatementDetails data;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public TxnStatementDetails getData() {
		return data;
	}

	public void setData(TxnStatementDetails data) {
		this.data = data;
	}
}
