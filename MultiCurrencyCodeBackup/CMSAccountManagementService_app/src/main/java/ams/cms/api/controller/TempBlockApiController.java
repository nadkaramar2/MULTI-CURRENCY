package ams.cms.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.handler.TemporaryCardBlockHandler;
import ams.cms.api.service.RestAPIClientService;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.config.TransactionConfig;
import ams.cms.model.CardAccountLinkage;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;

@RestController
@RequestMapping("/tempBlock")
public class TempBlockApiController {
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	private EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	TemporaryCardBlockHandler temporaryCardBlockHandler;
	
	@Autowired
	private RestAPIClientService restAPIClientService;
	
	
	@RequestMapping(value = "/tempBlockCard", method = RequestMethod.POST)
	public ResponseEntity<?> tempBlockCard(@RequestBody PayloadReqRes req) // custid, PIN, AccoutType, AccountNumber, CardToken, cardType, cardStatus
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			CardAccountLinkage cardAccountLinkage  = new ObjectMapper().readValue(decrypt, CardAccountLinkage.class);
			
			TransactionConfig transactionConfig = new TransactionConfig();	
			transactionConfig.setCardAccountLinkage(cardAccountLinkage);
			transactionConfig.setCode("S0000");
			
			ResponseEntity<?> cardNoFromClient = restAPIClientService.getCardDetails(cardAccountLinkage);		
			if(cardNoFromClient != null)
			{
				transactionConfig = temporaryCardBlockHandler.validatedCardValues(transactionConfig);
				if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
				{
					transactionConfig = temporaryCardBlockHandler.verifyUserPIN(transactionConfig);
					if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
					{
						ResponseEntity<?> cardNoFromClientResp = restAPIClientService.updateCardStatusBlock(cardAccountLinkage);
						transactionConfig = temporaryCardBlockHandler.updateCardStatusBlock(transactionConfig);
						temporaryCardBlockHandler.sendMail(transactionConfig);
					}
					else
					{
						transactionConfig.setCode("E0000");
						transactionConfig.setMessage("Incorrect PIN.");
					}
				}
				else
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage("Please Enter Correct Details");
				}
			}
			
			
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error");
			e.printStackTrace();
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(value = "/unblocktempBlockCard", method = RequestMethod.POST)
	public ResponseEntity<?> unblocktempBlockCard(@RequestBody PayloadReqRes req) // custid, PIN, AccoutType, AccountNumber, CardToken, cardType, cardStatus
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			CardAccountLinkage cardAccountLinkage  = new ObjectMapper().readValue(decrypt, CardAccountLinkage.class);
			
			TransactionConfig transactionConfig = new TransactionConfig();	
			transactionConfig.setCardAccountLinkage(cardAccountLinkage);
			transactionConfig.setCode("S0000");
			
			ResponseEntity<?> cardNoFromClient = restAPIClientService.getCardDetails(cardAccountLinkage);		
			if(cardNoFromClient != null)
			{
				transactionConfig = temporaryCardBlockHandler.validatedCardValues(transactionConfig);
				if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
				{
					transactionConfig = temporaryCardBlockHandler.verifyUserPIN(transactionConfig);
					if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
					{
						ResponseEntity<?> cardNoFromClientResp = restAPIClientService.updateCardStatusActive(cardAccountLinkage);
						transactionConfig = temporaryCardBlockHandler.updateCardStatusUnBlock(transactionConfig);
						temporaryCardBlockHandler.sendMail(transactionConfig);
					}
					else
					{
						transactionConfig.setCode("E0000");
						transactionConfig.setMessage("Incorrect PIN.");
					}
				}
				else
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage("Please Enter Correct Details.");
				}
			}
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error");
			e.printStackTrace();
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

}
