package ams.cms.handler.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.handler.ExternalTransactionHandler;
import ams.cms.api.model.AccountMaster;
import ams.cms.config.CommonConstants;
import ams.cms.config.TransactionConfig;
import ams.cms.config.TransactionIdCreationConfigDao;
import ams.cms.constants.TransactionType;
import ams.cms.handler.AccountLoadMasterHandler;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountLoadMaster;
import ams.cms.model.AccountStatement;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.TxnData;
import ams.cms.model.TxnHeader;
import ams.cms.model.TxnReqRes;
import ams.cms.notification.EmailTemplate;
import ams.cms.notification.email.EmailService;
import ams.cms.services.AccountLoadMasterService;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountStatementService;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.GLAccountStatementService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.services.TierAccountMasterService;
import ams.cms.txn.handler.AccountTransactionHandler;
import ams.cms.txn.handler.AccountTxnMasterHandler;
import ams.cms.txn.handler.ControlAccountTxnHandler;
import ams.cms.txn.handler.GLAccountTxnHandler;
import ams.cms.txn.handler.GLTransactionHandler;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.Utils;

@Component
public class AccountLoadMasterHandlerImpl implements AccountLoadMasterHandler
{
	private AMSLogger amsLogger = AMSLogger.getInstance(AccountLoadMasterHandlerImpl.class);
	
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
	private TierAccountMasterService tierAccountMasterService;
	
	private Map<String, GLAccountTypeMaster> glAccountTypeMapData = new HashMap<String, GLAccountTypeMaster>();
	
	@Override
	public TxnReqRes processTransactionToLoadBalance(TxnReqRes txnReqRes)
	{
		ProcessResponse processResponse = new ProcessResponse();
		processResponse.setCode("S0000");
		processResponse.setStatus("Success");
		txnReqRes.setResponse(processResponse);
		TransactionConfig transactionConfig = new TransactionConfig();
		try 
		{
			String txnId = transactionIdCreationConfigDao.getTransactionId();
			txnReqRes.getTxnData().setTxn_id(Long.parseLong(txnId));
			transactionConfig.setTxnId(txnId);
			
			if (txnReqRes.getHeader()==null) 
			{
				txnReqRes.setHeader(new TxnHeader());
			}
			if(txnReqRes.getHeader().getMti()!=null && txnReqRes.getHeader().getMti().trim().length() > 0)
			{
				txnReqRes.getHeader().setMti(Utils.getResponseMTI(txnReqRes.getHeader().getMti()));
			}
			
			if ("S0000".equalsIgnoreCase(txnReqRes.getResponse().getCode()))
			{
				txnReqRes = validationProcess(txnReqRes);			
				if ("S0000".equalsIgnoreCase(txnReqRes.getResponse().getCode()))
				{
					txnReqRes = insertionOrUpdationProcess(txnReqRes);
					
					if ("S0000".equalsIgnoreCase(txnReqRes.getResponse().getCode())) 
					{
						String authCode = Utils.getAlphaNumericString();
						TxnData txnData = txnReqRes.getTxnData();
						
						txnData.setDe038_auth_code(authCode);
						txnData.setDe039_response("00");
						
						transactionConfig.setCode("S0000");
						
						transactionConfig.setTxnId(String.valueOf(txnReqRes.getTxnData().getTxn_id()));
						transactionConfig.setTxnAmount(txnReqRes.getTxnData().getDe004_amount());
						transactionConfig.setAccountTranType("DPT");
						transactionConfig.setAccountNo(txnData.getAccountNumber());
						transactionConfig.setAuthCode(authCode);
						transactionConfig.setResponseCode("00");						
					}
				}
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error During processTransactionToLoadBalance");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		
		if (!"S0000".equalsIgnoreCase(txnReqRes.getResponse().getCode())) 
		{
			txnReqRes.getTxnData().setDe039_response("06"); //For Error
		}
		//addTranMasterSingleAccountEntry(transactionConfig);
		transactionConfig.setResponseCode(txnReqRes.getTxnData().getDe039_response());
		accountTxnMasterHandler.updateAccountTranMastersColumn(transactionConfig);
		return txnReqRes;
	}
	
	TxnReqRes validationProcess(TxnReqRes txnReqRes) 
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
					
					TxnData txnData = txnReqRes.getTxnData();					
					AccountMaster accountMaster = new AccountMaster();
					accountMaster.setStrAccountType(txnData.getAccountType());
					accountMaster.setStrAccountNumber(txnData.getAccountNumber());					
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
								transactionConfig.setTxnAmount(txnReqRes.getTxnData().getDe004_amount());
								transactionConfig = externalTransactionHandler.validateCummulativeTierLimit(transactionConfig);	
								
								if (!"S0000".equalsIgnoreCase(transactionConfig.getCode())) 
								{
									processResponse.setCode("E0000");
									processResponse.setStatus("Failed");
									processResponse.setMessage(transactionConfig.getMessage());
								}
							}
							//Added by Pankaj [End]
							if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
							{
								String allowLoadCash = (accountMaster.getStrAllowLoadCash()!=null && accountMaster.getStrAllowLoadCash().trim().length() > 0) ? accountMaster.getStrAllowLoadCash().trim(): "0";
								String loadedCount = (accountMaster.getStrLoadCount()!=null && accountMaster.getStrLoadCount().trim().length() > 0) ? accountMaster.getStrLoadCount().trim(): "0";
								double allowLoadCashVal = Utils.stringToDouble(allowLoadCash);
								double loadedCountTotal = Utils.stringToDouble(loadedCount);
								if (allowLoadCashVal < loadedCountTotal) 
								{
									processResponse.setCode("E0000");
									processResponse.setStatus("Failed");
									processResponse.setMessage("Cash Loading Limit Reached.");
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
									}
									else
									{
										processResponse.setCode("E0000");
										processResponse.setStatus("Failed");
										processResponse.setMessage("Linked GL Account Type Not Found.");
									}
								}
							}
						}
						else
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Account Not Active.");
						}
					}
					else
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Account Not Found.");
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
			processResponse.setMessage("Internal Server Error During Validation");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return txnReqRes;
	}
	
	private TxnReqRes insertionOrUpdationProcess(TxnReqRes txnReqRes) 
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try 
		{
			TransactionConfig transactionConfig = new TransactionConfig();
			transactionConfig.setCode("S0000");
			
			transactionConfig.setTxnId(String.valueOf(txnReqRes.getTxnData().getTxn_id()));
			transactionConfig.setTxnAmount(txnReqRes.getTxnData().getDe004_amount());
			transactionConfig.setAccountTranType("DPT");
			
			GLAccountTypeMaster ctrGLAccountTypeMaster = glAccountTypeMapData.get("CTR".toLowerCase());
			transactionConfig.setAccountNo(ctrGLAccountTypeMaster.getStrAccountNumber());			
			transactionConfig.setGlAccountTypeMaster(ctrGLAccountTypeMaster);
			
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
				
				txnReqRes = addGLAccountEntry(txnReqRes, transactionConfig);// <---- THIRD PARTY GL ENTRY
				if ("S0000".equalsIgnoreCase(txnReqRes.getResponse().getCode())) 
				{
					transactionConfig.setFromAccountNo(thirdPartyGLAccountTypeMaster.getStrAccountNumber());
					transactionConfig.setToAccountNo(txnReqRes.getTxnData().getAccountNumber());
					
					transactionConfig.setResponseCode("00");
					addTranMasterMultipleAccountEntry(transactionConfig); // <---- TRAN_MASTER ENTRY OF MULTIPLE ACCOUNT i.e. FROM AND TO ACCOUNT
					
					txnReqRes = updateAccountBalance(txnReqRes);
					if ("S0000".equalsIgnoreCase(txnReqRes.getResponse().getCode())) 
					{
						txnReqRes = addAccountStatmentEntry(txnReqRes);
						if ("S0000".equalsIgnoreCase(txnReqRes.getResponse().getCode())) 
						{
							txnReqRes = addAccountLoadEntry(txnReqRes);
							if ("S0000".equalsIgnoreCase(txnReqRes.getResponse().getCode())) 
							{
								GLAccountTypeMaster linkedGLAccountTypeMaster = glAccountTypeMapData.get("LINKED_GL".toLowerCase());
								transactionConfig.setGlAccountTypeMaster(linkedGLAccountTypeMaster);
								
								txnReqRes = addLinkedGLAccountEntry(txnReqRes, transactionConfig);
								
								ArrayList<String> emailList = new ArrayList<String>();
								emailList.add(txnReqRes.getTxnData().getAccountHolderEmailId());								
								transactionConfig.setEmailIdList(emailList);
								transactionConfig.setAccountHolderName(txnReqRes.getTxnData().getAccountHolderName());
								sendMailToCustomer(transactionConfig);
							}
						}
					}
				}
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error During Insertion");
		}
		return txnReqRes;
	}
	
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
	
	public TxnReqRes addAccountLoadEntry(TxnReqRes txnReqRes) 
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try 
		{
			TxnData txnData = txnReqRes.getTxnData();
			
			AccountLoadMaster accountLoadMaster = new AccountLoadMaster();
			accountLoadMaster.setStrParticipantId(String.valueOf(txnData.getParticipant_id()));
			accountLoadMaster.setStrAccountType(txnData.getAccountType());
			accountLoadMaster.setStrAccountNumber(txnData.getAccountNumber());
			accountLoadMaster.setStrAccountCategory(txnData.getAccountCategory());
			accountLoadMaster.setStrLoadedBalance(txnData.getDe004_amount());
			accountLoadMaster.setStrChannel(txnData.getChannel());
			accountLoadMaster.setStrTransactionId(String.valueOf(txnReqRes.getTxnData().getTxn_id()));
			accountLoadMaster.setStrTimeOfLoading(Utils.getFormattedCurrentTime());
			
			accountLoadMasterService.saveAccountLoadMasterInfo(accountLoadMaster);
		}
		catch (Exception e) {
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error During Insertion of Account Load");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return txnReqRes;
	}
	
	private TxnReqRes addAccountStatmentEntry(TxnReqRes txnReqRes) 
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			TxnData txnData = txnReqRes.getTxnData();
			String txnAmount = txnData.getDe004_amount();
			
			AccountStatement accountStatement = new AccountStatement();
			accountStatement.setStrParticipantId(String.valueOf(txnData.getParticipant_id()));
			accountStatement.setStrAccountNumber(txnData.getAccountNumber());
			accountStatement.setStrAccountType(txnData.getAccountType());
			
			accountStatement.setStrClosingBalance(Utils.decimalFormat.format(Utils.stringToDouble(txnData.getAvailableBalance())));
			accountStatement.setStrTransactionAmount(txnAmount);
			accountStatement.setStrParticipantId(String.valueOf(txnReqRes.getTxnData().getParticipant_id()));
			accountStatement.setStrNaration("Transaction Successful.");
			accountStatement.setStrTransactionID(String.valueOf(txnReqRes.getTxnData().getTxn_id()));
			accountStatement.setStrTransactionMode("CREDIT");
			accountStatement.setStrTransactionType("Deposit");
			accountStatement.setStrIsGLType("N");
			
			accountStatementService.addAccountTransactionData(accountStatement);
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error During Insertion of Account Statement");
		}
		return txnReqRes;
	}
	
	private TxnReqRes updateAccountBalance(TxnReqRes txnReqRes)
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			TxnData txnData = txnReqRes.getTxnData();
			double txnAmount = Utils.stringToDouble(txnData.getDe004_amount());
			double closingBalance = Utils.stringToDouble(txnData.getAvailableBalance()) + txnAmount ;
			
			double loadCount = Utils.stringToDouble(txnData.getLoadBalanceCount()) + 1;
			
			txnData.setAvailableBalance(Utils.decimalFormat.format(closingBalance));
			txnData.setLoadBalanceCount(String.valueOf(loadCount));
			
			AccountCreation accountCreation = new AccountCreation();
			accountCreation.setStrAccountNumber(txnData.getAccountNumber());
			accountCreation.setStrAccountType(txnData.getAccountType());
			accountCreation.setStrClosingBalance(Utils.decimalFormat.format(closingBalance));
			accountCreation.setStrLoadCount(String.valueOf(loadCount));
			
			int count =	accountMasterService.updateAccountBalance(accountCreation);
			if (count == 0) 
			{
				processResponse.setCode("E0000");
				processResponse.setMessage("Error while updating Account Balance!!");
			}
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error During Updating of Account Master");
		}
		return txnReqRes;
	}
	
	private void addTranMasterResponseEntry(TxnReqRes txnReqRes)
	{
		try
		{
			TxnData txnData = txnReqRes.getTxnData();
			
			AccountTranMaster accountTranMaster = new AccountTranMaster();
			accountTranMaster.setStrTxn_id(txnData.getTxn_id()+"");
			
			accountTranMaster.setStrFrom_account_number(Utils.getGlLoadingAccountNumber(txnData.getChannel()));
			accountTranMaster.setStrTo_account_number(txnData.getAccountNumber());
			
			accountTranMaster.setStrTransaction_amount(txnData.getDe004_amount());
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
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void updateGLAccountType(TxnReqRes txnReqRes) 
	{
		try 
		{
			TxnData txnData = txnReqRes.getTxnData();
			
			GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
			glAccountTypeMaster.setStrAccountNumber(Utils.getGlLoadingAccountNumber(txnData.getChannel()));
			glAccountTypeMaster.setStrGLAccountType(txnData.getChannel());
			
			String glClosingBalance = glAccountTypeMasterService.getClosingBalanceOfGlAccount(glAccountTypeMaster);
			
			Double closingBalance = Utils.stringToDouble(glClosingBalance) - Utils.stringToDouble(txnData.getDe004_amount());
			
			glAccountTypeMaster.setStrClosingBalance(Utils.decimalFormat.format(closingBalance));
			
			glAccountTypeMasterService.updateGLAccountTypeDetails(glAccountTypeMaster);
			
			
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void addGLAccountStatement(TxnReqRes txnReqRes) 
	{
		try 
		{
			TxnData txnData = txnReqRes.getTxnData();
			
			GLAccountStatement glAccountStatement = new GLAccountStatement();
			glAccountStatement.setStrAccountNumber(Utils.getGlLoadingAccountNumber(txnData.getChannel()));
			glAccountStatement.setStrGLAccountType(txnData.getChannel());
			glAccountStatement.setStrAmount(Utils.decimalFormat.format(Double.parseDouble(txnData.getDe004_amount())));
			glAccountStatement.setStrRef("Load Account Balance");
			
			glAccountStatement.setStrTxnId(txnData.getTxn_id()+"");
			glAccountStatement.setTransactionDate(Utils.getCurrentDate());
			glAccountStatement.setStrTranType("WDL");
			glAccountStatement.setStrTranMode("DEBIT");
			
			glAccountStatementService.addGlAccountStatementData(glAccountStatement);			
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	/*
	private TxnReqRes addControlAccountEntry(TxnReqRes txnReqRes) 
	{
		try 
		{
			TransactionConfig transactionConfig = new TransactionConfig();
			transactionConfig.setCode("S0000");
			
			transactionConfig.setTxnId(String.valueOf(txnReqRes.getTxnData().getTxn_id()));
			transactionConfig.setTxnAmount(txnReqRes.getTxnData().getDe004_amount());
			transactionConfig.setGlTranType("TIN");//Transfer IN
			transactionConfig.setGlTranMode(TransactionType.TransactionMode.CREDIT);
			
			GLAccountTypeMaster glAccountTypeMaster = glAccountTypeMapData.get("CTR");
			transactionConfig.setAccountNo(glAccountTypeMaster.getStrAccountNumber());			
			transactionConfig.setAccountTranType("DPT");
			addTranMasterSingleAccountEntry(transactionConfig);
			
			transactionConfig.setGlAccountTypeMaster(glAccountTypeMaster);
			transactionConfig = controlAccountTxnHandler.increaseControlGLBalance(transactionConfig);
			if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
			{
				transactionConfig = controlAccountTxnHandler.insertControlGLCreditEntryInStatement(transactionConfig);
				if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
				{
					transactionConfig.setGlTranType("DPT");//Debit
					transactionConfig.setGlTranMode(TransactionType.TransactionMode.DEBIT);
					transactionConfig = controlAccountTxnHandler.decreaseControlGLBalance(transactionConfig);
					if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
					{
						transactionConfig = controlAccountTxnHandler.insertControlGLDebitEntryInStatement(transactionConfig);
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
	*/
	
	private TxnReqRes addGLAccountEntry(TxnReqRes txnReqRes, TransactionConfig transactionConfig) 
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
			bodyMsg.append("Your account has been credited with transaction amount Rs."+transactionConfig.getTxnAmount()+" bearing transaction ID ");
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
	   	  	emailService.sendSimpleHtmlContentMessage(emailTemplate);	   	  
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
}
