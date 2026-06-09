package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.AccountTypeTierBasedLimitDao;
import ams.cms.model.AccountTypeTierBasedLimit;
import ams.cms.services.AccountTypeTierBasedLimitService;

@Transactional
@Service
public class AccountTypeTierBasedLimitServiceImpl implements AccountTypeTierBasedLimitService 
{
	@Autowired
	AccountTypeTierBasedLimitDao accountTypeTierBasedLimitDao; 
	
	@Override
	public AccountTypeTierBasedLimit getTierBasedLimits(AccountTypeTierBasedLimit accountTypeTierBasedLimit) 
	{
		return accountTypeTierBasedLimitDao.getTierBasedLimitsValues(accountTypeTierBasedLimit);
	}
	
	@Override
	public int[] batchEntryForAccountTypeTierLimit(List<AccountTypeTierBasedLimit> accountTypeTierBasedLimit) 
	{
		return accountTypeTierBasedLimitDao.batchEntryForAccountTypeTierLimit(accountTypeTierBasedLimit);
	}

	@Override
	public List<AccountTypeTierBasedLimit> getTiersLimitsBasedonAccountType(AccountTypeTierBasedLimit accountTypeTierBasedLimit) 
	{
		return accountTypeTierBasedLimitDao.getTiersLimitsBasedonAccountType(accountTypeTierBasedLimit);
	}

	@Override
	public int[] batchUpdateEntryForAccountTypeTierLimit(List<AccountTypeTierBasedLimit> accountTypeTierBasedLimitList) 
	{
		return accountTypeTierBasedLimitDao.batchUpdateEntryForAccountTypeTierLimit(accountTypeTierBasedLimitList);
	}
}
