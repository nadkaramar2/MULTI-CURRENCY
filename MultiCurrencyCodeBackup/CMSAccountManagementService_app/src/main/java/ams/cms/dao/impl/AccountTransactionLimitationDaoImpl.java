package ams.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ams.cms.dao.AccountTransactionLimitationDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.AccountTransactionLimitation;

@Repository
public class AccountTransactionLimitationDaoImpl extends AbstractGenericDao<AccountTransactionLimitation> implements AccountTransactionLimitationDao 
{

	@SuppressWarnings("unchecked")
	@Override
	public AccountTransactionLimitation getAccountTxnLimitBasedOnParam(AccountTransactionLimitation accountTransactionLimitation) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();
			
			if (accountTransactionLimitation.getStrParticipantID()!=null && accountTransactionLimitation.getStrParticipantID().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strParticipantID", accountTransactionLimitation.getStrParticipantID()));
			}
			if (accountTransactionLimitation.getStrAccountType()!=null && accountTransactionLimitation.getStrAccountType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountType", accountTransactionLimitation.getStrAccountType()));
			}
			
			List<AccountTransactionLimitation> accountTransactionLimitations = (List<AccountTransactionLimitation>) criteria.list();
			if (accountTransactionLimitations !=null && accountTransactionLimitations.size() > 0) 
			{
				return accountTransactionLimitations.get(0);
			}
		}
		catch (Exception e) 
		{
			System.out.println("Exception in getAccountTxnLimitBasedOnParam::"+e);
			e.printStackTrace();
		}
		return null;
	}
	
}
