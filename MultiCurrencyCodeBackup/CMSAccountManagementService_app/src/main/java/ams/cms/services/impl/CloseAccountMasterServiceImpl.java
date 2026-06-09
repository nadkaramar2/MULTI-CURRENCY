package ams.cms.services.impl;

import java.util.ArrayList;
import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.CustomerIdDao;
import ams.cms.api.model.AccountMaster;
import ams.cms.api.service.impl.UpgradeTierReqResSerivceImpl;
import ams.cms.config.TransactionIdCreationConfigDao;
import ams.cms.dao.AccountMasterDao;
import ams.cms.dao.AccountStatementDao;
import ams.cms.dao.AccountTranMasterDao;
import ams.cms.dao.AccountTypeMasterDao;
import ams.cms.dao.CloseAccountMasterDao;
import ams.cms.dao.GLAccountStatementDao;
import ams.cms.dao.GLAccountTypeMasterDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountStatement;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.BeneficiaryTxnMaster;
import ams.cms.model.CloseAccountMaster;
import ams.cms.model.CloseAccountResponse;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.notification.EmailTemplate;
import ams.cms.notification.SmsHandler;
import ams.cms.notification.email.EmailService;
import ams.cms.services.BeneficiaryTxnMasterService;
import ams.cms.services.CloseAccountMasterService;
import ams.cms.services.CustomerIDCreationService;
import ams.cms.utility.Utils;

@Transactional
@Service
public class CloseAccountMasterServiceImpl implements CloseAccountMasterService 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(CloseAccountMasterServiceImpl.class);

	@Autowired
	private CloseAccountMasterDao closeAccountMasterDao;

	@Autowired
	private AccountMasterDao accountMasterDao;

	@Autowired
	private EmailService emailService;

	@Autowired
	private SmsHandler smsHandler;

	@Autowired
	private GLAccountStatementDao glAccountStatementDao;
	
	@Autowired
	private CustomerIdDao customerIdDao;

	@Autowired
	private AccountTypeMasterDao accountTypeMasterDao;

	@Autowired
	private GLAccountTypeMasterDao glAccountTypeMasterDao;

	@Autowired
	private AccountTranMasterDao accountTranMasterDao;

	@Autowired
	private AccountStatementDao accountStatementDao;

	@Autowired
	private BeneficiaryTxnMasterService beneficiaryTxnMasterService;
	
	@Autowired
	private CustomerIDCreationService customerMasterService;
	
	@Autowired
	private TransactionIdCreationConfigDao transactionIdCreationConfigDao;

	@Override
	public CloseAccountResponse saveCloseAccountData(CloseAccountMaster closeAccountMaster) 
	{
		CloseAccountResponse closeAccountResponse = new CloseAccountResponse();
		closeAccountResponse.setCode("S0000");
		closeAccountResponse.setStatus("Success");
		try
		{
			String tranId = transactionIdCreationConfigDao.getTransactionId();
			
			closeAccountMaster.setTransactionId(tranId);
			closeAccountMaster.setClosureReqDate(Utils.getCurrentDate());
			closeAccountMaster.setClosureReqTime(Utils.getFormattedCurrentTime());
			closeAccountMaster.setRequestStatus("pending");
			
			CloseAccountMaster closerAccountMaster = saveCloseAccountMaster(closeAccountMaster);
			
			if (closerAccountMaster!=null && closerAccountMaster.getStrID()!=null) 
			{
				AccountMaster accountMaster = new AccountMaster();
				accountMaster.setStrAccountNumber(closeAccountMaster.getAccountNo());
				accountMaster.setStrStatus("PreClosure");
				accountMaster.setStrPreClosureDate(Utils.getCurrentDate());
				int updateResult =	accountMasterDao.updateAccountStatus(accountMaster);
				
				if (updateResult > 0) 
				{
					sendMailToGroup(closerAccountMaster);
					
					//sendSMStoCustomer(closerAccountMaster);
				}
				else
				{
					closeAccountResponse.setCode("E0000");
					closeAccountResponse.setStatus("Failed");
					closeAccountResponse.setMsg("Issue while updating account status of closure.");
				}
			}
			else
			{
				closeAccountResponse.setCode("E0000");
				closeAccountResponse.setStatus("Failed");
				closeAccountResponse.setMsg("Issue while adding closure request data.");
			}
		}
		catch (Exception e) 
		{
			closeAccountResponse.setCode("E0000");
			closeAccountResponse.setStatus("Failed");
			closeAccountResponse.setMsg("Internal Server Error.");
			amsLogger.writeExceptionLog("In saveCloseAccountData Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return closeAccountResponse;
	}

	private void sendMailToGroup(CloseAccountMaster closerAccountMaster) 
	{
		try 
		{
			StringBuilder bodyMsg = new StringBuilder("Dear User,");
			bodyMsg.append("<br/><br/>");
			bodyMsg.append("Account Closure has been initiated by Maker ");
			bodyMsg.append(closerAccountMaster.getMakerUserId());
			bodyMsg.append(" on " );
			bodyMsg.append(Utils.simpleDateFormat4.format(closerAccountMaster.getClosureReqDate()));
			bodyMsg.append(" ");
			bodyMsg.append(Utils.simpleTimeFormat1.format(closerAccountMaster.getClosureReqTime()));
			bodyMsg.append(" for ");
			bodyMsg.append(closerAccountMaster.getStrAccountType());
			bodyMsg.append(" having account Number ");
			bodyMsg.append(closerAccountMaster.getAccountNo());
			bodyMsg.append(" of Tier ");
			bodyMsg.append( closerAccountMaster.getCurrentAccountTier());
			bodyMsg.append(" of Mr/Mrs ");
			bodyMsg.append( closerAccountMaster.getAccountHolderName());
			bodyMsg.append(" having balance of ");
			bodyMsg.append(closerAccountMaster.getCurrentAccountBalance());
			bodyMsg.append(".");
			bodyMsg.append("<br/>");
			bodyMsg.append("Please note the reason for ");
			bodyMsg.append(closerAccountMaster.getClosureReason());
			bodyMsg.append(".");
			bodyMsg.append("<br/>");
			bodyMsg.append("Kindly log in to back-office and Approve/Reject the Closure request.");
			
			String emailSubject = "Account Clouser Request";
			System.out.println(bodyMsg.toString());
			sendMailToMultiplePerson(bodyMsg.toString(), emailSubject);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("In sendMailToGroup Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void sendSMStoCustomer(CloseAccountMaster closeAccountMaster) 
	{
		try
		{
			StringBuilder SmsbodyMsg = new StringBuilder("Dear Mr/Mrs " + closeAccountMaster.getAccountHolderName() + "your account closure process has been initiated for");
			SmsbodyMsg.append(" " + closeAccountMaster.getStrAccountType() + " " + closeAccountMaster.getAccountNo() + ". Your Balance amount will be transferred to " + closeAccountMaster.getRecipientAccountNo());
			SmsbodyMsg.append(" You will receive the confirmation shortly once the account is closed by the concerned authority and the balance transferred to respective account.");
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("In sendSMStoCustomer Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void sendMailToMultiplePerson(String bodyMsg, String subject) 
	{
		try {
			ArrayList<String> toMailId = Utils.getGroupMailId();
			EmailTemplate emailTemplate = Utils.getEmailTemplateForSendMailToMultiple("contactus@AMStechnologies.com", toMailId, subject, bodyMsg);
			emailService.sendSimpleHtmlContentMessage(emailTemplate);
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public CloseAccountResponse makerProcessToAccountClouser(CloseAccountMaster closeAccountMaster) throws Exception {
		AccountMaster accountMaster = new AccountMaster();
		accountMaster.setStrAccountNumber(closeAccountMaster.getAccountNo());
		AccountMaster accountMasterData = accountMasterDao.getAccountMasterByAccountNumber(accountMaster);
		
		// based on approve or reject
		if (closeAccountMaster.getRequestStatus().equalsIgnoreCase("Approved")) {
			// 1-> update status as closed in account master and account Blance as 0
			
			final String tranModeDebit = "DEBIT";
			final String tranTypeDebit = "WDL";
			
			final String tranModeCredit = "CREDIT";
			final String tranTypeCredit = "DPT";
			final String naration = "Amount Transfer Successfully.";
			
			final String ThirdPartyGLAccountNo = "999111111121";
			final String ThirdPartyGlAccountType = "MPS";
			
			final String controlAccountNo = "00023123000001";
			final String controlAccountType = "CTR";
			
			if (accountMasterData != null) {
				accountMasterData.setStrStatus("Closed");
				accountMasterData.setStrOpeningBalance("0");
				accountMasterDao.updateAccountStatus(accountMasterData);

				// reduce the linked GlAccount Statement
				// 2.1 = get the Account Type master by account type
				ams.cms.model.AccountTypeMaster accountTypeMaster = new ams.cms.model.AccountTypeMaster();
				accountTypeMaster.setStrAccountType(accountMasterData.getStrAccountType());
				ams.cms.model.AccountTypeMaster accountTypeMasterData = accountTypeMasterDao.getGLAccTypeAccNoOnAccType(accountTypeMaster);
				if (accountTypeMasterData != null) {
					GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
					glAccountTypeMaster.setStrAccountNumber(accountTypeMasterData.getStrGLAccountNumber());
					glAccountTypeMaster.setStrGLAccountType(accountTypeMasterData.getStrGLAccountType());
					GLAccountTypeMaster glAccountTypeMasterData = glAccountTypeMasterDao.getSingleGLAccountTypeMasterObj(glAccountTypeMaster);
					if (glAccountTypeMasterData != null) {
						
						//calling 3PArty Gl AccountType Master
						GLAccountTypeMaster thirdPartyGlMaster = new GLAccountTypeMaster();
						thirdPartyGlMaster.setStrAccountNumber(ThirdPartyGLAccountNo);
						thirdPartyGlMaster.setStrGLAccountType(ThirdPartyGlAccountType);
						GLAccountTypeMaster thirdPartyGlMasterAccount = glAccountTypeMasterDao.getSingleGLAccountTypeMasterObj(thirdPartyGlMaster);
						
						
						//calling 3PArty Gl AccountType Master
						GLAccountTypeMaster controlMasterAcc = new GLAccountTypeMaster();
						controlMasterAcc.setStrAccountNumber(controlAccountNo);
						controlMasterAcc.setStrGLAccountType(controlAccountType);
						GLAccountTypeMaster controlAccountMaster = glAccountTypeMasterDao.getSingleGLAccountTypeMasterObj(thirdPartyGlMaster);
						
						
						
						// cal minus the transfer amt in GlAccountTypeMaster and update
						Double finalClosingBlance = Double.parseDouble(glAccountTypeMasterData.getStrClosingBalance()) - closeAccountMaster.getTransferAmount();
						glAccountTypeMasterData.setStrAccountNumber(accountTypeMasterData.getStrGLAccountNumber());
						glAccountTypeMasterData.setStrGLAccountType(accountTypeMasterData.getStrGLAccountType());
						glAccountTypeMasterData.setStrClosingBalance(String.valueOf(finalClosingBlance));
						glAccountTypeMasterDao.updateGLAccountTypeDetails(glAccountTypeMasterData);

						// 3-> If option selected to transfer to external account
						if (closeAccountMaster.getBalanceTransferTo().equalsIgnoreCase("External Linked")) {
							// 3.1 --> Create entry in Tran Master and insert From Account as Customer Account and To Account as the 3rd Party GL account also mention transaction
							AccountTranMaster accountTranMaster = new AccountTranMaster();
							accountTranMaster.setStrTxn_id(closeAccountMaster.getTransactionId());
							accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());
							accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
							accountTranMaster.setStrTransaction_amount(String.valueOf(closeAccountMaster.getTransferAmount()));
							accountTranMaster.setStrFrom_account_number(accountMaster.getStrAccountNumber());
							accountTranMaster.setStrTo_account_number(glAccountTypeMasterData.getStrAccountNumber());
							accountTranMaster.setStrTran_type("TPT");
							accountTranMaster.setStrParticipantId("0");
							accountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
							accountTranMasterDao.save(accountTranMaster);

							// 3.2 --> Create entry in Account Statement for Customer Account with Mode as  Debit.
						
							AccountStatement accountStatement = new AccountStatement();
							accountStatement.setStrAccountNumber(accountMasterData.getStrAccountNumber());
							accountStatement.setStrAccountType(accountMasterData.getStrAccountType());
							accountStatement.setStrClosingBalance(accountMasterData.getStrClosingBalance());
							accountStatement.setStrIsGLType("Y");
							accountStatement.setStrTransactionAmount(String.valueOf(closeAccountMaster.getTransferAmount()));
							accountStatement.setTransactionDate(Utils.getCurrentDate());
							accountStatement.setStrNaration(naration);
							accountStatement.setStrTransactionID(closeAccountMaster.getTransactionId());
							accountStatement.setStrTransactionType(tranTypeDebit);
							accountStatement.setStrTransactionMode(tranModeDebit);
							accountStatementDao.save(accountStatement);

							// 3.3 --> Create entry in GL Account statement for Linked GL with Mode as Debit
							GLAccountStatement glAccountStatement = new GLAccountStatement();
							glAccountStatement.setStrAccountNumber(glAccountTypeMasterData.getStrAccountNumber());// gl  account
							glAccountStatement.setStrGLAccountType(glAccountTypeMasterData.getStrAccountType());// gl account type																										
							glAccountStatement.setStrAmount(String.valueOf(closeAccountMaster.getTransferAmount()));// doubt
							glAccountStatement.setStrClosingBalance(glAccountTypeMasterData.getStrClosingBalance());// doubt??
							glAccountStatement.setTransactionDate(new Date());
							glAccountStatement.setStrTranType(tranTypeDebit);
							glAccountStatement.setStrTxnId(accountTranMaster.getStrTxn_id());
							glAccountStatement.setStrTranMode(tranModeDebit);
							glAccountStatement.setStrGLAccountType(glAccountTypeMasterData.getStrGLAccountType());
							glAccountStatement.setCreatedDate(Utils.getCurrentDate());
							glAccountStatementDao.save(glAccountStatement);

							// 3.4 --> Increase balance in 3rd Party GL account based on the amount transferred
							GLAccountTypeMaster glAccountTypeMasterObj = new GLAccountTypeMaster();
							glAccountTypeMasterObj.setStrAccountNumber(ThirdPartyGLAccountNo);// static 3party gl account																							
							glAccountTypeMasterObj.setStrGLAccountType(ThirdPartyGlAccountType);// static 3party gl account
							String ReduceControlAccountBlance = glAccountTypeMasterDao.getClosingBalanceOfGlAccount(glAccountTypeMasterObj);// issue whith 0 blance
							Double thirdPartyGlClosingBlance = Double.parseDouble(ReduceControlAccountBlance) + closeAccountMaster.getTransferAmount();
							glAccountTypeMasterObj.setStrClosingBalance(String.valueOf(thirdPartyGlClosingBlance));
							glAccountTypeMasterDao.updateGLAccountTypeDetails(glAccountTypeMasterObj);

							// 3.5 --> Create entry in GL Account Statement of 3rd Party GL Account with Mode as Credit
							GLAccountStatement glAccountStatementData = new GLAccountStatement();
							glAccountStatementData.setStrAccountNumber(ThirdPartyGLAccountNo);// static 3party gl  account
							glAccountStatementData.setStrGLAccountType(ThirdPartyGlAccountType);// static 3party gl account
							glAccountStatementData.setStrAmount(String.valueOf(closeAccountMaster.getTransferAmount()));
							glAccountStatementData.setTransactionDate(new Date());
							glAccountStatementData.setStrTranType(tranTypeCredit);
							glAccountStatementData.setStrTxnId(closeAccountMaster.getTransactionId());
							glAccountStatementData.setStrCreated_by("System");
							glAccountStatementData.setStrClosingBalance(thirdPartyGlMasterAccount.getStrClosingBalance());//hear will come 3 party gl closing blance
							glAccountStatementData.setStrTranMode(tranModeCredit);
							glAccountStatementData.setCreatedDate(Utils.getCurrentDate());
							glAccountStatementDao.save(glAccountStatementData);

							// 3.6 --> Reduce balance in 3rd Party GL account based on the amount to be
							// transferred
							GLAccountTypeMaster reduceThirdPartyGlAccountType = new GLAccountTypeMaster();
							reduceThirdPartyGlAccountType.setStrAccountNumber(ThirdPartyGLAccountNo);// static 3party gl account
							reduceThirdPartyGlAccountType.setStrGLAccountType(ThirdPartyGlAccountType);// static 3party gl account
							String ReduceControlAccountBlanceAmt = glAccountTypeMasterDao.getClosingBalanceOfGlAccount(reduceThirdPartyGlAccountType);
							Double thirdPartyGlClosingBlanceAmt = Double.parseDouble(ReduceControlAccountBlanceAmt) - closeAccountMaster.getTransferAmount();
							reduceThirdPartyGlAccountType.setStrClosingBalance(String.valueOf(thirdPartyGlClosingBlanceAmt));
							glAccountTypeMasterDao.updateGLAccountTypeDetails(reduceThirdPartyGlAccountType);

							// 3.7 --> Increase balance in Control Account based on the amount to be
							// transferred
							GLAccountTypeMaster controlAccountGlTypeMaster = new GLAccountTypeMaster();
							controlAccountGlTypeMaster.setStrAccountNumber(controlAccountNo);// control Account
							controlAccountGlTypeMaster.setStrGLAccountType(controlAccountType);// static control account
							String controlAccountBlance = glAccountTypeMasterDao.getClosingBalanceOfGlAccount(controlAccountGlTypeMaster);
							Double strfinalClosingBlance = Double.valueOf(controlAccountBlance) + Double.valueOf(closeAccountMaster.getTransferAmount());
							controlAccountGlTypeMaster.setStrClosingBalance(String.valueOf(strfinalClosingBlance));
							glAccountTypeMasterDao.updateGLAccountTypeDetails(controlAccountGlTypeMaster);

							// 3.8 --> Create entry in Tran Master and update From Account as 3rd Party GL Account and To Account as the Control account .also mention transaction type
							AccountTranMaster glAccountTranMaster = new AccountTranMaster();
							glAccountTranMaster.setStrTxn_id(closeAccountMaster.getTransactionId());
							glAccountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());
							glAccountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
							glAccountTranMaster.setStrTransaction_amount(String.valueOf(closeAccountMaster.getTransferAmount()));
							glAccountTranMaster.setStrFrom_account_number(ThirdPartyGLAccountNo);// gl account number
							glAccountTranMaster.setStrTo_account_number(controlAccountNo);// control account number
							glAccountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
							glAccountTranMaster.setStrTran_type("TPT");
							accountTranMasterDao.save(glAccountTranMaster);

							// 3.9 --> Create entry in GL Account Statement of 3rd Party GL Account with
							// Mode as Debit
							GLAccountStatement glAccountStatementObj = new GLAccountStatement();
							glAccountStatementObj.setStrAccountNumber(ThirdPartyGLAccountNo);
							glAccountStatementObj.setStrGLAccountType(ThirdPartyGlAccountType);
							glAccountStatementObj.setStrAmount(String.valueOf(closeAccountMaster.getTransferAmount()));
							glAccountStatementObj.setTransactionDate(new Date());
							glAccountStatementObj.setStrTranType(tranTypeDebit);
							glAccountStatementObj.setStrTxnId(closeAccountMaster.getTransactionId());
							glAccountStatementObj.setStrCreated_by("System");
							glAccountStatementObj.setStrTranMode(tranModeDebit);
							glAccountStatementObj.setStrClosingBalance(thirdPartyGlMasterAccount.getStrClosingBalance());
							glAccountStatementObj.setCreatedDate(Utils.getCurrentDate());
							glAccountStatementDao.save(glAccountStatementObj);

							// 3.10 --> Create entry in GL Account Statement of Control Account with Mode as
							// Credit
							GLAccountStatement glAccountStatementForControlAccount = new GLAccountStatement();
							glAccountStatementForControlAccount.setStrAccountNumber(controlAccountNo);
							glAccountStatementForControlAccount.setStrGLAccountType(controlAccountType);
							glAccountStatementForControlAccount.setStrAmount(String.valueOf(closeAccountMaster.getTransferAmount()));
							glAccountStatementForControlAccount.setTransactionDate(new Date());
							glAccountStatementForControlAccount.setStrTranType(tranTypeCredit);
							glAccountStatementForControlAccount.setStrTxnId(closeAccountMaster.getTransactionId());
							glAccountStatementForControlAccount.setStrCreated_by("System");
							glAccountStatementForControlAccount.setStrTranMode(tranModeCredit);
							glAccountStatementForControlAccount.setCreatedDate(Utils.getCurrentDate());
							glAccountStatementForControlAccount.setStrClosingBalance(controlAccountMaster.getStrClosingBalance());
							glAccountStatementDao.save(glAccountStatementForControlAccount);

							// 3.11 --> Reduce balance of Control Account based on the amount to be
							// transferred
							GLAccountTypeMaster controlAccountGlTypeMasterObj = new GLAccountTypeMaster();
							controlAccountGlTypeMasterObj.setStrAccountNumber(controlAccountNo);// control Account
							controlAccountGlTypeMasterObj.setStrGLAccountType(controlAccountType);// static control account
																									
							String controlAccountBlanceAmt = glAccountTypeMasterDao.getClosingBalanceOfGlAccount(controlAccountGlTypeMasterObj);
							Double strfinalClosingBlanceAmt = Double.valueOf(controlAccountBlanceAmt) - Double.valueOf(closeAccountMaster.getTransferAmount());
							controlAccountGlTypeMasterObj.setStrClosingBalance(String.valueOf(strfinalClosingBlanceAmt));
							glAccountTypeMasterDao.updateGLAccountTypeDetails(controlAccountGlTypeMasterObj);

							// 3.12 --> Create entry in Tran Master and update From Account as Control
							// account also mention transaction type as “TOT”
							AccountTranMaster controlAccountTranMaster = new AccountTranMaster();
							controlAccountTranMaster.setStrTxn_id(closeAccountMaster.getTransactionId());
							controlAccountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());
							controlAccountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
							controlAccountTranMaster.setStrTransaction_amount(String.valueOf(closeAccountMaster.getTransferAmount()));
							controlAccountTranMaster.setStrFrom_account_number(controlAccountNo);// gl account number
						  //controlAccountTranMaster.setStrTo_account_number();//doubt which one ? 
							controlAccountTranMaster.setStrTran_type("TOT");
							controlAccountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
							accountTranMasterDao.save(controlAccountTranMaster);// ???

							// 3.13 --> Create entry in GL Account statement of Control Account with Mode as
							// Debit
							GLAccountStatement glAccountStatementForControlAccountDebit = new GLAccountStatement();
							glAccountStatementForControlAccountDebit.setStrAccountNumber(controlAccountNo);
							glAccountStatementForControlAccountDebit.setStrGLAccountType(controlAccountType);
							glAccountStatementForControlAccountDebit.setStrAmount(String.valueOf(closeAccountMaster.getTransferAmount()));
							glAccountStatementForControlAccountDebit.setTransactionDate(new Date());
							glAccountStatementForControlAccountDebit.setStrTranType(tranTypeDebit);
							glAccountStatementForControlAccountDebit.setStrTxnId(closeAccountMaster.getTransactionId());
							glAccountStatementForControlAccountDebit.setStrCreated_by("System");
							glAccountStatementForControlAccountDebit.setStrTranMode(tranModeDebit);
							glAccountStatementForControlAccountDebit.setStrClosingBalance(controlAccountMaster.getStrClosingBalance());
							glAccountStatementForControlAccountDebit.setCreatedDate(Utils.getCurrentDate());
							glAccountStatementDao.save(glAccountStatementForControlAccountDebit);

							// 3.14 --> Update the following table with appropriate details
							BeneficiaryTxnMaster beneficiaryTxnMaster = new BeneficiaryTxnMaster();
							// beneficiaryTxnMaster.setStrBeneficiaryAccountName(controlAccountBlanceAmt);
							// beneficiaryTxnMaster.setStrBeneficiaryAccountNo(controlAccountBlanceAmt);
							beneficiaryTxnMaster.setStrBeneficiaryBankIfsc(closeAccountMaster.getRecipientBankId());
							// beneficiaryTxnMaster.setStrBeneficiaryBankName(controlAccountBlanceAmt);
							beneficiaryTxnMaster.setStrFromAccountNo(accountMasterData.getStrAccountNumber());
							beneficiaryTxnMaster.setStrFromAccountType(accountMasterData.getStrAccountType());
							beneficiaryTxnMaster.setStrTxnAmount(String.valueOf(closeAccountMaster.getTransferAmount()));
							beneficiaryTxnMaster.setStrTxnId(closeAccountMaster.getTransactionId());
							beneficiaryTxnMaster.setTxnDate(Utils.getCurrentDate());
							beneficiaryTxnMaster.setTxnTime(Utils.getFormattedCurrentTime());
							// beneficiaryTxnMaster.setStrTxnStatus();
							// beneficiaryTxnMaster.setStrTxnType(controlAccountBlanceAmt);
							beneficiaryTxnMasterService.saveBeneficiaryTxnMasterRecords(beneficiaryTxnMaster);

						}
						// 4> If option selected to transfer to internal account whether linked to same
						// Cust ID or different account
						else if (closeAccountMaster.getBalanceTransferTo().equalsIgnoreCase("Internal Linked")) {
							// 4.1 --> Create Entry in Tran Master with Tran Type as “TRF” updating From and
							
							//get to account details
							//calling 3PArty Gl AccountType Master
							GLAccountTypeMaster recipientAccountDetails = new GLAccountTypeMaster();
							recipientAccountDetails.setStrAccountNumber(closeAccountMaster.getRecipientAccountNo());
							recipientAccountDetails.setStrGLAccountType(closeAccountMaster.getRecipientAccountType());
							GLAccountTypeMaster recipientAccountDetailsObj = glAccountTypeMasterDao.getSingleGLAccountTypeMasterObj(recipientAccountDetails);
							
							// To account numbers
							AccountTranMaster internalAccountTranMaster = new AccountTranMaster();
							internalAccountTranMaster.setStrTxn_id(closeAccountMaster.getTransactionId());
							internalAccountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());
							internalAccountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
							internalAccountTranMaster.setStrTransaction_amount(String.valueOf(closeAccountMaster.getTransferAmount()));
							internalAccountTranMaster.setStrFrom_account_number(closeAccountMaster.getAccountNo());
							internalAccountTranMaster.setStrTo_account_number(closeAccountMaster.getRecipientAccountNo());
							internalAccountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
							internalAccountTranMaster.setStrTran_type("TRT");
							accountTranMasterDao.save(internalAccountTranMaster);

							// 4.2--> Create Entry in Account Statement for From Account with Mode as Debit
							AccountStatement accountStatementForInternal = new AccountStatement();
							accountStatementForInternal.setStrAccountNumber(closeAccountMaster.getAccountNo());
							accountStatementForInternal.setStrAccountType(closeAccountMaster.getStrAccountType());
							accountStatementForInternal.setStrClosingBalance(accountMasterData.getStrClosingBalance());
							accountStatementForInternal.setStrIsGLType("N");
							accountStatementForInternal.setStrTransactionAmount(String.valueOf(closeAccountMaster.getTransferAmount()));
							accountStatementForInternal.setTransactionDate(Utils.getCurrentDate());
							accountStatementForInternal.setStrNaration(naration);
							accountStatementForInternal.setStrTransactionID(closeAccountMaster.getTransactionId());
							accountStatementForInternal.setStrTransactionType(tranTypeDebit);
							accountStatementForInternal.setStrTransactionMode(tranModeDebit);
							accountStatementDao.save(accountStatementForInternal);

							// 4.3 --> Create Entry in Linked GL Account Statement of the From Account Type
							// linked GL with Mode as Debit
							GLAccountStatement glAccountStatementData = new GLAccountStatement();
							glAccountStatementData.setStrAccountNumber(glAccountTypeMasterData.getStrAccountNumber());
							glAccountStatementData.setStrGLAccountType(glAccountTypeMasterData.getStrAccountType());
							glAccountStatementData.setStrAmount(String.valueOf(closeAccountMaster.getTransferAmount()));
							glAccountStatementData.setTransactionDate(new Date());
							glAccountStatementData.setStrTranType(tranTypeDebit);
							glAccountStatementData.setStrTxnId(closeAccountMaster.getTransactionId());
							glAccountStatementData.setStrCreated_by("System");
							glAccountStatementData.setStrTranMode(tranModeDebit);
							glAccountStatementData.setCreatedDate(Utils.getCurrentDate());
							glAccountStatementDao.save(glAccountStatementData);
						

							// 4.4 -->Create Entry in Account Statement for To Account with Mode as Credit
							AccountStatement accountStatement = new AccountStatement();
							accountStatement.setStrAccountNumber(closeAccountMaster.getRecipientAccountNo());
							accountStatement.setStrAccountType(closeAccountMaster.getRecipientAccountType());
							accountStatement.setStrClosingBalance(recipientAccountDetailsObj.getStrClosingBalance());
							accountStatement.setStrIsGLType("N");
							accountStatementForInternal.setStrTransactionAmount(String.valueOf(closeAccountMaster.getTransferAmount()));
							accountStatement.setTransactionDate(Utils.getCurrentDate());
							accountStatement.setStrNaration(naration);
							accountStatement.setStrTransactionID(closeAccountMaster.getTransactionId());
							accountStatement.setStrTransactionType(tranTypeCredit);
							accountStatement.setStrTransactionMode(tranModeCredit);
							accountStatementDao.save(accountStatement);

							// 4.5 --> Create Entry in Linked GL Account Statement of the To Account Type linked GL with Mode as Credit

							GLAccountStatement linkedGlAccountStatementData = new GLAccountStatement();
							linkedGlAccountStatementData.setStrAccountNumber(glAccountTypeMasterData.getStrAccountNumber());
							glAccountStatementData.setStrGLAccountType(glAccountTypeMasterData.getStrGLAccountType());
							linkedGlAccountStatementData.setStrAmount(String.valueOf(closeAccountMaster.getTransferAmount()));
							linkedGlAccountStatementData.setTransactionDate(new Date());
							linkedGlAccountStatementData.setStrTranType(tranTypeDebit);
							linkedGlAccountStatementData.setStrTxnId(closeAccountMaster.getTransactionId());
							linkedGlAccountStatementData.setStrCreated_by("System");
							linkedGlAccountStatementData.setStrTranMode(tranModeDebit);
							linkedGlAccountStatementData.setCreatedDate(Utils.getCurrentDate());
							glAccountStatementDao.save(linkedGlAccountStatementData);// not save

							// 4.6 --> Increase Balance of To Account and Linked GL Account

							Double finalClosingBlanceAmt = Double.parseDouble(glAccountTypeMasterData.getStrClosingBalance() + closeAccountMaster.getTransferAmount());
							glAccountTypeMasterData.setStrAccountNumber(accountTypeMasterData.getStrLastAccNumber());
							glAccountTypeMasterData.setStrGLAccountType(accountTypeMasterData.getStrAccountType());
							glAccountTypeMasterData.setStrClosingBalance(String.valueOf(finalClosingBlanceAmt));
							glAccountTypeMasterDao.updateGLAccountTypeDetails(glAccountTypeMasterData);

						}

						// Get the Account Clouser Data and Update the Closed Account Table
						CloseAccountMaster closeAccountMasterObj = new CloseAccountMaster();
						closeAccountMasterObj.setAccountClosedDate(Utils.getCurrentDate());
						closeAccountMasterObj.setAccountClosedTime(Utils.getFormattedCurrentTime());
						closeAccountMasterObj.setStrAccountType(closeAccountMaster.getStrAccountType());
						closeAccountMasterObj.setAccountNo(closeAccountMaster.getAccountNo());
						closeAccountMasterObj.setStrCustId(closeAccountMaster.getStrCustId());
						closeAccountMasterObj.setChekerUserId(closeAccountMaster.getChekerUserId());
						closeAccountMasterObj.setRequestStatus(closeAccountMaster.getRequestStatus());
						closeAccountMasterObj.setTransactionId(closeAccountMaster.getTransactionId());
						closeAccountMasterDao.updateCloseAccountMaster(closeAccountMasterObj);

						// Send email to Back-office
						StringBuilder bodyMsg = new StringBuilder("Dear User, Account has been Closed by Checker Id "
								+ closeAccountMaster.getChekerUserId());
						bodyMsg.append(" on " + closeAccountMaster.getStrClosureReqDate() + "  "
								+ closeAccountMaster.getAccountClosureRejectedTime() + " for "
								+ closeAccountMaster.getStrAccountType());
						bodyMsg.append(" having account Number " + closeAccountMaster.getAccountNo() + " of Tier "
								+ closeAccountMaster.getCurrentAccountTier() + "" + " of Mr/Mrs "
								+ closeAccountMaster.getAccountHolderName() + " having balance of "
								+ closeAccountMaster.getCurrentAccountBalance() + ".");
						bodyMsg.append(" Please note the reason for " + closeAccountMaster.getClosureReason()
								+ ".  Kindly make a note of this update .");
						String emailSubject = "Account Clouser Request";
						System.out.println(bodyMsg.toString());
						sendMailToMultiplePerson(bodyMsg.toString(), emailSubject);

						// Send SMS to Customer
						StringBuilder SmsbodyMsg = new StringBuilder("Dear Mr/Mrs  "
								+ closeAccountMaster.getAccountHolderName() + "  your account has been closed ");
						SmsbodyMsg.append(" " + closeAccountMaster.getStrAccountType() + " "
								+ closeAccountMaster.getAccountNo() + " . Your Balance amount will be transferred to  "
								+ closeAccountMaster.getRecipientAccountNo());
						SmsbodyMsg.append(" Your Balance amount has been transferred to "
								+ closeAccountMaster.getRecipientBankId()
								+ " . This is for your confirmation and future reference. ");
						System.out.println(bodyMsg.toString());
					   // smsHandler.processToSendSmS(accountMaster.getStrPhoneNo(),SmsbodyMsg);
						

						// Send email to Customer

						StringBuilder Smsbody = new StringBuilder("Dear Mr/Mrs "
								+ closeAccountMaster.getAccountHolderName() + "your account has been closed ");
						Smsbody.append(" " + closeAccountMaster.getStrAccountType() + " "
								+ closeAccountMaster.getAccountNo() + ". Your Balance amount has been transferred to"
								+ closeAccountMaster.getRecipientAccountNo());
						Smsbody.append(
								"Your Balance amount has been transferred to " + closeAccountMaster.getRecipientBankId()
										+ ". This is for your confirmation and future reference.");

						String customerEmailSubject = "Account Clouser Request";
						System.out.println(Smsbody.toString());

						sendMailToMultiplePerson(Smsbody.toString(), customerEmailSubject);
					}
				}
			}
		} else if (closeAccountMaster.getRequestStatus().equalsIgnoreCase("Rejected")) {
			// Update Account Status to Active and Pre-Closure Date to blank
			if (accountMasterData != null) {
				accountMasterData.setStrStatus("Active");
				accountMasterData.setStrPreClosureDate(null);
				accountMasterDao.updateAccountStatus(accountMaster);

				// Update the Closed Account Table
				CloseAccountMaster closeAccountMasterRejectedObj = new CloseAccountMaster();
		
				closeAccountMasterRejectedObj.setStrCustId(closeAccountMaster.getStrCustId());
				closeAccountMasterRejectedObj.setStrAccountType(closeAccountMaster.getStrAccountType());
				closeAccountMasterRejectedObj.setAccountNo(closeAccountMaster.getAccountNo());
				closeAccountMasterRejectedObj.setChekerUserId(closeAccountMaster.getChekerUserId());
				closeAccountMasterRejectedObj.setRequestStatus(closeAccountMaster.getRequestStatus());
				closeAccountMasterRejectedObj.setReasonForRejection(closeAccountMaster.getReasonForRejection());
				closeAccountMasterRejectedObj.setAccountClosureRejectedDate(Utils.getCurrentDate());
				closeAccountMasterRejectedObj.setAccountClosureRejectedTime(Utils.getFormattedCurrentTime());
				closeAccountMasterDao.updateCloseAccountMasterForRejection(closeAccountMasterRejectedObj);
				
				
				// Send email to Back-office
				StringBuilder bodyMsg = new StringBuilder(
						"“Dear User, Account has been closure has been Rejected by Checker"
								+ closeAccountMaster.getChekerUserId());
				bodyMsg.append("on" + closeAccountMaster.getAccountClosureRejectedDate() + " "
						+ closeAccountMaster.getAccountClosureRejectedTime() + "for"
						+ closeAccountMaster.getStrAccountType());
				bodyMsg.append("having account Number " + closeAccountMaster.getAccountNo() + " of Tier "
						+ closeAccountMaster.getCurrentAccountTier() + "" + " of Mr/Mrs "
						+ closeAccountMaster.getAccountHolderName() + " having balance of "
						+ closeAccountMaster.getCurrentAccountBalance() + ".");
				bodyMsg.append("<br/>");
				bodyMsg.append("Please note the reason for " + closeAccountMaster.getReasonForRejection()
						+ ".  Kindly make a note of this update .");
				bodyMsg.append("<br/><br/><br/>");
				String emailSubject = "Account Clouser Request";
				sendMailToMultiplePerson(bodyMsg.toString(), emailSubject);

				// Send SMS to Customer
				StringBuilder SmsbodyMsg = new StringBuilder("Dear Mr/Mrs" + closeAccountMaster.getAccountHolderName()
						+ "your account closure has been rejected  ");
				bodyMsg.append(" " + closeAccountMaster.getStrAccountType() + " " + closeAccountMaster.getAccountNo()
						+ ". Please note the reason for rejection" + closeAccountMaster.getReasonForRejection());
				bodyMsg.append("This is for your confirmation and future reference");
				//smsHandler.processToSendSmS(accountMaster.getStrPhoneNo(),SmsbodyMsg.toString());
				

				// Send email to Customer

				StringBuilder Smsbody = new StringBuilder("Dear Mr/Mrs" + closeAccountMaster.getAccountHolderName()
						+ "your account closure has been rejected ");
				bodyMsg.append(" " + closeAccountMaster.getStrAccountType() + " " + closeAccountMaster.getAccountNo()
						+ ". Please note the reason for rejection " + closeAccountMaster.getReasonForRejection());
				bodyMsg.append("This is for your confirmation and future reference");

				String customerEmailSubject = "Account Clouser Request";
				sendMailToMultiplePerson(Smsbody.toString(), customerEmailSubject);

			}
		}
		return null;

	}

	@Override
	public CloseAccountMaster saveCloseAccountMaster(CloseAccountMaster closeAccountMaster) {
		closeAccountMasterDao.save(closeAccountMaster);
		return closeAccountMaster;
	}
	
}
