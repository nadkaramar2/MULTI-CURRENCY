package ams.cms.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;


public class CardAccountLinkageResponse implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String strParticipantId;
	
	private String strAccountType;
	
	private String strAccountNumber;
	
	private String strCardNumber;
	
	private String strCardType;

	private String strCardStatus;
	
	private String strAccountStatus;
	
	private String strCardHolderName;
	
	private String strNetworkType;
	
	private String strCardExpDate;
	
	private String strCardCvv;
	
	private Date creation_date;
	
	private String strCardId;
	
	private String strCardDescription;
	
	private String strTokenCard;

	public String getStrParticipantId() {
		return strParticipantId;
	}

	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
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

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
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

	public void setStrCardCvv(String strCardCvv) {
		this.strCardCvv = strCardCvv;
	}

	public String getStrTokenCard() {
		return strTokenCard;
	}

	public void setStrTokenCard(String strTokenCard) {
		this.strTokenCard = strTokenCard;
	}
}
