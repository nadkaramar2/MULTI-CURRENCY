package ams.cms.txn.handler;

import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.handler.ExternalTransactionHandler;
import ams.cms.api.model.AccountMaster;
import ams.cms.config.CommonConstants;
import ams.cms.config.TransactionConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.JournalTransfer;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountStatementService;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.GLAccountStatementService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.services.JournalTransferService;
import ams.cms.utility.Utils;

@Component
public class JournalTransferHandlerImpl implements JournalTransferHandler 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(JournalTransferHandlerImpl.class);
	
	@Autowired
	JournalTransferService journalTransferService;
	
	@Autowired
	GLTransactionHandler glTransactionHandler;
	
	@Autowired
	AccountTransactionHandler accountTransactionHandler;
	
	@Autowired
	GLAccountTypeMasterService glAccountTypeMasterService;
	
	@Autowired
	GLAccountStatementService glAccountStatementService;
	
	@Autowired
	AccountMasterService accountMasterService;
	
	@Autowired 
	AccountTranMasterService accountTranMasterService;
	
	@Autowired
	AccountStatementService accountStatementService;
	
	@Autowired
	TransactionValidator transactionValidator;
	
	@Autowired
	ExternalTransactionHandler externalTransactionHandler;
	
	@Override
	public JournalTransfer processApproveJournalTransfer(JournalTransfer journalTransfer) 
	{
		TransactionConfig transactionConfig = new TransactionConfig();
		try
		{
			journalTransfer.setTransactionConfig(transactionConfig);
			transactionConfig.setCode("S0000");
			transactionConfig.setMessage("success");
			journalTransfer.setTransactionConfig(transactionConfig);
			
			String txnJournalTransferType = journalTransfer.getTxnJournalTransferType();
			if ("G2G".equalsIgnoreCase(txnJournalTransferType))
			{
					validateFromGLAccount(journalTransfer);
					if (transactionConfig!=null && "S0000".equalsIgnoreCase(transactionConfig.getCode()) ) 
					{
						validateToGLAccount(journalTransfer);
						if (transactionConfig!=null && "S0000".equalsIgnoreCase(transactionConfig.getCode()) ) 
						{
							performG2GOperation(journalTransfer);
							updateDefaultEntity(journalTransfer);
						}
					}
			}
			else if ("G2A".equalsIgnoreCase(txnJournalTransferType)) 
			{
				validateFromGLAccount(journalTransfer);
				if (transactionConfig!=null && "S0000".equalsIgnoreCase(transactionConfig.getCode()) ) 
				{
					validateToAccount(journalTransfer);
					if (transactionConfig!=null && "S0000".equalsIgnoreCase(transactionConfig.getCode()) ) 
					{
						performG2AOperation(journalTransfer);
						updateDefaultEntity(journalTransfer);
					}
				}
			}
			else if("A2G".equalsIgnoreCase(txnJournalTransferType)) 
			{
				validateFromAccount(journalTransfer);
				if (transactionConfig!=null && "S0000".equalsIgnoreCase(transactionConfig.getCode()) ) 
				{
						validateToGLAccount(journalTransfer);
						if (transactionConfig!=null && "S0000".equalsIgnoreCase(transactionConfig.getCode()) ) 
						{
							performA2GOperation(journalTransfer);
							updateDefaultEntity(journalTransfer);
						}
				}
			}
			else if("A2A".equalsIgnoreCase(txnJournalTransferType)) 
			{
				validateFromAccount(journalTransfer);
				if (transactionConfig!=null && "S0000".equalsIgnoreCase(transactionConfig.getCode()) ) 
				{
						validateToAccount(journalTransfer);
						if (transactionConfig!=null && "S0000".equalsIgnoreCase(transactionConfig.getCode()) ) 
						{
								performA2AOperation(journalTransfer);	
								updateDefaultEntity(journalTransfer);
						}
				}
			}
			else
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("No Operation Performed Because there is no Journal Transfer Type defined.");
			}
		}
		catch (Exception e) 
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("exception occured during approve process");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return journalTransfer;
	}

	@Override
	public JournalTransfer processRejectJournalTransfer(JournalTransfer journalTransfer) throws Exception 
	{
			TransactionConfig transactionConfig = new TransactionConfig();
			transactionConfig.setCode("S0000");
			transactionConfig.setMessage("success");
			try 
			{
				journalTransfer.setTransactionConfig(transactionConfig);
				journalTransfer.setTranType("TRF");
				journalTransfer.setResponseCode("06");
				addTranMasterResponseEntry(journalTransfer); 
				updateJournalTransferTxnStatus(journalTransfer);
			}
			catch (Exception e) 
			{
				amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("exception occured during reject process");
				journalTransfer.setResponseCode("06");
			}
			return journalTransfer;
	}
	
	private void updateDefaultEntity(JournalTransfer journalTransfer) 
	{
		try
		{
			journalTransfer.setTranType("TRF");
			journalTransfer.setResponseCode("00");
			addTranMasterResponseEntry(journalTransfer); 
			updateJournalTransferTxnStatus(journalTransfer);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private TransactionConfig validateFromGLAccount(JournalTransfer journalTransfer) 
	{
		TransactionConfig transactionConfig = journalTransfer.getTransactionConfig();
		try
		{
			GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
			glAccountTypeMaster.setStrAccountNumber(journalTransfer.getStrFromAccountName());
			glAccountTypeMaster.setStrGLAccountType(journalTransfer.getStrFromAccountType());
			glAccountTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(glAccountTypeMaster);
			
			if (glAccountTypeMaster!=null) 
			{
				if (!"A".equalsIgnoreCase(glAccountTypeMaster.getStrStatus()))
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage("From Account is not Active.");
					journalTransfer.setResponseCode("06");
				}
			}
			else
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("From Account is not Present.");
				journalTransfer.setResponseCode("06");
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("exception occured during reject process");
			journalTransfer.setResponseCode("06");
		}
		return transactionConfig;
	}
	private TransactionConfig validateToGLAccount(JournalTransfer journalTransfer) 
	{
		TransactionConfig transactionConfig = journalTransfer.getTransactionConfig();
		try
		{
			GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
			glAccountTypeMaster.setStrAccountNumber(journalTransfer.getStrToAccountNumber());
			glAccountTypeMaster.setStrGLAccountType(journalTransfer.getStrToAccountType());
			glAccountTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(glAccountTypeMaster);
			if (glAccountTypeMaster!=null ) 
			{
				if (!"A".equalsIgnoreCase(glAccountTypeMaster.getStrStatus()))
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage("To Account is not Active.");
					journalTransfer.setResponseCode("06");
				}
			}
			else
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("To Account is not Present.");
				journalTransfer.setResponseCode("06");
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("exception occured during reject process");
			journalTransfer.setResponseCode("06");
		}
		return transactionConfig;
	}
	private TransactionConfig validateFromAccount(JournalTransfer journalTransfer) 
	{
		TransactionConfig transactionConfig = journalTransfer.getTransactionConfig();
		try
		{	
				AccountCreation accountCreation = new AccountCreation();
				accountCreation.setStrAccountNumber(journalTransfer.getStrFromAccountNumber());
				accountCreation.setStrAccountType(journalTransfer.getStrFromAccountType());
				//accountCreation = accountMasterService.getAccountInformation(accountCreation);
				accountCreation = accountMasterService.getSenderAccountInformation(accountCreation);
				
				if (accountCreation!=null)
				{
					journalTransfer.setFromLinkedGlType(accountCreation.getStrGLAccountType()); //Added on 16-May-2023
					
					transactionConfig.setFromAccountCreation(accountCreation); //<--- Set From Account Creation Information
					if (!"Active".equalsIgnoreCase(accountCreation.getStrStatus())) 
					{
						transactionConfig.setCode("E0000");
						transactionConfig.setMessage("From Account is not Active.");
						journalTransfer.setResponseCode("06");
					}
					//added by Sunil Y ,  checkpoint for ear_mark_amt [started]  2023-06-16
					double finalClosingBlance =  Utils.stringToDouble(accountCreation.getStrClosingBalance()) -accountCreation.getStrEarMarkAmount();
					//if(journalTransfer.getStrAmoutToTransfer() > Utils.stringToDouble(accountCreation.getStrClosingBalance() ))
					if(journalTransfer.getStrAmoutToTransfer() > finalClosingBlance)
					{
						transactionConfig.setCode("E0000");
						transactionConfig.setMessage("Insufficient Balance.");
						journalTransfer.setResponseCode("06");
					}
					//added by Sunil Y ,  checkpoint for ear_mark_amt [End]  2023-06-16
					//Added for Nigeria based changes Start
					if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
					{
						TransactionConfig fromTransactionConfigObj = externalTransactionHandler.getTierInfoByCustID(accountCreation);	
						fromTransactionConfigObj.setTxnAmount(String.valueOf(journalTransfer.getStrAmoutToTransfer()));
						transactionConfig.setFromAccountTierAccountMaster(fromTransactionConfigObj.getTierAccountMaster()); //<--- Set From Tier Account Information
						
						fromTransactionConfigObj = externalTransactionHandler.validateDailyLimits(fromTransactionConfigObj);
						if(!"S0000".equalsIgnoreCase(fromTransactionConfigObj.getCode()))
						{
							transactionConfig.setCode("E0000");
							transactionConfig.setMessage(fromTransactionConfigObj.getMessage());	
							journalTransfer.setResponseCode("06");
						}
					}
					//Added for Nigeria based changes End
					else 
					{
						TransactionConfig fromTransactionCfgObj = new TransactionConfig();
						fromTransactionCfgObj.setAccountCreation(accountCreation);
						fromTransactionCfgObj.setTxnAmount(String.valueOf(journalTransfer.getStrAmoutToTransfer()));
						
						fromTransactionCfgObj = validateTxnLimits(fromTransactionCfgObj);
						if(!"S000".equalsIgnoreCase(fromTransactionCfgObj.getCode()))
						{
							transactionConfig.setCode("E0000");
							transactionConfig.setMessage(fromTransactionCfgObj.getMessage());	
							journalTransfer.setResponseCode("06");
						}
					}
				}
				else
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage("From Account is not Present.");
					journalTransfer.setResponseCode("06");
				}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("exception occured during reject process");
			journalTransfer.setResponseCode("06");
		}
		return transactionConfig;
	}
	@SuppressWarnings("unused")
	private TransactionConfig validateToAccount(JournalTransfer journalTransfer) 
	{
		TransactionConfig transactionConfig = journalTransfer.getTransactionConfig();
		try
		{
			AccountCreation accountCreation = new AccountCreation();
			accountCreation.setStrAccountNumber(journalTransfer.getStrToAccountNumber());
			accountCreation.setStrAccountType(journalTransfer.getStrToAccountType());
			//accountCreation = accountMasterService.getAccountInformation(accountCreation);
			accountCreation = accountMasterService.getRecipientAccountInformation(accountCreation);
			
			if (accountCreation!=null)
			{
				journalTransfer.setToLinkedGlType(accountCreation.getStrGLAccountType()); //Added on 16-May-2023
				
				transactionConfig.setToAccountCreation(accountCreation); //<-- set To Account Creation information
				if (!"Active".equalsIgnoreCase(accountCreation.getStrStatus()))
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage("To Account is not Active.");
					journalTransfer.setResponseCode("06");
				}
				//Added for Validating to account cumulative balance limit by Pankaj [start] 
				if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
				{
					TransactionConfig toTransactionConfig = new TransactionConfig();
					toTransactionConfig.setAccountCreation(accountCreation);
					toTransactionConfig.setTxnAmount(String.valueOf(journalTransfer.getStrAmoutToTransfer()));
					toTransactionConfig.setCode("S0000");
					//added by Sunil Y , for Deposite added pre_cred_Amt , [ Started ]  2023-06-15
					toTransactionConfig.setStrPreCredAmount(String.valueOf(accountCreation.getStrPreCredAmount())); 
					//added by Sunil Y , for Deposite added pre_cred_Amt , [ End ]  2023-06-15
					toTransactionConfig = transactionValidator.validateCummulativeBalanceLimit(toTransactionConfig); //Validate to Account cumulative balance
					if(!"S0000".equalsIgnoreCase(toTransactionConfig.getCode()))
					{
						transactionConfig.setCode("E0000");
						transactionConfig.setMessage(toTransactionConfig.getMessage());	
						journalTransfer.setResponseCode("06");
					}
					transactionConfig.setToAccountTierAccountMaster(toTransactionConfig.getTierAccountMaster());
				}
				//Added for Validating to account cumulative balance limit by Pankaj [end]
			}
			else
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("To Account is not Present.");
				journalTransfer.setResponseCode("06");
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("exception occured during reject process");
			journalTransfer.setResponseCode("06");
		}
		return transactionConfig;
	}
	
	
	private void performG2GOperation(JournalTransfer journalTransfer) 
	{
			try
			{
					//Reduce GL Account Type master closing balance with Debit GL Account Statement Start
					getFromGLAccountTypeObject(journalTransfer);
					reduceGLAccountBalance(journalTransfer);		//From GL			
					addDebitGLAccountStatement(journalTransfer);
					//Reduce GL Account Type master closing balance with Debit GL Account Statement End
					
					//Increasing GL Account Type master closing balance with Credit GL Account Statement Start
					getToGLAccountTypeObject(journalTransfer);
					increaseGLAccountBalance(journalTransfer);	//To GL				
					addCreditGLAccountStatement(journalTransfer);
					//Increasing GL Account Type master closing balance with Credit GL Account Statement End
			}
			catch (Exception e) 
			{
				amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			}
	}
	private void performG2AOperation(JournalTransfer journalTransfer) 
	{
			try 
			{
				//Reduce GL Account Type master closing balance with Debit GL Account Statement Start	
				getFromGLAccountTypeObject(journalTransfer);
				reduceGLAccountBalance(journalTransfer);		//From GL			
				addDebitGLAccountStatement(journalTransfer);
				//Reduce GL Account Type master closing balance with Debit GL Account Statement End
				
				//Increase to Account Balance Start
				increasingAccountBalance(journalTransfer);
				addCreditAccountStatementEntry(journalTransfer);
				//Increase to Account Balance End
				
				//For linkedGL account Credit change Start 
				getToLinkedGLAccountTypeObject(journalTransfer);
				if (journalTransfer.getStrIsLinkedGLAccount() != null && "Y".equalsIgnoreCase(journalTransfer.getStrIsLinkedGLAccount())) 
				{
					increaseGLAccountBalance(journalTransfer);		//Linked GL		
					addCreditGLAccountStatement(journalTransfer);
				}
				//For linkedGL account Credit change End
			}
			catch (Exception e) 
			{
				amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			}
	}
	private void performA2GOperation(JournalTransfer journalTransfer) 
	{
			try 
			{
				//Reduce from Account Balance Start
				reducingAccountBalanceWithLimits(journalTransfer);
				addDebitAccountStatementEntry(journalTransfer);
				//Reduce from Account Balance End
				
				//Reduce from Linked GL Account Balance Start
				getFromLinkedGLAccountTypeObject(journalTransfer);
				if (journalTransfer.getStrIsLinkedGLAccount() != null && "Y".equalsIgnoreCase(journalTransfer.getStrIsLinkedGLAccount())) 
				{
					reduceGLAccountBalance(journalTransfer);
					addDebitGLAccountStatement(journalTransfer);
				}
				//Reduce from Linked GL Account Balance End
				
				//Increase To GL account type master Start
				getToGLAccountTypeObject(journalTransfer);
				increaseGLAccountBalance(journalTransfer);	//To GL				
				addCreditGLAccountStatement(journalTransfer);
				//Increase To GL account type master End
			}
			catch (Exception e) 
			{
					amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			}
	}
	private void performA2AOperation(JournalTransfer journalTransfer) 
	{
		try
		{
				//Reduce from Account Balance Start
				reducingAccountBalanceWithLimits(journalTransfer);
				addDebitAccountStatementEntry(journalTransfer);
				//Reduce from Account Balance End
				
				//Reduce from Linked GL Account Balance Start
				getFromLinkedGLAccountTypeObject(journalTransfer);
				amsLogger.writeInfoLog("Inside performA2AOperation journalTransfer::"+journalTransfer);
				if (journalTransfer.getStrIsLinkedGLAccount() != null && "Y".equalsIgnoreCase(journalTransfer.getStrIsLinkedGLAccount())) 
				{
					reduceGLAccountBalance(journalTransfer);
					addDebitGLAccountStatement(journalTransfer);
				}
				//Reduce from Linked GL Account Balance End
				
				//Increase to account balance Start
				amsLogger.writeInfoLog("--1-- Inside performA2AOperation journalTransfer::"+journalTransfer);
				increasingAccountBalance(journalTransfer);
				
				amsLogger.writeInfoLog("--2-- Inside performA2AOperation journalTransfer::"+journalTransfer);
				addCreditAccountStatementEntry(journalTransfer);
				//Increase to account balance End
				
				//Increase to Linked GL Account Balance Start
				amsLogger.writeInfoLog("--3-- Inside performA2AOperation journalTransfer::"+journalTransfer);
				getToLinkedGLAccountTypeObject(journalTransfer);
				
				amsLogger.writeInfoLog("--4-- Inside performA2AOperation journalTransfer::"+journalTransfer);
				if (journalTransfer.getStrIsLinkedGLAccount() != null && "Y".equalsIgnoreCase(journalTransfer.getStrIsLinkedGLAccount()))
				{
					increaseGLAccountBalance(journalTransfer);
					addCreditGLAccountStatement(journalTransfer);
				}	
				//Increase to Linked GL Account Balance End
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void updateJournalTransferTxnStatus(JournalTransfer journalTransfer) throws Exception 
	{
			journalTransferService.updateJournalTransferTxnStatus(journalTransfer);
	}
	
	private GLAccountTypeMaster reduceGLAccountBalance(JournalTransfer journalTransfer) 
	{
		try 
		{
			amsLogger.writeInfoLog("Inside reduceGLAccountBalance journalTransfer::"+journalTransfer);
			TransactionConfig transactionConfig = journalTransfer.getTransactionConfig();
			transactionConfig.setCode("S0000");
			
			GLAccountTypeMaster glAccountTypeMasterDebit = glTransactionHandler.getReducingGLAccountBalance(transactionConfig);
			transactionConfig.setGlAccountTypeMaster(glAccountTypeMasterDebit);	
			journalTransfer.setTransactionConfig(transactionConfig);

			amsLogger.writeInfoLog("Inside reduceGLAccountBalance glAccountTypeMasterDebit::"+glAccountTypeMasterDebit);
			glAccountTypeMasterService.updateGLAccountTypeDetails(glAccountTypeMasterDebit);				
			return glAccountTypeMasterDebit;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	private GLAccountTypeMaster increaseGLAccountBalance(JournalTransfer journalTransfer) 
	{
			try 
			{
				TransactionConfig transactionConfig = journalTransfer.getTransactionConfig();
				transactionConfig.setCode("S0000");
				
				GLAccountTypeMaster glAccountTypeMasterCredit = glTransactionHandler.getIncreasingGLAccountBalance(transactionConfig);
				transactionConfig.setGlAccountTypeMaster(glAccountTypeMasterCredit);
				
				journalTransfer.setTransactionConfig(transactionConfig);
				
				glAccountTypeMasterService.updateGLAccountTypeDetails(glAccountTypeMasterCredit);			
				return glAccountTypeMasterCredit;
			}
			catch (Exception e) {
				amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			}
			return null;
	} 
	
	void addDebitGLAccountStatement(JournalTransfer journalTransfer) 
	{
		try 
		{
			amsLogger.writeInfoLog("Inside addDebitGLAccountStatement journalTransfer::"+journalTransfer);
			
			TransactionConfig transactionConfig = journalTransfer.getTransactionConfig();
			transactionConfig.setCode("S0000");
			GLAccountStatement debitGLAccountStatement = glTransactionHandler.creatingDebitGLAccountStatement(transactionConfig);					
			glAccountStatementService.addGlAccountStatementData(debitGLAccountStatement);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	void addCreditGLAccountStatement(JournalTransfer journalTransfer) 
	{
		try 
		{
			TransactionConfig transactionConfig = journalTransfer.getTransactionConfig();
			transactionConfig.setCode("S0000");
			GLAccountStatement creditGLAccountStatement = glTransactionHandler.creatingCreditGLAccountStatement(transactionConfig);
			glAccountStatementService.addGlAccountStatementData(creditGLAccountStatement);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void addTranMasterResponseEntry(JournalTransfer journalTransfer)
	{
		try
		{
			AccountTranMaster accountTranMaster = new AccountTranMaster();
			accountTranMaster.setStrTxn_id(journalTransfer.getStrTxnId());
			
			accountTranMaster.setStrFrom_account_number(journalTransfer.getStrFromAccountNumber());
			accountTranMaster.setStrTo_account_number(journalTransfer.getStrToAccountNumber());
			
			accountTranMaster.setStrTransaction_amount(Utils.decimalFormat.format(journalTransfer.getStrAmoutToTransfer()));
			accountTranMaster.setSwitchTxDate(new Date());
			
			accountTranMaster.setStrParticipantId("0");
			
			accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());			
			accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
			
			accountTranMaster.setStrTran_type(journalTransfer.getTranType());
			accountTranMaster.setStrResponseCode(journalTransfer.getResponseCode());
			
			accountTranMasterService.addAccountTransactionData(accountTranMaster);
		}
		catch(Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private JournalTransfer reducingAccountBalanceWithLimits(JournalTransfer journalTransfer) 
	{
		try 
		{
			amsLogger.writeInfoLog("Inside reducingAccountBalanceWithLimits journalTransfer::"+journalTransfer);
			//AccountCreation accountCreation = getFromAccountMasterObject(journalTransfer);
			AccountCreation accountCreation = journalTransfer.getTransactionConfig().getFromAccountCreation();
 			accountCreation.setAvailableBalance(accountCreation.getStrClosingBalance());
			
			TransactionConfig transactionConfig = journalTransfer.getTransactionConfig();
			transactionConfig.setCode("S0000");
			transactionConfig.setAvailableAccountClosingBalance(accountCreation.getAvailableBalance());			
			transactionConfig.setTxnAmount(String.valueOf(journalTransfer.getStrAmoutToTransfer()));
			
			AccountMaster accountMaster = new AccountMaster();
			accountMaster.setStrAccountNumber(accountCreation.getStrAccountNumber());
			accountMaster.setStrAccountType(accountCreation.getStrAccountType());
			
			accountMaster.setStrAvailableDailyLimit(accountCreation.getStrAvailableDailyLimit()); 
			accountMaster.setStrAvailableMonthlyLimit(accountCreation.getStrAvailableMonthlyLimit());
			accountMaster.setStrAvailableYearlyLimit(accountCreation.getStrAvailableYearlyLimit());
			
			transactionConfig.setAccountMaster(accountMaster);			
			transactionConfig = accountTransactionHandler.getReducingAccountBalance(transactionConfig);
			
			transactionConfig.setAccountNo(journalTransfer.getStrFromAccountNumber());
			transactionConfig.setAccountType(journalTransfer.getStrFromAccountType());
			transactionConfig.setAccountClosingBalance(accountMaster.getStrClosingBalance());
			
			journalTransfer.setTransactionConfig(transactionConfig);
			
			//accountMasterService.updateAccountMasterFields(accountMaster);			
			return journalTransfer;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	private TransactionConfig validateTxnLimits(TransactionConfig transactionConfig)
	{
		AccountCreation accountCreation = transactionConfig.getAccountCreation();		
		double txnAmount = Utils.stringToDouble(transactionConfig.getTxnAmount());
		
		String availableDailyLimit = accountCreation.getStrAvailableDailyLimit();
		amsLogger.writeInfoLog("availableDailyLimit::["+availableDailyLimit+"]");
		if( Utils.stringToDouble(availableDailyLimit) < txnAmount )
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Daily Limit Amount Breached.");
			return transactionConfig; 
		}
		
		String availableMonthlyLimit = accountCreation.getStrAvailableMonthlyLimit();
		amsLogger.writeInfoLog("availableMonthlyLimit::["+availableMonthlyLimit+"]");
		if(Utils.stringToDouble(availableMonthlyLimit) < txnAmount )
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Monthly Limit Amount Breached."); 
			return transactionConfig;
		}
		
		String availableYearlyLimit = accountCreation.getStrAvailableYearlyLimit();
		amsLogger.writeInfoLog("availableYearlyLimit::["+availableYearlyLimit+"]");		
		if(Utils.stringToDouble(availableYearlyLimit) < txnAmount )
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Yearly Limit Amount Breached.");
			return transactionConfig; 
		}
		return transactionConfig;
	}
	
	private JournalTransfer increasingAccountBalance(JournalTransfer journalTransfer) 
	{
		try 
		{
			amsLogger.writeInfoLog("--2-- Inside increasingAccountBalance journalTransfer::"+journalTransfer);
			AccountCreation accountCreation = getToAccountMasterObject(journalTransfer);
			
			TransactionConfig transactionConfig = journalTransfer.getTransactionConfig();
			transactionConfig.setCode("S0000");
			transactionConfig.setAvailableAccountClosingBalance(accountCreation.getAvailableBalance());
			transactionConfig.setTxnAmount(String.valueOf(journalTransfer.getStrAmoutToTransfer()));
			
			AccountMaster accountMaster = new AccountMaster();
			accountMaster.setStrAccountNumber(accountCreation.getStrAccountNumber());
			accountMaster.setStrAccountType(accountCreation.getStrAccountType());
			
			transactionConfig.setAccountMaster(accountMaster);			
			transactionConfig = accountTransactionHandler.getIncreasingAccountBalance(transactionConfig);
			
			transactionConfig.setAccountNo(journalTransfer.getStrToAccountNumber());
			transactionConfig.setAccountType(journalTransfer.getStrToAccountType());
			transactionConfig.setAccountClosingBalance(accountMaster.getStrClosingBalance());
			
			journalTransfer.setTransactionConfig(transactionConfig);
			
			//accountMasterService.updateAccountMasterFields(accountMaster);
			return journalTransfer;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	private void addCreditAccountStatementEntry(JournalTransfer journalTransfer) 
	{
			try
			{
				TransactionConfig transactionConfig = journalTransfer.getTransactionConfig();
				transactionConfig.setCode("S0000");
				transactionConfig =  accountTransactionHandler.creatingCreditAccountStatement(transactionConfig);
				//accountStatementService.addAccountTransactionData(accountStatement);
			}
			catch (Exception e) 
			{
				amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			}
	}
	private void addDebitAccountStatementEntry(JournalTransfer journalTransfer) 
	{
		amsLogger.writeInfoLog("Inside addDebitAccountStatementEntry journalTransfer::"+journalTransfer);
		try
		{
			TransactionConfig transactionConfig = journalTransfer.getTransactionConfig();
			transactionConfig.setCode("S0000");
			//code added for txnid on 03-05-2023
			String strTxnId = journalTransfer.getStrTxnId();
			transactionConfig.setTxnId(strTxnId);
			//code added for txnid on 03-05-2023
			transactionConfig = accountTransactionHandler.creatingDebitAccountStatement(transactionConfig);
			//accountStatementService.addAccountTransactionData(accountStatement);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	} 
	
	private JournalTransfer getFromGLAccountTypeObject(JournalTransfer journalTransfer) throws Exception
	{
		GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();		
		glAccountTypeMaster.setStrAccountNumber(journalTransfer.getStrFromAccountNumber());
		glAccountTypeMaster.setStrGLAccountType(journalTransfer.getStrFromAccountType());
		
		glAccountTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(glAccountTypeMaster);
		
		TransactionConfig transactionConfig = new TransactionConfig();
		transactionConfig.setCode("S0000");
		transactionConfig.setTxnId(journalTransfer.getStrTxnId());
		transactionConfig.setTxnAmount(String.valueOf(journalTransfer.getStrAmoutToTransfer()));		
		transactionConfig.setAccountNo(journalTransfer.getStrFromAccountNumber());
		transactionConfig.setGlAccountType(journalTransfer.getStrFromAccountType());		
		transactionConfig.setAvailableAccountClosingBalance(glAccountTypeMaster.getStrClosingBalance());
		transactionConfig.setGlAccountDescription(glAccountTypeMaster.getStrGLAccountDescription());
		transactionConfig.setGlAccountTypeMaster(glAccountTypeMaster);

		journalTransfer.setTransactionConfig(transactionConfig);		
		return journalTransfer;
	}
	private JournalTransfer getToGLAccountTypeObject(JournalTransfer journalTransfer) throws Exception
	{
		GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
		glAccountTypeMaster.setStrAccountNumber(journalTransfer.getStrToAccountNumber());
		glAccountTypeMaster.setStrGLAccountType(journalTransfer.getStrToAccountType());
		
		glAccountTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(glAccountTypeMaster);
		
		TransactionConfig transactionConfig = new TransactionConfig();
		transactionConfig.setCode("S0000");
		transactionConfig.setTxnId(journalTransfer.getStrTxnId());
		transactionConfig.setTxnAmount(String.valueOf(journalTransfer.getStrAmoutToTransfer()));		
		transactionConfig.setAccountNo(journalTransfer.getStrToAccountNumber());
		transactionConfig.setGlAccountType(journalTransfer.getStrToAccountType());		
		transactionConfig.setAvailableAccountClosingBalance(glAccountTypeMaster.getStrClosingBalance());
		transactionConfig.setGlAccountDescription(glAccountTypeMaster.getStrGLAccountDescription());
		transactionConfig.setGlAccountTypeMaster(glAccountTypeMaster);
		
		journalTransfer.setTransactionConfig(transactionConfig);
		return journalTransfer;
	}
	
	private JournalTransfer getFromLinkedGLAccountTypeObject(JournalTransfer journalTransfer) throws Exception 
	{
		amsLogger.writeInfoLog("###Inside getFromLinkedGLAccountTypeObject journalTransfer::"+journalTransfer);
		GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
		//glAccountTypeMaster.setUserAccountType(journalTransfer.getStrFromAccountType());
		glAccountTypeMaster.setUserAccountType(journalTransfer.getFromLinkedGlType());//Added on 16-May-2023
		
		
		glAccountTypeMaster = glAccountTypeMasterService.getMappedGLAccountTypeMasterObjWithAccountType(glAccountTypeMaster);
		
		if (glAccountTypeMaster!=null && glAccountTypeMaster.getStrClosingBalance()!=null && glAccountTypeMaster.getStrClosingBalance().trim().length() > 0)
		{
			TransactionConfig transactionConfig = new TransactionConfig();
			transactionConfig.setCode("S0000");
			transactionConfig.setTxnId(journalTransfer.getStrTxnId());
			transactionConfig.setTxnAmount(String.valueOf(journalTransfer.getStrAmoutToTransfer()));		
			
			//transactionConfig.setAccountNo(journalTransfer.getStrToAccountNumber());
			//transactionConfig.setGlAccountType(journalTransfer.getStrToAccountType());		
			
			transactionConfig.setAccountNo(glAccountTypeMaster.getStrAccountNumber());
			transactionConfig.setGlAccountType(glAccountTypeMaster.getStrGLAccountType());
			
			transactionConfig.setAvailableAccountClosingBalance(glAccountTypeMaster.getStrClosingBalance());
			transactionConfig.setGlAccountDescription(glAccountTypeMaster.getStrGLAccountDescription());
			transactionConfig.setGlAccountTypeMaster(glAccountTypeMaster);
			
			journalTransfer.setTransactionConfig(transactionConfig);
			journalTransfer.setStrIsLinkedGLAccount("Y");
		}
		amsLogger.writeInfoLog("Inside getFromLinkedGLAccountTypeObject journalTransfer::"+journalTransfer);
		return journalTransfer;
	}
	
	private JournalTransfer getToLinkedGLAccountTypeObject(JournalTransfer journalTransfer) throws Exception 
	{
		amsLogger.writeInfoLog("--3-- Inside getToLinkedGLAccountTypeObject journalTransfer::"+journalTransfer);
		GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
		//glAccountTypeMaster.setUserAccountType(journalTransfer.getStrToAccountType());
		glAccountTypeMaster.setUserAccountType(journalTransfer.getToLinkedGlType()); //Added on 16-May-2023
		
		glAccountTypeMaster = glAccountTypeMasterService.getMappedGLAccountTypeMasterObjWithAccountType(glAccountTypeMaster);
		
		amsLogger.writeInfoLog("--3-- Inside getToLinkedGLAccountTypeObject glAccountTypeMaster::"+glAccountTypeMaster);
		if (glAccountTypeMaster!=null && glAccountTypeMaster.getStrClosingBalance()!=null && glAccountTypeMaster.getStrClosingBalance().trim().length() > 0)
		{
			TransactionConfig transactionConfig = new TransactionConfig();
			transactionConfig.setCode("S0000");
			transactionConfig.setTxnId(journalTransfer.getStrTxnId());
			transactionConfig.setTxnAmount(String.valueOf(journalTransfer.getStrAmoutToTransfer()));
			
			/*
			transactionConfig.setAccountNo(journalTransfer.getStrToAccountNumber());
			transactionConfig.setGlAccountType(journalTransfer.getStrToAccountType());
			*/
			
			transactionConfig.setAccountNo(glAccountTypeMaster.getStrAccountNumber());
			transactionConfig.setGlAccountType(glAccountTypeMaster.getStrGLAccountType());
			
			transactionConfig.setAvailableAccountClosingBalance(glAccountTypeMaster.getStrClosingBalance());
			transactionConfig.setGlAccountDescription(glAccountTypeMaster.getStrGLAccountDescription());
			transactionConfig.setGlAccountTypeMaster(glAccountTypeMaster);
			
			journalTransfer.setTransactionConfig(transactionConfig);
			journalTransfer.setStrIsLinkedGLAccount("Y");
		}
		return journalTransfer;
	}
	
	private AccountCreation getFromAccountMasterObject(JournalTransfer journalTransfer) throws Exception 
	{
		AccountCreation accountCreation = new AccountCreation();
		accountCreation.setStrAccountNumber(journalTransfer.getStrFromAccountNumber());
		accountCreation.setStrAccountType(journalTransfer.getStrFromAccountType());
		
		String availableBalance = accountMasterService.getAvailableBalanceBasedOnAccountTypeAndNumber(accountCreation);
		accountCreation.setAvailableBalance(availableBalance);
		
		return accountCreation;
	}
	
	private AccountCreation getToAccountMasterObject(JournalTransfer journalTransfer) throws Exception 
	{
		AccountCreation accountCreation = new AccountCreation();
		accountCreation.setStrAccountNumber(journalTransfer.getStrToAccountNumber());
		accountCreation.setStrAccountType(journalTransfer.getStrToAccountType());
		
		String availableBalance = accountMasterService.getAvailableBalanceBasedOnAccountTypeAndNumber(accountCreation);
		accountCreation.setAvailableBalance(availableBalance);
		
		return accountCreation;
	}
}
