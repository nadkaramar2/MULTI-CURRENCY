package ams.cms.api.service.impl;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.AccountMasterApiDao;
import ams.cms.api.model.AccountCreditLimit;
import ams.cms.api.model.AccountLimitRequest;
import ams.cms.api.model.AccountMaster;
import ams.cms.api.service.AccountMasterApiService;
import ams.cms.model.AccountCreation;

@Transactional
@Service
public class AccountMasterApiServiceImpl implements AccountMasterApiService
{
	@Autowired
	private AccountMasterApiDao accountMasterApiDao;
	
	@Override
	public AccountMaster getAvailableMonthlyAndDailyLimit(AccountLimitRequest accountLimitRequest) {
		try {
		AccountMaster availableMonthlyAndDailyLimit = accountMasterApiDao.getAvailableMonthlyAndDailyLimit(accountLimitRequest);
		return availableMonthlyAndDailyLimit;
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getAccTypeOnCustId(AccountMaster accountMaster) throws Exception {
	List<String> accTypeOnCustId = accountMasterApiDao.getAccTypeOnCustId(accountMaster);
		return accTypeOnCustId;
	}
	
	
	@Override
	public AccountMaster getCreditFlagOnCustId(AccountMaster accountMaster) {
		return accountMasterApiDao.getCreditFlagOnCustId(accountMaster);
	}

	@Override
	public AccountMaster getUpdateOrAvailableTxnlimits(AccountMaster accontMaster) throws Exception 
	{
		return accountMasterApiDao.getUpdateOrAvailableTxnlimits(accontMaster);
	}
	
	@Override
	public AccountMaster getUpdateOrAvailableCreditlimits(AccountMaster accontMaster) {
		return accountMasterApiDao.getUpdateOrAvailableCreditlimits(accontMaster);
	}
	
	@Override
	public AccountMaster getCreditLimitAmount(AccountMaster accountMaster) {
		return accountMasterApiDao.getCreditLimitAmount(accountMaster);
	}

	@Override
	public int updatecreditLimitAmount(AccountMaster accountMaster) {
	   return accountMasterApiDao.updatecreditLimitAmount(accountMaster);
	}
}
