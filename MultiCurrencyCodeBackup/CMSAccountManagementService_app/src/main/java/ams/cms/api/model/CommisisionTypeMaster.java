package ams.cms.api.model;

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
@Table(name = "commision_type_master")
public class CommisisionTypeMaster implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "commision_type")
	private String commisionType;
		
	@Column(name = "vat_type")
	private String vatType;
	
	@Column(name = "commision_description")
	private String vatDescription;
	
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

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getCommisionType() {
		return commisionType;
	}

	public void setCommisionType(String commisionType) {
		this.commisionType = commisionType;
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
}
