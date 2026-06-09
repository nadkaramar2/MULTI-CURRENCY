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

@JsonAutoDetect
@Entity
@Table(name = "fee_type_master")
public class FeeTypeMaster implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "fee_type")
	private String feeType;
	
	@Column(name = "fee_description")
	private String feeDescription;
	
	@Column(name = "vat_type")
	private String vatType;
	
	@Column(name = "status")
	private String status;
	
	@Transient
	private String glAccountNo;
	
	@Transient
	private String glAccountBalance;
	
	@Transient
	private String glAccountType;
	
	@Transient
	private String glAccountRef;
	
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

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getFeeDescription() {
		return feeDescription;
	}

	public void setFeeDescription(String feeDescription) {
		this.feeDescription = feeDescription;
	}

	public String getVatType() {
		return vatType;
	}

	public void setVatType(String vatType) {
		this.vatType = vatType;
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

	public String getGlAccountType() {
		return glAccountType;
	}

	public void setGlAccountType(String glAccountType) {
		this.glAccountType = glAccountType;
	}

	public String getGlAccountRef() {
		return glAccountRef;
	}

	public void setGlAccountRef(String glAccountRef) {
		this.glAccountRef = glAccountRef;
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
