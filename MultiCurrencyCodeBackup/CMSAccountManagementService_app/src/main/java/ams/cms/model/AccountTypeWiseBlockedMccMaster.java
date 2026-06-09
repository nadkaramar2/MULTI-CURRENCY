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
@Table(name = "blocked_mcc_account_type_wise")
public class AccountTypeWiseBlockedMccMaster implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "participant_id")
	private String strParticipantID;
	
	@Column(name = "account_type")
	private String strAccounType;
	
	@Column(name = "blocked_mcc_code")
	private String strBlockedMCCCode;
	
	@Transient
	private String strMccCodeDesc;
	
	@Transient
	private String strMccCode;
	
	@Transient
	private String strCreditAcType;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "creation_date")
	private Date strDateOfCreation;
	
	@Column(name = "created_by")
	private String strCreatedBy;

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

	public String getStrAccounType() {
		return strAccounType;
	}

	public void setStrAccounType(String strAccounType) {
		this.strAccounType = strAccounType;
	}

	public String getStrBlockedMCCCode() {
		return strBlockedMCCCode;
	}

	public void setStrBlockedMCCCode(String strBlockedMCCCode) {
		this.strBlockedMCCCode = strBlockedMCCCode;
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

	public String getStrMccCodeDesc() {
		return strMccCodeDesc;
	}

	public void setStrMccCodeDesc(String strMccCodeDesc) {
		this.strMccCodeDesc = strMccCodeDesc;
	}

	public String getStrMccCode() {
		return strMccCode;
	}

	public void setStrMccCode(String strMccCode) {
		this.strMccCode = strMccCode;
	}
}
