package ams.cms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ams.cms.util.JsonDateDeserializer;
import ams.cms.util.JsonDateSerializer;

@JsonAutoDetect
@Entity
@Table(name = "account_type_master")
public class AccountTypeMaster implements Serializable 
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int strID;

	@Column(name = "participant_id")
	private String strParticipantId;

	@Column(name = "account_type")
	private String strAccountType;

	@Column(name = "description")
	private String strDescription;

	@Column(name = "account_number_length")
	private String strAccNumLength;

	@Column(name = "account_number_start_digit")
	private String strAccNumStartDigit;

	@Column(name = "last_account_number")
	private String strLastAccNumber;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "creation_date")
	private Date creationDate;
	
	@Column(name = "is_credit_type")
	private String strIsCreditType;
	
	@Column(name = "is_allow_wallet")
	private String strIsAllowWallet;
	
	@Column(name = "category_type")
	private String strCategoryType;
	
	@Column(name = "dormancy_periods_in_days")
	private String strDormancyPeriodsInDays;
	
	@Column(name = "allow_load_cash")
	private String strAllowLoadCash;
	
	@Column(name = "gl_account_type")
	private String strGLAccountType;
	
	@Column(name = "gl_account_no")
	private String strGLAccountNumber;
	
	@Column(name = "tax_type")
	private String strTaxType;
	
	@Column(name = "tax_value")
	private String strTaxVal;
	
	@Column(name = "status")
	private String strStatus;

	@Column(name = "created_by")
	private String strCreatedBy;
	
	@Column(name = "is_revolving_credit")
	private String strIsRevolvingCredit;
	
	@Column(name = "last_billing_cycle_date")
	private String strLastBillingCycleDate;
	
	@Column(name = "account_type_category")
	private String strAccountTypeCategory;
	
	@Column(name = "is_withdraw_allow_at_agent")
	private String strIsWithdrawAllowAtAgent;
	
	@Column(name = "is_deposit_allow_at_agent")
	private String strIsDepositAllowAtAgent;
	
	@Column(name = "is_automated")
	private String strIsAutomated;
	
	@Column(name = "nuban_type")
	private String strNubanType;
	
	@Column(name = "account_type_code")
	private String strAccountTypeCode;
	
	@Column(name = "nuban_serial_number")
	private String strNubanSerialNumber;
	
	@Column(name = "is_mcc_check")
	private String strIsMccCheck;
	
	@Column(name = "is_nuban_account")
	private String strIsNubanAccount;
	
	@Column(name = "is_point_account")
	private String strIsPointAccount;
	
	
	@Column(name = "is_multi_currency_support")
	private String isMultiCurrencySupport;
	
	@Column(name = "is_lrs")
	private String isLrs;
	
	@Column(name = "is_channel")
	private String isChannel;
	
	

	@Transient
	private String strSingleTxnLimit;
	
	@Transient
	private String strDailyTxnLimit;
	
	@Transient
	private String strMonthlyTxnLimit;
	
	@Transient
	private String strYearlyTxnLimit;
	
	@Transient
	private String strDailyCumulativeTxnLimit1;
	
	@Transient
	private String strCumulativeBalanceLimit1;
	
	@Transient
	private String strDailyCumulativeTxnLimit2;
	
	@Transient
	private String strCumulativeBalanceLimit2;
	
	@Transient
	private String strDailyCumulativeTxnLimit3;
	
	@Transient
	private String strCumulativeBalanceLimit3;
	
	@Transient
	private String strNubanCode;
	
	public int getStrID() {
		return strID;
	}

	public void setStrID(int strID) {
		this.strID = strID;
	}

	public String getStrParticipantId() {
		return strParticipantId;
	}

	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrDescription() {
		return strDescription;
	}

	public void setStrDescription(String strDescription) {
		this.strDescription = strDescription;
	}

	public String getStrAccNumLength() {
		return strAccNumLength;
	}

	public void setStrAccNumLength(String strAccNumLength) {
		this.strAccNumLength = strAccNumLength;
	}

	public String getStrAccNumStartDigit() {
		return strAccNumStartDigit;
	}

	public void setStrAccNumStartDigit(String strAccNumStartDigit) {
		this.strAccNumStartDigit = strAccNumStartDigit;
	}

	public String getStrLastAccNumber() {
		return strLastAccNumber;
	}

	public void setStrLastAccNumber(String strLastAccNumber) {
		this.strLastAccNumber = strLastAccNumber;
	}

	public String getStrStatus() {
		return strStatus;
	}

	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}

	public String getStrCreatedBy() {
		return strCreatedBy;
	}

	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public String getStrIsCreditType() {
		return strIsCreditType;
	}
	
	public void setStrIsCreditType(String strIsCreditType) {
		this.strIsCreditType = strIsCreditType;
	}
	
	public String getStrIsAllowWallet() {
		return strIsAllowWallet;
	}
	public void setStrIsAllowWallet(String strIsAllowWallet) {
		this.strIsAllowWallet = strIsAllowWallet;
	}
	
	public String getStrCategoryType() {
		return strCategoryType;
	}
	
	public void setStrCategoryType(String strCategoryType) {
		this.strCategoryType = strCategoryType;
	}

	public String getStrDormancyPeriodsInDays() {
		return strDormancyPeriodsInDays;
	}
	
	public void setStrDormancyPeriodsInDays(String strDormancyPeriodsInDays) {
		this.strDormancyPeriodsInDays = strDormancyPeriodsInDays;
	}

	public String getStrAllowLoadCash() {
		return strAllowLoadCash;
	}

	public void setStrAllowLoadCash(String strAllowLoadCash) {
		this.strAllowLoadCash = strAllowLoadCash;
	}

	public String getStrTaxType() {
		return strTaxType;
	}

	public void setStrTaxType(String strTaxType) {
		this.strTaxType = strTaxType;
	}

	public String getStrTaxVal() {
		return strTaxVal;
	}

	public void setStrTaxVal(String strTaxVal) {
		this.strTaxVal = strTaxVal;
	}

	public String getStrSingleTxnLimit() {
		return strSingleTxnLimit;
	}

	public void setStrSingleTxnLimit(String strSingleTxnLimit) {
		this.strSingleTxnLimit = strSingleTxnLimit;
	}

	public String getStrDailyTxnLimit() {
		return strDailyTxnLimit;
	}

	public void setStrDailyTxnLimit(String strDailyTxnLimit) {
		this.strDailyTxnLimit = strDailyTxnLimit;
	}

	public String getStrMonthlyTxnLimit() {
		return strMonthlyTxnLimit;
	}

	public void setStrMonthlyTxnLimit(String strMonthlyTxnLimit) {
		this.strMonthlyTxnLimit = strMonthlyTxnLimit;
	}

	public String getStrYearlyTxnLimit() {
		return strYearlyTxnLimit;
	}

	public void setStrYearlyTxnLimit(String strYearlyTxnLimit) {
		this.strYearlyTxnLimit = strYearlyTxnLimit;
	}

	public String getStrIsRevolvingCredit() {
		return strIsRevolvingCredit;
	}

	public void setStrIsRevolvingCredit(String strIsRevolvingCredit) {
		this.strIsRevolvingCredit = strIsRevolvingCredit;
	}

	public String getStrLastBillingCycleDate() {
		return strLastBillingCycleDate;
	}

	public void setStrLastBillingCycleDate(String strLastBillingCycleDate) {
		this.strLastBillingCycleDate = strLastBillingCycleDate;
	}

	public String getStrAccountTypeCategory() {
		return strAccountTypeCategory;
	}

	public void setStrAccountTypeCategory(String strAccountTypeCategory) {
		this.strAccountTypeCategory = strAccountTypeCategory;
	}

	public String getStrGLAccountType() {
		return strGLAccountType;
	}

	public void setStrGLAccountType(String strGLAccountType) {
		this.strGLAccountType = strGLAccountType;
	}

	public String getStrGLAccountNumber() {
		return strGLAccountNumber;
	}

	public void setStrGLAccountNumber(String strGLAccountNumber) {
		this.strGLAccountNumber = strGLAccountNumber;
	}

	public String getStrIsWithdrawAllowAtAgent() {
		return strIsWithdrawAllowAtAgent;
	}

	public void setStrIsWithdrawAllowAtAgent(String strIsWithdrawAllowAtAgent) {
		this.strIsWithdrawAllowAtAgent = strIsWithdrawAllowAtAgent;
	}

	public String getStrIsDepositAllowAtAgent() {
		return strIsDepositAllowAtAgent;
	}

	public void setStrIsDepositAllowAtAgent(String strIsDepositAllowAtAgent) {
		this.strIsDepositAllowAtAgent = strIsDepositAllowAtAgent;
	}

	public String getStrDailyCumulativeTxnLimit1() {
		return strDailyCumulativeTxnLimit1;
	}

	public void setStrDailyCumulativeTxnLimit1(String strDailyCumulativeTxnLimit1) {
		this.strDailyCumulativeTxnLimit1 = strDailyCumulativeTxnLimit1;
	}

	public String getStrCumulativeBalanceLimit1() {
		return strCumulativeBalanceLimit1;
	}

	public void setStrCumulativeBalanceLimit1(String strCumulativeBalanceLimit1) {
		this.strCumulativeBalanceLimit1 = strCumulativeBalanceLimit1;
	}

	public String getStrDailyCumulativeTxnLimit2() {
		return strDailyCumulativeTxnLimit2;
	}

	public void setStrDailyCumulativeTxnLimit2(String strDailyCumulativeTxnLimit2) {
		this.strDailyCumulativeTxnLimit2 = strDailyCumulativeTxnLimit2;
	}

	public String getStrCumulativeBalanceLimit2() {
		return strCumulativeBalanceLimit2;
	}

	public void setStrCumulativeBalanceLimit2(String strCumulativeBalanceLimit2) {
		this.strCumulativeBalanceLimit2 = strCumulativeBalanceLimit2;
	}

	public String getStrDailyCumulativeTxnLimit3() {
		return strDailyCumulativeTxnLimit3;
	}

	public void setStrDailyCumulativeTxnLimit3(String strDailyCumulativeTxnLimit3) {
		this.strDailyCumulativeTxnLimit3 = strDailyCumulativeTxnLimit3;
	}

	public String getStrCumulativeBalanceLimit3() {
		return strCumulativeBalanceLimit3;
	}

	public void setStrCumulativeBalanceLimit3(String strCumulativeBalanceLimit3) {
		this.strCumulativeBalanceLimit3 = strCumulativeBalanceLimit3;
	}

	public String getStrIsAutomated() {
		return strIsAutomated;
	}

	public void setStrIsAutomated(String strIsAutomated) {
		this.strIsAutomated = strIsAutomated;
	}

	public String getStrNubanType() {
		return strNubanType;
	}

	public void setStrNubanType(String strNubanType) {
		this.strNubanType = strNubanType;
	}

	public String getStrAccountTypeCode() {
		return strAccountTypeCode;
	}

	public void setStrAccountTypeCode(String strAccountTypeCode) {
		this.strAccountTypeCode = strAccountTypeCode;
	}

	public String getStrNubanSerialNumber() {
		return strNubanSerialNumber;
	}

	public void setStrNubanSerialNumber(String strNubanSerialNumber) {
		this.strNubanSerialNumber = strNubanSerialNumber;
	}

	public String getStrIsMccCheck() {
		return strIsMccCheck;
	}

	public void setStrIsMccCheck(String strIsMccCheck) {
		this.strIsMccCheck = strIsMccCheck;
	}

	public String getStrIsNubanAccount() {
		return strIsNubanAccount;
	}

	public void setStrIsNubanAccount(String strIsNubanAccount) {
		this.strIsNubanAccount = strIsNubanAccount;
	}

	public String getStrIsPointAccount() {
		return strIsPointAccount;
	}

	public void setStrIsPointAccount(String strIsPointAccount) {
		this.strIsPointAccount = strIsPointAccount;
	}

	public String getStrNubanCode() {
		return strNubanCode;
	}

	public void setStrNubanCode(String strNubanCode) {
		this.strNubanCode = strNubanCode;
	}

	

	public String getIsMultiCurrencySupport() {
		return isMultiCurrencySupport;
	}

	public void setIsMultiCurrencySupport(String isMultiCurrencySupport) {
		this.isMultiCurrencySupport = isMultiCurrencySupport;
	}

	public String getIsLrs() {
		return isLrs;
	}

	public void setIsLrs(String isLrs) {
		this.isLrs = isLrs;
	}

	public String getIsChannel() {
		return isChannel;
	}

	public void setIsChannel(String isChannel) {
		this.isChannel = isChannel;
	}

	
	
	

	@Override
	public String toString() {
		return "AccountTypeMaster [strID=" + strID + ", strParticipantId=" + strParticipantId + ", strAccountType="
				+ strAccountType + ", strDescription=" + strDescription + ", strAccNumLength=" + strAccNumLength
				+ ", strAccNumStartDigit=" + strAccNumStartDigit + ", strLastAccNumber=" + strLastAccNumber
				+ ", creationDate=" + creationDate + ", strIsCreditType=" + strIsCreditType + ", strIsAllowWallet="
				+ strIsAllowWallet + ", strCategoryType=" + strCategoryType + ", strDormancyPeriodsInDays="
				+ strDormancyPeriodsInDays + ", strAllowLoadCash=" + strAllowLoadCash + ", strGLAccountType="
				+ strGLAccountType + ", strGLAccountNumber=" + strGLAccountNumber + ", strTaxType=" + strTaxType
				+ ", strTaxVal=" + strTaxVal + ", strStatus=" + strStatus + ", strCreatedBy=" + strCreatedBy
				+ ", strIsRevolvingCredit=" + strIsRevolvingCredit + ", strLastBillingCycleDate="
				+ strLastBillingCycleDate + ", strAccountTypeCategory=" + strAccountTypeCategory
				+ ", strIsWithdrawAllowAtAgent=" + strIsWithdrawAllowAtAgent + ", strIsDepositAllowAtAgent="
				+ strIsDepositAllowAtAgent + ", strIsAutomated=" + strIsAutomated + ", strNubanType=" + strNubanType
				+ ", strAccountTypeCode=" + strAccountTypeCode + ", strNubanSerialNumber=" + strNubanSerialNumber
				+ ", strSingleTxnLimit=" + strSingleTxnLimit + ", strDailyTxnLimit=" + strDailyTxnLimit
				+ ", strMonthlyTxnLimit=" + strMonthlyTxnLimit + ", strYearlyTxnLimit=" + strYearlyTxnLimit
				+ ", strDailyCumulativeTxnLimit1=" + strDailyCumulativeTxnLimit1 + ", strCumulativeBalanceLimit1="
				+ strCumulativeBalanceLimit1 + ", strDailyCumulativeTxnLimit2=" + strDailyCumulativeTxnLimit2
				+ ", strCumulativeBalanceLimit2=" + strCumulativeBalanceLimit2 + ", strDailyCumulativeTxnLimit3="
				+ strDailyCumulativeTxnLimit3 + ", strCumulativeBalanceLimit3=" + strCumulativeBalanceLimit3 + "]";
	}

	
}
