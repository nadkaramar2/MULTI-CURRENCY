package ams.cms.dao;

import java.util.List;

import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.AccountTxnBalanceRequest;
import ams.cms.api.model.CustomerByAccountResponse;
import ams.cms.api.model.UserTransactionModel;
import ams.cms.model.AccountCreation;
import ams.cms.utility.AccountMasterResponse;

public interface AccountMasterDao extends GenericDao<AccountCreation>
{
	List<AccountCreation> getAccountInformationList(AccountCreation accountCreation);
	
	List<AccountCreation> getAccountInformationListById(String id);
	
	int updateAccountCreationFromInstanceAccount(AccountCreation accountCreation);
	
	int updateAccountCreation(AccountCreation accountCreation);
	
	List<AccountCreation> getAccountInformationListByTypes(AccountCreation accountCreation);
	
	int[] batchEntryOfInstanAccount(List<AccountCreation> accountCreationlist);
	
	AccountCreation getAccountInformation(AccountCreation accountCreation);
	
	String getAvailableBalanceBasedOnAccountTypeAndNumber(AccountCreation accountCreation);
	
	int updateBalanceRelatedData(AccountCreation accountCreation);

	List<AccountCreation> getAccountProfilelist(AccountCreation accountCreation);

	List<AccountCreation> getCreditCardBalancelist(AccountCreation accountCreation);

	List<AccountCreation> getCreditCardInterestlist(AccountCreation accountCreation);

	List<AccountCreation> getAccountBalanceView(AccountCreation accountCreation);

    List<AccountCreation> getAccountIssueData(AccountCreation accountCreation);

    AccountCreation getOutstandingAccount(AccountCreation accountCreation);
		
	List<AccountCreation> getAccountClosingBalance(AccountTxnBalanceRequest accountTxnBalanceRequest);
	
	int updateAccountAfterLinkedCard(AccountCreation accountCreation);
	
	int updateLastLoginDate(AccountCreation accountCreation);
	
	boolean isAccountAlreadyExist(AccountCreation accountCreation);
	
	List<ams.cms.scheduler.model.AccountWiseInterestMaster> getCreditAccountWiseInterestList(ams.cms.scheduler.model.AccountWiseInterestMaster accountWiseInterestMaster);
	
	boolean isCustomerAccountAlreadyConfigured(AccountCreation accountCreation);
	
	AccountMaster getAccountDetails(AccountMaster accountMaster);
	
	int updateAccountMasterFields(AccountMaster accountMaster);	
	
	AccountCreation viewAccountMasterDetails(AccountCreation accountCreation);

	int updateBalnceLimitValues(AccountCreation accountCreation);
	
	AccountCreation getSenderAccountInformation(AccountCreation accountCreation);
	
	AccountCreation getSenderAccountInformationWithoutCustId(AccountCreation accountCreation);
	
	AccountCreation getRecipientAccountInformation(AccountCreation accountCreation);
	
	List<AccountCreation> getSignInDataFromApp(AccountCreation accountCreation);
	
	List<AccountCreation> getUpdatedAccountsListForApp(AccountCreation accountCreation);
	
	int updateAccountMasterFieldsBasedOnQuery(String updateQuery);
	
	int updateCreditLimitValues(AccountCreation accountCreation);
	
	int updateLimitValues(AccountCreation accountCreation);

	AccountCreation updategracePeriodDate(AccountCreation accountCreation);

	int updateTotalOutstanding(AccountCreation accountCreation);
	
	int updateAccountBalance(AccountCreation accountCreation);
	
	int updateAccountCreationFields(AccountCreation accountCreation);

	//created by ankit
	List<AccountCreation> getAcountNameAndBalance(AccountCreation accountCreation);
	//created by ankit
	
	public AccountMaster getAccountQRDetails(AccountMaster accountMaster ) ;
	
	int updateTransactionLimit(AccountCreation accountCreation);

	AccountCreation getTransactionLimits(AccountCreation accountCreation);
	
	AccountMaster getAccountNameByAccountNo(AccountMaster accountMaster );
	
	/*
	 * //created by ankit List<AccountCreation>
	 * getAcountNameAndBalance(AccountCreation accountCreation); //created by ankit
	 */
	//created by ankit on 15-04-2023
	List<AccountCreation> isMerchantVerified(AccountCreation accountCreation);
	//created by ankit on 15-04-2023

	//created by ankit on 16-04-2023
	List<AccountCreation> getTransactionPayeeDetailsByAccountNumber(AccountCreation accountCreation);
	//created by ankit on 16-04-2023

	//created by ankit on 16-04-2023
	List<AccountCreation> getAccountInfoAndStatus(AccountCreation accountCreation);
	//created by ankit on 16-04-2023

	//created by ankit on 16-04-2023
	int updateOnlyBalance(AccountCreation accountCreation);
	//created by ankit on 16-04-2023

	//created by ankit on 16-04-2023
	List<AccountCreation> getReceiverInformation(AccountCreation accountCreation);
	//created by ankit on 16-04-2023
	
	//Added by Pankaj Pawar Start
	AccountMaster getAccountInfo(AccountMaster accountMaster);
	
	int updatePerformTxnValues(AccountCreation accountCreation);
	
	List<AccountMaster> getAccountInfoList(UserTransactionModel userTransactionModel);
	//Added by Pankaj Pawar End
	
	AccountCreation getRecipientAccountInformationWithoutCustId(AccountCreation accountCreation);
	
	
	//added by prashant
	AccountCreation getClosingBalanceonAccTypeAccNo(AccountCreation accountCreation);

	int updateClosingBalance(AccountCreation accountCreation);
	
	List<AccountCreation> getRegCustWithLinkAccount(AccountCreation accountCreation);

	AccountCreation getTierInfoByCustID(AccountCreation accountCreation);
	
	List<AccountMasterResponse> getAccountInformationForClosingAccount(AccountCreation accountCreation);
	
	//added by Sunil Y ,
	int updateAccountStatus(AccountMaster accountMaster);

	int updateAccountStatusAndBlance(AccountMaster accountMaster);
	
	//added by sunil Y
	AccountMaster getAccountMasterByAccountNumber(AccountMaster accountMaster);
	
	//created by ankit on 03-06-2023
	List<AccountCreation> getAccountHolderName(AccountCreation accountCreation);
	//created by ankit on 03-06-2023

	//created by ankit on 06-06-2023
	List<AccountCreation> findByAccountNumber(AccountCreation accountCreation);
	//created by ankit on 06-06-2023

	//created by ankit on 08-06-2023
	void updateEarMark(AccountCreation accountCreation);

	//created by ankit on 09-06-2023
	void reduceEarMarkAmount(AccountCreation accountCreation);

	//created by ankit on 09-06-2023
	AccountCreation getEarMarkAndClosingBalance(AccountCreation accountCreation);

	//created by ankit on 13-06-2023
	void preCredAccount(AccountCreation accountMaster);

	//created by ankit on 13-06-2023
	AccountCreation getPreCredAndClosingBalance(AccountCreation accountCreation);

	int earMarkTransactionAmount(AccountCreation accountMaster);

	int preCredTransactionAmount(AccountCreation accountMaster);

	void addPreCredAmount(AccountCreation accountCreation);

	CustomerByAccountResponse getCustomerAccountInfoByAccountNo(AccountCreation accountCreation);
	
	CustomerByAccountResponse getCustomerAccountInfoByAccountNoForNonNigeria(AccountCreation accountCreation);
	
	int[] batchEntryOfEarMarkUpdates(List<AccountCreation> accountCreationlist) throws Exception;
	
	int[] batchEntryOfPredCredUpdates(List<AccountCreation> accountCreationlist) throws Exception;
	
	int updatePreCredAmountOfCustomerAccount(AccountCreation accountCreation);
	
	int updateEarMarkAmountOfCustomerAccount(AccountCreation accountCreation);
	
	int[] updatesBatchEntryOfToAccount(List<AccountCreation> accountCreationlist);
	
	int[] updatesBatchEntryOfFromAccount(List<AccountCreation> accountCreationlist);
	
	int[] updatesBatchEntryOfLimitsAccountMasterFields(List<AccountCreation> accountCreationlist);
	
	AccountCreation getAccountLimitInfoToCompareTransferAmount(AccountCreation accountCreation);

	AccountCreation getCumlativeBalanceCompareTransAmt(AccountCreation accountCreation);
	
	int updateAccountBalanceWithoutAccountType(AccountCreation accountCreation);

	AccountMaster getAccountTypeIsExist(AccountMaster accountMaster);
	
	AccountMaster getAccountTypeIsActiveOnAccountNumber(AccountMaster accountMaster);
	
	int updateAccountStatusByAccountId(AccountCreation accountMaster);
	
	AccountResponse getAccountMasterInformation(AccountCreation accountCreation);
	
	List<AccountCreation> getAccoutBalanceList(AccountCreation accountCreation);
	
	List<AccountCreation> getAccoutInfoListbyCustId(AccountCreation accountCreation);
	
	List<AccountCreation> getAccoutInfoListByAccountType(AccountCreation accountCreation);
	
	//Added By Sunil Y , Dormancy Releated [Start - 2023-09-26] 
	List<AccountCreation> getActiveAccountList(AccountCreation accountCreation);

	int updateAccountMasterForDormancy(AccountCreation accountCreation);
	
	int[] updatesBatchEntryOfDormancyAccountMasterFields(List<AccountCreation> accountCreationlist);

	int updateAccountMasterForDormant(AccountCreation accountCreation);
	
	void updateLastTxnDate(AccountCreation accountCreation);
	//Added By Sunil Y , Dormancy Releated [Start - 2023-09-26] 

	List<AccountMaster> getAllActiveAccount();

	AccountMaster getBaseWalletAccount(AccountMaster baseWallet);

	void updateBaseWalletAccountMaster(AccountMaster baseWalletAccount);

	AccountCreation getAccountDetails(AccountCreation accountMaster);
}
