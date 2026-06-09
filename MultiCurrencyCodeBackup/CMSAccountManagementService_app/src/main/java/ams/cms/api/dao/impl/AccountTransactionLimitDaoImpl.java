package ams.cms.api.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.AccountTransactionLimitDao;
import ams.cms.api.model.AccountLimitRequest;
import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.AccountTransactionLimit;
import ams.cms.dao.generic.AbstractGenericDao;

@Repository
public class AccountTransactionLimitDaoImpl extends AbstractGenericDao<AccountTransactionLimit> implements AccountTransactionLimitDao{

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public AccountTransactionLimit getMonthlyAndDailyTransactionLimit(AccountLimitRequest accountLimitRequest) {
		AccountTransactionLimit accountTransactionLimit = null;
		try {
			String sql = "SELECT single_txn_limit as strSingleTxnLimit, daily_txn_limit as strDailyTxnLimit, monthly_txn_limit as strMonthlyTxnLimit from account_transaction_limit where account_type = ?";
			List<AccountTransactionLimit> accountTransactionLimitDetails = this.jdbcTemplate.query(sql,
					 (RowMapper) new BeanPropertyRowMapper(AccountTransactionLimit.class),
						new Object[] {
								accountLimitRequest.getStrAccountType()
						} 
					);
			
			if(accountTransactionLimitDetails.size()>0) {
				accountTransactionLimit = accountTransactionLimitDetails.get(0);
				
			}
			return accountTransactionLimit;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return accountTransactionLimit;
	}

	@Override
	public AccountTransactionLimit getAccountTransactionLimitation(AccountLimitRequest accountLimitRequest) {
		AccountTransactionLimit accountTransactionLimit = null;
		try {
			String sql = "SELECT single_txn_limit as strSingleTxnLimit, daily_txn_limit as strDailyTxnLimit, monthly_txn_limit as strMonthlyTxnLimit, yearly_txn_limit as strYearlyTxnLimit from account_transaction_limitation where account_type = ?";
			List<AccountTransactionLimit> accountTransactionLimitDetails = this.jdbcTemplate.query(sql,
					(RowMapper) new BeanPropertyRowMapper(AccountTransactionLimit.class),
					new Object[] { accountLimitRequest.getStrAccountType() });

			if (accountTransactionLimitDetails.size() > 0) {
				accountTransactionLimit = accountTransactionLimitDetails.get(0);
			}
			return accountTransactionLimit;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountTransactionLimit;
	}
	
}
	
	
