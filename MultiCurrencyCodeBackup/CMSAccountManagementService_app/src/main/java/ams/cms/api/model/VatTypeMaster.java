package ams.cms.api.model;

import java.io.Serializable;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(content = Include.NON_EMPTY,value = Include.NON_NULL)
@JsonAutoDetect
@Entity
@Table(name = "vat_type_master")
public class VatTypeMaster implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
		
	@Column(name = "vat_type")
	private String vatType;
	
	@Column(name = "vat_description")
	private String vatDescription;
	
	@Column(name = "status")
	private String status;
	
	@Transient
	private String glAccountNo;
	
	@Transient
	private String glAccountBalance;
	
	@Transient
	private String date;
	
	@Transient
	private Time time;
	

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getVatType() {
		return vatType;
	}

	public void setVatType(String vatType) {
		this.vatType = vatType;
	}

	public String getVatDescription() {
		return vatDescription;
	}

	public void setVatDescription(String vatDescription) {
		this.vatDescription = vatDescription;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGlAccountNo() {
		return glAccountNo;
	}

	public void setGlAccountNo(String glAccountNo) {
		this.glAccountNo = glAccountNo;
	}

	public String getGlAccountBalance() {
		return glAccountBalance;
	}

	public void setGlAccountBalance(String glAccountBalance) {
		this.glAccountBalance = glAccountBalance;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	
}
