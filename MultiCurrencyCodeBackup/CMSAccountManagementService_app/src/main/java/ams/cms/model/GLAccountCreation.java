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
@Table(name = "gl_account_type_master")
public class GLAccountCreation implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strId;
	
	@Column(name = "participant_id")
	private String strParticipantId;
	
	@Column(name = "account_type")
	private String strGLAccountType;
	
	@Column(name = "account_description")
	private String strGLDescription;
	
	@Column(name = "account_number")
	private String strGLAccountNumber;
	
	@Column(name = "opening_balance")
	private String strOpeningBalance;
	
	@Column(name = "closing_balance")
	private String strClosingBalance;
	
	@Column(name = "status")
	private String strStatus;
	
	@Column(name = "creation_date")
	private Date creationDate;
	
	@Column(name = "created_by")
	private String strCreatedBy;
	
	@Column(name = "third_party_allow")
	private String strThirdPartyAllow;
	
	@Transient
	private String strCreatedDate;
	
	@Transient
	private String strControlAccountBalance;
	
	@Transient
	private String strIsThirdPartyTransfer;
	
	@Transient
	private String strTransferTranType;
	
	@Transient
	private String strTransferAmount;
	
	@Transient
	private String strAllGLBalance;
	

	public String getStrId() {
		return strId;
	}

	public void setStrId(String strId) {
		this.strId = strId;
	}

	public String getStrParticipantId() {
		return strParticipantId;
	}

	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}

	public String getStrGLAccountType() {
		return strGLAccountType;
	}

	public void setStrGLAccountType(String strGLAccountType) {
		this.strGLAccountType = strGLAccountType;
	}

	public String getStrGLDescription() {
		return strGLDescription;
	}

	public void setStrGLDescription(String strGLDescription) {
		this.strGLDescription = strGLDescription;
	}

	public String getStrGLAccountNumber() {
		return strGLAccountNumber;
	}

	public void setStrGLAccountNumber(String strGLAccountNumber) {
		this.strGLAccountNumber = strGLAccountNumber;
	}

	public String getStrOpeningBalance() {
		return strOpeningBalance;
	}

	public void setStrOpeningBalance(String strOpeningBalance) {
		this.strOpeningBalance = strOpeningBalance;
	}

	public String getStrClosingBalance() {
		return strClosingBalance;
	}

	public void setStrClosingBalance(String strClosingBalance) {
		this.strClosingBalance = strClosingBalance;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getStrCreatedDate() {
		return strCreatedDate;
	}

	public void setStrCreatedDate(String strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}

	public String getStrCreatedBy() {
		return strCreatedBy;
	}

	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}

	public String getStrThirdPartyAllow() {
		return strThirdPartyAllow;
	}

	public void setStrThirdPartyAllow(String strThirdPartyAllow) {
		this.strThirdPartyAllow = strThirdPartyAllow;
	}

	public String getStrStatus() {
		return strStatus;
	}

	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}

	public String getStrControlAccountBalance() {
		return strControlAccountBalance;
	}

	public void setStrControlAccountBalance(String strControlAccountBalance) {
		this.strControlAccountBalance = strControlAccountBalance;
	}

	public String getStrIsThirdPartyTransfer() {
		return strIsThirdPartyTransfer;
	}

	public void setStrIsThirdPartyTransfer(String strIsThirdPartyTransfer) {
		this.strIsThirdPartyTransfer = strIsThirdPartyTransfer;
	}

	public String getStrTransferTranType() {
		return strTransferTranType;
	}

	public void setStrTransferTranType(String strTransferTranType) {
		this.strTransferTranType = strTransferTranType;
	}

	public String getStrTransferAmount() {
		return strTransferAmount;
	}

	public void setStrTransferAmount(String strTransferAmount) {
		this.strTransferAmount = strTransferAmount;
	}

	public String getStrAllGLBalance() {
		return strAllGLBalance;
	}

	public void setStrAllGLBalance(String strAllGLBalance) {
		this.strAllGLBalance = strAllGLBalance;
	}
}
