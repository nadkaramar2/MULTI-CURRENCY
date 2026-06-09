package ams.cms.txn.service;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.handler.AccountTxnHandler;
import ams.cms.api.handler.ExternalTxnRequestValidator;
import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.DynamicQrGeneration;
import ams.cms.api.model.ExternalTxnRequest;
import ams.cms.api.model.TierAccountResponse;
import ams.cms.api.service.QrCodeService;
import ams.cms.api.utitlity.Utils;
import ams.cms.config.AppInfo;
import ams.cms.config.CommonConstants;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.services.AccountMasterService;
import ams.cms.services.TransactionIdService;
import ams.cms.services.TxnReqRespLogMasterService;
import ams.cms.txn.handler.AccountValidator;
import ams.cms.util.AccountStatus;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.AccountUtility;
import ams.cms.utility.PayloadReqRes;

@Service
public class InternalTransactionService 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(InternalTransactionService.class);
	
	@Autowired
	private AccountMasterService accountMasterService;
	
	@Autowired
	private QrCodeService qrCodeService;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	private	AppInfo appInfo;
	
	@Autowired
	private AccountTxnHandler accountTxnHandler; 
	
	@Autowired
	private ExternalTxnRequestValidator externalTxnRequestValidator;
	
	@Autowired
	private AccountValidator accountValidator;
	
	@Autowired
	private TxnReqRespLogMasterService txnReqRespLogMasterService;
	
	@Autowired
	private TransactionIdService transactionIdService;
	
	@Autowired
	private EncryptDecryptConfig encryptDecryptData;
	
	@Async("multiThreadBean")
	public CompletableFuture<ProcessResponse> prōcessP2PInternalTransaction(HttpServletRequest request, AccountTranMaster accountTranMaster) throws InterruptedException, ExecutionException
	{
	//	String endpoint = "/NGN/txn/performTxn";
		
		ProcessResponse processResponse = new ProcessResponse();
		String mainTxnId = null;
		String montraTxnId = null;
		String participantId = null;
		Date requestDate = null;
		Date responseDate = null;
		try 
		{
			requestDate = Utils.getCurrentDate();
			mainTxnId = transactionIdService.getNewTransactionId();
			
			ProcessResponse	processResps = apiSecretKeyUtility.validateApiKey(request, processResponse);
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				responseDate = Utils.getCurrentDate();
			//	txnReqRespLogMasterService.addTxnReqRespLogData(accountTranMaster, appInfo, processResps, requestDate, responseDate, participantId, mainTxnId, montraTxnId, endpoint, "Failed");
				
				return CompletableFuture.completedFuture(processResponse);
			}
			
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");
			participantId = appInfo.getStrParticipantId();
			
			if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				montraTxnId = accountTranMaster.getMontraTxnId().trim();
				
				ExternalTxnRequest externalTxnRequest = new ExternalTxnRequest();
				externalTxnRequest.setCustId(accountTranMaster.getStrCustId());
				externalTxnRequest.setCid(accountTranMaster.getCid());
				externalTxnRequest.setBid(accountTranMaster.getBid());
				externalTxnRequest.setMontraTxnId(montraTxnId);
				externalTxnRequest.setSecretCode(accountTranMaster.getSecretCode());
				
				processResponse = externalTxnRequestValidator.validateMontraTxnRequest(processResponse, externalTxnRequest);
			}
			
			if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
			{
				if(!accountTranMaster.getSenderAccountNo().equalsIgnoreCase(accountTranMaster.getReceipentAccountNo())) 
				{
					if (!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
					{
						CustomerIdCreation customerIdCreation = new CustomerIdCreation();
						customerIdCreation.setStrCustId(accountTranMaster.getStrCustId());
						
						processResponse = accountValidator.validateCustomerPIN(processResponse, customerIdCreation);
					}
					if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
					{
						AccountCreation senderAccountInfo = new AccountCreation();						
						senderAccountInfo.setStrAccountNumber(accountTranMaster.getSenderAccountNo());
						senderAccountInfo.setStrCustId(accountTranMaster.getStrCustId());	
						
						AccountResponse senderAccountResponse =	accountMasterService.getAccountMasterInformation(senderAccountInfo);
						if (senderAccountResponse != null && senderAccountResponse.getStrCustId() != null) 
						{
							if ("Active".equalsIgnoreCase(senderAccountResponse.getStrStatus())) 
							{
								AccountCreation recipientAccountInfoIns = new AccountCreation();
								recipientAccountInfoIns.setStrAccountNumber(accountTranMaster.getReceipentAccountNo());
								
								AccountResponse recipientAccountInfo = accountMasterService.getAccountMasterInformation(recipientAccountInfoIns);
								if (recipientAccountInfo != null && recipientAccountInfo.getStrCustId() != null) 
								{
									if ("Active".equalsIgnoreCase(recipientAccountInfo.getStrStatus())) 
									{
										String fee = (accountTranMaster.getFee()!=null) ? accountTranMaster.getFee().trim(): "0";
										String vat = (accountTranMaster.getVat()!=null) ? accountTranMaster.getVat().trim(): "0";
										
										TransactionPostingConfig transactionPostingConfig = new TransactionPostingConfig();
										
										transactionPostingConfig.setTxnId(mainTxnId);//Added AMS txnId
										
										transactionPostingConfig.setParticipantId(participantId);
										transactionPostingConfig.setStrSrcTxnId(accountTranMaster.getMontraTxnId());
										transactionPostingConfig.setTxnType(accountTranMaster.getStrTran_type());
										
										transactionPostingConfig.setTxnAmount(accountTranMaster.getStrTransaction_amount());
										transactionPostingConfig.setFeeType(accountTranMaster.getFeeType());
										transactionPostingConfig.setFeeApplicableTo(accountTranMaster.getFeeApplicableTo());
										
										//Added By Sunil Y , Ams Will Populate EntityInfo And EntityRespon For Sender and Recipient [start]
										senderAccountResponse.setEntityInfo(recipientAccountInfo.getStrAccountHolderName());
										senderAccountResponse.setEntityNumber(recipientAccountInfo.getStrAccountNumber());
										
										recipientAccountInfo.setEntityInfo(senderAccountResponse.getStrAccountHolderName());
										recipientAccountInfo.setEntityNumber(senderAccountResponse.getStrAccountNumber());										
										//Added By Sunil Y , Ams Will Populate EntityInfo And EntityRespon For Sender and Recipient [End]
										
										senderAccountResponse.setAvailableBalance(AccountUtility.getUpdatedAvailableBalance(senderAccountResponse.getStrClosingBalance(), accountTranMaster.getStrTransaction_amount(), fee, vat));
										transactionPostingConfig.setFromAccount(senderAccountResponse);	
										
										transactionPostingConfig.setToAccount(recipientAccountInfo);
										
										TierAccountResponse fromTierAccountMaster = AccountUtility.getTierAccountResponse(senderAccountResponse);
										transactionPostingConfig.setFromTierAccountMaster(fromTierAccountMaster);
										
										TierAccountResponse toTierAccountMaster = AccountUtility.getTierAccountResponse(recipientAccountInfo);
										transactionPostingConfig.setToTierAccountMaster(toTierAccountMaster);
										
										GLAccountTypeMaster fromLinkedGLAccountType = AccountUtility.getGLAccountTypeMaster(senderAccountResponse);
										transactionPostingConfig.setFromLinkGLAccount(fromLinkedGLAccountType);
										
										GLAccountTypeMaster toLinkedGLAccountType = AccountUtility.getGLAccountTypeMaster(recipientAccountInfo);
										transactionPostingConfig.setToLinkGLAccount(toLinkedGLAccountType);	
										
										HashMap<String, String> linkedGLMap = new HashMap<String, String>();
										linkedGLMap.put(fromLinkedGLAccountType.getStrAccountNumber().trim(), fromLinkedGLAccountType.getStrClosingBalance().trim());
										linkedGLMap.put(toLinkedGLAccountType.getStrAccountNumber().trim(), toLinkedGLAccountType.getStrClosingBalance().trim());
										
										transactionPostingConfig.setLinkedGLMap(linkedGLMap);
										
										processResponse = accountTxnHandler.processClosedLoopTransaction(processResponse, transactionPostingConfig, fee, vat);
										if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
										{
											//txnId = transactionPostingConfig.getTxnId().trim();	
											processResponse.setMessage("Transaction Successful.");
											if(accountTranMaster != null && accountTranMaster.getDynamicQrRefNo() != null && accountTranMaster.getDynamicQrRefNo().trim().length() > 0 ) 
											{
												try 
												{
													DynamicQrGeneration dynamicQrGeneration = new DynamicQrGeneration();
													dynamicQrGeneration.setPaidByAccountNumber(transactionPostingConfig.getFromAccount().getStrAccountNumber());
													dynamicQrGeneration.setPaidByAccountType(transactionPostingConfig.getFromAccount().getStrAccountType());
													dynamicQrGeneration.setTranId(transactionPostingConfig.getTxnId());
													dynamicQrGeneration.setPaymentReceivedDate(transactionPostingConfig.getTxnDate());
													dynamicQrGeneration.setPaymentReceivedTime(transactionPostingConfig.getTxnTime());
													dynamicQrGeneration.setRefNo(accountTranMaster.getDynamicQrRefNo());
													
													dynamicQrGeneration.setResponseCode("00");
													int count =	qrCodeService.updateQrCodeFieldsAfterTxn(dynamicQrGeneration);
													amsLogger.writeInfoLog("Inside performTxn count::["+count+"]");
												}
												catch (Exception e) {
													amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
												}
											}
											if (accountTranMaster.getMontraTxnId() != null && accountTranMaster.getMontraTxnId().trim().length() > 0) 
											{
												processResponse.setCid(accountTranMaster.getCid());
												processResponse.setCustId(accountTranMaster.getStrCustId());
												processResponse.setMontraTxnId(accountTranMaster.getMontraTxnId().trim());
												
												processResponse.setAmsTransactionId(mainTxnId);
												processResponse.setAuthCode(transactionPostingConfig.getAuthCode());												
											}
											else
											{
												processResponse.setTransactionId(mainTxnId);
											}
											
											processResponse.setTransactionDate(ams.cms.utility.Utils.simpleDateTimeFormat.format(transactionPostingConfig.getTxnDate()));
											processResponse.setSenderAccountNo(accountTranMaster.getSenderAccountNo());
											processResponse.setReceipentAccountNo(accountTranMaster.getReceipentAccountNo());
											processResponse.setResponseCode(transactionPostingConfig.getResponseCode());
											processResponse.setCurrentAvailableBalance(transactionPostingConfig.getFromAccount().getAvailableBalance());
										}
									}
									else
									{
										processResponse.setCode("E0000");
										processResponse.setStatus("Failed");
										//processResponse.setMessage("Recipent Account Not Active");
										processResponse.setMessage("Recipent Account is in "+AccountStatus.getAccountStatus(recipientAccountInfo.getStrStatus())+" State!");
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
								//processResponse.setMessage("Sender Account Not Active");
								processResponse.setMessage("Sender Account is in "+AccountStatus.getAccountStatus(senderAccountResponse.getStrStatus())+" State!");
							}
						}
						else 
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Sender Account Not Found");
						}
					}
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Transaction Not Allowed for Same Account Number");
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
	//	txnReqRespLogMasterService.addTxnReqRespLogData(accountTranMaster, appInfo, processResponse, requestDate, responseDate, participantId, mainTxnId, montraTxnId, endpoint, processResponse.getStatus());
	
		//return processResponse;
		return CompletableFuture.completedFuture(processResponse);
	}
	
	@Async("multiThreadBean")
	public CompletableFuture<PayloadReqRes> prōcessP2PInternalTxnOfEncryptedRequest(HttpServletRequest request, PayloadReqRes req) throws InterruptedException, ExecutionException
	{
	//	String endpoint = "/NGN/txn/performTxn";
		
		ProcessResponse processResponse = new ProcessResponse();
		String mainTxnId = null;
		String montraTxnId = null;
		String participantId = null;
		Date requestDate = null;
		try 
		{
			requestDate = Utils.getCurrentDate();
			mainTxnId = transactionIdService.getNewTransactionId();
			
			ProcessResponse	processResps = apiSecretKeyUtility.validateRequestAPI(request, req);
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
		//		txnReqRespLogMasterService.addTxnReqRespLogData(req, appInfo, processResps, requestDate, Utils.getCurrentDate(), participantId, mainTxnId, montraTxnId, endpoint, "Failed");
				return CompletableFuture.completedFuture(encryptDecryptData.encryptPayloadReqRes(req, processResps));
			}
			
			String decrypt = appInfo.getDecryptedPayload();
			AccountTranMaster accountTranMaster = new ObjectMapper().readValue(decrypt, AccountTranMaster.class);
			
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");
			participantId = appInfo.getStrParticipantId();
			
			if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				montraTxnId = accountTranMaster.getMontraTxnId().trim();
				
				ExternalTxnRequest externalTxnRequest = new ExternalTxnRequest();
				externalTxnRequest.setCustId(accountTranMaster.getStrCustId());
				externalTxnRequest.setCid(accountTranMaster.getCid());
				externalTxnRequest.setBid(accountTranMaster.getBid());
				externalTxnRequest.setMontraTxnId(montraTxnId);
				externalTxnRequest.setSecretCode(accountTranMaster.getSecretCode());
				
				processResponse = externalTxnRequestValidator.validateMontraTxnRequest(processResponse, externalTxnRequest);
			}
			
			if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
			{
				if(!accountTranMaster.getSenderAccountNo().equalsIgnoreCase(accountTranMaster.getReceipentAccountNo())) 
				{
					if (!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
					{
						CustomerIdCreation customerIdCreation = new CustomerIdCreation();
						customerIdCreation.setStrCustId(accountTranMaster.getStrCustId());
						
						processResponse = accountValidator.validateCustomerPIN(processResponse, customerIdCreation);
					}
					if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
					{
						AccountCreation senderAccountInfo = new AccountCreation();						
						senderAccountInfo.setStrAccountNumber(accountTranMaster.getSenderAccountNo());
						senderAccountInfo.setStrCustId(accountTranMaster.getStrCustId());	
						
						AccountResponse senderAccountResponse =	accountMasterService.getAccountMasterInformation(senderAccountInfo);
						if (senderAccountResponse != null && senderAccountResponse.getStrCustId() != null) 
						{
							if ("Active".equalsIgnoreCase(senderAccountResponse.getStrStatus())) 
							{
								AccountCreation recipientAccountInfoIns = new AccountCreation();
								recipientAccountInfoIns.setStrAccountNumber(accountTranMaster.getReceipentAccountNo());
								
								AccountResponse recipientAccountInfo = accountMasterService.getAccountMasterInformation(recipientAccountInfoIns);
								if (recipientAccountInfo != null && recipientAccountInfo.getStrCustId() != null) 
								{
									if ("Active".equalsIgnoreCase(recipientAccountInfo.getStrStatus())) 
									{
										String fee = (accountTranMaster.getFee()!=null) ? accountTranMaster.getFee().trim(): "0";
										String vat = (accountTranMaster.getVat()!=null) ? accountTranMaster.getVat().trim(): "0";
										
										TransactionPostingConfig transactionPostingConfig = new TransactionPostingConfig();
										
										transactionPostingConfig.setTxnId(mainTxnId);//Added AMS txnId
										
										transactionPostingConfig.setParticipantId(participantId);
										transactionPostingConfig.setStrSrcTxnId(accountTranMaster.getMontraTxnId());
										transactionPostingConfig.setTxnType(accountTranMaster.getStrTran_type());
										
										transactionPostingConfig.setTxnAmount(accountTranMaster.getStrTransaction_amount());
										transactionPostingConfig.setFeeType(accountTranMaster.getFeeType());
										transactionPostingConfig.setFeeApplicableTo(accountTranMaster.getFeeApplicableTo());
										
										//Added By Sunil Y , Ams Will Populate EntityInfo And EntityRespon For Sender and Recipient [start]
										senderAccountResponse.setEntityInfo(recipientAccountInfo.getStrAccountHolderName());
										senderAccountResponse.setEntityNumber(recipientAccountInfo.getStrAccountNumber());
										
										recipientAccountInfo.setEntityInfo(senderAccountResponse.getStrAccountHolderName());
										recipientAccountInfo.setEntityNumber(senderAccountResponse.getStrAccountNumber());										
										//Added By Sunil Y , Ams Will Populate EntityInfo And EntityRespon For Sender and Recipient [End]
										
										senderAccountResponse.setAvailableBalance(AccountUtility.getUpdatedAvailableBalance(senderAccountResponse.getStrClosingBalance(), accountTranMaster.getStrTransaction_amount(), null, null));
										transactionPostingConfig.setFromAccount(senderAccountResponse);	
										
										transactionPostingConfig.setToAccount(recipientAccountInfo);
										
										TierAccountResponse fromTierAccountMaster = AccountUtility.getTierAccountResponse(senderAccountResponse);
										transactionPostingConfig.setFromTierAccountMaster(fromTierAccountMaster);
										
										TierAccountResponse toTierAccountMaster = AccountUtility.getTierAccountResponse(recipientAccountInfo);
										transactionPostingConfig.setToTierAccountMaster(toTierAccountMaster);
										
										GLAccountTypeMaster fromLinkedGLAccountType = AccountUtility.getGLAccountTypeMaster(senderAccountResponse);
										transactionPostingConfig.setFromLinkGLAccount(fromLinkedGLAccountType);
										
										GLAccountTypeMaster toLinkedGLAccountType = AccountUtility.getGLAccountTypeMaster(recipientAccountInfo);
										transactionPostingConfig.setToLinkGLAccount(toLinkedGLAccountType);	
										
										HashMap<String, String> linkedGLMap = new HashMap<String, String>();
										linkedGLMap.put(fromLinkedGLAccountType.getStrAccountNumber().trim(), fromLinkedGLAccountType.getStrClosingBalance().trim());
										linkedGLMap.put(toLinkedGLAccountType.getStrAccountNumber().trim(), toLinkedGLAccountType.getStrClosingBalance().trim());
										
										transactionPostingConfig.setLinkedGLMap(linkedGLMap);
										
										processResponse = accountTxnHandler.processClosedLoopTransaction(processResponse, transactionPostingConfig, fee, vat);
										if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
										{
											processResponse.setMessage("Transaction Successful.");
											if(accountTranMaster != null && accountTranMaster.getDynamicQrRefNo() != null && accountTranMaster.getDynamicQrRefNo().trim().length() > 0 ) 
											{
												try 
												{
													DynamicQrGeneration dynamicQrGeneration = new DynamicQrGeneration();
													dynamicQrGeneration.setPaidByAccountNumber(transactionPostingConfig.getFromAccount().getStrAccountNumber());
													dynamicQrGeneration.setPaidByAccountType(transactionPostingConfig.getFromAccount().getStrAccountType());
													dynamicQrGeneration.setTranId(transactionPostingConfig.getTxnId());
													dynamicQrGeneration.setPaymentReceivedDate(transactionPostingConfig.getTxnDate());
													dynamicQrGeneration.setPaymentReceivedTime(transactionPostingConfig.getTxnTime());
													dynamicQrGeneration.setRefNo(accountTranMaster.getDynamicQrRefNo());
													
													dynamicQrGeneration.setResponseCode("00");
													int count =	qrCodeService.updateQrCodeFieldsAfterTxn(dynamicQrGeneration);
													amsLogger.writeInfoLog("Inside performTxn count::["+count+"]");
												}
												catch (Exception e) {
													amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
												}
											}
											if (accountTranMaster.getMontraTxnId() != null && accountTranMaster.getMontraTxnId().trim().length() > 0) 
											{
												processResponse.setCid(accountTranMaster.getCid());
												processResponse.setCustId(accountTranMaster.getStrCustId());
												processResponse.setMontraTxnId(accountTranMaster.getMontraTxnId().trim());
												
												processResponse.setAmsTransactionId(mainTxnId);
												processResponse.setAuthCode(transactionPostingConfig.getAuthCode());												
											}
											else
											{
												processResponse.setTransactionId(mainTxnId);
											}
											
											processResponse.setTransactionDate(ams.cms.utility.Utils.simpleDateTimeFormat.format(transactionPostingConfig.getTxnDate()));
											processResponse.setSenderAccountNo(accountTranMaster.getSenderAccountNo());
											processResponse.setReceipentAccountNo(accountTranMaster.getReceipentAccountNo());
											processResponse.setResponseCode(transactionPostingConfig.getResponseCode());
											processResponse.setCurrentAvailableBalance(transactionPostingConfig.getFromAccount().getAvailableBalance());
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
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Transaction Not Allowed for Same Account Number");
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
		
//		txnReqRespLogMasterService.addTxnReqRespLogData(req, appInfo, processResponse, requestDate, Utils.getCurrentDate(), participantId, mainTxnId, montraTxnId, endpoint, processResponse.getStatus());
		return CompletableFuture.completedFuture(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
}
