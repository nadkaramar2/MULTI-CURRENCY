package ams.cms.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.model.AccountStatementResponse;
import ams.cms.config.AppInfo;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.jwt.JwtUtil;
import ams.cms.model.AccountStatement;
import ams.cms.model.LoadMoneyRequest;
import ams.cms.services.AccountStatementService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;

@RestController
@RequestMapping("/account-statement")
public class AccountStatementController
{
	@Autowired
	AccountStatementService accountStatementService;
	
	@Autowired 
	JwtUtil jwtUtil;
	
	@Autowired
	EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	private	AppInfo appInfo;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	

	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addAccountStatementData(@RequestBody AccountStatement accountStatement) 
	{
		String result = null;
		try 
		{
			accountStatement = accountStatementService.addAccountTransactionData(accountStatement);
			return ResponseEntity.ok(accountStatement);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getAccountStatementList", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountStatementListData(@RequestBody AccountStatement accountStatement) 
	{
		String result = null;
		try 
		{
			List<AccountStatement> accountStatementList = accountStatementService.getAccountStatementBasedonParameter(accountStatement);
			return ResponseEntity.ok(accountStatementList);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getAccountStatements", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountStatements(@RequestBody AccountStatement accountStatement) 
	{
		String result = null;
		try 
		{
			List<AccountStatement> accountStatementList = accountStatementService.getAccountStatements(accountStatement);
			return ResponseEntity.ok(accountStatementList);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	@RequestMapping(value = "/getAccountStatementsDateWise", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountStatementsDateWise(@RequestBody AccountStatement accountStatement) 
	{
		String result = null;
		try 
		{
			List<AccountStatement> accountStatementList = accountStatementService.getAccountStatementByDate(accountStatement);
			return ResponseEntity.ok(accountStatementList);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	//getAccountStatements

		@RequestMapping(value = "/getlastFiveWalletAccountStatements", method = RequestMethod.POST)
		public ResponseEntity<?> getlastFiveWalletAccountStatements(HttpServletRequest request,@RequestBody PayloadReqRes req) 
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
				AccountStatement accountStatement = new ObjectMapper().readValue(decrypt, AccountStatement.class);
				List<AccountStatementResponse> accountStatementList = accountStatementService.getlastFiveWalletAccountStatements(accountStatement);
				if(accountStatementList.size()>0) {
					processResponse.setCode("S0000");
					processResponse.setStatus("Scussed");
					processResponse.setMessage("Data Fetch Scussefully");
					processResponse.setAccountStatementResponses(accountStatementList);
				}else {
					
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("No Data Found");
				}
				
				
			
			} 
			catch (Exception e) 
			{
				//amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
				
			}
			return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		}
		@RequestMapping(value = "/getWalletAccountStatementsDateWise", method = RequestMethod.POST)
		public ResponseEntity<?> getWalletAccountStatementsDateWise(HttpServletRequest request,@RequestBody PayloadReqRes req) 
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
				AccountStatement accountStatement = new ObjectMapper().readValue(decrypt, AccountStatement.class);
				List<AccountStatementResponse> accountStatementList = accountStatementService.getWalletAccountStatementsDateWise(accountStatement);
				if(accountStatementList.size()>0) {
					processResponse.setCode("S0000");
					processResponse.setStatus("Scussed");
					processResponse.setMessage("Data Fetch Scussefully");
					processResponse.setAccountStatementResponses(accountStatementList);
				}else {
					
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("No Data Found");
				}
				
				
			
			} 
			catch (Exception e) 
			{
				//amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
				
			}
			return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		}
	

}
