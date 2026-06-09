package ams.cms.services.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.model.AccountBalanceResponse;
import ams.cms.api.model.AccountCreationResponse;
import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.AccountTxnBalanceRequest;
import ams.cms.api.model.CustomerByAccountResponse;
import ams.cms.api.model.NUBANAccountDto;
import ams.cms.api.model.QrInfo;
import ams.cms.api.model.RefundRequest;
import ams.cms.api.model.ReverseTransactionRequest;
import ams.cms.api.model.UserTransactionModel;
import ams.cms.api.response.AccountInfoResponse;
import ams.cms.api.service.NubanCodeConfigService;
import ams.cms.api.service.QrCodeService;
import ams.cms.config.AppInfo;
import ams.cms.config.CommonConstants;
import ams.cms.config.CustomerIdCreationConfigIF;
import ams.cms.dao.AccountCreditLimitCategoryDao;
import ams.cms.dao.AccountMasterDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountCreditLimitCategory;
import ams.cms.model.AccountTransactionLimitation;
import ams.cms.model.AccountTypeMaster;
import ams.cms.model.AccountTypeWiseWalletMaster;
import ams.cms.model.CurrencyMaster;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.CustomerIdMap;
import ams.cms.model.CustomerIdTable;
import ams.cms.model.CustomerInfo;
import ams.cms.model.MultiCurrencyWalletAccountMaster;
import ams.cms.model.MultiCurrencyWalletAccountTypeMaster;
import ams.cms.model.TierAccountMaster;
import ams.cms.model.WalletAccountMaster;
import ams.cms.notification.EmailTemplate;
import ams.cms.notification.email.EmailService;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountTransactionLimitationService;
import ams.cms.services.AccountTypeMasterService;
import ams.cms.services.AccountTypeWiseWalletService;
import ams.cms.services.CurrencyMasterService;
import ams.cms.services.CustomerIDCreationService;
import ams.cms.services.CustomerIdTableService;
import ams.cms.services.MultiCurrencyWalletAccountService;
import ams.cms.services.MultiCurrencyWalletAccountTypeService;
import ams.cms.services.TierAccountMasterService;
import ams.cms.services.WalletAccountMasterService;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.AccountMasterResponse;
import ams.cms.utility.Utils;

@Transactional
@Service
public class AccountMasterServiceImpl implements AccountMasterService
{
	private AMSLogger amsLogger = AMSLogger.getInstance(AccountMasterServiceImpl.class);
	
	@Autowired
	AccountMasterDao accountMasterDao;
	
	@Autowired
	AccountTypeWiseWalletService accountTypeWiseWalletService;
	
	@Autowired
	WalletAccountMasterService walletAccountMasterService; 
	
	@Autowired
	TierAccountMasterService tierAccountMasterService;
	
	@Autowired
	AccountTypeMasterService accountTypeMasterService;
	
	@Autowired
	NubanCodeConfigService nubanCodeConfigService;
	
	@Autowired
	CustomerIDCreationService customerIDCreationService;
	
	@Autowired
	QrCodeService qrCodeService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	CustomerIdCreationConfigIF customerIdCreationConfigIF;
	
	@Autowired
	CustomerIdTableService customerIdTableService;
	
	@Autowired
	AccountTransactionLimitationService accountTransactionLimitationService;
	
	@Autowired
	AccountCreditLimitCategoryDao accountCreditLimitCategoryDao;
	
	@Autowired
	MultiCurrencyWalletAccountService multiCurrencyWalletAccountService;
	
	@Autowired
	MultiCurrencyWalletAccountTypeService multiCurrencyWalletAccountTypeService;
	
	@Autowired
	CurrencyMasterService currencyMasterService;
	
	@Autowired
	private AppInfo appInfo;
	
	@Override
	public AccountCreation getAccountInformation(AccountCreation accountCreation) throws Exception 
	{
		return accountMasterDao.getAccountInformation(accountCreation);
	}
	
	@Override
	public AccountCreation saveAccountInformation(AccountCreation accountCreation) throws Exception
	{
		accountCreation.setStrPreCredAmount(0d);
		accountCreation.setStrEarMarkAmount(0d);
		
		accountMasterDao.save(accountCreation);
		return accountCreation;
	}
	
	@Override
	public List<AccountCreation> getAccountInformationList() throws Exception 
	{
		return accountMasterDao.findAll();
	}
	
	@Override
	public List<AccountCreation> getAccountInformationList(AccountCreation accountCreation) throws Exception 
	{
		return accountMasterDao.getAccountInformationList(accountCreation);
	}
	
	@Override
	public List<AccountCreation> getAccountInformationListById(String id) throws Exception 
	{
		return accountMasterDao.getAccountInformationListById(id);
	}
	
	@Override
	public int updateAccountCreationFromInstanceAccount(AccountCreation accountCreation) throws Exception 
	{
		return accountMasterDao.updateAccountCreationFromInstanceAccount(accountCreation);
	}
	
	@Override
	public int updateAccountCreation(AccountCreation accountCreation) throws Exception 
	{
		return accountMasterDao.updateAccountCreation(accountCreation);
	}
	
	@Override
	public List<AccountCreation> getAccountInformationListByTypes(AccountCreation accountCreation) throws Exception 
	{
		return accountMasterDao.getAccountInformationListByTypes(accountCreation);
	}
	
	@Override
	public int[] batchEntryOfInstanAccount(List<AccountCreation> accountCreationlist) throws Exception 
	{
		return accountMasterDao.batchEntryOfInstanAccount(accountCreationlist);
	}
	
	@Override
	public int createSubAccountForWallet(AccountCreation accountCreation) throws Exception 
	{
		int result = 0;
		try 
		{
			String accountNumber = accountCreation.getStrAccountNumber();
			String strCreatedBy = accountCreation.getStrCreatedBy();
			
			List<AccountTypeWiseWalletMaster> listDataAccountTypeWiseWalletMasters = getAccountTypeWiseWalletMasters(accountCreation) ;
			
			if (listDataAccountTypeWiseWalletMasters!=null && listDataAccountTypeWiseWalletMasters.size() > 0) 
			{
				List<WalletAccountMaster> listOfWalletAccountMasters = new ArrayList<WalletAccountMaster>();
				for(AccountTypeWiseWalletMaster accountTypeWiseWalletMaster : listDataAccountTypeWiseWalletMasters)
				{
					WalletAccountMaster walletAccountMaster = new WalletAccountMaster();
					
					walletAccountMaster.setStrAccountNumber(accountNumber);
					walletAccountMaster.setStrCreatedBy(strCreatedBy);
					walletAccountMaster.setStrDateOfCreation(new Date());
					
					walletAccountMaster.setStrParticipantID(accountTypeWiseWalletMaster.getStrParticipantID());
					walletAccountMaster.setStrAccountType(accountTypeWiseWalletMaster.getStrAccounType());
					walletAccountMaster.setStrMccCode(accountTypeWiseWalletMaster.getStrMccCode());
					walletAccountMaster.setStrPercentage(accountTypeWiseWalletMaster.getStrParticipantID());					
					
					String walletAccounNo = walletAccountMasterService.generateWalletAccountNumber(walletAccountMaster);
					walletAccountMaster.setStrWalletAccountNumber(walletAccounNo);
					
					listOfWalletAccountMasters.add(walletAccountMaster);
				}
				if (listOfWalletAccountMasters.size() > 0) 
				{
					result = walletAccountMasterService.createWalletAccountBasedOnMccWise(listOfWalletAccountMasters);
					if (result > 0) 
					{
						return result;
					}
				}
			}
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return result;
	}
	
	//Get account list based on participant id and account type
	private List<AccountTypeWiseWalletMaster> getAccountTypeWiseWalletMasters(AccountCreation accountCreation) 
	{
		List<AccountTypeWiseWalletMaster> listData = null;
		try 
		{
			AccountTypeWiseWalletMaster accountTypeWiseWalletMaster = new AccountTypeWiseWalletMaster();
			accountTypeWiseWalletMaster.setStrParticipantID(accountCreation.getStrParticipantID());
			accountTypeWiseWalletMaster.setStrAccounType(accountCreation.getStrAccountType());
			
			listData = accountTypeWiseWalletService.getAccountTypeWiseWalletMasterList(accountTypeWiseWalletMaster);
			
			if (listData != null && listData.size() > 0) 
			{
				return listData;
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return listData;
	}

	@Override
	public String getAvailableBalanceBasedOnAccountTypeAndNumber(AccountCreation accountCreation) throws Exception 
	{
		return accountMasterDao.getAvailableBalanceBasedOnAccountTypeAndNumber(accountCreation);
	}
	
	@Override
	public List<AccountCreationResponse> getAccountProfilelist(AccountCreation accountCreation) throws Exception
	{
		List<AccountCreation> accountProfilelist = accountMasterDao.getAccountProfilelist(accountCreation);
		
		List<AccountCreationResponse>  accountCreationResponselist= new ArrayList<>();
		for (int i = 0; i < accountProfilelist.size(); i++) 
		{
			AccountCreation accountCreationData=accountProfilelist.get(i);
			
			AccountCreationResponse mapaccountCreationToAccountCreationResponselist = mapaccountCreationToAccountCreationResponselist(accountCreationData);
			accountCreationResponselist.add(mapaccountCreationToAccountCreationResponselist);
		}
		return accountCreationResponselist;	
     }
	
	private AccountCreationResponse mapaccountCreationToAccountCreationResponselist(AccountCreation accountCreationData)
	{	
		AccountCreationResponse accountCreationResponse = new AccountCreationResponse();
		accountCreationResponse.setStrParticipantID((accountCreationData.getStrParticipantID()!=null) ? accountCreationData.getStrParticipantID() : "");                                                  
		accountCreationResponse.setStrAccountType((accountCreationData.getStrAccountType()!=null) ? accountCreationData.getStrAccountType() : "");                                         
		accountCreationResponse.setStrAccountNumber((accountCreationData.getStrAccountNumber()!=null) ? accountCreationData.getStrAccountNumber() : "");                                    
		accountCreationResponse.setStrTitle((accountCreationData.getStrTitle()!=null) ? accountCreationData.getStrTitle() : "");
		accountCreationResponse.setStrFirstName((accountCreationData.getStrFirstName() !=null) ? accountCreationData.getStrFirstName() : "");      
		accountCreationResponse.setStrMiddleName((accountCreationData.getStrMiddleName() !=null) ? accountCreationData.getStrMiddleName() : "");                                                         
		accountCreationResponse.setStrLastName((accountCreationData.getStrLastName() !=null) ? accountCreationData.getStrLastName() : "");                                              
		accountCreationResponse.setStrEmailID((accountCreationData.getStrEmailID() !=null) ? accountCreationData.getStrEmailID() : "");                                        
		accountCreationResponse.setStrMobileNo((accountCreationData.getStrMobileNo() !=null) ? accountCreationData.getStrMobileNo() : "");                                        
		accountCreationResponse.setStrAddress1((accountCreationData.getStrAddress1() !=null) ? accountCreationData.getStrAddress1() : "");                                        
		accountCreationResponse.setStrAddress2((accountCreationData.getStrAddress2() !=null) ? accountCreationData.getStrAddress2() : "");                                             
		accountCreationResponse.setStrAddress3((accountCreationData.getStrAddress3() !=null) ? accountCreationData.getStrAddress3() : "");                                             
		accountCreationResponse.setStrPinCode((accountCreationData.getStrPinCode() !=null) ? accountCreationData.getStrPinCode() : "");                                                    
		accountCreationResponse.setStrCity((accountCreationData.getStrCity() !=null) ? accountCreationData.getStrCity() : "");                                                         
		accountCreationResponse.setStrState((accountCreationData.getStrState() !=null) ? accountCreationData.getStrState() : "");                                                
		accountCreationResponse.setStrAccountIssueDate(accountCreationData.getStrDateOfCreation());
		
		//accountCreationResponse.setStrAccountIssueDate((accountCreationData.getStrDateOfCreation() !=null) ? accountCreationData.getStrDateOfCreation() : "");  
		accountCreationResponse.setStrLastloginDate((accountCreationData.getStrLastloginDate() !=null) ? accountCreationData.getStrLastloginDate() : ""); 
		
		return accountCreationResponse;
	}


	private Object setStrAccountIssueDate(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AccountBalanceResponse> getAccountBalanceView(AccountCreation accountCreation) 
	{
		List<AccountCreation> accountBalancelist=accountMasterDao.getAccountBalanceView(accountCreation);
		
		List<AccountBalanceResponse>  accountBalanceResponselist= new ArrayList<>();
		for (int i = 0; i < accountBalancelist.size(); i++) 
		{
			AccountCreation accountBalanceData=accountBalancelist.get(i);
			AccountBalanceResponse mapaccountBalanceToAccountBalanceResponselist=mapaccountBalanceToAccountBalanceResponselist(accountBalanceData);
			accountBalanceResponselist.add(mapaccountBalanceToAccountBalanceResponselist);
		}
		return accountBalanceResponselist;	
	}
	private AccountBalanceResponse mapaccountBalanceToAccountBalanceResponselist(AccountCreation accountBalanceData) 
	{
		AccountBalanceResponse accountBalanceResponse=new AccountBalanceResponse();
		accountBalanceResponse.setStrClosingBalance(accountBalanceData.getStrClosingBalance());
		return accountBalanceResponse;
	}

	@Override
	public int updateBalanceRelatedData(AccountCreation accountCreation) throws Exception {
		return accountMasterDao.updateBalanceRelatedData(accountCreation);
	}

	@Override
	public List<AccountCreation> getClosingBalance(AccountTxnBalanceRequest accountTxnBalanceRequest) {
		List<AccountCreation> accountClosingBalance = accountMasterDao.getAccountClosingBalance(accountTxnBalanceRequest);
		return accountClosingBalance;
	}

	@Override
	public List<AccountCreation> getAccountIssueData(AccountCreation accountCreation) {
		return accountMasterDao.getAccountIssueData(accountCreation);
	}

	@Override
	public AccountCreation getOutstandingAccount(AccountCreation accountCreation) {
		return accountMasterDao.getOutstandingAccount(accountCreation);
	}

	@Override
	public int updateAccountAfterLinkedCard(AccountCreation accountCreation) throws Exception {
		return accountMasterDao.updateAccountAfterLinkedCard(accountCreation);
	}
	
	@Override
	public int updateLastLoginDate(AccountCreation accountCreation)
	{
		return accountMasterDao.updateLastLoginDate(accountCreation);
	}

	@Override
	public boolean isAccountAlreadyExist(AccountCreation accountCreation) throws Exception {
		return accountMasterDao.isAccountAlreadyExist(accountCreation);
	}

	@Override
	public boolean isCustomerAccountAlreadyConfigured(AccountCreation accountCreation) throws Exception {
		return accountMasterDao.isCustomerAccountAlreadyConfigured(accountCreation);
	}
	
	@Override
	public AccountMaster getAccountDetails(AccountMaster accountMaster ) {
		return accountMasterDao.getAccountDetails(accountMaster);
	}

	//@Transactional
	@Override
	public int updateAccountMasterFields(AccountMaster accountMaster) throws Exception {
		return accountMasterDao.updateAccountMasterFields(accountMaster);
	}
	
	@Override
	public AccountCreation viewAccountMasterDetails(AccountCreation accountCreation) 
	{	
		return accountMasterDao.viewAccountMasterDetails(accountCreation);
	}

	@Override
	public int updateBalnceLimitValues(AccountCreation accountCreation) {
	
		return accountMasterDao.updateBalnceLimitValues(accountCreation);
	}

	@Override
	public AccountCreation getSenderAccountInformation(AccountCreation accountCreation) throws Exception
	{
		if (accountCreation!=null && accountCreation.getStrCustId()!=null) 
		{
			return accountMasterDao.getSenderAccountInformation(accountCreation);
		}
		return accountMasterDao.getSenderAccountInformationWithoutCustId(accountCreation);
	}

	@Override
	public AccountCreation getRecipientAccountInformation(AccountCreation accountCreation) throws Exception {
		return accountMasterDao.getRecipientAccountInformation(accountCreation);
	}

	@Override
	public List<AccountCreation> getSignInDataFromApp(AccountCreation accountCreation) throws Exception {
		return accountMasterDao.getSignInDataFromApp(accountCreation);
	}

	@Override
	public List<AccountCreation> getUpdatedAccountsListForApp(AccountCreation accountCreation) throws Exception {
		return accountMasterDao.getUpdatedAccountsListForApp(accountCreation);
	}
	
	@Override
	public int updateAccountMasterFieldsBasedOnQuery(String updateQuery) throws Exception {
		return accountMasterDao.updateAccountMasterFieldsBasedOnQuery(updateQuery);
	}

	@Override
	public void updateAccountMasterSomeFields(AccountCreation accountCreation) throws Exception {
		accountMasterDao.update(accountCreation);
	}

	@Override
	public int updateCreditLimitValues(AccountCreation accountCreation) throws Exception 
	{
		return accountMasterDao.updateCreditLimitValues(accountCreation);
	}

	@Override
	public int updateLimitValues(AccountCreation accountCreation) throws Exception 
	{
		return accountMasterDao.updateLimitValues(accountCreation);
	}

	@Override
	public AccountCreation updategracePeriodDate(AccountCreation accountCreation) 
	{
		return accountMasterDao.updategracePeriodDate(accountCreation);
	}

	@Override
	public int updateTotalOutstanding(AccountCreation accountCreation) 
	{		
		return accountMasterDao.updateTotalOutstanding(accountCreation);
	}

	@Override
	public int updateAccountBalance(AccountCreation accountCreation) throws Exception {
		return accountMasterDao.updateAccountBalance(accountCreation);
	}

	@Override
	public int updateAccountCreationFields(AccountCreation accountCreation) throws Exception {
		return accountMasterDao.updateAccountCreationFields(accountCreation);
	}
	
	//created by ankit
	@Override
	public List<AccountCreation> getAccountBalanceAndName(AccountCreation accountCreation) {
		return accountMasterDao.getAcountNameAndBalance(accountCreation);
	}
	//created by ankit
	
	@Override
	public AccountMaster getAccountQRDetails(AccountMaster accountMaster) {
		return accountMasterDao.getAccountQRDetails(accountMaster);
	}

	@Override
	public int updateTransactionLimit(AccountCreation accountCreation) 
	{
		return accountMasterDao.updateTransactionLimit(accountCreation);
	}

	@Override
	public AccountCreation getTransactionLimits(AccountCreation accountCreation) 
	{
		return accountMasterDao.getTransactionLimits(accountCreation);
	}
	
	@Override
	public AccountMaster getAccountNameByAccountNo(AccountMaster accountMaster) {
		return accountMasterDao.getAccountNameByAccountNo(accountMaster);
	}
	
	//created by ankit on 15-04-2023
	@Override
	public List<AccountCreation> verifyMerchantForRefundRequest(RefundRequest refundRequest) 
	{
		String strMerchantCustId = refundRequest.getStrMerchantCustId();
		String strMerchantAccountNo = refundRequest.getStrMerchantAccountNo();
		String strMerchantAccountType = refundRequest.getStrMerchantAccountType();
		
		AccountCreation accountCreation = new AccountCreation();
		accountCreation.setStrAccountType(strMerchantAccountType);
		accountCreation.setStrAccountNumber(strMerchantAccountNo);
		accountCreation.setStrCustId(strMerchantCustId);
		
		List<AccountCreation> merchantVerified = accountMasterDao.isMerchantVerified(accountCreation);
		
		return merchantVerified;
	}
	//created by ankit on 15-04-2023

	@Override
	public int verifyMerchantForRefundTransaction(ReverseTransactionRequest refundTransactionRequest) {
		
		AccountCreation accountCreation = new AccountCreation();
		accountMasterDao.getAccountInfoAndStatus(accountCreation);
		return 0;
	}

	//created by ankit on 16-04-2023
	@Override
	public List<AccountCreation> getAccountInfoAndStatus(AccountCreation accountCreation) {
		List<AccountCreation> merchantStatus = accountMasterDao.getAccountInfoAndStatus(accountCreation);
		return merchantStatus;
	}
	//created by ankit on 16-04-2023

	//created by ankit on 16-04-2023
	@Override
	public int updateOnlyBalance(AccountCreation accountCreation) {
		return accountMasterDao.updateOnlyBalance(accountCreation);
	}
	//created by ankit on 16-04-2023

	//created by ankit on 17-04-2023
	@Override
	public List<AccountCreation> getReceiverInfromation(AccountCreation accountCreation) {
		return accountMasterDao.getReceiverInformation(accountCreation);
	}
	//created by ankit on 17-04-2023

	//created by ankit on 18-04-2023
	@Override
	public List<AccountCreation> getAccountInfoByAccountNumber(AccountCreation accountCreation) {
		return accountMasterDao.getTransactionPayeeDetailsByAccountNumber(accountCreation);
	}
	//created by ankit on 18-04-2023

	//created by ankit on 18-04-2023
	@Override
	public List<AccountCreation> getTransactionPayeeDetailsByAccountNo(AccountCreation toAccountCreation) {
		return accountMasterDao.getTransactionPayeeDetailsByAccountNumber(toAccountCreation);
	}
	//created by ankit on 18-04-2023
	
	//Added by Pankaj Pawar Start
	@Override
	public AccountMaster getAccountInfo(AccountMaster accountMaster) {
		return accountMasterDao.getAccountInfo(accountMaster);
	}

	@Override
	public int updatePerformTxnValues(AccountCreation accountCreation) {
		return accountMasterDao.updatePerformTxnValues(accountCreation);
	}

	@Override
	public List<AccountMaster> getAccountInfoList(UserTransactionModel userTransactionModel) {
		return accountMasterDao.getAccountInfoList(userTransactionModel);
	}
	//Added by Pankaj Pawar End

	@Override
	public AccountCreation getRecipientAccountInformationWithoutCustId(AccountCreation accountCreation) 
	{
		return accountMasterDao.getRecipientAccountInformationWithoutCustId(accountCreation);
	}
	
	
	@Override
	public AccountCreation getClosingBalanceonAccTypeAccNo(AccountCreation accountCreation) {
		return accountMasterDao.getClosingBalanceonAccTypeAccNo(accountCreation);
	}

	@Override
	public int updateClosingBalance(AccountCreation accountCreation) {
		return accountMasterDao.updateClosingBalance(accountCreation);
	}
	
	@Override
	public List<AccountCreation> getRegCustWithLinkAccount(AccountCreation accountCreation) {
		return accountMasterDao.getRegCustWithLinkAccount(accountCreation);
	}

	//Added by Pankaj [start]
	@Override
	public AccountCreation getTierInfoByCustID(AccountCreation accountCreation) {
		return accountMasterDao.getTierInfoByCustID(accountCreation);
	}
	//Added by Pankaj [End]

	@Override
	public List<AccountMasterResponse> getAccountInformationForClosingAccount(AccountCreation accountCreation) 
	{
		return accountMasterDao.getAccountInformationForClosingAccount(accountCreation);
	}
	
	//created by ankit on 03-06-2023
	@Override
	public List<AccountCreation> getAccountHolderName(AccountCreation accountCreation) {
		return  accountMasterDao.getAccountHolderName(accountCreation);	
	}
	//created by ankit on 03-06-2023

	//created by ankit 06-06-2023
	@Override
	public List<AccountCreation> findByAccountNumber(AccountCreation accountCreation) {
		return accountMasterDao.findByAccountNumber(accountCreation);
	}
	//created by ankit 06-06-2023

 
	
	//ear mark and don't update the closing balance just check if there's closing balance
	@Override
	public void earMarkAccount(AccountCreation account) {
		
		try {
			
			double earMark= account.getStrEarMarkAmount();
			
			AccountCreation accountCreation = new AccountCreation();
			accountCreation.setStrAccountType(account.getStrAccountType());
			accountCreation.setStrAccountNumber(account.getStrAccountNumber());
			
			AccountCreation fetchedAccount = accountMasterDao.getEarMarkAndClosingBalance(accountCreation);
			
			double fethcedEarMark = fetchedAccount.getStrEarMarkAmount();
			double newEarMark = earMark + fethcedEarMark;
			
			String strClosingBalance = fetchedAccount.getStrClosingBalance();
			
			double closingBalance = Double.parseDouble(strClosingBalance);
			Double newClosingBalance = closingBalance - earMark;
			String strNewClosingBalance = String.valueOf(newClosingBalance);
			
			account.setStrClosingBalance(strNewClosingBalance);
			account.setStrEarMarkAmount(newEarMark);
			accountMasterDao.updateEarMark(account);
		}catch(Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void reduceEarBalance(AccountCreation accountCreation) {
		accountMasterDao.reduceEarMarkAmount(accountCreation);
	}

	@Override
	public void preCredAccount(AccountCreation accountMaster) {
		accountMasterDao.preCredAccount(accountMaster);
	}

	@Override
	public void creditPreCred(AccountCreation accountCreation) {
	}

	@Override
	public void earMarkTransactionAmount(AccountCreation accountCreation) {
		accountMasterDao.earMarkTransactionAmount(accountCreation);
	}

	@Override
	public void addPreCredAmount(AccountCreation accountCreation)
	{
		accountMasterDao.addPreCredAmount(accountCreation);
	}

	@Override
	public CustomerByAccountResponse getCustomerAccountInfoByAccountNo(AccountCreation accountCreation) {
		return accountMasterDao.getCustomerAccountInfoByAccountNo(accountCreation);
	}

	@Override
	public CustomerByAccountResponse getCustomerAccountInfoByAccountNoForNonNigeria(AccountCreation accountCreation) {
		return accountMasterDao.getCustomerAccountInfoByAccountNoForNonNigeria(accountCreation);
	}

	@Override
	public int[] batchEntryOfEarMarkUpdates(List<AccountCreation> accountCreationlist) throws Exception {
		return accountMasterDao.batchEntryOfEarMarkUpdates(accountCreationlist);
	}

	@Override
	public int[] batchEntryOfPredCredUpdates(List<AccountCreation> accountCreationlist) throws Exception {
		return accountMasterDao.batchEntryOfPredCredUpdates(accountCreationlist);
	}

	@Override
	public int updatePreCredAmountOfCustomerAccount(AccountCreation accountCreation) throws Exception {
		return accountMasterDao.updatePreCredAmountOfCustomerAccount(accountCreation);
	}

	@Override
	public int updateEarMarkAmountOfCustomerAccount(AccountCreation accountCreation) throws Exception {
		return accountMasterDao.updateEarMarkAmountOfCustomerAccount(accountCreation);
	}

	@Override
	public String getUpdatablePreCredAmount(String existingPreCredAmount, String txnAmount) 
	{
		String updatablePreCredAmount = null;
		try
		{
			double existingPreCredAmt = Double.parseDouble(existingPreCredAmount);
			double txnAmt = Double.parseDouble(txnAmount);
			
			double totalAmt = existingPreCredAmt + txnAmt;
			
			updatablePreCredAmount = Utils.decimalFormat.format(totalAmt);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return updatablePreCredAmount;
	}

	@Override
	public String getUpdatableEarMarkAmount(String existingEarMarkAmount, String txnAmount) 
	{
		String updatableEarMarkAmount = null;
		try
		{
			double existingEarMarkAmt = Double.parseDouble(existingEarMarkAmount);
			double txnAmt = Double.parseDouble(txnAmount);
			
			double totalAmt = existingEarMarkAmt + txnAmt;
			
			updatableEarMarkAmount = Utils.decimalFormat.format(totalAmt);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return updatableEarMarkAmount;
	}

	@Override
	public String getUpdatableReducePreCredAmount(String existingPreCredAmount, String txnAmount) 
	{
		String updatablePreCredAmount = null;
		try
		{
			double existingPreCredAmt = Double.parseDouble(existingPreCredAmount);
			double txnAmt = Double.parseDouble(txnAmount);
			
			double totalAmt = existingPreCredAmt - txnAmt;
			
			updatablePreCredAmount = Utils.decimalFormat.format(totalAmt);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return updatablePreCredAmount;
	
	}

	@Override
	public String getUpdatableReduceEarMarkAmount(String existingEarMarkAmount, String txnAmount) {

		String updatableEarMarkAmount = null;
		try
		{
			double existingEarMarkAmt = Double.parseDouble(existingEarMarkAmount);
			double txnAmt = Double.parseDouble(txnAmount);
			
			double totalAmt = existingEarMarkAmt - txnAmt;
			
			updatableEarMarkAmount = Utils.decimalFormat.format(totalAmt);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return updatableEarMarkAmount;
	
	}

	@Override
	public String getUpdatableReduceAccountMasterData(String existingAmount, String txnAmount) 
	{
		String updatableFieldValue = null;
		try 
		{
			double existingAmt = Double.parseDouble(existingAmount);
			double txnAmt = Double.parseDouble(txnAmount);
			
			double totalAmt = existingAmt - txnAmt;
			
			updatableFieldValue = Utils.decimalFormat.format(totalAmt);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return updatableFieldValue;
	}

	@Override
	public int[] updatesBatchEntryOfToAccount(List<AccountCreation> accountCreationlist) throws Exception {
		return accountMasterDao.updatesBatchEntryOfToAccount(accountCreationlist);
	}

	@Override
	public int[] updatesBatchEntryOfFromAccount(List<AccountCreation> accountCreationlist) throws Exception {
		return accountMasterDao.updatesBatchEntryOfFromAccount(accountCreationlist);
	}

	@Override
	public int[] updatesBatchEntryOfLimitsAccountMasterFields(List<AccountCreation> accountCreationlist) throws Exception {
		return accountMasterDao.updatesBatchEntryOfLimitsAccountMasterFields(accountCreationlist);
	}

	@Override
	public AccountCreation saveAutoCreationInfo(AccountCreation accountCreation, AccountTypeMaster accountTypeMaster) 
	{
		try 
		{
			accountCreation.setBirthDate(accountCreation.getBirthDate());
			accountCreation.setStrDateOfCreation(new Date());

			//update for account base currency from Account Type . .
			//get account Type
			if("Y".equalsIgnoreCase( accountTypeMaster.getIsMultiCurrencySupport())) {
			
				//get Base Currency Form Currency 
				CurrencyMaster currencyMaster  = currencyMasterService.getBaseCurrencyFromCurrencyMaster();
				if(currencyMaster != null) {
					accountCreation.setCurrencyCode(currencyMaster.getCurrencyCode());
				}
				
			}
			
			accountCreation = saveAccountInformation(accountCreation);				
			if (accountCreation.getStrID() != null) 
			{
				//Added Changes for Nigeria based Start
				if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
				{
					if ( !(accountTypeMaster.getStrAccountTypeCategory().equalsIgnoreCase("C"))) 
					{
						TierAccountMaster tierAccountMaster = new TierAccountMaster();
						
						tierAccountMaster.setStrCustId(accountCreation.getStrCustId());
						tierAccountMaster.setStrAccountNo(accountCreation.getStrAccountNumber());
						tierAccountMaster.setStrAccountType(accountCreation.getStrAccountType());
						tierAccountMaster.setStrMobileNo(accountCreation.getStrMobileNo());
						tierAccountMaster.setStrCreatedBy(accountCreation.getStrCreatedBy());
						
						tierAccountMasterService.saveTierAccountMaster(tierAccountMaster);
					}
				}
				//Added Changes for Nigeria based End
				if(!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
				{
					//Added for Updating Last Account Number Start				
					accountTypeMaster.setStrLastAccNumber(accountCreation.getStrAccountNumber());
					int i = accountTypeMasterService.updateLastAccountNumber(accountTypeMaster);
					System.out.println("Response of Updated Last Account Number::"+i);
					//Added for Updating Last Account Number End
				}
					
				//condition to create wallet based on allow wallet Start
				if (accountTypeMaster.getStrIsAllowWallet().equalsIgnoreCase("Y"))  
				{
					createSubAccountForWallet(accountCreation);
				}
				//condition to create wallet based on allow wallet End
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountCreation;	
	}
		
	@Override
	public AccountCreation getAccountLimitInfoToCompareTransferAmount(AccountCreation accountCreation) {
		return accountMasterDao.getAccountLimitInfoToCompareTransferAmount(accountCreation);
	}

	@Override
	public AccountCreation getCumlativeBalanceCompareTransAmt(AccountCreation accountCreation) {		
		return accountMasterDao.getCumlativeBalanceCompareTransAmt(accountCreation);
	}

	@Override
	public int updateAccountBalanceWithoutAccountType(AccountCreation accountCreation) throws Exception {
		return accountMasterDao.updateAccountBalanceWithoutAccountType(accountCreation);
	}

	//added by sunil Y , 2023-07-10 started
	@Override
	public boolean isAccountAlreadyExistBasedOnType(AccountMaster accountMaster) 
	{
		boolean isAccountExist = false;
		try 
		{
			AccountMaster accountMasterObj = accountMasterDao.getAccountTypeIsExist(accountMaster);
			if(accountMasterObj != null && accountMasterObj.getStrStatus() != null) 
			{
				if(accountMasterObj.getStrStatus().equalsIgnoreCase("Active")) 
				{
					isAccountExist = true;
				}
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return isAccountExist;
	}

	@Override
	public ProcessResponse createAccountMaster(CustomerIdCreation customerIdCreation, AccountTypeMaster accountTypeObject, ProcessResponse processResp)
	{
		ProcessResponse processResponseRes = new ProcessResponse();
		try 
		{
			processResponseRes.setCode("S0000");
			processResponseRes.setStatus("Success");
			
			String participantId = processResp.getStrParticipantId();
			
			List<CustomerIdCreation> customerAccountDetailsBasedOnCustId = customerIDCreationService.getCustomerAccountDetailsBasedOnCustId(customerIdCreation);
			if (customerAccountDetailsBasedOnCustId.size() > 0) 
			{
				processResponseRes = generateCustomerAccounNo(accountTypeObject, processResponseRes);
				if ("S0000".equalsIgnoreCase(processResponseRes.getCode())) 
				{
					CustomerIdCreation customerIdCreationMaster = customerAccountDetailsBasedOnCustId.get(0);
					
					AccountCreation accountCreationInstance = new AccountCreation();
					accountCreationInstance.setStrAccountNumber(processResponseRes.getAccountNumber());
					customerIdCreationMaster.setStrAccountType(customerIdCreation.getStrAccountType());
					
					amsLogger.writeInfoLog("Inside generateCustomerAccounNo customerIdCreationMaster::"+customerIdCreationMaster);
					createAccountMasterData(customerIdCreationMaster, accountCreationInstance);				
					
					if (!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
					{
						setAccountLimitsInfo(accountCreationInstance);
					}
					
					if (accountTypeObject.getStrAccountTypeCategory().equalsIgnoreCase("C")) 
					{
						setDefaultAccountCreditLimitsInfo(accountCreationInstance);
					}

					amsLogger.writeInfoLog("Inside generateCustomerAccounNo accountCreationInstance--222---::"+accountCreationInstance);
					accountCreationInstance = saveAutoCreationInfo(accountCreationInstance, accountTypeObject);
					
					amsLogger.writeInfoLog("Inside generateCustomerAccounNo accountCreationInstance--*****888---::"+accountCreationInstance);
					if (accountCreationInstance!=null && accountCreationInstance.getStrID()!=null) 
					{
						/* Changes done for QrCode Genaration and update in account_master - Start */
						QrInfo qrInfo = new QrInfo();
						qrInfo.setAccount_no(accountCreationInstance.getStrAccountNumber());
						qrInfo.setMobile_no(accountCreationInstance.getStrMobileNo());
						qrInfo.setAccount_name(accountCreationInstance.getStrFirstName());
						qrInfo.setAccount_type(accountTypeObject.getStrAccountType());
						
						qrCodeService.updateQrCodetoAccount(qrInfo);
						/* Changes done for QrCode Genaration and update in account_master - End */
						
						processResponseRes.setStrMobileNumber(accountCreationInstance.getStrMobileNo());
						processResponseRes.setStrEmail(accountCreationInstance.getStrEmailID());
						processResponseRes.setAccountNumber(accountCreationInstance.getStrAccountNumber());
						
						if (processResponseRes.getStrEmail()!=null && processResponseRes.getStrEmail().trim().length() > 0) 
						{
							sendMailToCustomer(processResponseRes.getAccountNumber(), processResponseRes.getStrEmail(), processResponseRes.getStrMobileNumber(), participantId);
							processResponseRes.setMessage("Successfully account number generated.Please check on your Email.");
						}
						else 
						{
							processResponseRes.setMessage("Successfully Account Number Generated.");
						}
					}
					else
					{
						processResponseRes.setCode("E0000");
						processResponseRes.setStatus("Failed");
						processResponseRes.setMessage("Issue Occurs during Account saving!!");
					}
				}
			}
			else 
			{
				processResponseRes.setCode("E0000");
				processResponseRes.setStatus("Failed");
				processResponseRes.setMessage("Customer Not Found");
			}
		}
		catch (Exception e) 
		{
			processResponseRes.setCode("E0000");
			processResponseRes.setStatus("Failed");
			processResponseRes.setMessage("Internal Server Error ");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponseRes;
	}
	
	public ProcessResponse generateCustomerAccounNo(AccountTypeMaster accountTypeObject, ProcessResponse processResponse) 
	{
		try 
		{
			amsLogger.writeInfoLog("Inside generateCustomerAccounNo Application Name=["+CommonConstants.applicationName+"]");
			if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				// generating 10 digit nuban account no
				String strNubanType = accountTypeObject.getStrNubanType();
				
				NUBANAccountDto nubanAccountDto = new NUBANAccountDto();
				nubanAccountDto.setStrNubanType(strNubanType);
				nubanAccountDto.setStrAccountType(accountTypeObject.getStrAccountType());
				
				NUBANAccountDto accountNo = nubanCodeConfigService.getAccountNo(nubanAccountDto);
				String strAccountNo = accountNo.getStrAccountNo();
				String finalAccountNo = strAccountNo.substring(6);
				processResponse.setAccountNumber(finalAccountNo);
			} 
			else 
			{
				String finalAccountNo = Utils.getUpdatedAccNumber(accountTypeObject.getStrLastAccNumber());
				processResponse.setAccountNumber(finalAccountNo);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Server Issue occurs during account number generation");
		}
		amsLogger.writeInfoLog("Inside generateCustomerAccounNo ***processResponse***=["+processResponse+"]");
		return processResponse;
	}
	
	public AccountCreation createAccountMasterData(CustomerIdCreation customerIdCreationMaster, AccountCreation accountCreationInstance) 
	{
		try 
		{
			accountCreationInstance.setStrCustId(customerIdCreationMaster.getStrCustId());
			accountCreationInstance.setStrAccountType(customerIdCreationMaster.getStrAccountType());
			
			accountCreationInstance.setStrFirstName(customerIdCreationMaster.getStrFirstName());
			accountCreationInstance.setStrLastName(customerIdCreationMaster.getStrLastName());
			accountCreationInstance.setStrMiddleName(customerIdCreationMaster.getStrMiddleName());
			accountCreationInstance.setStrCity(customerIdCreationMaster.getStrCity());
			accountCreationInstance.setStrCountry(customerIdCreationMaster.getStrCountry());
			accountCreationInstance.setStrGender(customerIdCreationMaster.getStrGender());
			accountCreationInstance.setStrMobileNo(customerIdCreationMaster.getStrMobileNo());
			accountCreationInstance.setStrTitle(customerIdCreationMaster.getStrTitle());
			accountCreationInstance.setStrPhoneCode(customerIdCreationMaster.getStrPhoneCode());
			accountCreationInstance.setStrPinCode(customerIdCreationMaster.getStrPinCode());
			accountCreationInstance.setStrState(customerIdCreationMaster.getStrState());
			
			accountCreationInstance.setStrAddress1(customerIdCreationMaster.getStrAddress1());
			accountCreationInstance.setStrAddress2(customerIdCreationMaster.getAddress2());
			accountCreationInstance.setStrAddress3(customerIdCreationMaster.getAddress3());
			accountCreationInstance.setStrEmailID(customerIdCreationMaster.getStrEmailID());
			
			if (customerIdCreationMaster.getBirthDate()!=null) 
			{
				accountCreationInstance.setBirthDate(customerIdCreationMaster.getBirthDate());
			}
			
			accountCreationInstance.setStrDateOfCreation(Utils.getCurrentDate());
			
			accountCreationInstance.setStrCreatedBy("System");
			accountCreationInstance.setStrStatus("Active");
			accountCreationInstance.setStrClosingBalance("0");
			accountCreationInstance.setStrOpeningBalance("0");
			accountCreationInstance.setStrLoadCount("0");
			accountCreationInstance.setStrIsLinkedwithCard("N");
			accountCreationInstance.setStrIsInstantAccount("N");
			accountCreationInstance.setStrTotalOutstandingBal("0");
			accountCreationInstance.setStrParticipantID(customerIdCreationMaster.getStrParticipantID());
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		amsLogger.writeInfoLog("Inside createAccountMasterData accountCreationInstance::"+accountCreationInstance);
		return accountCreationInstance;
	}
	
	public AccountCreation setAccountLimitsInfo(AccountCreation accountCreationInstance) 
	{
		try 
		{
			AccountTransactionLimitation accountTransactionLimitation = new AccountTransactionLimitation();
			accountTransactionLimitation.setStrAccountType(accountCreationInstance.getStrAccountType());
			
			AccountTransactionLimitation accountTransactionLimitationObj = accountTransactionLimitationService.getAccountTxnLimitBasedOnParam(accountTransactionLimitation);
			if (accountTransactionLimitationObj != null) 
			{
				accountCreationInstance.setStrAvailableDailyLimit(accountTransactionLimitationObj.getStrDailyTxnLimit());
				accountCreationInstance.setStrAvailableMonthlyLimit(accountTransactionLimitationObj.getStrMonthlyTxnLimit());
				accountCreationInstance.setStrAvailableYearlyLimit(accountTransactionLimitationObj.getStrYearlyTxnLimit());
				
				accountCreationInstance.setStrDailyTxnLimit(Integer.valueOf(accountTransactionLimitationObj.getStrDailyTxnLimit()));
				accountCreationInstance.setStrMonthlyTxnLimit(accountTransactionLimitationObj.getStrMonthlyTxnLimit());
				accountCreationInstance.setStrYearlyTxnLimit(accountTransactionLimitationObj.getStrYearlyTxnLimit());
				accountCreationInstance.setStrSingleTxnLimit(Integer.valueOf(accountTransactionLimitationObj.getStrSingleTxnLimit()));
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountCreationInstance;
	}
	
	public AccountCreation setDefaultAccountCreditLimitsInfo(AccountCreation accountCreationInstance) 
	{
		try 
		{
			AccountCreditLimitCategory accountCreditLimitCategory = new AccountCreditLimitCategory();
			accountCreditLimitCategory.setStrParticipantId(appInfo.getStrParticipantId());
			accountCreditLimitCategory.setStrCreditType("GENERAL");
			
			accountCreditLimitCategory = accountCreditLimitCategoryDao.getAccountCreditLimitCategoryObj(accountCreditLimitCategory);
			if (accountCreditLimitCategory != null) 
			{
				accountCreationInstance.setStrCreditLimitAmount(accountCreditLimitCategory.getStrCreditLimit());
				accountCreationInstance.setStrCreditLimitCategory(accountCreditLimitCategory.getStrCreditType());
				accountCreationInstance.setStrAvailableCreditLimit(accountCreditLimitCategory.getStrCreditLimit());
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountCreationInstance;
	}
	
	public boolean sendMailToCustomer(String autoGeneratedAcNo, String custEmailId, String custMobileNo, String participantId) 
	{
		try 
		{
			EmailTemplate emailTemplate = new EmailTemplate();
			
			String subject = "Account Creation Details";
			StringBuilder bodyMsg = new StringBuilder("Dear User,<br/><br/>");
			bodyMsg.append("Please find the below account number ");
			if (participantId != null && participantId.trim().length() > 0) //This is for Montra now
			{
				bodyMsg.append(": <br/>The Account Number is : <b>");
				bodyMsg.append(autoGeneratedAcNo);
			}
			else
			{
				bodyMsg.append("and user id : <br/> Account Number is : <b>");
				bodyMsg.append(autoGeneratedAcNo);
				
				bodyMsg.append(" </b> <br/> User Id is: <b>");			
				bodyMsg.append(custMobileNo);
			}			
			bodyMsg.append("</b>");
			
			//bodyMsg.append("</b><br/><br/><br/>");			
			//bodyMsg.append("Powered by AMS Technologies Pvt Ltd.");
			
			emailTemplate.setStrFrom("contactus@AMStechnologies.com");
			emailTemplate.setStrTo(custEmailId);
			emailTemplate.setStrSubject(subject);
			emailTemplate.setStrText(bodyMsg.toString());
			
			emailTemplate.setStrParticipantid(participantId);
			
			String result =	emailService.sendSimpleHtmlContentMessage(emailTemplate);
			if (result!=null) 
			{
				return true;
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return false;
	}
	
	@Override
    public CustomerIdCreation createCustomerMaster(CustomerInfo customerIdCreation) throws Exception 
	{
		//getting the cust Id
		CustomerIdMap custId = customerIdCreationConfigIF.getCustId();  //1131
		String strCustID = custId.getStrCustID();
		
		// cust id is assigned to Customer_Master of User
		CustomerIdCreation customerIdCreationData = customerIDCreationService.mapDataToCustomerIdCreation(customerIdCreation, strCustID);
		
		CustomerIdCreation customerIdCreationObj = customerIDCreationService.saveCustomerMaster(customerIdCreationData); //Entry in Customer Master Table
		if(customerIdCreationObj != null && customerIdCreationObj.getStrID() != null) 
		{
			CustomerIdTable custIdTable = new CustomerIdTable();
			custIdTable.setStrAction(custId.getStrAction());
			custIdTable.setstrLastTxnSerialNo(strCustID);
			custIdTable.setstrJulianDate(custId.getStrJulianDate());
			custIdTable.setStrYear(custId.getStrYear());
		
			customerIdTableService.updateCustIdTable(custIdTable);
		}
		return customerIdCreationObj;
	}

	@Override
	public boolean isAccountActivetBasedOnAccountNumber(AccountMaster accountMaster) 
	{
		AccountMaster accountMasterObj = accountMasterDao.getAccountTypeIsActiveOnAccountNumber(accountMaster);
		if(accountMasterObj != null && accountMasterObj.getStrStatus() != null) 
		{
			if(accountMasterObj.getStrStatus().equalsIgnoreCase("Active")) 
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	@Override
	public int updateAccountStatusByAccountId(AccountCreation accountMaster) 
	{
		return accountMasterDao.updateAccountStatusByAccountId(accountMaster);
	}

	//@Transactional
	@Override
	public AccountResponse getAccountMasterInformation(AccountCreation accountCreation)
	{
		return accountMasterDao.getAccountMasterInformation(accountCreation);
	}
	
	@Override
	public List<AccountCreation> getAccoutBalanceList(AccountCreation accountCreation) 
	{
		return accountMasterDao.getAccoutBalanceList(accountCreation);
	}
	
	@Override
	public List<AccountInfoResponse> getAccoutInfoListById(AccountCreation accountCreation) 
	{
		List<AccountInfoResponse> accountInfoList = new ArrayList<AccountInfoResponse>();
		List<AccountCreation> accountCreationList = accountMasterDao.getAccoutInfoListbyCustId(accountCreation);
		for(int i=0; i<accountCreationList.size(); i++)
		{
			AccountInfoResponse accountInfoResponse = new AccountInfoResponse();
			accountInfoResponse.setAccountType(accountCreationList.get(i).getStrAccountType());
			accountInfoResponse.setAccountNo(accountCreationList.get(i).getStrAccountNumber());
			accountInfoResponse.setAccountBalance(accountCreationList.get(i).getStrClosingBalance());
			accountInfoResponse.setAccountName(accountCreationList.get(i).getStrAccountHolderName());
			accountInfoResponse.setMccCode(accountCreationList.get(i).getMccCode());
			
			accountInfoList.add(accountInfoResponse);
		}
		return accountInfoList;
	}
	
	@Override
	public List<AccountInfoResponse> getAccoutInfoListByAccountType(AccountCreation accountCreation) 
	{
		List<AccountInfoResponse> accountInfoList = new ArrayList<AccountInfoResponse>();
		List<AccountCreation> accountCreationList = accountMasterDao.getAccoutInfoListByAccountType(accountCreation);
		
		if (accountCreationList!=null && accountCreationList.size() > 0) 
		{
			for(int i=0; i < accountCreationList.size(); i++)
			{
				AccountInfoResponse accountInfoResponse = new AccountInfoResponse();
				accountInfoResponse.setAccountType(accountCreationList.get(i).getStrAccountType());
				accountInfoResponse.setAccountNo(accountCreationList.get(i).getStrAccountNumber());
				accountInfoResponse.setAccountBalance(accountCreationList.get(i).getStrClosingBalance());
				accountInfoResponse.setAccountName(accountCreationList.get(i).getStrAccountHolderName());
				accountInfoResponse.setMccCode(accountCreationList.get(i).getMccCode());
				
				accountInfoList.add(accountInfoResponse);
			}
		}
		return accountInfoList;
	}

	@Override
	public List<AccountMaster> getAllActiveAccount() {
		return accountMasterDao.getAllActiveAccount();
	}

	@Override
	public AccountMaster getBaseWalletAccount(AccountMaster baseWallet) {
		// TODO Auto-generated method stub
		return accountMasterDao.getBaseWalletAccount(baseWallet);
	}

	@Override
	public void updateBaseWalletAccountMaster(AccountMaster baseWalletAccount) {
		 accountMasterDao.updateBaseWalletAccountMaster(baseWalletAccount);
		
	}

	@Override
	public int createSubAccountForMultiCurrency(AccountCreation accountCreation) {
		int result = 0;
		try 
		{
			String accountNumber = accountCreation.getStrAccountNumber();
			String strCreatedBy = accountCreation.getStrCreatedBy();
			
			List<MultiCurrencyWalletAccountTypeMaster> listDataAccountTypeWisePriorityWallet = getAccountTypeWisePriorityWallet(accountCreation) ;
			
			if (listDataAccountTypeWisePriorityWallet!=null && listDataAccountTypeWisePriorityWallet.size() > 0) 
			{
				List<MultiCurrencyWalletAccountMaster> listOfCurrecnyAccountMasters = new ArrayList<MultiCurrencyWalletAccountMaster>();
				
				for (MultiCurrencyWalletAccountTypeMaster accountWisePriorityWallet : listDataAccountTypeWisePriorityWallet) 
				{
					MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster = new MultiCurrencyWalletAccountMaster();
					multiCurrencyWalletAccountMaster.setParticipantId(accountCreation.getStrParticipantID());
					multiCurrencyWalletAccountMaster.setStrCreatedBy(strCreatedBy);
					multiCurrencyWalletAccountMaster.setStrCreatedDate(new Date());
					multiCurrencyWalletAccountMaster.setBaseCurrencyAccountType(accountWisePriorityWallet.getBaseCurrencyAccountType());
					multiCurrencyWalletAccountMaster.setStrAccountType(accountWisePriorityWallet.getStrAccountType());
					multiCurrencyWalletAccountMaster.setStrAccountNumber(accountNumber);
					multiCurrencyWalletAccountMaster.setStrCurrencyCode(accountWisePriorityWallet.getStrCurrencyCode());					
					multiCurrencyWalletAccountMaster.setStrClosingBalance(Utils.stringToDouble(accountCreation.getStrClosingBalance()));
					multiCurrencyWalletAccountMaster.setStrPriority(accountWisePriorityWallet.getStrPriority());
					
					String currencyAccNo = multiCurrencyWalletAccountService.generateMultiCurrencyAccountNumber(multiCurrencyWalletAccountMaster);
					multiCurrencyWalletAccountMaster.setStrCurrencyWalletAccountNumber(currencyAccNo);
					
					listOfCurrecnyAccountMasters.add(multiCurrencyWalletAccountMaster);
				}
				
				if (listOfCurrecnyAccountMasters.size() > 0) 
				{
					result = multiCurrencyWalletAccountService.createWalletAccountBasedOnCurrencyWise(listOfCurrecnyAccountMasters);
					if (result > 0) 
					{
						return result;
					}
				}
			}
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return result;
	}

	private List<MultiCurrencyWalletAccountTypeMaster> getAccountTypeWisePriorityWallet(
			AccountCreation accountCreation) {
		List<MultiCurrencyWalletAccountTypeMaster> listData = null;
		try 
		{
			MultiCurrencyWalletAccountTypeMaster accountWisePriorityWallet = new MultiCurrencyWalletAccountTypeMaster();
			accountWisePriorityWallet.setBaseCurrencyAccountType(accountCreation.getStrAccountType());
			
			listData = multiCurrencyWalletAccountTypeService.getAccountTypeWiseMultiCurrencyWalletList(accountWisePriorityWallet);
			
			if (listData != null && listData.size() > 0) 
			{
				return listData;
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return listData;
	}

	@Override
	public AccountCreation getAccountDetailsObj(AccountCreation accountMaster) {
		return accountMasterDao.getAccountDetails(accountMaster);
		
		}


}
