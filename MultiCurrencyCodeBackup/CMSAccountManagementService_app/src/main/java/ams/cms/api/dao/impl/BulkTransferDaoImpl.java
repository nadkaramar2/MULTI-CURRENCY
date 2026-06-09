package ams.cms.api.dao.impl;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.BulkTransferDao;
import ams.cms.api.model.BulkTransfer;
import ams.cms.api.model.CustomerByAccountResponse;
import ams.cms.config.CommonConstants;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.utility.Utils;

@Repository
public class BulkTransferDaoImpl extends AbstractGenericDao<BulkTransfer> implements BulkTransferDao 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(BulkTransferDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	@Override
	public List<BulkTransfer> findByTransactionId(BulkTransfer bulkTransfer) 
	{
		List<BulkTransfer> query = null;
		try
		{
			String sql = "SELECT bt.transaction_id AS strTransactionId, bt.amount AS strAmount," + 
					"bt.from_account_no AS strFromAccountNo, bt.from_account_type AS strFromAccountType ," + 
					"bt.to_account_no AS strToAccountNo, bt.to_account_type AS strToAccountType," + 
					"bt.time AS strTime, bt.date AS strDate , STATUS AS strStatus FROM bulk_transfer AS bt WHERE transaction_id = ?";
			
			 query = jdbcTemplate.query(sql,
						new Object[] {bulkTransfer.getStrTransactionId()},
						(RowMapper) new BeanPropertyRowMapper(BulkTransfer.class));
			
			return query;	
		}catch(Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));;
			
		}
		return query;
	}

	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	@Override
	public List<BulkTransfer> findByPreTransactionSerialId(BulkTransfer bulkTransfer) 
	{
		List<BulkTransfer> query = null;
		try 
		{
			String sql = "SELECT * FROM bulk_transfer ORDER BY id DESC LIMIT 1;";
			
			 query = jdbcTemplate.query(sql, (RowMapper<BulkTransfer>) new BeanPropertyRowMapper(BulkTransfer.class), 
					 new Object[] {bulkTransfer.getStrPreTransactionId()});
			 
			return query;
		}
		catch(Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));;
		}
		return query;
	}
	
	@Override
	public List<BulkTransfer> findOneByPreTransactionSerialId(BulkTransfer bulkTransfer) {
		
		List<BulkTransfer> query = null;
		try {
			String sql = "SELECT * FROM bulk_transfer ORDER BY id DESC LIMIT 1;";
			
			 query = jdbcTemplate.query(sql,
					 new Object[] {bulkTransfer.getStrPreTransactionId()},
						(RowMapper) new BeanPropertyRowMapper(BulkTransfer.class)
						);
			 
			return query;
		}catch(Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));;
			
		}
		return query;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<BulkTransfer> findOneByPreTransactionSerialId() {
		
		List<BulkTransfer> query = null;
		try {
			String sql = "SELECT pre_transaction_id as strPreTransactionId FROM bulk_transfer ORDER BY id DESC LIMIT 1;";
		   query = jdbcTemplate.query(sql, new Object[] {},
						(RowMapper) new BeanPropertyRowMapper(BulkTransfer.class) );
		  return query;
		}
		catch(Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));;
			
		}
		return query;
	}

	@Override
	public List<BulkTransfer> findAllByPreTransactionId(BulkTransfer bulkTransfer)
	{
		List<BulkTransfer> query = null;
		try 
		{
			String sql2 = "SELECT id as strId, transaction_id AS strTransactionId, amount AS strAmount,"+
			" from_account_no AS strFromAccountNo, from_account_type AS strFromAccountType ,"+
			" to_account_no AS strToAccountNo, to_account_type AS strToAccountType,"+
			" bulk_request_time AS strBulkRequestTime, bulk_request_date AS strBulkRequestDate , "
			+ "status AS strStatus FROM bulk_transfer  WHERE pre_transaction_id = ? AND status = ?";
			
			
			 query = jdbcTemplate.query(sql2, new BeanPropertyRowMapper<BulkTransfer>(BulkTransfer.class),
					new Object[] 
					{ 
						bulkTransfer.getStrPreTransactionId(),
						bulkTransfer.getStrStatus()
					});		
			return query;	
		}
		catch(Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));;
		}
		return null;
	}

	@Override
	public int updateRejectReason(BulkTransfer bulkTransferObject) 
	{
		StringBuilder sqlSb = new StringBuilder("UPDATE bulk_transfer AS bt SET bt.status = ?, bt.rejected_reason = ?, bt.is_verified = 'Y' WHERE bt.pre_transaction_id = ?");
		
		int update = jdbcTemplate.update(sqlSb.toString(),
		new Object[] 
		{
			bulkTransferObject.getStrStatus(),
			bulkTransferObject.getStrRejectedReason(),
			bulkTransferObject.getStrPreTransactionId()
		});
		amsLogger.writeInfoLog(bulkTransferObject.getStrId()+" is updated "+update );
		
		return update;
	}
	
	//updating based on id
	@Override
	public void updateVerifiedObject(BulkTransfer bulkTransferObject) {
		
		String sql = "UPDATE bulk_transfer AS bt"
					+ " SET"
					+ " bt.transaction_id = ?,"
					+ " bt.status = ?,"
					+ " bt.is_verified = ?,"
					+ " bt.rejected_reason = ?"
					+ " WHERE"
					+ " bt.id = ?";
		
		int update = jdbcTemplate.update(sql,
				new Object[] {
						bulkTransferObject.getStrTransactionId(),
						bulkTransferObject.getStrStatus(),
						bulkTransferObject.getStrIsVerified(),
						bulkTransferObject.getStrRejectedReason(),
						bulkTransferObject.getStrId()
				});
		amsLogger.writeInfoLog(bulkTransferObject.getStrId()+" is updated "+update );
	}

	@Override
	public List<BulkTransfer> getPreTransactionIdByTxnAmount(BulkTransfer bulkTransfer) 
	{
		try 
		{
			StringBuilder querySb = new StringBuilder("SELECT bt.pre_transaction_id as strPreTransactionId, bt.bulk_mode AS strBulkMode,");	
			querySb.append("bt.bulk_request_time AS strBulkRequestTime,bt.bulk_request_date AS strBulkRequestDate, SUM(bt.amount) AS bulkTransferAmount FROM bulk_transfer  AS bt  WHERE bt.is_verified= 'N' ");	  
			querySb.append("AND bt.status='pending' GROUP BY bt.pre_transaction_id"); 
			
			amsLogger.writeInfoLog("UserPasswordDaoImpl.getUserPassword()" + querySb.toString());
			
			List<BulkTransfer> preTxnIdWithAmountlist = jdbcTemplate.query(querySb.toString(),new BeanPropertyRowMapper<BulkTransfer>(BulkTransfer.class),
            new Object[] {});
			
			return preTxnIdWithAmountlist;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));;
		}
		return null;
	}

	@Override
	public void updateSuccessStatus(BulkTransfer bulkTransfer) 
	{
		try 
		{
			String sql = "UPDATE bulk_transfer AS bt"
					+ " SET"
					+ " bt.status = ? "
					+ " WHERE"
					+ " bt.id = ?";
		int update = jdbcTemplate.update(sql,new Object[] 
				{
						bulkTransfer.getStrStatus(),
						bulkTransfer.getStrId()
				});
		amsLogger.writeInfoLog(bulkTransfer.getStrId()+" is updated "+update );				
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));;
		}
	}
     
	//--pending query for authorize bluk transfer list
	@Override
	public List<BulkTransfer> getAuthorizeBlukTransferList(BulkTransfer bulkTransfer) 
	{
		try 
		{
			StringBuilder statementQuery = new StringBuilder("SELECT bt.pre_transaction_id as strPreTransactionId, SUM(bt.amount) AS strAmount, ");
			statementQuery.append("bt.bulk_request_time AS strBulkRequestTime,bt.bulk_request_date AS strBulkRequestDate, bt.bulk_mode AS strBulkMode,");
			statementQuery.append("maker_id AS strMakerId FROM bulk_transfer AS bt WHERE bt.is_verified= 'Y' AND bt.status='pending' ");
			statementQuery.append("AND bt.maker_id <> ? GROUP BY bt.pre_transaction_id");
			
			List<BulkTransfer> authorizelist = jdbcTemplate.query(statementQuery.toString(),
			new BeanPropertyRowMapper<BulkTransfer>(BulkTransfer.class), new Object[] 
			{							
				bulkTransfer.getStrMakerId()
			});			
			return authorizelist;	
			
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<CustomerByAccountResponse> getFromAccountBulkDataList(BulkTransfer bulkTransfer) 
	{
		try 
		{
			StringBuilder statementQuery = new StringBuilder("SELECT am.cust_id as strCustId, am.account_number AS strAccountNumber,");
			statementQuery.append("am.account_type AS strAccountType,am.ear_mark_amount AS strEarMarkAmount, ");
			statementQuery.append("bulk.to_account_no AS strToAccountNumber, bulk.to_account_type AS strToAccountType,");
			statementQuery.append("am.pre_cred_amount AS strPreCredAmount,am.closing_balance AS strClosingBalance,am.`status` AS strStatus, ");
			statementQuery.append("am.load_count AS strLoadCount,am.total_outstanding_balance AS strTotalOutstandingBal, ");
			statementQuery.append("am.total_available_grace_period AS strAvailableGracePeriod,am.available_credit_limit AS strAvailableCreditLimit, ");
			statementQuery.append("am.available_daily_limit AS strAvailableDailyLimit,am.available_monthly_limit AS strAvailableMonthlyLimit, ");
			statementQuery.append("am.available_yearly_limit AS strAvailableYearlyLimit,atym.account_type_category AS strAccountTypeCategory, ");
			statementQuery.append("atym.allow_load_cash AS strAllowLoadCash,atym.`status` AS strTStatus,atym.gl_account_type AS strGLAccountType, ");
			statementQuery.append("atym.gl_account_no AS strGLAccountNumber,atym.nuban_type AS strNubanType,atym.account_type_code AS strAccountTypeCode, ");
			statementQuery.append("cm.active_tier AS strActiveTier,tam.tier1_cummulative_balance AS tier1CummBalance,tam.tier2_cummulative_balance AS tier2CummBalance, ");
			statementQuery.append("tam.tier3_cummulative_balance AS tier3CummBalance,tam.available_tier1_daily_cum_limit AS strAvailableTier1DailyCumlimit, ");
			statementQuery.append("tam.available_tier2_daily_cum_limit AS strAvailableTier2DailyCumlimit,tam.available_tier3_daily_cum_limit AS strAvailableTier3DailyCumlimit, ");
			statementQuery.append("bulk.amount AS transferAmount, ");
			statementQuery.append("glatm.account_type AS glAccountType, glatm.account_number AS glAccountNo, glatm.closing_balance AS glAccountBalance, glatm.account_description AS glAccountDescr ");
			
			statementQuery.append("FROM account_master AS am ");
			statementQuery.append("INNER JOIN customer_master AS cm ON am.cust_id = cm.cust_id ");
			statementQuery.append("INNER JOIN account_type_master AS atym ON atym.account_type = am.account_type ");
			statementQuery.append("INNER JOIN tier_account_master AS tam ON tam.account_no = am.account_number ");				
			statementQuery.append("INNER JOIN bulk_transfer AS bulk ON bulk.from_account_no = am.account_number ");
			statementQuery.append("INNER JOIN gl_account_type_master AS glatm ON TRIM(glatm.account_number) = TRIM(atym.gl_account_no)");

			statementQuery.append("WHERE bulk.pre_transaction_id ='");			
			statementQuery.append(bulkTransfer.getStrPreTransactionId());
			statementQuery.append("'AND bulk.`status`='");
			statementQuery.append(bulkTransfer.getStrStatus());
			statementQuery.append("'");


			List<CustomerByAccountResponse> customerAccountByNo = jdbcTemplate.query(statementQuery.toString(), new BeanPropertyRowMapper<CustomerByAccountResponse>(CustomerByAccountResponse.class), new Object[] { });			
			return customerAccountByNo;
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));;
		}
		return null;
	}

	@Override
	public List<CustomerByAccountResponse> getToAccountBulkDataList(BulkTransfer bulkTransfer) 
	{
		amsLogger.writeInfoLog("Inside getToAccountBulkDataList-----111------");
		List<CustomerByAccountResponse> customerAccountResponsesList = null;
		try 
		{
			StringBuilder statementQuery = new StringBuilder("SELECT am.cust_id as strCustId, am.account_number AS strAccountNumber,");
			statementQuery.append("bulk.from_account_no AS strFromAccountNumber, bulk.from_account_type AS strFromAccountType,");
			statementQuery.append("am.account_type AS strAccountType,am.ear_mark_amount AS strEarMarkAmount, ");  
			statementQuery.append("am.pre_cred_amount AS strPreCredAmount,am.closing_balance AS strClosingBalance,am.`status` AS strStatus, ");
			statementQuery.append("am.load_count AS strLoadCount,am.total_outstanding_balance AS strTotalOutstandingBal, ");
			statementQuery.append("am.total_available_grace_period AS strAvailableGracePeriod,am.available_credit_limit AS strAvailableCreditLimit, ");
			statementQuery.append("am.available_daily_limit AS strAvailableDailyLimit,am.available_monthly_limit AS strAvailableMonthlyLimit, ");
			statementQuery.append("am.available_yearly_limit AS strAvailableYearlyLimit,atym.account_type_category AS strAccountTypeCategory, ");
			statementQuery.append("atym.allow_load_cash AS strAllowLoadCash,atym.`status` AS strTStatus,atym.gl_account_type AS strGLAccountType, ");
			statementQuery.append("atym.gl_account_no AS strGLAccountNumber,atym.nuban_type AS strNubanType,atym.account_type_code AS strAccountTypeCode, ");
			statementQuery.append("cm.active_tier AS strActiveTier,tam.tier1_cummulative_balance AS tier1CummBalance,tam.tier2_cummulative_balance AS tier2CummBalance, ");
			statementQuery.append("tam.tier3_cummulative_balance AS tier3CummBalance,tam.available_tier1_daily_cum_limit AS strAvailableTier1DailyCumlimit, ");
			statementQuery.append("tam.available_tier2_daily_cum_limit AS strAvailableTier2DailyCumlimit,tam.available_tier3_daily_cum_limit AS strAvailableTier3DailyCumlimit, ");
			statementQuery.append("bulk.amount AS transferAmount, ");
			statementQuery.append("glatm.account_type AS glAccountType, glatm.account_number AS glAccountNo, glatm.closing_balance AS glAccountBalance, glatm.account_description AS glAccountDescr ");

			statementQuery.append("FROM account_master AS am ");
			statementQuery.append("INNER JOIN customer_master AS cm ON am.cust_id = cm.cust_id ");
			statementQuery.append("INNER JOIN account_type_master AS atym ON atym.account_type = am.account_type ");
			statementQuery.append("INNER JOIN tier_account_master AS tam ON tam.account_no = am.account_number ");			
			statementQuery.append("INNER JOIN bulk_transfer AS bulk ON bulk.to_account_no = am.account_number ");			
			statementQuery.append("INNER JOIN gl_account_type_master AS glatm ON TRIM(glatm.account_number) = TRIM(atym.gl_account_no) ");

			statementQuery.append("WHERE bulk.pre_transaction_id ='");			
			statementQuery.append(bulkTransfer.getStrPreTransactionId());
			statementQuery.append("' AND bulk.`status`='");
			statementQuery.append(bulkTransfer.getStrStatus());
			statementQuery.append("'");
			
			amsLogger.writeInfoLog("Inside getToAccountBulkDataList statementQuery =["+statementQuery.toString()+"]");
			customerAccountResponsesList = jdbcTemplate.query(statementQuery.toString(), new BeanPropertyRowMapper<CustomerByAccountResponse>(CustomerByAccountResponse.class), new Object[] { });			
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return customerAccountResponsesList;
	}

	@Override
	public List<CustomerByAccountResponse> getToAccountBulkDataListForNonNigeria(BulkTransfer bulkTransfer) 
	{
		List<CustomerByAccountResponse> customerAccountResponsesList = null;
		try 
		{
			StringBuilder statementQuery = new StringBuilder("SELECT am.cust_id as strCustId, bulk.from_account_type AS strFromAccountType, bulk.from_account_no AS strFromAccountNumber,");
			statementQuery.append("bulk.amount AS transferAmount, am.account_type AS strAccountType, am.account_number AS strAccountNumber, ");
			statementQuery.append("am.closing_balance AS strClosingBalance, am.ear_mark_amount AS strEarMarkAmount, am.pre_cred_amount AS strPreCredAmount, ");
			statementQuery.append("am.`status` AS strStatus, am.load_count AS strLoadCount, am.total_outstanding_balance AS strTotalOutstandingBal,");
			statementQuery.append("am.total_available_grace_period AS strAvailableGracePeriod,am.available_credit_limit AS strAvailableCreditLimit,");
			statementQuery.append("am.available_daily_limit AS strAvailableDailyLimit,am.available_monthly_limit AS strAvailableMonthlyLimit,");
			statementQuery.append("am.available_yearly_limit AS strAvailableYearlyLimit,atym.account_type_category AS strAccountTypeCategory,");
			statementQuery.append("atym.allow_load_cash AS strAllowLoadCash,atym.`status` AS strTStatus, cm.active_tier AS strActiveTier, ");
			statementQuery.append("glatm.account_type AS glAccountType, glatm.account_number AS glAccountNo, glatm.closing_balance AS glAccountBalance, glatm.account_description AS glAccountDescr ");
			
			statementQuery.append("FROM account_master AS am ");
			statementQuery.append("INNER JOIN customer_master AS cm ON TRIM(am.cust_id) = TRIM(cm.cust_id) ");
			statementQuery.append("INNER JOIN account_type_master AS atym ON TRIM(atym.account_type) = TRIM(am.account_type) ");
			statementQuery.append("INNER JOIN bulk_transfer AS bulk ON TRIM(bulk.to_account_no) = TRIM(am.account_number) ");
			statementQuery.append("INNER JOIN gl_account_type_master AS glatm ON TRIM(glatm.account_number) = TRIM(atym.gl_account_no) ");
			
			statementQuery.append("WHERE bulk.pre_transaction_id ='");			
			statementQuery.append(bulkTransfer.getStrPreTransactionId());
			statementQuery.append("' AND bulk.`status`='");
			statementQuery.append(bulkTransfer.getStrStatus());
			statementQuery.append("'");
			
			
			customerAccountResponsesList = jdbcTemplate.query(statementQuery.toString(), new BeanPropertyRowMapper<CustomerByAccountResponse>(CustomerByAccountResponse.class), new Object[] {});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return customerAccountResponsesList;
	}

	@Override
	public List<CustomerByAccountResponse> getFromAccountBulkDataListForNonNigeria(BulkTransfer bulkTransfer) 
	{
		List<CustomerByAccountResponse> customerAccountResponsesList = null;
		try 
		{
			StringBuilder statementQuery = new StringBuilder("SELECT am.cust_id as strCustId, bulk.to_account_type AS strToAccountType, bulk.to_account_no AS strToAccountNumber,  ");
			statementQuery.append("bulk.amount AS transferAmount, am.account_type AS strAccountType, am.account_number AS strAccountNumber, ");			
			statementQuery.append("am.closing_balance AS strClosingBalance, am.ear_mark_amount AS strEarMarkAmount, am.pre_cred_amount AS strPreCredAmount,");			
			statementQuery.append("am.`status` AS strStatus, am.load_count AS strLoadCount, am.total_outstanding_balance AS strTotalOutstandingBal,");
			statementQuery.append("am.total_available_grace_period AS strAvailableGracePeriod,am.available_credit_limit AS strAvailableCreditLimit,");
			statementQuery.append("am.available_daily_limit AS strAvailableDailyLimit,am.available_monthly_limit AS strAvailableMonthlyLimit,");
			statementQuery.append("am.available_yearly_limit AS strAvailableYearlyLimit,atym.account_type_category AS strAccountTypeCategory,");
			statementQuery.append("atym.allow_load_cash AS strAllowLoadCash, atym.`status` AS strTStatus, cm.active_tier AS strActiveTier, ");
			statementQuery.append("glatm.account_type AS glAccountType, glatm.account_number AS glAccountNo, glatm.closing_balance AS glAccountBalance, glatm.account_description AS glAccountDescr ");
			
			statementQuery.append("FROM account_master AS am ");
			statementQuery.append("INNER JOIN customer_master AS cm ON TRIM(am.cust_id) = TRIM(cm.cust_id) ");
			statementQuery.append("INNER JOIN account_type_master AS atym ON TRIM(atym.account_type) = TRIM(am.account_type) ");			
			statementQuery.append("INNER JOIN bulk_transfer AS bulk ON TRIM(bulk.from_account_no) = TRIM(am.account_number) ");
			statementQuery.append("INNER JOIN gl_account_type_master AS glatm ON TRIM(glatm.account_number) = TRIM(atym.gl_account_no)");
			
			statementQuery.append("WHERE bulk.pre_transaction_id ='");			
			statementQuery.append(bulkTransfer.getStrPreTransactionId());
			statementQuery.append("'AND bulk.`status`='");
			statementQuery.append(bulkTransfer.getStrStatus());
			statementQuery.append("'");
			
			customerAccountResponsesList = jdbcTemplate.query(statementQuery.toString(), new BeanPropertyRowMapper<CustomerByAccountResponse>(CustomerByAccountResponse.class), new Object[] {});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return customerAccountResponsesList;
	}

	@Override
	public int[] batchEntryOfToAccountUpdateBulkTransfer(List<BulkTransfer> updatableBulkTransferList) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE bulk_transfer bulk SET bulk.is_verified = 'Y' ");
			updateQueryBuilder.append("WHERE bulk.pre_transaction_id = ? AND bulk.to_account_no = ?");
			
			return this.jdbcTemplate.batchUpdate(updateQueryBuilder.toString(), new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, updatableBulkTransferList.get(i).getStrPreTransactionId());
					psmt.setString(2, updatableBulkTransferList.get(i).getStrToAccountNo());
				}
				
				@Override
				public int getBatchSize() {
					return updatableBulkTransferList.size();
				}
			});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}

	@Override
	public int[] batchEntryOfFromAccountUpdateBulkTransfer(List<BulkTransfer> updatableBulkTransferList) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE bulk_transfer bulk SET bulk.is_verified = 'Y' ");
			updateQueryBuilder.append("WHERE bulk.pre_transaction_id = ? AND bulk.from_account_no = ?");
			
			return this.jdbcTemplate.batchUpdate(updateQueryBuilder.toString(), new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, updatableBulkTransferList.get(i).getStrPreTransactionId());
					psmt.setString(2, updatableBulkTransferList.get(i).getStrFromAccountNo());
				}
				
				@Override
				public int getBatchSize() {
					return updatableBulkTransferList.size();
				}
			});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}
	
	@Override
	public int[] batchEntryOfRjectedToAccountBulkTransfer(List<BulkTransfer> updatableBulkTransferList) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE bulk_transfer bulk SET bulk.is_verified = 'Y', ");
			updateQueryBuilder.append("bulk.`status`= 'rejected', bulk.rejected_reason = ? ");
			updateQueryBuilder.append("WHERE bulk.pre_transaction_id = ? AND bulk.to_account_no = ?");
			
			return this.jdbcTemplate.batchUpdate(updateQueryBuilder.toString(), new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, updatableBulkTransferList.get(i).getStrRejectedReason());
					psmt.setString(2, updatableBulkTransferList.get(i).getStrPreTransactionId());
					psmt.setString(3, updatableBulkTransferList.get(i).getStrToAccountNo());
				}
				
				@Override
				public int getBatchSize() {
					return updatableBulkTransferList.size();
				}
			});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}

	@Override
	public int[] batchEntryOfRjectedFromAccountBulkTransfer(List<BulkTransfer> updatableBulkTransferList) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE bulk_transfer bulk SET bulk.is_verified = 'Y', ");
			updateQueryBuilder.append("bulk.`status`= 'rejected', bulk.rejected_reason = ? ");
			updateQueryBuilder.append("WHERE bulk.pre_transaction_id = ? AND bulk.from_account_no = ?");
			
			return this.jdbcTemplate.batchUpdate(updateQueryBuilder.toString(), new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, updatableBulkTransferList.get(i).getStrRejectedReason());
					psmt.setString(2, updatableBulkTransferList.get(i).getStrPreTransactionId());
					psmt.setString(3, updatableBulkTransferList.get(i).getStrFromAccountNo());
				}
				
				@Override
				public int getBatchSize() {
					return updatableBulkTransferList.size();
				}
			});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	
	}

	@Override
	public int[] batchEntryOfApprovedStatusOfBulkTransferData(List<BulkTransfer> updatableBulkTransferList) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE bulk_transfer bulk SET bulk.transaction_id = ?, ");
			updateQueryBuilder.append("bulk.`status` = ?, bulk.checker_id = ?, ");
			updateQueryBuilder.append("bulk.bulk_response_date = ?, bulk.bulk_response_time = ? ");
			updateQueryBuilder.append("WHERE bulk.pre_transaction_id = ? AND bulk.from_account_no = ? ");
			updateQueryBuilder.append("AND bulk.to_account_no = ? ");
			
			return this.jdbcTemplate.batchUpdate(updateQueryBuilder.toString(), new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, updatableBulkTransferList.get(i).getStrTransactionId());
					psmt.setString(2, "approved");
					psmt.setString(3, updatableBulkTransferList.get(i).getStrCheckerId());
					psmt.setDate(4, Utils.getCurrentSqlDate());
					psmt.setTime(5, Utils.getCurrentSqlTime());
					psmt.setString(6, updatableBulkTransferList.get(i).getStrPreTransactionId());
					psmt.setString(7, updatableBulkTransferList.get(i).getStrFromAccountNo());
					psmt.setString(8, updatableBulkTransferList.get(i).getStrToAccountNo());
				}
				
				@Override
				public int getBatchSize() {
					return updatableBulkTransferList.size();
				}
			});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}

	@Override
	public List<BulkTransfer> getAuthorizePreTxnIdBulklist(BulkTransfer bulkTransfer) 
	{
		try 
		{
			StringBuilder statementQuery = new StringBuilder("SELECT bt.from_account_type AS strFromAccountType,bt.from_account_no AS strFromAccountNo,bt.from_account_name AS strFromAccountName,");
			statementQuery.append("bt.to_account_type AS strToAccountType,bt.to_account_no AS strToAccountNo,bt.to_account_name AS strToAccountName,bt.amount AS strAmount,");
			statementQuery.append("bt.maker_id AS strMakerId,bt.narration AS strNarration FROM bulk_transfer AS bt WHERE bt.pre_transaction_id = '"+bulkTransfer.getStrPreTransactionId()+"' AND bt.is_verified= 'Y' AND bt.status='pending'");

			List<BulkTransfer> authorizelist = jdbcTemplate.query(statementQuery.toString(),
					new BeanPropertyRowMapper<BulkTransfer>(BulkTransfer.class), new Object[] 
							{});			
			return authorizelist;		
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int[] batchEntryOfRejectedStatusOfBulkTransferData(List<BulkTransfer> updatableBulkTransferList) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE bulk_transfer bulk SET bulk.transaction_id = ?, ");
			updateQueryBuilder.append("bulk.`status` = ?, bulk.checker_id = ?, bulk.rejected_reason = ?, ");
			updateQueryBuilder.append("bulk.bulk_response_date = ?, bulk.bulk_response_time = ? ");
			updateQueryBuilder.append("WHERE bulk.pre_transaction_id = ? AND bulk.from_account_no = ? ");
			updateQueryBuilder.append("AND bulk.to_account_no = ? ");
			
			return this.jdbcTemplate.batchUpdate(updateQueryBuilder.toString(), new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, updatableBulkTransferList.get(i).getStrTransactionId());
					psmt.setString(2, updatableBulkTransferList.get(i).getStrStatus());
					psmt.setString(3, updatableBulkTransferList.get(i).getStrCheckerId());
					psmt.setString(4, updatableBulkTransferList.get(i).getStrRejectedReason());
					psmt.setDate(5, Utils.getCurrentSqlDate());
					psmt.setTime(6, Utils.getCurrentSqlTime());
					psmt.setString(7, updatableBulkTransferList.get(i).getStrPreTransactionId());
					psmt.setString(8, updatableBulkTransferList.get(i).getStrFromAccountNo());
					psmt.setString(9, updatableBulkTransferList.get(i).getStrToAccountNo());
				}
				
				@Override
				public int getBatchSize() {
					return updatableBulkTransferList.size();
				}
			});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	
	}

	@Override
	public int[] batchEntryOfBulkTransfer(List<BulkTransfer> bulkTransfers) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder insertQuerySb = new StringBuilder("INSERT INTO bulk_transfer ");
			insertQuerySb.append("(");
			insertQuerySb.append("pre_transaction_id, from_account_no, from_account_type, from_account_name,");
			insertQuerySb.append("to_account_no, to_account_type, to_account_name, amount, maker_id, ");
			insertQuerySb.append("status, is_verified, bulk_mode, bulk_request_date, bulk_request_time, narration");			
			insertQuerySb.append(")");
			insertQuerySb.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			return this.jdbcTemplate.batchUpdate(insertQuerySb.toString(), new BatchPreparedStatementSetter() 
					{
						@Override
						public void setValues(PreparedStatement psmt, int i) throws SQLException 
						{
							psmt.setString(1, bulkTransfers.get(i).getStrPreTransactionId());
							//----------From Account Info Start----------------------
							psmt.setString(2, bulkTransfers.get(i).getStrFromAccountNo());
							psmt.setString(3, bulkTransfers.get(i).getStrFromAccountType());
							psmt.setString(4, bulkTransfers.get(i).getStrFromAccountName());
							//----------From Account Info End----------------------
							
							//----------From Account Info Start----------------------
							psmt.setString(5, bulkTransfers.get(i).getStrToAccountNo());
							psmt.setString(6, bulkTransfers.get(i).getStrToAccountType());
							psmt.setString(7, bulkTransfers.get(i).getStrToAccountName());
							//----------From Account Info End----------------------
							
							psmt.setDouble(8, bulkTransfers.get(i).getStrAmount());
							
							psmt.setString(9, bulkTransfers.get(i).getStrMakerId());
							psmt.setString(10, bulkTransfers.get(i).getStrStatus());
							psmt.setString(11, bulkTransfers.get(i).getStrIsVerified());
							psmt.setString(12, bulkTransfers.get(i).getStrBulkMode());
							
							psmt.setDate(13, Utils.getCurrentSqlDate());
							psmt.setTime(14, Utils.getCurrentSqlTime());
							psmt.setString(15, bulkTransfers.get(i).getStrNarration());
						}
						
						@Override
						public int getBatchSize() {
							return bulkTransfers.size();
						}
					});
		}
		catch (Exception e) {
			amsLogger.writeInfoLog("Exception in batchEntryOfInstanAccount::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}

	@Override
	public List<BulkTransfer> getPreTxnIdBulklistForVerify(BulkTransfer bulkTransfer) 
	{
		amsLogger.writeInfoLog("getting bulk list for verify......11111111111");
		try 
		{
			StringBuilder statementQuery = new StringBuilder("SELECT bt.from_account_type AS strFromAccountType,bt.from_account_no AS strFromAccountNo,bt.from_account_name AS strFromAccountName,");
			statementQuery.append("bt.to_account_type AS strToAccountType,bt.to_account_no AS strToAccountNo,bt.to_account_name AS strToAccountName,bt.amount AS strAmount,");
			statementQuery.append("bt.maker_id AS strMakerId,bt.narration AS strNarration FROM bulk_transfer AS bt WHERE bt.pre_transaction_id = '"+bulkTransfer.getStrPreTransactionId().trim()+"' AND bt.is_verified= 'N' AND bt.status='pending'");

			amsLogger.writeInfoLog("Inside getPreTxnIdBulklistForVerify statementQuery:::"+statementQuery);
			
			List<BulkTransfer> authorizelist = jdbcTemplate.query(statementQuery.toString(), new BeanPropertyRowMapper<BulkTransfer>(BulkTransfer.class), new Object[] {});			
			return authorizelist;		
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
}
