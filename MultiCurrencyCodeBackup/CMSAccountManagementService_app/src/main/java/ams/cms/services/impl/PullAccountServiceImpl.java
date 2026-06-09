package ams.cms.services.impl;



import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.PullAccountDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.PullAccountModel;
import ams.cms.services.PullAccountService;
import ams.cms.utility.Utils;

@Transactional
@Service
public class PullAccountServiceImpl implements PullAccountService 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(AccountLoadMasterServiceImpl.class);

	@Autowired
	PullAccountDao pullAccountDao; 

/*	@Override
	public PullAccountModel saveSendAccountMoneyList(PullAccountModel pullAccountModel) throws Exception
	{
		pullAccountDao.saveSendAccountMoneyList(pullAccountModel);
		return pullAccountModel;
	}
*/
	@Override
	public int updateSendAccountMoneyList(PullAccountModel pullAccountModel) 
	{
		//return pullAccountDao.updateSendAccountMoneyList(pullAccountModel);
		amsLogger.writeInfoLog("Inside updateAccountTranMastersColumn transactionConfig::"+pullAccountModel);
		AccountTranMaster accountTranMaster = new AccountTranMaster();
		try 
		{			
			pullAccountDao.updateSendAccountMoneyList(accountTranMaster);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
         return 0;
	}

	@Override
	public PullAccountModel getPullAccountModelByParticipantId(PullAccountModel pullAccountModel) {
	  return pullAccountDao.getPullAccountModelByParticipantId(pullAccountModel);
		
	}

	@Override
	public PullAccountModel savePullAccountMaster(PullAccountModel pullAccountModel) {
		pullAccountModel.setStrCreatedDate(Utils.getCurrentDate());
		pullAccountDao.save(pullAccountModel);
		return pullAccountModel;
	}
	
	@Override
	public PullAccountModel getPullAccountModelByBankCode(PullAccountModel pullAccountModel) {
		PullAccountModel pullAccountModelObj = pullAccountDao.getPullAccountModelByBankCode(pullAccountModel);
		return pullAccountModelObj;
	}

}
