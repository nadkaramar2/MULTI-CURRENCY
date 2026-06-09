package ams.cms.model;

import java.io.Serializable;

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
@Table(name = "upgrade_tier_req_res")
public class ApproveUpgradeTier implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strId;
	
	@Column(name = "cust_id")
	private String strCustId;
	
	@Column(name = "tier_type")
	private String strTierType;
	
	@Column(name = "req_status")
	private String strReqStatus;
	
	@Column(name = "req_date_time")
	private String strReqDateTime;
	
	@Column(name = "res_date_time")
	private String strResDateTime;
	
	
	@Transient
	private String strCustName;
	
	

	public String getStrId() {
		return strId;
	}

	public void setStrId(String strId) {
		this.strId = strId;
	}

	public String getStrCustId() {
		return strCustId;
	}

	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}

	public String getStrTierType() {
		return strTierType;
	}

	public void setStrTierType(String strTierType) {
		this.strTierType = strTierType;
	}

	public String getStrReqStatus() {
		return strReqStatus;
	}

	public void setStrReqStatus(String strReqStatus) {
		this.strReqStatus = strReqStatus;
	}

	public String getStrReqDateTime() {
		return strReqDateTime;
	}

	public void setStrReqDateTime(String strReqDateTime) {
		this.strReqDateTime = strReqDateTime;
	}

	public String getStrResDateTime() {
		return strResDateTime;
	}

	public void setStrResDateTime(String strResDateTime) {
		this.strResDateTime = strResDateTime;
	}

	public String getStrCustName() {
		return strCustName;
	}

	public void setStrCustName(String strCustName) {
		this.strCustName = strCustName;
	}
	
	
	
}
