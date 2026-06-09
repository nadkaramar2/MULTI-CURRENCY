package ams.cms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonAutoDetect
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Entity
@Table(name = "currency_transfer_master")
public class CurrencyTransferMaster {
	
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "sweep_out_currency_code")
	private String sweepOutCurrencyCode;
	
	@Column(name = "sweep_out_currency_value")
	private String sweepOutCurrencyValue;
	
	@Column(name = "sweep_in_currency_code")
	private String sweepInCurrencyCode;
	
	@Column(name = "sweep_in_currency_value")
	private String sweepInCurrencyValue;
	
	@Column(name = "sweep_in_amnt")
	private String sweepInAmount;
	
	@Column(name = "sweep_out_amnt")
	private String sweepOutAmount;
	
	@Column(name = "tran_id")
	private String tranId;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "base_account_number")
	private String baseAccountNumber;
	
	@Column(name = "base_account_type")
	private String baseAccountType;
	
	@Column(name = "fee_amount")
	private double feeAmount;
	
	@Column(name = "gst_amount")
	private double gstAmount;
	
	@Column(name = "created_date")
	private Date createdDate;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getSweepOutCurrencyCode() {
		return sweepOutCurrencyCode;
	}

	public void setSweepOutCurrencyCode(String sweepOutCurrencyCode) {
		this.sweepOutCurrencyCode = sweepOutCurrencyCode;
	}

	public String getSweepOutCurrencyValue() {
		return sweepOutCurrencyValue;
	}

	public void setSweepOutCurrencyValue(String sweepOutCurrencyValue) {
		this.sweepOutCurrencyValue = sweepOutCurrencyValue;
	}

	public String getSweepInCurrencyCode() {
		return sweepInCurrencyCode;
	}

	public void setSweepInCurrencyCode(String sweepInCurrencyCode) {
		this.sweepInCurrencyCode = sweepInCurrencyCode;
	}

	public String getSweepInCurrencyValue() {
		return sweepInCurrencyValue;
	}

	public void setSweepInCurrencyValue(String sweepInCurrencyValue) {
		this.sweepInCurrencyValue = sweepInCurrencyValue;
	}

	public String getSweepInAmount() {
		return sweepInAmount;
	}

	public void setSweepInAmount(String sweepInAmount) {
		this.sweepInAmount = sweepInAmount;
	}

	public String getSweepOutAmount() {
		return sweepOutAmount;
	}

	public void setSweepOutAmount(String sweepOutAmount) {
		this.sweepOutAmount = sweepOutAmount;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getBaseAccountNumber() {
		return baseAccountNumber;
	}

	public void setBaseAccountNumber(String baseAccountNumber) {
		this.baseAccountNumber = baseAccountNumber;
	}

	public String getBaseAccountType() {
		return baseAccountType;
	}

	public void setBaseAccountType(String baseAccountType) {
		this.baseAccountType = baseAccountType;
	}

	public double getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(double feeAmount) {
		this.feeAmount = feeAmount;
	}

	public double getGstAmount() {
		return gstAmount;
	}

	public void setGstAmount(double gstAmount) {
		this.gstAmount = gstAmount;
	}
}
