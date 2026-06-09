package ams.cms.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.AccountInterestMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.AccountInterestMaster;

@Repository
public class AccountInterestMasterDaoImpl extends AbstractGenericDao<AccountInterestMaster> implements AccountInterestMasterDao 
{
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<AccountInterestMaster> getOutStandingInterestList(AccountInterestMaster accountInterestMaster)
	{
		try 
		{
			String sqlQuery = "SELECT mcc_code AS strMcc,transaction_id AS strTransactionID,"
					+"transaction_amount AS strTransactionAmount,transaction_date AS strTransactionDate,"
				    +"transaction_time AS strTransactionTime,interest_paid_amount AS strInterestPaidAmount,"
					+"calculated_interest AS strCalculateInterest,calculated_GST AS strCalculateGST,"
					+"interest_paid_date  AS strInterestPaidDate,interest_calculated_date AS strInterestCalculateDate "
					+ "FROM interest_account_wise WHERE account_number = ? and  is_paid='N'"; 
			
			List<AccountInterestMaster> getInterestOutstandingList  = jdbcTemplate.query(sqlQuery,
			new BeanPropertyRowMapper<AccountInterestMaster>(AccountInterestMaster.class), 
			new Object[] 
			{ 
				accountInterestMaster.getStrAccountNumber() 
			});
			return getInterestOutstandingList;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

}
