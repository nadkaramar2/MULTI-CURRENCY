package ams.cms.security;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetailsService;


public interface CustomUserDetails extends UserDetailsService
{
	SecurityUser getByPassUserDetails(Map<String, String> bypassMapData);
	
	SecurityUser getUserDetailsByUserName(String userName);
}
