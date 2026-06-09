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
@Table(name = "gl_account_load_master")
public class GLAccountLoadingMaster implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int strId;
	
	@Column(name = "account_type")
	private String strGLAccountType;
	
	@Column(name = "account_description")
	private String strGLAccountDescription;
	
	@Column(name = "account_number")
	private String strAccountNumber;
	
	@Column(name = "date_of_loading")
	private Date dateOfLoading;	
	
	@Column(name = "time_of_loading")
	private String strTimeOfLoading;	
	
	@Column(name = "tran_id")
	private String strTransactionId;	
	
	@Column(name = "channel")
	private String strChannel;	
	
	@Column(name = "loaded_amount")
	private String strLoadedBalance;	
	
	@Column(name = "account_created_by")
	private String strAccountCreatedBy;
	
	public int getStrId() {
		return strId;
	}
	public void setStrId(int strId) {
		this.strId = strId;
	}

	public String getStrGLAccountType() {
		return strGLAccountType;
	}

	public void setStrGLAccountType(String strGLAccountType) {
		this.strGLAccountType = strGLAccountType;
	}

	public String getStrGLAccountDescription() {
		return strGLAccountDescription;
	}

	public void setStrGLAccountDescription(String strGLAccountDescription) {
		this.strGLAccountDescription = strGLAccountDescription;
	}

	public String getStrAccountNumber() {
		return strAccountNumber;
	}

	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}

	public Date getDateOfLoading() {
		return dateOfLoading;
	}

	public void setDateOfLoading(Date dateOfLoading) {
		this.dateOfLoading = dateOfLoading;
	}

	public String getStrTimeOfLoading() {
		return strTimeOfLoading;
	}

	public void setStrTimeOfLoading(String strTimeOfLoading) {
		this.strTimeOfLoading = strTimeOfLoading;
	}

	public String getStrTransactionId() {
		return strTransactionId;
	}

	public void setStrTransactionId(String strTransactionId) {
		this.strTransactionId = strTransactionId;
	}

	public String getStrChannel() {
		return strChannel;
	}

	public void setStrChannel(String strChannel) {
		this.strChannel = strChannel;
	}

	public String getStrLoadedBalance() {
		return strLoadedBalance;
	}

	public void setStrLoadedBalance(String strLoadedBalance) {
		this.strLoadedBalance = strLoadedBalance;
	}

	public String getStrAccountCreatedBy() {
		return strAccountCreatedBy;
	}

	public void setStrAccountCreatedBy(String strAccountCreatedBy) {
		this.strAccountCreatedBy = strAccountCreatedBy;
	}	
}
