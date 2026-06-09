package ams.cms.txn.service;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.handler.AccountTxnHandler;
import ams.cms.api.handler.ExternalTxnRequestValidator;
import ams.cms.api.handler.UserTransactionHandler;
import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.ExternalTxnRequest;
import ams.cms.api.model.TierAccountResponse;
import ams.cms.config.AppInfo;
import ams.cms.config.CommonConstants;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.config.TransactionConfig;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.LoadMoney;
import ams.cms.model.MontraAccountMaster;
import ams.cms.model.TransactionTypeModel;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.services.MontraAccountMasterService;
import ams.cms.services.TransactionIdService;
import ams.cms.services.TransactionTypeService;
import ams.cms.services.TxnReqRespLogMasterService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.AccountUtility;
import ams.cms.utility.PayloadReqRes;
import ams.cms.utility.Utils;

@Service
public class BillTransactionService 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(BillTransactionService.class);
	
	@Autowired
	private MontraAccountMasterService montraAccountMasterService;
	
	@Autowired
	private AccountTranMasterService accountTranMasterService;
	
	@Autowired
	private UserTransactionHandler userTransactionHandler;
	
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
	private EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	private ExternalTxnRequestValidator externalTxnRequestValidator;
	
	@Async("multiThreadBean")
	public CompletableFuture<ProcessResponse> billPayment(HttpServletRequest request, LoadMoney loadMoney)
	{
		ProcessResponse processResponse = new ProcessResponse();
		String txnId = null;
		String montraTxnId = null;
		String participantId = null;
		Date requestDate = null;
		Date responseDate = null;
		try 
		{
			//ProcessResponse processResps = apiSecretKeyUtility.validateRequestAPI(request, req);	
			
			ProcessResponse	processResps = apiSecretKeyUtility.validateApiKey(request, processResponse);
			txnId = transactionIdService.getNewTransactionId();
			
			requestDate = Utils.getCurrentDate();
			
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				responseDate = Utils.getCurrentDate();
				//txnReqRespLogMasterService.addTxnReqRespLogData(loadMoney, appInfo, processResps, requestDate, responseDate, participantId, txnId, montraTxnId, "/NGN/txn/billPayment", "Failed");
				
				//return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResps)); 
				return CompletableFuture.completedFuture(processResponse);
			}
			 
			String decrypt = appInfo.getDecryptedPayload();			
			amsLogger.writeInfoLog("Inside billPayment decrypt::"+decrypt);
			
			//LoadMoney loadMoney = new ObjectMapper().readValue(decrypt, LoadMoney.class);
			participantId = appInfo.getStrParticipantId();			
				 
			amsLogger.writeInfoLog("Inside performTxn accountTranMaster::"+loadMoney);
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");
			
			//Added By Sunil Y , Validation For EntityInfo And EntityNo [start]
			//processResponse = accountTxnHandler.validateEntityInfoAndNumber(decrypt, processResponse);
			//Added By Sunil Y , Validation For EntityInfo And EntityNo [end]
			
			if ("S0000".equalsIgnoreCase(processResponse.getCode()) && "NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				montraTxnId = loadMoney.getMontraTxnId().trim();
				
				processResponse.setCustId(loadMoney.getStrCustId());
				processResponse.setCid(loadMoney.getCid());
				processResponse.setMontraTxnId(montraTxnId);		
				
				MontraAccountMaster montraAccountMaster = new MontraAccountMaster();
				montraAccountMaster.setCid(loadMoney.getCid());
				montraAccountMaster.setStrCustId(loadMoney.getStrCustId());
				
				ProcessResponse processresp = montraAccountMasterService.validateMontraIdAndCustId(montraAccountMaster);
				if ("S0000".equalsIgnoreCase(processresp.getCode())) 
				{
					if (loadMoney.getMontraTxnId() != null && loadMoney.getMontraTxnId().trim().length() > 0) 
					{
						AccountTranMaster accountTranMasterInst = new AccountTranMaster(); 
						accountTranMasterInst.setStrSrcTxnId(loadMoney.getMontraTxnId().trim());
						
						AccountTranMaster accmTransMst = accountTranMasterService.getAccountTranMaster(accountTranMasterInst);	
						if (accmTransMst != null) 
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Duplicate Montra txn id");
						}
						if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
						{
							TransactionConfig transactionConfigIns = new TransactionConfig();
							TransactionConfig secretCodeTransConfig = userTransactionHandler.validateSecretCode(transactionConfigIns);
							if (secretCodeTransConfig != null && !"S0000".equalsIgnoreCase(secretCodeTransConfig.getCode())) 
							{
								processResponse.setCode("E0000");
								processResponse.setStatus("Failed");
								processResponse.setMessage(secretCodeTransConfig.getMessage());
							}
						}
					}
					else 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Montra txn id can not be blank");
					}					
				}
				else
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage(processresp.getMessage());
				}
			}			
			
			if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
			{ 
				TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
				transactionTypeModel.setStrTxnTypeKeyWord(loadMoney.getTxnType());
				
				transactionTypeModel = transactionTypeService.getGlAccountNoByTxnKeyword(transactionTypeModel);
				
				if(transactionTypeModel != null && transactionTypeModel.getStrGLAccountType() != null && transactionTypeModel.getStrGLAccountNumber() != null) 
				{
					GLAccountTypeMaster glAccountviewModel = new GLAccountTypeMaster();
					glAccountviewModel.setStrGLAccountType(transactionTypeModel.getStrGLAccountType());
					
					GLAccountTypeMaster glAccountTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(glAccountviewModel);
					
					if(glAccountTypeMaster != null && glAccountTypeMaster.getStrAccountNumber() != null) 
					{
						AccountCreation senderAccountInfoIns = new AccountCreation();
						senderAccountInfoIns.setStrAccountNumber(loadMoney.getSenderAccountNo());
						
						AccountResponse senderAccountInfo = accountMasterService.getAccountMasterInformation(senderAccountInfoIns);						
						if (senderAccountInfo != null && senderAccountInfo.getStrCustId() != null) 
						{
							if ("Active".equalsIgnoreCase(senderAccountInfo.getStrStatus())) 
							{
								String fee = (loadMoney.getFee()!=null) ? loadMoney.getFee().trim(): "0";
								String vat = (loadMoney.getVat()!=null) ? loadMoney.getVat().trim(): "0";
								
								TransactionPostingConfig transactionPostingConfig = new TransactionPostingConfig();
								transactionPostingConfig.setTxnId(txnId);//New Changes
								
								transactionPostingConfig.setParticipantId(participantId);
								transactionPostingConfig.setStrSrcTxnId(loadMoney.getMontraTxnId());
								transactionPostingConfig.setTxnType(loadMoney.getTxnType());
								
								transactionPostingConfig.setTxnAmount(loadMoney.getTxnAmt());
								transactionPostingConfig.setFeeType(loadMoney.getFeeType());
								transactionPostingConfig.setFeeApplicableTo(loadMoney.getFeeApplicableTo());
								
								transactionPostingConfig.setGlAccountTypeMaster(glAccountTypeMaster);
								
								//Added By Sunil Y , setting Entity info and number in senderAccount [start]
								senderAccountInfo.setEntityInfo(loadMoney.getEntityInfo());
								senderAccountInfo.setEntityNumber(loadMoney.getEntityNumber());
								//Added By Sunil Y , setting Entity info and number in senderAccount [End]
								
								senderAccountInfo.setAvailableBalance(AccountUtility.getUpdatedAvailableBalance(senderAccountInfo.getStrClosingBalance(), loadMoney.getTxnAmt(), fee, vat));
								transactionPostingConfig.setFromAccount(senderAccountInfo);
								
								TierAccountResponse fromTierAccountMaster = AccountUtility.getTierAccountResponse(senderAccountInfo);
								transactionPostingConfig.setFromTierAccountMaster(fromTierAccountMaster);
								
								GLAccountTypeMaster fromLinkedGLAccountType = AccountUtility.getGLAccountTypeMaster(senderAccountInfo);
								transactionPostingConfig.setFromLinkGLAccount(fromLinkedGLAccountType);
								
								HashMap<String, String> linkedGLMap = new HashMap<String, String>();
								linkedGLMap.put(fromLinkedGLAccountType.getStrAccountNumber().trim(), fromLinkedGLAccountType.getStrClosingBalance().trim());
								
								transactionPostingConfig.setLinkedGLMap(linkedGLMap);
								
								processResponse = accountTxnHandler.processBillPayTransaction(processResponse, transactionPostingConfig, fee, vat);
								if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
								{
									processResponse.setMessage("Transaction Successful Amount NGN "+loadMoney.getTxnAmt()+" debited from "+senderAccountInfo.getStrAccountHolderName()+" "+senderAccountInfo.getStrAccountNumber()+ ".");
								
									txnId = transactionPostingConfig.getTxnId().trim();
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
									processResponse.setCurrentAvailableBalance(transactionPostingConfig.getFromAccount().getAvailableBalance());
								}
							}
							else
							{
								processResponse.setCode("E0000");
								processResponse.setStatus("Failed");
								processResponse.setMessage("Sender Account Not Active");
							}
						}
						else 
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Sender Account Not Found");
						}
					
					}
					else 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("GL Not Found.");
					}
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("GL Not Configured.");
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
		
		responseDate = Utils.getCurrentDate();
	//	txnReqRespLogMasterService.addTxnReqRespLogData(loadMoney, appInfo, processResponse, requestDate, responseDate, participantId, txnId, montraTxnId, "/NGN/txn/billPayment", processResponse.getStatus());
		
		//return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		return CompletableFuture.completedFuture(processResponse);
	}
	
	@Async("multiThreadBean")
	public CompletableFuture<PayloadReqRes> billPaymentofEncryptedRequest(HttpServletRequest request, PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		String txnId = null;
		String montraTxnId = null;
		String participantId = null;
		Date requestDate = null;
		try 
		{
			requestDate = Utils.getCurrentDate();
			txnId = transactionIdService.getNewTransactionId();
			
			ProcessResponse processResps = apiSecretKeyUtility.validateRequestAPI(request, req);			
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
		//		txnReqRespLogMasterService.addTxnReqRespLogData(req, appInfo, processResps, requestDate, Utils.getCurrentDate(), participantId, txnId, montraTxnId, "/NGN/txn/billPayment", "Failed");
				return CompletableFuture.completedFuture(encryptDecryptData.encryptPayloadReqRes(req, processResps));
			}
			 
			String decrypt = appInfo.getDecryptedPayload();			
			amsLogger.writeInfoLog("Inside billPayment decrypt::"+decrypt);
			
			LoadMoney loadMoney = new ObjectMapper().readValue(decrypt, LoadMoney.class);
			participantId = appInfo.getStrParticipantId();			
				 
			amsLogger.writeInfoLog("Inside performTxn accountTranMaster::"+loadMoney);
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");
			
			processResponse = accountTxnHandler.validateEntityInfoAndNumber(decrypt, processResponse);//Added By Sunil Y , Validation For EntityInfo And EntityNo			
			if ("S0000".equalsIgnoreCase(processResponse.getCode()) && "NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				montraTxnId = loadMoney.getMontraTxnId().trim();
				
				processResponse.setCustId(loadMoney.getStrCustId());
				processResponse.setCid(loadMoney.getCid());
				processResponse.setMontraTxnId(montraTxnId);		
				
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
				if(transactionTypeModel != null && transactionTypeModel.getStrGLAccountType() != null && transactionTypeModel.getStrGLAccountNumber() != null) 
				{
					GLAccountTypeMaster glAccountviewModel = new GLAccountTypeMaster();
					glAccountviewModel.setStrGLAccountType(transactionTypeModel.getStrGLAccountType());
					
					GLAccountTypeMaster glAccountTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(glAccountviewModel);
					
					if(glAccountTypeMaster != null && glAccountTypeMaster.getStrAccountNumber() != null) 
					{
						AccountCreation senderAccountInfoIns = new AccountCreation();
						senderAccountInfoIns.setStrAccountNumber(loadMoney.getSenderAccountNo());
						
						AccountResponse senderAccountInfo = accountMasterService.getAccountMasterInformation(senderAccountInfoIns);						
						if (senderAccountInfo != null && senderAccountInfo.getStrCustId() != null) 
						{
							if ("Active".equalsIgnoreCase(senderAccountInfo.getStrStatus())) 
							{
								String fee = (loadMoney.getFee()!=null) ? loadMoney.getFee().trim(): "0";
								String vat = (loadMoney.getVat()!=null) ? loadMoney.getVat().trim(): "0";
								
								TransactionPostingConfig transactionPostingConfig = new TransactionPostingConfig();
								transactionPostingConfig.setTxnId(txnId);//New Changes
								
								transactionPostingConfig.setParticipantId(participantId);
								transactionPostingConfig.setStrSrcTxnId(loadMoney.getMontraTxnId());
								transactionPostingConfig.setTxnType(loadMoney.getTxnType());
								
								transactionPostingConfig.setTxnAmount(loadMoney.getTxnAmt());
								transactionPostingConfig.setFeeType(loadMoney.getFeeType());
								transactionPostingConfig.setFeeApplicableTo(loadMoney.getFeeApplicableTo());
								
								transactionPostingConfig.setGlAccountTypeMaster(glAccountTypeMaster);
								
								//Added By Sunil Y , setting Entity info and number in senderAccount [start]
								senderAccountInfo.setEntityInfo(loadMoney.getEntityInfo());
								senderAccountInfo.setEntityNumber(loadMoney.getEntityNumber());
								//Added By Sunil Y , setting Entity info and number in senderAccount [End]
								
								senderAccountInfo.setAvailableBalance(AccountUtility.getUpdatedAvailableBalance(senderAccountInfo.getStrClosingBalance(), loadMoney.getTxnAmt(), null, null));//Fee and vat reduce after validation
								transactionPostingConfig.setFromAccount(senderAccountInfo);
								
								TierAccountResponse fromTierAccountMaster = AccountUtility.getTierAccountResponse(senderAccountInfo);
								transactionPostingConfig.setFromTierAccountMaster(fromTierAccountMaster);
								
								GLAccountTypeMaster fromLinkedGLAccountType = AccountUtility.getGLAccountTypeMaster(senderAccountInfo);
								transactionPostingConfig.setFromLinkGLAccount(fromLinkedGLAccountType);
								
								HashMap<String, String> linkedGLMap = new HashMap<String, String>();
								linkedGLMap.put(fromLinkedGLAccountType.getStrAccountNumber().trim(), fromLinkedGLAccountType.getStrClosingBalance().trim());
								
								transactionPostingConfig.setLinkedGLMap(linkedGLMap);
								
								processResponse = accountTxnHandler.processBillPayTransaction(processResponse, transactionPostingConfig, fee, vat);
								if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
								{
									processResponse.setMessage("Transaction Successful Amount NGN "+loadMoney.getTxnAmt()+" debited from "+senderAccountInfo.getStrAccountHolderName()+" "+senderAccountInfo.getStrAccountNumber()+ ".");
								
									txnId = transactionPostingConfig.getTxnId().trim();
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
									processResponse.setCurrentAvailableBalance(transactionPostingConfig.getFromAccount().getAvailableBalance());
								}
							}
							else
							{
								processResponse.setCode("E0000");
								processResponse.setStatus("Failed");
								processResponse.setMessage("Sender Account Not Active");
							}
						}
						else 
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Sender Account Not Found");
						}
					
					}
					else 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("GL Not Found.");
					}
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("GL Not Configured.");
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
		
		//txnReqRespLogMasterService.addTxnReqRespLogData(req, appInfo, processResponse, requestDate, Utils.getCurrentDate(), participantId, txnId, montraTxnId, "/NGN/txn/billPayment", processResponse.getStatus());
		return CompletableFuture.completedFuture(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
}
