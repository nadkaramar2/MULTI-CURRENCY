package ams.cms.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.MultiCurrencyWalletAccountDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.MultiCurrencyWalletAccountMaster;

@Repository
public  class MultiCurrencyWalletAccountDaoImpl extends AbstractGenericDao<MultiCurrencyWalletAccountMaster> implements MultiCurrencyWalletAccountDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(MultiCurrencyWalletAccountDaoImpl.class);
	
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public int[] batchEntryOfMultiCurrecnyWalletAccountMaster(List<MultiCurrencyWalletAccountMaster> listOfCurrecnyAccountMasters) 
	{
		int[] batchResponse = null;
		try
		{
			return this.jdbcTemplate.batchUpdate("INSERT INTO multi_currency_wallet_account_master "
					+ "(account_type, account_number,currency_wallet_account_number, closing_balance, priority, "
					+ "created_date, created_by , currency_code  , base_currency_account_type , participant_id ) "
					+ "values(?,?,?,?,?,?,?,?,?,?)", new BatchPreparedStatementSetter() 
					{
						@Override
						public void setValues(PreparedStatement psmt, int i) throws SQLException 
						{
							
							psmt.setString(1, listOfCurrecnyAccountMasters.get(i).getStrAccountType());
							psmt.setString(2, listOfCurrecnyAccountMasters.get(i).getStrAccountNumber());
							psmt.setString(3, listOfCurrecnyAccountMasters.get(i).getStrCurrencyWalletAccountNumber());
							psmt.setDouble(4, listOfCurrecnyAccountMasters.get(i).getStrClosingBalance());
							psmt.setInt(5, listOfCurrecnyAccountMasters.get(i).getStrPriority());
							psmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
							psmt.setString(7, listOfCurrecnyAccountMasters.get(i).getStrCreatedBy());
							psmt.setString(8, listOfCurrecnyAccountMasters.get(i).getStrCurrencyCode());
							psmt.setString(9, listOfCurrecnyAccountMasters.get(i).getBaseCurrencyAccountType());
							psmt.setString(10, listOfCurrecnyAccountMasters.get(i).getParticipantId());
							
						}
						@Override
						public int getBatchSize() 
						{
							return listOfCurrecnyAccountMasters.size();
						}
					});
		}
		catch (Exception e) 
		{
			System.out.println("MultiCurrencyWalletAccountDaoImpl.batchEntryOfMultiCurrecnyWalletAccountMaster()"+e);
			e.printStackTrace();
		}
		return batchResponse;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public MultiCurrencyWalletAccountMaster getMultiCurrencyWalletBaseAccount(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		
		try 
	{	
			StringBuilder sql = new StringBuilder("SELECT mcwam.id AS strID , mcwam.participant_id AS strParticipantId , mcwam.account_type AS strAccountType ");
			
			sql.append( " , mcwam.account_number AS strAccountNumber ,mcwam.currency_code AS strCurrencyCode ,mcwam.currency_wallet_account_number AS ");
			sql.append( " strCurrencyWalletAccountNumber ,mcwam.closing_balance AS strClosingBalance ,mcwam.priority AS strPriority");
			sql.append( " FROM multi_currency_wallet_account_master mcwam WHERE mcwam.currency_wallet_account_number= '"+multiCurrencyWalletAccountMaster.getStrAccountNumber().trim()+"'");
			
			

			List<MultiCurrencyWalletAccountMaster> accountMasters  = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<MultiCurrencyWalletAccountMaster>(MultiCurrencyWalletAccountMaster.class), new Object[]  {});
			if (accountMasters!=null && accountMasters.size() > 0) 
			{
				return accountMasters.get(0);
			}	
		} 
	catch (Exception e) 
	{
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		
	}

	return null;
	}

	@Override
	public MultiCurrencyWalletAccountMaster getMultiCurrencyWalletAccount(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		
		try 
	{	
			StringBuilder sql = new StringBuilder("SELECT mcwam.id AS strID , mcwam.participant_id AS strParticipantId , mcwam.account_type AS strAccountType ");
			
			sql.append( " , mcwam.account_number AS strAccountNumber ,mcwam.currency_code AS strCurrencyCode ,mcwam.currency_wallet_account_number AS ");
			sql.append( " strCurrencyWalletAccountNumber ,mcwam.closing_balance AS strClosingBalance ,mcwam.priority AS strPriority ,mcwam.base_currency_account_type as baseCurrencyAccountType ");
			sql.append( " FROM multi_currency_wallet_account_master mcwam ");
			sql.append( " WHERE mcwam.account_number= '"+multiCurrencyWalletAccountMaster.getStrAccountNumber().trim()+"'");
			sql.append( " AND mcwam.base_currency_account_type= '"+multiCurrencyWalletAccountMaster.getBaseCurrencyAccountType().trim()+"'");
			sql.append( " AND mcwam.currency_code = '"+multiCurrencyWalletAccountMaster.getStrCurrencyCode().trim()+"'");
			
			

			List<MultiCurrencyWalletAccountMaster> accountMasters  = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<MultiCurrencyWalletAccountMaster>(MultiCurrencyWalletAccountMaster.class), new Object[]  {});
			if (accountMasters!=null && accountMasters.size() > 0) 
			{
				return accountMasters.get(0);
			}	
		} 
	catch (Exception e) 
	{
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		
	}

	return null;
	}

	@Override
	public void updateMultiCurrencyWalletAccountMaster(MultiCurrencyWalletAccountMaster currencyWalletAccount) 
	
	{	
		try
	{
		StringBuilder updateQueryBuilder = new StringBuilder("UPDATE multi_currency_wallet_account_master gatm ");
		updateQueryBuilder.append( "SET gatm.closing_balance = '"+currencyWalletAccount.getStrClosingBalance()+"' ");
		updateQueryBuilder.append("WHERE gatm.currency_wallet_account_number = '"+currencyWalletAccount.getStrCurrencyWalletAccountNumber()+"'");
	
		//amsLogger.writeInfoLog("In updatePreCredAndClosingBalance Query::"+updateQueryBuilder.toString());
		
		int update = this.jdbcTemplate.update(updateQueryBuilder.toString(), new Object[] {});
		//amsLogger.writeInfoLog("result of update::"+update);
		
	}
	catch (Exception e) 
	{
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
	}
	}

	@Override
	public List<MultiCurrencyWalletAccountMaster> getMultiCurrencyWalletsAccountsList(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		List<MultiCurrencyWalletAccountMaster> multiCurrencyWalletAccountMastersList = new ArrayList<>();
		try 
	{	
			StringBuilder sql = new StringBuilder("SELECT mcam.priority AS strPriority , mcam.currency_wallet_account_number AS strCurrencyWalletAccountNumber  ");
			
			sql.append( " , mcam.closing_balance AS strClosingBalance, mcam.currency_code AS strCurrencyCode ");
			sql.append( " FROM multi_currency_wallet_account_master mcam ");
			sql.append( " WHERE mcam.account_number= '"+multiCurrencyWalletAccountMaster.getStrAccountNumber().trim()+"'");
			sql.append( " AND mcam.base_currency_account_type= '"+multiCurrencyWalletAccountMaster.getStrAccountType().trim()+"'");
			
			

			multiCurrencyWalletAccountMastersList  = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<MultiCurrencyWalletAccountMaster>(MultiCurrencyWalletAccountMaster.class), new Object[]  {});
			if (multiCurrencyWalletAccountMastersList!=null && multiCurrencyWalletAccountMastersList.size() > 0) 
			{
				return multiCurrencyWalletAccountMastersList;
			}	
		} 
	catch (Exception e) 
	{
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		
	}

	return multiCurrencyWalletAccountMastersList;
	}

	@Override
	public MultiCurrencyWalletAccountMaster getCurrencyAccount(
			MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT mcam.account_type AS strAccountType, mcam.account_number AS strAccountNumber, mcam.currency_code AS strCurrencyCode, ");
			queryBuilder.append("mcam.currency_wallet_account_number AS strCurrencyWalletAccountNumber, mcam.closing_balance AS strClosingBalance, mcam.priority AS strPriority, ");
			queryBuilder.append("atlrs.gl_account_type AS linkedGlType, atlrs.gl_account_number AS linkedGlNumber ");
			queryBuilder.append("FROM multi_currency_wallet_account_master mcam INNER JOIN multi_currency_wallet_account_type_master AS atlrs ON mcam.currency_code = atlrs.currency_code WHERE "); 
			queryBuilder.append("mcam.currency_code = ? AND mcam.account_number = ? ");
			
			List<MultiCurrencyWalletAccountMaster> multiCurrencyWalletAccountMasterList  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<MultiCurrencyWalletAccountMaster>(MultiCurrencyWalletAccountMaster.class), new Object[]  
			{ 
				multiCurrencyWalletAccountMaster.getStrCurrencyCode(),
				multiCurrencyWalletAccountMaster.getStrAccountNumber()
			});
			if (multiCurrencyWalletAccountMasterList!=null && multiCurrencyWalletAccountMasterList.size() > 0) 
			{
				return multiCurrencyWalletAccountMasterList.get(0);
			}	
			
		} 
		catch (Exception e) {
			e.getMessage();
		}
		return null;
	}

	@Override
	public int updateEarMark(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE multi_currency_wallet_account_master "
					+ "SET "
					+ "ear_mark = ? "
					+ "WHERE account_number = ? AND currency_code = ?",
					new Object[] 
					{ 	
							multiCurrencyWalletAccountMaster.getStrClosingBalance(),
							multiCurrencyWalletAccountMaster.getStrAccountNumber(),
							multiCurrencyWalletAccountMaster.getStrCurrencyCode()
					});
			return count;
		}
		catch (Exception e) 
		{
			e.getMessage();
		}
		return 0;
	}

	@Override
	public int updateClosingBalance(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE multi_currency_wallet_account_master "
					+ "SET "
					+ "closing_balance = ? "
					+ "WHERE account_number = ? AND currency_code = ?",
					new Object[] 
					{ 	
							multiCurrencyWalletAccountMaster.getStrClosingBalance(),
							multiCurrencyWalletAccountMaster.getStrAccountNumber(),
							multiCurrencyWalletAccountMaster.getStrCurrencyCode()
					});
			return count;
		}
		catch (Exception e) 
		{
			e.getMessage();
		}
		return 0;
	}

	@Override
	public MultiCurrencyWalletAccountMaster getFromToMultiCurrencyWalletAccount(
			MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT mcam.account_type AS strAccountType, mcam.account_number AS strAccountNumber, mcam.currency_code AS strCurrencyCode, ");
			queryBuilder.append("mcam.currency_wallet_account_number AS strCurrencyWalletAccountNumber, mcam.closing_balance AS strClosingBalance, mcam.priority AS strPriority ");
			queryBuilder.append("FROM multi_currency_wallet_account_master mcam WHERE "); 
			queryBuilder.append("mcam.multi_currency_wallet_account_master = ? ");
			
			List<MultiCurrencyWalletAccountMaster> multiCurrencyWalletAccountMasterList  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<MultiCurrencyWalletAccountMaster>(MultiCurrencyWalletAccountMaster.class), new Object[]  
			{ 
				
				multiCurrencyWalletAccountMaster.getStrAccountNumber()
			});
			if (multiCurrencyWalletAccountMasterList!=null && multiCurrencyWalletAccountMasterList.size() > 0) 
			{
				return multiCurrencyWalletAccountMasterList.get(0);
			}	
			
		} 
		catch (Exception e) {
			e.getMessage();
		}
		return null;
	}

	
	@Override
	public List<MultiCurrencyWalletAccountMaster> getMultiCurrencyAccount(
			MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT distinct mcam.account_number AS strAccountNumber, mcam.id AS strID, mcam.account_type AS strAccountType,mcam.closing_balance AS strClosingBalance, ");
			queryBuilder.append("mcam.currency_code AS strCurrencyCode, mcam.currency_wallet_account_number AS strCurrencyWalletAccountNumber ");
			queryBuilder.append(", atlrs.priority AS strPriority, ");
			queryBuilder.append("atlrs.gl_account_type AS linkedGlType, atlrs.gl_account_number AS linkedGlNumber ");
			queryBuilder.append("FROM multi_currency_wallet_account_master AS mcam ");
			queryBuilder.append("INNER JOIN multi_currency_wallet_account_type_master AS atlrs ON mcam.currency_code = atlrs.currency_code AND ");
			queryBuilder.append("mcam.account_type = atlrs.account_type ");
			queryBuilder.append("WHERE mcam.account_number = ? ");
			
			List<MultiCurrencyWalletAccountMaster> multiCurrencyWalletAccountMasterList  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<MultiCurrencyWalletAccountMaster>(MultiCurrencyWalletAccountMaster.class), new Object[]  
			{ 
					multiCurrencyWalletAccountMaster.getStrAccountNumber()
			});
			if (multiCurrencyWalletAccountMasterList!=null && multiCurrencyWalletAccountMasterList.size() > 0) 
			{
				return multiCurrencyWalletAccountMasterList;
			}	
			
		} 
		catch (Exception e) {
			e.getMessage();
		}
		return null;
	}

	@Override
	public List<MultiCurrencyWalletAccountMaster> getCurrenyWalletListForStatemetView(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		
		List<MultiCurrencyWalletAccountMaster> accountMasters  = new ArrayList<>();
		
		try 
	{	
			StringBuilder sql = new StringBuilder("SELECT  mcwam.account_type AS strAccountType ");
			sql.append( "  ,mcwam.currency_wallet_account_number AS ");
			sql.append( " strCurrencyWalletAccountNumber ");
			sql.append( " FROM multi_currency_wallet_account_master mcwam WHERE mcwam.account_number= '"+multiCurrencyWalletAccountMaster.getStrAccountNumber().trim()+"' AND   mcwam.base_currency_account_type= '"+multiCurrencyWalletAccountMaster.getStrAccountType().trim()+"' ");

			 accountMasters  = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<MultiCurrencyWalletAccountMaster>(MultiCurrencyWalletAccountMaster.class), new Object[]  {});
			if (accountMasters!=null && accountMasters.size() > 0) 
			{
				return accountMasters;
			}	
		} 
	catch (Exception e) 
	{
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		
	}

	return accountMasters;
	}

	@Override
	public void updateMultiCurrencyWalletAccountMasterWithPriority(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMasterList) {	
		try
	{
		StringBuilder updateQueryBuilder = new StringBuilder("UPDATE multi_currency_wallet_account_master gatm ");
		updateQueryBuilder.append( "SET gatm.priority = '"+multiCurrencyWalletAccountMasterList.getStrPriority()+"' ");
		updateQueryBuilder.append("WHERE gatm.currency_wallet_account_number = '"+multiCurrencyWalletAccountMasterList.getStrCurrencyWalletAccountNumber()+"'");
	
		
		int update = this.jdbcTemplate.update(updateQueryBuilder.toString(), new Object[] {});
		
	}
	catch (Exception e) 
	{
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
	}
	}

	@Override
	public List<MultiCurrencyWalletAccountMaster> getCurrenyWalletListForPriority(MultiCurrencyWalletAccountMaster multiCurrencyWalletAccountMaster) {
		
		List<MultiCurrencyWalletAccountMaster> accountMasters  = new ArrayList<>();
		
		try 
	{	
			StringBuilder sql = new StringBuilder("SELECT  mcwam.account_type AS strAccountType , mcwam.priority AS strPriority  ");
			sql.append( " , mcwam.currency_wallet_account_number AS ");
			sql.append( " strCurrencyWalletAccountNumber ");
			sql.append( " FROM multi_currency_wallet_account_master mcwam WHERE mcwam.account_number= '"+multiCurrencyWalletAccountMaster.getStrAccountNumber().trim()+"' AND   mcwam.base_currency_account_type= '"+multiCurrencyWalletAccountMaster.getStrAccountType().trim()+"' ");

			 accountMasters  = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<MultiCurrencyWalletAccountMaster>(MultiCurrencyWalletAccountMaster.class), new Object[]  {});
			if (accountMasters!=null && accountMasters.size() > 0) 
			{
				return accountMasters;
			}	
		} 
	catch (Exception e) 
	{
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		
	}

	return accountMasters;
	}
}
