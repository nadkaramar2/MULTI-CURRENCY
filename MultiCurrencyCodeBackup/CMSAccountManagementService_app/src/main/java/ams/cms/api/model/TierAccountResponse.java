package ams.cms.api.model;

import java.io.Serializable;
import java.util.HashMap;

public class TierAccountResponse implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String strCustId;
	private String strAccountNumber;
	private String strActiveTier;	
	private String accountAvailableBalance;
	
	private String tier1CumBalance;	
	private String tier2CumBalance;	
	private String tier3CumBalance;
	
	private String strTier1DailyCumlimit;	
	private String strTier2DailyCumlimit;	
	private String strTier3DailyCumlimit;	
	
	private String availableTier1DailyCumLimit;	
	private String availableTier2DailyCumLimit;	
	private String availableTier3DailyCumLimit;
	
	private String vat;
	private String fee;
	private Boolean isFeeApplicable = false;
	private Boolean isFeeApplicableToRecipent = false;
	
	private HashMap<String, String> tierWiseCummulativeBalanceMap = new HashMap<String, String>();
	private HashMap<String, String> tierWiseDailyCummulativeLimitMap = new HashMap<String, String>();
	private HashMap<String, String> tierWiseDailyCummulativeAvailableLimit = new HashMap<String, String>();
	
	public String getStrActiveTier() {
		return strActiveTier;
	}
	public void setStrActiveTier(String strActiveTier) {
		this.strActiveTier = strActiveTier;
	}
	public String getTier1CumBalance() {
		return tier1CumBalance;
	}
	public void setTier1CumBalance(String tier1CumBalance) 
	{
		this.tier1CumBalance = tier1CumBalance;
		this.tierWiseCummulativeBalanceMap.put("tier1", this.tier1CumBalance);
	}
	public String getTier2CumBalance() {
		return tier2CumBalance;
	}
	public void setTier2CumBalance(String tier2CumBalance) {
		this.tier2CumBalance = tier2CumBalance;
		this.tierWiseCummulativeBalanceMap.put("tier2", this.tier2CumBalance);
	}
	public String getTier3CumBalance() {
		return tier3CumBalance;
	}
	public void setTier3CumBalance(String tier3CumBalance) {
		this.tier3CumBalance = tier3CumBalance;
		this.tierWiseCummulativeBalanceMap.put("tier3", this.tier3CumBalance);
	}
	public String getStrTier1DailyCumlimit() {
		return strTier1DailyCumlimit;
	}
	public void setStrTier1DailyCumlimit(String strTier1DailyCumlimit) {
		this.strTier1DailyCumlimit = strTier1DailyCumlimit;
		this.tierWiseDailyCummulativeLimitMap.put("tier1", strTier1DailyCumlimit);
	}
	public String getStrTier2DailyCumlimit() {
		return strTier2DailyCumlimit;
	}
	public void setStrTier2DailyCumlimit(String strTier2DailyCumlimit) {
		this.strTier2DailyCumlimit = strTier2DailyCumlimit;
		this.tierWiseDailyCummulativeLimitMap.put("tier2", strTier2DailyCumlimit);
	}
	public String getStrTier3DailyCumlimit() {
		return strTier3DailyCumlimit;
	}
	public void setStrTier3DailyCumlimit(String strTier3DailyCumlimit) {
		this.strTier3DailyCumlimit = strTier3DailyCumlimit;
		this.tierWiseDailyCummulativeLimitMap.put("tier3", strTier3DailyCumlimit);
	}
	public String getAvailableTier1DailyCumLimit() {
		return availableTier1DailyCumLimit;
	}
	public void setAvailableTier1DailyCumLimit(String availableTier1DailyCumLimit) {
		this.availableTier1DailyCumLimit = availableTier1DailyCumLimit;
		this.tierWiseDailyCummulativeAvailableLimit.put("tier1", availableTier1DailyCumLimit);
		this.tierWiseDailyCummulativeAvailableLimit.put("tier1_updatable", "available_tier1_daily_cum_limit = ");
	}
	public String getAvailableTier2DailyCumLimit() {
		return availableTier2DailyCumLimit;
	}
	public void setAvailableTier2DailyCumLimit(String availableTier2DailyCumLimit) 
	{
		this.availableTier2DailyCumLimit = availableTier2DailyCumLimit;
		this.tierWiseDailyCummulativeAvailableLimit.put("tier2", availableTier2DailyCumLimit);
		this.tierWiseDailyCummulativeAvailableLimit.put("tier2_updatable", "available_tier2_daily_cum_limit = ");
	}
	public String getAvailableTier3DailyCumLimit() {
		return availableTier3DailyCumLimit;
	}
	public void setAvailableTier3DailyCumLimit(String availableTier3DailyCumLimit) {
		this.availableTier3DailyCumLimit = availableTier3DailyCumLimit;
		this.tierWiseDailyCummulativeAvailableLimit.put("tier3", availableTier3DailyCumLimit);
		this.tierWiseDailyCummulativeAvailableLimit.put("tier3_updatable", "available_tier3_daily_cum_limit = ");
	}
	public HashMap<String, String> getTierWiseCummulativeBalanceMap() {
		return tierWiseCummulativeBalanceMap;
	}
	public HashMap<String, String> getTierWiseDailyCummulativeLimitMap() {
		return tierWiseDailyCummulativeLimitMap;
	}
	public HashMap<String, String> getTierWiseDailyCummulativeAvailableLimit() {
		return tierWiseDailyCummulativeAvailableLimit;
	}
	public String getAccountAvailableBalance() {
		return accountAvailableBalance;
	}
	public void setAccountAvailableBalance(String accountAvailableBalance) {
		this.accountAvailableBalance = accountAvailableBalance;
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
	public Boolean getIsFeeApplicableToRecipent() {
		return isFeeApplicableToRecipent;
	}
	public void setIsFeeApplicableToRecipent(Boolean isFeeApplicableToRecipent) {
		this.isFeeApplicableToRecipent = isFeeApplicableToRecipent;
	}
	public String getStrCustId() {
		return strCustId;
	}
	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}
	public String getStrAccountNumber() {
		return strAccountNumber;
	}
	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}
}
