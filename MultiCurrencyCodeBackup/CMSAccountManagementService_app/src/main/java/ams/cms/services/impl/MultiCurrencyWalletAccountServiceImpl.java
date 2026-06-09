package ams.cms.services.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.model.WalletAccountResponse;
import ams.cms.dao.AccountMasterDao;
import ams.cms.dao.MultiCurrencyWalletAccountDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.CurrencyConversionMaster;
import ams.cms.model.CurrencyConversionRateRequest;
import ams.cms.model.MultiCurrencyWalletAccountMaster;
import ams.cms.model.MultiCurrencyWalletAccountStatementList;
import ams.cms.model.MultiCurrencyWalletAccountTypeMaster;
import ams.cms.model.PriorityWalletResponse;
import ams.cms.services.CurrencyConversionMasterService;
import ams.cms.services.MultiCurrencyWalletAccountService;
import ams.cms.util.ProcessResponse;
import ams.cms.util.ProcessWebResponse;

@Transactional
@Service
public class MultiCurrencyWalletAccountServiceImpl implements MultiCurrencyWalletAccountService 
{
	
	private AMSLogger amsLogger = AMSLogger.getInstance(MultiCurrencyWalletAccountServiceImpl.class);
	
	@Autowired
	private MultiCurrencyWalletAccountDao multiCurrencyWalletAccountDao;
	
	@Autowired
	private CurrencyConversionMasterService conversionMasterService;
	
	@Autowired
	private AccountMasterDao accountMasterDao;
	

	DecimalFormat formatter = new DecimalFormat("#0.00"); 
	
	@Override
	public String generateMultiCurrencyAccountNumber(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) 
	{
		String multiCurrencyAccountNumber = null;
		try 
		{
			multiCurrencyAccountNumber = multiCurrencyWalletAccountMaster.getStrAccountNumber() + multiCurrencyWalletAccountMaster.getStrCurrencyCode();
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured ::"+ExceptionUtils.getStackTrace(e));
			
		}
		return multiCurrencyAccountNumber;
	}

	@Override
	public int createWalletAccountBasedOnCurrencyWise(List<MultiCurrencyWalletAccountMaster> listOfCurrecnyAccountMasters) 
	{
		int response = 0;
		try 
		{
			int[] responseArr = multiCurrencyWalletAccountDao.batchEntryOfMultiCurrecnyWalletAccountMaster(listOfCurrecnyAccountMasters);
			if (responseArr != null && responseArr.length > 0)
			{
				response = responseArr.length;
				return response;
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured ::"+ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public MultiCurrencyWalletAccountMaster getMultiCurrencyWalletAccount(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		
		return multiCurrencyWalletAccountDao.getMultiCurrencyWalletAccount(multiCurrencyWalletAccountMaster);
	}

	@Override
	public void updateMultiCurrencyWalletAccountMaster(MultiCurrencyWalletAccountMaster currencyWalletAccount) {
		 multiCurrencyWalletAccountDao.updateMultiCurrencyWalletAccountMaster(currencyWalletAccount);
	}

	@Override
	public ProcessResponse viewCurrencyWallet(MultiCurrencyWalletAccountMaster accountCreation) {
		ProcessResponse processResponse = new ProcessResponse();
	
		try 
		{
			if(accountCreation.getStrAccountNumber() != null ) {
			
			//Check This Acount Number is Present Or not 
			AccountCreation accountMaster = new AccountCreation();
			accountMaster.setStrAccountNumber(accountCreation.getStrAccountNumber());
			accountMaster = accountMasterDao.getAccountInformation(accountMaster);
			if(accountMaster != null && accountMaster.getStrAccountNumber() != null) {
		
			List<WalletAccountResponse> multiCurrencyWalletAccountMasters = new ArrayList<>();
			
			List<MultiCurrencyWalletAccountMaster> listOfMultiCurrency = multiCurrencyWalletAccountDao.getMultiCurrencyWalletsAccountsList(accountCreation);
			listOfMultiCurrency.forEach( i -> {
				
				WalletAccountResponse walletAccountResponse = new WalletAccountResponse();
				walletAccountResponse.setWalletAccountNumber(i.getStrCurrencyWalletAccountNumber());
				walletAccountResponse.setBalance(formatter.format(i.getStrClosingBalance()));
				walletAccountResponse.setPriority(i.getStrPriority());
				walletAccountResponse.setCurrencyCode(i.getStrCurrencyCode());
				multiCurrencyWalletAccountMasters.add(walletAccountResponse);
			});
			
			
			if(listOfMultiCurrency.size()>0) {
				
				
				multiCurrencyWalletAccountMasters.sort(Comparator.comparing(WalletAccountResponse::getPriority));		
				processResponse.setCode("S0000");
				processResponse.setStatus("Scusses");
				processResponse.setMessage("Data Fetch Scussefully");
				processResponse.setWalletAccountResponses(multiCurrencyWalletAccountMasters);
			}else {
				processResponse.setCode("E0000");
				processResponse.setStatus("failed");
				processResponse.setMessage("No MultiCurrency Account Found");
			}
			
		}else {
			processResponse.setCode("E0000");
			processResponse.setStatus("failed");
			processResponse.setMessage("Account Not Found");
		}
			
		}else {
			processResponse.setCode("E0000");
			processResponse.setStatus("failed");
			processResponse.setMessage("Account Number Not Found In Request");
		}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured In viewCurrencyWallet::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}

	@Override
	public ProcessResponse getCurrencyCodeByAccountNumber(MultiCurrencyWalletAccountMaster accountCreation) {
		
		ProcessResponse processResponse = new ProcessResponse();
		try {
		if(accountCreation.getStrAccountNumber() != null ) {
			List<String> currencyCode = new ArrayList<>();
			List<MultiCurrencyWalletAccountMaster> listOfMultiCurrency = multiCurrencyWalletAccountDao.getMultiCurrencyWalletsAccountsList(accountCreation);
			if(listOfMultiCurrency.size()>0) {
				listOfMultiCurrency.forEach(i -> {
					currencyCode.add(i.getStrCurrencyCode());
				});
				
				
				processResponse.setCode("S0000");
				processResponse.setStatus("Scusses");
				processResponse.setMessage("Data Fetch Scussefully");
				processResponse.setCurrencyCode(currencyCode);
			}else {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("No Currency  Found");
			}
			
		}else {
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Account Number Not Found");
		}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured  In getCurrencyCodeByAccountNumber ::"+ExceptionUtils.getStackTrace(e));
			
		}
		return processResponse;
	}

	@Override
	public ProcessResponse getCurrencyConversionRate(CurrencyConversionRateRequest currencyConversionRateRequest) {
		ProcessResponse processResponse = new ProcessResponse();
		try {
			
		if(currencyConversionRateRequest.getFromCurrency() != null) {
			if(currencyConversionRateRequest.getToCurrency() != null) {
				
				//Convert LoadedMoney To INR Value	  
				CurrencyConversionMaster currencyConversionMaster = new CurrencyConversionMaster();
				currencyConversionMaster.setFromCurrency(currencyConversionRateRequest.getFromCurrency());
				currencyConversionMaster.setToCurrency(currencyConversionRateRequest.getToCurrency());
				currencyConversionMaster = conversionMasterService.getCurrencyConverionValue(currencyConversionMaster);
				if(currencyConversionMaster != null)
				{
					
					double convertedINRValue =  Double.parseDouble(String.valueOf(formatter.format(Double.parseDouble(currencyConversionRateRequest.getLoadedAmt()) * currencyConversionMaster.getFromCurrencyValue()) ));
					
					processResponse.setCode("S0000");
					processResponse.setStatus("Scusses");
					processResponse.setMessage("Data Fetch Scussefully");
					processResponse.setCurrencyConvertedAmount(String.valueOf(convertedINRValue));
					processResponse.setConversionRate(String.valueOf(currencyConversionMaster.getToCurrencyConversion()));
					
				}else {
					processResponse.setCode("E0000");
					processResponse.setStatus("failed");
					processResponse.setMessage("No Currency Conversion Data Found");
				}
			}else {
				processResponse.setCode("E0000");
				processResponse.setStatus("failed");
				processResponse.setMessage("To currency Not Found in the Request");
			}
		}else {
			processResponse.setCode("E0000");
			processResponse.setStatus("failed");
			processResponse.setMessage("From Currency Not Found in the Request");
		}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured  In getCurrencyConversionRate Method ::"+ExceptionUtils.getStackTrace(e));
			
		}
		return processResponse;
	}

	@Override
	public MultiCurrencyWalletAccountMaster getCurrencyAccount(
			MultiCurrencyWalletAccountMaster fromMultiCurrencyWalletDetails) {
		// TODO Auto-generated method stub
		return multiCurrencyWalletAccountDao.getCurrencyAccount(fromMultiCurrencyWalletDetails);
	}

	@Override
	public int updateEarMark(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		return multiCurrencyWalletAccountDao.updateEarMark(multiCurrencyWalletAccountMaster);
		}

	@Override
	public int updateClosingBalance(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		return multiCurrencyWalletAccountDao.updateClosingBalance(multiCurrencyWalletAccountMaster);
		}

	@Override
	public List<MultiCurrencyWalletAccountMaster> getMultiCurrencyAccount(
			MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		return multiCurrencyWalletAccountDao.getMultiCurrencyAccount(multiCurrencyWalletAccountMaster);
	}

	@Override
	public MultiCurrencyWalletAccountMaster getFromToMultiCurrencyWalletAccount(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		return multiCurrencyWalletAccountDao.getFromToMultiCurrencyWalletAccount(multiCurrencyWalletAccountMaster);
	}

	@Override
	public ProcessResponse getCurrenyWalletListForStatemetView(MultiCurrencyWalletAccountMaster accountCreation) {
		ProcessResponse processResponse = new ProcessResponse();
	
		try 
		{
			if(accountCreation.getStrAccountNumber() != null ) {
			
			//Check This Acount Number is Present Or not 
			AccountCreation accountMaster = new AccountCreation();
			accountMaster.setStrAccountNumber(accountCreation.getStrAccountNumber());
			accountMaster = accountMasterDao.getAccountInformation(accountMaster);
			if(accountMaster != null && accountMaster.getStrAccountNumber() != null) {
		
			List<MultiCurrencyWalletAccountStatementList> multiCurrencyWalletAccountMasters = new ArrayList<>();
			
			List<MultiCurrencyWalletAccountMaster> listOfMultiCurrency = multiCurrencyWalletAccountDao.getCurrenyWalletListForStatemetView(accountCreation);
			listOfMultiCurrency.forEach( i -> {
				
				MultiCurrencyWalletAccountStatementList multiCurrencyWalletAccountStatementList = new MultiCurrencyWalletAccountStatementList();
				multiCurrencyWalletAccountStatementList.setWalletAccountNumber(i.getStrCurrencyWalletAccountNumber());
				multiCurrencyWalletAccountStatementList.setWalletAccountType(i.getStrAccountType());
				
				multiCurrencyWalletAccountMasters.add(multiCurrencyWalletAccountStatementList);
			});
			
			
			if(listOfMultiCurrency.size()>0) {
				processResponse.setCode("S0000");
				processResponse.setStatus("Scusses");
				processResponse.setMessage("Data Fetch Scussefully");
				processResponse.setMultiCurrencyWalletAccountMastersList(multiCurrencyWalletAccountMasters);
			}else {
				processResponse.setCode("E0000");
				processResponse.setStatus("failed");
				processResponse.setMessage("No MultiCurrency Account Found");
			}
			
		}else {
			processResponse.setCode("E0000");
			processResponse.setStatus("failed");
			processResponse.setMessage("Account Not Found");
		}
			
		}else {
			processResponse.setCode("E0000");
			processResponse.setStatus("failed");
			processResponse.setMessage("Account Number Not Found In Request");
		}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured In viewCurrencyWallet::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}

	@Override
	public ProcessResponse getWalletPriorityList(MultiCurrencyWalletAccountMaster accountCreation) {
		ProcessResponse processResponse = new ProcessResponse();
		try {
			
			List<PriorityWalletResponse> priorityWalletResponse = new ArrayList<>();
			
			MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster = new MultiCurrencyWalletAccountMaster();
			multiCurrencyWalletAccountMaster.setStrAccountNumber(accountCreation.getStrAccountNumber());
			multiCurrencyWalletAccountMaster.setStrAccountType(accountCreation.getBaseCurrencyAccountType());
			List<MultiCurrencyWalletAccountMaster> multiCurrencyWalletAccountMasterList =  multiCurrencyWalletAccountDao.getCurrenyWalletListForPriority(multiCurrencyWalletAccountMaster);
			if(multiCurrencyWalletAccountMasterList.size()>0) {
				
				multiCurrencyWalletAccountMasterList.forEach(i -> {
					
					PriorityWalletResponse priorityWalletResponseObj = new PriorityWalletResponse();
					priorityWalletResponseObj.setAccountType(i.getStrAccountType());
					priorityWalletResponseObj.setPriority(i.getStrPriority());
					priorityWalletResponseObj.setWalletAccountNumber(i.getStrCurrencyWalletAccountNumber());
					priorityWalletResponse.add(priorityWalletResponseObj);
					
				});
				
				
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Data Fetch Successfully");
				processResponse.setPriorityWalletResponsesList(priorityWalletResponse);
				
				
			}else {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Data Fetch Successfully");
				processResponse.setPriorityWalletResponsesList(priorityWalletResponse);
			}
			
			
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured  In   ::"+ExceptionUtils.getStackTrace(e));
			
		}
		return processResponse;
	}

	@Override
	public ProcessResponse updateWalletPriorityList(MultiCurrencyWalletAccountMaster accountCreation) {
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
		// get
		List<PriorityWalletResponse> priorityWalletResponses = accountCreation.getPriorityWalletList();
		if(priorityWalletResponses.size()>0) {
			
			priorityWalletResponses.forEach(i-> {
				
				//update in MultiCurrency Account Master
				MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMasterList = new MultiCurrencyWalletAccountMaster();
				multiCurrencyWalletAccountMasterList.setStrCurrencyWalletAccountNumber(i.getWalletAccountNumber());
				multiCurrencyWalletAccountMasterList.setStrPriority(i.getPriority());
				multiCurrencyWalletAccountDao.updateMultiCurrencyWalletAccountMasterWithPriority(multiCurrencyWalletAccountMasterList);
				
			});
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");
			processResponse.setMessage("Data Updated Successfully");
			
		}else {
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("No Wallets Found");
		}
		
	}
	catch (Exception e) 
	{
		amsLogger.writeExceptionLog("Exception occured  In   ::"+ExceptionUtils.getStackTrace(e));
		
	}
		
		
		return processResponse;
	}


}
