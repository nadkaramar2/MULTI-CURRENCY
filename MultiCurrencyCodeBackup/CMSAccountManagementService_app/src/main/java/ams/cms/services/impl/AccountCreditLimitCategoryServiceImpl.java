package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.AccountCreditLimitCategoryDao;
import ams.cms.model.AccountCreditLimitCategory;
import ams.cms.services.AccountCreditLimitCategoryService;

@Transactional
@Service
public class AccountCreditLimitCategoryServiceImpl implements AccountCreditLimitCategoryService
{
	@Autowired
	AccountCreditLimitCategoryDao accountCreditLimitCategoryDao;
	
	@Override
	public AccountCreditLimitCategory getAccountCreditLimitCategoryObj(AccountCreditLimitCategory accountCreditLimitCategory) throws Exception 
	{
		return accountCreditLimitCategoryDao.getAccountCreditLimitCategoryObj(accountCreditLimitCategory);
	}
	
	@Override
	public List<AccountCreditLimitCategory> getAccountCreditLimitCategoriesListByParticipantWise(String participantId) throws Exception 
	{
		return accountCreditLimitCategoryDao.getAccountCreditLimitCategoriesListByParticipantWise(participantId);
	}

	@Override
	public AccountCreditLimitCategory saveAccountCreditLimitCategory(AccountCreditLimitCategory accountCreditLimitCategory) throws Exception 
	{
		accountCreditLimitCategoryDao.save(accountCreditLimitCategory);
		return accountCreditLimitCategory;
	}
	@Override
	public boolean isCreditTypeAlreadyExist(AccountCreditLimitCategory accountCreditLimitCategory) throws Exception 
	{
		return accountCreditLimitCategoryDao.isCreditTypeAlreadyExist(accountCreditLimitCategory);
	}

	@Override
	public int updateAccountTypeCategoryInfo(AccountCreditLimitCategory accountCreditLimitCategory) 
	{
		return accountCreditLimitCategoryDao.updateAccountTypeCategoryInfo(accountCreditLimitCategory);

	}
}
