package ams.cms.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import ams.cms.api.model.AccountBalanceResponse;
import ams.cms.api.model.AccountCreationResponse;
import ams.cms.api.model.AccountCreditBalanceTxnResponse;
import ams.cms.api.model.AccountCreditCardInterestResponse;
import ams.cms.api.model.AccountCreditCardTxnResponse;
import ams.cms.api.model.AccountStatementResponse;
import ams.cms.api.model.AccountStatementSortResponse;
import ams.cms.api.model.AccountTranMasterResponse;
import ams.cms.api.model.AccountTranscationResponse;
import ams.cms.api.model.AccountTxnBalanceRequest;
import ams.cms.api.model.AccountTxnLimitResponse;
import ams.cms.api.model.AccountWiseInterestMasterResponse;
import ams.cms.api.model.BankListResponse;
import ams.cms.api.model.BulkTransferExcelReqRes;
import ams.cms.api.model.BulkTransferFromAccounts;
import ams.cms.api.model.BulkTransferToAccounts;
import ams.cms.api.model.CardAccountLinkageResponse;
import ams.cms.api.model.City_MasterDto;
import ams.cms.api.model.Complaint;
import ams.cms.api.model.CountryCodeMaster;
import ams.cms.api.model.Country_MasterDto;
import ams.cms.api.model.CreditAccountLimitResponse;
import ams.cms.api.model.CreditTxnLimitResponse;
import ams.cms.api.model.CumulativelimitResponse;
import ams.cms.api.model.FeeTypeMaster;
import ams.cms.api.model.PendingComplaintResponse;
import ams.cms.api.model.RefundResponse;
import ams.cms.api.model.State_MasterDto;
import ams.cms.api.model.TxnLimitResponse;
import ams.cms.api.model.VatTypeMaster;
import ams.cms.api.model.WalletAccountResponse;
import ams.cms.api.model.WalletBalanceResponse;
import ams.cms.api.model.WalletBalanceShowResponse;
import ams.cms.api.response.AccountInfoResponse;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountTypeLrsView;
import ams.cms.model.AccountTypeMaster;
import ams.cms.model.BankDetailsInfo;
import ams.cms.model.CurrencyTransferMaster;
import ams.cms.model.DormantAccountInformation;
import ams.cms.model.DormantAccountMaster;
import ams.cms.model.DormantToActiveMaster;
import ams.cms.model.FundTransferResponse;
import ams.cms.model.LoadMoneyPayStructure;
import ams.cms.model.LoadMoneyTransactionResponse;
import ams.cms.model.LrsView;
import ams.cms.model.MontraAccountMaster;
import ams.cms.model.MultiCurrencyWalletAccountMaster;
import ams.cms.model.MultiCurrencyWalletAccountStatementList;
import ams.cms.model.PriorityWalletResponse;
import ams.cms.model.TransactionInformation;
import ams.cms.utility.BankDetailsResponse;
import ams.cms.utility.BankInfoResponseModel;
import ams.cms.utility.Data;
import ams.cms.utility.ExternalServerRequestResponseModel;
import ams.cms.utility.PayloadReqRes;
import ams.cms.utility.SignInResponseParam;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessResponse implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String code;
	private String desc;
	private String userDefinedMessage;
	private String errorEvent;
	
	private String token;
	private String status;
	private String message;
	private String result;
	private String isAccountLinkedWithCustId;
	private String accountHolderName;  
	private Date createdDate;
	
	private String conversionRate;
	
	private List<CurrencyTransferMaster> currencyTransferMasterList = new ArrayList<CurrencyTransferMaster>();
	private List<AccountTypeMaster> getAccountTypeMasterList = new ArrayList<AccountTypeMaster>();
	private String isCurrencyTransfer;
	private List<String> accountType = new ArrayList<String>();
	private List<String> addressType = new ArrayList<String>();
	private List<String> identityType = new ArrayList<String>();

	// Added for Sign In response Start
	private String account_Type;
	private String userId;
	private String accountNumber;
	private String isCreditTypeAccount;
	// Added for Sign In response Start
	private String strName;
	private String strDob;
	private String strGender;
	private String strEmail;
	private String strMobileNumber;
	private String strAccountType;	
	private String responseCode;
	private String fromAccount;
	private String toAccount;
	private String currentAvailableBalance;
	private String transactionId;
	private String transactionDate;
	//private Date transactionDate;
	private String transactionTime;
	private String transactionDateTime;
	// Added by prashant Tayde
	private String cust_id;
	private List<CountryCodeMaster> countryCodeMaster = new ArrayList<CountryCodeMaster>();

	// Added By Sagar Khawse Start
	private List<WalletBalanceResponse> walletBalance = new ArrayList<WalletBalanceResponse>();
	private List<AccountCreationResponse> accountProfile = new ArrayList<AccountCreationResponse>();
	private List<AccountBalanceResponse> accountBalanceView = new ArrayList<AccountBalanceResponse>();
	private List<AccountTranMasterResponse> accountTxnlist = new ArrayList<AccountTranMasterResponse>();
	private List<CardAccountLinkageResponse> cardlinkagelist = new ArrayList<CardAccountLinkageResponse>();
	private List<AccountStatementResponse> accountstatementlist = new ArrayList<AccountStatementResponse>();
	private List<WalletBalanceShowResponse> walletBalanceshow = new ArrayList<WalletBalanceShowResponse>();
	private List<AccountCreditCardTxnResponse> carditBalancelist = new ArrayList<AccountCreditCardTxnResponse>();
	private List<AccountCreditBalanceTxnResponse> carditcardtxnlist = new ArrayList<AccountCreditBalanceTxnResponse>();
	private List<AccountCreditCardInterestResponse> carditInterestlist = new ArrayList<AccountCreditCardInterestResponse>();
	private List<AccountTxnBalanceRequest> accountTxnDatelist = new ArrayList<AccountTxnBalanceRequest>();
	private List<AccountTranMasterResponse> accountTranMasterResponse = new ArrayList<AccountTranMasterResponse>();
	private List<AccountStatementSortResponse> accountstatementSortlist = new ArrayList<AccountStatementSortResponse>();
	private List<AccountTranMasterResponse> accountTransactionlist = new ArrayList<AccountTranMasterResponse>();
	private String totalWalletBalance;
	private List<AccountWiseInterestMasterResponse> carditcardInterestlist = new ArrayList<AccountWiseInterestMasterResponse>();
	// Added By Sagar Khawse End
	
	//Added by Sunny soni for getting sign in Data in list Start
	private List<SignInResponseParam> accountNoList = new ArrayList<SignInResponseParam>();
	//Added by Sunny soni for getting sign in Data in list End
	private List<CreditAccountLimitResponse> creditAccountLimitResponses = new ArrayList<CreditAccountLimitResponse>();
	private List<AccountTranscationResponse> accountTransaction = new ArrayList<AccountTranscationResponse>();
	private TransactionInformation accountTxnInfo;
	
	//Added by ankit Start
	List<Complaint> complaint = new ArrayList<>();
	List<String> complaintTypes = new ArrayList<>();	
	//List<AccountLimitResponse> accountLimitResponse = new ArrayList<>();	
	List<PendingComplaintResponse> pendingComplaintResponse = new ArrayList<>();	
	private List<TxnLimitResponse> txnLimitResponse = new ArrayList<TxnLimitResponse>();	
	//Added by ankit End
	
	//Added by Abhishek Start
	List<AccountTxnLimitResponse> accountLimitResponse = new ArrayList<AccountTxnLimitResponse>();
	private List<CreditTxnLimitResponse>  accountCreditResponse = new ArrayList<CreditTxnLimitResponse>();
	//Added by Abhishek End
	
	//added by ankit on 19-04-2023
	private RefundResponse refundResponse;
	//added by ankit on 19-04-2023
	
	//added by sagar
	private String activeTier;
	
	List<Country_MasterDto> countryList = new ArrayList<Country_MasterDto>();	
	
	List<State_MasterDto> stateList = new ArrayList<State_MasterDto>();	
	
	List<City_MasterDto> cityList = new ArrayList<City_MasterDto>();	
	//added by sagar
	
	private String accountStatus;
	
	private String dynamicQrCodeImgUrl;
	
	private String strClearCardNo;
	
	private String requestId;
	
	private Data data;
	
	private String success;
	
	private List<Data> dataList;
	
	private List<BankDetailsInfo> bankDetailsInfo = new ArrayList<BankDetailsInfo>();
	
	private List<BankInfoResponseModel> bankListResponses = new ArrayList<BankInfoResponseModel>();
	
	private List<String> bankNames = new ArrayList<String>();
	private BankDetailsResponse bankDetails;
	
	private List<BankListResponse> banksList = new ArrayList<BankListResponse>();
	private ExternalServerRequestResponseModel beneficiaryDetails;
	private List<CumulativelimitResponse> cumulativeLimitlist = new ArrayList<CumulativelimitResponse>();
	
	private List<BulkTransferFromAccounts> bulkTrasnsferFromResponse = new ArrayList<>();	
	private List<BulkTransferToAccounts> bulkTransferToResponse = new ArrayList<>();
	private List<BulkTransferExcelReqRes> bulkTransferExcelReqRes = new ArrayList<>();
	private FundTransferResponse accountObj;
	private MontraAccountMaster montraAccountMasters; 
	
	private String strAccountStatus;
	private String montraId; 
	
	private PayloadReqRes payloadReqRes;
	private String participantApiKey;
	private String cid;
	private String bid;
	private String strCustId;
	private String accountName;
	private String authCode;
	private String montraTxnId;
	private String toAccountNo;
	private String FromAccountNo;
	private String amsTransactionId;
	private String strTransaction_amount;
	
	private String custId;
	private String strParticipantId;
	
	private String senderAccountNo;
	private String receipentAccountNo;
	private String txnAmount;
	
	private String agentAccountNo;
	private String agentAvailableBalance;
	private String customerAvailableBalance;
	
	private List<VatTypeMaster> vatCollectedList = new ArrayList<>();
	private List<FeeTypeMaster> feesCollectedList = new ArrayList<>();
	private List<AccountCreation> accountBalanceList = new ArrayList<>();
	private List<AccountInfoResponse> accountInfo = new ArrayList<>();
	
	private String customerFromAccountNo;
	private String agentToAccountNo;
	
	private String poolAccountBalance;
	private String entityInfo;
	private String entityNumber;
	
	//added by ankit 
	private String tier1ProfilePicutre;
	
	public String getTier1ProfilePicutre() {
		return tier1ProfilePicutre;
	}
	public void setTier1ProfilePicutre(String tier1ProfilePicutre) {
		this.tier1ProfilePicutre = tier1ProfilePicutre;
	}
	//added by ankit 
	
	

	private DormantAccountInformation dormantAccountInformation;
	private DormantAccountMaster dormantAccountMaster;
//	private boolean isChannelConfigerd = null != null;
	private AccountTypeLrsView accountTypeLrsView;
	
	
	List<DormantToActiveMaster> dormantToActiveMasters = new ArrayList<>();
	List<MultiCurrencyWalletAccountMaster> multiCurrencyWalletAccountMasters = new ArrayList<>();
	List<WalletAccountResponse> walletAccountResponses = new ArrayList<>();
	List<AccountStatementResponse> accountStatementResponses = new ArrayList<>();
	List<MultiCurrencyWalletAccountStatementList> multiCurrencyWalletAccountMastersList = new ArrayList<>();
	
	List<PriorityWalletResponse> priorityWalletResponsesList = new ArrayList<>();
	
	List<String> currencyCode;
	
	private String currencyConvertedAmount;
	
	private LoadMoneyPayStructure loadMoneyPayStructure;
	
	private LoadMoneyTransactionResponse loadMoneyTransactionResponse ;
	private LrsView lrsView;
	
	public ProcessResponse() {}
	public ProcessResponse(String token, String status, String message, String result) 
	{
		this.token = token;
		this.status = status;
		this.message = message;
		this.result = result;
	}
	public static ProcessResponse constructProcessResponse(ProcessResponse res,String errorCode, String errorDesc, String exceptionError)
	{
		if (null == res)
		{
			res = new ProcessResponse();
		}
		res.setCode(errorCode);
		res.setDesc(errorDesc);
		res.setErrorEvent(exceptionError);
		return res;
	}
	
	//added by prashant T
	public List<CountryCodeMaster> getCountryCodeMaster() {
		return countryCodeMaster;
	}
	public void setCountryCodeMaster(List<CountryCodeMaster> countryCodeMaster) {
		this.countryCodeMaster = countryCodeMaster;
	}
	public List<CreditAccountLimitResponse> getCreditAccountLimitResponses() 
	{
		return creditAccountLimitResponses;
	}

	public void setCreditAccountLimitResponses(List<CreditAccountLimitResponse> creditAccountLimitResponses) {
		this.creditAccountLimitResponses = creditAccountLimitResponses;
	}

	public List<TxnLimitResponse> getTxnLimitResponse() {
		return txnLimitResponse;
	}

	public void setTxnLimitResponse(List<TxnLimitResponse> txnLimitResponse) {
		this.txnLimitResponse = txnLimitResponse;
	}
	
	public List<Complaint> getComplaint() {
		return complaint;
	}

	public void setComplaint(List<Complaint> complaint) {
		this.complaint = complaint;
	}
	
	public List<PendingComplaintResponse> getPendingComplaintResponse() {
		return pendingComplaintResponse;
	}

	public void setPendingComplaintResponse(List<PendingComplaintResponse> pendingComplaintResponse) {
		this.pendingComplaintResponse = pendingComplaintResponse;
	}

	/*
	public List<AccountLimitResponse> getAccountLimitResponse() {
		return accountLimitResponse;
	}
	public void setAccountLimitResponse(List<AccountLimitResponse> accountLimitResponse) {
		this.accountLimitResponse = accountLimitResponse;
	}
	*/

	public List<String> getComplaintTypes() {
		return complaintTypes;
	}

	public void setComplaintTypes(List<String> complaintTypes) {
		this.complaintTypes = complaintTypes;
	}

	private List<String> complaitIdList = new ArrayList<String>();

	public List<String> getComplaitIdList() {
		return complaitIdList;
	}

	public void setComplaitIdList(List<String> complaitIdList) {
		this.complaitIdList = complaitIdList;
	}
	// added by ankit

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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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

	public String getUserDefinedMessage() {
		return userDefinedMessage;
	}

	public void setUserDefinedMessage(String userDefinedMessage) {
		this.userDefinedMessage = userDefinedMessage;
	}

	public String getErrorEvent() {
		return errorEvent;
	}

	public void setErrorEvent(String errorEvent) {
		this.errorEvent = errorEvent;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<AccountTypeMaster> getGetAccountTypeMasterList() {
		return getAccountTypeMasterList;
	}

	public void setGetAccountTypeMasterList(List<AccountTypeMaster> getAccountTypeMasterList) {
		this.getAccountTypeMasterList = getAccountTypeMasterList;
	}

	public List<String> getAccountType() {
		return accountType;
	}

	public void setAccountType(List<String> accountType) {
		this.accountType = accountType;
	}

	public String getAccount_Type() {
		return account_Type;
	}

	public void setAccount_Type(String account_Type) {
		this.account_Type = account_Type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
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

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
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

	public List<WalletBalanceResponse> getWalletBalance() {
		return walletBalance;
	}

	public void setWalletBalance(List<WalletBalanceResponse> walletBalance) {
		this.walletBalance = walletBalance;
	}

	public List<AccountCreationResponse> getAccountProfile() {
		return accountProfile;
	}

	public void setAccountProfile(List<AccountCreationResponse> accountProfile) {
		this.accountProfile = accountProfile;
	}

	public List<AccountBalanceResponse> getAccountBalanceView() {
		return accountBalanceView;
	}

	public void setAccountBalanceView(List<AccountBalanceResponse> accountBalanceView) {
		this.accountBalanceView = accountBalanceView;
	}

	public List<AccountTranMasterResponse> getAccountTxnlist() {
		return accountTxnlist;
	}

	public void setAccountTxnlist(List<AccountTranMasterResponse> accountTxnlist) {
		this.accountTxnlist = accountTxnlist;
	}

	public List<CardAccountLinkageResponse> getCardlinkagelist() {
		return cardlinkagelist;
	}

	public void setCardlinkagelist(List<CardAccountLinkageResponse> cardlinkagelist) {
		this.cardlinkagelist = cardlinkagelist;
	}

	public List<AccountStatementResponse> getAccountstatementlist() {
		return accountstatementlist;
	}

	public void setAccountstatementlist(List<AccountStatementResponse> accountstatementlist) {
		this.accountstatementlist = accountstatementlist;
	}

	public List<WalletBalanceShowResponse> getWalletBalanceshow() {
		return walletBalanceshow;
	}

	public void setWalletBalanceshow(List<WalletBalanceShowResponse> walletBalanceshow) {
		this.walletBalanceshow = walletBalanceshow;
	}

	public List<AccountWiseInterestMasterResponse> getCarditcardInterestlist() {
		return carditcardInterestlist;
	}

	public void setCarditcardInterestlist(List<AccountWiseInterestMasterResponse> carditcardInterestlist) {
		this.carditcardInterestlist = carditcardInterestlist;
	}

	public List<AccountCreditCardTxnResponse> getCarditBalancelist() {
		return carditBalancelist;
	}

	public void setCarditBalancelist(List<AccountCreditCardTxnResponse> carditBalancelist) {
		this.carditBalancelist = carditBalancelist;
	}

	public List<AccountCreditBalanceTxnResponse> getCarditcardtxnlist() {
		return carditcardtxnlist;
	}

	public void setCarditcardtxnlist(List<AccountCreditBalanceTxnResponse> carditcardtxnlist) {
		this.carditcardtxnlist = carditcardtxnlist;
	}

	public List<AccountCreditCardInterestResponse> getCarditInterestlist() {
		return carditInterestlist;
	}

	public void setCarditInterestlist(List<AccountCreditCardInterestResponse> carditInterestlist) {
		this.carditInterestlist = carditInterestlist;
	}

	public String getIsCreditTypeAccount() {
		return isCreditTypeAccount;
	}

	public void setIsCreditTypeAccount(String isCreditTypeAccount) {
		this.isCreditTypeAccount = isCreditTypeAccount;
	}

	public List<AccountTranMasterResponse> getAccountTransactionlist() {
		return accountTransactionlist;
	}

	public void setAccountTransactionlist(List<AccountTranMasterResponse> accountTransactionlist) {
		this.accountTransactionlist = accountTransactionlist;
	}

	public List<AccountTxnBalanceRequest> getAccountTxnDatelist() {
		return accountTxnDatelist;
	}

	public void setAccountTxnDatelist(List<AccountTxnBalanceRequest> accountTxnDatelist) {
		this.accountTxnDatelist = accountTxnDatelist;
	}

	public List<AccountTranMasterResponse> getAccountTranMasterResponse() {
		return accountTranMasterResponse;
	}

	public void setAccountTranMasterResponse(List<AccountTranMasterResponse> accountTranMasterResponse) {
		this.accountTranMasterResponse = accountTranMasterResponse;
	}

	public String getTotalWalletBalance() {
		return totalWalletBalance;
	}

	public void setTotalWalletBalance(String totalWalletBalance) {
		this.totalWalletBalance = totalWalletBalance;
	}

	public List<AccountStatementSortResponse> getAccountstatementSortlist() {
		return accountstatementSortlist;
	}

	public void setAccountstatementSortlist(List<AccountStatementSortResponse> accountstatementSortlist) {
		this.accountstatementSortlist = accountstatementSortlist;
	}
	public String getIsAccountLinkedWithCustId() {
		return isAccountLinkedWithCustId;
	}
	public void setIsAccountLinkedWithCustId(String isAccountLinkedWithCustId) {
		this.isAccountLinkedWithCustId = isAccountLinkedWithCustId;
	}
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getAccountHolderName() {
		return accountHolderName;
	}
	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}
	public String getToAccount() {
		return toAccount;
	}
	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}
	public String getCurrentAvailableBalance() {
		return currentAvailableBalance;
	}
	public void setCurrentAvailableBalance(String currentAvailableBalance) {
		this.currentAvailableBalance = currentAvailableBalance;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public String getTransactionTime() {
		return transactionTime;
	}
	/*
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	*/
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	//Added by Sunny soni for getting sign in Data in list Start
	public List<SignInResponseParam> getAccountNoList() {
		return accountNoList;
	}
	public void setAccountNoList(List<SignInResponseParam> accountNoList) {
		this.accountNoList = accountNoList;
	}
	//Added by Sunny soni for getting sign in Data in list End
	public List<AccountTranscationResponse> getAccountTransaction() {
		return accountTransaction;
	}
	public void setAccountTransaction(List<AccountTranscationResponse> accountTransaction) {
		this.accountTransaction = accountTransaction;
	}
	public String getDynamicQrCodeImgUrl() {
		return dynamicQrCodeImgUrl;
	}
	public void setDynamicQrCodeImgUrl(String dynamicQrCodeImgUrl) {
		this.dynamicQrCodeImgUrl = dynamicQrCodeImgUrl;
	}
	public String getStrClearCardNo() {
		return strClearCardNo;
	}
	public void setStrClearCardNo(String strClearCardNo) {
		this.strClearCardNo = strClearCardNo;
	}
	public List<AccountTxnLimitResponse> getAccountLimitResponse() {
		return accountLimitResponse;
	}
	public void setAccountLimitResponse(List<AccountTxnLimitResponse> accountLimitResponse) {
		this.accountLimitResponse = accountLimitResponse;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public List<CreditTxnLimitResponse> getAccountCreditResponse() {
		return accountCreditResponse;
	}
	public void setAccountCreditResponse(List<CreditTxnLimitResponse> accountCreditResponse) {
		this.accountCreditResponse = accountCreditResponse;
	}
	public RefundResponse getRefundResponse() {
		return refundResponse;
	}
	public void setRefundResponse(RefundResponse refundResponse) {
		this.refundResponse = refundResponse;
	}
	public String getTransactionDateTime() {
		return transactionDateTime;
	}
	public void setTransactionDateTime(String transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}
	public List<String> getBankNames() {
		return bankNames;
	}
	public void setBankNames(List<String> bankNames) {
		this.bankNames = bankNames;
	}
	public BankDetailsResponse getBankDetails() {
		return bankDetails;
	}
	public void setBankDetails(BankDetailsResponse bankDetails) {
		this.bankDetails = bankDetails;
	}
	public List<BankListResponse> getBanksList() {
		return banksList;
	}
	public void setBanksList(List<BankListResponse> banksList) {
		this.banksList = banksList;
	}
	public ExternalServerRequestResponseModel getBeneficiaryDetails() {
		return beneficiaryDetails;
	}
	public void setBeneficiaryDetails(ExternalServerRequestResponseModel beneficiaryDetails) {
		this.beneficiaryDetails = beneficiaryDetails;
	}
	public String getActiveTier() {
		return activeTier;
	}
	public void setActiveTier(String activeTier) {
		this.activeTier = activeTier;
	}
	public List<Country_MasterDto> getCountryList() {
		return countryList;
	}
	public void setCountryList(List<Country_MasterDto> countryList) {
		this.countryList = countryList;
	}
	public List<State_MasterDto> getStateList() {
		return stateList;
	}
	public void setStateList(List<State_MasterDto> stateList) {
		this.stateList = stateList;
	}
	public List<City_MasterDto> getCityList() {
		return cityList;
	}
	public void setCityList(List<City_MasterDto> cityList) {
		this.cityList = cityList;
	}
	public List<CumulativelimitResponse> getCumulativeLimitlist() {
		return cumulativeLimitlist;
	}
	public void setCumulativeLimitlist(List<CumulativelimitResponse> cumulativeLimitlist) {
		this.cumulativeLimitlist = cumulativeLimitlist;
	}
	
	public List<BulkTransferFromAccounts> getBulkTrasnsferFromResponse() {
		return bulkTrasnsferFromResponse;
	}
	public void setBulkTrasnsferFromResponse(List<BulkTransferFromAccounts> bulkTrasnsferFromResponse) {
		this.bulkTrasnsferFromResponse = bulkTrasnsferFromResponse;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public List<BankInfoResponseModel> getBankListResponses() {
		return bankListResponses;
	}
	public void setBankListResponses(List<BankInfoResponseModel> bankListResponses) {
		this.bankListResponses = bankListResponses;
	}
	public List<BulkTransferToAccounts> getBulkTransferToResponse() {
		return bulkTransferToResponse;
	}
	public void setBankDetailsInfo(List<BankDetailsInfo> bankDetailsInfo) {
		this.bankDetailsInfo = bankDetailsInfo;
	}
	public List<Data> getDataList() {
		return dataList;
	}
	public void setDataList(List<Data> dataList) {
		this.dataList = dataList;
	}
	
	public List<BankDetailsInfo> getBankDetailsInfo() {
		return bankDetailsInfo;
	}
	public void setBulkTransferToResponse(List<BulkTransferToAccounts> bulkTransferToResponse) {
		this.bulkTransferToResponse = bulkTransferToResponse;
	}	
	public List<BulkTransferExcelReqRes> getBulkTransferExcelReqRes() {
		return bulkTransferExcelReqRes;
	}
	public void setBulkTransferExcelReqRes(List<BulkTransferExcelReqRes> bulkTransferExcelReqRes) {
		this.bulkTransferExcelReqRes = bulkTransferExcelReqRes;
	}
	public FundTransferResponse getAccountObj() {
		return accountObj;
	}
	public void setAccountObj(FundTransferResponse accountObj) {
		this.accountObj = accountObj;
	}
	public String getStrAccountStatus() {
		return strAccountStatus;
	}
	public void setStrAccountStatus(String strAccountStatus) {
		this.strAccountStatus = strAccountStatus;
	}
	public String getMontraId() {
		return montraId;
	}
	public void setMontraId(String montraId) {
		this.montraId = montraId;
	}
	public PayloadReqRes getPayloadReqRes() {
		return payloadReqRes;
	}
	public void setPayloadReqRes(PayloadReqRes payloadReqRes) {
		this.payloadReqRes = payloadReqRes;
	}
	public String getParticipantApiKey() {
		return participantApiKey;
	}
	public void setParticipantApiKey(String participantApiKey) {
		this.participantApiKey = participantApiKey;
	}	
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getStrCustId() {
		return strCustId;
	}
	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}	
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getMontraTxnId() {
		return montraTxnId;
	}
	public void setMontraTxnId(String montraTxnId) {
		this.montraTxnId = montraTxnId;
	}
	public String getToAccountNo() {
		return toAccountNo;
	}
	public void setToAccountNo(String toAccountNo) {
		this.toAccountNo = toAccountNo;
	}
	public String getFromAccountNo() {
		return FromAccountNo;
	}
	public void setFromAccountNo(String fromAccountNo) {
		FromAccountNo = fromAccountNo;
	}
	public String getAmsTransactionId() {
		return amsTransactionId;
	}
	public void setAmsTransactionId(String amsTransactionId) {
		this.amsTransactionId = amsTransactionId;
	}
	public String getStrTransaction_amount() {
		return strTransaction_amount;
	}
	public void setStrTransaction_amount(String strTransaction_amount) {
		this.strTransaction_amount = strTransaction_amount;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getStrParticipantId() {
		return strParticipantId;
	}
	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getSenderAccountNo() {
		return senderAccountNo;
	}
	public void setSenderAccountNo(String senderAccountNo) {
		this.senderAccountNo = senderAccountNo;
	}
	public String getReceipentAccountNo() {
		return receipentAccountNo;
	}
	public void setReceipentAccountNo(String receipentAccountNo) {
		this.receipentAccountNo = receipentAccountNo;
	}
	public String getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}
	public String getAgentAccountNo() {
		return agentAccountNo;
	}
	public void setAgentAccountNo(String agentAccountNo) {
		this.agentAccountNo = agentAccountNo;
	}
	public String getAgentAvailableBalance() {
		return agentAvailableBalance;
	}
	public void setAgentAvailableBalance(String agentAvailableBalance) {
		this.agentAvailableBalance = agentAvailableBalance;
	}
	public String getCustomerAvailableBalance() {
		return customerAvailableBalance;
	}
	public void setCustomerAvailableBalance(String customerAvailableBalance) {
		this.customerAvailableBalance = customerAvailableBalance;
	}
	public String getCustomerFromAccountNo() {
		return customerFromAccountNo;
	}
	public void setCustomerFromAccountNo(String customerFromAccountNo) {
		this.customerFromAccountNo = customerFromAccountNo;
	}
	public String getAgentToAccountNo() {
		return agentToAccountNo;
	}
	public void setAgentToAccountNo(String agentToAccountNo) {
		this.agentToAccountNo = agentToAccountNo;
	}
	
	public String getPoolAccountBalance() {
		return poolAccountBalance;
	}
	public void setPoolAccountBalance(String poolAccountBalance) {
		this.poolAccountBalance = poolAccountBalance;
	}
	
	public List<VatTypeMaster> getVatCollectedList() {
		return vatCollectedList;
	}
	public void setVatCollectedList(List<VatTypeMaster> vatCollectedList) {
		this.vatCollectedList = vatCollectedList;
	}
	public List<FeeTypeMaster> getFeesCollectedList() {
		return feesCollectedList;
	}
	public void setFeesCollectedList(List<FeeTypeMaster> feesCollectedList) {
		this.feesCollectedList = feesCollectedList;
	}
	public List<AccountCreation> getAccountBalanceList() {
		return accountBalanceList;
	}
	public void setAccountBalanceList(List<AccountCreation> accountBalanceList) {
		this.accountBalanceList = accountBalanceList;
	}	
	public TransactionInformation getAccountTxnInfo() {
		return accountTxnInfo;
	}
	public void setAccountTxnInfo(TransactionInformation accountTxnInfo) {
		this.accountTxnInfo = accountTxnInfo;
	}
	public String getEntityInfo() {
		return entityInfo;
	}
	public void setEntityInfo(String entityInfo) {
		this.entityInfo = entityInfo;
	}
	public String getEntityNumber() {
		return entityNumber;
	}
	public void setEntityNumber(String entityNumber) {
		this.entityNumber = entityNumber;
	}
	public MontraAccountMaster getMontraAccountMasters() {
		return montraAccountMasters;
	}
	public void setMontraAccountMasters(MontraAccountMaster montraAccountMasters) {
		this.montraAccountMasters = montraAccountMasters;
	}
	public List<AccountInfoResponse> getAccountInfo() {
		return accountInfo;
	}
	public void setAccountInfo(List<AccountInfoResponse> accountInfo) {
		this.accountInfo = accountInfo;
	}
	public DormantAccountInformation getDormantAccountInformation() {
		return dormantAccountInformation;
	}
	public void setDormantAccountInformation(DormantAccountInformation dormantAccountInformation) {
		this.dormantAccountInformation = dormantAccountInformation;
	}	
	public DormantAccountMaster getDormantAccountMaster() {
		return dormantAccountMaster;
	}
	public void setDormantAccountMaster(DormantAccountMaster dormantAccountMaster) {
		this.dormantAccountMaster = dormantAccountMaster;
	}
	public List<DormantToActiveMaster> getDormantToActiveMasters() {
		return dormantToActiveMasters;
	}
	public void setDormantToActiveMasters(List<DormantToActiveMaster> dormantToActiveMasters) {
		this.dormantToActiveMasters = dormantToActiveMasters;
	}
	
	
	public List<MultiCurrencyWalletAccountMaster> getMultiCurrencyWalletAccountMasters() {
		return multiCurrencyWalletAccountMasters;
	}
	public void setMultiCurrencyWalletAccountMasters(
			List<MultiCurrencyWalletAccountMaster> multiCurrencyWalletAccountMasters) {
		this.multiCurrencyWalletAccountMasters = multiCurrencyWalletAccountMasters;
	}
	
	
	
	public AccountTypeLrsView getAccountTypeLrsView() {
		return accountTypeLrsView;
	}
	public void setAccountTypeLrsView(AccountTypeLrsView accountTypeLrsView) {
		this.accountTypeLrsView = accountTypeLrsView;
	}
	
	
	
	
	public LrsView getLrsView() {
		return lrsView;
	}
	public void setLrsView(LrsView lrsView) {
		this.lrsView = lrsView;
	}
	public LoadMoneyTransactionResponse getLoadMoneyTransactionResponse() {
		return loadMoneyTransactionResponse;
	}
	public void setLoadMoneyTransactionResponse(LoadMoneyTransactionResponse loadMoneyTransactionResponse) {
		this.loadMoneyTransactionResponse = loadMoneyTransactionResponse;
	}
	public LoadMoneyPayStructure getLoadMoneyPayStructure() {
		return loadMoneyPayStructure;
	}
	public void setLoadMoneyPayStructure(LoadMoneyPayStructure loadMoneyPayStructure) {
		this.loadMoneyPayStructure = loadMoneyPayStructure;
	}
	public List<String> getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(List<String> currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	
	
	
	public List<MultiCurrencyWalletAccountStatementList> getMultiCurrencyWalletAccountMastersList() {
		return multiCurrencyWalletAccountMastersList;
	}
	public void setMultiCurrencyWalletAccountMastersList(
			List<MultiCurrencyWalletAccountStatementList> multiCurrencyWalletAccountMastersList) {
		this.multiCurrencyWalletAccountMastersList = multiCurrencyWalletAccountMastersList;
	}
	public List<AccountStatementResponse> getAccountStatementResponses() {
		return accountStatementResponses;
	}
	public void setAccountStatementResponses(List<AccountStatementResponse> accountStatementResponses) {
		this.accountStatementResponses = accountStatementResponses;
	}
	public List<WalletAccountResponse> getWalletAccountResponses() {
		return walletAccountResponses;
	}
	public void setWalletAccountResponses(List<WalletAccountResponse> walletAccountResponses) {
		this.walletAccountResponses = walletAccountResponses;
	}
	public String getCurrencyConvertedAmount() {
		return currencyConvertedAmount;
	}
	public void setCurrencyConvertedAmount(String currencyConvertedAmount) {
		this.currencyConvertedAmount = currencyConvertedAmount;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getConversionRate() {
		return conversionRate;
	}
	public void setConversionRate(String conversionRate) {
		this.conversionRate = conversionRate;
	}
	public List<PriorityWalletResponse> getPriorityWalletResponsesList() {
		return priorityWalletResponsesList;
	}
	public void setPriorityWalletResponsesList(List<PriorityWalletResponse> priorityWalletResponsesList) {
		this.priorityWalletResponsesList = priorityWalletResponsesList;
	}
	public List<CurrencyTransferMaster> getCurrencyTransferMasterList() {
		return currencyTransferMasterList;
	}
	public void setCurrencyTransferMasterList(List<CurrencyTransferMaster> currencyTransferMasterList) {
		this.currencyTransferMasterList = currencyTransferMasterList;
	}
	public String getIsCurrencyTransfer() {
		return isCurrencyTransfer;
	}
	public void setIsCurrencyTransfer(String isCurrencyTransfer) {
		this.isCurrencyTransfer = isCurrencyTransfer;
	}
	@Override
	public String toString() {
		return "ProcessResponse [code=" + code + ", status=" + status + ", message=" + message + ", result=" + result
				+ "]";
	}
}
