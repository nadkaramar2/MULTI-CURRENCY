package ams.cms.services.impl;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.handler.TransactionHandlerAPI;
import ams.cms.api.model.AccountMaster;
import ams.cms.config.CommonConstants;
import ams.cms.config.TransactionConfig;
import ams.cms.dao.GLAccountTypeMasterDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AcTypeLrsTcsMaster;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountStatement;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.AccountTypeMaster;
import ams.cms.model.Channels;
import ams.cms.model.CurrencyConversionMaster;
import ams.cms.model.CurrencyMaster;
import ams.cms.model.CurrencyTransferMaster;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.GstTypeMaster;
import ams.cms.model.LoadMoneyAccountingRequest;
import ams.cms.model.LoadMoneyPayStructure;
import ams.cms.model.LoadMoneyRequest;
import ams.cms.model.LoadMoneyTransactionResponse;
import ams.cms.model.LoadMoneyTxnDetails;
import ams.cms.model.MultiCurrencyChargesReport;
import ams.cms.model.MultiCurrencyFeeTypeMaster;
import ams.cms.model.MultiCurrencyFinancialYearMaster;
import ams.cms.model.MultiCurrencyWalletAccountMaster;
import ams.cms.model.MultiCurrencyWalletAccountTypeMaster;
import ams.cms.model.TcsTypeMaster;
import ams.cms.model.TransactionTypeModel;
import ams.cms.model.TransaferCurrencyRequest;
import ams.cms.services.AcTypeLrsTcsMasterService;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountStatementService;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.AccountTypeMasterService;
import ams.cms.services.ChannelsService;
import ams.cms.services.CurrencyConversionMasterService;
import ams.cms.services.CurrencyMasterService;
import ams.cms.services.CurrencyTransferMasterService;
import ams.cms.services.GLAccountStatementService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.services.GstTypeMasterService;
import ams.cms.services.LoadMoneyService;
import ams.cms.services.LoadMoneyTxnDetailsService;
import ams.cms.services.MultiCurrencyChargesReportService;
import ams.cms.services.MultiCurrencyFeeTypeMasterService;
import ams.cms.services.MultiCurrencyFinancialYearMasterService;
import ams.cms.services.MultiCurrencyWalletAccountService;
import ams.cms.services.MultiCurrencyWalletAccountTypeService;
import ams.cms.services.TcsTypeMasterService;
import ams.cms.services.TransactionCurrencyMasterService;
import ams.cms.services.TransactionIdService;
import ams.cms.services.TransactionTypeService;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.Utils;

@Transactional
@Service
public class LoadMoneyServiceImpl implements LoadMoneyService{
	
	private AMSLogger amsLogger = AMSLogger.getInstance(LoadMoneyServiceImpl.class);

	
	@Autowired
	private AcTypeLrsTcsMasterService acTypeLrsTcsMasterService;
	
	@Autowired
	private MultiCurrencyWalletAccountService multiCurrencyWalletAccountService;
	
	@Autowired
	private MultiCurrencyFinancialYearMasterService multiCurrencyFinancialYearMasterService;
	
	@Autowired
	private GLAccountTypeMasterService glAccountTypeMasterService;
	
	
	@Autowired
	private CurrencyConversionMasterService conversionMasterService;
	
	@Autowired
	private LoadMoneyTxnDetailsService loadMoneyTxnDetailsService;
	
	@Autowired
	private GLAccountTypeMasterDao glAccountTypeMasterDao;
	
	@Autowired
	private TransactionIdService transactionIdService;
	
	@Autowired
	private MultiCurrencyChargesReportService multiCurrencyChargesReportService;
	
	
	@Autowired
	private MultiCurrencyWalletAccountTypeService multiCurrencyWalletAccountTypeService;
	
	@Autowired
	private AccountTypeMasterService accountTypeMasterService;
	
	@Autowired
	private TransactionHandlerAPI transactionHandlerAPI;
	
	@Autowired
	private ChannelsService channelsService;
	
	@Autowired
	private CurrencyMasterService currencyMasterService;
	
	@Autowired
	private GLAccountStatementService glAccountStatementService;
	
	@Autowired
	private AccountStatementService accountStatementService;
	
	@Autowired
	private AccountTranMasterService accountTranMasterService;
	
	@Autowired
	private AccountMasterService accountMasterService;
	
	@Autowired
	private TransactionCurrencyMasterService transactionCurrencyMasterService;
	
	@Autowired
	private MultiCurrencyFeeTypeMasterService multiCurrencyFeeTypeMasterService;
	
	@Autowired
	private GstTypeMasterService gstTypeMasterService;
	
	@Autowired
	private TcsTypeMasterService tcsTypeMasterService;
	
	@Autowired
	private CurrencyTransferMasterService currencyTransferMasterService;
	
	@Autowired
	private TransactionTypeService transactionTypeService;
	
	DecimalFormat formatter = new DecimalFormat("#0.00"); 
	
	@Override
	public ProcessResponse multiCurrencyLoadMoneyTxn(LoadMoneyRequest loadMoneyRequest) {
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			 double feeAmt = 0.00 ;
			 double gstAmt = 0.00;
			 double tcsAmt = 0.00;
			 amsLogger.writeExceptionLog("LoadMoney  PIN CHECK::"+loadMoneyRequest);
		
			 //check For Pin
			 AccountCreation accountCreation = new AccountCreation();
			 accountCreation.setStrCustPin(loadMoneyRequest.getPin());
			 accountCreation.setStrCustId(loadMoneyRequest.getCustId());
			 TransactionConfig transactionHandlerAPIObj = transactionHandlerAPI.verifyCustomerPin(accountCreation);
				
			if(transactionHandlerAPIObj.getCode().equalsIgnoreCase("S0000")) {
			
		if(loadMoneyRequest.getBaseWallet() != null)
		{	
			
			
			MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster = new MultiCurrencyWalletAccountMaster();
			
		// Check  Base Wallet is There Or Not In Table.----->> AccountMaster By AccountNumber
		AccountMaster baseWallet = new AccountMaster();
		baseWallet.setStrAccountNumber(loadMoneyRequest.getBaseWallet());
		 baseWallet = accountMasterService.getBaseWalletAccount(baseWallet);
		if(baseWallet != null && baseWallet.getStrAccountNumber() != null) 
		{
			// Check  For This Base Wallet "isMultiCurrencySupported"--> "Y"/"N"
			AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
			accountTypeMaster.setStrAccountType(baseWallet.getStrAccountType());
			accountTypeMaster = accountTypeMasterService.getAccountTypeObject(accountTypeMaster);
			if("Y".equalsIgnoreCase(accountTypeMaster.getIsMultiCurrencySupport())) {
				
				//Check for isLRS Applied
				if("Y".equalsIgnoreCase(accountTypeMaster.getIsLrs())) {
					
				//Check for Channel is COnfiger Or Not 
				//Get TCS And LRS Values Based On Account Type
				AcTypeLrsTcsMaster acTypeLrsTcsMaster = new AcTypeLrsTcsMaster();
				if("Y".equalsIgnoreCase(accountTypeMaster.getIsChannel())) {
					
					if(loadMoneyRequest.getChannel() != null) {
						
						acTypeLrsTcsMaster.setStrAccountType(baseWallet.getStrAccountType());
						acTypeLrsTcsMaster.setStrChannelCode(loadMoneyRequest.getChannel());
					}else {
						acTypeLrsTcsMaster.setStrAccountType(baseWallet.getStrAccountType());
						acTypeLrsTcsMaster.setStrChannelCode("NB");
						loadMoneyRequest.setChannel("NB");
					}
					
				}else {
					acTypeLrsTcsMaster.setStrAccountType(baseWallet.getStrAccountType());	
					acTypeLrsTcsMaster.setStrChannelCode("NB");
					loadMoneyRequest.setChannel("NB");
				}	
				
				acTypeLrsTcsMaster = acTypeLrsTcsMasterService.getAccountTypeLrsAndTcs(acTypeLrsTcsMaster);
				
				if(acTypeLrsTcsMaster != null)
				{
				
				
					//Get Wallet Account By Currency Code--------------->  [CURRENCY WALLET]
					multiCurrencyWalletAccountMaster.setStrAccountNumber(baseWallet.getStrAccountNumber());
					//multiCurrencyWalletAccountMaster.setStrAccountType(baseWallet.getStrAccountType());
					multiCurrencyWalletAccountMaster.setBaseCurrencyAccountType(baseWallet.getStrAccountType());
					
					multiCurrencyWalletAccountMaster.setStrCurrencyCode(loadMoneyRequest.getCurrencyCode());
					MultiCurrencyWalletAccountMaster currencyWallet = multiCurrencyWalletAccountService.getMultiCurrencyWalletAccount(multiCurrencyWalletAccountMaster);
					if(currencyWallet != null && currencyWallet.getStrCurrencyWalletAccountNumber() != null)
					{
						//Find Currency Master For TCS and Gst Calculation
						CurrencyMaster currencyMaster = new CurrencyMaster();
						currencyMaster.setCurrencyCode(baseWallet.getCurrencyCode());
						currencyMaster = currencyMasterService.getCurrencyMasterByCurrencyCode(currencyMaster);
						if(currencyMaster !=null ) {
							
							//check the Currency is Base Currency Or Not 
							if("Y".equalsIgnoreCase(currencyMaster.getBaseCountry())) {
		
						//Convert LoadedMoney To INR Value	  
						CurrencyConversionMaster currencyConversionMaster = new CurrencyConversionMaster();
						currencyConversionMaster.setFromCurrency(currencyWallet.getStrCurrencyCode());
						currencyConversionMaster.setToCurrency(baseWallet.getCurrencyCode());
						currencyConversionMaster = conversionMasterService.getCurrencyConverionValue(currencyConversionMaster);
						if(currencyConversionMaster != null)
						{
							
							double convertedINRValue =  Double.parseDouble(String.valueOf(formatter.format(loadMoneyRequest.getLoadAmount() * currencyConversionMaster.getFromCurrencyValue()) ));
							
							//FEE And GST Calculation Start
							MultiCurrencyFeeTypeMaster multiCurrencyFeeTypeMaster = new MultiCurrencyFeeTypeMaster();
							multiCurrencyFeeTypeMaster.setFeeType(currencyMaster.getFeeType());
							multiCurrencyFeeTypeMaster =multiCurrencyFeeTypeMasterService.getFeeTypeDetails(multiCurrencyFeeTypeMaster);
							
							if(multiCurrencyFeeTypeMaster != null)
							{
								
							//Getting GstType Based On Fee Type
								GstTypeMaster gstTypeMaster = new GstTypeMaster();
								gstTypeMaster.setGstType(multiCurrencyFeeTypeMaster.getGstType());
								gstTypeMaster = gstTypeMasterService.getGstTypeMasterByGstType(gstTypeMaster);
								if(gstTypeMaster != null) {
							
								if("Y".equalsIgnoreCase(multiCurrencyFeeTypeMaster.getIsFlatFee()))
								{
							
									feeAmt =  multiCurrencyFeeTypeMaster.getFeeAmt();
									gstAmt = Double.parseDouble(String.valueOf(formatter.format(feeAmt  * gstTypeMaster.getGstPercentage() / 100)));
							
								}
								else if("Y".equalsIgnoreCase(multiCurrencyFeeTypeMaster.getIsPercentageFee())) 
								{
							
									feeAmt =  Double.parseDouble(String.valueOf(formatter.format(convertedINRValue * gstTypeMaster.getGstPercentage())));
									gstAmt = Double.parseDouble(String.valueOf(formatter.format(feeAmt  * gstTypeMaster.getGstPercentage() / 100)));
								}
								//FEE And GST Calculation End
			
									double lrsLimitBasedOnAccountType = acTypeLrsTcsMaster.getStrLrs();
									double tcsPerecentage = acTypeLrsTcsMaster.getStrTcs();
		  						
									String financialYear ;
									String nextYear;
									
									int CurrentMonth = Calendar.getInstance().get(Calendar.MONTH);
									if(CurrentMonth==3) {
									
										 financialYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
										 nextYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR) + 1);	
										
									}else {
										 financialYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - 1);
										 nextYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
									}
									
									//Checking LRS Limit  Of the year
									MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMaster = new MultiCurrencyFinancialYearMaster();
									multiCurrencyFinancialYearMaster.setAccountNumber(baseWallet.getStrAccountNumber());
									multiCurrencyFinancialYearMaster.setFinancialYear(String.valueOf(financialYear.substring(2)+"-"+nextYear.substring(2)));
									multiCurrencyFinancialYearMaster.setChannelCode(acTypeLrsTcsMaster.getStrChannelCode());
									multiCurrencyFinancialYearMaster.setAccountType(baseWallet.getStrAccountType());
									multiCurrencyFinancialYearMaster = multiCurrencyFinancialYearMasterService.getFinancialYearNyAccountNumber(multiCurrencyFinancialYearMaster);
									
									if(multiCurrencyFinancialYearMaster != null)
									{
							
										double totalAmt = multiCurrencyFinancialYearMaster.getTotalLoaded() + convertedINRValue;
										double loadAmt = convertedINRValue;
										double balanceLRS = lrsLimitBasedOnAccountType;
										double totalLRSConsumed =0;
										double totalLoaded =multiCurrencyFinancialYearMaster.getTotalLoaded();
										double totalExcessLoading =0;
										double tcsOn =0;
										double totalTcsOn =0;
										String totalAmountWithCharges ;
										if(multiCurrencyFinancialYearMaster.getAvailableLrsLimit()>0) {
											balanceLRS = multiCurrencyFinancialYearMaster.getAvailableLrsLimit();
										}
										
										// 1 Part - if LRS is Breached Or not 
										if( totalAmt > lrsLimitBasedOnAccountType) 
										{	 
											// LRS is BREACHED.....
							  
											// Check is TCS Applied Previously or Not 	
											if(multiCurrencyFinancialYearMaster.getTotalTcsOn()>0)
											{
												totalTcsOn = multiCurrencyFinancialYearMaster.getTotalTcsOn();
												loadAmt = convertedINRValue;
												balanceLRS = 0 ;
												totalLRSConsumed = lrsLimitBasedOnAccountType ;    //LoadAmt + PReviouseLoadAmt
												totalLoaded =  Double.parseDouble(String.valueOf(formatter.format(totalLoaded + loadAmt))) ;
							  
												totalExcessLoading = Double.parseDouble(String.valueOf(formatter.format(totalLoaded  - lrsLimitBasedOnAccountType)));
												tcsOn = Double.parseDouble(String.valueOf(formatter.format(totalExcessLoading -multiCurrencyFinancialYearMaster.getTotalTcsOn())));
												totalTcsOn = Double.parseDouble(String.valueOf(formatter.format(totalTcsOn + tcsOn)));
								  
												tcsAmt = Double.parseDouble(String.valueOf(formatter.format(tcsOn * tcsPerecentage / 100)));
												double totalAmount = Double.parseDouble(String.valueOf(formatter.format(convertedINRValue + feeAmt + tcsAmt + gstAmt)));
												totalAmountWithCharges = formatter.format(totalAmount);
												
												LoadMoneyAccountingRequest loadMoneyAccountingRequest = new LoadMoneyAccountingRequest();
												loadMoneyAccountingRequest.setBaseWalletAccount(baseWallet);
												loadMoneyAccountingRequest.setCurrencyWalletAccount(currencyWallet);
												loadMoneyAccountingRequest.setMultiCurrencyFeeTypeMaster(multiCurrencyFeeTypeMaster);
												loadMoneyAccountingRequest.setGstTypeMaster(gstTypeMaster);
												loadMoneyAccountingRequest.setCurrencyMaster(currencyMaster);
												loadMoneyAccountingRequest.setConvertedAmount( convertedINRValue);
												loadMoneyAccountingRequest.setFeeAmount(feeAmt);
												loadMoneyAccountingRequest.setGstAmount(gstAmt);
												loadMoneyAccountingRequest.setLoadMoneyRequest(loadMoneyRequest);
												loadMoneyAccountingRequest.setTcsApplicable(true);
												loadMoneyAccountingRequest.setTcsAmount(tcsAmt);
												loadMoneyAccountingRequest.setTxnType(loadMoneyRequest.getTxnType());
												loadMoneyAccountingRequest.setParticipantId(loadMoneyRequest.getParticipantId());
												loadMoneyAccountingRequest.setConversionRate(currencyConversionMaster.getFromCurrencyValue());
												loadMoneyAccountingRequest.setTxnType(loadMoneyRequest.getTxnType());
												loadMoneyAccountingRequest.setTotalAmount(Double.parseDouble(totalAmountWithCharges));
												
												 processResponse = loadMoneyAccountingForMultiCurrency(loadMoneyAccountingRequest);
				  
												if("S0000".equalsIgnoreCase(processResponse.getCode())) 
												{
													//update FinicialTear Table 
													multiCurrencyFinancialYearMaster.setTotalLoaded(totalLoaded);
													multiCurrencyFinancialYearMaster.setAvailableLrsLimit(balanceLRS);
													multiCurrencyFinancialYearMaster.setLrsLimit(lrsLimitBasedOnAccountType);
													multiCurrencyFinancialYearMaster.setTotalLoaded(totalLoaded);
													multiCurrencyFinancialYearMaster.setTotalLrsConsumed(totalLRSConsumed);
													multiCurrencyFinancialYearMaster.setTotalExcessLoading(totalExcessLoading);
													multiCurrencyFinancialYearMaster.setTotalTcsOn(totalTcsOn);
													multiCurrencyFinancialYearMasterService.updatFinancialYearTable(multiCurrencyFinancialYearMaster);
									  
													LoadMoneyTxnDetails loadMoneyTxnDetailsObj = new LoadMoneyTxnDetails();
													loadMoneyTxnDetailsObj.setLoadAmt(loadAmt);
													loadMoneyTxnDetailsObj.setBalancedLRS(balanceLRS);
													loadMoneyTxnDetailsObj.setTotalLrsConsumed(totalLRSConsumed);
													loadMoneyTxnDetailsObj.setTotalLoaded(totalLoaded);
													loadMoneyTxnDetailsObj.setTotalExcessLoading(totalExcessLoading);
													loadMoneyTxnDetailsObj.setAccountNumber(loadMoneyRequest.getBaseWallet());
													loadMoneyTxnDetailsObj.setAccountType(baseWallet.getStrAccountType());
													loadMoneyTxnDetailsObj.setTcsOn(tcsOn);
													loadMoneyTxnDetailsObj.setTotalTcsOn(totalTcsOn);
													loadMoneyTxnDetailsObj.setFyBalance(totalLoaded);
													loadMoneyTxnDetailsObj.setLrsLimit(lrsLimitBasedOnAccountType);
													loadMoneyTxnDetailsObj.setTcsCalculatedAmt(tcsAmt);
													loadMoneyTxnDetailsObj.setParticipantId(loadMoneyRequest.getParticipantId());
													loadMoneyTxnDetailsObj.setCurrencyCode(loadMoneyRequest.getCurrencyCode());
													loadMoneyTxnDetailsService.saveLoadMoney(loadMoneyTxnDetailsObj);
									  
													
													LoadMoneyTransactionResponse loadMoneyTransactionResponse = new LoadMoneyTransactionResponse();
													loadMoneyTransactionResponse.setAmountLoaded(String.valueOf(formatter.format( loadMoneyAccountingRequest.getLoadMoneyRequest().getLoadAmount())));
													loadMoneyTransactionResponse.setConvertedAmount(String.valueOf(formatter.format(convertedINRValue)));
													loadMoneyTransactionResponse.setFeeAmount(String.valueOf(formatter.format(feeAmt)));
													loadMoneyTransactionResponse.setGstAmount(String.valueOf(formatter.format(gstAmt)));
													loadMoneyTransactionResponse.setRequestAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getLoadMoneyRequest().getLoadAmount())));
													loadMoneyTransactionResponse.setTcsAmount(String.valueOf(formatter.format(tcsAmt)));
													loadMoneyTransactionResponse.setTransaferToCurrency(loadMoneyAccountingRequest.getLoadMoneyRequest().getCurrencyCode());
													
													
													processResponse.setLoadMoneyTransactionResponse(loadMoneyTransactionResponse);
													processResponse.setCode("S0000");
													processResponse.setStatus("Success");
													processResponse.setMessage("Data Fetch Scussefully");
												}
				  
											}
												else 
											{
													// 1 Time TCS Will be Applied 
													loadAmt = convertedINRValue;
													balanceLRS = 0 ;
													totalLRSConsumed = lrsLimitBasedOnAccountType ;    //LoadAmt + PReviouseLoadAmt
													totalLoaded =  Double.parseDouble(String.valueOf(formatter.format(totalLoaded + loadAmt))) ;
				  
													totalExcessLoading = Double.parseDouble(String.valueOf(formatter.format(totalLoaded  - lrsLimitBasedOnAccountType)));
													tcsOn = Double.parseDouble(String.valueOf(formatter.format(totalExcessLoading - tcsOn)));
													totalTcsOn = Double.parseDouble(String.valueOf(formatter.format(totalTcsOn + tcsOn)));
					  
					  
													 tcsAmt = Double.parseDouble(String.valueOf(formatter.format( tcsOn * tcsPerecentage / 100)));
					  
													//CalculateTotal Amount To Be Recived 
													double totalAmount = Double.parseDouble(String.valueOf(formatter.format(convertedINRValue + feeAmt + tcsAmt + gstAmt)));
													totalAmountWithCharges = formatter.format(totalAmount);
				  
													LoadMoneyAccountingRequest loadMoneyAccountingRequest = new LoadMoneyAccountingRequest();
													loadMoneyAccountingRequest.setBaseWalletAccount(baseWallet);
													loadMoneyAccountingRequest.setCurrencyWalletAccount(currencyWallet);
													loadMoneyAccountingRequest.setMultiCurrencyFeeTypeMaster(multiCurrencyFeeTypeMaster);
													loadMoneyAccountingRequest.setGstTypeMaster(gstTypeMaster);
													loadMoneyAccountingRequest.setCurrencyMaster(currencyMaster);
													loadMoneyAccountingRequest.setConvertedAmount(convertedINRValue);
													loadMoneyAccountingRequest.setLoadMoneyRequest(loadMoneyRequest);
													loadMoneyAccountingRequest.setFeeAmount(feeAmt);
													loadMoneyAccountingRequest.setGstAmount(gstAmt);
													loadMoneyAccountingRequest.setTcsApplicable(true);
													loadMoneyAccountingRequest.setTcsAmount(tcsAmt);
													loadMoneyAccountingRequest.setTotalAmount(Double.parseDouble(totalAmountWithCharges));
													loadMoneyAccountingRequest.setConversionRate(currencyConversionMaster.getFromCurrencyValue());
													loadMoneyAccountingRequest.setTxnType(loadMoneyRequest.getTxnType());
													loadMoneyAccountingRequest.setParticipantId(loadMoneyRequest.getParticipantId());
													
													 processResponse = loadMoneyAccountingForMultiCurrency(loadMoneyAccountingRequest);
  
				  
													if("S0000".equalsIgnoreCase(processResponse.getCode())) 
													{
														//update FinicialTear Table 
														multiCurrencyFinancialYearMaster.setTotalLoaded(totalLoaded);
														multiCurrencyFinancialYearMaster.setAvailableLrsLimit(balanceLRS);
														multiCurrencyFinancialYearMaster.setLrsLimit(lrsLimitBasedOnAccountType);
														multiCurrencyFinancialYearMaster.setTotalLoaded(totalLoaded);
														multiCurrencyFinancialYearMaster.setTotalLrsConsumed(totalLRSConsumed);
														multiCurrencyFinancialYearMaster.setTotalExcessLoading(totalExcessLoading);
														multiCurrencyFinancialYearMaster.setTotalTcsOn(totalTcsOn);
														multiCurrencyFinancialYearMasterService.updatFinancialYearTable(multiCurrencyFinancialYearMaster);
					 
														LoadMoneyTxnDetails loadMoneyTxnDetailsObj = new LoadMoneyTxnDetails();
														
														loadMoneyTxnDetailsObj.setLoadAmt(loadAmt);
														loadMoneyTxnDetailsObj.setBalancedLRS(balanceLRS);
														loadMoneyTxnDetailsObj.setTotalLrsConsumed(totalLRSConsumed);
														loadMoneyTxnDetailsObj.setTotalLoaded(totalLoaded);
														loadMoneyTxnDetailsObj.setAccountNumber(loadMoneyRequest.getBaseWallet());
														loadMoneyTxnDetailsObj.setAccountType(baseWallet.getStrAccountType());
														loadMoneyTxnDetailsObj.setTotalExcessLoading(totalExcessLoading);
														loadMoneyTxnDetailsObj.setTcsOn(tcsOn);
														loadMoneyTxnDetailsObj.setTotalTcsOn(totalTcsOn);
														loadMoneyTxnDetailsObj.setFyBalance(totalLoaded);
														loadMoneyTxnDetailsObj.setLrsLimit(lrsLimitBasedOnAccountType);
														loadMoneyTxnDetailsObj.setTcsCalculatedAmt(tcsAmt);
														loadMoneyTxnDetailsObj.setCurrencyCode(loadMoneyRequest.getCurrencyCode());
														loadMoneyTxnDetailsObj.setParticipantId(loadMoneyRequest.getParticipantId());
														
														loadMoneyTxnDetailsService.saveLoadMoney(loadMoneyTxnDetailsObj);
																  
															
														LoadMoneyTransactionResponse loadMoneyTransactionResponse = new LoadMoneyTransactionResponse();
														loadMoneyTransactionResponse.setAmountLoaded(String.valueOf(formatter.format( loadMoneyAccountingRequest.getLoadMoneyRequest().getLoadAmount())));
														loadMoneyTransactionResponse.setConvertedAmount(String.valueOf(formatter.format(convertedINRValue)));
														loadMoneyTransactionResponse.setFeeAmount(String.valueOf(formatter.format(feeAmt)));
														loadMoneyTransactionResponse.setGstAmount(String.valueOf(formatter.format(gstAmt)));
														loadMoneyTransactionResponse.setRequestAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getLoadMoneyRequest().getLoadAmount())));
														loadMoneyTransactionResponse.setTcsAmount(String.valueOf(formatter.format(tcsAmt)));
														loadMoneyTransactionResponse.setTransaferToCurrency(loadMoneyAccountingRequest.getLoadMoneyRequest().getCurrencyCode());
														
														processResponse.setLoadMoneyTransactionResponse(loadMoneyTransactionResponse);
														processResponse.setCode("S0000");
														processResponse.setStatus("Success");
														processResponse.setMessage("Data Fetch Scussefully");
														
														  
													}else
													{
														processResponse.setCode(processResponse.getCode());
														processResponse.setStatus(processResponse.getStatus());
														processResponse.setMessage(processResponse.getMessage());
													}
											}
		
										}else 
										{
											// LRS is Not Breeched...
			  
											//Calculation For LoadMoneyTransaction
											double totalINRValue = Double.parseDouble(String.valueOf(formatter.format(convertedINRValue + feeAmt + gstAmt)))  ;
											 totalAmountWithCharges = formatter.format(totalINRValue);
											
											loadAmt = convertedINRValue;
											balanceLRS = Double.parseDouble(String.valueOf(formatter.format( balanceLRS - loadAmt)));  
											totalLRSConsumed = Double.parseDouble(String.valueOf(formatter.format(loadAmt + totalLoaded))) ;    
											totalLoaded = Double.parseDouble(String.valueOf(formatter.format(loadAmt + totalLoaded)));
											  
											
			  
											LoadMoneyAccountingRequest loadMoneyAccountingRequest = new LoadMoneyAccountingRequest();
											loadMoneyAccountingRequest.setBaseWalletAccount(baseWallet);
											loadMoneyAccountingRequest.setCurrencyWalletAccount(currencyWallet);
											loadMoneyAccountingRequest.setMultiCurrencyFeeTypeMaster(multiCurrencyFeeTypeMaster);
											loadMoneyAccountingRequest.setGstTypeMaster(gstTypeMaster);
											loadMoneyAccountingRequest.setCurrencyMaster(currencyMaster);
											loadMoneyAccountingRequest.setLoadMoneyRequest(loadMoneyRequest);
											loadMoneyAccountingRequest.setConvertedAmount(convertedINRValue);
											loadMoneyAccountingRequest.setFeeAmount(feeAmt);
											loadMoneyAccountingRequest.setGstAmount(gstAmt);
											loadMoneyAccountingRequest.setTcsApplicable(false);
											loadMoneyAccountingRequest.setTcsAmount(tcsAmt);
											loadMoneyAccountingRequest.setTotalAmount(Double.parseDouble(totalAmountWithCharges));
											loadMoneyAccountingRequest.setConversionRate(currencyConversionMaster.getFromCurrencyValue());
											loadMoneyAccountingRequest.setTxnType(loadMoneyRequest.getTxnType());
											
											loadMoneyAccountingRequest.setParticipantId(loadMoneyRequest.getParticipantId());
											
											 processResponse = loadMoneyAccountingForMultiCurrency(loadMoneyAccountingRequest);
			  
											if("S0000".equalsIgnoreCase(processResponse.getCode())) 
											{
											  //update FinicialTear Table 
											  multiCurrencyFinancialYearMaster.setTotalLoaded(totalLoaded);
											  multiCurrencyFinancialYearMaster.setAvailableLrsLimit(balanceLRS);
											  multiCurrencyFinancialYearMaster.setLrsLimit(lrsLimitBasedOnAccountType);
											  multiCurrencyFinancialYearMaster.setTotalLoaded(totalLoaded);
											  multiCurrencyFinancialYearMaster.setTotalLrsConsumed(totalLRSConsumed);
											  multiCurrencyFinancialYearMasterService.updatFinancialYearTable(multiCurrencyFinancialYearMaster);
											  
											  LoadMoneyTxnDetails loadMoneyTxnDetailsObj = new LoadMoneyTxnDetails();
											  loadMoneyTxnDetailsObj.setLoadAmt(loadAmt);
											  loadMoneyTxnDetailsObj.setBalancedLRS(balanceLRS);
											  loadMoneyTxnDetailsObj.setTotalLrsConsumed(totalLRSConsumed);
											  loadMoneyTxnDetailsObj.setTotalLoaded(totalLoaded);
											  loadMoneyTxnDetailsObj.setAccountNumber(loadMoneyRequest.getBaseWallet());
											  loadMoneyTxnDetailsObj.setAccountType(baseWallet.getStrAccountType());
											  loadMoneyTxnDetailsObj.setFyBalance(totalLoaded);
											  loadMoneyTxnDetailsObj.setTotalExcessLoading(totalExcessLoading);
											  loadMoneyTxnDetailsObj.setTcsOn(tcsOn);
											  loadMoneyTxnDetailsObj.setTotalTcsOn(totalTcsOn);	
											  loadMoneyTxnDetailsObj.setLrsLimit(lrsLimitBasedOnAccountType);
											  loadMoneyTxnDetailsObj.setTcsCalculatedAmt(tcsAmt);
											  loadMoneyTxnDetailsObj.setCurrencyCode(loadMoneyRequest.getCurrencyCode());
											  loadMoneyTxnDetailsObj.setParticipantId(loadMoneyRequest.getParticipantId());
												
											  loadMoneyTxnDetailsService.saveLoadMoney(loadMoneyTxnDetailsObj);
											
												
												LoadMoneyTransactionResponse loadMoneyTransactionResponse = new LoadMoneyTransactionResponse();
												loadMoneyTransactionResponse.setAmountLoaded(String.valueOf(formatter.format( loadMoneyAccountingRequest.getLoadMoneyRequest().getLoadAmount())));
												loadMoneyTransactionResponse.setConvertedAmount(String.valueOf(formatter.format(convertedINRValue)));
												loadMoneyTransactionResponse.setFeeAmount(String.valueOf(formatter.format(feeAmt)));
												loadMoneyTransactionResponse.setGstAmount(String.valueOf(formatter.format(gstAmt)));
												loadMoneyTransactionResponse.setRequestAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getLoadMoneyRequest().getLoadAmount())));
												loadMoneyTransactionResponse.setTcsAmount(String.valueOf(formatter.format(tcsAmt)));
												loadMoneyTransactionResponse.setTransaferToCurrency(loadMoneyAccountingRequest.getLoadMoneyRequest().getCurrencyCode());
												
												processResponse.setLoadMoneyTransactionResponse(loadMoneyTransactionResponse);
												processResponse.setCode("S0000");
												processResponse.setStatus("Success");
												processResponse.setMessage("Data Fetch Scussefully");
											}else 
											{
												processResponse.setCode(processResponse.getCode());
												processResponse.setStatus(processResponse.getStatus());
												processResponse.setMessage(processResponse.getMessage());
											}
										}
									}
									else
									{
										processResponse.setCode("E0000");
										processResponse.setStatus("Failed");
										processResponse.setMessage("Finicial Year Data Not Found");
									}
									
							}else
							{
							  processResponse.setCode("E0000");
							  processResponse.setStatus("Failed");
								 processResponse.setMessage("Gst Type Data Not Found");
							}
									
							}else
							{
							  processResponse.setCode("E0000");
							  processResponse.setStatus("Failed");
								 processResponse.setMessage("Fee Type Data Not Found");
							}
						}else
						{ 
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("No Conversion Currency Found");
						}
						}else {
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Currency is Not Base Currency");
						}
						}
						else {
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("No Currency Master Found");
						}
					}else 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Currency Wallet Type Not Found .");
					}
					
				
			}else {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Channel Not Configured"); 
			}
			}else {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Lrs Not Configerd For This Account Type");
			}
			  }else 
			  {
				  processResponse.setCode("E0000");
				  processResponse.setStatus("Failed");
				  processResponse.setMessage("Multi-Currency Not Supported For This Account Type");
			  }	  
		}
		else 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Base Wallet Account Not Found .");
		}
		
			}else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Please Provide Base Wallet.");
			}
		
			}else {
				processResponse.setCode(transactionHandlerAPIObj.getCode());
				processResponse.setStatus(transactionHandlerAPIObj.getStatus());
				processResponse.setMessage(transactionHandlerAPIObj.getMessage());
			}
		
	}catch (Exception e)
		{
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		
	}
	return processResponse;
	}


	private ProcessResponse loadMoneyAccountingForMultiCurrency(LoadMoneyAccountingRequest loadMoneyAccountingRequest) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		processResponse.setCode("S0000");
		processResponse.setStatus("Success");
		 amsLogger.writeExceptionLog("LoadMoney  Accounting Pocess Start::"+loadMoneyAccountingRequest);
		try 
		{
			double balance ;
			// Getting  [Channel]  From Request
			Channels channels = new Channels();
			channels.setChannelCode(loadMoneyAccountingRequest.getLoadMoneyRequest().getChannel());
			channels = channelsService.getChannelsList(channels);
			if(channels != null) 
			{
				//Getting   [Channel GL] 
				GLAccountTypeMaster channelGl = new GLAccountTypeMaster();
				channelGl.setStrAccountNumber(channels.getGlAccountNumber());
				channelGl.setStrAccountType(channels.getGlAccountType());
				channelGl = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(channelGl);
				if(channelGl != null) 
				{
					
					//Base Wallet Account 
					if(loadMoneyAccountingRequest.getBaseWalletAccount() != null) 
					{
						AccountTypeMaster accountTypeMster = new AccountTypeMaster();
						accountTypeMster.setStrAccountType(loadMoneyAccountingRequest.getBaseWalletAccount().getStrAccountType());	
						accountTypeMster = accountTypeMasterService.getAccountTypeObject(accountTypeMster);
						
						
						
						if(accountTypeMster != null)
						{
						
							// Getting [ LinkedBaseWallet GL ]
							GLAccountTypeMaster linkedGlBaseWallet = new GLAccountTypeMaster();
							linkedGlBaseWallet.setStrAccountNumber(accountTypeMster.getStrGLAccountNumber());
							linkedGlBaseWallet.setStrAccountType(accountTypeMster.getStrGLAccountType());
							linkedGlBaseWallet = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(linkedGlBaseWallet);
							if(linkedGlBaseWallet != null) 
							{
							//Getting [ Fee GL ] - From Fee Type Master
							GLAccountTypeMaster feeCurrencyGL = new GLAccountTypeMaster();
							feeCurrencyGL.setStrAccountNumber(loadMoneyAccountingRequest.getMultiCurrencyFeeTypeMaster().getGlAccountNumber());
							feeCurrencyGL.setStrAccountType(loadMoneyAccountingRequest.getMultiCurrencyFeeTypeMaster().getGlAccountType());
							feeCurrencyGL = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(feeCurrencyGL);
							if(feeCurrencyGL != null) 
							{
								
								//Getting [ GST GL ] - From GST Type Master
								GLAccountTypeMaster gstCurrencyGL = new GLAccountTypeMaster();
								gstCurrencyGL.setStrAccountNumber(loadMoneyAccountingRequest.getGstTypeMaster().getGlAccountNumber());
								gstCurrencyGL.setStrAccountType(loadMoneyAccountingRequest.getGstTypeMaster().getGlAccountType());
								gstCurrencyGL = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(gstCurrencyGL);
								if(gstCurrencyGL != null) 
								{
									//getting [ TCS Type]
									TcsTypeMaster tcsTypeMaster = new TcsTypeMaster();
									tcsTypeMaster.setTcsType(loadMoneyAccountingRequest.getCurrencyMaster().getTcsType());
									tcsTypeMaster = tcsTypeMasterService.getTcsTypeMasterObj(tcsTypeMaster);
									if(tcsTypeMaster != null) {
									
									// Getting [ TCS GL ]
									GLAccountTypeMaster tcsCurrencyGL = new GLAccountTypeMaster();
									tcsCurrencyGL.setStrAccountNumber(tcsTypeMaster.getGlAccountNumber());
									tcsCurrencyGL.setStrAccountType(tcsTypeMaster.getGlAccountType());
									tcsCurrencyGL = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(tcsCurrencyGL);
									if(tcsCurrencyGL != null) 
									{
										
										//Getting [BaseCurrency GL] From Currency Master
										GLAccountTypeMaster baseCurrencyGL = new GLAccountTypeMaster();
										baseCurrencyGL.setStrAccountNumber(loadMoneyAccountingRequest.getCurrencyMaster().getGlAccountNumber());
										baseCurrencyGL.setStrAccountType(loadMoneyAccountingRequest.getCurrencyMaster().getGlAccountType());
										baseCurrencyGL = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(baseCurrencyGL);
										if(baseCurrencyGL != null) 
										{
											//Getting Wallet Currency Details
											CurrencyMaster WallerCurrencyMaster = new CurrencyMaster();
											WallerCurrencyMaster.setCurrencyCode(loadMoneyAccountingRequest.getCurrencyWalletAccount().getStrCurrencyCode());
											WallerCurrencyMaster = currencyMasterService.getCurrencyMasterByCurrencyCode(WallerCurrencyMaster);
											if(WallerCurrencyMaster != null) 
											{
												
												//Getting [ Wallet Currency GL ] From Above Currency Details
												GLAccountTypeMaster walletCurrencyGL = new GLAccountTypeMaster();
												walletCurrencyGL.setStrAccountNumber(WallerCurrencyMaster.getGlAccountNumber());
												walletCurrencyGL.setStrAccountType(WallerCurrencyMaster.getGlAccountType());
												walletCurrencyGL = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(walletCurrencyGL);
												if(walletCurrencyGL != null) 
												{
												
													// Getting Currency Wallet 
													if(loadMoneyAccountingRequest.getCurrencyWalletAccount() != null) {
												
													MultiCurrencyWalletAccountTypeMaster currencyWalletAccountType = new MultiCurrencyWalletAccountTypeMaster();
												//	currencyWalletAccountType.setStrAccountType(loadMoneyAccountingRequest.getCurrencyWalletAccount().getBaseCurrencyAccountType());
													currencyWalletAccountType.setBaseCurrencyAccountType(loadMoneyAccountingRequest.getCurrencyWalletAccount().getBaseCurrencyAccountType());
													currencyWalletAccountType.setStrCurrencyCode(loadMoneyAccountingRequest.getCurrencyWalletAccount().getStrCurrencyCode());
													currencyWalletAccountType = multiCurrencyWalletAccountTypeService.getMultiCurrencyAccountTypeMasterByCurrencyCodeAndAccountType(currencyWalletAccountType);
													
													if(currencyWalletAccountType != null) {
													
													//Getting Linked Currency GL For Currency Wallet	
													GLAccountTypeMaster linkedGlCurrencyWallet = new GLAccountTypeMaster();
													linkedGlCurrencyWallet.setStrAccountNumber(currencyWalletAccountType.getGlAccountNumber());
													linkedGlCurrencyWallet.setStrAccountType(currencyWalletAccountType.getBaseCurrencyAccountType());
													linkedGlCurrencyWallet = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(linkedGlCurrencyWallet);
													if(linkedGlCurrencyWallet != null) 
													{
														
														String txnId = transactionIdService.getNewTransactionId();
														double previousBalnce ;
														String authCode = Utils.getAlphaNumericString();
														
														//Get Processing Code 
														TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
														transactionTypeModel.setStrTxnTypeKeyWord(loadMoneyAccountingRequest.getTxnType());
														
														transactionTypeModel = transactionTypeService.getTransactionTypeMaster(transactionTypeModel);
														if (transactionTypeModel != null && transactionTypeModel.getStrID() != null) 
														{
															
															
														//--------------> GL Account Statement DEBIT For  GL Channel <------------------------
														String currentBalance = channelGl.getStrClosingBalance();
														previousBalnce = Double.parseDouble(currentBalance);
														channelGl.setStrClosingBalance(String.valueOf(formatter.format(previousBalnce - loadMoneyAccountingRequest.getTotalAmount())));
														glAccountTypeMasterService.updateGLAccountTypeDetails(channelGl);

													
														GLAccountStatement debitGlChannel = new GLAccountStatement();
														debitGlChannel.setStrTxnId(txnId);
														debitGlChannel.setTransactionDate(Utils.getCurrentDate());
														debitGlChannel.setCreatedDate(Utils.getCurrentDate());
														debitGlChannel.setStrCreated_by(CommonConstants.SYSTEM);
														debitGlChannel.setStrTranType(loadMoneyAccountingRequest.getTxnType());
														debitGlChannel.setStrAccountNumber(channelGl.getStrAccountNumber());
														debitGlChannel.setStrGLAccountType(channelGl.getStrGLAccountType());
														debitGlChannel.setStrClosingBalance(channelGl.getStrClosingBalance()); 
														debitGlChannel.setStrAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getTotalAmount())));
														debitGlChannel.setStrTranMode(CommonConstants.DEBIT);
														debitGlChannel.setCurrencyCode(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														debitGlChannel.setStrRef(channelGl.getStrGLAccountDescription());
														glAccountStatementService.addGlAccountStatementData(debitGlChannel);
														

														//--------------> Account Statement CREDIT  For  Base Wallet Account  <----------------------------------
													    AccountMaster baseWalletAccount = loadMoneyAccountingRequest.getBaseWalletAccount();
													    previousBalnce = Double.parseDouble(baseWalletAccount.getStrClosingBalance()) ;
														balance = previousBalnce +  loadMoneyAccountingRequest.getTotalAmount();
														baseWalletAccount.setStrClosingBalance(String.valueOf(formatter.format(balance)));
														accountMasterService.updateBaseWalletAccountMaster(baseWalletAccount);

													
														AccountStatement creditBaseWalletTotalAmt = new AccountStatement();
														creditBaseWalletTotalAmt.setStrParticipantId(loadMoneyAccountingRequest.getParticipantId());
														creditBaseWalletTotalAmt.setStrIsGLType(CommonConstants.NO);
														creditBaseWalletTotalAmt.setTransactionDate(Utils.getCurrentDate());
														creditBaseWalletTotalAmt.setStrTransactionID(txnId);
														creditBaseWalletTotalAmt.setStrAccountNumber(baseWalletAccount.getStrAccountNumber());
														creditBaseWalletTotalAmt.setStrAccountType(baseWalletAccount.getStrAccountType());
														creditBaseWalletTotalAmt.setStrClosingBalance(String.valueOf(baseWalletAccount.getStrClosingBalance()));
														creditBaseWalletTotalAmt.setStrTransactionAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getTotalAmount())));
														creditBaseWalletTotalAmt.setStrNaration(CommonConstants.CREDIT_NARATION);
														creditBaseWalletTotalAmt.setCurrencyCode(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														creditBaseWalletTotalAmt.setStrTransactionMode(CommonConstants.CREDIT);
														creditBaseWalletTotalAmt.setStrTransactionType(loadMoneyAccountingRequest.getTxnType());
														accountStatementService.addAccountTransactionData(creditBaseWalletTotalAmt);
														
														//--------------> GL Account Statement CREDIT For  GL Base Wallet Account  <----------------------------------
														previousBalnce = Double.parseDouble(linkedGlBaseWallet.getStrClosingBalance()) ;
														linkedGlBaseWallet.setStrClosingBalance(String.valueOf(formatter.format(previousBalnce + loadMoneyAccountingRequest.getTotalAmount())));
														//glAccountTypeMasterDao.updateGLAccountTypeDetails(linkedGlBaseWallet); 
														glAccountTypeMasterService.updateGLAccountTypeDetails(linkedGlBaseWallet);
														
														GLAccountStatement creditGlBaseWalletTotalAmt = new GLAccountStatement();
														creditGlBaseWalletTotalAmt.setStrTxnId(txnId);
														creditGlBaseWalletTotalAmt.setTransactionDate(Utils.getCurrentDate());
														creditGlBaseWalletTotalAmt.setCreatedDate(Utils.getCurrentDate());
														creditGlBaseWalletTotalAmt.setStrCreated_by(CommonConstants.SYSTEM);
														creditGlBaseWalletTotalAmt.setStrTranType(loadMoneyAccountingRequest.getTxnType());
														creditGlBaseWalletTotalAmt.setStrAccountNumber(linkedGlBaseWallet.getStrAccountNumber());
														creditGlBaseWalletTotalAmt.setStrGLAccountType(linkedGlBaseWallet.getStrGLAccountType());
														creditGlBaseWalletTotalAmt.setStrClosingBalance(linkedGlBaseWallet.getStrClosingBalance());
														creditGlBaseWalletTotalAmt.setStrAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getTotalAmount())));
														creditGlBaseWalletTotalAmt.setStrTranMode(CommonConstants.CREDIT);
														creditGlBaseWalletTotalAmt.setCurrencyCode(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														creditGlBaseWalletTotalAmt.setStrRef(linkedGlBaseWallet.getStrGLAccountDescription());
														
														glAccountStatementService.addGlAccountStatementData(creditGlBaseWalletTotalAmt);
														
														
														
														AccountTranMaster tranChannelGlToBaseWallet = new AccountTranMaster();
														tranChannelGlToBaseWallet.setStrTxn_id(txnId);
														tranChannelGlToBaseWallet.setStrLocal_tran_date(Utils.getCurrentDate());
														tranChannelGlToBaseWallet.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
														tranChannelGlToBaseWallet.setStrParticipantId(loadMoneyAccountingRequest.getParticipantId());
														tranChannelGlToBaseWallet.setSwitchTxDate(Utils.getCurrentDate());
														tranChannelGlToBaseWallet.setStrResponseCode("00");//?
														tranChannelGlToBaseWallet.setStrAuthCode(authCode);
														tranChannelGlToBaseWallet.setStrProcessingCode(transactionTypeModel.getStrProcessingCode());
														tranChannelGlToBaseWallet.setStrTran_type(loadMoneyAccountingRequest.getTxnType());
														tranChannelGlToBaseWallet.setStrTransaction_amount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getTotalAmount())));
														tranChannelGlToBaseWallet.setStrFrom_account_number(channelGl.getStrAccountNumber());
														tranChannelGlToBaseWallet.setStrTo_account_number(baseWalletAccount.getStrAccountNumber());
														accountTranMasterService.addAccountTransactionData(tranChannelGlToBaseWallet);
														
														
														//--------------> Account Statement DEBIT For  Base Wallet Account  <----------------------------------
														previousBalnce = Double.parseDouble(baseWalletAccount.getStrClosingBalance()) ;
														baseWalletAccount.setStrClosingBalance(String.valueOf(formatter.format(previousBalnce - loadMoneyAccountingRequest.getFeeAmount() )));
														accountMasterService.updateBaseWalletAccountMaster(baseWalletAccount);
														
														
														AccountStatement debitBaseWalletFeeAmt = new AccountStatement();
														debitBaseWalletFeeAmt.setStrParticipantId(loadMoneyAccountingRequest.getParticipantId());
														debitBaseWalletFeeAmt.setStrTransactionType(loadMoneyAccountingRequest.getTxnType());	
														debitBaseWalletFeeAmt.setStrIsGLType(CommonConstants.NO);
														debitBaseWalletFeeAmt.setTransactionDate(Utils.getCurrentDate());
														debitBaseWalletFeeAmt.setStrTransactionID(txnId);
														debitBaseWalletFeeAmt.setStrAccountNumber(baseWalletAccount.getStrAccountNumber());
														debitBaseWalletFeeAmt.setStrAccountType(baseWalletAccount.getStrAccountType());
														debitBaseWalletFeeAmt.setStrClosingBalance(String.valueOf(baseWalletAccount.getStrClosingBalance())); 
														debitBaseWalletFeeAmt.setStrTransactionAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getFeeAmount())));
														debitBaseWalletFeeAmt.setStrNaration(CommonConstants.DEBIT_NARRATION);
														debitBaseWalletFeeAmt.setCurrencyCode(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														debitBaseWalletFeeAmt.setStrTransactionMode(CommonConstants.DEBIT);
														accountStatementService.addAccountTransactionData(debitBaseWalletFeeAmt);
														
														
														//--------------> GL Account Statement DEBIT For GL  Base Wallet Account  <----------------------------------
														previousBalnce = Double.parseDouble(linkedGlBaseWallet.getStrClosingBalance()) ;
														linkedGlBaseWallet.setStrClosingBalance(String.valueOf(formatter.format(previousBalnce - loadMoneyAccountingRequest.getFeeAmount())));
														glAccountTypeMasterService.updateGLAccountTypeDetails(linkedGlBaseWallet);
														
														GLAccountStatement creditGlBaseWalletFeeAmt = new GLAccountStatement();
														creditGlBaseWalletFeeAmt.setStrTxnId(txnId);
														
														creditGlBaseWalletFeeAmt.setTransactionDate(Utils.getCurrentDate());
														creditGlBaseWalletFeeAmt.setCreatedDate(Utils.getCurrentDate());
														creditGlBaseWalletFeeAmt.setStrCreated_by(CommonConstants.SYSTEM);
														creditGlBaseWalletFeeAmt.setStrTranType(loadMoneyAccountingRequest.getTxnType());
														creditGlBaseWalletFeeAmt.setStrAccountNumber(linkedGlBaseWallet.getStrAccountNumber());
														creditGlBaseWalletFeeAmt.setStrGLAccountType(linkedGlBaseWallet.getStrGLAccountType());
														creditGlBaseWalletFeeAmt.setStrClosingBalance(linkedGlBaseWallet.getStrClosingBalance());
														creditGlBaseWalletFeeAmt.setStrAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getFeeAmount())));
														creditGlBaseWalletFeeAmt.setStrTranMode(CommonConstants.DEBIT);
														creditGlBaseWalletFeeAmt.setCurrencyCode(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														creditGlBaseWalletFeeAmt.setStrRef(linkedGlBaseWallet.getStrGLAccountDescription());
														glAccountStatementService.addGlAccountStatementData(creditGlBaseWalletFeeAmt);
														
														//--------------> GL Account Statement CREDIT For FEE GL <----------------------------------
														previousBalnce = Double.parseDouble(feeCurrencyGL.getStrClosingBalance()) ;
														feeCurrencyGL.setStrClosingBalance(String.valueOf(formatter.format(previousBalnce + loadMoneyAccountingRequest.getFeeAmount())));
														glAccountTypeMasterService.updateGLAccountTypeDetails(feeCurrencyGL);
													
														
														GLAccountStatement creditFeeGl = new GLAccountStatement();
														creditFeeGl.setStrTxnId(txnId);
														
														creditFeeGl.setTransactionDate(Utils.getCurrentDate());
														creditFeeGl.setCreatedDate(Utils.getCurrentDate());
														creditFeeGl.setStrCreated_by(CommonConstants.SYSTEM);
														creditFeeGl.setStrTranType(loadMoneyAccountingRequest.getTxnType());
														creditFeeGl.setStrAccountNumber(feeCurrencyGL.getStrAccountNumber());
														creditFeeGl.setStrGLAccountType(feeCurrencyGL.getStrGLAccountType());
														creditFeeGl.setStrClosingBalance(feeCurrencyGL.getStrClosingBalance());
														creditFeeGl.setStrAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getFeeAmount())));
														creditFeeGl.setStrTranMode(CommonConstants.CREDIT);
														creditFeeGl.setCurrencyCode(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														creditFeeGl.setStrRef(feeCurrencyGL.getStrGLAccountDescription());
														glAccountStatementService.addGlAccountStatementData(creditFeeGl);
														
														//Transaction Entry
														AccountTranMaster tranBaseWalletToFeeGl = new AccountTranMaster();
														tranBaseWalletToFeeGl.setStrTxn_id(txnId);
														tranBaseWalletToFeeGl.setStrLocal_tran_date(Utils.getCurrentDate());
														tranBaseWalletToFeeGl.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
														tranBaseWalletToFeeGl.setStrParticipantId(loadMoneyAccountingRequest.getParticipantId());
														tranBaseWalletToFeeGl.setSwitchTxDate(Utils.getCurrentDate());
														tranBaseWalletToFeeGl.setStrResponseCode("00");//?
														tranBaseWalletToFeeGl.setStrProcessingCode(transactionTypeModel.getStrProcessingCode());
														tranBaseWalletToFeeGl.setStrTran_type(loadMoneyAccountingRequest.getTxnType());
														tranBaseWalletToFeeGl.setStrAuthCode(authCode);
														tranBaseWalletToFeeGl.setStrTransaction_amount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getFeeAmount())));
														tranBaseWalletToFeeGl.setStrFrom_account_number(baseWalletAccount.getStrAccountNumber());
														tranBaseWalletToFeeGl.setStrTo_account_number(feeCurrencyGL.getStrAccountNumber());
														accountTranMasterService.addAccountTransactionData(tranBaseWalletToFeeGl);
														
														//Entry in FeeGL Report
														MultiCurrencyChargesReport multiCurrencyFeeReport = new MultiCurrencyChargesReport();
														multiCurrencyFeeReport.setAccountCurrency(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														multiCurrencyFeeReport.setAccountNumber(loadMoneyAccountingRequest.getBaseWalletAccount().getStrAccountNumber());
														multiCurrencyFeeReport.setAccountType(loadMoneyAccountingRequest.getBaseWalletAccount().getStrAccountType());
														multiCurrencyFeeReport.setChargesType(CommonConstants.FEE_TYPE);
														multiCurrencyFeeReport.setGlAccountNumber(feeCurrencyGL.getStrAccountNumber());
														multiCurrencyFeeReport.setGlAccountType(feeCurrencyGL.getStrGLAccountType());
														multiCurrencyFeeReport.setParticipantId(loadMoneyAccountingRequest.getParticipantId());
														multiCurrencyFeeReport.setTxnAmt(loadMoneyAccountingRequest.getFeeAmount());
														multiCurrencyFeeReport.setTxnDate(Utils.getCurrentDate());
														multiCurrencyFeeReport.setTxnId(txnId);
														multiCurrencyFeeReport.setBaseConversionRate(loadMoneyAccountingRequest.getConversionRate());
														multiCurrencyFeeReport.setBaseCurrency(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														multiCurrencyFeeReport.setBaseCurrencyAmt(loadMoneyAccountingRequest.getFeeAmount());
														multiCurrencyFeeReport.setTxnType(loadMoneyAccountingRequest.getTxnType());
														
														multiCurrencyChargesReportService.saveMultiCurrencyChargeReport(multiCurrencyFeeReport);
														
														
														//--------------> Account Statement DEBIT For  Base Wallet Account  CREDIT IN GST GL  <----------------------------------
													     previousBalnce = Double.parseDouble(baseWalletAccount.getStrClosingBalance()) ;
														baseWalletAccount.setStrClosingBalance(String.valueOf(formatter.format(previousBalnce - loadMoneyAccountingRequest.getGstAmount() )));
														accountMasterService.updateBaseWalletAccountMaster(baseWalletAccount);
														
														AccountStatement debitBaseWalletGstAmt = new AccountStatement();
														debitBaseWalletGstAmt.setStrTransactionType(loadMoneyAccountingRequest.getTxnType());
														debitBaseWalletGstAmt.setStrParticipantId(loadMoneyAccountingRequest.getParticipantId());
														debitBaseWalletGstAmt.setStrIsGLType(CommonConstants.NO);
														debitBaseWalletGstAmt.setTransactionDate(Utils.getCurrentDate());
														debitBaseWalletGstAmt.setStrTransactionID(txnId);
														debitBaseWalletGstAmt.setStrAccountNumber(baseWalletAccount.getStrAccountNumber());
														debitBaseWalletGstAmt.setStrAccountType(baseWalletAccount.getStrAccountType());
														debitBaseWalletGstAmt.setStrClosingBalance(String.valueOf(baseWalletAccount.getStrClosingBalance())); 
														debitBaseWalletGstAmt.setStrTransactionAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getGstAmount())));
														debitBaseWalletGstAmt.setStrNaration(CommonConstants.DEBIT_NARRATION);
														debitBaseWalletGstAmt.setCurrencyCode(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														debitBaseWalletGstAmt.setStrTransactionMode(CommonConstants.DEBIT);
														accountStatementService.addAccountTransactionData(debitBaseWalletGstAmt);
														
														
														//--------------> GL Account Statement DEBIT For GL  Base Wallet Account  <----------------------------------
														  previousBalnce = Double.parseDouble(linkedGlBaseWallet.getStrClosingBalance()) ;
															
														linkedGlBaseWallet.setStrClosingBalance(String.valueOf(formatter.format(previousBalnce - loadMoneyAccountingRequest.getGstAmount())));
														glAccountTypeMasterService.updateGLAccountTypeDetails(linkedGlBaseWallet);
														
														
														GLAccountStatement debitLinkedGlBaseWalletGstAmt = new GLAccountStatement();
														debitLinkedGlBaseWalletGstAmt.setStrTxnId(txnId);
														debitLinkedGlBaseWalletGstAmt.setTransactionDate(Utils.getCurrentDate());
														debitLinkedGlBaseWalletGstAmt.setCreatedDate(Utils.getCurrentDate());
														debitLinkedGlBaseWalletGstAmt.setStrCreated_by(CommonConstants.SYSTEM);
														debitLinkedGlBaseWalletGstAmt.setStrTranType(loadMoneyAccountingRequest.getTxnType());
														debitLinkedGlBaseWalletGstAmt.setStrAccountNumber(linkedGlBaseWallet.getStrAccountNumber());
														debitLinkedGlBaseWalletGstAmt.setStrGLAccountType(linkedGlBaseWallet.getStrGLAccountType());
														debitLinkedGlBaseWalletGstAmt.setStrClosingBalance(linkedGlBaseWallet.getStrClosingBalance());
														debitLinkedGlBaseWalletGstAmt.setStrAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getGstAmount())));
														debitLinkedGlBaseWalletGstAmt.setStrTranMode(CommonConstants.DEBIT);
														debitLinkedGlBaseWalletGstAmt.setCurrencyCode(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														debitLinkedGlBaseWalletGstAmt.setStrRef(linkedGlBaseWallet.getStrGLAccountDescription());
														glAccountStatementService.addGlAccountStatementData(debitLinkedGlBaseWalletGstAmt);
														
														//--------------> GL Account Statement CREDIT For GST GL <----------------------------------
														 previousBalnce = Double.parseDouble(gstCurrencyGL.getStrClosingBalance()) ;
															
														gstCurrencyGL.setStrClosingBalance(String.valueOf(formatter.format(previousBalnce + loadMoneyAccountingRequest.getGstAmount())));
														glAccountTypeMasterService.updateGLAccountTypeDetails(gstCurrencyGL);
													
														GLAccountStatement creditGstGl = new GLAccountStatement();
														creditGstGl.setStrTxnId(txnId);
														creditGstGl.setStrTranType(loadMoneyAccountingRequest.getTxnType());
														creditGstGl.setTransactionDate(Utils.getCurrentDate());
														creditGstGl.setCreatedDate(Utils.getCurrentDate());
														creditGstGl.setStrCreated_by(CommonConstants.SYSTEM);
												
														creditGstGl.setStrAccountNumber(gstCurrencyGL.getStrAccountNumber());
														creditGstGl.setStrGLAccountType(gstCurrencyGL.getStrGLAccountType());
														creditGstGl.setStrClosingBalance(gstCurrencyGL.getStrClosingBalance());
														creditGstGl.setStrAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getGstAmount())));
														creditGstGl.setStrTranMode(CommonConstants.CREDIT);
														creditGstGl.setCurrencyCode(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														creditGstGl.setStrRef(gstCurrencyGL.getStrGLAccountDescription());
														glAccountStatementService.addGlAccountStatementData(creditGstGl);
														
														AccountTranMaster tranBaseWalletToGstGl = new AccountTranMaster();
														tranBaseWalletToGstGl.setStrTxn_id(txnId);
														tranBaseWalletToGstGl.setStrLocal_tran_date(Utils.getCurrentDate());
														tranBaseWalletToGstGl.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
														tranBaseWalletToGstGl.setStrParticipantId(loadMoneyAccountingRequest.getParticipantId());
														tranBaseWalletToGstGl.setSwitchTxDate(Utils.getCurrentDate());
														tranBaseWalletToGstGl.setStrResponseCode("00");//?
														tranBaseWalletToGstGl.setStrAuthCode(authCode);
														tranBaseWalletToGstGl.setStrProcessingCode(transactionTypeModel.getStrProcessingCode());
														tranBaseWalletToGstGl.setStrTran_type(loadMoneyAccountingRequest.getTxnType());
														tranBaseWalletToGstGl.setStrTransaction_amount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getGstAmount())));
														tranBaseWalletToGstGl.setStrFrom_account_number(baseWalletAccount.getStrAccountNumber());
														tranBaseWalletToGstGl.setStrTo_account_number(gstCurrencyGL.getStrAccountNumber());
														accountTranMasterService.addAccountTransactionData(tranBaseWalletToGstGl);
														
														
														MultiCurrencyChargesReport multiCurrencyGstReport = new MultiCurrencyChargesReport();
														multiCurrencyGstReport.setAccountCurrency(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														multiCurrencyGstReport.setAccountNumber(loadMoneyAccountingRequest.getBaseWalletAccount().getStrAccountNumber());
														multiCurrencyGstReport.setAccountType(loadMoneyAccountingRequest.getBaseWalletAccount().getStrAccountType());
														multiCurrencyGstReport.setChargesType(CommonConstants.GST_TYPE);
														multiCurrencyGstReport.setGlAccountNumber(creditGstGl.getStrAccountNumber());
														multiCurrencyGstReport.setGlAccountType(creditGstGl.getStrGLAccountType());
														multiCurrencyGstReport.setParticipantId(loadMoneyAccountingRequest.getParticipantId());
														multiCurrencyGstReport.setTxnAmt(loadMoneyAccountingRequest.getGstAmount());
														multiCurrencyGstReport.setTxnDate(Utils.getCurrentDate());
														multiCurrencyGstReport.setTxnId(txnId);
														multiCurrencyGstReport.setTxnType(loadMoneyAccountingRequest.getTxnType());
														multiCurrencyGstReport.setBaseConversionRate(loadMoneyAccountingRequest.getConversionRate());
														multiCurrencyGstReport.setBaseCurrency(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														multiCurrencyGstReport.setBaseCurrencyAmt(loadMoneyAccountingRequest.getGstAmount());
														multiCurrencyChargesReportService.saveMultiCurrencyChargeReport(multiCurrencyGstReport);
														
														
														if(loadMoneyAccountingRequest.isTcsApplicable()) {
															
															
															//--------------> Account Statement DEBIT For  Base Wallet Account  CREDIT IN TCS GL  <----------------------------------
															  previousBalnce = Double.parseDouble(baseWalletAccount.getStrClosingBalance()) ;
																	
															baseWalletAccount.setStrClosingBalance(String.valueOf(formatter.format(previousBalnce - loadMoneyAccountingRequest.getTcsAmount())));
															accountMasterService.updateBaseWalletAccountMaster(baseWalletAccount);
															
															
															AccountStatement debitBaseWalletTscAmt = new AccountStatement();
															debitBaseWalletTscAmt.setStrTransactionType(loadMoneyAccountingRequest.getTxnType());
															debitBaseWalletTscAmt.setStrParticipantId(loadMoneyAccountingRequest.getParticipantId());
															debitBaseWalletTscAmt.setStrIsGLType(CommonConstants.NO);
															debitBaseWalletTscAmt.setTransactionDate(Utils.getCurrentDate());
															debitBaseWalletTscAmt.setStrTransactionID(txnId);
															debitBaseWalletTscAmt.setStrAccountNumber(baseWalletAccount.getStrAccountNumber());
															debitBaseWalletTscAmt.setStrAccountType(baseWalletAccount.getStrAccountType());
															debitBaseWalletTscAmt.setStrClosingBalance(String.valueOf(baseWalletAccount.getStrClosingBalance())); 
															debitBaseWalletTscAmt.setStrTransactionAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getTcsAmount())));
															debitBaseWalletTscAmt.setStrNaration(CommonConstants.DEBIT_NARRATION);
															debitBaseWalletTscAmt.setCurrencyCode(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
															debitBaseWalletTscAmt.setStrTransactionMode(CommonConstants.DEBIT);
															accountStatementService.addAccountTransactionData(debitBaseWalletTscAmt);
															
															
															//--------------> GL Account Statement DEBIT For GL  Base Wallet Account  <----------------------------------
															  previousBalnce = Double.parseDouble(linkedGlBaseWallet.getStrClosingBalance()) ;
																
															linkedGlBaseWallet.setStrClosingBalance(String.valueOf(formatter.format(previousBalnce - loadMoneyAccountingRequest.getTcsAmount() )));
															glAccountTypeMasterService.updateGLAccountTypeDetails(linkedGlBaseWallet);
															
															
															GLAccountStatement debitbasewalletTcsAmt = new GLAccountStatement();
															debitbasewalletTcsAmt.setStrTxnId(txnId);
															debitbasewalletTcsAmt.setStrTranType(loadMoneyAccountingRequest.getTxnType());
															debitbasewalletTcsAmt.setTransactionDate(Utils.getCurrentDate());
															debitbasewalletTcsAmt.setCreatedDate(Utils.getCurrentDate());
															debitbasewalletTcsAmt.setStrCreated_by(CommonConstants.SYSTEM);
															
															debitbasewalletTcsAmt.setStrAccountNumber(linkedGlBaseWallet.getStrAccountNumber());
															debitbasewalletTcsAmt.setStrGLAccountType(linkedGlBaseWallet.getStrGLAccountType());
															debitbasewalletTcsAmt.setStrClosingBalance(linkedGlBaseWallet.getStrClosingBalance());
															debitbasewalletTcsAmt.setStrAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getTcsAmount())));
															debitbasewalletTcsAmt.setStrTranMode(CommonConstants.DEBIT);
															debitbasewalletTcsAmt.setStrRef(linkedGlBaseWallet.getStrGLAccountDescription());
															debitbasewalletTcsAmt.setCurrencyCode(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
															debitbasewalletTcsAmt.setStrRef(linkedGlBaseWallet.getStrGLAccountDescription());
															glAccountStatementService.addGlAccountStatementData(debitbasewalletTcsAmt);
															
															//--------------> GL Account Statement CREDIT For TCS GL <----------------------------------
															  previousBalnce = Double.parseDouble(tcsCurrencyGL.getStrClosingBalance()) ;
																
															tcsCurrencyGL.setStrClosingBalance(String.valueOf(formatter.format(previousBalnce + loadMoneyAccountingRequest.getTcsAmount() )));
															glAccountTypeMasterService.updateGLAccountTypeDetails(tcsCurrencyGL);
														
															
															GLAccountStatement creditTcsGl = new GLAccountStatement();
															creditTcsGl.setStrTxnId(txnId);
															creditTcsGl.setStrTranType(loadMoneyAccountingRequest.getTxnType());
															creditTcsGl.setTransactionDate(Utils.getCurrentDate());
															creditTcsGl.setCreatedDate(Utils.getCurrentDate());
															creditTcsGl.setStrCreated_by(CommonConstants.SYSTEM);
															creditTcsGl.setStrTranType(loadMoneyAccountingRequest.getTxnType());
															creditTcsGl.setStrAccountNumber(tcsCurrencyGL.getStrAccountNumber());
															creditTcsGl.setStrGLAccountType(tcsCurrencyGL.getStrGLAccountType());
															creditTcsGl.setStrClosingBalance(tcsCurrencyGL.getStrClosingBalance());
															creditTcsGl.setStrAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getTcsAmount())));
															creditTcsGl.setStrTranMode(CommonConstants.CREDIT);
															creditTcsGl.setCurrencyCode(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
															creditTcsGl.setStrRef(tcsCurrencyGL.getStrGLAccountDescription());
															glAccountStatementService.addGlAccountStatementData(creditTcsGl);
															
															//Transaction Entry
															AccountTranMaster tranBaseWalletToTcsGl = new AccountTranMaster();
															tranBaseWalletToTcsGl.setStrTxn_id(txnId);
															tranBaseWalletToTcsGl.setStrLocal_tran_date(Utils.getCurrentDate());
															tranBaseWalletToTcsGl.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
															tranBaseWalletToTcsGl.setStrParticipantId(loadMoneyAccountingRequest.getParticipantId());
															tranBaseWalletToTcsGl.setSwitchTxDate(Utils.getCurrentDate());
															tranBaseWalletToTcsGl.setStrResponseCode("00");//?
															tranBaseWalletToTcsGl.setStrAuthCode(authCode);
															tranBaseWalletToTcsGl.setStrProcessingCode(transactionTypeModel.getStrProcessingCode());
															tranBaseWalletToTcsGl.setStrTran_type(loadMoneyAccountingRequest.getTxnType());
															tranBaseWalletToTcsGl.setStrTransaction_amount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getGstAmount())));
															tranBaseWalletToTcsGl.setStrFrom_account_number(baseWalletAccount.getStrAccountNumber());
															tranBaseWalletToTcsGl.setStrTo_account_number(tcsCurrencyGL.getStrAccountNumber());
															accountTranMasterService.addAccountTransactionData(tranBaseWalletToTcsGl);
															
															MultiCurrencyChargesReport multiCurrencyTcsReport = new MultiCurrencyChargesReport();
															multiCurrencyTcsReport.setAccountCurrency(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
															multiCurrencyTcsReport.setAccountNumber(loadMoneyAccountingRequest.getBaseWalletAccount().getStrAccountNumber());
															multiCurrencyTcsReport.setAccountType(loadMoneyAccountingRequest.getBaseWalletAccount().getStrAccountType());
															multiCurrencyTcsReport.setChargesType(CommonConstants.GST_TYPE);
															multiCurrencyTcsReport.setGlAccountNumber(creditTcsGl.getStrAccountNumber());
															multiCurrencyTcsReport.setGlAccountType(creditTcsGl.getStrGLAccountType());
															multiCurrencyTcsReport.setParticipantId(loadMoneyAccountingRequest.getParticipantId());
															multiCurrencyTcsReport.setTxnAmt(loadMoneyAccountingRequest.getTcsAmount());
															multiCurrencyTcsReport.setTxnDate(Utils.getCurrentDate());
															multiCurrencyTcsReport.setTxnId(txnId);
															multiCurrencyTcsReport.setTxnType(loadMoneyAccountingRequest.getTxnType());
															multiCurrencyTcsReport.setBaseConversionRate(loadMoneyAccountingRequest.getConversionRate());
															multiCurrencyTcsReport.setBaseCurrency(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
															multiCurrencyTcsReport.setBaseCurrencyAmt(loadMoneyAccountingRequest.getTcsAmount());
															multiCurrencyChargesReportService.saveMultiCurrencyChargeReport(multiCurrencyTcsReport);
															
															
														}
														
														//Debit From Base Wallet and Credit To Base Currency GL
														//--------------> Account Statement DEBIT For  Base Wallet Account  CREDIT BaseCurrencyGL  <----------------------------------
														 
														double decutingAllTaxsAmount =  loadMoneyAccountingRequest.getTotalAmount() - loadMoneyAccountingRequest.getFeeAmount() - loadMoneyAccountingRequest.getGstAmount() - loadMoneyAccountingRequest.getTcsAmount();
														String dectAllTaxAmt = String.valueOf( formatter.format( decutingAllTaxsAmount)) ;
														 
														  previousBalnce = Double.parseDouble(baseWalletAccount.getStrClosingBalance()) ;
															
														baseWalletAccount.setStrClosingBalance(String.valueOf(formatter.format(previousBalnce - Double.parseDouble(dectAllTaxAmt) )));
														accountMasterService.updateBaseWalletAccountMaster(baseWalletAccount);
														
														AccountStatement debitBaseWalletTotalAmt = new AccountStatement();
														debitBaseWalletTotalAmt.setStrTransactionType(loadMoneyAccountingRequest.getTxnType());	
														debitBaseWalletTotalAmt.setStrTransactionType(loadMoneyAccountingRequest.getTxnType());
														debitBaseWalletTotalAmt.setStrParticipantId(loadMoneyAccountingRequest.getParticipantId());
														debitBaseWalletTotalAmt.setStrIsGLType(CommonConstants.NO);
														debitBaseWalletTotalAmt.setTransactionDate(Utils.getCurrentDate());
														debitBaseWalletTotalAmt.setStrTransactionID(txnId);
														debitBaseWalletTotalAmt.setStrAccountNumber(baseWalletAccount.getStrAccountNumber());
														debitBaseWalletTotalAmt.setStrAccountType(baseWalletAccount.getStrAccountType());
														debitBaseWalletTotalAmt.setStrClosingBalance(String.valueOf(baseWalletAccount.getStrClosingBalance())); 
														debitBaseWalletTotalAmt.setStrTransactionAmount(String.valueOf(formatter.format(decutingAllTaxsAmount)));
														
														debitBaseWalletTotalAmt.setStrNaration(CommonConstants.DEBIT_NARRATION);
														debitBaseWalletTotalAmt.setCurrencyCode(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														
														debitBaseWalletTotalAmt.setStrTransactionMode(CommonConstants.DEBIT);
														accountStatementService.addAccountTransactionData(debitBaseWalletTotalAmt);
														
														
														//--------------> GL Account Statement DEBIT For GL  Base Wallet Account  <----------------------------------
														  previousBalnce = Double.parseDouble(linkedGlBaseWallet.getStrClosingBalance()) ;
															
														linkedGlBaseWallet.setStrClosingBalance(String.valueOf(formatter.format(previousBalnce - decutingAllTaxsAmount)));
														glAccountTypeMasterService.updateGLAccountTypeDetails(linkedGlBaseWallet); 
														
														GLAccountStatement debitGlBaseWalletTotalAmt = new GLAccountStatement();
														debitGlBaseWalletTotalAmt.setStrTranType(loadMoneyAccountingRequest.getTxnType());
														debitGlBaseWalletTotalAmt.setStrTxnId(txnId);
														debitGlBaseWalletTotalAmt.setTransactionDate(Utils.getCurrentDate());
														debitGlBaseWalletTotalAmt.setCreatedDate(Utils.getCurrentDate());
														debitGlBaseWalletTotalAmt.setStrCreated_by(CommonConstants.SYSTEM);
														debitGlBaseWalletTotalAmt.setStrTranType(loadMoneyAccountingRequest.getTxnType());
														debitGlBaseWalletTotalAmt.setStrAccountNumber(linkedGlBaseWallet.getStrAccountNumber());
														debitGlBaseWalletTotalAmt.setStrGLAccountType(linkedGlBaseWallet.getStrGLAccountType());
														debitGlBaseWalletTotalAmt.setStrClosingBalance(linkedGlBaseWallet.getStrClosingBalance());
														debitGlBaseWalletTotalAmt.setStrAmount(String.valueOf(formatter.format(decutingAllTaxsAmount)));
														debitGlBaseWalletTotalAmt.setStrTranMode(CommonConstants.DEBIT);
														debitGlBaseWalletTotalAmt.setCurrencyCode(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														debitGlBaseWalletTotalAmt.setStrRef(linkedGlBaseWallet.getStrGLAccountDescription());
														glAccountStatementService.addGlAccountStatementData(debitGlBaseWalletTotalAmt);
														
														//.....
														//--------------> GL Account Statement CREDIT For Base Currency GL <----------------------------------
														 previousBalnce = Double.parseDouble(baseCurrencyGL.getStrClosingBalance()) ;
															
														baseCurrencyGL.setStrClosingBalance(String.valueOf(formatter.format(previousBalnce + decutingAllTaxsAmount)));
														glAccountTypeMasterService.updateGLAccountTypeDetails(baseCurrencyGL);
													
														GLAccountStatement creditBaseCurrencyGL = new GLAccountStatement();
														creditBaseCurrencyGL.setStrTxnId(txnId);
														creditBaseCurrencyGL.setStrTranType(loadMoneyAccountingRequest.getTxnType());
														creditBaseCurrencyGL.setTransactionDate(Utils.getCurrentDate());
														creditBaseCurrencyGL.setCreatedDate(Utils.getCurrentDate());
														creditBaseCurrencyGL.setStrCreated_by(CommonConstants.SYSTEM);
														creditBaseCurrencyGL.setStrTranType(loadMoneyAccountingRequest.getTxnType());
														creditBaseCurrencyGL.setStrAccountNumber(baseCurrencyGL.getStrAccountNumber());
														creditBaseCurrencyGL.setStrGLAccountType(baseCurrencyGL.getStrGLAccountType());
														creditBaseCurrencyGL.setStrClosingBalance(baseCurrencyGL.getStrClosingBalance());
														creditBaseCurrencyGL.setStrAmount(String.valueOf(formatter.format(decutingAllTaxsAmount)));
														creditBaseCurrencyGL.setStrTranMode(CommonConstants.CREDIT);
														creditBaseCurrencyGL.setCurrencyCode(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														creditBaseCurrencyGL.setStrRef(baseCurrencyGL.getStrGLAccountDescription());
														glAccountStatementService.addGlAccountStatementData(creditBaseCurrencyGL);
														
														//Transaction Entry
														AccountTranMaster tranBaseWalletToCurrencyGl = new AccountTranMaster();
														tranBaseWalletToCurrencyGl.setStrTxn_id(txnId);
														tranBaseWalletToCurrencyGl.setStrLocal_tran_date(Utils.getCurrentDate());
														tranBaseWalletToCurrencyGl.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
														tranBaseWalletToCurrencyGl.setStrParticipantId(loadMoneyAccountingRequest.getParticipantId());
														tranBaseWalletToCurrencyGl.setSwitchTxDate(Utils.getCurrentDate());
														tranBaseWalletToCurrencyGl.setStrResponseCode("00");//?
														tranBaseWalletToCurrencyGl.setStrAuthCode(authCode);
														tranBaseWalletToCurrencyGl.setStrProcessingCode(transactionTypeModel.getStrProcessingCode());
														tranBaseWalletToCurrencyGl.setStrTran_type(loadMoneyAccountingRequest.getTxnType());
														tranBaseWalletToCurrencyGl.setStrTransaction_amount(String.valueOf(formatter.format(decutingAllTaxsAmount)));
														tranBaseWalletToCurrencyGl.setStrFrom_account_number(baseWalletAccount.getStrAccountNumber());
														tranBaseWalletToCurrencyGl.setStrTo_account_number(baseCurrencyGL.getStrAccountNumber());
														tranBaseWalletToCurrencyGl.setStrParticipantId(loadMoneyAccountingRequest.getParticipantId());
														accountTranMasterService.addAccountTransactionData(tranBaseWalletToCurrencyGl);
														
														
														//TransaferOUT Transaction
														AccountTranMaster transferOut = new AccountTranMaster();
														transferOut.setStrTxn_id(txnId);
														transferOut.setStrLocal_tran_date(Utils.getCurrentDate());
														transferOut.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
														transferOut.setStrParticipantId(loadMoneyAccountingRequest.getParticipantId());
														transferOut.setSwitchTxDate(Utils.getCurrentDate());
														transferOut.setStrResponseCode("00");//?
														transferOut.setStrAuthCode(authCode);
														transferOut.setStrProcessingCode(transactionTypeModel.getStrProcessingCode());
														transferOut.setStrTran_type("TOT");
														
														transferOut.setStrTransaction_amount(String.valueOf(formatter.format(decutingAllTaxsAmount)));
														transferOut.setStrAccountNumber(baseCurrencyGL.getStrAccountNumber());
														accountTranMasterService.addAccountTransactionData(transferOut);
														
														//TransaferIN Transaction
														AccountTranMaster transferIn = new AccountTranMaster();
														transferIn.setStrTxn_id(txnId);
														transferIn.setStrLocal_tran_date(Utils.getCurrentDate());
														transferIn.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
														transferIn.setStrParticipantId(loadMoneyAccountingRequest.getParticipantId());
														transferIn.setSwitchTxDate(Utils.getCurrentDate());
														transferIn.setStrResponseCode("00");//?
														transferIn.setStrAuthCode(authCode);
														transferIn.setStrProcessingCode(transactionTypeModel.getStrProcessingCode());//?
														transferIn.setStrTran_type("TIN");
														transferIn.setStrTransaction_amount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getLoadMoneyRequest().getLoadAmount())));
														transferIn.setStrAccountNumber(walletCurrencyGL.getStrAccountNumber());
														accountTranMasterService.addAccountTransactionData(transferIn);
														
														
														//Entry in SO and SI 
														CurrencyTransferMaster currencyTransferMaster = new CurrencyTransferMaster();
														currencyTransferMaster.setCreatedBy(CommonConstants.SYSTEM);
														currencyTransferMaster.setCreatedDate(new Date());
														currencyTransferMaster.setSweepInAmount(String.valueOf(loadMoneyAccountingRequest.getLoadMoneyRequest().getLoadAmount()));
														currencyTransferMaster.setSweepInCurrencyCode(loadMoneyAccountingRequest.getCurrencyWalletAccount().getStrCurrencyCode());
														currencyTransferMaster.setSweepInCurrencyValue(String.valueOf(loadMoneyAccountingRequest.getConversionRate()));
														currencyTransferMaster.setSweepOutAmount(String.valueOf(decutingAllTaxsAmount));
														currencyTransferMaster.setSweepOutCurrencyCode(baseWalletAccount.getCurrencyCode());
														currencyTransferMaster.setSweepOutCurrencyValue(String.valueOf(loadMoneyAccountingRequest.getConversionRate()));
														currencyTransferMaster.setTranId(txnId);
														currencyTransferMaster.setFeeAmount(loadMoneyAccountingRequest.getFeeAmount());
														currencyTransferMaster.setGstAmount(loadMoneyAccountingRequest.getGstAmount());
														currencyTransferMaster.setBaseAccountNumber(loadMoneyAccountingRequest.getBaseWalletAccount().getStrAccountNumber());
														currencyTransferMaster.setBaseAccountType(loadMoneyAccountingRequest.getBaseWalletAccount().getStrAccountType());
														currencyTransferMasterService.addEntryInCurrencyTransferMaster(currencyTransferMaster);
														
														
														//Debit From Currncy Wallet GL
														 previousBalnce = Double.parseDouble(walletCurrencyGL.getStrClosingBalance()) ;
															
														walletCurrencyGL.setStrClosingBalance(String.valueOf(formatter.format(previousBalnce - loadMoneyAccountingRequest.getLoadMoneyRequest().getLoadAmount())));
														glAccountTypeMasterService.updateGLAccountTypeDetails(walletCurrencyGL);
													
														
														GLAccountStatement debitCurrencyWalletGl = new GLAccountStatement();
														debitCurrencyWalletGl.setStrTranType(loadMoneyAccountingRequest.getTxnType());
														debitCurrencyWalletGl.setStrTxnId(txnId);
														debitCurrencyWalletGl.setTransactionDate(Utils.getCurrentDate());
														debitCurrencyWalletGl.setCreatedDate(Utils.getCurrentDate());
														debitCurrencyWalletGl.setStrCreated_by(CommonConstants.SYSTEM);
														debitCurrencyWalletGl.setStrTranType(loadMoneyAccountingRequest.getTxnType());
														debitCurrencyWalletGl.setStrAccountNumber(walletCurrencyGL.getStrAccountNumber());
														debitCurrencyWalletGl.setStrGLAccountType(walletCurrencyGL.getStrGLAccountType());
														debitCurrencyWalletGl.setStrClosingBalance(walletCurrencyGL.getStrClosingBalance());
														debitCurrencyWalletGl.setStrAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getLoadMoneyRequest().getLoadAmount())));
														debitCurrencyWalletGl.setStrTranMode(CommonConstants.DEBIT);
														debitCurrencyWalletGl.setCurrencyCode(loadMoneyAccountingRequest.getBaseWalletAccount().getCurrencyCode());
														debitCurrencyWalletGl.setStrRef(walletCurrencyGL.getStrGLAccountDescription());
														glAccountStatementService.addGlAccountStatementData(debitCurrencyWalletGl);
														
														//Credit in Currncy Wallet Account 
														
														//--------------> Account Statement CREDIT  For  Base Wallet Account  <----------------------------------
														MultiCurrencyWalletAccountMaster currencyWalletAccount = loadMoneyAccountingRequest.getCurrencyWalletAccount();
														
														currencyWalletAccount.setStrClosingBalance(currencyWalletAccount.getStrClosingBalance() + loadMoneyAccountingRequest.getLoadMoneyRequest().getLoadAmount() );
														multiCurrencyWalletAccountService.updateMultiCurrencyWalletAccountMaster(currencyWalletAccount);

														AccountStatement creditCurrencyBaseWalletAmt = new AccountStatement();
														creditCurrencyBaseWalletAmt.setStrTransactionType(loadMoneyAccountingRequest.getTxnType());
														creditCurrencyBaseWalletAmt.setStrParticipantId(loadMoneyAccountingRequest.getParticipantId());
														creditCurrencyBaseWalletAmt.setStrIsGLType(CommonConstants.NO);
														creditCurrencyBaseWalletAmt.setTransactionDate(Utils.getCurrentDate());
														creditCurrencyBaseWalletAmt.setStrTransactionID(txnId);
														creditCurrencyBaseWalletAmt.setStrAccountNumber(currencyWalletAccount.getStrCurrencyWalletAccountNumber());
														creditCurrencyBaseWalletAmt.setStrAccountType(currencyWalletAccount.getStrAccountType());
														creditCurrencyBaseWalletAmt.setStrClosingBalance(String.valueOf(currencyWalletAccount.getStrClosingBalance()));
														creditCurrencyBaseWalletAmt.setStrTransactionAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getLoadMoneyRequest().getLoadAmount())));
														creditCurrencyBaseWalletAmt.setStrNaration(CommonConstants.CREDIT_NARATION);
														creditCurrencyBaseWalletAmt.setCurrencyCode(loadMoneyAccountingRequest.getCurrencyWalletAccount().getStrCurrencyCode());
														creditCurrencyBaseWalletAmt.setStrTransactionMode(CommonConstants.CREDIT);
														accountStatementService.addAccountTransactionData(creditCurrencyBaseWalletAmt);
														
														
														//--------------> GL Account Statement CREDIT For  GL Base Wallet Account  <----------------------------------
													//	String linkedCurrencyWalletBalance = linkedGlCurrencyWallet.getStrClosingBalance();
														 previousBalnce = Double.parseDouble(linkedGlCurrencyWallet.getStrClosingBalance()) ;	
														linkedGlCurrencyWallet.setStrClosingBalance(String.valueOf(formatter.format(previousBalnce +  loadMoneyAccountingRequest.getLoadMoneyRequest().getLoadAmount())));
														glAccountTypeMasterService.updateGLAccountTypeDetails(linkedGlCurrencyWallet);
														
														
														GLAccountStatement creditLinkedCurrencyGl = new GLAccountStatement();
														creditLinkedCurrencyGl.setStrTxnId(txnId);
														creditLinkedCurrencyGl.setStrTranType(loadMoneyAccountingRequest.getTxnType());
														creditLinkedCurrencyGl.setTransactionDate(Utils.getCurrentDate());
														creditLinkedCurrencyGl.setCreatedDate(Utils.getCurrentDate());
														creditLinkedCurrencyGl.setStrCreated_by(CommonConstants.SYSTEM);
														creditLinkedCurrencyGl.setStrTranType(loadMoneyAccountingRequest.getTxnType());
														creditLinkedCurrencyGl.setStrAccountNumber(linkedGlCurrencyWallet.getStrAccountNumber());
														creditLinkedCurrencyGl.setStrGLAccountType(linkedGlCurrencyWallet.getStrGLAccountType());
														creditLinkedCurrencyGl.setStrClosingBalance(linkedGlCurrencyWallet.getStrClosingBalance());
														creditLinkedCurrencyGl.setStrAmount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getLoadMoneyRequest().getLoadAmount())));
														creditLinkedCurrencyGl.setStrTranMode(CommonConstants.CREDIT);
														creditLinkedCurrencyGl.setCurrencyCode(loadMoneyAccountingRequest.getCurrencyWalletAccount().getStrCurrencyCode());
														creditLinkedCurrencyGl.setStrRef(linkedGlCurrencyWallet.getStrGLAccountDescription());
														glAccountStatementService.addGlAccountStatementData(creditLinkedCurrencyGl);
														
														AccountTranMaster tranWalletCurrencyGlToWalletCurrency = new AccountTranMaster();
														tranWalletCurrencyGlToWalletCurrency.setStrTxn_id(txnId);
														tranWalletCurrencyGlToWalletCurrency.setStrLocal_tran_date(Utils.getCurrentDate());
														tranWalletCurrencyGlToWalletCurrency.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
														tranWalletCurrencyGlToWalletCurrency.setStrParticipantId(loadMoneyAccountingRequest.getParticipantId());
														tranWalletCurrencyGlToWalletCurrency.setSwitchTxDate(Utils.getCurrentDate());
														tranWalletCurrencyGlToWalletCurrency.setStrResponseCode("00");//?
														tranWalletCurrencyGlToWalletCurrency.setStrProcessingCode(transactionTypeModel.getStrProcessingCode());//?
														tranWalletCurrencyGlToWalletCurrency.setStrAuthCode(authCode);
														tranWalletCurrencyGlToWalletCurrency.setStrTran_type(loadMoneyAccountingRequest.getTxnType());
														tranWalletCurrencyGlToWalletCurrency.setStrTransaction_amount(String.valueOf(formatter.format(loadMoneyAccountingRequest.getLoadMoneyRequest().getLoadAmount())));
														tranWalletCurrencyGlToWalletCurrency.setStrFrom_account_number(walletCurrencyGL.getStrAccountNumber());
														tranWalletCurrencyGlToWalletCurrency.setStrTo_account_number(currencyWalletAccount.getStrCurrencyWalletAccountNumber());
														accountTranMasterService.addAccountTransactionData(tranWalletCurrencyGlToWalletCurrency);
														
														}else {
															processResponse.setCode("E0000");
															processResponse.setStatus("Failed");
															processResponse.setMessage("Transaction Type Not Found");
														}
																						
													}else {
														processResponse.setCode("E0000");
														processResponse.setStatus("Failed");
														processResponse.setMessage("Currency Wallet Linked Gl Account Not Found ");
											}
													}else {
														processResponse.setCode("E0000");
														processResponse.setStatus("Failed");
														processResponse.setMessage("Currency Wallet Account Type  Not Found ");
													}

												}else {
													processResponse.setCode("E0000");
													processResponse.setStatus("Failed");
													processResponse.setMessage(" Currency Wallet  Not Found ");	
												}
												
											}else {
												processResponse.setCode("E0000");
												processResponse.setStatus("Failed");
												processResponse.setMessage(" Currency Wallet Gl Not Found ");	
											}

										}else {
											processResponse.setCode("E0000");
											processResponse.setStatus("Failed");
											processResponse.setMessage(" Currency Wallet Not Found For Currency Wallet ");
										}
									}else {
										
										processResponse.setCode("E0000");
										processResponse.setStatus("Failed");
										processResponse.setMessage("Base Currency  GL Not Found ");
									}

								}else {
									processResponse.setCode("E0000");
									processResponse.setStatus("Failed");
									processResponse.setMessage("Base Currency TCS GL Not Found ");
								}
								}else {
									processResponse.setCode("E0000");
									processResponse.setStatus("Failed");
									processResponse.setMessage("Tcs Type Not Found ");
								}
								

							}else {
								processResponse.setCode("E0000");
								processResponse.setStatus("Failed");
								processResponse.setMessage("Base Currency GST GL Not Found ");
							}
						}else {
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Base Currency Fee GL Not Found ");
						}



						}else
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Base Account Gl Not Found ");
						}
					}else {
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Base Wallet Account Type Not Found ");
					}	
				}else {
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Base Wallet Account Not Found ");
				}
			}else {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Channel Gl  Not Found ");
			}
			
			}else {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Channel Not Found");
				
			}
				
		}catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			
		}
		return processResponse;
	}

	@Override
	public ProcessResponse getLoadMoneyPayStructure(LoadMoneyRequest loadMoneyRequest) {
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			 double feeAmt = 0.00 ;
			 double gstAmt = 0.00;
			 
			 if(loadMoneyRequest.getBaseWallet() != null)
		{	
				 if(loadMoneyRequest.getLoadAmount() > 0) {
			loadMoneyRequest.setChannel("NB");
			
			MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster = new MultiCurrencyWalletAccountMaster();
			
		// Check  Base Wallet is There Or Not In Table.----->> AccountMaster By AccountNumber
		AccountMaster baseWallet = new AccountMaster();
		baseWallet.setStrAccountNumber(loadMoneyRequest.getBaseWallet());
		 baseWallet = accountMasterService.getBaseWalletAccount(baseWallet);
		if(baseWallet != null && baseWallet.getStrAccountNumber() != null) 
		{
			// Check  For This Base Wallet "isMultiCurrencySupported"--> "Y"/"N"
			AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
			accountTypeMaster.setStrAccountType(baseWallet.getStrAccountType());
			accountTypeMaster = accountTypeMasterService.getAccountTypeObject(accountTypeMaster);
			if("Y".equalsIgnoreCase(accountTypeMaster.getIsMultiCurrencySupport())) {
				
				//Check for isLRS Applied
				if("Y".equalsIgnoreCase(accountTypeMaster.getIsLrs())) {
					
				//Check for Channel is COnfiger Or Not 
				//Get TCS And LRS Values Based On Account Type
				AcTypeLrsTcsMaster acTypeLrsTcsMaster = new AcTypeLrsTcsMaster();
				acTypeLrsTcsMaster.setStrAccountType(baseWallet.getStrAccountType());
				acTypeLrsTcsMaster.setStrChannelCode(loadMoneyRequest.getChannel());
				acTypeLrsTcsMaster = acTypeLrsTcsMasterService.getAccountTypeLrsAndTcs(acTypeLrsTcsMaster);
				if(acTypeLrsTcsMaster != null)
				{
				
				//Get Wallet Account By Currency Code--------------->  [CURRENCY WALLET]
					multiCurrencyWalletAccountMaster.setStrAccountNumber(baseWallet.getStrAccountNumber());
					multiCurrencyWalletAccountMaster.setBaseCurrencyAccountType(baseWallet.getStrAccountType());
					multiCurrencyWalletAccountMaster.setStrCurrencyCode(loadMoneyRequest.getCurrencyCode());
					MultiCurrencyWalletAccountMaster currencyWallet = multiCurrencyWalletAccountService.getMultiCurrencyWalletAccount(multiCurrencyWalletAccountMaster);
					if(currencyWallet != null && currencyWallet.getStrCurrencyWalletAccountNumber() != null)
					{
						//Find Currency Master For TCS and Gst Calculation
						CurrencyMaster currencyMaster = new CurrencyMaster();
						currencyMaster.setCurrencyCode(baseWallet.getCurrencyCode());
						currencyMaster = currencyMasterService.getCurrencyMasterByCurrencyCode(currencyMaster);
						if(currencyMaster !=null ) {
							
							//check the Currency is Base Currency Or Not 
							if("Y".equalsIgnoreCase(currencyMaster.getBaseCountry())) {
		
						//Convert LoadedMoney To INR Value	  
						CurrencyConversionMaster currencyConversionMaster = new CurrencyConversionMaster();
						currencyConversionMaster.setFromCurrency(currencyWallet.getStrCurrencyCode());
						currencyConversionMaster.setToCurrency(baseWallet.getCurrencyCode());
						currencyConversionMaster = conversionMasterService.getCurrencyConverionValue(currencyConversionMaster);
						if(currencyConversionMaster != null)
						{
							
							double convertedINRValue =  Double.parseDouble(String.valueOf(formatter.format(loadMoneyRequest.getLoadAmount() * currencyConversionMaster.getFromCurrencyValue()) ));
							
							//FEE And GST Calculation Start
							MultiCurrencyFeeTypeMaster multiCurrencyFeeTypeMaster = new MultiCurrencyFeeTypeMaster();
							multiCurrencyFeeTypeMaster.setFeeType(currencyMaster.getFeeType());
							multiCurrencyFeeTypeMaster =multiCurrencyFeeTypeMasterService.getFeeTypeDetails(multiCurrencyFeeTypeMaster);
							
							if(multiCurrencyFeeTypeMaster != null)
							{
								
							//Getting GstType Based On Fee Type
								GstTypeMaster gstTypeMaster = new GstTypeMaster();
								gstTypeMaster.setGstType(multiCurrencyFeeTypeMaster.getGstType());
								gstTypeMaster = gstTypeMasterService.getGstTypeMasterByGstType(gstTypeMaster);
								if(gstTypeMaster != null) {
							
								if("Y".equalsIgnoreCase(multiCurrencyFeeTypeMaster.getIsFlatFee()))
								{
							
									feeAmt =  multiCurrencyFeeTypeMaster.getFeeAmt();
									gstAmt = Double.parseDouble(String.valueOf(formatter.format(feeAmt  * gstTypeMaster.getGstPercentage() / 100)));
							
								}
								else if("Y".equalsIgnoreCase(multiCurrencyFeeTypeMaster.getIsPercentageFee())) 
								{
							
									feeAmt =  Double.parseDouble(String.valueOf(formatter.format(convertedINRValue * gstTypeMaster.getGstPercentage())));
									gstAmt = Double.parseDouble(String.valueOf(formatter.format(feeAmt  * gstTypeMaster.getGstPercentage() / 100)));
								}
								//FEE And GST Calculation End
			
									double lrsLimitBasedOnAccountType = acTypeLrsTcsMaster.getStrLrs();
									double tcsPerecentage = acTypeLrsTcsMaster.getStrTcs();
		  
									String financialYear ;
									String nextYear;
									
									
									
									int CurrentMonth = Calendar.getInstance().get(Calendar.MONTH);
									if(CurrentMonth==3) {
									
										 financialYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
										 nextYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR) + 1);	
										
									}else {
										 financialYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - 1);
										 nextYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
									}
									
						  
									//Checking LRS Limit  Of the year
									MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMaster = new MultiCurrencyFinancialYearMaster();
									multiCurrencyFinancialYearMaster.setAccountNumber(baseWallet.getStrAccountNumber());
									multiCurrencyFinancialYearMaster.setFinancialYear(String.valueOf(financialYear.substring(2)+"-"+nextYear.substring(2)));
									multiCurrencyFinancialYearMaster.setChannelCode(acTypeLrsTcsMaster.getStrChannelCode());
									multiCurrencyFinancialYearMaster.setAccountType(baseWallet.getStrAccountType());
									multiCurrencyFinancialYearMaster = multiCurrencyFinancialYearMasterService.getFinancialYearNyAccountNumber(multiCurrencyFinancialYearMaster);
									
									if(multiCurrencyFinancialYearMaster != null)
									{
							
										double totalAmt = multiCurrencyFinancialYearMaster.getTotalLoaded() + convertedINRValue;
										double loadAmt = convertedINRValue;
										double balanceLRS = lrsLimitBasedOnAccountType;
										double totalLRSConsumed =0;
										double totalLoaded =multiCurrencyFinancialYearMaster.getTotalLoaded();
										double totalExcessLoading =0;
										double tcsOn =0;
										double totalTcsOn =0;
										String totalAmountWithCharges ;
										if(multiCurrencyFinancialYearMaster.getAvailableLrsLimit()>0) {
											balanceLRS = multiCurrencyFinancialYearMaster.getAvailableLrsLimit();
										}
										
										// 1 Part - if LRS is Breached Or not 
										if( totalAmt > lrsLimitBasedOnAccountType) 
										{	 
											// LRS is BREACHED.....
							  
											// Check is TCS Applied Previously or Not 	
											if(multiCurrencyFinancialYearMaster.getTotalTcsOn()>0)
											{
												totalTcsOn = multiCurrencyFinancialYearMaster.getTotalTcsOn();
												loadAmt = convertedINRValue;
												balanceLRS = 0 ;
												totalLRSConsumed = lrsLimitBasedOnAccountType ;    //LoadAmt + PReviouseLoadAmt
												totalLoaded =  Double.parseDouble(String.valueOf(formatter.format(totalLoaded + loadAmt))) ;
							  
												totalExcessLoading = Double.parseDouble(String.valueOf(formatter.format(totalLoaded  - lrsLimitBasedOnAccountType)));
												tcsOn = Double.parseDouble(String.valueOf(formatter.format(totalExcessLoading -multiCurrencyFinancialYearMaster.getTotalTcsOn())));
												totalTcsOn = Double.parseDouble(String.valueOf(formatter.format(totalTcsOn + tcsOn)));
								  
								  
												double tcsAmt = Double.parseDouble(String.valueOf(formatter.format(tcsOn * tcsPerecentage)));
												double totalAmount = Double.parseDouble(String.valueOf(formatter.format(convertedINRValue + feeAmt + tcsAmt + gstAmt)));
												totalAmountWithCharges = formatter.format(totalAmount);
												
											
				  
											}
												else 
											{
													// 1 Time TCS Will be Applied 
													loadAmt = convertedINRValue;
													balanceLRS = 0 ;
													totalLRSConsumed = lrsLimitBasedOnAccountType ;    //LoadAmt + PReviouseLoadAmt
													totalLoaded =  Double.parseDouble(String.valueOf(formatter.format(totalLoaded + loadAmt))) ;
				  
													totalExcessLoading = Double.parseDouble(String.valueOf(formatter.format(totalLoaded  - lrsLimitBasedOnAccountType)));
													tcsOn = Double.parseDouble(String.valueOf(formatter.format(totalExcessLoading - tcsOn)));
													totalTcsOn = Double.parseDouble(String.valueOf(formatter.format(totalTcsOn + tcsOn)));
					  
					  
													double tcsAmt = Double.parseDouble(String.valueOf(formatter.format( tcsOn * tcsPerecentage)));
					  
													//CalculateTotal Amount To Be Recived 
													double totalAmount = Double.parseDouble(String.valueOf(formatter.format(convertedINRValue + feeAmt + tcsAmt + gstAmt)));
													totalAmountWithCharges = formatter.format(totalAmount);
				  
											}
		
										}else 
										{
											// LRS is Not Breeched...
			  
											//Calculation For LoadMoneyTransaction
											double totalINRValue = Double.parseDouble(String.valueOf(formatter.format(convertedINRValue + feeAmt + gstAmt)))  ;
											 totalAmountWithCharges = formatter.format(totalINRValue);
											
											loadAmt = convertedINRValue;
											balanceLRS = Double.parseDouble(String.valueOf(formatter.format( balanceLRS - loadAmt)));  
											totalLRSConsumed = Double.parseDouble(String.valueOf(formatter.format(loadAmt + totalLoaded))) ;    
											totalLoaded = Double.parseDouble(String.valueOf(formatter.format(loadAmt + totalLoaded)));
											  
											
			  
										}
										
										LoadMoneyPayStructure loadMoneyPayStructure = new LoadMoneyPayStructure();
										loadMoneyPayStructure.setFeeAmount(String.valueOf(formatter.format(feeAmt)));
										loadMoneyPayStructure.setGstAmount(String.valueOf(formatter.format(gstAmt)));
										loadMoneyPayStructure.setLoadAmount(String.valueOf(formatter.format(loadMoneyRequest.getLoadAmount())));
										loadMoneyPayStructure.setTcsAmount(String.valueOf(formatter.format(tcsOn)));
										loadMoneyPayStructure.setTotalInrRequired(String.valueOf(formatter.format(convertedINRValue)));
										loadMoneyPayStructure.setTransferToCurrency(loadMoneyRequest.getCurrencyCode());
										
										processResponse.setCode("S0000");
										processResponse.setStatus("Scussed");
										processResponse.setMessage("Data Fetch Scussefully");
										processResponse.setLoadMoneyPayStructure(loadMoneyPayStructure);
									}
									else
									{
										processResponse.setCode("E0000");
										processResponse.setStatus("Failed");
										processResponse.setMessage("Finicial Year Data Not Found");
									}
									
							}else
							{
							  processResponse.setCode("E0000");
							  processResponse.setStatus("Failed");
								 processResponse.setMessage("Gst Type Data Not Found");
							}
									
							}else
							{
							  processResponse.setCode("E0000");
							  processResponse.setStatus("Failed");
								 processResponse.setMessage("Fee Type Data Not Found");
							}
						}else
						{ 
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("No Conversion Currency Found");
						}
						}else {
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Currency is Not Base Currency");
						}
						}
						else {
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("No Currency Master Found");
						}
					}else 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Currency Wallet Type Not Found .");
					}
					
				
			}else {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Channel Not Configured"); 
			}
			}else {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Lrs Not Configerd For This Account Type");
			}
			  }else 
			  {
				  processResponse.setCode("E0000");
				  processResponse.setStatus("Failed");
				  processResponse.setMessage("Multi-Currency Not Supported For This Account Type");
			  }	  
		}
		else 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Base Wallet Account Not Found .");
		}
		
		}else {
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Amout Should Be GreaterThan Zero");
		}
		
			}else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Please Provide Base Wallet.");
			}
			}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			
		}
		return processResponse;
	}






	@Override
	public ProcessResponse transferCurrency(TransaferCurrencyRequest transferCurrency) {
	 
		ProcessResponse processResponse = new ProcessResponse();
		//chek From Wallet Account is there or not
		
		double feeAmt;
		double gstAmt;
		if(transferCurrency.getFromAccount() != null) {
			if(transferCurrency.getToAccount() != null) {
				if(transferCurrency.getTxnAmt() != null ) {
					
					//From Currency Wallet
					MultiCurrencyWalletAccountMaster multiCurrencyFromWalletAccountMaster = new MultiCurrencyWalletAccountMaster();
					multiCurrencyFromWalletAccountMaster.setStrCurrencyWalletAccountNumber(transferCurrency.getFromAccount());
					multiCurrencyFromWalletAccountMaster = multiCurrencyWalletAccountService.getFromToMultiCurrencyWalletAccount(multiCurrencyFromWalletAccountMaster);
					if(multiCurrencyFromWalletAccountMaster != null && multiCurrencyFromWalletAccountMaster.getStrCurrencyWalletAccountNumber() != null)
					{
						
						//To Currency Wallet
						MultiCurrencyWalletAccountMaster multiCurrencyToWalletAccountMaster = new MultiCurrencyWalletAccountMaster();
						multiCurrencyToWalletAccountMaster.setStrCurrencyWalletAccountNumber(transferCurrency.getFromAccount());
						multiCurrencyToWalletAccountMaster  = multiCurrencyWalletAccountService.getFromToMultiCurrencyWalletAccount(multiCurrencyToWalletAccountMaster);
						if(multiCurrencyToWalletAccountMaster != null && multiCurrencyToWalletAccountMaster.getStrCurrencyWalletAccountNumber() != null)
						{
							//Find Currency Master For TCS and Gst Calculation
							CurrencyMaster fromCurrencyMaster = new CurrencyMaster();
							fromCurrencyMaster.setCurrencyCode(multiCurrencyFromWalletAccountMaster.getStrCurrencyCode());
							fromCurrencyMaster = currencyMasterService.getCurrencyMasterByCurrencyCode(fromCurrencyMaster);
							if(fromCurrencyMaster !=null ) 
							{
						
								//Find Currency Master For TCS and Gst Calculation
								CurrencyMaster toCurrencyMaster = new CurrencyMaster();
								toCurrencyMaster.setCurrencyCode(multiCurrencyFromWalletAccountMaster.getStrCurrencyCode());
								toCurrencyMaster = currencyMasterService.getCurrencyMasterByCurrencyCode(toCurrencyMaster);
								if(toCurrencyMaster !=null ) 
								{
									
									//Convert LoadedMoney To INR Value	  
									CurrencyConversionMaster currencyConversionMaster = new CurrencyConversionMaster();
									currencyConversionMaster.setFromCurrency(fromCurrencyMaster.getCurrencyCode());
									currencyConversionMaster.setToCurrency(toCurrencyMaster.getCurrencyCode());
									currencyConversionMaster = conversionMasterService.getCurrencyConverionValue(currencyConversionMaster);
									if(currencyConversionMaster != null)
									{
						
										double txnAmt = Double.parseDouble(transferCurrency.getTxnAmt());
										double convertedINRValue =  Double.parseDouble(String.valueOf(formatter.format(txnAmt * currencyConversionMaster.getFromCurrencyValue()) ));
						
										//FEE And GST Calculation Start
										MultiCurrencyFeeTypeMaster multiCurrencyFeeTypeMaster = new MultiCurrencyFeeTypeMaster();
										multiCurrencyFeeTypeMaster.setFeeType(fromCurrencyMaster.getFeeType());
										multiCurrencyFeeTypeMaster =multiCurrencyFeeTypeMasterService.getFeeTypeDetails(multiCurrencyFeeTypeMaster);
										
										if(multiCurrencyFeeTypeMaster != null)
										{
											
										//Getting GstType Based On Fee Type
											GstTypeMaster gstTypeMaster = new GstTypeMaster();
											gstTypeMaster.setGstType(multiCurrencyFeeTypeMaster.getGstType());
											gstTypeMaster = gstTypeMasterService.getGstTypeMasterByGstType(gstTypeMaster);
											if(gstTypeMaster != null) {
										
											
											if("Y".equalsIgnoreCase(multiCurrencyFeeTypeMaster.getIsFlatFee()))
											{
										
												feeAmt =  multiCurrencyFeeTypeMaster.getFeeAmt();
												gstAmt = Double.parseDouble(String.valueOf(formatter.format(feeAmt  * gstTypeMaster.getGstPercentage() / 100)));
										
											}
											else if("Y".equalsIgnoreCase(multiCurrencyFeeTypeMaster.getIsPercentageFee())) 
											{
										
												feeAmt =  Double.parseDouble(String.valueOf(formatter.format(convertedINRValue * gstTypeMaster.getGstPercentage())));
												gstAmt = Double.parseDouble(String.valueOf(formatter.format(feeAmt  * gstTypeMaster.getGstPercentage() / 100)));
											}
											//FEE And GST Calculation End
											}
											
											//entries
											
										
									}
						}
						}else {
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Currency Not Found");
						}
					}else {
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Currency Not Found");
					}
						}else {
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("To Wallet Not Found");
						}
					
					}else {
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("From Wallet Not Found");	
					}
					
					
				}else {
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Please Provide To txnAmt");	
				}
				
			}else {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Please Provide To Wallet");
			}
			
			
		}else {
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Please Provide From Wallet");
		}
		return null;
	}


}
