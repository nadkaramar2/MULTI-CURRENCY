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

import ams.cms.api.model.PreAccountMaster;
import ams.cms.api.service.PreAccountMasterService;
import ams.cms.config.AppInfo;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.jwt.JwtUtil;
import ams.cms.logger.AMSLogger;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;
import ams.cms.utility.UserSessionControl;

@RestController
public class SignOutController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(SignOutController.class);

	@Autowired
	PreAccountMasterService preAccountMasterService;

	@Autowired
	UserSessionControl userController;

	@Autowired 
	JwtUtil jwtUtil;
	
	@Autowired
	EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	private	AppInfo appInfo;
	
	@RequestMapping(value = "/signOut", method = RequestMethod.POST)
	public ResponseEntity<?> signOut(HttpServletRequest request,@RequestBody PayloadReqRes req) throws Exception
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
			PreAccountMaster preAccountMaster = new ObjectMapper().readValue(decrypt, PreAccountMaster.class);
			PreAccountMaster preAccountMasterData = preAccountMasterService.getPreAccountInfo(preAccountMaster.getStrMobileNo());
			String sessionLogout = userController.logOutSessionUpdate(preAccountMasterData);

			if (sessionLogout.equalsIgnoreCase("Success"))
			{
				processResponse.setStatus("S0000");
				processResponse.setResult("Success");
				processResponse.setMessage("Logout Successfull");
			} 
			else
			{
				processResponse.setStatus("E0000");
				processResponse.setResult("Failure");
				processResponse.setMessage("Logout Failed");
			}
		}
		catch (Exception e) {
			processResponse.setStatus("E0000");
			processResponse.setResult("Failure");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
}
