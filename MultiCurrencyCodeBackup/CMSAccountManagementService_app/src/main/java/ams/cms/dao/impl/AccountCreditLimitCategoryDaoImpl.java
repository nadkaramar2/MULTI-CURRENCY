package ams.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.AccountCreditLimitCategoryDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.AccountCreditLimitCategory;

@Repository
public class AccountCreditLimitCategoryDaoImpl extends AbstractGenericDao<AccountCreditLimitCategory> implements AccountCreditLimitCategoryDao
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unchecked")
	@Override
	public AccountCreditLimitCategory getAccountCreditLimitCategoryObj(AccountCreditLimitCategory accountCreditLimitCategory) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();
			criteria.add(Restrictions.eq("strParticipantId", accountCreditLimitCategory.getStrParticipantId()));
			criteria.add(Restrictions.eq("strCreditType", accountCreditLimitCategory.getStrCreditType()));
			
			List<AccountCreditLimitCategory> accountCreditLimitCategorylist = (List<AccountCreditLimitCategory>) criteria.list();
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
	public List<AccountCreditLimitCategory> getAccountCreditLimitCategoriesListByParticipantWise(String participantId) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (participantId!=null && participantId.trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strParticipantId", participantId));
			}
			
			List<AccountCreditLimitCategory> accountCreditLimitCategorylist = (List<AccountCreditLimitCategory>) criteria.list();
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
	public boolean isCreditTypeAlreadyExist(AccountCreditLimitCategory accountCreditLimitCategory)
	{
		try
		{
			String sql = "SELECT count(*) FROM account_credit_limit_category WHERE credit_type = ?";
			int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { accountCreditLimitCategory.getStrCreditType() });
			return count > 0;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int updateAccountTypeCategoryInfo(AccountCreditLimitCategory accountCreditLimitCategory) 
	{
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE account_credit_limit_category "
					+ "SET credit_limit = ? "
					+ "WHERE  credit_type= ? ",
					new Object[] 
					{ 	
						accountCreditLimitCategory.getStrCreditLimit(),
						accountCreditLimitCategory.getStrCreditType()
						
					});
			return count;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	

}
