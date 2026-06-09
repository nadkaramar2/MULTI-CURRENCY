package ams.cms.model;

import java.io.Serializable;
import java.sql.Time;
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
@Table(name = "account_load_master")
public class AccountLoadMaster implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "participant_id")
	private String strParticipantId;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "account_number")
	private String strAccountNumber;
	
	@Column(name = "account_category")
	private String strAccountCategory;
	
	@Column(name = "loaded_balance")
	private String strLoadedBalance;
	
	@Column(name = "channel")
	private String strChannel;
	
	@Column(name = "date_of_loading")
	private Date dateOfLoading; 
	
	@Column(name = "time_of_loading")
	private Time strTimeOfLoading;
	
	@Column(name = "transaction_id")
	private String strTransactionId;
    
	@Column(name = "created_by")
	private String strCreatedBy;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

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

	public String getStrAccountCategory() {
		return strAccountCategory;
	}

	public void setStrAccountCategory(String strAccountCategory) {
		this.strAccountCategory = strAccountCategory;
	}

	public String getStrLoadedBalance() {
		return strLoadedBalance;
	}

	public void setStrLoadedBalance(String strLoadedBalance) {
		this.strLoadedBalance = strLoadedBalance;
	}

	public String getStrChannel() {
		return strChannel;
	}

	public void setStrChannel(String strChannel) {
		this.strChannel = strChannel;
	}

	public Date getDateOfLoading() {
		return dateOfLoading;
	}
	public void setDateOfLoading(Date dateOfLoading) {
		this.dateOfLoading = dateOfLoading;
	}
	
	public Time getStrTimeOfLoading() {
		return strTimeOfLoading;
	}

	public void setStrTimeOfLoading(Time strTimeOfLoading) {
		this.strTimeOfLoading = strTimeOfLoading;
	}

	public String getStrTransactionId() {
		return strTransactionId;
	}

	public void setStrTransactionId(String strTransactionId) {
		this.strTransactionId = strTransactionId;
	}

	public String getStrCreatedBy() {
		return strCreatedBy;
	}

	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}	
}
