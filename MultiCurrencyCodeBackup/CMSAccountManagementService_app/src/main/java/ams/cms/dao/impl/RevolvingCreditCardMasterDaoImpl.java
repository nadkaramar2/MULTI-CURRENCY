package ams.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.api.model.AccountCreditBalanceTxnResponse;
import ams.cms.api.model.AccountWiseInterestMasterResponse;
import ams.cms.dao.RevolvingCreditCardMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.AccountCreditCardTransactionModel;
import ams.cms.model.RevolvingCreditCardMaster;
import ams.cms.scheduler.model.AccountWiseInterestMaster;

@Repository
public class RevolvingCreditCardMasterDaoImpl extends AbstractGenericDao<RevolvingCreditCardMaster> implements RevolvingCreditCardMasterDao 
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public Boolean validateRevolvingCreditCard(RevolvingCreditCardMaster revolvingCreditCardMaster) {
		
		try {	
			String sql = "SELECT COUNT(account_type) FROM revolving_credit_card_master "
					+ "where account_type = ? " ;

			String validateCount = this.jdbcTemplate.queryForObject(sql, String.class,
					new Object[]
					{
						revolvingCreditCardMaster.getStrAccountType()
					}
			);
			if(Integer.valueOf(validateCount) > 0)
			{
				return true;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}



	@Override
	public List<AccountCreditBalanceTxnResponse> getRevolvingCreditCard(AccountCreditCardTransactionModel accountCreditCardTransactionModel) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();			
			if (accountCreditCardTransactionModel.getStrAccountType()!=null && accountCreditCardTransactionModel.getStrAccountType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountType", accountCreditCardTransactionModel.getStrAccountType()));
			}			
			List<AccountCreditBalanceTxnResponse> checkCreditCarsIsRevolvingType = (List<AccountCreditBalanceTxnResponse>) criteria.list();
			return checkCreditCarsIsRevolvingType;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}



	@Override
	public List<AccountWiseInterestMasterResponse> getRevolvingCreditCard(AccountWiseInterestMaster accountWiseInterestMaster) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();			
			if (accountWiseInterestMaster.getStrAccountType()!=null && accountWiseInterestMaster.getStrAccountType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountType", accountWiseInterestMaster.getStrAccountType()));
			}			
			List<AccountWiseInterestMasterResponse> checkCreditCarsIsRevolvingType = (List<AccountWiseInterestMasterResponse>) criteria.list();
			return checkCreditCarsIsRevolvingType;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

}
