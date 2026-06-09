package ams.cms.services.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.config.CommonConstants;
import ams.cms.config.TransactionHandler;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.CurrencyConversionMaster;
import ams.cms.model.CurrencyMaster;
import ams.cms.model.CurrencyTransferMaster;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.MultiCurrencyChargesReport;
import ams.cms.model.MultiCurrencyWalletAccountMaster;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.CurrencyConversionMasterService;
import ams.cms.services.CurrencyConvertAndTransfer;
import ams.cms.services.CurrencyMasterService;
import ams.cms.services.CurrencyTransferMasterService;
import ams.cms.services.MultiCurrencyChargesReportService;
import ams.cms.services.MultiCurrencyWalletAccountService;
import ams.cms.util.ProcessWebResponse;
import ams.cms.utility.Utils;



@Component
public class CurrencyConvertAndTransferImpl implements CurrencyConvertAndTransfer{
	
	@Autowired
	TransactionHandler transactionHandler;
	
	@Autowired
	CurrencyConversionMasterService conversionMasterService;
	
	@Autowired
	CurrencyMasterService currencyMasterService;
	
	@Autowired
	MultiCurrencyWalletAccountService accountService;
	
	@Autowired
	CurrencyTransferMasterService currencyTransferMasterService;
	
	@Autowired
	AccountTranMasterService accountTranMasterService;
	

	@Autowired
	MultiCurrencyChargesReportService multiCurrencyChargesReportService; 
	
	@Override
	public ProcessWebResponse currencyConvertAndTransferToWallet(ProcessWebResponse processWebResponse) {
		try
		{
			HashMap<String, CurrencyConversionMaster> currencyConversionDetailMap = processWebResponse.getCurrencyConversionDetailMap();
			TreeMap<Integer, String> remaingCurrencyMap = new TreeMap<Integer, String>(Collections.reverseOrder());
			remaingCurrencyMap = processWebResponse.getRemaingCurrencyMap();
			HashMap<String, CurrencyMaster> currencyDetailMap = processWebResponse.getCurrencyMasterDetailMap();
			
			double remainingAmount = processWebResponse.getRemaingAmount();
			String remainingAmountCurrency = processWebResponse.getRemaingAmountCurrency();
			
			
			CurrencyMaster currencyToMaster = new CurrencyMaster();
			currencyToMaster.setCurrencyCode(processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
			currencyToMaster = currencyMasterService.getCurrencyDetailByCurrencyCode(currencyToMaster);
			double feeAmountReq = 0;
			if("Y".equals(currencyToMaster.getStrFlatFee()) )
			{
				feeAmountReq = currencyToMaster.getFeeAmount();
			}
			
			if("Y".equalsIgnoreCase(currencyToMaster.getStrIsFee()))
			{
				feeAmountReq = ((Utils.stringToDouble(currencyToMaster.getStrFeePercentage())/100)) * Utils.stringToDouble(processWebResponse.getTransactionRequest().getStrTxnAmount());
				currencyToMaster.setFeeAmount(feeAmountReq);
			}
			processWebResponse.setCurrencyMaster(currencyToMaster);
			currencyDetailMap.put(currencyToMaster.getCurrencyCode(), currencyToMaster);
			processWebResponse.setCurrencyMasterDetailMap(currencyDetailMap);
			
			MultiCurrencyWalletAccountMaster fromMultiCurrencyWalletDetails = new MultiCurrencyWalletAccountMaster();
			fromMultiCurrencyWalletDetails.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
			fromMultiCurrencyWalletDetails.setStrCurrencyCode(remainingAmountCurrency);
			
			fromMultiCurrencyWalletDetails = accountService.getCurrencyAccount(fromMultiCurrencyWalletDetails);
			
			CurrencyConversionMaster conversionMasterValue = new CurrencyConversionMaster();
			conversionMasterValue.setFromCurrency(remainingAmountCurrency);
			conversionMasterValue.setToCurrency(processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
			conversionMasterValue = conversionMasterService.getCurrencyMasterByCurrencyCode(conversionMasterValue);
			currencyConversionDetailMap.put(conversionMasterValue.getFromCurrency()+"_"+conversionMasterValue.getToCurrency(), conversionMasterValue);
			processWebResponse.setCurrencyConversionDetailMap(currencyConversionDetailMap);
			processWebResponse.setCurrencyConversionMaster(conversionMasterValue);
			
			double convertedAmount = remainingAmount * conversionMasterValue.getToCurrencyConversion();
			double gstAmount = (currencyToMaster.getGstPercentage()/100) * currencyToMaster.getFeeAmount() ;
			double updatedTxnAmount = convertedAmount + currencyToMaster.getFeeAmount() + gstAmount;
			processWebResponse.setTxnAmountwithCharges(updatedTxnAmount);
			
			double balanceRemg = processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrClosingBalance() - processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrEarAmount();
			processWebResponse.getToMultiCurrencyWalletAccountMaster().setStrClosingBalance(balanceRemg);
			
			if(!(processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrCurrencyCode()).equalsIgnoreCase(remainingAmountCurrency) )
			{
				processWebResponse.setMultiCurrencyWalletAccountMaster(processWebResponse.getToMultiCurrencyWalletAccountMaster());
				if( processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrClosingBalance() > updatedTxnAmount)
				{
					processWebResponse.getTransactionRequest().setStrTxnAmount(convertedAmount+"");
					processWebResponse.setFeeAmount(currencyToMaster.getFeeAmount());
					processWebResponse.setGstAmount(gstAmount);
					
					MultiCurrencyWalletAccountMaster earMarkUpdate = new MultiCurrencyWalletAccountMaster();
					earMarkUpdate.setStrCurrencyCode(processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
					earMarkUpdate.setStrAccountNumber(processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrAccountNumber());
					double earMarkAmount = processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrEarAmount() + updatedTxnAmount;
					earMarkUpdate.setStrEarAmount(earMarkAmount);
					accountService.updateEarMark(earMarkUpdate);
					
					MultiCurrencyWalletAccountMaster currencyAccountMaster = transactionHandler.updateClosingBalance(processWebResponse);
					processWebResponse.setCurrencyAccountMaster(currencyAccountMaster);
					MultiCurrencyWalletAccountMaster currencyAccountMasterFee = transactionHandler.updateClosingBalanceFee(processWebResponse);
					processWebResponse.setCurrencyAccountMasterFee(currencyAccountMasterFee);
					MultiCurrencyWalletAccountMaster currencyAccountMasterGst = transactionHandler.updateClosingBalanceGst(processWebResponse);
					processWebResponse.setCurrencyAccountMasterGst(currencyAccountMasterGst);
				    
					transactionHandler.addBatchEntryAccountStatment(processWebResponse);
					
					GLAccountTypeMaster glAccountTypeMaster = transactionHandler.updateGlClosingBalance(processWebResponse);
					processWebResponse.setCurrencyGl(glAccountTypeMaster);
					GLAccountTypeMaster feeGlMaster = transactionHandler.updateFeeGlClosingBalance(processWebResponse);
					processWebResponse.setFeeCurrencyGl(feeGlMaster);
					GLAccountTypeMaster gstGlMaster = transactionHandler.updateGstGlClosingBalance(processWebResponse);
					processWebResponse.setGstCurrencyGl(gstGlMaster);
					
					transactionHandler.addGLStatements(processWebResponse);
					
					CurrencyTransferMaster currencyTransferMaster = new CurrencyTransferMaster();
					currencyTransferMaster.setTranId(processWebResponse.getTxnId());
					currencyTransferMaster.setSweepOutAmount(convertedAmount+"");
					currencyTransferMaster.setSweepOutCurrencyValue(String.valueOf(conversionMasterValue.getToCurrencyConversion()));
					currencyTransferMaster.setSweepInCurrencyValue(conversionMasterValue.getFromCurrencyValue()+"");
					currencyTransferMaster.setSweepOutCurrencyCode(processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
					currencyTransferMaster.setSweepInAmount(remainingAmount+"");
					currencyTransferMaster.setSweepInCurrencyCode(remainingAmountCurrency);
					currencyTransferMaster.setBaseAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
					processWebResponse.setLastSweepInCurrencyCode(remainingAmountCurrency);
					currencyTransferMaster.setBaseAccountType(processWebResponse.getAccountCreation().getStrAccountType());
					currencyTransferMaster.setFeeAmount( processWebResponse.getFeeAmount());
					currencyTransferMaster.setGstAmount(processWebResponse.getGstAmount());
					currencyTransferMasterService.addEntryInCurrencyTransferMaster(currencyTransferMaster);
					

					MultiCurrencyChargesReport multiCurrencyGstReport = new MultiCurrencyChargesReport();
					multiCurrencyGstReport.setAccountCurrency(processWebResponse.getLastSweepInCurrencyCode());
					multiCurrencyGstReport.setAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
					multiCurrencyGstReport.setAccountType(processWebResponse.getAccountCreation().getStrAccountType());
					multiCurrencyGstReport.setChargesType(CommonConstants.GST_TYPE);
					multiCurrencyGstReport.setGlAccountNumber(processWebResponse.getGstCurrencyGl().getStrAccountNumber());
					multiCurrencyGstReport.setGlAccountType(processWebResponse.getGstCurrencyGl().getStrAccountType());
					multiCurrencyGstReport.setParticipantId(processWebResponse.getStrParticipantId());
					multiCurrencyGstReport.setTxnAmt(processWebResponse.getGstAmount());
					multiCurrencyGstReport.setTxnDate(Utils.getCurrentDate());
					multiCurrencyGstReport.setTxnId(processWebResponse.getTxnId());
					multiCurrencyGstReport.setTxnType(processWebResponse.getTxnType());
					multiCurrencyGstReport.setBaseConversionRate(conversionMasterValue.getToCurrencyConversion());
					multiCurrencyGstReport.setBaseCurrency("INR");
					multiCurrencyGstReport.setBaseCurrencyAmt(processWebResponse.getGstAmount());
					multiCurrencyGstReport.setTxnType("DEBIT");
					multiCurrencyChargesReportService.saveMultiCurrencyChargReport(multiCurrencyGstReport);
					
					MultiCurrencyChargesReport multiCurrencyFeeReport = new MultiCurrencyChargesReport();
					multiCurrencyFeeReport.setAccountCurrency(processWebResponse.getLastSweepInCurrencyCode());
					multiCurrencyFeeReport.setAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
					multiCurrencyFeeReport.setAccountType(processWebResponse.getAccountCreation().getStrAccountType());
					multiCurrencyFeeReport.setChargesType(CommonConstants.FEE_TYPE);
					multiCurrencyGstReport.setGlAccountNumber(processWebResponse.getFeeCurrencyGl().getStrAccountNumber());
					multiCurrencyGstReport.setGlAccountType(processWebResponse.getFeeCurrencyGl().getStrAccountType());
					multiCurrencyFeeReport.setParticipantId(processWebResponse.getStrParticipantId());
					multiCurrencyFeeReport.setTxnAmt(processWebResponse.getFeeAmount());
					multiCurrencyFeeReport.setTxnDate(Utils.getCurrentDate());
					multiCurrencyFeeReport.setTxnId(processWebResponse.getTxnId());
					multiCurrencyFeeReport.setTxnType(processWebResponse.getTxnType());
					multiCurrencyFeeReport.setBaseConversionRate(conversionMasterValue.getToCurrencyConversion());
					multiCurrencyFeeReport.setBaseCurrency("INR");
					multiCurrencyFeeReport.setBaseCurrencyAmt(processWebResponse.getFeeAmount());
					multiCurrencyFeeReport.setTxnType("DEBIT");
					multiCurrencyChargesReportService.saveMultiCurrencyChargReport(multiCurrencyFeeReport);
					
					AccountTranMaster accountTranMasterIn = new AccountTranMaster();
					accountTranMasterIn.setStrParticipantId(processWebResponse.getStrParticipantId());
					accountTranMasterIn.setStrFrom_account_number(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyWalletAccountNumber());
					accountTranMasterIn.setStrTxn_id(processWebResponse.getTxnId());
					accountTranMasterIn.setSwitchTxDate(Utils.getCurrentDate());
					accountTranMasterIn.setStrLocal_tran_date(Utils.getCurrentDate());
					accountTranMasterIn.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
					accountTranMasterIn.setStrResponseCode("00");
					accountTranMasterIn.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
					accountTranMasterIn.setStrTransaction_amount(convertedAmount+"");
					accountTranMasterService.addAccountTransactionData(accountTranMasterIn);
					
					AccountTranMaster accountTranMasterFeeIn = new AccountTranMaster();
					accountTranMasterFeeIn.setStrParticipantId(processWebResponse.getStrParticipantId());
					accountTranMasterFeeIn.setStrFrom_account_number(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyWalletAccountNumber());
					accountTranMasterFeeIn.setStrTxn_id(processWebResponse.getTxnId());
					accountTranMasterFeeIn.setSwitchTxDate(Utils.getCurrentDate());
					accountTranMasterFeeIn.setStrLocal_tran_date(Utils.getCurrentDate());
					accountTranMasterFeeIn.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
					accountTranMasterFeeIn.setStrResponseCode("00");
					accountTranMasterFeeIn.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
					accountTranMasterFeeIn.setStrTransaction_amount(processWebResponse.getFeeAmount()+"");
					accountTranMasterService.addAccountTransactionData(accountTranMasterFeeIn);
					
					AccountTranMaster accountTranMasterGstIn = new AccountTranMaster();
					accountTranMasterGstIn.setStrParticipantId(processWebResponse.getStrParticipantId());
					accountTranMasterGstIn.setStrFrom_account_number(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyWalletAccountNumber());
					accountTranMasterGstIn.setStrTxn_id(processWebResponse.getTxnId());
					accountTranMasterGstIn.setSwitchTxDate(Utils.getCurrentDate());
					accountTranMasterGstIn.setStrLocal_tran_date(Utils.getCurrentDate());
					accountTranMasterGstIn.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
					accountTranMasterGstIn.setStrResponseCode("00");
					accountTranMasterGstIn.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
					accountTranMasterGstIn.setStrTransaction_amount(processWebResponse.getGstAmount()+"");
					accountTranMasterService.addAccountTransactionData(accountTranMasterGstIn);
					
					MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMasterIn = new MultiCurrencyWalletAccountMaster();
					double updatedBalanceIn = fromMultiCurrencyWalletDetails.getStrClosingBalance() + remainingAmount;
					multiCurrencyWalletAccountMasterIn.setStrClosingBalance(updatedBalanceIn);
					multiCurrencyWalletAccountMasterIn.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
					multiCurrencyWalletAccountMasterIn.setStrCurrencyCode(remainingAmountCurrency);
					transactionHandler.addClosingBalanceInWallet(multiCurrencyWalletAccountMasterIn); 
		//			processWebResponse.setMultiCurrencyWalletAccountMaster(multiCurrencyWalletAccountMasterIn);
					
					MultiCurrencyWalletAccountMaster walletAccountInn = new MultiCurrencyWalletAccountMaster();
					walletAccountInn.setStrCurrencyCode(remainingAmountCurrency);
					walletAccountInn.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
					walletAccountInn = accountService.getCurrencyAccount(walletAccountInn);
					
					processWebResponse.setMultiCurrencyWalletAccountMaster(walletAccountInn);
					
					processWebResponse.setRemaingAmount(remainingAmount);
					transactionHandler.addAccountTransactionData(processWebResponse);
					
					AccountTranMaster accountTranMasterAdd = new AccountTranMaster();
					accountTranMasterAdd.setStrParticipantId(processWebResponse.getStrParticipantId());
					accountTranMasterAdd.setStrFrom_account_number(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyWalletAccountNumber());
					accountTranMasterAdd.setStrTxn_id(processWebResponse.getTxnId());
					accountTranMasterAdd.setSwitchTxDate(Utils.getCurrentDate());
					accountTranMasterAdd.setStrLocal_tran_date(Utils.getCurrentDate());
					accountTranMasterAdd.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
					accountTranMasterAdd.setStrResponseCode("00");
					accountTranMasterAdd.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
					accountTranMasterAdd.setTxnType("Credit");
					accountTranMasterAdd.setStrTransaction_amount(processWebResponse.getRemaingAmount()+"");
					accountTranMasterService.addAccountTransactionData(accountTranMasterAdd);
					
			/*		MultiCurrencyWalletAccountMaster walletAccountInn = new MultiCurrencyWalletAccountMaster();
					walletAccountInn.setStrCurrencyCode(remainingAmountCurrency);
					walletAccountInn.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
					walletAccountInn = accountService.getCurrencyAccount(walletAccountInn);
					
					processWebResponse.setMultiCurrencyWalletAccountMaster(walletAccountInn); */
					
					double txnBalanceIn = walletAccountInn.getStrClosingBalance() - processWebResponse.getFeeAmount() - processWebResponse.getGstAmount();
					processWebResponse.getTransactionRequest().setStrTxnAmount(txnBalanceIn+"");
					MultiCurrencyWalletAccountMaster remCurrencyAccountMasterIn = transactionHandler.updateClosingBalance(processWebResponse);
					processWebResponse.setCurrencyAccountMaster(remCurrencyAccountMasterIn);
					MultiCurrencyWalletAccountMaster remCurrencyAccountMasterFeeIn = transactionHandler.updateClosingBalanceFee(processWebResponse);
					processWebResponse.setCurrencyAccountMasterFee(remCurrencyAccountMasterFeeIn);
					MultiCurrencyWalletAccountMaster remCurrencyAccountMasterGstIn = transactionHandler.updateClosingBalanceGst(processWebResponse);
					processWebResponse.setCurrencyAccountMasterGst(remCurrencyAccountMasterGstIn);
				    
					transactionHandler.addBatchEntryAccountStatment(processWebResponse);
					
					CurrencyMaster currencyMaster = new CurrencyMaster();
					currencyMaster.setCurrencyCode(remainingAmountCurrency);
					currencyMaster = currencyMasterService.getCurrencyDetailByCurrencyCode(currencyMaster);
					double feeAmount = 0;
					if("Y".equals(currencyToMaster.getStrFlatFee()) )
					{
						feeAmount = currencyToMaster.getFeeAmount();
					}
					
					if("Y".equalsIgnoreCase(currencyToMaster.getStrIsFee()))
					{
						feeAmount = ((Utils.stringToDouble(currencyToMaster.getStrFeePercentage())/100)) * Utils.stringToDouble(processWebResponse.getTransactionRequest().getStrTxnAmount());
						currencyToMaster.setFeeAmount(feeAmount);
					}
					processWebResponse.setCurrencyMaster(currencyMaster);
					
					GLAccountTypeMaster glAccountTypeMasterUpdate = transactionHandler.updateGlClosingBalance(processWebResponse);
					processWebResponse.setCurrencyGl(glAccountTypeMasterUpdate);
					GLAccountTypeMaster feeGlMasterUpdate = transactionHandler.updateFeeGlClosingBalance(processWebResponse);
					processWebResponse.setFeeCurrencyGl(feeGlMasterUpdate);
					GLAccountTypeMaster gstGlMasterUpdate = transactionHandler.updateGstGlClosingBalance(processWebResponse);
					processWebResponse.setGstCurrencyGl(gstGlMasterUpdate);
					
					transactionHandler.addGLStatements(processWebResponse);
					
					MultiCurrencyWalletAccountMaster earMarkRelease = new MultiCurrencyWalletAccountMaster();
					earMarkRelease.setStrCurrencyCode(processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrCurrencyCode());
					earMarkRelease.setStrAccountNumber(processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrAccountNumber());
					double earMark = processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrEarAmount() - updatedTxnAmount;
					earMarkUpdate.setStrEarAmount(earMark);
					accountService.updateEarMark(earMarkRelease);
					
					int i = processWebResponse.getRemaingCurrencyMap().size();
					System.out.println("i::::::::"+i);
					String remaingCurrencyCode ="";
					double remaingCurrency = 0;
					
//					Map<Integer, String> currencyRemainingMap = processWebResponse.getRemaingCurrencyMap();
//					currencyRemainingMap.
					System.out.println(""+processWebResponse.getRemaingCurrencyMap().entrySet());
					if(i != 0)
					{
						for(Map.Entry<Integer, String> remaingCurencyMap : processWebResponse.getRemaingCurrencyMap().entrySet())
						{
							if(remaingCurencyMap.getKey() != i )
							{	
								String remainingAmountWithCurrency = remaingCurencyMap.getValue();
								String remaingCurrencyArray[] = remainingAmountWithCurrency.split("_");
								 remaingCurrencyCode = remaingCurrencyArray[0];
								 remaingCurrency = Utils.stringToDouble(remaingCurrencyArray[1]) ;
							
								currencyDetailMap = processWebResponse.getCurrencyMasterDetailMap();
								CurrencyMaster currencyMasterDetail = currencyDetailMap.get(remaingCurrencyCode);
								double feeAmountIn = 0;
								if("Y".equals(currencyMasterDetail.getStrFlatFee()) )
								{
									feeAmountIn = currencyMasterDetail.getFeeAmount();
								}
								
								if("Y".equalsIgnoreCase(currencyMasterDetail.getStrIsFee()))
								{
									feeAmountIn = ((Utils.stringToDouble(currencyMasterDetail.getStrFeePercentage())/100)) * Utils.stringToDouble(processWebResponse.getTransactionRequest().getStrTxnAmount());
									currencyMasterDetail.setFeeAmount(feeAmountIn);
								}
								
								processWebResponse.setCurrencyMaster(currencyMasterDetail);
								
								processWebResponse.getTransactionRequest().setStrTxnAmount(remaingCurrency+"");
								processWebResponse.setFeeAmount(currencyMasterDetail.getFeeAmount());
								double gstAmountRemaingCurrency = (currencyMasterDetail.getGstPercentage()/100) * currencyMasterDetail.getFeeAmount() ;
								processWebResponse.setGstAmount(gstAmountRemaingCurrency);
								double updateAmount = remaingCurrency + gstAmountRemaingCurrency + currencyMasterDetail.getFeeAmount() ;
								
								MultiCurrencyWalletAccountMaster walletAccountMaster = new MultiCurrencyWalletAccountMaster();
								walletAccountMaster.setStrCurrencyCode(remaingCurrencyCode);
								walletAccountMaster.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
								walletAccountMaster = accountService.getCurrencyAccount(walletAccountMaster);
								
								processWebResponse.setMultiCurrencyWalletAccountMaster(walletAccountMaster);
								
								MultiCurrencyWalletAccountMaster earMarkAdd = new MultiCurrencyWalletAccountMaster();
								earMarkAdd.setStrCurrencyCode(walletAccountMaster.getStrCurrencyCode());
								earMarkAdd.setStrAccountNumber(walletAccountMaster.getStrAccountNumber());
								double earMarkAmnt = processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrEarAmount() + updateAmount;
								earMarkUpdate.setStrEarAmount(earMarkAmnt);
								accountService.updateEarMark(earMarkUpdate);
								
								MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster = new MultiCurrencyWalletAccountMaster();
								double updatedBalance = processWebResponse.getMultiCurrencyWalletAccountMaster().getStrClosingBalance() + remaingCurrency;
								multiCurrencyWalletAccountMaster.setStrClosingBalance(updatedBalance);
								multiCurrencyWalletAccountMaster.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
								multiCurrencyWalletAccountMaster.setStrCurrencyCode(remaingCurrencyCode);
								transactionHandler.addClosingBalanceInWallet(multiCurrencyWalletAccountMaster); 
								
								processWebResponse.setRemaingAmount(remaingCurrency);
								transactionHandler.addAccountTransactionData(processWebResponse);
								
								AccountTranMaster accountTranMaster = new AccountTranMaster();
								accountTranMaster.setStrParticipantId(processWebResponse.getStrParticipantId());
								accountTranMaster.setStrFrom_account_number(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyWalletAccountNumber());
								accountTranMaster.setStrTxn_id(processWebResponse.getTxnId());
								accountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
								accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());
								accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
								accountTranMaster.setStrResponseCode("00");
								accountTranMaster.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
								accountTranMaster.setTxnType("Credit");
								accountTranMaster.setStrTransaction_amount(processWebResponse.getRemaingAmount()+"");
								accountTranMasterService.addAccountTransactionData(accountTranMaster);
								
								MultiCurrencyWalletAccountMaster walletAccount = new MultiCurrencyWalletAccountMaster();
								walletAccount.setStrCurrencyCode(remaingCurrencyCode);
								walletAccount.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
								walletAccount = accountService.getCurrencyAccount(walletAccountMaster);
								
								processWebResponse.setMultiCurrencyWalletAccountMaster(walletAccount);
								
								double txnBalance = walletAccount.getStrClosingBalance() - processWebResponse.getFeeAmount() - processWebResponse.getGstAmount();
								processWebResponse.getTransactionRequest().setStrTxnAmount(txnBalance+"");
								MultiCurrencyWalletAccountMaster remCurrencyAccountMaster = transactionHandler.updateClosingBalance(processWebResponse);
								processWebResponse.setCurrencyAccountMaster(remCurrencyAccountMaster);
								MultiCurrencyWalletAccountMaster remCurrencyAccountMasterFee = transactionHandler.updateClosingBalanceFee(processWebResponse);
								processWebResponse.setCurrencyAccountMasterFee(remCurrencyAccountMasterFee);
								MultiCurrencyWalletAccountMaster remCurrencyAccountMasterGst = transactionHandler.updateClosingBalanceGst(processWebResponse);
								processWebResponse.setCurrencyAccountMasterGst(remCurrencyAccountMasterGst);
							    
								transactionHandler.addBatchEntryAccountStatment(processWebResponse);
								
								GLAccountTypeMaster glAccountTypeMasterUpdateIn = transactionHandler.updateGlClosingBalance(processWebResponse);
								processWebResponse.setCurrencyGl(glAccountTypeMasterUpdateIn);
								GLAccountTypeMaster feeGlMasterUpdateIn = transactionHandler.updateFeeGlClosingBalance(processWebResponse);
								processWebResponse.setFeeCurrencyGl(feeGlMasterUpdateIn);
								GLAccountTypeMaster gstGlMasterUpdateIn = transactionHandler.updateGstGlClosingBalance(processWebResponse);
								processWebResponse.setGstCurrencyGl(gstGlMasterUpdateIn);
								
								transactionHandler.addGLStatements(processWebResponse);
								
								 
								HashMap<String, CurrencyConversionMaster> currencyConversionMap = processWebResponse.getCurrencyConversionDetailMap();
								
								conversionMasterValue = currencyConversionMap.get(remaingCurrencyCode+"_"+processWebResponse.getLastSweepInCurrencyCode());
								
								MultiCurrencyWalletAccountMaster walletAccountMasterDetails = new MultiCurrencyWalletAccountMaster();
								walletAccountMasterDetails.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
								walletAccountMasterDetails.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
								walletAccountMasterDetails = accountService.getCurrencyAccount(walletAccountMaster);
								
								MultiCurrencyWalletAccountMaster earMarkReleas = new MultiCurrencyWalletAccountMaster();
								earMarkReleas.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
								earMarkReleas.setStrAccountNumber(multiCurrencyWalletAccountMaster.getStrAccountNumber());
								double earMarkAmt = walletAccountMaster.getStrEarAmount() - updateAmount;
								earMarkUpdate.setStrEarAmount(earMarkAmt);
								accountService.updateEarMark(earMarkRelease);
								
								CurrencyTransferMaster currencyTransfer= new CurrencyTransferMaster();
								currencyTransfer.setTranId(processWebResponse.getTxnId());
								currencyTransfer.setSweepOutAmount(conversionMasterValue.getToCurrencyConversion()*remaingCurrency+"");
								currencyTransfer.setSweepOutCurrencyValue(String.valueOf(conversionMasterValue.getToCurrencyConversion()));
								currencyTransfer.setSweepOutCurrencyCode(processWebResponse.getLastSweepInCurrencyCode());
								currencyTransfer.setSweepInAmount(remaingCurrency+"");
								currencyTransfer.setSweepInCurrencyCode(remaingCurrencyCode);
								currencyTransfer.setBaseAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
								processWebResponse.setLastSweepInCurrencyCode(remaingCurrencyCode);
								currencyTransfer.setBaseAccountType(processWebResponse.getAccountCreation().getStrAccountType());
								currencyTransfer.setFeeAmount( processWebResponse.getFeeAmount());
								currencyTransfer.setGstAmount(processWebResponse.getGstAmount());
								currencyTransferMasterService.addEntryInCurrencyTransferMaster(currencyTransfer);
								
								MultiCurrencyChargesReport multiCurrencyGstReportIn = new MultiCurrencyChargesReport();
								multiCurrencyGstReportIn.setAccountCurrency(processWebResponse.getLastSweepInCurrencyCode());
								multiCurrencyGstReportIn.setAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
								multiCurrencyGstReportIn.setAccountType(processWebResponse.getAccountCreation().getStrAccountType());
								multiCurrencyGstReportIn.setChargesType(CommonConstants.GST_TYPE);
								multiCurrencyGstReportIn.setGlAccountNumber(processWebResponse.getGstCurrencyGl().getStrAccountNumber());
								multiCurrencyGstReportIn.setGlAccountType(processWebResponse.getGstCurrencyGl().getStrGLAccountType());
								multiCurrencyGstReportIn.setParticipantId(processWebResponse.getStrParticipantId());
								multiCurrencyGstReportIn.setTxnAmt(processWebResponse.getGstAmount());
								multiCurrencyGstReportIn.setTxnDate(Utils.getCurrentDate());
								multiCurrencyGstReportIn.setTxnId(processWebResponse.getTxnId());
								multiCurrencyGstReportIn.setTxnType(processWebResponse.getTxnType());
								multiCurrencyGstReportIn.setBaseConversionRate(conversionMasterValue.getToCurrencyConversion());
								multiCurrencyGstReportIn.setBaseCurrency("INR");
								multiCurrencyGstReportIn.setBaseCurrencyAmt(processWebResponse.getGstAmount());
								multiCurrencyGstReportIn.setTxnType("DEBIT");
								multiCurrencyChargesReportService.saveMultiCurrencyChargReport(multiCurrencyGstReportIn);
								
								MultiCurrencyChargesReport multiCurrencyFeeReportIn = new MultiCurrencyChargesReport();
								multiCurrencyFeeReportIn.setAccountCurrency(processWebResponse.getLastSweepInCurrencyCode());
								multiCurrencyFeeReportIn.setAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
								multiCurrencyFeeReportIn.setAccountType(processWebResponse.getAccountCreation().getStrAccountType());
								multiCurrencyFeeReportIn.setChargesType(CommonConstants.FEE_TYPE);
								multiCurrencyFeeReportIn.setGlAccountNumber(processWebResponse.getFeeCurrencyGl().getStrAccountNumber());
								multiCurrencyFeeReportIn.setGlAccountType(processWebResponse.getFeeCurrencyGl().getStrGLAccountType());
								multiCurrencyFeeReportIn.setParticipantId(processWebResponse.getStrParticipantId());
								multiCurrencyFeeReportIn.setTxnAmt(processWebResponse.getFeeAmount());
								multiCurrencyFeeReportIn.setTxnDate(Utils.getCurrentDate());
								multiCurrencyFeeReportIn.setTxnId(processWebResponse.getTxnId());
								multiCurrencyFeeReportIn.setTxnType(processWebResponse.getTxnType());
								multiCurrencyFeeReportIn.setBaseConversionRate(conversionMasterValue.getToCurrencyConversion());
								multiCurrencyFeeReportIn.setBaseCurrency("INR");
								multiCurrencyFeeReportIn.setBaseCurrencyAmt(processWebResponse.getFeeAmount());
								multiCurrencyFeeReportIn.setTxnType("DEBIT");
								multiCurrencyChargesReportService.saveMultiCurrencyChargReport(multiCurrencyFeeReportIn);
								
								System.out.println("Remaining Hashmap in Data::"+processWebResponse.getRemaingCurrencyMap());
								
								AccountTranMaster accountTranMasterAd = new AccountTranMaster();
								accountTranMasterAd.setStrParticipantId(processWebResponse.getStrParticipantId());
								accountTranMasterAd.setStrFrom_account_number(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyWalletAccountNumber());
								accountTranMasterAd.setStrTxn_id(processWebResponse.getTxnId());
								accountTranMasterAd.setSwitchTxDate(Utils.getCurrentDate());
								accountTranMasterAd.setStrLocal_tran_date(Utils.getCurrentDate());
								accountTranMasterAd.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
								accountTranMasterAd.setStrResponseCode("00");
								accountTranMasterAd.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
								accountTranMasterAd.setStrTransaction_amount(conversionMasterValue.getToCurrencyConversion()*remaingCurrency+"");
								accountTranMasterService.addAccountTransactionData(accountTranMasterAd);
								
								AccountTranMaster accountTranMasterFee = new AccountTranMaster();
								accountTranMasterFee.setStrParticipantId(processWebResponse.getStrParticipantId());
								accountTranMasterFee.setStrFrom_account_number(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyWalletAccountNumber());
								accountTranMasterFee.setStrTxn_id(processWebResponse.getTxnId());
								accountTranMasterFee.setSwitchTxDate(Utils.getCurrentDate());
								accountTranMasterFee.setStrLocal_tran_date(Utils.getCurrentDate());
								accountTranMasterFee.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
								accountTranMasterFee.setStrResponseCode("00");
								accountTranMasterFee.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
								accountTranMasterFee.setStrTransaction_amount(processWebResponse.getFeeAmount()+"");
								accountTranMasterService.addAccountTransactionData(accountTranMasterFee);
								
								AccountTranMaster accountTranMasterGst = new AccountTranMaster();
								accountTranMasterGst.setStrParticipantId(processWebResponse.getStrParticipantId());
								accountTranMasterGst.setStrFrom_account_number(processWebResponse.getMultiCurrencyWalletAccountMaster().getStrCurrencyWalletAccountNumber());
								accountTranMasterGst.setStrTxn_id(processWebResponse.getTxnId());
								accountTranMasterGst.setSwitchTxDate(Utils.getCurrentDate());
								accountTranMasterGst.setStrLocal_tran_date(Utils.getCurrentDate());
								accountTranMasterGst.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
								accountTranMasterGst.setStrResponseCode("00");
								accountTranMasterGst.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
								accountTranMasterGst.setStrTransaction_amount(processWebResponse.getGstAmount()+"");
								accountTranMasterService.addAccountTransactionData(accountTranMasterGst);
							}
						}
						
					}
					
					processWebResponse.setRemaingAmount(0);
				//	processWebResponse.setRemaingAmountCurrency(remainingAmountCurrency);
					processWebResponse.getTransactionRequest().setStrTxnAmount(updatedTxnAmount+"");
					processWebResponse.setFeeAmount(currencyToMaster.getFeeAmount());
					processWebResponse.setGstAmount(gstAmount);
				}
				else
				{
					remainingAmount = updatedTxnAmount - processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrClosingBalance();
					remainingAmountCurrency = processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrCurrencyCode();
					updatedTxnAmount = processWebResponse.getToMultiCurrencyWalletAccountMaster().getStrClosingBalance();
					
					int priority = 1;
					remaingCurrencyMap = processWebResponse.getRemaingCurrencyMap();
					processWebResponse.setRemaingAmount(remainingAmount);
					processWebResponse.setRemaingAmountCurrency(remainingAmountCurrency);
					processWebResponse.getTransactionRequest().setStrTxnAmount(updatedTxnAmount+"");
					processWebResponse.setFeeAmount(currencyToMaster.getFeeAmount());
					processWebResponse.setGstAmount(gstAmount);
					processWebResponse.setTxnAmountwithCharges(updatedTxnAmount);
					
					if(remaingCurrencyMap.size() > 0)
					{
						remaingCurrencyMap.put(remaingCurrencyMap.size() + priority, remainingAmountCurrency+"_"+remainingAmount+"");
					}
					else
					{
						remaingCurrencyMap.put(priority, remainingAmountCurrency+"_"+remainingAmount+"");
					}
					
					processWebResponse.setRemaingCurrencyMap(remaingCurrencyMap);
					
					MultiCurrencyWalletAccountMaster fromMultiCurrencyWalletAccountMaster = new MultiCurrencyWalletAccountMaster();
					fromMultiCurrencyWalletAccountMaster.setStrCurrencyCode(remainingAmountCurrency);
					fromMultiCurrencyWalletAccountMaster.setStrClosingBalance(fromMultiCurrencyWalletDetails.getStrClosingBalance() + remainingAmount);
					fromMultiCurrencyWalletAccountMaster.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
					
				}
			}
		}
		catch(Exception e )
		{
			e.getMessage();
		}
		
		return processWebResponse;
	}

}
