package ams.cms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ams.cms.util.JsonDateDeserializer;
import ams.cms.util.JsonDateSerializer;

@JsonAutoDetect
@Entity
@Table(name = "kyc_details")
public class AccountKycDetails implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "participant_id")
	private String strParticipantID;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "mobile_no")
	private String strMobileNo;
	
	@Column(name = "cust_id")
	private String strCustId;
	
	@Column(name = "address_proof_document_type")
	private String strAddressProofDocumentType;
	
	@Column(name = "address_proof_document_value")
	private String strAddressDocumentValue;
	
	@Column(name = "address_proof_document_name")
	private String strAddressDocumentName;
	
	@Column(name = "identity_proof_document_type")
	private String strIdentityProofDocumentType;
	
	@Column(name = "identity_proof_document_name")
	private String strIdentityProofDocumentName;
	
	@Column(name = "identity_proof_document_value")
	private String strIdentityProofDocumentValue;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "address_proof_upload_date")
	private Date strAddressProofUploadDate;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "identity_proof_upload_date")
	private Date strIdentityProofUploadDate;
	
	@Column(name = "date_of_submission")
	private Date dateOfSubmission;
	
	@Column(name = "bvn_no")
	private String strBvnNumber;
	
	@Column(name = "tier1_passport_photograph")
	private String strTier1PassportPhotograph;
	
	@Column(name = "tier2_passport_photograph")
	private String strTier2PassportPhotograph;

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

	public String getStrCustId() {
		return strCustId;
	}

	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}

	public String getStrAddressProofDocumentType() {
		return strAddressProofDocumentType;
	}

	public void setStrAddressProofDocumentType(String strAddressProofDocumentType) {
		this.strAddressProofDocumentType = strAddressProofDocumentType;
	}

	public String getStrAddressDocumentValue() {
		return strAddressDocumentValue;
	}

	public void setStrAddressDocumentValue(String strAddressDocumentValue) {
		this.strAddressDocumentValue = strAddressDocumentValue;
	}

	public String getStrIdentityProofDocumentType() {
		return strIdentityProofDocumentType;
	}

	public void setStrIdentityProofDocumentType(String strIdentityProofDocumentType) {
		this.strIdentityProofDocumentType = strIdentityProofDocumentType;
	}

	public String getStrIdentityProofDocumentValue() {
		return strIdentityProofDocumentValue;
	}

	public void setStrIdentityProofDocumentValue(String strIdentityProofDocumentValue) {
		this.strIdentityProofDocumentValue = strIdentityProofDocumentValue;
	}

	public Date getDateOfSubmission() {
		return dateOfSubmission;
	}

	public void setDateOfSubmission(Date dateOfSubmission) {
		this.dateOfSubmission = dateOfSubmission;
	}
	
	public String getStrMobileNo() {
		return strMobileNo;
	}
	public void setStrMobileNo(String strMobileNo) {
		this.strMobileNo = strMobileNo;
	}

	public String getStrAddressDocumentName() {
		return strAddressDocumentName;
	}

	public void setStrAddressDocumentName(String strAddressDocumentName) {
		this.strAddressDocumentName = strAddressDocumentName;
	}

	public String getStrIdentityProofDocumentName() {
		return strIdentityProofDocumentName;
	}

	public void setStrIdentityProofDocumentName(String strIdentityProofDocumentName) {
		this.strIdentityProofDocumentName = strIdentityProofDocumentName;
	}

	public Date getStrAddressProofUploadDate() {
		return strAddressProofUploadDate;
	}

	public void setStrAddressProofUploadDate(Date strAddressProofUploadDate) {
		this.strAddressProofUploadDate = strAddressProofUploadDate;
	}

	public Date getStrIdentityProofUploadDate() {
		return strIdentityProofUploadDate;
	}

	public void setStrIdentityProofUploadDate(Date strIdentityProofUploadDate) {
		this.strIdentityProofUploadDate = strIdentityProofUploadDate;
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
	
}
