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

@JsonAutoDetect
@Entity
@Table(name = "participant_master_table")
public class ParticipantMaster implements Serializable 
{
	private static final long serialVersionUID = -2561112228643367874L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "participant_master")
	private String strParticipantMaster;
	
	@Column(name = "participant_id")
	private String strParticipantID; 
	
	@Column(name = "participant_name")
	private String strParticipantName;
	
	@Column(name = "participant_type")
	private String strParticipant;
	
	@Column(name = "Address1")
	private String strAddress1;
	
	@Column(name = "Address2")
	private String strAddress2;
	
	@Column(name = "Address3")
	private String strAddress3;
	
	@Column(name = "city")
	private String strCity;
	
	@Column(name = "state")
	private String strState ;
	
	@Column(name = "pincode")
	private String strPincode;
	
	@Column(name = "country")
	private String strCountry;
	
	@Column(name = "contact_person_name")
	private String strContactPersonName;
	
	@Column(name = "contact_person_no")
	private String strContactPersonNumber;
	
	@Column(name = "created_by")
	private String strCreatedBy;
	
	@Column(name = "created_date")
	private Date strCreatedDate;
	
	@Column(name = "participant_descr")
	private String strParticipantDescr;
	
	@Column(name = "apikey")
	private String strApikey;
	
	@Transient
	private String strUserId;	
	
	public String getStrUserId() {
		return strUserId;
	}
	public void setStrUserId(String strUserId) {
		this.strUserId = strUserId;
	}
	public String getStrCreatedBy() {
		return strCreatedBy;
	}
	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}
	
	public String getStrID() {
		return strID;
	}
	public void setStrID(String strID) {
		this.strID = strID;
	}
	
	
	public Date getStrCreatedDate() {
		return strCreatedDate;
	}
	public void setStrCreatedDate(Date strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}
	public String getStrParticipantMaster() {
		return strParticipantMaster;
	}
	public void setStrParticipantMaster(String strParticipantMaster) {
		this.strParticipantMaster = strParticipantMaster;
	}
	public String getStrParticipantID() {
		return strParticipantID;
	}
	public void setStrParticipantID(String strParticipantID) {
		this.strParticipantID = strParticipantID;
	}
	public String getStrParticipantName() {
		return strParticipantName;
	}
	public void setStrParticipantName(String strParticipantName) {
		this.strParticipantName = strParticipantName;
	}
	public String getStrParticipant() {
		return strParticipant;
	}
	public void setStrParticipant(String strParticipant) {
		this.strParticipant = strParticipant;
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
	public String getStrPincode() {
		return strPincode;
	}
	public void setStrPincode(String strPincode) {
		this.strPincode = strPincode;
	}
	public String getStrCountry() {
		return strCountry;
	}
	public void setStrCountry(String strCountry) {
		this.strCountry = strCountry;
	}
	public String getStrContactPersonName() {
		return strContactPersonName;
	}
	public void setStrContactPersonName(String strContactPersonName) {
		this.strContactPersonName = strContactPersonName;
	}
	public String getStrContactPersonNumber() {
		return strContactPersonNumber;
	}
	public void setStrContactPersonNumber(String strContactPersonNumber) {
		this.strContactPersonNumber = strContactPersonNumber;
	}
	public String getStrParticipantDescr() {
		return strParticipantDescr;
	}
	public void setStrParticipantDescr(String strParticipantDescr) {
		this.strParticipantDescr = strParticipantDescr;
	}
	public String getStrApikey() {
		return strApikey;
	}
	public void setStrApikey(String strApikey) {
		this.strApikey = strApikey;
	}
}
