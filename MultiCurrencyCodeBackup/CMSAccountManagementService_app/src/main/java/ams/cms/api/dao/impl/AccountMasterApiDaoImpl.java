package ams.cms.api.dao.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.AccountMasterApiDao;
import ams.cms.api.model.AccountLimitRequest;
import ams.cms.api.model.AccountMaster;
import ams.cms.dao.generic.AbstractGenericDao;

@Repository
public class AccountMasterApiDaoImpl extends AbstractGenericDao<AccountMaster> implements AccountMasterApiDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public AccountMaster getAvailableMonthlyAndDailyLimit(AccountLimitRequest accountLimitRequest) 
	{
		AccountMaster accountMaster = null;
		try 
		{
			String sql = "SELECT credit_limit_amount as strCreditLimitAmount, available_daily_limit as strAvailableDailyLimit, available_monthly_limit AS strAvailableMonthlyLimit from account_master where account_number = ?";
			List<AccountMaster> accountMasterDetails = this.jdbcTemplate.query(sql,
			  (RowMapper) new BeanPropertyRowMapper(AccountMaster.class),
						new Object[] 
						{
								accountLimitRequest.getStrAccountNumber()
						} 
					);
			
			if(accountMasterDetails.size()>0) 
			{
				accountMaster = accountMasterDetails.get(0);
			}
			return accountMaster;
	}catch(Exception e) {
		e.printStackTrace();
	}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<String> getAccTypeOnCustId(AccountMaster accountMaster) {
		try {
			String sql = "SELECT account_type FROM account_master WHERE cust_id = ? ";
			List<String> accTypeList = this.jdbcTemplate.queryForList(sql,
					new Object[] {accountMaster.getStrCustId()},
					String.class);
			return accTypeList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public AccountMaster getCreditFlagOnCustId(AccountMaster accountMaster) {
		try {
			String sql = "SELECT am.account_number AS strAccountNumber, am.cust_id AS strCustId, "
					+ "am.account_type AS strAccountType, am.mobile_no AS strMobileNo, "
					+ "atm.is_credit_type AS strIsCreditType "
					+ "FROM account_master am "
					+ "INNER JOIN account_type_master atm "
					+ "ON am.account_type = atm.account_type "
					+ "WHERE am.cust_id = '"+accountMaster.getStrCustId()+"' "
					+ "AND am.account_type = '"+accountMaster.getStrAccountType()+"' ";
			List<AccountMaster> accountdetails = this.jdbcTemplate.query(sql,
					(RowMapper) new BeanPropertyRowMapper(AccountMaster.class),
					new Object[] {});
			
			if(accountdetails != null && accountdetails.size()>0)
			{
				accountMaster = accountdetails.get(0);
			}
			return accountMaster;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AccountMaster getUpdateOrAvailableTxnlimits(AccountMaster accontMaster) 
	{
		try
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT am.id AS strID, am.per_txn_limit AS strSingleTxnLimit,");
			queryBuilder.append("am.daily_txn_limit AS strPerDayAssignLimit,");
			queryBuilder.append("am.monthly_txn_limit AS strMonthlyAssignLimit,"); 
			queryBuilder.append("am.yearly_txn_limit AS strYearlyAssignLimit,"); 
			queryBuilder.append("am.available_yearly_limit AS strAvailableYearlyLimit,");
			queryBuilder.append("am.available_daily_limit AS strAvailableDailyLimit,");
			queryBuilder.append("am.available_monthly_limit AS strAvailableMonthlyLimit,");
			queryBuilder.append("atl.single_txn_limit AS strSingleTxnMaxLimit,");
			queryBuilder.append("atl.daily_txn_limit AS strPerDayMaxAssignedlimit,");
			queryBuilder.append("atl.monthly_txn_limit AS strMonthlyMaxAssignedLimit,");
			queryBuilder.append("atl.yearly_txn_limit AS strYearlyMaxAssignedLimit ");
			queryBuilder.append("FROM account_master am ");
			queryBuilder.append("INNER JOIN account_transaction_limit atl ");
			queryBuilder.append("ON atl.account_type = am.account_type ");
			queryBuilder.append("WHERE am.account_number = ? ");
			
			List<AccountMaster> accountMasterDetails = this.jdbcTemplate.query(queryBuilder.toString(),
			(RowMapper<AccountMaster>) new BeanPropertyRowMapper<AccountMaster>(AccountMaster.class),
			new Object[] 
			{
				accontMaster.getStrAccountNumber()
			} 
			);
			if(accountMasterDetails.size()>0) 
			{
				accontMaster = accountMasterDetails.get(0);
				return accontMaster;
			}
		}
		catch(Exception e) 
		{
				e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public AccountMaster getUpdateOrAvailableCreditlimits(AccountMaster accontMaster) {
		try {

			StringBuilder queryBuilder = new StringBuilder("SELECT am.id AS strID,am.credit_limit_amount AS strCreditLimitAmount,");
			queryBuilder.append("am.available_credit_limit AS strAvailableCreditLimit,");
			queryBuilder.append("am.per_txn_limit AS strSingleTxnLimit,");
			queryBuilder.append("am.daily_txn_limit AS strPerDayAssignLimit,");
			queryBuilder.append("am.monthly_txn_limit AS strMonthlyAssignLimit,");
			queryBuilder.append("am.yearly_txn_limit AS strYearlyAssignLimit, ");
			queryBuilder.append("am.available_yearly_limit AS strAvailableYearlyLimit,");
			queryBuilder.append("am.available_daily_limit AS strAvailableDailyLimit,");
			queryBuilder.append("am.available_monthly_limit AS strAvailableMonthlyLimit,");
			queryBuilder.append("atl.single_txn_limit AS strSingleTxnMaxLimit,");
			queryBuilder.append("atl.daily_txn_limit AS strPerDayMaxAssignedlimit,");
			queryBuilder.append("atl.monthly_txn_limit AS strMonthlyMaxAssignedLimit,");
			queryBuilder.append("atl.yearly_txn_limit AS strYearlyMaxAssignedLimit,");
			queryBuilder.append("aclt.credit_limit AS strCreditMaxAssignedlimit ");
			queryBuilder.append("FROM account_master am ");
			queryBuilder.append("INNER JOIN account_transaction_limit atl ");
			queryBuilder.append("ON am.account_type = atl.account_type ");
			queryBuilder.append("INNER JOIN account_credit_limit_category aclt ");
			queryBuilder.append("ON am.credit_limit_category=aclt.credit_type ");
			queryBuilder.append("WHERE am.account_number= ? ");
			List<AccountMaster> accountmaster1 = this.jdbcTemplate.query(queryBuilder.toString(),
					(RowMapper<AccountMaster>) new BeanPropertyRowMapper<AccountMaster>(AccountMaster.class),
					new Object[] { accontMaster.getStrAccountNumber() });
			if (accountmaster1.size() > 0) {
				accontMaster = accountmaster1.get(0);
				return accontMaster;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Add by Abhishek T for updatecreditlimitAmount-Start
	@Override
	public AccountMaster getCreditLimitAmount(AccountMaster accountMaster)
	{
		try
		{
			StringBuilder selectQuerySb = new StringBuilder("SELECT act.credit_limit AS strCreditLimitAmount FROM account_master am INNER JOIN account_credit_limit_category act ON act.credit_type = am.credit_limit_category WHERE am.account_number = ? ");
			List<AccountMaster> creditAmountLimit = this.jdbcTemplate.query(selectQuerySb.toString(),
					(RowMapper<AccountMaster>) new BeanPropertyRowMapper<AccountMaster>(AccountMaster.class),
					new Object[] {accountMaster.getStrAccountNumber() });

			if (creditAmountLimit != null && creditAmountLimit.size() > 0) 
			{
				accountMaster = creditAmountLimit.get(0);
			}
			return accountMaster;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updatecreditLimitAmount(AccountMaster accountMaster) 
	{
		try 
		{
			StringBuilder updateQuerySb = new StringBuilder("UPDATE account_master SET ");
			
			/*updateQuerySb.append("credit_limit_amount = ? ");
			updateQuerySb.append("WHERE account_number = ? ");
			*/
			
			updateQuerySb.append("per_txn_limit = ? , daily_txn_limit = ? , ");
			updateQuerySb.append("monthly_txn_limit = ?, credit_limit_amount = ? ");
			updateQuerySb.append("WHERE account_number = ? AND cust_id = ? ");

			int count = this.jdbcTemplate.update(updateQuerySb.toString(),
					new Object[]
					{
						accountMaster.getStrPerTxnLimit(),
						accountMaster.getStrDailyTxnLimit(),
						accountMaster.getStrMonthlyTxnLimit(),
						accountMaster.getStrCreditLimitAmount(),
						accountMaster.getStrAccountNumber(),
						accountMaster.getStrCustId()							 
					});
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
		//Add by Abhishek for updatecreditlimitAmount-End
}
