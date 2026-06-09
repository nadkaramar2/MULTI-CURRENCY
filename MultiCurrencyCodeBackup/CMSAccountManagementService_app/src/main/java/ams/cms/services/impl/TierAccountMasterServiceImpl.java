package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.TierAccountMasterDao;
import ams.cms.model.AccountTypeTierBasedLimit;
import ams.cms.model.TierAccountMaster;
import ams.cms.services.AccountTypeTierBasedLimitService;
import ams.cms.services.TierAccountMasterService;
import ams.cms.utility.Utils;

@Transactional
@Service
public class TierAccountMasterServiceImpl implements TierAccountMasterService
{
	@Autowired
	private TierAccountMasterDao tierAccountMasterDao;
	
	@Autowired
	private AccountTypeTierBasedLimitService accountTypeTierBasedLimitService;
	
	@Override
	public TierAccountMaster getTierBasedLimits(TierAccountMaster tierAccountMaster) {
		return tierAccountMasterDao.getTierBasedLimitsValues(tierAccountMaster);
	}

	@Override
	public int updateCummAvailableBalance(TierAccountMaster TierAccountMaster) {
		return tierAccountMasterDao.updateCummAvailableBalance(TierAccountMaster);
	}

	@Override
	public TierAccountMaster getTierInfoByCustID(TierAccountMaster tierAccountMaster)
	{
		return tierAccountMasterDao.getTierInfoByCustID(tierAccountMaster);
	}

	@Override
	public TierAccountMaster saveTierAccountMaster(TierAccountMaster tierAccountMaster) 
	{
		try 
		{
			AccountTypeTierBasedLimit accountTypeTierBasedLimit = new AccountTypeTierBasedLimit();
			accountTypeTierBasedLimit.setStrAccountType(tierAccountMaster.getStrAccountType());
			
			List<AccountTypeTierBasedLimit> accountTypeTierBasedLimits = accountTypeTierBasedLimitService.getTiersLimitsBasedonAccountType(accountTypeTierBasedLimit);
			
			for(AccountTypeTierBasedLimit acTierBasedLimit: accountTypeTierBasedLimits) 
			{
				if ("tier1".equalsIgnoreCase(acTierBasedLimit.getStrTierType())) 
				{
					tierAccountMaster.setStrTier1DailyCumlimit(Utils.stringToDouble(acTierBasedLimit.getStrDailyCumulativeTxnLimit()));
					tierAccountMaster.setStrAvailableTier1DailyCumlimit((Utils.stringToDouble(acTierBasedLimit.getStrDailyCumulativeTxnLimit())));
					tierAccountMaster.setTier1CummBalance(Utils.stringToDouble(acTierBasedLimit.getStrCumulativeBalanceLimit()));
				}
				else if("tier2".equalsIgnoreCase(acTierBasedLimit.getStrTierType())) 
				{
					tierAccountMaster.setStrTier2DailyCumlimit(Utils.stringToDouble(acTierBasedLimit.getStrDailyCumulativeTxnLimit()));
					tierAccountMaster.setStrAvailableTier2DailyCumlimit((Utils.stringToDouble(acTierBasedLimit.getStrDailyCumulativeTxnLimit())));
					tierAccountMaster.setTier2CummBalance(Utils.stringToDouble(acTierBasedLimit.getStrCumulativeBalanceLimit()));
				}
				else 
				{
					tierAccountMaster.setStrTier3DailyCumlimit(Utils.stringToDouble(acTierBasedLimit.getStrDailyCumulativeTxnLimit()));
					tierAccountMaster.setStrAvailableTier3DailyCumlimit((Utils.stringToDouble(acTierBasedLimit.getStrDailyCumulativeTxnLimit())));
					tierAccountMaster.setTier3CummBalance(Utils.stringToDouble(acTierBasedLimit.getStrCumulativeBalanceLimit()));
				}
			}
			tierAccountMaster.setStrCreatedDate(Utils.getCurrentDate());
			tierAccountMasterDao.save(tierAccountMaster);
			
			return tierAccountMaster;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int[] updatesBatchEntryOfTier1LimitsAccountMasterFields(List<TierAccountMaster> tier1AccountMasterlist) {
		return tierAccountMasterDao.updatesBatchEntryOfTier1LimitsAccountMasterFields(tier1AccountMasterlist);
	}

	@Override
	public int[] updatesBatchEntryOfTier2LimitsAccountMasterFields(List<TierAccountMaster> tier2AccountMasterlist) {
		return tierAccountMasterDao.updatesBatchEntryOfTier2LimitsAccountMasterFields(tier2AccountMasterlist);
	}

	@Override
	public int[] updatesBatchEntryOfTier3LimitsAccountMasterFields(List<TierAccountMaster> tier3AccountMasterlist) {
		return tierAccountMasterDao.updatesBatchEntryOfTier3LimitsAccountMasterFields(tier3AccountMasterlist);
	}

	@Override
	public int updateCummulativeDailyLimitBasedOnActiveTier(TierAccountMaster TierAccountMaster) {
		return tierAccountMasterDao.updateCummulativeDailyLimitBasedOnActiveTier(TierAccountMaster);
	}

	@Override
	public int[] updatesBatchEntryOfTierDailyLimitsAccountMasterFields(List<TierAccountMaster> tierAccountMasterlist) 
	{
		return tierAccountMasterDao.updatesBatchEntryOfTierDailyLimitsAccountMasterFields(tierAccountMasterlist);
	}

	@Override
	public List<TierAccountMaster> getTierAccountMasterDailyLimits() {
		return tierAccountMasterDao.getTierAccountMasterDailyLimits();
	}

}
