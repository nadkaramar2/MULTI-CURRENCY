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

import ams.cms.config.AppInfo;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.jwt.JwtUtil;
import ams.cms.logger.AMSLogger;
import ams.cms.model.LoadMoneyRequest;
import ams.cms.model.TransaferCurrencyRequest;
import ams.cms.services.LoadMoneyService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;

@RestController
@RequestMapping("loadmoney")
public class LoadMoneyController {
	
	private AMSLogger amsLogger = AMSLogger.getInstance(LoadMoneyController.class);

	
	@Autowired
	private LoadMoneyService loadMoneyService;
	
	@Autowired 
	JwtUtil jwtUtil;
	
	@Autowired
	EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	private	AppInfo appInfo;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	
	@RequestMapping(value = "/wallettransaction", method = RequestMethod.POST)
	public ResponseEntity<?> multiCurrencyLoadMoneyTxn(@RequestBody LoadMoneyRequest loadMoneyRequest )
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			processResponse = loadMoneyService.multiCurrencyLoadMoneyTxn(loadMoneyRequest);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			
		}
		return ResponseEntity.ok(processResponse);
	}
	
	//Same Api above With Encryption.
	@RequestMapping(path = "/wallettransactionprocess", method = RequestMethod.POST)
	public ResponseEntity<?> multiCurrencyLoadMoneyTxnProcess(HttpServletRequest request,@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			
			 if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			  {
				  return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			  }
			
			String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			LoadMoneyRequest loadMoneyRequest = new ObjectMapper().readValue(decrypt, LoadMoneyRequest.class);
			amsLogger.writeExceptionLog("LoadMoney Mobile  API ENTRY ::"+loadMoneyRequest);
			loadMoneyRequest.setParticipantId(participantId);
			processResponse = loadMoneyService.multiCurrencyLoadMoneyTxn(loadMoneyRequest);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	
	@RequestMapping(path = "/getloadmoneypaystructure", method = RequestMethod.POST)
	public ResponseEntity<?> getLoadMoneyPayStructure(HttpServletRequest request,@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			
			 if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			  {
				  return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			  }
			
			String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			LoadMoneyRequest loadMoneyRequest = new ObjectMapper().readValue(decrypt, LoadMoneyRequest.class);
			
			loadMoneyRequest.setParticipantId(participantId);
			processResponse = loadMoneyService.getLoadMoneyPayStructure(loadMoneyRequest);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	
	@RequestMapping(path = "/transferCurrency", method = RequestMethod.POST)
	public ResponseEntity<?> transferCurrency(HttpServletRequest request,@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			
			 if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			  {
				  return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			  }
			
			String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			TransaferCurrencyRequest transferCurrency = new ObjectMapper().readValue(decrypt, TransaferCurrencyRequest.class);
			amsLogger.writeExceptionLog("LoadMoney Mobile  API ENTRY ::"+transferCurrency);
		
			processResponse = loadMoneyService.transferCurrency(transferCurrency);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	

}
