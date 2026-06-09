package ams.cms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name = "charge_related_master")
public class ChargeRelatedMaster implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	
	@Column(name = "charge_related")
	private String strChargeRelated;
	
	@Column(name = "charge_related_description")
	private String strChargeRelatedDescription;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
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

	@Override
	public String toString() {
		return "ChargeRelatedMaster [strID=" + strID + ", strChargeRelated=" + strChargeRelated
				+ ", strChargeRelatedDescription=" + strChargeRelatedDescription + "]";
	}
	
	
	
	
}
