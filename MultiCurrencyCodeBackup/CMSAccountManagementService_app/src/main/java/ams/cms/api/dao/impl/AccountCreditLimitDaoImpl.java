package ams.cms.api.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.AccountCreditLimitDao;
import ams.cms.api.model.AccountCreditLimit;
import ams.cms.dao.generic.AbstractGenericDao;

@Repository
public class AccountCreditLimitDaoImpl extends AbstractGenericDao<AccountCreditLimit> implements AccountCreditLimitDao{

	@SuppressWarnings("unchecked")
	@Override
	public AccountCreditLimit getAccountCreditLimitCategoryObj(AccountCreditLimit accountCreditLimitCategory) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();
			criteria.add(Restrictions.eq("strParticipantId", accountCreditLimitCategory.getStrParticipantId()));
			criteria.add(Restrictions.eq("strCreditType", accountCreditLimitCategory.getStrCreditType()));
			
			List<AccountCreditLimit> accountCreditLimitCategorylist = (List<AccountCreditLimit>) criteria.list();
			if (accountCreditLimitCategorylist !=null && accountCreditLimitCategorylist.size() > 0) 
			{
				return accountCreditLimitCategorylist.get(0);
			}
		}
		catch (Exception e) 
		{
			System.out.println("Exception in getAccountInformationListByTypes::"+e);
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AccountCreditLimit> getAccountCreditLimitCategoriesListByParticipantWise(String participantId) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			criteria.add(Restrictions.eq("strParticipantId", participantId));
			
			List<AccountCreditLimit> accountCreditLimitCategorylist = (List<AccountCreditLimit>) criteria.list();
			if (accountCreditLimitCategorylist !=null && accountCreditLimitCategorylist.size() > 0) 
			{
				return accountCreditLimitCategorylist;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
