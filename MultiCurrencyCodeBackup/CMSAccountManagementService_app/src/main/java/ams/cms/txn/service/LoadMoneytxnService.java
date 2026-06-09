package ams.cms.txn.service;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.handler.AccountTxnHandler;
import ams.cms.api.handler.ExternalTxnRequestValidator;
import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.ExternalTxnRequest;
import ams.cms.api.model.TierAccountResponse;
import ams.cms.config.AppInfo;
import ams.cms.config.CommonConstants;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.LoadMoney;
import ams.cms.model.MontraAccountMaster;
import ams.cms.model.TransactionTypeModel;
import ams.cms.services.AccountMasterService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.services.TransactionIdService;
import ams.cms.services.TransactionTypeService;
import ams.cms.services.TxnReqRespLogMasterService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.AccountUtility;
import ams.cms.utility.PayloadReqRes;
import ams.cms.utility.Utils;

@Service
public class LoadMoneytxnService 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(LoadMoneytxnService.class);
	
	@Autowired
	private EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	private AccountMasterService accountMasterService;
	
	@Autowired
	private AccountTxnHandler accountTxnHandler;
	
	@Autowired
	private TransactionTypeService transactionTypeService;
	
	@Autowired
	private GLAccountTypeMasterService glAccountTypeMasterService;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	private	AppInfo appInfo;
	
	@Autowired
	private TxnReqRespLogMasterService txnReqRespLogMasterService; 
	
	@Autowired
	private TransactionIdService transactionIdService;
	
	@Autowired
	private ExternalTxnRequestValidator externalTxnRequestValidator;
	
	@Async("multiThreadBean")
	public CompletableFuture<PayloadReqRes> loadMoneyofEncryptedRequest(HttpServletRequest request, PayloadReqRes req)
	{
	//	String endpoint = "/NGN/txn/loadMoneytxn";
		ProcessResponse processResponse = new ProcessResponse();
		String txnId = null;
		String montraTxnId = null;
		String participantId = null;
		Date requestDate = null;
		try 
		{
			requestDate = Utils.getCurrentDate();
			txnId = transactionIdService.getNewTransactionId();
			
			ProcessResponse	processResps = apiSecretKeyUtility.validateRequestAPI(request, req);			
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
		//		txnReqRespLogMasterService.addTxnRequestResponseLogData(req, appInfo, processResps, requestDate, Utils.getCurrentDate(), participantId, txnId, montraTxnId, endpoint, "Failed");
				return CompletableFuture.completedFuture(encryptDecryptData.encryptPayloadReqRes(req, processResps));
			}
			
			String decrypt = appInfo.getDecryptedPayload();			
			LoadMoney loadMoney = new ObjectMapper().readValue(decrypt, LoadMoney.class);
			
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");
			
			participantId = appInfo.getStrParticipantId();
			 
			processResponse = accountTxnHandler.validateEntityInfoAndNumber(decrypt, processResponse);//Added By Sunil Y , Validation For EntityInfo And EntityNo			
			if ("S0000".equalsIgnoreCase(processResponse.getCode()) && "NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				montraTxnId = loadMoney.getMontraTxnId().trim();
				
				processResponse.setCustId(loadMoney.getStrCustId());
				processResponse.setCid(loadMoney.getCid());
				processResponse.setMontraTxnId(montraTxnId);		
				
				MontraAccountMaster montraAccountMaster = new MontraAccountMaster();
				montraAccountMaster.setCid(loadMoney.getCid());
				montraAccountMaster.setStrCustId(loadMoney.getStrCustId());
				
				ExternalTxnRequest externalTxnRequest = new ExternalTxnRequest();
				externalTxnRequest.setCustId(loadMoney.getStrCustId());
				externalTxnRequest.setCid(loadMoney.getCid());
				externalTxnRequest.setBid(loadMoney.getBid());
				externalTxnRequest.setMontraTxnId(montraTxnId);
				
				processResponse = externalTxnRequestValidator.validateMontraTxnRequest(processResponse, externalTxnRequest);
			}
			
			if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
			{ 
				TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
				transactionTypeModel.setStrTxnTypeKeyWord(loadMoney.getTxnType());
				
				transactionTypeModel = transactionTypeService.getGlAccountNoByTxnKeyword(transactionTypeModel);				
				if(transactionTypeModel != null && transactionTypeModel.getStrGLAccountType() != null && transactionTypeModel.getStrGLAccountType() != null) 
				{
					GLAccountTypeMaster glAccountviewModel = new GLAccountTypeMaster();
					glAccountviewModel.setStrGLAccountType(transactionTypeModel.getStrGLAccountType());
					
					GLAccountTypeMaster glAccountTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(glAccountviewModel);
					
					if(glAccountTypeMaster != null && glAccountTypeMaster.getStrAccountNumber() != null) 
					{
						AccountCreation recipientAccountInfoIns = new AccountCreation();
						recipientAccountInfoIns.setStrAccountNumber(loadMoney.getReceipentAccountNo());
						
						AccountResponse recipientAccountInfo = accountMasterService.getAccountMasterInformation(recipientAccountInfoIns);
						if (recipientAccountInfo != null && recipientAccountInfo.getStrCustId() != null) 
						{
							if ("Active".equalsIgnoreCase(recipientAccountInfo.getStrStatus())) 
							{
								String fee = (loadMoney.getFee()!=null) ? loadMoney.getFee().trim(): "0";
								String vat = (loadMoney.getVat()!=null) ? loadMoney.getVat().trim(): "0";
								
								TransactionPostingConfig transactionPostingConfig = new TransactionPostingConfig();												
								transactionPostingConfig.setParticipantId(participantId);
								transactionPostingConfig.setTxnId(txnId);
								
								transactionPostingConfig.setStrSrcTxnId(loadMoney.getMontraTxnId());
								transactionPostingConfig.setTxnType(loadMoney.getTxnType());
								
								transactionPostingConfig.setTxnAmount(loadMoney.getTxnAmt());
								transactionPostingConfig.setFeeType(loadMoney.getFeeType());
								transactionPostingConfig.setFeeApplicableTo(loadMoney.getFeeApplicableTo());
								
								transactionPostingConfig.setGlAccountTypeMaster(glAccountTypeMaster);
								
								Double availableBalance = AccountUtility.getAccountUpdatedBalance(recipientAccountInfo) + Utils.stringToDouble(loadMoney.getTxnAmt());								
								recipientAccountInfo.setAvailableBalance(Utils.decimalFormat.format(availableBalance));
								
								//Added By Sunil Y , setting Entity info and no in recipient [start] 07-09-2023
								recipientAccountInfo.setEntityInfo(loadMoney.getEntityInfo());
								recipientAccountInfo.setEntityNumber(loadMoney.getEntityNumber());
								//Added By Sunil Y , setting Entity info and no in recipient [end] 07-09-2023
								
								transactionPostingConfig.setToAccount(recipientAccountInfo);
								
								TierAccountResponse toTierAccountMaster = AccountUtility.getTierAccountResponse(recipientAccountInfo);
								transactionPostingConfig.setToTierAccountMaster(toTierAccountMaster);
								
								GLAccountTypeMaster toLinkedGLAccountType = AccountUtility.getGLAccountTypeMaster(recipientAccountInfo);
								transactionPostingConfig.setToLinkGLAccount(toLinkedGLAccountType);	
								
								HashMap<String, String> linkedGLMap = new HashMap<String, String>();
								linkedGLMap.put(toLinkedGLAccountType.getStrAccountNumber().trim(), toLinkedGLAccountType.getStrClosingBalance().trim());
								
								transactionPostingConfig.setLinkedGLMap(linkedGLMap);
								
								processResponse = accountTxnHandler.processLoadMoneyTransaction(processResponse, transactionPostingConfig, fee, vat);
								if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
								{
									txnId = transactionPostingConfig.getTxnId().trim();
									
									processResponse.setMessage("Transaction Successful Amount NGN "+loadMoney.getTxnAmt()+" to "+recipientAccountInfo.getStrAccountHolderName()+" "+recipientAccountInfo.getStrAccountNumber()+ ".");
									if (loadMoney.getMontraTxnId() != null && loadMoney.getMontraTxnId().trim().length() > 0) 
									{
										processResponse.setCid(loadMoney.getCid());
										processResponse.setCustId(loadMoney.getStrCustId());
										processResponse.setMontraTxnId(loadMoney.getMontraTxnId().trim());
										
										processResponse.setAmsTransactionId(txnId);
									}
									else
									{
										processResponse.setTransactionId(txnId);
										processResponse.setTransactionDate(ams.cms.utility.Utils.simpleDateTimeFormat.format(transactionPostingConfig.getTxnDate()));
									}
									processResponse.setCurrentAvailableBalance(transactionPostingConfig.getToAccount().getAvailableBalance());
								}
							}
							else
							{
								processResponse.setCode("E0000");
								processResponse.setStatus("Failed");
								processResponse.setMessage("Recipent Account Not Active");
							}
						}
						else 
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Recipent Account Not Found");
						}
					
					}
					else 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Suspense GL Not Found.");
					}
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Suspense GL Not Configured.");
				}
			}			
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		
	//	txnReqRespLogMasterService.addTxnRequestResponseLogData(req, appInfo, processResponse, requestDate, Utils.getCurrentDate(), participantId, txnId, montraTxnId, endpoint, processResponse.getStatus());
		return CompletableFuture.completedFuture(encryptDecryptData.encryptPayloadReqRes(req, processResponse));	
	}
}
