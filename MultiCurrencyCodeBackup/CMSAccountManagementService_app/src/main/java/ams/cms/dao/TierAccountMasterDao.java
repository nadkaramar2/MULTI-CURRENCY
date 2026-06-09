package ams.cms.dao;

import java.util.List;

import ams.cms.model.TierAccountMaster;

public interface TierAccountMasterDao extends GenericDao<TierAccountMaster>
{
	TierAccountMaster getTierBasedLimitsValues(TierAccountMaster TierAccountMaster);
	
	int updateCummAvailableBalance(TierAccountMaster TierAccountMaster);
	
	TierAccountMaster getTierInfoByCustID(TierAccountMaster tierAccountMaster);
	
	int[] updatesBatchEntryOfTier1LimitsAccountMasterFields(List<TierAccountMaster> tier1AccountMasterlist);
	
	int[] updatesBatchEntryOfTier2LimitsAccountMasterFields(List<TierAccountMaster> tier2AccountMasterlist);
	
	int[] updatesBatchEntryOfTier3LimitsAccountMasterFields(List<TierAccountMaster> tier3AccountMasterlist);
	
	int updateCummulativeDailyLimitBasedOnActiveTier(TierAccountMaster TierAccountMaster);
	
	int[] updatesBatchEntryOfTierDailyLimitsAccountMasterFields(List<TierAccountMaster> tierAccountMasterlist);
	
	List<TierAccountMaster> getTierAccountMasterDailyLimits();
}
