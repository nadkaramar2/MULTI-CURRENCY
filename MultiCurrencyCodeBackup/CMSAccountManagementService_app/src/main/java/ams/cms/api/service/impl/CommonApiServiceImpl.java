package ams.cms.api.service.impl;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

import io.jsonwebtoken.Claims;
import ams.cms.api.service.CommonApiService;
import ams.cms.jwt.JwtUtil;
import ams.cms.security.CustomUserDetailsService;

@Transactional
@Service
public class CommonApiServiceImpl implements CommonApiService
{
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Override
	public String getGeneratedJWTToken(Map<String, String> userHashMap)
	{
		UserDetails userDetails = null;
		String userName = "";
		if ("BY-PASS-REQ-API".equalsIgnoreCase(userHashMap.get("USER_NAME")))
		{
			userName = userHashMap.get("USER_NAME");
			userDetails = this.customUserDetailsService.getByPassUserDetails(userHashMap);
		}
		else 
		{
			userName = userHashMap.get("USER_NAME");
			userDetails = this.customUserDetailsService.loadUserByUsername(userHashMap.get("USER_NAME"));
		}
		String jwtToken = jwtUtil.generateToken(userDetails);
		
		//this.customUserDetailsService.updatejwtToken(jwtToken);
		this.customUserDetailsService.updatejwtToken(jwtToken, userName);
		
		return jwtToken;
	}
	
	//Added by Sunny soni for new token generation Start

	@Override
	public JsonObject getGeneratedJWTTokenWithIVandPhrase(Map<String, String> userHashMap)
	{
		JsonObject res = new JsonObject();
		String userName = "";
		try 
		{
			UserDetails userDetails = null;
			//if ("by-pass-req-api".equalsIgnoreCase(userHashMap.get("user_name")))
			if (userHashMap.containsKey("user_name") &&  userHashMap.get("user_name")!=null)
			{
				userName = userHashMap.get("user_name");
				userDetails = this.customUserDetailsService.getByPassUserDetails(userHashMap);
			}
			else 
			{
				userName = userHashMap.get("USER_NAME");
				userDetails = this.customUserDetailsService.loadUserByUsername(userHashMap.get("USER_NAME"));
			}
			
			Claims claims = jwtUtil.generateClaims(userDetails);
			
			res.addProperty("iv", jwtUtil.getIVPhraseFromClaim(claims));
			res.addProperty("pharse", jwtUtil.getPhraseFromClaim(claims));			
			
			String jwtToken = jwtUtil.generateToken(claims);
			res.addProperty("token", jwtToken);
			
			//this.customUserDetailsService.updatejwtToken(jwtToken);
			
			this.customUserDetailsService.updatejwtToken(jwtToken, userName);
			
			return res;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Added by Sunny soni for new token generation End
}
