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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ams.cms.util.JsonDateDeserializer;
import ams.cms.util.JsonDateSerializer;

@JsonAutoDetect
@Entity
@Table(name = "customer_master")
public class CustomerIdCreation implements Serializable 
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
	
	@Column(name = "address")
	private String strAddress1;
	
	@Column(name = "address2")
	private String address2;
	
	@Column(name = "address3")
	private String address3;
	
	@Column(name = "pincode")
	private String strPinCode;
	
	@Column(name = "city")
	private String strCity;
	
	@Column(name = "state")
	private String strState;
	
	@Column(name = "country")
	private String strCountry;	
	
	@Column(name="country_code")
	private String strPhoneCode;
	
	@Column(name = "phone_no")
	private String strPhoneNo;
	
	@Column(name = "pin")
	private String strPin;
	
	@Column(name = "pin_otp")
	private String strPinOTP;
	
	@Column(name = "txn_otp")
	private String strTxnOTP;
	
	//added by ankit on 10-05-2023
	@Column(name = "active_tier")
	private String strActiveTier;
	
	@Column(name = "active_tier_date")
	private Date strActiveTierDate;
	
	@Column(name = "active_tier_time")
	private Time strActiveTierTime;
	
	@Column(name = "tier1_date")
	private Date strTier1Date;
	
	@Column(name = "tier1_time")
	private Time strTier1Time;
	
	@Column(name = "tier1_status")
	private String strTier1Status;
	
	@Column(name = "tier2_date")
	private Date strTier2Date;
	
	@Column(name = "tier2_time")
	private Time strTier2Time;
	
	@Column(name = "tier2_status")
	private String strTier2Status;
	
	@Column(name = "tier3_date")
	private Date strTier3Date;
	
	@Column(name = "tier3_time")
	private Time strTier3Time;
	
	@Column(name = "tier3_status")
	private String strTier3Status;
	//added by ankit on 10-05-2023
	
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
	private String strAccountType;
	
	@Transient
	private String strNewPin;
	
	@Transient
	private String strConfirmNewPin;
	
	@Transient
	private String strCurrentPin;
	
	@Transient
	private String strCustomerName;
	
	@Transient
	private String strTierType;
	
	@Transient
	private String strBvnNumber;
	
	@Transient
	private String strTier1PassportPhotograph;
	
	@Transient
	private String strTier2PassportPhotograph;
	
	@Transient
	private String strAddProofImage;
	
	@Transient
	private String strIdeProofImage;
	
	@Transient
	private String montraId;
	
	@Transient
	private String cid;
	
	@Transient
	private String customerEnterPIN;
	
	@Transient
	private String bid;
	
	@Transient
	private String mccCode;

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

	public String getStrCustId() {
		return strCustId;
	}

	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
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

	public String getStrKycUpdateRequired() {
		return strKycUpdateRequired;
	}

	public void setStrKycUpdateRequired(String strKycUpdateRequired) {
		this.strKycUpdateRequired = strKycUpdateRequired;
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

	public String getStrPhoneCode() {
		return strPhoneCode;
	}

	public void setStrPhoneCode(String strPhoneCode) {
		this.strPhoneCode = strPhoneCode;
	}

	public String getStrPhoneNo() {
		return strPhoneNo;
	}

	public void setStrPhoneNo(String strPhoneNo) {
		this.strPhoneNo = strPhoneNo;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrPin() {
		return strPin;
	}

	public void setStrPin(String strPin) {
		this.strPin = strPin;
	}

	public String getStrPinOTP() {
		return strPinOTP;
	}
	
	public void setStrPinOTP(String strPinOTP) {
		this.strPinOTP = strPinOTP;
	}

	public String getStrNewPin() {
		return strNewPin;
	}

	public void setStrNewPin(String strNewPin) {
		this.strNewPin = strNewPin;
	}

	public String getStrConfirmNewPin() {
		return strConfirmNewPin;
	}

	public void setStrConfirmNewPin(String strConfirmNewPin) {
		this.strConfirmNewPin = strConfirmNewPin;
	}

	public String getStrCurrentPin() {
		return strCurrentPin;
	}

	public void setStrCurrentPin(String strCurrentPin) {
		this.strCurrentPin = strCurrentPin;
	}

	public String getStrTxnOTP() {
		return strTxnOTP;
	}

	public void setStrTxnOTP(String strTxnOTP) {
		this.strTxnOTP = strTxnOTP;
	}

	public String getStrCustomerName() {
		return strCustomerName;
	}

	public void setStrCustomerName(String strCustomerName) {
		this.strCustomerName = strCustomerName;
	}

	public String getStrActiveTier() {
		return strActiveTier;
	}

	public void setStrActiveTier(String strActiveTier) {
		this.strActiveTier = strActiveTier;
	}

	public Date getStrActiveTierDate() {
		return strActiveTierDate;
	}

	public void setStrActiveTierDate(Date strActiveTierDate) {
		this.strActiveTierDate = strActiveTierDate;
	}

	public Time getStrActiveTierTime() {
		return strActiveTierTime;
	}

	public void setStrActiveTierTime(Time strActiveTierTime) {
		this.strActiveTierTime = strActiveTierTime;
	}

	public Date getStrTier1Date() {
		return strTier1Date;
	}

	public void setStrTier1Date(Date strTier1Date) {
		this.strTier1Date = strTier1Date;
	}

	public Time getStrTier1Time() {
		return strTier1Time;
	}

	public void setStrTier1Time(Time strTier1Time) {
		this.strTier1Time = strTier1Time;
	}

	public String getStrTier1Status() {
		return strTier1Status;
	}

	public void setStrTier1Status(String strTier1Status) {
		this.strTier1Status = strTier1Status;
	}

	public Date getStrTier2Date() {
		return strTier2Date;
	}

	public void setStrTier2Date(Date strTier2Date) {
		this.strTier2Date = strTier2Date;
	}

	public Time getStrTier2Time() {
		return strTier2Time;
	}

	public void setStrTier2Time(Time strTier2Time) {
		this.strTier2Time = strTier2Time;
	}

	public String getStrTier2Status() {
		return strTier2Status;
	}

	public void setStrTier2Status(String strTier2Status) {
		this.strTier2Status = strTier2Status;
	}

	public Date getStrTier3Date() {
		return strTier3Date;
	}

	public void setStrTier3Date(Date strTier3Date) {
		this.strTier3Date = strTier3Date;
	}

	public Time getStrTier3Time() {
		return strTier3Time;
	}

	public void setStrTier3Time(Time strTier3Time) {
		this.strTier3Time = strTier3Time;
	}

	public String getStrTier3Status() {
		return strTier3Status;
	}

	public void setStrTier3Status(String strTier3Status) {
		this.strTier3Status = strTier3Status;
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

	public String getStrTierType() {
		return strTierType;
	}

	public void setStrTierType(String strTierType) {
		this.strTierType = strTierType;
	}

	public String getStrBvnNumber() {
		return strBvnNumber;
	}

	public void setStrBvnNumber(String strBvnNumber) {
		this.strBvnNumber = strBvnNumber;
	}

	public String getStrTier1PassportPhotograph() {
		return strTier1PassportPhotograph;
	}

	public void setStrTier1PassportPhotograph(String strTier1PassportPhotograph) {
		this.strTier1PassportPhotograph = strTier1PassportPhotograph;
	}

	public String getStrTier2PassportPhotograph() {
		return strTier2PassportPhotograph;
	}

	public void setStrTier2PassportPhotograph(String strTier2PassportPhotograph) {
		this.strTier2PassportPhotograph = strTier2PassportPhotograph;
	}

	public String getStrAddProofImage() {
		return strAddProofImage;
	}

	public void setStrAddProofImage(String strAddProofImage) {
		this.strAddProofImage = strAddProofImage;
	}

	public String getStrIdeProofImage() {
		return strIdeProofImage;
	}

	public void setStrIdeProofImage(String strIdeProofImage) {
		this.strIdeProofImage = strIdeProofImage;
	}

	public String getMontraId() {
		return montraId;
	}

	public void setMontraId(String montraId) {
		this.montraId = montraId;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCustomerEnterPIN() {
		return customerEnterPIN;
	}

	public void setCustomerEnterPIN(String customerEnterPIN) {
		this.customerEnterPIN = customerEnterPIN;
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

	@Override
	public String toString() {
		return "CustomerIdCreation [strID=" + strID + ", strParticipantID=" + strParticipantID + ", strCustId="
				+ strCustId + ", strTitle=" + strTitle + ", strFirstName=" + strFirstName + ", strMiddleName="
				+ strMiddleName + ", strLastName=" + strLastName + ", strGender=" + strGender + ", birthDate="
				+ birthDate + ", strEmailID=" + strEmailID + ", strMobileNo=" + strMobileNo + ", strAddress1="
				+ strAddress1 + ", address2=" + address2 + ", address3=" + address3 + ", strPinCode=" + strPinCode
				+ ", strCity=" + strCity + ", strState=" + strState + ", strCountry=" + strCountry + ", strPhoneCode="
				+ strPhoneCode + ", strPhoneNo=" + strPhoneNo + ", strPin=" + strPin + ", strPinOTP=" + strPinOTP
				+ ", strTxnOTP=" + strTxnOTP + ", strActiveTier=" + strActiveTier + ", strActiveTierDate="
				+ strActiveTierDate + ", strActiveTierTime=" + strActiveTierTime + ", strTier1Date=" + strTier1Date
				+ ", strTier1Time=" + strTier1Time + ", strTier1Status=" + strTier1Status + ", strTier2Date="
				+ strTier2Date + ", strTier2Time=" + strTier2Time + ", strTier2Status=" + strTier2Status
				+ ", strTier3Date=" + strTier3Date + ", strTier3Time=" + strTier3Time + ", strTier3Status="
				+ strTier3Status + ", strDOB=" + strDOB + ", strAddressProofDocumentId=" + strAddressProofDocumentId
				+ ", strAddressProofDocumentValue=" + strAddressProofDocumentValue + ", strIdentityProofDocumentId="
				+ strIdentityProofDocumentId + ", strIdentityProofDocumentValue=" + strIdentityProofDocumentValue
				+ ", strIsCreditType=" + strIsCreditType + ", strKycUpdateRequired=" + strKycUpdateRequired
				+ ", strAccountType=" + strAccountType + ", strNewPin=" + strNewPin + ", strConfirmNewPin="
				+ strConfirmNewPin + ", strCurrentPin=" + strCurrentPin + ", strCustomerName=" + strCustomerName + "]";
	}
}
