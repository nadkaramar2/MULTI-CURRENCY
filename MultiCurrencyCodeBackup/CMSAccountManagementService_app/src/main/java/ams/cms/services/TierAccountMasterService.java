package ams.cms.services;

import java.util.List;

import ams.cms.model.TierAccountMaster;

public interface TierAccountMasterService 
{
	TierAccountMaster getTierBasedLimits(TierAccountMaster tierAccountMaster);
	
	int updateCummAvailableBalance(TierAccountMaster TierAccountMaster);
	
	TierAccountMaster getTierInfoByCustID(TierAccountMaster tierAccountMaster);
	
	TierAccountMaster saveTierAccountMaster(TierAccountMaster tierAccountMaster);
	
	int[] updatesBatchEntryOfTier1LimitsAccountMasterFields(List<TierAccountMaster> tier1AccountMasterlist);
	
	int[] updatesBatchEntryOfTier2LimitsAccountMasterFields(List<TierAccountMaster> tier2AccountMasterlist);
	
	int[] updatesBatchEntryOfTier3LimitsAccountMasterFields(List<TierAccountMaster> tier3AccountMasterlist);
	
	int updateCummulativeDailyLimitBasedOnActiveTier(TierAccountMaster TierAccountMaster);
	
	int[] updatesBatchEntryOfTierDailyLimitsAccountMasterFields(List<TierAccountMaster> tierAccountMasterlist);
	
	List<TierAccountMaster> getTierAccountMasterDailyLimits();
}
