
package ams.cms.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import ams.cms.config.AppInfo;
import ams.cms.config.CommonConstants;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.config.TransactionHandler;
import ams.cms.handler.AccountLoadMasterHandler;
import ams.cms.logger.AMSLogger;
import ams.cms.model.TxnReqRes;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.util.ProcessWebResponse;
import ams.cms.utility.PayloadReqRes;

@RestController
@RequestMapping("/transactionHandler") 
public class TransactionHandlerController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(TransactionHandlerController.class);
	
	@Autowired
	private EncryptDecryptConfig encryptDecryptData;
	
	@Autowired 
	private TransactionHandler transactionHandler;
	
	@Autowired
	private AccountLoadMasterHandler accountLoadMasterHandler;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	private	AppInfo appInfo;
	
	@RequestMapping(value = "/processTxn", method = RequestMethod.POST) 
	public ResponseEntity<?> processTransactionData(HttpServletRequest request,@RequestBody TxnReqRes txnReqRes,@RequestBody PayloadReqRes req) 
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
			CommonConstants.applicationName = "NIGERIA"; 
			TxnReqRes txnReqResponse = transactionHandler.processTransaction(txnReqRes);	
			return ResponseEntity.ok(txnReqResponse);
		}
		catch (Exception e) 
		{
			System.out.println("Exception in processTransactionData::"+e);
			e.printStackTrace(); 
		} 
		return ResponseEntity.ok(null); 
	} 
	
	@RequestMapping(value = "/processToLoadBalance", method = RequestMethod.POST) 
	public ResponseEntity<?> processToLoadBalance(@RequestBody TxnReqRes txnReqRes) 
	{ 
		try
		{ 
			CommonConstants.applicationName = "NIGERIA";
			TxnReqRes txnReqResponse = accountLoadMasterHandler.processTransactionToLoadBalance(txnReqRes);	
			return ResponseEntity.ok(txnReqResponse);
		}
		catch (Exception e) 
		{
			System.out.println("Exception in processToLoadBalance::"+e);
			e.printStackTrace(); 
		} 
		return ResponseEntity.ok(null); 
	}
	//------------------------------------------------------------------------------------------------//
	
	@RequestMapping(value = "/processTransactionData", method = RequestMethod.POST) 
	public ResponseEntity<?> processTransactnData(@RequestBody TxnReqRes txnReqRes) 
	{ 
		ProcessWebResponse processWebResponse = new ProcessWebResponse();
		try
		{ 
			CommonConstants.applicationName = "NIGERIA"; 
			TxnReqRes txnReqResponse = transactionHandler.processTransaction(txnReqRes);
			
			processWebResponse.setCode("S0000");
			processWebResponse.setStatus("Success");
			processWebResponse.setMessage("Successfully Process.");
			processWebResponse.setTxnReqResponse(txnReqResponse);
		}
		catch (Exception e) 
		{
			System.out.println("Exception in processTransactionData::"+e);
			processWebResponse.setCode("E0000");
			processWebResponse.setStatus("Failed");
			processWebResponse.setMessage("Internal Server Error ");
			e.printStackTrace(); 
		} 
		return ResponseEntity.ok(processWebResponse); 
	}
	
}
