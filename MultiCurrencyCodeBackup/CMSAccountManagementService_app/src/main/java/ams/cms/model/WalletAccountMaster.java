package ams.cms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ams.cms.util.JsonDateDeserializer;
import ams.cms.util.JsonDateSerializer;

@JsonAutoDetect
@Entity
@Table(name = "wallet_account_master")
public class WalletAccountMaster implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "participant_id")
	private String strParticipantID;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "account_number")
	private String strAccountNumber;
	
	@Column(name = "wallet_account_number")
	private String strWalletAccountNumber;
	
	@Column(name = "mcc_code")
	private String strMccCode;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "creation_date")
	private Date strDateOfCreation;
	
	@Column(name = "created_by")
	private String strCreatedBy;
	
	@Column(name = "percentage")
	private String strPercentage;
	
	@Column(name="available_balance")
	private String strAvailableBalance;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
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

	public String getStrWalletAccountNumber() {
		return strWalletAccountNumber;
	}

	public void setStrWalletAccountNumber(String strWalletAccountNumber) {
		this.strWalletAccountNumber = strWalletAccountNumber;
	}

	public String getStrMccCode() {
		return strMccCode;
	}

	public void setStrMccCode(String strMccCode) {
		this.strMccCode = strMccCode;
	}

	public Date getStrDateOfCreation() {
		return strDateOfCreation;
	}

	public void setStrDateOfCreation(Date strDateOfCreation) {
		this.strDateOfCreation = strDateOfCreation;
	}

	public String getStrCreatedBy() {
		return strCreatedBy;
	}

	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}
	
	public String getStrPercentage() {
		return strPercentage;
	}
	
	public void setStrPercentage(String strPercentage) {
		this.strPercentage = strPercentage;
	}
	
	public String getStrAvailableBalance() {
		return strAvailableBalance;
	}

	public void setStrAvailableBalance(String strAvailableBalance) {
		this.strAvailableBalance = strAvailableBalance;
	}
}
