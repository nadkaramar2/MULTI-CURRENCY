package ams.cms.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ams.cms.util.JsonDateDeserializer;
import ams.cms.util.JsonDateSerializer;

//same as that of AccountCreditLimitCategory -can be removed-
@Entity
@Table(name="account_credit_limit_category")
public class AccountCreditLimit implements Serializable{

private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int strID;
	
	@Column(name = "participant_id")
	private String strParticipantId;
	
	@Column(name = "credit_type")
	private String strCreditType;
	
	@Column(name = "credit_limit")
	private String strCreditLimit;
	
	@Column(name = "created_by")
	private String strCreatedBy;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "created_date")
	private Date strCreatedDate;

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

	public String getStrCreditType() {
		return strCreditType;
	}

	public void setStrCreditType(String strCreditType) {
		this.strCreditType = strCreditType;
	}

	public String getStrCreditLimit() {
		return strCreditLimit;
	}

	public void setStrCreditLimit(String strCreditLimit) {
		this.strCreditLimit = strCreditLimit;
	}

	public String getStrCreatedBy() {
		return strCreatedBy;
	}

	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}

	public Date getStrCreatedDate() {
		return strCreatedDate;
	}

	public void setStrCreatedDate(Date strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}
	/*
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "TEXT", name = "id", unique = true)
	private long strId;
	@Column(name = "participant_id")
	private String strParticipantId;
	@Column(name = "credit_type", unique = true)
	private String strCreditType;
	@Column(name = "credit_limit", unique = true)
	private String strCreditLimit;
	@Column(name = "created_by", unique = true)
	private String strCreatedBy;
	@Column(name = "created_date", unique = true)
	private Date strCreatedDate;
	public long getStrId() {
		return strId;
	}
	public void setStrId(long strId) {
		this.strId = strId;
	}
	public String getStrParticipantId() {
		return strParticipantId;
	}
	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}
	public String getStrCreditType() {
		return strCreditType;
	}
	public void setStrCreditType(String strCreditType) {
		this.strCreditType = strCreditType;
	}
	public String getStrCreditLimit() {
		return strCreditLimit;
	}
	public void setStrCreditLimit(String strCreditLimit) {
		this.strCreditLimit = strCreditLimit;
	}
	public String getStrCreatedBy() {
		return strCreatedBy;
	}
	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}
	public Date getStrCreatedDate() {
		return strCreatedDate;
	}
	public void setStrCreatedDate(Date strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}*/
}