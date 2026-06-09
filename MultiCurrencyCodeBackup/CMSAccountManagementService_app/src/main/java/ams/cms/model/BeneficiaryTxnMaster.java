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

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@DynamicUpdate
@Table(name = "beneficiary_txn")
public class BeneficiaryTxnMaster implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "txn_date")
	private Date txnDate;
	
	@Column(name = "txn_time")
	private Time txnTime;
	
	@Column(name = "txn_id")
	private String strTxnId;	
	
	@Column(name = "from_account_type")
	private String strFromAccountType;

	@Column(name = "from_account_no")
	private String strFromAccountNo;
	
	@Column(name = "txn_amount")
	private String strTxnAmount;
	
	@Column(name = "txn_status")
	private String strTxnStatus;
	
	@Column(name = "txn_type")
	private String strTxnType;
	
	@Column(name = "beneficiary_bank_name")
	private String strBeneficiaryBankName;
	
	@Column(name = "beneficiary_bank_ifsc")
	private String strBeneficiaryBankIfsc;
	
	@Column(name = "beneficiary_account_no")
	private String strBeneficiaryAccountNo;
	
	@Column(name = "beneficiary_account_name")
	private String strBeneficiaryAccountName;
	
	@Column(name = "beneficiary_vpa_id")
	private String strBeneficiaryVpaId;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public Date getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}

	public Time getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(Time txnTime) {
		this.txnTime = txnTime;
	}

	public String getStrTxnId() {
		return strTxnId;
	}

	public void setStrTxnId(String strTxnId) {
		this.strTxnId = strTxnId;
	}

	public String getStrFromAccountType() {
		return strFromAccountType;
	}

	public void setStrFromAccountType(String strFromAccountType) {
		this.strFromAccountType = strFromAccountType;
	}

	public String getStrFromAccountNo() {
		return strFromAccountNo;
	}

	public void setStrFromAccountNo(String strFromAccountNo) {
		this.strFromAccountNo = strFromAccountNo;
	}

	public String getStrTxnAmount() {
		return strTxnAmount;
	}

	public void setStrTxnAmount(String strTxnAmount) {
		this.strTxnAmount = strTxnAmount;
	}

	public String getStrTxnStatus() {
		return strTxnStatus;
	}

	public void setStrTxnStatus(String strTxnStatus) {
		this.strTxnStatus = strTxnStatus;
	}

	public String getStrTxnType() {
		return strTxnType;
	}

	public void setStrTxnType(String strTxnType) {
		this.strTxnType = strTxnType;
	}

	public String getStrBeneficiaryBankName() {
		return strBeneficiaryBankName;
	}

	public void setStrBeneficiaryBankName(String strBeneficiaryBankName) {
		this.strBeneficiaryBankName = strBeneficiaryBankName;
	}

	public String getStrBeneficiaryBankIfsc() {
		return strBeneficiaryBankIfsc;
	}

	public void setStrBeneficiaryBankIfsc(String strBeneficiaryBankIfsc) {
		this.strBeneficiaryBankIfsc = strBeneficiaryBankIfsc;
	}

	public String getStrBeneficiaryAccountNo() {
		return strBeneficiaryAccountNo;
	}

	public void setStrBeneficiaryAccountNo(String strBeneficiaryAccountNo) {
		this.strBeneficiaryAccountNo = strBeneficiaryAccountNo;
	}

	public String getStrBeneficiaryAccountName() {
		return strBeneficiaryAccountName;
	}

	public void setStrBeneficiaryAccountName(String strBeneficiaryAccountName) {
		this.strBeneficiaryAccountName = strBeneficiaryAccountName;
	}

	public String getStrBeneficiaryVpaId() {
		return strBeneficiaryVpaId;
	}

	public void setStrBeneficiaryVpaId(String strBeneficiaryVpaId) {
		this.strBeneficiaryVpaId = strBeneficiaryVpaId;
	}
}
