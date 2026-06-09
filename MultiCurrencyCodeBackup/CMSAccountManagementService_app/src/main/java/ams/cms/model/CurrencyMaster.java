package ams.cms.model;

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
@Table(name = "currency_master")
public class CurrencyMaster {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "participant_id")
	private String strParticipantId;
	
	@Column(name = "currency_code")
	private String currencyCode;
	
	@Column(name = "currency_symbol")
	private String currencySymbol;
	
	@Column(name = "currency_descr")
	private String currencyDescr;
	
	@Column(name = "base_country")
	private String baseCountry;
	
	@Column(name = "currency_status")
	private String currencyStatus;
	
	@Column(name = "gl_account_type")
	private String glAccountType;
	
	@Column(name = "gl_account_number")
	private String glAccountNumber;
	
	@Column(name = "charges_type")
	private String chargesType;
	
	@Column(name = "network_type")
	private String networkType;
	
	@Column(name = "fee_type")
	private String feeType;
	
	@Column(name = "tcs_type")
	private String tcsType;
	
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_date")
	private String createdDate;

	@Transient
	private double feeAmount;
	
	@Transient
	private String feeGl;
	
	@Transient
	private String feeGLAccountNumber;
	
	@Transient
	private String gstType;
	
	@Transient
	private String gstGL;
	
	@Transient
	private String gstAccountNumber;
	
	@Transient
	private double gstPercentage;
	
	@Transient
	private String channelAccountNumber;
	
	@Transient
	private String channelGlAccountType;
	
	@Transient
	private String GstGlAccountType;
	
	@Transient
	private String FeeGlAccountType;
	
	@Transient
	private String strFlatFee;
	
	@Transient
	private String strFeePercentage;
	
	@Transient
	private String strIsFee;
	
	
	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getCurrencyDescr() {
		return currencyDescr;
	}

	public void setCurrencyDescr(String currencyDescr) {
		this.currencyDescr = currencyDescr;
	}

	public String getBaseCountry() {
		return baseCountry;
	}

	public void setBaseCountry(String baseCountry) {
		this.baseCountry = baseCountry;
	}

	

	public String getCurrencyStatus() {
		return currencyStatus;
	}

	public void setCurrencyStatus(String currencyStatus) {
		this.currencyStatus = currencyStatus;
	}

	public String getGlAccountType() {
		return glAccountType;
	}

	public void setGlAccountType(String glAccountType) {
		this.glAccountType = glAccountType;
	}

	public String getGlAccountNumber() {
		return glAccountNumber;
	}

	public void setGlAccountNumber(String glAccountNumber) {
		this.glAccountNumber = glAccountNumber;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getTcsType() {
		return tcsType;
	}

	public void setTcsType(String tcsType) {
		this.tcsType = tcsType;
	}

	public String getStrParticipantId() {
		return strParticipantId;
	}

	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}

	public String getChargesType() {
		return chargesType;
	}

	public void setChargesType(String chargesType) {
		this.chargesType = chargesType;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public double getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(double feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getFeeGl() {
		return feeGl;
	}

	public void setFeeGl(String feeGl) {
		this.feeGl = feeGl;
	}

	public String getFeeGLAccountNumber() {
		return feeGLAccountNumber;
	}

	public void setFeeGLAccountNumber(String feeGLAccountNumber) {
		this.feeGLAccountNumber = feeGLAccountNumber;
	}

	public String getGstType() {
		return gstType;
	}

	public void setGstType(String gstType) {
		this.gstType = gstType;
	}

	public String getGstGL() {
		return gstGL;
	}

	public void setGstGL(String gstGL) {
		this.gstGL = gstGL;
	}

	public String getGstAccountNumber() {
		return gstAccountNumber;
	}

	public void setGstAccountNumber(String gstAccountNumber) {
		this.gstAccountNumber = gstAccountNumber;
	}

	public double getGstPercentage() {
		return gstPercentage;
	}

	public void setGstPercentage(double gstPercentage) {
		this.gstPercentage = gstPercentage;
	}

	public String getChannelAccountNumber() {
		return channelAccountNumber;
	}

	public void setChannelAccountNumber(String channelAccountNumber) {
		this.channelAccountNumber = channelAccountNumber;
	}



	public String getChannelGlAccountType() {
		return channelGlAccountType;
	}

	public void setChannelGlAccountType(String channelGlAccountType) {
		this.channelGlAccountType = channelGlAccountType;
	}

	public String getGstGlAccountType() {
		return GstGlAccountType;
	}

	public void setGstGlAccountType(String gstGlAccountType) {
		GstGlAccountType = gstGlAccountType;
	}

	public String getFeeGlAccountType() {
		return FeeGlAccountType;
	}

	public void setFeeGlAccountType(String feeGlAccountType) {
		FeeGlAccountType = feeGlAccountType;
	}

	public String getStrFlatFee() {
		return strFlatFee;
	}

	public void setStrFlatFee(String strFlatFee) {
		this.strFlatFee = strFlatFee;
	}

	public String getStrFeePercentage() {
		return strFeePercentage;
	}

	public void setStrFeePercentage(String strFeePercentage) {
		this.strFeePercentage = strFeePercentage;
	}

	public String getStrIsFee() {
		return strIsFee;
	}

	public void setStrIsFee(String strIsFee) {
		this.strIsFee = strIsFee;
	}
	
}
