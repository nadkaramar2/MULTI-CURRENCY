package ams.cms.services;

import java.util.List;

import ams.cms.api.model.AccountBalanceResponse;
import ams.cms.api.model.AccountCreationResponse;
import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.AccountTxnBalanceRequest;
import ams.cms.api.model.CustomerByAccountResponse;
import ams.cms.api.model.RefundRequest;
import ams.cms.api.model.ReverseTransactionRequest;
import ams.cms.api.model.UserTransactionModel;
import ams.cms.api.response.AccountInfoResponse;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountTypeMaster;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.CustomerInfo;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.AccountMasterResponse;

public interface AccountMasterService
{
	AccountCreation saveAccountInformation(AccountCreation accountCreation) throws Exception;
	
	List<AccountCreation> getAccountInformationList(AccountCreation accountCreation) throws Exception;
	
	List<AccountCreation> getAccountInformationList() throws Exception;
	
	List<AccountCreation> getAccountInformationListById(String id) throws Exception;
	
	int updateAccountCreationFromInstanceAccount(AccountCreation accountCreation) throws Exception;
	
	int updateAccountCreation(AccountCreation accountCreation) throws Exception;
	
	List<AccountCreation> getAccountInformationListByTypes(AccountCreation accountCreation) throws Exception;
	
	int[] batchEntryOfInstanAccount(List<AccountCreation> accountCreationlist) throws Exception;
	
	int createSubAccountForWallet(AccountCreation accountCreation) throws Exception;
	
	AccountCreation getAccountInformation(AccountCreation accountCreation) throws Exception;
	
	String getAvailableBalanceBasedOnAccountTypeAndNumber(AccountCreation accountCreation) throws Exception;
	
	int updateBalanceRelatedData(AccountCreation accountCreation) throws Exception;
	
	List<AccountCreationResponse> getAccountProfilelist(AccountCreation accountCreation) throws Exception;

	List<AccountBalanceResponse> getAccountBalanceView(AccountCreation accountCreation);

	List<AccountCreation> getClosingBalance(AccountTxnBalanceRequest accountTxnBalanceRequest);

	List<AccountCreation> getAccountIssueData(AccountCreation accountCreation);

	AccountCreation getOutstandingAccount(AccountCreation accountCreation);
	
	int updateAccountAfterLinkedCard(AccountCreation accountCreation) throws Exception;
	
	int updateLastLoginDate(AccountCreation accountCreation) throws Exception;
	
	boolean isAccountAlreadyExist(AccountCreation accountCreation) throws Exception;
	
	boolean isCustomerAccountAlreadyConfigured(AccountCreation accountCreation) throws Exception;
	
	AccountMaster getAccountDetails(AccountMaster accountMaster);
	
	int updateAccountMasterFields(AccountMaster accountMaster) throws Exception;
	
	AccountCreation viewAccountMasterDetails(AccountCreation accountCreation);

	int updateBalnceLimitValues(AccountCreation accountCreation);
	
	AccountCreation getSenderAccountInformation(AccountCreation accountCreation) throws Exception;
	
	AccountCreation getRecipientAccountInformation(AccountCreation accountCreation) throws Exception;
	
	List<AccountCreation> getSignInDataFromApp(AccountCreation accountCreation) throws Exception;
	
	List<AccountCreation> getUpdatedAccountsListForApp(AccountCreation accountCreation) throws Exception;
	
	int updateAccountMasterFieldsBasedOnQuery(String updateQuery) throws Exception; 
	
	void updateAccountMasterSomeFields(AccountCreation accountCreation) throws Exception;
	
	int updateCreditLimitValues(AccountCreation accountCreation) throws Exception;
	
	int updateLimitValues(AccountCreation accountCreation) throws Exception;
	
	AccountCreation updategracePeriodDate(AccountCreation accountCreation);

	int updateTotalOutstanding(AccountCreation accountCreation);
	
	int updateAccountBalance(AccountCreation accountCreation) throws Exception;
	
	int updateAccountCreationFields(AccountCreation accountCreation) throws Exception;

	//created by ankit
	List<AccountCreation> getAccountBalanceAndName(AccountCreation accountCreation);
	//created by ankit
	
	AccountMaster getAccountQRDetails(AccountMaster accountMaster);
	
	int updateTransactionLimit(AccountCreation accountCreation);

	AccountCreation getTransactionLimits(AccountCreation accountCreation);
	
	AccountMaster getAccountNameByAccountNo(AccountMaster accountMaster );
	
	//created by ankit on 15-04-2023
	List<AccountCreation> verifyMerchantForRefundRequest(RefundRequest refundRequest);
	//created by ankit on 15-04-2023

	//created by ankit on 15-04-2023
	int verifyMerchantForRefundTransaction(ReverseTransactionRequest refundTransactionRequest);
	//created by ankit on 15-04-2023

	//created by ankit on 16-04-2023
	List<AccountCreation> getAccountInfoAndStatus(AccountCreation accountCreation);
	//created by ankit on 16-04-2023

	//created by ankit on 16-04-2023
	int updateOnlyBalance(AccountCreation accountCreation);
	//created by ankit on 16-04-2023
	
	//created by ankit on 17-04-2023
	List<AccountCreation> getReceiverInfromation(AccountCreation accountCreation);
	//created by ankit on 17-04-2023

	//created by ankit on 18-04-2023
	List<AccountCreation> getAccountInfoByAccountNumber(AccountCreation accountCreation);
	//created by ankit on 18-04-2023

	List<AccountCreation> getTransactionPayeeDetailsByAccountNo(AccountCreation toAccountCreation);
	
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
	
	//created by ankit on 03-06-2023
	List<AccountCreation> getAccountHolderName(AccountCreation accountCreation);
	//created by ankit on 03-06-2023

	//created by ankit on 06-06-2023
	List<AccountCreation> findByAccountNumber(AccountCreation accountCreation);
	//created by ankit on 06-06-2023

	//created by ankit 
	void earMarkAccount(AccountCreation account);

	//created by ankit 
	void reduceEarBalance(AccountCreation accountCreation);

	//created by ankit
	void preCredAccount(AccountCreation accountMaster);
	
	//created by ankit
	void creditPreCred(AccountCreation accountCreation);
	
	void earMarkTransactionAmount(AccountCreation earMarkAccountMaster);

	void addPreCredAmount(AccountCreation accountCreation);
	
	CustomerByAccountResponse getCustomerAccountInfoByAccountNo(AccountCreation accountCreation);
	
	CustomerByAccountResponse getCustomerAccountInfoByAccountNoForNonNigeria(AccountCreation accountCreation);
	
	int updatePreCredAmountOfCustomerAccount(AccountCreation accountCreation) throws Exception;
	
	int updateEarMarkAmountOfCustomerAccount(AccountCreation accountCreation) throws Exception;
	
	String getUpdatablePreCredAmount(String existingPreCredAmount, String txnAmount);
	
	String getUpdatableEarMarkAmount(String existingEarMarkAmount, String txnAmount);
	
	String getUpdatableReducePreCredAmount(String existingPreCredAmount, String txnAmount);
	
	String getUpdatableReduceEarMarkAmount(String existingEarMarkAmount, String txnAmount);
	
	int[] batchEntryOfEarMarkUpdates(List<AccountCreation> accountCreationlist) throws Exception;
	
	int[] batchEntryOfPredCredUpdates(List<AccountCreation> accountCreationlist) throws Exception;
	
	String getUpdatableReduceAccountMasterData(String existingAmount, String txnAmount) throws Exception;

	int[] updatesBatchEntryOfToAccount(List<AccountCreation> accountCreationlist) throws Exception;

	int[] updatesBatchEntryOfFromAccount(List<AccountCreation> accountCreationlist) throws Exception;

	int[] updatesBatchEntryOfLimitsAccountMasterFields(List<AccountCreation> accountCreationlist) throws Exception;
	
	AccountCreation saveAutoCreationInfo(AccountCreation accountCreation, AccountTypeMaster accountTypeObject);
	
	AccountCreation getAccountLimitInfoToCompareTransferAmount(AccountCreation accountCreation);

	AccountCreation getCumlativeBalanceCompareTransAmt(AccountCreation accountCreation);
	
	int updateAccountBalanceWithoutAccountType(AccountCreation accountCreation) throws Exception;
	
	boolean isAccountAlreadyExistBasedOnType(AccountMaster accountMaster);
	
	boolean isAccountActivetBasedOnAccountNumber(AccountMaster accountMaster);
	
	ProcessResponse createAccountMaster(CustomerIdCreation customerIdCreation, AccountTypeMaster accountTypeObject, ProcessResponse processResponse);
	
	ProcessResponse generateCustomerAccounNo(AccountTypeMaster accountTypeObject, ProcessResponse processResponse);
	
	CustomerIdCreation createCustomerMaster(CustomerInfo customerIdCreation) throws Exception;

	int updateAccountStatusByAccountId(AccountCreation accountMaster);
	
	AccountResponse getAccountMasterInformation(AccountCreation accountCreation);
	
	List<AccountCreation> getAccoutBalanceList(AccountCreation accountCreation);
	
	List<AccountInfoResponse> getAccoutInfoListById(AccountCreation accountCreation);
	
	List<AccountInfoResponse> getAccoutInfoListByAccountType(AccountCreation accountCreation);

	List<AccountMaster> getAllActiveAccount();

	AccountMaster getBaseWalletAccount(AccountMaster baseWallet);
	
	void updateBaseWalletAccountMaster(AccountMaster baseWalletAccount);

	int createSubAccountForMultiCurrency(AccountCreation accountCreationInstance);

	AccountCreation getAccountDetailsObj(AccountCreation accountCreation);
}
