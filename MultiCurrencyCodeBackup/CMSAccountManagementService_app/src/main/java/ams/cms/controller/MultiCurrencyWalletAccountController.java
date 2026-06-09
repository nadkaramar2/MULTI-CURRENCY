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

import ams.cms.api.handler.TransactionHandlerAPI;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.config.TransactionConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.CurrencyConversionRateRequest;
import ams.cms.model.MultiCurrencyWalletAccountMaster;
import ams.cms.services.MultiCurrencyWalletAccountService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;

@RestController
@RequestMapping("/currencywalletaccount")
public class MultiCurrencyWalletAccountController {
	
private AMSLogger amsLogger = AMSLogger.getInstance(MultiCurrencyWalletAccountController.class);
	

	@Autowired
	private  MultiCurrencyWalletAccountService multiCurrencyWalletAccountService;
	
	@Autowired
	private  ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	 TransactionHandlerAPI transactionHandlerAPI;
	
	@Autowired
	EncryptDecryptConfig encryptDecryptData;
	
	@RequestMapping(value = "/viewcurrencywallet", method = RequestMethod.POST)
	public ResponseEntity<?> viewCurrencyWallet(HttpServletRequest request, @RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			
			
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
		String decrypt = encryptDecryptData.decryptPayloadReqRes(req);			
		MultiCurrencyWalletAccountMaster accountCreation =  new ObjectMapper().readValue(decrypt, MultiCurrencyWalletAccountMaster.class);
		
		
			processResponse = multiCurrencyWalletAccountService.viewCurrencyWallet(accountCreation);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured In ViewCurrencyWallet ::"+ExceptionUtils.getStackTrace(e)); 
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		
	}
	
	@RequestMapping(value = "/getWalletPriorityList", method = RequestMethod.POST)
	public ResponseEntity<?> getWalletPriorityList(HttpServletRequest request, @RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			
			
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
		String decrypt = encryptDecryptData.decryptPayloadReqRes(req);			
		MultiCurrencyWalletAccountMaster accountCreation =  new ObjectMapper().readValue(decrypt, MultiCurrencyWalletAccountMaster.class);
		
		
			processResponse = multiCurrencyWalletAccountService.getWalletPriorityList(accountCreation);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured In ViewCurrencyWallet ::"+ExceptionUtils.getStackTrace(e)); 
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		
	}
	
	
	@RequestMapping(value = "/updateWalletPriorityList", method = RequestMethod.POST)
	public ResponseEntity<?> updateWalletPriorityList(HttpServletRequest request, @RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			amsLogger.writeExceptionLog(" In updateWalletPriorityList ::"+req);
			System.out.println("req::"+req);
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			
			
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
		String decrypt = encryptDecryptData.decryptPayloadReqRes(req);		
		amsLogger.writeExceptionLog(" In updateWalletPriorityList decrypt::"+decrypt);
		MultiCurrencyWalletAccountMaster accountCreation =  new ObjectMapper().readValue(decrypt, MultiCurrencyWalletAccountMaster.class);
		
		
		//validate pin
		
		 AccountCreation accountCreationObj = new AccountCreation();
		 accountCreationObj.setStrCustPin(accountCreation.getPin());
		 accountCreationObj.setStrCustId(accountCreation.getCustId());
		 TransactionConfig transactionHandlerAPIObj = transactionHandlerAPI.verifyCustomerPin(accountCreationObj);
			
		if(transactionHandlerAPIObj.getCode().equalsIgnoreCase("S0000")) {
		
		
			processResponse = multiCurrencyWalletAccountService.updateWalletPriorityList(accountCreation);
		}else {
			processResponse.setCode(transactionHandlerAPIObj.getCode());
			processResponse.setStatus(transactionHandlerAPIObj.getStatus());
			processResponse.setMessage(transactionHandlerAPIObj.getMessage());	
		}
			
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured In ViewCurrencyWallet ::"+ExceptionUtils.getStackTrace(e)); 
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		
	}
	
	@RequestMapping(value = "/getCurrencyCode", method = RequestMethod.POST)
	public ResponseEntity<?> getCurrencyCode(HttpServletRequest request, @RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			
			
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
		String decrypt = encryptDecryptData.decryptPayloadReqRes(req);			
		MultiCurrencyWalletAccountMaster accountCreation =  new ObjectMapper().readValue(decrypt, MultiCurrencyWalletAccountMaster.class);
		
		
			processResponse = multiCurrencyWalletAccountService.getCurrencyCodeByAccountNumber(accountCreation);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured In getCurrencyCode ::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		
	}
	
	@RequestMapping(value = "/currencyconversionrate", method = RequestMethod.POST)
	public ResponseEntity<?> getCurrencyConversionRate(HttpServletRequest request, @RequestBody PayloadReqRes req)
	{	
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
		String decrypt = encryptDecryptData.decryptPayloadReqRes(req);			
		CurrencyConversionRateRequest currencyConversionRateRequest =  new ObjectMapper().readValue(decrypt, CurrencyConversionRateRequest.class);
		
		
		processResponse = multiCurrencyWalletAccountService.getCurrencyConversionRate(currencyConversionRateRequest);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured In currencyconversionrate::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		
	}
	
	@RequestMapping(value = "/getCurrenyWalletListForStatemetView", method = RequestMethod.POST)
	public ResponseEntity<?> getCurrenyWalletListForStatemetView(HttpServletRequest request, @RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			
			
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
		String decrypt = encryptDecryptData.decryptPayloadReqRes(req);			
		MultiCurrencyWalletAccountMaster accountCreation =  new ObjectMapper().readValue(decrypt, MultiCurrencyWalletAccountMaster.class);
		
		
			processResponse = multiCurrencyWalletAccountService.getCurrenyWalletListForStatemetView(accountCreation);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured In ViewCurrencyWallet ::"+ExceptionUtils.getStackTrace(e)); 
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		
	}
	
	

}
