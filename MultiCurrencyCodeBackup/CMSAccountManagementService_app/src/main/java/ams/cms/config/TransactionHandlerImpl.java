
package ams.cms.config;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.handler.ExternalTransactionHandler;
import ams.cms.constants.TransactionType;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountCreditCardTransactionModel;
import ams.cms.model.AccountStatement;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.DeviceInfo;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.MultiCurrencyWalletAccountMaster;
import ams.cms.model.RevolvingCreditCardMaster;
import ams.cms.model.RevolvingCreditCardTxnMaster;
import ams.cms.model.TxnData;
import ams.cms.model.TxnReqRes;
import ams.cms.services.AccountCreditCardTxnServices;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountStatementService;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.AccountTypeMasterService;
import ams.cms.services.CurrencyConversionMasterService;
import ams.cms.services.CurrencyMasterService;
import ams.cms.services.GLAccountStatementService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.services.MccWiseInterestService;
import ams.cms.services.MultiCurrencyWalletAccountService;
import ams.cms.services.RevolvingCreditCardMasterService;
import ams.cms.services.RevolvingCreditCardService;
import ams.cms.services.RevolvingCreditCardTxnService;
import ams.cms.services.RevolvingCreditInterestTxnService;
import ams.cms.services.TierAccountMasterService;
import ams.cms.services.TransactionIdService;
import ams.cms.services.TransactionTypeService;
import ams.cms.txn.handler.AccountTxnMasterHandler;
import ams.cms.txn.handler.GLTransactionHandler;
import ams.cms.txn.handler.TransactionValidator;
import ams.cms.util.ProcessResponse;
import ams.cms.util.ProcessWebResponse;
import ams.cms.utility.Utils;

@Component
public class TransactionHandlerImpl implements TransactionHandler
{
	private AMSLogger amsLogger = AMSLogger.getInstance(TransactionHandlerImpl.class);
	
	@Autowired
	AccountMasterService accountMasterService;
	
	@Autowired
	MultiCurrencyWalletAccountService accountService;
	
	@Autowired
	GLAccountTypeMasterService glAccountTypeMasterService;
	
	@Autowired
	CurrencyMasterService currencyMasterService;
	
	@Autowired
	AccountStatementService accountStatementService;
	
	@Autowired
	private TransactionIdService transactionIdService;
	
	@Autowired
	GLAccountStatementService glAccountStatementService;
	
	@Autowired
	CurrencyConversionMasterService conversionMasterService;


	@Autowired 
	AccountTypeMasterService accountTypeMasterService;

	@Autowired 
	AccountTranMasterService accountTranMasterService;

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
	TransactionIdCreationConfigDao transactionIdCreationConfigDao;
	
	@Autowired
	TransactionValidator transactionValidator;
	
	@Autowired
	RevolvingCreditCardService revolvingCreditCardService;
	
	@Autowired
	GLTransactionHandler glTransactionHandler;
	
	@Autowired
	TransactionTypeService transactionTypeService;
	
	@Autowired
	AccountTxnMasterHandler accountTxnMasterHandler;
	
	@Autowired
	ExternalTransactionHandler externalTransactionHandler;
	
	@Autowired
	TierAccountMasterService tierAccountMasterService;
	
	@Override 
	public void getTransactionData(AccountCreation accountCreation) throws Exception { }

	@Override 
	public TxnReqRes processTransaction(TxnReqRes txnReqRes)
	{
		ProcessResponse processResponse = new ProcessResponse();
		processResponse.setCode("S0000");
		processResponse.setStatus("Success");
		txnReqRes.setResponse(processResponse);
		TxnData txnData = null;
		TransactionConfig transactionConfig = new TransactionConfig();
		try 
		{
			txnData = txnReqRes.getTxnData();
			
			String processingCode = txnData.getDe003_processing_code();
			
			String transactionType = transactionTypeService.getTranTypeByProcessinCode(processingCode);
			txnData.setTran_type(transactionType);
			
			String txnId = transactionIdCreationConfigDao.getTransactionId();
			txnData.setTxn_id(Long.parseLong(txnId));
			transactionConfig.setTxnId(txnId);
			transactionConfig.setAccountTranType(transactionType);
			if(txnData.getDe022_pos_entry_mode() == null)
			{
				txnData.setDe022_pos_entry_mode("");
			}
			
			addTranMasterRequestEntry(txnReqRes); //Adding Tran Master Request Entry [POS & SIMULATOR]
			
			txnReqRes.getHeader().setMti(Utils.getResponseMTI(txnReqRes.getHeader().getMti()));
			
			AccountCreation accountCreationObj = new AccountCreation();
			accountCreationObj.setStrAccountNumber(txnData.getAccountNumber());
			accountCreationObj.setStrAccountType(txnData.getAccountType());			
			
			AccountCreation accountCreation = accountMasterService.getSenderAccountInformation(accountCreationObj);
			accountCreation.setStrAccountNumber(txnData.getAccountNumber());
			
			if("Active".equalsIgnoreCase(accountCreation.getStrAccounTypeStatus())) //<------ Account Type Status CHECK POINT
			{
				if("Active".equalsIgnoreCase(accountCreation.getStrStatus()))//<------ Account Status CHECK POINT
				{
					//TransactionConfig transactionConfig = new TransactionConfig();
					transactionConfig.setAccountCreation(accountCreation);				
					transactionConfig = getTransactionConfigInstance(txnReqRes, transactionConfig);
					//added by Sunil Y , for Withdraw added ear_mark_amt , [ Started ]  2023-06-15
					transactionConfig.setStrEarMarkAmount(String.valueOf(accountCreation.getStrEarMarkAmount()));
					//added by Sunil Y , for Withdraw added ear_mark_amt , [ Started ]  2023-06-15
					transactionConfig = transactionValidator.txnValidate(transactionConfig);//<------  Validation Check POINT
					//Added by Pankaj P [start]
					if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
					{
						if(transactionConfig.getAccountTranType() != null && transactionConfig.getAccountTranType().equalsIgnoreCase("DPT"))
						{
							//added by Sunil Y , for Withdraw added ear_mark_amt , [ Started ]  2023-06-15
							transactionConfig.setStrPreCredAmount(String.valueOf(accountCreation.getStrPreCredAmount()));
							//added by Sunil Y , for Withdraw added ear_mark_amt , [ Started ]  2023-06-15
							transactionConfig = transactionValidator.validateCummulativeBalanceLimit(transactionConfig);
						}
					}
					//Added by Pankaj P [End]
					if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
					{
						txnReqRes = updatesAccountMaster(txnReqRes, accountCreation);
						//Added by Pankaj [Start]
						if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
						{
							if(transactionConfig.getAccountTranType() != null && !transactionConfig.getAccountTranType().equalsIgnoreCase("DPT")) 
							{
								if (!"C".equalsIgnoreCase(accountCreation.getAccountCategoryType()))//For Handling Credit Account Issue on 09-Aug-2023 
								{
									transactionConfig = externalTransactionHandler.updateLimitValues(transactionConfig);
									tierAccountMasterService.updateCummAvailableBalance(transactionConfig.getTierAccountMaster());
								}
							}
						}
						//Added by Pankaj [End]
						if ("S0000".equalsIgnoreCase(txnReqRes.getResponse().getCode()))
						{
							txnReqRes = addProcess(txnReqRes, accountCreation);
							if ("S0000".equalsIgnoreCase(txnReqRes.getResponse().getCode()))
							{
								String authCode = Utils.getAlphaNumericString();
								txnData.setDe038_auth_code(authCode);
								
								transactionConfig.setAuthCode(authCode);
								txnData.setDe007_transaction_date(Utils.simpleDateTimeFormat.format(Utils.getCurrentDate()));
								
								DeviceInfo deviceInfo = new DeviceInfo();
								deviceInfo.setTerminalType("POS");
								deviceInfo.setKeyExchangeType("D");
								deviceInfo.setIsTidRiskEnable("N");
								deviceInfo.setIsMidRiskEnable("Y");
								
								txnReqRes.setDeviceInfo(deviceInfo);
								
								//Creating GL Entry Start
								GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
								glAccountTypeMaster.setTranId(String.valueOf(txnData.getTxn_id()));
								
								glAccountTypeMaster.setUserAccountType(accountCreation.getStrGLAccountType()); //Added Linked GL Account Type
								glAccountTypeMaster.setStrGLAccountType("VGL");
								
								glAccountTypeMaster.setTranType(transactionType);
								glAccountTypeMaster.setStrGLAccountDescription(TransactionType.LOAD_BAL);
								
								transactionConfig.setGlAccountTypeMaster(glAccountTypeMaster);	
								
								glTransactionHandler.addAndUpdateGLAccount(transactionConfig);
								//Creating GL Entry End
								
								processResponse.setCode("S0000");
								processResponse.setStatus("Success");
								processResponse.setMessage("Transaction SucessFull.");
								processResponse.setDesc("Transaction SucessFull.");
								txnReqRes.setResponse(processResponse);
								txnData.setDe039_response("00");								
							}
						}
					}
					else
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage(transactionConfig.getMessage());
						processResponse.setDesc(transactionConfig.getMessage());
						txnReqRes.setResponse(processResponse);
					}
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Account Deactivated.");
					processResponse.setDesc("Account Deactivated.");
					txnReqRes.setResponse(processResponse);
				}
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Account Type Deactivated.");
				processResponse.setDesc("Account Type Deactivated.");
				txnReqRes.setResponse(processResponse);
			}
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			txnReqRes.setResponse(processResponse);
		}
		
		if (txnData!=null && !"S0000".equalsIgnoreCase(txnReqRes.getResponse().getCode())) 
		{
			txnData.setDe039_response("06"); //For Error
		}
		//addTranMasterResponseEntry(txnReqRes);
		transactionConfig.setResponseCode(txnData.getDe039_response());
		accountTxnMasterHandler.updateAccountTranMastersColumn(transactionConfig);
		
		return txnReqRes;
	}
	
	public TxnReqRes validateTxnLimits(TxnReqRes txnReqRes, AccountCreation accountCreation)
	{
		TxnData txnData = txnReqRes.getTxnData();
		String amount = null;
		if (txnReqRes.getTxnData().getDe022_pos_entry_mode()!=null && txnReqRes.getTxnData().getDe022_pos_entry_mode().equalsIgnoreCase("810")) 
		{
			DecimalFormat df = new DecimalFormat("0.00");
			amount = df.format(Long.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00);
		} 
		else
		{
			amount = txnData.getDe004_amount();
		}
		
		ProcessResponse processResponse = txnReqRes.getResponse();		
		double amnt = Utils.stringToDouble(amount);
		if( Utils.stringToDouble(accountCreation.getStrAvailableDailyLimit()) < amnt )
		{
			processResponse.setMessage("Daily Limit Amount Breached.");
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			return txnReqRes; 
		} 
		if(Utils.stringToDouble(accountCreation.getStrAvailableMonthlyLimit()) < amnt )
		{
			processResponse.setMessage("Monthly Limit Amount Breached."); 
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			return txnReqRes;
		}
		if(Utils.stringToDouble(accountCreation.getStrAvailableYearlyLimit()) < amnt )
		{
			processResponse.setMessage("Yearly Limit Amount Breached.");
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			return txnReqRes; 
		}
		return txnReqRes; 
	}

	@Override
	public TxnReqRes addCreditCardTxns(TxnReqRes txnReqRes, AccountCreation accountCreation) 
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			TxnData txnData = txnReqRes.getTxnData();
			//if(accountCreation.getStrRevolvingCredit().equalsIgnoreCase("Y"))
			String strIsRevolvingCredit = accountCreation.getStrIsRevolvingCredit();
			amsLogger.writeInfoLog("strIsRevolvingCredit::["+strIsRevolvingCredit+"]");
			
			if(strIsRevolvingCredit != null && strIsRevolvingCredit.equalsIgnoreCase("Y"))
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
					//remainingGracePeriod = revolvingCreditCardMasterService.getGracePeriod(accountCreation.getStrAccountType());
					accountCreation.setStrGracePeriodStartDate(Utils.getLocalDate());
					//accountCreation.setStrAvailableGracePeriod(remainingGracePeriod);
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
				
				//revolvingCreditCardTxnMaster.setStrTxnAmount(txnData.getDe004_amount());
				//POS Transaction changes
				if (txnData.getDe022_pos_entry_mode().equalsIgnoreCase("810")) {
					DecimalFormat df = new DecimalFormat("0.00");
					revolvingCreditCardTxnMaster.setStrTxnAmount(
							df.format(Long.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00));
				} else {
					revolvingCreditCardTxnMaster.setStrTxnAmount(txnData.getDe004_amount());
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
					DecimalFormat df = new DecimalFormat("0.00");
					accountCreditCardTransactionModel.setStrTransactionAmount(df.format(Long.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00));
				}
				else
				{
					accountCreditCardTransactionModel.setStrTransactionAmount(txnData.getDe004_amount());
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
			processResponse.setMessage("Internal Server Error During addCreditCardTxns-");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return txnReqRes;
	}

	@Override
	public TxnReqRes updateAccountBalance(TxnReqRes txnReqRes, AccountCreation accountCreation)
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
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return txnReqRes;
	}

	@Override
	public TxnReqRes addAccountStatmentEntry(TxnReqRes txnReqRes, AccountCreation accountCreation) 
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			AccountStatement accountStatement = new AccountStatement();
			String txnAmount = null;
			if (txnReqRes.getTxnData().getDe022_pos_entry_mode()!=null && txnReqRes.getTxnData().getDe022_pos_entry_mode().equalsIgnoreCase("810")) 
			{
				txnAmount = Utils.decimalFormat.format(Double.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00);
			}
			else
			{
				txnAmount = Utils.decimalFormat.format(Double.valueOf(txnReqRes.getTxnData().getDe004_amount()));
			}
			//double strNewClosingBalance = 0d;
			double strNewClosingBalance = Utils.stringToDouble(accountCreation.getStrClosingBalance());;
			amsLogger.writeInfoLog("Transaction Type:::["+txnReqRes.getTxnData().getTran_type()+"]");
			if(txnReqRes.getTxnData().getTran_type() != null && txnReqRes.getTxnData().getTran_type().equalsIgnoreCase("DPT"))
			{
				//strNewClosingBalance = Utils.stringToDouble(accountCreation.getStrClosingBalance()) + Utils.stringToDouble(txnAmount);
				accountStatement.setStrTransactionMode(TransactionType.TransactionMode.CREDIT);
				accountStatement.setStrNaration("Transaction Successfully Credited.");
			}
			else
			{
				// strNewClosingBalance = Utils.stringToDouble(accountCreation.getStrClosingBalance()) - Utils.stringToDouble(txnAmount);
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
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return txnReqRes;
	}

	@Override
	public void addTranMasterRequestEntry(TxnReqRes txnReqRes)
	{
		try
		{
			TxnData txnData = txnReqRes.getTxnData();
			
			AccountTranMaster accountTranMaster = new AccountTranMaster();
			accountTranMaster.setStrTxn_id(txnData.getTxn_id()+"");
			accountTranMaster.setStrAccountNumber(txnData.getAccountNumber());
			if(txnData.getDe022_pos_entry_mode().equalsIgnoreCase("810")) 
			{
				accountTranMaster.setStrTransaction_amount(Utils.decimalFormat.format(Double.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00));
			}
			else
			{
				accountTranMaster.setStrTransaction_amount(Utils.decimalFormat.format(Double.valueOf(txnData.getDe004_amount())));
			}
			accountTranMaster.setSwitchTxDate(new Date());
			accountTranMaster.setStrParticipantId(String.valueOf(txnData.getParticipant_id()));
			accountTranMaster.setStrMcc(txnData.getDe018_mcc());
			accountTranMaster.setStrMcc(txnData.getDe018_mcc());
			accountTranMaster.setStrMti(txnReqRes.getHeader().getMti());
			
			accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());			
			accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
			
			accountTranMaster.setStrRRN(txnData.getDe037_rrn());
			accountTranMaster.setStrTID(txnData.getDe041_terminal_id());
			accountTranMaster.setStrStan(txnData.getDe011_stan());
			accountTranMaster.setStrMid(txnData.getDe042_mid());
			accountTranMaster.setStrTran_type(txnData.getTran_type());
			
			accountTranMasterService.addAccountTransactionData(accountTranMaster);
		}
		catch(Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void addTranMasterResponseEntry(TxnReqRes txnReqRes)
	{
		try
		{
			TxnData txnData = txnReqRes.getTxnData();
			
			AccountTranMaster accountTranMaster = new AccountTranMaster();
			accountTranMaster.setStrTxn_id(txnData.getTxn_id()+"");
			accountTranMaster.setStrAccountNumber(txnData.getAccountNumber());
			//accountTranMaster.setStrTransaction_amount(txnData.getDe004_amount());
			if(txnData.getDe022_pos_entry_mode().equalsIgnoreCase("810")) 
			{
				DecimalFormat df = new DecimalFormat("0.00");
				accountTranMaster.setStrTransaction_amount(df.format(Long.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00));
			}
			else
			{
				accountTranMaster.setStrTransaction_amount(txnData.getDe004_amount());
			}
			accountTranMaster.setSwitchTxDate(new Date());
			accountTranMaster.setStrMcc(txnData.getDe018_mcc());
			accountTranMaster.setStrParticipantId(txnData.getParticipant_id()+"");
			accountTranMaster.setStrMti(txnReqRes.getHeader().getMti());
			
			accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());			
			accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
			
			accountTranMaster.setStrRRN(txnData.getDe037_rrn());
			accountTranMaster.setStrTID(txnData.getDe041_terminal_id());
			accountTranMaster.setStrStan(txnData.getDe011_stan());
			accountTranMaster.setStrMid(txnData.getDe042_mid());
			accountTranMaster.setStrAuthCode(txnData.getDe038_auth_code());
			accountTranMaster.setStrTran_type(txnData.getTran_type());

			accountTranMaster.setStrResponseCode(txnReqRes.getTxnData().getDe039_response());
			accountTranMasterService.addAccountTransactionData(accountTranMaster);
		}
		catch(Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public TxnReqRes validateBalance(TxnReqRes txnReqRes, AccountCreation accountCreation)
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			TxnData txnData = txnReqRes.getTxnData();
			
			//double txnAmount = Utils.stringToDouble(txnData.getDe004_amount());
			double txnAmount = 0.00;
			if (txnReqRes.getTxnData().getDe022_pos_entry_mode()!=null && txnReqRes.getTxnData().getDe022_pos_entry_mode().equalsIgnoreCase("810")) 
			{
				DecimalFormat df = new DecimalFormat("0.00");
				txnAmount = Utils.stringToDouble(df.format(Long.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00));
			} 
			else
			{
				txnAmount = Utils.stringToDouble(txnReqRes.getTxnData().getDe004_amount());
			}
			double availableBalance = Utils.stringToDouble(accountCreation.getStrClosingBalance());
			
			if( availableBalance < txnAmount)
			{
				processResponse.setMessage("Insufficient Balance.");
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				txnReqRes.setResponse(processResponse);
			}
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			txnReqRes.setResponse(processResponse);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			return txnReqRes; 
		}
		return txnReqRes;
	}

	@Override
	public TxnReqRes validateCreditLimits(TxnReqRes txnReqRes, AccountCreation accountCreation) 
	{
		TxnData txnData = txnReqRes.getTxnData();
		//double txnAmount = Utils.stringToDouble(txnData.getDe004_amount());
		double txnAmount = 0.00;
		if (txnReqRes.getTxnData().getDe022_pos_entry_mode()!=null && txnReqRes.getTxnData().getDe022_pos_entry_mode().equalsIgnoreCase("810")) 
		{
			DecimalFormat df = new DecimalFormat("0.00");
			txnAmount = Utils.stringToDouble(df.format(Long.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00));
		}
		else
		{
			txnAmount = Utils.stringToDouble(txnReqRes.getTxnData().getDe004_amount());
		}
		
		ProcessResponse processResponse = txnReqRes.getResponse();
		double strAblCreditLimit = Utils.stringToDouble(accountCreation.getStrAvailableCreditLimit());
		
		if(strAblCreditLimit < txnAmount) 
		{
			processResponse.setMessage("Available Credit Limit balance Amount Exceeded."); 
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			txnReqRes.setResponse(processResponse);
		}
		return txnReqRes;
	}

	@Override
	public TxnReqRes validationProcess(TxnReqRes txnReqRes, AccountCreation accountCreation)
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			//txnReqRes = validateTxnLimits(txnReqRes, accountCreation);
			//Added By Pankaj Start
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
			{
				TransactionConfig transactionConfig = new TransactionConfig();	
				transactionConfig = externalTransactionHandler.getTierInfoByCustID(accountCreation);				
				transactionConfig = externalTransactionHandler.validateDailyTxnTierLimits(transactionConfig);
				if (!"S0000".equalsIgnoreCase(transactionConfig.getCode())) 
				{
					txnReqRes.getResponse().setMessage(transactionConfig.getMessage());
					return txnReqRes;
				}
			}
			else
			{
				txnReqRes = validateTxnLimits(txnReqRes, accountCreation);		
			}
			//Added By Pankaj End
			if (!"S0000".equalsIgnoreCase(txnReqRes.getResponse().getCode())) 
			{
				return txnReqRes;
			}
			
			if ("C".equalsIgnoreCase(accountCreation.getAccountCategoryType()))
			{
				txnReqRes = validateCreditLimits(txnReqRes, accountCreation);
			}
			else 
			{
				txnReqRes = validateBalance(txnReqRes, accountCreation);
			}			
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error During Validation");
			txnReqRes.setResponse(processResponse);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			return txnReqRes; 
		}
		return txnReqRes;
	}

	@Override
	public TxnReqRes updatesAccountMaster(TxnReqRes txnReqRes, AccountCreation accountCreation)
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			//Added changes For nigeria based Start
			if(!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				updatesTxnLimits(txnReqRes, accountCreation);	
			}
			//Added changes For nigeria based End
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

	@Override
	public TxnReqRes updatesTxnLimits(TxnReqRes txnReqRes, AccountCreation accountCreation)
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
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return txnReqRes;
	}

	@Override
	public TxnReqRes updatesCreditLimits(TxnReqRes txnReqRes, AccountCreation accountCreation)
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			double updatedAvailableCrdLimit;
			//double txnAmount = Utils.stringToDouble(txnReqRes.getTxnData().getDe004_amount());
			double txnAmount = 0.00;
			if (txnReqRes.getTxnData().getDe022_pos_entry_mode()!=null && txnReqRes.getTxnData().getDe022_pos_entry_mode().equalsIgnoreCase("810")) 
			{
				DecimalFormat df = new DecimalFormat("0.00");
				txnAmount = Utils.stringToDouble(df.format(Long.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00));
			}
			else
			{
				txnAmount = Utils.stringToDouble(txnReqRes.getTxnData().getDe004_amount());
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
			
			//accountMasterService.updateCreditLimitValues(accountCreation);
		}
		catch(Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error During updatesCreditLimits"); 
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return txnReqRes;
	}

	@Override
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
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
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
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			return txnReqRes; 
		}
		
		return txnReqRes;
	}
	
	public TxnReqRes updateTotalOutstanding(TxnReqRes txnReqRes, AccountCreation accountCreation)
	{
		ProcessResponse processResponse = txnReqRes.getResponse();
		try
		{
			//double txnAmount = Utils.stringToDouble(txnReqRes.getTxnData().getDe004_amount());
			double txnAmount = 0.00;
			if (txnReqRes.getTxnData().getDe022_pos_entry_mode()!=null && txnReqRes.getTxnData().getDe022_pos_entry_mode().equalsIgnoreCase("810")) 
			{
				DecimalFormat df = new DecimalFormat("0.00");
				txnAmount = Utils.stringToDouble(df.format(Long.valueOf(txnReqRes.getTxnData().getDe004_amount()) / 100.00));
			} 
			else
			{
				txnAmount = Utils.stringToDouble(txnReqRes.getTxnData().getDe004_amount());
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
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			return txnReqRes; 
		}
		return txnReqRes;
	}
	@Override
	public TxnReqRes viewAccountMasterDetails(TxnData txnData) {
		// TODO Auto-generated method stub
		return null;
	}
	//Added by Sunny Soni for getting TransactionConfig instance Start
	private TransactionConfig getTransactionConfigInstance(TxnReqRes txnReqRes, TransactionConfig transactionConfig) 
	{
		TxnData txnData = txnReqRes.getTxnData();
		transactionConfig.setAccountTranType(txnData.getTran_type().trim());
		transactionConfig.setTxnId(String.valueOf(txnData.getTxn_id()));
		
		//POS Transaction Changes Start
		if (txnData.getDe022_pos_entry_mode().equalsIgnoreCase("810")) 
		{
			transactionConfig.setTxnAmount(Utils.decimalFormat.format(Double.valueOf(txnData.getDe004_amount()) / 100.00));
		} 
		else
		{
			transactionConfig.setTxnAmount(Utils.decimalFormat.format(Double.valueOf(txnData.getDe004_amount())));
		}
		//POS Transaction Changes End
		
		transactionConfig.setParticipantId(String.valueOf(txnData.getParticipant_id()));
		transactionConfig.setGlTranType(txnData.getTran_type().trim());
		
		transactionConfig.setMcc(txnData.getDe018_mcc());
		transactionConfig.setStrMti(txnReqRes.getHeader().getMti());		
		transactionConfig.setRrn(txnData.getDe037_rrn());
		transactionConfig.setTid(txnData.getDe041_terminal_id());
		transactionConfig.setStan(txnData.getDe011_stan());
		transactionConfig.setMid(txnData.getDe042_mid());
		transactionConfig.setProcessingCode(txnData.getDe003_processing_code());
		
		return transactionConfig;
	}
	//Added by Sunny Soni for getting TransactionConfig instance End
	
	@Override
	public MultiCurrencyWalletAccountMaster updateClosingBalance(ProcessWebResponse processWebResponse) {
		double updatedBalance = processWebResponse.getMultiCurrencyWalletAccountMaster().getStrClosingBalance() - Utils.stringToDouble(processWebResponse.getTransactionRequest().getStrTxnAmount());
		processWebResponse.getMultiCurrencyWalletAccountMaster().setStrClosingBalance(updatedBalance);
		int i = accountService.updateClosingBalance(processWebResponse.getMultiCurrencyWalletAccountMaster());
		processWebResponse.getCurrencyAccountMaster().setStrClosingBalance(updatedBalance);;
		return processWebResponse.getCurrencyAccountMaster();
	}
	
	@Override
	public MultiCurrencyWalletAccountMaster updateClosingBalanceFee(ProcessWebResponse processWebResponse) {
		double updatedBalance = processWebResponse.getMultiCurrencyWalletAccountMaster().getStrClosingBalance() - processWebResponse.getFeeAmount();
		processWebResponse.getMultiCurrencyWalletAccountMaster().setStrClosingBalance(updatedBalance);
		int i = accountService.updateClosingBalance(processWebResponse.getMultiCurrencyWalletAccountMaster());
		processWebResponse.getCurrencyAccountMasterFee().setStrClosingBalance(updatedBalance);
		return processWebResponse.getCurrencyAccountMasterFee();
	}
	
	@Override
	public MultiCurrencyWalletAccountMaster updateClosingBalanceGst(ProcessWebResponse processWebResponse) {
		double updatedBalance = processWebResponse.getMultiCurrencyWalletAccountMaster().getStrClosingBalance() - processWebResponse.getGstAmount();
		processWebResponse.getMultiCurrencyWalletAccountMaster().setStrClosingBalance(updatedBalance);
		int i = accountService.updateClosingBalance(processWebResponse.getMultiCurrencyWalletAccountMaster());
		processWebResponse.getCurrencyAccountMasterGst().setStrClosingBalance(updatedBalance);
		return processWebResponse.getCurrencyAccountMasterGst();
	}
	
	@Override
	public int updateClosingBalanceWithSameAmount(ProcessWebResponse processWebResponse) {
		double updatedBalance = processWebResponse.getMultiCurrencyWalletAccountMaster().getStrClosingBalance() - processWebResponse.getMultiCurrencyWalletAccountMaster().getStrClosingBalance();
		processWebResponse.getMultiCurrencyWalletAccountMaster().setStrClosingBalance(updatedBalance);
		int i = accountService.updateClosingBalance(processWebResponse.getMultiCurrencyWalletAccountMaster());
		return i;
	}

	@Override
	public GLAccountTypeMaster updateGlClosingBalance(ProcessWebResponse processWebResponse) {
		int i = 0;
		GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
		try
		{
			glAccountTypeMaster.setStrGLAccountType(processWebResponse.getCurrencyMaster().getGlAccountType());
			glAccountTypeMaster.setStrAccountNumber(processWebResponse.getCurrencyMaster().getGlAccountNumber());
			glAccountTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(glAccountTypeMaster);
			double glCurrencyGlBal = Utils.stringToDouble(glAccountTypeMaster.getStrClosingBalance()) + Utils.stringToDouble(processWebResponse.getTransactionRequest().getStrTxnAmount());
			glAccountTypeMaster.setStrClosingBalance(glCurrencyGlBal+"");
			i = glAccountTypeMasterService.updateGLAccountTypeDetails(glAccountTypeMaster);
			System.out.println("updateGlClosingBalance::"+i);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return glAccountTypeMaster;
	}

	@Override
	public GLAccountTypeMaster updateFeeGlClosingBalance(ProcessWebResponse processWebResponse) {
		int i = 0;
		GLAccountTypeMaster feeGLupdate = new GLAccountTypeMaster();
		try
		{
			feeGLupdate.setStrGLAccountType(processWebResponse.getCurrencyMaster().getFeeGl());
			feeGLupdate.setStrAccountNumber(processWebResponse.getCurrencyMaster().getFeeGLAccountNumber());
			feeGLupdate = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(feeGLupdate);
			
			double feeClosingBal = Utils.stringToDouble(feeGLupdate.getStrClosingBalance()) + processWebResponse.getFeeAmount();
			feeGLupdate.setStrClosingBalance(feeClosingBal+"");
			i = glAccountTypeMasterService.updateGLAccountTypeDetails(feeGLupdate);
			System.out.println("updateFeeGlClosingBalance::"+i);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return feeGLupdate;
	}

	@Override
	public GLAccountTypeMaster updateGstGlClosingBalance(ProcessWebResponse processWebResponse) {
		int i = 0;
		GLAccountTypeMaster gstGLupdate = new GLAccountTypeMaster();
		try
		{
			gstGLupdate.setStrGLAccountType(processWebResponse.getCurrencyMaster().getGstGL());
			gstGLupdate.setStrAccountNumber(processWebResponse.getCurrencyMaster().getGstAccountNumber());
			gstGLupdate = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(gstGLupdate);
			double gstClosingBal = processWebResponse.getGstAmount() + Utils.stringToDouble(gstGLupdate.getStrClosingBalance());
			gstGLupdate.setStrClosingBalance(gstClosingBal+"");
			i = glAccountTypeMasterService.updateGLAccountTypeDetails(gstGLupdate);
			System.out.println("updateGstGlClosingBalance::"+i);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return gstGLupdate;
	}

	@Override
	public AccountStatement addAccountTransactionData(ProcessWebResponse processWebResponse) throws Exception {
		AccountStatement accountStatement = new AccountStatement();
		accountStatement.setStrParticipantId(processWebResponse.getStrParticipantId());
		accountStatement.setStrAccountNumber(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyWalletAccountNumber());
		accountStatement.setStrAccountType(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrAccountType());
		accountStatement.setStrClosingBalance(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrClosingBalance()+"");
		accountStatement.setStrTransactionAmount(processWebResponse.getRemaingAmount()+"");
		accountStatement.setStrNaration("Transaction Successful.");
		accountStatement.setStrTransactionMode("Credit");                  
		accountStatement.setStrIsGLType("N");
		accountStatement.setStrTransactionType(processWebResponse.getTransactionRequest().getStrTxnType());;
		accountStatement.setStrTransactionID(processWebResponse.getTxnId());
		accountStatement.setCurrencyCode(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
		accountStatement = accountStatementService.addAccountTransactionData(accountStatement);
		return accountStatement;
	}
	
	@Override
	public void addBatchEntryAccountStatment(ProcessWebResponse processWebResponse) throws Exception{
		List<AccountStatement> accountStatementList = new ArrayList<AccountStatement>();
		
		AccountStatement accountStatement = new AccountStatement();
		accountStatement.setStrParticipantId(processWebResponse.getStrParticipantId());
		accountStatement.setStrAccountNumber(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyWalletAccountNumber());
		accountStatement.setStrAccountType(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrAccountType());
		accountStatement.setStrClosingBalance(processWebResponse.getCurrencyAccountMaster().getStrClosingBalance()+"");
		accountStatement.setStrTransactionAmount(processWebResponse.getTransactionRequest().getStrTxnAmount());
		accountStatement.setStrNaration("Transaction Successful.");
		accountStatement.setStrTransactionMode("Debit");
		accountStatement.setStrTransactionType(processWebResponse.getTxnType());
		accountStatement.setStrIsGLType("N");
		accountStatement.setStrTransactionID(processWebResponse.getTxnId());
		accountStatement.setCurrencyCode(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
		accountStatement.setStrTransactionType(processWebResponse.getTransactionRequest().getStrTxnType());
		accountStatementList.add(accountStatement);
		
		AccountStatement feeAccountStatement = new AccountStatement();
		feeAccountStatement.setStrParticipantId(processWebResponse.getStrParticipantId());
		feeAccountStatement.setStrAccountNumber(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyWalletAccountNumber());
		feeAccountStatement.setStrAccountType(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrAccountType());
		feeAccountStatement.setStrClosingBalance(processWebResponse.getCurrencyAccountMasterFee().getStrClosingBalance()+"");
		feeAccountStatement.setStrTransactionAmount(processWebResponse.getFeeAmount()+"");
		feeAccountStatement.setStrNaration("Transaction Successful.");
		feeAccountStatement.setStrTransactionMode("Debit");
		feeAccountStatement.setStrIsGLType("N");
		feeAccountStatement.setStrTransactionID(processWebResponse.getTxnId());
		feeAccountStatement.setCurrencyCode(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
		feeAccountStatement.setStrTransactionType(processWebResponse.getTransactionRequest().getStrTxnType());
		accountStatementList.add(feeAccountStatement);
		
		AccountStatement gstAccountStatement = new AccountStatement();
		gstAccountStatement.setStrParticipantId(processWebResponse.getStrParticipantId());
		gstAccountStatement.setStrAccountNumber(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyWalletAccountNumber());
		gstAccountStatement.setStrAccountType(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrAccountType());
		gstAccountStatement.setStrClosingBalance(processWebResponse.getCurrencyAccountMasterGst().getStrClosingBalance()+"");
		gstAccountStatement.setStrTransactionAmount(processWebResponse.getGstAmount()+"");
		gstAccountStatement.setStrNaration("Transaction Successful.");
		gstAccountStatement.setStrTransactionMode("Debit");
		gstAccountStatement.setStrIsGLType("N");
		gstAccountStatement.setStrTransactionID(processWebResponse.getTxnId());
		gstAccountStatement.setCurrencyCode(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
		gstAccountStatement.setStrTransactionType(processWebResponse.getTransactionRequest().getStrTxnType());
		accountStatementList.add(gstAccountStatement);
		
		accountStatementService.addBatchEntryAccountTransactionData(accountStatementList);
	}
	
	@Override
	public void addGLStatements(ProcessWebResponse processWebResponse) {
		List<GLAccountStatement> glAccountStatementsList = new ArrayList<GLAccountStatement>();
		try
		{
		GLAccountStatement gLAccountStatement = new GLAccountStatement();
		gLAccountStatement.setStrGLAccountType(processWebResponse.getCurrencyMaster().getGlAccountType());
		gLAccountStatement.setStrRef("");		
		gLAccountStatement.setStrAccountNumber(processWebResponse.getCurrencyMaster().getGlAccountNumber());		
		gLAccountStatement.setStrTxnId(processWebResponse.getTxnId());		
		gLAccountStatement.setTransactionDate(Utils.getCurrentDate());
		gLAccountStatement.setStrTranType(processWebResponse.getTransactionRequest().getStrTxnType());
		gLAccountStatement.setStrTranMode("Credit");
		Double txnAmountInDouble = Double.parseDouble(processWebResponse.getTransactionRequest().getStrTxnAmount());
		gLAccountStatement.setStrAmount(Utils.decimalFormat.format(txnAmountInDouble));
		gLAccountStatement.setStrClosingBalance(processWebResponse.getCurrencyGl().getStrClosingBalance());
		gLAccountStatement.setCurrencyCode(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
		glAccountStatementsList.add(gLAccountStatement);
		
		GLAccountStatement feeGLAccountStatement = new GLAccountStatement();
		feeGLAccountStatement.setStrGLAccountType(processWebResponse.getCurrencyMaster().getFeeGlAccountType());
		feeGLAccountStatement.setStrRef("");		
		feeGLAccountStatement.setStrTranType(processWebResponse.getTransactionRequest().getStrTxnType());
		feeGLAccountStatement.setStrAccountNumber(processWebResponse.getCurrencyMaster().getFeeGLAccountNumber());	
		feeGLAccountStatement.setStrTranMode("Credit");
		feeGLAccountStatement.setStrTxnId(processWebResponse.getTxnId());		
		feeGLAccountStatement.setTransactionDate(Utils.getCurrentDate());
		feeGLAccountStatement.setStrAmount(processWebResponse.getFeeAmount()+"");
		feeGLAccountStatement.setStrClosingBalance(processWebResponse.getFeeCurrencyGl().getStrClosingBalance());
		feeGLAccountStatement.setCurrencyCode(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
		glAccountStatementsList.add(feeGLAccountStatement);
		
		GLAccountStatement gstGLAccountStatement = new GLAccountStatement();
		gstGLAccountStatement.setStrGLAccountType(processWebResponse.getCurrencyMaster().getGstType());
		gstGLAccountStatement.setStrRef("");		
		gstGLAccountStatement.setStrTranType(processWebResponse.getTransactionRequest().getStrTxnType());
		gstGLAccountStatement.setStrAccountNumber(processWebResponse.getCurrencyMaster().getGstAccountNumber());
		gstGLAccountStatement.setStrTranMode("Credit");	
		gstGLAccountStatement.setStrTxnId(processWebResponse.getTxnId());		
		gstGLAccountStatement.setTransactionDate(Utils.getCurrentDate());
		gstGLAccountStatement.setStrAmount(processWebResponse.getGstAmount()+"");
		gstGLAccountStatement.setStrClosingBalance(processWebResponse.getGstCurrencyGl().getStrClosingBalance());
		gstGLAccountStatement.setCurrencyCode(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
		glAccountStatementsList.add(gstGLAccountStatement);
		if("Y".equalsIgnoreCase(processWebResponse.getAccountCreation().getIsChannel()))
		{
		/*	GLAccountTypeMaster channelGLCredit = new GLAccountTypeMaster();
			channelGLCredit.setStrGLAccountType(processWebResponse.getCurrencyMaster().getChannelGlAccountType());
			channelGLCredit.setStrAccountNumber(processWebResponse.getCurrencyMaster().getChannelAccountNumber());
			channelGLCredit = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(channelGLCredit);
			double channelClosingBal =  Double.parseDouble(processWebResponse.getTransactionRequest().getStrTxnAmount()) + Utils.stringToDouble(channelGLCredit.getStrClosingBalance());
			channelGLCredit.setStrClosingBalance(channelClosingBal+"");
			int i = glAccountTypeMasterService.updateGLAccountTypeDetails(channelGLCredit);
			
			GLAccountStatement channelGLAccountStatement = new GLAccountStatement();
			channelGLAccountStatement.setStrGLAccountType(processWebResponse.getCurrencyMaster().getGstGlAccountType());
			channelGLAccountStatement.setStrRef("");		
			channelGLAccountStatement.setStrTranType(processWebResponse.getTxnType());
			channelGLAccountStatement.setStrAccountNumber(processWebResponse.getCurrencyMaster().getGstAccountNumber());		
			channelGLAccountStatement.setStrTxnId(processWebResponse.getTxnId());		
			channelGLAccountStatement.setTransactionDate(Utils.getCurrentDate());
			Double txnAmountInchannel = Double.parseDouble(processWebResponse.getTransactionRequest().getStrTxnAmount());
			channelGLAccountStatement.setStrAmount(Utils.decimalFormat.format(txnAmountInchannel));
			channelGLAccountStatement.setStrClosingBalance(txnAmountInchannel+"");
			channelGLAccountStatement.setCurrencyCode(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
			glAccountStatementsList.add(channelGLAccountStatement); */
		}
		glAccountStatementService.batchEntryOfGLAccountStatement(glAccountStatementsList);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public int addClosingBalanceInWallet(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		int i = accountService.updateClosingBalance(multiCurrencyWalletAccountMaster);
		return i;
	}
	
	
	@Override
	public GLAccountTypeMaster updateLinkedGl(ProcessWebResponse processWebResponse) {
		int i = 0;
		GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
		try
		{
			glAccountTypeMaster.setStrGLAccountType(processWebResponse.getMultiCurrencyWalletAccountMaster().getLinkedGlType());
			glAccountTypeMaster.setStrAccountNumber(processWebResponse.getMultiCurrencyWalletAccountMaster().getLinkedGlNumber());
			glAccountTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(glAccountTypeMaster);
			double glCurrencyGlBal = Utils.stringToDouble(glAccountTypeMaster.getStrClosingBalance()) - processWebResponse.getAmount();
			glAccountTypeMaster.setStrClosingBalance(glCurrencyGlBal+"");
			i = glAccountTypeMasterService.updateGLAccountTypeDetails(glAccountTypeMaster);
			System.out.println("updateGlClosingBalance::"+i);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return glAccountTypeMaster;
	}
	@Override
	public GLAccountStatement addGLAccountStatment(ProcessWebResponse processWebResponse) 
	{
		GLAccountStatement gLAccountStatement = new GLAccountStatement();
		try
		{
		
			gLAccountStatement.setStrGLAccountType(processWebResponse.getMultiCurrencyWalletAccountMaster().getLinkedGlType());
			gLAccountStatement.setStrRef("");
			gLAccountStatement.setStrTranMode("Debit");
			gLAccountStatement.setStrAccountNumber(processWebResponse.getMultiCurrencyWalletAccountMaster().getLinkedGlType());		
			gLAccountStatement.setStrTxnId(processWebResponse.getTxnId());		
			gLAccountStatement.setTransactionDate(Utils.getCurrentDate());
		//	Double txnAmountInDouble = Double.parseDouble(processWebResponse.getTransactionRequest().getStrTxnAmount());
			gLAccountStatement.setStrAmount(processWebResponse.getAmount()+"");
			gLAccountStatement.setStrClosingBalance(processWebResponse.getCurrencyGl().getStrClosingBalance());
			gLAccountStatement.setCurrencyCode(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
			gLAccountStatement.setStrTranType(processWebResponse.getTransactionRequest().getStrTxnType());
			glAccountStatementService.addGLAccountStatement(gLAccountStatement);
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return gLAccountStatement;
	}
	
	@Override
	public MultiCurrencyWalletAccountMaster w2wupdateClosingBalance(ProcessWebResponse processWebResponse) {
		double updatedBalance = processWebResponse.getMultiCurrencyWalletAccountMaster().getStrClosingBalance() - processWebResponse.getTxnAmount();
		processWebResponse.getMultiCurrencyWalletAccountMaster().setStrClosingBalance(updatedBalance);
		int i = accountService.updateClosingBalance(processWebResponse.getMultiCurrencyWalletAccountMaster());
		return processWebResponse.getCurrencyAccountMaster();
	}
	
	@Override
	public MultiCurrencyWalletAccountMaster w2wupdateClosingBalanceFee(ProcessWebResponse processWebResponse) {
		double updatedBalance = processWebResponse.getMultiCurrencyWalletAccountMaster().getStrClosingBalance() - processWebResponse.getFeeAmount();
		processWebResponse.getMultiCurrencyWalletAccountMaster().setStrClosingBalance(updatedBalance);
		int i = accountService.updateClosingBalance(processWebResponse.getMultiCurrencyWalletAccountMaster());
		return processWebResponse.getCurrencyAccountMasterFee();
	}
	
	@Override
	public MultiCurrencyWalletAccountMaster w2wupdateClosingBalanceGst(ProcessWebResponse processWebResponse) {
		double updatedBalance = processWebResponse.getMultiCurrencyWalletAccountMaster().getStrClosingBalance() - processWebResponse.getGstAmount();
		processWebResponse.getMultiCurrencyWalletAccountMaster().setStrClosingBalance(updatedBalance);
		int i = accountService.updateClosingBalance(processWebResponse.getMultiCurrencyWalletAccountMaster());
		return processWebResponse.getCurrencyAccountMasterGst();
	}
	
	@Override
	public void w2waddBatchEntryAccountStatment(ProcessWebResponse processWebResponse) throws Exception{
		List<AccountStatement> accountStatementList = new ArrayList<AccountStatement>();
		
		AccountStatement accountStatement = new AccountStatement();
		accountStatement.setStrParticipantId("");
		accountStatement.setStrAccountNumber(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyWalletAccountNumber());
		accountStatement.setStrAccountType(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrAccountType());
		accountStatement.setStrClosingBalance(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrClosingBalance()+"");
		accountStatement.setStrTransactionAmount(processWebResponse.getTxnAmount()+"");
		accountStatement.setStrNaration("Transaction Successful.");
		accountStatement.setStrTransactionMode("Debit");
		accountStatement.setStrIsGLType("N");
		accountStatement.setStrTransactionID(processWebResponse.getTxnId());
		accountStatement.setCurrencyCode(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
		accountStatementList.add(accountStatement);
		
		AccountStatement feeAccountStatement = new AccountStatement();
		feeAccountStatement.setStrParticipantId("");
		feeAccountStatement.setStrAccountNumber(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyWalletAccountNumber());
		feeAccountStatement.setStrAccountType(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrAccountType());
		feeAccountStatement.setStrClosingBalance(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrClosingBalance()-processWebResponse.getFeeAmount()+"");
		feeAccountStatement.setStrTransactionAmount(processWebResponse.getFeeAmount()+"");
		feeAccountStatement.setStrNaration("Transaction Successful.");
		feeAccountStatement.setStrTransactionMode("Debit");
		feeAccountStatement.setStrIsGLType("N");
		feeAccountStatement.setStrTransactionID(processWebResponse.getTxnId());
		feeAccountStatement.setCurrencyCode(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
		accountStatementList.add(feeAccountStatement);
		
		AccountStatement gstAccountStatement = new AccountStatement();
		gstAccountStatement.setStrParticipantId("");
		gstAccountStatement.setStrAccountNumber(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyWalletAccountNumber());
		gstAccountStatement.setStrAccountType(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrAccountType());
		gstAccountStatement.setStrClosingBalance(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrClosingBalance() - processWebResponse.getGstAmount()+"");
		gstAccountStatement.setStrTransactionAmount(processWebResponse.getGstAmount()+"");
		gstAccountStatement.setStrNaration("Transaction Successful.");
		gstAccountStatement.setStrTransactionMode("Debit");
		gstAccountStatement.setStrIsGLType("N");
		gstAccountStatement.setStrTransactionID(processWebResponse.getTxnId());
		gstAccountStatement.setCurrencyCode(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
		accountStatementList.add(gstAccountStatement);
		
		accountStatementService.addBatchEntryAccountTransactionData(accountStatementList);
	}
	
	@Override
	public void w2wAddGLStatements(ProcessWebResponse processWebResponse) {
		List<GLAccountStatement> glAccountStatementsList = new ArrayList<GLAccountStatement>();
		try
		{
			GLAccountStatement gLAccountStatement = new GLAccountStatement();
			gLAccountStatement.setStrGLAccountType(processWebResponse.getCurrencyMaster().getGlAccountType());
			gLAccountStatement.setStrRef("");		
			gLAccountStatement.setStrAccountNumber(processWebResponse.getCurrencyMaster().getGlAccountNumber());		
			gLAccountStatement.setStrTxnId(processWebResponse.getTxnId());		
			gLAccountStatement.setTransactionDate(Utils.getCurrentDate());
			Double txnAmountInDouble = processWebResponse.getTxnAmount();
			gLAccountStatement.setStrAmount(Utils.decimalFormat.format(txnAmountInDouble));
			gLAccountStatement.setStrClosingBalance(processWebResponse.getCurrencyGl().getStrClosingBalance());
			gLAccountStatement.setCurrencyCode(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
			glAccountStatementsList.add(gLAccountStatement);
			
			GLAccountStatement feeGLAccountStatement = new GLAccountStatement();
			feeGLAccountStatement.setStrGLAccountType(processWebResponse.getCurrencyMaster().getFeeGlAccountType());
			feeGLAccountStatement.setStrRef("");		
			feeGLAccountStatement.setStrAccountNumber(processWebResponse.getCurrencyMaster().getFeeGLAccountNumber());		
			feeGLAccountStatement.setStrTxnId(processWebResponse.getTxnId());		
			feeGLAccountStatement.setTransactionDate(Utils.getCurrentDate());
			feeGLAccountStatement.setStrAmount(processWebResponse.getFeeAmount()+"");
			feeGLAccountStatement.setStrClosingBalance(processWebResponse.getFeeCurrencyGl().getStrClosingBalance());
			feeGLAccountStatement.setCurrencyCode(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
			glAccountStatementsList.add(feeGLAccountStatement);
			
			GLAccountStatement gstGLAccountStatement = new GLAccountStatement();
			gstGLAccountStatement.setStrGLAccountType(processWebResponse.getCurrencyMaster().getGstType());
			gstGLAccountStatement.setStrRef("");		
			gstGLAccountStatement.setStrAccountNumber(processWebResponse.getCurrencyMaster().getGstAccountNumber());		
			gstGLAccountStatement.setStrTxnId(processWebResponse.getTxnId());		
			gstGLAccountStatement.setTransactionDate(Utils.getCurrentDate());
			gstGLAccountStatement.setStrAmount(processWebResponse.getGstAmount()+"");
			gstGLAccountStatement.setStrClosingBalance(processWebResponse.getGstCurrencyGl().getStrClosingBalance());
			gstGLAccountStatement.setCurrencyCode(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
			glAccountStatementsList.add(gstGLAccountStatement);
			
			glAccountStatementService.batchEntryOfGLAccountStatement(glAccountStatementsList);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public AccountStatement w2wAddAccountTransactionData(ProcessWebResponse processWebResponse) throws Exception {
		AccountStatement accountStatement = new AccountStatement();
		accountStatement.setStrParticipantId("");
		accountStatement.setStrAccountNumber(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyWalletAccountNumber());
		accountStatement.setStrAccountType(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrAccountType());
		accountStatement.setStrClosingBalance(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrClosingBalance()+"");
		accountStatement.setStrTransactionAmount(processWebResponse.getWalleToWalletTransfer().getStrTxnAmount()+"");
		accountStatement.setStrNaration("Transaction Successful.");
		accountStatement.setStrTransactionMode("Credit");
		accountStatement.setStrIsGLType("N");
		accountStatement.setStrTransactionID(processWebResponse.getTxnId());
		accountStatement.setCurrencyCode(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
		accountStatement = accountStatementService.addAccountTransactionData(accountStatement);
		return accountStatement;
	}
}
