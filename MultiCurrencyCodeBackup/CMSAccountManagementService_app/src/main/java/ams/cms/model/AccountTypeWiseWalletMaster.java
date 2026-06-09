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
@Table(name = "account_type_wise_wallet_master")
public class AccountTypeWiseWalletMaster implements Serializable
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
	
	@Column(name = "mcc_code")
	private String strMccCode;
	
	@Column(name = "percentage")
	private String strPercentage;
	
	/*
	@Column(name = "mcc_desc")
	private String strMccCodeDesc;
	*/
	
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

	public String getStrMccCode() {
		return strMccCode;
	}

	public void setStrMccCode(String strMccCode) {
		this.strMccCode = strMccCode;
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
	
	public String getStrPercentage() {
		return strPercentage;
	}
	public void setStrPercentage(String strPercentage) {
		this.strPercentage = strPercentage;
	}
}
