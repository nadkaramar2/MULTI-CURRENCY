package ams.cms.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonInclude;

import ams.cms.api.model.AccountStatementHeader;
import ams.cms.api.model.CountryCodeMaster;
import ams.cms.api.model.DenominationMaster;
import ams.cms.api.model.FeeTypeMaster;
import ams.cms.api.model.PreAccountMaster;
import ams.cms.api.model.PreSubAccountMaster;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountCreditCardTransactionModel;
import ams.cms.model.AccountCreditLimitCategory;
import ams.cms.model.AccountInterestMaster;
import ams.cms.model.AccountLoadMaster;
import ams.cms.model.AccountStatement;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.AccountTransactionLimitation;
import ams.cms.model.AccountTypeCharges;
import ams.cms.model.AccountTypeMaster;
import ams.cms.model.AccountTypeWiseBlockedMccMaster;
import ams.cms.model.AccountTypeWiseWalletMaster;
import ams.cms.model.AccountWiseCharges;
import ams.cms.model.AddressProofDocumentTypeMaster;
import ams.cms.model.ApproveUpgradeTier;
import ams.cms.model.CardAccountLinkage;
import ams.cms.model.CategoryListModel;
import ams.cms.model.Categorytype;
import ams.cms.model.Channels;
import ams.cms.model.ChargeMaster;
import ams.cms.model.ChargeRelatedMaster;
import ams.cms.model.CurrencyConversionMaster;
import ams.cms.model.CurrencyMaster;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.CustomerIdMap;
import ams.cms.model.GLAccountCreation;
import ams.cms.model.GLAccountLoadingMaster;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.IdentityProofDocumentTypeMaster;
import ams.cms.model.JournalTransfer;
import ams.cms.model.MccWiseInterestModel;
import ams.cms.model.MerchantCategoryCodeMaster;
import ams.cms.model.MultiCurrencyWalletAccountMaster;
import ams.cms.model.ParticipantWiseWalletMaster;
import ams.cms.model.RevolvingCreditCardTxnMaster;
import ams.cms.model.RevolvingCreditInterestTxn;
import ams.cms.model.TaxConfigModel;
import ams.cms.model.TransactionRequest;
import ams.cms.model.TransactionTypeModel;
import ams.cms.model.TxnReqRes;
import ams.cms.model.WalleToWalletTransfer;
import ams.cms.model.WalletAccountMaster;
import ams.cms.utility.SignInResponseParam;
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ProcessWebResponse implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String code;
	private String desc;
	private String status;
	private String message;
	private String result;
	private String count;
	
	private String strParticipantId;
	private String activeTier;
	private String categorytypeExit;
	private String isGLAccountTypeExist;
	private String isGLAccountNumberExistcheck;
	private String isAccountExist;
	private String isCreditTypeExist;
	private String validateChargeRelated;
	private String strID;
	private String balanceAmtStr;
	private String  isAccountTypeExist;
	private String isDataExist;
	private String revolvingCreditCardMastersValidate;
	private String mccWiseInterestValidate;
	private String participantMaster;
	private String userMaster;
	private String accountNoPrefix;
	private String accountHolderName;  
	private String accountNumber;
	private String isCreditTypeAccount;
	private String strName;
	private String strDob;
	private String strGender;
	private String strEmail;
	private String strMobileNumber;
	private String cust_id;
	private String userId;
	private String account_Type;
	private String isAccountLinkedWithCustId;
	private String strUserName;
	private String strSessionId;
	private Date strLastLoginDate;
	private Date createdDate;
	
	private WalleToWalletTransfer WalleToWalletTransfer;
	private List<String> accountType = new ArrayList<String>();
	private List<String> addressType = new ArrayList<String>();
	private List<String> identityType = new ArrayList<String>();
	
	private AccountCreation accountCreation;
	private AccountStatement accountStatement;
	private GLAccountCreation isGLAccountNumberExist;
	private CategoryListModel categoryListModel;
	private TransactionTypeModel txnTypeMaster ;
	private AccountCreditLimitCategory accountCreditLimitCategory;
	private AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMaster;
	private AccountTypeCharges accountTypeCharges;
	private AccountTransactionLimitation accountTransactionLimitationObj ;
	private AccountLoadMaster accountLoadMaster;
	private  AccountTypeWiseWalletMaster accountTypeWiseWalletMaster;
	private AccountWiseCharges accountWiseCharges;
	private CardAccountLinkage cardAccountLinkageDta ;
	private GLAccountLoadingMaster glAccountLoadingMaster;
	private TxnReqRes txnReqResponse;
	private  RevolvingCreditInterestTxn revolvingCreditInterestTxn;
	private RevolvingCreditCardTxnMaster revolvingCreditCardTxnMaster;
	private PreAccountMaster preAccountMaster;
	private ParticipantWiseWalletMaster participantWiseWalletMaster;
	private MccWiseInterestModel mccWiseInterestModel;
	private JournalTransfer journalTransfer;
	private GLAccountStatement glAccountStatement;
	private GLAccountCreation glAccountCreation;
	private CustomerIdMap custIdMapData ;
	private CustomerIdCreation customerIdCreation;
	private List<Categorytype> category = new ArrayList<Categorytype>();
	private List<GLAccountTypeMaster>glAccountviewModels = new ArrayList<GLAccountTypeMaster>();
	private List<CategoryListModel> categoryListModelsList = new ArrayList<CategoryListModel>();
	private List<TaxConfigModel> categoryListModelsListByTaxConfig = new ArrayList<TaxConfigModel>();
	private List<GLAccountCreation> gLAccountCreationlist = new ArrayList<GLAccountCreation>();
	private List<AccountTypeMaster> AccountTypeMasterlistData = new ArrayList<AccountTypeMaster>();
	private List<AccountCreditLimitCategory> accountCreditLimitCategoriesList = new ArrayList<AccountCreditLimitCategory>();
	private List<AccountTypeMaster> accountTypeMaster2 = new ArrayList<AccountTypeMaster>();
	private List<MerchantCategoryCodeMaster> mccListData = new ArrayList<MerchantCategoryCodeMaster>();
	private List<ParticipantWiseWalletMaster> mccParticipantListData = new ArrayList<ParticipantWiseWalletMaster>();
	private List<AccountTypeWiseBlockedMccMaster> accountTypeWiseBlockedMccMasters = new ArrayList<AccountTypeWiseBlockedMccMaster>();
	private List<ChargeRelatedMaster> chargeRelatedList =  new ArrayList<ChargeRelatedMaster>();
	private List<ChargeMaster> chargeMasters = new ArrayList<ChargeMaster>();
	private List<AccountTypeCharges> accTypeCharges = new ArrayList<AccountTypeCharges>();
	private List<AddressProofDocumentTypeMaster> addressProofDocumentTypeMasters = new ArrayList<AddressProofDocumentTypeMaster>();
	private List<IdentityProofDocumentTypeMaster> identityProofDocumentTypeMasters = new ArrayList<IdentityProofDocumentTypeMaster>();
	private List<PreAccountMaster> preAccountMasters = new  ArrayList<PreAccountMaster>();
	private List<AccountCreation> accountInfoList = new ArrayList<AccountCreation>();
	private List<AccountCreditCardTransactionModel> AccountCreditCardTransactionList = new ArrayList<AccountCreditCardTransactionModel>();
	private List<AccountInterestMaster> accountInterestMasters = new ArrayList<AccountInterestMaster>();
	private List<AccountStatement> accountStatementList = new ArrayList<AccountStatement>();
	private List<AccountTranMaster> accountTranMaster = new ArrayList<AccountTranMaster>();
	private List<JournalTransfer> journalTxnlist = new ArrayList<JournalTransfer>();
	private List<TransactionTypeModel> txnReportlist = new ArrayList<TransactionTypeModel>();
	private List<AccountTypeCharges> acountTypeChargesList = new ArrayList<AccountTypeCharges>();
	private List<ApproveUpgradeTier> approveUpgradeList = new ArrayList<ApproveUpgradeTier>();	
	private List<CardAccountLinkage> cardAccountLinkagelist = new ArrayList<CardAccountLinkage>();	
	private List<Channels> channelList = new ArrayList<Channels>();	
	private List<CountryCodeMaster> countryCodeMaster =  new ArrayList<CountryCodeMaster>();	
	private List<DenominationMaster> denominationMasterResp =  new ArrayList<DenominationMaster>();	
	private List<WalletAccountMaster> walletAccountMasters = new ArrayList<WalletAccountMaster>();	
	private List<PreSubAccountMaster> preSubAccountMaster = new ArrayList<PreSubAccountMaster>();
	private List<MccWiseInterestModel> mccWiseInterestModels =  new ArrayList<MccWiseInterestModel>();
	private List<JournalTransfer> JournalTransferList = new ArrayList<JournalTransfer>();
	private List<GLAccountStatement> gLAccountStatementList = new ArrayList<GLAccountStatement>();
	private List<AccountStatementHeader> accountStatementHeader =  new ArrayList<AccountStatementHeader>();
	private List<CustomerIdCreation> CustomerIdCreationlist = new ArrayList<CustomerIdCreation>();
	private List<SignInResponseParam> signInDatalist = new ArrayList<SignInResponseParam>();
	
	
	private String configCodeExists;
	private String isParticipantListEmpty;
	
	private double totalElementsSize;
	private double pageCount;
	
	private String txnId;
	private double updatedBalance;
	private double txnAmountwithCharges;
	private double txnAmount;
	private double amount;
	
	private double remaingAmount;
	private String remaingAmountCurrency;
	private MultiCurrencyWalletAccountMaster toMultiCurrencyWalletAccountMaster;
	private MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster;
	private TransactionRequest transactionRequest;
	private CurrencyMaster currencyMaster;
	private FeeTypeMaster feeTypeMaster;
	private CurrencyConversionMaster currencyConversionMaster;
	
	private HashMap<String, CurrencyMaster> currencyMasterDetailMap = new HashMap<String, CurrencyMaster>();
	private HashMap<String, CurrencyMaster> multiCurrencyWalletMap = new HashMap<String, CurrencyMaster>();
	
	private MultiCurrencyWalletAccountMaster baseCurrencyWalletMaster;
	
	private MultiCurrencyWalletAccountMaster currencyAccountMaster;
	private MultiCurrencyWalletAccountMaster currencyAccountMasterFee;
	private MultiCurrencyWalletAccountMaster currencyAccountMasterGst;
	
	private GLAccountTypeMaster currencyGl;
	private GLAccountTypeMaster feeCurrencyGl;
	private GLAccountTypeMaster gstCurrencyGl;
	
	private double feeAmount;
	private double gstAmount;
	private String txnType;
	

	private String lastSweepInCurrencyCode ;
	
	private List<MultiCurrencyWalletAccountMaster> multiCurrencyWalletAccountMasterList = new ArrayList<MultiCurrencyWalletAccountMaster>();
	
	private TreeMap<Integer, String> remaingCurrencyMap = new TreeMap<Integer, String>(Collections.reverseOrder());
	
	private HashMap<String, CurrencyConversionMaster> CurrencyConversionDetailMap = new HashMap<String, CurrencyConversionMaster>();
	
	
	private CurrencyMaster currencyFromMaster;
	private CurrencyMaster currencyToMaster;
	
	private String strAccountType;
	
	public String getLastSweepInCurrencyCode() {
		return lastSweepInCurrencyCode;
	}
	public void setLastSweepInCurrencyCode(String lastSweepInCurrencyCode) {
		this.lastSweepInCurrencyCode = lastSweepInCurrencyCode;
	}
	public TreeMap<Integer, String> getRemaingCurrencyMap() {
		return remaingCurrencyMap;
	}
	public void setRemaingCurrencyMap(TreeMap<Integer, String> remaingCurrencyMap) {
		this.remaingCurrencyMap = remaingCurrencyMap;
	}
	public String getStrSessionId() {
		return strSessionId;
	}
	public void setStrSessionId(String strSessionId) {
		this.strSessionId = strSessionId;
	}
	public Date getStrLastLoginDate() {
		return strLastLoginDate;
	}
	public void setStrLastLoginDate(Date strLastLoginDate) {
		this.strLastLoginDate = strLastLoginDate;
	}
	public String getStrParticipantId() {
		return strParticipantId;
	}
	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}
	public String getStrUserName() {
		return strUserName;
	}
	public void setStrUserName(String strUserName) {
		this.strUserName = strUserName;
	}
	public List<SignInResponseParam> getSignInDatalist() {
		return signInDatalist;
	}
	public void setSignInDatalist(List<SignInResponseParam> signInDatalist) {
		this.signInDatalist = signInDatalist;
	}
	
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public List<CustomerIdCreation> getCustomerIdCreationlist() {
		return CustomerIdCreationlist;
	}
	public void setCustomerIdCreationlist(List<CustomerIdCreation> customerIdCreationlist) {
		CustomerIdCreationlist = customerIdCreationlist;
	}
	public CustomerIdCreation getCustomerIdCreation() {
		return customerIdCreation;
	}
	public void setCustomerIdCreation(CustomerIdCreation customerIdCreation) {
		this.customerIdCreation = customerIdCreation;
	}
	public CustomerIdMap getCustIdMapData() {
		return custIdMapData;
	}
	public void setCustIdMapData(CustomerIdMap custIdMapData) {
		this.custIdMapData = custIdMapData;
	}
	public GLAccountCreation getGlAccountCreation() {
		return glAccountCreation;
	}
	public void setGlAccountCreation(GLAccountCreation glAccountCreation) {
		this.glAccountCreation = glAccountCreation;
	}
	public List<AccountStatementHeader> getAccountStatementHeader() {
		return accountStatementHeader;
	}
	public void setAccountStatementHeader(List<AccountStatementHeader> accountStatementHeader) {
		this.accountStatementHeader = accountStatementHeader;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public GLAccountCreation getIsGLAccountNumberExist() {
		return isGLAccountNumberExist;
	}
	public void setIsGLAccountNumberExist(GLAccountCreation isGLAccountNumberExist) {
		this.isGLAccountNumberExist = isGLAccountNumberExist;
	}
	public List<Categorytype> getCategory() {
		return category;
	}
	public void setCategory(List<Categorytype> category) {
		this.category = category;
	}
	public CategoryListModel getCategoryListModel() {
		return categoryListModel;
	}
	public void setCategoryListModel(CategoryListModel categoryListModel) {
		this.categoryListModel = categoryListModel;
	}
	public List<GLAccountTypeMaster> getGlAccountviewModels() {
		return glAccountviewModels;
	}
	public void setGlAccountviewModels(List<GLAccountTypeMaster> glAccountviewModels) {
		this.glAccountviewModels = glAccountviewModels;
	}
	public List<CategoryListModel> getCategoryListModelsList() {
		return categoryListModelsList;
	}
	public void setCategoryListModelsList(List<CategoryListModel> categoryListModelsList) {
		this.categoryListModelsList = categoryListModelsList;
	}
	public List<TaxConfigModel> getCategoryListModelsListByTaxConfig() {
		return categoryListModelsListByTaxConfig;
	}
	public void setCategoryListModelsListByTaxConfig(List<TaxConfigModel> categoryListModelsListByTaxConfig) {
		this.categoryListModelsListByTaxConfig = categoryListModelsListByTaxConfig;
	}
	public List<GLAccountCreation> getgLAccountCreationlist() {
		return gLAccountCreationlist;
	}
	public void setgLAccountCreationlist(List<GLAccountCreation> gLAccountCreationlist) {
		this.gLAccountCreationlist = gLAccountCreationlist;
	}
	public List<AccountTypeMaster> getAccountTypeMasterlistData() {
		return AccountTypeMasterlistData;
	}
	public void setAccountTypeMasterlistData(List<AccountTypeMaster> accountTypeMasterlistData) {
		AccountTypeMasterlistData = accountTypeMasterlistData;
	}
	public String getCategorytypeExit() {
		return categorytypeExit;
	}
	public void setCategorytypeExit(String categorytypeExit) {
		this.categorytypeExit = categorytypeExit;
	}
	public String getIsGLAccountTypeExist() {
		return isGLAccountTypeExist;
	}
	public void setIsGLAccountTypeExist(String isGLAccountTypeExist) {
		this.isGLAccountTypeExist = isGLAccountTypeExist;
	}
	public String getIsGLAccountNumberExistcheck() {
		return isGLAccountNumberExistcheck;
	}
	public void setIsGLAccountNumberExistcheck(String isGLAccountNumberExistcheck) {
		this.isGLAccountNumberExistcheck = isGLAccountNumberExistcheck;
	}
	public String getIsAccountExist() {
		return isAccountExist;
	}
	public void setIsAccountExist(String isAccountExist) {
		this.isAccountExist = isAccountExist;
	}
	public TransactionTypeModel getTxnTypeMaster() {
		return txnTypeMaster;
	}
	public void setTxnTypeMaster(TransactionTypeModel txnTypeMaster) {
		this.txnTypeMaster = txnTypeMaster;
	}
	public List<AccountCreditLimitCategory> getAccountCreditLimitCategoriesList() {
		return accountCreditLimitCategoriesList;
	}
	public void setAccountCreditLimitCategoriesList(List<AccountCreditLimitCategory> accountCreditLimitCategoriesList) {
		this.accountCreditLimitCategoriesList = accountCreditLimitCategoriesList;
	}
	public String getIsCreditTypeExist() {
		return isCreditTypeExist;
	}
	public void setIsCreditTypeExist(String isCreditTypeExist) {
		this.isCreditTypeExist = isCreditTypeExist;
	}
	public AccountCreditLimitCategory getAccountCreditLimitCategory() {
		return accountCreditLimitCategory;
	}
	public void setAccountCreditLimitCategory(AccountCreditLimitCategory accountCreditLimitCategory) {
		this.accountCreditLimitCategory = accountCreditLimitCategory;
	}
	public List<AccountTypeMaster> getAccountTypeMaster2() {
		return accountTypeMaster2;
	}
	public void setAccountTypeMaster2(List<AccountTypeMaster> accountTypeMaster2) {
		this.accountTypeMaster2 = accountTypeMaster2;
	}
	public List<MerchantCategoryCodeMaster> getMccListData() {
		return mccListData;
	}
	public void setMccListData(List<MerchantCategoryCodeMaster> mccListData) {
		this.mccListData = mccListData;
	}
	public AccountTypeWiseBlockedMccMaster getAccountTypeWiseBlockedMccMaster() {
		return accountTypeWiseBlockedMccMaster;
	}
	public void setAccountTypeWiseBlockedMccMaster(AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMaster) {
		this.accountTypeWiseBlockedMccMaster = accountTypeWiseBlockedMccMaster;
	}
	public List<ParticipantWiseWalletMaster> getMccParticipantListData() {
		return mccParticipantListData;
	}
	public void setMccParticipantListData(List<ParticipantWiseWalletMaster> mccParticipantListData) {
		this.mccParticipantListData = mccParticipantListData;
	}
	public List<AccountTypeWiseBlockedMccMaster> getAccountTypeWiseBlockedMccMasters() {
		return accountTypeWiseBlockedMccMasters;
	}
	public void setAccountTypeWiseBlockedMccMasters(
			List<AccountTypeWiseBlockedMccMaster> accountTypeWiseBlockedMccMasters) {
		this.accountTypeWiseBlockedMccMasters = accountTypeWiseBlockedMccMasters;
	}
	public String getValidateChargeRelated() {
		return validateChargeRelated;
	}
	public void setValidateChargeRelated(String validateChargeRelated) {
		this.validateChargeRelated = validateChargeRelated;
	}
	public String getStrID() {
		return strID;
	}
	public void setStrID(String strID) {
		this.strID = strID;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public List<ChargeRelatedMaster> getChargeRelatedList() {
		return chargeRelatedList;
	}
	public void setChargeRelatedList(List<ChargeRelatedMaster> chargeRelatedList) {
		this.chargeRelatedList = chargeRelatedList;
	}
	public List<ChargeMaster> getChargeMasters() {
		return chargeMasters;
	}
	public void setChargeMasters(List<ChargeMaster> chargeMasters) {
		this.chargeMasters = chargeMasters;
	}
	public List<AccountTypeCharges> getAccTypeCharges() {
		return accTypeCharges;
	}
	public void setAccTypeCharges(List<AccountTypeCharges> accTypeCharges) {
		this.accTypeCharges = accTypeCharges;
	}
	public AccountTypeCharges getAccountTypeCharges() {
		return accountTypeCharges;
	}
	public void setAccountTypeCharges(AccountTypeCharges accountTypeCharges) {
		this.accountTypeCharges = accountTypeCharges;
	}
	public List<AddressProofDocumentTypeMaster> getAddressProofDocumentTypeMasters() {
		return addressProofDocumentTypeMasters;
	}
	public void setAddressProofDocumentTypeMasters(List<AddressProofDocumentTypeMaster> addressProofDocumentTypeMasters) {
		this.addressProofDocumentTypeMasters = addressProofDocumentTypeMasters;
	}
	public List<IdentityProofDocumentTypeMaster> getIdentityProofDocumentTypeMasters() {
		return identityProofDocumentTypeMasters;
	}
	public void setIdentityProofDocumentTypeMasters(
			List<IdentityProofDocumentTypeMaster> identityProofDocumentTypeMasters) {
		this.identityProofDocumentTypeMasters = identityProofDocumentTypeMasters;
	}
	public AccountTransactionLimitation getAccountTransactionLimitationObj() {
		return accountTransactionLimitationObj;
	}
	public void setAccountTransactionLimitationObj(AccountTransactionLimitation accountTransactionLimitationObj) {
		this.accountTransactionLimitationObj = accountTransactionLimitationObj;
	}
	public List<PreAccountMaster> getPreAccountMasters() {
		return preAccountMasters;
	}
	public void setPreAccountMasters(List<PreAccountMaster> preAccountMasters) {
		this.preAccountMasters = preAccountMasters;
	}
	public List<AccountCreation> getAccountInfoList() {
		return accountInfoList;
	}
	public void setAccountInfoList(List<AccountCreation> accountInfoList) {
		this.accountInfoList = accountInfoList;
	}
	public List<AccountCreditCardTransactionModel> getAccountCreditCardTransactionList() {
		return AccountCreditCardTransactionList;
	}
	public void setAccountCreditCardTransactionList(
			List<AccountCreditCardTransactionModel> accountCreditCardTransactionList) {
		AccountCreditCardTransactionList = accountCreditCardTransactionList;
	}
	public List<AccountInterestMaster> getAccountInterestMasters() {
		return accountInterestMasters;
	}
	public void setAccountInterestMasters(List<AccountInterestMaster> accountInterestMasters) {
		this.accountInterestMasters = accountInterestMasters;
	}
	public AccountLoadMaster getAccountLoadMaster() {
		return accountLoadMaster;
	}
	public void setAccountLoadMaster(AccountLoadMaster accountLoadMaster) {
		this.accountLoadMaster = accountLoadMaster;
	}
	public String getBalanceAmtStr() {
		return balanceAmtStr;
	}
	public void setBalanceAmtStr(String balanceAmtStr) {
		this.balanceAmtStr = balanceAmtStr;
	}
	public AccountCreation getAccountCreation() {
		return accountCreation;
	}
	public void setAccountCreation(AccountCreation accountCreation) {
		this.accountCreation = accountCreation;
	}
	public String getIsAccountTypeExist() {
		return isAccountTypeExist;
	}
	public void setIsAccountTypeExist(String isAccountTypeExist) {
		this.isAccountTypeExist = isAccountTypeExist;
	}
	public AccountStatement getAccountStatement() {
		return accountStatement;
	}
	public void setAccountStatement(AccountStatement accountStatement) {
		this.accountStatement = accountStatement;
	}
	public List<AccountStatement> getAccountStatementList() {
		return accountStatementList;
	}
	public void setAccountStatementList(List<AccountStatement> accountStatementList) {
		this.accountStatementList = accountStatementList;
	}
	public List<AccountTranMaster> getAccountTranMaster() {
		return accountTranMaster;
	}
	public void setAccountTranMaster(List<AccountTranMaster> accountTranMaster) {
		this.accountTranMaster = accountTranMaster;
	}
	public List<JournalTransfer> getJournalTxnlist() {
		return journalTxnlist;
	}
	public void setJournalTxnlist(List<JournalTransfer> journalTxnlist) {
		this.journalTxnlist = journalTxnlist;
	}
	public List<TransactionTypeModel> getTxnReportlist() {
		return txnReportlist;
	}
	public void setTxnReportlist(List<TransactionTypeModel> txnReportlist) {
		this.txnReportlist = txnReportlist;
	}
	public List<AccountTypeCharges> getAcountTypeChargesList() {
		return acountTypeChargesList;
	}
	public void setAcountTypeChargesList(List<AccountTypeCharges> acountTypeChargesList) {
		this.acountTypeChargesList = acountTypeChargesList;
	}
	public AccountTypeWiseWalletMaster getAccountTypeWiseWalletMaster() {
		return accountTypeWiseWalletMaster;
	}
	public void setAccountTypeWiseWalletMaster(AccountTypeWiseWalletMaster accountTypeWiseWalletMaster) {
		this.accountTypeWiseWalletMaster = accountTypeWiseWalletMaster;
	}
	public AccountWiseCharges getAccountWiseCharges() {
		return accountWiseCharges;
	}
	public void setAccountWiseCharges(AccountWiseCharges accountWiseCharges) {
		this.accountWiseCharges = accountWiseCharges;
	}
	public List<ApproveUpgradeTier> getApproveUpgradeList() {
		return approveUpgradeList;
	}
	public void setApproveUpgradeList(List<ApproveUpgradeTier> approveUpgradeList) {
		this.approveUpgradeList = approveUpgradeList;
	}
	public CardAccountLinkage getCardAccountLinkageDta() {
		return cardAccountLinkageDta;
	}
	public void setCardAccountLinkageDta(CardAccountLinkage cardAccountLinkageDta) {
		this.cardAccountLinkageDta = cardAccountLinkageDta;
	}
	public List<CardAccountLinkage> getCardAccountLinkagelist() {
		return cardAccountLinkagelist;
	}
	public void setCardAccountLinkagelist(List<CardAccountLinkage> cardAccountLinkagelist) {
		this.cardAccountLinkagelist = cardAccountLinkagelist;
	}
	public String getIsDataExist() {
		return isDataExist;
	}
	public void setIsDataExist(String isDataExist) {
		this.isDataExist = isDataExist;
	}
	public List<Channels> getChannelList() {
		return channelList;
	}
	public void setChannelList(List<Channels> channelList) {
		this.channelList = channelList;
	}
	public List<CountryCodeMaster> getCountryCodeMaster() {
		return countryCodeMaster;
	}
	public void setCountryCodeMaster(List<CountryCodeMaster> countryCodeMaster) {
		this.countryCodeMaster = countryCodeMaster;
	}
	public List<DenominationMaster> getDenominationMasterResp() {
		return denominationMasterResp;
	}
	public void setDenominationMasterResp(List<DenominationMaster> denominationMasterResp) {
		this.denominationMasterResp = denominationMasterResp;
	}
	public GLAccountLoadingMaster getGlAccountLoadingMaster() {
		return glAccountLoadingMaster;
	}
	public void setGlAccountLoadingMaster(GLAccountLoadingMaster glAccountLoadingMaster) {
		this.glAccountLoadingMaster = glAccountLoadingMaster;
	}
	public List<WalletAccountMaster> getWalletAccountMasters() {
		return walletAccountMasters;
	}
	public void setWalletAccountMasters(List<WalletAccountMaster> walletAccountMasters) {
		this.walletAccountMasters = walletAccountMasters;
	}
	public TxnReqRes getTxnReqResponse() {
		return txnReqResponse;
	}
	public void setTxnReqResponse(TxnReqRes txnReqResponse) {
		this.txnReqResponse = txnReqResponse;
	}
	public RevolvingCreditInterestTxn getRevolvingCreditInterestTxn() {
		return revolvingCreditInterestTxn;
	}
	public void setRevolvingCreditInterestTxn(RevolvingCreditInterestTxn revolvingCreditInterestTxn) {
		this.revolvingCreditInterestTxn = revolvingCreditInterestTxn;
	}
	public RevolvingCreditCardTxnMaster getRevolvingCreditCardTxnMaster() {
		return revolvingCreditCardTxnMaster;
	}
	public void setRevolvingCreditCardTxnMaster(RevolvingCreditCardTxnMaster revolvingCreditCardTxnMaster) {
		this.revolvingCreditCardTxnMaster = revolvingCreditCardTxnMaster;
	}
	public String getRevolvingCreditCardMastersValidate() {
		return revolvingCreditCardMastersValidate;
	}
	public void setRevolvingCreditCardMastersValidate(String revolvingCreditCardMastersValidate) {
		this.revolvingCreditCardMastersValidate = revolvingCreditCardMastersValidate;
	}
	public List<PreSubAccountMaster> getPreSubAccountMaster() {
		return preSubAccountMaster;
	}
	public void setPreSubAccountMaster(List<PreSubAccountMaster> preSubAccountMaster) {
		this.preSubAccountMaster = preSubAccountMaster;
	}
	public PreAccountMaster getPreAccountMaster() {
		return preAccountMaster;
	}
	public void setPreAccountMaster(PreAccountMaster preAccountMaster) {
		this.preAccountMaster = preAccountMaster;
	}
	public ParticipantWiseWalletMaster getParticipantWiseWalletMaster() {
		return participantWiseWalletMaster;
	}
	public void setParticipantWiseWalletMaster(ParticipantWiseWalletMaster participantWiseWalletMaster) {
		this.participantWiseWalletMaster = participantWiseWalletMaster;
	}
	public MccWiseInterestModel getMccWiseInterestModel() {
		return mccWiseInterestModel;
	}
	public void setMccWiseInterestModel(MccWiseInterestModel mccWiseInterestModel) {
		this.mccWiseInterestModel = mccWiseInterestModel;
	}
	public List<MccWiseInterestModel> getMccWiseInterestModels() {
		return mccWiseInterestModels;
	}
	public void setMccWiseInterestModels(List<MccWiseInterestModel> mccWiseInterestModels) {
		this.mccWiseInterestModels = mccWiseInterestModels;
	}
	public String getMccWiseInterestValidate() {
		return mccWiseInterestValidate;
	}
	public void setMccWiseInterestValidate(String mccWiseInterestValidate) {
		this.mccWiseInterestValidate = mccWiseInterestValidate;
	}
	public List<JournalTransfer> getJournalTransferList() {
		return JournalTransferList;
	}
	public void setJournalTransferList(List<JournalTransfer> journalTransferList) {
		JournalTransferList = journalTransferList;
	}
	public JournalTransfer getJournalTransfer() {
		return journalTransfer;
	}
	public void setJournalTransfer(JournalTransfer journalTransfer) {
		this.journalTransfer = journalTransfer;
	}
	public GLAccountStatement getGlAccountStatement() {
		return glAccountStatement;
	}
	public void setGlAccountStatement(GLAccountStatement glAccountStatement) {
		this.glAccountStatement = glAccountStatement;
	}
	public List<GLAccountStatement> getgLAccountStatementList() {
		return gLAccountStatementList;
	}
	public void setgLAccountStatementList(List<GLAccountStatement> gLAccountStatementList) {
		this.gLAccountStatementList = gLAccountStatementList;
	}
	
	public String getUserMaster() {
		return userMaster;
	}
	public void setUserMaster(String userMaster) {
		this.userMaster = userMaster;
	}
	
	public String getParticipantMaster() {
		return participantMaster;
	}
	public void setParticipantMaster(String participantMaster) {
		this.participantMaster = participantMaster;
	}
	public String getAccountHolderName() {
		return accountHolderName;
	}
	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getIsCreditTypeAccount() {
		return isCreditTypeAccount;
	}
	public void setIsCreditTypeAccount(String isCreditTypeAccount) {
		this.isCreditTypeAccount = isCreditTypeAccount;
	}
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public String getStrDob() {
		return strDob;
	}
	public void setStrDob(String strDob) {
		this.strDob = strDob;
	}
	public String getStrGender() {
		return strGender;
	}
	public void setStrGender(String strGender) {
		this.strGender = strGender;
	}
	public String getStrEmail() {
		return strEmail;
	}
	public void setStrEmail(String strEmail) {
		this.strEmail = strEmail;
	}
	public String getStrMobileNumber() {
		return strMobileNumber;
	}
	public void setStrMobileNumber(String strMobileNumber) {
		this.strMobileNumber = strMobileNumber;
	}
	public List<String> getAccountType() {
		return accountType;
	}
	public void setAccountType(List<String> accountType) {
		this.accountType = accountType;
	}
	public List<String> getAddressType() {
		return addressType;
	}
	public void setAddressType(List<String> addressType) {
		this.addressType = addressType;
	}
	public List<String> getIdentityType() {
		return identityType;
	}
	public void setIdentityType(List<String> identityType) {
		this.identityType = identityType;
	}
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAccount_Type() {
		return account_Type;
	}
	public void setAccount_Type(String account_Type) {
		this.account_Type = account_Type;
	}
	public String getIsAccountLinkedWithCustId() {
		return isAccountLinkedWithCustId;
	}
	public void setIsAccountLinkedWithCustId(String isAccountLinkedWithCustId) {
		this.isAccountLinkedWithCustId = isAccountLinkedWithCustId;
	}
	public String getActiveTier() {
		return activeTier;
	}
	public void setActiveTier(String activeTier) {
		this.activeTier = activeTier;
	}
	
	
	
	
	public String getAccountNoPrefix() {
		return accountNoPrefix;
	}
	public void setAccountNoPrefix(String accountNoPrefix) {
		this.accountNoPrefix = accountNoPrefix;
	}
	public String getConfigCodeExists() {
		return configCodeExists;
	}
	public void setConfigCodeExists(String configCodeExists) {
		this.configCodeExists = configCodeExists;
	}
	public String getIsParticipantListEmpty() {
		return isParticipantListEmpty;
	}
	public void setIsParticipantListEmpty(String isParticipantListEmpty) {
		this.isParticipantListEmpty = isParticipantListEmpty;
	}
	public double getTotalElementsSize() {
		return totalElementsSize;
	}
	public void setTotalElementsSize(double totalElementsSize) {
		this.totalElementsSize = totalElementsSize;
	}
	public double getPageCount() {
		return pageCount;
	}
	public void setPageCount(double pageCount) {
		this.pageCount = pageCount;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public double getUpdatedBalance() {
		return updatedBalance;
	}
	public void setUpdatedBalance(double updatedBalance) {
		this.updatedBalance = updatedBalance;
	}
	public double getTxnAmountwithCharges() {
		return txnAmountwithCharges;
	}
	public void setTxnAmountwithCharges(double txnAmountwithCharges) {
		this.txnAmountwithCharges = txnAmountwithCharges;
	}
	public double getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(double txnAmount) {
		this.txnAmount = txnAmount;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getRemaingAmount() {
		return remaingAmount;
	}
	public void setRemaingAmount(double remaingAmount) {
		this.remaingAmount = remaingAmount;
	}
	public String getRemaingAmountCurrency() {
		return remaingAmountCurrency;
	}
	public void setRemaingAmountCurrency(String remaingAmountCurrency) {
		this.remaingAmountCurrency = remaingAmountCurrency;
	}
	public MultiCurrencyWalletAccountMaster getToMultiCurrencyWalletAccountMaster() {
		return toMultiCurrencyWalletAccountMaster;
	}
	public void setToMultiCurrencyWalletAccountMaster(MultiCurrencyWalletAccountMaster toMultiCurrencyWalletAccountMaster) {
		this.toMultiCurrencyWalletAccountMaster = toMultiCurrencyWalletAccountMaster;
	}
	public MultiCurrencyWalletAccountMaster getMultiCurrencyWalletAccountMaster() {
		return multiCurrencyWalletAccountMaster;
	}
	public void setMultiCurrencyWalletAccountMaster(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		this.multiCurrencyWalletAccountMaster = multiCurrencyWalletAccountMaster;
	}
	public TransactionRequest getTransactionRequest() {
		return transactionRequest;
	}
	public void setTransactionRequest(TransactionRequest transactionRequest) {
		this.transactionRequest = transactionRequest;
	}
	public CurrencyMaster getCurrencyMaster() {
		return currencyMaster;
	}
	public void setCurrencyMaster(CurrencyMaster currencyMaster) {
		this.currencyMaster = currencyMaster;
	}
	public FeeTypeMaster getFeeTypeMaster() {
		return feeTypeMaster;
	}
	public void setFeeTypeMaster(FeeTypeMaster feeTypeMaster) {
		this.feeTypeMaster = feeTypeMaster;
	}
	public CurrencyConversionMaster getCurrencyConversionMaster() {
		return currencyConversionMaster;
	}
	public void setCurrencyConversionMaster(CurrencyConversionMaster currencyConversionMaster) {
		this.currencyConversionMaster = currencyConversionMaster;
	}
	public HashMap<String, CurrencyMaster> getCurrencyMasterDetailMap() {
		return currencyMasterDetailMap;
	}
	public void setCurrencyMasterDetailMap(HashMap<String, CurrencyMaster> currencyMasterDetailMap) {
		this.currencyMasterDetailMap = currencyMasterDetailMap;
	}
	public HashMap<String, CurrencyMaster> getMultiCurrencyWalletMap() {
		return multiCurrencyWalletMap;
	}
	public void setMultiCurrencyWalletMap(HashMap<String, CurrencyMaster> multiCurrencyWalletMap) {
		this.multiCurrencyWalletMap = multiCurrencyWalletMap;
	}
	public MultiCurrencyWalletAccountMaster getBaseCurrencyWalletMaster() {
		return baseCurrencyWalletMaster;
	}
	public void setBaseCurrencyWalletMaster(MultiCurrencyWalletAccountMaster baseCurrencyWalletMaster) {
		this.baseCurrencyWalletMaster = baseCurrencyWalletMaster;
	}
	public MultiCurrencyWalletAccountMaster getCurrencyAccountMaster() {
		return currencyAccountMaster;
	}
	public void setCurrencyAccountMaster(MultiCurrencyWalletAccountMaster currencyAccountMaster) {
		this.currencyAccountMaster = currencyAccountMaster;
	}
	public MultiCurrencyWalletAccountMaster getCurrencyAccountMasterFee() {
		return currencyAccountMasterFee;
	}
	public void setCurrencyAccountMasterFee(MultiCurrencyWalletAccountMaster currencyAccountMasterFee) {
		this.currencyAccountMasterFee = currencyAccountMasterFee;
	}
	public MultiCurrencyWalletAccountMaster getCurrencyAccountMasterGst() {
		return currencyAccountMasterGst;
	}
	public void setCurrencyAccountMasterGst(MultiCurrencyWalletAccountMaster currencyAccountMasterGst) {
		this.currencyAccountMasterGst = currencyAccountMasterGst;
	}
	public GLAccountTypeMaster getCurrencyGl() {
		return currencyGl;
	}
	public void setCurrencyGl(GLAccountTypeMaster currencyGl) {
		this.currencyGl = currencyGl;
	}
	public GLAccountTypeMaster getFeeCurrencyGl() {
		return feeCurrencyGl;
	}
	public void setFeeCurrencyGl(GLAccountTypeMaster feeCurrencyGl) {
		this.feeCurrencyGl = feeCurrencyGl;
	}
	public GLAccountTypeMaster getGstCurrencyGl() {
		return gstCurrencyGl;
	}
	public void setGstCurrencyGl(GLAccountTypeMaster gstCurrencyGl) {
		this.gstCurrencyGl = gstCurrencyGl;
	}
	public double getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(double feeAmount) {
		this.feeAmount = feeAmount;
	}
	public double getGstAmount() {
		return gstAmount;
	}
	public void setGstAmount(double gstAmount) {
		this.gstAmount = gstAmount;
	}
	public List<MultiCurrencyWalletAccountMaster> getMultiCurrencyWalletAccountMasterList() {
		return multiCurrencyWalletAccountMasterList;
	}
	public void setMultiCurrencyWalletAccountMasterList(
			List<MultiCurrencyWalletAccountMaster> multiCurrencyWalletAccountMasterList) {
		this.multiCurrencyWalletAccountMasterList = multiCurrencyWalletAccountMasterList;
	}
	
	public HashMap<String, CurrencyConversionMaster> getCurrencyConversionDetailMap() {
		return CurrencyConversionDetailMap;
	}
	public void setCurrencyConversionDetailMap(HashMap<String, CurrencyConversionMaster> currencyConversionDetailMap) {
		CurrencyConversionDetailMap = currencyConversionDetailMap;
	}
	
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public CurrencyMaster getCurrencyFromMaster() {
		return currencyFromMaster;
	}
	public void setCurrencyFromMaster(CurrencyMaster currencyFromMaster) {
		this.currencyFromMaster = currencyFromMaster;
	}
	public CurrencyMaster getCurrencyToMaster() {
		return currencyToMaster;
	}
	public void setCurrencyToMaster(CurrencyMaster currencyToMaster) {
		this.currencyToMaster = currencyToMaster;
	}
	public WalleToWalletTransfer getWalleToWalletTransfer() {
		return WalleToWalletTransfer;
	}
	public void setWalleToWalletTransfer(WalleToWalletTransfer walleToWalletTransfer) {
		WalleToWalletTransfer = walleToWalletTransfer;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getStrAccountType() {
		return strAccountType;
	}
	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}
	@Override
	public String toString() {
		return "ProcessResponse [code=" + code + ", status=" + status + ", message=" + message + "]";
	}
}
