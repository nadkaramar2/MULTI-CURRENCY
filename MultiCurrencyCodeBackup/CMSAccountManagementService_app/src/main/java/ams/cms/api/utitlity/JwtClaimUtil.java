package ams.cms.api.utitlity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.jwt.JwtUtil;
import ams.cms.logger.AMSLogger;

@Component
public class JwtClaimUtil implements Serializable
{
	private AMSLogger amsLogger = AMSLogger.getInstance(JwtClaimUtil.class);
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private Map<String, DataEncryptionDecryptionUtil> userInfo = new HashMap<String, DataEncryptionDecryptionUtil>();

	public Map<String, DataEncryptionDecryptionUtil> getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(Map<String, DataEncryptionDecryptionUtil> userInfo) {
		this.userInfo = userInfo;
	}
	
	
	public String getUserName(HttpServletRequest request) 
	{
		String requestTokenHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;		
		
		if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) 
		{
			jwtToken = requestTokenHeader.substring(7);
		}
		else
		{
			jwtToken = requestTokenHeader;
		}
		if(jwtToken!=null )
		{
			try 
			{
				username = this.jwtUtil.getUsernameFromToken(jwtToken);
			}
			catch (Exception e) {
				amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			}
		}
		return username;
	}
}
