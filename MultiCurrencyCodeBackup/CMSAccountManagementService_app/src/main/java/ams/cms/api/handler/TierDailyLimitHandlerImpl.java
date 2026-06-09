package ams.cms.api.handler;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.dao.impl.TierAccountMasterDaoImpl;
import ams.cms.logger.AMSLogger;
import ams.cms.model.TierAccountMaster;
import ams.cms.services.TierAccountMasterService;

@Component("TierDailyLimitHandler")
public class TierDailyLimitHandlerImpl implements TierDailyLimitHandler
{
	private AMSLogger amsLogger = AMSLogger.getInstance(TierAccountMasterDaoImpl.class);
	
	@Autowired
	private TierAccountMasterService tierAccountMasterService;

	@Transactional
	@Override
	public void updateDailyTierLimits(String data) 
	{
		try 
		{
			List<TierAccountMaster> tierAccountMasters = tierAccountMasterService.getTierAccountMasterDailyLimits();
			if (tierAccountMasters!=null) 
			{
				int[] result = tierAccountMasterService.updatesBatchEntryOfTierDailyLimitsAccountMasterFields(tierAccountMasters);
				amsLogger.writeInfoLog("updateDailyTierLimits result=["+result+"]");
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
}
