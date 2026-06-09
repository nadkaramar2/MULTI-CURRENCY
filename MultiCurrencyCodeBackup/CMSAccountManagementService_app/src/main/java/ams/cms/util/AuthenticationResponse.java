package ams.cms.util;

public class AuthenticationResponse {

	private String token;
	private String status;
	private String resDesc;
	private String sysId;
	private ProcessResponse response;

	public AuthenticationResponse() {
		super();
	}

	public AuthenticationResponse(String sysId,String token, String status, String resDesc) {
		this.setSysId(sysId);
		this.setToken(token);
		this.setStatus(status);
		this.setResDesc(resDesc);
		this.setResponse(resDesc);
		
		
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResDesc() {
		return resDesc;
	}

	public void setResDesc(String resDesc) {
		this.resDesc = resDesc;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public ProcessResponse getResponse() {
		return response;
	}

	public void setResponse(String resDesc) {
		this.response = new ProcessResponse();
		this.response.setDesc(resDesc);
		
		if("SUCCESSFUL".equalsIgnoreCase(resDesc)) {
			this.response.setCode("S0000");
		}else{
			this.response.setCode("E0000");
		}
	}

	
	
}
