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
@Table(name = "account_type_charges")
public class AccountTypeCharges implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strId;

	@Column(name = "participant_id")
	private String strParticipantID;
	@Column(name = "account_type")
	private String strAccountType;
	@Column(name = "charge_type")
	private String strChargeType;
	@Column(name = "charge_description")
	private String strChargeDescription;
	@Column(name = "amount")
	private String strAmount;
	@Column(name = "percentage")
	private String strPercentage;

	@Column(name = "created_by")
	private String strCreatedBy;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "created_date")
	private Date strCreatedDate;
	
	@Transient
	private String strChargeRelated;
	
	@Transient
	private String selectedChargeTypeWithAmount;
	
	@Transient
	private String selectedChargeTypeWithPercentage;

	public String getStrId() {
		return strId;
	}

	public void setStrId(String strId) {
		this.strId = strId;
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

	public String getStrAmount() {
		return strAmount;
	}

	public void setStrAmount(String strAmount) {
		this.strAmount = strAmount;
	}

	public String getStrPercentage() {
		return strPercentage;
	}

	public void setStrPercentage(String strPercentage) {
		this.strPercentage = strPercentage;
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

	public String getStrChargeRelated() {
		return strChargeRelated;
	}

	public void setStrChargeRelated(String strChargeRelated) {
		this.strChargeRelated = strChargeRelated;
	}

	public String getSelectedChargeTypeWithAmount() {
		return selectedChargeTypeWithAmount;
	}

	public void setSelectedChargeTypeWithAmount(String selectedChargeTypeWithAmount) {
		this.selectedChargeTypeWithAmount = selectedChargeTypeWithAmount;
	}

	public String getSelectedChargeTypeWithPercentage() {
		return selectedChargeTypeWithPercentage;
	}

	public void setSelectedChargeTypeWithPercentage(String selectedChargeTypeWithPercentage) {
		this.selectedChargeTypeWithPercentage = selectedChargeTypeWithPercentage;
	}
}
