package ams.cms.utility;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class PayloadReqRes implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private String salt;

	private String payload;

	private String code;
	
	private String deviceId;

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String toString() {
		return "PayloadReqRes [salt=" + salt + ", payload=" + payload + ", code=" + code + ", deviceId=" + deviceId
				+ "]";
	}

}
