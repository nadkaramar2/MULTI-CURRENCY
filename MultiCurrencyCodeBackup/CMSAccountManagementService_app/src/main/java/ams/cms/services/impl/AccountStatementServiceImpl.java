package ams.cms.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.model.AccountStatementResponse;
import ams.cms.api.model.AccountStatementSortResponse;
import ams.cms.api.model.AccountTranscationResponse;
import ams.cms.api.response.TransactionStatementDetails;
import ams.cms.api.response.TransactionSummaryDetails;
import ams.cms.api.response.TxnDetails;
import ams.cms.api.response.TxnStatementDetails;
import ams.cms.api.response.TxnStatementDetailsMaster;
import ams.cms.api.response.TxnSummary;
import ams.cms.config.CommonConstants;
import ams.cms.dao.AccountMasterDao;
import ams.cms.dao.AccountStatementDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountStatement;
import ams.cms.model.AccountTxnResponse;
import ams.cms.model.TransactionInformation;
import ams.cms.model.TransactionSummary;
import ams.cms.services.AccountStatementService;
import ams.cms.utility.Utils;

@Transactional
@Service
public class AccountStatementServiceImpl implements AccountStatementService
{
	private AMSLogger amsLogger = AMSLogger.getInstance(AccountStatementServiceImpl.class);
	
	@Autowired
	private AccountStatementDao accountStatementDao;
	
	@Autowired 
	private AccountMasterDao accountMasterDao;
	
	@Override
	public AccountStatement addAccountStatement(AccountStatement accountStatement) throws Exception 
	{
		accountStatementDao.save(accountStatement);
		return accountStatement;
	}

	@Override
	public AccountStatement addAccountTransactionData(AccountStatement accountStatement) throws Exception 
	{
		accountStatement.setTransactionDate(new Date());
		accountStatementDao.save(accountStatement);
		return accountStatement;
	}
	
	@Override
	public List<AccountStatement> getAccountStatementBasedonParameter(AccountStatement accountStatement) throws Exception 
	{
		return accountStatementDao.getAccountStatementBasedonParameter(accountStatement);
	}
	
	@Override
	public List<AccountStatementResponse> getAccountStatementlist(AccountStatement accountStatement) throws Exception
	{
		List<AccountStatement> accountStatementlist=accountStatementDao.getAccountStatementBasedonParameter(accountStatement);
		
		List<AccountStatementResponse> accountStatementResponses=new ArrayList<>();
		
		for(int i=0;i<accountStatementlist.size();i++)
		{
			AccountStatement accountStatmentData =accountStatementlist.get(i);
			
			AccountStatementResponse mapAccountStatementToAccountStatementResponse= mapAccountStatementToAccountStatementResponse(accountStatmentData);
			accountStatementResponses.add(mapAccountStatementToAccountStatementResponse);
		}
	    return accountStatementResponses;
	}
	
	private AccountStatementResponse mapAccountStatementToAccountStatementResponse(AccountStatement accountStatmentData) {
	     
		AccountStatementResponse accountStatementResponse=new AccountStatementResponse();
		accountStatementResponse.setStrParticipantId(accountStatmentData.getStrParticipantId());
		accountStatementResponse.setStrAccountNumber(accountStatmentData.getStrAccountNumber());
		accountStatementResponse.setStrAccountType(accountStatmentData.getStrAccountType());
		accountStatementResponse.setStrClosingBalance(accountStatmentData.getStrClosingBalance());
		accountStatementResponse.setStrTransactionAmount(accountStatmentData.getStrTransactionAmount());
		accountStatementResponse.setStrTransactionID(accountStatmentData.getStrTransactionID());
		accountStatementResponse.setStrTransactionType(accountStatmentData.getStrTransactionType());
		accountStatementResponse.setStrTransactionMode(accountStatmentData.getStrTranMode());
		accountStatementResponse.setStrTransactionDate(accountStatmentData.getStrTransactionDate());
		accountStatementResponse.setCurrencyCode(accountStatmentData.getCurrencyCode());
		
		return accountStatementResponse;
	}

	@Override
	public List<AccountStatementSortResponse> updateAccountStatementlist(AccountStatement accountStatement) throws Exception 
	{
      List<AccountStatement> accountUpdatelist=accountStatementDao.updateAccountStatementlist(accountStatement);
		
		List<AccountStatementSortResponse> accountStatementSortResponse =new ArrayList<>();
		
		for (int i = 0; i < accountUpdatelist.size(); i++) 
		{
			AccountStatement accountStatementSortData=accountUpdatelist.get(i);
			
			AccountStatementSortResponse mapAccountUpateToAccountStatementSortResponse=mapAccountUpateToAccountStatementSortResponse(accountStatementSortData);
			accountStatementSortResponse.add(mapAccountUpateToAccountStatementSortResponse);
		
		}	
		return accountStatementSortResponse;				  
	}
	
	private AccountStatementSortResponse mapAccountUpateToAccountStatementSortResponse(AccountStatement accountStatementSortData) {
	     
		AccountStatementSortResponse accountStatementSortResponse=new AccountStatementSortResponse();
		accountStatementSortResponse.setStrParticipantId(accountStatementSortData.getStrParticipantId());
		accountStatementSortResponse.setStrAccountNumber(accountStatementSortData.getStrAccountNumber());
		accountStatementSortResponse.setStrAccountType(accountStatementSortData.getStrAccountType());
		accountStatementSortResponse.setStrClosingBalance(accountStatementSortData.getStrClosingBalance());
		accountStatementSortResponse.setStrTransactionAmount(accountStatementSortData.getStrTransactionAmount());
		accountStatementSortResponse.setStrTransactionID(accountStatementSortData.getStrTransactionID());
		accountStatementSortResponse.setStrTransactionType(accountStatementSortData.getStrTransactionType());
		accountStatementSortResponse.setStrTransactionMode(accountStatementSortData.getStrTranMode());
		accountStatementSortResponse.setStrTransactionDate(accountStatementSortData.getStrTransactionDate());
		
		return accountStatementSortResponse;
	}

	@Override
	public List<AccountStatement> getAccountStatements(AccountStatement accountStatement) throws Exception {
		return accountStatementDao.getAccountStatements(accountStatement);
	}

	@Override
	public List<AccountStatement> getAccountStatementByDate(AccountStatement accountStatement) throws Exception {
		return accountStatementDao.getAccountStatementByDate(accountStatement);
	}

	@Override
	public AccountStatement saveSenderAccountstAccountStatement(AccountStatement accountStatement) throws Exception {
		accountStatement.setTransactionDate(new Date());
		accountStatementDao.save(accountStatement);
		return accountStatement;
	}

	@Override
	public AccountStatement saveRecipientAccountStatement(AccountStatement accountStatement) throws Exception {
		accountStatement.setTransactionDate(new Date());
		accountStatementDao.save(accountStatement);
		return accountStatement;
	}	
	
	@Override
	public List<AccountTranscationResponse> getLastFiveAccountTxn(AccountStatement accountStatement) 
	{
		List<AccountStatement> accountTransactionlist = null;
		if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
		{
			accountTransactionlist = accountStatementDao.getLastFiveAccountTxnForMontra(accountStatement);
		}
		else
		{
			accountTransactionlist = accountStatementDao.getLastFiveAccountTxn(accountStatement);
		}
		//List<AccountStatement> accountTransactionlist = accountStatementDao.getLastFiveAccountTxn(accountStatement);
		
		return getAccountTransactionResponseList(accountTransactionlist);
	}

	@Override
	public List<AccountTranscationResponse> getAccountTranscation(AccountStatement accountStatement) 
	{
		
		List<AccountStatement> accountTransactionlist = accountStatementDao.getAccountTranscation(accountStatement);
		return getAccountTransactionResponseList(accountTransactionlist);
	}
	
	private List<AccountTranscationResponse> getAccountTransactionResponseList(List<AccountStatement> accountTransactionlist)
	{
		List<AccountTranscationResponse> accountTransactionResponseList = new ArrayList<AccountTranscationResponse>();
		try 
		{  
			if (accountTransactionlist != null && accountTransactionlist.size() > 0) 
			{
			    for(int i=0;i<accountTransactionlist.size();i++)
			    {
			    	AccountStatement accountStatmentInfo = accountTransactionlist.get(i);
			    	
			    	try 
					{
			    		accountStatmentInfo.setStrTransactionDate(ams.cms.utility.Utils.simpleDateTimeFormat.format(accountStatmentInfo.getTransactionDate()));
					}
					catch (Exception e) {
						amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
					}
				
			    	AccountTranscationResponse mapAccountTransactionToAccountTranscationResponse= mapAccountTransactionToAccountTranscationResponse(accountStatmentInfo);
			    	accountTransactionResponseList.add(mapAccountTransactionToAccountTranscationResponse);
			    }
			    return accountTransactionResponseList;
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
			

	private AccountTranscationResponse mapAccountTransactionToAccountTranscationResponse(AccountStatement accountStatmentInfo) 
	{
		AccountTranscationResponse accountTranscationResponse = new AccountTranscationResponse();
		accountTranscationResponse.setStrAccountType(accountStatmentInfo.getStrAccountType());
		accountTranscationResponse.setStrClosingBalance(accountStatmentInfo.getStrClosingBalance());
		accountTranscationResponse.setStrTransactionAmount(accountStatmentInfo.getTransactionAmount());
		accountTranscationResponse.setStrTransactionID(accountStatmentInfo.getStrTransactionID());
		accountTranscationResponse.setStrTransactionType(accountStatmentInfo.getStrTransactionType());
		accountTranscationResponse.setStrTransactionMode(accountStatmentInfo.getStrTranMode());
		accountTranscationResponse.setStrTransactionDate(accountStatmentInfo.getStrTransactionDate());
		accountTranscationResponse.setMontraTxnId(accountStatmentInfo.getMontraTxnId());
		
		//Added Changes for Account Statement Structure by Pankaj Pawar Start
		if(accountStatmentInfo.getEntityInfo() != null) {
			accountTranscationResponse.setEntityInfo(accountStatmentInfo.getEntityInfo());
		}
		if(accountStatmentInfo.getEntityNumber() != null) {
			accountTranscationResponse.setEntityNumber(accountStatmentInfo.getEntityNumber());
		}
		if(accountStatmentInfo.getStrIsGLType() != null)
		{	
			accountTranscationResponse.setStrIsFeeTxn("NO");
			accountTranscationResponse.setStrIsTxn("NO");
			accountTranscationResponse.setStrIsVatTxn("NO");
			
			if("T".equalsIgnoreCase(accountStatmentInfo.getStrIsGLType()))
			{
				accountTranscationResponse.setStrIsTxn("YES");				
			}
			else if("F".equalsIgnoreCase(accountStatmentInfo.getStrIsGLType()))				
			{
				accountTranscationResponse.setStrIsFeeTxn("YES");
			}
			else if("V".equalsIgnoreCase(accountStatmentInfo.getStrIsGLType())) 
			{
				accountTranscationResponse.setStrIsVatTxn("YES");
			}			
		}
		//Added Changes for Account Statement Structure by Pankaj Pawar End
		
		return accountTranscationResponse;
	}
	
	@Override
	public AccountStatement addAccountStatementData(AccountStatement accountStatement) 
	{
		accountStatement.setTransactionDate(new Date());
		accountStatementDao.save(accountStatement);
		return accountStatement;

	}

	//@Transactional
	@Override
	public int[] batchEntryOfAccountStatementMaster(List<AccountStatement> accountStatements) throws Exception 
	{
		return accountStatementDao.batchEntryOfAccountStatementMaster(accountStatements);
	}
	
	@Override
	public List<AccountTranscationResponse> getAccountTransaction(AccountStatement accountStatement, TransactionInformation transactionInformation) 
	{
		List<AccountTranscationResponse> accountTxnResponseDataList = null;
		try 
		{
			List<AccountStatement> accountTransactionlist = accountStatementDao.getAccountTranscation(accountStatement);			
			if (accountTransactionlist != null && accountTransactionlist.size() > 0) 
			{
				amsLogger.writeInfoLog("Inside getAccountTransaction accountTransactionlist size::["+accountTransactionlist.size()+"]");
				accountTxnResponseDataList = getAccountTranscationResponseList(accountTransactionlist, transactionInformation);
			}			
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountTxnResponseDataList;
	}
	
	@Override
	public List<AccountTranscationResponse> getAgencyAccountStatementTransactionList(AccountStatement accountStatement, TransactionInformation transactionInformation) 
	{
		List<AccountTranscationResponse> accountTxnResponseDataList = null;
		try 
		{
			List<AccountStatement> accountTransactionlist = accountStatementDao.getAgencyAccountStatementTranscationList(accountStatement);
			
			if (accountTransactionlist !=null && accountTransactionlist.size() > 0) 
			{
				amsLogger.writeInfoLog("Inside getAccountTransaction accountTransactionlist size::["+accountTransactionlist.size()+"]");
				accountTxnResponseDataList = getAccountTranscationResponseList(accountTransactionlist, transactionInformation);
			}			
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountTxnResponseDataList;
	}
	
	@Override
	public List<AccountTranscationResponse> getLastFiveAccountTransaction(AccountStatement accountStatement, TransactionInformation transactionInformation) 
	{
		List<AccountTranscationResponse> accountTxnResponseDataList = null;
		try 
		{
			List<AccountStatement> accountTransactionlist = accountStatementDao.getLastFiveAccountTransaction(accountStatement);
			if (accountTransactionlist != null && accountTransactionlist.size() > 0) 
			{
				amsLogger.writeInfoLog("Inside getLastFiveAccountTransaction accountTransactionlist size::["+accountTransactionlist.size()+"]");
				accountTxnResponseDataList = getAccountTranscationResponseList(accountTransactionlist, transactionInformation);
			}			
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountTxnResponseDataList;
	}

	@Override
	public List<AccountTranscationResponse> getLastFiveAgencyAccountStatementTransactionList(AccountStatement accountStatement, TransactionInformation transactionInformation) 
	{
		List<AccountTranscationResponse> accountTxnResponseDataList = null;
		try 
		{
			List<AccountStatement> accountTransactionlist = accountStatementDao.getLastFiveAgencyAccountStatementTransactionList(accountStatement);
			if (accountTransactionlist != null && accountTransactionlist.size() > 0) 
			{
				amsLogger.writeInfoLog("Inside getAccountTransaction accountTransactionlist size::["+accountTransactionlist+"]");
				accountTxnResponseDataList = getAccountTranscationResponseList(accountTransactionlist, transactionInformation);
			}			
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountTxnResponseDataList;
	}
	
	@Override
	public TransactionInformation getTotalCreditDebitAmountAndCount(AccountStatement accountStatement) 
	{	
		TransactionInformation transactionInformation = null;
		try 
		{
			List<AccountStatement> accountStatements = accountStatementDao.getTotalTransModeAndTransactionAmount(accountStatement);
			if (accountStatements != null && accountStatements.size() > 0) 
			{
				transactionInformation = new TransactionInformation();
				for (AccountStatement accountStmt: accountStatements) 
				{
					Double totalTransactionAmount = Utils.stringToDouble(accountStmt.getStrTotalTransactionAmount());
					Integer totalTransModeCount = Utils.stringToInt(accountStmt.getTotalTransModeCount());
					
					String tranMode = accountStmt.getStrTranMode();
					amsLogger.writeInfoLog("tranMode=["+tranMode+"]");					
					if ("credit".equalsIgnoreCase(tranMode.trim())) 
					{
						transactionInformation.setTotalCreditCount(totalTransModeCount);
						transactionInformation.setTotalCreditAmount(Utils.stringToDouble(Utils.decimalFormat.format(totalTransactionAmount)));
					}
					else 
					{
						transactionInformation.setTotalDebitCount(totalTransModeCount);
						transactionInformation.setTotalDebitAmount(Utils.stringToDouble(Utils.decimalFormat.format(totalTransactionAmount)));
					}
				}
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionInformation;
	}
	
	private List<AccountTranscationResponse> getAccountTranscationResponseList(List<AccountStatement> accountTransactionlist, TransactionInformation transactionInformation)
	{
		List<AccountTranscationResponse> accountTxnResponseDataList = new ArrayList<AccountTranscationResponse>();
		try 
		{
			List<AccountTranscationResponse> accountTranscationResponse = getAccountTransactionResponseList(accountTransactionlist);
			
			HashMap<String,AccountTxnResponse> accountTxnResponseMap = getAccountTxnResponseMap(accountTranscationResponse, transactionInformation);			
			for (Map.Entry<String, AccountTxnResponse> entry : accountTxnResponseMap.entrySet()) 
			{  
				AccountTxnResponse accountTxnResponse = entry.getValue();				
				
				HashMap<String, Double> txnFeeVatMap = new HashMap<String, Double>();
				String txnKeyName = "txn_amount";
				String feeKeyName = "fee_txn_amount";
				String vatKeyName = "vat_txn_amount";
				
				List<AccountTranscationResponse> transactionDetails = accountTxnResponse.getTransactionDetails();
				
				Collections.sort(transactionDetails, new Comparator<AccountTranscationResponse>() {
					@Override
					public int compare(AccountTranscationResponse o1, AccountTranscationResponse o2) {
						return (int) (o2.getStrTransactionAmount() - o1.getStrTransactionAmount());
					}
				});
				
				String txnMode = null;
				for (AccountTranscationResponse accResponse : transactionDetails) 
				{
					String strTxnAmount = "0";
					String keyName = "";
					if("YES".equalsIgnoreCase(accResponse.getStrIsTxn().trim())) 
					{
						strTxnAmount = Utils.decimalFormat.format(accResponse.getStrTransactionAmount());
						keyName = txnKeyName;
						txnMode = accResponse.getStrTransactionMode(); //New Added on 22-Sep-2023
					}
					else if ("YES".equalsIgnoreCase(accResponse.getStrIsFeeTxn().trim())) 
					{
						strTxnAmount = Utils.decimalFormat.format(accResponse.getStrTransactionAmount());
						keyName = feeKeyName;
					}
					else if ("YES".equalsIgnoreCase(accResponse.getStrIsVatTxn().trim())) 
					{
						strTxnAmount = Utils.decimalFormat.format(accResponse.getStrTransactionAmount());
						keyName = vatKeyName;
					}
					
					Double txnAmount = Utils.stringToDouble(strTxnAmount);
					txnFeeVatMap.put(keyName, txnAmount);
				}
				amsLogger.writeInfoLog("Transaction Mode=["+txnMode+"]");
				String netoff = getNetOffValue(txnMode, txnFeeVatMap);				
				accountTxnResponse.getTransactionSumary().setNetOff(netoff);
				
				/*
				try 
				{
					Double netOff = 0.00d;
					if (txnFeeVatMap.containsKey(txnKeyName) && txnFeeVatMap.get(txnKeyName) != null) 
					{
						netOff = txnFeeVatMap.get(txnKeyName);							
						if (txnFeeVatMap.containsKey(feeKeyName) && txnFeeVatMap.get(feeKeyName)!=null) {
							netOff = netOff - txnFeeVatMap.get(feeKeyName);
						}
						if (txnFeeVatMap.containsKey(vatKeyName) && txnFeeVatMap.get(vatKeyName)!=null) {
							netOff = netOff - txnFeeVatMap.get(vatKeyName);
						}
					}
					accountTxnResponse.getTransactionSumary().setNetOff(Utils.decimalFormat.format(netOff));
				}
				catch (Exception e) 
				{
					amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
				}
				*/
				
				AccountTranscationResponse accountTranResp = new AccountTranscationResponse();
				accountTranResp.setAccountTxnResponse(accountTxnResponse);
				
				accountTxnResponseDataList.add(accountTranResp);
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountTxnResponseDataList;
	}
	
	private String getNetOffValue(String txnMode, HashMap<String, Double> txnFeeVatMap) 
	{
		Double netOff = 0.00d;
		Double feeValue = 0.00d;
		Double vatValue = 0.00d;
		
		String txnKeyName = "txn_amount";
		String feeKeyName = "fee_txn_amount";
		String vatKeyName = "vat_txn_amount";
		
		try 
		{
			if (txnFeeVatMap.containsKey(txnKeyName) && txnFeeVatMap.get(txnKeyName) != null) 
			{
				netOff = txnFeeVatMap.get(txnKeyName);	
			}
			if (txnFeeVatMap.containsKey(feeKeyName) && txnFeeVatMap.get(feeKeyName)!=null) 
			{
				feeValue = txnFeeVatMap.get(feeKeyName);
			}
			if (txnFeeVatMap.containsKey(vatKeyName) && txnFeeVatMap.get(vatKeyName)!=null) 
			{
				vatValue = txnFeeVatMap.get(vatKeyName);
			}
			
			if ("CREDIT".equalsIgnoreCase(txnMode)) 
			{
				netOff = netOff - feeValue - vatValue;
			}
			else 
			{
				netOff = netOff + feeValue + vatValue;
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured in getNetOffValue::"+ExceptionUtils.getStackTrace(e));
		}
		amsLogger.writeInfoLog("netOff value=["+netOff+"]");
		return Utils.decimalFormat.format(netOff);
	}
	
	private HashMap<String, AccountTxnResponse> getAccountTxnResponseMap(List<AccountTranscationResponse> accountTranscationResponse, TransactionInformation transactionInformation) 
	{
		HashMap<String,AccountTxnResponse> accountTxnResponseMap = new HashMap<String,AccountTxnResponse>();		
		try 
		{
			HashMap<String, TransactionSummary> transactionSummaryMap = new HashMap<String, TransactionSummary>();
			HashMap<String, List<AccountTranscationResponse>> transactionDetailMap = new HashMap<String, List<AccountTranscationResponse>>();
			
			for(AccountTranscationResponse accountTxnRsp : accountTranscationResponse)
			{
				String txnId = accountTxnRsp.getStrTransactionID().trim();
				String txnSummaryKey = txnId + "_summary";
				String txnDetailKey = txnId + "_detail";				
				
				if(transactionSummaryMap.containsKey(txnSummaryKey))
				{
					TransactionSummary existingTransactionSummary = transactionSummaryMap.get(txnSummaryKey);
					addEntityInfoAndNumberInSummary(existingTransactionSummary, accountTxnRsp);
							
					List<AccountTranscationResponse> transactionDetailList = transactionDetailMap.get(txnDetailKey);
					transactionDetailList.add(accountTxnRsp);
				}
				else
				{
					TransactionSummary transactionSummary = new TransactionSummary();
					transactionSummary.setStrTransactionID(accountTxnRsp.getStrTransactionID());
					transactionSummary.setMontraTxnId(accountTxnRsp.getMontraTxnId());
					transactionSummary.setStrTransactionType(accountTxnRsp.getStrTransactionType());
					transactionSummary.setStrTransactionDate(accountTxnRsp.getStrTransactionDate());
					
					addEntityInfoAndNumberInSummary(transactionSummary, accountTxnRsp);
					
					List<AccountTranscationResponse> transactionDetailList = new ArrayList<AccountTranscationResponse>();
					transactionDetailList.add(accountTxnRsp);
					
					transactionSummaryMap.put(txnSummaryKey, transactionSummary);
					transactionDetailMap.put(txnDetailKey, transactionDetailList);
				}
				
				if(accountTxnResponseMap.containsKey(accountTxnRsp.getStrTransactionID()))
				{
					AccountTxnResponse txnResponse = accountTxnResponseMap.get(txnId);
					txnResponse.setTransactionSumary(transactionSummaryMap.get(txnSummaryKey));
					txnResponse.setTransactionDetails(transactionDetailMap.get(txnDetailKey));
				}
				else
				{
					AccountTxnResponse txnResponse = new AccountTxnResponse();
					txnResponse.setTransactionSumary(transactionSummaryMap.get(txnSummaryKey));
					txnResponse.setTransactionDetails(transactionDetailMap.get(txnDetailKey));
					accountTxnResponseMap.put(txnId, txnResponse);
				}
			}			
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountTxnResponseMap;
	}
	
	private void addEntityInfoAndNumberInSummary(TransactionSummary transactionSummary, AccountTranscationResponse accountTxnRsp) 
	{
		try 
		{
			if (accountTxnRsp.getEntityInfo()!=null && accountTxnRsp.getEntityInfo().trim().length() > 0) 
			{
				transactionSummary.setEntityInfo(accountTxnRsp.getEntityInfo().trim());
				accountTxnRsp.setEntityInfo(null);
			}
			if (accountTxnRsp.getEntityNumber()!=null && accountTxnRsp.getEntityNumber().trim().length() > 0) 
			{
				transactionSummary.setEntityNumber(accountTxnRsp.getEntityNumber().trim());
				accountTxnRsp.setEntityNumber(null);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}

	//Added By Sunil Y , For New SummrayStatement and TxnDetailsStatement Started [2023-10-05]
	@Override
	public TransactionSummaryDetails getAllTransactionSummaryDetails(AccountStatement accountStatement) 
	{
		TransactionSummaryDetails transactionSummaryDetailsObj = new TransactionSummaryDetails();
		try 
		{
			List<TxnSummary> txnSummary = new ArrayList<>();
			
			AccountCreation accountCreation = new AccountCreation();
			accountCreation.setStrAccountNumber(accountStatement.getStrAccountNumber());
			AccountCreation accountMasterObj = accountMasterDao.getAccountInformation(accountCreation);
			if(accountMasterObj != null) 
			{
				List<AccountStatement> accountStatementsList  = accountStatementDao.getDistinctListOfAccountStatement(accountStatement);
				if(accountStatementsList.size()>0) 
				{
					List<AccountStatement> distinctAccountStatements = accountStatementsList.stream().distinct().filter(i -> i.getStrIsGLType().equalsIgnoreCase("T")).collect(Collectors.toList());
					distinctAccountStatements.forEach(accountStatementObj -> 
					{
						TxnSummary txnSummaryObj = new TxnSummary();
						
						//Calculate NetOff
						Double netOff = 0.00d;
						List<AccountStatement> feeVatDetalisObj = accountStatementsList.stream().filter(i -> i.getStrTransactionID().equalsIgnoreCase(accountStatementObj.getStrTransactionID())).collect(Collectors.toList());
					    if(feeVatDetalisObj.size()>0) 
					    {
					    	Optional<AccountStatement> txnObj = feeVatDetalisObj.stream().filter(i -> i.getStrIsGLType().equalsIgnoreCase("T")).findAny();
					    	Optional<AccountStatement> feeObj = feeVatDetalisObj.stream().filter(i -> i.getStrIsGLType().equalsIgnoreCase("F")).findAny();
					    	Optional<AccountStatement> vatObj = feeVatDetalisObj.stream().filter(i -> i.getStrIsGLType().equalsIgnoreCase("V")).findAny();
							  
					    	netOff = 0.00d;
							Double feeValue = 0.00d;
							Double vatValue = 0.00d;
							
					    	if(txnObj.isPresent()) 
					    	{
					    		 netOff = Double.valueOf(txnObj.get().getTransactionAmount());
					    	}
					    	if(feeObj.isPresent()) 
					    	{
					    		 feeValue = Double.valueOf(feeObj.get().getTransactionAmount());
					    	}
					    	if(vatObj.isPresent()) 
					    	{
					    		vatValue = Double.valueOf(vatObj.get().getTransactionAmount());
					    	}
					    	
					    	if ("CREDIT".equalsIgnoreCase(accountStatementObj.getStrTranMode())) 
							{
								netOff = netOff - feeValue - vatValue;
							}
							else 
							{
								netOff = netOff + feeValue + vatValue;
							}
					    }
					    
						String transactionStatus = null;
						if(accountStatementObj.getStrReservefield2() != null) 
						{
							if(accountStatementObj.getStrReservefield2().equalsIgnoreCase("pending")) 
							{
								transactionStatus = "STAGING";
							}
							else 
							{
								transactionStatus = "SUCCESS";
							}
						}
						else if(accountStatementObj.getStrResponseCode() != null) 
						{
							if(accountStatementObj.getStrResponseCode().equalsIgnoreCase("00")) 
							{
								transactionStatus = "SUCCESS";
							}
							else 
							{
								transactionStatus = "FAILURE";
							}	
						}
						
						txnSummaryObj.setPaymenttype(accountStatementObj.getStrTranMode());
						txnSummaryObj.setTransactionstatus(transactionStatus);
						txnSummaryObj.setTxnid(accountStatementObj.getStrTransactionID());
						txnSummaryObj.setAmount(String.valueOf(netOff));
						txnSummaryObj.setTransactiontype(accountStatementObj.getStrTransactionType());
						txnSummaryObj.setTransactiondate(Utils.simpleDateTimeFormat.format(accountStatementObj.getTransactionDate()));
						txnSummaryObj.setEntityinfo(accountStatementObj.getEntityInfo());
						txnSummaryObj.setEntitynumber(accountStatementObj.getEntityNumber());
						txnSummaryObj.setCid(accountStatement.getCid());
						txnSummaryObj.setFailurereason(null);
						txnSummary.add(txnSummaryObj);	
					});
					
					List<TxnSummary> limitedTxnSummary = txnSummary.stream().limit(Long.parseLong(accountStatement.getPageSize())).collect(Collectors.toList());
					Collections.reverse(limitedTxnSummary);
					
					long totalCount = txnSummary.stream().count();
					transactionSummaryDetailsObj.setCode("S0000"); 
					transactionSummaryDetailsObj.setSuccess(true);
					transactionSummaryDetailsObj.setMessage("Please check account statement");
					transactionSummaryDetailsObj.setBalance(accountMasterObj.getStrClosingBalance());
					transactionSummaryDetailsObj.setTransactioncount(String.valueOf(totalCount));
					transactionSummaryDetailsObj.setTransactions(limitedTxnSummary);
				}
				else 
				{
					transactionSummaryDetailsObj.setCode("E0000"); 
					transactionSummaryDetailsObj.setSuccess(false);
					transactionSummaryDetailsObj.setMessage("No Data Found!");
				}
			}
			else 
			{
				transactionSummaryDetailsObj.setCode("E0000"); 
				transactionSummaryDetailsObj.setSuccess(false);
				transactionSummaryDetailsObj.setMessage("No Account Found!");
			}
		}
		catch (Exception e) 
		{
			transactionSummaryDetailsObj.setCode("E0000"); 
			transactionSummaryDetailsObj.setSuccess(false);
			transactionSummaryDetailsObj.setMessage("Internal Server Error!");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionSummaryDetailsObj;
	}

	@Override
	public TxnStatementDetailsMaster viewAccountStatementDetails(AccountStatement accountStatement) 
	{
		TxnStatementDetailsMaster txnStatementDetailsMaster = new TxnStatementDetailsMaster();
		try 
		{
			List<TxnDetails> txnDetails = new ArrayList<>();
			
			// checker for account is there or not
			List<AccountStatement> accountTranMastersList = accountStatementDao.viewAccountStatementTxnDetails(accountStatement);
			if(accountTranMastersList != null && accountTranMastersList.size( )> 0) 
			{
				accountTranMastersList.forEach(i -> 
				{
					TxnDetails txnDetailsObj = new TxnDetails();
					txnDetailsObj.setStrtransactionamount(String.valueOf(i.getTransactionAmount()));
					txnDetailsObj.setStrtransactionmode(i.getStrTranMode());
					if(i.getStrIsGLType() != null)
					{	
						txnDetailsObj.setStristxn("NO");
						txnDetailsObj.setStrisfeetxn("NO");
						txnDetailsObj.setStrisvattxn("NO");
						
						if("T".equalsIgnoreCase(i.getStrIsGLType()))
						{
							txnDetailsObj.setStristxn("YES");				
						}
						else if("F".equalsIgnoreCase(i.getStrIsGLType()))				
						{
							txnDetailsObj.setStrisfeetxn("YES");
						}
						else if("V".equalsIgnoreCase(i.getStrIsGLType())) 
						{
							txnDetailsObj.setStrisvattxn("YES");
						}			
					}
					txnDetails.add(txnDetailsObj);
				});
				//Construct Object
				TransactionStatementDetails transactionStatementDetails = new TransactionStatementDetails();
				transactionStatementDetails.setTransactionid(accountTranMastersList.get(0).getStrTransactionID());
				transactionStatementDetails.setMontratxnid(accountTranMastersList.get(0).getStrSrcTxnId());
				transactionStatementDetails.setNarration(accountTranMastersList.get(0).getStrNaration());
				transactionStatementDetails.setTransactiondetails(txnDetails);
				
				List<TransactionStatementDetails> transactionStatementDetailsList = new ArrayList<>();
				transactionStatementDetailsList.add(transactionStatementDetails);
				TxnStatementDetails txnStatementDetails = new TxnStatementDetails();
				txnStatementDetails.setTransactions(transactionStatementDetailsList);
				
				txnStatementDetailsMaster.setCode("S0000");
				txnStatementDetailsMaster.setSuccess(true);
				txnStatementDetailsMaster.setMessage("Please check account statement");
				txnStatementDetailsMaster.setData(txnStatementDetails);
			}
			else
			{
				txnStatementDetailsMaster.setCode("E0000");
				txnStatementDetailsMaster.setSuccess(false);
				txnStatementDetailsMaster.setMessage("No Data Found!");
			}
		}
		catch (Exception e) 
		{
			txnStatementDetailsMaster.setCode("E0000");
			txnStatementDetailsMaster.setSuccess(false);
			txnStatementDetailsMaster.setMessage("Internal Server Error!");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return txnStatementDetailsMaster;
	}
	//Added By Sunil Y , End [2023-10-05]

	@Override
	public void addBatchEntryAccountTransactionData(List<AccountStatement> accountStatementlist) {
		for(AccountStatement accountStatement:accountStatementlist)
		{
			accountStatement.setTransactionDate(new Date());
			accountStatementDao.save(accountStatement);
		}
	}

	@Override
	public List<AccountStatementResponse> getlastFiveWalletAccountStatements(AccountStatement accountStatement) {
		List<AccountStatement> accountStatementlist=accountStatementDao.getLastFiveAccountStatementBasedonParameter(accountStatement);
		
		List<AccountStatementResponse> accountStatementResponses=new ArrayList<>();
		
		for(int i=0;i<accountStatementlist.size();i++)
		{
			AccountStatement accountStatmentData =accountStatementlist.get(i);
			
			AccountStatementResponse mapAccountStatementToAccountStatementResponse= mapAccountStatementToAccountStatementResponse(accountStatmentData);
			accountStatementResponses.add(mapAccountStatementToAccountStatementResponse);
		}
	    return accountStatementResponses;
	}

	@Override
	public List<AccountStatementResponse> getWalletAccountStatementsDateWise(AccountStatement accountStatement) {
		List<AccountStatement> accountStatementlist=accountStatementDao.getWalletAccountStatementBasedonParameter(accountStatement);
		
		List<AccountStatementResponse> accountStatementResponses=new ArrayList<>();
		
		for(int i=0;i<accountStatementlist.size();i++)
		{
			AccountStatement accountStatmentData =accountStatementlist.get(i);
			
			AccountStatementResponse mapAccountStatementToAccountStatementResponse= mapAccountStatementToAccountStatementResponse(accountStatmentData);
			accountStatementResponses.add(mapAccountStatementToAccountStatementResponse);
		}
	    return accountStatementResponses;
	}
}
