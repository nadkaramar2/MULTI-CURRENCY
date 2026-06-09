package ams.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.AccountCreditCardTxnDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.AccountCreditCardTransactionModel;

@Repository
public class AccountCreditCardTxnDaoImpl extends AbstractGenericDao<AccountCreditCardTransactionModel> implements AccountCreditCardTxnDao
{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<AccountCreditCardTransactionModel> getCreditCardTxn(AccountCreditCardTransactionModel accountCreditCardTransactionModel) {
		try 
		{
			Criteria criteria = createEntityCriteria();
			if(accountCreditCardTransactionModel.getStrAccountType() != null && accountCreditCardTransactionModel.getStrAccountType().trim().length() > 0) 
			{
				criteria.add(Restrictions.eq("strAccountType",accountCreditCardTransactionModel.getStrAccountType()));
			}
			if(accountCreditCardTransactionModel.getStrAccountNumber() != null && accountCreditCardTransactionModel.getStrAccountNumber().trim().length() > 0) 
			{
				criteria.add(Restrictions.eq("strAccountNumber",accountCreditCardTransactionModel.getStrAccountNumber()));
			}
				
			List<AccountCreditCardTransactionModel> creditcardTxnData = (List<AccountCreditCardTransactionModel>) criteria.list();
			if (creditcardTxnData !=null && creditcardTxnData.size() > 0) 
			{
				return creditcardTxnData;
			}
		}
		catch (Exception e) 
		{
			System.out.println("Exception in getAccountCreditCardTxnWise::"+e);
			e.printStackTrace();
		}
		return null;
}

	@Override
	public List<AccountCreditCardTransactionModel> getAccountCreditCardTxnWise(AccountCreditCardTransactionModel accountCreditCardTransactionModel) 
	{
		try 
		{
			String sqlQuery = "SELECT mcc_code AS strMcc, "
					+ "transaction_amount AS strTransactionAmount,"
					+" amount_paid AS strAmountPaid, amount_paid_date AS strAmountPaidDate, "
					+ "txn_date AS strTxnDate"
					+" FROM credit_card_transaction WHERE account_number = ? and is_paid= 'N'";
			
			List<AccountCreditCardTransactionModel> getOutStandingBalanceList  = jdbcTemplate.query(sqlQuery,
			new BeanPropertyRowMapper<AccountCreditCardTransactionModel>(AccountCreditCardTransactionModel.class), 
			new Object[] 
			{ 
				accountCreditCardTransactionModel.getStrAccountNumber() 
			});
			return getOutStandingBalanceList;
		}
	    catch (Exception e) 
	    {
		   e.printStackTrace();
		 }
		return null;
	}
}
