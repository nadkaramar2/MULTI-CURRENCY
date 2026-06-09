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

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ams.cms.util.JsonDateDeserializer;
import ams.cms.util.JsonDateSerializer;

@JsonAutoDetect
@Entity
@Table(name = "pre_account_master")
public class PreAccountMaster implements Serializable 
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "account_type")
	private String strAccountType;

	@Column(name = "country_code_shortname")
	private String strCountryCodeShortName;
	
	@Column(name = "country_code")
	private String strCountryCode;

	@Column(name = "fname")
	private String strFirstName;

	@Column(name = "mname")
	private String strMiddleName;

	@Column(name = "lname")
	private String strLastName;

	@Column(name = "gender")
	private String strGender;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "birth_date")
	private Date birthDate;	

	@Column(name = "email_id")
	private String strEmailID;

	@Column(name = "mobile_no")
	private String strMobileNo;

	@Column(name = "password")
	private String strPassword;

	@Column(name = "otp")
	private String strOtp;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "created_date")
	private Date strDateOfCreation;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "account_issue_date")
	private Date strAccountIssueDate;
    
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "last_login_date")
	private Date strLastloginDate;

	@Column(name = "created_by")
	private String strCreatedBy;
	
	@Column(name = "jwtToken")
	private String strJwtToken;
	
	@Column(name = "is_account_no_created")
	private String strIsAccountNoCreated;
	
	@Column(name = "account_created_flag")
	private String strAccountCreatedFlag;
	
	@Column(name = "is_kyc_verified")
	private String strIsKycVerified;
	
	@Column(name = "session_id")
	private String strSessionId;
	
	@Column(name = "is_cust_id_created")
	private String isCustIdCreated;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "address2")
	private String address2;
	
	@Column(name = "address3")
	private String address3;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "pincode")
	private String pincode;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "country")
	private String country;
	
	@Transient
	private String strDOB;
	
	@Transient
	private String userName;
	
	@Transient
	private String strIsCustomerIdRelated;
	
	@Transient
	private String strIsAccountNoRelated;
	
	@Transient
	private String cust_id;
	
	@Transient
	private String account_Type;
	
	@Transient
	private String accountTypeCategory;
	
	@Transient
	private String fromDate;
	
	@Transient
	private String toDate;
	
	@Transient
	private String strDateofRegistration;
	
	@Transient
	private String strTimeofRegistration;
	
	@Transient
	private String strAccountRegistered ;
	
	@Transient
	private String strStatus;
	
	@Transient
	private String strAgeing;
	
	@Transient
	private String strBvnNo;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
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

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
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

	public String getStrDOB() {
		return strDOB;
	}
	
	public void setStrDOB(String strDOB) {
		this.strDOB = strDOB;
	}

	public String getStrPassword() {
		return strPassword;
	}

	public void setStrPassword(String strPassword) {
		this.strPassword = strPassword;
	}

	public String getStrOtp() {
		return strOtp;
	}

	public void setStrOtp(String strOtp) {
		this.strOtp = strOtp;
	}
	
	public String getStrJwtToken() {
		return strJwtToken;
	}

	public void setStrJwtToken(String strJwtToken) {
		this.strJwtToken = strJwtToken;
	}
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStrIsAccountNoCreated() {
		return strIsAccountNoCreated;
	}

	public void setStrIsAccountNoCreated(String strIsAccountNoCreated) {
		this.strIsAccountNoCreated = strIsAccountNoCreated;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrAccountCreatedFlag() {
		return strAccountCreatedFlag;
	}

	public void setStrAccountCreatedFlag(String strAccountCreatedFlag) {
		this.strAccountCreatedFlag = strAccountCreatedFlag;
	}

	public Date getStrAccountIssueDate() {
		return strAccountIssueDate;
	}

	public void setStrAccountIssueDate(Date strAccountIssueDate) {
		this.strAccountIssueDate = strAccountIssueDate;
	}

	public Date getStrLastloginDate() {
		return strLastloginDate;
	}

	public void setStrLastloginDate(Date strLastloginDate) {
		this.strLastloginDate = strLastloginDate;
	}	

	public String getStrIsKycVerified() {
		return strIsKycVerified;
	}

	public void setStrIsKycVerified(String strIsKycVerified) {
		this.strIsKycVerified = strIsKycVerified;
	}

	public String getStrIsCustomerIdRelated() {
		return strIsCustomerIdRelated;
	}

	public void setStrIsCustomerIdRelated(String strIsCustomerIdRelated) {
		this.strIsCustomerIdRelated = strIsCustomerIdRelated;
	}

	public String getStrIsAccountNoRelated() {
		return strIsAccountNoRelated;
	}

	public void setStrIsAccountNoRelated(String strIsAccountNoRelated) {
		this.strIsAccountNoRelated = strIsAccountNoRelated;
	}
	
	public String getStrCountryCodeShortName() {
		return strCountryCodeShortName;
	}

	public void setStrCountryCodeShortName(String strCountryCodeShortName) {
		this.strCountryCodeShortName = strCountryCodeShortName;
	}

	public String getStrCountryCode() {
		return strCountryCode;
	}

	public void setStrCountryCode(String strCountryCode) {
		this.strCountryCode = strCountryCode;
	}	

	public String getStrSessionId() {
		return strSessionId;
	}

	public void setStrSessionId(String strSessionId) {
		this.strSessionId = strSessionId;
	}

	public String getIsCustIdCreated() {
		return isCustIdCreated;
	}

	public void setIsCustIdCreated(String isCustIdCreated) {
		this.isCustIdCreated = isCustIdCreated;
	}

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	public String getAccount_Type() {
		return account_Type;
	}

	public void setAccount_Type(String account_Type) {
		this.account_Type = account_Type;
	}

	public String getAccountTypeCategory() {
		return accountTypeCategory;
	}

	public void setAccountTypeCategory(String accountTypeCategory) {
		this.accountTypeCategory = accountTypeCategory;
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

	public String getStrDateofRegistration() {
		return strDateofRegistration;
	}

	public void setStrDateofRegistration(String strDateofRegistration) {
		this.strDateofRegistration = strDateofRegistration;
	}

	public String getStrTimeofRegistration() {
		return strTimeofRegistration;
	}

	public void setStrTimeofRegistration(String strTimeofRegistration) {
		this.strTimeofRegistration = strTimeofRegistration;
	}

	public String getStrAccountRegistered() {
		return strAccountRegistered;
	}

	public void setStrAccountRegistered(String strAccountRegistered) {
		this.strAccountRegistered = strAccountRegistered;
	}

	public String getStrStatus() {
		return strStatus;
	}

	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}

	public String getStrAgeing() {
		return strAgeing;
	}

	public void setStrAgeing(String strAgeing) {
		this.strAgeing = strAgeing;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStrBvnNo() {
		return strBvnNo;
	}

	public void setStrBvnNo(String strBvnNo) {
		this.strBvnNo = strBvnNo;
	}

	@Override
	public String toString() {
		return "PreAccountMaster [strID=" + strID + ", strFirstName=" + strFirstName + ", strEmailID=" + strEmailID
				+ ", strMobileNo=" + strMobileNo + ", strPassword=" + strPassword + ", userName="+userName+"]";
	}
}
