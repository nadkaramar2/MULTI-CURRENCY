package ams.cms.services.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.AccountTransactionLimitationDao;
import ams.cms.model.AccountTransactionLimitation;
import ams.cms.services.AccountTransactionLimitationService;

@Transactional
@Service
public class AccountTransactionLimitationServiceImpl implements AccountTransactionLimitationService
{
	
	@Autowired
	private AccountTransactionLimitationDao accountTransactionLimitationDao;
	
	@Override
	public AccountTransactionLimitation saveAccountTransactionLimitation(AccountTransactionLimitation accountTransactionLimitation) throws Exception 
	{
		accountTransactionLimitationDao.save(accountTransactionLimitation);
		return accountTransactionLimitation;
	}

	@Override
	public AccountTransactionLimitation getAccountTxnLimitBasedOnParam(AccountTransactionLimitation accountTransactionLimitation) throws Exception 
	{
		return accountTransactionLimitationDao.getAccountTxnLimitBasedOnParam(accountTransactionLimitation);
	}
}
