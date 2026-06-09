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
@Table(name = "card_account_linkage_master")
public class CardAccountLinkage implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int strID;
	
	@Column(name = "participant_id")
	private String strParticipantID;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "account_number")
	private String strAccountNumber;
	
	@Column(name = "card_number")
	private String strCardNumber;
	
	@Column(name = "card_id")
	private String strCardId;
	
	@Column(name = "card_type")
	private String strCardType;
	
	@Column(name = "card_description")
	private String strCardDescription;
	
	@Column(name = "enc_card_number")
	private String strCardEncCard;	
	
	@Column(name = "card_status")
	private String strCardStatus;
	
	@Column(name = "account_status")
	private String strAccountStatus;
	
	@Column(name = "card_holder_name")
	private String strCardHolderName;
	
	@Column(name = "network_type")
	private String strNetworkType;
	
	@Column(name = "card_expiry_date")
	private String strCardExpDate;
	
	@Column(name = "card_cvv")
	private String strCardCvv;
	
	@Column(name = "creation_date")
	private Date creationDate;	
	
	@Column(name = "created_by")
	private String strCreatedBy;
	
	@Column(name = "token_card")
	private String strTokenCard;
	
	@Transient
	private String strCreationDate;
	
	@Transient
	private String strCustId;
	
	@Transient
	private String strPIN;
	
	@Transient
	private String oldTokenCard;

	public int getStrID() {
		return strID;
	}

	public void setStrID(int strID) {
		this.strID = strID;
	}

	public String getStrParticipantID() {
		return strParticipantID;
	}

	public void setStrParticipantID(String strParticipantID) {
		this.strParticipantID = strParticipantID;
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

	public String getStrCardNumber() {
		return strCardNumber;
	}

	public void setStrCardNumber(String strCardNumber) {
		this.strCardNumber = strCardNumber;
	}

	public String getStrCardType() {
		return strCardType;
	}

	public void setStrCardType(String strCardType) {
		this.strCardType = strCardType;
	}

	public String getStrCardStatus() {
		return strCardStatus;
	}

	public void setStrCardStatus(String strCardStatus) {
		this.strCardStatus = strCardStatus;
	}

	public String getStrAccountStatus() {
		return strAccountStatus;
	}

	public void setStrAccountStatus(String strAccountStatus) {
		this.strAccountStatus = strAccountStatus;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getStrCreationDate() {
		return strCreationDate;
	}

	public void setStrCreationDate(String strCreationDate) {
		this.strCreationDate = strCreationDate;
	}

	public String getStrCreatedBy() {
		return strCreatedBy;
	}

	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}

	public String getStrCardId() {
		return strCardId;
	}

	public void setStrCardId(String strCardId) {
		this.strCardId = strCardId;
	}

	public String getStrCardDescription() {
		return strCardDescription;
	}

	public void setStrCardDescription(String strCardDescription) {
		this.strCardDescription = strCardDescription;
	}

	public String getStrCardHolderName() {
		return strCardHolderName;
	}

	public void setStrCardHolderName(String strCardHolderName) {
		this.strCardHolderName = strCardHolderName;
	}

	public String getStrNetworkType() {
		return strNetworkType;
	}

	public void setStrNetworkType(String strNetworkType) {
		this.strNetworkType = strNetworkType;
	}

	public String getStrCardExpDate() {
		return strCardExpDate;
	}

	public void setStrCardExpDate(String strCardExpDate) {
		this.strCardExpDate = strCardExpDate;
	}

	public String getStrCardCvv() {
		return strCardCvv;
	}
	public void setStrCardEncCard(String strCardEncCard) {
		this.strCardEncCard = strCardEncCard;
	}
	
	public void setStrCardCvv(String strCardCvv) {
		this.strCardCvv = strCardCvv;
	}

	public String getStrCustId() {
		return strCustId;
	}

	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}

	public String getStrCardEncCard() {
		return strCardEncCard;
	}

	public String getStrTokenCard() {
		return strTokenCard;
	}

	public void setStrTokenCard(String strTokenCard) {
		this.strTokenCard = strTokenCard;
	}

	public String getStrPIN() {
		return strPIN;
	}

	public void setStrPIN(String strPIN) {
		this.strPIN = strPIN;
	}

	public String getOldTokenCard() {
		return oldTokenCard;
	}

	public void setOldTokenCard(String oldTokenCard) {
		this.oldTokenCard = oldTokenCard;
	}
}
