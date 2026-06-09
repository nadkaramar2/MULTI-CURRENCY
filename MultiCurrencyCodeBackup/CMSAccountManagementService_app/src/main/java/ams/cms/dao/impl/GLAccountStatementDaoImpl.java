package ams.cms.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.api.model.AccountStatementHeader;
import ams.cms.dao.GLAccountStatementDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.utility.Utils;

@Repository
public class GLAccountStatementDaoImpl extends AbstractGenericDao<GLAccountStatement> implements GLAccountStatementDao 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(GLAccountStatementDaoImpl.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<GLAccountStatement> getGLAccountStatement(GLAccountStatement glAccountStatement) 
	{
		try 
		{
			StringBuilder statementSelectQuerySb = new StringBuilder("SELECT accstmt.account_number AS strAccountNumber, accstmt.account_type AS strGLAccountType, ");
			statementSelectQuerySb.append("accstmt.tran_id AS strTxnId, accstmt.transaction_date AS strTransactionDate, ");
			statementSelectQuerySb.append("DATE(accstmt.transaction_date) AS strTxnDate, TIME(accstmt.transaction_date) AS strTxnTime,");
			statementSelectQuerySb.append("accstmt.amount AS strAmount, ");
			statementSelectQuerySb.append("accstmt.Ref AS strRef, accstmt.tran_type AS strTranType, accstmt.tran_mode AS strTranMode  ");
			statementSelectQuerySb.append("from gl_account_statement accstmt LEFT JOIN gl_account_type_master acmst  ");
			statementSelectQuerySb.append("ON accstmt.account_number = acmst.account_number ");
			statementSelectQuerySb.append("where accstmt.account_type = '"+glAccountStatement.getStrGLAccountType()+"' and accstmt.account_number = '"+glAccountStatement.getStrAccountNumber()+"'  ");
			statementSelectQuerySb.append("and accstmt.transaction_date BETWEEN '"+glAccountStatement.getFromDate()+" 00:00:00' AND '"+glAccountStatement.getToDate()+" 23:59:59'  ");
			statementSelectQuerySb.append("ORDER BY accstmt.transaction_date ASC");
			
			System.out.println("statementSelectQuerySb to String::"+statementSelectQuerySb.toString());
			
			List<GLAccountStatement> getAccountStatement = jdbcTemplate.query(statementSelectQuerySb.toString(), new BeanPropertyRowMapper<GLAccountStatement>(GLAccountStatement.class), new Object[] {});

			System.out.println("getAccountStatements:::" + getAccountStatement.size());
			return getAccountStatement;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	//added by sunil Y , new api creation for header response in gl account statement and account statement , started
	@Override
	public List<AccountStatementHeader> getAccountStatementAndGlDetails(String strAccountNumber) 
	{
		try 
		{
			StringBuilder statementSelectQuerySb = new StringBuilder("SELECT am.email AS strEmail, am.account_type AS strAccountType , am.closing_balance AS strClosingBalance ,");
			statementSelectQuerySb.append("atm.description AS strDescription, am.account_number As strAccountNumber ,");
			statementSelectQuerySb.append("CONCAT_WS(' ', am.address1, am.address2, am.address3) AS strAccountHolderAdress ,");
			statementSelectQuerySb.append("CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName ");
			statementSelectQuerySb.append("FROM account_master am INNER JOIN account_type_master atm ON atm.account_type=am.account_type ");
			statementSelectQuerySb.append("WHERE am.account_number='"+strAccountNumber+"'");
			System.out.println("statementSelectQuerySb to String::"+statementSelectQuerySb.toString());
			
			
			List<AccountStatementHeader> getAccountStatement = jdbcTemplate.query(statementSelectQuerySb.toString(), new BeanPropertyRowMapper<AccountStatementHeader>(AccountStatementHeader.class), new Object[] {});
			
			return getAccountStatement;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;

			
	} 

	//added by sunil Y , new api creation for header response in gl account statement and account statement , started
	@Override
	public List<GLAccountTypeMaster> getGlDetailsHeader(String id) 
	{
		try 
		{
			StringBuilder statementSelectQuerySb = new StringBuilder("SELECT  glatm.account_type AS strAccountType , glatm.closing_balance AS strClosingBalance ,");
			statementSelectQuerySb.append("glatm.account_description AS strGLAccountDescription, glatm.account_number As strAccountNumber ");
			statementSelectQuerySb.append("  FROM gl_account_type_master glatm WHERE glatm.account_number='"+id+"'");
			System.out.println("statementSelectQuerySb to String::"+statementSelectQuerySb.toString());
			System.out.println(statementSelectQuerySb);			
			
			List<GLAccountTypeMaster> getAccountStatement = jdbcTemplate.query(statementSelectQuerySb.toString(), new BeanPropertyRowMapper<GLAccountTypeMaster>(GLAccountTypeMaster.class), new Object[] {});
			
			return getAccountStatement;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public int[] batchEntryOfGLAccountStatement(List<GLAccountStatement> glAccountStatements) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder batchInsertQuerySb = new StringBuilder("INSERT INTO gl_account_statement");
			batchInsertQuerySb.append("(account_type, account_number, Ref, tran_id, amount, closing_balance,");
			batchInsertQuerySb.append("transaction_date, tran_type, tran_mode, created_date) ");
			batchInsertQuerySb.append("values(?,?,?,?,?,?,?,?,?,?)");
			
			return this.jdbcTemplate.batchUpdate(batchInsertQuerySb.toString(), new BatchPreparedStatementSetter() 
					{
						@Override
						public void setValues(PreparedStatement psmt, int i) throws SQLException 
						{
							psmt.setString(1, glAccountStatements.get(i).getStrGLAccountType());
							psmt.setString(2, glAccountStatements.get(i).getStrAccountNumber());
							psmt.setString(3, glAccountStatements.get(i).getStrRef());
							psmt.setString(4, glAccountStatements.get(i).getStrTxnId());
							psmt.setString(5, glAccountStatements.get(i).getStrAmount());
							psmt.setString(6, glAccountStatements.get(i).getStrClosingBalance());
							//psmt.setDate(7, Utils.getCurrentSqlDate());
							psmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
							psmt.setString(8, glAccountStatements.get(i).getStrTranType());
							psmt.setString(9, glAccountStatements.get(i).getStrTranMode());
							psmt.setDate(10, Utils.getCurrentSqlDate());
						}
						
						@Override
						public int getBatchSize() {
							return glAccountStatements.size();
						}
					});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	} 
	
		
}
