package ams.cms.api.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.AgencyBankingRequest;
import ams.cms.api.model.AgencyBankingResponse;
import ams.cms.api.model.InflightTransactionMaster;
import ams.cms.config.CommonConstants;
import ams.cms.config.TransactionConfig;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountStatement;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountStatementService;
import ams.cms.services.GLAccountStatementService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.Utils;

@Component
public class AgencyBankingHandlerImpl implements AgencyBankingHandler
{
	private AMSLogger amsLogger = AMSLogger.getInstance(AgencyBankingHandlerImpl.class);
	
	@Autowired
	private GLAccountTypeMasterService glAccountTypeMasterService;
	
	@Autowired
	private UserTransactionHandler userTransactionHandler;
	
	@Autowired
	private AccountMasterService accountMasterService;
	
	@Autowired
	private AccountStatementService accountStatementService;
	
	@Autowired
	private GLAccountStatementService glAccountStatementService;

	@Override
	public void processAgentDepositToParkingGL(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster gLAccountTypeMaster) 
	{
		try 
		{
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());			
			
			String closingBal = (gLAccountTypeMaster.getStrClosingBalance() != null && gLAccountTypeMaster.getStrClosingBalance().trim().length() > 0) ? gLAccountTypeMaster.getStrClosingBalance().trim(): "0";
			Double balanceAmount = Double.parseDouble(closingBal);
			
			Double closingBalance = balanceAmount + txnAmount;
			String strClosingBalance = Utils.decimalFormat.format(closingBalance);	
			
			GLAccountTypeMaster linkedGLAccountType = new GLAccountTypeMaster();
			linkedGLAccountType.setStrGLAccountType(gLAccountTypeMaster.getStrGLAccountType());
			linkedGLAccountType.setStrAccountNumber(gLAccountTypeMaster.getStrAccountNumber());
			
			linkedGLAccountType.setStrClosingBalance(strClosingBalance);
			
			glAccountTypeMasterService.updateGLAccountTypeDetails(linkedGLAccountType);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void processParkingGLDebitUpdate(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster gLAccountTypeMaster) 
	{
		try 
		{
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());			
			
			String closingBal = (gLAccountTypeMaster.getStrClosingBalance() != null && gLAccountTypeMaster.getStrClosingBalance().trim().length() > 0) ? gLAccountTypeMaster.getStrClosingBalance().trim(): "0";
			Double balanceAmount = Double.parseDouble(closingBal);
			
			Double closingBalance = balanceAmount - txnAmount;
			String strClosingBalance = Utils.decimalFormat.format(closingBalance);	
			
			GLAccountTypeMaster linkedGLAccountType = new GLAccountTypeMaster();
			linkedGLAccountType.setStrGLAccountType(gLAccountTypeMaster.getStrGLAccountType());
			linkedGLAccountType.setStrAccountNumber(gLAccountTypeMaster.getStrAccountNumber());
			
			linkedGLAccountType.setStrClosingBalance(strClosingBalance);
			
			glAccountTypeMasterService.updateGLAccountTypeDetails(linkedGLAccountType);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}	
	
	@Override
	public void sendMailToCustomerDepositAtAgentOutlet(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			TransactionConfig transactionConfig = new TransactionConfig();
			transactionConfig.setStrEmail(transactionPostingConfig.getEmailId());
			transactionConfig.setParticipantId(transactionPostingConfig.getParticipantId());
			
			StringBuilder bodyMsg = new StringBuilder("Dear ");			
			bodyMsg.append(transactionPostingConfig.getAccountHolderName());
			bodyMsg.append(",");
			bodyMsg.append("<br/><br/>"); 
			
			if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				bodyMsg.append("Your account has been successfully deposited NGN "+transactionPostingConfig.getTxnAmount()+" at Agent Outlet ");
			}
			else
			{
				bodyMsg.append("Your account has been successfully deposited Rs."+transactionPostingConfig.getTxnAmount()+" at Agent Outlet ");
			}
			
			bodyMsg.append(" on "+Utils.simpleDateFormat2.format(transactionPostingConfig.getTxnDate())+" at "+Utils.simpleTimeFormat1.format(transactionPostingConfig.getTxnDate()));
		
			transactionConfig.setMailMessage(bodyMsg.toString());
			userTransactionHandler.sendEmail(transactionConfig);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void sendMailToCustomerWithdrawalAtAgentOutlet(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			TransactionConfig transactionConfig = new TransactionConfig();
			transactionConfig.setStrEmail(transactionPostingConfig.getEmailId());
			transactionConfig.setParticipantId(transactionPostingConfig.getParticipantId());
			
			StringBuilder bodyMsg = new StringBuilder("Dear ");			
			bodyMsg.append(transactionPostingConfig.getAccountHolderName());
			bodyMsg.append(",");
			bodyMsg.append("<br/><br/>"); 
			
			if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				bodyMsg.append("Your account has been successfully debited NGN "+transactionPostingConfig.getTxnAmount()+" at Agent Outlet ");
			}
			else
			{
				bodyMsg.append("Your account has been successfully debited Rs."+transactionPostingConfig.getTxnAmount()+" at Agent Outlet ");
			}
			
			bodyMsg.append(" on "+Utils.simpleDateFormat2.format(transactionPostingConfig.getTxnDate())+" at "+Utils.simpleTimeFormat1.format(transactionPostingConfig.getTxnDate()));
		
			transactionConfig.setMailMessage(bodyMsg.toString());
			userTransactionHandler.sendEmail(transactionConfig);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void updateAndAddFeeVatCreditInAgentAccount(InflightTransactionMaster inflightTransactionMaster) 
	{
		try 
		{
			TransactionPostingConfig toAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getToAccountNo());
			Double txnAmount = Utils.stringToDouble(toAccountTransConfig.getTxnAmount());
			
			AccountResponse toAccountResponse = toAccountTransConfig.getToAccount();
			
			AccountResponse agentAccountResponse = new AccountResponse();
			agentAccountResponse.setStrAccountNumber(toAccountResponse.getStrAccountNumber());
			agentAccountResponse.setStrAccountType(toAccountResponse.getStrAccountType());
			agentAccountResponse.setStrCustId(toAccountResponse.getStrCustId());
			
			String availableToAccountBalanace = toAccountResponse.getStrClosingBalance();
			double agentUpdatedClosingBal = Utils.stringToDouble(availableToAccountBalanace) + txnAmount;
			
			GLAccountTypeMaster linkedGLAccountTypeMaster = toAccountTransConfig.getToLinkGLAccount();
			
			GLAccountTypeMaster linkedGLAccountType = new GLAccountTypeMaster();
			linkedGLAccountType.setStrGLAccountType(linkedGLAccountTypeMaster.getStrGLAccountType());
			linkedGLAccountType.setStrAccountNumber(linkedGLAccountTypeMaster.getStrAccountNumber());
			linkedGLAccountType.setStrGLAccountDescription(linkedGLAccountTypeMaster.getStrGLAccountDescription());
			
			String availableToLinkedGLBalanace = linkedGLAccountTypeMaster.getStrClosingBalance();
			double agentUpdatedLinkedGLBalance = Utils.stringToDouble(availableToLinkedGLBalanace) + txnAmount;
			
			TransactionPostingConfig feeAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getFeeGLAccountNumber());
			String feeValue = feeAccountTransConfig.getFeeGLAccount().getFee();
			
			agentAccountResponse.setStrClosingBalance(Utils.decimalFormat.format(agentUpdatedClosingBal));
			linkedGLAccountType.setStrClosingBalance(Utils.decimalFormat.format(agentUpdatedLinkedGLBalance));
			
			toAccountTransConfig.setTxnAmount(feeValue);
			String isGLTypeVal = "F";
			updateAndAddAgentCreditAccount(toAccountTransConfig, agentAccountResponse, linkedGLAccountType, isGLTypeVal); /* -----1------- */
			
			agentUpdatedClosingBal = agentUpdatedClosingBal + Utils.stringToDouble(feeValue);
			agentUpdatedLinkedGLBalance = agentUpdatedLinkedGLBalance + Utils.stringToDouble(feeValue);			
			
			TransactionPostingConfig vatAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getVatGLAccountNumber());
			String vatValue = vatAccountTransConfig.getVatGLAccount().getVat();
			
			agentAccountResponse.setStrClosingBalance(Utils.decimalFormat.format(agentUpdatedClosingBal));
			linkedGLAccountType.setStrClosingBalance(Utils.decimalFormat.format(agentUpdatedLinkedGLBalance));
			
			toAccountTransConfig.setTxnAmount(vatValue);
			isGLTypeVal = "V";
			updateAndAddAgentCreditAccount(toAccountTransConfig, agentAccountResponse, linkedGLAccountType, isGLTypeVal);/* -----2------- */
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public HashMap<String, GLAccountTypeMaster> getAgencyBankingResponseMap(List<AgencyBankingResponse> agencyBankingResponseList) 
	{
		HashMap<String, GLAccountTypeMaster> map = new HashMap<String, GLAccountTypeMaster>();
		try 
		{
			for(AgencyBankingResponse agencyBankingResponse : agencyBankingResponseList) 
			{
				String reserveFieldValue = agencyBankingResponse.getReceipentAccountNo().trim();
				GLAccountTypeMaster glAccountTypeMaster = getGLAccountTypeMasterObject(agencyBankingResponse);
				if ("fee_charged".equalsIgnoreCase(reserveFieldValue)) 
				{
					map.put("fee_gl", glAccountTypeMaster);
				}
				else if("vat_charged".equalsIgnoreCase(reserveFieldValue)) 
				{
					map.put("vat_gl", glAccountTypeMaster);
				}
				else 
				{
					map.put("parking_gl", glAccountTypeMaster);
				}
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return map;
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
			accountStatement.setStrParticipantId(transactionPostingConfig.getParticipantId()); 
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
	
	private GLAccountTypeMaster getGLAccountTypeMasterObject(AgencyBankingResponse agencyBankingResponse) 
	{
		GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
		try 
		{
			glAccountTypeMaster.setStrGLAccountType(agencyBankingResponse.getGlAccountType());
			glAccountTypeMaster.setStrAccountNumber(agencyBankingResponse.getGlAccountNo());
			glAccountTypeMaster.setStrGLAccountDescription(agencyBankingResponse.getGlAccountDescription());
			glAccountTypeMaster.setStrClosingBalance(agencyBankingResponse.getGlAcccounBalance());
			glAccountTypeMaster.setTxnAmount(agencyBankingResponse.getTxnAmount());			
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return glAccountTypeMaster;
	}
}
