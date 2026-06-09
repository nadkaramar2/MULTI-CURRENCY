package ams.cms.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.handler.ExternalTransactionHandler;
import ams.cms.api.handler.ThirdPartyTxnHandler;
import ams.cms.api.model.BankListResponse;
import ams.cms.api.service.CustomerIdService;
import ams.cms.config.AppInfo;
import ams.cms.config.CommonConstants;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.config.TransactionConfig;
import ams.cms.config.TransactionIdCreationConfigDao;
import ams.cms.constants.TransactionType;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountCreditCardTransactionModel;
import ams.cms.model.AccountStatement;
import ams.cms.model.BankDetails;
import ams.cms.model.BeneficiaryTxnMaster;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.RevolvingCreditCardMaster;
import ams.cms.model.RevolvingCreditCardTxnMaster;
import ams.cms.model.TransactionTypeModel;
import ams.cms.model.TxnData;
import ams.cms.model.TxnReqRes;
import ams.cms.services.AccountCreditCardTxnServices;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountStatementService;
import ams.cms.services.BankDetailsInfoService;
import ams.cms.services.BankDetailsService;
import ams.cms.services.BeneficiaryTxnMasterService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.services.MccWiseInterestService;
import ams.cms.services.RevolvingCreditCardMasterService;
import ams.cms.services.RevolvingCreditCardService;
import ams.cms.services.RevolvingCreditCardTxnService;
import ams.cms.services.RevolvingCreditInterestTxnService;
import ams.cms.services.TierAccountMasterService;
import ams.cms.services.TransactionTypeService;
import ams.cms.txn.handler.AccountTxnEmailHandler;
import ams.cms.txn.handler.AccountTxnMasterHandler;
import ams.cms.txn.handler.ControlAccountTxnHandler;
import ams.cms.txn.handler.ExternalServerHandler;
import ams.cms.txn.handler.GLAccountTxnHandler;
import ams.cms.txn.handler.GLTransactionHandler;
import ams.cms.txn.handler.TransactionValidator;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.BankDetailsResponse;
import ams.cms.utility.ExternalServerRequestResponseModel;
import ams.cms.utility.PayloadReqRes;
import ams.cms.utility.ThirdPartyRequestParam;
import ams.cms.utility.Utils;

@RestController
@RequestMapping("/thirdParty")
public class ThirdPartyTransactionController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(ThirdPartyTransactionController.class);
	
	@Autowired
	BankDetailsService bankDetailsService;
	
	@Autowired
	BankDetailsInfoService bankDetailsInfoService;
	
	@Autowired
	private EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	TransactionValidator transactionValidator;
	
	@Autowired
	TransactionIdCreationConfigDao transactionIdCreationConfigDao;
	
	@Autowired
	TransactionTypeService transactionTypeService;
	
	@Autowired 
	AccountMasterService accountMasterService;
	
	@Autowired 
	AccountStatementService accountStatementService;
	
	@Autowired
	GLTransactionHandler glTransactionHandler;
	
	@Autowired
	GLAccountTypeMasterService glAccountTypeMasterService;
	
	@Autowired
	AccountTxnMasterHandler accountTxnMasterHandler;
	
	@Autowired
	GLAccountTxnHandler	glAccountTxnHandler;
	
	@Autowired
	ControlAccountTxnHandler controlAccountTxnHandler;
	
	@Autowired
	RevolvingCreditCardService revolvingCreditCardService;
	
	@Autowired 
	RevolvingCreditCardMasterService revolvingCreditCardMasterService;

	@Autowired 
	MccWiseInterestService mccWiseInterestService;

	@Autowired 
	RevolvingCreditCardTxnService revolvingCreditCardTxnService;

	@Autowired 
	RevolvingCreditInterestTxnService revolvingCreditInterestTxnService;
	
	@Autowired
	AccountCreditCardTxnServices accountCreditCardTxnServices;
	
	@Autowired
	private AccountTxnEmailHandler accountTxnEmailHandler;
	
	@Autowired
	private BeneficiaryTxnMasterService beneficiaryTxnMasterService;
	
	@Autowired
	private ExternalServerHandler externalServerHandler;
	
	@Autowired
	private CustomerIdService customerIdService;
	
	@Autowired
	private ExternalTransactionHandler externalTransactionHandler;
	
	@Autowired
	private TierAccountMasterService tierAccountMasterService;
	
	@Autowired
	private ThirdPartyTxnHandler thirdPartyTxnHandler;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	private	AppInfo appInfo;
	
	
	private Map<String, GLAccountTypeMaster> glAccountTypeMapData = new HashMap<String, GLAccountTypeMaster>();
	
	@RequestMapping(value="/getBankNameList", method = RequestMethod.POST)
	public ResponseEntity<?> getBankNameListAPI(HttpServletRequest request,@RequestBody PayloadReqRes req) 
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
			boolean isDataFound = true;
			if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				List<BankListResponse> bankListResponses = bankDetailsInfoService.getNibssBankList();
				if (bankListResponses!=null && bankListResponses.size() > 0)
				{
					processResponse.setBanksList(bankListResponses);
				}
				else 
				{
					isDataFound = false;
				}
			}
			else
			{
				List<BankDetails> bankNameList = bankDetailsService.getAllBankNameList();
				if (bankNameList != null && bankNameList.size() > 0) 
				{
					List<String> bankNames = new ArrayList<String>();
					for (BankDetails bankDetails: bankNameList) 
					{
						if (bankDetails.getStrBankName()!=null && bankDetails.getStrBankName().trim().length() > 0) 
						{
							bankNames.add(bankDetails.getStrBankName().trim());
						}
					}
					processResponse.setBankNames(bankNames);
				}
				else 
				{
					isDataFound = false;
				}
			}
			if (isDataFound) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Successfully Retrive bank Name List.");
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("There is No Bank Name List Contain.");
			}
		} 
		catch (Exception e) 
		{
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error during get bank name list");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(value="/checkBenificiary", method = RequestMethod.POST)
	public ResponseEntity<?> checkBenificiary( HttpServletRequest request,@RequestBody PayloadReqRes req) 
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
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			ExternalServerRequestResponseModel externalServerRequestResponseModel = new ObjectMapper().readValue(decrypt, ExternalServerRequestResponseModel.class);
			
			TransactionConfig transactionConfig = new TransactionConfig();
			transactionConfig.setCode("S0000");
			transactionConfig.setExternalServerRequestResponseModel(externalServerRequestResponseModel);
			
			transactionConfig = externalServerHandler.getBeneficiaryInfo(transactionConfig);
			
			if("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setAccountHolderName(transactionConfig.getExternalServerRequestResponseModel().getAccountName()) ;
				processResponse.setMessage("Successfully Retrive account name.");
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage(transactionConfig.getMessage());
			}
		} 
		catch (Exception e) 
		{
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error during checkBenificiary");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(value="/getThirdPartyAccountName", method = RequestMethod.POST)
	public ResponseEntity<?> getBenificiaryName(@RequestBody ExternalServerRequestResponseModel externalServerRequestResponseModel) 
	{
		TransactionConfig transactionConfig = new TransactionConfig();
		transactionConfig.setExternalServerRequestResponseModel(externalServerRequestResponseModel);
		ProcessResponse processResponse = thirdPartyTxnHandler.getThirdPartyAccountName(transactionConfig);
		return ResponseEntity.ok(processResponse);
	}
	
	@RequestMapping(value="/getThirdPartyBankList", method = RequestMethod.POST)
	public ResponseEntity<?> getBankNameListAPI(@RequestBody ExternalServerRequestResponseModel externalServerRequestResponseModel) 
	{
		TransactionConfig transactionConfig = new TransactionConfig();
		ProcessResponse processResponse = thirdPartyTxnHandler.getThirdPartyBankList(transactionConfig);
		return ResponseEntity.ok(processResponse);
	}
	
	@RequestMapping(value="/checkBankIFSC", method = RequestMethod.POST)
	public ResponseEntity<?> getBankInforBasedonIFSC(HttpServletRequest request,@RequestBody PayloadReqRes req) 
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
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			ThirdPartyRequestParam thirdPartyRequestParam = new ObjectMapper().readValue(decrypt, ThirdPartyRequestParam.class);
			
			BankDetails bankDetails = new BankDetails();
			bankDetails.setStrIfscCode(thirdPartyRequestParam.getBankIFSC()); 

			bankDetails = bankDetailsService.getBankDetailsInstanceBasedOnParam(bankDetails);
			
			if(bankDetails!=null && bankDetails.getStrBranchName()!=null) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setBankDetails(getBankDetailsResponseMappedData(bankDetails));
				processResponse.setMessage("Successfully Retrive bank details.");
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("IFSC Code Not Found. Please Enter Valid IFSC.");
			}
		} 
		catch (Exception e) 
		{
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error retrive bank details");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(value="/processTxn", method = RequestMethod.POST)
	public ResponseEntity<?> processThirdPartyTxn(HttpServletRequest request,@RequestBody PayloadReqRes req) 
	{
		CommonConstants.applicationName = "NIGERIA";
		
		ProcessResponse processResponse = new ProcessResponse();
		processResponse.setCode("S0000");
		processResponse.setStatus("Success");
		TransactionConfig transactionConfig = new TransactionConfig();
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			ThirdPartyRequestParam thirdPartyRequestParam = new ObjectMapper().readValue(decrypt, ThirdPartyRequestParam.class);
			
			transactionConfig.setThirdPartyRequestParam(thirdPartyRequestParam);
			
			String txnId = transactionIdCreationConfigDao.getTransactionId();
			transactionConfig.setTxnId(txnId);
			
			TxnData txnData = new TxnData();			
			txnData.setTxn_id(Long.parseLong(txnId));
			txnData.setAccountNumber(thirdPartyRequestParam.getCustAccountNo());
			txnData.setAccountType(thirdPartyRequestParam.getCustAccountType());
			txnData.setTran_type(thirdPartyRequestParam.getTxnType());
			txnData.setDe004_amount(thirdPartyRequestParam.getTxnAmount());

			TxnReqRes txnReqRes = new TxnReqRes();
			txnReqRes.setTxnData(txnData);
			txnReqRes.setResponse(processResponse);
			
			//Hard Coded Third party GL START
			String thirdPartyGLType = ""; 
			if ("TPT".equalsIgnoreCase(thirdPartyRequestParam.getTxnType())) 
			{
				thirdPartyGLType = "IMPS";
				txnData.setChannelCode("MPS");
			}
			else
			{
				thirdPartyGLType = "UPI";
				txnData.setChannelCode("UPI");
			}
			//Hard Coded Third party GL END
			
			TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
			transactionTypeModel.setStrTxnTypeKeyWord(thirdPartyRequestParam.getTxnType());
			
			transactionTypeModel = transactionTypeService.getTransactionTypeMaster(transactionTypeModel);
			if (transactionTypeModel != null && transactionTypeModel.getStrID() != null) 
			{
				AccountCreation accountCreationObj = new AccountCreation();
				accountCreationObj.setStrAccountNumber(txnData.getAccountNumber());
				accountCreationObj.setStrAccountType(txnData.getAccountType());
				accountCreationObj.setStrCustId(thirdPartyRequestParam.getCustId());
				
				AccountCreation accountCreation = accountMasterService.getSenderAccountInformation(accountCreationObj);
				if("Active".equalsIgnoreCase(accountCreation.getStrAccounTypeStatus())) //<------ Account Type Status CHECK POINT
				{
					if("Active".equalsIgnoreCase(accountCreation.getStrStatus()))//<------ Account Status CHECK POINT
					{
						transactionConfig.setAccountCreation(accountCreation);
						transactionConfig.setProcessingCode(transactionTypeModel.getStrProcessingCode());
						
						transactionConfig = transactionConfig.getTransactionConfigInstance(txnReqRes, transactionConfig);
						
						transactionConfig = transactionValidator.txnValidate(transactionConfig);//<------  Validation Check POINT
						if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
						{
							txnReqRes = gLValidationProcess(txnReqRes, transactionConfig);
							amsLogger.writeInfoLog("Inside performTxn transactionConfig::"+transactionConfig);
							if ("S0000".equalsIgnoreCase(txnReqRes.getResponse().getCode())) 
							{
								CustomerIdCreation customerIdCreation = new CustomerIdCreation();
								customerIdCreation.setStrCustId(thirdPartyRequestParam.getCustId());
								
								String existingPIN = customerIdService.getCustomerPIN(customerIdCreation);
								amsLogger.writeInfoLog("Inside performTxn existingPIN::"+existingPIN);
								if (existingPIN != null)
								{
									if (existingPIN.equals(ams.cms.utility.Utils.generateHash(thirdPartyRequestParam.getCustPIN()))) 
									{
										txnReqRes = updatesAccountMaster(txnReqRes, accountCreation); //<---- Update balance and Limits
										
										//Added by Pankaj [Start]
										amsLogger.writeInfoLog("Inside performTxn applicationName::["+CommonConstants.applicationName+"]");
										if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
										{
											if (!"C".equalsIgnoreCase(accountCreation.getAccountCategoryType())) 
											{
												transactionConfig = externalTransactionHandler.updateLimitValues(transactionConfig);
												tierAccountMasterService.updateCummAvailableBalance(transactionConfig.getTierAccountMaster());
											}
										}
										//Added by Pankaj [End]
										
										GLAccountTypeMaster ctrGLAccountTypeMaster = glAccountTypeMapData.get("CTR".toLowerCase());
										
										TransactionConfig ctrlTransactionConfig = new TransactionConfig();
										ctrlTransactionConfig.setGlAccountTypeMaster(ctrGLAccountTypeMaster);								
										ctrlTransactionConfig = ctrlTransactionConfig.getTransactionConfigInstance(txnReqRes, ctrlTransactionConfig);								
										ctrlTransactionConfig.setAccountNo(ctrGLAccountTypeMaster.getStrAccountNumber());	
										
										GLAccountTypeMaster thirdPartyGLAccountTypeMaster = glAccountTypeMapData.get(txnData.getChannelCode().trim().toLowerCase());
										
										TransactionConfig thirdPartyTransactionConfig = new TransactionConfig();
										thirdPartyTransactionConfig.setGlAccountTypeMaster(thirdPartyGLAccountTypeMaster);								
										thirdPartyTransactionConfig = thirdPartyTransactionConfig.getTransactionConfigInstance(txnReqRes, thirdPartyTransactionConfig);
										
										GLAccountTypeMaster linkedGLAccountTypeMaster = glAccountTypeMapData.get("LINKED_GL".toLowerCase());
										
										TransactionConfig linkedGLTransactionConfig = new TransactionConfig();
										linkedGLTransactionConfig.setGlAccountTypeMaster(linkedGLAccountTypeMaster);							
										linkedGLTransactionConfig = linkedGLTransactionConfig.getTransactionConfigInstance(txnReqRes, linkedGLTransactionConfig);
										
										linkedGLTransactionConfig.setFromAccountNo(transactionConfig.getAccountCreation().getStrAccountNumber());
										//linkedGLTransactionConfig.setToAccountNo(linkedGLAccountTypeMaster.getStrAccountNumber());
										linkedGLTransactionConfig.setToAccountNo(thirdPartyGLAccountTypeMaster.getStrAccountNumber());
										
										accountTxnMasterHandler.insertTranMasterEntryWithMultipleAccount(linkedGLTransactionConfig); //<---- TRAN_MASTER ENTRY OF MULTIPLE ACCOUNT i.e. FROM AND TO GL ACCOUNT
										
										txnReqRes = addProcess(txnReqRes, accountCreation); //<--- Statement and credit card related Entries of Customer
										
										ArrayList<String> emailList = new ArrayList<String>();
										emailList.add(accountCreation.getStrEmailID());								
										transactionConfig.setEmailIdList(emailList);
										transactionConfig.setAccountHolderName(accountCreation.getStrAccountHolderName());
										
										addLinkedGLDebitAccountEntry(txnReqRes, linkedGLTransactionConfig);
										
										//thirdPartyTransactionConfig.setFromAccountNo(linkedGLAccountTypeMaster.getStrAccountNumber());
										//thirdPartyTransactionConfig.setToAccountNo(thirdPartyGLAccountTypeMaster.getStrAccountNumber());
										thirdPartyTransactionConfig.setFromAccountNo(thirdPartyGLAccountTypeMaster.getStrAccountNumber());
										thirdPartyTransactionConfig.setToAccountNo(ctrGLAccountTypeMaster.getStrAccountNumber());
										
										accountTxnMasterHandler.insertTranMasterEntryWithMultipleAccount(thirdPartyTransactionConfig); //<---- TRAN_MASTER ENTRY OF MULTIPLE ACCOUNT i.e. FROM AND TO GL ACCOUNT
										
										addThirdPartyGLAccountEntry(txnReqRes, thirdPartyTransactionConfig);
										
										accountTxnMasterHandler.insertTranMasterEntryWithSingleAccount(ctrlTransactionConfig);// <---- TRAN_MASTER ENTRY OF SINGLE ACCOUNT
										
										controlAccountTxnHandler.addTransferOUTControlAccountEntry(ctrlTransactionConfig);
										
										StringBuilder ResponseMsgSb = new StringBuilder("Transferred successfully amount Rs.");
										ResponseMsgSb.append(txnData.getDe004_amount());
										ResponseMsgSb.append(" via ");
										ResponseMsgSb.append(thirdPartyGLType);
										ResponseMsgSb.append(" to ");
										
										if ("TPU".equalsIgnoreCase(thirdPartyRequestParam.getTxnType())) 
										{
											ResponseMsgSb.append(thirdPartyRequestParam.getBeneficiaryVPAId());
										}
										else
										{
											ResponseMsgSb.append(thirdPartyRequestParam.getBeneficiaryName());
											ResponseMsgSb.append(" ");
											ResponseMsgSb.append(thirdPartyRequestParam.getBeneficiaryBankAccountNo());
											ResponseMsgSb.append(" ");
											ResponseMsgSb.append(thirdPartyRequestParam.getBeneficiaryBankIFSC());								
										}
										ResponseMsgSb.append(".");
										
										amsLogger.writeInfoLog("Inside performTxn processResponse::"+processResponse);
										processResponse.setMessage(ResponseMsgSb.toString());
										
										accountTxnEmailHandler.sendThirdPartyDebitMailToCustomer(transactionConfig);
										
										String authCode = Utils.getAlphaNumericString();
										transactionConfig.setAuthCode(authCode);
										transactionConfig.setResponseCode("00");
										amsLogger.writeInfoLog("Inside performTxn Final transactionConfig::"+transactionConfig);
									}
									else
									{
										processResponse.setCode("E0000");
										processResponse.setStatus("Failed");
										processResponse.setMessage("Entered PIN is Incorrect.");
									}
								}
								else 
								{
									processResponse.setCode("E0000");
									processResponse.setStatus("Failed");
									processResponse.setMessage("PIN Not Found Against this cust Id.");
								}								
							}
							else
							{
								processResponse = txnReqRes.getResponse();
							}
						}
						else
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage(transactionConfig.getMessage());
						}
					}
					else 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Account Deactivated.");
					}
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Account Type Deactivated.");
				}
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Processing Code not found.");
			}
		} 
		catch (Exception e) 
		{
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error during processing transaction");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		amsLogger.writeInfoLog("-------->>>>Inside performTxn transactionConfig::"+transactionConfig);
		if (!"S0000".equalsIgnoreCase(transactionConfig.getCode()))
		{
			transactionConfig.setResponseCode("06");
		}
		
		transactionConfig.setMessage(processResponse.getStatus());
		
		addBeneficiaryTxnRecords(transactionConfig);
		
		accountTxnMasterHandler.updateAccountTranMastersColumn(transactionConfig);
		
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	TxnReqRes gLValidationProcess(TxnReqRes txnReqRes, TransactionConfig transactionConfig) 
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
			glAccountTypeMaster.setStrGLAccountType("CTR");
			
			glAccountTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(glAccountTypeMaster);			
			if (glAccountTypeMaster!=null && glAccountTypeMaster.getStrID()!=null) 
			{
				glAccountTypeMapData.put(glAccountTypeMaster.getStrGLAccountType().trim().toLowerCase(), glAccountTypeMaster);
				
				glAccountTypeMaster = new GLAccountTypeMaster();			
				glAccountTypeMaster.setStrGLAccountType(txnReqRes.getTxnData().getChannelCode());				
				
				glAccountTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(glAccountTypeMaster);				
				if (glAccountTypeMaster!=null && glAccountTypeMaster.getStrID()!=null) 
				{
					glAccountTypeMapData.put(txnReqRes.getTxnData().getChannelCode().trim().toLowerCase(), glAccountTypeMaster);
					
					glAccountTypeMaster = new GLAccountTypeMaster();
					glAccountTypeMaster.setStrGLAccountType(transactionConfig.getAccountCreation().getStrGLAccountType());								
					
					glAccountTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(glAccountTypeMaster);					
					if(glAccountTypeMaster != null && glAccountTypeMaster.getStrID() != null) 
					{
						glAccountTypeMapData.put("LINKED_GL".toLowerCase(), glAccountTypeMaster);
					}
					else
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Linked GL Account Type Not Found.");
					}
				}
				else
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("GL Account Type Not Found.");
				}
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Control Account Not Found.");
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error During GL Validation");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return txnReqRes;
	}
	private TxnReqRes updatesAccountMaster(TxnReqRes txnReqRes, AccountCreation accountCreation)
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			//Added by Sunny Soni Start
			if (!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				updatesTxnLimits(txnReqRes, accountCreation);			
			}
			//Added by Sunny Soni End
			if ("C".equalsIgnoreCase(accountCreation.getAccountCategoryType()))
			{
				updatesCreditLimits(txnReqRes, accountCreation);
			}			
			updateAccountBalance(txnReqRes, accountCreation);			
			accountMasterService.updateAccountCreationFields(accountCreation);//Updating Account_Master Table
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error During updatesAccountMaster");
		}
		return txnReqRes;
	}
	private TxnReqRes updatesTxnLimits(TxnReqRes txnReqRes, AccountCreation accountCreation)
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			double txnAmount = 0.00;
			if (txnReqRes.getTxnData().getDe022_pos_entry_mode()!=null && txnReqRes.getTxnData().getDe022_pos_entry_mode().equalsIgnoreCase("810")) 
			{
				txnAmount = Utils.stringToDouble(Utils.decimalFormat.format(Double.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00));
			} 
			else
			{
				txnAmount = Utils.stringToDouble(Utils.decimalFormat.format(Double.valueOf(txnReqRes.getTxnData().getDe004_amount())));
			}
			double strNewDailyLimit = 0d;
			double strNewMonthlylimit = 0d ;
			double strNewYearlyLimit = 0d ;
			if(txnReqRes.getTxnData().getTran_type() != null && txnReqRes.getTxnData().getTran_type().equalsIgnoreCase("DPT"))
			{
				 strNewDailyLimit = Utils.stringToDouble(accountCreation.getStrAvailableDailyLimit()) + txnAmount;
				 strNewMonthlylimit = Utils.stringToDouble(accountCreation.getStrAvailableMonthlyLimit()) + txnAmount; 
				 strNewYearlyLimit = Utils.stringToDouble(accountCreation.getStrAvailableYearlyLimit()) + txnAmount;
				
			}
			else
			{
				 strNewDailyLimit = Utils.stringToDouble(accountCreation.getStrAvailableDailyLimit()) - txnAmount;
				 strNewMonthlylimit = Utils.stringToDouble(accountCreation.getStrAvailableMonthlyLimit()) - txnAmount; 
				 strNewYearlyLimit = Utils.stringToDouble(accountCreation.getStrAvailableYearlyLimit()) - txnAmount;
			}
			accountCreation.setStrAvailableDailyLimit(Utils.decimalFormat.format(strNewDailyLimit));
			accountCreation.setStrAvailableMonthlyLimit(Utils.decimalFormat.format(strNewMonthlylimit));
			accountCreation.setStrAvailableYearlyLimit(Utils.decimalFormat.format(strNewYearlyLimit));
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error During updatesTxnLimits");
		}
		return txnReqRes;
	}
	private TxnReqRes updateAccountBalance(TxnReqRes txnReqRes, AccountCreation accountCreation)
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			double txnAmount = 0.00;
			if (txnReqRes.getTxnData().getDe022_pos_entry_mode()!=null && txnReqRes.getTxnData().getDe022_pos_entry_mode().equalsIgnoreCase("810"))
			{
				txnAmount = Utils.stringToDouble(Utils.decimalFormat.format(Double.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00));
			} 
			else
			{
				txnAmount = Utils.stringToDouble(Utils.decimalFormat.format(Double.valueOf(txnReqRes.getTxnData().getDe004_amount())));
			}
			double updatedClosingBalance;
			if(txnReqRes.getTxnData().getTran_type() != null && txnReqRes.getTxnData().getTran_type().equalsIgnoreCase("DPT"))
			{
				 updatedClosingBalance = Utils.stringToDouble(accountCreation.getStrClosingBalance()) + txnAmount ;
			}
			else
			{
				 updatedClosingBalance = Utils.stringToDouble(accountCreation.getStrClosingBalance()) - txnAmount ;
			}
			accountCreation.setStrClosingBalance(Utils.decimalFormat.format(updatedClosingBalance));
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error During updateAccountBalance");
		}
		return txnReqRes;
	}
	private TxnReqRes updatesCreditLimits(TxnReqRes txnReqRes, AccountCreation accountCreation)
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			double updatedAvailableCrdLimit;
			double txnAmount = 0.00;
			if (txnReqRes.getTxnData().getDe022_pos_entry_mode()!=null && txnReqRes.getTxnData().getDe022_pos_entry_mode().equalsIgnoreCase("810")) 
			{
				txnAmount = Utils.stringToDouble(Utils.decimalFormat.format(Double.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00));
			}
			else
			{
				txnAmount = Utils.stringToDouble(Utils.decimalFormat.format(Double.valueOf(txnReqRes.getTxnData().getDe004_amount())));
			}
			if(txnReqRes.getTxnData().getTran_type() != null && txnReqRes.getTxnData().getTran_type().equalsIgnoreCase("DPT"))
			{
				updatedAvailableCrdLimit = Utils.stringToDouble(accountCreation.getStrAvailableCreditLimit()) + txnAmount;
			}
			else
			{
				updatedAvailableCrdLimit = Utils.stringToDouble(accountCreation.getStrAvailableCreditLimit()) - txnAmount;
			}
			accountCreation.setStrAvailableCreditLimit(Utils.decimalFormat.format(updatedAvailableCrdLimit));
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error During updatesCreditLimits"); 
		}
		return txnReqRes;
	}
	public TxnReqRes addProcess(TxnReqRes txnReqRes, AccountCreation accountCreation)
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			addAccountStatmentEntry(txnReqRes, accountCreation);
			
			if ("C".equalsIgnoreCase(accountCreation.getAccountCategoryType()))
			{
				addCreditCardTxns(txnReqRes, accountCreation);
			}
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error During addProcess");
		}
		return txnReqRes;
	}
	
	private TxnReqRes addAccountStatmentEntry(TxnReqRes txnReqRes, AccountCreation accountCreation) 
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			String txnAmount = null;
			AccountStatement accountStatement = new AccountStatement();
			if (txnReqRes.getTxnData().getDe022_pos_entry_mode()!=null && txnReqRes.getTxnData().getDe022_pos_entry_mode().equalsIgnoreCase("810")) 
			{
				txnAmount = Utils.decimalFormat.format(Double.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00);
			}
			else
			{
				txnAmount = Utils.decimalFormat.format(Double.valueOf(txnReqRes.getTxnData().getDe004_amount()));
			}
			
			double strNewClosingBalance = Utils.stringToDouble(accountCreation.getStrClosingBalance());;
			amsLogger.writeInfoLog("Transaction Type:::["+txnReqRes.getTxnData().getTran_type()+"]");
			if(txnReqRes.getTxnData().getTran_type() != null && txnReqRes.getTxnData().getTran_type().equalsIgnoreCase("DPT"))
			{
				accountStatement.setStrTransactionMode(TransactionType.TransactionMode.CREDIT);
				accountStatement.setStrNaration("Transaction Successfully Credited.");
			}
			else
			{
				 accountStatement.setStrTransactionMode(TransactionType.TransactionMode.DEBIT);
				 accountStatement.setStrNaration("Transaction Successfully Debited.");
			}
			
			accountStatement.setStrParticipantId(accountCreation.getStrParticipantID());
			accountStatement.setStrAccountNumber(accountCreation.getStrAccountNumber());
			accountStatement.setStrAccountType(accountCreation.getStrAccountType());
			
			accountStatement.setStrClosingBalance(Utils.decimalFormat.format(strNewClosingBalance));
			accountStatement.setStrTransactionAmount(txnAmount);
			accountStatement.setStrParticipantId(String.valueOf(txnReqRes.getTxnData().getParticipant_id()));			
			accountStatement.setStrTransactionID(String.valueOf(txnReqRes.getTxnData().getTxn_id()));
			accountStatement.setStrIsGLType("N");
			accountStatement.setStrTransactionType(txnReqRes.getTxnData().getTran_type());
			
			accountStatementService.addAccountTransactionData(accountStatement);			
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error During addAccountStatmentEntry");
		}
		return txnReqRes;
	}
	
	private TxnReqRes addCreditCardTxns(TxnReqRes txnReqRes, AccountCreation accountCreation) 
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			TxnData txnData = txnReqRes.getTxnData();
			if(accountCreation.getStrRevolvingCredit().equalsIgnoreCase("Y"))
			{ 
				RevolvingCreditCardMaster revolvingCreditCardMaster = new RevolvingCreditCardMaster();
				
				String billingYearAndMonthAndDay = Utils.getBillingYearAndMonthAndDay(accountCreation.getStrBillingCycleDate());
				amsLogger.writeInfoLog("billingYearAndMonthAndDay::["+billingYearAndMonthAndDay+"]");
				
				String billingMonthAndDay = billingYearAndMonthAndDay.substring(billingYearAndMonthAndDay.indexOf("-") + 1);
				amsLogger.writeInfoLog("billingMonthAndDay::["+billingMonthAndDay+"]");
				
				revolvingCreditCardMaster.setStrBillingMonthAndDay(billingMonthAndDay);				
				revolvingCreditCardMaster.setStrGracePeriodInDays(accountCreation.getRevolvingGracePeriodInDays());
				
				String remainingGracePeriod = revolvingCreditCardService.getRemainingGracePeriodInDays(revolvingCreditCardMaster);

				RevolvingCreditCardTxnMaster revolvingCreditCardTxnMaster = new RevolvingCreditCardTxnMaster();
				revolvingCreditCardTxnMaster.setStrAccountNumber(accountCreation.getStrAccountNumber());
				revolvingCreditCardTxnMaster.setStrAccountType(accountCreation.getStrAccountType());
				
				if (Utils.stringToDouble(accountCreation.getStrTotalOutstandingBal()) == 0)
				{
					accountCreation.setStrGracePeriodStartDate(Utils.getLocalDate());
					accountCreation.setStrAvailableGracePeriod(accountCreation.getRevolvingGracePeriodInDays());
					
					//Adding Payment Due date Start
					Date paymentDueDate = Utils.getPaymentDueDate(billingYearAndMonthAndDay, Integer.parseInt(accountCreation.getRevolvingGracePeriodInDays()));
					accountCreation.setPayementDueDate(paymentDueDate);
					//Adding Payment Due date End
					
					updategracePeriodDate(txnReqRes, accountCreation);
				}
				
				revolvingCreditCardTxnMaster.setStrRemaningGracePeriod(remainingGracePeriod);				
				revolvingCreditCardTxnMaster.setStrParticipantId(String.valueOf(txnData.getParticipant_id()));
				revolvingCreditCardTxnMaster.setStrMcc(txnData.getDe018_mcc());
				
				//POS Transaction changes
				if (txnData.getDe022_pos_entry_mode().equalsIgnoreCase("810")) 
				{
					revolvingCreditCardTxnMaster.setStrTxnAmount(Utils.decimalFormat.format(Double.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00));
				} 
				else
				{
					revolvingCreditCardTxnMaster.setStrTxnAmount(Utils.decimalFormat.format(Double.valueOf(txnData.getDe004_amount())));
				}
				
				revolvingCreditCardTxnMaster.setTxnDate(new Date());
				revolvingCreditCardTxnMaster.setTxnTime(Utils.getFormattedCurrentTime());
				revolvingCreditCardTxnMaster.setStrTxnId(txnData.getTxn_id()+"");
				
				revolvingCreditCardTxnService.addAccountTransactionData(revolvingCreditCardTxnMaster);
			} 
			else 
			{ 
				AccountCreditCardTransactionModel accountCreditCardTransactionModel = new AccountCreditCardTransactionModel();
				accountCreditCardTransactionModel.setStrAccountNumber(accountCreation.getStrAccountNumber());
				accountCreditCardTransactionModel.setStrParticipantID(String.valueOf(txnData.getParticipant_id()));
				accountCreditCardTransactionModel.setStrMcc(txnData.getDe018_mcc());
				//accountCreditCardTransactionModel.setStrTransactionAmount(txnData.getDe004_amount());
				if (txnData.getDe022_pos_entry_mode().equalsIgnoreCase("810"))
				{
					accountCreditCardTransactionModel.setStrTransactionAmount(Utils.decimalFormat.format(Double.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00));
				}
				else
				{
					accountCreditCardTransactionModel.setStrTransactionAmount(Utils.decimalFormat.format(Double.valueOf(txnData.getDe004_amount())));
				}
				accountCreditCardTransactionModel.setStrAccountType(accountCreation.getStrAccountType());
				accountCreditCardTransactionModel.setStrTransactionId(txnData.getTxn_id()+"");
				accountCreditCardTransactionModel.setStrIsPaid("N");
				
				accountCreditCardTxnServices.addAccountCreditCardTxn(accountCreditCardTransactionModel);
			}
			
			updateTotalOutstanding(txnReqRes, accountCreation);
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error During addCreditCardTxns");
		}
		return txnReqRes;
	}
	
	public  TxnReqRes updategracePeriodDate(TxnReqRes txnReqRes, AccountCreation accountCreation)
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			accountMasterService.updategracePeriodDate(accountCreation);
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			txnReqRes.setResponse(processResponse);
			return txnReqRes; 
		}
		
		return txnReqRes;
	}
	private TxnReqRes updateTotalOutstanding(TxnReqRes txnReqRes, AccountCreation accountCreation)
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			//double txnAmount = Utils.stringToDouble(txnReqRes.getTxnData().getDe004_amount());
			double txnAmount = 0.00;
			if (txnReqRes.getTxnData().getDe022_pos_entry_mode()!=null && txnReqRes.getTxnData().getDe022_pos_entry_mode().equalsIgnoreCase("810")) 
			{
				txnAmount = Utils.stringToDouble(Utils.decimalFormat.format(Double.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00));
			} 
			else
			{
				txnAmount = Utils.stringToDouble(Utils.decimalFormat.format(Double.valueOf(txnReqRes.getTxnData().getDe004_amount())));
			}
			double strNewOutSatndingCreditLimit;
			if(txnReqRes.getTxnData().getTran_type() != null && txnReqRes.getTxnData().getTran_type().equalsIgnoreCase("DPT"))
			{
				strNewOutSatndingCreditLimit = Utils.stringToDouble(accountCreation.getStrTotalOutstandingBal()) + txnAmount;
			}
			else
			{
				 strNewOutSatndingCreditLimit = Utils.stringToDouble(accountCreation.getStrTotalOutstandingBal()) - txnAmount;
			}
			
			AccountCreation accountCretion = new AccountCreation();
			accountCretion.setStrTotalOutstandingBal(Utils.decimalFormat.format(strNewOutSatndingCreditLimit));
			accountCretion.setStrAccountNumber(accountCreation.getStrAccountNumber());
			
			accountMasterService.updateTotalOutstanding(accountCreation);
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			txnReqRes.setResponse(processResponse);
			return txnReqRes; 
		}
		return txnReqRes;
	}
	private TxnReqRes addLinkedGLDebitAccountEntry(TxnReqRes txnReqRes, TransactionConfig transactionConfig) 
	{
		try 
		{
			transactionConfig = glAccountTxnHandler.decreaseGLAccountBalance(transactionConfig);
			if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
			{
				transactionConfig.setGlTranMode(TransactionType.TransactionMode.DEBIT);
				transactionConfig = glAccountTxnHandler.insertGLAccountDebitEntryInStatement(transactionConfig);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return txnReqRes;
	}
	private TxnReqRes addLinkedGLCreditAccountEntry(TxnReqRes txnReqRes, TransactionConfig transactionConfig) 
	{
		try 
		{
			transactionConfig = glAccountTxnHandler.increaseGLAccountBalance(transactionConfig);
			if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
			{
				transactionConfig.setGlTranMode(TransactionType.TransactionMode.CREDIT);
				transactionConfig = glAccountTxnHandler.insertGLAccountCreditEntryInStatement(transactionConfig);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return txnReqRes;
	}
	private TxnReqRes addThirdPartyGLAccountEntry(TxnReqRes txnReqRes, TransactionConfig transactionConfig) 
	{
		try 
		{
			transactionConfig = glAccountTxnHandler.increaseGLAccountBalance(transactionConfig);			
			if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
			{
				transactionConfig.setGlTranMode(TransactionType.TransactionMode.CREDIT);
				transactionConfig = glAccountTxnHandler.insertGLAccountCreditEntryInStatement(transactionConfig);
				if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
				{
					transactionConfig = glAccountTxnHandler.decreaseGLAccountBalance(transactionConfig);
					if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
					{
						transactionConfig.setGlTranMode(TransactionType.TransactionMode.DEBIT);
						transactionConfig = glAccountTxnHandler.insertGLAccountDebitEntryInStatement(transactionConfig);
					}
				}
			}
			if(!"S0000".equalsIgnoreCase(transactionConfig.getCode()))
			{
				txnReqRes.getResponse().setCode(transactionConfig.getCode());
				txnReqRes.getResponse().setMessage(transactionConfig.getMessage());
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return txnReqRes;
	}
	
	BankDetailsResponse getBankDetailsResponseMappedData(BankDetails bankDetails) 
	{
		BankDetailsResponse bankDetailsResponse = new BankDetailsResponse();
		bankDetailsResponse.setStrBranchName(bankDetails.getStrBranchName());
		bankDetailsResponse.setStrBankLocation(bankDetails.getStrBankAddress());
		bankDetailsResponse.setStrBankName(bankDetails.getStrBankName());
		bankDetailsResponse.setStrIfscCode(bankDetails.getStrIfscCode());
		return bankDetailsResponse;
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
			
			beneficiaryTxnMaster.setStrBeneficiaryAccountName(thirdPartyRequestParam.getBeneficiaryName());
			beneficiaryTxnMaster.setStrBeneficiaryAccountNo(thirdPartyRequestParam.getBeneficiaryBankAccountNo());
			beneficiaryTxnMaster.setStrBeneficiaryBankIfsc(thirdPartyRequestParam.getBeneficiaryBankIFSC());
			beneficiaryTxnMaster.setStrBeneficiaryBankName(thirdPartyRequestParam.getBeneficiaryBankName());
			
			beneficiaryTxnMaster.setStrBeneficiaryVpaId(thirdPartyRequestParam.getBeneficiaryVPAId());
			
			beneficiaryTxnMaster.setStrFromAccountNo(thirdPartyRequestParam.getCustAccountNo());
			beneficiaryTxnMaster.setStrFromAccountType(thirdPartyRequestParam.getCustAccountType());
			beneficiaryTxnMaster.setStrTxnAmount(thirdPartyRequestParam.getTxnAmount()); 
			beneficiaryTxnMaster.setStrTxnType(thirdPartyRequestParam.getTxnType());
			
			beneficiaryTxnMaster.setStrTxnStatus(transactionConfig.getMessage());
			
			beneficiaryTxnMasterService.saveBeneficiaryTxnMasterRecords(beneficiaryTxnMaster);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
}
