package ams.cms.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.config.AppInfo;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.ParticipantAppInfoMaster;
import ams.cms.services.ParticipantAppInfoMasterService;
import ams.cms.utility.PayloadReqRes;

@Component
public class ApiSecretKeyUtility 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(ApiSecretKeyUtility.class);	
	
	private static final String SEPARATOR = "|";
	
	@Autowired
	private EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	private ParticipantAppInfoMasterService participantAppInfoMasterService;
	
	@Autowired
	private	AppInfo appInfo;
	
	@SuppressWarnings("unchecked")
	public ProcessResponse validateRequestAPI(HttpServletRequest request, PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse(); 
		try 
		{
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");			
			
			processResponse = validateApiKey(request, processResponse);
			if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
			{
				String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
				appInfo.setDecryptedPayload(decrypt);
				
				LinkedHashMap<String, String> requestJson = new ObjectMapper().readValue(decrypt, LinkedHashMap.class);
				
				processResponse = validateApiRequest(request, processResponse, requestJson);
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal server exception during decryption");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	public ProcessResponse validateApiRequest(HttpServletRequest request, ProcessResponse processResponse, LinkedHashMap<String, String> linkedMapData) 
	{
		try 
		{
			final String SECRET_KEY_DATA = appInfo.getSecretKey();
			
			String apiHash = request.getHeader("apihash");
			appInfo.setApiHash(apiHash);
			
			TreeMap<String, String> requestObject = new TreeMap<String, String> (linkedMapData);
			
			String finalStr = "";
			for (Map.Entry<String, String> entry : requestObject.entrySet()) 
			{
				if(entry.getValue() instanceof String)
				{
					String value = (String) entry.getValue();
					if (finalStr.length() == 0)
					{
						if (value != null && value.trim().length() > 0) 
						{
							finalStr = value.trim() + SEPARATOR;
						}
					}
					else
					{
						if (value != null && value.trim().length() > 0) 
						{
							finalStr = finalStr + value.trim() + SEPARATOR;
						}
					}
				  }
				  else
				  {
					  amsLogger.writeInfoLog("This is an object which not belong to String object");
				  }
			}
			
			finalStr = finalStr + SECRET_KEY_DATA;
			
			String hashValue = SHA_512(finalStr);			
			if (!apiHash.equalsIgnoreCase(hashValue))
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Unauthorized Request");
			}						
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal server exception");
		}
		return processResponse;
	}
	
	public ProcessResponse validateApiKey(HttpServletRequest request, ProcessResponse processResponse) 
	{
		try 
		{
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");
			
			String apiKeyVal = request.getHeader("apikey");
			amsLogger.writeInfoLog("apiKeyVal::"+apiKeyVal);
			appInfo.setApiKey(apiKeyVal); 
			
			if (apiKeyVal != null && apiKeyVal.trim().length() > 0)    
			{
				String participantId = apiKeyVal.substring(apiKeyVal.lastIndexOf("SEPARATOR")+9);
				
				ParticipantAppInfoMaster participantAppInfoMaster = new ParticipantAppInfoMaster();
				participantAppInfoMaster.setStrParticipantId(participantId);
				
				appInfo.setStrParticipantId(participantId);//New Added for Montra
				
				ParticipantAppInfoMaster participantAppInfoMasterInstance = participantAppInfoMasterService.getParticipantAppInfoObject(participantAppInfoMaster);
				
				if (participantAppInfoMasterInstance != null) 
				{
					String apikey = participantAppInfoMasterInstance.getStrApiKey();
					amsLogger.writeInfoLog("From DB apikey::"+apiKeyVal);
					
					//set iv,phrase,apikey and secret key Start
					appInfo.setIv(participantAppInfoMasterInstance.getStrIv());
					appInfo.setPhrase(participantAppInfoMasterInstance.getStrPhrase());
					appInfo.setSalt(participantAppInfoMasterInstance.getStrSalt());
					appInfo.setApiKey(apikey);
					appInfo.setSecretKey(participantAppInfoMasterInstance.getStrSecretKey());
					//set iv,phrase,apikey and secret key End
					
					if (!apiKeyVal.equalsIgnoreCase(apikey)) 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Invalid api-key");
					}
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Invalid request");
				}
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("api-key not found in header");
			}
			
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal server exception");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	private static String SHA_512(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException 
	{
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(str.getBytes("UTF-8"));
        byte byteData[] = md.digest(); 
        
        StringBuffer hashCodeBuffer = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            hashCodeBuffer.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return hashCodeBuffer.toString().toUpperCase();
	}
}
