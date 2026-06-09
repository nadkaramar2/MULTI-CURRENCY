package ams.cms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import ams.cms.utility.Data;

@JsonAutoDetect
@Entity
@DynamicUpdate
@Table(name = "bank_details_info")
public class BankDetailsInfo implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@JsonProperty("name")
	@Column(name = "bank_name")
	private String strBankName;	
	
	@JsonProperty("bankCode")
	@Column(name = "bank_code")
	private String strBankCode;
	
	//@JsonProperty("nipCode")
	@JsonProperty("code")
	@Column(name = "nip_code")
	private String strNipCode;
	
	@JsonProperty("active")
	@Column(name = "active")
	private String strActive;
	
	@JsonProperty("abcSortCode")
	@Column(name = "abc_sort_code")
	private String strAbcSortCode;
	
	@JsonProperty("isOfi")
	@Column(name = "is_ofi")
	private String strIsOfi;	
	
	@JsonProperty("category")
	@Column(name = "bank_category")
	private String strBankCategory;	
	
	@Column(name = "creation_date_time")
	private Date creationDateTime;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getStrBankName() {
		return strBankName;
	}

	public void setStrBankName(String strBankName) {
		this.strBankName = strBankName;
	}

	public String getStrBankCode() {
		return strBankCode;
	}

	public void setStrBankCode(String strBankCode) {
		this.strBankCode = strBankCode;
	}

	public String getStrNipCode() {
		return strNipCode;
	}

	public void setStrNipCode(String strNipCode) {
		this.strNipCode = strNipCode;
	}

	public String getStrActive() {
		return strActive;
	}

	public void setStrActive(String strActive) {
		this.strActive = strActive;
	}

	public String getStrAbcSortCode() {
		return strAbcSortCode;
	}

	public void setStrAbcSortCode(String strAbcSortCode) {
		this.strAbcSortCode = strAbcSortCode;
	}

	public String getStrIsOfi() {
		return strIsOfi;
	}

	public void setStrIsOfi(String strIsOfi) {
		this.strIsOfi = strIsOfi;
	}

	

	public Date getCreationDateTime() {
		return creationDateTime;
	}



	public void setCreationDateTime(Date creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public String getStrBankCategory() {
		return strBankCategory;
	}

	public void setStrBankCategory(String strBankCategory) {
		this.strBankCategory = strBankCategory;
	}
	
	
}
