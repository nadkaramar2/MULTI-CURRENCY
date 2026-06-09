package ams.cms.txn.service;

import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.response.TransactionSummaryDetails;
import ams.cms.api.response.TxnStatementDetailsMaster;
import ams.cms.config.AppInfo;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountStatement;
import ams.cms.model.MontraAccountMaster;
import ams.cms.services.AccountStatementService;
import ams.cms.services.MontraAccountMasterService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;

@Service
public class TransactionStatementService 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(TransactionStatementService.class);
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	private AccountStatementService accountStatementService;
	
	@Autowired
	private EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	private MontraAccountMasterService montraAccountMasterService;
	
	@Autowired
	private	AppInfo appInfo;
	
	//Created By Sunil Y , For SummrayStatement and TxnStatement , Started
	
	@Async("multiThreadBean")
	public CompletableFuture<PayloadReqRes> processToViewTransactionStatementSummaray(HttpServletRequest request, @RequestBody PayloadReqRes req)	
	{
		TransactionSummaryDetails summrayStatement = new TransactionSummaryDetails();
		try 
		{
			ProcessResponse	processResp = apiSecretKeyUtility.validateRequestAPI(request, req);			
			if ("E0000".equalsIgnoreCase(processResp.getCode())) 
			{
				ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResp));	
			}
			String decrypt = appInfo.getDecryptedPayload();
			amsLogger.writeInfoLog("Inside processToViewTransactionStatement decrypt::"+decrypt);
			
			AccountStatement accountStatement = new ObjectMapper().readValue(decrypt, AccountStatement.class);
			
			accountStatement.setFromDate(accountStatement.getStrFromDate());
			accountStatement.setToDate(accountStatement.getStrToDate());
			
			MontraAccountMaster montraAccountMaster = new MontraAccountMaster();
			montraAccountMaster.setCid(accountStatement.getCid());
			
			ProcessResponse validationResponse = montraAccountMasterService.validateMontraId(montraAccountMaster);
			if (validationResponse != null && validationResponse.getCode().equalsIgnoreCase("S0000")) 
			{
				 summrayStatement = accountStatementService.getAllTransactionSummaryDetails(accountStatement);
			}
			else 
			{
				summrayStatement.setCode("E0000");
				summrayStatement.setSuccess(false);
				summrayStatement.setMessage("Cid is UnAvailable");
			}			
		}
		catch (Exception e) 
		{
			summrayStatement.setCode("E0000");
			summrayStatement.setSuccess(false);
			summrayStatement.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return CompletableFuture.completedFuture(encryptDecryptData.encryptPayloadReqRes(req, summrayStatement));
	}
	
	public CompletableFuture<PayloadReqRes> processToViewTransactionStatementDetails(HttpServletRequest request, @RequestBody PayloadReqRes req)	
	{
		TxnStatementDetailsMaster summrayTxnStatement = new TxnStatementDetailsMaster();
		try 
		{
			ProcessResponse	processResp = apiSecretKeyUtility.validateRequestAPI(request, req);			
			if ("E0000".equalsIgnoreCase(processResp.getCode())) 
			{
				ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResp));
			}
			String decrypt = appInfo.getDecryptedPayload();
			amsLogger.writeInfoLog("Inside processToViewTransactionStatement decrypt::"+decrypt);
			
			AccountStatement accountStatement = new ObjectMapper().readValue(decrypt, AccountStatement.class);
			if(accountStatement != null && accountStatement.getTransactionID() != null) 
			{
				MontraAccountMaster montraAccountMaster = new MontraAccountMaster();
				montraAccountMaster.setCid(accountStatement.getCid());
			
				ProcessResponse validationResponse = montraAccountMasterService.validateMontraId(montraAccountMaster);
				if (validationResponse != null && validationResponse.getCode().equalsIgnoreCase("S0000")) 
				{
					 summrayTxnStatement = accountStatementService.viewAccountStatementDetails(accountStatement);
				}
				else 
				{
					summrayTxnStatement.setCode("E0000"); 
					summrayTxnStatement.setSuccess(false);
					summrayTxnStatement.setMessage("Cid is UnAvailable");
				}
			}
			else 
			{
				summrayTxnStatement.setCode("E0000"); 
				summrayTxnStatement.setSuccess(false);
				summrayTxnStatement.setMessage("Transaction ID Not Found");
			}
		}
		catch (Exception e) 
		{
			summrayTxnStatement.setCode("E0000"); 
			summrayTxnStatement.setSuccess(false);
			summrayTxnStatement.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return CompletableFuture.completedFuture(encryptDecryptData.encryptPayloadReqRes(req, summrayTxnStatement));
	}
	//Created By Sunil Y , For SummrayStatement and TxnStatement , End
}
