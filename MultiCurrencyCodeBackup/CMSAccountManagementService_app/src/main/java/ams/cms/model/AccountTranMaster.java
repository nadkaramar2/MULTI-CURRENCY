package ams.cms.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

//import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
//@DynamicInsert
@Entity
@Table(name = "account_tran_master")
public class AccountTranMaster implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "txn_id")
	private String strTxn_id;
	
	@Column(name = "sys_id")
	private String strSys_id;
	
	@Column(name = "participant_id")
	private String strParticipantId;
	
	@JsonProperty("strLocal_tran_date")
	@Column(name = "local_tran_date")
	private Date strLocal_tran_date;
	//private String strLocal_tran_date;
	
	@JsonProperty("strLocal_tran_time")
	@Column(name = "local_tran_time")
	private Time strLocal_tran_time;
	//private String strLocal_tran_time;
	
//	@Column(name = "local_tran_date")
//	private String strtxnId;
	
	@Column(name = "mti")
	private String strMti;
	
	@Column(name = "tran_type")
	private String strTran_type;
	
	@Column(name = "processing_code")
	private String strProcessingCode;
	
	@Column(name = "transaction_amount")
	private String strTransaction_amount;
	
	@Column(name = "response_code")
	private String strResponseCode;
	
	@Column(name = "auth_code")
	private String strAuthCode;
	
	@Column(name = "source_txn_id")
	private String strSrcTxnId;
	
	@Column(name = "rrn")
	private String strRRN;
	
	@Column(name = "stan")
	private String strStan;
	
	@Column(name = "mid")
	private String strMid;
	
	@Column(name = "tid")
	private String strTID;
	
	@Column(name = "card_acceptor_id")
	private String strCrdAcceptorId;
	
	@Column(name = "card_acceptor_name")
	private String strCrdAccptorLoc;
	
	@Column(name = "mcc")
	private String strMcc;
	
	@Column(name = "acquirer_inst_id")
	private String strAcquiringInstCode;
	
	@Column(name = "pos_condition_code")
	private String strPosCondCode;
	
	@Column(name = "account_no")
	private String strAccountNumber;
	
	@Column(name = "ency_acc_no")
	private String strEcnAccountNumber;
	
	@Column(name = "vpa_id")
	private String strVpaId;
	
	@Column(name = "from_account_number")
	private String strFrom_account_number;
	
	@Column(name = "switch_txn_date")
	private Date switchTxDate;
	
	@Column(name = "enc_from_acc_no")
	private String strEncFrmAccntNo;
	
	@Column(name = "to_account_number")
	private String strTo_account_number;
	
	@Column(name = "enc_to_acc_no")
	private String strEncToAccntNo;
	
	@Column(name = "currency_code")
	private String strCurrncyCode;
	
	@Column(name = "pos_data_code")
	private String strPosDataCode;
	
	@Column(name = "product_code")
	private String strProductCode;
	
	@Column(name = "fraud_score")
	private String strFraudScore;
	
	@Column(name = "reserve_field1")
	private String strReservefield1;
	
	@Column(name = "reserve_field2")
	private String strReservefield2;
	
	@Column(name = "reserve_field3")
	private String strReservefield3;
	
	@Column(name = "reserve_field4")
	private String strReservefield4;
	
	@Column(name = "reserve_field5")
	private String strReservefield5;
	
	@Column(name = "audit_id")
	private String auditId;
	
	@Transient
	private String strAccountType;
	
	@Transient
	private String fromDate;
	
	@Transient
	private String toDate;
	
	@Transient
	private String strClosingBalance;
	
	@Transient
	private String receipentAccountNo;
	
	@Transient
	private String senderAccountNo;
	
	@Transient
	private String strPIN;
	
	@Transient
	private String strCustId;
	
	@Transient
	private String strSenderAccountType;
	
	@Transient
	private String strReceipentAccountType;
	
	@Transient
	private String strSenderClosingBalance;
	
	@Transient
	private String strReceipentClosingBalance;
	
	@Transient
	private String strSwitch_txn_date;
	
	@Transient
	private String strAccountCategory;
	
	@Transient
	private String strAvailableCreditLimit;
	
	@Transient
	private String strIsRevolvingCredit;
	
	@Transient
	private String accountCategoryType;
	
	@Transient
	private String strTotalOutstandingBal;
	
	@Transient
	private String mccWiseGracePeriodInDays;
	
	@Transient
	private String revolvingGracePeriodInDays;
	
	@Transient
	private String strBillingCycleDate;
	
	@Transient
	private String dynamicQrRefNo;
	
	@Transient
	private String txn_Date;
	
	@Transient
	private String txn_Time;

	@Transient
	private String strTxnTypeKeyWord;
	
	@Transient 
	private AccountCreation accountCreation;
	
	@Transient
	private String cid;
	
	@Transient
	private String bid;
	
	@Transient
	private String montraTxnId;
	
	@Transient
	private String secretCode;
	
	@Transient
	private String feeType;
	
	@Transient
	private String feeApplicableTo;
	
	@Transient
	private String fee;
	
	@Transient
	private String vat;
	
	@Transient
	private String custId;
	
	@Transient
	private String txnType;
	
	@Transient
	private String beneficiaryBankCode;
	
	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getStrTxn_id() {
		return strTxn_id;
	}

	public void setStrTxn_id(String strTxn_id) {
		this.strTxn_id = strTxn_id;
	}

	public String getStrSys_id() {
		return strSys_id;
	}

	public void setStrSys_id(String strSys_id) {
		this.strSys_id = strSys_id;
	}

	public String getStrParticipantId() {
		return strParticipantId;
	}

	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}
	/*
	public String getStrLocal_tran_date() {
		return strLocal_tran_date;
	}

	public void setStrLocal_tran_date(String strLocal_tran_date) {
		this.strLocal_tran_date = strLocal_tran_date;
	}

	public String getStrLocal_tran_time() {
		return strLocal_tran_time;
	}

	public void setStrLocal_tran_time(String strLocal_tran_time) {
		this.strLocal_tran_time = strLocal_tran_time;
	}
	*/

	public Date getStrLocal_tran_date() {
		return strLocal_tran_date;
	}

	public void setStrLocal_tran_date(Date strLocal_tran_date) {
		this.strLocal_tran_date = strLocal_tran_date;
	}

	public Time getStrLocal_tran_time() {
		return strLocal_tran_time;
	}

	public void setStrLocal_tran_time(Time strLocal_tran_time) {
		this.strLocal_tran_time = strLocal_tran_time;
	}

	public String getStrMti() {
		return strMti;
	}

	public void setStrMti(String strMti) {
		this.strMti = strMti;
	}

	public String getStrTran_type() {
		return strTran_type;
	}

	public void setStrTran_type(String strTran_type) {
		this.strTran_type = strTran_type;
	}

	public String getStrProcessingCode() {
		return strProcessingCode;
	}

	public void setStrProcessingCode(String strProcessingCode) {
		this.strProcessingCode = strProcessingCode;
	}

	public String getStrTransaction_amount() {
		return strTransaction_amount;
	}

	public void setStrTransaction_amount(String strTransaction_amount) {
		this.strTransaction_amount = strTransaction_amount;
	}

	public String getStrResponseCode() {
		return strResponseCode;
	}

	public void setStrResponseCode(String strResponseCode) {
		this.strResponseCode = strResponseCode;
	}

	public String getStrAuthCode() {
		return strAuthCode;
	}

	public void setStrAuthCode(String strAuthCode) {
		this.strAuthCode = strAuthCode;
	}

	public String getStrSrcTxnId() {
		return strSrcTxnId;
	}

	public void setStrSrcTxnId(String strSrcTxnId) {
		this.strSrcTxnId = strSrcTxnId;
	}

	public String getStrRRN() {
		return strRRN;
	}

	public void setStrRRN(String strRRN) {
		this.strRRN = strRRN;
	}

	public String getStrStan() {
		return strStan;
	}

	public void setStrStan(String strStan) {
		this.strStan = strStan;
	}

	public String getStrMid() {
		return strMid;
	}

	public void setStrMid(String strMid) {
		this.strMid = strMid;
	}

	public String getStrTID() {
		return strTID;
	}

	public void setStrTID(String strTID) {
		this.strTID = strTID;
	}

	public String getStrCrdAcceptorId() {
		return strCrdAcceptorId;
	}

	public void setStrCrdAcceptorId(String strCrdAcceptorId) {
		this.strCrdAcceptorId = strCrdAcceptorId;
	}

	public String getStrCrdAccptorLoc() {
		return strCrdAccptorLoc;
	}

	public void setStrCrdAccptorLoc(String strCrdAccptorLoc) {
		this.strCrdAccptorLoc = strCrdAccptorLoc;
	}

	public String getStrMcc() {
		return strMcc;
	}

	public void setStrMcc(String strMcc) {
		this.strMcc = strMcc;
	}

	public String getStrAcquiringInstCode() {
		return strAcquiringInstCode;
	}

	public void setStrAcquiringInstCode(String strAcquiringInstCode) {
		this.strAcquiringInstCode = strAcquiringInstCode;
	}

	public String getStrPosCondCode() {
		return strPosCondCode;
	}

	public void setStrPosCondCode(String strPosCondCode) {
		this.strPosCondCode = strPosCondCode;
	}

	public String getStrAccountNumber() {
		return strAccountNumber;
	}

	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}

	public String getStrEcnAccountNumber() {
		return strEcnAccountNumber;
	}

	public void setStrEcnAccountNumber(String strEcnAccountNumber) {
		this.strEcnAccountNumber = strEcnAccountNumber;
	}

	public String getStrVpaId() {
		return strVpaId;
	}

	public void setStrVpaId(String strVpaId) {
		this.strVpaId = strVpaId;
	}

	
	public String getStrFrom_account_number() {
		return strFrom_account_number;
	}

	public void setStrFrom_account_number(String strFrom_account_number) {
		this.strFrom_account_number = strFrom_account_number;
	}

	public String getStrTo_account_number() {
		return strTo_account_number;
	}

	public void setStrTo_account_number(String strTo_account_number) {
		this.strTo_account_number = strTo_account_number;
	}

	public Date getSwitchTxDate() {
		return switchTxDate;
	}

	public void setSwitchTxDate(Date switchTxDate) {
		this.switchTxDate = switchTxDate;
	}

	public String getStrEncFrmAccntNo() {
		return strEncFrmAccntNo;
	}

	public void setStrEncFrmAccntNo(String strEncFrmAccntNo) {
		this.strEncFrmAccntNo = strEncFrmAccntNo;
	}

	public String getStrEncToAccntNo() {
		return strEncToAccntNo;
	}

	public void setStrEncToAccntNo(String strEncToAccntNo) {
		this.strEncToAccntNo = strEncToAccntNo;
	}

	public String getStrCurrncyCode() {
		return strCurrncyCode;
	}

	public void setStrCurrncyCode(String strCurrncyCode) {
		this.strCurrncyCode = strCurrncyCode;
	}

	public String getStrPosDataCode() {
		return strPosDataCode;
	}

	public void setStrPosDataCode(String strPosDataCode) {
		this.strPosDataCode = strPosDataCode;
	}

	public String getStrProductCode() {
		return strProductCode;
	}

	public void setStrProductCode(String strProductCode) {
		this.strProductCode = strProductCode;
	}

	public String getStrFraudScore() {
		return strFraudScore;
	}

	public void setStrFraudScore(String strFraudScore) {
		this.strFraudScore = strFraudScore;
	}

	public String getStrReservefield1() {
		return strReservefield1;
	}

	public void setStrReservefield1(String strReservefield1) {
		this.strReservefield1 = strReservefield1;
	}

	public String getStrReservefield2() {
		return strReservefield2;
	}

	public void setStrReservefield2(String strReservefield2) {
		this.strReservefield2 = strReservefield2;
	}

	public String getStrReservefield3() {
		return strReservefield3;
	}

	public void setStrReservefield3(String strReservefield3) {
		this.strReservefield3 = strReservefield3;
	}

	public String getStrReservefield4() {
		return strReservefield4;
	}

	public void setStrReservefield4(String strReservefield4) {
		this.strReservefield4 = strReservefield4;
	}

	public String getStrReservefield5() {
		return strReservefield5;
	}

	public void setStrReservefield5(String strReservefield5) {
		this.strReservefield5 = strReservefield5;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getStrClosingBalance() {
		return strClosingBalance;
	}

	public void setStrClosingBalance(String strClosingBalance) {
		this.strClosingBalance = strClosingBalance;
	}

	public String getReceipentAccountNo() {
		return receipentAccountNo;
	}

	public void setReceipentAccountNo(String receipentAccountNo) {
		this.receipentAccountNo = receipentAccountNo;
	}

	public String getSenderAccountNo() {
		return senderAccountNo;
	}

	public void setSenderAccountNo(String senderAccountNo) {
		this.senderAccountNo = senderAccountNo;
	}

	public String getStrPIN() {
		return strPIN;
	}

	public void setStrPIN(String strPIN) {
		this.strPIN = strPIN;
	}

	public String getStrCustId() {
		return strCustId;
	}

	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}

	public String getStrSenderAccountType() {
		return strSenderAccountType;
	}

	public void setStrSenderAccountType(String strSenderAccountType) {
		this.strSenderAccountType = strSenderAccountType;
	}

	public String getStrReceipentAccountType() {
		return strReceipentAccountType;
	}

	public void setStrReceipentAccountType(String strReceipentAccountType) {
		this.strReceipentAccountType = strReceipentAccountType;
	}

	public String getStrSenderClosingBalance() {
		return strSenderClosingBalance;
	}

	public void setStrSenderClosingBalance(String strSenderClosingBalance) {
		this.strSenderClosingBalance = strSenderClosingBalance;
	}

	public String getStrReceipentClosingBalance() {
		return strReceipentClosingBalance;
	}

	public void setStrReceipentClosingBalance(String strReceipentClosingBalance) {
		this.strReceipentClosingBalance = strReceipentClosingBalance;
	}

	public String getStrSwitch_txn_date() {
		return strSwitch_txn_date;
	}

	public void setStrSwitch_txn_date(String strSwitch_txn_date) {
		this.strSwitch_txn_date = strSwitch_txn_date;
	}

	public String getStrAccountCategory() {
		return strAccountCategory;
	}

	public void setStrAccountCategory(String strAccountCategory) {
		this.strAccountCategory = strAccountCategory;
	}

	public String getStrAvailableCreditLimit() {
		return strAvailableCreditLimit;
	}

	public void setStrAvailableCreditLimit(String strAvailableCreditLimit) {
		this.strAvailableCreditLimit = strAvailableCreditLimit;
	}

	public String getStrIsRevolvingCredit() {
		return strIsRevolvingCredit;
	}

	public void setStrIsRevolvingCredit(String strIsRevolvingCredit) {
		this.strIsRevolvingCredit = strIsRevolvingCredit;
	}

	public String getAccountCategoryType() {
		return accountCategoryType;
	}

	public void setAccountCategoryType(String accountCategoryType) {
		this.accountCategoryType = accountCategoryType;
	}

	public String getStrTotalOutstandingBal() {
		return strTotalOutstandingBal;
	}

	public void setStrTotalOutstandingBal(String strTotalOutstandingBal) {
		this.strTotalOutstandingBal = strTotalOutstandingBal;
	}

	public String getMccWiseGracePeriodInDays() {
		return mccWiseGracePeriodInDays;
	}

	public void setMccWiseGracePeriodInDays(String mccWiseGracePeriodInDays) {
		this.mccWiseGracePeriodInDays = mccWiseGracePeriodInDays;
	}

	public String getRevolvingGracePeriodInDays() {
		return revolvingGracePeriodInDays;
	}

	public void setRevolvingGracePeriodInDays(String revolvingGracePeriodInDays) {
		this.revolvingGracePeriodInDays = revolvingGracePeriodInDays;
	}

	public String getStrBillingCycleDate() {
		return strBillingCycleDate;
	}

	public void setStrBillingCycleDate(String strBillingCycleDate) {
		this.strBillingCycleDate = strBillingCycleDate;
	}

	public String getDynamicQrRefNo() {
		return dynamicQrRefNo;
	}

	public void setDynamicQrRefNo(String dynamicQrRefNo) {
		this.dynamicQrRefNo = dynamicQrRefNo;
	}

	public String getTxn_Date() {
		return txn_Date;
	}

	public void setTxn_Date(String txn_Date) {
		this.txn_Date = txn_Date;
	}

	public String getTxn_Time() {
		return txn_Time;
	}

	public void setTxn_Time(String txn_Time) {
		this.txn_Time = txn_Time;
	}

	public String getStrTxnTypeKeyWord() {
		return strTxnTypeKeyWord;
	}

	public void setStrTxnTypeKeyWord(String strTxnTypeKeyWord) {
		this.strTxnTypeKeyWord = strTxnTypeKeyWord;
	}

	public AccountCreation getAccountCreation() {
		return accountCreation;
	}

	public void setAccountCreation(AccountCreation accountCreation) {
		this.accountCreation = accountCreation;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getMontraTxnId() {
		return montraTxnId;
	}

	public void setMontraTxnId(String montraTxnId) {
		this.montraTxnId = montraTxnId;
	}

	public String getSecretCode() {
		return secretCode;
	}

	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getFeeApplicableTo() {
		return feeApplicableTo;
	}

	public void setFeeApplicableTo(String feeApplicableTo) {
		this.feeApplicableTo = feeApplicableTo;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getBeneficiaryBankCode() {
		return beneficiaryBankCode;
	}

	public void setBeneficiaryBankCode(String beneficiaryBankCode) {
		this.beneficiaryBankCode = beneficiaryBankCode;
	}
	
}
