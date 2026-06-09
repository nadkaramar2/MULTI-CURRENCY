package ams.cms.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.handler.AgencyBankingHandler;
import ams.cms.api.handler.ThirdPartyTxnHandler;
import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.InflightTransactionMaster;
import ams.cms.api.model.TierAccountResponse;
import ams.cms.api.service.TransactionPostingIF;
import ams.cms.config.CommonConstants;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountStatement;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.TierAccountMaster;
import ams.cms.model.TransactionTypeModel;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountStatementService;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.GLAccountStatementService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.services.TierAccountMasterService;
import ams.cms.services.TransactionTypeService;
import ams.cms.utility.AccountUtility;
import ams.cms.utility.Utils;

@Service
public class TransactionPosting implements TransactionPostingIF
{
	private AMSLogger amsLogger = AMSLogger.getInstance(TransactionPosting.class);
	
	private Map<String, InflightTransactionMaster> transactionMap = new HashMap<String, InflightTransactionMaster>();
	
	@Autowired
	private AccountStatementService accountStatementService;
	
	@Autowired
	private GLAccountStatementService glAccountStatementService;
	
	@Autowired
	private TierAccountMasterService tierAccountMasterService;
	
	@Autowired
	private AccountMasterService accountMasterService;
	
	@Autowired
	private GLAccountTypeMasterService glAccountTypeMasterService;
	
	@Autowired
	private TransactionTypeService transactionTypeService;
	
	@Autowired 
	private AccountTranMasterService accountTranMasterService;
	
	@Autowired
	private AgencyBankingHandler agencyBankingHandler;
	
	@Autowired
	private ThirdPartyTxnHandler thirdPartyTxnHandler;

	public Map<String, InflightTransactionMaster> getTransactionMap() {
		return transactionMap;
	}

	public void setTransactionMap(Map<String, InflightTransactionMaster> transactionMap) {
		this.transactionMap = transactionMap;
	} 

	@Override
	public void createTransactionPostingData(TransactionPostingConfig transactionPostingConfig)
	{
		try 
		{
			String txnId = transactionPostingConfig.getTxnId();
			InflightTransactionMaster inflightTransactionMaster = new InflightTransactionMaster();
			
			inflightTransactionMaster.setTransactionId(txnId);
			inflightTransactionMaster.setParticipantId(transactionPostingConfig.getParticipantId());
			inflightTransactionMaster.setTxnDate(transactionPostingConfig.getTxnDate());
			inflightTransactionMaster.setTxnTime(transactionPostingConfig.getTxnTime());
			inflightTransactionMaster.setTxnType(transactionPostingConfig.getTxnType());
			inflightTransactionMaster.setTxnAmount(transactionPostingConfig.getTxnAmount());
			
			inflightTransactionMaster.setAuthCode(transactionPostingConfig.getAuthCode());
			inflightTransactionMaster.setStrSrcTxnId(transactionPostingConfig.getStrSrcTxnId());
			inflightTransactionMaster.setResponseCode(transactionPostingConfig.getResponseCode());
			
			inflightTransactionMaster.setIsFeeTypeExist(transactionPostingConfig.getIsFeeTypeExist());
			
			inflightTransactionMaster.setFee(transactionPostingConfig.getFee());
			inflightTransactionMaster.setVat(transactionPostingConfig.getFee());
			inflightTransactionMaster.setCommission(transactionPostingConfig.getCommission());
			inflightTransactionMaster.setFeeApplicableTo(transactionPostingConfig.getFeeApplicableTo());
			
			HashMap<String, TransactionPostingConfig> dataMap = new HashMap<String, TransactionPostingConfig>();
			
			if(transactionPostingConfig.getFromAccount() != null && transactionPostingConfig.getFromAccount().getStrAccountNumber().trim()!=null && transactionPostingConfig.getFromAccount().getStrAccountNumber().trim().length() > 0)
			{
				dataMap.put(transactionPostingConfig.getFromAccount().getStrAccountNumber().trim(), transactionPostingConfig);
				inflightTransactionMaster.setFromAccountNo(transactionPostingConfig.getFromAccount().getStrAccountNumber().trim());
			}
			if(transactionPostingConfig.getToAccount() != null && transactionPostingConfig.getToAccount().getStrAccountNumber() != null && transactionPostingConfig.getToAccount().getStrAccountNumber().trim().length() > 0 )
			{
				dataMap.put(transactionPostingConfig.getToAccount().getStrAccountNumber().trim(), transactionPostingConfig);
				inflightTransactionMaster.setToAccountNo(transactionPostingConfig.getToAccount().getStrAccountNumber().trim());
			}			
			if(transactionPostingConfig.getFromLinkGLAccount() != null && transactionPostingConfig.getFromLinkGLAccount().getStrAccountNumber() != null && transactionPostingConfig.getFromLinkGLAccount().getStrAccountNumber().trim().length()> 0 )
			{
				dataMap.put(transactionPostingConfig.getFromLinkGLAccount().getStrAccountNumber().trim(), transactionPostingConfig);
				inflightTransactionMaster.setFromLinkedGLAccountNumber(transactionPostingConfig.getFromLinkGLAccount().getStrAccountNumber().trim());
			}			
			if(transactionPostingConfig.getToLinkGLAccount() != null && transactionPostingConfig.getToLinkGLAccount().getStrAccountNumber() != null && transactionPostingConfig.getToLinkGLAccount().getStrAccountNumber().trim().length() > 0)
			{
				dataMap.put(transactionPostingConfig.getToLinkGLAccount().getStrAccountNumber().trim(), transactionPostingConfig);
				inflightTransactionMaster.setToLinkedGLAccountNumber(transactionPostingConfig.getToLinkGLAccount().getStrAccountNumber().trim());
			}
			if(transactionPostingConfig.getFeeGLAccount() != null && transactionPostingConfig.getFeeGLAccount().getStrAccountNumber() != null && transactionPostingConfig.getFeeGLAccount().getStrAccountNumber().trim().length() > 0)
			{
				dataMap.put(transactionPostingConfig.getFeeGLAccount().getStrAccountNumber().trim(), transactionPostingConfig);
				inflightTransactionMaster.setFeeGLAccountNumber(transactionPostingConfig.getFeeGLAccount().getStrAccountNumber().trim());
			}
			if(transactionPostingConfig.getVatGLAccount() != null && transactionPostingConfig.getVatGLAccount().getStrAccountNumber() != null && transactionPostingConfig.getVatGLAccount().getStrAccountNumber().trim().length() > 0)
			{
				dataMap.put(transactionPostingConfig.getVatGLAccount().getStrAccountNumber().trim(), transactionPostingConfig);
				inflightTransactionMaster.setVatGLAccountNumber(transactionPostingConfig.getVatGLAccount().getStrAccountNumber().trim()); 
			}
			if(transactionPostingConfig.getGlAccountTypeMaster()!=null && transactionPostingConfig.getGlAccountTypeMaster().getStrAccountNumber() != null && transactionPostingConfig.getGlAccountTypeMaster().getStrAccountNumber().trim().length() > 0) 
			{
				dataMap.put(transactionPostingConfig.getGlAccountTypeMaster().getStrAccountNumber().trim(), transactionPostingConfig);
				inflightTransactionMaster.setGlAccountNumber(transactionPostingConfig.getGlAccountTypeMaster().getStrAccountNumber().trim());
			}
			if(transactionPostingConfig.getCtrlGlAccountTypeMaster()!=null && transactionPostingConfig.getCtrlGlAccountTypeMaster().getStrAccountNumber() != null && transactionPostingConfig.getCtrlGlAccountTypeMaster().getStrAccountNumber().trim().length() > 0) 
			{
				dataMap.put(transactionPostingConfig.getCtrlGlAccountTypeMaster().getStrAccountNumber().trim(), transactionPostingConfig);
				inflightTransactionMaster.setCtrlGlAccountNumber(transactionPostingConfig.getCtrlGlAccountTypeMaster().getStrAccountNumber().trim()); 
			}
			
			inflightTransactionMaster.setDataMap(dataMap);
			transactionMap.put(txnId, inflightTransactionMaster);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private String getLinkedGLUpdatedBalance(TransactionPostingConfig transactionPostingConfig, String glAccounNumber) 
	{
		return transactionPostingConfig.getLinkedGLMap().get(glAccounNumber);
	}
	private void updatedBalance(TransactionPostingConfig transactionPostingConfig, String glAccounNumber, String updatedBal) 
	{
		transactionPostingConfig.getLinkedGLMap().put(glAccounNumber, updatedBal);
	}
	
	@Override
	public void processClosedLoopTxnPosting(TransactionPostingConfig transactionPostingConfig) 
	{
		try
		{
			String txnId = transactionPostingConfig.getTxnId();
			InflightTransactionMaster inflightTransactionMaster = transactionMap.get(txnId);
			
			addUpdateSenderAccount(inflightTransactionMaster);
			
			if (inflightTransactionMaster.getIsFeeTypeExist()) 
			{
				feeVatGLEntries(inflightTransactionMaster);
				
				TransactionPostingConfig transactionPostingConf = getTransactionPostingConfigInstance(inflightTransactionMaster);
				
				transactionPostingConf.setFeeGLAccount(inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getFeeGLAccountNumber()).getFeeGLAccount());
				addFeeReleateTranMasterEntry(inflightTransactionMaster.getFromAccountNo(), transactionPostingConf);
				
				transactionPostingConf.setVatGLAccount(inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getVatGLAccountNumber()).getVatGLAccount());
				addVatReleateTranMasterEntry(inflightTransactionMaster.getFromAccountNo(), transactionPostingConf);
			}
			
			addUpdateRecipentAccount(inflightTransactionMaster);
						
			addClosedLoopTxnTranMasterEntry(inflightTransactionMaster);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	@Override
	public void processThirdPartyTransactionPosting(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			String txnId = transactionPostingConfig.getTxnId();
			InflightTransactionMaster inflightTransactionMaster = transactionMap.get(txnId);
			
			addUpdateSenderAccount(inflightTransactionMaster);
			
			if (inflightTransactionMaster.getIsFeeTypeExist()) 
			{
				feeVatGLEntries(inflightTransactionMaster);
				
				TransactionPostingConfig transactionPostingConf = getTransactionPostingConfigInstance(inflightTransactionMaster);
				
				transactionPostingConf.setFeeGLAccount(inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getFeeGLAccountNumber()).getFeeGLAccount());
				addFeeReleateTranMasterEntry(inflightTransactionMaster.getFromAccountNo(), transactionPostingConf);
				
				transactionPostingConf.setVatGLAccount(inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getVatGLAccountNumber()).getVatGLAccount());
				addVatReleateTranMasterEntry(inflightTransactionMaster.getFromAccountNo(), transactionPostingConf);
			}
			
			TransactionPostingConfig fromAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getFromAccountNo());
			
			thirdPartyTxnHandler.processCreditThirdPartyGLBalanceUpdate(fromAccountTransConfig, fromAccountTransConfig.getGlAccountTypeMaster());
			
			addCreditGLAccountStatementEntry(fromAccountTransConfig, fromAccountTransConfig.getGlAccountTypeMaster());
			
			TransactionPostingConfig transactionPostingConf = getTransactionPostingConfigInstance(inflightTransactionMaster);
			transactionPostingConf.setFromAccountNo(fromAccountTransConfig.getFromAccount().getStrAccountNumber());
			transactionPostingConf.setToAccountNo(fromAccountTransConfig.getGlAccountTypeMaster().getStrAccountNumber());
			
			addMultipleTxnTranMasterEntry(transactionPostingConf);
			
			thirdPartyTxnHandler.processDebitThirdPartyGLBalanceUpdate(fromAccountTransConfig, fromAccountTransConfig.getGlAccountTypeMaster());
			
			addDebitGLAccountStatementEntry(fromAccountTransConfig, fromAccountTransConfig.getGlAccountTypeMaster());			
			
			thirdPartyTxnHandler.processCreditControlGLBalanceUpdate(fromAccountTransConfig, fromAccountTransConfig.getCtrlGlAccountTypeMaster());
			
			addCreditGLAccountStatementEntry(fromAccountTransConfig, fromAccountTransConfig.getCtrlGlAccountTypeMaster());
			
			//TransactionPostingConfig transactionPostingConf1 = getTransactionPostingConfigInstance(inflightTransactionMaster);
			transactionPostingConf.setFromAccountNo(fromAccountTransConfig.getGlAccountTypeMaster().getStrAccountNumber());
			transactionPostingConf.setToAccountNo(fromAccountTransConfig.getCtrlGlAccountTypeMaster().getStrAccountNumber());
			
			addMultipleTxnTranMasterEntry(transactionPostingConf);
			
			thirdPartyTxnHandler.processDebitControlGLBalanceUpdate(fromAccountTransConfig, fromAccountTransConfig.getCtrlGlAccountTypeMaster());
			
			TransactionPostingConfig transactionPostingConf2 = getTransactionPostingConfigInstance(inflightTransactionMaster);
			
			transactionPostingConf2.setTxnType("TOT");
			addDebitGLAccountStatementEntry(transactionPostingConf2, fromAccountTransConfig.getCtrlGlAccountTypeMaster());
			
			transactionPostingConf2.setAccountNo(fromAccountTransConfig.getCtrlGlAccountTypeMaster().getStrAccountNumber());
			addAccountTransactionData(transactionPostingConf2);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void processAgentDepositTxnPosting(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			String txnId = transactionPostingConfig.getTxnId();
			InflightTransactionMaster inflightTransactionMaster = transactionMap.get(txnId);
			
			addUpdateSenderAccount(inflightTransactionMaster);
			
			if (inflightTransactionMaster.getIsFeeTypeExist()) 
			{
				feeVatGLEntries(inflightTransactionMaster);
				
				TransactionPostingConfig transactionPostingConf = getTransactionPostingConfigInstance(inflightTransactionMaster);
				transactionPostingConf.setStrReservefield2("pending");
				
				transactionPostingConf.setFeeGLAccount(inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getFeeGLAccountNumber()).getFeeGLAccount());
				addFeeReleateTranMasterEntry(inflightTransactionMaster.getFromAccountNo(), transactionPostingConf);
				
				transactionPostingConf.setVatGLAccount(inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getVatGLAccountNumber()).getVatGLAccount());
				addVatReleateTranMasterEntry(inflightTransactionMaster.getFromAccountNo(), transactionPostingConf);
			}
			
			TransactionPostingConfig fromAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getFromAccountNo());
			agencyBankingHandler.processAgentDepositToParkingGL(fromAccountTransConfig, fromAccountTransConfig.getGlAccountTypeMaster());
			
			addCreditGLAccountStatementEntry(fromAccountTransConfig, fromAccountTransConfig.getGlAccountTypeMaster());
			
			TransactionPostingConfig transactionPostingConf = getTransactionPostingConfigInstance(inflightTransactionMaster);
			transactionPostingConf.setFromAccountNo(fromAccountTransConfig.getFromAccount().getStrAccountNumber());
			transactionPostingConf.setToAccountNo(fromAccountTransConfig.getGlAccountTypeMaster().getStrAccountNumber());
			
			TransactionPostingConfig toAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getToAccountNo());
			transactionPostingConf.setStrReservefield1(toAccountTransConfig.getToAccount().getStrAccountNumber());
			transactionPostingConf.setStrReservefield2("pending");
			addMultipleTxnTranMasterEntry(transactionPostingConf);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	@Override
	public void processAgentWithdrawalTxnPosting(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			String txnId = transactionPostingConfig.getTxnId();
			InflightTransactionMaster inflightTransactionMaster = transactionMap.get(txnId);
			
			addUpdateSenderAccount(inflightTransactionMaster);
			
			TransactionPostingConfig fromAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getFromAccountNo());
			agencyBankingHandler.processAgentDepositToParkingGL(fromAccountTransConfig, fromAccountTransConfig.getGlAccountTypeMaster());
			
			addCreditGLAccountStatementEntry(fromAccountTransConfig, fromAccountTransConfig.getGlAccountTypeMaster());
			
			TransactionPostingConfig transactionPostingConf = getTransactionPostingConfigInstance(inflightTransactionMaster);
			transactionPostingConf.setFromAccountNo(fromAccountTransConfig.getFromAccount().getStrAccountNumber());
			transactionPostingConf.setToAccountNo(fromAccountTransConfig.getGlAccountTypeMaster().getStrAccountNumber());
			
			TransactionPostingConfig toAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getToAccountNo());
			transactionPostingConf.setStrReservefield1(toAccountTransConfig.getToAccount().getStrAccountNumber());
			transactionPostingConf.setStrReservefield2("pending");
			addMultipleTxnTranMasterEntry(transactionPostingConf);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void processAgentConfirmDepositTxnPosting(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			String txnId = transactionPostingConfig.getTxnId();
			InflightTransactionMaster inflightTransactionMaster = transactionMap.get(txnId);
			
			TransactionPostingConfig toAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getToAccountNo());
			
			agencyBankingHandler.processParkingGLDebitUpdate(transactionPostingConfig, toAccountTransConfig.getGlAccountTypeMaster());
			
			addDebitGLAccountStatementEntry(transactionPostingConfig, toAccountTransConfig.getGlAccountTypeMaster());
			
			addUpdateRecipentAccount(inflightTransactionMaster);
			
			TransactionPostingConfig transactionPostingConf = getTransactionPostingConfigInstance(inflightTransactionMaster);
			transactionPostingConf.setFromAccountNo(toAccountTransConfig.getGlAccountTypeMaster().getStrAccountNumber());
			transactionPostingConf.setToAccountNo(toAccountTransConfig.getToAccount().getStrAccountNumber());
			
			transactionPostingConf.setStrReservefield2("success");
			addMultipleTxnTranMasterEntry(transactionPostingConf);
			
			updateTransMaster(transactionPostingConf);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void processAgentConfirmWithdrawalTxnPosting(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			String txnId = transactionPostingConfig.getTxnId();
			InflightTransactionMaster inflightTransactionMaster = transactionMap.get(txnId);
			
			TransactionPostingConfig toAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getToAccountNo());
			
			agencyBankingHandler.processParkingGLDebitUpdate(transactionPostingConfig, toAccountTransConfig.getGlAccountTypeMaster());
			
			addDebitGLAccountStatementEntry(transactionPostingConfig, toAccountTransConfig.getGlAccountTypeMaster());
			
			addUpdateRecipentAccount(inflightTransactionMaster);
			
			TransactionPostingConfig transactionPostingConf = getTransactionPostingConfigInstance(inflightTransactionMaster);
			transactionPostingConf.setFromAccountNo(toAccountTransConfig.getGlAccountTypeMaster().getStrAccountNumber());
			transactionPostingConf.setToAccountNo(toAccountTransConfig.getToAccount().getStrAccountNumber());
			
			addMultipleTxnTranMasterEntry(transactionPostingConf);
			
			if (inflightTransactionMaster.getIsFeeTypeExist()) 
			{
				feeVatGLEntries(inflightTransactionMaster);
				
				transactionPostingConf.setFeeGLAccount(inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getFeeGLAccountNumber()).getFeeGLAccount());
				addFeeReleateTranMasterEntry(inflightTransactionMaster.getToAccountNo(), transactionPostingConf);
				
				transactionPostingConf.setVatGLAccount(inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getVatGLAccountNumber()).getVatGLAccount());
				addVatReleateTranMasterEntry(inflightTransactionMaster.getToAccountNo(), transactionPostingConf);
				
				updateTransactionLimits(transactionPostingConf, toAccountTransConfig.getToTierAccountMaster(), toAccountTransConfig.getToAccount());;
			}
			
			transactionPostingConf.setStrReservefield2("success");
			updateTransMaster(transactionPostingConf);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void processAgentReversedDepositTransaction(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			String txnId = transactionPostingConfig.getTxnId();
			InflightTransactionMaster inflightTransactionMaster = transactionMap.get(txnId);
			
			debitGLAccountBalance(inflightTransactionMaster);
			
			addDebitEntryInGLAccountStatement(inflightTransactionMaster);
			
			addUpdateRecipentAccount(inflightTransactionMaster);
			
			TransactionPostingConfig transactionPostingConf = getTransactionPostingConfigInstance(inflightTransactionMaster);
			transactionPostingConf.setFromAccountNo(inflightTransactionMaster.getGlAccountNumber());
			transactionPostingConf.setToAccountNo(inflightTransactionMaster.getToAccountNo());
			
			addMultipleTxnTranMasterEntry(transactionPostingConf);/*--------<From Parking GL to Agent Account NO>------------ */
			
			if (inflightTransactionMaster.getIsFeeTypeExist()) 
			{
				feeVatDebitGLEntries(inflightTransactionMaster);
				
				agencyBankingHandler.updateAndAddFeeVatCreditInAgentAccount(inflightTransactionMaster);
				
				TransactionPostingConfig feeAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getFeeGLAccountNumber());				
				transactionPostingConf.setTxnAmount(feeAccountTransConfig.getFeeGLAccount().getFee());
				transactionPostingConf.setFromAccountNo(feeAccountTransConfig.getFeeGLAccount().getStrAccountNumber());
				transactionPostingConf.setToAccountNo(inflightTransactionMaster.getToAccountNo());
				addMultipleTxnTranMasterEntry(transactionPostingConf);/*--------<From FEE GL to Agent Account NO>------------ */				
				
				TransactionPostingConfig vatAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getVatGLAccountNumber());				
				transactionPostingConf.setTxnAmount(vatAccountTransConfig.getVatGLAccount().getVat());
				transactionPostingConf.setFromAccountNo(vatAccountTransConfig.getVatGLAccount().getStrAccountNumber());
				transactionPostingConf.setToAccountNo(inflightTransactionMaster.getToAccountNo());
				addMultipleTxnTranMasterEntry(transactionPostingConf);/*--------<From Vat GL to Agent Account NO>------------ */
			}
			
			transactionPostingConf.setStrReservefield2("reversed");
			updateTransMaster(transactionPostingConf);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void processAgentReversedWithdrawalTransaction(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			String txnId = transactionPostingConfig.getTxnId();
			InflightTransactionMaster inflightTransactionMaster = transactionMap.get(txnId);
			
			debitGLAccountBalance(inflightTransactionMaster);
			
			addDebitEntryInGLAccountStatement(inflightTransactionMaster);
			
			addUpdateRecipentAccount(inflightTransactionMaster);
			
			TransactionPostingConfig transactionPostingConf = getTransactionPostingConfigInstance(inflightTransactionMaster);
			transactionPostingConf.setFromAccountNo(inflightTransactionMaster.getGlAccountNumber());
			transactionPostingConf.setToAccountNo(inflightTransactionMaster.getToAccountNo());
			
			addMultipleTxnTranMasterEntry(transactionPostingConf);/*--------<From Parking GL to Agent Account NO>------------ */
			
			transactionPostingConf.setStrReservefield2("reversed");
			updateTransMaster(transactionPostingConf);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void processLoadMoneytxnPostingData(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			String txnId = transactionPostingConfig.getTxnId();
			InflightTransactionMaster inflightTransactionMaster = transactionMap.get(txnId);
			
			updateSuspenseGLAccountBalance(inflightTransactionMaster);
			
			addDebitEntryInGLAccountStatement(inflightTransactionMaster);
			
			addUpdateRecipentAccount(inflightTransactionMaster);
			
			TransactionPostingConfig transactionPostingConf = getTransactionPostingConfigInstance(inflightTransactionMaster);
			transactionPostingConf.setFromAccountNo(inflightTransactionMaster.getGlAccountNumber());
			transactionPostingConf.setToAccountNo(inflightTransactionMaster.getToAccountNo());
			
			addMultipleTxnTranMasterEntry(transactionPostingConf);
			
			if (inflightTransactionMaster.getIsFeeTypeExist()) 
			{
				feeVatGLEntries(inflightTransactionMaster);
				
				transactionPostingConf.setFeeGLAccount(inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getFeeGLAccountNumber()).getFeeGLAccount());
				addFeeReleateTranMasterEntry(inflightTransactionMaster.getToAccountNo(), transactionPostingConf);
				
				transactionPostingConf.setVatGLAccount(inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getVatGLAccountNumber()).getVatGLAccount());
				addVatReleateTranMasterEntry(inflightTransactionMaster.getToAccountNo(), transactionPostingConf);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void processBillPayTxnPostingData(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			String txnId = transactionPostingConfig.getTxnId();
			InflightTransactionMaster inflightTransactionMaster = transactionMap.get(txnId);
			
			addUpdateSenderAccount(inflightTransactionMaster);
			
			updateGLAccountBalance(inflightTransactionMaster);
			
			addCreditGLAccountStatementEntry(transactionPostingConfig, transactionPostingConfig.getGlAccountTypeMaster());			
			
			TransactionPostingConfig fromAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getFromAccountNo());
			
			TransactionPostingConfig transactionPostingConf = getTransactionPostingConfigInstance(inflightTransactionMaster);
			transactionPostingConf.setFromAccountNo(fromAccountTransConfig.getFromAccount().getStrAccountNumber());
			transactionPostingConf.setToAccountNo(fromAccountTransConfig.getGlAccountTypeMaster().getStrAccountNumber());
			
			addMultipleTxnTranMasterEntry(transactionPostingConf);
			
			if (inflightTransactionMaster.getIsFeeTypeExist()) 
			{
				feeVatGLEntries(inflightTransactionMaster);
				
				transactionPostingConf.setFeeGLAccount(inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getFeeGLAccountNumber()).getFeeGLAccount());
				addFeeReleateTranMasterEntry(fromAccountTransConfig.getFromAccount().getStrAccountNumber(), transactionPostingConf);
				
				transactionPostingConf.setVatGLAccount(inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getVatGLAccountNumber()).getVatGLAccount());
				addVatReleateTranMasterEntry(fromAccountTransConfig.getFromAccount().getStrAccountNumber(), transactionPostingConf);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void updateTransMaster(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			AccountTranMaster accountTranMaster = new AccountTranMaster();
			accountTranMaster.setStrTxn_id(transactionPostingConfig.getTxnId());
			accountTranMaster.setStrReservefield2(transactionPostingConfig.getStrReservefield2());
			accountTranMasterService.updateAccountTranMasterColumns(accountTranMaster);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void addUpdateSenderAccount(InflightTransactionMaster inflightTransactionMaster) 
	{
		try 
		{
			TransactionPostingConfig fromAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getFromAccountNo());
			updateSenderAccount(fromAccountTransConfig);
			
			addSenderAccountStatements(fromAccountTransConfig);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void feeVatGLEntries(InflightTransactionMaster inflightTransactionMaster) 
	{
		try 
		{
			TransactionPostingConfig feeAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getFeeGLAccountNumber());
			TransactionPostingConfig vatAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getVatGLAccountNumber());
			
			TransactionPostingConfig transPostConfig = new TransactionPostingConfig();
			transPostConfig.setFeeGLAccount(feeAccountTransConfig.getFeeGLAccount());
			
			transPostConfig.setVatGLAccount(vatAccountTransConfig.getVatGLAccount());
			
			transPostConfig.setTxnType(inflightTransactionMaster.getTxnType());
			transPostConfig.setTxnId(inflightTransactionMaster.getTransactionId());
			transPostConfig.setTxnDate(inflightTransactionMaster.getTxnDate());
			
			updateFeeVatGLAccountMaster(transPostConfig);
			
			addFeeVatGLAccountStatementEntry(transPostConfig);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void feeVatDebitGLEntries(InflightTransactionMaster inflightTransactionMaster) 
	{
		try 
		{
			TransactionPostingConfig feeAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getFeeGLAccountNumber());
			TransactionPostingConfig vatAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getVatGLAccountNumber());
			
			TransactionPostingConfig transPostConfig = new TransactionPostingConfig();
			transPostConfig.setFeeGLAccount(feeAccountTransConfig.getFeeGLAccount());
			
			transPostConfig.setVatGLAccount(vatAccountTransConfig.getVatGLAccount());
			
			transPostConfig.setTxnType(inflightTransactionMaster.getTxnType());
			transPostConfig.setTxnId(inflightTransactionMaster.getTransactionId());
			transPostConfig.setTxnDate(inflightTransactionMaster.getTxnDate());
			
			updateDebitFeeVatGLAccountMaster(transPostConfig);
			
			addFeeVatDebitGLAccountStatementEntry(transPostConfig);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void addFeeReleateTranMasterEntry(String fromAccountNo, TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			GLAccountTypeMaster feeGLAccountType = transactionPostingConfig.getFeeGLAccount();
			transactionPostingConfig.setTxnAmount(feeGLAccountType.getFee());
			
			transactionPostingConfig.setFromAccountNo(fromAccountNo);
			transactionPostingConfig.setToAccountNo(feeGLAccountType.getStrAccountNumber());
			
			transactionPostingConfig.setStrReservefield1("fee_charged");
			addAccountTransactionData(transactionPostingConfig);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void addVatReleateTranMasterEntry(String fromAccountNo, TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			GLAccountTypeMaster vatGLAccountType = transactionPostingConfig.getVatGLAccount();
			transactionPostingConfig.setTxnAmount(vatGLAccountType.getVat());
			transactionPostingConfig.setFromAccountNo(fromAccountNo);
			transactionPostingConfig.setToAccountNo(vatGLAccountType.getStrAccountNumber());
			
			transactionPostingConfig.setStrReservefield1("vat_charged");
			addAccountTransactionData(transactionPostingConfig);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}	
	private void addUpdateRecipentAccount(InflightTransactionMaster inflightTransactionMaster) 
	{
		try 
		{
			TransactionPostingConfig toAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getToAccountNo());
			updateRecipentAccount(toAccountTransConfig);
			
			addRecipentAccountStatements(toAccountTransConfig);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void addClosedLoopTxnTranMasterEntry(InflightTransactionMaster inflightTransactionMaster) 
	{
		try 
		{
			TransactionPostingConfig transactionPostingConfig = getTransactionPostingConfigInstance(inflightTransactionMaster);			
			
			TransactionPostingConfig fromAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getFromAccountNo());
			//transactionPostingConfig.setFromAccount(fromAccountTransConfig.getFromAccount());
			
			TransactionPostingConfig toAccountTransConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getToAccountNo());
			//transactionPostingConfig.setToAccount(toAccountTransConfig.getToAccount());
			
			//addAccountTransactionData(transactionPostingConfig);
			
			transactionPostingConfig.setFromAccountNo(fromAccountTransConfig.getFromAccount().getStrAccountNumber());
			transactionPostingConfig.setToAccountNo(toAccountTransConfig.getToAccount().getStrAccountNumber());
			
			addMultipleTxnTranMasterEntry(transactionPostingConfig);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void addMultipleTxnTranMasterEntry(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			transactionPostingConfig.setFromAccountNo(transactionPostingConfig.getFromAccountNo());			
			transactionPostingConfig.setToAccountNo(transactionPostingConfig.getToAccountNo());
			
			addAccountTransactionData(transactionPostingConfig); 
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private TransactionPostingConfig getTransactionPostingConfigInstance(InflightTransactionMaster inflightTransactionMaster) 
	{
		TransactionPostingConfig transactionPostingConfig = new TransactionPostingConfig();
		transactionPostingConfig.setParticipantId(inflightTransactionMaster.getParticipantId());
		transactionPostingConfig.setTxnAmount(inflightTransactionMaster.getTxnAmount()); 
		transactionPostingConfig.setTxnDate(inflightTransactionMaster.getTxnDate());
		transactionPostingConfig.setTxnTime(inflightTransactionMaster.getTxnTime());
		transactionPostingConfig.setTxnId(inflightTransactionMaster.getTransactionId());
		transactionPostingConfig.setTxnType(inflightTransactionMaster.getTxnType());
		
		transactionPostingConfig.setStrSrcTxnId(inflightTransactionMaster.getStrSrcTxnId());
		transactionPostingConfig.setAuthCode(inflightTransactionMaster.getAuthCode());
		transactionPostingConfig.setResponseCode(inflightTransactionMaster.getResponseCode());
		
		return transactionPostingConfig;
	}
	private void addSenderAccountStatements(TransactionPostingConfig fromAccountTransConfig) 
	{
		try 
		{
			AccountResponse fromAccountResponse = fromAccountTransConfig.getFromAccount();
			addSenderAccountStatementEntry(fromAccountTransConfig, fromAccountResponse);
			
			GLAccountTypeMaster linkedGLAccountTypeMaster = fromAccountTransConfig.getFromLinkGLAccount();
			addSenderLinkGLAccountStatementEntry(fromAccountTransConfig, linkedGLAccountTypeMaster);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void addRecipentAccountStatements(TransactionPostingConfig toAccountTransConfig) 
	{
		try 
		{
			AccountResponse toAccountResponse = toAccountTransConfig.getToAccount();
			addRecipientAccountStatementEntry(toAccountTransConfig, toAccountResponse);
			
			GLAccountTypeMaster linkedGLAccountTypeMaster = toAccountTransConfig.getToLinkGLAccount();
			addRecipentLinkGLAccountStatementEntry(toAccountTransConfig, linkedGLAccountTypeMaster);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void updateSenderAccount(TransactionPostingConfig fromAccountTransConfig) 
	{
		try
		{
			AccountResponse fromAccountResponse = fromAccountTransConfig.getFromAccount();			
			updateSenderAccountBalanceAndLimits(fromAccountTransConfig, fromAccountResponse);
			
			GLAccountTypeMaster linkedGLAccountTypeMaster = fromAccountTransConfig.getFromLinkGLAccount();
			updateSenderLinkedGLAccountBalance(fromAccountTransConfig, linkedGLAccountTypeMaster);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void updateRecipentAccount(TransactionPostingConfig toAccountTransConfig) 
	{
		try
		{
			AccountResponse toAccountResponse = toAccountTransConfig.getToAccount();
			updateRecipientAccountBalance(toAccountTransConfig, toAccountResponse);
			
			GLAccountTypeMaster linkedGLAccountTypeMaster = toAccountTransConfig.getToLinkGLAccount();
			updateRecipentLinkedGLAccountBalance(toAccountTransConfig, linkedGLAccountTypeMaster);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void updateTransactionLimits(TransactionPostingConfig transactionPostingConfig, TierAccountResponse tierAccountResponse, AccountResponse accountResponse) 
	{
		try 
		{
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			
			if(!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				AccountMaster accountMaster = new AccountMaster();	
				accountMaster.setStrAccountNumber(accountResponse.getStrAccountNumber());
				accountMaster.setStrCustId(accountResponse.getStrCustId());
				
				if (accountResponse.getIsFeeApplicable()) 
				{
					txnAmount = AccountUtility.getUpdatedTxnAmount(transactionPostingConfig.getTxnAmount(), accountResponse, null);				
				}
				
				Double availableDailyAmount = Double.parseDouble(accountResponse.getStrAvailableDailyLimit()) - txnAmount;
				Double availableMonthlyAmount = Double.parseDouble(accountResponse.getStrAvailableMonthlyLimit()) - txnAmount;
				Double availableYearlyAmount = Double.parseDouble(accountResponse.getStrAvailableYearlyLimit()) - txnAmount;
				
				accountMaster.setStrAvailableDailyLimit(Utils.decimalFormat.format(availableDailyAmount));			
				accountMaster.setStrAvailableMonthlyLimit(Utils.decimalFormat.format(availableMonthlyAmount));			
				accountMaster.setStrAvailableYearlyLimit(Utils.decimalFormat.format(availableYearlyAmount));
			}
			else
			{
				//TierAccountResponse tierAccountResponse = transactionPostingConfig.getFromTierAccountMaster();
				updateDailyCummulativeTxnLimit(transactionPostingConfig, tierAccountResponse);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}

	private void updateDailyCummulativeTxnLimit(TransactionPostingConfig transactionPostingConfig, TierAccountResponse tierAccountResponse) 
	{
		try 
		{
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			if (tierAccountResponse.getIsFeeApplicable()) 
			{
				txnAmount = AccountUtility.getUpdatedTxnAmount(transactionPostingConfig.getTxnAmount(), null, tierAccountResponse);				
				/*
				if (tierAccountResponse.getIsFeeApplicableToRecipent()) 
				{
					txnAmount = Utils.stringToDouble(tierAccountResponse.getFee()) + Utils.stringToDouble(tierAccountResponse.getVat());
				}
				*/				
			}
			
			String activeTier = tierAccountResponse.getStrActiveTier();
			activeTier = (activeTier != null) ? activeTier.toLowerCase().trim() : "";			
			if (activeTier.length() > 0) 
			{
				String dailyAvailableTierLimit = tierAccountResponse.getTierWiseDailyCummulativeAvailableLimit().get(activeTier);
				double dailyAvailableTierLimitVal = Utils.stringToDouble(dailyAvailableTierLimit);
				
				double updatableDailyAvailableTxnLimit = dailyAvailableTierLimitVal - txnAmount;
				
				String dailyAvailableTierQuery = tierAccountResponse.getTierWiseDailyCummulativeAvailableLimit().get(activeTier+"_updatable");
				dailyAvailableTierQuery = dailyAvailableTierQuery +"'"+updatableDailyAvailableTxnLimit+"'";
				
				TierAccountMaster tierAccountMaster = new TierAccountMaster();
				tierAccountMaster.setStrAccountNo(tierAccountResponse.getStrAccountNumber());
				tierAccountMaster.setStrCustId(tierAccountResponse.getStrCustId());
				tierAccountMaster.setStrUpdatableDailyAvailableLimitQuery(dailyAvailableTierQuery);
				
				tierAccountMasterService.updateCummulativeDailyLimitBasedOnActiveTier(tierAccountMaster);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void updateSenderAccountBalanceAndLimits(TransactionPostingConfig transactionPostingConfig, AccountResponse accountResponse)
	{
		try 
		{
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			if (accountResponse.getIsFeeApplicable()) 
			{
				txnAmount = AccountUtility.getUpdatedTxnAmount(transactionPostingConfig.getTxnAmount(), accountResponse, null); 
			}
			
			AccountMaster accountMaster = new AccountMaster();	
			accountMaster.setStrAccountNumber(accountResponse.getStrAccountNumber());
			accountMaster.setStrCustId(accountResponse.getStrCustId());			
			
			String closingBal = (accountResponse.getStrClosingBalance() != null && accountResponse.getStrClosingBalance().trim().length() > 0) ? accountResponse.getStrClosingBalance().trim(): "0";
			Double balanceAmount = Double.parseDouble(closingBal);
			
			Double closingBalance = balanceAmount - txnAmount;
			String strClosingBalance = Utils.decimalFormat.format(closingBalance);			
			accountMaster.setStrClosingBalance(strClosingBalance);
			
			if(!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				Double availableDailyAmount = Double.parseDouble(accountResponse.getStrAvailableDailyLimit()) - txnAmount;
				Double availableMonthlyAmount = Double.parseDouble(accountResponse.getStrAvailableMonthlyLimit()) - txnAmount;
				Double availableYearlyAmount = Double.parseDouble(accountResponse.getStrAvailableYearlyLimit()) - txnAmount;
				
				accountMaster.setStrAvailableDailyLimit(Utils.decimalFormat.format(availableDailyAmount));			
				accountMaster.setStrAvailableMonthlyLimit(Utils.decimalFormat.format(availableMonthlyAmount));			
				accountMaster.setStrAvailableYearlyLimit(Utils.decimalFormat.format(availableYearlyAmount));
			}
			else
			{
				TierAccountResponse tierAccountResponse = transactionPostingConfig.getFromTierAccountMaster();
				updateDailyCummulativeTxnLimit(transactionPostingConfig, tierAccountResponse);
			}
			
			accountMasterService.updateAccountMasterFields(accountMaster);
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void updateRecipientAccountBalance(TransactionPostingConfig transactionPostingConfig, AccountResponse accountResponse) 
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
			
			if (accountResponse.getIsFeeApplicable()) 
			{
				Double feeVatTxnAmount = Utils.stringToDouble(accountResponse.getFee()) + Utils.stringToDouble(accountResponse.getVat());
				closingBal = closingBal - feeVatTxnAmount;
				strClosingBalance = Utils.decimalFormat.format(closingBal);
			}
			
			accountMaster.setStrClosingBalance(strClosingBalance);			
			accountMasterService.updateAccountMasterFields(accountMaster);
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void updateSenderLinkedGLAccountBalance(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster linkedGLAccountTypeMaster) 
	{
		try 
		{
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());			
			if (linkedGLAccountTypeMaster.getIsFeeApplicable()) 
			{
				//txnAmount = Utils.stringToDouble(linkedGLAccountTypeMaster.getFee()) + Utils.stringToDouble(linkedGLAccountTypeMaster.getVat());
				txnAmount = txnAmount + Utils.stringToDouble(linkedGLAccountTypeMaster.getFee()) + Utils.stringToDouble(linkedGLAccountTypeMaster.getVat());
			}
			
			String closingBal = (linkedGLAccountTypeMaster.getStrClosingBalance() != null && linkedGLAccountTypeMaster.getStrClosingBalance().trim().length() > 0) ? linkedGLAccountTypeMaster.getStrClosingBalance().trim(): "0";
			
			closingBal = (closingBal != null && closingBal.trim().length() > 0) ? closingBal.trim(): "0";
			Double balanceAmount = Double.parseDouble(closingBal);
			
			Double closingBalance = balanceAmount - txnAmount;
			String strClosingBalance = Utils.decimalFormat.format(closingBalance);			
			
			GLAccountTypeMaster linkedGLAccountType = new GLAccountTypeMaster();
			linkedGLAccountType.setStrGLAccountType(linkedGLAccountTypeMaster.getStrGLAccountType());
			linkedGLAccountType.setStrAccountNumber(linkedGLAccountTypeMaster.getStrAccountNumber());
			
			linkedGLAccountType.setStrClosingBalance(strClosingBalance);	
			
			glAccountTypeMasterService.updateGLAccountTypeDetails(linkedGLAccountType);
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void updateRecipentLinkedGLAccountBalance(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster linkedGLAccountTypeMaster) 
	{
		try 
		{
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			
			String glAccounNumber = linkedGLAccountTypeMaster.getStrAccountNumber();
			glAccounNumber = glAccounNumber.trim();
			
			String closingBal = getLinkedGLUpdatedBalance(transactionPostingConfig, glAccounNumber);
			closingBal = (closingBal != null && closingBal.trim().length() > 0) ? closingBal.trim(): "0";
			
			//String closingBal = (linkedGLAccountTypeMaster.getStrClosingBalance() != null && linkedGLAccountTypeMaster.getStrClosingBalance().trim().length() > 0) ? linkedGLAccountTypeMaster.getStrClosingBalance().trim(): "0";
			Double balanceAmount = Double.parseDouble(closingBal);
			
			Double closingBalance = balanceAmount + txnAmount;
			String strClosingBalance = Utils.decimalFormat.format(closingBalance);	
			
			GLAccountTypeMaster linkedGLAccountType = new GLAccountTypeMaster();
			linkedGLAccountType.setStrGLAccountType(linkedGLAccountTypeMaster.getStrGLAccountType());
			linkedGLAccountType.setStrAccountNumber(linkedGLAccountTypeMaster.getStrAccountNumber());
			
			if (linkedGLAccountTypeMaster.getIsFeeApplicable()) 
			{
				Double feeVatTxnAmount = Utils.stringToDouble(linkedGLAccountTypeMaster.getFee()) + Utils.stringToDouble(linkedGLAccountTypeMaster.getVat());
				closingBalance = closingBalance - feeVatTxnAmount;
				strClosingBalance = Utils.decimalFormat.format(closingBalance);				
			}
			
			linkedGLAccountType.setStrClosingBalance(strClosingBalance);
			
			glAccountTypeMasterService.updateGLAccountTypeDetails(linkedGLAccountType);
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void updateFeeVatGLAccountMaster(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			List<GLAccountTypeMaster> glAccountTypeMasters = new ArrayList<GLAccountTypeMaster>();
			
			GLAccountTypeMaster exitingFeeGLAccountType = transactionPostingConfig.getFeeGLAccount();			
			Double feeTxnAmount = Utils.stringToDouble(exitingFeeGLAccountType.getFee());			
			String feeStrClosingBalance = getUpdatedClosingBalance(feeTxnAmount, exitingFeeGLAccountType);				
			
			GLAccountTypeMaster feeGLAccountType = new GLAccountTypeMaster();
			feeGLAccountType.setStrAccountNumber(exitingFeeGLAccountType.getStrAccountNumber());
			feeGLAccountType.setStrGLAccountType(exitingFeeGLAccountType.getStrGLAccountType());
			feeGLAccountType.setStrClosingBalance(feeStrClosingBalance);
			glAccountTypeMasters.add(feeGLAccountType);/*-------------------******/
			
			GLAccountTypeMaster exitingVatGLAccountType = transactionPostingConfig.getVatGLAccount();
			Double vatTxnAmount = Utils.stringToDouble(exitingVatGLAccountType.getVat());			
			String vatStrClosingBalance = getUpdatedClosingBalance(vatTxnAmount, exitingVatGLAccountType);
			
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
	
	private void updateDebitFeeVatGLAccountMaster(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			List<GLAccountTypeMaster> glAccountTypeMasters = new ArrayList<GLAccountTypeMaster>();
			
			GLAccountTypeMaster exitingFeeGLAccountType = transactionPostingConfig.getFeeGLAccount();			
			Double feeTxnAmount = Utils.stringToDouble(exitingFeeGLAccountType.getFee());			
			String feeStrClosingBalance = getDebitedGLClosingBalanceAmount(feeTxnAmount, exitingFeeGLAccountType);				
			
			GLAccountTypeMaster feeGLAccountType = new GLAccountTypeMaster();
			feeGLAccountType.setStrAccountNumber(exitingFeeGLAccountType.getStrAccountNumber());
			feeGLAccountType.setStrGLAccountType(exitingFeeGLAccountType.getStrGLAccountType());
			feeGLAccountType.setStrClosingBalance(feeStrClosingBalance);
			glAccountTypeMasters.add(feeGLAccountType);/*-------------------******/
			
			GLAccountTypeMaster exitingVatGLAccountType = transactionPostingConfig.getVatGLAccount();
			Double vatTxnAmount = Utils.stringToDouble(exitingVatGLAccountType.getVat());			
			String vatStrClosingBalance = getDebitedGLClosingBalanceAmount(vatTxnAmount, exitingVatGLAccountType);
			
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
	
	private void addFeeVatGLAccountStatementEntry(TransactionPostingConfig transactionPostingConfig) 
	{
		try
		{
			List<GLAccountStatement> glAccountStatements = new ArrayList<GLAccountStatement>();
			
			GLAccountTypeMaster feeGLAccountType = transactionPostingConfig.getFeeGLAccount();
			Double feeTxnAmount = Utils.stringToDouble(feeGLAccountType.getFee());			
			String feeStrClosingBalance = getUpdatedClosingBalance(feeTxnAmount, feeGLAccountType);				
			
			TransactionPostingConfig transactionPostConf = new TransactionPostingConfig();
			transactionPostConf.setTxnId(transactionPostingConfig.getTxnId());
			transactionPostConf.setTxnDate(transactionPostingConfig.getTxnDate());
			transactionPostConf.setTxnType(transactionPostingConfig.getTxnType());
			
			transactionPostConf.setTxnAmount(feeGLAccountType.getFee());
			transactionPostConf.setClosingBalance(feeStrClosingBalance);
			
			GLAccountStatement feeGLStatement = getCreditGLAccountStatementInstance(transactionPostConf, feeGLAccountType);
			glAccountStatements.add(feeGLStatement);
			
			GLAccountTypeMaster vatGLAccountType = transactionPostingConfig.getVatGLAccount();
			Double vatTxnAmount = Utils.stringToDouble(vatGLAccountType.getVat());		
			String vatStrClosingBalance = getUpdatedClosingBalance(vatTxnAmount, vatGLAccountType);				
									
			transactionPostConf.setTxnAmount(vatGLAccountType.getVat());
			transactionPostConf.setClosingBalance(vatStrClosingBalance);
			
			GLAccountStatement vatGLStatement = getCreditGLAccountStatementInstance(transactionPostConf, vatGLAccountType);
			glAccountStatements.add(vatGLStatement);
			
			glAccountStatementService.batchEntryOfGLAccountStatement(glAccountStatements);
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void addFeeVatDebitGLAccountStatementEntry(TransactionPostingConfig transactionPostingConfig) 
	{
		try
		{
			List<GLAccountStatement> glAccountStatements = new ArrayList<GLAccountStatement>();
			
			GLAccountTypeMaster feeGLAccountType = transactionPostingConfig.getFeeGLAccount();
			Double feeTxnAmount = Utils.stringToDouble(feeGLAccountType.getFee());			
			String feeStrClosingBalance = getDebitedGLClosingBalanceAmount(feeTxnAmount, feeGLAccountType);				
			
			TransactionPostingConfig transactionPostConf = new TransactionPostingConfig();
			transactionPostConf.setTxnId(transactionPostingConfig.getTxnId());
			transactionPostConf.setTxnDate(transactionPostingConfig.getTxnDate());
			transactionPostConf.setTxnType(transactionPostingConfig.getTxnType());
			
			transactionPostConf.setTxnAmount(feeGLAccountType.getFee());
			transactionPostConf.setClosingBalance(feeStrClosingBalance);
			
			GLAccountStatement feeGLStatement = addDebitEntryInGLAccountStatement(transactionPostConf, feeGLAccountType);
			glAccountStatements.add(feeGLStatement);
			
			GLAccountTypeMaster vatGLAccountType = transactionPostingConfig.getVatGLAccount();
			Double vatTxnAmount = Utils.stringToDouble(vatGLAccountType.getVat());		
			String vatStrClosingBalance = getDebitedGLClosingBalanceAmount(vatTxnAmount, vatGLAccountType);				
									
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
	
	private String getUpdatedClosingBalance(Double txnAmount, GLAccountTypeMaster glAccountTypeMaster) 
	{
		String glClosingBal = (glAccountTypeMaster.getStrClosingBalance() != null && glAccountTypeMaster.getStrClosingBalance().trim().length() > 0) ? glAccountTypeMaster.getStrClosingBalance().trim(): "0";
		Double glBalanceAmount = Double.parseDouble(glClosingBal);
		
		Double feeClosingBalance = glBalanceAmount + txnAmount;
		return Utils.decimalFormat.format(feeClosingBalance);
	}
	
	private String getDebitedGLClosingBalanceAmount(Double txnAmount, GLAccountTypeMaster glAccountTypeMaster) 
	{
		String glClosingBal = (glAccountTypeMaster.getStrClosingBalance() != null && glAccountTypeMaster.getStrClosingBalance().trim().length() > 0) ? glAccountTypeMaster.getStrClosingBalance().trim(): "0";
		Double glBalanceAmount = Double.parseDouble(glClosingBal);
		
		Double closingBal = glBalanceAmount - txnAmount;
		return Utils.decimalFormat.format(closingBal);
	}
	
	private void addSenderAccountStatementEntry(TransactionPostingConfig transactionPostingConfig, AccountResponse accountResponse) 
	{
		try 
		{
			List<AccountStatement> accountStatements = new ArrayList<AccountStatement>();
			
			String closingBalance = null;
			String strTxnAmount = transactionPostingConfig.getTxnAmount();
			Double accountClosingBal = Utils.stringToDouble(accountResponse.getStrClosingBalance());
			Double txnAmount = Utils.stringToDouble(strTxnAmount);
			
			TransactionPostingConfig transactionPostConf = new TransactionPostingConfig();
			transactionPostConf.setParticipantId(transactionPostingConfig.getParticipantId());
			transactionPostConf.setTxnId(transactionPostingConfig.getTxnId());
			transactionPostConf.setTxnDate(transactionPostingConfig.getTxnDate());
			transactionPostConf.setTxnType(transactionPostingConfig.getTxnType());
			
			accountClosingBal = accountClosingBal - txnAmount;
			closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
			
			transactionPostConf.setTxnAmount(strTxnAmount);
			transactionPostConf.setClosingBalance(closingBalance);
			
			AccountStatement accountStatement = getDebitAccountStatementInstance(transactionPostConf, accountResponse);
			
			//Added By Sunil Y , Ams Will Populate EntityInfo And EntityRespon For Sender [start]
			accountStatement.setEntityInfo(transactionPostingConfig.getFromAccount().getEntityInfo());
			accountStatement.setEntityNumber(transactionPostingConfig.getFromAccount().getEntityNumber());
			//Added By Sunil Y , Ams Will Populate EntityInfo And EntityRespon For Recipient [End]
			
			accountStatement.setStrIsGLType("T");//T - Transaction for Identify transaction Amount txn
			accountStatements.add(accountStatement);
			
			if (accountResponse.getIsFeeApplicable()) 
			{
				strTxnAmount = accountResponse.getFee();
				txnAmount = Utils.stringToDouble(strTxnAmount);
				
				accountClosingBal = accountClosingBal - txnAmount;
				closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
				
				transactionPostConf.setTxnAmount(strTxnAmount);
				transactionPostConf.setClosingBalance(closingBalance);
				
				AccountStatement feeAccountStatement = getDebitAccountStatementInstance(transactionPostConf, accountResponse);
				feeAccountStatement.setStrIsGLType("F");//F - Fee for Identify fee transaction Amount txn
				accountStatements.add(feeAccountStatement);
				
				strTxnAmount = accountResponse.getVat();
				txnAmount = Utils.stringToDouble(strTxnAmount);
				
				accountClosingBal = accountClosingBal - txnAmount;
				closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
				
				transactionPostConf.setTxnAmount(strTxnAmount);
				transactionPostConf.setClosingBalance(closingBalance);

				AccountStatement vatAccountStatement = getDebitAccountStatementInstance(transactionPostConf, accountResponse);
				vatAccountStatement.setStrIsGLType("V");//V - Vat for Identify Vat transaction Amount txn
				accountStatements.add(vatAccountStatement);
			}
			accountStatementService.batchEntryOfAccountStatementMaster(accountStatements);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void addRecipientAccountStatementEntry(TransactionPostingConfig transactionPostingConfig, AccountResponse accountResponse) 
	{
		try 
		{
			List<AccountStatement> accountStatements = new ArrayList<AccountStatement>();
			
			String closingBalance = null;
			String strTxnAmount = transactionPostingConfig.getTxnAmount();
			Double accountClosingBal = Utils.stringToDouble(accountResponse.getStrClosingBalance());
			Double txnAmount = Utils.stringToDouble(strTxnAmount);
			
			TransactionPostingConfig transactionPostConf = new TransactionPostingConfig();
			transactionPostConf.setParticipantId(transactionPostingConfig.getParticipantId());
			transactionPostConf.setTxnId(transactionPostingConfig.getTxnId());
			transactionPostConf.setTxnDate(transactionPostingConfig.getTxnDate());
			transactionPostConf.setTxnType(transactionPostingConfig.getTxnType());
			
			accountClosingBal = accountClosingBal + txnAmount;
			closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
			
			transactionPostConf.setTxnAmount(strTxnAmount);
			transactionPostConf.setClosingBalance(closingBalance);
			
			AccountStatement accountStatement = getCreditAccountStatementInstance(transactionPostConf, accountResponse);
			
			//Added By Sunil Y , Ams Will Populate EntityInfo And EntityRespon For Recipient [start]
			accountStatement.setEntityInfo(transactionPostingConfig.getToAccount().getEntityInfo());
			accountStatement.setEntityNumber(transactionPostingConfig.getToAccount().getEntityNumber());
			//Added By Sunil Y , Ams Will Populate EntityInfo And EntityRespon For Recipient [End]
			
			accountStatement.setStrIsGLType("T");//T - Transaction for Identify transaction Amount txn
			
			accountStatements.add(accountStatement);
			
			if (accountResponse.getIsFeeApplicable()) 
			{
				strTxnAmount = accountResponse.getFee();
				txnAmount = Utils.stringToDouble(strTxnAmount);
				
				accountClosingBal = accountClosingBal - txnAmount;
				closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
				
				transactionPostConf.setTxnAmount(strTxnAmount);
				transactionPostConf.setClosingBalance(closingBalance);
				
				AccountStatement feeAccountStatement = getDebitAccountStatementInstance(transactionPostConf, accountResponse);
				feeAccountStatement.setStrIsGLType("F");//F - Fee for Identify Fee transaction Amount txn
				accountStatements.add(feeAccountStatement);
				
				strTxnAmount = accountResponse.getVat();
				txnAmount = Utils.stringToDouble(strTxnAmount);
				
				accountClosingBal = accountClosingBal - txnAmount;
				closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
				
				transactionPostConf.setTxnAmount(strTxnAmount);
				transactionPostConf.setClosingBalance(closingBalance);

				AccountStatement vatAccountStatement = getDebitAccountStatementInstance(transactionPostConf, accountResponse);
				vatAccountStatement.setStrIsGLType("V");//V - Vat for Identify Vat transaction Amount txn
				accountStatements.add(vatAccountStatement);
			}
			accountStatementService.batchEntryOfAccountStatementMaster(accountStatements);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void addSenderLinkGLAccountStatementEntry(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster linkedGlAccountTypeMaster) 
	{
		try 
		{
			List<GLAccountStatement> glAccountStatements = new ArrayList<GLAccountStatement>();
			
			String closingBalance = null;
			String strTxnAmount = transactionPostingConfig.getTxnAmount();
			
			String glAccounNumber = linkedGlAccountTypeMaster.getStrAccountNumber();
			glAccounNumber = glAccounNumber.trim();
			
			String closingBal = getLinkedGLUpdatedBalance(transactionPostingConfig, glAccounNumber);
			
			//Double accountClosingBal = Utils.stringToDouble(linkedGlAccountTypeMaster.getStrClosingBalance());
			Double accountClosingBal = Utils.stringToDouble(closingBal);
			Double txnAmount = Utils.stringToDouble(strTxnAmount);
			
			TransactionPostingConfig transactionPostConf = new TransactionPostingConfig();
			transactionPostConf.setTxnId(transactionPostingConfig.getTxnId());
			transactionPostConf.setTxnDate(transactionPostingConfig.getTxnDate());
			transactionPostConf.setTxnType(transactionPostingConfig.getTxnType());
			
			accountClosingBal = accountClosingBal - txnAmount;
			closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
			
			
			transactionPostConf.setTxnAmount(strTxnAmount);
			transactionPostConf.setClosingBalance(closingBalance);
			
			GLAccountStatement linkedGLStatement = getDebitGLAccountStatementInstance(transactionPostConf, linkedGlAccountTypeMaster);
			glAccountStatements.add(linkedGLStatement);
			if (linkedGlAccountTypeMaster.getIsFeeApplicable()) 
			{
				strTxnAmount = linkedGlAccountTypeMaster.getFee();
				txnAmount = Utils.stringToDouble(strTxnAmount);
				
				accountClosingBal = accountClosingBal - txnAmount;
				closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
				
				transactionPostConf.setTxnAmount(strTxnAmount);
				transactionPostConf.setClosingBalance(closingBalance);
				GLAccountStatement feeLinkedGLStatement = getDebitGLAccountStatementInstance(transactionPostConf, linkedGlAccountTypeMaster);
				glAccountStatements.add(feeLinkedGLStatement);
				
				strTxnAmount = linkedGlAccountTypeMaster.getVat();
				txnAmount = Utils.stringToDouble(strTxnAmount);
				
				accountClosingBal = accountClosingBal - txnAmount;
				closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
				
				transactionPostConf.setTxnAmount(strTxnAmount);
				transactionPostConf.setClosingBalance(closingBalance);
				GLAccountStatement vatLinkedGLStatement = getDebitGLAccountStatementInstance(transactionPostConf, linkedGlAccountTypeMaster);
				glAccountStatements.add(vatLinkedGLStatement); 
			}
			
			updatedBalance(transactionPostingConfig, glAccounNumber, closingBalance);
			
			glAccountStatementService.batchEntryOfGLAccountStatement(glAccountStatements);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void addRecipentLinkGLAccountStatementEntry(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster linkedGLAccountTypeMaster) 
	{
		try 
		{
			List<GLAccountStatement> glAccountStatements = new ArrayList<GLAccountStatement>();
			
			String closingBalance = null;
			String strTxnAmount = transactionPostingConfig.getTxnAmount();
			
			String glAccounNumber = linkedGLAccountTypeMaster.getStrAccountNumber();
			glAccounNumber = glAccounNumber.trim();
			
			String closingBal = getLinkedGLUpdatedBalance(transactionPostingConfig, glAccounNumber);
			closingBal = (closingBal != null && closingBal.trim().length() > 0) ? closingBal.trim(): "0";
			
			//Double accountClosingBal = Utils.stringToDouble(glAccountTypeMaster.getStrClosingBalance());
			Double accountClosingBal = Utils.stringToDouble(closingBal);
			Double txnAmount = Utils.stringToDouble(strTxnAmount);
			
			TransactionPostingConfig transactionPostConf = new TransactionPostingConfig();
			transactionPostConf.setParticipantId(transactionPostingConfig.getParticipantId());
			transactionPostConf.setTxnId(transactionPostingConfig.getTxnId());
			transactionPostConf.setTxnDate(transactionPostingConfig.getTxnDate());
			transactionPostConf.setTxnType(transactionPostingConfig.getTxnType());
			
			accountClosingBal = accountClosingBal + txnAmount;
			closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
			
			transactionPostConf.setTxnAmount(strTxnAmount);
			transactionPostConf.setClosingBalance(closingBalance);
			
			GLAccountStatement linkedGLStatement = getCreditGLAccountStatementInstance(transactionPostConf, linkedGLAccountTypeMaster);
			glAccountStatements.add(linkedGLStatement);
			
			if (linkedGLAccountTypeMaster.getIsFeeApplicable()) 
			{
				strTxnAmount = linkedGLAccountTypeMaster.getFee();
				txnAmount = Utils.stringToDouble(strTxnAmount);
				
				accountClosingBal = accountClosingBal - txnAmount;
				closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
				
				transactionPostConf.setTxnAmount(strTxnAmount);
				transactionPostConf.setClosingBalance(closingBalance);
				GLAccountStatement feeLinkedGLStatement = getDebitGLAccountStatementInstance(transactionPostConf, linkedGLAccountTypeMaster);
				glAccountStatements.add(feeLinkedGLStatement);
				
				strTxnAmount = linkedGLAccountTypeMaster.getVat();
				txnAmount = Utils.stringToDouble(strTxnAmount);
				
				accountClosingBal = accountClosingBal - txnAmount;
				closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
				
				transactionPostConf.setTxnAmount(strTxnAmount);
				transactionPostConf.setClosingBalance(closingBalance);
				GLAccountStatement vatLinkedGLStatement = getDebitGLAccountStatementInstance(transactionPostConf, linkedGLAccountTypeMaster);
				glAccountStatements.add(vatLinkedGLStatement); 
			}
			glAccountStatementService.batchEntryOfGLAccountStatement(glAccountStatements);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void addDebitGLAccountStatementEntry(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster glAccountTypeMaster) 
	{
		try 
		{
			Double accountClosingBal = Utils.stringToDouble(glAccountTypeMaster.getStrClosingBalance());
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			
			TransactionPostingConfig transactionPostConf = new TransactionPostingConfig();
			transactionPostConf.setTxnId(transactionPostingConfig.getTxnId());
			transactionPostConf.setTxnDate(transactionPostingConfig.getTxnDate());
			transactionPostConf.setTxnType(transactionPostingConfig.getTxnType());
			
			accountClosingBal = accountClosingBal - txnAmount;
			String closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
			
			transactionPostConf.setTxnAmount(String.valueOf(txnAmount));
			transactionPostConf.setClosingBalance(closingBalance);
			
			GLAccountStatement glAccountStatement = getDebitGLAccountStatementInstance(transactionPostConf, glAccountTypeMaster);
			glAccountStatementService.addGLAccountStatement(glAccountStatement);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	private void addCreditGLAccountStatementEntry(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster glAccountTypeMaster) 
	{
		try 
		{
			Double accountClosingBal = Utils.stringToDouble(glAccountTypeMaster.getStrClosingBalance());
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			
			TransactionPostingConfig transactionPostConf = new TransactionPostingConfig();
			transactionPostConf.setTxnId(transactionPostingConfig.getTxnId());
			transactionPostConf.setTxnDate(transactionPostingConfig.getTxnDate());
			transactionPostConf.setTxnType(transactionPostingConfig.getTxnType());
			
			accountClosingBal = accountClosingBal + txnAmount;
			String closingBalance = String.valueOf(Utils.decimalFormat.format(accountClosingBal));
			
			transactionPostConf.setTxnAmount(String.valueOf(txnAmount));
			transactionPostConf.setClosingBalance(closingBalance);
			
			GLAccountStatement glAccountStatement = getCreditGLAccountStatementInstance(transactionPostConf, glAccountTypeMaster);
			glAccountStatementService.addGLAccountStatement(glAccountStatement);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	//getAccountTranMaster
	 @Override
	public void addAccountTransactionData(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			AccountTranMaster accountTranMaster = getAccountTranMaster(transactionPostingConfig);
			accountTranMasterService.addAccountTransactionData(accountTranMaster);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void removeExistingTransactionPosting(TransactionPostingConfig transactionPostingConfig) 
	{
		transactionMap.remove(transactionPostingConfig.getTxnId());
	}
	
	 public AccountStatement getDebitAccountStatementInstance(TransactionPostingConfig transactionPostingConfig, AccountResponse accountResponse)
	 {
		 AccountStatement accountStatement = new AccountStatement();
		 try 
		 {
			accountStatement.setStrParticipantId(transactionPostingConfig.getParticipantId());
			accountStatement.setStrTransactionID(transactionPostingConfig.getTxnId());
			accountStatement.setStrTransactionType(transactionPostingConfig.getTxnType());
			accountStatement.setTransactionDate(transactionPostingConfig.getTxnDate());	
			
			accountStatement.setStrTransactionAmount(transactionPostingConfig.getTxnAmount());
			accountStatement.setStrAccountNumber(accountResponse.getStrAccountNumber());
			accountStatement.setStrAccountType(accountResponse.getStrAccountType());
			
			accountStatement.setStrTransactionMode("DEBIT");
			accountStatement.setStrNaration("Amount Debited Successfully.");
			//accountStatement.setStrIsGLType("N");
			
			accountStatement.setStrClosingBalance(transactionPostingConfig.getClosingBalance());
		 }
		 catch (Exception e) 
		 {
			 amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		 }
		return accountStatement;
	 } 
	 
	 @Override
	 public AccountStatement getCreditAccountStatementInstance(TransactionPostingConfig transactionPostingConfig, AccountResponse accountResponse)
	 {
		 AccountStatement accountStatement = new AccountStatement();
		 try 
		 {
			accountStatement.setStrParticipantId(transactionPostingConfig.getParticipantId());
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
	 private AccountTranMaster getAccountTranMaster(TransactionPostingConfig transactionPostingConfig) 
	 {
		 AccountTranMaster accountTranMaster = new AccountTranMaster();
		 try 
		 {
			accountTranMaster.setStrTran_type(transactionPostingConfig.getTxnType());
			
			TransactionTypeModel transactionTypeModel = getTransactionTypeObject(accountTranMaster);			
			accountTranMaster.setStrProcessingCode(transactionTypeModel.getStrProcessingCode());
			
			accountTranMaster.setStrAccountNumber(transactionPostingConfig.getAccountNo());
			accountTranMaster.setStrFrom_account_number(transactionPostingConfig.getFromAccountNo());
			accountTranMaster.setStrTo_account_number(transactionPostingConfig.getToAccountNo());
			
			accountTranMaster.setStrTxn_id(transactionPostingConfig.getTxnId());
			accountTranMaster.setSwitchTxDate(transactionPostingConfig.getTxnDate());
			accountTranMaster.setStrLocal_tran_date(transactionPostingConfig.getTxnDate());
			accountTranMaster.setStrLocal_tran_time(transactionPostingConfig.getTxnTime());
			
			accountTranMaster.setStrResponseCode(transactionPostingConfig.getResponseCode());
			accountTranMaster.setStrAuthCode(transactionPostingConfig.getAuthCode());
			accountTranMaster.setStrSrcTxnId(transactionPostingConfig.getStrSrcTxnId());
			accountTranMaster.setStrTransaction_amount(transactionPostingConfig.getTxnAmount());
			
			accountTranMaster.setStrReservefield1(transactionPostingConfig.getStrReservefield1());
			accountTranMaster.setStrReservefield2(transactionPostingConfig.getStrReservefield2());
			accountTranMaster.setStrReservefield3(transactionPostingConfig.getStrReservefield3());
			accountTranMaster.setStrParticipantId(transactionPostingConfig.getParticipantId());
		 }
		 catch (Exception e) 
		 {
			 amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		 }
		 return accountTranMaster;
	 }
	
	private TransactionTypeModel getTransactionTypeObject(AccountTranMaster accountTranMaster) throws Exception
	{
		TransactionTypeModel transactionTypeModel = null;
		try 
		{
			transactionTypeModel = new TransactionTypeModel();
			if (accountTranMaster.getStrTran_type()!=null && accountTranMaster.getStrTran_type().trim().length() > 0) 
			{
				transactionTypeModel.setStrTxnTypeKeyWord(accountTranMaster.getStrTran_type());			
			}
			transactionTypeModel = transactionTypeService.getTransactionTypeMasterBasedOnTxnType(transactionTypeModel);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionTypeModel;
	}
	
	private GLAccountStatement getDebitGLAccountStatementInstance(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster glAccountTypeMaster) 
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
			
			linkedGLStatement.setStrTranMode("DEBIT");
		 }
		 catch (Exception e) 
		 {
			 amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		 }
		 return linkedGLStatement;
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
	 
	private void updateSuspenseGLAccountBalance(InflightTransactionMaster inflightTransactionMaster)
	{
		debitGLAccountBalance(inflightTransactionMaster);
	}
	 
	private void debitGLAccountBalance(InflightTransactionMaster inflightTransactionMaster)
	{
		try 
		{
			TransactionPostingConfig transactionPostingConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getGlAccountNumber());
			
			GLAccountTypeMaster gLAccountTypeMasterdetails = transactionPostingConfig.getGlAccountTypeMaster();
			
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			
			String glAccounNumber = gLAccountTypeMasterdetails.getStrAccountNumber();
			glAccounNumber = glAccounNumber.trim();
			
			String closingBal = gLAccountTypeMasterdetails.getStrClosingBalance();
			
			closingBal = (closingBal != null && closingBal.trim().length() > 0) ? closingBal.trim(): "0";
			
			Double balanceAmount = Double.parseDouble(closingBal);
			
			Double closingBalance = balanceAmount - txnAmount;
			String strClosingBalance = Utils.decimalFormat.format(closingBalance);	
			
			GLAccountTypeMaster gLAccountTypeMaster = new GLAccountTypeMaster();
			gLAccountTypeMaster.setStrGLAccountType(gLAccountTypeMasterdetails.getStrGLAccountType());
			gLAccountTypeMaster.setStrAccountNumber(gLAccountTypeMasterdetails.getStrAccountNumber());
			gLAccountTypeMaster.setStrClosingBalance(strClosingBalance);
			
			glAccountTypeMasterService.updateGLAccountTypeDetails(gLAccountTypeMaster);
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void addDebitEntryInGLAccountStatement(InflightTransactionMaster inflightTransactionMaster)
	 {
		 try 
		 {
			 TransactionPostingConfig transactionPostingConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getGlAccountNumber());
			 GLAccountTypeMaster gLAccountTypeMaster = transactionPostingConfig.getGlAccountTypeMaster();
				
			 GLAccountStatement glAccountStatement = new GLAccountStatement();
			 glAccountStatement.setStrGLAccountType(gLAccountTypeMaster.getStrGLAccountType());
			 glAccountStatement.setStrRef(gLAccountTypeMaster.getStrGLAccountDescription());
			 glAccountStatement.setStrAccountNumber(gLAccountTypeMaster.getStrAccountNumber());	
			 
			 glAccountStatement.setStrTxnId(transactionPostingConfig.getTxnId());
			 glAccountStatement.setTransactionDate(transactionPostingConfig.getTxnDate());		
			 glAccountStatement.setStrTranType(transactionPostingConfig.getTxnType());	
			 glAccountStatement.setStrAmount(transactionPostingConfig.getTxnAmount());
			 
			 double closingBal = Utils.stringToDouble(gLAccountTypeMaster.getStrClosingBalance()) - Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			 
			 glAccountStatement.setStrClosingBalance(Utils.decimalFormat.format(closingBal));
				
			 glAccountStatement.setStrTranMode("DEBIT");				
				
			 glAccountStatementService.addGlAccountStatementData(glAccountStatement);
		 }
		 catch (Exception e) 
		 {
			 amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	 }
	
	private void updateGLAccountBalance(InflightTransactionMaster inflightTransactionMaster)
	{
		creditGLAccountBalance(inflightTransactionMaster);
	}
	
	private void creditGLAccountBalance(InflightTransactionMaster inflightTransactionMaster)
	{
		try 
		{
			TransactionPostingConfig transactionPostingConfig = inflightTransactionMaster.getDataMap().get(inflightTransactionMaster.getGlAccountNumber());
			
			GLAccountTypeMaster gLAccountTypeMasterdetails = transactionPostingConfig.getGlAccountTypeMaster();
			
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			
			String glAccounNumber = gLAccountTypeMasterdetails.getStrAccountNumber();
			glAccounNumber = glAccounNumber.trim();
			
			String closingBal = gLAccountTypeMasterdetails.getStrClosingBalance();
			
			closingBal = (closingBal != null && closingBal.trim().length() > 0) ? closingBal.trim(): "0";
			
			Double balanceAmount = Double.parseDouble(closingBal);
			
			Double closingBalance = balanceAmount + txnAmount;
			String strClosingBalance = Utils.decimalFormat.format(closingBalance);	
			
			GLAccountTypeMaster gLAccountTypeMaster = new GLAccountTypeMaster();
			gLAccountTypeMaster.setStrGLAccountType(gLAccountTypeMasterdetails.getStrGLAccountType());
			gLAccountTypeMaster.setStrAccountNumber(gLAccountTypeMasterdetails.getStrAccountNumber());
			gLAccountTypeMaster.setStrClosingBalance(strClosingBalance);
			
			glAccountTypeMasterService.updateGLAccountTypeDetails(gLAccountTypeMaster);
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
}


