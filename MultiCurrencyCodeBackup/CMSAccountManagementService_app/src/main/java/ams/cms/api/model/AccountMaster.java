package ams.cms.api.model;

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
/*
 * Account Master and Account Creation are same was throwing same name and same class exception so created a new One 
 * created by ankit
 * */


@JsonAutoDetect
@Entity
@Table(name = "account_master")
public class AccountMaster implements Serializable
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
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "creation_date")
	private Date strDateOfCreation;
	
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
	
	@Column(name = "tax_type")
	private String strTaxType;
	
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
	
	@Column(name="Is_linked_with_card")
	private String strIsLinkedwithCard;
	
	//Added by Sunny Soni Start
	@Column(name="total_available_grace_period")
	private String strAvailableGracePeriod;
	
	@Column(name="grace_period_start_date")
	private Date gracePeriodStartDate;
	
	@Column(name="payement_due_date")
	private Date payementDueDate;
	
	@Column(name="total_calculated_gst_amount")
	private String strTotalCalGstAmount;
	
	@Column(name="last_total_amount_clear_date")
	private Date lastTotalAmountClearDate;
	
	@Column(name="date_of_account_closure")
	private String strDateOfAccountClosure;
	
	@Column(name="date_of_dormancy")
	private Date dateOfDormancy;
	
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
	//Added by Sunny Soni End
	
	@Column(name="per_txn_limit")
	private int strSingleTxnLimit;
	
	@Column(name="daily_txn_limit")
	private int strDailyTxnLimit;
	
	@Column(name="monthly_txn_limit")
	private String strMonthlyTxnLimit;
	
	@Column(name="yearly_txn_limit")
	private String strYearlyTxnLimit;

	@Column(name = "pre_closure_date")
	private Date strPreClosureDate;
	
	@Column(name = "ear_mark_amount")
	private Double strEarMarkAmount;
	
	@Column(name = "pre_cred_amount")
	private Double strPreCredAmount;
	
	@Column(name = "currency_code")
	private String currencyCode;
	
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
	private String strReceipentAccountNo;
	
	@Transient
	private String strAccountTypeCategory;
	
	@Transient
	private String strAccounTypeStatus;
	
	@Transient
	private String strRevolvingCredit;
	
	@Transient
	private String strAllowLoadCash;
	
	@Transient
	private String strAccountCategory;
	
	@Transient
	private String strTxnAmount;	
	
	@Transient
	private String strPerDayAssignLimit;	
	
	@Transient
	private String strMonthlyAssignLimit;	
	
	@Transient
	private String strYearlyAssignLimit;	
	
	@Transient
	private String strSingleTxnMaxLimit;
	
	@Transient
	private String strPerDayMaxAssignedlimit;
	
	@Transient
	private String strMonthlyMaxAssignedLimit;
	
	@Transient
	private String strYearlyMaxAssignedLimit;
	
	@Transient
	private String strAccountHolderName;
	
	@Transient
	private String strIsWithdrawAllowAtAgent;
	
	@Transient
	private String strIsDepositAllowAtAgent;
	
	//add by Abhishek T
	@Transient
	private String strCreditMaxAssignedLimit;
	
	@Transient
	private String strTxnType;	
	
	@Transient
	private String strGLAccountType;
	
	@Transient
	private String strGLAccountNo;
	
	@Transient
	private int strPerTxnLimit;
		
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

	public String getStrTaxType() {
		return strTaxType;
	}

	public void setStrTaxType(String strTaxType) {
		this.strTaxType = strTaxType;
	}

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

	public String getStrCustId() {
		return strCustId;
	}

	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}

	public String getStrReceipentAccountNo() {
		return strReceipentAccountNo;
	}

	public void setStrReceipentAccountNo(String strReceipentAccountNo) {
		this.strReceipentAccountNo = strReceipentAccountNo;
	}

	public String getStrAccountTypeCategory() {
		return strAccountTypeCategory;
	}

	public void setStrAccountTypeCategory(String strAccountTypeCategory) {
		this.strAccountTypeCategory = strAccountTypeCategory;
	}

	public String getStrAvailableGracePeriod() {
		return strAvailableGracePeriod;
	}

	public void setStrAvailableGracePeriod(String strAvailableGracePeriod) {
		this.strAvailableGracePeriod = strAvailableGracePeriod;
	}

	public Date getPayementDueDate() {
		return payementDueDate;
	}

	public void setPayementDueDate(Date payementDueDate) {
		this.payementDueDate = payementDueDate;
	}

	public String getStrTotalCalGstAmount() {
		return strTotalCalGstAmount;
	}

	public void setStrTotalCalGstAmount(String strTotalCalGstAmount) {
		this.strTotalCalGstAmount = strTotalCalGstAmount;
	}

	public Date getLastTotalAmountClearDate() {
		return lastTotalAmountClearDate;
	}

	public void setLastTotalAmountClearDate(Date lastTotalAmountClearDate) {
		this.lastTotalAmountClearDate = lastTotalAmountClearDate;
	}

	public String getStrDateOfAccountClosure() {
		return strDateOfAccountClosure;
	}

	public void setStrDateOfAccountClosure(String strDateOfAccountClosure) {
		this.strDateOfAccountClosure = strDateOfAccountClosure;
	}

	public String getStrPhoneCode() {
		return strPhoneCode;
	}

	public void setStrPhoneCode(String strPhoneCode) {
		this.strPhoneCode = strPhoneCode;
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

	public String getQrCodeImageUrl() {
		return qrCodeImageUrl;
	}

	public void setQrCodeImageUrl(String qrCodeImageUrl) {
		this.qrCodeImageUrl = qrCodeImageUrl;
	}

	public Date getGracePeriodStartDate() {
		return gracePeriodStartDate;
	}

	public void setGracePeriodStartDate(Date gracePeriodStartDate) {
		this.gracePeriodStartDate = gracePeriodStartDate;
	}

	public Date getDateOfDormancy() {
		return dateOfDormancy;
	}

	public void setDateOfDormancy(Date dateOfDormancy) {
		this.dateOfDormancy = dateOfDormancy;
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

	public String getStrAllowLoadCash() {
		return strAllowLoadCash;
	}

	public void setStrAllowLoadCash(String strAllowLoadCash) {
		this.strAllowLoadCash = strAllowLoadCash;
	}

	public String getStrAccountCategory() {
		return strAccountCategory;
	}

	public void setStrAccountCategory(String strAccountCategory) {
		this.strAccountCategory = strAccountCategory;
	}

	public String getStrTxnAmount() {
		return strTxnAmount;
	}

	public void setStrTxnAmount(String strTxnAmount) {
		this.strTxnAmount = strTxnAmount;
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

	public String getStrPerDayAssignLimit() {
		return strPerDayAssignLimit;
	}

	public void setStrPerDayAssignLimit(String strPerDayAssignLimit) {
		this.strPerDayAssignLimit = strPerDayAssignLimit;
	}

	public String getStrMonthlyAssignLimit() {
		return strMonthlyAssignLimit;
	}

	public void setStrMonthlyAssignLimit(String strMonthlyAssignLimit) {
		this.strMonthlyAssignLimit = strMonthlyAssignLimit;
	}

	public String getStrYearlyAssignLimit() {
		return strYearlyAssignLimit;
	}

	public void setStrYearlyAssignLimit(String strYearlyAssignLimit) {
		this.strYearlyAssignLimit = strYearlyAssignLimit;
	}	

	public String getStrSingleTxnMaxLimit() {
		return strSingleTxnMaxLimit;
	}

	public void setStrSingleTxnMaxLimit(String strSingleTxnMaxLimit) {
		this.strSingleTxnMaxLimit = strSingleTxnMaxLimit;
	}

	public String getStrPerDayMaxAssignedlimit() {
		return strPerDayMaxAssignedlimit;
	}

	public void setStrPerDayMaxAssignedlimit(String strPerDayMaxAssignedlimit) {
		this.strPerDayMaxAssignedlimit = strPerDayMaxAssignedlimit;
	}

	public String getStrMonthlyMaxAssignedLimit() {
		return strMonthlyMaxAssignedLimit;
	}

	public void setStrMonthlyMaxAssignedLimit(String strMonthlyMaxAssignedLimit) {
		this.strMonthlyMaxAssignedLimit = strMonthlyMaxAssignedLimit;
	}

	public String getStrYearlyMaxAssignedLimit() {
		return strYearlyMaxAssignedLimit;
	}

	public void setStrYearlyMaxAssignedLimit(String strYearlyMaxAssignedLimit) {
		this.strYearlyMaxAssignedLimit = strYearlyMaxAssignedLimit;
	}

	public String getStrAccountHolderName() {
		return strAccountHolderName;
	}

	public void setStrAccountHolderName(String strAccountHolderName) {
		this.strAccountHolderName = strAccountHolderName;
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

	public String getStrCreditMaxAssignedLimit() {
		return strCreditMaxAssignedLimit;
	}

	public void setStrCreditMaxAssignedLimit(String strCreditMaxAssignedLimit) {
		this.strCreditMaxAssignedLimit = strCreditMaxAssignedLimit;
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

	public String getStrTxnType() {
		return strTxnType;
	}

	public void setStrTxnType(String strTxnType) {
		this.strTxnType = strTxnType;
	}

	public int getStrPerTxnLimit() {
		return strPerTxnLimit;
	}

	public void setStrPerTxnLimit(int strPerTxnLimit) {
		this.strPerTxnLimit = strPerTxnLimit;
	}

	public Date getStrPreClosureDate() {
		return strPreClosureDate;
	}

	public void setStrPreClosureDate(Date strPreClosureDate) {
		this.strPreClosureDate = strPreClosureDate;
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

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	
	
}
