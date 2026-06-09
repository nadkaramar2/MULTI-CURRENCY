package ams.cms.dao.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.AccountTypeTierBasedLimitDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.AccountTypeTierBasedLimit;
import java.sql.PreparedStatement;

@Repository
public class AccountTypeTierBasedLimitDaoImpl extends AbstractGenericDao<AccountTypeTierBasedLimit> implements AccountTypeTierBasedLimitDao
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public AccountTypeTierBasedLimit getTierBasedLimitsValues(AccountTypeTierBasedLimit accountTypeTierBasedLimit) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT * from account_type_tier_based_limit ");
			queryBuilder.append("WHERE account_type = ? ");
			
			List<AccountTypeTierBasedLimit> accountCretion  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountTypeTierBasedLimit>(AccountTypeTierBasedLimit.class),
			     new Object[]  { 
			    		 accountTypeTierBasedLimit.getStrAccountType(), 
			    		});
			if (accountCretion!=null && accountCretion.size() > 0) 
			{
				return accountCretion.get(0);
			}	
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public int[] batchEntryForAccountTypeTierLimit(List<AccountTypeTierBasedLimit> accountTypeTierBasedLimit) 
	{
		int[] batchResponse = null;
		try {
			return this.jdbcTemplate.batchUpdate("INSERT INTO account_type_tier_based_limit "
					+ " (account_type,tier_type,daily_cumulative_txn_limit,cumulative_balance_limit,created_date,created_time,created_by)"
					+ "values(?,?,?,?,?,?,?) ", new BatchPreparedStatementSetter() {

						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							ps.setString(1, accountTypeTierBasedLimit.get(i).getStrAccountType());
							ps.setString(2, accountTypeTierBasedLimit.get(i).getStrTierType());
							ps.setString(3, accountTypeTierBasedLimit.get(i).getStrDailyCumulativeTxnLimit());
							ps.setString(4, accountTypeTierBasedLimit.get(i).getStrCumulativeBalanceLimit());
							ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
							ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
							ps.setString(7, accountTypeTierBasedLimit.get(i).getStrCreatedBy());
							System.out.println("new BatchPreparedStatementSetter() {...}" + ps);
						}

						@Override
						public int getBatchSize() {
							return accountTypeTierBasedLimit.size();

						}

					});

		} catch (Exception e) {
			System.out.println("AccountTypeTierBasedLimitDaoImpl.batchEntryforAccountTypeTierLimit()"+e);
			e.printStackTrace();
		}
		return batchResponse;
	}
	@Override
	public List<AccountTypeTierBasedLimit> getTiersLimitsBasedonAccountType(AccountTypeTierBasedLimit accountTypeTierBasedLimit) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT attbl.tier_type AS strTierType, ");
			queryBuilder.append("attbl.daily_cumulative_txn_limit AS strDailyCumulativeTxnLimit, ");
			queryBuilder.append("cumulative_balance_limit AS strCumulativeBalanceLimit ");
			queryBuilder.append("from account_type_tier_based_limit attbl ");
			queryBuilder.append("WHERE attbl.account_type = ? ");
			
			List<AccountTypeTierBasedLimit> accountTypeTierBasedLimits  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountTypeTierBasedLimit>(AccountTypeTierBasedLimit.class),
			     new Object[]  { 
			    		 accountTypeTierBasedLimit.getStrAccountType(), 
			    		});
			if (accountTypeTierBasedLimits!=null && accountTypeTierBasedLimits.size() > 0) 
			{
				return accountTypeTierBasedLimits;
			}	
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int[] batchUpdateEntryForAccountTypeTierLimit(List<AccountTypeTierBasedLimit> accountTypeTierBasedLimitList) 
	{
		int[] batchResponse = null;
		
			try
			{
				return this.jdbcTemplate.batchUpdate("UPDATE account_type_tier_based_limit attbl "
		                +" SET attbl.daily_cumulative_txn_limit = ? , attbl.cumulative_balance_limit = ? , created_date = ? , created_time = ? "
		                +" WHERE account_type = ? AND tier_type = ? ",
		                
		                new BatchPreparedStatementSetter() {
		                	public void setValues(PreparedStatement ps, int i) 
		    						throws SQLException {
		                            ps.setString(1, accountTypeTierBasedLimitList.get(i).getStrDailyCumulativeTxnLimit());
		                            ps.setString(2, accountTypeTierBasedLimitList.get(i).getStrCumulativeBalanceLimit());
		                            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
		                            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
		                            ps.setString(5, accountTypeTierBasedLimitList.get(i).getStrAccountType());
		                            ps.setString(6, accountTypeTierBasedLimitList.get(i).getStrTierType());
		                        }
		                	
		                	public int getBatchSize() {
		                        return accountTypeTierBasedLimitList.size();
		                    }
		                });

			}
			catch (Exception e) 
			{
				System.out.println("AccountTypeTierBasedLimitDaoImpl.batchUpdateEntryForAccountTypeTierLimit()"+e);
				e.printStackTrace();
			}
			return batchResponse;
	}
}
