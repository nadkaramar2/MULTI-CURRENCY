# MULTI-CURRENCY API Catalog

**Generated:** 2026-05-21  
**Base URL:** `http://localhost:8285/AccountManagementAPI`  
**Controllers scanned:** 84  
**Total endpoints:** 421  

---

## AccountTransactionLimitController (`api`)
**File:** `ams/cms/api/controller/AccountTransactionLimitController.java`  
**Base path:** `/txnLimitDetails`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/txnLimitDetails/txnCreditLimit` | `post_AccountTransactionLimitController__txnCreditLimit` |
| GET | `/txnLimitDetails/txnLimitDetails` | `get_AccountTransactionLimitController__txnLimitDetails` |
| POST | `/txnLimitDetails/txnPrepaidLimit` | `post_AccountTransactionLimitController__txnPrepaidLimit` |
| POST | `/txnLimitDetails/updateCreditLimitAccount` | `post_AccountTransactionLimitController__updateCreditLimitAccount` |

## AddressApiController (`api`)
**File:** `ams/cms/api/controller/AddressApiController.java`  
**Base path:** `/address`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/address/addAddress` | `post_AddressApiController__addAddress` |
| POST | `/address/address` | `post_AddressApiController__address` |
| POST | `/address/getCity` | `post_AddressApiController__getCity` |
| POST | `/address/getCountry` | `post_AddressApiController__getCountry` |
| POST | `/address/getState` | `post_AddressApiController__getState` |

## CommonApiController (`api`)
**File:** `ams/cms/api/controller/CommonApiController.java`  
**Base path:** `/walletbalanceshow`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/walletbalanceshow/accountTransactionview` | `post_CommonApiController__accountTransactionview` |
| POST | `/walletbalanceshow/accountbalanceview` | `post_CommonApiController__accountbalanceview` |
| POST | `/walletbalanceshow/accountinfo` | `post_CommonApiController__accountinfo` |
| POST | `/walletbalanceshow/accountlinkedcardlist` | `post_CommonApiController__accountlinkedcardlist` |
| POST | `/walletbalanceshow/accountstatement` | `post_CommonApiController__accountstatement` |
| POST | `/walletbalanceshow/accountstatementlist` | `post_CommonApiController__accountstatementlist` |
| POST | `/walletbalanceshow/dyamicQrCodeImgUrl` | `post_CommonApiController__dyamicQrCodeImgUrl` |
| POST | `/walletbalanceshow/lastfivetxnlist` | `post_CommonApiController__lastfivetxnlist` |
| POST | `/walletbalanceshow/oustandingbalancelist` | `post_CommonApiController__oustandingbalancelist` |
| POST | `/walletbalanceshow/outstandinginterestlist` | `post_CommonApiController__outstandinginterestlist` |
| POST | `/walletbalanceshow/tierAccountTxnLimitBasedOnType` | `post_CommonApiController__tierAccountTxnLimitBasedOnType` |
| POST | `/walletbalanceshow/totalOustandingBalance` | `post_CommonApiController__totalOustandingBalance` |
| POST | `/walletbalanceshow/totalOutstandingInterest` | `post_CommonApiController__totalOutstandingInterest` |
| POST | `/walletbalanceshow/updateAccountTxnLimit` | `post_CommonApiController__updateAccountTxnLimit` |
| POST | `/walletbalanceshow/walletbalancelist` | `post_CommonApiController__walletbalancelist` |
| POST | `/walletbalanceshow/walletbalanceshow` | `post_CommonApiController__walletbalanceshow` |
| POST | `/walletbalanceshow/walletpercentageupdate` | `post_CommonApiController__walletpercentageupdate` |

## ComplaintController (`api`)
**File:** `ams/cms/api/controller/ComplaintController.java`  
**Base path:** `/complaint`  

| Method | Path | Operation ID |
|--------|------|--------------|
| GET | `/complaint/all` | `get_ComplaintController__all` |
| POST | `/complaint/allTypes` | `post_ComplaintController__allTypes` |
| POST | `/complaint/complaint` | `post_ComplaintController__complaint` |
| POST | `/complaint/getComplaintOfAccount` | `post_ComplaintController__getComplaintOfAccount` |
| POST | `/complaint/id` | `post_ComplaintController__id` |
| POST | `/complaint/raise` | `post_ComplaintController__raise` |
| POST | `/complaint/resolve` | `post_ComplaintController__resolve` |
| GET | `/complaint/types` | `get_ComplaintController__types` |

## EncryptDecrypytTestingController (`api`)
**File:** `ams/cms/api/controller/EncryptDecrypytTestingController.java`  
**Base path:** `/encryptedData`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/encryptedData/deccryptedData` | `post_EncryptDecrypytTestingController__deccryptedData` |
| POST | `/encryptedData/encryptedData` | `post_EncryptDecrypytTestingController__encryptedData` |
| POST | `/encryptedData/saveBankDetails` | `post_EncryptDecrypytTestingController__saveBankDetails` |

## GenerateTokenController (`api`)
**File:** `ams/cms/api/controller/GenerateTokenController.java`  
**Base path:** `/apiky`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/apiky/apiky` | `post_GenerateTokenController__apiky` |
| POST | `/apiky/getIvPhraseSalt` | `post_GenerateTokenController__getIvPhraseSalt` |

## ImageController (`api`)
**File:** `ams/cms/api/controller/ImageController.java`  
**Base path:** `/image`  

| Method | Path | Operation ID |
|--------|------|--------------|
| GET | `/image/display/{imageName:.+}` | `get_ImageController__display__imageName____` |
| GET | `/image/dqrcode/download/{imageName:.+}` | `get_ImageController__dqrcode_download__imageName____` |
| POST | `/image/image` | `post_ImageController__image` |
| GET | `/image/qrcode/download/{imageName:.+}` | `get_ImageController__qrcode_download__imageName____` |
| POST | `/image/upload` | `post_ImageController__upload` |

## NubanCodeConfigController (`api`)
**File:** `ams/cms/api/controller/NubanCodeConfigController.java`  
**Base path:** `/nubanCode`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/nubanCode/add` | `post_NubanCodeConfigController__add` |
| POST | `/nubanCode/checkConfig` | `post_NubanCodeConfigController__checkConfig` |
| POST | `/nubanCode/get` | `post_NubanCodeConfigController__get` |
| POST | `/nubanCode/getNUBAN` | `post_NubanCodeConfigController__getNUBAN` |
| POST | `/nubanCode/getNubanCode` | `post_NubanCodeConfigController__getNubanCode` |
| POST | `/nubanCode/nubanCode` | `post_NubanCodeConfigController__nubanCode` |

## NubanTypeConfigController (`api`)
**File:** `ams/cms/api/controller/NubanTypeConfigController.java`  
**Base path:** `/nubanType`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/nubanType/getDescription` | `post_NubanTypeConfigController__getDescription` |
| GET | `/nubanType/getType` | `get_NubanTypeConfigController__getType` |
| POST | `/nubanType/nubanType` | `post_NubanTypeConfigController__nubanType` |

## PinController (`api`)
**File:** `ams/cms/api/controller/PinController.java`  
**Base path:** `/pin`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/pin/change` | `post_PinController__change` |
| POST | `/pin/forgot` | `post_PinController__forgot` |
| POST | `/pin/newPIN` | `post_PinController__newPIN` |
| POST | `/pin/pin` | `post_PinController__pin` |
| POST | `/pin/replacePIN` | `post_PinController__replacePIN` |
| POST | `/pin/verifyOTP` | `post_PinController__verifyOTP` |

## QRCodeController (`api`)
**File:** `ams/cms/api/controller/QRCodeController.java`  
**Base path:** `/qrcode`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/qrcode/genarate` | `post_QRCodeController__genarate` |
| POST | `/qrcode/genarateDynamicQr` | `post_QRCodeController__genarateDynamicQr` |
| POST | `/qrcode/qrcode` | `post_QRCodeController__qrcode` |

## RestAPIClientController (`api`)
**File:** `ams/cms/api/controller/RestAPIClientController.java`  
**Base path:** `/test`  

| Method | Path | Operation ID |
|--------|------|--------------|
| GET | `/test/getCardTypes` | `get_RestAPIClientController__getCardTypes` |
| POST | `/test/getClearCardNo` | `post_RestAPIClientController__getClearCardNo` |
| POST | `/test/issueCard` | `post_RestAPIClientController__issueCard` |
| POST | `/test/issueCardDummyRequest` | `post_RestAPIClientController__issueCardDummyRequest` |
| POST | `/test/rest` | `post_RestAPIClientController__rest` |
| GET | `/test/test` | `get_RestAPIClientController__test` |

## SignInController (`api`)
**File:** `ams/cms/api/controller/SignInController.java`  
**Base path:** `/signIn`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/signIn/getAccountInfo` | `post_SignInController__getAccountInfo` |
| POST | `/signIn/getUpdatedAccountsList` | `post_SignInController__getUpdatedAccountsList` |
| POST | `/signIn/signIn` | `post_SignInController__signIn` |

## SignOutController (`api`)
**File:** `ams/cms/api/controller/SignOutController.java`  
**Base path:** `/signOut`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/signOut/signOut` | `post_SignOutController__signOut` |

## SignUpController (`api`)
**File:** `ams/cms/api/controller/SignUpController.java`  
**Base path:** `/verifyMobileNumber`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/verifyMobileNumber/accountCategoryList` | `post_SignUpController__accountCategoryList` |
| POST | `/verifyMobileNumber/accountTypeStore` | `post_SignUpController__accountTypeStore` |
| POST | `/verifyMobileNumber/addAccount` | `post_SignUpController__addAccount` |
| POST | `/verifyMobileNumber/addPOAInfo` | `post_SignUpController__addPOAInfo` |
| POST | `/verifyMobileNumber/addPOIInfo` | `post_SignUpController__addPOIInfo` |
| POST | `/verifyMobileNumber/addressInfo` | `post_SignUpController__addressInfo` |
| GET | `/verifyMobileNumber/addressprooftypes` | `get_SignUpController__addressprooftypes` |
| POST | `/verifyMobileNumber/autoAccountGeneration` | `post_SignUpController__autoAccountGeneration` |
| POST | `/verifyMobileNumber/changePassword` | `post_SignUpController__changePassword` |
| POST | `/verifyMobileNumber/forgotPassword` | `post_SignUpController__forgotPassword` |
| POST | `/verifyMobileNumber/getBasicInformation` | `post_SignUpController__getBasicInformation` |
| GET | `/verifyMobileNumber/getCountryCodeList` | `get_SignUpController__getCountryCodeList` |
| POST | `/verifyMobileNumber/identityInfo` | `post_SignUpController__identityInfo` |
| GET | `/verifyMobileNumber/identityprooftypes` | `get_SignUpController__identityprooftypes` |
| GET | `/verifyMobileNumber/poaTypes` | `get_SignUpController__poaTypes` |
| GET | `/verifyMobileNumber/poiTypes` | `get_SignUpController__poiTypes` |
| POST | `/verifyMobileNumber/reSendOtp` | `post_SignUpController__reSendOtp` |
| POST | `/verifyMobileNumber/registerAccountType` | `post_SignUpController__registerAccountType` |
| POST | `/verifyMobileNumber/sendOtp` | `post_SignUpController__sendOtp` |
| POST | `/verifyMobileNumber/setPassword` | `post_SignUpController__setPassword` |
| POST | `/verifyMobileNumber/upload/addressDocument` | `post_SignUpController__upload_addressDocument` |
| POST | `/verifyMobileNumber/upload/identityDocument` | `post_SignUpController__upload_identityDocument` |
| POST | `/verifyMobileNumber/upload/poa` | `post_SignUpController__upload_poa` |
| POST | `/verifyMobileNumber/upload/poi` | `post_SignUpController__upload_poi` |
| POST | `/verifyMobileNumber/upload/tier1PassportPhoto` | `post_SignUpController__upload_tier1PassportPhoto` |
| POST | `/verifyMobileNumber/upload/tier1Photo` | `post_SignUpController__upload_tier1Photo` |
| POST | `/verifyMobileNumber/upload/tier2PassportPhoto` | `post_SignUpController__upload_tier2PassportPhoto` |
| POST | `/verifyMobileNumber/upload/tier2Photo` | `post_SignUpController__upload_tier2Photo` |
| POST | `/verifyMobileNumber/verifyMobileNumber` | `post_SignUpController__verifyMobileNumber` |
| POST | `/verifyMobileNumber/verifyOtp` | `post_SignUpController__verifyOtp` |

## TempBlockApiController (`api`)
**File:** `ams/cms/api/controller/TempBlockApiController.java`  
**Base path:** `/tempBlock`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/tempBlock/tempBlock` | `post_TempBlockApiController__tempBlock` |
| POST | `/tempBlock/tempBlockCard` | `post_TempBlockApiController__tempBlockCard` |
| POST | `/tempBlock/unblocktempBlockCard` | `post_TempBlockApiController__unblocktempBlockCard` |

## TestController (`api`)
**File:** `ams/cms/api/controller/TestController.java`  
**Base path:** `/test`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/test/getApiHash` | `post_TestController__getApiHash` |
| POST | `/test/test` | `post_TestController__test` |
| POST | `/test/test/addImage` | `post_TestController__test_addImage` |
| GET | `/test/test/testStr` | `get_TestController__test_testStr` |

## ThirdPartyTransactionController (`api`)
**File:** `ams/cms/api/controller/ThirdPartyTransactionController.java`  
**Base path:** `/thirdParty`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/thirdParty/checkBankIFSC` | `post_ThirdPartyTransactionController__checkBankIFSC` |
| POST | `/thirdParty/checkBenificiary` | `post_ThirdPartyTransactionController__checkBenificiary` |
| POST | `/thirdParty/getBankNameList` | `post_ThirdPartyTransactionController__getBankNameList` |
| POST | `/thirdParty/getThirdPartyAccountName` | `post_ThirdPartyTransactionController__getThirdPartyAccountName` |
| POST | `/thirdParty/getThirdPartyBankList` | `post_ThirdPartyTransactionController__getThirdPartyBankList` |
| POST | `/thirdParty/processTxn` | `post_ThirdPartyTransactionController__processTxn` |
| POST | `/thirdParty/thirdParty` | `post_ThirdPartyTransactionController__thirdParty` |

## TierController (`api`)
**File:** `ams/cms/api/controller/TierController.java`  
**Base path:** `/tier`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/tier/tier` | `post_TierController__tier` |
| POST | `/tier/tier/setBvn` | `post_TierController__tier_setBvn` |
| POST | `/tier/tier/upgradeTier` | `post_TierController__tier_upgradeTier` |

## TransactionController (`api`)
**File:** `ams/cms/api/controller/TransactionController.java`  
**Base path:** `/transactionControl`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/transactionControl/doTransaction` | `post_TransactionController__doTransaction` |
| POST | `/transactionControl/doTransactionForSimulator` | `post_TransactionController__doTransactionForSimulator` |
| POST | `/transactionControl/transactionControl` | `post_TransactionController__transactionControl` |
| POST | `/transactionControl/w2wTransfer` | `post_TransactionController__w2wTransfer` |

## TransactionControllerApi (`api`)
**File:** `ams/cms/api/controller/TransactionControllerApi.java`  
**Base path:** `/txn`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/txn/txn` | `post_TransactionControllerApi__txn` |
| POST | `/txn/txn/checkRecipientAccountNo` | `post_TransactionControllerApi__txn_checkRecipientAccountNo` |
| POST | `/txn/txn/checkSenderAccountBalance` | `post_TransactionControllerApi__txn_checkSenderAccountBalance` |
| POST | `/txn/txn/performTxn` | `post_TransactionControllerApi__txn_performTxn` |
| POST | `/txn/txn/refundTxn` | `post_TransactionControllerApi__txn_refundTxn` |
| POST | `/txn/txn/reverseTxn` | `post_TransactionControllerApi__txn_reverseTxn` |

## UserTransactionController (`api`)
**File:** `ams/cms/api/controller/UserTransactionController.java`  
**Base path:** `/userTxn`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/userTxn/NGN/userTxn/PerformAgentTxn` | `post_UserTransactionController__NGN_userTxn_PerformAgentTxn` |
| POST | `/userTxn/userTxn` | `post_UserTransactionController__userTxn` |
| POST | `/userTxn/userTxn/PerformTransaction` | `post_UserTransactionController__userTxn_PerformTransaction` |
| POST | `/userTxn/userTxn/checkUserAccount` | `post_UserTransactionController__userTxn_checkUserAccount` |
| POST | `/userTxn/userTxn/resendUserOTP` | `post_UserTransactionController__userTxn_resendUserOTP` |
| POST | `/userTxn/userTxn/sendUserOTP` | `post_UserTransactionController__userTxn_sendUserOTP` |
| POST | `/userTxn/userTxn/validateDenomination` | `post_UserTransactionController__userTxn_validateDenomination` |

## AccountCreditCardTxnController (`admin`)
**File:** `ams/cms/controller/AccountCreditCardTxnController.java`  
**Base path:** `/account-credit-card-txn`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/account-credit-card-txn/account-credit-card-txn` | `post_AccountCreditCardTxnController__account_credit_card_txn` |
| POST | `/account-credit-card-txn/getCreditCardTxnWise` | `post_AccountCreditCardTxnController__getCreditCardTxnWise` |

## AccountCreditLimitCategoryController (`admin`)
**File:** `ams/cms/controller/AccountCreditLimitCategoryController.java`  
**Base path:** `/credit_limit`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/credit_limit/add` | `post_AccountCreditLimitCategoryController__add` |
| POST | `/credit_limit/credit_limit` | `post_AccountCreditLimitCategoryController__credit_limit` |
| POST | `/credit_limit/getAccountCreditLimitCategoryObj` | `post_AccountCreditLimitCategoryController__getAccountCreditLimitCategoryObj` |
| POST | `/credit_limit/getCreditLimitListByParticipantWise` | `post_AccountCreditLimitCategoryController__getCreditLimitListByParticipantWise` |
| POST | `/credit_limit/isCreditTypeExist` | `post_AccountCreditLimitCategoryController__isCreditTypeExist` |
| POST | `/credit_limit/updateCreditTypeCategoryLimit` | `post_AccountCreditLimitCategoryController__updateCreditTypeCategoryLimit` |

## AccountInterestMasterController (`admin`)
**File:** `ams/cms/controller/AccountInterestMasterController.java`  
**Base path:** `/account-wise-credit-interest`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/account-wise-credit-interest/account-wise-credit-interest` | `post_AccountInterestMasterController__account_wise_credit_interest` |
| POST | `/account-wise-credit-interest/getOutStandingInterest` | `post_AccountInterestMasterController__getOutStandingInterest` |

## AccountKycDetailsController (`admin`)
**File:** `ams/cms/controller/AccountKycDetailsController.java`  
**Base path:** `/account-wise-kyc-details`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/account-wise-kyc-details/account-wise-kyc-details` | `post_AccountKycDetailsController__account_wise_kyc_details` |
| POST | `/account-wise-kyc-details/getSingleAccountKycDetail` | `post_AccountKycDetailsController__getSingleAccountKycDetail` |

## AccountLoadMasterController (`admin`)
**File:** `ams/cms/controller/AccountLoadMasterController.java`  
**Base path:** `/account-load`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/account-load/account-load` | `post_AccountLoadMasterController__account_load` |
| POST | `/account-load/add` | `post_AccountLoadMasterController__add` |
| POST | `/account-load/loadBalance` | `post_AccountLoadMasterController__loadBalance` |

## AccountMasterController (`admin`)
**File:** `ams/cms/controller/AccountMasterController.java`  
**Base path:** `/account`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/account/account` | `post_AccountMasterController__account` |
| POST | `/account/getAccountBalanceAndName` | `post_AccountMasterController__getAccountBalanceAndName` |
| POST | `/account/getAccountInfoForClosingAccount` | `post_AccountMasterController__getAccountInfoForClosingAccount` |
| POST | `/account/getAccountInfoListBasedOnTypes` | `post_AccountMasterController__getAccountInfoListBasedOnTypes` |
| POST | `/account/getAccountInfoListByAccountNo` | `post_AccountMasterController__getAccountInfoListByAccountNo` |
| POST | `/account/getAccountInfoListByParticipantId` | `post_AccountMasterController__getAccountInfoListByParticipantId` |
| POST | `/account/getAccountInformation` | `post_AccountMasterController__getAccountInformation` |
| POST | `/account/getAccoutBalanceList` | `post_AccountMasterController__getAccoutBalanceList` |
| POST | `/account/getAvailableBalBasedOnAccountTypeAndNumber` | `post_AccountMasterController__getAvailableBalBasedOnAccountTypeAndNumber` |
| POST | `/account/getCreditCardOutstanding` | `post_AccountMasterController__getCreditCardOutstanding` |
| POST | `/account/getFullAccountInfoBasedOnAccountNumber` | `post_AccountMasterController__getFullAccountInfoBasedOnAccountNumber` |
| POST | `/account/getIssuedAccount` | `post_AccountMasterController__getIssuedAccount` |
| POST | `/account/getRegCustWithLinkAccount` | `post_AccountMasterController__getRegCustWithLinkAccount` |
| POST | `/account/getTxn` | `post_AccountMasterController__getTxn` |
| POST | `/account/isAccountAlreadyExist` | `post_AccountMasterController__isAccountAlreadyExist` |
| POST | `/account/isCustomerAccountAlreadyConfigured` | `post_AccountMasterController__isCustomerAccountAlreadyConfigured` |
| POST | `/account/saveAccountInfo` | `post_AccountMasterController__saveAccountInfo` |
| POST | `/account/updateAccountCardLinked` | `post_AccountMasterController__updateAccountCardLinked` |
| POST | `/account/updateAccountInfo` | `post_AccountMasterController__updateAccountInfo` |
| POST | `/account/updateBalanceRelatedInfo` | `post_AccountMasterController__updateBalanceRelatedInfo` |
| POST | `/account/updateBalnceLimitValues` | `post_AccountMasterController__updateBalnceLimitValues` |
| POST | `/account/updateInstantAccountInfo` | `post_AccountMasterController__updateInstantAccountInfo` |

## AccountStatementController (`admin`)
**File:** `ams/cms/controller/AccountStatementController.java`  
**Base path:** `/account-statement`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/account-statement/account-statement` | `post_AccountStatementController__account_statement` |
| POST | `/account-statement/add` | `post_AccountStatementController__add` |
| POST | `/account-statement/getAccountStatementList` | `post_AccountStatementController__getAccountStatementList` |
| POST | `/account-statement/getAccountStatements` | `post_AccountStatementController__getAccountStatements` |
| POST | `/account-statement/getAccountStatementsDateWise` | `post_AccountStatementController__getAccountStatementsDateWise` |
| POST | `/account-statement/getWalletAccountStatementsDateWise` | `post_AccountStatementController__getWalletAccountStatementsDateWise` |
| POST | `/account-statement/getlastFiveWalletAccountStatements` | `post_AccountStatementController__getlastFiveWalletAccountStatements` |

## AccountTranMasterController (`admin`)
**File:** `ams/cms/controller/AccountTranMasterController.java`  
**Base path:** `/accountTxnMaster`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/accountTxnMaster/accountTxnMaster` | `post_AccountTranMasterController__accountTxnMaster` |
| POST | `/accountTxnMaster/add` | `post_AccountTranMasterController__add` |
| POST | `/accountTxnMaster/searchTransactionByTxnId` | `post_AccountTranMasterController__searchTransactionByTxnId` |

## AccountTransactionLimitationController (`admin`)
**File:** `ams/cms/controller/AccountTransactionLimitationController.java`  
**Base path:** `/account-txn-limit`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/account-txn-limit/account-txn-limit` | `post_AccountTransactionLimitationController__account_txn_limit` |
| POST | `/account-txn-limit/getAccountTxnLimit` | `post_AccountTransactionLimitationController__getAccountTxnLimit` |

## AccountTransactionReportController (`admin`)
**File:** `ams/cms/controller/AccountTransactionReportController.java`  
**Base path:** `/txnReport`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/txnReport/getAccountTxnType` | `post_AccountTransactionReportController__getAccountTxnType` |
| POST | `/txnReport/getJouranalTxnReport` | `post_AccountTransactionReportController__getJouranalTxnReport` |
| POST | `/txnReport/getTxnReportStatement` | `post_AccountTransactionReportController__getTxnReportStatement` |
| POST | `/txnReport/txnReport` | `post_AccountTransactionReportController__txnReport` |

## AccountTypeChargesController (`admin`)
**File:** `ams/cms/controller/AccountTypeChargesController.java`  
**Base path:** `/accountTypeCharges`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/accountTypeCharges/accountTypeCharges` | `post_AccountTypeChargesController__accountTypeCharges` |
| POST | `/accountTypeCharges/add` | `post_AccountTypeChargesController__add` |
| POST | `/accountTypeCharges/getSelectedChargesAccountTypeWise` | `post_AccountTypeChargesController__getSelectedChargesAccountTypeWise` |

## AccountTypeMasterController (`admin`)
**File:** `ams/cms/controller/AccountTypeMasterController.java`  
**Base path:** `/accountType`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/accountType/accountType` | `post_AccountTypeMasterController__accountType` |
| POST | `/accountType/getACTypeListByParticipantWise` | `post_AccountTypeMasterController__getACTypeListByParticipantWise` |
| POST | `/accountType/getAccDescription` | `post_AccountTypeMasterController__getAccDescription` |
| GET | `/accountType/getAccountType` | `get_AccountTypeMasterController__getAccountType` |
| GET | `/accountType/getAccountTypeCode` | `get_AccountTypeMasterController__getAccountTypeCode` |
| POST | `/accountType/getAccountTypeMasterDetailsBasedOnAccountType` | `post_AccountTypeMasterController__getAccountTypeMasterDetailsBasedOnAccountType` |
| POST | `/accountType/getCreditAccountType` | `post_AccountTypeMasterController__getCreditAccountType` |
| POST | `/accountType/getLastAccounNumber` | `post_AccountTypeMasterController__getLastAccounNumber` |
| POST | `/accountType/getNonCreditAccounType` | `post_AccountTypeMasterController__getNonCreditAccounType` |
| POST | `/accountType/getSingleAccountType` | `post_AccountTypeMasterController__getSingleAccountType` |
| POST | `/accountType/getYCreditAccounType` | `post_AccountTypeMasterController__getYCreditAccounType` |
| POST | `/accountType/isAccountTypeExist` | `post_AccountTypeMasterController__isAccountTypeExist` |
| POST | `/accountType/isGLAccountTypeExist` | `post_AccountTypeMasterController__isGLAccountTypeExist` |
| POST | `/accountType/saveAccountTypeInfo` | `post_AccountTypeMasterController__saveAccountTypeInfo` |
| POST | `/accountType/updateAccountTypeDetails` | `post_AccountTypeMasterController__updateAccountTypeDetails` |
| POST | `/accountType/updateIsRevolvingCredit` | `post_AccountTypeMasterController__updateIsRevolvingCredit` |
| POST | `/accountType/updateIsRevolvingCreditFromMcc` | `post_AccountTypeMasterController__updateIsRevolvingCreditFromMcc` |

## AccountTypeWiseBlockedMccController (`admin`)
**File:** `ams/cms/controller/AccountTypeWiseBlockedMccController.java`  
**Base path:** `/blocked_mcc_account_type_wise`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/blocked_mcc_account_type_wise/add` | `post_AccountTypeWiseBlockedMccController__add` |
| POST | `/blocked_mcc_account_type_wise/blocked_mcc_account_type_wise` | `post_AccountTypeWiseBlockedMccController__blocked_mcc_account_type_wise` |
| POST | `/blocked_mcc_account_type_wise/getBlockMccAccountTypeWise` | `post_AccountTypeWiseBlockedMccController__getBlockMccAccountTypeWise` |
| POST | `/blocked_mcc_account_type_wise/getBlockMccListAccountTypeWise` | `post_AccountTypeWiseBlockedMccController__getBlockMccListAccountTypeWise` |
| POST | `/blocked_mcc_account_type_wise/getUnblockMCC` | `post_AccountTypeWiseBlockedMccController__getUnblockMCC` |

## AccountTypeWiseWalletController (`admin`)
**File:** `ams/cms/controller/AccountTypeWiseWalletController.java`  
**Base path:** `/account_type_wallet`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/account_type_wallet/accountTypeBasedWallet` | `post_AccountTypeWiseWalletController__accountTypeBasedWallet` |
| POST | `/account_type_wallet/account_type_wallet` | `post_AccountTypeWiseWalletController__account_type_wallet` |
| POST | `/account_type_wallet/add` | `post_AccountTypeWiseWalletController__add` |

## AccountWiseChargesController (`admin`)
**File:** `ams/cms/controller/AccountWiseChargesController.java`  
**Base path:** `/accountWiseCharge`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/accountWiseCharge/accountWiseCharge` | `post_AccountWiseChargesController__accountWiseCharge` |
| POST | `/accountWiseCharge/processToCharge` | `post_AccountWiseChargesController__processToCharge` |

## AcTypeLrsTcsMasterController (`admin`)
**File:** `ams/cms/controller/AcTypeLrsTcsMasterController.java`  
**Base path:** `/accountlrstcsmaster`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/accountlrstcsmaster/accountlrstcsmaster` | `post_AcTypeLrsTcsMasterController__accountlrstcsmaster` |
| POST | `/accountlrstcsmaster/checklrslimit` | `post_AcTypeLrsTcsMasterController__checklrslimit` |

## AddressProofDocumentTypeMasterController (`admin`)
**File:** `ams/cms/controller/AddressProofDocumentTypeMasterController.java`  
**Base path:** `/address_proof_document_type`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/address_proof_document_type/address_proof_document_type` | `post_AddressProofDocumentTypeMasterController__address_proof_document_type` |
| POST | `/address_proof_document_type/documentList` | `post_AddressProofDocumentTypeMasterController__documentList` |

## ApplicationController (`admin`)
**File:** `ams/cms/controller/ApplicationController.java`  
**Base path:** `/applicationName`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/applicationName/applicationName` | `post_ApplicationController__applicationName` |
| POST | `/applicationName/setApplicationName` | `post_ApplicationController__setApplicationName` |

## ApproveUpgradeTierController (`admin`)
**File:** `ams/cms/controller/ApproveUpgradeTierController.java`  
**Base path:** `/approve_upgrade_tier`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/approve_upgrade_tier/approve` | `post_ApproveUpgradeTierController__approve` |
| POST | `/approve_upgrade_tier/approve_upgrade_tier` | `post_ApproveUpgradeTierController__approve_upgrade_tier` |
| POST | `/approve_upgrade_tier/getUpgradeTierType` | `post_ApproveUpgradeTierController__getUpgradeTierType` |
| POST | `/approve_upgrade_tier/reject` | `post_ApproveUpgradeTierController__reject` |

## BulkTransferController (`admin`)
**File:** `ams/cms/controller/BulkTransferController.java`  
**Base path:** `/bulkTransfer`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/bulkTransfer/approve` | `post_BulkTransferController__approve` |
| POST | `/bulkTransfer/authorizeBulklist` | `post_BulkTransferController__authorizeBulklist` |
| POST | `/bulkTransfer/authorizePreTxnBulklist` | `post_BulkTransferController__authorizePreTxnBulklist` |
| POST | `/bulkTransfer/bulkTransfer` | `post_BulkTransferController__bulkTransfer` |
| POST | `/bulkTransfer/findAll` | `post_BulkTransferController__findAll` |
| POST | `/bulkTransfer/findAllByPreTransactionId` | `post_BulkTransferController__findAllByPreTransactionId` |
| POST | `/bulkTransfer/getAccountLimitInfoToCompareTransferAmount` | `post_BulkTransferController__getAccountLimitInfoToCompareTransferAmount` |
| POST | `/bulkTransfer/getBulkTransferTransactions` | `post_BulkTransferController__getBulkTransferTransactions` |
| POST | `/bulkTransfer/getCumlativeBalanceCompareTransAmt` | `post_BulkTransferController__getCumlativeBalanceCompareTransAmt` |
| POST | `/bulkTransfer/manyToOne` | `post_BulkTransferController__manyToOne` |
| POST | `/bulkTransfer/oneToMany` | `post_BulkTransferController__oneToMany` |
| POST | `/bulkTransfer/preTxnBulklistForVerify` | `post_BulkTransferController__preTxnBulklistForVerify` |
| POST | `/bulkTransfer/reject` | `post_BulkTransferController__reject` |
| POST | `/bulkTransfer/saveBulkTransferExcelData` | `post_BulkTransferController__saveBulkTransferExcelData` |
| POST | `/bulkTransfer/uploadExcel` | `post_BulkTransferController__uploadExcel` |
| POST | `/bulkTransfer/verify` | `post_BulkTransferController__verify` |

## CardAccountLinkageController (`admin`)
**File:** `ams/cms/controller/CardAccountLinkageController.java`  
**Base path:** `/card-account-linkage`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/card-account-linkage/add` | `post_CardAccountLinkageController__add` |
| POST | `/card-account-linkage/card-account-linkage` | `post_CardAccountLinkageController__card_account_linkage` |
| POST | `/card-account-linkage/checkDataExist` | `post_CardAccountLinkageController__checkDataExist` |
| POST | `/card-account-linkage/getCardLinkAccountList` | `post_CardAccountLinkageController__getCardLinkAccountList` |
| POST | `/card-account-linkage/getLinkageCardDetailsBasedOnCustId` | `post_CardAccountLinkageController__getLinkageCardDetailsBasedOnCustId` |
| POST | `/card-account-linkage/getLinkagedata` | `post_CardAccountLinkageController__getLinkagedata` |
| POST | `/card-account-linkage/getLinkagedata-based-on-account` | `post_CardAccountLinkageController__getLinkagedata_based_on_account` |
| POST | `/card-account-linkage/getLinkagedata-based-on-card` | `post_CardAccountLinkageController__getLinkagedata_based_on_card` |
| POST | `/card-account-linkage/updateExpiryDate` | `post_CardAccountLinkageController__updateExpiryDate` |

## CategoryListModelController (`admin`)
**File:** `ams/cms/controller/CategoryListModelController.java`  
**Base path:** `/account_type_category`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/account_type_category/account_type_category` | `post_CategoryListModelController__account_type_category` |
| POST | `/account_type_category/getAccountTypeCategory` | `post_CategoryListModelController__getAccountTypeCategory` |

## CategoryTypeController (`admin`)
**File:** `ams/cms/controller/CategoryTypeController.java`  
**Base path:** `/category_type`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/category_type/categoryTypeData` | `post_CategoryTypeController__categoryTypeData` |
| POST | `/category_type/category_type` | `post_CategoryTypeController__category_type` |
| POST | `/category_type/categorytypeExit` | `post_CategoryTypeController__categorytypeExit` |
| POST | `/category_type/save` | `post_CategoryTypeController__save` |

## ChannelsController (`admin`)
**File:** `ams/cms/controller/ChannelsController.java`  
**Base path:** `/channels`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/channels/channels` | `post_ChannelsController__channels` |
| POST | `/channels/getChannelList` | `post_ChannelsController__getChannelList` |

## ChargeMasterAccountController (`admin`)
**File:** `ams/cms/controller/ChargeMasterAccountController.java`  
**Base path:** `/accountType`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/accountType/accountType` | `post_ChargeMasterAccountController__accountType` |
| POST | `/accountType/getSelectedChargesAccountTypeWise` | `post_ChargeMasterAccountController__getSelectedChargesAccountTypeWise` |

## ChargeMasterController (`admin`)
**File:** `ams/cms/controller/ChargeMasterController.java`  
**Base path:** `/getChargeMasterList`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/getChargeMasterList/add` | `post_ChargeMasterController__add` |
| POST | `/getChargeMasterList/addChargeType` | `post_ChargeMasterController__addChargeType` |
| POST | `/getChargeMasterList/getChargeMasterList` | `post_ChargeMasterController__getChargeMasterList` |
| POST | `/getChargeMasterList/getChargesMasterList` | `post_ChargeMasterController__getChargesMasterList` |
| POST | `/getChargeMasterList/getFuelChargeList` | `post_ChargeMasterController__getFuelChargeList` |
| POST | `/getChargeMasterList/getSelectedChargesAccountTypeWise` | `post_ChargeMasterController__getSelectedChargesAccountTypeWise` |
| POST | `/getChargeMasterList/getTransactionChargeList` | `post_ChargeMasterController__getTransactionChargeList` |
| POST | `/getChargeMasterList/validateChargeType` | `post_ChargeMasterController__validateChargeType` |

## ChargeRelatedMasterController (`admin`)
**File:** `ams/cms/controller/ChargeRelatedMasterController.java`  
**Base path:** `/chargeRelatedMaster`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/chargeRelatedMaster/addChargeRelated` | `post_ChargeRelatedMasterController__addChargeRelated` |
| POST | `/chargeRelatedMaster/chargeRelatedMaster` | `post_ChargeRelatedMasterController__chargeRelatedMaster` |
| POST | `/chargeRelatedMaster/getChargeRelatedDescription` | `post_ChargeRelatedMasterController__getChargeRelatedDescription` |
| POST | `/chargeRelatedMaster/getchargeRelatedList` | `post_ChargeRelatedMasterController__getchargeRelatedList` |
| POST | `/chargeRelatedMaster/validateChargeRelated` | `post_ChargeRelatedMasterController__validateChargeRelated` |

## CloseAccountMasterController (`admin`)
**File:** `ams/cms/controller/CloseAccountMasterController.java`  
**Base path:** `/accountClosure`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/accountClosure/accountClosure` | `post_CloseAccountMasterController__accountClosure` |
| GET | `/accountClosure/process` | `get_CloseAccountMasterController__process` |
| POST | `/accountClosure/request` | `post_CloseAccountMasterController__request` |

## CountryCodeMasterController (`admin`)
**File:** `ams/cms/controller/CountryCodeMasterController.java`  
**Base path:** `/country_code_api`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/country_code_api/country_code_api` | `post_CountryCodeMasterController__country_code_api` |
| POST | `/country_code_api/getCountryCode` | `post_CountryCodeMasterController__getCountryCode` |

## CurrencyMasterController (`admin`)
**File:** `ams/cms/controller/CurrencyMasterController.java`  
**Base path:** `/currencymaster`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/currencymaster/currencymaster` | `post_CurrencyMasterController__currencymaster` |
| POST | `/currencymaster/getcurrencylist` | `post_CurrencyMasterController__getcurrencylist` |

## CustomerIdTableController (`admin`)
**File:** `ams/cms/controller/CustomerIdTableController.java`  
**Base path:** `/customerIdTable`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/customerIdTable/customerIdTable` | `post_CustomerIdTableController__customerIdTable` |
| POST | `/customerIdTable/updateCustId` | `post_CustomerIdTableController__updateCustId` |

## CustomerMasterController (`admin`)
**File:** `ams/cms/controller/CustomerMasterController.java`  
**Base path:** `/customerId`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/customerId/customerId` | `post_CustomerMasterController__customerId` |
| POST | `/customerId/getActiveTier` | `post_CustomerMasterController__getActiveTier` |
| POST | `/customerId/getCustByMobileNumber` | `post_CustomerMasterController__getCustByMobileNumber` |
| POST | `/customerId/getCustomerAccountDetailsBasedOnCustId` | `post_CustomerMasterController__getCustomerAccountDetailsBasedOnCustId` |
| POST | `/customerId/getCustomerAccountInfo` | `post_CustomerMasterController__getCustomerAccountInfo` |
| GET | `/customerId/getCustomerId` | `get_CustomerMasterController__getCustomerId` |
| POST | `/customerId/getCustomerInfo` | `post_CustomerMasterController__getCustomerInfo` |
| POST | `/customerId/getcustomerdetailsbyId` | `post_CustomerMasterController__getcustomerdetailsbyId` |
| POST | `/customerId/insertCustId` | `post_CustomerMasterController__insertCustId` |
| POST | `/customerId/saveCustomerInfo` | `post_CustomerMasterController__saveCustomerInfo` |
| POST | `/customerId/updateCustomerAccountDetails` | `post_CustomerMasterController__updateCustomerAccountDetails` |

## DenominationMasterController (`admin`)
**File:** `ams/cms/controller/DenominationMasterController.java`  
**Base path:** `/denomination_master`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/denomination_master/denomination_master` | `post_DenominationMasterController__denomination_master` |
| POST | `/denomination_master/getTxnDenominationDetails` | `post_DenominationMasterController__getTxnDenominationDetails` |
| POST | `/denomination_master/getTxnDenominationDetailsbyAgent` | `post_DenominationMasterController__getTxnDenominationDetailsbyAgent` |

## DormancyMasterController (`admin`)
**File:** `ams/cms/controller/DormancyMasterController.java`  
**Base path:** `/dormancy`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/dormancy/checkerProcess` | `post_DormancyMasterController__checkerProcess` |
| POST | `/dormancy/checkervalidation` | `post_DormancyMasterController__checkervalidation` |
| POST | `/dormancy/dormancy` | `post_DormancyMasterController__dormancy` |
| POST | `/dormancy/makerprocess` | `post_DormancyMasterController__makerprocess` |
| POST | `/dormancy/makervalidation` | `post_DormancyMasterController__makervalidation` |
| POST | `/dormancy/requestprocessmobile` | `post_DormancyMasterController__requestprocessmobile` |
| POST | `/dormancy/status` | `post_DormancyMasterController__status` |

## DormantAccountController (`admin`)
**File:** `ams/cms/controller/DormantAccountController.java`  
**Base path:** `/dormant`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/dormant/dormant` | `post_DormantAccountController__dormant` |
| GET | `/dormant/getPendingCheckerUserList` | `get_DormantAccountController__getPendingCheckerUserList` |

## FeeTypeMasterController (`admin`)
**File:** `ams/cms/controller/FeeTypeMasterController.java`  
**Base path:** `/feeTypeMaster`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/feeTypeMaster/feeTypeMaster` | `post_FeeTypeMasterController__feeTypeMaster` |
| POST | `/feeTypeMaster/getFeesCollected` | `post_FeeTypeMasterController__getFeesCollected` |

## GLAccountCreationController (`admin`)
**File:** `ams/cms/controller/GLAccountCreationController.java`  
**Base path:** `/gl-account-type-creation`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/gl-account-type-creation/add` | `post_GLAccountCreationController__add` |
| POST | `/gl-account-type-creation/allGLAccountBalance` | `post_GLAccountCreationController__allGLAccountBalance` |
| POST | `/gl-account-type-creation/getGlAccTypeAccNumber` | `post_GLAccountCreationController__getGlAccTypeAccNumber` |
| POST | `/gl-account-type-creation/getGlAccTypeAccNumberList` | `post_GLAccountCreationController__getGlAccTypeAccNumberList` |
| POST | `/gl-account-type-creation/gl-account-type-creation` | `post_GLAccountCreationController__gl_account_type_creation` |
| POST | `/gl-account-type-creation/glCtrAccountTypeObj` | `post_GLAccountCreationController__glCtrAccountTypeObj` |
| POST | `/gl-account-type-creation/isGLAccountNumberExist` | `post_GLAccountCreationController__isGLAccountNumberExist` |
| POST | `/gl-account-type-creation/isGLAccountTypeExist` | `post_GLAccountCreationController__isGLAccountTypeExist` |
| POST | `/gl-account-type-creation/isGLAccountTypeExistdetails` | `post_GLAccountCreationController__isGLAccountTypeExistdetails` |
| POST | `/gl-account-type-creation/updateGLClosingBalance` | `post_GLAccountCreationController__updateGLClosingBalance` |

## GLAccountLoadingController (`admin`)
**File:** `ams/cms/controller/GLAccountLoadingController.java`  
**Base path:** `/gl-account-load`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/gl-account-load/add` | `post_GLAccountLoadingController__add` |
| POST | `/gl-account-load/gl-account-load` | `post_GLAccountLoadingController__gl_account_load` |

## GlAccountStatementController (`admin`)
**File:** `ams/cms/controller/GlAccountStatementController.java`  
**Base path:** `/gl-account-statement`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/gl-account-statement/add` | `post_GlAccountStatementController__add` |
| POST | `/gl-account-statement/getAccountStatementandGlAccountStatementHeader` | `post_GlAccountStatementController__getAccountStatementandGlAccountStatementHeade` |
| POST | `/gl-account-statement/getGLAccountStatementList` | `post_GlAccountStatementController__getGLAccountStatementList` |
| POST | `/gl-account-statement/getGlAccountStatementHeader` | `post_GlAccountStatementController__getGlAccountStatementHeader` |
| POST | `/gl-account-statement/gl-account-statement` | `post_GlAccountStatementController__gl_account_statement` |

## GLAccountTypeMasterController (`admin`)
**File:** `ams/cms/controller/GLAccountTypeMasterController.java`  
**Base path:** `/gl-account-type`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/gl-account-type/addGLAccountEntry` | `post_GLAccountTypeMasterController__addGLAccountEntry` |
| POST | `/gl-account-type/get-glaccount-closing-bal` | `post_GLAccountTypeMasterController__get_glaccount_closing_bal` |
| POST | `/gl-account-type/getGLAccountData` | `post_GLAccountTypeMasterController__getGLAccountData` |
| POST | `/gl-account-type/getGLAccountList` | `post_GLAccountTypeMasterController__getGLAccountList` |
| POST | `/gl-account-type/getGLAccountTypeList` | `post_GLAccountTypeMasterController__getGLAccountTypeList` |
| POST | `/gl-account-type/getGLDescription` | `post_GLAccountTypeMasterController__getGLDescription` |
| POST | `/gl-account-type/getGlAccountTypedata` | `post_GLAccountTypeMasterController__getGlAccountTypedata` |
| GET | `/gl-account-type/getThirdPartyAllowGLAccountTypeList` | `get_GLAccountTypeMasterController__getThirdPartyAllowGLAccountTypeList` |
| POST | `/gl-account-type/gl-account-type` | `post_GLAccountTypeMasterController__gl_account_type` |
| POST | `/gl-account-type/update-glaccount-type-details` | `post_GLAccountTypeMasterController__update_glaccount_type_details` |

## IdentityProofDocumentTypeMasterController (`admin`)
**File:** `ams/cms/controller/IdentityProofDocumentTypeMasterController.java`  
**Base path:** `/identity_proof_document_type`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/identity_proof_document_type/documentList` | `post_IdentityProofDocumentTypeMasterController__documentList` |
| POST | `/identity_proof_document_type/identity_proof_document_type` | `post_IdentityProofDocumentTypeMasterController__identity_proof_document_type` |

## InstantAccountMasterController (`admin`)
**File:** `ams/cms/controller/InstantAccountMasterController.java`  
**Base path:** `/instanceAccount`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/instanceAccount/create` | `post_InstantAccountMasterController__create` |
| POST | `/instanceAccount/instanceAccount` | `post_InstantAccountMasterController__instanceAccount` |

## JournalTransferController (`admin`)
**File:** `ams/cms/controller/JournalTransferController.java`  
**Base path:** `/journalTransfer`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/journalTransfer/add` | `post_JournalTransferController__add` |
| POST | `/journalTransfer/approve` | `post_JournalTransferController__approve` |
| POST | `/journalTransfer/getAccountauthoriselist` | `post_JournalTransferController__getAccountauthoriselist` |
| POST | `/journalTransfer/getTxnIdAuthoriselist` | `post_JournalTransferController__getTxnIdAuthoriselist` |
| POST | `/journalTransfer/journalTransfer` | `post_JournalTransferController__journalTransfer` |
| POST | `/journalTransfer/reject` | `post_JournalTransferController__reject` |
| POST | `/journalTransfer/updateReasonInfo` | `post_JournalTransferController__updateReasonInfo` |

## LoadMoneyController (`admin`)
**File:** `ams/cms/controller/LoadMoneyController.java`  
**Base path:** `/loadmoney`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/loadmoney/getloadmoneypaystructure` | `post_LoadMoneyController__getloadmoneypaystructure` |
| POST | `/loadmoney/loadmoney` | `post_LoadMoneyController_loadmoney` |
| POST | `/loadmoney/transferCurrency` | `post_LoadMoneyController__transferCurrency` |
| POST | `/loadmoney/wallettransaction` | `post_LoadMoneyController__wallettransaction` |
| POST | `/loadmoney/wallettransactionprocess` | `post_LoadMoneyController__wallettransactionprocess` |

## MccWiseInterestController (`admin`)
**File:** `ams/cms/controller/MccWiseInterestController.java`  
**Base path:** `/mcc-wise-interest`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/mcc-wise-interest/add` | `post_MccWiseInterestController__add` |
| POST | `/mcc-wise-interest/getMccWiseInterest` | `post_MccWiseInterestController__getMccWiseInterest` |
| POST | `/mcc-wise-interest/getMccWiseInterestView` | `post_MccWiseInterestController__getMccWiseInterestView` |
| POST | `/mcc-wise-interest/mcc-wise-interest` | `post_MccWiseInterestController__mcc_wise_interest` |
| POST | `/mcc-wise-interest/validateMccWiseInterst` | `post_MccWiseInterestController__validateMccWiseInterst` |

## MerchantCategoryCodeMasterController (`admin`)
**File:** `ams/cms/controller/MerchantCategoryCodeMasterController.java`  
**Base path:** `/mcc_code`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/mcc_code/getAllMccCode` | `post_MerchantCategoryCodeMasterController__getAllMccCode` |
| POST | `/mcc_code/getSelectedMCCList` | `post_MerchantCategoryCodeMasterController__getSelectedMCCList` |
| POST | `/mcc_code/getUnSelectedAllMCC` | `post_MerchantCategoryCodeMasterController__getUnSelectedAllMCC` |
| POST | `/mcc_code/mcc_code` | `post_MerchantCategoryCodeMasterController__mcc_code` |

## MultiCurrencyWalletAccountController (`admin`)
**File:** `ams/cms/controller/MultiCurrencyWalletAccountController.java`  
**Base path:** `/currencywalletaccount`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/currencywalletaccount/currencyconversionrate` | `post_MultiCurrencyWalletAccountController__currencyconversionrate` |
| POST | `/currencywalletaccount/currencywalletaccount` | `post_MultiCurrencyWalletAccountController__currencywalletaccount` |
| POST | `/currencywalletaccount/getCurrencyCode` | `post_MultiCurrencyWalletAccountController__getCurrencyCode` |
| POST | `/currencywalletaccount/getCurrenyWalletListForStatemetView` | `post_MultiCurrencyWalletAccountController__getCurrenyWalletListForStatemetView` |
| POST | `/currencywalletaccount/getWalletPriorityList` | `post_MultiCurrencyWalletAccountController__getWalletPriorityList` |
| POST | `/currencywalletaccount/updateWalletPriorityList` | `post_MultiCurrencyWalletAccountController__updateWalletPriorityList` |
| POST | `/currencywalletaccount/viewcurrencywallet` | `post_MultiCurrencyWalletAccountController__viewcurrencywallet` |

## ParticipantMasterController (`admin`)
**File:** `ams/cms/controller/ParticipantMasterController.java`  
**Base path:** `/participant_master`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/participant_master/add` | `post_ParticipantMasterController__add` |
| POST | `/participant_master/generateApiKey` | `post_ParticipantMasterController__generateApiKey` |
| POST | `/participant_master/getAllParticipantList` | `post_ParticipantMasterController__getAllParticipantList` |
| POST | `/participant_master/participant_master` | `post_ParticipantMasterController__participant_master` |

## ParticipantWiseWalletController (`admin`)
**File:** `ams/cms/controller/ParticipantWiseWalletController.java`  
**Base path:** `/participant_wallet`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/participant_wallet/add` | `post_ParticipantWiseWalletController__add` |
| POST | `/participant_wallet/getParticipantBasedMcc` | `post_ParticipantWiseWalletController__getParticipantBasedMcc` |
| POST | `/participant_wallet/participant_wallet` | `post_ParticipantWiseWalletController__participant_wallet` |

## PreAccountMasterController (`admin`)
**File:** `ams/cms/controller/PreAccountMasterController.java`  
**Base path:** `/preAccountMaster`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/preAccountMaster/getKycDataForverification` | `post_PreAccountMasterController__getKycDataForverification` |
| POST | `/preAccountMaster/getPreAccount` | `post_PreAccountMasterController__getPreAccount` |
| POST | `/preAccountMaster/getPreAccountData` | `post_PreAccountMasterController__getPreAccountData` |
| POST | `/preAccountMaster/insertDatalist` | `post_PreAccountMasterController__insertDatalist` |
| POST | `/preAccountMaster/nonLinkedCustomerForAccountNo` | `post_PreAccountMasterController__nonLinkedCustomerForAccountNo` |
| POST | `/preAccountMaster/nonLinkedCustomersForCutomerId` | `post_PreAccountMasterController__nonLinkedCustomersForCutomerId` |
| POST | `/preAccountMaster/preAccountMaster` | `post_PreAccountMasterController__preAccountMaster` |
| POST | `/preAccountMaster/registerCustomersList` | `post_PreAccountMasterController__registerCustomersList` |
| POST | `/preAccountMaster/updateMobileAgaintCustId` | `post_PreAccountMasterController__updateMobileAgaintCustId` |
| POST | `/preAccountMaster/updatePreAccountAccount` | `post_PreAccountMasterController__updatePreAccountAccount` |
| POST | `/preAccountMaster/updatePreAccountAccountBasedOnParam` | `post_PreAccountMasterController__updatePreAccountAccountBasedOnParam` |
| POST | `/preAccountMaster/updatekyc` | `post_PreAccountMasterController__updatekyc` |

## PreSubAccountMasterController (`admin`)
**File:** `ams/cms/controller/PreSubAccountMasterController.java`  
**Base path:** `/preSubAccountMaster`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/preSubAccountMaster/getPendingRegCustWithLinkAccount` | `post_PreSubAccountMasterController__getPendingRegCustWithLinkAccount` |
| POST | `/preSubAccountMaster/preSubAccountMaster` | `post_PreSubAccountMasterController__preSubAccountMaster` |
| POST | `/preSubAccountMaster/updateIsAccountNoCreatedField` | `post_PreSubAccountMasterController__updateIsAccountNoCreatedField` |

## RevolvingCreditCardController (`admin`)
**File:** `ams/cms/controller/RevolvingCreditCardController.java`  
**Base path:** `/revolvingCreditCard`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/revolvingCreditCard/addRevolvingCreditCard` | `post_RevolvingCreditCardController__addRevolvingCreditCard` |
| POST | `/revolvingCreditCard/revolvingCreditCard` | `post_RevolvingCreditCardController__revolvingCreditCard` |
| POST | `/revolvingCreditCard/validateRevolvingCreditCard` | `post_RevolvingCreditCardController__validateRevolvingCreditCard` |

## RevolvingCreditCardMasterController (`admin`)
**File:** `ams/cms/controller/RevolvingCreditCardMasterController.java`  
**Base path:** `/revolvingCreditCardMstr`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/revolvingCreditCardMstr/getGracePeriod` | `post_RevolvingCreditCardMasterController__getGracePeriod` |
| POST | `/revolvingCreditCardMstr/revolvingCreditCardMstr` | `post_RevolvingCreditCardMasterController__revolvingCreditCardMstr` |

## RevolvingCreditCardTxnController (`admin`)
**File:** `ams/cms/controller/RevolvingCreditCardTxnController.java`  
**Base path:** `/revolvingCreditCardTxn`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/revolvingCreditCardTxn/add` | `post_RevolvingCreditCardTxnController__add` |
| POST | `/revolvingCreditCardTxn/revolvingCreditCardTxn` | `post_RevolvingCreditCardTxnController__revolvingCreditCardTxn` |

## RevolvingCreditInterestTxnController (`admin`)
**File:** `ams/cms/controller/RevolvingCreditInterestTxnController.java`  
**Base path:** `/revolvingCreditInterestTxn`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/revolvingCreditInterestTxn/add` | `post_RevolvingCreditInterestTxnController__add` |
| POST | `/revolvingCreditInterestTxn/revolvingCreditInterestTxn` | `post_RevolvingCreditInterestTxnController__revolvingCreditInterestTxn` |

## TaxConfigModelController (`admin`)
**File:** `ams/cms/controller/TaxConfigModelController.java`  
**Base path:** `/tax_type_config`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/tax_type_config/getTaxTypeConfig` | `post_TaxConfigModelController__getTaxTypeConfig` |
| POST | `/tax_type_config/tax_type_config` | `post_TaxConfigModelController__tax_type_config` |

## TransactionHandlerController (`admin`)
**File:** `ams/cms/controller/TransactionHandlerController.java`  
**Base path:** `/transactionHandler`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/transactionHandler/processToLoadBalance` | `post_TransactionHandlerController__processToLoadBalance` |
| POST | `/transactionHandler/processTransactionData` | `post_TransactionHandlerController__processTransactionData` |
| POST | `/transactionHandler/processTxn` | `post_TransactionHandlerController__processTxn` |
| POST | `/transactionHandler/transactionHandler` | `post_TransactionHandlerController__transactionHandler` |

## TransactionTypeController (`admin`)
**File:** `ams/cms/controller/TransactionTypeController.java`  
**Base path:** `/transaction_type_master`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/transaction_type_master/getTransactionTypeModelData` | `post_TransactionTypeController__getTransactionTypeModelData` |
| POST | `/transaction_type_master/isGLAccountTypeExist` | `post_TransactionTypeController__isGLAccountTypeExist` |
| POST | `/transaction_type_master/isTransactionTypeAlreadyExist` | `post_TransactionTypeController__isTransactionTypeAlreadyExist` |
| POST | `/transaction_type_master/saveTxnTypeCreationData` | `post_TransactionTypeController__saveTxnTypeCreationData` |
| POST | `/transaction_type_master/transaction_type_master` | `post_TransactionTypeController__transaction_type_master` |

## UpgradeTierReqResController (`admin`)
**File:** `ams/cms/controller/UpgradeTierReqResController.java`  
**Base path:** `/upgradeTier`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/upgradeTier/upgradeTier` | `post_UpgradeTierReqResController__upgradeTier` |

## VatTypeMasterController (`admin`)
**File:** `ams/cms/controller/VatTypeMasterController.java`  
**Base path:** `/vatTypeMaster`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/vatTypeMaster/getVatCollected` | `post_VatTypeMasterController__getVatCollected` |
| POST | `/vatTypeMaster/vatTypeMaster` | `post_VatTypeMasterController__vatTypeMaster` |

## WalletAccountMasterController (`admin`)
**File:** `ams/cms/controller/WalletAccountMasterController.java`  
**Base path:** `/wallet_account`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/wallet_account/add` | `post_WalletAccountMasterController__add` |
| POST | `/wallet_account/getLinkedAccountWalletList` | `post_WalletAccountMasterController__getLinkedAccountWalletList` |
| POST | `/wallet_account/wallet_account` | `post_WalletAccountMasterController__wallet_account` |

## NotificationController (`admin`)
**File:** `ams/cms/notification/controller/NotificationController.java`  
**Base path:** `/notify-by`  

| Method | Path | Operation ID |
|--------|------|--------------|
| POST | `/notify-by/notify-by` | `post_NotificationController__notify_by` |
| POST | `/notify-by/sendEmail` | `post_NotificationController__sendEmail` |
| POST | `/notify-by/sendTestEmail` | `post_NotificationController__sendTestEmail` |
