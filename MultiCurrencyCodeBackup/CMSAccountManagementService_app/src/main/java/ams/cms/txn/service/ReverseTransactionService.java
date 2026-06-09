package ams.cms.txn.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.model.AgencyBankingRequest;
import ams.cms.api.model.AgencyBankingResponse;
import ams.cms.config.AppInfo;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.handler.AccountReverseHandler;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountTranMaster;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.TxnReqRespLogMasterService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;
import ams.cms.utility.Utils;

@Service
public class ReverseTransactionService 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(ReverseTransactionService.class);
	
	@Autowired
	private EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	private	AppInfo appInfo;
	
	@Autowired
	private TxnReqRespLogMasterService txnReqRespLogMasterService; 
	
	@Autowired
	private AccountTranMasterService accountTranMasterService;	
	
	@Autowired
	private AccountReverseHandler accountReverseHandler;
	
	@Async("multiThreadBean")
	public CompletableFuture<PayloadReqRes> reverseTxnofEncryptedRequest(HttpServletRequest request, PayloadReqRes req)
	{
	//	String endpoint = "/NGN/txn/confirmBillPayment";
		ProcessResponse processResponse = new ProcessResponse();
		String txnId = null;
		String montraTxnId = null;
		String participantId = null;
		Date requestDate = null;
		try 
		{
			requestDate = Utils.getCurrentDate();
			ProcessResponse	processResps = apiSecretKeyUtility.validateRequestAPI(request, req);			
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
		//		txnReqRespLogMasterService.addTxnRequestResponseLogData(req, appInfo, processResps, requestDate, Utils.getCurrentDate(), participantId, txnId, montraTxnId, endpoint, "Failed");
				return CompletableFuture.completedFuture(encryptDecryptData.encryptPayloadReqRes(req, processResps));
			}
			
			String decrypt = appInfo.getDecryptedPayload();			
			AgencyBankingRequest agentTxnRequest = new ObjectMapper().readValue(decrypt, AgencyBankingRequest.class);
			
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");
			participantId = appInfo.getStrParticipantId();
			
			montraTxnId = agentTxnRequest.getMontraTxnId().trim();
			txnId = agentTxnRequest.getAmsTransactionId();
			 
			AccountTranMaster accountTranMasterInst = new AccountTranMaster();
			accountTranMasterInst.setStrTxn_id(txnId);
			accountTranMasterInst.setStrSrcTxnId(montraTxnId);				
			
			List<AgencyBankingResponse> agencyBankingResponseList = accountTranMasterService.getAccountTranMasterWithAccountInfo(accountTranMasterInst);			
			if (agencyBankingResponseList != null && agencyBankingResponseList.size() > 0) 
			{
				AgencyBankingResponse agencyBankingResponse = agencyBankingResponseList.get(0);	
				amsLogger.writeInfoLog("agencyBankingResponse:::"+agencyBankingResponse);
				
				if (agencyBankingResponse.getTranStatus() != null && "reversed".equalsIgnoreCase(agencyBankingResponse.getTranStatus())) 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Transaction Already Reversed!");
				}
				
				if ( !(agencyBankingResponse != null && agencyBankingResponse.getAccountNo() != null && agencyBankingResponse.getAccountNo().trim().length() > 0)) 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Invalid Transaction Id!");									
				}				
				
				if ("S0000".equalsIgnoreCase(processResponse.getCode()) && agencyBankingResponse != null)
				{
					agencyBankingResponse.setParticipantId(participantId);
					if ("Y".equalsIgnoreCase(agentTxnRequest.getConfirmTxn())) 
					{
						processResponse.setCode("S0000");
						processResponse.setStatus("Success");
						processResponse.setMessage("Bill Payment Transaction Confirmed");
					}
					else 
					{
						agentTxnRequest.setParticipantId(participantId);
						processResponse = accountReverseHandler.processToReversedTxnFromTxn(processResponse, agencyBankingResponseList, agentTxnRequest);
					}
				}
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("No Transaction Records found!");
			}
		}
		catch (Exception e) {
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error!");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		
//		txnReqRespLogMasterService.addTxnRequestResponseLogData(req, appInfo, processResponse, requestDate, Utils.getCurrentDate(), participantId, txnId, montraTxnId, endpoint, processResponse.getStatus());
		return CompletableFuture.completedFuture(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
}
