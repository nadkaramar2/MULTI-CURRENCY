package ams.cms.config;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.DenominationMaster;
import ams.cms.api.model.SecretCodeRequest;
import ams.cms.api.model.UserTransactionModel;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.AccountTypeMaster;
import ams.cms.model.CardAccountLinkage;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.PullAccountModel;
import ams.cms.model.TierAccountMaster;
import ams.cms.model.TxnData;
import ams.cms.model.TxnReqRes;
import ams.cms.utility.ExternalServerRequestResponseModel;
import ams.cms.utility.MiddleWareRequestModel;
import ams.cms.utility.NameEnquiryRequestResponseModel;
import ams.cms.utility.PoolAccountBalanceInfo;
import ams.cms.utility.ThirdPartyRequestParam;
import ams.cms.utility.Utils;

public class TransactionConfig implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String message;
	private String success;
	
	private String txnId;
	private String txnAmount;	
	
	private String glAccountType;
	private String glAccountDescription;	
	private String accountNo;
	private String accountType;
	private String accountClosingBalance;
	private String accountTranType;
	private String accountNaration;
	private String accountTranMode;
	private String availableAccountClosingBalance;	
	private String accountHolderName;	
	private String strEmail;	
	private String userLinkedGLAccountType;	
	private String userLinkedGLAccountDescription;
	private String strNaration;
	private String status;
	private String accountName;
	private String strPreCredAmount;
	private String strEarMarkAmount;
	private AccountCreation accountCreation;
	private AccountMaster accountMaster;
	private GLAccountTypeMaster glAccountTypeMaster;
	private UserTransactionModel userTxnReq;
	private DenominationMaster denominationMaster;
	
	private AccountTypeMaster accountTypeMaster;
	private ThirdPartyRequestParam thirdPartyRequestParam;
	private CardAccountLinkage cardAccountLinkage;
	private ExternalServerRequestResponseModel externalServerRequestResponseModel;
	private AccountTranMaster accountTranMaster;
	private TierAccountMaster tierAccountMaster;
	
	private AccountMaster accountMasterFromAccount;
	private AccountMaster accountMasterToAccount;
	
	private TierAccountMaster fromAccountTierAccountMaster;
	private TierAccountMaster ToAccountTierAccountMaster;
	
	private AccountCreation fromAccountCreation;
	private AccountCreation ToAccountCreation;
	
	private String participantId;
	private String fromAccountNo;
	private String toAccountNo;
	private String rrn;
	private String mcc;
	private String tid;
	private String mid;
	private String stan;
	private String strMti;
	private String authCode;
	private String responseCode;
	private	String processingCode;
	private String mailMessage;
	private String glTranType;
	private String glTranMode;
	
	//Added by Pankaj P [start]
	private String strDailyCumuTxnLimit;
	private String strCumBalanceLimit;
	private String strUpdatedAmount;
	//Added by Pankaj P [End]	
	private String smsMessage;
	private String montraTxnId;
	private String srcTxnId;
	
	private ArrayList<String> emailIdList;
	private List<String> toNumbersListforSms = new ArrayList<String>();
	
	private MiddleWareRequestModel middleWareRequestModel;
	private NameEnquiryRequestResponseModel nameEnquiryRequestResponseModel;
	private String strParticipantId;
	
	private String vat;
	private String fee;
	
	private String systemTxnId;//From Montra its Session Id 
	
	private String information;
	
	private PullAccountModel pullAccountModel;
	private PoolAccountBalanceInfo poolAccountBalanceInfo;
	
	private SecretCodeRequest secretCodeRequest;
	private boolean isReversedTxn = false;
	
	public String getStrEarMarkAmount() {
		return strEarMarkAmount;
	}

	public void setStrEarMarkAmount(String strEarMarkAmount) {
		this.strEarMarkAmount = strEarMarkAmount;
	}

	public String getStrPreCredAmount() {
		return strPreCredAmount;
	}

	public void setStrPreCredAmount(String strPreCredAmount) {
		this.strPreCredAmount = strPreCredAmount;
	}

	

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
	public TransactionConfig getTransactionConfigInstance(TxnReqRes txnReqRes, TransactionConfig transactionConfig) 
	{
		TxnData txnData = txnReqRes.getTxnData();
		transactionConfig.setAccountTranType(txnData.getTran_type().trim());
		transactionConfig.setTxnId(String.valueOf(txnData.getTxn_id()));
		
		if (txnData.getDe022_pos_entry_mode()!=null && txnData.getDe022_pos_entry_mode().equalsIgnoreCase("810")) 
		{
			transactionConfig.setTxnAmount(Utils.decimalFormat.format(Double.valueOf(txnData.getDe004_amount()) / 100.00));
		} 
		else
		{
			transactionConfig.setTxnAmount(Utils.decimalFormat.format(Double.valueOf(txnData.getDe004_amount())));
		}
		
		if (txnData.getParticipant_id()!=null ) 
		{
			transactionConfig.setParticipantId(String.valueOf(txnData.getParticipant_id()));
		}
		else
		{
			transactionConfig.setParticipantId("0");
		}
		
		transactionConfig.setGlTranType(txnData.getTran_type().trim());
		
		if(txnReqRes.getHeader()!=null && txnReqRes.getHeader().getMti()!=null) 
		{
			transactionConfig.setStrMti(txnReqRes.getHeader().getMti());		
		}
		
		transactionConfig.setMcc(txnData.getDe018_mcc());
		transactionConfig.setRrn(txnData.getDe037_rrn());
		transactionConfig.setTid(txnData.getDe041_terminal_id());
		transactionConfig.setStan(txnData.getDe011_stan());
		transactionConfig.setMid(txnData.getDe042_mid());
		
		return transactionConfig;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}

	public AccountCreation getAccountCreation() {
		return accountCreation;
	}

	public void setAccountCreation(AccountCreation accountCreation) {
		this.accountCreation = accountCreation;
	}

	public GLAccountTypeMaster getGlAccountTypeMaster() {
		return glAccountTypeMaster;
	}

	public void setGlAccountTypeMaster(GLAccountTypeMaster glAccountTypeMaster) {
		this.glAccountTypeMaster = glAccountTypeMaster;
	}

	public String getGlAccountType() {
		return glAccountType;
	}

	public void setGlAccountType(String glAccountType) {
		this.glAccountType = glAccountType;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getGlAccountDescription() {
		return glAccountDescription;
	}

	public void setGlAccountDescription(String glAccountDescription) {
		this.glAccountDescription = glAccountDescription;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountClosingBalance() {
		return accountClosingBalance;
	}

	public void setAccountClosingBalance(String accountClosingBalance) {
		this.accountClosingBalance = accountClosingBalance;
	}

	public String getAccountTranType() {
		return accountTranType;
	}

	public void setAccountTranType(String accountTranType) {
		this.accountTranType = accountTranType;
	}

	public String getAccountNaration() {
		return accountNaration;
	}

	public void setAccountNaration(String accountNaration) {
		this.accountNaration = accountNaration;
	}

	public String getAccountTranMode() {
		return accountTranMode;
	}

	public void setAccountTranMode(String accountTranMode) {
		this.accountTranMode = accountTranMode;
	}
	public String getAvailableAccountClosingBalance() {
		return availableAccountClosingBalance;
	}

	public void setAvailableAccountClosingBalance(String availableAccountClosingBalance) {
		this.availableAccountClosingBalance = availableAccountClosingBalance;
	}

	public AccountMaster getAccountMaster() {
		return accountMaster;
	}

	public void setAccountMaster(AccountMaster accountMaster) {
		this.accountMaster = accountMaster;
	}

	public UserTransactionModel getUserTxnReq() {
		return userTxnReq;
	}

	public void setUserTxnReq(UserTransactionModel userTxnReq) {
		this.userTxnReq = userTxnReq;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public DenominationMaster getDenominationMaster() {
		return denominationMaster;
	}

	public void setDenominationMaster(DenominationMaster denominationMaster) {
		this.denominationMaster = denominationMaster;
	}

	public String getStrEmail() {
		return strEmail;
	}

	public void setStrEmail(String strEmail) {
		this.strEmail = strEmail;
	}

	public String getUserLinkedGLAccountType() {
		return userLinkedGLAccountType;
	}

	public void setUserLinkedGLAccountType(String userLinkedGLAccountType) {
		this.userLinkedGLAccountType = userLinkedGLAccountType;
	}

	public String getStrNaration() {
		return strNaration;
	}

	public void setStrNaration(String strNaration) {
		this.strNaration = strNaration;
	}

	public AccountMaster getAccountMasterFromAccount() {
		return accountMasterFromAccount;
	}

	public void setAccountMasterFromAccount(AccountMaster accountMasterFromAccount) {
		this.accountMasterFromAccount = accountMasterFromAccount;
	}

	public AccountMaster getAccountMasterToAccount() {
		return accountMasterToAccount;
	}

	public void setAccountMasterToAccount(AccountMaster accountMasterToAccount) {
		this.accountMasterToAccount = accountMasterToAccount;
	}

	public String getMailMessage() {
		return mailMessage;
	}

	public void setMailMessage(String mailMessage) {
		this.mailMessage = mailMessage;
	}

	public String getUserLinkedGLAccountDescription() {
		return userLinkedGLAccountDescription;
	}

	public void setUserLinkedGLAccountDescription(String userLinkedGLAccountDescription) {
		this.userLinkedGLAccountDescription = userLinkedGLAccountDescription;
	}

	public String getGlTranType() {
		return glTranType;
	}

	public void setGlTranType(String glTranType) {
		this.glTranType = glTranType;
	}

	public AccountTypeMaster getAccountTypeMaster() {
		return accountTypeMaster;
	}

	public void setAccountTypeMaster(AccountTypeMaster accountTypeMaster) {
		this.accountTypeMaster = accountTypeMaster;
	}

	public String getGlTranMode() {
		return glTranMode;
	}

	public void setGlTranMode(String glTranMode) {
		this.glTranMode = glTranMode;
	}

	public String getFromAccountNo() {
		return fromAccountNo;
	}

	public void setFromAccountNo(String fromAccountNo) {
		this.fromAccountNo = fromAccountNo;
	}

	public String getToAccountNo() {
		return toAccountNo;
	}

	public void setToAccountNo(String toAccountNo) {
		this.toAccountNo = toAccountNo;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getStan() {
		return stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getStrMti() {
		return strMti;
	}

	public void setStrMti(String strMti) {
		this.strMti = strMti;
	}

	public ArrayList<String> getEmailIdList() {
		return emailIdList;
	}

	public void setEmailIdList(ArrayList<String> emailIdList) {
		this.emailIdList = emailIdList;
	}

	public String getProcessingCode() {
		return processingCode;
	}

	public void setProcessingCode(String processingCode) {
		this.processingCode = processingCode;
	}

	public ThirdPartyRequestParam getThirdPartyRequestParam() {
		return thirdPartyRequestParam;
	}

	public void setThirdPartyRequestParam(ThirdPartyRequestParam thirdPartyRequestParam) {
		this.thirdPartyRequestParam = thirdPartyRequestParam;
	}

	public CardAccountLinkage getCardAccountLinkage() {
		return cardAccountLinkage;
	}

	public void setCardAccountLinkage(CardAccountLinkage cardAccountLinkage) {
		this.cardAccountLinkage = cardAccountLinkage;
	}

	public ExternalServerRequestResponseModel getExternalServerRequestResponseModel() {
		return externalServerRequestResponseModel;
	}

	public void setExternalServerRequestResponseModel(
			ExternalServerRequestResponseModel externalServerRequestResponseModel) {
		this.externalServerRequestResponseModel = externalServerRequestResponseModel;
	}
	
	public String getStrDailyCumuTxnLimit() {
		return strDailyCumuTxnLimit;
	}

	public void setStrDailyCumuTxnLimit(String strDailyCumuTxnLimit) {
		this.strDailyCumuTxnLimit = strDailyCumuTxnLimit;
	}

	public String getStrCumBalanceLimit() {
		return strCumBalanceLimit;
	}

	public void setStrCumBalanceLimit(String strCumBalanceLimit) {
		this.strCumBalanceLimit = strCumBalanceLimit;
	}

	public String getStrUpdatedAmount() {
		return strUpdatedAmount;
	}

	public void setStrUpdatedAmount(String strUpdatedAmount) {
		this.strUpdatedAmount = strUpdatedAmount;
	}

	public AccountTranMaster getAccountTranMaster() {
		return accountTranMaster;
	}

	public void setAccountTranMaster(AccountTranMaster accountTranMaster) {
		this.accountTranMaster = accountTranMaster;
	}

	public TierAccountMaster getTierAccountMaster() {
		return tierAccountMaster;
	}

	public void setTierAccountMaster(TierAccountMaster tierAccountMaster) {
		this.tierAccountMaster = tierAccountMaster;
	}

	public String getSmsMessage() {
		return smsMessage;
	}

	public void setSmsMessage(String smsMessage) {
		this.smsMessage = smsMessage;
	}

	public List<String> getToNumbersListforSms() {
		return toNumbersListforSms;
	}

	public void setToNumbersListforSms(List<String> toNumbersListforSms) {
		this.toNumbersListforSms = toNumbersListforSms;
	}

	public TierAccountMaster getFromAccountTierAccountMaster() {
		return fromAccountTierAccountMaster;
	}

	public void setFromAccountTierAccountMaster(TierAccountMaster fromAccountTierAccountMaster) {
		this.fromAccountTierAccountMaster = fromAccountTierAccountMaster;
	}

	public TierAccountMaster getToAccountTierAccountMaster() {
		return ToAccountTierAccountMaster;
	}

	public void setToAccountTierAccountMaster(TierAccountMaster toAccountTierAccountMaster) {
		ToAccountTierAccountMaster = toAccountTierAccountMaster;
	}

	public AccountCreation getFromAccountCreation() {
		return fromAccountCreation;
	}

	public void setFromAccountCreation(AccountCreation fromAccountCreation) {
		this.fromAccountCreation = fromAccountCreation;
	}

	public AccountCreation getToAccountCreation() {
		return ToAccountCreation;
	}

	public void setToAccountCreation(AccountCreation toAccountCreation) {
		ToAccountCreation = toAccountCreation;
	}

	
	
	public MiddleWareRequestModel getMiddleWareRequestModel() {
		return middleWareRequestModel;
	}

	public void setMiddleWareRequestModel(MiddleWareRequestModel middleWareRequestModel) {
		this.middleWareRequestModel = middleWareRequestModel;
	}

	public NameEnquiryRequestResponseModel getNameEnquiryRequestResponseModel() {
		return nameEnquiryRequestResponseModel;
	}

	public void setNameEnquiryRequestResponseModel(NameEnquiryRequestResponseModel nameEnquiryRequestResponseModel) {
		this.nameEnquiryRequestResponseModel = nameEnquiryRequestResponseModel;
	}
	
	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getMontraTxnId() {
		return montraTxnId;
	}

	public void setMontraTxnId(String montraTxnId) {
		this.montraTxnId = montraTxnId;
	}

	public String getSrcTxnId() {
		return srcTxnId;
	}

	public void setSrcTxnId(String srcTxnId) {
		this.srcTxnId = srcTxnId;
	}

	public String getStrParticipantId() {
		return strParticipantId;
	}

	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}

	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getSystemTxnId() {
		return systemTxnId;
	}

	public void setSystemTxnId(String systemTxnId) {
		this.systemTxnId = systemTxnId;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public PullAccountModel getPullAccountModel() {
		return pullAccountModel;
	}

	public void setPullAccountModel(PullAccountModel pullAccountModel) {
		this.pullAccountModel = pullAccountModel;
	}

	public PoolAccountBalanceInfo getPoolAccountBalanceInfo() {
		return poolAccountBalanceInfo;
	}

	public void setPoolAccountBalanceInfo(PoolAccountBalanceInfo poolAccountBalanceInfo) {
		this.poolAccountBalanceInfo = poolAccountBalanceInfo;
	}

	public SecretCodeRequest getSecretCodeRequest() {
		return secretCodeRequest;
	}

	public void setSecretCodeRequest(SecretCodeRequest secretCodeRequest) {
		this.secretCodeRequest = secretCodeRequest;
	}

	public boolean isReversedTxn() {
		return isReversedTxn;
	}

	public void setReversedTxn(boolean isReversedTxn) {
		this.isReversedTxn = isReversedTxn;
	}
}
