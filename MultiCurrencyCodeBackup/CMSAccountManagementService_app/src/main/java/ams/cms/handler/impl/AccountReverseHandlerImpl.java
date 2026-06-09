package ams.cms.handler.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.handler.AccountTxnHandler;
import ams.cms.api.handler.AgencyBankingHandler;
import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.AgencyBankingRequest;
import ams.cms.api.model.AgencyBankingResponse;
import ams.cms.api.model.TierAccountResponse;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.handler.AccountReverseHandler;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.services.AccountMasterService;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.AccountUtility;
import ams.cms.utility.Utils;

@Component
public class AccountReverseHandlerImpl  implements AccountReverseHandler 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(AccountReverseHandlerImpl.class);

	@Autowired
	private AccountMasterService accountMasterService;
	
	@Autowired
	private AccountTxnHandler accountTxnHandler;
	
	@Autowired
	private AgencyBankingHandler agencyBankingHandler;

	@Override
	public ProcessResponse processToReversedTxnFromTxn(ProcessResponse processResponse,List<AgencyBankingResponse> agencyBankingResponseList, AgencyBankingRequest agentTxnRequest) 
	{
		try 
		{
			HashMap<String, GLAccountTypeMaster> mapData = AccountUtility.getFeeVatAndOtherGLResponseMap(agencyBankingResponseList);  
			
			GLAccountTypeMaster feeGLAccountTypeMaster =  (mapData.containsKey("fee_gl")) ? mapData.get("fee_gl") : null;			
			GLAccountTypeMaster vatGLAccountTypeMaster =  (mapData.containsKey("vat_gl")) ? mapData.get("vat_gl") : null;			
			GLAccountTypeMaster otherGLAccountTypeMaster = (mapData.containsKey("other_gl")) ? mapData.get("other_gl") : null;
			
			AgencyBankingResponse agencyBankingResponse = agencyBankingResponseList.get(0);
			
			AccountCreation recipientAccountInfoIns = new AccountCreation();
			recipientAccountInfoIns.setStrAccountNumber(agencyBankingResponse.getAccountNo());
			
			AccountResponse recipientAccountInfo = accountMasterService.getAccountMasterInformation(recipientAccountInfoIns);
			if (recipientAccountInfo != null && recipientAccountInfo.getStrCustId() != null) 
			{
				TransactionPostingConfig transactionPostingConfig = new TransactionPostingConfig();
				transactionPostingConfig.setParticipantId(agentTxnRequest.getParticipantId());
				transactionPostingConfig.setTxnId(agencyBankingResponse.getAmsTransactionId());
				
				transactionPostingConfig.setTxnTime(Utils.getCurrentSqlTime());
				transactionPostingConfig.setTxnDate(Utils.getCurrentDate());
				
				transactionPostingConfig.setTxnType(agencyBankingResponse.getTxnType());				
				
				transactionPostingConfig.setStrSrcTxnId(agencyBankingResponse.getMontraTxnId());
				transactionPostingConfig.setAuthCode(agencyBankingResponse.getAuthCode());
				transactionPostingConfig.setResponseCode(agencyBankingResponse.getResponseCode());
				
				if (otherGLAccountTypeMaster != null) 
				{
					String txnAmount = otherGLAccountTypeMaster.getTxnAmount();
					Double txnAmt = Utils.stringToDouble(txnAmount);
					
					transactionPostingConfig.setTxnAmount(txnAmount);					
					transactionPostingConfig.setGlAccountTypeMaster(otherGLAccountTypeMaster);
					
					if (feeGLAccountTypeMaster != null && vatGLAccountTypeMaster != null) 
					{
						transactionPostingConfig.setIsFeeTypeExist(true);
						
						feeGLAccountTypeMaster.setFee(feeGLAccountTypeMaster.getTxnAmount());
						transactionPostingConfig.setFeeGLAccount(feeGLAccountTypeMaster);
						
						vatGLAccountTypeMaster.setVat(vatGLAccountTypeMaster.getTxnAmount());
						transactionPostingConfig.setVatGLAccount(vatGLAccountTypeMaster);
						
						txnAmt = txnAmt + Utils.stringToDouble(feeGLAccountTypeMaster.getTxnAmount()) + Utils.stringToDouble(vatGLAccountTypeMaster.getTxnAmount()); 
					}
					
					String currentAvailableBal = recipientAccountInfo.getStrClosingBalance();
					Double currAvailableBalance = Utils.stringToDouble(currentAvailableBal);
					
					currAvailableBalance = currAvailableBalance + txnAmt;					
					recipientAccountInfo.setAvailableBalance(Utils.decimalFormat.format(currAvailableBalance));
					
					transactionPostingConfig.setToAccount(recipientAccountInfo);
					
					TierAccountResponse toTierAccountMaster = AccountUtility.getTierAccountResponse(recipientAccountInfo);
					transactionPostingConfig.setToTierAccountMaster(toTierAccountMaster);
					
					GLAccountTypeMaster toLinkedGLAccountType = AccountUtility.getGLAccountTypeMaster(recipientAccountInfo);
					transactionPostingConfig.setToLinkGLAccount(toLinkedGLAccountType);	
					
					HashMap<String, String> linkedGLMap = new HashMap<String, String>();
					linkedGLMap.put(toLinkedGLAccountType.getStrAccountNumber().trim(), toLinkedGLAccountType.getStrClosingBalance().trim());
					transactionPostingConfig.setLinkedGLMap(linkedGLMap);
					
					accountTxnHandler.processAgentReversedDepositTransaction(transactionPostingConfig);
					
					if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
					{
						processResponse.setMessage("Transaction Revesed Successfully.");
						processResponse.setMontraTxnId(transactionPostingConfig.getStrSrcTxnId());
						processResponse.setAmsTransactionId(transactionPostingConfig.getTxnId());
						processResponse.setCid(agentTxnRequest.getCid());
						processResponse.setBid(agentTxnRequest.getBid());
						processResponse.setStrCustId(agentTxnRequest.getStrCustId());
						processResponse.setTxnAmount(agentTxnRequest.getTxnAmount());
						
						processResponse.setCurrentAvailableBalance(transactionPostingConfig.getToAccount().getAvailableBalance());
						
						processResponse.setTransactionDate(ams.cms.utility.Utils.simpleDateTimeFormat.format(transactionPostingConfig.getTxnDate()));
						processResponse.setResponseCode(transactionPostingConfig.getResponseCode());
						processResponse.setAuthCode(transactionPostingConfig.getAuthCode());
					}
				}				
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error!");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	@Override
	public ProcessResponse processReversedAgentDepositTransaction(ProcessResponse processResponse, List<AgencyBankingResponse> agencyBankingResponseList, AgencyBankingRequest agentTxnRequest)
	{
		try 
		{
			HashMap<String, GLAccountTypeMaster> mapData = AccountUtility.getFeeVatAndParkingGLResponseMap(agencyBankingResponseList);  
			
			GLAccountTypeMaster feeGLAccountTypeMaster =  (mapData.containsKey("fee_gl")) ? mapData.get("fee_gl") : null;			
			GLAccountTypeMaster vatGLAccountTypeMaster =  (mapData.containsKey("vat_gl")) ? mapData.get("vat_gl") : null;			
			GLAccountTypeMaster parkingGLAccountTypeMaster = (mapData.containsKey("parking_gl")) ? mapData.get("parking_gl") : null;
			
			AgencyBankingResponse agencyBankingResponse = agencyBankingResponseList.get(0);
			
			AccountCreation recipientAccountInfoIns = new AccountCreation();
			recipientAccountInfoIns.setStrAccountNumber(agencyBankingResponse.getAccountNo());
			
			AccountResponse recipientAccountInfo = accountMasterService.getAccountMasterInformation(recipientAccountInfoIns);
			if (recipientAccountInfo != null && recipientAccountInfo.getStrCustId() != null) 
			{
				TransactionPostingConfig transactionPostingConfig = new TransactionPostingConfig();
				transactionPostingConfig.setParticipantId(agentTxnRequest.getParticipantId());
				transactionPostingConfig.setTxnId(agencyBankingResponse.getAmsTransactionId());
				
				transactionPostingConfig.setTxnTime(Utils.getCurrentSqlTime());
				transactionPostingConfig.setTxnDate(Utils.getCurrentDate());
				
				transactionPostingConfig.setTxnType(agencyBankingResponse.getTxnType());				
				
				transactionPostingConfig.setStrSrcTxnId(agencyBankingResponse.getMontraTxnId());
				transactionPostingConfig.setAuthCode(agencyBankingResponse.getAuthCode());
				transactionPostingConfig.setResponseCode(agencyBankingResponse.getResponseCode());
				
				if (parkingGLAccountTypeMaster!=null) 
				{
					String txnAmount = parkingGLAccountTypeMaster.getTxnAmount();
					Double txnAmt = Utils.stringToDouble(txnAmount);
					
					transactionPostingConfig.setTxnAmount(txnAmount);					
					transactionPostingConfig.setGlAccountTypeMaster(parkingGLAccountTypeMaster);
					
					if (feeGLAccountTypeMaster != null && vatGLAccountTypeMaster != null) 
					{
						transactionPostingConfig.setIsFeeTypeExist(true);
						
						feeGLAccountTypeMaster.setFee(feeGLAccountTypeMaster.getTxnAmount());
						transactionPostingConfig.setFeeGLAccount(feeGLAccountTypeMaster);
						
						vatGLAccountTypeMaster.setVat(vatGLAccountTypeMaster.getTxnAmount());
						transactionPostingConfig.setVatGLAccount(vatGLAccountTypeMaster);
						
						txnAmt = txnAmt + Utils.stringToDouble(feeGLAccountTypeMaster.getTxnAmount()) + Utils.stringToDouble(vatGLAccountTypeMaster.getTxnAmount()); 
					}
					String currentAvailableBal = recipientAccountInfo.getStrClosingBalance();
					Double currAvailableBalance = Utils.stringToDouble(currentAvailableBal);
					
					currAvailableBalance = currAvailableBalance + txnAmt;					
					recipientAccountInfo.setAvailableBalance(Utils.decimalFormat.format(currAvailableBalance));
					
					transactionPostingConfig.setToAccount(recipientAccountInfo);
					
					TierAccountResponse toTierAccountMaster = AccountUtility.getTierAccountResponse(recipientAccountInfo);
					transactionPostingConfig.setToTierAccountMaster(toTierAccountMaster);
					
					GLAccountTypeMaster toLinkedGLAccountType = AccountUtility.getGLAccountTypeMaster(recipientAccountInfo);
					transactionPostingConfig.setToLinkGLAccount(toLinkedGLAccountType);	
					
					HashMap<String, String> linkedGLMap = new HashMap<String, String>();
					linkedGLMap.put(toLinkedGLAccountType.getStrAccountNumber().trim(), toLinkedGLAccountType.getStrClosingBalance().trim());
					transactionPostingConfig.setLinkedGLMap(linkedGLMap);
					
					accountTxnHandler.processAgentReversedDepositTransaction(transactionPostingConfig);
					
					if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
					{
						processResponse.setMessage("Transaction Revesed Successfully.");
						processResponse.setMontraTxnId(transactionPostingConfig.getStrSrcTxnId());
						processResponse.setAmsTransactionId(transactionPostingConfig.getTxnId());
						processResponse.setCid(agentTxnRequest.getCid());
						processResponse.setBid(agentTxnRequest.getBid());
						processResponse.setStrCustId(agentTxnRequest.getStrCustId());
						processResponse.setTxnAmount(agentTxnRequest.getTxnAmount());
						
						processResponse.setAgentAvailableBalance(transactionPostingConfig.getToAccount().getAvailableBalance());
						
						processResponse.setTransactionDate(ams.cms.utility.Utils.simpleDateTimeFormat.format(transactionPostingConfig.getTxnDate()));
						processResponse.setResponseCode(transactionPostingConfig.getResponseCode());
						processResponse.setAuthCode(transactionPostingConfig.getAuthCode());
					}
				}				
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	@Override
	public ProcessResponse processReversedAgentWithdrawalTransaction(ProcessResponse processResponse, List<AgencyBankingResponse> agencyBankingResponseList, AgencyBankingRequest agentTxnRequest) 
	{
		try 
		{
			HashMap<String, GLAccountTypeMaster> mapData = agencyBankingHandler.getAgencyBankingResponseMap(agencyBankingResponseList);
			
			GLAccountTypeMaster parkingGLAccountTypeMaster = (mapData.containsKey("parking_gl")) ? mapData.get("parking_gl") : null;
			
			AgencyBankingResponse agencyBankingResponse = agencyBankingResponseList.get(0);
			
			AccountCreation recipientAccountInfoIns = new AccountCreation();
			recipientAccountInfoIns.setStrAccountNumber(agencyBankingResponse.getAccountNo());
			
			AccountResponse recipientAccountInfo = accountMasterService.getAccountMasterInformation(recipientAccountInfoIns);
			if (recipientAccountInfo != null && recipientAccountInfo.getStrCustId() != null) 
			{
				TransactionPostingConfig transactionPostingConfig = new TransactionPostingConfig();
				transactionPostingConfig.setParticipantId(agentTxnRequest.getParticipantId());
				transactionPostingConfig.setTxnId(agencyBankingResponse.getAmsTransactionId());
				
				transactionPostingConfig.setTxnTime(Utils.getCurrentSqlTime());
				transactionPostingConfig.setTxnDate(Utils.getCurrentDate());
				
				transactionPostingConfig.setTxnType(agencyBankingResponse.getTxnType());				
				
				transactionPostingConfig.setStrSrcTxnId(agencyBankingResponse.getMontraTxnId());
				transactionPostingConfig.setAuthCode(agencyBankingResponse.getAuthCode());
				transactionPostingConfig.setResponseCode(agencyBankingResponse.getResponseCode());
				
				if (parkingGLAccountTypeMaster!=null) 
				{
					String txnAmount = parkingGLAccountTypeMaster.getTxnAmount();
					Double txnAmt = Utils.stringToDouble(txnAmount);
					
					transactionPostingConfig.setTxnAmount(txnAmount);					
					
					String currentAvailableBal = recipientAccountInfo.getStrClosingBalance();
					Double currAvailableBalance = Utils.stringToDouble(currentAvailableBal);
					
					currAvailableBalance = currAvailableBalance + txnAmt;					
					recipientAccountInfo.setAvailableBalance(Utils.decimalFormat.format(currAvailableBalance));
					
					transactionPostingConfig.setGlAccountTypeMaster(parkingGLAccountTypeMaster);
					transactionPostingConfig.setToAccount(recipientAccountInfo);
					
					TierAccountResponse toTierAccountMaster = AccountUtility.getTierAccountResponse(recipientAccountInfo);
					transactionPostingConfig.setToTierAccountMaster(toTierAccountMaster);
					
					GLAccountTypeMaster toLinkedGLAccountType = AccountUtility.getGLAccountTypeMaster(recipientAccountInfo);
					transactionPostingConfig.setToLinkGLAccount(toLinkedGLAccountType);	
					
					HashMap<String, String> linkedGLMap = new HashMap<String, String>();
					linkedGLMap.put(toLinkedGLAccountType.getStrAccountNumber().trim(), toLinkedGLAccountType.getStrClosingBalance().trim());
					transactionPostingConfig.setLinkedGLMap(linkedGLMap); 
					
					accountTxnHandler.processAgentReversedWithdrawalTransaction(transactionPostingConfig); 
					
					if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
					{
						processResponse.setMessage("Transaction Revesed Successfully.");
						processResponse.setMontraTxnId(transactionPostingConfig.getStrSrcTxnId());
						processResponse.setAmsTransactionId(transactionPostingConfig.getTxnId());
						processResponse.setCid(agentTxnRequest.getCid());
						processResponse.setBid(agentTxnRequest.getBid());
						processResponse.setStrCustId(agentTxnRequest.getStrCustId());
						processResponse.setTxnAmount(agentTxnRequest.getTxnAmount());
						
						processResponse.setCustomerAvailableBalance(transactionPostingConfig.getToAccount().getAvailableBalance());
						
						processResponse.setTransactionDate(ams.cms.utility.Utils.simpleDateTimeFormat.format(transactionPostingConfig.getTxnDate()));
						processResponse.setResponseCode(transactionPostingConfig.getResponseCode());
						processResponse.setAuthCode(transactionPostingConfig.getAuthCode());
					}
				}
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}

}
