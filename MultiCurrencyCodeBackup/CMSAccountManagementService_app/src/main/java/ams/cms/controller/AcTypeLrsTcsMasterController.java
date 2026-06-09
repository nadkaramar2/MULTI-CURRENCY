package ams.cms.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.config.EncryptDecryptConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AcTypeLrsTcsMaster;
import ams.cms.services.AcTypeLrsTcsMasterService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;

@RestController
@RequestMapping("/accountlrstcsmaster")
public class AcTypeLrsTcsMasterController {
	

	private AMSLogger amsLogger = AMSLogger.getInstance(AcTypeLrsTcsMasterController.class);
	

	@Autowired
	private  AcTypeLrsTcsMasterService acTypeLrsTcsMasterService;
	
	@Autowired
	private  ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	EncryptDecryptConfig encryptDecryptData;
	
	@RequestMapping(value = "/checklrslimit", method = RequestMethod.POST)
	public ProcessResponse checkLrsLimit( @RequestBody AcTypeLrsTcsMaster req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		
		try
		{
			amsLogger.writeInfoLog("Inside CheckLRSLIMIT[1]::"+processResponse); 
			
		//	ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			
			
		/*
		 * if ("E0000".equalsIgnoreCase(processResps.getCode())) { return
		 * ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps));
		 * }
		 */
			
		//String decrypt = encryptDecryptData.decryptPayloadReqRes(req);			
	//	AcTypeLrsTcsMaster accountCreation =  new ObjectMapper().readValue(decrypt, AcTypeLrsTcsMaster.class);
		
		
		processResponse = acTypeLrsTcsMasterService.checkLrsLimit(req);
		
		} 
		catch (Exception e) 
		{
			amsLogger.writeInfoLog("Inside AcTypeLrsTcsMasterController processResp2::"+processResponse); 
		}
		//return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		return processResponse;
		
	}
	

}
