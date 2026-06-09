package ams.cms.utility;

import java.io.Serializable;

public class ExternalServerTokenModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String token_type;
	private String expires_in;
	private String ext_expires_in;
	private String access_token;
	private String token_active;
	private String token_id;
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	public String getExt_expires_in() {
		return ext_expires_in;
	}
	public void setExt_expires_in(String ext_expires_in) {
		this.ext_expires_in = ext_expires_in;
	}
	public String getToken_active() {
		return token_active;
	}
	public void setToken_active(String token_active) {
		this.token_active = token_active;
	}
	public String getToken_id() {
		return token_id;
	}
	public void setToken_id(String token_id) {
		this.token_id = token_id;
	}
	@Override
	public String toString() {
		return "ExternalServerTokenModel [access_token=" + access_token + "]";
	}
}
