package ams.cms.api.model;

import java.io.Serializable;
import java.sql.Time;
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
@Table(name = "denomination_master")
public class DenominationMaster implements Serializable
{
	private static final long serialVersionUID = 5459581698798549309L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "txn_id")
	private String txnId;
	
	@Column(name = "txn_type")
	private String strTxnType;
	
	@Column(name = "txn_mode")
	private String strTxnMode;
	
	@Column(name = "txn_amount")
	private String strTxnAmount;
	
	@Column(name = "d_10")
	private Integer d10;
	
	@Column(name = "d_20")
	private Integer d20;
	
	@Column(name = "d_50")
	private Integer d50;
	
	@Column(name = "d_100")
	private Integer d100;
	
	@Column(name = "d_200")
	private Integer d200;
	
	@Column(name = "d_500")
	private Integer d500;
	
	@Column(name = "d_1000")
	private Integer d1000;
	
	@Column(name = "d_2000")
	private Integer d2000;

	@Transient
	private String strDate;

	@Transient
	private Time strTime;

	@Transient
	private String fromAccount;

	@Transient
	private String toAccount;

	@Transient
	private String strFromDate;

	@Transient
	private String strToDate;

	@Transient
	private String strAccountNumber;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getStrTxnType() {
		return strTxnType;
	}

	public void setStrTxnType(String strTxnType) {
		this.strTxnType = strTxnType;
	}

	public String getStrTxnMode() {
		return strTxnMode;
	}

	public void setStrTxnMode(String strTxnMode) {
		this.strTxnMode = strTxnMode;
	}

	public String getStrTxnAmount() {
		return strTxnAmount;
	}

	public void setStrTxnAmount(String strTxnAmount) {
		this.strTxnAmount = strTxnAmount;
	}

	public Integer getD10() {
		return d10;
	}

	public void setD10(Integer d10) {
		this.d10 = d10;
	}

	public Integer getD20() {
		return d20;
	}

	public void setD20(Integer d20) {
		this.d20 = d20;
	}

	public Integer getD50() {
		return d50;
	}

	public void setD50(Integer d50) {
		this.d50 = d50;
	}

	public Integer getD100() {
		return d100;
	}

	public void setD100(Integer d100) {
		this.d100 = d100;
	}

	public Integer getD200() {
		return d200;
	}

	public void setD200(Integer d200) {
		this.d200 = d200;
	}

	public Integer getD500() {
		return d500;
	}

	public void setD500(Integer d500) {
		this.d500 = d500;
	}

	public Integer getD1000() {
		return d1000;
	}

	public void setD1000(Integer d1000) {
		this.d1000 = d1000;
	}

	public Integer getD2000() {
		return d2000;
	}

	public void setD2000(Integer d2000) {
		this.d2000 = d2000;
	}

	public Time getStrTime() {
		return strTime;
	}

	public void setStrTime(Time strTime) {
		this.strTime = strTime;
	}

	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public String getStrFromDate() {
		return strFromDate;
	}

	public void setStrFromDate(String strFromDate) {
		this.strFromDate = strFromDate;
	}

	public String getStrToDate() {
		return strToDate;
	}

	public void setStrToDate(String strToDate) {
		this.strToDate = strToDate;
	}

	public String getStrAccountNumber() {
		return strAccountNumber;
	}

	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}

	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}
}
