package ams.cms.jwt;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import ams.cms.logger.AMSLogger;
import ams.cms.security.CustomUserDetailsService;
import ams.cms.security.SecurityUser;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
	private AMSLogger amsLogger = AMSLogger.getInstance(JwtAuthenticationFilter.class);
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public static String jwtTokenData;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException 
	{
		try 
		{
			
			String requestTokenHeader = request.getHeader("Authorization");
			String username = null;
			String jwtToken = null;
			
			String requestUrl = getFullURL(request);
			
			//amsLogger.writeInfoLog("%%%%%% --- Request URL=["+requestUrl+"]");
			if (requestUrl.indexOf("/actuator/health/") == -1) 
			{
				jwtUtil.setClaims(null);
			}
			
			if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) 
			{
				jwtToken = requestTokenHeader.substring(7);
			}
			else
			{
				jwtToken = requestTokenHeader;
			}
			//amsLogger.writeInfoLog("%%%%%% --- Inside doFilterInternal method jwtToken::["+jwtToken+"]");
			if(jwtToken!=null )
			{
				jwtTokenData = jwtToken;
				try 
				{
					username = this.jwtUtil.getUsernameFromToken(jwtToken);
					//amsLogger.writeInfoLog("%%%%%% --- Inside doFilterInternal method username::["+username+"]");
				}
				catch (Exception e) {
					amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
				}
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
				{
					SecurityUser securityUser = (SecurityUser) this.customUserDetailsService.getUserDetailsByUserName(username);

					Boolean isValid = false;
					if (securityUser != null) 
					{
						isValid = this.jwtUtil.validateTokenByJwtToken(jwtToken, securityUser);
					}
					if (isValid)
					{
						Claims claim = this.jwtUtil.getClaimsFromToken(jwtToken);		
						//amsLogger.writeInfoLog("%%%%%% --- Inside doFilterInternal method claim::["+claim+"]-jwtToken=["+jwtToken+"]");
						this.jwtUtil.setClaims(claim);
						
						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
						usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}
				}
			}
			
			//System.out.println("request::"+request);
			filterChain.doFilter(request, response);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	public static String getFullURL(HttpServletRequest request) 
	{
	    StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
	    String queryString = request.getQueryString();

	    if (queryString == null) {
	        return requestURL.toString();
	    } else {
	        return requestURL.append('?').append(queryString).toString();
	    }
	}
	
	//New Added for test
	private HttpServletRequest requestBody(HttpServletRequest req) 
	{
		try 
		{
			byte[] inputStreamBytes = StreamUtils.copyToByteArray(req.getInputStream());
			@SuppressWarnings("unchecked")
			Map<String, String> jsonRequest = new ObjectMapper().readValue(inputStreamBytes, Map.class);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return req;
	}
	
}
