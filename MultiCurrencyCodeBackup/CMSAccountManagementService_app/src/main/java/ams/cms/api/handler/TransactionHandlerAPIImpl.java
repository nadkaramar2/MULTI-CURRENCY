package ams.cms.api.handler;

import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.dao.CustomerIdDao;
import ams.cms.api.model.AccountMaster;
import ams.cms.config.CommonConstants;
import ams.cms.config.TransactionConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountCreditCardTransactionModel;
import ams.cms.model.AccountStatement;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.RevolvingCreditCardMaster;
import ams.cms.model.RevolvingCreditCardTxnMaster;
import ams.cms.model.TransactionTypeModel;
import ams.cms.services.AccountCreditCardTxnServices;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountStatementService;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.RevolvingCreditCardService;
import ams.cms.services.RevolvingCreditCardTxnService;
import ams.cms.services.TransactionTypeService;
import ams.cms.txn.handler.GLTransactionHandler;
import ams.cms.utility.Utils;


@Component
public class TransactionHandlerAPIImpl implements TransactionHandlerAPI
{
	private AMSLogger amsLogger = AMSLogger.getInstance(TransactionHandlerAPIImpl.class);
	
	@Autowired
	private AccountMasterService accountMasterService;
	
	@Autowired
	private AccountStatementService accountStatementService;
	
	@Autowired 
	private AccountTranMasterService accountTranMasterService;
	
	@Autowired
	private TransactionTypeService transactionTypeService;
	
	@Autowired
	private AccountCreditCardTxnServices accountCreditCardTxnServices;
	
	@Autowired
	private RevolvingCreditCardTxnService revolvingCreditCardTxnService;
	
	@Autowired
	private RevolvingCreditCardService revolvingCreditCardService;
	
	@Autowired
	private GLTransactionHandler glTransactionHandler;
	
	@Autowired
	private CustomerIdDao customerIdDao;
	
	@Override
	public void updateSenderAccountMaster(AccountTranMaster accountTranMaster, AccountCreation senderAccountInfo) throws Exception 
	{
		Double txnAmount = Double.parseDouble(accountTranMaster.getStrTransaction_amount());
		
		AccountMaster accountMaster = new AccountMaster();	
		accountMaster.setStrAccountNumber(accountTranMaster.getSenderAccountNo());
		accountMaster.setStrCustId(accountTranMaster.getStrCustId());
		
		if ("C".equalsIgnoreCase(senderAccountInfo.getAccountCategoryType()))
		{
			updateSenderCreditAccountMaster(accountTranMaster, senderAccountInfo, accountMaster, txnAmount);
		}
		
		String closingBal = (senderAccountInfo.getStrClosingBalance() != null && senderAccountInfo.getStrClosingBalance().trim().length() > 0) ? senderAccountInfo.getStrClosingBalance().trim(): "0";
		Double balanceAmount = Double.parseDouble(closingBal);
		
		Double closingBalance = balanceAmount - txnAmount;
		String strClosingBalance = Utils.decimalFormat.format(closingBalance);
		
		accountMaster.setStrClosingBalance(strClosingBalance);
		
		//Added By Sunny soni for Handling Nigeria issues Start
		if(!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
		{
			Double availableDailyAmount = Double.parseDouble(senderAccountInfo.getStrAvailableDailyLimit()) - txnAmount;
			Double availableMonthlyAmount = Double.parseDouble(senderAccountInfo.getStrAvailableMonthlyLimit()) - txnAmount;
			Double availableYearlyAmount = Double.parseDouble(senderAccountInfo.getStrAvailableYearlyLimit()) - txnAmount;
			
			accountMaster.setStrAvailableDailyLimit(Utils.decimalFormat.format(availableDailyAmount));			
			accountMaster.setStrAvailableMonthlyLimit(Utils.decimalFormat.format(availableMonthlyAmount));			
			accountMaster.setStrAvailableYearlyLimit(Utils.decimalFormat.format(availableYearlyAmount));
		}
		//Added By Sunny soni for Handling Nigeria issues End
		
		accountTranMaster.setStrSenderClosingBalance(strClosingBalance);
		accountTranMaster.setStrSenderAccountType(senderAccountInfo.getStrAccountType());
		
		String tranType = (accountTranMaster.getStrTran_type()!=null && accountTranMaster.getStrTran_type().trim().length() > 0) ? accountTranMaster.getStrTran_type().trim() : "";   
		if (tranType.length() == 0) 
		{
			tranType = "WDL";
		}
		accountTranMaster.setStrTran_type(tranType);
		
		accountMasterService.updateAccountMasterFields(accountMaster);
	}
	
	private void updateSenderCreditAccountMaster(AccountTranMaster accountTranMaster, AccountCreation senderAccountInfo, AccountMaster accountMaster, Double txnAmount) 
	{
		try 
		{
			String strOutStandingBalance = (senderAccountInfo.getStrTotalOutstandingBal()!= null && senderAccountInfo.getStrTotalOutstandingBal().trim().length()>0) ? senderAccountInfo.getStrTotalOutstandingBal() : "0";
			
			Double outstandingBalance = Double.parseDouble(strOutStandingBalance);
			accountMaster.setStrTotalOutstandingBal(Utils.decimalFormat.format(outstandingBalance + txnAmount));
			
			Double availableCreditLimit =  Double.parseDouble(senderAccountInfo.getStrAvailableCreditLimit());
			accountMaster.setStrAvailableCreditLimit(Utils.decimalFormat.format(availableCreditLimit - txnAmount));
			
			accountTranMaster.setStrIsRevolvingCredit(senderAccountInfo.getStrIsRevolvingCredit());
			accountTranMaster.setAccountCategoryType(senderAccountInfo.getAccountCategoryType());
			
			accountTranMaster.setMccWiseGracePeriodInDays(senderAccountInfo.getMccWiseGracePeriodInDays());
			accountTranMaster.setRevolvingGracePeriodInDays(senderAccountInfo.getRevolvingGracePeriodInDays());
			accountTranMaster.setStrBillingCycleDate(senderAccountInfo.getStrBillingCycleDate());
			
			accountTranMaster.setStrTotalOutstandingBal(strOutStandingBalance);
			if (strOutStandingBalance!=null && Double.parseDouble(strOutStandingBalance) == 0)
			{
				accountMaster.setGracePeriodStartDate(Utils.getCurrentDate());
				if ("Y".equalsIgnoreCase(senderAccountInfo.getStrIsRevolvingCredit()))
				{
					accountMaster.setStrAvailableGracePeriod(senderAccountInfo.getRevolvingGracePeriodInDays());
					if (Double.parseDouble(strOutStandingBalance) == 0)
					{
						accountMaster.setGracePeriodStartDate(Utils.getCurrentDate());
						
						//Adding Payment Due date Start
						String billingYearAndMonthAndDay = Utils.getBillingYearAndMonthAndDay(accountTranMaster.getStrBillingCycleDate());
						Date paymentDueDate = Utils.getPaymentDueDate(billingYearAndMonthAndDay, Integer.parseInt(accountTranMaster.getRevolvingGracePeriodInDays()));
						accountMaster.setPayementDueDate(paymentDueDate);
						//Adding Payment Due date End
					}
				}
				else 
				{
					accountMaster.setStrAvailableGracePeriod(senderAccountInfo.getMccWiseGracePeriodInDays());
				}
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void updateSenderLinkedGLAccountMaster(AccountTranMaster accountTranMaster, AccountCreation senderAccountInfo) throws Exception
	{
		try 
		{
			Double txnAmount = Double.parseDouble(accountTranMaster.getStrTransaction_amount());
			
			GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
			glAccountTypeMaster.setTranId(accountTranMaster.getStrTxn_id());
			
			glAccountTypeMaster.setUserAccountType(senderAccountInfo.getStrGLAccountType());//Linked GL Account Type
			
			//glAccountTypeMaster.setStrGLAccountType("ULB");
			glAccountTypeMaster.setTranType("WDL");
			glAccountTypeMaster.setStrGLAccountDescription("Withdrawal");
			
			TransactionConfig transactionConfig = new TransactionConfig();
			transactionConfig.setTxnAmount(String.valueOf(txnAmount));
			transactionConfig.setGlAccountTypeMaster(glAccountTypeMaster);			
			
			glTransactionHandler.addAndUpdateGLAccount(transactionConfig);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void updateRecipientAccountMaster(AccountTranMaster accountTranMaster) throws Exception 
	{
		Double txnAmount = Double.parseDouble(accountTranMaster.getStrTransaction_amount());
		
		AccountCreation recipientAccountInfo = new AccountCreation();
		recipientAccountInfo.setStrAccountNumber(accountTranMaster.getReceipentAccountNo());
		
		//recipientAccountInfo = accountMasterService.getRecipientAccountInformation(recipientAccountInfo);
		recipientAccountInfo = accountMasterService.getRecipientAccountInformationWithoutCustId(recipientAccountInfo);
		
		String closingBalance = (recipientAccountInfo.getStrClosingBalance()!=null && recipientAccountInfo.getStrClosingBalance().trim().length()>0) ? recipientAccountInfo.getStrClosingBalance():"0";
		//Double closingBal = Double.parseDouble(recipientAccountInfo.getStrClosingBalance()) +  txnAmount;
		Double closingBal = Double.parseDouble(closingBalance) +  txnAmount;
		String strClosingBalance = Utils.decimalFormat.format(closingBal);
		
		AccountMaster accountMaster = new AccountMaster();	
		
		accountMaster.setStrClosingBalance(strClosingBalance);	
		accountMaster.setStrAccountNumber(recipientAccountInfo.getStrAccountNumber());
		accountMaster.setStrCustId(recipientAccountInfo.getStrCustId());			
		
		accountTranMaster.setStrReceipentAccountType(recipientAccountInfo.getStrAccountType());
		accountTranMaster.setStrReceipentClosingBalance(strClosingBalance);
		
		String tranType = (accountTranMaster.getStrTran_type()!=null && accountTranMaster.getStrTran_type().trim().length() > 0) ? accountTranMaster.getStrTran_type().trim() : "";   
		if (tranType.length() == 0) 
		{
			tranType = "DPT";
		}
		accountTranMaster.setStrTran_type(tranType);
		
		accountMasterService.updateAccountMasterFields(accountMaster);	
		
		//Creating GL Entry Start
		GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
		
		glAccountTypeMaster.setTranId(accountTranMaster.getStrTxn_id());
		
		glAccountTypeMaster.setUserAccountType(recipientAccountInfo.getStrGLAccountType());//Added Linked GL account Type.
		//glAccountTypeMaster.setStrGLAccountType("ULB");
		
		glAccountTypeMaster.setTranType("DPT");
		glAccountTypeMaster.setStrGLAccountDescription("Deposit");
		
		TransactionConfig transactionConfig = new TransactionConfig();
		transactionConfig.setTxnAmount(String.valueOf(txnAmount));
		transactionConfig.setGlAccountTypeMaster(glAccountTypeMaster);	
		
		glTransactionHandler.addAndUpdateGLAccount(transactionConfig);
		//Creating GL Entry End
	}

	@Override
	public void addSenderAccountStatement(AccountTranMaster accountTranMaster) throws Exception 
	{
		AccountStatement accountStatement = new AccountStatement();
		
		accountStatement.setStrAccountNumber(accountTranMaster.getSenderAccountNo());			
		accountStatement.setStrAccountType(accountTranMaster.getStrSenderAccountType());			
		accountStatement.setStrClosingBalance(accountTranMaster.getStrSenderClosingBalance());			
		accountStatement.setStrTransactionAmount(accountTranMaster.getStrTransaction_amount());			
		accountStatement.setStrTransactionID(accountTranMaster.getStrTxn_id());			
		accountStatement.setStrTransactionType(accountTranMaster.getStrTran_type());
		accountStatement.setStrParticipantId(accountTranMaster.getStrParticipantId());
		accountStatement.setStrNaration("Amount Transfer Successfully.");
		accountStatement.setStrIsGLType("N");
		accountStatement.setStrTransactionMode("DEBIT");
		
		accountStatementService.saveSenderAccountstAccountStatement(accountStatement);
	}

	@Override
	public void addRecipientAccountStatement(AccountTranMaster accountTranMaster) throws Exception 
	{
		AccountStatement accountStatement = new AccountStatement();
		
		accountStatement.setStrAccountNumber(accountTranMaster.getReceipentAccountNo());			
		accountStatement.setStrAccountType(accountTranMaster.getStrReceipentAccountType());			
		accountStatement.setStrClosingBalance(accountTranMaster.getStrReceipentClosingBalance());			
		accountStatement.setStrTransactionAmount(accountTranMaster.getStrTransaction_amount());			
		accountStatement.setStrTransactionID(accountTranMaster.getStrTxn_id());			
		accountStatement.setStrTransactionType(accountTranMaster.getStrTran_type());
		accountStatement.setStrParticipantId(accountTranMaster.getStrParticipantId());
		accountStatement.setStrIsGLType("N");
		accountStatement.setStrNaration("Amount Credited Successfully.");
		accountStatement.setStrTransactionMode("CREDIT");
		
		accountStatementService.saveRecipientAccountStatement(accountStatement);
	}

	@Override
	public void addTransactionMasterData(AccountTranMaster accountTranMaster) throws Exception 
	{
		TransactionTypeModel transactionTypeModel = getTransactionTypeObject(accountTranMaster);			
		accountTranMaster.setStrProcessingCode(transactionTypeModel.getStrProcessingCode());
		
		accountTranMaster.setStrFrom_account_number(accountTranMaster.getSenderAccountNo());
		accountTranMaster.setStrTo_account_number(accountTranMaster.getReceipentAccountNo());
		accountTranMaster.setStrTxn_id(accountTranMaster.getStrTxn_id());
		accountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
		accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());
		accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
		accountTranMaster.setStrResponseCode("00");
		accountTranMaster.setStrAuthCode(accountTranMaster.getStrAuthCode());//Added Auth Code changes on 25-July-2023
		
		accountTranMasterService.addAccountTransactionData(accountTranMaster);
	}

	@Override
	public void addCreditCardRelatedEntry(AccountTranMaster accountTranMaster) throws Exception 
	{
		//Double outStandingBalance = Double.parseDouble(accountTranMaster.getStrTotalOutstandingBal());
		
		TransactionTypeModel transactionTypeModel = getTransactionTypeObject(accountTranMaster);			
		
		if ("Y".equalsIgnoreCase(accountTranMaster.getStrIsRevolvingCredit())) 
		{
			RevolvingCreditCardTxnMaster revolvingCreditCardTxnMaster = new RevolvingCreditCardTxnMaster();
			revolvingCreditCardTxnMaster.setStrAccountNumber(accountTranMaster.getSenderAccountNo());
			revolvingCreditCardTxnMaster.setStrAccountType(accountTranMaster.getStrSenderAccountType());
			
			RevolvingCreditCardMaster revolvingCreditCardMaster = new RevolvingCreditCardMaster();
			
			String billingYearAndMonthAndDay = Utils.getBillingYearAndMonthAndDay(accountTranMaster.getStrBillingCycleDate());
			billingYearAndMonthAndDay = billingYearAndMonthAndDay.substring(billingYearAndMonthAndDay.indexOf("-") + 1);
			
			revolvingCreditCardMaster.setStrBillingMonthAndDay(billingYearAndMonthAndDay);
			revolvingCreditCardMaster.setStrGracePeriodInDays(accountTranMaster.getRevolvingGracePeriodInDays());
			
			String remainingGracePeriod = revolvingCreditCardService.getRemainingGracePeriodInDays(revolvingCreditCardMaster);
			
			/*
			if (outStandingBalance == 0)
			{
				revolvingCreditCardTxnMaster.setStrRemaningGracePeriod(accountTranMaster.getRevolvingGracePeriodInDays());
			}
			else
			{
				RevolvingCreditCardMaster revolvingCreditCardMaster = new RevolvingCreditCardMaster();
				revolvingCreditCardMaster.setStrAccounNumber(accountTranMaster.getSenderAccountNo());
				
				String remainingGracePeriodInDays =	revolvingCreditCardService.getRemainingGracePeriodInDays(revolvingCreditCardMaster);
				if (remainingGracePeriodInDays!=null && Long.parseLong(remainingGracePeriodInDays) == 0)
				{
					remainingGracePeriodInDays = accountTranMaster.getRevolvingGracePeriodInDays();
				}
				
				revolvingCreditCardTxnMaster.setStrRemaningGracePeriod(remainingGracePeriodInDays);
			}
			*/
			revolvingCreditCardTxnMaster.setStrRemaningGracePeriod(remainingGracePeriod);
			revolvingCreditCardTxnMaster.setStrTranType(accountTranMaster.getStrTran_type());
			
			revolvingCreditCardTxnMaster.setStrTranTypeDes(transactionTypeModel.getStrTxnTypeDescrp());
			
			revolvingCreditCardTxnMaster.setStrTxnAmount(accountTranMaster.getStrTransaction_amount());
			revolvingCreditCardTxnMaster.setStrTxnId(accountTranMaster.getStrTxn_id());
			revolvingCreditCardTxnMaster.setTxnDate(Utils.getCurrentDate());
			revolvingCreditCardTxnMaster.setTxnTime(Utils.getFormattedCurrentTime());
			
			revolvingCreditCardTxnService.addAccountTransactionData(revolvingCreditCardTxnMaster);
		}
		else
		{
			AccountCreditCardTransactionModel accountCreditCardTransactionModel = new AccountCreditCardTransactionModel();
			
			accountCreditCardTransactionModel.setStrAccountNumber(accountTranMaster.getSenderAccountNo());
			accountCreditCardTransactionModel.setStrAccountType(accountTranMaster.getStrSenderAccountType());
			accountCreditCardTransactionModel.setStrIsPaid("N");
			accountCreditCardTransactionModel.setTxnDate(Utils.getCurrentDate());
			accountCreditCardTransactionModel.setStrTransactionAmount(accountTranMaster.getStrTransaction_amount());
			accountCreditCardTransactionModel.setStrTransactionType(accountTranMaster.getStrTran_type());
			accountCreditCardTransactionModel.setStrTranTypeDescription(transactionTypeModel.getStrTxnTypeDescrp());
			accountCreditCardTransactionModel.setStrTxnTime(Utils.getStrTime());
			
			accountCreditCardTxnServices.addAccountCreditCardTransactionModel(accountCreditCardTransactionModel);
		}
	}

	@Override
	public TransactionTypeModel getTransactionTypeObject(AccountTranMaster accountTranMaster) throws Exception
	{
		TransactionTypeModel transactionTypeModel = null;
		try 
		{
			transactionTypeModel = new TransactionTypeModel();
			if (accountTranMaster.getStrTran_type()!=null && accountTranMaster.getStrTran_type().trim().length() > 0) 
			{
				transactionTypeModel.setStrTxnTypeKeyWord(accountTranMaster.getStrTran_type());			
			}
			transactionTypeModel = transactionTypeService.getTransactionTypeMaster(transactionTypeModel);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionTypeModel;
	}

	@Override
	public void updateAccountMasterSomeFields(AccountCreation accountCreation) throws Exception
	{
		accountMasterService.updateAccountMasterSomeFields(accountCreation);
	}
	
	@Override
	public void reduceEarMarkBalance(AccountTranMaster accountTranMaster, AccountCreation accountCreation) {
		
		String strFrom_account_number = accountTranMaster.getStrFrom_account_number();
		String strTransaction_amount = accountTranMaster.getStrTransaction_amount();
		double transactionAmount = Double.parseDouble(strTransaction_amount);
		
		double earMarkAmount = accountCreation.getStrEarMarkAmount();
		accountCreation.setStrAccountNumber(strFrom_account_number);
		
		double newEarMark = earMarkAmount - transactionAmount;
		accountCreation.setStrEarMarkAmount(newEarMark);
		
		String strClosingBalance = accountCreation.getStrClosingBalance();
		Double closingBalance = Double.valueOf(strClosingBalance);
		double newClosingBalance = closingBalance - transactionAmount;
		String strNewClosingBalance = String.valueOf(newClosingBalance);
		accountCreation.setStrClosingBalance(strNewClosingBalance);
		
		accountMasterService.reduceEarBalance(accountCreation);
		
		// Creating GL Entry Start
		GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
		glAccountTypeMaster.setTranId(accountTranMaster.getStrTxn_id());

		glAccountTypeMaster.setUserAccountType(accountCreation.getStrGLAccountType());// Linked GL Account Type

		// glAccountTypeMaster.setStrGLAccountType("ULB");
		glAccountTypeMaster.setTranType("WDL");
		glAccountTypeMaster.setStrGLAccountDescription("Withdrawal");

		TransactionConfig transactionConfig = new TransactionConfig();
		transactionConfig.setTxnAmount(String.valueOf(strTransaction_amount));
		transactionConfig.setGlAccountTypeMaster(glAccountTypeMaster);

		try 
		{
			glTransactionHandler.addAndUpdateGLAccount(transactionConfig);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		// Creating GL Entry End
		//should create statement here itself
	}
	//created by ankit on 09-06-2023

	@Override
	public void addPreCredAmount(AccountTranMaster accountTranMaster,AccountCreation accountCreation)
	{
		Double strPreCredAmount = accountCreation.getStrPreCredAmount();
		String strClosingBalance = accountCreation.getStrClosingBalance();
		double closingBalance = Double.parseDouble(strClosingBalance);
		String strTransaction_amount = accountTranMaster.getStrTransaction_amount();
		double transactionAmount = Double.parseDouble(strTransaction_amount);
		
		double newClosingBalance = closingBalance + transactionAmount;
		double newPreCredAmount = strPreCredAmount - transactionAmount;
		
		accountCreation.setStrPreCredAmount(newPreCredAmount);
		accountCreation.setStrClosingBalance(String.valueOf(newClosingBalance));
		accountMasterService.addPreCredAmount(accountCreation);
		
		//Creating GL Entry Start
		GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
				
		glAccountTypeMaster.setTranId(accountTranMaster.getStrTxn_id());
				
		glAccountTypeMaster.setUserAccountType(accountCreation.getStrGLAccountType());
		//Added Linked GL account Type.
		//glAccountTypeMaster.setStrGLAccountType("ULB");
				
		glAccountTypeMaster.setTranType("DPT");
		glAccountTypeMaster.setStrGLAccountDescription("Deposit");
				
		TransactionConfig transactionConfig = new TransactionConfig();
		transactionConfig.setTxnAmount(String.valueOf(transactionAmount));
		transactionConfig.setGlAccountTypeMaster(glAccountTypeMaster);	
				
		try
		{
			glTransactionHandler.addAndUpdateGLAccount(transactionConfig);
		} 
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public TransactionConfig verifyCustomerPin(AccountCreation accountMaster) throws Exception 
	{
		TransactionConfig transactionConfig = new TransactionConfig(); 
		
		CustomerIdCreation  customerIdCreation = new CustomerIdCreation();
		customerIdCreation.setStrCustId(accountMaster.getStrCustId());
		
		String s = ams.cms.utility.Utils.generateHash(accountMaster.getStrCustPin());
		System.out.println(s);
		String existingPIN = customerIdDao.getCustomerPIN(customerIdCreation);
		if (existingPIN != null)
		{
			if (existingPIN.equals(ams.cms.utility.Utils.generateHash(accountMaster.getStrCustPin()))) 
			{
				transactionConfig.setCode("S0000");
				transactionConfig.setMessage("Successfully Matched.");
			}
			else 
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Pin not matched!");
			}
		}
		else 
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Pin not exist!");
		}
		return transactionConfig;
	}
}
