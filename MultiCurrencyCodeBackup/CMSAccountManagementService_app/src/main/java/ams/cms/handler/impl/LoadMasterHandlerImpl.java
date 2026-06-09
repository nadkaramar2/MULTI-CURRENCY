package ams.cms.handler.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.handler.ExternalTransactionHandler;
import ams.cms.api.handler.ThirdPartyTxnHandler;
import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.TierAccountResponse;
import ams.cms.api.service.TransactionPostingIF;
import ams.cms.api.service.impl.TransactionPosting;
import ams.cms.config.AppInfo;
import ams.cms.config.CommonConstants;
import ams.cms.config.TransactionConfig;
import ams.cms.config.TransactionIdCreationConfigDao;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.constants.TransactionType;
import ams.cms.handler.LoadMasterHandler;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountLoadMaster;
import ams.cms.model.AccountStatement;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.BeneficiaryTxnMaster;
import ams.cms.model.FundTransferIn;
import ams.cms.model.FundTransferResponse;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.TierAccountMaster;
import ams.cms.model.TransactionTypeModel;
import ams.cms.model.TxnData;
import ams.cms.model.TxnHeader;
import ams.cms.model.TxnReqRes;
import ams.cms.notification.EmailTemplate;
import ams.cms.notification.email.EmailService;
import ams.cms.services.AccountLoadMasterService;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountStatementService;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.BeneficiaryTxnMasterService;
import ams.cms.services.GLAccountStatementService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.services.TierAccountMasterService;
import ams.cms.services.TransactionTypeService;
import ams.cms.txn.handler.AccountTransactionHandler;
import ams.cms.txn.handler.AccountTxnMasterHandler;
import ams.cms.txn.handler.ControlAccountTxnHandler;
import ams.cms.txn.handler.GLAccountTxnHandler;
import ams.cms.txn.handler.GLTransactionHandler;
import ams.cms.util.FundTransferInConstant;
import ams.cms.util.FundTransferProcessResponse;
import ams.cms.utility.AccountUtility;
import ams.cms.utility.Utils;

@Component
public class LoadMasterHandlerImpl implements LoadMasterHandler
{
	private AMSLogger amsLogger = AMSLogger.getInstance(LoadMasterHandlerImpl.class);
	
	@Autowired
	AccountMasterService accountMasterService;
	
	@Autowired
	TransactionIdCreationConfigDao transactionIdCreationConfigDao;
	
	@Autowired 
	AccountTranMasterService accountTranMasterService;
	
	@Autowired 
	AccountStatementService accountStatementService;
	
	@Autowired
	AccountLoadMasterService accountLoadMasterService;
	
	@Autowired
	GLAccountStatementService glAccountStatementService;
	
	@Autowired
	GLAccountTypeMasterService glAccountTypeMasterService;
	
	@Autowired
	GLTransactionHandler glTransactionHandler;
	
	@Autowired
	ControlAccountTxnHandler controlAccountTxnHandler;
	
	@Autowired
	GLAccountTxnHandler	glAccountTxnHandler;
	
	@Autowired
	AccountTransactionHandler accountTransactionHandler;
	
	@Autowired
	AccountTxnMasterHandler accountTxnMasterHandler;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ExternalTransactionHandler externalTransactionHandler;
	
	@Autowired
	private BeneficiaryTxnMasterService beneficiaryTxnMasterService;
	
	@Autowired
	private TransactionPostingIF transactionPosting;
	
	@Autowired
	private ThirdPartyTxnHandler thirdPartyTxnHandler;
	
	@Autowired
	private	AppInfo appInfo;
	
	private Map<String, GLAccountTypeMaster> glAccountTypeMapData = new HashMap<String, GLAccountTypeMaster>();
	
	@Override
	public FundTransferProcessResponse processTransactionToLoadBalance(TxnReqRes txnReqRes) throws Exception
	{
		FundTransferProcessResponse fundTransferResponse = new FundTransferProcessResponse();
		fundTransferResponse.setCode("S0000");
		fundTransferResponse.setStatus("Success");
		fundTransferResponse.setMessage("Transaction Sucessful.");		
		
		BeneficiaryTxnMaster beneficiaryTxnMaster = new BeneficiaryTxnMaster();
		String authCode = "";
		String txnId = "";
		
		FundTransferIn fundTransferIn = txnReqRes.getFundTransferIn();
		
		txnReqRes.setTxnData(new TxnData());
		txnReqRes.getTxnData().setChannelCode(fundTransferIn.getChannelCode());
		txnReqRes.getTxnData().setParticipant_id(appInfo.getStrParticipantId());
		
		TransactionConfig transactionConfig = new TransactionConfig();
		
		//String tranType = fundTransferIn.getTranType();
		//tranType = (tranType!=null && tranType.trim().length()>0) ? tranType.trim() : "";
		
		//amsLogger.writeInfoLog("processTransactionToLoadBalance::->> tranType=["+tranType+"]");
		amsLogger.writeInfoLog("processTransactionToLoadBalance::->> isReversedTxn=["+fundTransferIn.isReversedTxn()+"]");
		try 
		{
			//if("REV".equalsIgnoreCase(tranType))
			if(fundTransferIn.isReversedTxn())
			{
				String previousTxnId = fundTransferIn.getTranId();
				beneficiaryTxnMaster.setStrTxnId(previousTxnId);
				
				beneficiaryTxnMaster = beneficiaryTxnMasterService.getInfoByTxnID(beneficiaryTxnMaster);
				
				fundTransferIn.setBeneficiaryAccountNumber(beneficiaryTxnMaster.getStrFromAccountNo());
				
				if (beneficiaryTxnMaster.getStrBeneficiaryAccountNo()!=null && beneficiaryTxnMaster.getStrBeneficiaryAccountNo().trim().length() > 0) 
				{
					fundTransferIn.setSenderAccountNumber(beneficiaryTxnMaster.getStrBeneficiaryAccountNo());
				}
				
				fundTransferIn.setAmount(beneficiaryTxnMaster.getStrTxnAmount());
				
				txnId = transactionIdCreationConfigDao.getTransactionId();
				transactionConfig.setTxnId(txnId);
				txnReqRes.getTxnData().setTranId(txnId);
				
				beneficiaryTxnMaster.setStrTxnId(previousTxnId);
				txnReqRes.setBeneficiaryTxnMaster(beneficiaryTxnMaster);
			}
			else
			{
				txnId = transactionIdCreationConfigDao.getTransactionId();
				transactionConfig.setTxnId(txnId);
				txnReqRes.getTxnData().setTranId(txnId);
				
				String systemId = fundTransferIn.getSessionId();
				transactionConfig.setSystemTxnId(systemId);
				
				String srcTxnId = fundTransferIn.getRequestId();
				transactionConfig.setSrcTxnId(srcTxnId);
			}
			fundTransferIn.setTranId(txnId);
			
			if (txnReqRes.getHeader()==null) 
			{
				txnReqRes.setHeader(new TxnHeader());
			}
			if(txnReqRes.getHeader().getMti()!=null && txnReqRes.getHeader().getMti().trim().length() > 0)
			{
				txnReqRes.getHeader().setMti(Utils.getResponseMTI(txnReqRes.getHeader().getMti()));
			}
			//Added by Pankaj Pawar [01-07-2023]
			amsLogger.writeInfoLog("processTransactionToLoadBalance::->>From txnReqRes tranType=["+txnReqRes.getFundTransferIn().getTranType()+"]");
			//if((!("").equalsIgnoreCase(txnReqRes.getFundTransferIn().getTranType()) && (("FNW").equalsIgnoreCase(txnReqRes.getFundTransferIn().getTranType()))))
			if( (txnReqRes.getFundTransferIn().getTranType()!=null && txnReqRes.getFundTransferIn().getTranType().trim().length() > 0 ) && 
					("FNW".equalsIgnoreCase(txnReqRes.getFundTransferIn().getTranType()) || "AM_EXTERNALACCOUNT_TO_WALLET".equalsIgnoreCase(txnReqRes.getFundTransferIn().getTranType())) )
			{
				//txnReqRes.getTxnData().setChannelCode("NLB");
				txnReqRes.getTxnData().setChannelCode(FundTransferInConstant.FNW_GL);
			}
			//Added by Pankaj Pawar [01-07-2023]
			
			if ("S0000".equalsIgnoreCase(fundTransferResponse.getCode()))
			{
				fundTransferResponse = validationProcess(txnReqRes, fundTransferResponse);			
				if ("S0000".equalsIgnoreCase(fundTransferResponse.getCode()))
				{
					fundTransferResponse = insertionOrUpdationProcess(txnReqRes, fundTransferResponse);					
					if ("S0000".equalsIgnoreCase(fundTransferResponse.getCode())) 
					{
						authCode = Utils.getAlphaNumericString();
						TxnData txnData = txnReqRes.getTxnData();
						
						txnData.setDe038_auth_code(authCode);
						txnData.setDe039_response("00");
						
						transactionConfig.setCode("S0000");
						
						transactionConfig.setTxnId(txnId);
						transactionConfig.setTxnAmount(fundTransferIn.getAmount());
						transactionConfig.setAccountTranType("DPT");
						transactionConfig.setAccountNo(fundTransferIn.getBeneficiaryAccountNumber());
						transactionConfig.setAuthCode(authCode);
						transactionConfig.setResponseCode("00");
						
						
						FundTransferResponse fundTransfrResponse = new FundTransferResponse();
						//fundTransfrResponse.setReference(" ");
						fundTransfrResponse.setReference(txnId);
						fundTransfrResponse.setStatus("Success");
						fundTransfrResponse.setInformation("Fund Transfer successfully.");
						
						//if ("FNW".equalsIgnoreCase(tranType)) 
						if (!fundTransferIn.isReversedTxn())
						{
							transactionConfig.setInformation(fundTransfrResponse.getInformation());
						}
						
						fundTransferResponse.setSuccess(true);
						fundTransferResponse.setRequestId(fundTransferIn.getRequestId());
						fundTransferResponse.setData(fundTransfrResponse);
					}
				}
			}
		}
		catch (Exception e) 
		{
			fundTransferResponse.setCode("E0000");
			fundTransferResponse.setStatus("Failed");
			fundTransferResponse.setMessage("Internal Server Error During processTransactionToLoadBalance");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		
		if (!"S0000".equalsIgnoreCase(fundTransferResponse.getCode())) 
		{
			txnReqRes.getTxnData().setDe039_response("06"); //For Error
			//Added for set reason of failed at account tran master table for Montra Start
			//if ("FNW".equalsIgnoreCase(tranType))
			if (!fundTransferIn.isReversedTxn())
			{
				transactionConfig.setInformation(fundTransferResponse.getMessage());
			}
			//Added for set reason of failed at account tran master table for Montra End
		}
		transactionConfig.setResponseCode(txnReqRes.getTxnData().getDe039_response());
		accountTxnMasterHandler.updateAccountTranMastersColumn(transactionConfig);
		addBeneficiaryTxnEntry(txnReqRes);		
		return fundTransferResponse;
	}
	
	private FundTransferProcessResponse validationProcess(TxnReqRes txnReqRes, FundTransferProcessResponse fundTransferResponse) 
	{
		FundTransferIn fundTransferIn = txnReqRes.getFundTransferIn();
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
					
					TxnData txnData = txnReqRes.getTxnData();
					
					AccountMaster accountMaster = new AccountMaster();
					accountMaster.setStrAccountNumber(fundTransferIn.getBeneficiaryAccountNumber());
					
					accountMaster = accountMasterService.getAccountDetails(accountMaster);				
					if (accountMaster!=null && accountMaster.getStrID()!=null)
					{
						if ("Active".equalsIgnoreCase(accountMaster.getStrStatus()))
						{
							//Added by Pankaj [Start]
							if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
							{
								TransactionConfig transactionConfig = new TransactionConfig();
								transactionConfig.setCode("S0000");
								
								AccountCreation accountCreation = new AccountCreation();								
								accountCreation.setStrAccountNumber(accountMaster.getStrAccountNumber());
								accountCreation.setStrClosingBalance(accountMaster.getStrClosingBalance());
								accountCreation.setStrPreCredAmount(accountMaster.getStrPreCredAmount());
								
								transactionConfig = externalTransactionHandler.getTierInfoByCustID(accountCreation);
								
								transactionConfig.setAccountCreation(accountCreation);
								transactionConfig.setTxnAmount(fundTransferIn.getAmount());
								transactionConfig = externalTransactionHandler.validateCummulativeTierLimit(transactionConfig);	
								
								if (!"S0000".equalsIgnoreCase(transactionConfig.getCode())) 
								{
									fundTransferResponse.setCode("E0000");
									fundTransferResponse.setStatus("Failed");
									fundTransferResponse.setMessage(transactionConfig.getMessage());
								}
							}
							//Added by Pankaj [End]
							if ("S0000".equalsIgnoreCase(fundTransferResponse.getCode())) 
							{
								String allowLoadCash = (accountMaster.getStrAllowLoadCash()!=null && accountMaster.getStrAllowLoadCash().trim().length() > 0) ? accountMaster.getStrAllowLoadCash().trim(): "0";
								String loadedCount = (accountMaster.getStrLoadCount()!=null && accountMaster.getStrLoadCount().trim().length() > 0) ? accountMaster.getStrLoadCount().trim(): "0";
								
								double allowLoadCashVal = Utils.stringToDouble(allowLoadCash);
								double loadedCountTotal = Utils.stringToDouble(loadedCount);
								if (allowLoadCashVal < loadedCountTotal) 
								{
									fundTransferResponse.setCode("E0000");
									fundTransferResponse.setStatus("Failed");
									fundTransferResponse.setMessage("Cash Loading Limit Reached.");
								}
								else
								{
									glAccountTypeMaster = new GLAccountTypeMaster();
									glAccountTypeMaster.setStrGLAccountType(accountMaster.getStrGLAccountType());								
									glAccountTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(glAccountTypeMaster);
									if(glAccountTypeMaster!=null && glAccountTypeMaster.getStrID()!=null) 
									{
										glAccountTypeMapData.put("LINKED_GL".toLowerCase(), glAccountTypeMaster);
										
										String closingBalance = (accountMaster.getStrClosingBalance()!=null && accountMaster.getStrClosingBalance().trim().length() > 0) ? accountMaster.getStrClosingBalance().trim() : "0";
										closingBalance = (closingBalance.trim().length()==0) ? "0" : closingBalance;
										
										txnData.setAvailableBalance(closingBalance);
										txnData.setAccountCategory(accountMaster.getStrAccountCategory());
										txnData.setLoadBalanceCount(accountMaster.getStrLoadCount());
										txnData.setLinkedGLAccounType(accountMaster.getStrGLAccountType());
										txnData.setAccountHolderName(accountMaster.getStrAccountHolderName());
										txnData.setAccountHolderEmailId(accountMaster.getStrEmailID());
										txnData.setAccountType(accountMaster.getStrAccountType());
									}
									else
									{
										fundTransferResponse.setCode("E0000");
										fundTransferResponse.setStatus("Failed");
										fundTransferResponse.setMessage("Linked GL Account Type Not Found.");
									}
								}
							}
						}
						else
						{
							fundTransferResponse.setCode("E0000");
							fundTransferResponse.setStatus("Failed");
							fundTransferResponse.setMessage("Account Not Active.");
						}
					}
					else
					{
						fundTransferResponse.setCode("E0000");
						fundTransferResponse.setStatus("Failed");
						fundTransferResponse.setMessage("Account Not Found.");
					}
				}
				else
				{
					fundTransferResponse.setCode("E0000");
					fundTransferResponse.setStatus("Failed");
					fundTransferResponse.setMessage("GL Account Type Not Found.");
				}
			}
			else
			{
				fundTransferResponse.setCode("E0000");
				fundTransferResponse.setStatus("Failed");
				fundTransferResponse.setMessage("Control Account Not Found.");
			}
		}
		catch (Exception e) 
		{
			fundTransferResponse.setCode("E0000");
			fundTransferResponse.setStatus("Failed");
			fundTransferResponse.setMessage("Internal Server Error During Validation");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return fundTransferResponse;
	}
	
	private FundTransferProcessResponse insertionOrUpdationProcess(TxnReqRes txnReqRes, FundTransferProcessResponse fundTransferResponse) 
	{
		FundTransferIn fundTransferIn = txnReqRes.getFundTransferIn();
		try 
		{
			TransactionConfig transactionConfig = new TransactionConfig();
			transactionConfig.setCode("S0000");
			
			transactionConfig.setTxnId(String.valueOf(txnReqRes.getTxnData().getTranId()));
			transactionConfig.setTxnAmount(txnReqRes.getFundTransferIn().getAmount());
			
			transactionConfig.setAccountTranType(fundTransferIn.getTranType());
			
			// Added By Sunil Y , 2023-09-14 , for FeeVat Revesal Part Start 
			if (fundTransferIn.isReversedTxn()) 
			{
				transactionConfig.setReversedTxn(fundTransferIn.isReversedTxn());
				transactionConfig.setSrcTxnId(txnReqRes.getFundTransferIn().getTransactionPostingConfig().getStrSrcTxnId());
				transactionConfig.setParticipantId(txnReqRes.getFundTransferIn().getTransactionPostingConfig().getParticipantId());
			}
			// Added By Sunil Y , 2023-09-14 , for FeeVat Revesal Part End
			
			GLAccountTypeMaster ctrGLAccountTypeMaster = glAccountTypeMapData.get("CTR".toLowerCase());
			transactionConfig.setAccountNo(ctrGLAccountTypeMaster.getStrAccountNumber());			
			transactionConfig.setGlAccountTypeMaster(ctrGLAccountTypeMaster);
			
			// Added By Sunil Y , 2023-09-14 , for FeeVat Reversal Part Start 
			if (fundTransferIn.isReversedTxn()) 
			{
				transactionConfig.setAccountTranType(txnReqRes.getFundTransferIn().getTransactionPostingConfig().getTxnType());
			}
			// Added By Sunil Y , 2023-09-14 , for FeeVat Reversal Part Start
			
			addTranMasterSingleAccountEntry(transactionConfig);	// <---- TRAN_MASTER ENTRY OF SINGLE ACCOUNT
			
			controlAccountTxnHandler.addTransferINControlAccountEntry(transactionConfig); // <---- CONTROL GL ENTRY
			if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
			{
				transactionConfig.setFromAccountNo(ctrGLAccountTypeMaster.getStrAccountNumber());		
				
				GLAccountTypeMaster thirdPartyGLAccountTypeMaster = glAccountTypeMapData.get(txnReqRes.getTxnData().getChannelCode().trim().toLowerCase());
				transactionConfig.setToAccountNo(thirdPartyGLAccountTypeMaster.getStrAccountNumber());
				transactionConfig.setResponseCode("00");
				transactionConfig.setGlAccountTypeMaster(thirdPartyGLAccountTypeMaster);
				
				addTranMasterMultipleAccountEntry(transactionConfig);// <---- TRAN_MASTER ENTRY OF MULTIPLE ACCOUNT i.e. FROM AND TO ACCOUNT	
				
				fundTransferResponse = addGLAccountEntry(txnReqRes, transactionConfig, fundTransferResponse);// <---- THIRD PARTY GL ENTRY
				if ("S0000".equalsIgnoreCase(fundTransferResponse.getCode())) 
				{
					transactionConfig.setFromAccountNo(thirdPartyGLAccountTypeMaster.getStrAccountNumber());
					transactionConfig.setToAccountNo(txnReqRes.getFundTransferIn().getBeneficiaryAccountNumber());
					transactionConfig.setResponseCode("00");
					
					//Added By Sunil Y , 2023-09-14 , for FeeVat Revesal Part Start 
					if (fundTransferIn.isReversedTxn()) 
					{
						transactionConfig.setAccountTranType(txnReqRes.getFundTransferIn().getTransactionPostingConfig().getTxnType());
					}
					//Added By Sunil Y , 2023-09-14 , for FeeVat Revesal Part End
					
					addTranMasterMultipleAccountEntry(transactionConfig); // <---- TRAN_MASTER ENTRY OF MULTIPLE ACCOUNT i.e. FROM AND TO ACCOUNT
					
					//Added By Sunil Y , 2023-09-14 , for FeeVat Revesal Part Start
					if (fundTransferIn.isReversedTxn()) 
					{
						thirdPartyTxnHandler.updateAccountLimits(null, txnReqRes.getFundTransferIn().getTransactionPostingConfig().getFromTierAccountMaster(), txnReqRes.getFundTransferIn().getAmount());
					}
					//Added By Sunil Y , 2023-09-14 , for FeeVat Revesal Part End
					
					fundTransferResponse = updateAccountBalance(txnReqRes, fundTransferResponse);
					if ("S0000".equalsIgnoreCase(fundTransferResponse.getCode())) //Changes done
					{
						fundTransferResponse = addAccountStatmentEntry(txnReqRes, fundTransferResponse);
						if ("S0000".equalsIgnoreCase(fundTransferResponse.getCode())) //Changes done
						{
							fundTransferResponse = addAccountLoadEntry(txnReqRes, fundTransferResponse);
							if ("S0000".equalsIgnoreCase(fundTransferResponse.getCode()))  //Changes done
							{
								GLAccountTypeMaster linkedGLAccountTypeMaster = glAccountTypeMapData.get("LINKED_GL".toLowerCase());
								transactionConfig.setGlAccountTypeMaster(linkedGLAccountTypeMaster);
								
								txnReqRes = addLinkedGLAccountEntry(txnReqRes, transactionConfig);
								
								//Added changes for fee and vat related changes Start
								if (fundTransferIn.isReversedTxn() && fundTransferIn.getIsFeeTypeExist()) 
								{
									fundTransferIn.getTransactionPostingConfig().setTxnId(String.valueOf(txnReqRes.getTxnData().getTranId()));
									feeVatDebitGLEntriesWithAccountEntries(fundTransferIn.getTransactionPostingConfig());
								}
								//Added changes for fee and vat related changes End
								
								if (!fundTransferIn.isReversedTxn() && txnReqRes.getTxnData().getAccountHolderEmailId() != null && txnReqRes.getTxnData().getAccountHolderEmailId().trim().length() > 0) 
								{
									ArrayList<String> emailList = new ArrayList<String>();
									emailList.add(txnReqRes.getTxnData().getAccountHolderEmailId());								
									transactionConfig.setEmailIdList(emailList);
									transactionConfig.setAccountHolderName(txnReqRes.getTxnData().getAccountHolderName());
									//Added by Sagar Khawse for getting participantId of FNW Start
									if (fundTransferIn.getParticipantId()!=null && fundTransferIn.getParticipantId().trim().length() > 0) 
									{
										transactionConfig.setParticipantId(fundTransferIn.getParticipantId().trim());
									}
									//Added by Sagar Khawse for getting participantId of FNW End
									sendMailToCustomer(transactionConfig);
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			fundTransferResponse.setCode("E0000");
			fundTransferResponse.setStatus("Failed");
			fundTransferResponse.setMessage("Internal Server Error During Insertion");
		}
		return fundTransferResponse;
	}
	
	//private void 
	
	public void addTranMasterRequestEntry(TxnReqRes txnReqRes)
	{
		try
		{
			TxnData txnData = txnReqRes.getTxnData();
			AccountTranMaster accountTranMaster = new AccountTranMaster();
			accountTranMaster.setStrTxn_id(txnData.getTxn_id()+"");
			accountTranMaster.setStrAccountNumber(txnData.getAccountNumber());
			accountTranMaster.setStrTransaction_amount(txnData.getDe004_amount());
			accountTranMaster.setSwitchTxDate(new Date());
			accountTranMaster.setStrParticipantId(txnData.getParticipant_id()+"");
			accountTranMaster.setStrMcc(txnData.getDe018_mcc());
			accountTranMaster.setStrParticipantId(txnData.getParticipant_id()+"");
			accountTranMaster.setStrMcc(txnData.getDe018_mcc());
			accountTranMaster.setStrMti(txnReqRes.getHeader().getMti());
			
			accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());			
			accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
			
			accountTranMaster.setStrRRN(txnData.getDe037_rrn());
			accountTranMaster.setStrTID(txnData.getDe041_terminal_id());
			accountTranMaster.setStrStan(txnData.getDe011_stan());
			accountTranMaster.setStrMid(txnData.getDe042_mid());
			
			accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());			
			accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
			
			accountTranMasterService.addAccountTransactionData(accountTranMaster);
		}
		catch(Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	public FundTransferProcessResponse addAccountLoadEntry(TxnReqRes txnReqRes, FundTransferProcessResponse fundTransferResponse) 
	{
		FundTransferIn fundTransferIn = txnReqRes.getFundTransferIn();
		try 
		{
			TxnData txnData = txnReqRes.getTxnData();
			
			AccountLoadMaster accountLoadMaster = new AccountLoadMaster();
			accountLoadMaster.setStrParticipantId(String.valueOf(txnData.getParticipant_id()));
			accountLoadMaster.setStrAccountNumber(fundTransferIn.getSenderAccountNumber());
			accountLoadMaster.setStrLoadedBalance(fundTransferIn.getAmount());
			accountLoadMaster.setStrChannel(txnData.getChannelCode());
			accountLoadMaster.setStrTransactionId(String.valueOf(txnReqRes.getTxnData().getTranId()));
			accountLoadMaster.setStrTimeOfLoading(Utils.getFormattedCurrentTime());
			
			accountLoadMasterService.saveAccountLoadMasterInfo(accountLoadMaster);
		}
		catch (Exception e) {
			fundTransferResponse.setCode("E0000");
			fundTransferResponse.setStatus("Failed");
			fundTransferResponse.setMessage("Internal Server Error During Insertion of Account Load");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return fundTransferResponse;
	}
	
	private FundTransferProcessResponse addAccountStatmentEntry(TxnReqRes txnReqRes, FundTransferProcessResponse fundTransferResponse) 
	{
		FundTransferIn fundTransferIn = txnReqRes.getFundTransferIn();
		try
		{
			TxnData txnData = txnReqRes.getTxnData();
			String txnAmount = fundTransferIn.getAmount();
			
			AccountStatement accountStatement = new AccountStatement();
			accountStatement.setStrParticipantId(String.valueOf(txnData.getParticipant_id()));
			//Added by Sagar Khawse for getting participantId of FNW Start
			if (fundTransferIn.getParticipantId()!=null && fundTransferIn.getParticipantId().trim().length() > 0) 
			{
				accountStatement.setStrParticipantId(fundTransferIn.getParticipantId().trim());
			}
			// Added By Sunil Y , for Revarsal Send Money Start
			if(fundTransferIn.isReversedTxn())
			{
				accountStatement.setStrParticipantId(txnReqRes.getFundTransferIn().getTransactionPostingConfig().getParticipantId());
			}
			// Added By Sunil Y , for Revarsal Send Money End
			
			accountStatement.setStrAccountNumber(fundTransferIn.getBeneficiaryAccountNumber());
			accountStatement.setStrAccountType(txnData.getAccountType());
			
			accountStatement.setStrClosingBalance(Utils.decimalFormat.format(Utils.stringToDouble(txnData.getAvailableBalance())));
			accountStatement.setStrTransactionAmount(txnAmount);			
			
			if((!("").equalsIgnoreCase(txnReqRes.getFundTransferIn().getTranType()) && (("REV").equalsIgnoreCase(txnReqRes.getFundTransferIn().getTranType()))))
			{
				accountStatement.setStrNaration("Transaction Reversed Successfully Against txnId: "+txnReqRes.getBeneficiaryTxnMaster().getStrTxnId());
			}
			else
			{
				accountStatement.setStrNaration("Transaction Successfull.");
			}
			accountStatement.setStrTransactionID(String.valueOf(txnReqRes.getTxnData().getTranId()));
			
			//accountStatement.setStrTransactionType("DPT");
			accountStatement.setStrTransactionType(fundTransferIn.getTranType());
			// Added By Sunil Y , for Revarsal Send Money Start
			if(txnReqRes.getFundTransferIn().isReversedTxn())
			{
				accountStatement.setStrTransactionType(txnReqRes.getFundTransferIn().getTransactionPostingConfig().getTxnType());
			}
			// Added By Sunil Y , for Revarsal Send Money End
			
			accountStatement.setStrTransactionMode(TransactionType.MODE.get("DPT"));
			//accountStatement.setStrIsGLType("N");
			accountStatement.setStrIsGLType("T");
			
			accountStatement.setEntityNumber(fundTransferIn.getSenderAccountNumber());
			accountStatement.setEntityInfo(fundTransferIn.getSenderAccountName());
			
			accountStatementService.addAccountTransactionData(accountStatement);
		}
		catch(Exception e)
		{
			fundTransferResponse.setCode("E0000");
			fundTransferResponse.setStatus("Failed");
			fundTransferResponse.setMessage("Internal Server Error During Insertion of Account Statement");
		}
		return fundTransferResponse;
	}
	
	private FundTransferProcessResponse updateAccountBalance(TxnReqRes txnReqRes, FundTransferProcessResponse fundTrsfrResponse)
	{
		FundTransferIn fundTransferIn = txnReqRes.getFundTransferIn();
		try
		{
			TxnData txnData = txnReqRes.getTxnData();
			double txnAmount = Utils.stringToDouble(fundTransferIn.getAmount());
			double closingBalance = Utils.stringToDouble(txnData.getAvailableBalance()) + txnAmount ;
			
			double loadCount = Utils.stringToDouble(txnData.getLoadBalanceCount()) + 1;
			
			txnData.setAvailableBalance(Utils.decimalFormat.format(closingBalance));
			txnData.setLoadBalanceCount(String.valueOf(loadCount));
			
			AccountCreation accountCreation = new AccountCreation();
			accountCreation.setStrAccountNumber(fundTransferIn.getBeneficiaryAccountNumber());
			accountCreation.setStrClosingBalance(Utils.decimalFormat.format(closingBalance));
			accountCreation.setStrLoadCount(String.valueOf(loadCount));
			accountCreation.setStrAccountType(txnReqRes.getTxnData().getAccountType());
			int count = 0;
			//if( (!("").equalsIgnoreCase(txnReqRes.getFundTransferIn().getTranType())) && ((("FNW").equalsIgnoreCase(txnReqRes.getFundTransferIn().getTranType())))
			//		|| ((("REV").equalsIgnoreCase(txnReqRes.getFundTransferIn().getTranType()))))
			if( (!("").equalsIgnoreCase(txnReqRes.getFundTransferIn().getTranType())) && ((("AM_EXTERNALACCOUNT_TO_WALLET").equalsIgnoreCase(txnReqRes.getFundTransferIn().getTranType()))) || ((("REV").equalsIgnoreCase(txnReqRes.getFundTransferIn().getTranType()))))
			{
				count = accountMasterService.updateAccountBalanceWithoutAccountType(accountCreation);
			}
			else
			{
				 count = accountMasterService.updateAccountBalance(accountCreation);
			}
			if (count == 0) 
			{
				fundTrsfrResponse.setCode("E0000");
				fundTrsfrResponse.setMessage("Error while updating Account Balance!!");
			}
		}
		catch(Exception e)
		{
			fundTrsfrResponse.setCode("E0000");
			fundTrsfrResponse.setStatus("Failed");
			fundTrsfrResponse.setMessage("Internal Server Error During Updating of Account Master");
		}
		return fundTrsfrResponse;
	}
	
	private void addTranMasterResponseEntry(TxnReqRes txnReqRes)
	{
		FundTransferIn fundTransferIn = txnReqRes.getFundTransferIn();
		try
		{
			TxnData txnData = txnReqRes.getTxnData();
			
			AccountTranMaster accountTranMaster = new AccountTranMaster();
			accountTranMaster.setStrTxn_id(txnData.getTranId()+"");
			
			accountTranMaster.setStrFrom_account_number(Utils.getGlLoadingAccountNumber(txnData.getChannel()));
			accountTranMaster.setStrTo_account_number(fundTransferIn.getBeneficiaryAccountNumber());
			
			accountTranMaster.setStrTransaction_amount(fundTransferIn.getAmount());
			accountTranMaster.setSwitchTxDate(new Date());
			accountTranMaster.setStrMcc(txnData.getDe018_mcc());
			
			String participantId = (txnData.getParticipant_id()!=null) ? String.valueOf(txnData.getParticipant_id()): "0";
			
			accountTranMaster.setStrParticipantId(participantId);
			//accountTranMaster.setStrMti(txnReqRes.getHeader().getMti());
			
			accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());			
			accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
			
			accountTranMaster.setStrRRN(txnData.getDe037_rrn());
			accountTranMaster.setStrTID(txnData.getDe041_terminal_id());
			accountTranMaster.setStrStan(txnData.getDe011_stan());
			accountTranMaster.setStrMid(txnData.getDe042_mid());
			
			accountTranMaster.setStrAuthCode(txnData.getDe038_auth_code());
			
			accountTranMaster.setStrResponseCode(txnReqRes.getTxnData().getDe039_response());
			accountTranMasterService.addAccountTransactionData(accountTranMaster);
		}
		catch(Exception e)
		{
			fundTransferIn.setCode("E0000");
			fundTransferIn.setStatus("Failed");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void updateGLAccountType(TxnReqRes txnReqRes) 
	{
		FundTransferIn fundTransferIn = txnReqRes.getFundTransferIn();
		try 
		{
			TxnData txnData = txnReqRes.getTxnData();
			
			GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
			glAccountTypeMaster.setStrAccountNumber(Utils.getGlLoadingAccountNumber(txnData.getChannel()));
		//	glAccountTypeMaster.setStrAccountNumber(Utils.getGlLoadingAccountNumber("NLB"));
			
			glAccountTypeMaster.setStrGLAccountType(txnData.getChannel());
		//	glAccountTypeMaster.setStrGLAccountType("NLB");
			
			String glClosingBalance = glAccountTypeMasterService.getClosingBalanceOfGlAccount(glAccountTypeMaster);
			
			Double closingBalance = Utils.stringToDouble(glClosingBalance) - Utils.stringToDouble(txnData.getDe004_amount());
			
			glAccountTypeMaster.setStrClosingBalance(Utils.decimalFormat.format(closingBalance));
			
			glAccountTypeMasterService.updateGLAccountTypeDetails(glAccountTypeMaster);
			
			
		}
		catch (Exception e) 
		{
			fundTransferIn.setCode("E0000");
			fundTransferIn.setStatus("Failed");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void addGLAccountStatement(TxnReqRes txnReqRes) 
	{
		FundTransferIn fundTransferIn = txnReqRes.getFundTransferIn();
		try 
		{
			TxnData txnData = txnReqRes.getTxnData();
			
			GLAccountStatement glAccountStatement = new GLAccountStatement();
			glAccountStatement.setStrAccountNumber(Utils.getGlLoadingAccountNumber(txnData.getChannel()));
	//		glAccountStatement.setStrAccountNumber(Utils.getGlLoadingAccountNumber("NLB"));
			
			glAccountStatement.setStrGLAccountType(txnData.getChannel());
	//		glAccountStatement.setStrGLAccountType("NLB");
			
			glAccountStatement.setStrAmount(Utils.decimalFormat.format(Double.parseDouble(fundTransferIn.getAmount())));
			glAccountStatement.setStrRef("Load Account Balance");
			
			glAccountStatement.setStrTxnId(txnData.getTranId()+"");
			glAccountStatement.setTransactionDate(Utils.getCurrentDate());
			glAccountStatement.setStrTranType("WDL");
			glAccountStatement.setStrTranMode("DEBIT");
			
			glAccountStatementService.addGlAccountStatementData(glAccountStatement);			
		}
		catch (Exception e) 
		{
			fundTransferIn.setCode("E0000");
			fundTransferIn.setStatus("Failed");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private FundTransferProcessResponse addGLAccountEntry(TxnReqRes txnReqRes, TransactionConfig transactionConfig, FundTransferProcessResponse fundTrfsrProcessResponse) 
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
						//Added by Sunil Y , 2023-09-14 , for Fee vat Reversal Part Start
						if(transactionConfig.isReversedTxn())
						{
							transactionConfig.setAccountTranType(transactionConfig.getAccountTranType());
						}
						//Added by Sunil Y , 2023-09-14 , for Fee vat Reversal Part End						
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
			fundTrfsrProcessResponse.setCode("E0000");
			fundTrfsrProcessResponse.setStatus("Failed");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return fundTrfsrProcessResponse;
	}
	private TxnReqRes addLinkedGLAccountEntry(TxnReqRes txnReqRes, TransactionConfig transactionConfig) 
	{
		try 
		{
			transactionConfig.setGlTranMode(TransactionType.TransactionMode.CREDIT);
			transactionConfig = glAccountTxnHandler.increaseGLAccountBalance(transactionConfig);
			if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
			{
				transactionConfig = glAccountTxnHandler.insertGLAccountCreditEntryInStatement(transactionConfig);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return txnReqRes;
	}
	
	private void addTranMasterSingleAccountEntry(TransactionConfig transactionConfig) 
	{
		try
		{
			accountTxnMasterHandler.insertTranMasterEntryWithSingleAccount(transactionConfig);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void addTranMasterMultipleAccountEntry(TransactionConfig transactionConfig)
	{
		try 
		{
			accountTxnMasterHandler.insertTranMasterEntryWithMultipleAccount(transactionConfig);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void sendMailToCustomer(TransactionConfig transactionConfig) 
	{
		transactionConfig.setCode("S0000");
		try
		{
			StringBuilder bodyMsg = new StringBuilder("Dear ");
			
			bodyMsg.append(transactionConfig.getAccountHolderName());
			bodyMsg.append(",");
			bodyMsg.append("<br/><br/>"); 
			bodyMsg.append("Your account has been credited with transaction amount ");
			
			if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				bodyMsg.append("NGN ");
			}
			else 
			{
				bodyMsg.append("Rs.");
			}
			bodyMsg.append(transactionConfig.getTxnAmount()+" bearing transaction ID ");
			bodyMsg.append(transactionConfig.getTxnId());
			bodyMsg.append(" on ");
			bodyMsg.append(Utils.simpleDateFormat4.format(Utils.getCurrentDate()));
			bodyMsg.append(" at ");
			bodyMsg.append(Utils.simpleTimeFormat1.format(Utils.getCurrentDate()));
			
			//bodyMsg.append( "<br/><br/><br/>");
			//bodyMsg.append( "Powered by AMS Technologies Pvt Ltd");
			transactionConfig.setMailMessage(bodyMsg.toString());
			
			String subject = "Regarding to Transaction";
			
		  
	   	  	EmailTemplate emailTemplate = Utils.getEmailTemplateForSendMail("contactus@AMStechnologies.com", transactionConfig.getEmailIdList().get(0), subject, bodyMsg.toString());
	   	  	emailTemplate.setStrParticipantid(transactionConfig.getParticipantId());
	   	  	//Hard coded for montra mw Start
	   	  	if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
	   	  	{
	   	  		emailTemplate.setStrParticipantid("MON232070000001");
	   	  		if (transactionConfig.getParticipantId()!=null && transactionConfig.getParticipantId().trim().length() > 0) 
	   	  		{
	   	  			emailTemplate.setStrParticipantid(transactionConfig.getParticipantId());
	   	  		}
	   	  	}
	   	  	//Hard coded for montra mw Start
	   	  	
	   	  	emailService.sendSimpleHtmlContentMessage(emailTemplate);	   	  
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private TxnReqRes addResponseData(TxnReqRes txnReqRes)
	{
		FundTransferIn data = new FundTransferIn();
		data.setData((new FundTransferResponse()));
		data.setMessage("Transaction Sucessfull.");
		data.setCode(txnReqRes.getFundTransferIn().getCode());
		if(txnReqRes.getTxnData().getDe038_auth_code() != null && txnReqRes.getTxnData().getDe038_auth_code().trim().length() > 0)
		{
			data.setSuccess(txnReqRes.getFundTransferIn().getSuccess());
		}
		else
		{
			data.setSuccess("failed");
		}
		data.setRequestId(txnReqRes.getFundTransferIn().getRequestId());
		data.getData().setReference("");
		data.getData().setStatus("Success.");
		data.getData().setInformation("Fund Transfer successfully");
		data.getData().setAmsTranId(String.valueOf(txnReqRes.getTxnData().getTranId()));
		data.getData().setAmsAuthCode(txnReqRes.getTxnData().getDe038_auth_code());
		txnReqRes.setFundTransferIn(data);
		return txnReqRes;
	}
	
	private TxnReqRes addBeneficiaryTxnEntry(TxnReqRes txnReqRes) throws Exception
	{
		FundTransferIn fundTransferIn =	txnReqRes.getFundTransferIn();
		
		BeneficiaryTxnMaster beneficiaryTxnMaster = new BeneficiaryTxnMaster();
		beneficiaryTxnMaster.setStrBeneficiaryAccountName(fundTransferIn.getBeneficiaryAccountName());
		
		beneficiaryTxnMaster.setStrBeneficiaryAccountNo(fundTransferIn.getBeneficiaryAccountNumber());
		
		beneficiaryTxnMaster.setStrFromAccountNo(fundTransferIn.getSenderAccountName());
		beneficiaryTxnMaster.setStrFromAccountNo(fundTransferIn.getSenderAccountNumber());
		
		beneficiaryTxnMaster.setStrTxnAmount(fundTransferIn.getAmount());
		beneficiaryTxnMaster.setStrTxnId(fundTransferIn.getTranId());
		
		beneficiaryTxnMaster.setTxnDate(Utils.getCurrentDate());
		beneficiaryTxnMaster.setTxnTime(Utils.getFormattedCurrentTime());
		
		beneficiaryTxnMaster.setStrTxnType(fundTransferIn.getTranType());
		
		if(txnReqRes.getTxnData().getDe038_auth_code() != null && txnReqRes.getTxnData().getDe038_auth_code().trim().length() > 0)
		{
			beneficiaryTxnMaster.setStrTxnStatus("Success");
		}
		else
		{
			beneficiaryTxnMaster.setStrTxnStatus("Failed");
		}
		beneficiaryTxnMasterService.saveBeneficiaryTxnMasterRecords(beneficiaryTxnMaster);
		return txnReqRes;
	}
	
	//Added Fee and Vat Related Entries Changes Start
	private void feeVatDebitGLEntriesWithAccountEntries(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			updateDebitFeeVatGLAccountMaster(transactionPostingConfig);
			
			addFeeVatDebitGLAccountStatementEntry(transactionPostingConfig);
			 
			updateCreditFeeVatAccountMaster(transactionPostingConfig);
			
			addFeeVatReversalEntryInAccountStatement(transactionPostingConfig);
			
			addMultipleTxnTranMasterEntryForFeeVat(transactionPostingConfig);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured while feeVatDebitGLEntries ::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void updateDebitFeeVatGLAccountMaster(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			List<GLAccountTypeMaster> glAccountTypeMasters = new ArrayList<GLAccountTypeMaster>();
			
			GLAccountTypeMaster exitingFeeGLAccountType = transactionPostingConfig.getFeeGLAccount();
			GLAccountTypeMaster getCurrentFeeGlAccountMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(exitingFeeGLAccountType);
			
			Double feeTxnAmount = Utils.stringToDouble(exitingFeeGLAccountType.getFee());			
			String feeStrClosingBalance = getDebitedGLClosingBalanceAmount(feeTxnAmount, getCurrentFeeGlAccountMaster);				
			
			GLAccountTypeMaster feeGLAccountType = new GLAccountTypeMaster();
			feeGLAccountType.setStrAccountNumber(exitingFeeGLAccountType.getStrAccountNumber());
			feeGLAccountType.setStrGLAccountType(exitingFeeGLAccountType.getStrGLAccountType());
			feeGLAccountType.setStrClosingBalance(feeStrClosingBalance);
			glAccountTypeMasters.add(feeGLAccountType);/*-------------------******/
			
			GLAccountTypeMaster exitingVatGLAccountType = transactionPostingConfig.getVatGLAccount();
			GLAccountTypeMaster getCurrentVatGlAccountMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(exitingVatGLAccountType);
			
			Double vatTxnAmount = Utils.stringToDouble(exitingVatGLAccountType.getVat());			
			String vatStrClosingBalance = getDebitedGLClosingBalanceAmount(vatTxnAmount, getCurrentVatGlAccountMaster);
			
			GLAccountTypeMaster vatGLAccountType = new GLAccountTypeMaster();
			vatGLAccountType.setStrAccountNumber(exitingVatGLAccountType.getStrAccountNumber());
			vatGLAccountType.setStrGLAccountType(exitingVatGLAccountType.getStrGLAccountType());			
			vatGLAccountType.setStrClosingBalance(vatStrClosingBalance);
			
			glAccountTypeMasters.add(vatGLAccountType);/*-------------------******/
			
			glAccountTypeMasterService.updatesBatchEntryOfLinkedGLAccount(glAccountTypeMasters);
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private String getDebitedGLClosingBalanceAmount(Double txnAmount, GLAccountTypeMaster glAccountTypeMaster) 
	{
		String glClosingBal = (glAccountTypeMaster.getStrClosingBalance() != null && glAccountTypeMaster.getStrClosingBalance().trim().length() > 0) ? glAccountTypeMaster.getStrClosingBalance().trim(): "0";
		Double glBalanceAmount = Double.parseDouble(glClosingBal);
		
		Double closingBal = glBalanceAmount - txnAmount;
		return Utils.decimalFormat.format(closingBal);
	}
	
	private void addFeeVatDebitGLAccountStatementEntry(TransactionPostingConfig transactionPostingConfig) 
	{
		try
		{
			List<GLAccountStatement> glAccountStatements = new ArrayList<GLAccountStatement>();
			
			GLAccountTypeMaster feeGLAccountType = transactionPostingConfig.getFeeGLAccount();
			GLAccountTypeMaster getCurrentFeeGlAccountMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(feeGLAccountType);
			
			Double feeTxnAmount = Utils.stringToDouble(feeGLAccountType.getFee());			
			String feeStrClosingBalance = getDebitedGLClosingBalanceAmount(feeTxnAmount, getCurrentFeeGlAccountMaster);				
			
			TransactionPostingConfig transactionPostConf = new TransactionPostingConfig();
			transactionPostConf.setTxnId(transactionPostingConfig.getTxnId());
			transactionPostConf.setTxnDate(transactionPostingConfig.getTxnDate());
			transactionPostConf.setTxnType(transactionPostingConfig.getTxnType());
			
			transactionPostConf.setTxnAmount(feeGLAccountType.getFee());
			transactionPostConf.setClosingBalance(feeStrClosingBalance);
			
			GLAccountStatement feeGLStatement = addDebitEntryInGLAccountStatement(transactionPostConf, feeGLAccountType);
			glAccountStatements.add(feeGLStatement);
			
			GLAccountTypeMaster vatGLAccountType = transactionPostingConfig.getVatGLAccount();
			GLAccountTypeMaster getCurrentVatGlAccountMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(vatGLAccountType);
			
			
			Double vatTxnAmount = Utils.stringToDouble(vatGLAccountType.getVat());		
			String vatStrClosingBalance = getDebitedGLClosingBalanceAmount(vatTxnAmount, getCurrentVatGlAccountMaster);				
									
			transactionPostConf.setTxnAmount(vatGLAccountType.getVat());
			transactionPostConf.setClosingBalance(vatStrClosingBalance);
			
			GLAccountStatement vatGLStatement = addDebitEntryInGLAccountStatement(transactionPostConf, vatGLAccountType);
			glAccountStatements.add(vatGLStatement);
			
			glAccountStatementService.batchEntryOfGLAccountStatement(glAccountStatements);
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private GLAccountStatement addDebitEntryInGLAccountStatement(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster glAccountTypeMaster)
	{
		 GLAccountStatement gLStatement = new GLAccountStatement();
		 try 
		 {
			 gLStatement.setStrGLAccountType(glAccountTypeMaster.getStrGLAccountType());
			 gLStatement.setStrAccountNumber(glAccountTypeMaster.getStrAccountNumber());		
			 gLStatement.setStrRef(glAccountTypeMaster.getStrGLAccountDescription());
				
			 gLStatement.setStrTxnId(transactionPostingConfig.getTxnId());		
			 gLStatement.setStrTranType(transactionPostingConfig.getTxnType());		
			 gLStatement.setTransactionDate(transactionPostingConfig.getTxnDate());		
				
			 gLStatement.setStrAmount(transactionPostingConfig.getTxnAmount());
			 gLStatement.setCreatedDate(transactionPostingConfig.getTxnDate());
				
			 gLStatement.setStrClosingBalance(transactionPostingConfig.getClosingBalance());
				
			 gLStatement.setStrTranMode("DEBIT");			
		 }
		 catch (Exception e) 
		 {
			 amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		 }
		 return gLStatement;
	 }
	
	public void updateAndAddFeeVatCreditInAgentAccount(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			
			AccountResponse fromAccountResponse = transactionPostingConfig.getFromAccount();
			
			AccountResponse agentAccountResponse = new AccountResponse();
			agentAccountResponse.setStrAccountNumber(fromAccountResponse.getStrAccountNumber());
			agentAccountResponse.setStrAccountType(fromAccountResponse.getStrAccountType());
			agentAccountResponse.setStrCustId(fromAccountResponse.getStrCustId());
			
			String availableToAccountBalanace = fromAccountResponse.getStrClosingBalance();
			double agentUpdatedClosingBal = Utils.stringToDouble(availableToAccountBalanace) + txnAmount;
			
			GLAccountTypeMaster linkedGLAccountTypeMaster = transactionPostingConfig.getFromLinkGLAccount();
			
			GLAccountTypeMaster linkedGLAccountType = new GLAccountTypeMaster();
			linkedGLAccountType.setStrGLAccountType(linkedGLAccountTypeMaster.getStrGLAccountType());
			linkedGLAccountType.setStrAccountNumber(linkedGLAccountTypeMaster.getStrAccountNumber());
			linkedGLAccountType.setStrGLAccountDescription(linkedGLAccountTypeMaster.getStrGLAccountDescription());
			
			String availableToLinkedGLBalanace = linkedGLAccountTypeMaster.getStrClosingBalance();
			double agentUpdatedLinkedGLBalance = Utils.stringToDouble(availableToLinkedGLBalanace) + txnAmount;
			
			String feeValue = transactionPostingConfig.getFeeGLAccount().getFee();
			
			agentAccountResponse.setStrClosingBalance(Utils.decimalFormat.format(agentUpdatedClosingBal));
			linkedGLAccountType.setStrClosingBalance(Utils.decimalFormat.format(agentUpdatedLinkedGLBalance));
			
			transactionPostingConfig.setTxnAmount(feeValue);
			String isGLTypeVal = "F";
			updateAndAddAgentCreditAccount(transactionPostingConfig, agentAccountResponse, linkedGLAccountType, isGLTypeVal); /* -----1------- */
			
			agentUpdatedClosingBal = agentUpdatedClosingBal + Utils.stringToDouble(feeValue);
			agentUpdatedLinkedGLBalance = agentUpdatedLinkedGLBalance + Utils.stringToDouble(feeValue);			
			
			String vatValue = transactionPostingConfig.getVatGLAccount().getVat();
			
			agentAccountResponse.setStrClosingBalance(Utils.decimalFormat.format(agentUpdatedClosingBal));
			linkedGLAccountType.setStrClosingBalance(Utils.decimalFormat.format(agentUpdatedLinkedGLBalance));
			
			transactionPostingConfig.setTxnAmount(vatValue);
			isGLTypeVal = "V";
			updateAndAddAgentCreditAccount(transactionPostingConfig, agentAccountResponse, linkedGLAccountType, isGLTypeVal);/* -----2------- */
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void updateAndAddAgentCreditAccount(TransactionPostingConfig toAccountTransConfig, AccountResponse agentAccountResponse, GLAccountTypeMaster linkedGLAccountType, String isGLTypeVal) 
	{
		try 
		{
			updateAgentAccountBalance(toAccountTransConfig, agentAccountResponse);
			updateAgentLinkedGLAccountBalance(toAccountTransConfig, linkedGLAccountType);
			
			addCreditAgentAccountStatementEntry(toAccountTransConfig, agentAccountResponse, isGLTypeVal);
			addCreditAgentGLAccountStatementEntry(toAccountTransConfig, linkedGLAccountType);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void updateAgentAccountBalance(TransactionPostingConfig transactionPostingConfig, AccountResponse accountResponse) 
	{
		try 
		{
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			
			AccountMaster accountMaster = new AccountMaster();	
			accountMaster.setStrAccountNumber(accountResponse.getStrAccountNumber());
			accountMaster.setStrCustId(accountResponse.getStrCustId());
			
			String closingBalance = (accountResponse.getStrClosingBalance() != null && accountResponse.getStrClosingBalance().trim().length()>0) ? accountResponse.getStrClosingBalance():"0";
			
			Double closingBal = Double.parseDouble(closingBalance) +  txnAmount;
			String strClosingBalance = Utils.decimalFormat.format(closingBal);
									
			accountMaster.setStrClosingBalance(strClosingBalance);			
			accountMasterService.updateAccountMasterFields(accountMaster);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void updateAgentLinkedGLAccountBalance(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster linkedGLAccountTypeMaster) 
	{
		try 
		{
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			
			String glAccounNumber = linkedGLAccountTypeMaster.getStrAccountNumber();
			glAccounNumber = glAccounNumber.trim();
			
			String closingBal = (linkedGLAccountTypeMaster.getStrClosingBalance() != null && linkedGLAccountTypeMaster.getStrClosingBalance().trim().length()>0) ? linkedGLAccountTypeMaster.getStrClosingBalance():"0";
			
			Double balanceAmount = Double.parseDouble(closingBal);
			
			Double updatedBalance = balanceAmount + txnAmount;
			String updatedClosingBal = Utils.decimalFormat.format(updatedBalance);	
			
			GLAccountTypeMaster linkedGLAccountType = new GLAccountTypeMaster();
			linkedGLAccountType.setStrGLAccountType(linkedGLAccountTypeMaster.getStrGLAccountType());
			linkedGLAccountType.setStrAccountNumber(linkedGLAccountTypeMaster.getStrAccountNumber());
			
			linkedGLAccountType.setStrClosingBalance(updatedClosingBal);
			
			glAccountTypeMasterService.updateGLAccountTypeDetails(linkedGLAccountType);		
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void addCreditAgentAccountStatementEntry(TransactionPostingConfig transactionPostingConfig, AccountResponse accountResponse, String isGLTypeVal) 
	{
		try 
		{
			List<AccountStatement> accountStatements = new ArrayList<AccountStatement>();
			
			String closingBalance = null;
			String strTxnAmount = transactionPostingConfig.getTxnAmount();
			Double accountClosingBal = Utils.stringToDouble(accountResponse.getStrClosingBalance());
			Double txnAmount = Utils.stringToDouble(strTxnAmount);
			
			TransactionPostingConfig transactionPostConf = new TransactionPostingConfig();
			transactionPostConf.setTxnId(transactionPostingConfig.getTxnId());
			transactionPostConf.setTxnDate(transactionPostingConfig.getTxnDate());
			transactionPostConf.setTxnType(transactionPostingConfig.getTxnType());
			
			accountClosingBal = accountClosingBal + txnAmount;
			closingBalance = Utils.decimalFormat.format(accountClosingBal);
			
			transactionPostConf.setTxnAmount(strTxnAmount);
			transactionPostConf.setClosingBalance(closingBalance);
			
			AccountStatement accountStatement = getCreditAccountStatementInstance(transactionPostConf, accountResponse);
			accountStatement.setStrIsGLType(isGLTypeVal);
			accountStatements.add(accountStatement);
			
			accountStatementService.batchEntryOfAccountStatementMaster(accountStatements);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void addCreditAgentGLAccountStatementEntry(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster linkedGLAccountTypeMaster) 
	{
		try 
		{
			List<GLAccountStatement> glAccountStatements = new ArrayList<GLAccountStatement>();
			
			String strTxnAmount = transactionPostingConfig.getTxnAmount();
			
			String glAccounNumber = linkedGLAccountTypeMaster.getStrAccountNumber();
			glAccounNumber = glAccounNumber.trim();
			
			String closingBal = (linkedGLAccountTypeMaster.getStrClosingBalance() != null && linkedGLAccountTypeMaster.getStrClosingBalance().trim().length()>0) ? linkedGLAccountTypeMaster.getStrClosingBalance():"0";
			
			Double accountClosingBal = Utils.stringToDouble(closingBal);
			Double txnAmount = Utils.stringToDouble(strTxnAmount);
			
			TransactionPostingConfig transactionPostConf = new TransactionPostingConfig();
			transactionPostConf.setTxnId(transactionPostingConfig.getTxnId());
			transactionPostConf.setTxnDate(transactionPostingConfig.getTxnDate());
			transactionPostConf.setTxnType(transactionPostingConfig.getTxnType());
			
			accountClosingBal = accountClosingBal + txnAmount;
			String updatedClosingBalance = Utils.decimalFormat.format(accountClosingBal);
			
			transactionPostConf.setTxnAmount(strTxnAmount);
			transactionPostConf.setClosingBalance(updatedClosingBalance);
			
			GLAccountStatement linkedGLStatement = getCreditGLAccountStatementInstance(transactionPostConf, linkedGLAccountTypeMaster);
			glAccountStatements.add(linkedGLStatement);
			
			glAccountStatementService.batchEntryOfGLAccountStatement(glAccountStatements);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private AccountStatement getCreditAccountStatementInstance(TransactionPostingConfig transactionPostingConfig, AccountResponse accountResponse)
	{
		 AccountStatement accountStatement = new AccountStatement();
		 try 
		 {
			accountStatement.setStrTransactionID(transactionPostingConfig.getTxnId());
			accountStatement.setStrTransactionType(transactionPostingConfig.getTxnType());
			accountStatement.setTransactionDate(transactionPostingConfig.getTxnDate());	
			
			accountStatement.setStrTransactionAmount(transactionPostingConfig.getTxnAmount());
			accountStatement.setStrAccountNumber(accountResponse.getStrAccountNumber());
			accountStatement.setStrAccountType(accountResponse.getStrAccountType());
			
			accountStatement.setStrTransactionMode("CREDIT");
			accountStatement.setStrNaration("Amount Credited Successfully.");
			//accountStatement.setStrIsGLType("N");
			
			accountStatement.setStrClosingBalance(transactionPostingConfig.getClosingBalance());
		 }
		 catch (Exception e) 
		 {
			 amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		 }
		return accountStatement;
	}
	
	private GLAccountStatement getCreditGLAccountStatementInstance(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster glAccountTypeMaster) 
	{
		GLAccountStatement linkedGLStatement = new GLAccountStatement();
		try 
		{
			linkedGLStatement.setStrGLAccountType(glAccountTypeMaster.getStrGLAccountType());
			linkedGLStatement.setStrAccountNumber(glAccountTypeMaster.getStrAccountNumber());		
			linkedGLStatement.setStrRef(glAccountTypeMaster.getStrGLAccountDescription());
			
			linkedGLStatement.setStrTxnId(transactionPostingConfig.getTxnId());		
			linkedGLStatement.setStrTranType(transactionPostingConfig.getTxnType());		
			linkedGLStatement.setTransactionDate(transactionPostingConfig.getTxnDate());		
			
			linkedGLStatement.setStrAmount(transactionPostingConfig.getTxnAmount());
			linkedGLStatement.setCreatedDate(transactionPostingConfig.getTxnDate());
			
			linkedGLStatement.setStrClosingBalance(transactionPostingConfig.getClosingBalance());
			
			linkedGLStatement.setStrTranMode("CREDIT");
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return linkedGLStatement;
	}
	//Added Fee and Vat Related Entries Changes End
	
	private void addMultipleTxnTranMasterEntryForFeeVat(TransactionPostingConfig transactionPostingConfig) 
	{
		//Add Txn Details For Fee and vat 
		transactionPostingConfig.setFromAccountNo(transactionPostingConfig.getFeeGLAccount().getStrAccountNumber());			
		transactionPostingConfig.setToAccountNo(transactionPostingConfig.getFromAccount().getStrAccountNumber());
		transactionPostingConfig.setTxnAmount(transactionPostingConfig.getFromAccount().getFee());
		transactionPosting.addAccountTransactionData(transactionPostingConfig); 
		
		transactionPostingConfig.setFromAccountNo(transactionPostingConfig.getVatGLAccount().getStrAccountNumber());			
		transactionPostingConfig.setToAccountNo(transactionPostingConfig.getFromAccount().getStrAccountNumber());
		transactionPostingConfig.setTxnAmount(transactionPostingConfig.getFromAccount().getVat());
		transactionPosting.addAccountTransactionData(transactionPostingConfig);
		
	}

	private void updateCreditFeeVatAccountMaster(TransactionPostingConfig transactionPostingConfig) 
	{
        try 
        {
        	AccountResponse fromAccountResponse = transactionPostingConfig.getFromAccount();
    		
    		AccountMaster accountMaster = new AccountMaster();	
    		accountMaster.setStrAccountNumber(fromAccountResponse.getStrAccountNumber());
    		accountMaster.setStrCustId(fromAccountResponse.getStrCustId());	
    		
    		String closingBal = (fromAccountResponse.getAvailableBalance() != null && fromAccountResponse.getAvailableBalance().trim().length() > 0) ? fromAccountResponse.getAvailableBalance().trim(): "0";
    		Double balanceAmount = Double.parseDouble(closingBal);
    		
    		Double feeBalance = Double.parseDouble(transactionPostingConfig.getFromAccount().getFee());
    		Double vatBalance = Double.parseDouble(transactionPostingConfig.getFromAccount().getVat());
    		Double txnAmt = Double.parseDouble(transactionPostingConfig.getTxnAmount());
    		
    		Double closingBalance = balanceAmount + feeBalance + vatBalance + txnAmt;
    		String strClosingBalance = Utils.decimalFormat.format(closingBalance);			
    		accountMaster.setStrClosingBalance(strClosingBalance);
    		
    		accountMasterService.updateAccountMasterFields(accountMaster);
        }
        catch (Exception e) 
        {
        	amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}			
	}

	private void addFeeVatReversalEntryInAccountStatement(TransactionPostingConfig transactionPostingConfig)  
	{
		try 
		{
			List<AccountStatement> accountStatements = new ArrayList<AccountStatement>();
			
			AccountResponse accountResponse = transactionPostingConfig.getFromAccount();
			String closingBalance = null;
			String strTxnAmount = transactionPostingConfig.getTxnAmount();
			Double accountClosingBal = Utils.stringToDouble(accountResponse.getAvailableBalance());
			Double txnAmount = Utils.stringToDouble(strTxnAmount);
			
			TransactionPostingConfig transactionPostConf = new TransactionPostingConfig();
			transactionPostConf.setParticipantId(transactionPostingConfig.getParticipantId());
			transactionPostConf.setTxnId(transactionPostingConfig.getTxnId());
			transactionPostConf.setTxnDate(transactionPostingConfig.getTxnDate());
			transactionPostConf.setTxnType(transactionPostingConfig.getTxnType());
			
			accountClosingBal = accountClosingBal + txnAmount;
			closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
			
			transactionPostConf.setTxnAmount(strTxnAmount);
			closingBalance = String.valueOf(accountClosingBal);
		
			transactionPostConf.setClosingBalance(closingBalance);
			
			if (accountResponse.getIsFeeApplicable()) 
			{
				strTxnAmount = accountResponse.getFee();
				txnAmount = Utils.stringToDouble(strTxnAmount);
				
				accountClosingBal = accountClosingBal + txnAmount;
				closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
				
				transactionPostConf.setTxnAmount(strTxnAmount);
				transactionPostConf.setClosingBalance(closingBalance);
				
				AccountStatement feeAccountStatement = transactionPosting.getCreditAccountStatementInstance(transactionPostConf, accountResponse);
				feeAccountStatement.setStrIsGLType("F");//F - Fee for Identify fee transaction Amount txn
				accountStatements.add(feeAccountStatement);
				
				strTxnAmount = accountResponse.getVat();
				txnAmount = Utils.stringToDouble(strTxnAmount);
				
				accountClosingBal = accountClosingBal + txnAmount;
				closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
				
				transactionPostConf.setTxnAmount(strTxnAmount);
				transactionPostConf.setClosingBalance(closingBalance);

				AccountStatement vatAccountStatement =  transactionPosting.getCreditAccountStatementInstance(transactionPostConf, accountResponse);
				vatAccountStatement.setStrIsGLType("V");//V - Vat for Identify Vat transaction Amount txn
				accountStatements.add(vatAccountStatement);
			}
			accountStatementService.batchEntryOfAccountStatementMaster(accountStatements);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
}
