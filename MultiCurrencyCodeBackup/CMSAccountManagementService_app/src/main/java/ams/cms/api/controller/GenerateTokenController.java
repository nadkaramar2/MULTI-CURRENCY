package ams.cms.api.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.api.service.CommonApiService;
import ams.cms.api.utitlity.Utils;
import ams.cms.config.CommonConstants;
import ams.cms.logger.AMSLogger;
import ams.cms.model.ParticipantAppInfoMaster;
import ams.cms.services.ParticipantAppInfoMasterService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.Data;

import com.google.gson.JsonObject;

@RestController
public class GenerateTokenController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(GenerateTokenController.class);
	
	//Added by Sunny soni for generate token with IV and Phrase Start
	@Autowired
	private CommonApiService commonApiService;
	//Added by Sunny soni for generate token with IV and Phrase End
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	private ParticipantAppInfoMasterService participantAppInfoMasterService;
	
	@RequestMapping(value = "/apiky", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<?> processApiKeyRequest(HttpServletRequest request, @RequestBody Map<String, String> apiKeyRequestParam)
	{
		JsonObject res = new JsonObject();
		JsonObject resObject = new JsonObject();
		try 
		{
			String appName = "Montra";
			String appSuffixName = "AMS";
			
			String genDeviceId = ams.cms.utility.Utils.getNewGeneratedSecretCode(appName, appSuffixName);
			amsLogger.writeInfoLog("Generated DeviceId=["+genDeviceId+"]");
			
			//Added by Sunny Soni for generate token with IV and Phrase Start
			Map<String, String> userhashmap = new HashMap<String, String>();
			String deviceId = apiKeyRequestParam.get("device_Id");
			amsLogger.writeInfoLog("From Request DeviceId=["+deviceId+"]");
			try
			{
				String countryName = apiKeyRequestParam.get("countryName");
				//CommonConstants.applicationName = countryName;
				CommonConstants.applicationName = "NIGERIA";
			}
			catch (Exception e) 
			{
				amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			}
			resObject.addProperty("status", "200");
			deviceId = deviceId.trim();
			//if (genDeviceId.equalsIgnoreCase(deviceId)) 
			{
				resObject.addProperty("code", "S0000");
				resObject.addProperty("desc", "SUCCESSFUL");
				
				Utils.deviceId = deviceId;
				userhashmap.put("user_name" , deviceId);
				
				//userhashmap.put("user_name", "by-pass-req-api");
				res = commonApiService.getGeneratedJWTTokenWithIVandPhrase(userhashmap);
			}
			/*else 
			{
				resObject.addProperty("code", "E0000");
				resObject.addProperty("desc", "UNSUCCESSFUL");
				res.addProperty("errorMsg", "REQUEST NOT PROPER!");
			}
			*/			
			//Added by Sunny Soni for generate token with IV and Phrase End			
		} 
		catch (Exception e) 
		{
			resObject.addProperty("status", "500");
			resObject.addProperty("code", "E0000");
			resObject.addProperty("desc", "UNABLE_TO_ACCESS_CLAIM");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		}
		res.add("response", resObject);
		amsLogger.writeInfoLog("processApiKeyRequest - " + res.toString());
		return ResponseEntity.ok(res.toString());
	}
	
	
	
	@RequestMapping(value = "/getIvPhraseSalt", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<?> requestProcessToGetApiIvAndPhrase(HttpServletRequest request, @RequestBody LinkedHashMap<String, String> apiKeyRequestParam)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");
			
			processResponse = apiSecretKeyUtility.validateApiKey(request, processResponse);
			if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
			{
				processResponse = apiSecretKeyUtility.validateApiRequest(request, processResponse, apiKeyRequestParam);			
				if ("S0000".equalsIgnoreCase(processResponse.getCode()))
				{
					String participantId = apiKeyRequestParam.get("authCode");
					
					ParticipantAppInfoMaster participantAppInfoMaster = new ParticipantAppInfoMaster();
					participantAppInfoMaster.setStrParticipantId(participantId);
					
					ParticipantAppInfoMaster participantAppInfoMasterInstance = participantAppInfoMasterService.getParticipantAppInfoObject(participantAppInfoMaster);
					if (participantAppInfoMasterInstance!=null && participantAppInfoMasterInstance.getStrID()!=null) 
					{
						processResponse.setMessage("Successfully Retrieve information.");
						
						Data data = new Data();
						data.setIv(participantAppInfoMasterInstance.getStrIv());
						data.setPhrase(participantAppInfoMasterInstance.getStrPhrase());
						data.setSalt(participantAppInfoMasterInstance.getStrSalt());
						processResponse.setData(data);
					}
					else 
					{
						processResponse.setCode("E0000");
						processResponse.setMessage("Invalid Auth Code!");
					}				
				}
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error");			
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		}
		return ResponseEntity.ok(processResponse);
	}
}
