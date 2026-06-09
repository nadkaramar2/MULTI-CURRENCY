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
@Table(name = "multi_currency_wallet_account_type_master")
public class MultiCurrencyWalletAccountTypeMaster implements Serializable
{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "currency_code")
	private String strCurrencyCode;
	
	@Column(name = "priority")
	private int strPriority;
	
	@Column(name = "created_date")
	private Date strCreatedDate;
	
	@Column(name = "created_by")
	private String strCreatedBy;
	
	@Column(name = "gl_account_type")
	private String glAccountType;
	
	@Column(name = "gl_account_number")
	private String glAccountNumber;
	
	@Column(name = "base_currency_account_type")
	private String baseCurrencyAccountType;
	
	@Transient
	private String strClosingBalance;
	
	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrCurrencyCode() {
		return strCurrencyCode;
	}

	public void setStrCurrencyCode(String strCurrencyCode) {
		this.strCurrencyCode = strCurrencyCode;
	}

	public int getStrPriority() {
		return strPriority;
	}

	public void setStrPriority(int strPriority) {
		this.strPriority = strPriority;
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

	public String getStrClosingBalance() {
		return strClosingBalance;
	}

	public void setStrClosingBalance(String strClosingBalance) {
		this.strClosingBalance = strClosingBalance;
	}

	public String getGlAccountType() {
		return glAccountType;
	}

	public void setGlAccountType(String glAccountType) {
		this.glAccountType = glAccountType;
	}

	public String getGlAccountNumber() {
		return glAccountNumber;
	}

	public void setGlAccountNumber(String glAccountNumber) {
		this.glAccountNumber = glAccountNumber;
	}

	public String getBaseCurrencyAccountType() {
		return baseCurrencyAccountType;
	}

	public void setBaseCurrencyAccountType(String baseCurrencyAccountType) {
		this.baseCurrencyAccountType = baseCurrencyAccountType;
	}


	
	
}
