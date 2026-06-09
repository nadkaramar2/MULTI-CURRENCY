package ams.cms.config;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;
import ams.cms.jwt.JwtUtil;
import ams.cms.logger.AMSLogger;
import ams.cms.util.AesUtil;
import ams.cms.util.NameEnquiryInResponse;
import io.jsonwebtoken.Claims;

@Component
public class EncryptDecryptConfig 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(EncryptDecryptConfig.class);
	
	private static ObjectMapper mapper = new ObjectMapper();

	//@Autowired
	//private TokenUtils tokenUtils;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AppInfo appInfo;
	
	//Constants constant;

	//public PayloadReqRes encryptPayloadReqRes(PayloadReqRes req, ProcessResponse result, Claims claim) {
	public PayloadReqRes encryptPayloadReqRes(PayloadReqRes req, ProcessResponse result) 
	{
		try
		{
			AesUtil aesUtil = new AesUtil(256, 1000);
			//Added By Sunny Soni for getting validate request claim Start
			Claims claim = jwtUtil.getRequestClaim();
			//Added By Sunny Soni for getting validate request claim End
			
			//Added for Nigeria based salt changes Start			
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				if (req.getSalt() == null)
				{
					req.setSalt(appInfo.getSalt());
				}
			}			
			//Added for Nigeria based salt changes End
			
			String data = aesUtil.encrypt(req.getSalt(), jwtUtil.getIVPhraseFromClaim(claim), jwtUtil.getPhraseFromClaim(claim), this.convertJsonToString(result));
			
			req.setPayload(data);
			req.setCode("S0000");
		} 
		catch (Exception e)
		{
			req.setCode("E0000");
			//e.printStackTrace();
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return req;
	}
	public PayloadReqRes encryptPayloadReqRes(PayloadReqRes req, NameEnquiryInResponse result) 
	{
		try
		{
			AesUtil aesUtil = new AesUtil(256, 1000);
			//Added By Sunny Soni for getting validate request claim Start
			Claims claim = jwtUtil.getRequestClaim();
			//Added By Sunny Soni for getting validate request claim End
			
			//Added for Nigeria based salt changes Start			
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				if (req.getSalt() == null)
				{
					req.setSalt(appInfo.getSalt());
				}
			}			
			//Added for Nigeria based salt changes End
			
			String data = aesUtil.encrypt(req.getSalt(), jwtUtil.getIVPhraseFromClaim(claim), jwtUtil.getPhraseFromClaim(claim), this.convertJsonToString(result));
			
			req.setPayload(data);
			req.setCode("S0000");
		} 
		catch (Exception e)
		{
			req.setCode("E0000");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return req;
	}
	public PayloadReqRes encryptPayloadReqRes(PayloadReqRes req, Object result) 
	{
		try
		{
			AesUtil aesUtil = new AesUtil(256, 1000);
			Claims claim = jwtUtil.getRequestClaim();
			
			//Added for Nigeria based salt changes Start
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				if (req.getSalt() == null)
				{
					req.setSalt(appInfo.getSalt());
				}
			}			
			//Added for Nigeria based salt changes End
			
			String data = aesUtil.encrypt(req.getSalt(), jwtUtil.getIVPhraseFromClaim(claim), jwtUtil.getPhraseFromClaim(claim), this.convertJsonToString(result));
			
			req.setPayload(data);
			req.setCode("S0000");
		} 
		catch (Exception e)
		{
			req.setCode("E0000");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return req;
	}
	
	public String decryptPayloadReqRes(PayloadReqRes req) throws Exception
	{
		AesUtil aesUtil = new AesUtil(256, 1000);			
		Claims claim = jwtUtil.getRequestClaim();
		

		String decryptedData = aesUtil.decrypt(req.getSalt(), jwtUtil.getIVPhraseFromClaim(claim), jwtUtil.getPhraseFromClaim(claim), req.getPayload());		
		return decryptedData;
	}
	

	public String convertJsonToString(Object obj) 
	{
		String reqres = "";
		try
		{
			reqres = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} 
		catch (JsonProcessingException e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return reqres;
	}
}
