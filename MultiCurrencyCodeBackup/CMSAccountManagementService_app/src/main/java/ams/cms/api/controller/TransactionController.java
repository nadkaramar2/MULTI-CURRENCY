package ams.cms.api.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.handler.TransactionHandlerAPI;
import ams.cms.config.AppInfo;
import ams.cms.config.CommonConstants;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.config.TransactionConfig;
import ams.cms.config.TransactionHandler;
import ams.cms.jwt.JwtUtil;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.CurrencyConversionMaster;
import ams.cms.model.CurrencyMaster;
import ams.cms.model.CurrencyTransferMaster;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.MultiCurrencyChargesReport;
import ams.cms.model.MultiCurrencyWalletAccountMaster;
import ams.cms.model.TransactionRequest;
import ams.cms.model.TxnReqRes;
import ams.cms.model.WalleToWalletTransfer;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountStatementService;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.CurrencyConversionMasterService;
import ams.cms.services.CurrencyConvertAndTransfer;
import ams.cms.services.CurrencyMasterService;
import ams.cms.services.CurrencyTransferMasterService;
import ams.cms.services.GLAccountStatementService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.services.MultiCurrencyChargesReportService;
import ams.cms.services.MultiCurrencyWalletAccountService;
import ams.cms.services.TransactionIdService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.util.ProcessWebResponse;
import ams.cms.utility.PayloadReqRes;
import ams.cms.utility.Utils;

@RestController
@RequestMapping("/transactionControl")
public class TransactionController {

	@Autowired 
	JwtUtil jwtUtil;

	@Autowired
	EncryptDecryptConfig encryptDecryptData;

	@Autowired
	private	AppInfo appInfo;

	@Autowired
	private TransactionHandlerAPI transactionHandlerAPI;

	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;


	@Autowired
	AccountMasterService accountMasterService;

	@Autowired
	MultiCurrencyWalletAccountService accountService;



	@Autowired
	GLAccountTypeMasterService glAccountTypeMasterService;

	@Autowired
	CurrencyMasterService currencyMasterService;

	@Autowired
	AccountStatementService accountStatementService;

	@Autowired
	private TransactionIdService transactionIdService;

	@Autowired
	GLAccountStatementService glAccountStatementService;

	@Autowired
	CurrencyConversionMasterService conversionMasterService;

	@Autowired
	TransactionHandler transactionHandler;

	@Autowired
	CurrencyConvertAndTransfer currencyConvertAndTransfer;

	@Autowired
	CurrencyTransferMasterService currencyTransferMasterService;

	@Autowired
	AccountTranMasterService accountTranMasterService;
	
	@Autowired
	MultiCurrencyChargesReportService multiCurrencyChargesReportService; 
	
	@RequestMapping(value = "/doTransaction", method = RequestMethod.POST)
	public ResponseEntity<?> doTransaction(HttpServletRequest request,@RequestBody PayloadReqRes req) throws Exception
	{
		ProcessWebResponse processWebResponse = new ProcessWebResponse();
		ProcessResponse prsWebResponse = new ProcessResponse();
		try 
		{

			TreeMap<Integer, String> remaingCurrencyMap = new TreeMap<Integer, String>(Collections.reverseOrder());
			ProcessResponse processResps = new ProcessResponse();

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}

			String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			TransactionRequest transactionRequest = new ObjectMapper().readValue(decrypt, TransactionRequest.class);
			processWebResponse.setStrParticipantId(participantId);

			//check For Pin
			AccountCreation accountCreationPin = new AccountCreation();
			accountCreationPin.setStrCustPin(transactionRequest.getPin());
			accountCreationPin.setStrCustId(transactionRequest.getCustId());
			TransactionConfig transactionHandlerAPIObj = transactionHandlerAPI.verifyCustomerPin(accountCreationPin);

			if(transactionHandlerAPIObj.getCode().equalsIgnoreCase("S0000")) 
			{

				System.out.println("IN Transaction");
				HashMap<String, CurrencyMaster> CurrencyDetailMap = new HashMap<String, CurrencyMaster>();
				HashMap<String, CurrencyConversionMaster> currencyConversionDetailMap = new HashMap<String, CurrencyConversionMaster>();


				processWebResponse.setTransactionRequest(transactionRequest);
				processWebResponse.setCurrencyConversionDetailMap(currencyConversionDetailMap);
				double txnAmount = Utils.stringToDouble(transactionRequest.getStrTxnAmount());
				processWebResponse.setTxnAmount(txnAmount);


				AccountCreation accountCreation = new AccountCreation();
				accountCreation.setStrAccountNumber(transactionRequest.getStrAccountNumber());
				accountCreation = accountMasterService.getAccountDetailsObj(accountCreation);
				processWebResponse.setAccountCreation(accountCreation);
				processWebResponse.setCurrencyAccountMaster(new MultiCurrencyWalletAccountMaster());
				processWebResponse.setCurrencyAccountMasterFee(new MultiCurrencyWalletAccountMaster());
				processWebResponse.setCurrencyAccountMasterGst(new MultiCurrencyWalletAccountMaster());
				if(accountCreation != null && "Active".equalsIgnoreCase(accountCreation.getStrStatus()) && "Y".equalsIgnoreCase(accountCreation.getIsMultiCurrencySupport()))
				{
					int firstPriority = 1;
					String txnId = transactionIdService.getNewTransactionId();
					processWebResponse.setTxnId(txnId);

					MultiCurrencyWalletAccountMaster currencyWalletAccountMaster = new MultiCurrencyWalletAccountMaster();
					currencyWalletAccountMaster.setStrAccountNumber(accountCreation.getStrAccountNumber());

					List<MultiCurrencyWalletAccountMaster> multiCurrencyWalletAccountMasterList = accountService.getMultiCurrencyAccount(currencyWalletAccountMaster);
					processWebResponse.setMultiCurrencyWalletAccountMasterList(multiCurrencyWalletAccountMasterList);
					// Query Change 

					if(multiCurrencyWalletAccountMasterList != null && multiCurrencyWalletAccountMasterList.size() > 0)
					{
						for(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster :multiCurrencyWalletAccountMasterList)
						{

							processWebResponse.setMultiCurrencyWalletAccountMaster(multiCurrencyWalletAccountMaster);
							if(transactionRequest.getStrCurrency().equalsIgnoreCase(multiCurrencyWalletAccountMaster.getStrCurrencyCode()) && 1 == multiCurrencyWalletAccountMaster.getStrPriority())
							{
								processWebResponse.setBaseCurrencyWalletMaster(multiCurrencyWalletAccountMaster);
								CurrencyMaster currencyMaster = new CurrencyMaster();
								currencyMaster.setCurrencyCode(transactionRequest.getStrCurrency());

								currencyMaster = currencyMasterService.getCurrencyDetailByCurrencyCode(currencyMaster);
								double feeAmount = 0;
								if("Y".equals(currencyMaster.getStrFlatFee()) )
								{
									feeAmount = currencyMaster.getFeeAmount();
								}

								if("Y".equalsIgnoreCase(currencyMaster.getStrIsFee()))
								{
									feeAmount = ((Utils.stringToDouble(currencyMaster.getStrFeePercentage())/100)) * Utils.stringToDouble(transactionRequest.getStrTxnAmount());
									currencyMaster.setFeeAmount(feeAmount);
								}

								CurrencyDetailMap.put(currencyMaster.getCurrencyCode(), currencyMaster);
								processWebResponse.setCurrencyMasterDetailMap(CurrencyDetailMap);
								processWebResponse.setCurrencyMaster(currencyMaster);

								double gstAmount = (currencyMaster.getGstPercentage()/100) * currencyMaster.getFeeAmount();
								processWebResponse.setFeeAmount(feeAmount);
								processWebResponse.setGstAmount(gstAmount);

								double updatedTxnAmount = Utils.stringToDouble(transactionRequest.getStrTxnAmount()) + feeAmount + gstAmount;
								processWebResponse.setTxnAmountwithCharges(updatedTxnAmount);
								double balanceAmount = multiCurrencyWalletAccountMaster.getStrClosingBalance() - multiCurrencyWalletAccountMaster.getStrEarAmount() ;
								multiCurrencyWalletAccountMaster.setStrClosingBalance(balanceAmount);

								if( multiCurrencyWalletAccountMaster.getStrClosingBalance() > updatedTxnAmount || multiCurrencyWalletAccountMaster.getStrClosingBalance() == updatedTxnAmount)
								{

									MultiCurrencyWalletAccountMaster earMarkUpdate = new MultiCurrencyWalletAccountMaster();
									earMarkUpdate.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
									earMarkUpdate.setStrAccountNumber(multiCurrencyWalletAccountMaster.getStrAccountNumber());
									double earMarkAmount = multiCurrencyWalletAccountMaster.getStrEarAmount() + updatedTxnAmount;
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

									processWebResponse.setAmount(Utils.stringToDouble(processWebResponse.getTransactionRequest().getStrTxnAmount()));
									GLAccountTypeMaster linkedGl = transactionHandler.updateLinkedGl(processWebResponse);
									processWebResponse.setCurrencyGl(linkedGl);
									transactionHandler.addGLAccountStatment(processWebResponse);
									processWebResponse.setAmount(processWebResponse.getFeeAmount());
									GLAccountTypeMaster linkedGlFee = transactionHandler.updateLinkedGl(processWebResponse);
									processWebResponse.setCurrencyGl(linkedGlFee);
									transactionHandler.addGLAccountStatment(processWebResponse);
									processWebResponse.setAmount(processWebResponse.getGstAmount());
									GLAccountTypeMaster linkedGlGst = transactionHandler.updateLinkedGl(processWebResponse);
									processWebResponse.setCurrencyGl(linkedGlGst);
									transactionHandler.addGLAccountStatment(processWebResponse);

									MultiCurrencyWalletAccountMaster walletAccountMaster = new MultiCurrencyWalletAccountMaster();
									walletAccountMaster.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
									walletAccountMaster.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
									walletAccountMaster = accountService.getCurrencyAccount(walletAccountMaster);

									MultiCurrencyWalletAccountMaster earMarkRelease = new MultiCurrencyWalletAccountMaster();
									earMarkRelease.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
									earMarkRelease.setStrAccountNumber(multiCurrencyWalletAccountMaster.getStrAccountNumber());
									double earMark = walletAccountMaster.getStrEarAmount() - updatedTxnAmount;
									earMarkUpdate.setStrEarAmount(earMark);
									accountService.updateEarMark(earMarkRelease);

									AccountTranMaster accountTranMaster = new AccountTranMaster();
									accountTranMaster.setStrParticipantId(processWebResponse.getStrParticipantId());
									accountTranMaster.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
									accountTranMaster.setStrTxn_id(processWebResponse.getTxnId());
									accountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
									accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());
									accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
									accountTranMaster.setStrResponseCode("00");
									accountTranMaster.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
									accountTranMaster.setStrTransaction_amount(processWebResponse.getTransactionRequest().getStrTxnAmount());
									accountTranMasterService.addAccountTransactionData(accountTranMaster);

									AccountTranMaster accountTranMasterFee = new AccountTranMaster();
									accountTranMasterFee.setStrParticipantId(processWebResponse.getStrParticipantId());
									accountTranMasterFee.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
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
									accountTranMasterGst.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
									accountTranMasterGst.setStrTxn_id(processWebResponse.getTxnId());
									accountTranMasterGst.setSwitchTxDate(Utils.getCurrentDate());
									accountTranMasterGst.setStrLocal_tran_date(Utils.getCurrentDate());
									accountTranMasterGst.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
									accountTranMasterGst.setStrResponseCode("00");
									accountTranMasterGst.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
									accountTranMasterGst.setStrTransaction_amount(processWebResponse.getGstAmount()+"");
									accountTranMasterService.addAccountTransactionData(accountTranMasterGst);

									prsWebResponse.setCode("S0000");
									prsWebResponse.setMessage("Succesfully Transfered.");
									prsWebResponse.setStatus("Success.");
									prsWebResponse.setIsCurrencyTransfer("false");

								}
								else
								{
									double remainingAmount = updatedTxnAmount - multiCurrencyWalletAccountMaster.getStrClosingBalance();
									String remainingAmountCurrency = transactionRequest.getStrCurrency();
									//	remaingCurrencyMap.put(1, remainingAmountCurrency+"_"+remainingAmount+"");
									//	processWebResponse.setRemaingCurrencyMap(remaingCurrencyMap);
									processWebResponse.setRemaingAmount(remainingAmount);
									processWebResponse.setRemaingAmountCurrency(remainingAmountCurrency);

									List<MultiCurrencyWalletAccountMaster> sortedList = multiCurrencyWalletAccountMasterList.stream().sorted(Comparator.comparingInt(MultiCurrencyWalletAccountMaster::getStrPriority)).collect(Collectors.toList());

									int basePriority = 2;
									for(MultiCurrencyWalletAccountMaster multicurrency :sortedList)
									{
										if(!multicurrency.getStrCurrencyCode().equalsIgnoreCase(processWebResponse.getTransactionRequest().getStrCurrency()))
										{

											processWebResponse.setMultiCurrencyWalletAccountMaster(multicurrency);
											if( basePriority ==  multicurrency.getStrPriority() )
											{
												processWebResponse.setToMultiCurrencyWalletAccountMaster(multicurrency);
												processWebResponse = currencyConvertAndTransfer.currencyConvertAndTransferToWallet(processWebResponse);
												if(processWebResponse.getRemaingAmount() == 0)
												{
													for(MultiCurrencyWalletAccountMaster multicurrencyBalanceAdd:multiCurrencyWalletAccountMasterList)
													{
														if(remainingAmountCurrency.equalsIgnoreCase(multicurrencyBalanceAdd.getStrCurrencyCode()))
														{
															HashMap<String, CurrencyConversionMaster> currencyConversionMap = processWebResponse.getCurrencyConversionDetailMap();
															double updatedBalance = multicurrencyBalanceAdd.getStrClosingBalance() + remainingAmount;
															multicurrencyBalanceAdd.setStrClosingBalance(updatedBalance);
															multicurrencyBalanceAdd.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
															multicurrencyBalanceAdd.setStrCurrencyCode(remainingAmountCurrency);

															if(currencyConversionMap.size() != 1)
															{
																int i = transactionHandler.addClosingBalanceInWallet(multicurrencyBalanceAdd); 
															}

															processWebResponse.setRemaingAmount(remainingAmount);
															processWebResponse.setMultiCurrencyWalletAccountMaster(multicurrencyBalanceAdd);
															if(currencyConversionMap.size() != 1)
															{
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
															}
															processWebResponse.setRemaingAmount(0);
															//	if(i > 0)
															{

																MultiCurrencyWalletAccountMaster multiCurrencyWalletAccount = new MultiCurrencyWalletAccountMaster();
																multiCurrencyWalletAccount.setStrAccountNumber(processWebResponse.getBaseCurrencyWalletMaster().getStrAccountNumber());
																multiCurrencyWalletAccount.setStrCurrencyCode(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyCode());

																multiCurrencyWalletAccount = accountService.getCurrencyAccount(multiCurrencyWalletAccount);


																CurrencyMaster currencyMasterDetail = new CurrencyMaster();
																currencyMasterDetail.setCurrencyCode(transactionRequest.getStrCurrency());

																currencyMasterDetail = currencyMasterService.getCurrencyDetailByCurrencyCode(currencyMasterDetail);
																double feeAmountReq = 0;
																if("Y".equals(currencyMaster.getStrFlatFee()) )
																{
																	feeAmountReq = currencyMasterDetail.getFeeAmount();
																}

																if("Y".equalsIgnoreCase(currencyMaster.getStrIsFee()))
																{
																	feeAmountReq = ((Utils.stringToDouble(currencyMaster.getStrFeePercentage())/100)) * Utils.stringToDouble(transactionRequest.getStrTxnAmount());
																	currencyMasterDetail.setFeeAmount(feeAmountReq);
																}

																processWebResponse.setCurrencyMaster(currencyMasterDetail);

																double gstAmountReq = (currencyMasterDetail.getGstPercentage()/100) * feeAmountReq;
																processWebResponse.setFeeAmount(feeAmountReq);
																processWebResponse.setGstAmount(gstAmountReq);
																processWebResponse.getTransactionRequest().setStrTxnAmount(txnAmount+"");
																double updatedTxnAmountReq = txnAmount + feeAmount + gstAmount;
																processWebResponse.setTxnAmountwithCharges(updatedTxnAmountReq);
																processWebResponse.setMultiCurrencyWalletAccountMaster(multiCurrencyWalletAccount);

																MultiCurrencyWalletAccountMaster earMarkReleas = new MultiCurrencyWalletAccountMaster();
																earMarkReleas.setStrCurrencyCode(multiCurrencyWalletAccount.getStrCurrencyCode());
																earMarkReleas.setStrAccountNumber(multiCurrencyWalletAccount.getStrAccountNumber());
																double earMarkAmt = multiCurrencyWalletAccount.getStrEarAmount() + updatedTxnAmountReq;
																earMarkReleas.setStrEarAmount(earMarkAmt);
																accountService.updateEarMark(earMarkReleas);

																if(currencyConversionMap.size() != 1)
																{

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

																}

																processWebResponse.setAmount(Utils.stringToDouble(processWebResponse.getTransactionRequest().getStrTxnAmount()));
																GLAccountTypeMaster linkedGl = transactionHandler.updateLinkedGl(processWebResponse);
																processWebResponse.setCurrencyGl(linkedGl);
																
																transactionHandler.addGLAccountStatment(processWebResponse);
																processWebResponse.setAmount(processWebResponse.getFeeAmount());
																GLAccountTypeMaster linkedGlFee = transactionHandler.updateLinkedGl(processWebResponse);
																processWebResponse.setCurrencyGl(linkedGlFee);
																processWebResponse.setFeeCurrencyGl(linkedGlFee);
																transactionHandler.addGLAccountStatment(processWebResponse);
																processWebResponse.setAmount(processWebResponse.getGstAmount());
																GLAccountTypeMaster linkedGlGst = transactionHandler.updateLinkedGl(processWebResponse);
																processWebResponse.setCurrencyGl(linkedGlGst);
																processWebResponse.setGstCurrencyGl(linkedGlGst);
																transactionHandler.addGLAccountStatment(processWebResponse);


																//		HashMap<String, CurrencyConversionMaster> currencyConversionMap = processWebResponse.getCurrencyConversionDetailMap();
																CurrencyConversionMaster conversionMasterValue = new CurrencyConversionMaster();
																if(currencyConversionMap.size() == 1)
																{
																	for(Map.Entry<String, CurrencyConversionMaster> remaingCurencyMap : currencyConversionMap.entrySet())
																	{
																		conversionMasterValue = remaingCurencyMap.getValue();
																	}
																}
																else
																{
																	conversionMasterValue = currencyConversionMap.get(remainingAmountCurrency+"_"+processWebResponse.getLastSweepInCurrencyCode());
																}

																if(currencyConversionMap.size() != 1)
																{
																	CurrencyTransferMaster currencyTransfer= new CurrencyTransferMaster();
																	currencyTransfer.setTranId(processWebResponse.getTxnId());
																	currencyTransfer.setSweepOutAmount(conversionMasterValue.getToCurrencyConversion()*remainingAmount+"");
																	currencyTransfer.setSweepOutCurrencyValue(String.valueOf(conversionMasterValue.getToCurrencyConversion()));
																	currencyTransfer.setSweepOutCurrencyCode(processWebResponse.getLastSweepInCurrencyCode());
																	currencyTransfer.setSweepInAmount(remainingAmount+"");
																	currencyTransfer.setSweepInCurrencyCode(remainingAmountCurrency);
																	processWebResponse.setLastSweepInCurrencyCode(remainingAmountCurrency);
																	currencyTransfer.setBaseAccountNumber(transactionRequest.getStrAccountNumber());
																	currencyTransfer.setBaseAccountType(processWebResponse.getAccountCreation().getStrAccountType());
																	currencyTransfer.setFeeAmount( processWebResponse.getFeeAmount());
																	currencyTransfer.setGstAmount(processWebResponse.getGstAmount());
																	currencyTransferMasterService.addEntryInCurrencyTransferMaster(currencyTransfer);
																	
																	MultiCurrencyChargesReport multiCurrencyGstReport = new MultiCurrencyChargesReport();
																	multiCurrencyGstReport.setAccountCurrency(processWebResponse.getLastSweepInCurrencyCode());
																	multiCurrencyGstReport.setAccountNumber(transactionRequest.getStrAccountNumber());
																	multiCurrencyGstReport.setAccountType(processWebResponse.getAccountCreation().getStrAccountType());
																	multiCurrencyGstReport.setChargesType(CommonConstants.GST_TYPE);
																	multiCurrencyGstReport.setGlAccountNumber(processWebResponse.getGstCurrencyGl().getStrAccountNumber());
																	multiCurrencyGstReport.setGlAccountType(processWebResponse.getGstCurrencyGl().getStrGLAccountType());
																	multiCurrencyGstReport.setParticipantId(processWebResponse.getStrParticipantId());
																	multiCurrencyGstReport.setTxnAmt(processWebResponse.getGstAmount());
																	multiCurrencyGstReport.setTxnDate(Utils.getCurrentDate());
																	multiCurrencyGstReport.setTxnId(txnId);
																	multiCurrencyGstReport.setTxnType(processWebResponse.getTxnType());
																	multiCurrencyGstReport.setBaseConversionRate(conversionMasterValue.getToCurrencyConversion());
																	multiCurrencyGstReport.setBaseCurrency("INR");
																	multiCurrencyGstReport.setBaseCurrencyAmt(processWebResponse.getGstAmount());
																	multiCurrencyGstReport.setTxnType("DEBIT");
																	multiCurrencyChargesReportService.saveMultiCurrencyChargReport(multiCurrencyGstReport);
																	
																	MultiCurrencyChargesReport multiCurrencyFeeReport = new MultiCurrencyChargesReport();
																	multiCurrencyFeeReport.setAccountCurrency(processWebResponse.getLastSweepInCurrencyCode());
																	multiCurrencyFeeReport.setAccountNumber(transactionRequest.getStrAccountNumber());
																	multiCurrencyFeeReport.setAccountType(processWebResponse.getAccountCreation().getStrAccountType());
																	multiCurrencyFeeReport.setChargesType(CommonConstants.FEE_TYPE);
																	multiCurrencyGstReport.setGlAccountNumber(processWebResponse.getFeeCurrencyGl().getStrAccountNumber());
																	multiCurrencyGstReport.setGlAccountType(processWebResponse.getFeeCurrencyGl().getStrGLAccountType());
																	multiCurrencyFeeReport.setParticipantId(processWebResponse.getStrParticipantId());
																	multiCurrencyFeeReport.setTxnAmt(processWebResponse.getFeeAmount());
																	multiCurrencyFeeReport.setTxnDate(Utils.getCurrentDate());
																	multiCurrencyFeeReport.setTxnId(txnId);
																	multiCurrencyFeeReport.setTxnType(processWebResponse.getTxnType());
																	multiCurrencyFeeReport.setBaseConversionRate(conversionMasterValue.getToCurrencyConversion());
																	multiCurrencyFeeReport.setBaseCurrency("INR");
																	multiCurrencyFeeReport.setBaseCurrencyAmt(processWebResponse.getFeeAmount());
																	multiCurrencyFeeReport.setTxnType("DEBIT");
																	multiCurrencyChargesReportService.saveMultiCurrencyChargReport(multiCurrencyGstReport);
																}

																MultiCurrencyWalletAccountMaster earMarkupdte = new MultiCurrencyWalletAccountMaster();
																earMarkupdte.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
																earMarkupdte.setStrAccountNumber(multiCurrencyWalletAccountMaster.getStrAccountNumber());
																double earMarkAmnt = multiCurrencyWalletAccountMaster.getStrEarAmount() - processWebResponse.getTxnAmountwithCharges();
																earMarkupdte.setStrEarAmount(earMarkAmnt);
																accountService.updateEarMark(earMarkupdte);

																AccountTranMaster accountTranMaster = new AccountTranMaster();
																accountTranMaster.setStrParticipantId(processWebResponse.getStrParticipantId());
																accountTranMaster.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
																accountTranMaster.setStrTxn_id(processWebResponse.getTxnId());
																accountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
																accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());
																accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
																accountTranMaster.setStrResponseCode("00");
																accountTranMaster.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
																accountTranMaster.setStrTransaction_amount(processWebResponse.getTransactionRequest().getStrTxnAmount());
																accountTranMasterService.addAccountTransactionData(accountTranMaster);

																AccountTranMaster accountTranMasterFee = new AccountTranMaster();
																accountTranMasterFee.setStrParticipantId(processWebResponse.getStrParticipantId());
																accountTranMasterFee.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
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
																accountTranMasterGst.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
																accountTranMasterGst.setStrTxn_id(processWebResponse.getTxnId());
																accountTranMasterGst.setSwitchTxDate(Utils.getCurrentDate());
																accountTranMasterGst.setStrLocal_tran_date(Utils.getCurrentDate());
																accountTranMasterGst.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
																accountTranMasterGst.setStrResponseCode("00");
																accountTranMasterGst.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
																accountTranMasterGst.setStrTransaction_amount(processWebResponse.getGstAmount()+"");
																accountTranMasterService.addAccountTransactionData(accountTranMasterGst);

																CurrencyTransferMaster currencyTransferdetails= new CurrencyTransferMaster();
																currencyTransferdetails.setTranId(processWebResponse.getTxnId());
																List<CurrencyTransferMaster> currencyTransferList = currencyTransferMasterService.getCurrencyMasterByTranId(currencyTransferdetails);

																processWebResponse.setRemaingAmount(0);
																prsWebResponse.setCode("S0000");
																prsWebResponse.setMessage("Succesfully Transfered.");
																prsWebResponse.setStatus("Success.");
																prsWebResponse.setIsCurrencyTransfer("true");
																prsWebResponse.setCurrencyTransferMasterList(currencyTransferList);
															}
														}
													}
													break;
												}
												else
												{
													basePriority++;
												}
											}
										}
									}
									if(processWebResponse.getRemaingAmount() > 0)
									{
										prsWebResponse.setMessage("Insufficient Balance.");
										prsWebResponse.setCode("E0000");
										prsWebResponse.setStatus("Failed");
										return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, prsWebResponse));
									}
								}
							}
							else if(transactionRequest.getStrCurrency().equalsIgnoreCase(multiCurrencyWalletAccountMaster.getStrCurrencyCode()) && 1 != multiCurrencyWalletAccountMaster.getStrPriority())
							{
								List<MultiCurrencyWalletAccountMaster> updatedMultiCurrencyWalletAccountMasterList = new ArrayList<MultiCurrencyWalletAccountMaster>();
								int previousPos = multiCurrencyWalletAccountMaster.getStrPriority();
								multiCurrencyWalletAccountMaster.setStrPriority(1);

								for(MultiCurrencyWalletAccountMaster multicurrency :multiCurrencyWalletAccountMasterList)
								{
									if(!transactionRequest.getStrCurrency().equalsIgnoreCase(multicurrency.getStrCurrencyCode()))
									{
										if(multicurrency.getStrPriority() < previousPos)
										{
											int updatedPosition = multicurrency.getStrPriority() + 1;
											multicurrency.setStrPriority(updatedPosition);
										}
									}
									updatedMultiCurrencyWalletAccountMasterList.add(multicurrency);
								}
								System.out.println("UpdatedMultiCurrencyWalletAccountMasterList:::"+updatedMultiCurrencyWalletAccountMasterList.toString());
								processWebResponse.setBaseCurrencyWalletMaster(multiCurrencyWalletAccountMaster);
								CurrencyMaster currencyMaster = new CurrencyMaster();
								currencyMaster.setCurrencyCode(transactionRequest.getStrCurrency());

								currencyMaster = currencyMasterService.getCurrencyDetailByCurrencyCode(currencyMaster);
								currencyMaster = currencyMasterService.getCurrencyDetailByCurrencyCode(currencyMaster);
								double feeAmount = 0;
								if("Y".equals(currencyMaster.getStrFlatFee()) )
								{
									feeAmount = currencyMaster.getFeeAmount();
								}

								if("Y".equalsIgnoreCase(currencyMaster.getStrIsFee()))
								{
									feeAmount = ((Utils.stringToDouble(currencyMaster.getStrFeePercentage())/100)) * Utils.stringToDouble(transactionRequest.getStrTxnAmount());
									currencyMaster.setFeeAmount(feeAmount);
								}
								processWebResponse.setCurrencyMaster(currencyMaster);

								double gstAmount = (currencyMaster.getGstPercentage()/100) * feeAmount;
								processWebResponse.setFeeAmount(feeAmount);
								processWebResponse.setGstAmount(gstAmount);

								double updatedTxnAmount = Utils.stringToDouble(transactionRequest.getStrTxnAmount()) + feeAmount + gstAmount;
								processWebResponse.setTxnAmountwithCharges(updatedTxnAmount);

								if( multiCurrencyWalletAccountMaster.getStrClosingBalance() > updatedTxnAmount)
								{
									MultiCurrencyWalletAccountMaster earMarkUpdate = new MultiCurrencyWalletAccountMaster();
									earMarkUpdate.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
									earMarkUpdate.setStrAccountNumber(multiCurrencyWalletAccountMaster.getStrAccountNumber());
									double earMarkAmount = multiCurrencyWalletAccountMaster.getStrEarAmount() + updatedTxnAmount;
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

									processWebResponse.setAmount(Utils.stringToDouble(processWebResponse.getTransactionRequest().getStrTxnAmount()));
									GLAccountTypeMaster linkedGl = transactionHandler.updateLinkedGl(processWebResponse);
									processWebResponse.setCurrencyGl(linkedGl);
									transactionHandler.addGLAccountStatment(processWebResponse);
									processWebResponse.setAmount(processWebResponse.getFeeAmount());
									GLAccountTypeMaster linkedGlFee = transactionHandler.updateLinkedGl(processWebResponse);
									processWebResponse.setCurrencyGl(linkedGlFee);
									transactionHandler.addGLAccountStatment(processWebResponse);
									processWebResponse.setAmount(processWebResponse.getGstAmount());
									GLAccountTypeMaster linkedGlGst = transactionHandler.updateLinkedGl(processWebResponse);
									processWebResponse.setCurrencyGl(linkedGlGst);
									transactionHandler.addGLAccountStatment(processWebResponse);

									MultiCurrencyWalletAccountMaster walletAccountMaster = new MultiCurrencyWalletAccountMaster();
									walletAccountMaster.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
									walletAccountMaster.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
									walletAccountMaster = accountService.getCurrencyAccount(walletAccountMaster);

									MultiCurrencyWalletAccountMaster earMarkRelease = new MultiCurrencyWalletAccountMaster();
									earMarkRelease.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
									earMarkRelease.setStrAccountNumber(multiCurrencyWalletAccountMaster.getStrAccountNumber());
									double earMark = walletAccountMaster.getStrEarAmount() - updatedTxnAmount;
									earMarkUpdate.setStrEarAmount(earMark);
									accountService.updateEarMark(earMarkRelease);

									AccountTranMaster accountTranMaster = new AccountTranMaster();
									accountTranMaster.setStrParticipantId(processWebResponse.getStrParticipantId());
									accountTranMaster.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
									accountTranMaster.setStrTxn_id(processWebResponse.getTxnId());
									accountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
									accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());
									accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
									accountTranMaster.setStrResponseCode("00");
									accountTranMaster.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
									accountTranMaster.setStrTransaction_amount(processWebResponse.getTransactionRequest().getStrTxnAmount());
									accountTranMasterService.addAccountTransactionData(accountTranMaster);

									AccountTranMaster accountTranMasterFee = new AccountTranMaster();
									accountTranMasterFee.setStrParticipantId(processWebResponse.getStrParticipantId());
									accountTranMasterFee.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
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
									accountTranMasterGst.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
									accountTranMasterGst.setStrTxn_id(processWebResponse.getTxnId());
									accountTranMasterGst.setSwitchTxDate(Utils.getCurrentDate());
									accountTranMasterGst.setStrLocal_tran_date(Utils.getCurrentDate());
									accountTranMasterGst.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
									accountTranMasterGst.setStrResponseCode("00");
									accountTranMasterGst.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
									accountTranMasterGst.setStrTransaction_amount(processWebResponse.getGstAmount()+"");
									accountTranMasterService.addAccountTransactionData(accountTranMasterGst);

									prsWebResponse.setCode("S0000");
									prsWebResponse.setMessage("Succesfully Transfered.");
									prsWebResponse.setStatus("Success.");
									prsWebResponse.setIsCurrencyTransfer("false");
								}
								else
								{
									double remainingAmount = updatedTxnAmount - multiCurrencyWalletAccountMaster.getStrClosingBalance();
									String remainingAmountCurrency = transactionRequest.getStrCurrency();

									processWebResponse.setRemaingAmount(remainingAmount);
									processWebResponse.setRemaingAmountCurrency(remainingAmountCurrency);

									int basePriority = 2;
									for(MultiCurrencyWalletAccountMaster multicurrency :multiCurrencyWalletAccountMasterList)
									{
										if(!multicurrency.getStrCurrencyCode().equalsIgnoreCase(processWebResponse.getTransactionRequest().getStrCurrency()))
										{

											processWebResponse.setMultiCurrencyWalletAccountMaster(multicurrency);
											if( basePriority ==  multicurrency.getStrPriority() )
											{
												processWebResponse.setToMultiCurrencyWalletAccountMaster(multicurrency);
												processWebResponse = currencyConvertAndTransfer.currencyConvertAndTransferToWallet(processWebResponse);
												if(processWebResponse.getRemaingAmount() == 0)
												{
													for(MultiCurrencyWalletAccountMaster multicurrencyBalanceAdd:multiCurrencyWalletAccountMasterList)
													{
														if(remainingAmountCurrency.equalsIgnoreCase(multicurrencyBalanceAdd.getStrCurrencyCode()))
														{
															HashMap<String, CurrencyConversionMaster> currencyConversionMap = processWebResponse.getCurrencyConversionDetailMap();
															double updatedBalance = multicurrencyBalanceAdd.getStrClosingBalance() + remainingAmount;
															multicurrencyBalanceAdd.setStrClosingBalance(updatedBalance);
															multicurrencyBalanceAdd.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
															multicurrencyBalanceAdd.setStrCurrencyCode(remainingAmountCurrency);
															if(currencyConversionMap.size() != 1)
															{
																int i = transactionHandler.addClosingBalanceInWallet(multicurrencyBalanceAdd); 
															}
															processWebResponse.setRemaingAmount(remainingAmount);
															processWebResponse.setMultiCurrencyWalletAccountMaster(multicurrencyBalanceAdd);
															if(currencyConversionMap.size() != 1)
															{
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
															}
															processWebResponse.setRemaingAmount(0);
															//		if(i > 0)
															{

																MultiCurrencyWalletAccountMaster multiCurrencyWalletAccount = new MultiCurrencyWalletAccountMaster();
																multiCurrencyWalletAccount.setStrAccountNumber(processWebResponse.getBaseCurrencyWalletMaster().getStrAccountNumber());
																multiCurrencyWalletAccount.setStrCurrencyCode(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyCode());

																multiCurrencyWalletAccount = accountService.getCurrencyAccount(multiCurrencyWalletAccount);

																CurrencyMaster currencyMasterDetail = new CurrencyMaster();
																currencyMasterDetail.setCurrencyCode(transactionRequest.getStrCurrency());

																currencyMasterDetail = currencyMasterService.getCurrencyDetailByCurrencyCode(currencyMasterDetail);

																double feeAmountReq = 0;
																if("Y".equals(currencyMaster.getStrFlatFee()) )
																{
																	feeAmountReq = currencyMasterDetail.getFeeAmount();
																}

																if("Y".equalsIgnoreCase(currencyMaster.getStrIsFee()))
																{
																	feeAmountReq = ((Utils.stringToDouble(currencyMaster.getStrFeePercentage())/100)) * Utils.stringToDouble(transactionRequest.getStrTxnAmount());
																	currencyMasterDetail.setFeeAmount(feeAmountReq);
																}


																processWebResponse.setCurrencyMaster(currencyMaster);

																double gstAmountReq = (currencyMasterDetail.getGstPercentage()/100) * feeAmountReq;
																processWebResponse.setFeeAmount(feeAmountReq);
																processWebResponse.setGstAmount(gstAmountReq);
																processWebResponse.getTransactionRequest().setStrTxnAmount(txnAmount+"");
																double updatedTxnAmountReq = txnAmount + feeAmount + gstAmount;
																processWebResponse.setTxnAmountwithCharges(updatedTxnAmountReq);
																processWebResponse.setMultiCurrencyWalletAccountMaster(multiCurrencyWalletAccount);

																MultiCurrencyWalletAccountMaster earMarkupdt = new MultiCurrencyWalletAccountMaster();
																earMarkupdt.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
																earMarkupdt.setStrAccountNumber(multiCurrencyWalletAccountMaster.getStrAccountNumber());
																double earMarkAmt = multiCurrencyWalletAccountMaster.getStrEarAmount() - updatedTxnAmountReq;
																earMarkupdt.setStrEarAmount(earMarkAmt);
																accountService.updateEarMark(earMarkupdt);

																if(currencyConversionMap.size() != 1)
																{
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
																}
																processWebResponse.setAmount(Utils.stringToDouble(processWebResponse.getTransactionRequest().getStrTxnAmount()));
																GLAccountTypeMaster linkedGl = transactionHandler.updateLinkedGl(processWebResponse);
																processWebResponse.setCurrencyGl(linkedGl);
																transactionHandler.addGLAccountStatment(processWebResponse);
																processWebResponse.setAmount(processWebResponse.getFeeAmount());
																GLAccountTypeMaster linkedGlFee = transactionHandler.updateLinkedGl(processWebResponse);
																processWebResponse.setCurrencyGl(linkedGlFee);
																processWebResponse.setFeeCurrencyGl(linkedGlFee);
																transactionHandler.addGLAccountStatment(processWebResponse);
																processWebResponse.setAmount(processWebResponse.getGstAmount());
																GLAccountTypeMaster linkedGlGst = transactionHandler.updateLinkedGl(processWebResponse);
																processWebResponse.setCurrencyGl(linkedGlGst);
																processWebResponse.setGstCurrencyGl(linkedGlGst);
																transactionHandler.addGLAccountStatment(processWebResponse);

																//			HashMap<String, CurrencyConversionMaster> currencyConversionMap = processWebResponse.getCurrencyConversionDetailMap();
																CurrencyConversionMaster currencyConnversionMap = new CurrencyConversionMaster();
																if(currencyConversionMap.size() == 1)
																{
																	for(Map.Entry<String, CurrencyConversionMaster> remaingCurencyMap : currencyConversionMap.entrySet())
																	{
																		currencyConnversionMap = remaingCurencyMap.getValue();
																	}
																}
																else
																{
																	currencyConnversionMap = currencyConversionMap.get(remainingAmountCurrency+"_"+processWebResponse.getLastSweepInCurrencyCode());
																}

																if(currencyConversionMap.size() != 1)
																{
																	CurrencyTransferMaster currencyTransfer= new CurrencyTransferMaster();
																	currencyTransfer.setTranId(processWebResponse.getTxnId());
																	currencyTransfer.setSweepOutAmount(currencyConnversionMap.getToCurrencyConversion()*remainingAmount+"");
																	currencyTransfer.setSweepOutCurrencyValue(String.valueOf(currencyConnversionMap.getToCurrencyConversion()));
																	currencyTransfer.setSweepOutCurrencyCode(processWebResponse.getLastSweepInCurrencyCode());
																	currencyTransfer.setSweepInAmount(remainingAmount+"");
																	currencyTransfer.setSweepInCurrencyCode(remainingAmountCurrency);
																	currencyTransfer.setBaseAccountNumber(transactionRequest.getStrAccountNumber());
																	processWebResponse.setLastSweepInCurrencyCode(remainingAmountCurrency);
																	currencyTransfer.setBaseAccountType(processWebResponse.getAccountCreation().getStrAccountType());
																	currencyTransfer.setFeeAmount( processWebResponse.getFeeAmount());
																	currencyTransfer.setGstAmount(processWebResponse.getGstAmount());
																	currencyTransferMasterService.addEntryInCurrencyTransferMaster(currencyTransfer);
																	
																	MultiCurrencyChargesReport multiCurrencyGstReport = new MultiCurrencyChargesReport();
																	multiCurrencyGstReport.setAccountCurrency(processWebResponse.getLastSweepInCurrencyCode());
																	multiCurrencyGstReport.setAccountNumber(transactionRequest.getStrAccountNumber());
																	multiCurrencyGstReport.setAccountType(processWebResponse.getAccountCreation().getStrAccountType());
																	multiCurrencyGstReport.setChargesType(CommonConstants.GST_TYPE);
																	multiCurrencyGstReport.setGlAccountNumber(processWebResponse.getGstCurrencyGl().getStrAccountNumber());
																	multiCurrencyGstReport.setGlAccountType(processWebResponse.getGstCurrencyGl().getStrAccountType());
																	multiCurrencyGstReport.setParticipantId(processWebResponse.getStrParticipantId());
																	multiCurrencyGstReport.setTxnAmt(processWebResponse.getGstAmount());
																	multiCurrencyGstReport.setTxnDate(Utils.getCurrentDate());
																	multiCurrencyGstReport.setTxnId(txnId);
																	multiCurrencyGstReport.setTxnType(processWebResponse.getTxnType());
																	multiCurrencyGstReport.setBaseConversionRate(currencyConnversionMap.getToCurrencyConversion());
																	multiCurrencyGstReport.setBaseCurrency("INR");
																	multiCurrencyGstReport.setTxnType("DEBIT");
																	multiCurrencyGstReport.setBaseCurrencyAmt(processWebResponse.getGstAmount());
																	
																	multiCurrencyChargesReportService.saveMultiCurrencyChargReport(multiCurrencyGstReport);
																	
																	MultiCurrencyChargesReport multiCurrencyFeeReport = new MultiCurrencyChargesReport();
																	multiCurrencyFeeReport.setAccountCurrency(processWebResponse.getLastSweepInCurrencyCode());
																	multiCurrencyFeeReport.setAccountNumber(transactionRequest.getStrAccountNumber());
																	multiCurrencyFeeReport.setAccountType(processWebResponse.getAccountCreation().getStrAccountType());
																	multiCurrencyFeeReport.setChargesType(CommonConstants.FEE_TYPE);
																	multiCurrencyGstReport.setGlAccountNumber(processWebResponse.getGstCurrencyGl().getStrAccountNumber());
																	multiCurrencyGstReport.setGlAccountType(processWebResponse.getGstCurrencyGl().getStrAccountType());
																	multiCurrencyFeeReport.setParticipantId(processWebResponse.getStrParticipantId());
																	multiCurrencyFeeReport.setTxnAmt(processWebResponse.getFeeAmount());
																	multiCurrencyFeeReport.setTxnDate(Utils.getCurrentDate());
																	multiCurrencyFeeReport.setTxnId(txnId);
																	multiCurrencyFeeReport.setTxnType(processWebResponse.getTxnType());
																	multiCurrencyFeeReport.setBaseConversionRate(currencyConnversionMap.getToCurrencyConversion());
																	multiCurrencyFeeReport.setBaseCurrency("INR");
																	multiCurrencyFeeReport.setBaseCurrencyAmt(processWebResponse.getFeeAmount());
																	multiCurrencyFeeReport.setTxnType("DEBIT");
																	multiCurrencyChargesReportService.saveMultiCurrencyChargReport(multiCurrencyGstReport);
																	
																}
																MultiCurrencyWalletAccountMaster earMarkupdte = new MultiCurrencyWalletAccountMaster();
																earMarkupdte.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
																earMarkupdte.setStrAccountNumber(multiCurrencyWalletAccountMaster.getStrAccountNumber());
																double earMarkAmnt = multiCurrencyWalletAccountMaster.getStrEarAmount() - updatedTxnAmountReq;
																earMarkupdte.setStrEarAmount(earMarkAmnt);
																accountService.updateEarMark(earMarkupdte);



																AccountTranMaster accountTranMaster = new AccountTranMaster();
																accountTranMaster.setStrParticipantId(processWebResponse.getStrParticipantId());
																accountTranMaster.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
																accountTranMaster.setStrTxn_id(processWebResponse.getTxnId());
																accountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
																accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());
																accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
																accountTranMaster.setStrResponseCode("00");
																accountTranMaster.setStrTran_type(transactionRequest.getStrTxnType()); // change to type
																accountTranMaster.setStrTransaction_amount(processWebResponse.getTransactionRequest().getStrTxnAmount());
																accountTranMasterService.addAccountTransactionData(accountTranMaster);

																AccountTranMaster accountTranMasterFee = new AccountTranMaster();
																accountTranMasterFee.setStrParticipantId(processWebResponse.getStrParticipantId());
																accountTranMasterFee.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
																accountTranMasterFee.setStrTxn_id(processWebResponse.getTxnId());
																accountTranMasterFee.setSwitchTxDate(Utils.getCurrentDate());
																accountTranMasterFee.setStrLocal_tran_date(Utils.getCurrentDate());
																accountTranMasterFee.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
																accountTranMasterFee.setStrResponseCode("00");
																accountTranMasterFee.setStrTran_type(transactionRequest.getStrTxnType()); // change to type
																accountTranMasterFee.setStrTransaction_amount(processWebResponse.getFeeAmount()+"");
																accountTranMasterService.addAccountTransactionData(accountTranMasterFee);

																AccountTranMaster accountTranMasterGst = new AccountTranMaster();
																accountTranMasterGst.setStrParticipantId(processWebResponse.getStrParticipantId());
																accountTranMasterGst.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
																accountTranMasterGst.setStrTxn_id(processWebResponse.getTxnId());
																accountTranMasterGst.setSwitchTxDate(Utils.getCurrentDate());
																accountTranMasterGst.setStrLocal_tran_date(Utils.getCurrentDate());
																accountTranMasterGst.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
																accountTranMasterGst.setStrResponseCode("00");
																accountTranMasterGst.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
																accountTranMasterGst.setStrTransaction_amount(processWebResponse.getGstAmount()+"");
																accountTranMasterService.addAccountTransactionData(accountTranMasterGst);

																CurrencyTransferMaster currencyTransferdetails= new CurrencyTransferMaster();
																currencyTransferdetails.setTranId(processWebResponse.getTxnId());
																List<CurrencyTransferMaster> currencyTransferList = currencyTransferMasterService.getCurrencyMasterByTranId(currencyTransferdetails);

																processWebResponse.setRemaingAmount(0);
																prsWebResponse.setCode("S0000");
																prsWebResponse.setMessage("Succesfully Transfered.");
																prsWebResponse.setStatus("Success.");
																prsWebResponse.setIsCurrencyTransfer("true");
																prsWebResponse.setCurrencyTransferMasterList(currencyTransferList);
															}
														}
													}
													break;
												}
												else
												{
													basePriority++;
												}
											}
										}
									}
									if(processWebResponse.getRemaingAmount() > 0)
									{
										prsWebResponse.setCode("E0000");
										prsWebResponse.setMessage("Insufficient Balance.");
										prsWebResponse.setStatus("Failed");
										return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, prsWebResponse));
									}
								}
							}

						}
					}
					else
					{
						prsWebResponse.setCode("E0000");
						prsWebResponse.setMessage("Currency Account Not Supported.");
						prsWebResponse.setStatus("Failed");
						return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, prsWebResponse));
					}
				}
				else
				{
					prsWebResponse.setCode("E0000");
					prsWebResponse.setMessage("Account Number not Found");
					prsWebResponse.setStatus("Failed");
					return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, prsWebResponse));
				}

			}
			else 
			{
				prsWebResponse.setCode("E0000");
				prsWebResponse.setStatus("Failed");
				prsWebResponse.setMessage(transactionHandlerAPIObj.getMessage());
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, prsWebResponse));
			}
			return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, prsWebResponse));
		}
		catch (Exception e)
		{
			prsWebResponse.setCode("E0000");
			prsWebResponse.setStatus("Failed");
			System.out.println("Exception:::::::::::::::::::"+e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, prsWebResponse));
	}

	@RequestMapping(value = "/w2wTransfer", method = RequestMethod.POST)
	public ResponseEntity<?> walletToWalletTransfer(@RequestBody PayloadReqRes req)
	{
		System.out.println("IN Wallet to wallet.");
		ProcessWebResponse processWebResponse = new ProcessWebResponse();
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			ProcessResponse processResps = new ProcessResponse();

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}

			String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			WalleToWalletTransfer walleToWalletTransfer = new ObjectMapper().readValue(decrypt, WalleToWalletTransfer.class);


			//check For Pin
			AccountCreation accountCreationPin = new AccountCreation();
			accountCreationPin.setStrCustPin(walleToWalletTransfer.getPin());
			accountCreationPin.setStrCustId(walleToWalletTransfer.getCustId());
			TransactionConfig transactionHandlerAPIObj = transactionHandlerAPI.verifyCustomerPin(accountCreationPin);

			if(transactionHandlerAPIObj.getCode().equalsIgnoreCase("S0000"))
			{
				processWebResponse.setWalleToWalletTransfer(walleToWalletTransfer);
				processWebResponse.setTransactionRequest(new TransactionRequest());

				String txnId = transactionIdService.getNewTransactionId();
				processWebResponse.setTxnId(txnId);

				CurrencyMaster currencyFromMaster = new CurrencyMaster();
				currencyFromMaster.setCurrencyCode(processWebResponse.getWalleToWalletTransfer().getFromCurrency());
				currencyFromMaster = currencyMasterService.getCurrencyDetailByCurrencyCode(currencyFromMaster);
				processWebResponse.setCurrencyFromMaster(currencyFromMaster);

				CurrencyMaster currencyToMaster = new CurrencyMaster();
				currencyToMaster.setCurrencyCode(processWebResponse.getWalleToWalletTransfer().getToCurrency());
				currencyToMaster = currencyMasterService.getCurrencyDetailByCurrencyCode(currencyToMaster);
				processWebResponse.setCurrencyToMaster(currencyFromMaster);

				MultiCurrencyWalletAccountMaster fromMultiCurrencyWalletDetails = new MultiCurrencyWalletAccountMaster();
				fromMultiCurrencyWalletDetails.setStrAccountNumber(processWebResponse.getWalleToWalletTransfer().getStrAccountNumber());
				fromMultiCurrencyWalletDetails.setStrCurrencyCode(walleToWalletTransfer.getFromCurrency());

				fromMultiCurrencyWalletDetails = accountService.getCurrencyAccount(fromMultiCurrencyWalletDetails);
				processWebResponse.setMultiCurrencyWalletAccountMaster(fromMultiCurrencyWalletDetails);

				CurrencyConversionMaster conversionMasterValue = new CurrencyConversionMaster();
				conversionMasterValue.setFromCurrency(walleToWalletTransfer.getFromCurrency());
				conversionMasterValue.setToCurrency(walleToWalletTransfer.getToCurrency());
				conversionMasterValue = conversionMasterService.getCurrencyMasterByCurrencyCode(conversionMasterValue);

				double convertedAmount = walleToWalletTransfer.getStrTxnAmount() *  conversionMasterValue.getToCurrencyConversion();
				double gstAmount = (currencyToMaster.getGstPercentage()/100) * currencyToMaster.getFeeAmount() ;
				double updatedTxnAmount = convertedAmount + currencyToMaster.getFeeAmount() + gstAmount;

				processWebResponse.setTxnAmount(convertedAmount);
				processWebResponse.setFeeAmount(currencyToMaster.getFeeAmount());
				processWebResponse.setGstAmount(gstAmount);

				if( fromMultiCurrencyWalletDetails.getStrClosingBalance() > updatedTxnAmount)
				{
					MultiCurrencyWalletAccountMaster earMarkUpdate = new MultiCurrencyWalletAccountMaster();
					earMarkUpdate.setStrCurrencyCode(fromMultiCurrencyWalletDetails.getStrCurrencyCode());
					earMarkUpdate.setStrAccountNumber(fromMultiCurrencyWalletDetails.getStrAccountNumber());
					double earMarkAmount = fromMultiCurrencyWalletDetails.getStrEarAmount() + updatedTxnAmount;
					earMarkUpdate.setStrEarAmount(earMarkAmount);
					accountService.updateEarMark(earMarkUpdate);
					processWebResponse.getTransactionRequest().setStrTxnAmount(convertedAmount+"");

					MultiCurrencyWalletAccountMaster currencyAccountMaster = transactionHandler.w2wupdateClosingBalance(processWebResponse);
					processWebResponse.setCurrencyAccountMaster(currencyAccountMaster);
					MultiCurrencyWalletAccountMaster currencyAccountMasterFee = transactionHandler.w2wupdateClosingBalanceFee(processWebResponse);
					processWebResponse.setCurrencyAccountMasterFee(currencyAccountMasterFee);
					MultiCurrencyWalletAccountMaster currencyAccountMasterGst = transactionHandler.w2wupdateClosingBalanceGst(processWebResponse);
					processWebResponse.setCurrencyAccountMasterGst(currencyAccountMasterGst);

					transactionHandler.w2waddBatchEntryAccountStatment(processWebResponse);

					processWebResponse.setCurrencyMaster(currencyFromMaster);
					GLAccountTypeMaster glAccountTypeMaster = transactionHandler.updateGlClosingBalance(processWebResponse);
					processWebResponse.setCurrencyGl(glAccountTypeMaster);
					GLAccountTypeMaster feeGlMaster = transactionHandler.updateFeeGlClosingBalance(processWebResponse);
					processWebResponse.setFeeCurrencyGl(feeGlMaster);
					GLAccountTypeMaster gstGlMaster = transactionHandler.updateGstGlClosingBalance(processWebResponse);
					processWebResponse.setGstCurrencyGl(gstGlMaster);

					transactionHandler.w2wAddGLStatements(processWebResponse);

					processWebResponse.setAmount(convertedAmount);
					processWebResponse.setAmount(Utils.stringToDouble(processWebResponse.getTransactionRequest().getStrTxnAmount()));
					GLAccountTypeMaster linkedGl = transactionHandler.updateLinkedGl(processWebResponse);
					processWebResponse.setCurrencyGl(linkedGl);
					transactionHandler.addGLAccountStatment(processWebResponse);
					processWebResponse.setAmount(processWebResponse.getFeeAmount());
					GLAccountTypeMaster linkedGlFee = transactionHandler.updateLinkedGl(processWebResponse);
					processWebResponse.setCurrencyGl(linkedGlFee);
					transactionHandler.addGLAccountStatment(processWebResponse);
					processWebResponse.setAmount(processWebResponse.getGstAmount());
					GLAccountTypeMaster linkedGlGst = transactionHandler.updateLinkedGl(processWebResponse);
					processWebResponse.setCurrencyGl(linkedGlGst);
					transactionHandler.addGLAccountStatment(processWebResponse);

					MultiCurrencyWalletAccountMaster toMultiCurrencyWalletDetails = new MultiCurrencyWalletAccountMaster();
					toMultiCurrencyWalletDetails.setStrAccountNumber(processWebResponse.getWalleToWalletTransfer().getStrAccountNumber());
					toMultiCurrencyWalletDetails.setStrCurrencyCode(walleToWalletTransfer.getToCurrency());
					toMultiCurrencyWalletDetails = accountService.getCurrencyAccount(toMultiCurrencyWalletDetails);

					processWebResponse.setMultiCurrencyWalletAccountMaster(toMultiCurrencyWalletDetails);

					double updatedBalance = toMultiCurrencyWalletDetails.getStrClosingBalance() + walleToWalletTransfer.getStrTxnAmount();
					toMultiCurrencyWalletDetails.setStrClosingBalance(updatedBalance);
					toMultiCurrencyWalletDetails.setStrAccountNumber(walleToWalletTransfer.getStrAccountNumber());
					toMultiCurrencyWalletDetails.setStrCurrencyCode(walleToWalletTransfer.getToCurrency());
					transactionHandler.addClosingBalanceInWallet(toMultiCurrencyWalletDetails); 

					transactionHandler.w2wAddAccountTransactionData(processWebResponse);

					CurrencyTransferMaster currencyTransfer= new CurrencyTransferMaster();
					currencyTransfer.setTranId(processWebResponse.getTxnId());
					currencyTransfer.setSweepOutAmount(convertedAmount+"");
					currencyTransfer.setSweepOutCurrencyValue(conversionMasterValue.getToCurrencyConversion()+"");
					currencyTransfer.setSweepOutCurrencyCode(walleToWalletTransfer.getFromCurrency());
					currencyTransfer.setSweepInAmount(walleToWalletTransfer.getStrTxnAmount()+"");
					currencyTransfer.setSweepInCurrencyCode(walleToWalletTransfer.getToCurrency());
					currencyTransfer.setSweepInCurrencyValue("1");
					currencyTransfer.setCreatedDate(Utils.getCurrentDate());
					currencyTransferMasterService.addEntryInCurrencyTransferMaster(currencyTransfer);

					MultiCurrencyWalletAccountMaster earMarkupdte = new MultiCurrencyWalletAccountMaster();
					earMarkupdte.setStrCurrencyCode(fromMultiCurrencyWalletDetails.getStrCurrencyCode());
					earMarkupdte.setStrAccountNumber(fromMultiCurrencyWalletDetails.getStrAccountNumber());
					double earMarkAmnt = fromMultiCurrencyWalletDetails.getStrEarAmount() - updatedTxnAmount;
					earMarkupdte.setStrEarAmount(earMarkAmnt);
					accountService.updateEarMark(earMarkupdte);

					AccountTranMaster accountTranMaster = new AccountTranMaster();
					//participantId
					accountTranMaster.setStrParticipantId(participantId);
					accountTranMaster.setStrTxn_id(processWebResponse.getTxnId());
					accountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
					accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());
					accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
					accountTranMaster.setStrResponseCode("00");
					accountTranMaster.setStrTran_type("Transfer");
					String authCode = Utils.getAlphaNumericString();
					accountTranMaster.setStrAuthCode(authCode);
					accountTranMaster.setStrTransaction_amount(convertedAmount+"");
					accountTranMasterService.addAccountTransactionData(accountTranMaster);

					AccountTranMaster accountTranMasterFee = new AccountTranMaster();
					accountTranMasterFee.setStrParticipantId(participantId);
					accountTranMasterFee.setStrFrom_account_number(fromMultiCurrencyWalletDetails.getStrCurrencyWalletAccountNumber());
					accountTranMasterFee.setStrTxn_id(processWebResponse.getTxnId());
					accountTranMasterFee.setSwitchTxDate(Utils.getCurrentDate());
					accountTranMasterFee.setStrLocal_tran_date(Utils.getCurrentDate());
					accountTranMasterFee.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
					accountTranMasterFee.setStrResponseCode("00");
					accountTranMasterFee.setStrTran_type("Transfer"); // change to type
					accountTranMasterFee.setStrTransaction_amount(processWebResponse.getFeeAmount()+"");
					accountTranMasterService.addAccountTransactionData(accountTranMasterFee);

					AccountTranMaster accountTranMasterGst = new AccountTranMaster();
					accountTranMasterGst.setStrParticipantId(participantId);
					accountTranMasterGst.setStrFrom_account_number(fromMultiCurrencyWalletDetails.getStrCurrencyWalletAccountNumber());
					accountTranMasterGst.setStrTxn_id(processWebResponse.getTxnId());
					accountTranMasterGst.setSwitchTxDate(Utils.getCurrentDate());
					accountTranMasterGst.setStrLocal_tran_date(Utils.getCurrentDate());
					accountTranMasterGst.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
					accountTranMasterGst.setStrResponseCode("00");
					accountTranMasterGst.setStrTran_type("Transfer"); // change to type
					accountTranMasterGst.setStrTransaction_amount(processWebResponse.getGstAmount()+"");
					accountTranMasterService.addAccountTransactionData(accountTranMasterGst);

					processResponse.setCode("S0000");
					processResponse.setMessage("Succesfully Transfered.");
					processResponse.setStatus("Sucess.");
					return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResponse));
				}
				else
				{
					processResponse.setCode("E0000");
					processResponse.setMessage("Insufficient Balance.");
					processResponse.setStatus("Success.");
					return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResponse));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResponse));
		//	return ResponseEntity.ok(processResponse);
	}
	

	@RequestMapping(value = "/doTransactionForSimulator", method = RequestMethod.POST)
	public ResponseEntity<?> doTransaction(@RequestBody TxnReqRes txnReqRes) throws Exception
	{
		ProcessWebResponse processWebResponse = new ProcessWebResponse();
		ProcessResponse prsWebResponse = new ProcessResponse();
		
		try 
		{
			System.out.println("Entry In----------------->>  /doTransactionForSimulator  ");
			
			TreeMap<Integer, String> remaingCurrencyMap = new TreeMap<Integer, String>(Collections.reverseOrder());
			ProcessResponse processResps = new ProcessResponse();

			TransactionRequest transactionRequest = new TransactionRequest();
			transactionRequest.setStrAccountNumber(txnReqRes.getTxnData().getAccountNumber());
			transactionRequest.setStrTxnAmount(txnReqRes.getTxnData().getDe004_amount());
			transactionRequest.setStrTxnType("PUR");
			transactionRequest.setStrCurrency(txnReqRes.getTxnData().getDe050_settle_currency_code());
			transactionRequest.setParticipantid(txnReqRes.getTxnData().getParticipant_id());
			//String participantId = appInfo.getStrParticipantId();
			//processWebResponse.setStrParticipantId(participantId);

			System.out.println("Request :: "+txnReqRes+"  ");
			System.out.println("RequestIN :: "+transactionRequest+"  ");
		

				System.out.println("IN Transaction");
				HashMap<String, CurrencyMaster> CurrencyDetailMap = new HashMap<String, CurrencyMaster>();
				HashMap<String, CurrencyConversionMaster> currencyConversionDetailMap = new HashMap<String, CurrencyConversionMaster>();


				processWebResponse.setTransactionRequest(transactionRequest);
				processWebResponse.setCurrencyConversionDetailMap(currencyConversionDetailMap);
				double txnAmount = Utils.stringToDouble(transactionRequest.getStrTxnAmount());
				processWebResponse.setTxnAmount(txnAmount);


				AccountCreation accountCreation = new AccountCreation();
				accountCreation.setStrAccountNumber(transactionRequest.getStrAccountNumber());
				accountCreation = accountMasterService.getAccountDetailsObj(accountCreation);
				processWebResponse.setAccountCreation(accountCreation);
				processWebResponse.setCurrencyAccountMaster(new MultiCurrencyWalletAccountMaster());
				processWebResponse.setCurrencyAccountMasterFee(new MultiCurrencyWalletAccountMaster());
				processWebResponse.setCurrencyAccountMasterGst(new MultiCurrencyWalletAccountMaster());
				if(accountCreation != null && "Active".equalsIgnoreCase(accountCreation.getStrStatus()) && "Y".equalsIgnoreCase(accountCreation.getIsMultiCurrencySupport()))
				{
					int firstPriority = 1;
					String txnId = transactionIdService.getNewTransactionId();
					processWebResponse.setTxnId(txnId);

					MultiCurrencyWalletAccountMaster currencyWalletAccountMaster = new MultiCurrencyWalletAccountMaster();
					currencyWalletAccountMaster.setStrAccountNumber(accountCreation.getStrAccountNumber());

					List<MultiCurrencyWalletAccountMaster> multiCurrencyWalletAccountMasterList = accountService.getMultiCurrencyAccount(currencyWalletAccountMaster);
					processWebResponse.setMultiCurrencyWalletAccountMasterList(multiCurrencyWalletAccountMasterList);
					// Query Change 

					if(multiCurrencyWalletAccountMasterList != null && multiCurrencyWalletAccountMasterList.size() > 0)
					{
						for(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster :multiCurrencyWalletAccountMasterList)
						{

							processWebResponse.setMultiCurrencyWalletAccountMaster(multiCurrencyWalletAccountMaster);
							if(transactionRequest.getStrCurrency().equalsIgnoreCase(multiCurrencyWalletAccountMaster.getStrCurrencyCode()) && 1 == multiCurrencyWalletAccountMaster.getStrPriority())
							{
								processWebResponse.setBaseCurrencyWalletMaster(multiCurrencyWalletAccountMaster);
								CurrencyMaster currencyMaster = new CurrencyMaster();
								currencyMaster.setCurrencyCode(transactionRequest.getStrCurrency());

								currencyMaster = currencyMasterService.getCurrencyDetailByCurrencyCode(currencyMaster);
								double feeAmount = 0;
								if("Y".equals(currencyMaster.getStrFlatFee()) )
								{
									feeAmount = currencyMaster.getFeeAmount();
								}

								if("Y".equalsIgnoreCase(currencyMaster.getStrIsFee()))
								{
									feeAmount = ((Utils.stringToDouble(currencyMaster.getStrFeePercentage())/100)) * Utils.stringToDouble(transactionRequest.getStrTxnAmount());
									currencyMaster.setFeeAmount(feeAmount);
								}

								CurrencyDetailMap.put(currencyMaster.getCurrencyCode(), currencyMaster);
								processWebResponse.setCurrencyMasterDetailMap(CurrencyDetailMap);
								processWebResponse.setCurrencyMaster(currencyMaster);

								double gstAmount = (currencyMaster.getGstPercentage()/100) * currencyMaster.getFeeAmount();
								processWebResponse.setFeeAmount(feeAmount);
								processWebResponse.setGstAmount(gstAmount);

								double updatedTxnAmount = Utils.stringToDouble(transactionRequest.getStrTxnAmount()) + feeAmount + gstAmount;
								processWebResponse.setTxnAmountwithCharges(updatedTxnAmount);
								double balanceAmount = multiCurrencyWalletAccountMaster.getStrClosingBalance() - multiCurrencyWalletAccountMaster.getStrEarAmount() ;
								multiCurrencyWalletAccountMaster.setStrClosingBalance(balanceAmount);

								if( multiCurrencyWalletAccountMaster.getStrClosingBalance() > updatedTxnAmount || multiCurrencyWalletAccountMaster.getStrClosingBalance() == updatedTxnAmount)
								{

									MultiCurrencyWalletAccountMaster earMarkUpdate = new MultiCurrencyWalletAccountMaster();
									earMarkUpdate.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
									earMarkUpdate.setStrAccountNumber(multiCurrencyWalletAccountMaster.getStrAccountNumber());
									double earMarkAmount = multiCurrencyWalletAccountMaster.getStrEarAmount() + updatedTxnAmount;
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

									processWebResponse.setAmount(Utils.stringToDouble(processWebResponse.getTransactionRequest().getStrTxnAmount()));
									GLAccountTypeMaster linkedGl = transactionHandler.updateLinkedGl(processWebResponse);
									processWebResponse.setCurrencyGl(linkedGl);
									transactionHandler.addGLAccountStatment(processWebResponse);
									processWebResponse.setAmount(processWebResponse.getFeeAmount());
									GLAccountTypeMaster linkedGlFee = transactionHandler.updateLinkedGl(processWebResponse);
									processWebResponse.setCurrencyGl(linkedGlFee);
									transactionHandler.addGLAccountStatment(processWebResponse);
									processWebResponse.setAmount(processWebResponse.getGstAmount());
									GLAccountTypeMaster linkedGlGst = transactionHandler.updateLinkedGl(processWebResponse);
									processWebResponse.setCurrencyGl(linkedGlGst);
									transactionHandler.addGLAccountStatment(processWebResponse);

									MultiCurrencyWalletAccountMaster walletAccountMaster = new MultiCurrencyWalletAccountMaster();
									walletAccountMaster.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
									walletAccountMaster.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
									walletAccountMaster = accountService.getCurrencyAccount(walletAccountMaster);

									MultiCurrencyWalletAccountMaster earMarkRelease = new MultiCurrencyWalletAccountMaster();
									earMarkRelease.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
									earMarkRelease.setStrAccountNumber(multiCurrencyWalletAccountMaster.getStrAccountNumber());
									double earMark = walletAccountMaster.getStrEarAmount() - updatedTxnAmount;
									earMarkUpdate.setStrEarAmount(earMark);
									accountService.updateEarMark(earMarkRelease);

									AccountTranMaster accountTranMaster = new AccountTranMaster();
									accountTranMaster.setStrParticipantId(processWebResponse.getStrParticipantId());
									accountTranMaster.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
									accountTranMaster.setStrTxn_id(processWebResponse.getTxnId());
									accountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
									accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());
									accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
									accountTranMaster.setStrResponseCode("00");
									accountTranMaster.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
									accountTranMaster.setStrTransaction_amount(processWebResponse.getTransactionRequest().getStrTxnAmount());
									accountTranMasterService.addAccountTransactionData(accountTranMaster);

									AccountTranMaster accountTranMasterFee = new AccountTranMaster();
									accountTranMasterFee.setStrParticipantId(processWebResponse.getStrParticipantId());
									accountTranMasterFee.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
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
									accountTranMasterGst.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
									accountTranMasterGst.setStrTxn_id(processWebResponse.getTxnId());
									accountTranMasterGst.setSwitchTxDate(Utils.getCurrentDate());
									accountTranMasterGst.setStrLocal_tran_date(Utils.getCurrentDate());
									accountTranMasterGst.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
									accountTranMasterGst.setStrResponseCode("00");
									accountTranMasterGst.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
									accountTranMasterGst.setStrTransaction_amount(processWebResponse.getGstAmount()+"");
									accountTranMasterService.addAccountTransactionData(accountTranMasterGst);

									prsWebResponse.setCode("S0000");
									prsWebResponse.setMessage("Succesfully Transfered.");
									prsWebResponse.setStatus("Success.");
									prsWebResponse.setIsCurrencyTransfer("false");

								}
								else
								{
									double remainingAmount = updatedTxnAmount - multiCurrencyWalletAccountMaster.getStrClosingBalance();
									String remainingAmountCurrency = transactionRequest.getStrCurrency();
									//	remaingCurrencyMap.put(1, remainingAmountCurrency+"_"+remainingAmount+"");
									//	processWebResponse.setRemaingCurrencyMap(remaingCurrencyMap);
									processWebResponse.setRemaingAmount(remainingAmount);
									processWebResponse.setRemaingAmountCurrency(remainingAmountCurrency);

									List<MultiCurrencyWalletAccountMaster> sortedList = multiCurrencyWalletAccountMasterList.stream().sorted(Comparator.comparingInt(MultiCurrencyWalletAccountMaster::getStrPriority)).collect(Collectors.toList());

									int basePriority = 2;
									for(MultiCurrencyWalletAccountMaster multicurrency :sortedList)
									{
										if(!multicurrency.getStrCurrencyCode().equalsIgnoreCase(processWebResponse.getTransactionRequest().getStrCurrency()))
										{

											processWebResponse.setMultiCurrencyWalletAccountMaster(multicurrency);
											if( basePriority ==  multicurrency.getStrPriority() )
											{
												processWebResponse.setToMultiCurrencyWalletAccountMaster(multicurrency);
												processWebResponse = currencyConvertAndTransfer.currencyConvertAndTransferToWallet(processWebResponse);
												if(processWebResponse.getRemaingAmount() == 0)
												{
													for(MultiCurrencyWalletAccountMaster multicurrencyBalanceAdd:multiCurrencyWalletAccountMasterList)
													{
														if(remainingAmountCurrency.equalsIgnoreCase(multicurrencyBalanceAdd.getStrCurrencyCode()))
														{
															HashMap<String, CurrencyConversionMaster> currencyConversionMap = processWebResponse.getCurrencyConversionDetailMap();
															double updatedBalance = multicurrencyBalanceAdd.getStrClosingBalance() + remainingAmount;
															multicurrencyBalanceAdd.setStrClosingBalance(updatedBalance);
															multicurrencyBalanceAdd.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
															multicurrencyBalanceAdd.setStrCurrencyCode(remainingAmountCurrency);

															if(currencyConversionMap.size() != 1)
															{
																int i = transactionHandler.addClosingBalanceInWallet(multicurrencyBalanceAdd); 
															}

															processWebResponse.setRemaingAmount(remainingAmount);
															processWebResponse.setMultiCurrencyWalletAccountMaster(multicurrencyBalanceAdd);
															if(currencyConversionMap.size() != 1)
															{
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
															}
															processWebResponse.setRemaingAmount(0);
															//	if(i > 0)
															{

																MultiCurrencyWalletAccountMaster multiCurrencyWalletAccount = new MultiCurrencyWalletAccountMaster();
																multiCurrencyWalletAccount.setStrAccountNumber(processWebResponse.getBaseCurrencyWalletMaster().getStrAccountNumber());
																multiCurrencyWalletAccount.setStrCurrencyCode(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyCode());

																multiCurrencyWalletAccount = accountService.getCurrencyAccount(multiCurrencyWalletAccount);


																CurrencyMaster currencyMasterDetail = new CurrencyMaster();
																currencyMasterDetail.setCurrencyCode(transactionRequest.getStrCurrency());

																currencyMasterDetail = currencyMasterService.getCurrencyDetailByCurrencyCode(currencyMasterDetail);
																double feeAmountReq = 0;
																if("Y".equals(currencyMaster.getStrFlatFee()) )
																{
																	feeAmountReq = currencyMasterDetail.getFeeAmount();
																}

																if("Y".equalsIgnoreCase(currencyMaster.getStrIsFee()))
																{
																	feeAmountReq = ((Utils.stringToDouble(currencyMaster.getStrFeePercentage())/100)) * Utils.stringToDouble(transactionRequest.getStrTxnAmount());
																	currencyMasterDetail.setFeeAmount(feeAmountReq);
																}

																processWebResponse.setCurrencyMaster(currencyMasterDetail);

																double gstAmountReq = (currencyMasterDetail.getGstPercentage()/100) * feeAmountReq;
																processWebResponse.setFeeAmount(feeAmountReq);
																processWebResponse.setGstAmount(gstAmountReq);
																processWebResponse.getTransactionRequest().setStrTxnAmount(txnAmount+"");
																double updatedTxnAmountReq = txnAmount + feeAmount + gstAmount;
																processWebResponse.setTxnAmountwithCharges(updatedTxnAmountReq);
																processWebResponse.setMultiCurrencyWalletAccountMaster(multiCurrencyWalletAccount);

																MultiCurrencyWalletAccountMaster earMarkReleas = new MultiCurrencyWalletAccountMaster();
																earMarkReleas.setStrCurrencyCode(multiCurrencyWalletAccount.getStrCurrencyCode());
																earMarkReleas.setStrAccountNumber(multiCurrencyWalletAccount.getStrAccountNumber());
																double earMarkAmt = multiCurrencyWalletAccount.getStrEarAmount() + updatedTxnAmountReq;
																earMarkReleas.setStrEarAmount(earMarkAmt);
																accountService.updateEarMark(earMarkReleas);

																if(currencyConversionMap.size() != 1)
																{

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

																}

																processWebResponse.setAmount(Utils.stringToDouble(processWebResponse.getTransactionRequest().getStrTxnAmount()));
																GLAccountTypeMaster linkedGl = transactionHandler.updateLinkedGl(processWebResponse);
																processWebResponse.setCurrencyGl(linkedGl);
																transactionHandler.addGLAccountStatment(processWebResponse);
																processWebResponse.setAmount(processWebResponse.getFeeAmount());
																GLAccountTypeMaster linkedGlFee = transactionHandler.updateLinkedGl(processWebResponse);
																processWebResponse.setCurrencyGl(linkedGlFee);
																transactionHandler.addGLAccountStatment(processWebResponse);
																processWebResponse.setAmount(processWebResponse.getGstAmount());
																GLAccountTypeMaster linkedGlGst = transactionHandler.updateLinkedGl(processWebResponse);
																processWebResponse.setCurrencyGl(linkedGlGst);
																transactionHandler.addGLAccountStatment(processWebResponse);


																//		HashMap<String, CurrencyConversionMaster> currencyConversionMap = processWebResponse.getCurrencyConversionDetailMap();
																CurrencyConversionMaster conversionMasterValue = new CurrencyConversionMaster();
																if(currencyConversionMap.size() == 1)
																{
																	for(Map.Entry<String, CurrencyConversionMaster> remaingCurencyMap : currencyConversionMap.entrySet())
																	{
																		conversionMasterValue = remaingCurencyMap.getValue();
																	}
																}
																else
																{
																	conversionMasterValue = currencyConversionMap.get(remainingAmountCurrency+"_"+processWebResponse.getLastSweepInCurrencyCode());
																}

																if(currencyConversionMap.size() != 1)
																{
																	CurrencyTransferMaster currencyTransfer= new CurrencyTransferMaster();
																	currencyTransfer.setTranId(processWebResponse.getTxnId());
																	currencyTransfer.setSweepOutAmount(conversionMasterValue.getToCurrencyConversion()*remainingAmount+"");
																	currencyTransfer.setSweepOutCurrencyValue(String.valueOf(conversionMasterValue.getToCurrencyConversion()));
																	currencyTransfer.setSweepOutCurrencyCode(processWebResponse.getLastSweepInCurrencyCode());
																	currencyTransfer.setSweepInAmount(remainingAmount+"");
																	currencyTransfer.setSweepInCurrencyCode(remainingAmountCurrency);
																	processWebResponse.setLastSweepInCurrencyCode(remainingAmountCurrency);
																	currencyTransfer.setBaseAccountNumber(transactionRequest.getStrAccountNumber());
																	currencyTransfer.setBaseAccountType(processWebResponse.getAccountCreation().getStrAccountType());
																	currencyTransfer.setFeeAmount( processWebResponse.getFeeAmount());
																	currencyTransfer.setGstAmount(processWebResponse.getGstAmount());
																	currencyTransferMasterService.addEntryInCurrencyTransferMaster(currencyTransfer);
																	
																	MultiCurrencyChargesReport multiCurrencyGstReport = new MultiCurrencyChargesReport();
																	multiCurrencyGstReport.setAccountCurrency(processWebResponse.getLastSweepInCurrencyCode());
																	multiCurrencyGstReport.setAccountNumber(transactionRequest.getStrAccountNumber());
																	multiCurrencyGstReport.setAccountType(processWebResponse.getAccountCreation().getStrAccountType());
																	multiCurrencyGstReport.setChargesType(CommonConstants.FEE_TYPE);
														//			multiCurrencyGstReport.setGlAccountNumber(processWebResponse.getStrAccountNumber());
														//			multiCurrencyGstReport.setGlAccountType(processWebResponse.getStrGLAccountType());
																	multiCurrencyGstReport.setParticipantId(processWebResponse.getStrParticipantId());
																	multiCurrencyGstReport.setTxnAmt(processWebResponse.getFeeAmount());
																	multiCurrencyGstReport.setTxnDate(Utils.getCurrentDate());
																	multiCurrencyGstReport.setTxnId(txnId);
																	multiCurrencyGstReport.setTxnType(processWebResponse.getTxnType());
																	multiCurrencyGstReport.setBaseConversionRate(conversionMasterValue.getToCurrencyConversion());
														//			multiCurrencyGstReport.setBaseCurrency(processWebResponse.getBaseWalletAccount().getCurrencyCode());
																	multiCurrencyGstReport.setBaseCurrencyAmt(processWebResponse.getGstAmount());
																	multiCurrencyChargesReportService.saveMultiCurrencyChargeReport(multiCurrencyGstReport);
																	
																	MultiCurrencyChargesReport multiCurrencyFeeReport = new MultiCurrencyChargesReport();
																	multiCurrencyFeeReport.setAccountCurrency(processWebResponse.getLastSweepInCurrencyCode());
																	multiCurrencyFeeReport.setAccountNumber(transactionRequest.getStrAccountNumber());
																	multiCurrencyFeeReport.setAccountType(processWebResponse.getAccountCreation().getStrAccountType());
																	multiCurrencyFeeReport.setChargesType(CommonConstants.FEE_TYPE);
														//			multiCurrencyGstReport.setGlAccountNumber(processWebResponse.getStrAccountNumber());
														//			multiCurrencyGstReport.setGlAccountType(processWebResponse.getStrGLAccountType());
																	multiCurrencyFeeReport.setParticipantId(processWebResponse.getStrParticipantId());
																	multiCurrencyFeeReport.setTxnAmt(processWebResponse.getFeeAmount());
																	multiCurrencyFeeReport.setTxnDate(Utils.getCurrentDate());
																	multiCurrencyFeeReport.setTxnId(txnId);
																	multiCurrencyFeeReport.setTxnType(processWebResponse.getTxnType());
																	multiCurrencyFeeReport.setBaseConversionRate(conversionMasterValue.getToCurrencyConversion());
														//			multiCurrencyFeeReport.setBaseCurrency(processWebResponse.getBaseWalletAccount().getCurrencyCode());
																	multiCurrencyFeeReport.setBaseCurrencyAmt(processWebResponse.getGstAmount());
																	multiCurrencyChargesReportService.saveMultiCurrencyChargeReport(multiCurrencyGstReport);
																}

																MultiCurrencyWalletAccountMaster earMarkupdte = new MultiCurrencyWalletAccountMaster();
																earMarkupdte.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
																earMarkupdte.setStrAccountNumber(multiCurrencyWalletAccountMaster.getStrAccountNumber());
																double earMarkAmnt = multiCurrencyWalletAccountMaster.getStrEarAmount() - processWebResponse.getTxnAmountwithCharges();
																earMarkupdte.setStrEarAmount(earMarkAmnt);
																accountService.updateEarMark(earMarkupdte);

																AccountTranMaster accountTranMaster = new AccountTranMaster();
																accountTranMaster.setStrParticipantId(processWebResponse.getStrParticipantId());
																accountTranMaster.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
																accountTranMaster.setStrTxn_id(processWebResponse.getTxnId());
																accountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
																accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());
																accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
																accountTranMaster.setStrResponseCode("00");
																accountTranMaster.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
																accountTranMaster.setStrTransaction_amount(processWebResponse.getTransactionRequest().getStrTxnAmount());
																accountTranMasterService.addAccountTransactionData(accountTranMaster);

																AccountTranMaster accountTranMasterFee = new AccountTranMaster();
																accountTranMasterFee.setStrParticipantId(processWebResponse.getStrParticipantId());
																accountTranMasterFee.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
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
																accountTranMasterGst.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
																accountTranMasterGst.setStrTxn_id(processWebResponse.getTxnId());
																accountTranMasterGst.setSwitchTxDate(Utils.getCurrentDate());
																accountTranMasterGst.setStrLocal_tran_date(Utils.getCurrentDate());
																accountTranMasterGst.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
																accountTranMasterGst.setStrResponseCode("00");
																accountTranMasterGst.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
																accountTranMasterGst.setStrTransaction_amount(processWebResponse.getGstAmount()+"");
																accountTranMasterService.addAccountTransactionData(accountTranMasterGst);

																CurrencyTransferMaster currencyTransferdetails= new CurrencyTransferMaster();
																currencyTransferdetails.setTranId(processWebResponse.getTxnId());
																List<CurrencyTransferMaster> currencyTransferList = currencyTransferMasterService.getCurrencyMasterByTranId(currencyTransferdetails);

																processWebResponse.setRemaingAmount(0);
																prsWebResponse.setCode("S0000");
																prsWebResponse.setMessage("Succesfully Transfered.");
																prsWebResponse.setStatus("Success.");
																prsWebResponse.setIsCurrencyTransfer("true");
																prsWebResponse.setCurrencyTransferMasterList(currencyTransferList);
																
																
															}
														}
													}
													break;
												}
												else
												{
													basePriority++;
												}
											}
										}
									}
									if(processWebResponse.getRemaingAmount() > 0)
									{
										prsWebResponse.setMessage("Insufficient Balance.");
										prsWebResponse.setCode("E0000");
										prsWebResponse.setStatus("Failed");
										return ResponseEntity.ok(prsWebResponse);
									}
								}
							}
							else if(transactionRequest.getStrCurrency().equalsIgnoreCase(multiCurrencyWalletAccountMaster.getStrCurrencyCode()) && 1 != multiCurrencyWalletAccountMaster.getStrPriority())
							{
								List<MultiCurrencyWalletAccountMaster> updatedMultiCurrencyWalletAccountMasterList = new ArrayList<MultiCurrencyWalletAccountMaster>();
								int previousPos = multiCurrencyWalletAccountMaster.getStrPriority();
								multiCurrencyWalletAccountMaster.setStrPriority(1);

								for(MultiCurrencyWalletAccountMaster multicurrency :multiCurrencyWalletAccountMasterList)
								{
									if(!transactionRequest.getStrCurrency().equalsIgnoreCase(multicurrency.getStrCurrencyCode()))
									{
										if(multicurrency.getStrPriority() < previousPos)
										{
											int updatedPosition = multicurrency.getStrPriority() + 1;
											multicurrency.setStrPriority(updatedPosition);
										}
									}
									updatedMultiCurrencyWalletAccountMasterList.add(multicurrency);
								}
								System.out.println("UpdatedMultiCurrencyWalletAccountMasterList:::"+updatedMultiCurrencyWalletAccountMasterList.toString());
								processWebResponse.setBaseCurrencyWalletMaster(multiCurrencyWalletAccountMaster);
								CurrencyMaster currencyMaster = new CurrencyMaster();
								currencyMaster.setCurrencyCode(transactionRequest.getStrCurrency());

								currencyMaster = currencyMasterService.getCurrencyDetailByCurrencyCode(currencyMaster);
								currencyMaster = currencyMasterService.getCurrencyDetailByCurrencyCode(currencyMaster);
								double feeAmount = 0;
								if("Y".equals(currencyMaster.getStrFlatFee()) )
								{
									feeAmount = currencyMaster.getFeeAmount();
								}

								if("Y".equalsIgnoreCase(currencyMaster.getStrIsFee()))
								{
									feeAmount = ((Utils.stringToDouble(currencyMaster.getStrFeePercentage())/100)) * Utils.stringToDouble(transactionRequest.getStrTxnAmount());
									currencyMaster.setFeeAmount(feeAmount);
								}
								processWebResponse.setCurrencyMaster(currencyMaster);

								double gstAmount = (currencyMaster.getGstPercentage()/100) * feeAmount;
								processWebResponse.setFeeAmount(feeAmount);
								processWebResponse.setGstAmount(gstAmount);

								double updatedTxnAmount = Utils.stringToDouble(transactionRequest.getStrTxnAmount()) + feeAmount + gstAmount;
								processWebResponse.setTxnAmountwithCharges(updatedTxnAmount);

								if( multiCurrencyWalletAccountMaster.getStrClosingBalance() > updatedTxnAmount)
								{
									MultiCurrencyWalletAccountMaster earMarkUpdate = new MultiCurrencyWalletAccountMaster();
									earMarkUpdate.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
									earMarkUpdate.setStrAccountNumber(multiCurrencyWalletAccountMaster.getStrAccountNumber());
									double earMarkAmount = multiCurrencyWalletAccountMaster.getStrEarAmount() + updatedTxnAmount;
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

									processWebResponse.setAmount(Utils.stringToDouble(processWebResponse.getTransactionRequest().getStrTxnAmount()));
									GLAccountTypeMaster linkedGl = transactionHandler.updateLinkedGl(processWebResponse);
									processWebResponse.setCurrencyGl(linkedGl);
									transactionHandler.addGLAccountStatment(processWebResponse);
									processWebResponse.setAmount(processWebResponse.getFeeAmount());
									GLAccountTypeMaster linkedGlFee = transactionHandler.updateLinkedGl(processWebResponse);
									processWebResponse.setCurrencyGl(linkedGlFee);
									transactionHandler.addGLAccountStatment(processWebResponse);
									processWebResponse.setAmount(processWebResponse.getGstAmount());
									GLAccountTypeMaster linkedGlGst = transactionHandler.updateLinkedGl(processWebResponse);
									processWebResponse.setCurrencyGl(linkedGlGst);
									transactionHandler.addGLAccountStatment(processWebResponse);

									MultiCurrencyWalletAccountMaster walletAccountMaster = new MultiCurrencyWalletAccountMaster();
									walletAccountMaster.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
									walletAccountMaster.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
									walletAccountMaster = accountService.getCurrencyAccount(walletAccountMaster);

									MultiCurrencyWalletAccountMaster earMarkRelease = new MultiCurrencyWalletAccountMaster();
									earMarkRelease.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
									earMarkRelease.setStrAccountNumber(multiCurrencyWalletAccountMaster.getStrAccountNumber());
									double earMark = walletAccountMaster.getStrEarAmount() - updatedTxnAmount;
									earMarkUpdate.setStrEarAmount(earMark);
									accountService.updateEarMark(earMarkRelease);

									AccountTranMaster accountTranMaster = new AccountTranMaster();
									accountTranMaster.setStrParticipantId(processWebResponse.getStrParticipantId());
									accountTranMaster.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
									accountTranMaster.setStrTxn_id(processWebResponse.getTxnId());
									accountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
									accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());
									accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
									accountTranMaster.setStrResponseCode("00");
									accountTranMaster.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
									accountTranMaster.setStrTransaction_amount(processWebResponse.getTransactionRequest().getStrTxnAmount());
									accountTranMasterService.addAccountTransactionData(accountTranMaster);

									AccountTranMaster accountTranMasterFee = new AccountTranMaster();
									accountTranMasterFee.setStrParticipantId(processWebResponse.getStrParticipantId());
									accountTranMasterFee.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
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
									accountTranMasterGst.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
									accountTranMasterGst.setStrTxn_id(processWebResponse.getTxnId());
									accountTranMasterGst.setSwitchTxDate(Utils.getCurrentDate());
									accountTranMasterGst.setStrLocal_tran_date(Utils.getCurrentDate());
									accountTranMasterGst.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
									accountTranMasterGst.setStrResponseCode("00");
									accountTranMasterGst.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
									accountTranMasterGst.setStrTransaction_amount(processWebResponse.getGstAmount()+"");
									accountTranMasterService.addAccountTransactionData(accountTranMasterGst);

									prsWebResponse.setCode("S0000");
									prsWebResponse.setMessage("Succesfully Transfered.");
									prsWebResponse.setStatus("Success.");
									prsWebResponse.setIsCurrencyTransfer("false");
								}
								else
								{
									double remainingAmount = updatedTxnAmount - multiCurrencyWalletAccountMaster.getStrClosingBalance();
									String remainingAmountCurrency = transactionRequest.getStrCurrency();

									processWebResponse.setRemaingAmount(remainingAmount);
									processWebResponse.setRemaingAmountCurrency(remainingAmountCurrency);

									int basePriority = 2;
									for(MultiCurrencyWalletAccountMaster multicurrency :multiCurrencyWalletAccountMasterList)
									{
										if(!multicurrency.getStrCurrencyCode().equalsIgnoreCase(processWebResponse.getTransactionRequest().getStrCurrency()))
										{

											processWebResponse.setMultiCurrencyWalletAccountMaster(multicurrency);
											if( basePriority ==  multicurrency.getStrPriority() )
											{
												processWebResponse.setToMultiCurrencyWalletAccountMaster(multicurrency);
												processWebResponse = currencyConvertAndTransfer.currencyConvertAndTransferToWallet(processWebResponse);
												if(processWebResponse.getRemaingAmount() == 0)
												{
													for(MultiCurrencyWalletAccountMaster multicurrencyBalanceAdd:multiCurrencyWalletAccountMasterList)
													{
														if(remainingAmountCurrency.equalsIgnoreCase(multicurrencyBalanceAdd.getStrCurrencyCode()))
														{
															HashMap<String, CurrencyConversionMaster> currencyConversionMap = processWebResponse.getCurrencyConversionDetailMap();
															double updatedBalance = multicurrencyBalanceAdd.getStrClosingBalance() + remainingAmount;
															multicurrencyBalanceAdd.setStrClosingBalance(updatedBalance);
															multicurrencyBalanceAdd.setStrAccountNumber(processWebResponse.getTransactionRequest().getStrAccountNumber());
															multicurrencyBalanceAdd.setStrCurrencyCode(remainingAmountCurrency);
															if(currencyConversionMap.size() != 1)
															{
																int i = transactionHandler.addClosingBalanceInWallet(multicurrencyBalanceAdd); 
															}
															processWebResponse.setRemaingAmount(remainingAmount);
															processWebResponse.setMultiCurrencyWalletAccountMaster(multicurrencyBalanceAdd);
															if(currencyConversionMap.size() != 1)
															{
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
															}
															processWebResponse.setRemaingAmount(0);
															//		if(i > 0)
															{

																MultiCurrencyWalletAccountMaster multiCurrencyWalletAccount = new MultiCurrencyWalletAccountMaster();
																multiCurrencyWalletAccount.setStrAccountNumber(processWebResponse.getBaseCurrencyWalletMaster().getStrAccountNumber());
																multiCurrencyWalletAccount.setStrCurrencyCode(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyCode());

																multiCurrencyWalletAccount = accountService.getCurrencyAccount(multiCurrencyWalletAccount);

																CurrencyMaster currencyMasterDetail = new CurrencyMaster();
																currencyMasterDetail.setCurrencyCode(transactionRequest.getStrCurrency());

																currencyMasterDetail = currencyMasterService.getCurrencyDetailByCurrencyCode(currencyMasterDetail);

																double feeAmountReq = 0;
																if("Y".equals(currencyMaster.getStrFlatFee()) )
																{
																	feeAmountReq = currencyMasterDetail.getFeeAmount();
																}

																if("Y".equalsIgnoreCase(currencyMaster.getStrIsFee()))
																{
																	feeAmountReq = ((Utils.stringToDouble(currencyMaster.getStrFeePercentage())/100)) * Utils.stringToDouble(transactionRequest.getStrTxnAmount());
																	currencyMasterDetail.setFeeAmount(feeAmountReq);
																}


																processWebResponse.setCurrencyMaster(currencyMaster);

																double gstAmountReq = (currencyMasterDetail.getGstPercentage()/100) * feeAmountReq;
																processWebResponse.setFeeAmount(feeAmountReq);
																processWebResponse.setGstAmount(gstAmountReq);
																processWebResponse.getTransactionRequest().setStrTxnAmount(txnAmount+"");
																double updatedTxnAmountReq = txnAmount + feeAmount + gstAmount;
																processWebResponse.setTxnAmountwithCharges(updatedTxnAmountReq);
																processWebResponse.setMultiCurrencyWalletAccountMaster(multiCurrencyWalletAccount);

																MultiCurrencyWalletAccountMaster earMarkupdt = new MultiCurrencyWalletAccountMaster();
																earMarkupdt.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
																earMarkupdt.setStrAccountNumber(multiCurrencyWalletAccountMaster.getStrAccountNumber());
																double earMarkAmt = multiCurrencyWalletAccountMaster.getStrEarAmount() - updatedTxnAmountReq;
																earMarkupdt.setStrEarAmount(earMarkAmt);
																accountService.updateEarMark(earMarkupdt);

																if(currencyConversionMap.size() != 1)
																{
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
																}
																processWebResponse.setAmount(Utils.stringToDouble(processWebResponse.getTransactionRequest().getStrTxnAmount()));
																GLAccountTypeMaster linkedGl = transactionHandler.updateLinkedGl(processWebResponse);
																processWebResponse.setCurrencyGl(linkedGl);
																transactionHandler.addGLAccountStatment(processWebResponse);
																processWebResponse.setAmount(processWebResponse.getFeeAmount());
																GLAccountTypeMaster linkedGlFee = transactionHandler.updateLinkedGl(processWebResponse);
																processWebResponse.setCurrencyGl(linkedGlFee);
																transactionHandler.addGLAccountStatment(processWebResponse);
																processWebResponse.setAmount(processWebResponse.getGstAmount());
																GLAccountTypeMaster linkedGlGst = transactionHandler.updateLinkedGl(processWebResponse);
																processWebResponse.setCurrencyGl(linkedGlGst);
																transactionHandler.addGLAccountStatment(processWebResponse);

																//			HashMap<String, CurrencyConversionMaster> currencyConversionMap = processWebResponse.getCurrencyConversionDetailMap();
																CurrencyConversionMaster currencyConnversionMap = new CurrencyConversionMaster();
																if(currencyConversionMap.size() == 1)
																{
																	for(Map.Entry<String, CurrencyConversionMaster> remaingCurencyMap : currencyConversionMap.entrySet())
																	{
																		currencyConnversionMap = remaingCurencyMap.getValue();
																	}
																}
																else
																{
																	currencyConnversionMap = currencyConversionMap.get(remainingAmountCurrency+"_"+processWebResponse.getLastSweepInCurrencyCode());
																}

																if(currencyConversionMap.size() != 1)
																{
																	CurrencyTransferMaster currencyTransfer= new CurrencyTransferMaster();
																	currencyTransfer.setTranId(processWebResponse.getTxnId());
																	currencyTransfer.setSweepOutAmount(currencyConnversionMap.getToCurrencyConversion()*remainingAmount+"");
																	currencyTransfer.setSweepOutCurrencyValue(String.valueOf(currencyConnversionMap.getToCurrencyConversion()));
																	currencyTransfer.setSweepOutCurrencyCode(processWebResponse.getLastSweepInCurrencyCode());
																	currencyTransfer.setSweepInAmount(remainingAmount+"");
																	currencyTransfer.setSweepInCurrencyCode(remainingAmountCurrency);
																	currencyTransfer.setBaseAccountNumber(transactionRequest.getStrAccountNumber());
																	processWebResponse.setLastSweepInCurrencyCode(remainingAmountCurrency);
																	currencyTransfer.setBaseAccountType(processWebResponse.getAccountCreation().getStrAccountType());
																	currencyTransfer.setFeeAmount( processWebResponse.getFeeAmount());
																	currencyTransfer.setGstAmount(processWebResponse.getGstAmount());
																	currencyTransferMasterService.addEntryInCurrencyTransferMaster(currencyTransfer);
																	
																	MultiCurrencyChargesReport multiCurrencyGstReport = new MultiCurrencyChargesReport();
																	multiCurrencyGstReport.setAccountCurrency(processWebResponse.getLastSweepInCurrencyCode());
																	multiCurrencyGstReport.setAccountNumber(transactionRequest.getStrAccountNumber());
																	multiCurrencyGstReport.setAccountType(processWebResponse.getAccountCreation().getStrAccountType());
																	multiCurrencyGstReport.setChargesType(CommonConstants.FEE_TYPE);
														//			multiCurrencyGstReport.setGlAccountNumber(processWebResponse.getStrAccountNumber());
														//			multiCurrencyGstReport.setGlAccountType(processWebResponse.getStrGLAccountType());
																	multiCurrencyGstReport.setParticipantId(processWebResponse.getStrParticipantId());
																	multiCurrencyGstReport.setTxnAmt(processWebResponse.getFeeAmount());
																	multiCurrencyGstReport.setTxnDate(Utils.getCurrentDate());
																	multiCurrencyGstReport.setTxnId(txnId);
																	multiCurrencyGstReport.setTxnType(processWebResponse.getTxnType());
																	multiCurrencyGstReport.setBaseConversionRate(currencyConnversionMap.getToCurrencyConversion());
														//			multiCurrencyGstReport.setBaseCurrency(processWebResponse.getBaseWalletAccount().getCurrencyCode());
																	multiCurrencyGstReport.setBaseCurrencyAmt(processWebResponse.getGstAmount());
																	multiCurrencyChargesReportService.saveMultiCurrencyChargeReport(multiCurrencyGstReport);
																	
																	MultiCurrencyChargesReport multiCurrencyFeeReport = new MultiCurrencyChargesReport();
																	multiCurrencyFeeReport.setAccountCurrency(processWebResponse.getLastSweepInCurrencyCode());
																	multiCurrencyFeeReport.setAccountNumber(transactionRequest.getStrAccountNumber());
																	multiCurrencyFeeReport.setAccountType(processWebResponse.getAccountCreation().getStrAccountType());
																	multiCurrencyFeeReport.setChargesType(CommonConstants.FEE_TYPE);
														//			multiCurrencyGstReport.setGlAccountNumber(processWebResponse.getStrAccountNumber());
														//			multiCurrencyGstReport.setGlAccountType(processWebResponse.getStrGLAccountType());
																	multiCurrencyFeeReport.setParticipantId(processWebResponse.getStrParticipantId());
																	multiCurrencyFeeReport.setTxnAmt(processWebResponse.getFeeAmount());
																	multiCurrencyFeeReport.setTxnDate(Utils.getCurrentDate());
																	multiCurrencyFeeReport.setTxnId(txnId);
																	multiCurrencyFeeReport.setTxnType(processWebResponse.getTxnType());
																	multiCurrencyFeeReport.setBaseConversionRate(currencyConnversionMap.getToCurrencyConversion());
														//			multiCurrencyFeeReport.setBaseCurrency(processWebResponse.getBaseWalletAccount().getCurrencyCode());
																	multiCurrencyFeeReport.setBaseCurrencyAmt(processWebResponse.getGstAmount());
																	multiCurrencyChargesReportService.saveMultiCurrencyChargeReport(multiCurrencyGstReport);
																	
																}
																MultiCurrencyWalletAccountMaster earMarkupdte = new MultiCurrencyWalletAccountMaster();
																earMarkupdte.setStrCurrencyCode(multiCurrencyWalletAccountMaster.getStrCurrencyCode());
																earMarkupdte.setStrAccountNumber(multiCurrencyWalletAccountMaster.getStrAccountNumber());
																double earMarkAmnt = multiCurrencyWalletAccountMaster.getStrEarAmount() - updatedTxnAmountReq;
																earMarkupdte.setStrEarAmount(earMarkAmnt);
																accountService.updateEarMark(earMarkupdte);



																AccountTranMaster accountTranMaster = new AccountTranMaster();
																accountTranMaster.setStrParticipantId(processWebResponse.getStrParticipantId());
																accountTranMaster.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
																accountTranMaster.setStrTxn_id(processWebResponse.getTxnId());
																accountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
																accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());
																accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
																accountTranMaster.setStrResponseCode("00");
																accountTranMaster.setStrTran_type(transactionRequest.getStrTxnType()); // change to type
																accountTranMaster.setStrTransaction_amount(processWebResponse.getTransactionRequest().getStrTxnAmount());
																accountTranMasterService.addAccountTransactionData(accountTranMaster);

																AccountTranMaster accountTranMasterFee = new AccountTranMaster();
																accountTranMasterFee.setStrParticipantId(processWebResponse.getStrParticipantId());
																accountTranMasterFee.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
																accountTranMasterFee.setStrTxn_id(processWebResponse.getTxnId());
																accountTranMasterFee.setSwitchTxDate(Utils.getCurrentDate());
																accountTranMasterFee.setStrLocal_tran_date(Utils.getCurrentDate());
																accountTranMasterFee.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
																accountTranMasterFee.setStrResponseCode("00");
																accountTranMasterFee.setStrTran_type(transactionRequest.getStrTxnType()); // change to type
																accountTranMasterFee.setStrTransaction_amount(processWebResponse.getFeeAmount()+"");
																accountTranMasterService.addAccountTransactionData(accountTranMasterFee);

																AccountTranMaster accountTranMasterGst = new AccountTranMaster();
																accountTranMasterGst.setStrParticipantId(processWebResponse.getStrParticipantId());
																accountTranMasterGst.setStrFrom_account_number(processWebResponse.getBaseCurrencyWalletMaster().getStrCurrencyWalletAccountNumber());
																accountTranMasterGst.setStrTxn_id(processWebResponse.getTxnId());
																accountTranMasterGst.setSwitchTxDate(Utils.getCurrentDate());
																accountTranMasterGst.setStrLocal_tran_date(Utils.getCurrentDate());
																accountTranMasterGst.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
																accountTranMasterGst.setStrResponseCode("00");
																accountTranMasterGst.setStrTran_type(processWebResponse.getTransactionRequest().getStrTxnType()); // change to type
																accountTranMasterGst.setStrTransaction_amount(processWebResponse.getGstAmount()+"");
																accountTranMasterService.addAccountTransactionData(accountTranMasterGst);

																CurrencyTransferMaster currencyTransferdetails= new CurrencyTransferMaster();
																currencyTransferdetails.setTranId(processWebResponse.getTxnId());
																List<CurrencyTransferMaster> currencyTransferList = currencyTransferMasterService.getCurrencyMasterByTranId(currencyTransferdetails);

																processWebResponse.setRemaingAmount(0);
																prsWebResponse.setCode("S0000");
																prsWebResponse.setMessage("Succesfully Transfered.");
																prsWebResponse.setStatus("Success.");
																prsWebResponse.setIsCurrencyTransfer("true");
																prsWebResponse.setCurrencyTransferMasterList(currencyTransferList);
															}
														}
													}
													break;
												}
												else
												{
													basePriority++;
												}
											}
										}
									}
									if(processWebResponse.getRemaingAmount() > 0)
									{
										prsWebResponse.setCode("E0000");
										prsWebResponse.setMessage("Insufficient Balance.");
										prsWebResponse.setStatus("Failed");
										return ResponseEntity.ok( prsWebResponse);
									}
								}
							}

						}
					}
					else
					{
						prsWebResponse.setCode("E0000");
						prsWebResponse.setMessage("Currency Account Not Supported.");
						prsWebResponse.setStatus("Failed");
						return ResponseEntity.ok( prsWebResponse);
					}
				}
				else
				{
					prsWebResponse.setCode("E0000");
					prsWebResponse.setMessage("Account Number not Found");
					prsWebResponse.setStatus("Failed");
					return ResponseEntity.ok(prsWebResponse);
				}

			//3
			return ResponseEntity.ok(prsWebResponse);
		}
		catch (Exception e)
		{
			prsWebResponse.setCode("E0000");
			prsWebResponse.setStatus("Failed");
			System.out.println("Exception:::::::::::::::::::"+e.getMessage());
		}
		return ResponseEntity.ok(prsWebResponse);
	}
}
