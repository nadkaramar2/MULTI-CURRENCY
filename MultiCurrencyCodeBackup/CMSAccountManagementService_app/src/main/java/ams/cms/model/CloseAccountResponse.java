package ams.cms.model;

import java.io.Serializable;

public class CloseAccountResponse implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String code;
	
    private String status;
    private String msg;
    
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
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "CloseAccountResponse [code=" + code + ", status=" + status + ", msg=" + msg + "]";
	}
}
