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

@JsonAutoDetect
@Entity
@Table(name = "montra_account_master")
public class MontraAccountMaster implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "cid")
	private String cid;
	
	@Column(name = "account_type")
	private String accountType;
	
	@Column(name = "account_number")
	private String accountNumber;
	
	@Column(name = "bid")
	private String bid;
	
	@Column(name = "cust_id")
	private String strCustId;
	
	@Column(name = "created_date")
	private Date strCreatedDate;
	
	@Column(name = "mcc_code")
	private String mccCode;	
	
	@Transient
	private String mobileNo;
	
	@Transient
	private String activeTier;
	
	@Transient
	private String strAccountType;
	
	@Transient
	private String accountCategory;
	
	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getStrCustId() {
		return strCustId;
	}

	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}

	public Date getStrCreatedDate() {
		return strCreatedDate;
	}

	public void setStrCreatedDate(Date strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getActiveTier() {
		return activeTier;
	}

	public void setActiveTier(String activeTier) {
		this.activeTier = activeTier;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getMccCode() {
		return mccCode;
	}

	public void setMccCode(String mccCode) {
		this.mccCode = mccCode;
	}
	public String getAccountCategory() {
		return accountCategory;
	}
	public void setAccountCategory(String accountCategory) {
		this.accountCategory = accountCategory;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public String toString() {
		return "MontraAccountMaster [strID=" + strID + ", cid=" + cid + ", accountType=" + accountType + ", bid=" + bid
				+ ", strCustId=" + strCustId + ", strCreatedDate=" + strCreatedDate + ", mccCode=" + mccCode
				+ ", mobileNo=" + mobileNo + ", activeTier=" + activeTier + ", strAccountType=" + strAccountType + "]";
	}
	
}
