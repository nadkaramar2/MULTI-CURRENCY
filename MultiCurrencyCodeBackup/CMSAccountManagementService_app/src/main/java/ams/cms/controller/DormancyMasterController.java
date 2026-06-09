package ams.cms.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.config.EncryptDecryptConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.DormantAccountMaster;
import ams.cms.services.DormantMasterService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;

@RestController
@RequestMapping("/dormancy")
public class DormancyMasterController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(DormancyMasterController.class);
	
	@Autowired
	private DormantMasterService dormantMasterService;
	
	@Autowired
	private  ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	EncryptDecryptConfig encryptDecryptData;
	
	
	@RequestMapping(value = "/makerprocess", method = RequestMethod.POST)
	public ResponseEntity<?> makerProcess(@RequestBody DormantAccountMaster dormantAccountMaster )
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			processResponse = dormantMasterService.makerProcessForDormancy(dormantAccountMaster);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("In addClosureRequest Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(processResponse);
	}
	
	@RequestMapping(value = "/makervalidation", method = RequestMethod.POST)
	public ResponseEntity<?> makerProcessDormancy(@RequestBody AccountCreation accountCreation )
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			processResponse = dormantMasterService.DormancyValidationProcess(accountCreation);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("In addClosureRequest Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(processResponse);
	}
	
	
	@RequestMapping(value = "/checkervalidation", method = RequestMethod.POST)
	public ResponseEntity<?> checkerProcessValidation(@RequestBody AccountCreation accountCreation )
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			processResponse = dormantMasterService.checkerProcessValidation(accountCreation);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("In addClosureRequest Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(processResponse);
	}

	@RequestMapping(value = "/requestprocessmobile", method = RequestMethod.POST)
	public ResponseEntity<?> makerProcessDormancyMobile(HttpServletRequest request, @RequestBody PayloadReqRes req)
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
			
		String decrypt = encryptDecryptData.decryptPayloadReqRes(req);			
		AccountCreation accountCreation =  new ObjectMapper().readValue(decrypt, AccountCreation.class);
		
		
			processResponse = dormantMasterService.makerProcessForMobileDormancy(accountCreation);
		} 
		catch (Exception e) 
		{
			amsLogger.writeInfoLog("Inside MakerRequestProcessMobile processResp2::"+processResponse); 
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		
	}
	
	@RequestMapping(value = "/checkerProcess", method = RequestMethod.POST)
	public ResponseEntity<?> checkerProcess(@RequestBody DormantAccountMaster dormantAccountMaster )
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			processResponse = dormantMasterService.checkerProcessForDormancy(dormantAccountMaster);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("In addClosureRequest Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(processResponse);
	}
	
	
	@RequestMapping(value = "/status", method = RequestMethod.POST)
	public ResponseEntity<?> dormantStatusByAccountNo(@RequestBody DormantAccountMaster dormantAccountMaster )
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			processResponse = dormantMasterService.dormantStatusByAccountNo(dormantAccountMaster);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("In addClosureRequest Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(processResponse);
	}
	
	
	
	
}
