package ams.cms.utility;

import java.io.Serializable;

public class MiddleWareTokenModel implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String middleware_token;
	private String token_active;
	private String token_id;
	private String created_date;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMiddleware_token() {
		return middleware_token;
	}
	public void setMiddleware_token(String middleware_token) {
		this.middleware_token = middleware_token;
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
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	
	
	
	
}
