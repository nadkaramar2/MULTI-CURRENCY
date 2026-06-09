package ams.cms.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUser implements UserDetails
{
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	private boolean isAlreadyLogin = false;
	private String jwtToken;
	
	private Boolean accountNonExpired = true;
	private Boolean accountNonLocked = true;
	private Boolean credentialsNonExpired = true;
	private Boolean enabled = true;
	
	public SecurityUser(String userName, String password, Collection<? extends GrantedAuthority> authorities, 
			boolean isAlreadyLogin) 
	{
		this.userName = userName;
		this.password = password;
		this.authorities = authorities;
		this.isAlreadyLogin = isAlreadyLogin;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	public boolean isAlreadyLogin() {
		return this.isAlreadyLogin;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	public String getJwtToken() 
	{
		return this.jwtToken;
	}
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
}
