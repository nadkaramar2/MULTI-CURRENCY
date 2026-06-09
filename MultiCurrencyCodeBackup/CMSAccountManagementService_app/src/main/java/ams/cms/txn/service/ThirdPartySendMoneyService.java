package ams.cms.txn.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.handler.AccountTxnHandler;
import ams.cms.api.handler.ExternalTxnRequestValidator;
import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.ExternalTxnRequest;
import ams.cms.api.model.MiddleWareBankRequestModel;
import ams.cms.api.model.MiddleWareSendMoneyResponse;
import ams.cms.api.model.TierAccountResponse;
import ams.cms.api.utitlity.Utils;
import ams.cms.config.AppInfo;
import ams.cms.config.CommonConstants;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.config.TransactionConfig;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.dao.AccountTranMasterDao;
import ams.cms.handler.LoadMasterHandler;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.BankDetailsInfo;
import ams.cms.model.BeneficiaryTxnMaster;
import ams.cms.model.FundTransferIn;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.PullAccountModel;
import ams.cms.model.TransactionTypeModel;
import ams.cms.model.TxnReqRes;
import ams.cms.services.AccountMasterService;
import ams.cms.services.BankDetailsInfoService;
import ams.cms.services.BeneficiaryTxnMasterService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.services.PullAccountService;
import ams.cms.services.TransactionIdService;
import ams.cms.services.TransactionTypeService;
import ams.cms.services.TxnReqRespLogMasterService;
import ams.cms.txn.handler.AccountTxnEmailHandler;
import ams.cms.txn.handler.AccountTxnMasterHandler;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.FundTransferProcessResponse;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.AccountUtility;
import ams.cms.utility.PayloadReqRes;
import ams.cms.utility.ThirdPartyRequestParam;

@Service
public class ThirdPartySendMoneyService 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(ThirdPartySendMoneyService.class);
	
	@Autowired
	private TxnReqRespLogMasterService txnReqRespLogMasterService;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	private	TransactionTypeService transactionTypeService;
	
	@Autowired 
	private	AccountMasterService accountMasterService;
	
	@Autowired
	private	GLAccountTypeMasterService glAccountTypeMasterService;
	
	@Autowired
	private	AccountTxnMasterHandler accountTxnMasterHandler;
	
	@Autowired
	private AccountTxnEmailHandler accountTxnEmailHandler;
	
	@Autowired
	private BeneficiaryTxnMasterService beneficiaryTxnMasterService;
	
	@Autowired
	private PullAccountService pullAccountService;
	
	@Autowired
	private AccountTranMasterDao accountTranMasterDao;
	
	@Autowired
	private LoadMasterHandler loadMasterHandler;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	private AppInfo appInfo;
	
	@Autowired
	private ExternalTxnRequestValidator externalTxnRequestValidator;
	
	@Autowired
	private AccountTxnHandler accountTxnHandler;
	
	@Autowired
	private BankDetailsInfoService bankDetailsInfoService;
	
	@Autowired
	private TransactionIdService transactionIdService;
	
	@Async("multiThreadBean")
	public CompletableFuture<PayloadReqRes> processThirdPartySendMoneyTxn(HttpServletRequest request, PayloadReqRes req) throws InterruptedException, ExecutionException
	{
	//	String endpoint = "/NGN/sendMoney";
		
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
			ThirdPartyRequestParam thirdPartyRequestParam = new ObjectMapper().readValue(decrypt, ThirdPartyRequestParam.class);
			
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");
			
			participantId = appInfo.getStrParticipantId();
			
			processResponse = accountTxnHandler.validateEntityInfoAndNumber(decrypt, processResponse);//Added By Sunil Y , Validation For EntityInfo And EntityNo
			if ("S0000".equalsIgnoreCase(processResponse.getCode()) && "NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				montraTxnId = thirdPartyRequestParam.getMontraTxnId().trim();
				
				ExternalTxnRequest externalTxnRequest = new ExternalTxnRequest();
				externalTxnRequest.setCustId(thirdPartyRequestParam.getCustId());
				externalTxnRequest.setCid(thirdPartyRequestParam.getCid());
				externalTxnRequest.setBid(thirdPartyRequestParam.getBid());
				externalTxnRequest.setMontraTxnId(thirdPartyRequestParam.getMontraTxnId());
				externalTxnRequest.setSecretCode(thirdPartyRequestParam.getSecretCode());
				
				processResponse = externalTxnRequestValidator.validateMontraTxnRequest(processResponse, externalTxnRequest);
			}
			
			String thirdPartyTxnType = "TPA";
			thirdPartyRequestParam.setBeneficiaryBankName("Access Bank");
			if (thirdPartyRequestParam.getBeneficiaryBankCode() != null && thirdPartyRequestParam.getBeneficiaryBankCode().trim().length() > 0) 
			{
				BankDetailsInfo beBankDetailsInfo =	bankDetailsInfoService.getBankDetailsInfoFromBankCode(thirdPartyRequestParam.getBeneficiaryBankCode().trim());
				if (!"044".equalsIgnoreCase(thirdPartyRequestParam.getBeneficiaryBankCode().trim())) 
				{
					thirdPartyRequestParam.setBeneficiaryInstitutionCode(beBankDetailsInfo.getStrNipCode());
					thirdPartyRequestParam.setBeneficiaryCurrencyCode("566");
					thirdPartyTxnType = "TPO";
					thirdPartyRequestParam.setBeneficiaryBankName("Other Bank");
				}
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Beneficiary Bank Code Not Found In Request!");
			}			
			
			if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
			{
				AccountCreation senderAccountInfo = new AccountCreation();						
				senderAccountInfo.setStrAccountNumber(thirdPartyRequestParam.getCustAccountNo());
				senderAccountInfo.setStrCustId(thirdPartyRequestParam.getCustId());	
				
				AccountResponse senderAccountResponse =	accountMasterService.getAccountMasterInformation(senderAccountInfo);
				if (senderAccountResponse != null && senderAccountResponse.getStrCustId() != null) 
				{
					TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
					transactionTypeModel.setStrTxnTypeKeyWord(thirdPartyRequestParam.getTxnType());
					
					transactionTypeModel = transactionTypeService.getGlAccountNoByTxnKeyword(transactionTypeModel);					
					if (!(transactionTypeModel!=null && transactionTypeModel.getStrGLAccountType()!=null && transactionTypeModel.getStrGLAccountType().trim().length() > 0)) 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Transaction type based GL not configured!");
					}		
					if ("S0000".equalsIgnoreCase(processResponse.getCode()) && transactionTypeModel!=null) 
					{
						GLAccountTypeMaster glAccountviewModel = new GLAccountTypeMaster();
						glAccountviewModel.setStrGLAccountType(transactionTypeModel.getStrGLAccountType().trim());
						
						GLAccountTypeMaster thirdPartyGLAccount = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(glAccountviewModel);
						if(thirdPartyGLAccount !=null && thirdPartyGLAccount.getStrAccountNumber() != null)
						{
							GLAccountTypeMaster ctrGlAccountTypeMaster = new GLAccountTypeMaster();
							ctrGlAccountTypeMaster.setStrGLAccountType("CTR");
							
							ctrGlAccountTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(ctrGlAccountTypeMaster);			
							if (ctrGlAccountTypeMaster != null && ctrGlAccountTypeMaster.getStrID() != null) 
							{
								String fee = (thirdPartyRequestParam.getFee() != null) ? thirdPartyRequestParam.getFee().trim(): "0";
								String vat = (thirdPartyRequestParam.getVat() != null) ? thirdPartyRequestParam.getVat().trim(): "0";
								
								TransactionPostingConfig transactionPostingConfig = new TransactionPostingConfig();
								transactionPostingConfig.setTxnId(txnId);
								
								transactionPostingConfig.setParticipantId(participantId);
								transactionPostingConfig.setStrSrcTxnId(thirdPartyRequestParam.getMontraTxnId());
								transactionPostingConfig.setTxnType(thirdPartyRequestParam.getTxnType());
								
								transactionPostingConfig.setTxnAmount(thirdPartyRequestParam.getTxnAmount());
								transactionPostingConfig.setFeeType(thirdPartyRequestParam.getFeeType());
								transactionPostingConfig.setFeeApplicableTo(thirdPartyRequestParam.getFeeApplicableTo());
								
								//Added By Sunil Y , Setting entity detalis in sender response  [start]
								senderAccountResponse.setEntityInfo(thirdPartyRequestParam.getEntityInfo());
								senderAccountResponse.setEntityNumber(thirdPartyRequestParam.getEntityNumber());
								//Added By Sunil Y , Setting entity detalis in sender response  [end]
								
								senderAccountResponse.setAvailableBalance(AccountUtility.getUpdatedAvailableBalance(senderAccountResponse.getStrClosingBalance(), thirdPartyRequestParam.getTxnAmount(), null, null));
								transactionPostingConfig.setFromAccount(senderAccountResponse);	
								
								transactionPostingConfig.setEmailId(senderAccountResponse.getStrEmailID());
								transactionPostingConfig.setAccountHolderName(senderAccountResponse.getStrAccountHolderName());
								
								TierAccountResponse fromTierAccountMaster = AccountUtility.getTierAccountResponse(senderAccountResponse);
								transactionPostingConfig.setFromTierAccountMaster(fromTierAccountMaster);
								
								GLAccountTypeMaster fromLinkedGLAccountType = AccountUtility.getGLAccountTypeMaster(senderAccountResponse);
								transactionPostingConfig.setFromLinkGLAccount(fromLinkedGLAccountType);
								
								HashMap<String, String> linkedGLMap = new HashMap<String, String>();
								linkedGLMap.put(fromLinkedGLAccountType.getStrAccountNumber().trim(), fromLinkedGLAccountType.getStrClosingBalance().trim());
								
								transactionPostingConfig.setLinkedGLMap(linkedGLMap);
								
								transactionPostingConfig.setGlAccountTypeMaster(thirdPartyGLAccount);
								transactionPostingConfig.setCtrlGlAccountTypeMaster(ctrGlAccountTypeMaster);
								
								processResponse = accountTxnHandler.processThirdPartyTxn(processResponse, transactionPostingConfig, fee, vat);
								if ("S0000".equalsIgnoreCase(processResponse.getCode()))
								{
									txnId = transactionPostingConfig.getTxnId().trim();
									
									TransactionConfig transactionConfig = new TransactionConfig();
									transactionConfig.setTxnId(txnId);
									transactionConfig.setThirdPartyRequestParam(thirdPartyRequestParam);
									transactionConfig.setParticipantId(participantId);
									transactionConfig.setAccountHolderName(transactionPostingConfig.getAccountHolderName());
									
									if (transactionPostingConfig.getEmailId() != null && transactionPostingConfig.getEmailId().trim().length() > 0) 
									{
										ArrayList<String> emailList = new ArrayList<String>();
										emailList.add(transactionPostingConfig.getEmailId().trim());
										
										transactionConfig.setEmailIdList(emailList);
									}									
									addBeneficiaryTxnRecords(transactionConfig);
									
									thirdPartyRequestParam.setTxnType(thirdPartyTxnType);
									thirdPartyRequestParam.setAmsTxnId(txnId);
									thirdPartyRequestParam.setAuthCode(transactionPostingConfig.getAuthCode());
									
									StringBuilder ResponseMsgSb = new StringBuilder();
									
									processResponse = callMiddleWareSendMoneyAPI(thirdPartyRequestParam, processResponse);									
									if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
									{
										try
										{
											BeneficiaryTxnMaster beneficiaryTxnMaster = new BeneficiaryTxnMaster();
											beneficiaryTxnMaster.setStrTxnId(txnId);
											beneficiaryTxnMaster.setStrTxnStatus("Success");
											beneficiaryTxnMasterService.updateBeneficiaryTxnMaster(beneficiaryTxnMaster);
										}
										catch (Exception e) 
										{
											amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
										}
										
										ResponseMsgSb.append("Transferred successfully amount ");
										if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
										{
												ResponseMsgSb.append("NGN ");
										}
										else 
										{
												ResponseMsgSb.append("Rs.");
										}
										ResponseMsgSb.append(thirdPartyRequestParam.getTxnAmount());
										ResponseMsgSb.append(" to ");											
										ResponseMsgSb.append(thirdPartyRequestParam.getBeneficiaryName());
										ResponseMsgSb.append(" ");
										ResponseMsgSb.append(thirdPartyRequestParam.getBeneficiaryAccountNo());											
										ResponseMsgSb.append(" ");
										ResponseMsgSb.append(thirdPartyRequestParam.getBeneficiaryBankCode());								
										ResponseMsgSb.append(".");
										
										if (transactionPostingConfig.getEmailId() != null && transactionPostingConfig.getEmailId().trim().length() > 0) 
										{
											transactionConfig.setTxnAmount(transactionPostingConfig.getTxnAmount());
 											accountTxnEmailHandler.sendThirdPartyDebitMailToCustomer(transactionConfig);
										}
										
										processResponse.setMessage(ResponseMsgSb.toString());
										processResponse.setCid(thirdPartyRequestParam.getCid());
										processResponse.setMontraTxnId(thirdPartyRequestParam.getMontraTxnId());
										processResponse.setCustId(thirdPartyRequestParam.getCustId());
										
										processResponse.setAmsTransactionId(transactionPostingConfig.getTxnId());
										processResponse.setResponseCode(transactionPostingConfig.getResponseCode());
										processResponse.setAuthCode(transactionPostingConfig.getAuthCode());
									}
									else
									{
										amsLogger.writeInfoLog("[ELSE]----------------->>>\n\n");
										transactionConfig.setAuthCode(transactionPostingConfig.getAuthCode());
										transactionConfig.setResponseCode("00");
										
										BeneficiaryTxnMaster beneficiaryTxnMaster = new BeneficiaryTxnMaster();
										beneficiaryTxnMaster.setStrTxnId(txnId);
										beneficiaryTxnMaster.setStrTxnStatus("Failed");
										
										beneficiaryTxnMasterService.updateBeneficiaryTxnMaster(beneficiaryTxnMaster);
										
										accountTxnMasterHandler.updateAccountTranMastersColumn(transactionConfig);
										
										FundTransferIn fundTransferIn = new FundTransferIn();
										fundTransferIn.setTranId(transactionPostingConfig.getTxnId());
										
										fundTransferIn.setTranType(thirdPartyRequestParam.getTxnType());
										fundTransferIn.setReversedTxn(true);
										fundTransferIn.setChannelCode(transactionTypeModel.getStrGLAccountType().trim());
										fundTransferIn.setIsFeeTypeExist(transactionPostingConfig.getIsFeeTypeExist());
										fundTransferIn.setTransactionPostingConfig(transactionPostingConfig);
										fundTransferIn.getTransactionPostingConfig().setStrSrcTxnId(transactionPostingConfig.getStrSrcTxnId());
										
										TxnReqRes txnReqResObj = new TxnReqRes();
										txnReqResObj.setFundTransferIn(fundTransferIn);
										
										FundTransferProcessResponse funProcessResponse = loadMasterHandler.processTransactionToLoadBalance(txnReqResObj);
										amsLogger.writeInfoLog("FundTransferProcessResponse funProcessResponse=["+funProcessResponse+"]");
										if ("S0000".equalsIgnoreCase(funProcessResponse.getCode())) 
										{
											ResponseMsgSb.append("Reversed Successfully Amount ");
											if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
											{
												ResponseMsgSb.append("NGN ");
											}
											else 
											{
												ResponseMsgSb.append("Rs.");
											}
											ResponseMsgSb.append(transactionPostingConfig.getTxnAmount());
											ResponseMsgSb.append(" to ");
											ResponseMsgSb.append(thirdPartyRequestParam.getCustAccountNo());
											ResponseMsgSb.append(".");
											
											processResponse.setCode("E0000");
											processResponse.setStatus("Failed");
											processResponse.setMessage("Transaction Reversed Successfully!");						
											
											if (transactionConfig.getEmailIdList() != null && transactionConfig.getEmailIdList().get(0) != null) 
											{
												accountTxnEmailHandler.sendReversedMailToCustomer(transactionConfig);
											}
										}
										else 
										{
											processResponse.setCode(funProcessResponse.getCode());
											processResponse.setStatus("Failed");
											processResponse.setMessage(funProcessResponse.getMessage());
										}
									}
								}
							}
							else
							{
								processResponse.setCode("E0000");
								processResponse.setStatus("Failed");
								processResponse.setMessage("Control Account Not Found.");
							}
							
						}
						else
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Third Party GL Account Not Found!");
						}
					}					
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Sender Account Not Found!");
				}
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error!");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		
	//	txnReqRespLogMasterService.addTxnReqRespLogData(req, appInfo, processResponse, requestDate, Utils.getCurrentDate(), participantId, txnId, montraTxnId, endpoint, processResponse.getStatus());
		return CompletableFuture.completedFuture(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	private void addBeneficiaryTxnRecords(TransactionConfig transactionConfig) 
	{
		try 
		{
			ThirdPartyRequestParam thirdPartyRequestParam =	transactionConfig.getThirdPartyRequestParam();
			
			BeneficiaryTxnMaster beneficiaryTxnMaster = new BeneficiaryTxnMaster();
			
			beneficiaryTxnMaster.setStrTxnId(transactionConfig.getTxnId());
			beneficiaryTxnMaster.setTxnDate(Utils.getCurrentDate());
			beneficiaryTxnMaster.setTxnTime(Utils.getFormattedCurrentTime());
			
			if (thirdPartyRequestParam.getBeneficiaryName()!=null && thirdPartyRequestParam.getBeneficiaryName().trim().length() > 0)
			{
				beneficiaryTxnMaster.setStrBeneficiaryAccountName(thirdPartyRequestParam.getBeneficiaryName());
			}
			if (thirdPartyRequestParam.getBeneficiaryBankAccountNo()!=null && thirdPartyRequestParam.getBeneficiaryBankAccountNo().trim().length() > 0) 
			{
				beneficiaryTxnMaster.setStrBeneficiaryAccountNo(thirdPartyRequestParam.getBeneficiaryBankAccountNo());
			}
			if (thirdPartyRequestParam.getBeneficiaryBankIFSC()!=null && thirdPartyRequestParam.getBeneficiaryBankIFSC().trim().length() > 0) 
			{
				beneficiaryTxnMaster.setStrBeneficiaryBankIfsc(thirdPartyRequestParam.getBeneficiaryBankIFSC());
			}
			if (thirdPartyRequestParam.getBeneficiaryVPAId()!=null && thirdPartyRequestParam.getBeneficiaryVPAId().trim().length() > 0) 
			{
				beneficiaryTxnMaster.setStrBeneficiaryVpaId(thirdPartyRequestParam.getBeneficiaryVPAId());
			}
			
			if (thirdPartyRequestParam.getBeneficiaryAccountNo()!=null && thirdPartyRequestParam.getBeneficiaryAccountNo().trim().length() > 0) 
			{
				beneficiaryTxnMaster.setStrBeneficiaryAccountNo(thirdPartyRequestParam.getBeneficiaryAccountNo());
			}
			if (thirdPartyRequestParam.getBeneficiaryBankCode()!=null && thirdPartyRequestParam.getBeneficiaryBankCode().trim().length() > 0) 
			{
				beneficiaryTxnMaster.setStrBeneficiaryBankIfsc(thirdPartyRequestParam.getBeneficiaryBankCode());
			}
			if (thirdPartyRequestParam.getBeneficiaryBankName()!=null && thirdPartyRequestParam.getBeneficiaryBankName().trim().length() > 0) 
			{
				beneficiaryTxnMaster.setStrBeneficiaryBankName(thirdPartyRequestParam.getBeneficiaryBankName());
			}
			
			beneficiaryTxnMaster.setStrFromAccountNo(thirdPartyRequestParam.getCustAccountNo());
			beneficiaryTxnMaster.setStrFromAccountType(thirdPartyRequestParam.getCustAccountType());
			beneficiaryTxnMaster.setStrTxnAmount(thirdPartyRequestParam.getTxnAmount()); 
			beneficiaryTxnMaster.setStrTxnType(thirdPartyRequestParam.getTxnType());
			
			beneficiaryTxnMasterService.saveBeneficiaryTxnMasterRecords(beneficiaryTxnMaster);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private ProcessResponse callMiddleWareSendMoneyAPI(ThirdPartyRequestParam thirdPartyRequestParam, ProcessResponse processResponse)
	{
		processResponse.setCode("S0000");
		processResponse.setStatus("Success");
		processResponse.setMessage("Successfully getting response");
		
		try 
		{
			MiddleWareBankRequestModel middleWareAppInfoData = getMiddleWareAppInfoModel("6");
			
			PullAccountModel pullAccountModelObj =  new PullAccountModel();
			pullAccountModelObj.setStrParticipantId(appInfo.getStrParticipantId());
			 
			PullAccountModel pullAccountModel = pullAccountService.getPullAccountModelByParticipantId(pullAccountModelObj);			
			processResponse = middleWareSendMoney(thirdPartyRequestParam, middleWareAppInfoData, pullAccountModel, processResponse );
		 }
		 catch (Exception e) 
		 {
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		 }
		return processResponse;
	}
	private MiddleWareBankRequestModel getMiddleWareAppInfoModel(String participantId)
	{
		MiddleWareBankRequestModel middleBankRequestModel = new MiddleWareBankRequestModel();
		middleBankRequestModel.setParticipantId(participantId);
		 
		MiddleWareBankRequestModel middleWareAppInfoData = bankDetailsInfoService.getRequestDataForBank(middleBankRequestModel);
		return middleWareAppInfoData;
	}
	private ProcessResponse middleWareSendMoney(ThirdPartyRequestParam thirdPartyRequestParam, MiddleWareBankRequestModel middleWareBankRequestModel, PullAccountModel pullAccountModel, ProcessResponse processResponse) throws JsonMappingException, JsonProcessingException
	{ 
		try 
		{
			String serverUrl = environment.getProperty("middleware_sendMoney_url");
					
			HttpHeaders headers = new HttpHeaders();			
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			headers.set("loginid", "amsuser");
			headers.set("Authorization", "YW11c2Vyc2VjcmV0MjAyMw==");
			amsLogger.writeInfoLog("inside middleWareSendMoney headers=["+headers+"]");
			
			String txnType = thirdPartyRequestParam.getTxnType();
			txnType = txnType.trim();
			amsLogger.writeInfoLog("inside middleWareSendMoney txnType=["+txnType+"]");
				    
		 	Map<String, Object> requestBody = new HashMap<>();
		    
		 	requestBody.put("transactionid", thirdPartyRequestParam.getAmsTxnId());
		    requestBody.put("transaction_type", txnType);
		    requestBody.put("amount", thirdPartyRequestParam.getTxnAmount());	    
		   
		    requestBody.put("beneficiary_account", thirdPartyRequestParam.getBeneficiaryAccountNo());		    
		    requestBody.put("beneficiary_name",thirdPartyRequestParam.getBeneficiaryName());
		    
		    requestBody.put("wallet_account", thirdPartyRequestParam.getCustAccountNo());
		    requestBody.put("trans_authcode", thirdPartyRequestParam.getAuthCode());		    
		    
		    requestBody.put("narration", "Save Money");
		    requestBody.put("fee", "0.0");
		    requestBody.put("vat", "0.0");
		    
		    if ("TPA".equalsIgnoreCase(txnType)) 
		    {
		    	requestBody.put("beneficiary_bankcode", pullAccountModel.getStrPoolAccountBankCode());//For Access Bank 
		    	requestBody.put("beneficiary_institutioncode", "000015");
		    	requestBody.put("beneficiary_account_currency", "NGN");
		    	
		    	requestBody.put("poolaccount_currency", "NGN");
		    }
		    else
		    {
		    	requestBody.put("poolaccount_currency", pullAccountModel.getStrPoolAccountCurrency());
		    	
		    	requestBody.put("beneficiary_bankcode", thirdPartyRequestParam.getBeneficiaryBankCode());
		    	requestBody.put("beneficiary_institutioncode", thirdPartyRequestParam.getBeneficiaryInstitutionCode());
		    	requestBody.put("beneficiary_account_currency", thirdPartyRequestParam.getBeneficiaryCurrencyCode());		    	
		    }
		    requestBody.put("poolaccount", pullAccountModel.getStrPoolAccount());
		    requestBody.put("poolaccount_bankcode", pullAccountModel.getStrPoolAccountBankCode());
		    requestBody.put("poolaccount_name", pullAccountModel.getStrPoolAccountName());		    
		    
		    requestBody.put("channel_code", middleWareBankRequestModel.getChannelCode());		  
		    requestBody.put("user_loginid", middleWareBankRequestModel.getUserLoginId());		    
		    
		    requestBody.put("appid", middleWareBankRequestModel.getAppId());
		  
		    String auditId = "ARTH" + thirdPartyRequestParam.getAmsTxnId();
		    requestBody.put("auditid", auditId);
		    
		    amsLogger.writeInfoLog("---->>> Inside middleWareSendMoney requestBody::"+requestBody);
			      
			HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(requestBody, headers);
			    
			RestTemplate restTemplate = new RestTemplate();			  
		    String resp = restTemplate.postForObject(serverUrl, request, String.class);
		    amsLogger.writeInfoLog("Inside middleWareSendMoney response str::"+resp);
		    
			MiddleWareSendMoneyResponse middleWareSendMoneyResponse = new ObjectMapper().readValue(resp, MiddleWareSendMoneyResponse.class);
			
			AccountTranMaster accountTranMaster = new AccountTranMaster();
			
			accountTranMaster.setAuditId(auditId);
			accountTranMaster.setStrTxn_id(thirdPartyRequestParam.getAmsTxnId());
			
			String jsonStr = null;
			if(middleWareSendMoneyResponse != null) 
			{
				String responseCode = middleWareSendMoneyResponse.getStrRespCode();
				amsLogger.writeInfoLog("Middle-Ware responseCode=["+responseCode+"]");
				
				responseCode = responseCode.trim();
				amsLogger.writeInfoLog("Middle-Ware After TRIM responseCode=["+responseCode+"]");
				
				jsonStr = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(middleWareSendMoneyResponse);
				amsLogger.writeInfoLog("Middle-Ware After TRIM jsonStr=["+jsonStr+"]");
				
				if (!"00".equalsIgnoreCase(responseCode.trim())) 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage(middleWareSendMoneyResponse.getStrRespDesc());
				}
				accountTranMaster.setStrResponseCode(responseCode);
			}
			else 
			{
				accountTranMaster.setStrResponseCode("06");
				
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Not Getting data");
			}
			
			accountTranMaster.setStrReservefield1(jsonStr);
			accountTranMaster.setStrTxn_id(thirdPartyRequestParam.getAmsTxnId());
			accountTranMasterDao.updateAccountTranMasterForSendMoney(accountTranMaster);
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error while getting response from Middle Ware Send Money API..");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		amsLogger.writeInfoLog("middleWareSendMoney processResponse::"+processResponse);
	    return processResponse;
	}
}
