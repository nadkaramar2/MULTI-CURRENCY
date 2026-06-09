package ams.cms.api.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.AccountCreditLimitDao;
import ams.cms.api.model.AccountCreditLimit;
import ams.cms.api.service.AccountCreditLimtService;

@Transactional
@Service
public class AccountCreditLimitServiceImpl implements AccountCreditLimtService{

	@Autowired
	AccountCreditLimitDao accountCreditLimitDao;
	
	@Override
	public AccountCreditLimit getAccountCreditLimitCategoryObj(AccountCreditLimit accountCreditLimit) throws Exception 
	{
		return accountCreditLimitDao.getAccountCreditLimitCategoryObj(accountCreditLimit);
	}
	
	@Override
	public List<AccountCreditLimit> getAccountCreditLimitCategoriesListByParticipantWise(String participantId) throws Exception 
	{
		return accountCreditLimitDao.getAccountCreditLimitCategoriesListByParticipantWise(participantId);
	}

	@Override
	public AccountCreditLimit saveAccountCreditLimitCategory(AccountCreditLimit accountCreditLimit) throws Exception 
	{
		accountCreditLimitDao.save(accountCreditLimit);
		return accountCreditLimit;
	}
	
}
