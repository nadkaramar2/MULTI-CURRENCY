package ams.cms.model;



import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonAutoDetect
@Entity
@Table(name = "multi_currency_wallet_account_master")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class MultiCurrencyWalletAccountMaster implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "account_number")
	private String strAccountNumber;
	
	@Column(name = "currency_wallet_account_number")
	private String strCurrencyWalletAccountNumber;
	
	@Column(name = "closing_balance")
	private double strClosingBalance;
	
	@Column(name = "base_currency_account_type")
	private String baseCurrencyAccountType;
	
	@Column(name = "priority")
	private int  strPriority;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "created_date")
	private Date strCreatedDate;
	
	@Column(name = "created_by")
	private String strCreatedBy;
	
	@Column(name = "currency_code")
	private String strCurrencyCode;
	
	@Column(name = "participant_id")
	private String participantId;
	
	@Column(name = "ear_mark")
	private double  strEarAmount;
	
	@Transient
	private String linkedGlType;
	
	@Transient
	private String linkedGlNumber;
	
	@Transient
	private String pin;
	
	@Transient
	private String custId;
	
	@Transient
	private List<PriorityWalletResponse> priorityWalletList;

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

	public String getStrAccountNumber() {
		return strAccountNumber;
	}

	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}


	public double getStrClosingBalance() {
		return strClosingBalance;
	}

	public void setStrClosingBalance(double strClosingBalance) {
		this.strClosingBalance = strClosingBalance;
	}

	

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public int getStrPriority() {
		return strPriority;
	}

	public void setStrPriority(int strPriority) {
		this.strPriority = strPriority;
	}

	public Date getStrCreatedDate() {
		return strCreatedDate;
	}

	public void setStrCreatedDate(Date strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}

	public String getStrCreatedBy() {
		return strCreatedBy;
	}

	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}

	public String getStrCurrencyCode() {
		return strCurrencyCode;
	}

	public void setStrCurrencyCode(String strCurrencyCode) {
		this.strCurrencyCode = strCurrencyCode;
	}

	public String getStrCurrencyWalletAccountNumber() {
		return strCurrencyWalletAccountNumber;
	}

	public void setStrCurrencyWalletAccountNumber(String strCurrencyWalletAccountNumber) {
		this.strCurrencyWalletAccountNumber = strCurrencyWalletAccountNumber;
	}

	public double getStrEarAmount() {
		return strEarAmount;
	}

	public void setStrEarAmount(double strEarAmount) {
		this.strEarAmount = strEarAmount;
	}

	public String getLinkedGlType() {
		return linkedGlType;
	}

	public void setLinkedGlType(String linkedGlType) {
		this.linkedGlType = linkedGlType;
	}

	public String getLinkedGlNumber() {
		return linkedGlNumber;
	}

	public void setLinkedGlNumber(String linkedGlNumber) {
		this.linkedGlNumber = linkedGlNumber;
	}

	public String getBaseCurrencyAccountType() {
		return baseCurrencyAccountType;
	}

	public void setBaseCurrencyAccountType(String baseCurrencyAccountType) {
		this.baseCurrencyAccountType = baseCurrencyAccountType;
	}

	public List<PriorityWalletResponse> getPriorityWalletList() {
		return priorityWalletList;
	}

	public void setPriorityWalletList(List<PriorityWalletResponse> priorityWalletList) {
		this.priorityWalletList = priorityWalletList;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}
	
}
