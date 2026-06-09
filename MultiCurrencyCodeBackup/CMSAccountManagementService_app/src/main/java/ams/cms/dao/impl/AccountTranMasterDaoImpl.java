package ams.cms.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.api.model.AgencyBankingResponse;
import ams.cms.dao.AccountTranMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountTranMaster;
import ams.cms.utility.Utils;

@Repository
public class AccountTranMasterDaoImpl extends AbstractGenericDao<AccountTranMaster> implements AccountTranMasterDao 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(AccountTranMasterDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<AccountTranMaster> getAccountTransactionlist(AccountTranMaster accountTranMaster) 
	{
		try 
		{
			 String statementQuery = "SELECT atm.txn_id AS strTxn_id,atm.sys_id AS strSys_id, " 
				   		+"atm.tran_type AS strTran_type,ast.account_type AS strAccountType, " 
				   		+"atm.from_account_number AS strFrom_account_number, " 
				   		+"atm.to_account_number AS strTo_account_number, "  
				   		+"atm.transaction_amount AS strTransaction_amount, " 
				   		+"atm.local_tran_time AS strLocal_tran_time, "  
				   		+"atm.local_tran_date AS strLocal_tran_date, "
				   		+ "atm.switch_txn_date AS strSwitch_txn_date,"  
				   		+"ast.closing_balance AS strClosingBalance " 
				   		+"FROM account_tran_master AS atm INNER JOIN account_statement AS ast "  
				   		+"ON atm.sys_id=ast.transaction_id " 
				   		+"where ast.account_type = '"+accountTranMaster.getStrAccountType()+"' "
				   		+"AND atm.to_account_number = '"+accountTranMaster.getStrTo_account_number()+"' " 
				   		+"ORDER BY switch_txn_date DESC LIMIT 5";
			
					List<AccountTranMaster> accountTransactionSortlist  = jdbcTemplate.query(statementQuery,
							new BeanPropertyRowMapper<AccountTranMaster>(AccountTranMaster.class),
					new Object[] 
					{ 
						   // accountTranMaster.getStrTo_account_number(),   
						   // accountTranMaster.getStrTran_type()
					});
					
			return accountTransactionSortlist;
		}
		catch (Exception e) 
		{
			System.out.println("Exception in updateAccountStatementlist::"+e);
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public List<AccountTranMaster> getAcctTransactionlist(AccountTranMaster accountTranMaster) 
	{
		try
		{
		  String statementQuery = "SELECT atm.txn_id AS strTxn_id,sys_id AS strSys_id, " 
			   		+"atm.tran_type AS strTran_type,ast.account_type AS strAccountType, " 
			   		+"atm.from_account_number AS strFrom_account_number, " 
			   		+"atm.to_account_number AS strTo_account_number, "  
			   		+"atm.transaction_amount AS strTransaction_amount, " 
			   		+"atm.local_tran_time AS strLocal_tran_time, "  
			   		+"atm.local_tran_date AS strLocal_tran_date, "
			   		+"atm.switch_txn_date AS strSwitch_txn_date, "
			   		+"ast.closing_balance AS strClosingBalance " 
			   		+"FROM account_tran_master AS atm INNER JOIN account_statement AS ast "  
			   		+"ON atm.sys_id=ast.transaction_id " 
			   		+"where ast.account_type = '"+accountTranMaster.getStrAccountType()+"' "
			   		+ "AND atm.to_account_number = '"+accountTranMaster.getStrTo_account_number()+"' "
			   		+"and atm.switch_txn_date BETWEEN '"+accountTranMaster.getFromDate()+"' "
			   		+ "AND '"+accountTranMaster.getToDate()+"' "
			   		+ "ORDER BY atm.switch_txn_date ASC LIMIT 3";
			   
				List<AccountTranMaster> accountTransactionlist  = jdbcTemplate.query(statementQuery,
						new BeanPropertyRowMapper<AccountTranMaster>(AccountTranMaster.class),
				new Object[] 
				{ 
					//accountTranMaster.getStrTo_account_number(),
					//accountTranMaster.getStrTran_type()
					//accountTranMaster.getFromDate(),
					//accountTranMaster.getToDate()
					
				});
				
				return accountTransactionlist;
			  }
		      catch (Exception e) 
		     {
			   System.out.println("Exception in AccountTransactionlist::"+e);
			   e.printStackTrace();
		     }
		      return null;
	}
	
	@Override
	public AccountTranMaster getAccountInfo(AccountTranMaster accountTranMaster) 
	{
		try 
	    {
			StringBuilder selectQuery = new StringBuilder("SELECT am.closing_balance AS strClosingBalance, atm.account_type_category AS strAccountCategory, ");
			selectQuery.append("am.available_credit_limit AS strAvailableCreditLimit ");
			selectQuery.append("FROM account_master AS am INNER JOIN account_type_master atm ON am.account_type = atm.account_type ");
			selectQuery.append("WHERE am.account_number = ?");
			
			List<AccountTranMaster> accountTranMasters  = jdbcTemplate.query(selectQuery.toString(),
			new BeanPropertyRowMapper<AccountTranMaster>(AccountTranMaster.class),
			new Object[]  
			{ 
				accountTranMaster.getSenderAccountNo()
			});
			if (accountTranMasters != null && accountTranMasters.size() > 0) 
			{
				return accountTranMasters.get(0);
			}	
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}	
		return null;
	}
	
	//created by ankit on 16-04-2023
	//cannot find the narration
	@Override
	public List<AccountTranMaster> getTransactionIfExist(AccountTranMaster accountTranMaster)
	{
		try {
			
			String sql = "SELECT local_tran_date as strLocal_tran_date, local_tran_time as strLocal_tran_time,"
					+ " tran_type as strTran_type, from_account_number as strFrom_account_number,"
					+ " txn_id as strTxn_id,"
					+ " to_account_number as strTo_account_number, transaction_amount as strTransaction_amount" 
					+ " FROM account_tran_master" 
					+ " WHERE txn_id = ?";
			
			List<AccountTranMaster> accouontTranMaster = this.jdbcTemplate.query(sql,
					(RowMapper<AccountTranMaster>) new BeanPropertyRowMapper<AccountTranMaster>(AccountTranMaster.class), 
					new Object[] {accountTranMaster.getStrTxn_id()});
			return accouontTranMaster;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//created by ankit on 16-04-2023
	
	
	@Override
	public List<AccountTranMaster> getTxnAccountTypelist(AccountTranMaster accountTranMaster) 
	{
		try {
			 StringBuilder statementQuery = new StringBuilder("SELECT DATE(atm.switch_txn_date) AS txn_date,TIME(atm.switch_txn_date) AS txn_Time,atm.txn_id AS strTxn_id,atm.tran_type AS strTran_type, ");
			 statementQuery.append("atm.transaction_amount AS strTransaction_amount,atm.response_code AS strResponseCode, ");
			 statementQuery.append("atm.account_no AS strAccountNumber ,atm.from_account_number AS strFrom_account_number, ");
			 statementQuery.append("atm.to_account_number AS strTo_account_number from account_tran_master AS atm WHERE ");
			 statementQuery.append("atm.switch_txn_date BETWEEN '"+accountTranMaster.getFromDate()+" 00:00:00' AND '"+accountTranMaster.getToDate()+" 23:59:59' ");

		     if (accountTranMaster.getStrTxnTypeKeyWord()!=null && accountTranMaster.getStrTxnTypeKeyWord().trim().length() > 0)
			 {
				 statementQuery.append("AND atm.tran_type='"+accountTranMaster.getStrTxnTypeKeyWord()+"' ");
			 } 
			 List<AccountTranMaster> accountTxnlist = jdbcTemplate.query(statementQuery.toString(),
			 new BeanPropertyRowMapper<AccountTranMaster>(AccountTranMaster.class), new Object[]
			  {});
			 return accountTxnlist;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		 return null;
	}


	@Override
	public int updateAccountTranMasterColumns(AccountTranMaster accountTranMaster) 
	{
		int count = 0;
		try
		{
			List<Object> objectList = new ArrayList<Object>();
			
			StringBuilder queryBuilder = new StringBuilder("UPDATE account_tran_master SET ");
			
			if (accountTranMaster.getStrResponseCode() != null && accountTranMaster.getStrResponseCode().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("response_code = ").append("?");
				objectList.add(accountTranMaster.getStrResponseCode().trim());
			}
			if (accountTranMaster.getStrAuthCode()!=null && accountTranMaster.getStrAuthCode().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("auth_code = ").append("?");
				objectList.add(accountTranMaster.getStrAuthCode().trim());
			}
			if (accountTranMaster.getStrReservefield2()!=null && accountTranMaster.getStrReservefield2().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("reserve_field2 = ").append("?");
				objectList.add(accountTranMaster.getStrReservefield2().trim());
			}
			if (accountTranMaster.getStrReservefield1()!=null && accountTranMaster.getStrReservefield1().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("reserve_field1 = ").append("?");
				objectList.add(accountTranMaster.getStrReservefield1().trim());
			}
			if (accountTranMaster.getStrSrcTxnId()!=null && accountTranMaster.getStrSrcTxnId().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("source_txn_id = ").append("?");
				objectList.add(accountTranMaster.getStrSrcTxnId().trim());
			}
			if (accountTranMaster.getStrSys_id() != null && accountTranMaster.getStrSys_id().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("sys_id = ").append("?");
				objectList.add(accountTranMaster.getStrSys_id().trim());
			}
			
			queryBuilder.append(" WHERE txn_id = ").append("?");
			objectList.add(accountTranMaster.getStrTxn_id().trim());
			
			Object[] object = objectList.toArray();
			
			count = this.jdbcTemplate.update(queryBuilder.toString(), object);
			return count;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return count;
	}
	
	// for search transaction by id--prashant Tayde Start
	@Override
	public List<AccountTranMaster> getTransactionByTxnId(AccountTranMaster accountTranMaster) 
	{
		try {
			String searchTransactionQuery = "SELECT atm.txn_id AS strTxn_id, DATE(atm.switch_txn_date) AS txn_Date, TIME(atm.switch_txn_date) AS txn_Time,"
					+ "tran_type AS strTran_type, transaction_amount AS strTransaction_amount,"
					+ " IFNULL(atm.account_no, '-' ) AS strAccountNumber, "
					+ " IFNULL(atm.from_account_number,'-') AS strFrom_account_number, "
					+ " IFNULL(atm.to_account_number,'-') AS strTo_account_number, "
					+ " IFNULL(jt.maker_Id, '-') AS strMakerId, IFNULL(jt.checker_Id,'-') AS strCheckerId, IFNULL(jt.txn_status,'-') AS strTxnStatus,"
					+ " IFNULL((SELECT CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) from account_master am WHERE am.account_number = atm.account_no),'-') AS strAccountName,"
					+ " IFNULL((SELECT am.account_type from account_master am WHERE am.account_number = atm.account_no),'-') AS strAccountType,"
					+ " IFNULL((SELECT am.account_type from account_master am WHERE am.account_number = atm.from_account_number),'-') AS strFromAccountType,"
					+ " IFNULL((SELECT CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) from account_master am WHERE am.account_number = atm.from_account_number),'-') AS strFromAccountName,"
					+ " IFNULL((SELECT am.account_type from account_master am WHERE am.account_number = atm.to_account_number),'-') AS strToAccountType,"
					+ " IFNULL((SELECT CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) from account_master am WHERE am.account_number = atm.to_account_number),'-') AS strToAccountName,"
					+ " IFNULL(atm.auth_code,'-') AS strAuthCode, IFNULL(atm.response_code, '-' ) AS strResponseCode from account_tran_master atm LEFT JOIN journal_transfer jt ON jt.txn_id = atm.txn_id "
					+ " WHERE atm.txn_id = '" + accountTranMaster.getStrTxn_id() + "' ";

			System.out.println("searchTransactionQuery::" + searchTransactionQuery);
			List<AccountTranMaster> getSearchTransaction = jdbcTemplate.query(searchTransactionQuery,
					new BeanPropertyRowMapper<AccountTranMaster>(AccountTranMaster.class), new Object[] {});
			System.out.println("getSearchTransaction:::" + getSearchTransaction.size());
			return getSearchTransaction;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	// for search transaction by id--prashant Tayde End


	@Override
	public int[] batchEntryOfAccountTranMaster(List<AccountTranMaster> accountTranMasters) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder batchInsertQuerySb = new StringBuilder("INSERT INTO account_tran_master");
			batchInsertQuerySb.append("(txn_id, participant_id, local_tran_date, local_tran_time, tran_type, transaction_amount,");
			batchInsertQuerySb.append("response_code, from_account_number, to_account_number, switch_txn_date) ");
			batchInsertQuerySb.append("values(?,?,?,?,?,?,?,?,?,?)");
			
			return this.jdbcTemplate.batchUpdate(batchInsertQuerySb.toString(), new BatchPreparedStatementSetter() 
					{
						@Override
						public void setValues(PreparedStatement psmt, int i) throws SQLException 
						{
							psmt.setString(1, accountTranMasters.get(i).getStrTxn_id());
							psmt.setString(2, accountTranMasters.get(i).getStrParticipantId());
							psmt.setDate(3, Utils.getCurrentSqlDate());
							//psmt.setTime(4, Utils.getCurrentSqlTime());
							psmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
							psmt.setString(5, accountTranMasters.get(i).getStrTran_type());
							psmt.setString(6, accountTranMasters.get(i).getStrTransaction_amount());
							psmt.setString(7, accountTranMasters.get(i).getStrResponseCode());
							psmt.setString(8, accountTranMasters.get(i).getStrFrom_account_number());
							psmt.setString(9, accountTranMasters.get(i).getStrTo_account_number());
							//psmt.setDate(10, Utils.getCurrentSqlDate());
							psmt.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
						}
						
						@Override
						public int getBatchSize() {
							return accountTranMasters.size();
						}
					});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}
	
	@Override
	public int updateAccountTranMasterForSendMoney(AccountTranMaster accountTranMaster) {
		try {
			int count = this.jdbcTemplate.update("UPDATE account_tran_master  as atm SET atm.reserve_field1 =?,atm.response_code=? WHERE atm.txn_id=?",
					new Object[] {
							accountTranMaster.getStrReservefield1(),
							accountTranMaster.getStrResponseCode(),
							accountTranMaster.getStrTxn_id() });
			
			return count;
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}


	@SuppressWarnings("unchecked")
	@Override
	public AccountTranMaster getAccountTranMaster(AccountTranMaster accountTranMaster) 
	{
		boolean isQueryExecute = false;
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (accountTranMaster.getStrTxn_id() != null && accountTranMaster.getStrTxn_id().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strTxn_id", accountTranMaster.getStrTxn_id().trim()));
				isQueryExecute = true;
			}
			if (accountTranMaster.getStrSrcTxnId() != null && accountTranMaster.getStrSrcTxnId().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strSrcTxnId", accountTranMaster.getStrSrcTxnId().trim()));
				isQueryExecute = true;
			}
			if (accountTranMaster.getStrSys_id() != null && accountTranMaster.getStrSys_id().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strSys_id", accountTranMaster.getStrSys_id().trim()));
				isQueryExecute = true;
			}
			
			if (isQueryExecute) 
			{
				List<AccountTranMaster> listData = (List<AccountTranMaster>) criteria.list();
				if (listData !=null && listData.size() > 0) 
				{
					return listData.get(0);
				}
			}			
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}


	@Override
	public List<AgencyBankingResponse> getAccountTranMasterWithAccountInfo(AccountTranMaster accountTranMaster) 
	{
		try 
		{
			 StringBuilder selectQueryDb = new StringBuilder("SELECT atm.txn_id AS amsTransactionId, atm.transaction_amount AS txnAmount, atm.tran_type AS txnType,  ");
			 selectQueryDb.append("atm.response_code AS responseCode, atm.source_txn_id AS montraTxnId, ");
			 selectQueryDb.append("atm.auth_code AS authCode, atm.processing_code AS processingCode, atm.reserve_field1 AS receipentAccountNo, atm.reserve_field2 AS tranStatus,");
			 selectQueryDb.append("am.cust_id AS strCustId, am.email AS emailId, am.account_number AS accountNo, am.closing_balance AS availableBalance, ");
			 
			 selectQueryDb.append("gtm.account_number AS glAccountNo, gtm.account_type AS glAccountType, gtm.closing_balance AS glAcccounBalance, ");
			 selectQueryDb.append("gtm.account_description AS glAccountDescription, CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName ");
			 
			 selectQueryDb.append("FROM account_tran_master AS atm ");
			 selectQueryDb.append("LEFT JOIN account_master AS am ON am.account_number = atm.from_account_number ");
			 selectQueryDb.append("LEFT JOIN gl_account_type_master AS gtm ON gtm.account_number = atm.to_account_number ");
			 //selectQueryDb.append("LEFT JOIN montra_account_master AS mam ON mam.cust_id = am.cust_id ");
			 selectQueryDb.append("WHERE atm.txn_id = '"+accountTranMaster.getStrTxn_id().trim()+"' ");
			 selectQueryDb.append("AND atm.source_txn_id = '"+accountTranMaster.getStrSrcTxnId().trim()+"'");
			 
			 amsLogger.writeInfoLog("getAccountTranMasterWithAccountInfo::["+selectQueryDb.toString()+"]");			 
			 List<AgencyBankingResponse> agencyBankingResponses = jdbcTemplate.query(selectQueryDb.toString(), new BeanPropertyRowMapper<AgencyBankingResponse>(AgencyBankingResponse.class), new Object[] {});
			 
			 if(agencyBankingResponses!=null && agencyBankingResponses.size() > 0 ) 
			 {
				 return agencyBankingResponses;
			 }
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		 return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public AccountTranMaster getAccountTranInfoData(AccountTranMaster accountTranMaster) 
	{
		try 
		{
			boolean isCriteriaExecute = false;
			
			Criteria criteria = createEntityCriteria();			
			if (accountTranMaster!=null && accountTranMaster.getStrSys_id()!= null && accountTranMaster.getStrSys_id().trim().length() > 0) 
			{
				criteria.add(Restrictions.eq("strSys_id", accountTranMaster.getStrSys_id().trim()));
				isCriteriaExecute = true;
			}
			if (accountTranMaster!=null && accountTranMaster.getStrSrcTxnId() != null && accountTranMaster.getStrSrcTxnId().trim().length() > 0) 
			{
				criteria.add(Restrictions.eq("strSrcTxnId", accountTranMaster.getStrSrcTxnId().trim()));
				isCriteriaExecute = true;
			}
			
			if (isCriteriaExecute) 
			{
				List<AccountTranMaster> accountTranMasterListData = (List<AccountTranMaster>) criteria.list();
				if (accountTranMasterListData !=null && accountTranMasterListData.size() > 0) 
				{
					return accountTranMasterListData.get(0);
				}
			}			
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
}
