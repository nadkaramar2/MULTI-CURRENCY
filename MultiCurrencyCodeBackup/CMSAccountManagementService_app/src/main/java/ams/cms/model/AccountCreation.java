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

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ams.cms.util.JsonDateDeserializer;
import ams.cms.util.JsonDateSerializer;

@JsonAutoDetect
@Entity
@DynamicUpdate
@Table(name = "account_master")
public class AccountCreation implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "participant_id")
	private String strParticipantID;
	
	@Column(name = "cust_id")
	private String strCustId;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "account_number")
	private String strAccountNumber;
	
	@Column(name = "title")
	private String strTitle;
	
	@Column(name = "first_name")
	private String strFirstName;
	
	@Column(name = "middle_name")
	private String strMiddleName;
	
	@Column(name = "last_name")
	private String strLastName;
	
	@Column(name = "gender")
	private String strGender;	
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "dob")
	private Date birthDate;
	
	@Column(name = "email")
	private String strEmailID;
	
	@Column(name = "mobile_no")
	private String strMobileNo;
	
	@Column(name = "address1")
	private String strAddress1;
	
	@Column(name = "address2")
	private String strAddress2;
	
	@Column(name = "address3")
	private String strAddress3;
	
	@Column(name = "pincode")
	private String strPinCode;
	
	@Column(name = "city")
	private String strCity;
	
	@Column(name = "state")
	private String strState;
	
	@Column(name = "country")
	private String strCountry;	
	
	@Column(name = "phone_no")
	private String strPhoneNo;		
	
	@Column(name = "created_by")
	private String strCreatedBy;
	
	@Column(name = "status")
	private String strStatus;
	
	@Column(name = "is_instant_account")
	private String strIsInstantAccount;
	
	@Column(name = "opening_balance")
	private String strOpeningBalance;
	
	@Column(name = "closing_balance")
	private String strClosingBalance;
	
	@Column(name = "credit_limit_category")
	private String strCreditLimitCategory;
	
	@Column(name = "credit_limit_amount")
	private String strCreditLimitAmount;
	
	@Column(name = "available_credit_limit")
	private String strAvailableCreditLimit;
	
	@Column(name = "load_count")
	private String strLoadCount;
	
	@Column(name = "available_daily_limit")
	private String strAvailableDailyLimit;
	
	@Column(name = "available_monthly_limit")
	private String strAvailableMonthlyLimit;
	
	@Column(name = "available_yearly_limit")
	private String strAvailableYearlyLimit;
	
	@Column(name = "total_outstanding_balance")
	private String strTotalOutstandingBal;
	
	@Column(name = "total_outstanding_interest")
	private String strTotalOutstandingInterest;
	
	@Column(name="total_available_grace_period")
	private String strAvailableGracePeriod;
	
	@Column(name="grace_period_start_date")
	private Date gracePeriodStartDate;
	
	@Column(name="payement_due_date")
	private Date payementDueDate;
	
	@Column(name="total_calculated_gst_amount")
	private String strTotalCalGstAmount;
	
	@Column(name="last_total_amount_clear_date")
	private String strTotalAmountClearDate;
	
	@Column(name="date_of_account_closure")
	private String strDateOfAccountClosure;
	
	@Column(name="date_of_dormancy")
	private String strDateOfDormancy;
	
	@Column(name="Is_linked_with_card")
	private String strIsLinkedwithCard;
	
	@Column(name="country_code")
	private String strPhoneCode;
	
	@Column(name="notify_by_payment_date")
	private Date notifyByPaymentDate;
	
	@Column(name="qr_code_data")
	private String strQrCodeData;
	
	@Column(name="qr_code_file_path")
	private String strQrCodeFilePath;
	
	@Column(name="qr_code_image_url")
	private String qrCodeImageUrl;
	
	@JsonProperty("strPerTxnLimit")
	@Column(name="per_txn_limit")
	private int strSingleTxnLimit;
	
	@Column(name="daily_txn_limit")
	private int strDailyTxnLimit;
	
	@Column(name="monthly_txn_limit")
	private String strMonthlyTxnLimit;
	
	@Column(name="yearly_txn_limit")
	private String strYearlyTxnLimit;
	
	@Column(name = "ear_mark_amount")
	private Double strEarMarkAmount;
	
	@Column(name = "pre_cred_amount")
	private Double strPreCredAmount;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "creation_date")
	private Date strDateOfCreation;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "last_successfull_txn_date")
	private Date lastTxnDate;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "dormant_marked_date")
	private Date dormantMarkedDate;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "dormant_released_date")
	private Date dormantReleasedDate;	
	
	@Column(name = "currency_code")
	private String currencyCode;
	
	
	@Transient
	private String strGracePeriodStartDate;
	
	@Transient
	private String strDOB;
	
	@Transient
	private String strAddressProofDocumentId;
	
	@Transient
	private String strAddressProofDocumentValue;
	
	@Transient
	private String strIdentityProofDocumentId;
	
	@Transient
	private String strIdentityProofDocumentValue;
	
	@Transient
	private String strIsCreditType;
	
	@Transient
	private String strKycUpdateRequired;
	
	@Transient
	private String strAccountIssueDate;
    
	@Transient
	private String strLastloginDate;
	
	@Transient
	private String strAccountHolderName;
	
	@Transient
	private String accountCategoryType;

	@Transient
	private String availableBalance;
	
	@Transient
	private String strIsRevolvingCredit;
	
	@Transient
	private String mccWiseGracePeriodInDays;
	
	@Transient
	private String revolvingGracePeriodInDays;
	
	@Transient
	private String strAccounTypeStatus;
	
	@Transient
	private String strRevolvingCredit;
	
	@Transient
	private String strBillingCycleDate;
	
	@Transient
	private String strGLAccountType;
	
	@Transient
	private String strGLAccountNo;
	
	@Transient
	private String strChargeType;	
	
	@Transient
	private String strAmount;	
	
	@Transient
	private String fromDate;
	
	@Transient
	private String toDate;
	
	@Transient
	private String strDateOfRegistration;
	
	@Transient
	private String strTimeOfRegistration;

	@Transient
	private String strPaymentDueDate;
	
	@Transient
	private String strActiveTier;
	
	@Transient
	private String strTier1CummulativeBalance;
	
	@Transient
	private String strTier2CummulativeBalance;
	
	@Transient
	private String strTier3CummulativeBalance;
	
	@Transient
	private String strTier1DailyCumlimit;
	
	@Transient
	private String strTier2DailyCumlimit;
	
	@Transient
	private String strTier3DailyCumlimit;
	
	@Transient
	private String strAvailableTier1DailyCumlimit;
	
	@Transient
	private String strAvailableTier2DailyCumlimit;
	
	@Transient
	private String strAvailableTier3DailyCumlimit;
	
	@Transient
	private String cid;
	
	@Transient
	private String strGLAccountDescription;
	
	@Transient
	private String strGLAccountBalance;
	
	@Transient
	private String date;
	
	@Transient
	private Time time;
	
	@Transient
	private String bid;
	
	@Transient
	private String mccCode;
	
	@Transient
	private String strCustPin;
	
	@Transient
	private String reason;
	
	@Transient
	private String dormancyPeriodsIndays;
	
	@Transient
	private String isMultiCurrencySupport;
	
	@Transient
	private String isLRS;
	
	@Transient
	private String isChannel;
	
	@Transient
	private String baseCurrencyAccountType;
	
	@Transient
	private String description;
	
	@Transient
	private String strTier1PassportPhotograph;  //for image get
	
	
	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getStrParticipantID() {
		return strParticipantID;
	}

	public void setStrParticipantID(String strParticipantID) {
		this.strParticipantID = strParticipantID;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrAccountNumber() {
		return strAccountNumber;
	}

	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}

	public String getStrTitle() {
		return strTitle;
	}

	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
	}

	public String getStrFirstName() {
		return strFirstName;
	}

	public void setStrFirstName(String strFirstName) {
		this.strFirstName = strFirstName;
	}

	public String getStrMiddleName() {
		return strMiddleName;
	}

	public void setStrMiddleName(String strMiddleName) {
		this.strMiddleName = strMiddleName;
	}

	public String getStrLastName() {
		return strLastName;
	}

	public void setStrLastName(String strLastName) {
		this.strLastName = strLastName;
	}

	public String getStrGender() {
		return strGender;
	}

	public void setStrGender(String strGender) {
		this.strGender = strGender;
	}

	/*
	public Date getStrDOB() {
		return strDOB;
	}

	public void setStrDOB(Date strDOB) {
		this.strDOB = strDOB;
	}
	*/
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	public String getStrDOB() {
		return strDOB;
	}
	
	public void setStrDOB(String strDOB) {
		this.strDOB = strDOB;
	}

	public String getStrEmailID() {
		return strEmailID;
	}

	public void setStrEmailID(String strEmailID) {
		this.strEmailID = strEmailID;
	}

	public String getStrMobileNo() {
		return strMobileNo;
	}

	public void setStrMobileNo(String strMobileNo) {
		this.strMobileNo = strMobileNo;
	}

	public String getStrAddress1() {
		return strAddress1;
	}

	public void setStrAddress1(String strAddress1) {
		this.strAddress1 = strAddress1;
	}

	public String getStrAddress2() {
		return strAddress2;
	}

	public void setStrAddress2(String strAddress2) {
		this.strAddress2 = strAddress2;
	}

	public String getStrAddress3() {
		return strAddress3;
	}

	public void setStrAddress3(String strAddress3) {
		this.strAddress3 = strAddress3;
	}

	public String getStrPinCode() {
		return strPinCode;
	}

	public void setStrPinCode(String strPinCode) {
		this.strPinCode = strPinCode;
	}

	public String getStrCity() {
		return strCity;
	}

	public void setStrCity(String strCity) {
		this.strCity = strCity;
	}

	public String getStrState() {
		return strState;
	}

	public void setStrState(String strState) {
		this.strState = strState;
	}

	public String getStrCountry() {
		return strCountry;
	}

	public void setStrCountry(String strCountry) {
		this.strCountry = strCountry;
	}

	public String getStrPhoneNo() {
		return strPhoneNo;
	}

	public void setStrPhoneNo(String strPhoneNo) {
		this.strPhoneNo = strPhoneNo;
	}

	public Date getStrDateOfCreation() {
		return strDateOfCreation;
	}

	public void setStrDateOfCreation(Date strDateOfCreation) {
		this.strDateOfCreation = strDateOfCreation;
	}

	public String getStrCreatedBy() {
		return strCreatedBy;
	}

	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}

	public String getStrStatus() {
		return strStatus;
	}

	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}

	public String getStrIsInstantAccount() {
		return strIsInstantAccount;
	}

	public void setStrIsInstantAccount(String strIsInstantAccount) {
		this.strIsInstantAccount = strIsInstantAccount;
	}

	public String getStrOpeningBalance() {
		return strOpeningBalance;
	}

	public void setStrOpeningBalance(String strOpeningBalance) {
		this.strOpeningBalance = strOpeningBalance;
	}

	public String getStrClosingBalance() {
		return strClosingBalance;
	}

	public void setStrClosingBalance(String strClosingBalance) {
		this.strClosingBalance = strClosingBalance;
	}

	public String getStrCreditLimitCategory() {
		return strCreditLimitCategory;
	}

	public void setStrCreditLimitCategory(String strCreditLimitCategory) {
		this.strCreditLimitCategory = strCreditLimitCategory;
	}


	public String getStrCreditLimitAmount() {
		return strCreditLimitAmount;
	}

	public void setStrCreditLimitAmount(String strCreditLimitAmount) {
		this.strCreditLimitAmount = strCreditLimitAmount;
	}

	public String getStrAvailableCreditLimit() {
		return strAvailableCreditLimit;
	}

	public void setStrAvailableCreditLimit(String strAvailableCreditLimit) {
		this.strAvailableCreditLimit = strAvailableCreditLimit;
	}

	/*
	public String getStrTaxType() {
		return strTaxType;
	}
	public void setStrTaxType(String strTaxType) {
		this.strTaxType = strTaxType;
	}
	*/
	
	public String getStrLoadCount() {
		return strLoadCount;
	}

	public void setStrLoadCount(String strLoadCount) {
		this.strLoadCount = strLoadCount;
	}

	public String getStrAvailableDailyLimit() {
		return strAvailableDailyLimit;
	}

	public void setStrAvailableDailyLimit(String strAvailableDailyLimit) {
		this.strAvailableDailyLimit = strAvailableDailyLimit;
	}

	public String getStrAvailableMonthlyLimit() {
		return strAvailableMonthlyLimit;
	}

	public void setStrAvailableMonthlyLimit(String strAvailableMonthlyLimit) {
		this.strAvailableMonthlyLimit = strAvailableMonthlyLimit;
	}

	public String getStrAvailableYearlyLimit() {
		return strAvailableYearlyLimit;
	}

	public void setStrAvailableYearlyLimit(String strAvailableYearlyLimit) {
		this.strAvailableYearlyLimit = strAvailableYearlyLimit;
	}

	public String getStrTotalOutstandingBal() {
		return strTotalOutstandingBal;
	}

	public void setStrTotalOutstandingBal(String strTotalOutstandingBal) {
		this.strTotalOutstandingBal = strTotalOutstandingBal;
	}

	public String getStrTotalOutstandingInterest() {
		return strTotalOutstandingInterest;
	}

	public void setStrTotalOutstandingInterest(String strTotalOutstandingInterest) {
		this.strTotalOutstandingInterest = strTotalOutstandingInterest;
	}

	public String getStrAddressProofDocumentId() {
		return strAddressProofDocumentId;
	}

	public void setStrAddressProofDocumentId(String strAddressProofDocumentId) {
		this.strAddressProofDocumentId = strAddressProofDocumentId;
	}

	public String getStrAddressProofDocumentValue() {
		return strAddressProofDocumentValue;
	}

	public void setStrAddressProofDocumentValue(String strAddressProofDocumentValue) {
		this.strAddressProofDocumentValue = strAddressProofDocumentValue;
	}

	public String getStrIdentityProofDocumentId() {
		return strIdentityProofDocumentId;
	}

	public void setStrIdentityProofDocumentId(String strIdentityProofDocumentId) {
		this.strIdentityProofDocumentId = strIdentityProofDocumentId;
	}

	public String getStrIdentityProofDocumentValue() {
		return strIdentityProofDocumentValue;
	}

	public void setStrIdentityProofDocumentValue(String strIdentityProofDocumentValue) {
		this.strIdentityProofDocumentValue = strIdentityProofDocumentValue;
	}
	
	public String getStrIsCreditType() {
		return strIsCreditType;
	}
	public void setStrIsCreditType(String strIsCreditType) {
		this.strIsCreditType = strIsCreditType;
	}
									
	public String getStrIsLinkedwithCard() {
		return strIsLinkedwithCard;
	}
	public void setStrIsLinkedwithCard(String strIsLinkedwithCard) {
		this.strIsLinkedwithCard = strIsLinkedwithCard;
	}
	
	public String getStrKycUpdateRequired() {
		return strKycUpdateRequired;
	}
	public void setStrKycUpdateRequired(String strKycUpdateRequired) {
		this.strKycUpdateRequired = strKycUpdateRequired;
	}

	public String getStrAccountIssueDate() {
		return strAccountIssueDate;
	}

	public void setStrAccountIssueDate(String strAccountIssueDate) {
		this.strAccountIssueDate = strAccountIssueDate;
	}

	public String getStrLastloginDate() {
		return strLastloginDate;
	}

	public void setStrLastloginDate(String strLastloginDate) {
		this.strLastloginDate = strLastloginDate;
	}

	public String getStrAvailableGracePeriod() {
		return strAvailableGracePeriod;
	}

	public void setStrAvailableGracePeriod(String strAvailableGracePeriod) {
		this.strAvailableGracePeriod = strAvailableGracePeriod;
	}

	public String getStrGracePeriodStartDate() {
		return strGracePeriodStartDate;
	}

	public void setStrGracePeriodStartDate(String strGracePeriodStartDate) {
		this.strGracePeriodStartDate = strGracePeriodStartDate;
	}

	public String getStrTotalCalGstAmount() {
		return strTotalCalGstAmount;
	}

	public void setStrTotalCalGstAmount(String strTotalCalGstAmount) {
		this.strTotalCalGstAmount = strTotalCalGstAmount;
	}

	public String getStrTotalAmountClearDate() {
		return strTotalAmountClearDate;
	}

	public void setStrTotalAmountClearDate(String strTotalAmountClearDate) {
		this.strTotalAmountClearDate = strTotalAmountClearDate;
	}

	public String getStrDateOfAccountClosure() {
		return strDateOfAccountClosure;
	}

	public void setStrDateOfAccountClosure(String strDateOfAccountClosure) {
		this.strDateOfAccountClosure = strDateOfAccountClosure;
	}

	public String getStrDateOfDormancy() {
		return strDateOfDormancy;
	}

	public void setStrDateOfDormancy(String strDateOfDormancy) {
		this.strDateOfDormancy = strDateOfDormancy;
	}
	
	public String getStrCustId() {
		return strCustId;
	}

	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}

	public String getStrPhoneCode() {
		return strPhoneCode;
	}

	public void setStrPhoneCode(String strPhoneCode) {
		this.strPhoneCode = strPhoneCode;
	}

	public String getStrAccountHolderName() {
		return strAccountHolderName;
	}

	public void setStrAccountHolderName(String strAccountHolderName) {
		this.strAccountHolderName = strAccountHolderName;
	}

	public Date getPayementDueDate() {
		return payementDueDate;
	}

	public void setPayementDueDate(Date payementDueDate) {
		this.payementDueDate = payementDueDate;
	}

	public Date getNotifyByPaymentDate() {
		return notifyByPaymentDate;
	}

	public void setNotifyByPaymentDate(Date notifyByPaymentDate) {
		this.notifyByPaymentDate = notifyByPaymentDate;
	}
   
	public String getStrQrCodeData() {
		return strQrCodeData;
	}

	public void setStrQrCodeData(String strQrCodeData) {
		this.strQrCodeData = strQrCodeData;
	}

	public String getStrQrCodeFilePath() {
		return strQrCodeFilePath;
	}

	public void setStrQrCodeFilePath(String strQrCodeFilePath) {
		this.strQrCodeFilePath = strQrCodeFilePath;
	}

	public String getAccountCategoryType() {
		return accountCategoryType;
	}

	public void setAccountCategoryType(String accountCategoryType) {
		this.accountCategoryType = accountCategoryType;
	}

	public String getQrCodeImageUrl() {
		return qrCodeImageUrl;
	}

	public void setQrCodeImageUrl(String qrCodeImageUrl) {
		this.qrCodeImageUrl = qrCodeImageUrl;
	}

	public String getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getStrIsRevolvingCredit() {
		return strIsRevolvingCredit;
	}

	public void setStrIsRevolvingCredit(String strIsRevolvingCredit) {
		this.strIsRevolvingCredit = strIsRevolvingCredit;
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

	public Date getGracePeriodStartDate() {
		return gracePeriodStartDate;
	}

	public void setGracePeriodStartDate(Date gracePeriodStartDate) {
		this.gracePeriodStartDate = gracePeriodStartDate;
	}

	public String getStrAccounTypeStatus() {
		return strAccounTypeStatus;
	}

	public void setStrAccounTypeStatus(String strAccounTypeStatus) {
		this.strAccounTypeStatus = strAccounTypeStatus;
	}

	public String getStrRevolvingCredit() {
		return strRevolvingCredit;
	}

	public void setStrRevolvingCredit(String strRevolvingCredit) {
		this.strRevolvingCredit = strRevolvingCredit;
	}

	public String getStrBillingCycleDate() {
		return strBillingCycleDate;
	}

	public void setStrBillingCycleDate(String strBillingCycleDate) {
		this.strBillingCycleDate = strBillingCycleDate;
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

	public int getStrSingleTxnLimit() {
		return strSingleTxnLimit;
	}

	public void setStrSingleTxnLimit(int strSingleTxnLimit) {
		this.strSingleTxnLimit = strSingleTxnLimit;
	}

	public int getStrDailyTxnLimit() {
		return strDailyTxnLimit;
	}

	public void setStrDailyTxnLimit(int strDailyTxnLimit) {
		this.strDailyTxnLimit = strDailyTxnLimit;
	}

	public String getStrGLAccountType() {
		return strGLAccountType;
	}

	public void setStrGLAccountType(String strGLAccountType) {
		this.strGLAccountType = strGLAccountType;
	}

	public String getStrGLAccountNo() {
		return strGLAccountNo;
	}

	public void setStrGLAccountNo(String strGLAccountNo) {
		this.strGLAccountNo = strGLAccountNo;
	}

	public String getStrChargeType() {
		return strChargeType;
	}

	public void setStrChargeType(String strChargeType) {
		this.strChargeType = strChargeType;
	}

	public String getStrAmount() {
		return strAmount;
	}

	public void setStrAmount(String strAmount) {
		this.strAmount = strAmount;
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

	public String getStrDateOfRegistration() {
		return strDateOfRegistration;
	}

	public void setStrDateOfRegistration(String strDateOfRegistration) {
		this.strDateOfRegistration = strDateOfRegistration;
	}

	public String getStrTimeOfRegistration() {
		return strTimeOfRegistration;
	}

	public void setStrTimeOfRegistration(String strTimeOfRegistration) {
		this.strTimeOfRegistration = strTimeOfRegistration;
	}

	public String getStrPaymentDueDate() {
		return strPaymentDueDate;
	}

	public void setStrPaymentDueDate(String strPaymentDueDate) {
		this.strPaymentDueDate = strPaymentDueDate;
	}

	public Double getStrEarMarkAmount() {
		return strEarMarkAmount;
	}

	public void setStrEarMarkAmount(Double strEarMarkAmount) {
		this.strEarMarkAmount = strEarMarkAmount;
	}

	public Double getStrPreCredAmount() {
		return strPreCredAmount;
	}

	public void setStrPreCredAmount(Double strPreCredAmount) {
		this.strPreCredAmount = strPreCredAmount;
	}

	public String getStrActiveTier() {
		return strActiveTier;
	}

	public void setStrActiveTier(String strActiveTier) {
		this.strActiveTier = strActiveTier;
	}

	public String getStrTier1CummulativeBalance() {
		return strTier1CummulativeBalance;
	}

	public void setStrTier1CummulativeBalance(String strTier1CummulativeBalance) {
		this.strTier1CummulativeBalance = strTier1CummulativeBalance;
	}

	public String getStrTier2CummulativeBalance() {
		return strTier2CummulativeBalance;
	}

	public void setStrTier2CummulativeBalance(String strTier2CummulativeBalance) {
		this.strTier2CummulativeBalance = strTier2CummulativeBalance;
	}

	public String getStrTier3CummulativeBalance() {
		return strTier3CummulativeBalance;
	}

	public void setStrTier3CummulativeBalance(String strTier3CummulativeBalance) {
		this.strTier3CummulativeBalance = strTier3CummulativeBalance;
	}

	public String getStrTier1DailyCumlimit() {
		return strTier1DailyCumlimit;
	}

	public void setStrTier1DailyCumlimit(String strTier1DailyCumlimit) {
		this.strTier1DailyCumlimit = strTier1DailyCumlimit;
	}

	public String getStrTier2DailyCumlimit() {
		return strTier2DailyCumlimit;
	}

	public void setStrTier2DailyCumlimit(String strTier2DailyCumlimit) {
		this.strTier2DailyCumlimit = strTier2DailyCumlimit;
	}

	public String getStrTier3DailyCumlimit() {
		return strTier3DailyCumlimit;
	}

	public void setStrTier3DailyCumlimit(String strTier3DailyCumlimit) {
		this.strTier3DailyCumlimit = strTier3DailyCumlimit;
	}

	public String getStrAvailableTier1DailyCumlimit() {
		return strAvailableTier1DailyCumlimit;
	}

	public void setStrAvailableTier1DailyCumlimit(String strAvailableTier1DailyCumlimit) {
		this.strAvailableTier1DailyCumlimit = strAvailableTier1DailyCumlimit;
	}

	public String getStrAvailableTier2DailyCumlimit() {
		return strAvailableTier2DailyCumlimit;
	}

	public void setStrAvailableTier2DailyCumlimit(String strAvailableTier2DailyCumlimit) {
		this.strAvailableTier2DailyCumlimit = strAvailableTier2DailyCumlimit;
	}

	public String getStrAvailableTier3DailyCumlimit() {
		return strAvailableTier3DailyCumlimit;
	}

	public void setStrAvailableTier3DailyCumlimit(String strAvailableTier3DailyCumlimit) {
		this.strAvailableTier3DailyCumlimit = strAvailableTier3DailyCumlimit;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getStrGLAccountDescription() {
		return strGLAccountDescription;
	}

	public void setStrGLAccountDescription(String strGLAccountDescription) {
		this.strGLAccountDescription = strGLAccountDescription;
	}

	public String getStrGLAccountBalance() {
		return strGLAccountBalance;
	}

	public void setStrGLAccountBalance(String strGLAccountBalance) {
		this.strGLAccountBalance = strGLAccountBalance;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getMccCode() {
		return mccCode;
	}

	public void setMccCode(String mccCode) {
		this.mccCode = mccCode;
	}

	public Date getLastTxnDate() {
		return lastTxnDate;
	}

	public void setLastTxnDate(Date lastTxnDate) {
		this.lastTxnDate = lastTxnDate;
	}

	public Date getDormantMarkedDate() {
		return dormantMarkedDate;
	}

	public void setDormantMarkedDate(Date dormantMarkedDate) {
		this.dormantMarkedDate = dormantMarkedDate;
	}

	public Date getDormantReleasedDate() {
		return dormantReleasedDate;
	}

	public void setDormantReleasedDate(Date dormantReleasedDate) {
		this.dormantReleasedDate = dormantReleasedDate;
	}

	public String getStrCustPin() {
		return strCustPin;
	}

	public void setStrCustPin(String strCustPin) {
		this.strCustPin = strCustPin;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getDormancyPeriodsIndays() {
		return dormancyPeriodsIndays;
	}

	public void setDormancyPeriodsIndays(String dormancyPeriodsIndays) {
		this.dormancyPeriodsIndays = dormancyPeriodsIndays;
	}

	public String getIsMultiCurrencySupport() {
		return isMultiCurrencySupport;
	}

	public void setIsMultiCurrencySupport(String isMultiCurrencySupport) {
		this.isMultiCurrencySupport = isMultiCurrencySupport;
	}

	public String getIsLRS() {
		return isLRS;
	}

	public void setIsLRS(String isLRS) {
		this.isLRS = isLRS;
	}

	public String getIsChannel() {
		return isChannel;
	}

	public void setIsChannel(String isChannel) {
		this.isChannel = isChannel;
	}

	public String getBaseCurrencyAccountType() {
		return baseCurrencyAccountType;
	}

	public void setBaseCurrencyAccountType(String baseCurrencyAccountType) {
		this.baseCurrencyAccountType = baseCurrencyAccountType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStrTier1PassportPhotograph() {
		return strTier1PassportPhotograph;
	}

	public void setStrTier1PassportPhotograph(String strTier1PassportPhotograph) {
		this.strTier1PassportPhotograph = strTier1PassportPhotograph;
	}

	
	
	
	
}
