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

@JsonAutoDetect
@Entity
@Table(name = "pool_account_master")
public class PullAccountModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "participant_id")
	private String strParticipantId;
	
	@Column(name = "poolaccount")
	private String strPoolAccount;
		
	@Column(name = "poolaccount_name")
	private String strPoolAccountName;

	@Column(name = "poolaccount_currency")
	private String strPoolAccountCurrency;
	
	@Column(name = "poolaccount_bankcode")
	private String strPoolAccountBankCode;
	
	@Column(name = "created_date")
	private Date strCreatedDate;


	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getStrPoolAccount() {
		return strPoolAccount;
	}

	public void setStrPoolAccount(String strPoolAccount) {
		this.strPoolAccount = strPoolAccount;
	}

	public String getStrPoolAccountName() {
		return strPoolAccountName;
	}

	public void setStrPoolAccountName(String strPoolAccountName) {
		this.strPoolAccountName = strPoolAccountName;
	}

	public String getStrPoolAccountCurrency() {
		return strPoolAccountCurrency;
	}

	public void setStrPoolAccountCurrency(String strPoolAccountCurrency) {
		this.strPoolAccountCurrency = strPoolAccountCurrency;
	}

	public String getStrPoolAccountBankCode() {
		return strPoolAccountBankCode;
	}

	public void setStrPoolAccountBankCode(String strPoolAccountBankCode) {
		this.strPoolAccountBankCode = strPoolAccountBankCode;
	}
	
	public Date getStrCreatedDate() {
		return strCreatedDate;
	}

	public void setStrCreatedDate(Date strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}

	public String getStrParticipantId() {
		return strParticipantId;
	}

	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}

	@Override
	public String toString() {
		return "PullAccountModel [strID=" + strID + ", strParticipantId=" + strParticipantId + ", strPoolAccount="
				+ strPoolAccount + ", strPoolAccountName=" + strPoolAccountName + ", strPoolAccountCurrency="
				+ strPoolAccountCurrency + ", strPoolAccountBankCode=" + strPoolAccountBankCode + ", strCreatedDate="
				+ strCreatedDate + "]";
	}
		
}
