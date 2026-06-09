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
@Table(name = "charge_master")
public class ChargeMaster implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "participant_id")
	private String strParticipantID;
	
	@Column(name = "charge_type")
	private String strChargeType;
	
	@Column(name = "charge_description")
	private String strChargeDescription;
	
	@Column(name = "charge_related")
	private String strChargeRelated;
	
	@Column(name = "charge_related_description")
	private String strChargeRelatedDescription;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "created_date")
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

	public String getStrChargeType() {
		return strChargeType;
	}

	public void setStrChargeType(String strChargeType) {
		this.strChargeType = strChargeType;
	}

	public String getStrChargeDescription() {
		return strChargeDescription;
	}

	public void setStrChargeDescription(String strChargeDescription) {
		this.strChargeDescription = strChargeDescription;
	}

	public String getStrChargeRelated() {
		return strChargeRelated;
	}

	public void setStrChargeRelated(String strChargeRelated) {
		this.strChargeRelated = strChargeRelated;
	}

	public String getStrChargeRelatedDescription() {
		return strChargeRelatedDescription;
	}

	public void setStrChargeRelatedDescription(String strChargeRelatedDescription) {
		this.strChargeRelatedDescription = strChargeRelatedDescription;
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

	
	
	
	
	
}
