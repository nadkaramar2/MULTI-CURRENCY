package ams.cms.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.config.EncryptDecryptConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.services.CurrencyMasterService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;

@RestController
@RequestMapping("/currencymaster")
public class CurrencyMasterController {
	
	private AMSLogger amsLogger = AMSLogger.getInstance(CurrencyMasterController.class);
	
	
	@Autowired
	private  ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	private CurrencyMasterService currencyMasterService;
	
	@Autowired
	EncryptDecryptConfig encryptDecryptData;
	
	@RequestMapping(value = "/getcurrencylist", method = RequestMethod.POST)
	public ResponseEntity<?> getCurrencyList(HttpServletRequest request, @RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		
		try
		{
			
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			
			
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
	
		processResponse = currencyMasterService.getCurrencyList();
		
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		
	}

}
