package ams.cms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name = "transaction_currency_master")
public class TransactionCurrencyMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "sweep_in")
	private double sweepIn;
	
	@Column(name = "sweep_out")
	private double sweepOut;
	
	@Column(name = "txn_id")
	private String txnId;
	
	@Column(name = "currency_code")
	private String currencyCode;
	
	@Column(name = "conversion_rate")
	private double conversionRate;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public double getSweepIn() {
		return sweepIn;
	}

	public void setSweepIn(double sweepIn) {
		this.sweepIn = sweepIn;
	}

	public double getSweepOut() {
		return sweepOut;
	}

	public void setSweepOut(double sweepOut) {
		this.sweepOut = sweepOut;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public double getConversionRate() {
		return conversionRate;
	}

	public void setConversionRate(double conversionRate) {
		this.conversionRate = conversionRate;
	}

	
	
	
	

}
