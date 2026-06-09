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
@Table(name = "tier_account_master")
public class TierAccountMaster implements Serializable 
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "cust_id")
	private String strCustId;
	
	@Column(name = "account_type")
	private String strAccountType;
	
	@Column(name = "account_no")
	private String strAccountNo;
	
	@Column(name = "mobile_no")
	private String strMobileNo;
	
	@Column(name = "tier1_daily_cum_limit")
	private double strTier1DailyCumlimit;
	
	@Column(name = "tier2_daily_cum_limit")
	private double strTier2DailyCumlimit;
	
	@Column(name = "tier3_daily_cum_limit")
	private double strTier3DailyCumlimit;
	
	@Column(name = "available_tier1_daily_cum_limit")
	private double strAvailableTier1DailyCumlimit;
	
	@Column(name = "available_tier2_daily_cum_limit")
	private double strAvailableTier2DailyCumlimit;
	
	@Column(name = "available_tier3_daily_cum_limit")
	private double strAvailableTier3DailyCumlimit;
	
	@Column(name = "created_date")
	private Date strCreatedDate;
	
	@Column(name = "created_by")
	private String strCreatedBy;
	
	@Column(name = "tier1_cummulative_balance")
	private double tier1CummBalance;
	
	@Column(name = "tier2_cummulative_balance")
	private double tier2CummBalance;
	
	@Column(name = "tier3_cummulative_balance")
	private double tier3CummBalance;
	
	@Transient
	private String strCumulativeBalanceLimit;
	
	@Transient
	private String strActiveTier;
	
	@Transient
	private String strUpdatableDailyAvailableLimitQuery;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getStrCustId() {
		return strCustId;
	}

	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}

	public String getStrAccountType() {
		return strAccountType;
	}

	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}

	public String getStrMobileNo() {
		return strMobileNo;
	}

	public void setStrMobileNo(String strMobileNo) {
		this.strMobileNo = strMobileNo;
	}
	
	public double getStrTier1DailyCumlimit() {
		return strTier1DailyCumlimit;
	}

	public void setStrTier1DailyCumlimit(double strTier1DailyCumlimit) {
		this.strTier1DailyCumlimit = strTier1DailyCumlimit;
	}

	public double getStrTier2DailyCumlimit() {
		return strTier2DailyCumlimit;
	}

	public void setStrTier2DailyCumlimit(double strTier2DailyCumlimit) {
		this.strTier2DailyCumlimit = strTier2DailyCumlimit;
	}

	public double getStrTier3DailyCumlimit() {
		return strTier3DailyCumlimit;
	}

	public void setStrTier3DailyCumlimit(double strTier3DailyCumlimit) {
		this.strTier3DailyCumlimit = strTier3DailyCumlimit;
	}

	public double getStrAvailableTier1DailyCumlimit() {
		return strAvailableTier1DailyCumlimit;
	}

	public void setStrAvailableTier1DailyCumlimit(double strAvailableTier1DailyCumlimit) {
		this.strAvailableTier1DailyCumlimit = strAvailableTier1DailyCumlimit;
	}

	public double getStrAvailableTier2DailyCumlimit() {
		return strAvailableTier2DailyCumlimit;
	}

	public void setStrAvailableTier2DailyCumlimit(double strAvailableTier2DailyCumlimit) {
		this.strAvailableTier2DailyCumlimit = strAvailableTier2DailyCumlimit;
	}

	public double getStrAvailableTier3DailyCumlimit() {
		return strAvailableTier3DailyCumlimit;
	}

	public void setStrAvailableTier3DailyCumlimit(double strAvailableTier3DailyCumlimit) {
		this.strAvailableTier3DailyCumlimit = strAvailableTier3DailyCumlimit;
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

	public String getStrAccountNo() {
		return strAccountNo;
	}

	public void setStrAccountNo(String strAccountNo) {
		this.strAccountNo = strAccountNo;
	}

	public String getStrActiveTier() {
		return strActiveTier;
	}

	public void setStrActiveTier(String strActiveTier) {
		this.strActiveTier = strActiveTier;
	}

	public double getTier1CummBalance() {
		return tier1CummBalance;
	}

	public void setTier1CummBalance(double tier1CummBalance) {
		this.tier1CummBalance = tier1CummBalance;
	}

	public double getTier2CummBalance() {
		return tier2CummBalance;
	}

	public void setTier2CummBalance(double tier2CummBalance) {
		this.tier2CummBalance = tier2CummBalance;
	}

	public double getTier3CummBalance() {
		return tier3CummBalance;
	}

	public void setTier3CummBalance(double tier3CummBalance) {
		this.tier3CummBalance = tier3CummBalance;
	}

	public String getStrCumulativeBalanceLimit() {
		return strCumulativeBalanceLimit;
	}

	public void setStrCumulativeBalanceLimit(String strCumulativeBalanceLimit) {
		this.strCumulativeBalanceLimit = strCumulativeBalanceLimit;
	}

	public String getStrUpdatableDailyAvailableLimitQuery() {
		return strUpdatableDailyAvailableLimitQuery;
	}

	public void setStrUpdatableDailyAvailableLimitQuery(String strUpdatableDailyAvailableLimitQuery) {
		this.strUpdatableDailyAvailableLimitQuery = strUpdatableDailyAvailableLimitQuery;
	}

	@Override
	public String toString() {
		return "TierAccountMaster [strID=" + strID + ", strCustId=" + strCustId + ", strAccountType=" + strAccountType
				+ ", strAccountNo=" + strAccountNo + ", strMobileNo=" + strMobileNo + ", strTier1DailyCumlimit="
				+ strTier1DailyCumlimit + ", strTier2DailyCumlimit=" + strTier2DailyCumlimit
				+ ", strTier3DailyCumlimit=" + strTier3DailyCumlimit + ", strAvailableTier1DailyCumlimit="
				+ strAvailableTier1DailyCumlimit + ", strAvailableTier2DailyCumlimit=" + strAvailableTier2DailyCumlimit
				+ ", strAvailableTier3DailyCumlimit=" + strAvailableTier3DailyCumlimit + ", strCreatedDate="
				+ strCreatedDate + ", strCreatedBy=" + strCreatedBy + ", tier1CummBalance=" + tier1CummBalance
				+ ", tier2CummBalance=" + tier2CummBalance + ", tier3CummBalance=" + tier3CummBalance
				+ ", strCumulativeBalanceLimit=" + strCumulativeBalanceLimit + ", strActiveTier=" + strActiveTier + "]";
	}
}
