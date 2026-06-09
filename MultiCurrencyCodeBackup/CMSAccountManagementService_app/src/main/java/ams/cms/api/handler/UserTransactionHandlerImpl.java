package ams.cms.api.handler;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.DenominationMaster;
import ams.cms.api.model.DenominationValuesMaster;
import ams.cms.api.model.UserTransactionModel;
import ams.cms.api.service.CustomerIdService;
import ams.cms.api.service.DenominationMasterService;
import ams.cms.api.service.DenominationValuesMasterService;
import ams.cms.api.service.PreAccountMasterService;
import ams.cms.config.CommonConstants;
import ams.cms.config.TransactionConfig;
import ams.cms.constants.TransactionType;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountStatement;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.TransactionTypeModel;
import ams.cms.notification.EmailTemplate;
import ams.cms.notification.SmsHandler;
import ams.cms.notification.email.EmailService;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountStatementService;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.GLAccountStatementService;
import ams.cms.services.TransactionTypeService;
import ams.cms.txn.handler.GLTransactionHandler;
import ams.cms.utility.Utils;

@Component
public class UserTransactionHandlerImpl implements UserTransactionHandler
{
	private AMSLogger amsLogger = AMSLogger.getInstance(UserTransactionHandlerImpl.class);
	
	@Autowired
	private AccountMasterService accountMasterService;
	
	@Autowired
	private CustomerIdService customerIdService;
	
	@Autowired
	private PreAccountMasterService preAccountMasterService;
	
	@Autowired
	private SmsHandler smsHandler;
	
	@Autowired
	private DenominationValuesMasterService denominationValuesMasterService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private AccountStatementService accountStatementService;
	
	@Autowired
	private GLAccountStatementService gLAccountStatementService;
	
	@Autowired
	private AccountTranMasterService accountTranMasterService;
	
	@Autowired
	private GLTransactionHandler glTransactionHandler;
	
	@Autowired
	private DenominationMasterService denominationMasterService;
	
	@Autowired
	private TransactionTypeService transactionTypeService;
	
	@Autowired
	private ExternalTransactionHandler externalTransactionHandler;
	
	@Override
	public TransactionConfig validatedTransaction(TransactionConfig transactionConfig) 
	{
		try
		{
			UserTransactionModel userTransactionModel = transactionConfig.getUserTxnReq();
			
			 AccountMaster accountMaster = new AccountMaster();
			 accountMaster.setStrAccountNumber(userTransactionModel.getUserAccountNumber());
			 accountMaster.setStrAccountType(userTransactionModel.getUserAccountType());
			 
			 accountMaster = accountMasterService.getAccountNameByAccountNo(accountMaster);
			 if(accountMaster != null && accountMaster.getStrID().trim().length() > 0 )
			 {
				 userTransactionModel.setAccountStatus(accountMaster.getStrStatus());
				 transactionConfig = checkUserAccountStatus(transactionConfig);
				 if ("S0000".equalsIgnoreCase(transactionConfig.getCode()))
				 {
					 //Set User Information Start
					 setUserInformation(accountMaster, transactionConfig);
					 //Set User Information End
				 }
			 }
			 else
			 {
				 transactionConfig.setCode("E0000");
				transactionConfig.setMessage("User Account is not found");
			 }
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	
	private void setUserInformation(AccountMaster accountMaster, TransactionConfig transactionConfig) 
	{
			try  
			{
				UserTransactionModel userTransactionModel = transactionConfig.getUserTxnReq();
				
				userTransactionModel.setCurrentAvailableBalance(accountMaster.getStrClosingBalance());
				userTransactionModel.setStrMobileNo(accountMaster.getStrMobileNo());
				userTransactionModel.setStrAccountTypeCategory(accountMaster.getStrAccountTypeCategory());
				userTransactionModel.setStrAccountHolderName(accountMaster.getStrAccountHolderName());
				userTransactionModel.setUserAccountType(accountMaster.getStrAccountType());
				userTransactionModel.setStrIsWithdrawAllowAtAgent(accountMaster.getStrIsWithdrawAllowAtAgent());
				userTransactionModel.setStrIsDepositAllowAtAgent(accountMaster.getStrIsDepositAllowAtAgent());
				userTransactionModel.setUserCustId(accountMaster.getStrCustId());
			}
			catch (Exception e) 
			{
				amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			}
	}
	
	@Override
	public TransactionConfig validateTxnAmount(TransactionConfig transactionConfig)
	{
		try
		{
			UserTransactionModel userTransactionModel = transactionConfig.getUserTxnReq();
			//added by sunil Y , checkpoint For Withdraw ear_mark_amt [start] 2023-06-17
			double availableCurrentBalance = Utils.stringToDouble(userTransactionModel.getCurrentAvailableBalance()) - Utils.stringToDouble(transactionConfig.getStrEarMarkAmount()) ;
			//added by sunil Y , checkpoint For Withdraw ear_mark_amt [End] 2023-06-17
			System.out.println("availableCurrentBalance::::["+availableCurrentBalance+"]");
			
			double txnAmount = Utils.stringToDouble(transactionConfig.getUserTxnReq().getUserTxnAmount());
			System.out.println("txnAmount::::"+txnAmount);
			
			if(availableCurrentBalance < txnAmount )
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Insufficient Balance");
			}
			//Added By Pankaj Start
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
			{
				AccountCreation accountCreation = new AccountCreation();
				accountCreation.setStrAccountNumber(transactionConfig.getAccountMasterFromAccount().getStrAccountNumber());
				
				TransactionConfig transactionConfigObj = externalTransactionHandler.getTierInfoByCustID(accountCreation);	
				transactionConfig.setTierAccountMaster(transactionConfigObj.getTierAccountMaster());
				if(transactionConfig.getAccountTranType() != null && !transactionConfig.getAccountTranType().equalsIgnoreCase("DPT"))
				{
					transactionConfig = externalTransactionHandler.validateDailyLimits(transactionConfigObj);
				}
			}
			//Added By Pankaj End
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error.");
		}
		return transactionConfig;
	}
	
	@Override
	public TransactionConfig getAccountHolderName(TransactionConfig transactionConfig) 
	{
		UserTransactionModel userTransactionModel = transactionConfig.getUserTxnReq();
		transactionConfig.setAccountHolderName(userTransactionModel.getStrAccountHolderName());
		return transactionConfig;
	}
	
	private TransactionConfig checkUserAccountStatus(TransactionConfig transactionConfig)
	{
		try
		{
			UserTransactionModel userTransactionModel = transactionConfig.getUserTxnReq();
			if(!"Active".equalsIgnoreCase(userTransactionModel.getAccountStatus()))
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("User Account is not active.");
			}
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error.");
		}
		return transactionConfig;
	}
	@Override
	public TransactionConfig checkUserAccountTypeCategory(TransactionConfig transactionConfig)
	{
		//if(!"P".equalsIgnoreCase(userTransactionModel.getStrAccountTypeCategory()))

		boolean isValidUser = true;
		UserTransactionModel userTransactionModel = transactionConfig.getUserTxnReq();
		if ("WDL".equalsIgnoreCase(transactionConfig.getUserTxnReq().getTranType())) 
		{
			if("N".equalsIgnoreCase(userTransactionModel.getStrIsWithdrawAllowAtAgent()))
			{
				isValidUser = false;
			}
		}
		else if ("DPT".equalsIgnoreCase(transactionConfig.getUserTxnReq().getTranType())) 
		{
			if("N".equalsIgnoreCase(userTransactionModel.getStrIsDepositAllowAtAgent())) 
			{
				isValidUser = false;
			}
		}
		else 
		{
			System.out.println("NO TRANSACTION TYPE FOUND---->>>");
			isValidUser = false;
		}
		
		if (!isValidUser) 
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Transaction not allowed for this Account");
		}		
		return transactionConfig;
	}
	@Override
	public TransactionConfig verifyUserPIN(TransactionConfig transactionConfig) 
	{
		try 
		{
			UserTransactionModel userTransactionModel = transactionConfig.getUserTxnReq();
			
			String custId =	userTransactionModel.getUserCustId();		
			CustomerIdCreation customerIdCreation = new CustomerIdCreation();
			customerIdCreation.setStrCustId(custId);
			String existingPIN = customerIdService.getCustomerPIN(customerIdCreation);
			
			if (existingPIN != null)
			{
				if (!existingPIN.equals(ams.cms.utility.Utils.generateHash(userTransactionModel.getReqPIN()))) 
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage("Entered PIN is Incorrect.");
				}
			}
			else 
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("PIN Not Found for this account.");
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	@Override
	public TransactionConfig sendUserOTP(TransactionConfig transactionConfig)
	{
		try 
		{
			System.out.println("Send User otp METHOD Called.........");
			 String otp = Utils.generateOTP(6);
	    	 System.out.println("INSIDE sendUserOTP::: OTP IS["+otp+"]");
	    	  
	    	 UserTransactionModel userTransactionModel = transactionConfig.getUserTxnReq();
				
	    	 String custId = userTransactionModel.getUserCustId();	
	    	 CustomerIdCreation customerIdCreation = new CustomerIdCreation();
	    	 customerIdCreation.setStrCustId(custId);
	    	 customerIdCreation.setStrTxnOTP(Utils.generateHash(otp));		    	  
	    	  
	    	 int updateCount = customerIdService.updateOtpAgainstCustId(customerIdCreation);
	    	 if (updateCount > 0) 
	    	 {
	    		  	StringBuilder message = new StringBuilder("Dear User, Your one time password is ");
					message.append(otp);
					message.append(". Please enter the OTP to proceed. ");
					message.append("Powered by AMS Technologies Pvt Ltd");
					
					String mobileNo = transactionConfig.getUserTxnReq().getStrMobileNo();					
					String countryCode = preAccountMasterService.getCountryCode(mobileNo);
					
					StringBuilder countryCodeWithMobileNo = new StringBuilder();
					if (countryCode!=null && countryCode.trim().length() > 0)
					{
						countryCodeWithMobileNo.append(countryCode);
					}
					countryCodeWithMobileNo.append(mobileNo);
					
					System.out.println("Mobile No for Sending OTP::["+countryCodeWithMobileNo+"]");
					
					smsHandler.processToSendSmSOTP(otp, countryCodeWithMobileNo.toString(), message.toString());
	    	  }
	    	  else 
	    	  {
	    		  transactionConfig.setCode("E0000");
	    		  transactionConfig.setMessage("OTP updation failed.");
	    	  }
		}
		catch (Exception e) 
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error.");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}

	@Override
	public TransactionConfig validateDenominationNotes(TransactionConfig transactionConfig)
	{
		transactionConfig.setCode("S0000");
		try 
		{
			DenominationMaster denominationMaster = transactionConfig.getDenominationMaster();
			
			Long txnAmount = Long.parseLong(denominationMaster.getStrTxnAmount());
			
			Long totalCalculatedDenominationAmount = getTotalCalculatedDominationAmount(denominationMaster);	
			System.out.println((txnAmount == totalCalculatedDenominationAmount));
			if (!txnAmount.equals(totalCalculatedDenominationAmount)) 
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Denomination Amount not matched with Entered Amount.");
			}
		}
		catch (Exception e) 
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error.");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	
	private Long getTotalCalculatedDominationAmount(DenominationMaster denominationMaster) 
	{
		Long totalCalculatedDenominationAmount = 0l;
		try 
		{
			DenominationValuesMaster denominationValuesMaster = new DenominationValuesMaster();
			denominationValuesMaster.setStrCurrencyCode("INR"); 
			denominationValuesMaster = denominationValuesMasterService.getDenominationValuesMasterBasedOnParameters(denominationValuesMaster);
		
			if (denominationMaster.getD10()!=null) 
			{
					totalCalculatedDenominationAmount = totalCalculatedDenominationAmount +(denominationMaster.getD10() * denominationValuesMaster.getD10());
			}
			if (denominationMaster.getD20()!=null) 
			{
					totalCalculatedDenominationAmount = totalCalculatedDenominationAmount +(denominationMaster.getD20() * denominationValuesMaster.getD20());
			}
			if (denominationMaster.getD50()!=null) 
			{
					totalCalculatedDenominationAmount = totalCalculatedDenominationAmount +(denominationMaster.getD50() * denominationValuesMaster.getD50());
			}
			if (denominationMaster.getD100()!=null) 
			{
					totalCalculatedDenominationAmount = totalCalculatedDenominationAmount +(denominationMaster.getD100() * denominationValuesMaster.getD100());
			}
			if (denominationMaster.getD200()!=null) 
			{
					totalCalculatedDenominationAmount = totalCalculatedDenominationAmount +(denominationMaster.getD200() * denominationValuesMaster.getD200());
			}
			if (denominationMaster.getD500()!=null) 
			{
					totalCalculatedDenominationAmount = totalCalculatedDenominationAmount +(denominationMaster.getD500() * denominationValuesMaster.getD500());
			}
			if (denominationMaster.getD1000()!=null) 
			{
					totalCalculatedDenominationAmount = totalCalculatedDenominationAmount +(denominationMaster.getD1000() * denominationValuesMaster.getD1000());
			}
			if (denominationMaster.getD2000()!=null) 
			{
					totalCalculatedDenominationAmount = totalCalculatedDenominationAmount +(denominationMaster.getD2000() * denominationValuesMaster.getD2000());
			}		
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return totalCalculatedDenominationAmount;
	}
	
	//Added By Pankaj Pawar Start
	@Override
	public TransactionConfig getAccountInformation(TransactionConfig transactionConfig) 
	{
		try
		{
			 UserTransactionModel userTransactionModel = transactionConfig.getUserTxnReq();
			
			 AccountMaster accountMaster = new AccountMaster();
			 accountMaster.setStrCustId(userTransactionModel.getUserCustId());
			 accountMaster.setStrTxnType(userTransactionModel.getTranType());
			 accountMaster.setStrAccountNumber(userTransactionModel.getUserAccountNumber());
			 accountMaster.setStrAccountType(userTransactionModel.getUserAccountType());
			 accountMaster.setStrTxnAmount(userTransactionModel.getUserTxnAmount());
			 // accountMaster = accountMasterService.getAccountInfo(accountMaster);
			 
			 userTransactionModel.setFromAccountNo(transactionConfig.getUserTxnReq().getFromAccountNo());
			 userTransactionModel.setToAccountNumber(transactionConfig.getUserTxnReq().getToAccountNumber());
			 List<AccountMaster> accountMasterList = accountMasterService.getAccountInfoList(userTransactionModel);
			 
			 AccountMaster accountMasterFromAccount = null;
			 AccountMaster accountMasterToAccount = null;
			 for (AccountMaster accountMasterData: accountMasterList) 
			 {
				 if (accountMasterData.getStrAccountNumber().equalsIgnoreCase(userTransactionModel.getFromAccountNo()))
				 {
					 accountMasterFromAccount = accountMasterData;
					 transactionConfig.setAccountMasterFromAccount(accountMasterData);
				 }
				 if (accountMasterData.getStrAccountNumber().equalsIgnoreCase(userTransactionModel.getToAccountNumber()))
				 {
					 accountMasterToAccount = accountMasterData;
					 transactionConfig.setAccountMasterToAccount(accountMasterData);
				 }
			 }
			 if (accountMasterFromAccount!=null) 
			 {
				 userTransactionModel.setAccountStatus(accountMasterFromAccount.getStrStatus());
				 transactionConfig = checkUserAccountStatus(transactionConfig);
				 if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
				 {
					 userTransactionModel.setCurrentAvailableBalance(accountMasterFromAccount.getStrClosingBalance());
					 userTransactionModel.setStrAccountHolderName(accountMasterFromAccount.getStrAccountHolderName());
					 if (accountMasterToAccount!=null) 
					 {
						 userTransactionModel.setAccountStatus(accountMasterToAccount.getStrStatus());
						 transactionConfig = checkUserAccountStatus(transactionConfig);
						 if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
						 {
							 userTransactionModel.setStrToAccountHolderName(accountMasterToAccount.getStrAccountHolderName());
						 }
						 else
						 {
							 transactionConfig.setCode("E0000");
							 transactionConfig.setMessage("To Account is not Active.");
						 }
						 //Added By Pankaj Start
						 if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
						 {
							 AccountCreation accountCreation = new AccountCreation();
							 accountCreation.setStrAccountNumber(accountMasterToAccount.getStrAccountNumber());
							 accountCreation.setStrClosingBalance(accountMasterToAccount.getStrClosingBalance());
							 accountCreation.setStrPreCredAmount(accountMasterToAccount.getStrPreCredAmount());
							 
							 TransactionConfig transactionConfigObj = externalTransactionHandler.getTierInfoByCustID(accountCreation);
							 
							 transactionConfigObj.setAccountCreation(accountCreation);
							 transactionConfigObj.setTxnAmount(userTransactionModel.getUserTxnAmount());
							 
							 transactionConfigObj = externalTransactionHandler.validateCummulativeTierLimit(transactionConfigObj);
							 
							 if (!"S0000".equalsIgnoreCase(transactionConfigObj.getCode())) 
							 {
								 transactionConfig.setCode("E0000");
								 transactionConfig.setMessage(transactionConfigObj.getMessage());
							 }
						 }
						//Added By Pankaj End
						 
					 }
					 else
					 {
						 transactionConfig.setCode("E0000");
						 transactionConfig.setMessage("Invalid To Account.");
					 }
				 }
				 else
				 {
					 transactionConfig.setCode("E0000");
					 transactionConfig.setMessage("From Account is not Active.");
				 }				
			 }
			 else
			 {
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Invalid From Account.");
			 }
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}

	@Override
	public TransactionConfig validateOtp(TransactionConfig transactionConfig)
	{
		transactionConfig.setCode("S0000");
		try
		{
			 UserTransactionModel userTransactionModel = transactionConfig.getUserTxnReq();
			 
			 CustomerIdCreation customerIdCreation = new CustomerIdCreation();
			 customerIdCreation.setStrCustId(userTransactionModel.getUserCustId());
			 String txnOtp = customerIdService.getTransactionPinOTP(customerIdCreation);
			 
			 if(txnOtp != null && txnOtp.trim().length() > 0)
			 {
				 if(!txnOtp.equalsIgnoreCase(Utils.generateHash(userTransactionModel.getReqOTP())))
				 {
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage("Incorrect OTP.");
				 }
			 }
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
		}	
		return transactionConfig;
	}

	@Override
	public TransactionConfig sendEmail(TransactionConfig transactionConfig)
	{
		transactionConfig.setCode("S0000");
		try
		{
		  String subject = "Regarding to Transaction.";
		  String bodyMsg = transactionConfig.getMailMessage();
		  
	   	  EmailTemplate emailTemplate =	Utils.getEmailTemplateForSendMail("contactus@AMStechnologies.com", transactionConfig.getStrEmail(), subject, bodyMsg);
	   	  emailTemplate.setStrParticipantid(transactionConfig.getParticipantId());
	   	  emailService.sendSimpleHtmlContentMessage(emailTemplate);	   	  
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
		}	
		return transactionConfig;
	}
	
	/*
	private String createMessageBasedOnTranType(TransactionConfig transactionConfig) 
	{
		UserTransactionModel userTransactionModel = transactionConfig.getUserTxnReq();
		StringBuilder messageBuilder = new StringBuilder();
		if ("WDL".equalsIgnoreCase(userTransactionModel.getTranType())) 
		{
			messageBuilder.append("Dear ");
			messageBuilder.append(userTransactionModel.getF);
		}
		else 
		{
			
		}
		return null;
	}
	*/
	
	@Override
	public TransactionConfig addAccountStatementData(TransactionConfig transactionConfig)
	{
		transactionConfig.setCode("S0000");
		try
		{
			AccountStatement accountStatement = new AccountStatement();
			accountStatement.setStrParticipantId("0");
			accountStatement.setStrAccountNumber(transactionConfig.getAccountNo());
			accountStatement.setStrAccountType(transactionConfig.getAccountType());
			accountStatement.setStrClosingBalance(transactionConfig.getAccountClosingBalance());
			accountStatement.setStrIsGLType("N");
			accountStatement.setStrTransactionAmount(transactionConfig.getTxnAmount());
			accountStatement.setStrTransactionID(transactionConfig.getTxnId());
			accountStatement.setTransactionDate(Utils.getCurrentDate());
			accountStatement.setStrTransactionType(transactionConfig.getAccountTranType());
			accountStatement.setStrTransactionMode(TransactionType.MODE.get(transactionConfig.getAccountTranType()));
			accountStatement.setStrNaration(transactionConfig.getStrNaration());			
			
			accountStatementService.addAccountTransactionData(accountStatement);
			transactionConfig.setCode("S0000");
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
		}
		return transactionConfig;
	}
	
	@Override
	public TransactionConfig addGLAccountStatementData(TransactionConfig transactionConfig)
	{
		transactionConfig.setCode("S0000");
		try
		{
			GLAccountStatement glAccountStatement = new GLAccountStatement();
			glAccountStatement.setStrAccountNumber(transactionConfig.getAccountNo());
			glAccountStatement.setStrGLAccountType(transactionConfig.getAccountType());
			gLAccountStatementService.addGlAccountStatementData(glAccountStatement);
			  
			transactionConfig.setCode("S0000");
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
		}
		return transactionConfig;
	}
	
	@Override
	public TransactionConfig addTranMasterRequestEntry(TransactionConfig transactionConfig)
	{
		transactionConfig.setCode("S0000");
		try
		{
			AccountTranMaster accountTranMaster = new AccountTranMaster();
			accountTranMaster.setStrFrom_account_number(transactionConfig.getAccountMasterFromAccount().getStrAccountNumber());
			accountTranMaster.setStrTo_account_number(transactionConfig.getAccountMasterToAccount().getStrAccountNumber());
			accountTranMaster.setStrTransaction_amount(transactionConfig.getUserTxnReq().getUserTxnAmount());
			accountTranMaster.setStrTran_type(transactionConfig.getUserTxnReq().getTranType());
			
			try 
			{
				TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
				transactionTypeModel.setStrTxnTypeKeyWord(transactionConfig.getUserTxnReq().getTranType());
				
				transactionTypeModel = transactionTypeService.getTransactionTypeMaster(transactionTypeModel);
				
				accountTranMaster.setStrProcessingCode(transactionTypeModel.getStrProcessingCode());
			}
			catch (Exception e) 
			{
				amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			}
			
			accountTranMaster.setStrTxn_id(transactionConfig.getTxnId());
			accountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
			accountTranMaster.setStrParticipantId("0");			
			
			accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());			
			accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
			accountTranMaster.setStrResponseCode("00");
			
			String authCode = Utils.getAlphaNumericString();
			accountTranMaster.setStrAuthCode(authCode);
			
			transactionConfig.setResponseCode("00");
			transactionConfig.setAuthCode(authCode);
			
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				if (transactionConfig!=null && transactionConfig.getMontraTxnId()!=null && transactionConfig.getMontraTxnId().trim().length() > 0) {
					accountTranMaster.setStrSrcTxnId(transactionConfig.getMontraTxnId().trim());
				}
			}
			
			accountTranMasterService.addAccountTransactionData(accountTranMaster);
			transactionConfig.setCode("S0000");
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}

	@Override
	public TransactionConfig reduceAccountBalanceWithLimits(TransactionConfig transactionConfig)
	{
		transactionConfig.setCode("S0000");
		try
		{
			double txnAmount = Utils.stringToDouble(transactionConfig.getUserTxnReq().getUserTxnAmount());
			AccountMaster accountMasterFromAccount = transactionConfig.getAccountMasterFromAccount();
			
			AccountCreation accountCreation = new AccountCreation();
			double strNewClosingBalance =  Utils.stringToDouble(accountMasterFromAccount.getStrClosingBalance()) - txnAmount;
			accountCreation.setStrClosingBalance(Utils.decimalFormat.format(strNewClosingBalance));
			accountCreation.setStrAccountNumber(transactionConfig.getAccountMasterFromAccount().getStrAccountNumber());
			
			if(!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				double strNewDailyLimit = Utils.stringToDouble(accountMasterFromAccount.getStrAvailableDailyLimit()) - txnAmount;
				double strNewMonthlylimit = Utils.stringToDouble(accountMasterFromAccount.getStrAvailableMonthlyLimit()) - txnAmount; 
				double strNewYearlyLimit = Utils.stringToDouble(accountMasterFromAccount.getStrAvailableYearlyLimit()) - txnAmount;
				
				accountCreation.setStrAvailableDailyLimit(Utils.decimalFormat.format(strNewDailyLimit));
				accountCreation.setStrAvailableMonthlyLimit(Utils.decimalFormat.format(strNewMonthlylimit));
				accountCreation.setStrAvailableYearlyLimit(Utils.decimalFormat.format(strNewYearlyLimit));
			}
			
			accountMasterFromAccount.setStrClosingBalance(Utils.decimalFormat.format(strNewClosingBalance));
			
			accountMasterService.updatePerformTxnValues(accountCreation);
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	
	@Override
	public TransactionConfig increaseAccountBalance(TransactionConfig transactionConfig)
	{
		transactionConfig.setCode("S0000");
		try
		{
			double txnAmount = Utils.stringToDouble(transactionConfig.getUserTxnReq().getUserTxnAmount());
			
			AccountMaster accountMasterToAccount = transactionConfig.getAccountMasterToAccount();
			
			AccountCreation accountCreation = new AccountCreation();
			
			double strNewClosingBalance = Utils.stringToDouble(accountMasterToAccount.getStrClosingBalance()) + txnAmount;
			accountCreation.setStrClosingBalance(Utils.decimalFormat.format(strNewClosingBalance));
			accountCreation.setStrAccountNumber(transactionConfig.getAccountMasterToAccount().getStrAccountNumber());
			
			if(!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				double strNewDailyLimit = Utils.stringToDouble(accountMasterToAccount.getStrAvailableDailyLimit()) + txnAmount;
				double strNewMonthlylimit = Utils.stringToDouble(accountMasterToAccount.getStrAvailableMonthlyLimit()) + txnAmount; 
				double strNewYearlyLimit = Utils.stringToDouble(accountMasterToAccount.getStrAvailableYearlyLimit()) + txnAmount;
				
				accountCreation.setStrAvailableDailyLimit(Utils.decimalFormat.format(strNewDailyLimit));
				accountCreation.setStrAvailableMonthlyLimit(Utils.decimalFormat.format(strNewMonthlylimit));
				accountCreation.setStrAvailableYearlyLimit(Utils.decimalFormat.format(strNewYearlyLimit));
			}
			
			accountMasterToAccount.setStrClosingBalance(Utils.decimalFormat.format(strNewClosingBalance));
			
			accountMasterService.updatePerformTxnValues(accountCreation);
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	@Override
	public TransactionConfig addAgentAccountStatementData(TransactionConfig transactionConfig)
	{
		transactionConfig.setCode("S0000");
		try
		{
			AccountStatement accountStatement = new AccountStatement();
			accountStatement.setStrAccountNumber(transactionConfig.getAccountNo());
			accountStatement.setTransactionAmount(Utils.stringToDouble(transactionConfig.getUserTxnReq().getUserTxnAmount()));
			accountStatementService.addAccountTransactionData(accountStatement);
			  
			transactionConfig.setCode("S0000");
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	@Override
	public TransactionConfig addAgentGLAccountStatementData(TransactionConfig transactionConfig)
	{
		transactionConfig.setCode("S0000");
		try
		{
			GLAccountStatement glAccountStatement = new GLAccountStatement();
			glAccountStatement.setStrAccountNumber(transactionConfig.getUserTxnReq().getToAccountNumber());
			glAccountStatement.setStrGLAccountType(transactionConfig.getUserTxnReq().getToAccountType());
			gLAccountStatementService.addGlAccountStatementData(glAccountStatement);
			  
			transactionConfig.setCode("S0000");
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	
	@Override
	public void performGlAccountTransaction(TransactionConfig transactionConfig)
	{
		try
		{
			GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
			glAccountTypeMaster.setTranId(transactionConfig.getTxnId());
			
			glAccountTypeMaster.setUserAccountType(transactionConfig.getUserLinkedGLAccountType());
			//glAccountTypeMaster.setStrGLAccountType("ULB"); // ULB
			
			glAccountTypeMaster.setTranType(transactionConfig.getAccountTranType());  //WDL
			
			transactionConfig.setTxnAmount(String.valueOf(transactionConfig.getUserTxnReq().getUserTxnAmount()));
			transactionConfig.setGlAccountTypeMaster(glAccountTypeMaster);	
			
			glTransactionHandler.addAndUpdateGLAccount(transactionConfig);
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	//Added By Pankaj Pawar End
	
	@Override
	public TransactionConfig saveDenominationMasterData(TransactionConfig transactionConfig) 
	{
		try 
		{
			DenominationMaster denominationMaster = transactionConfig.getUserTxnReq().getDenominationData();
			
			denominationMaster.setTxnId(transactionConfig.getTxnId());
			denominationMaster.setStrTxnAmount(transactionConfig.getUserTxnReq().getUserTxnAmount());
			String tranType = transactionConfig.getUserTxnReq().getTranType();
			denominationMaster.setStrTxnType(tranType);
			denominationMaster.setStrTxnMode(TransactionType.MODE.get(tranType));
			
			denominationMasterService.saveDenominationMasterData(denominationMaster);
		}
		catch (Exception e) 
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}

	@Override
	public TransactionConfig validateSecretCode(TransactionConfig transactionConfig) throws Exception 
	{
		transactionConfig.setCode("S0000");
		try 
		{
			//Need to add secret code validation here
		}
		catch (Exception e) {
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
}
