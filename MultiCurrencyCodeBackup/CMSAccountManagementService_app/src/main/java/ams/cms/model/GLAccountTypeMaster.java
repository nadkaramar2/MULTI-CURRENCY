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
public class GLAccountTypeMaster implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "account_type")
	private String strGLAccountType;
	
	@Column(name = "account_description")
	private String strGLAccountDescription;
	
	@Column(name = "account_number")
	private String strAccountNumber;
	
	@Column(name = "opening_balance")
	private String strOpeningBalance;
	
	@Column(name = "closing_balance")
	private String strClosingBalance;
	
	@Column(name = "status")
	private String strStatus;
	
	@Column(name = "creation_date")
	private Date strCreatedDate;
	
	@Column(name = "created_by")
	private String strCreatedBy;
	
	@Transient
	private String strParticipantId;
	
	@Transient
	private String strAccountType;
	
	@Transient
	private String tranId;
	
	@Transient
	private String tranType;
	
	@Transient
	private String tranMode;
	
	@Transient
	private String userAccountType;	
	
	@Transient
	private String txnAmount;
	
	@Transient
	private String vat;
	
	@Transient
	private String fee;
	
	@Transient
	private Boolean isFeeApplicable = false;
	
	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
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

	public String getStrParticipantId() {
		return strParticipantId;
	}

	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public String getTranMode() {
		return tranMode;
	}

	public void setTranMode(String tranMode) {
		this.tranMode = tranMode;
	}

	public String getUserAccountType() {
		return userAccountType;
	}

	public void setUserAccountType(String userAccountType) {
		this.userAccountType = userAccountType;
	}

	public String getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrStatus() {
		return strStatus;
	}

	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}

	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public Boolean getIsFeeApplicable() {
		return isFeeApplicable;
	}

	public void setIsFeeApplicable(Boolean isFeeApplicable) {
		this.isFeeApplicable = isFeeApplicable;
	}

	@Override
	public String toString() {
		return "GLAccountTypeMaster [strID=" + strID + ", strGLAccountType=" + strGLAccountType
				+ ", strGLAccountDescription=" + strGLAccountDescription + ", strAccountNumber=" + strAccountNumber
				+ ", strOpeningBalance=" + strOpeningBalance + ", strClosingBalance=" + strClosingBalance
				+ ", strStatus=" + strStatus + ", strCreatedDate=" + strCreatedDate + ", strCreatedBy=" + strCreatedBy
				+ ", strParticipantId=" + strParticipantId + ", strAccountType=" + strAccountType + ", tranId=" + tranId
				+ ", tranType=" + tranType + ", tranMode=" + tranMode + ", userAccountType=" + userAccountType
				+ ", txnAmount=" + txnAmount + "]";
	}	
}
