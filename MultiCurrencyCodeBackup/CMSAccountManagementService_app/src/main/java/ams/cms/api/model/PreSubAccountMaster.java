package ams.cms.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ams.cms.util.JsonDateDeserializer;
import ams.cms.util.JsonDateSerializer;

@JsonAutoDetect
@Entity
@DynamicUpdate
@Table(name = "pre_sub_account_master")
public class PreSubAccountMaster implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "mobile_no")
	private String strMobileNo;
	
	@Column(name = "account_type")
	private String strAccountType;

	@Column(name = "is_account_no_created")
	private String strIsAccountNoCreated;
	
	@Column(name = "is_cust_id_created")
	private String strIsCustIdCreated;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "created_date")
	private Date dateOfCreation;

	@Column(name = "created_by")
	private String strCreatedBy;
	
	
	@Transient
	private String fromDate;
	
	@Transient
	private String toDate;
	
	@Transient
	private String ageing;
	
	@Transient
	private String strDescription;
	
	@Transient
	private String strDateOfRegistration;
	
	@Transient
	private String strCustId;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}


	public String getStrMobileNo() {
		return strMobileNo;
	}


	public void setStrMobileNo(String strMobileNo) {
		this.strMobileNo = strMobileNo;
	}


	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrIsAccountNoCreated() {
		return strIsAccountNoCreated;
	}

	public void setStrIsAccountNoCreated(String strIsAccountNoCreated) {
		this.strIsAccountNoCreated = strIsAccountNoCreated;
	}

	public String getStrIsCustIdCreated() {
		return strIsCustIdCreated;
	}

	public void setStrIsCustIdCreated(String strIsCustIdCreated) {
		this.strIsCustIdCreated = strIsCustIdCreated;
	}

	public Date getDateOfCreation() {
		return dateOfCreation;
	}

	public void setDateOfCreation(Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public String getStrCreatedBy() {
		return strCreatedBy;
	}

	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getAgeing() {
		return ageing;
	}

	public void setAgeing(String ageing) {
		this.ageing = ageing;
	}

	public String getStrDescription() {
		return strDescription;
	}

	public void setStrDescription(String strDescription) {
		this.strDescription = strDescription;
	}

	public String getStrDateOfRegistration() {
		return strDateOfRegistration;
	}

	public void setStrDateOfRegistration(String strDateOfRegistration) {
		this.strDateOfRegistration = strDateOfRegistration;
	}

	public String getStrCustId() {
		return strCustId;
	}

	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}
}
