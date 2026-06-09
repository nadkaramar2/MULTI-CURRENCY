package ams.cms.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.model.TierUpdateDto;
import ams.cms.api.service.PreAccountMasterService;
import ams.cms.api.service.UpgradeTierReqResService;
import ams.cms.config.AppInfo;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;
//created by ankit on 13-05-2023
@RestController
//@RequestMapping("/tier")
public class TierController
{
	private AMSLogger amsLogger = AMSLogger.getInstance(TierController.class);
	
	@Autowired
	private UpgradeTierReqResService upgradeTierReqResService;
	
	@Autowired
	private PreAccountMasterService preAccountMasterService;
	
	@Autowired
	private EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	private AppInfo appInfo;
	
	//save bvn inside kyc Details against custId or mobileNo
	//@RequestBody TierUpdateDto tierUpdateDto
	@RequestMapping(value = "/tier/setBvn", method = RequestMethod.POST) //[Note:Need to add Montra Id Validation change]
	public ResponseEntity<?> setBvnNo(HttpServletRequest request,@RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 
			 if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			  {
				  return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			  }
			
			 String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			TierUpdateDto tierUpdateDto = new ObjectMapper().readValue(decrypt, TierUpdateDto.class);
			
			processResponse = preAccountMasterService.addBvnNumber(tierUpdateDto);
		}
		catch(Exception e) 
		{
			processResponse.setCode("E000");
			processResponse.setStatus("Error");
			processResponse.setStatus("Internal Server Error.");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	//upgrade tier add in the customer master
	@RequestMapping(value = "/tier/upgradeTier", method = RequestMethod.POST)
	public ResponseEntity<?> upgradeTier(HttpServletRequest request, @RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			 amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 
			 if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			  {
				  return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			  }
		    
			 String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			TierUpdateDto tierUpdateDto = new ObjectMapper().readValue(decrypt, TierUpdateDto.class);
			
			processResponse = upgradeTierReqResService.addEntryInUpgradeTierReqRes(tierUpdateDto);
			amsLogger.writeInfoLog("Inside upgradeTier:: processResponse="+processResponse);
		}
		catch(Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Error");
			processResponse.setStatus("Internal Server Error!!.");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
}