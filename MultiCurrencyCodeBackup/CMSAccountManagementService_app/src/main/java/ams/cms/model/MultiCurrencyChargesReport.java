package ams.cms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonAutoDetect
@Entity
@Table(name = "multi_currency_charges_report")
public class MultiCurrencyChargesReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id;
	
	@Column(name = "participant_id")
	private String participantId;
	
	@Column(name = "charges_type")
	private String chargesType;
	
	@Column(name = "account_type")
	private String accountType;
	
	@Column(name = "account_number")
	private String accountNumber;
	
	@Column(name = "account_currency")
	private String accountCurrency;
	
	@Column(name = "gl_account_type")
	private String glAccountType;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "txn_date")
	private Date txnDate;
	
	@Column(name = "gl_account_number")
	private String glAccountNumber;
	

	@Column(name = "txn_type")
	private String txnType;
	
	@Column(name = "txn_id")
	private String txnId;
	
	@Column(name = "txn_amt")
	private double txnAmt;
	
	@Column(name = "base_currency_amt")
	private double baseCurrencyAmt;
	
	@Column(name = "base_currency")
	private String baseCurrency;
	
	@Column(name = "base_conversion_rate")
	private double baseConversionRate;
	
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public String getChargesType() {
		return chargesType;
	}

	public void setChargesType(String chargesType) {
		this.chargesType = chargesType;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	

	public double getBaseConversionRate() {
		return baseConversionRate;
	}

	public void setBaseConversionRate(double baseConversionRate) {
		this.baseConversionRate = baseConversionRate;
	}

	public void setBaseCurrencyAmt(double baseCurrencyAmt) {
		this.baseCurrencyAmt = baseCurrencyAmt;
	}

	public String getAccountCurrency() {
		return accountCurrency;
	}

	public void setAccountCurrency(String accountCurrency) {
		this.accountCurrency = accountCurrency;
	}

	public String getGlAccountType() {
		return glAccountType;
	}

	public void setGlAccountType(String glAccountType) {
		this.glAccountType = glAccountType;
	}

	public Date getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}

	public String getGlAccountNumber() {
		return glAccountNumber;
	}

	public void setGlAccountNumber(String glAccountNumber) {
		this.glAccountNumber = glAccountNumber;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public double getTxnAmt() {
		return txnAmt;
	}

	public void setTxnAmt(double txnAmt) {
		this.txnAmt = txnAmt;
	}

	
}
