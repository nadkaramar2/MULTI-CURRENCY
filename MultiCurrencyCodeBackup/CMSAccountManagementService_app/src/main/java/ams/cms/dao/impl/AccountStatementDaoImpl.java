package ams.cms.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Criteria;

import ams.cms.config.CommonConstants;
import ams.cms.dao.AccountStatementDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountStatement;

@Repository
public class AccountStatementDaoImpl extends AbstractGenericDao<AccountStatement> implements AccountStatementDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(AccountStatementDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<AccountStatement> getAccountStatementBasedonParameter(AccountStatement accountStatement) 
	{
		try
		{
			StringBuilder statementQuerySb = new StringBuilder("SELECT account_number AS strAccountNumber, ");
			statementQuerySb.append("account_type AS strAccountType, transaction_id AS strTranID, transaction_date AS strTransactionDate,");
			statementQuerySb.append("naration AS strTransactionDetails, transaction_id AS strTransactionID, ");
			statementQuerySb.append("transaction_amount AS strTransactionAmount, txn_mode AS strTranMode,");
			statementQuerySb.append("closing_balance AS strClosingBalance, txn_type AS strTransactionType ");
			statementQuerySb.append("FROM account_statement where account_type = '").append(accountStatement.getStrAccountType()).append("' ");
			statementQuerySb.append("AND account_number = '").append(accountStatement.getStrAccountNumber()).append("' ");
			statementQuerySb.append("AND transaction_date BETWEEN ");
			statementQuerySb.append("'").append(accountStatement.getFromDate()).append(" 00:00:00' ");
			statementQuerySb.append("AND '").append(accountStatement.getToDate()).append(" 23:59:59' ");
			statementQuerySb.append("ORDER BY transaction_date DESC");
			
			List<AccountStatement> accountStatementsList = jdbcTemplate.query(statementQuerySb.toString(), new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class), new Object[] {});
			return accountStatementsList;
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	@Override
	public List<AccountStatement> getAccountStatementlist(AccountStatement accountStatement) 
	{
		try 
		{
				          
			Criteria criteria = createEntityCriteria();
			{
			if(accountStatement.getStrTransactionDate() != null && accountStatement.getStrTransactionDate().trim().length() > 0)
			   criteria.add(Restrictions.eq("fromDate",accountStatement.getStrTransactionDate()));
			}
			if(accountStatement.getStrTransactionDate() != null && accountStatement.getStrTransactionDate().trim().length() > 0) 
			{
			   criteria.add(Restrictions.eq("toDate",accountStatement.getStrTransactionDate()));
			}
			if(accountStatement.getStrAccountType() != null && accountStatement.getStrAccountType().trim().length() > 0) 
			{
				criteria.add(Restrictions.eq("strAccountType",accountStatement.getStrAccountType()));
			}
			if(accountStatement.getStrAccountNumber() != null && accountStatement.getStrAccountNumber().trim().length() > 0) 
			{
				criteria.add(Restrictions.eq("strAccountNumber",accountStatement.getStrAccountNumber()));
			}
			
			List<AccountStatement> accountlist = (List<AccountStatement>) criteria.list();
			if (accountlist !=null && accountlist.size() > 0) 
			{
				return accountlist;
			}
		}
		catch (Exception e) 
		{
			System.out.println("Exception in getAccountstatementlist::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	//for account statement last 5 record fetch DaoImpl
	@Override
	public List<AccountStatement> updateAccountStatementlist(AccountStatement accountStatement) 
	{
		try
		{
			String statementQuery = "SELECT participant_id AS strParticipantId,account_number AS strAccountNumber, "
					+"account_type AS strAccountType,closing_balance AS strClosingBalance, "  
					+"transaction_amount AS strTransactionAmount, " 
					+"transaction_date AS strTransactionDate,transaction_id AS strTransactionID, " 
					+"txn_type AS strTransactionType, txn_mode AS strTranMode FROM account_statement "
					+"where account_type = ? AND account_number = ? " 
					+"ORDER BY transaction_date DESC LIMIT 5 ";
			
			List<AccountStatement> accountStatementsList = jdbcTemplate.query(statementQuery,
					new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class),
			new Object[] 
			{ 
					accountStatement.getStrAccountType(), 
					accountStatement.getStrAccountNumber()
			});
			return accountStatementsList;
		}
		catch (Exception e)
		{
			System.out.println("getAccountStatementBasedonParameter::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;	
	}

	@Override
	public List<AccountStatement> getAccountStatements(AccountStatement accountStatement) 
	{
		try 
		{
			String accounStatementQuery = "SELECT CONCAT_WS(' ', acmst.first_name, acmst.middle_name, acmst.last_name) AS strAccountHolderName,"
					+ "accstmt.account_number AS strAccountNumber, accstmt.account_type AS strAccountType, "
					+ "DATE(accstmt.transaction_date) AS strTxnDate, TIME(accstmt.transaction_date) AS strTxnTime,"
					+ "accstmt.transaction_id AS strTransactionID, accstmt.transaction_date AS strTransactionDate, "
					+ "accstmt.transaction_amount AS strTransactionAmount, accstmt.closing_balance AS strClosingBalance, "
					+ "accstmt.naration AS strTransactionDetails, accstmt.txn_type AS strTranType, "
					+ "accstmt.txn_mode AS strTranMode "
					+ "from account_statement accstmt LEFT JOIN account_master acmst "
					+ "ON accstmt.account_number = acmst.account_number "
					+ "where accstmt.account_type = '"+accountStatement.getStrAccountType()+"' "
					+ "and accstmt.account_number = '"+accountStatement.getStrAccountNumber()+"' "
					+ "and accstmt.transaction_date BETWEEN '"+accountStatement.getFromDate()+" 00:00:00' "
					+ "AND '"+accountStatement.getToDate()+" 23:59:59' "
					+ "ORDER BY accstmt.transaction_date DESC";
			
			System.out.println("accounStatementQuery::"+accounStatementQuery);
			
			List<AccountStatement> getAccountStatement = jdbcTemplate.query(accounStatementQuery, new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class), new Object[]{});

			System.out.println("getAccountStatements:::" + getAccountStatement.size());
			return getAccountStatement;
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<AccountStatement> getAccountStatementByDate(AccountStatement accountStatement) 
	{
		try 
		{
			String accounStatementQuery = "SELECT CONCAT_WS(' ', acmst.first_name, acmst.middle_name, acmst.last_name) AS strAccountHolderName,"
					+ "accstmt.account_number AS strAccountNumber, accstmt.account_type AS strAccountType, "
					+ "accstmt.transaction_id AS strTransactionID, accstmt.transaction_date AS strTransactionDate, "
					+ "accstmt.transaction_amount AS strTransactionAmount, accstmt.closing_balance AS strClosingBalance, "
					+ "accstmt.naration AS strTransactionDetails, accstmt.txn_type AS strTranType, "
					+ "accstmt.txn_mode AS strTranMode "
					+ "from account_statement accstmt LEFT JOIN account_master acmst "
					+ "ON accstmt.account_number = acmst.account_number "
					+ "where is_gl_type = 'N' "
					+ "and accstmt.transaction_date BETWEEN ? AND ? "
					+ "ORDER BY accstmt.transaction_date DESC";
			
			List<AccountStatement> getAccountStatement = jdbcTemplate.query(accounStatementQuery, new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class),new Object[]
			{ 
				accountStatement.getFromDate(), 
				accountStatement.getToDate() 
			});

			System.out.println("getAccountStatements:::" + getAccountStatement.size());
			return getAccountStatement;
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public List<AccountStatement> getAccountTranscation(AccountStatement accountStatement) 
	{
		try
		{
			int offset = 0;
			if (accountStatement.getPageSize()!= null && accountStatement.getPageSize().trim().length() > 0) 
			{
				if (accountStatement.getPageNumber() != null && accountStatement.getPageNumber().trim().length() > 0)
				{
					offset = getOffsetValue(accountStatement.getPageSize(), accountStatement.getPageNumber());
				}
			}			
			String statementQuery = getAccountStatementQuery(accountStatement, offset);
			
			List<AccountStatement> accountTranList = jdbcTemplate.query(statementQuery, new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class), new Object[]{});
			return accountTranList;
			
			/*
			StringBuilder statementQuerySb = new StringBuilder("SELECT Distinct ast.transaction_id AS strTransactionID, ");
			statementQuerySb.append("ast.account_type AS strAccountType, ast.closing_balance AS strClosingBalance, ");
			statementQuerySb.append("ast.transaction_amount AS transactionAmount, ast.txn_type AS strTransactionType, ast.is_gl_type AS strIsGLType, ");
			
			if(!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				statementQuerySb.append("transaction_date AS strTransactionDate, txn_type AS strTransactionType, ");
			}
			else
			{
				statementQuerySb.append("atm.source_txn_id AS montraTxnId,");
				statementQuerySb.append("transaction_date AS transactionDate, DATE(transaction_date) AS strTransactionDate, TIME(transaction_date) AS strTransactionTime, ");
			}			
			statementQuerySb.append("ast.txn_mode AS strTranMode, ast.entity_info AS entityInfo, ast.entity_number AS entityNumber FROM account_statement AS ast ");
			
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				statementQuerySb.append("LEFT JOIN account_tran_master AS atm ON ast.transaction_id = atm.txn_id ");
			}
			
			statementQuerySb.append("WHERE ");			
			if (accountStatement.getStrAccountType()!=null && accountStatement.getStrAccountType().trim().length() > 0) 
			{
				statementQuerySb.append("account_type = '").append(accountStatement.getStrAccountType().trim()).append("' ");
				statementQuerySb.append("AND");
			}
			
			statementQuerySb.append("ast.account_number = '"+accountStatement.getStrAccountNumber().trim()+"' AND ");
			statementQuerySb.append("ast.transaction_date BETWEEN '"+accountStatement.getFromDate().trim()+" 00:00:00' AND ");
			statementQuerySb.append("'"+accountStatement.getToDate().trim()+" 23:59:59' ORDER BY ast.transaction_id DESC ");
			
			if (accountStatement.getPageSize() != null && accountStatement.getPageSize().trim().length() > 0)
			{
				statementQuerySb.append("LIMIT "+accountStatement.getPageSize() +" ");
				if (accountStatement.getPageNumber() != null && accountStatement.getPageNumber().trim().length() > 0) 
				{
					int offset = getOffsetValue(accountStatement.getPageSize(), accountStatement.getPageNumber());			
					statementQuerySb.append("OFFSET "+offset +" ");
				}
			}
			
			amsLogger.writeInfoLog("Statement Query::"+statementQuerySb.toString());
			
			List<AccountStatement> accountTranList = jdbcTemplate.query(statementQuerySb.toString(), new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class), new Object[]{});
			return accountTranList;
			*/
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<AccountStatement> getLastFiveAccountTxn(AccountStatement accountStatement) 
	{
		try 
		{
			String statementQuery = "SELECT transaction_id AS strTransactionID,account_type AS strAccountType, "
					+ "closing_balance AS strClosingBalance,transaction_amount AS transactionAmount, "
					+ "transaction_date AS strTransactionDate, transaction_date AS transactionDate, txn_type AS strTransactionType, is_gl_type AS strIsGLType,"  
					+ "txn_mode AS strTranMode FROM account_statement where account_number = '"+accountStatement.getStrAccountNumber()+"' ";
					
					if (accountStatement.getStrAccountType() != null && accountStatement.getStrAccountType().trim().length() > 0)
					{
						statementQuery = statementQuery + "AND account_type = '"+accountStatement.getStrAccountType()+"' ";
					}
					
			        //+ "ORDER BY transaction_date DESC LIMIT 5 ";
					statementQuery = statementQuery + "ORDER BY transaction_id DESC LIMIT 5 ";
			
			List<AccountStatement> accountTranList = jdbcTemplate.query(statementQuery, new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class), new Object[] {});
			return accountTranList;	
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int[] batchEntryOfAccountStatementMaster(List<AccountStatement> accountStatements) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder batchInsertQuerySb = new StringBuilder("INSERT INTO account_statement");
			batchInsertQuerySb.append("(participant_id, account_number, account_type, closing_balance, is_gl_type, transaction_amount,");
			batchInsertQuerySb.append("transaction_date, naration, transaction_id, txn_type, txn_mode, entity_info, entity_number) ");
			batchInsertQuerySb.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			return this.jdbcTemplate.batchUpdate(batchInsertQuerySb.toString(), new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, accountStatements.get(i).getStrParticipantId());
					psmt.setString(2, accountStatements.get(i).getStrAccountNumber());
					psmt.setString(3, accountStatements.get(i).getStrAccountType());
					psmt.setString(4, accountStatements.get(i).getStrClosingBalance());
					psmt.setString(5, accountStatements.get(i).getStrIsGLType());
					psmt.setString(6, accountStatements.get(i).getStrTransactionAmount());
					
					psmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
					
					psmt.setString(8, accountStatements.get(i).getStrNaration());
					psmt.setString(9, accountStatements.get(i).getStrTransactionID());
					psmt.setString(10, accountStatements.get(i).getStrTransactionType());
					psmt.setString(11, accountStatements.get(i).getStrTransactionMode());
					
					psmt.setString(12, accountStatements.get(i).getEntityInfo());
					psmt.setString(13, accountStatements.get(i).getEntityNumber());
				}
						
				@Override
				public int getBatchSize() {
					return accountStatements.size();
				}
			});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}

	@Override
	public List<AccountStatement> getLastFiveAccountTxnForMontra(AccountStatement accountStatement) {

		try
		{
			StringBuilder statementQuerySb = new StringBuilder("SELECT Distinct ast.transaction_id AS strTransactionID, ");
			statementQuerySb.append("ast.account_type AS strAccountType, ast.closing_balance AS strClosingBalance, ");
			statementQuerySb.append("ast.transaction_amount AS transactionAmount, ast.txn_type AS strTransactionType, ast.is_gl_type AS strIsGLType, ");
			
			if(!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				statementQuerySb.append("transaction_date AS strTransactionDate, txn_type AS strTransactionType, ");
			}
			else
			{
				statementQuerySb.append("atm.source_txn_id AS montraTxnId,");
				statementQuerySb.append("transaction_date AS transactionDate, DATE(transaction_date) AS strTransactionDate, TIME(transaction_date) AS strTransactionTime, ");
			}			
			statementQuerySb.append("ast.txn_mode AS strTranMode FROM account_statement AS ast ");
			
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				statementQuerySb.append("LEFT JOIN account_tran_master AS atm ON ast.transaction_id = atm.txn_id ");
			}			
			statementQuerySb.append("WHERE ");			
			if (accountStatement.getStrAccountType()!=null && accountStatement.getStrAccountType().trim().length() > 0) 
			{
				statementQuerySb.append("account_type = '").append(accountStatement.getStrAccountType().trim()).append("' ");
				statementQuerySb.append("AND ");
			}
			
			statementQuerySb.append("ast.account_number = '"+accountStatement.getStrAccountNumber().trim()+"' ORDER BY transaction_id DESC LIMIT 5  ");
			
			List<AccountStatement> accountTranList = jdbcTemplate.query(statementQuerySb.toString(), new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class), new Object[]{});
			System.out.println(statementQuerySb);
			return accountTranList;
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<AccountStatement> getAgencyAccountStatementTranscationList(AccountStatement accountStatement) 
	{
		try
		{
			int offset = 0;
			if (accountStatement.getPageSize()!= null && accountStatement.getPageSize().trim().length() > 0) 
			{
				if (accountStatement.getPageNumber() != null && accountStatement.getPageNumber().trim().length() > 0)
				{
					offset = getOffsetValue(accountStatement.getPageSize(), accountStatement.getPageNumber());
				}
			}
			String statementQuery = getAgencyAccountStatementQuery(accountStatement, offset);
			
			List<AccountStatement> accountTranList = jdbcTemplate.query(statementQuery, new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class), new Object[]{});
			return accountTranList;
			
			/*
			StringBuilder statementQuerySb = new StringBuilder("SELECT Distinct ast.transaction_id AS strTransactionID, ");
			statementQuerySb.append("ast.account_type AS strAccountType, ast.closing_balance AS strClosingBalance, ");
			statementQuerySb.append("ast.transaction_amount AS transactionAmount, ast.txn_type AS strTransactionType, ast.is_gl_type AS strIsGLType, ");
			
			if(!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				statementQuerySb.append("transaction_date AS strTransactionDate, txn_type AS strTransactionType, ");
			}
			else
			{
				statementQuerySb.append("atm.source_txn_id AS montraTxnId,");
				statementQuerySb.append("transaction_date AS transactionDate, DATE(transaction_date) AS strTransactionDate, TIME(transaction_date) AS strTransactionTime, ");
			}			
			statementQuerySb.append("ast.txn_mode AS strTranMode, ast.entity_info AS entityInfo, ast.entity_number AS entityNumber FROM account_statement AS ast ");
			
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				statementQuerySb.append("LEFT JOIN account_tran_master AS atm ON ast.transaction_id = atm.txn_id ");
			}
			
			statementQuerySb.append("WHERE ");			
			if (accountStatement.getStrAccountType()!=null && accountStatement.getStrAccountType().trim().length() > 0) 
			{
				statementQuerySb.append("account_type = '").append(accountStatement.getStrAccountType().trim()).append("' ");
				statementQuerySb.append("AND");
			}
			
			statementQuerySb.append("ast.account_number = '"+accountStatement.getStrAccountNumber().trim()+"' AND ");
			statementQuerySb.append("ast.txn_type LIKE 'AB%' AND ");
			statementQuerySb.append("ast.transaction_date BETWEEN '"+accountStatement.getFromDate().trim()+" 00:00:00' AND ");
			statementQuerySb.append("'"+accountStatement.getToDate().trim()+" 23:59:59' ORDER BY ast.transaction_id DESC ");
			
			statementQuerySb.append("LIMIT "+accountStatement.getPageSize() +" ");			
			int offset = getOffsetValue(accountStatement.getPageSize(), accountStatement.getPageNumber());			
			statementQuerySb.append("OFFSET "+offset +" ");
			
			amsLogger.writeInfoLog("Statement Query::"+statementQuerySb.toString());
			
			List<AccountStatement> accountTranList = jdbcTemplate.query(statementQuerySb.toString(), new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class), new Object[]{});
			return accountTranList;
			*/
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	@Override
	public List<AccountStatement> getLastFiveAccountTransaction(AccountStatement accountStatement) 
	{
		try
		{
			int offset = 0;
			accountStatement.setPageSize("5");						
			String statementQuery = getAccountStatementQuery(accountStatement, offset);
			
			List<AccountStatement> accountTranList = jdbcTemplate.query(statementQuery, new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class), new Object[]{});
			return accountTranList;
			
			/*
			StringBuilder statementQuerySb = new StringBuilder("SELECT Distinct ast.transaction_id AS strTransactionID, ");
			statementQuerySb.append("ast.account_type AS strAccountType, ast.closing_balance AS strClosingBalance, ");
			statementQuerySb.append("ast.transaction_amount AS transactionAmount, ast.txn_type AS strTransactionType, ast.is_gl_type AS strIsGLType, ");
			
			if(!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				statementQuerySb.append("transaction_date AS strTransactionDate, txn_type AS strTransactionType, ");
			}
			else
			{
				statementQuerySb.append("atm.source_txn_id AS montraTxnId,");
				statementQuerySb.append("transaction_date AS transactionDate, DATE(transaction_date) AS strTransactionDate, TIME(transaction_date) AS strTransactionTime, ");
			}			
			statementQuerySb.append("ast.txn_mode AS strTranMode, ast.entity_info AS entityInfo, ast.entity_number AS entityNumber FROM account_statement AS ast ");
			
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				statementQuerySb.append("LEFT JOIN account_tran_master AS atm ON ast.transaction_id = atm.txn_id ");
			}
			
			statementQuerySb.append("WHERE ");			
			if (accountStatement.getStrAccountType()!=null && accountStatement.getStrAccountType().trim().length() > 0) 
			{
				statementQuerySb.append("account_type = '").append(accountStatement.getStrAccountType().trim()).append("' ");
				statementQuerySb.append("AND");
			}
			
			statementQuerySb.append("ast.account_number = '"+accountStatement.getStrAccountNumber().trim()+"' AND ");
			statementQuerySb.append("ast.transaction_date BETWEEN '"+Utils.getCurrentDbDateStr().trim()+" 00:00:00' AND ");			
			statementQuerySb.append("'"+Utils.getCurrentDbDateStr().trim()+" 23:59:59' ORDER BY ast.transaction_id DESC ");
			
			statementQuerySb.append("LIMIT 5 ");
			
			amsLogger.writeInfoLog("Statement Query::"+statementQuerySb.toString());
			
			List<AccountStatement> accountTranList = jdbcTemplate.query(statementQuerySb.toString(), new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class), new Object[]{});
			return accountTranList;
			*/
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<AccountStatement> getLastFiveAgencyAccountStatementTransactionList(AccountStatement accountStatement) 
	{
		try
		{
			int offset = 0;
			accountStatement.setPageSize("5");			
			String statementQuery = getAgencyAccountStatementQuery(accountStatement, offset);
			
			List<AccountStatement> accountTranList = jdbcTemplate.query(statementQuery, new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class), new Object[]{});
			return accountTranList;
			
			/*
			StringBuilder statementQuerySb = new StringBuilder("SELECT Distinct ast.transaction_id AS strTransactionID, ");
			statementQuerySb.append("ast.account_type AS strAccountType, ast.closing_balance AS strClosingBalance, ");
			statementQuerySb.append("ast.transaction_amount AS transactionAmount, ast.txn_type AS strTransactionType, ast.is_gl_type AS strIsGLType, ");
			
			if(!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				statementQuerySb.append("transaction_date AS strTransactionDate, txn_type AS strTransactionType, ");
			}
			else
			{
				statementQuerySb.append("atm.source_txn_id AS montraTxnId,");
				statementQuerySb.append("transaction_date AS transactionDate, DATE(transaction_date) AS strTransactionDate, TIME(transaction_date) AS strTransactionTime, ");
			}			
			statementQuerySb.append("ast.txn_mode AS strTranMode, ast.entity_info AS entityInfo, ast.entity_number AS entityNumber FROM account_statement AS ast ");
			
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				statementQuerySb.append("LEFT JOIN account_tran_master AS atm ON ast.transaction_id = atm.txn_id ");
			}
			
			statementQuerySb.append("WHERE ");			
			if (accountStatement.getStrAccountType()!=null && accountStatement.getStrAccountType().trim().length() > 0) 
			{
				statementQuerySb.append("account_type = '").append(accountStatement.getStrAccountType().trim()).append("' ");
				statementQuerySb.append("AND");
			}
			
			statementQuerySb.append("ast.account_number = '"+accountStatement.getStrAccountNumber().trim()+"' AND ");
			statementQuerySb.append("ast.txn_type LIKE 'AB%' AND ");
			statementQuerySb.append("ast.transaction_date BETWEEN '"+Utils.getCurrentDbDateStr().trim()+" 00:00:00' AND ");
			statementQuerySb.append("'"+Utils.getCurrentDbDateStr().trim()+" 23:59:59' ORDER BY ast.transaction_id DESC ");
			
			statementQuerySb.append("LIMIT 5 ");			
			
			amsLogger.writeInfoLog("Statement Query::"+statementQuerySb.toString());
			
			List<AccountStatement> accountTranList = jdbcTemplate.query(statementQuerySb.toString(), new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class), new Object[]{});
			return accountTranList;
			*/
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;	
	}
	
	@Override
	public List<AccountStatement> getTotalTransModeAndTransactionAmount(AccountStatement accountStatement) 
	{
		try 
		{
			StringBuilder selectQuery = new StringBuilder("SELECT ast.txn_mode AS strTranMode, SUM(ast.transaction_amount) AS strTotalTransactionAmount, COUNT(ast.txn_mode) AS totalTransModeCount ");
			selectQuery.append("FROM account_statement AS ast  WHERE ast.account_number = '"+accountStatement.getStrAccountNumber()+"' ");
			
			selectQuery.append("AND ast.transaction_date BETWEEN '"+accountStatement.getFromDate().trim()+" 00:00:00' AND ");
			selectQuery.append("'"+accountStatement.getToDate().trim()+" 23:59:59' GROUP BY ast.txn_mode ");
			
			amsLogger.writeInfoLog("getTotalTransModeAndTransactionAmount Query::"+selectQuery.toString());
			
			List<AccountStatement> accountTranList = jdbcTemplate.query(selectQuery.toString(), new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class), new Object[] {});
			return accountTranList;	
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	private int getOffsetValue(String pageSize, String pageNumber) 
	{
		int offset = 0;
		try 
		{
			int recordPerSize = Integer.parseInt(pageSize);
			int pageNo = Integer.parseInt(pageNumber);
			
			offset = (pageNo - 1) * recordPerSize;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured in getOffsetValue::"+ExceptionUtils.getStackTrace(e));
		}
		return offset;
	}

	private String getAccountStatementQuery(AccountStatement accountStatement, int offset) 
	{
		StringBuilder statementQuery = new StringBuilder("SELECT ast.transaction_id AS strTransactionID, ");
		try 
		{
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				statementQuery.append("summary.montraTxnId AS montraTxnId, ");
			}
			statementQuery.append("ast.account_type AS strAccountType, ast.closing_balance AS strClosingBalance, ");
			statementQuery.append("ast.transaction_amount AS transactionAmount, ast.txn_type AS strTransactionType,	");
			statementQuery.append("transaction_date AS transactionDate, DATE(transaction_date) AS strTransactionDate, ");
			statementQuery.append("TIME(transaction_date) AS strTransactionTime, ast.txn_mode AS strTranMode, ast.is_gl_type AS strIsGLType,");
			statementQuery.append("ast.entity_info AS entityInfo, ast.entity_number AS entityNumber ");
			statementQuery.append("FROM account_statement AS ast ");
			
			statementQuery.append("INNER JOIN ");
			
			statementQuery.append("(SELECT astmt.transaction_id AS tranid, atm.source_txn_id AS montraTxnId ");
			statementQuery.append("FROM account_statement AS astmt LEFT JOIN account_tran_master AS atm ON astmt.transaction_id = atm.txn_id ");
			statementQuery.append("WHERE astmt.account_number = '"+accountStatement.getStrAccountNumber().trim()+"' ");			
			statementQuery.append("AND transaction_date BETWEEN '"+accountStatement.getFromDate().trim()+" 00:00:00' ");
			statementQuery.append("AND '"+accountStatement.getToDate().trim()+" 23:59:59' ");			
			statementQuery.append("GROUP BY astmt.transaction_id, atm.source_txn_id ORDER BY astmt.transaction_id DESC ");
			
			if(accountStatement.getPageSize()!=null && accountStatement.getPageSize().trim().length() > 0) 
			{
				statementQuery.append("LIMIT "+accountStatement.getPageSize()+" ");
				statementQuery.append("OFFSET "+offset+" ");
			}
			
			statementQuery.append(") AS summary ");
			
			statementQuery.append("ON summary.tranid = ast.transaction_id ");
			statementQuery.append("WHERE ast.account_number = '"+accountStatement.getStrAccountNumber().trim()+"' ");
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		amsLogger.writeInfoLog("getAccountStatementQuery Query::"+statementQuery.toString());
		return statementQuery.toString();
	}
	
	private String getAgencyAccountStatementQuery(AccountStatement accountStatement, int offset) 
	{
		StringBuilder statementQuery = new StringBuilder("SELECT ast.transaction_id AS strTransactionID, ");
		try 
		{
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				statementQuery.append("summary.montraTxnId AS montraTxnId, ");
			}
			statementQuery.append("ast.account_type AS strAccountType, ast.closing_balance AS strClosingBalance, ");
			statementQuery.append("ast.transaction_amount AS transactionAmount, ast.txn_type AS strTransactionType,	");
			statementQuery.append("transaction_date AS transactionDate, DATE(transaction_date) AS strTransactionDate, ");
			statementQuery.append("TIME(transaction_date) AS strTransactionTime, ast.txn_mode AS strTranMode, ast.is_gl_type AS strIsGLType,");
			statementQuery.append("ast.entity_info AS entityInfo, ast.entity_number AS entityNumber ");
			statementQuery.append("FROM account_statement AS ast ");
			
			statementQuery.append("INNER JOIN ");
			
			statementQuery.append("(SELECT astmt.transaction_id AS tranid, atm.source_txn_id AS montraTxnId ");
			statementQuery.append("FROM account_statement AS astmt LEFT JOIN account_tran_master AS atm ON astmt.transaction_id = atm.txn_id ");
			statementQuery.append("WHERE astmt.account_number = '"+accountStatement.getStrAccountNumber().trim()+"' ");
			
			statementQuery.append("AND astmt.txn_type LIKE 'AB%'");
			
			statementQuery.append("AND transaction_date BETWEEN '"+accountStatement.getFromDate().trim()+" 00:00:00' ");
			statementQuery.append("AND '"+accountStatement.getToDate().trim()+" 23:59:59' ");			
			statementQuery.append("GROUP BY astmt.transaction_id, atm.source_txn_id ORDER BY astmt.transaction_id DESC ");
			
			if (accountStatement.getPageSize()!= null && accountStatement.getPageSize().trim().length() > 0) 
			{
				statementQuery.append("LIMIT "+accountStatement.getPageSize().trim()+" ");
				statementQuery.append("OFFSET "+offset+" ");
			}
			
			statementQuery.append(") AS summary ");
			
			statementQuery.append("ON summary.tranid = ast.transaction_id ");
			statementQuery.append("WHERE ast.account_number = '"+accountStatement.getStrAccountNumber().trim()+"' ");
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		amsLogger.writeInfoLog("getAccountStatementQuery Query::"+statementQuery.toString());
		return statementQuery.toString();
	}

	//Added by Sunil Y , For New SummrayStatement and TxnDetailsStatement , Started
	@Override
	public List<AccountStatement> getDistinctListOfAccountStatement(AccountStatement accountStatement) {
		List<AccountStatement> accountTranList = new ArrayList<>();
		try 
		{
			StringBuilder selectQuery = new StringBuilder("SELECT DISTINCT(ast.transaction_id) AS strTransactionID ,  ast.account_type AS strAccountType, ast.closing_balance AS strClosingBalance, ast.transaction_amount AS transactionAmount, ast.txn_type AS strTransactionType,");
			selectQuery.append("transaction_date AS transactionDate, DATE(transaction_date) AS strTransactionDate, TIME(transaction_date) AS strTransactionTime,");
			selectQuery.append("ast.txn_mode AS strTranMode, ast.is_gl_type AS strIsGLType,ast.entity_info AS entityInfo,");
			selectQuery.append("ast.entity_number AS entityNumber , atm.response_code AS strResponseCode ,atm.reserve_field2 AS strReservefield2");
			selectQuery.append(" FROM account_statement AS ast INNER JOIN account_tran_master AS atm ON ast.transaction_id=atm.txn_id ");
			selectQuery.append("WHERE ast.account_number = '"+accountStatement.getStrAccountNumber().trim()+"' " );
			selectQuery.append("AND transaction_date BETWEEN '"+accountStatement.getStrFromDate().trim()+" 00:00:00' ");
			selectQuery.append("AND '"+accountStatement.getStrToDate().trim()+" 23:59:59' ");	
			
			amsLogger.writeInfoLog("Query::"+selectQuery.toString());
			System.out.println(selectQuery.toString());
			 accountTranList = jdbcTemplate.query(selectQuery.toString(), new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class), new Object[] {});
			return accountTranList;	
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountTranList;
	}

	@Override
	public List<AccountStatement> viewAccountStatementTxnDetails(AccountStatement accountStatement) {
		List<AccountStatement> accountTranList = new ArrayList<>();
		try 
		{
			StringBuilder selectQuery = new StringBuilder("SELECT DISTINCT(ast.transaction_id) AS strTransactionID ,  ast.account_type AS strAccountType, ast.closing_balance AS strClosingBalance, ast.transaction_amount AS transactionAmount, ast.txn_type AS strTransactionType,");
			selectQuery.append("transaction_date AS transactionDate, DATE(transaction_date) AS strTransactionDate, TIME(transaction_date) AS strTransactionTime,");
			selectQuery.append("ast.txn_mode AS strTranMode, ast.is_gl_type AS strIsGLType,ast.entity_info AS entityInfo,");
			selectQuery.append("ast.entity_number AS entityNumber , atm.response_code AS strResponseCode ,atm.reserve_field2 AS strReservefield2");
			selectQuery.append(" FROM account_statement AS ast INNER JOIN account_tran_master AS atm ON ast.transaction_id=atm.txn_id ");
			selectQuery.append("WHERE ast.account_number = '"+accountStatement.getStrAccountNumber().trim()+"' " );
			selectQuery.append("AND transaction_date BETWEEN '"+accountStatement.getStrFromDate().trim()+" 00:00:00' ");
			selectQuery.append("AND '"+accountStatement.getStrToDate().trim()+" 23:59:59' ");	
			
			amsLogger.writeInfoLog("Query::"+selectQuery.toString());
			System.out.println(selectQuery.toString());
			 accountTranList = jdbcTemplate.query(selectQuery.toString(), new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class), new Object[] {});
			return accountTranList;	
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountTranList;
	}
	//Added by Sunil Y ,For New SummrayStatement and TxnDetailsStatement , End

	@Override
	public List<AccountStatement> getLastFiveAccountStatementBasedonParameter(AccountStatement accountStatement) 
	{
		try 
		{
			String statementQuery = "SELECT transaction_id AS strTransactionID,account_type AS strAccountType , account_number AS strAccountNumber ,  "
					+ "closing_balance AS strClosingBalance,transaction_amount AS strTransactionAmount, "
					+ "transaction_date AS strTransactionDate,txn_type AS strTransactionType, "  
					+ "txn_mode AS strTranMode , currency_code AS currencyCode  FROM account_statement where account_type = ? AND account_number = ?"
			        + "ORDER BY transaction_date DESC LIMIT 5 ";
			List<AccountStatement> accountTranList = jdbcTemplate.query(statementQuery,
					new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class),
			new Object[] 
			{ 
					accountStatement.getStrAccountType(), 
					accountStatement.getStrAccountNumber()
			});
			return accountTranList;	
		} catch (Exception e) 
		{
			System.out.println("getLastFiveAccountTxn::"+e);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<AccountStatement> getWalletAccountStatementBasedonParameter(AccountStatement accountStatement) {
		List<AccountStatement> accountTranList = new ArrayList<>();
		try 
		{
			StringBuilder statementQuerySb = new StringBuilder("SELECT account_number AS strAccountNumber, ");
			statementQuerySb.append("account_type AS strAccountType, transaction_id AS strTranID, transaction_date AS strTransactionDate,");
			statementQuerySb.append("naration AS strTransactionDetails, transaction_id AS strTransactionID, ");
			statementQuerySb.append("transaction_amount AS strTransactionAmount, txn_mode AS strTranMode,");
			statementQuerySb.append("closing_balance AS strClosingBalance, txn_type AS strTransactionType , currency_code AS currencyCode  ");
			statementQuerySb.append(" FROM account_statement where account_type = '").append(accountStatement.getStrAccountType()).append("' ");
			statementQuerySb.append("AND account_number = '").append(accountStatement.getStrAccountNumber()).append("' ");
			statementQuerySb.append("AND transaction_date BETWEEN ");
			statementQuerySb.append("'").append(accountStatement.getFromDate()).append(" 00:00:00' ");
			statementQuerySb.append("AND '").append(accountStatement.getToDate()).append(" 23:59:59' ");
			statementQuerySb.append("ORDER BY transaction_date DESC");
			
			List<AccountStatement> accountStatementsList = jdbcTemplate.query(statementQuerySb.toString(), new BeanPropertyRowMapper<AccountStatement>(AccountStatement.class), new Object[] {});
			return accountStatementsList;
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountTranList;
	}

}
