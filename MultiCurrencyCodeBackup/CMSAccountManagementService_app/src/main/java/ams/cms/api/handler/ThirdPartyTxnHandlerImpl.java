package ams.cms.api.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.controller.ThirdPartyTransactionController;
import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.BankListResponse;
import ams.cms.api.model.TierAccountResponse;
import ams.cms.config.CommonConstants;
import ams.cms.config.TransactionConfig;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.BankDetails;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.TierAccountMaster;
import ams.cms.services.AccountMasterService;
import ams.cms.services.BankDetailsInfoService;
import ams.cms.services.BankDetailsService;
import ams.cms.services.GLAccountStatementService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.services.TierAccountMasterService;
import ams.cms.txn.handler.ExternalServerHandler;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.AccountUtility;
import ams.cms.utility.Utils;

@Component
public class ThirdPartyTxnHandlerImpl implements ThirdPartyTxnHandler 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(ThirdPartyTransactionController.class);
	
	@Autowired
	private	BankDetailsService bankDetailsService;
	
	@Autowired
	private	BankDetailsInfoService bankDetailsInfoService;
	
	@Autowired
	private ExternalServerHandler externalServerHandler;
	
	@Autowired
	private GLAccountTypeMasterService glAccountTypeMasterService;
	
	@Autowired
	private GLAccountStatementService glAccountStatementService;
	
	@Autowired
	private AccountMasterService accountMasterService;
	
	@Autowired
	private TierAccountMasterService tierAccountMasterService;
	
	@Override
	public ProcessResponse getThirdPartyBankList(TransactionConfig transactionConfig) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
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
		return processResponse;
	}

	@Override
	public ProcessResponse getThirdPartyAccountName(TransactionConfig transactionConfig) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
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
		return processResponse;
	}

	@Override
	public void processCreditThirdPartyGLBalanceUpdate(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster gLAccountTypeMaster) 
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
	public void processDebitThirdPartyGLBalanceUpdate(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster gLAccountTypeMaster) 
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
	public void processCreditControlGLBalanceUpdate(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster gLAccountTypeMaster) 
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
	public void processDebitControlGLBalanceUpdate(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster gLAccountTypeMaster) 
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
	public void processControlGLTransferOUTStatementEntry(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster gLAccountTypeMaster) 
	{
		try 
		{
			Double accountClosingBal = Utils.stringToDouble(gLAccountTypeMaster.getStrClosingBalance());
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			
			TransactionPostingConfig transactionPostConf = new TransactionPostingConfig();
			transactionPostConf.setTxnId(transactionPostingConfig.getTxnId());
			transactionPostConf.setTxnDate(transactionPostingConfig.getTxnDate());
			//transactionPostConf.setTxnType(transactionPostingConfig.getTxnType());
			transactionPostConf.setTxnType("TOT");
			
			accountClosingBal = accountClosingBal - txnAmount;
			String closingBalance = String.valueOf(accountClosingBal);
			
			transactionPostConf.setTxnAmount(String.valueOf(txnAmount));
			transactionPostConf.setClosingBalance(closingBalance);
			
			GLAccountStatement glAccountStatement = getDebitGLAccountStatementInstance(transactionPostConf, gLAccountTypeMaster);
			glAccountStatementService.addGLAccountStatement(glAccountStatement);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	
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

	//Added by Sunil Y , Reversal Part For TierLimit And Cummultive Limit , 2023-09-15 Start
	@Override
	public void updateAccountLimits(AccountResponse accountResponse, TierAccountResponse tierAccountResponse, String txnAmount) 
	{
		try
		{
			double txnAmt = Utils.stringToDouble(txnAmount);
			if(!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				AccountMaster accountMaster = new AccountMaster();	
				accountMaster.setStrAccountNumber(accountResponse.getStrAccountNumber());
				accountMaster.setStrCustId(accountResponse.getStrCustId());	
				
				Double availableDailyAmount = Double.parseDouble(accountResponse.getStrAvailableDailyLimit()) + txnAmt;
				Double availableMonthlyAmount = Double.parseDouble(accountResponse.getStrAvailableMonthlyLimit()) + txnAmt;
				Double availableYearlyAmount = Double.parseDouble(accountResponse.getStrAvailableYearlyLimit()) + txnAmt;
				
				accountMaster.setStrAvailableDailyLimit(Utils.decimalFormat.format(availableDailyAmount));			
				accountMaster.setStrAvailableMonthlyLimit(Utils.decimalFormat.format(availableMonthlyAmount));			
				accountMaster.setStrAvailableYearlyLimit(Utils.decimalFormat.format(availableYearlyAmount));
				accountMasterService.updateAccountMasterFields(accountMaster);
			}
			else
			{
				updateDailyCummulativeTxnLimit(txnAmt, tierAccountResponse);
			}
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}

	private void updateDailyCummulativeTxnLimit(Double txnAmount, TierAccountResponse tierAccountResponse) 
	{
		try 
		{
			if (tierAccountResponse.getIsFeeApplicable()) 
			{
				txnAmount = AccountUtility.getUpdatedTxnAmount(String.valueOf(txnAmount), null, tierAccountResponse);				
			}
			
			String activeTier = tierAccountResponse.getStrActiveTier();
			activeTier = (activeTier != null) ? activeTier.toLowerCase().trim() : "";			
			if (activeTier.length() > 0) 
			{
				String dailyAvailableTierLimit = tierAccountResponse.getTierWiseDailyCummulativeAvailableLimit().get(activeTier);
				double dailyAvailableTierLimitVal = Utils.stringToDouble(dailyAvailableTierLimit);
				
				double updatableDailyAvailableTxnLimit = dailyAvailableTierLimitVal + txnAmount;
				
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
	//Added by Sunil Y , Reversal Part For TierLimit And Cummultive Limit , 2023-09-15 End
}
