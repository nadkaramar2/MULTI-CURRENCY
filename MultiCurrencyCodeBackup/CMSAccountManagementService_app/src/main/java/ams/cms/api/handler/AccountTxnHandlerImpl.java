package ams.cms.api.handler;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.TierAccountResponse;
import ams.cms.api.service.TransactionPostingIF;
import ams.cms.config.CommonConstants;
import ams.cms.config.TransactionIdCreationConfigDao;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.handler.FeeTypeHandler;
import ams.cms.logger.AMSLogger;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.AccountUtility;
import ams.cms.utility.Utils;

@Component
public class AccountTxnHandlerImpl implements AccountTxnHandler
{
	private AMSLogger amsLogger = AMSLogger.getInstance(AccountTxnHandlerImpl.class);
	
	@Autowired
	private TransactionIdCreationConfigDao transactionIdCreationConfigDao;
	
	@Autowired
	private TransactionPostingIF transactionPostingIF;
	
	@Autowired
	private FeeTypeHandler feeTypeHandler;
	
	private void createTransactionPostingData(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			String txnId = null;
			if (transactionPostingConfig.getTxnId() != null && transactionPostingConfig.getTxnId().trim().length() > 0) 
			{
				txnId = transactionPostingConfig.getTxnId();
			}
			else 
			{
				txnId = transactionIdCreationConfigDao.getTransactionId();
			}
			//String txnId = transactionIdCreationConfigDao.getTransactionId();
			
			transactionPostingConfig.setTxnId(txnId);
			transactionPostingConfig.setTxnDate(Utils.getCurrentDate());
			transactionPostingConfig.setTxnTime(Utils.getFormattedCurrentTime());
			
			transactionPostingConfig.setResponseCode("00");
			
			String authCode = Utils.getAlphaNumericString();
			transactionPostingConfig.setAuthCode(authCode);
			
			transactionPostingIF.createTransactionPostingData(transactionPostingConfig);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public ProcessResponse processClosedLoopTransaction(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, String fee, String vat) 
	{
		try 
		{
			processResponse = validateTxnAmount(processResponse, transactionPostingConfig);
			if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
			{
				processResponse = validateFeeVatGL(processResponse, transactionPostingConfig, fee, vat);
				if ("S0000".equalsIgnoreCase(processResponse.getCode()))
				{
					processResponse = validateTransaction(processResponse, transactionPostingConfig);
					if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
					{
						createTransactionPostingData(transactionPostingConfig);				
						
						transactionPostingIF.processClosedLoopTxnPosting(transactionPostingConfig);//P2P, P2M Transaction
					}
				}			
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Excepton Occured during process transaction");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	@Override
	public ProcessResponse processThirdPartyTxn(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, String fee, String vat) 
	{
		try 
		{
			processResponse = validateFeeVatGL(processResponse, transactionPostingConfig, fee, vat);
			if ("S0000".equalsIgnoreCase(processResponse.getCode()))
			{
				processResponse = validateSenderAccount(processResponse, transactionPostingConfig);
				if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
				{
					createTransactionPostingData(transactionPostingConfig);
					
					transactionPostingIF.processThirdPartyTransactionPosting(transactionPostingConfig);
				}
			}			
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Excepton Occured during process transaction");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	
	}
	
	@Override
	public ProcessResponse processAgentDepositTransaction(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, String fee, String vat) 
	{
		try 
		{
			processResponse = validateFeeVatGL(processResponse, transactionPostingConfig, fee, vat);
			if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
			{
				processResponse = validateAgentDepositTransaction(processResponse, transactionPostingConfig);
				if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
				{
					createTransactionPostingData(transactionPostingConfig);
					
					transactionPostingIF.processAgentDepositTxnPosting(transactionPostingConfig);
				}
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Excepton Occured during process transaction");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	@Override
	public ProcessResponse processAgentWithdrawalTxnPhaseI(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			processResponse = validateAgentWithdrawalTxn(processResponse, transactionPostingConfig);
			if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
			{
				createTransactionPostingData(transactionPostingConfig);
				
				transactionPostingIF.processAgentWithdrawalTxnPosting(transactionPostingConfig);
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Excepton Occured during process transaction");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	@Override
	public void processAgentConfirmDepositTransaction(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			transactionPostingIF.createTransactionPostingData(transactionPostingConfig);

			transactionPostingIF.processAgentConfirmDepositTxnPosting(transactionPostingConfig);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	@Override
	public void processAgentConfirmWithdrawalTransaction(TransactionPostingConfig transactionPostingConfig, String fee, String vat) 
	{
		try 
		{
			ProcessResponse processResponse = new ProcessResponse();
			processResponse.setCode("S0000");
			
			validateFeeVatGL(processResponse, transactionPostingConfig, fee, vat);
			
			transactionPostingIF.createTransactionPostingData(transactionPostingConfig);
			
			transactionPostingIF.processAgentConfirmWithdrawalTxnPosting(transactionPostingConfig);
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
			transactionPostingIF.createTransactionPostingData(transactionPostingConfig);
			
			transactionPostingIF.processAgentReversedDepositTransaction(transactionPostingConfig);
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
			transactionPostingIF.createTransactionPostingData(transactionPostingConfig);
			
			transactionPostingIF.processAgentReversedWithdrawalTransaction(transactionPostingConfig);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public ProcessResponse processLoadMoneyTransaction(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, String fee, String vat) 
	{
		try 
		{
			processResponse = validateFeeVatGL(processResponse, transactionPostingConfig, fee, vat);
			if ("S0000".equalsIgnoreCase(processResponse.getCode()))
			{
				processResponse = validateLoadMoneyTransaction(processResponse, transactionPostingConfig);
				if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
				{
					transactionPostingConfig.setFee(fee);
					transactionPostingConfig.setVat(vat);
					
					createTransactionPostingData(transactionPostingConfig);					
					
					transactionPostingIF.processLoadMoneytxnPostingData(transactionPostingConfig);
				}
			}			
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Excepton Occured during process transaction");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	@Override
	public ProcessResponse processBillPayTransaction(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, String fee, String vat) 
	{
		try 
		{
			processResponse = validateFeeVatGL(processResponse, transactionPostingConfig, fee, vat);
			if ("S0000".equalsIgnoreCase(processResponse.getCode()))
			{
				processResponse = validateBillPayTransaction(processResponse, transactionPostingConfig);
				if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
				{
					transactionPostingConfig.setFee(fee);
					transactionPostingConfig.setVat(vat);
					
					createTransactionPostingData(transactionPostingConfig);					
					
					transactionPostingIF.processBillPayTxnPostingData(transactionPostingConfig);
				}
			}			
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Excepton Occured during process transaction");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	private ProcessResponse validateFeeVatGL(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, String fee, String vat) 
	{
		try 
		{
			String feeType = (transactionPostingConfig.getFeeType() != null) ? transactionPostingConfig.getFeeType().trim():"";
			if (feeType.trim().length() > 0) 
			{
				String feeApplicabeTo = (transactionPostingConfig.getFeeApplicableTo()!=null ) ? transactionPostingConfig.getFeeApplicableTo().trim(): "";
				
				boolean isFeeVatTxnByPass = false;
				if (feeApplicabeTo.trim().length() > 0) 
				{
					amsLogger.writeInfoLog("In validateFeeVatGL fee=["+fee+"] vat=["+vat+"]");
					
					if (fee != null && fee.trim().length() > 0 && AccountUtility.isNumeric(fee)) 
					{
						fee = fee.trim();
						if (vat != null && vat.trim().length() > 0 && AccountUtility.isNumeric(vat)) 
						{
							vat = vat.trim();
						}
						else 
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Invalid Vat Value!");
						}
					}
					else 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Invalid Fee Value!");
					}
					
					amsLogger.writeInfoLog("-->> In validateFeeVatGL processResponse=["+processResponse+"]");
					if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
					{
						if (fee != null && !AccountUtility.isGreaterThanZero(fee) && vat != null && !AccountUtility.isGreaterThanZero(vat)) 
						{
							isFeeVatTxnByPass = true;
						}					
						if (!isFeeVatTxnByPass) 
						{
							if (fee != null && !fee.trim().equalsIgnoreCase("0")) 
							{
								if (vat != null && vat.trim().equalsIgnoreCase("0"))
								{
									processResponse.setCode("E0000");
									processResponse.setStatus("Failed");
									processResponse.setMessage("Vat Details are not available!");
								}
							}
							else 
							{
								processResponse.setCode("E0000");
								processResponse.setStatus("Failed");
								processResponse.setMessage("Fee Details are not available!");
							}
						}					
					}					
				}
				else
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Fee Applicable To Details are not available!");
				}
				
				amsLogger.writeInfoLog("-->>--- In validateFeeVatGL isFeeVatTxnByPass=["+isFeeVatTxnByPass+"] processResponse=["+processResponse+"]");
				if (!isFeeVatTxnByPass && "S0000".equalsIgnoreCase(processResponse.getCode()))
				{
					transactionPostingConfig.setIsFeeTypeExist(true);
					transactionPostingConfig = feeTypeHandler.getFeeAndVatGLAccounts(transactionPostingConfig);															
					if (transactionPostingConfig.getFeeGLAccount()!=null) 
					{
						if (transactionPostingConfig.getVatGLAccount()!=null) 
						{
							transactionPostingConfig.getFeeGLAccount().setFee(fee); 
							transactionPostingConfig.getVatGLAccount().setVat(vat);
							
							if ("sender".equalsIgnoreCase(feeApplicabeTo)) 
							{
								transactionPostingConfig.getFromAccount().setIsFeeApplicable(true);
								transactionPostingConfig.getFromAccount().setFee(fee);
								transactionPostingConfig.getFromAccount().setVat(vat);
								
								transactionPostingConfig.getFromAccount().setAvailableBalance(AccountUtility.getUpdatedAvailableBalance(transactionPostingConfig.getFromAccount().getAvailableBalance(), null, fee, vat));
								
								transactionPostingConfig.getFromTierAccountMaster().setIsFeeApplicable(true);
								transactionPostingConfig.getFromTierAccountMaster().setFee(fee);
								transactionPostingConfig.getFromTierAccountMaster().setVat(vat);
								
								transactionPostingConfig.getFromLinkGLAccount().setIsFeeApplicable(true);
								transactionPostingConfig.getFromLinkGLAccount().setFee(fee);
								transactionPostingConfig.getFromLinkGLAccount().setVat(vat);
							}
							else 
							{
								transactionPostingConfig.getToAccount().setIsFeeApplicable(true);
								transactionPostingConfig.getToAccount().setIsFeeApplicableToRecipent(true);
								transactionPostingConfig.getToAccount().setFee(fee);
								transactionPostingConfig.getToAccount().setVat(vat);
								
								transactionPostingConfig.getToAccount().setAvailableBalance(AccountUtility.getUpdatedAvailableBalance(transactionPostingConfig.getToAccount().getAvailableBalance(), null, fee, vat));
								
								transactionPostingConfig.getToTierAccountMaster().setIsFeeApplicable(true);
								transactionPostingConfig.getToTierAccountMaster().setIsFeeApplicableToRecipent(true);
								transactionPostingConfig.getToTierAccountMaster().setFee(fee);
								transactionPostingConfig.getToTierAccountMaster().setVat(vat);
								
								transactionPostingConfig.getToLinkGLAccount().setIsFeeApplicable(true);
								transactionPostingConfig.getToLinkGLAccount().setFee(fee);
								transactionPostingConfig.getToLinkGLAccount().setVat(vat);
							}
						}
						else
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Vat GL Account Type Not Found!");
						}
					}
					else 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Fee GL Account Type Not Found!");
					}
				}
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Excepton Occured during validateFeeVatGL");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		amsLogger.writeInfoLog("-->> In validateFeeVatGL Final processResponse=["+processResponse+"]");
		return processResponse;	
	}
	
	private ProcessResponse validateTransaction(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			processResponse = validateSenderAccount(processResponse, transactionPostingConfig);
			if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
			{
				processResponse = validateRecipentAccount(processResponse, transactionPostingConfig, transactionPostingConfig.getToAccount());
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Excepton Occured during validate Transaction");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	private ProcessResponse validateAgentDepositTransaction(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			processResponse = validateSenderAccount(processResponse, transactionPostingConfig);
			if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
			{
				processResponse = validateRecipentAccount(processResponse, transactionPostingConfig, transactionPostingConfig.getToAccount());
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Excepton Occured during validate Transaction");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	private ProcessResponse validateAgentWithdrawalTxn(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			processResponse = validateSenderAccount(processResponse, transactionPostingConfig);
			if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
			{
				processResponse = validateRecipentAccount(processResponse, transactionPostingConfig, transactionPostingConfig.getToAccount());
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Excepton Occured during validate Transaction");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	//Validate Sender Account Start
	private ProcessResponse validateSenderAccount(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			AccountResponse senderAccountInfo = transactionPostingConfig.getFromAccount();
			
			processResponse = validateAccountBalance(processResponse, transactionPostingConfig, senderAccountInfo);
			if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
			{
				if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
				{
					TierAccountResponse senderTierAccount = transactionPostingConfig.getFromTierAccountMaster();					
					processResponse = validateDailyTierLimit(processResponse, transactionPostingConfig, senderTierAccount);
				}
				else 
				{
					processResponse = validateAccountLimits(processResponse, transactionPostingConfig, senderAccountInfo);
				}
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Excepton Occured during validate Sender Account");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	private ProcessResponse validateRecipentAccount(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, AccountResponse accountResponse) 
	{
		try 
		{
			TierAccountResponse recipentTierAccountMaster = transactionPostingConfig.getToTierAccountMaster();
			if (recipentTierAccountMaster != null) 
			{
				processResponse = validateCummulativeTierBalance(processResponse, transactionPostingConfig, recipentTierAccountMaster);
			}
			if (accountResponse.getIsFeeApplicableToRecipent()) 
			{
				processResponse = validateAccountBalance(processResponse, transactionPostingConfig, accountResponse);
				if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
				{
					if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
					{
						processResponse = validateDailyTierLimit(processResponse, transactionPostingConfig, recipentTierAccountMaster);
					}
					else 
					{
						processResponse = validateAccountLimits(processResponse, transactionPostingConfig, accountResponse);
					}
				}
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Excepton Occured during validate Recipent Account");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	private ProcessResponse validateAccountBalance(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, AccountResponse accountResponse) 
	{
		try 
		{
			Double availableBalance = AccountUtility.getAccountUpdatedBalance(accountResponse);
			
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			if (accountResponse.getIsFeeApplicable()) 
			{
				if (accountResponse.getIsFeeApplicableToRecipent()) 
				{
					Double updatedBalance = AccountUtility.getUpdatedBalance(String.valueOf(txnAmount), accountResponse);
					Double feeVatVal = AccountUtility.getFeeVatValue(accountResponse);
					availableBalance = updatedBalance;
					txnAmount = feeVatVal;				
				}
				else
				{
					txnAmount = AccountUtility.getUpdatedTxnAmount(transactionPostingConfig.getTxnAmount(), accountResponse, null);												
				}
			}
			
			if( availableBalance < txnAmount) 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Insufficient Balance!");
			}			
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	private ProcessResponse validateAccountLimits(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, AccountResponse accountResponse) 
	{
		try 
		{
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			if (accountResponse.getIsFeeApplicable()) 
			{
				txnAmount = AccountUtility.getUpdatedTxnAmount(transactionPostingConfig.getTxnAmount(), accountResponse, null);
				if (accountResponse.getIsFeeApplicableToRecipent()) 
				{
					txnAmount = Utils.stringToDouble(accountResponse.getFee()) + Utils.stringToDouble(accountResponse.getVat());
				}
			}
			
			String dailyTxnLimit = accountResponse.getStrDailyTxnLimit();
			double dailyTxnLimitVal = Utils.stringToDouble(dailyTxnLimit);			
			if (dailyTxnLimitVal > txnAmount) 
			{
				String availableDailyLimit = accountResponse.getStrAvailableDailyLimit();
				double availableDailyLimitVal = Utils.stringToDouble(availableDailyLimit);
				if( availableDailyLimitVal < txnAmount )
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Availabe Daily Limit Amount Breached!");
					return processResponse; 
				}
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Daily Transaction Limit Amount Breached!");
				return processResponse; 
			}
			
			String monthlyTxnLimit = accountResponse.getStrMonthlyTxnLimit();
			double monthlyTxnLimitVal = Utils.stringToDouble(monthlyTxnLimit);			
			if (monthlyTxnLimitVal > txnAmount) 
			{
				String availableMonthlyLimit = accountResponse.getStrAvailableMonthlyLimit();
				double availableMonthlyLimitVal = Utils.stringToDouble(availableMonthlyLimit);
				if( availableMonthlyLimitVal < txnAmount )
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Availabe Monthly Transaction Limit Amount Breached!"); 
					return processResponse;
				}
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Monthly Transaction Limit Amount Breached!");
				return processResponse; 
			}			
			
			String yearlyTxnLimit = accountResponse.getStrYearlyTxnLimit();
			double yearlyTxnLimitVal = Utils.stringToDouble(yearlyTxnLimit);
			if (yearlyTxnLimitVal > txnAmount) 
			{
				String availableYearlyLimit = accountResponse.getStrAvailableYearlyLimit();
				double availableYearlyLimitVal = Utils.stringToDouble(availableYearlyLimit);
				if(availableYearlyLimitVal < txnAmount )
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Availabe Yearly Transaction Limit Amount Breached!");
					return processResponse; 
				}
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Yearly Transaction Limit Amount Breached!");
				return processResponse; 
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	private ProcessResponse validateCummulativeTierBalance(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, TierAccountResponse tierAccountResponse) 
	{
		try 
		{
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			
			String updatedStrBalance =  Utils.getIncreaseAmount(tierAccountResponse.getAccountAvailableBalance(), String.valueOf(txnAmount));
			double updatedBalance = Utils.stringToDouble(updatedStrBalance);
			
			String activeTier = tierAccountResponse.getStrActiveTier();
			activeTier = (activeTier != null) ? activeTier.toLowerCase().trim() : "";
			
			if (activeTier.length() > 0) 
			{
				String cummulativeTierBalance =	tierAccountResponse.getTierWiseCummulativeBalanceMap().get(activeTier);
				double cummulativeTierBalanceVal = Utils.stringToDouble(cummulativeTierBalance);
				
				if (updatedBalance > cummulativeTierBalanceVal) 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed"); 
					processResponse.setMessage("Commulative Balance Breached!");
				}
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed"); 
				processResponse.setMessage("Active Tier Not Found");
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	private ProcessResponse validateDailyTierLimit(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig, TierAccountResponse tierAccountResponse) 
	{
		try 
		{
			Double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());
			if (tierAccountResponse.getIsFeeApplicable()) 
			{
				txnAmount = AccountUtility.getUpdatedTxnAmount(transactionPostingConfig.getTxnAmount(), null, tierAccountResponse);				
				if (tierAccountResponse.getIsFeeApplicableToRecipent()) 
				{
					txnAmount = Utils.stringToDouble(tierAccountResponse.getFee()) + Utils.stringToDouble(tierAccountResponse.getVat());
				}				
			}
			
			String activeTier = tierAccountResponse.getStrActiveTier();
			activeTier = (activeTier != null) ? activeTier.toLowerCase().trim() : "";			
			if (activeTier.length() > 0) 
			{
				String dailyTierLimit =	tierAccountResponse.getTierWiseDailyCummulativeLimitMap().get(activeTier);
				double dailyTierLimitVal = Utils.stringToDouble(dailyTierLimit);
				
				if (txnAmount < dailyTierLimitVal) 
				{
					String dailyAvailableTierLimit = tierAccountResponse.getTierWiseDailyCummulativeAvailableLimit().get(activeTier);
					double dailyAvailableTierLimitVal = Utils.stringToDouble(dailyAvailableTierLimit);
					if (txnAmount > dailyAvailableTierLimitVal) 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Available Daily Transaction Limit Breached!");
					}
				}
				else
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Daily Transaction Limit Breached!");
				}
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed"); 
				processResponse.setMessage("Active Tier Not Found!");
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	private ProcessResponse validateLoadMoneyTransaction(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			processResponse = validateRecipentAccount(processResponse, transactionPostingConfig, transactionPostingConfig.getToAccount());
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Excepton Occured during validate Transaction");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	private ProcessResponse validateBillPayTransaction(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			processResponse = validateSenderAccount(processResponse, transactionPostingConfig);
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Excepton Occured during validate Transaction");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	@Override
	public void updateTransactionRequestLogData(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public ProcessResponse validateEntityInfoAndNumber(String decrypt, ProcessResponse processResponse) 
	{
		processResponse.setCode("S0000");
		processResponse.setStatus("Success");
		try 
		{
			Object requestObject =  new ObjectMapper().readValue(decrypt, Object.class);
			if(requestObject != null) 
			{
				JSONObject jsonObject = (JSONObject) JSONValue.parse(new ObjectMapper().writeValueAsString(requestObject));
				
				String entityInfo = null;
				String entityNumber = null;
				if (jsonObject.containsKey("entityInfo")) 
				{
					entityInfo = (String)jsonObject.get("entityInfo");
					if (jsonObject.containsKey("entityNumber")) 
					{
						entityNumber = (String)jsonObject.get("entityNumber");
					}
					else 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Entity Number key not found!");
					}
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Entity Info key not found!");
				}
			
				if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
				{
					if((entityInfo != null && entityInfo.trim().length() > 0 && !entityInfo.equalsIgnoreCase("null"))) 
					{
						if(!(entityNumber != null && entityInfo.trim().length() > 0 && !entityNumber.equalsIgnoreCase("null"))) 
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Entity Number can not be blank or null!");
						}
					}
					else 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Entity info can not be blank or null!");
					}
				}			
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("No Request Found!");
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error during entityInfo and number validation!");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	private ProcessResponse validateTxnAmount(ProcessResponse processResponse, TransactionPostingConfig transactionPostingConfig) 
	{
		processResponse.setCode("S0000");
		processResponse.setStatus("Success");
		try 
		{
			String txnAmount = transactionPostingConfig.getTxnAmount();
			if (AccountUtility.isNumeric(txnAmount)) 
			{
				if (!AccountUtility.isGreaterThanZero(txnAmount)) 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Transaction Amount should be greater than 0");
				}
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Transaction Amount should be number not an string!");
			}			
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error during validateTxnAmount!");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
}
