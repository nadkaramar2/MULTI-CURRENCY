package ams.cms.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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

import ams.cms.dao.GLAccountTypeMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.GLAccountTypeMaster;

@Repository
public class GLAccountTypeMasterDaoImpl extends AbstractGenericDao<GLAccountTypeMaster> implements GLAccountTypeMasterDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(GLAccountTypeMaster.class);
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GLAccountTypeMaster> getGLAccountTypesList(GLAccountTypeMaster glAccountTypeMaster) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();
			
			if (glAccountTypeMaster.getStrGLAccountType()!=null && glAccountTypeMaster.getStrGLAccountType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strGLAccountType", glAccountTypeMaster.getStrGLAccountType()));
			}
			
			List<GLAccountTypeMaster> glAccountTypeMasterslist = (List<GLAccountTypeMaster>) criteria.list();
			return glAccountTypeMasterslist;
		}
		catch (Exception e) 
		{
			System.out.println("Exception in getGLAccountTypesList::"+e);
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public String getClosingBalanceOfGlAccount(GLAccountTypeMaster glAccountTypeMaster)
	{
		try {	
			String sql = "SELECT closing_balance as strClosingBalance "
					+ "FROM gl_account_type_master "
					+ "where account_type = ? "
					+ "AND account_number = ?";

			String closingBalance = this.jdbcTemplate.queryForObject(sql, String.class,
					new Object[]
					{
							glAccountTypeMaster.getStrGLAccountType(),
							glAccountTypeMaster.getStrAccountNumber()
					}
			);
			
			if (closingBalance == null) 
			{
				closingBalance = "0";
			}			
			return closingBalance;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int updateGLAccountTypeDetails(GLAccountTypeMaster glAccountTypeMaster) 
	{
		try 
		{
			String updateClosingBalance = "UPDATE gl_account_type_master SET closing_balance = '"+glAccountTypeMaster.getStrClosingBalance()+"' where account_type = '"+glAccountTypeMaster.getStrGLAccountType()+"' AND account_number = '"+glAccountTypeMaster.getStrAccountNumber()+"'";
			System.out.println("updateGLAccountTypeDetails:: updateClosingBalance=["+updateClosingBalance+"]");
			
			int chedckUpdate = this.jdbcTemplate.update(updateClosingBalance,new Object[] {});
			return chedckUpdate;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GLAccountTypeMaster> GLAccountcreationView( GLAccountTypeMaster glAccountviewModel) {
		try 
		{
		
			Criteria criteria = createEntityCriteria();
			if (glAccountviewModel.getStrID()!=null && glAccountviewModel.getStrID().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strID", glAccountviewModel.getStrID()));
			}
			if (glAccountviewModel.getStrGLAccountType()!=null && glAccountviewModel.getStrGLAccountType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountType", glAccountviewModel.getStrGLAccountType()));
			}
			
			
			List<GLAccountTypeMaster> listData = (List<GLAccountTypeMaster>) criteria.list();
			return listData;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public GLAccountTypeMaster getSingleGLAccountTypeMasterObj(GLAccountTypeMaster glAccountTypeMaster) 
	{
		try 
		{
			boolean isCriteriaExecute = false;
			Criteria criteria = createEntityCriteria();
			if (glAccountTypeMaster.getStrGLAccountType()!=null && glAccountTypeMaster.getStrGLAccountType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strGLAccountType", glAccountTypeMaster.getStrGLAccountType().trim()));
				isCriteriaExecute = true;
			}
			if (glAccountTypeMaster.getStrAccountNumber()!=null && glAccountTypeMaster.getStrAccountNumber().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountNumber", glAccountTypeMaster.getStrAccountNumber().trim()));
				isCriteriaExecute = true;
			}
			if (isCriteriaExecute) 
			{
				List<GLAccountTypeMaster> glAccountTypeMasters = (List<GLAccountTypeMaster>) criteria.list();				
				if (glAccountTypeMasters != null && glAccountTypeMasters.size() > 0) 
				{
					return glAccountTypeMasters.get(0);
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public GLAccountTypeMaster getMappedGLAccountTypeMasterObjWithAccountType(GLAccountTypeMaster glAccountTypeMaster) 
	{
		try 
		{
			/*
			StringBuilder selectQuerySb = new StringBuilder("SELECT glatm.id AS strID, atm.gl_account_type AS strGLAccountType, atm.gl_account_no AS strAccountNumber,");
			selectQuerySb.append("glatm.account_description AS strGLAccountDescription, COALESCE(glatm.closing_balance, 0 ) AS strClosingBalance ");
			selectQuerySb.append("FROM account_type_master AS atm ");
			selectQuerySb.append("INNER JOIN gl_account_type_master AS glatm ");
			selectQuerySb.append("ON glatm.account_type = atm.gl_account_type ");
			selectQuerySb.append("WHERE atm.account_type = '"+glAccountTypeMaster.getUserAccountType()+"'");
			*/
			
			String glAccountype = (glAccountTypeMaster.getUserAccountType()!=null && glAccountTypeMaster.getUserAccountType().trim().length()>0) ? glAccountTypeMaster.getUserAccountType().trim():"";
			System.out.println("glAccountype::["+glAccountype+"]");
			
			StringBuilder selectQuerySb = new StringBuilder("SELECT glatm.id AS strID, glatm.account_type AS strGLAccountType, glatm.account_number AS strAccountNumber,");
			selectQuerySb.append("glatm.account_description AS strGLAccountDescription, COALESCE(glatm.closing_balance, 0 ) AS strClosingBalance ");
			selectQuerySb.append("FROM gl_account_type_master glatm WHERE glatm.account_type ='"+glAccountype+"' ");
			
			System.out.println("getMappedGLAccountTypeMasterObjWithAccountType:: selectQuerySb::"+selectQuerySb.toString());			
			List<GLAccountTypeMaster> glAccountTypeMasters = this.jdbcTemplate.query(selectQuerySb.toString(), (RowMapper<GLAccountTypeMaster>) new BeanPropertyRowMapper<GLAccountTypeMaster>(GLAccountTypeMaster.class), 
					new Object[] {}
			);
			
			if (glAccountTypeMasters!=null && glAccountTypeMasters.size() > 0) 
			{
				return glAccountTypeMasters.get(0);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// Added by pankaj pawar for charts of accounts menu [Start]
	@Override
	public List<GLAccountTypeMaster> getGLAccountList(GLAccountTypeMaster glAccountTypeMaster) 
	{
		try
		{
			StringBuilder statementQuery = new StringBuilder("SELECT IFNULL(atm.account_type, '-' ) AS strAccountType , glatm.account_type AS strGLAccountType,");
			statementQuery.append("glatm.account_description AS strGLAccountDescription, glatm.account_number AS strAccountNumber,");
			statementQuery.append("COALESCE(glatm.closing_balance, 0 ) AS strClosingBalance ");
			statementQuery.append("FROM  gl_account_type_master  AS glatm ");
			statementQuery.append("LEFT JOIN account_type_master AS atm ");
			statementQuery.append("ON glatm.account_type = atm.gl_account_type ORDER BY glatm.creation_date DESC");
			
			
			List<GLAccountTypeMaster> gLAccountTypeMaster = jdbcTemplate.query(statementQuery.toString(), new BeanPropertyRowMapper<GLAccountTypeMaster>(GLAccountTypeMaster.class), new Object[] {});
			return gLAccountTypeMaster;
		}
		catch (Exception e)
		{
			System.out.println("getGLAccountList::"+e);
			e.printStackTrace();
		}
		return null;
	}
	// Added by pankaj pawar for charts of accounts menu [End]
	
	//created by ankit -start-
	@Override
	public List<GLAccountTypeMaster> getGLAccountTypeListThirdPartyAllow() 
	{
		try 
		{	
			String sql = "SELECT account_type as strGLAccountType,account_description as strGLAccountDescription FROM gl_account_type_master where third_party_allow = 'Y'";
			List<GLAccountTypeMaster> query = this.jdbcTemplate.query(sql, (RowMapper<GLAccountTypeMaster>) new BeanPropertyRowMapper<GLAccountTypeMaster>(GLAccountTypeMaster.class), new Object[] {});
			return query;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<GLAccountTypeMaster> getDataOfGLAccount(GLAccountTypeMaster glAccountTypeMaster)
	{
		try {	
			String sql = "SELECT account_description as strGLAccountDescription ,closing_balance as strClosingBalance,status as strStatus "
					+ "FROM gl_account_type_master where account_type = ? AND account_number = ?";

			List<GLAccountTypeMaster> query = this.jdbcTemplate.query(sql, 
						(RowMapper<GLAccountTypeMaster>) new BeanPropertyRowMapper<GLAccountTypeMaster>(GLAccountTypeMaster.class),
					new Object[]
					{
							glAccountTypeMaster.getStrGLAccountType(),
							glAccountTypeMaster.getStrAccountNumber()
					}
			);
			
			return query;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<GLAccountTypeMaster> getDataOfGLAccountTypeMaster(GLAccountTypeMaster glAccountTypeMaster)
	{
		try {	
			String sql = "SELECT account_type as strGLAccountType ,closing_balance as strClosingBalance,account_number as strAccountNumber "
					+ "FROM gl_account_type_master where account_type = ? AND account_number = ?";

			List<GLAccountTypeMaster> query = this.jdbcTemplate.query(sql, 
						(RowMapper<GLAccountTypeMaster>) new BeanPropertyRowMapper<GLAccountTypeMaster>(GLAccountTypeMaster.class),
					new Object[]
					{
							glAccountTypeMaster.getStrGLAccountType(),
							glAccountTypeMaster.getStrAccountNumber()
					}
			);
			
			
			return query;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	public List<GLAccountTypeMaster> getGLAccountDescription(GLAccountTypeMaster glAccountTypeMaster)
	{
		try {	
			String sql = "SELECT account_description as strGLAccountDescription, account_number as strAccountNumber, "
					+ " closing_balance as strClosingBalance FROM gl_account_type_master"
					+ " WHERE account_type = ?";

			List<GLAccountTypeMaster> query = this.jdbcTemplate.query(sql, 
						(RowMapper<GLAccountTypeMaster>) new BeanPropertyRowMapper<GLAccountTypeMaster>(GLAccountTypeMaster.class),
					new Object[]
					{
							glAccountTypeMaster.getStrGLAccountType()
					}
			);
			
			return query;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//created by ankit -end-

	@Override
	public List<GLAccountTypeMaster> getGLAccountTypeListExceptCTR() {
		try 
		{	
			String sql = "SELECT account_type as strGLAccountType,account_description as strGLAccountDescription FROM gl_account_type_master where NOT account_type = 'CTR' and third_party_allow = 'Y'";
			List<GLAccountTypeMaster> query = this.jdbcTemplate.query(sql, (RowMapper<GLAccountTypeMaster>) new BeanPropertyRowMapper<GLAccountTypeMaster>(GLAccountTypeMaster.class), new Object[] {});
			return query;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	//created by ankit on 04-05-2023
	@Override
	public List<GLAccountTypeMaster> getGLClosingBalanceAndDescription(GLAccountTypeMaster glAccountTypeMaster)
	{
		try {	
			String sql = "SELECT account_description as strGLAccountDescription,"
					+ " closing_balance as strClosingBalance"
					+ " FROM gl_account_type_master"
					+ " WHERE account_type = ?";

			List<GLAccountTypeMaster> query = this.jdbcTemplate.query(sql, 
						(RowMapper<GLAccountTypeMaster>) new BeanPropertyRowMapper<GLAccountTypeMaster>(GLAccountTypeMaster.class),
					new Object[]
					{
							glAccountTypeMaster.getStrGLAccountType()
					}
			);
			
			return query;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//created by ankit on 04-05-2023
	
	@Override
	public GLAccountTypeMaster getGLAccountNumberOnAccountType(GLAccountTypeMaster glAccountTypeMaster) 
	{
		try 
		{
			StringBuilder selectQuerySb = new StringBuilder("SELECT account_number AS strAccountNumber, account_type AS strGLAccountType , closing_balance AS strClosingBalance ");
			selectQuerySb.append("FROM gl_account_type_master WHERE account_type = ?");
			
			List<GLAccountTypeMaster> glAccountTypeMaster1 = this.jdbcTemplate.query(selectQuerySb.toString(), (RowMapper<GLAccountTypeMaster>) new BeanPropertyRowMapper<GLAccountTypeMaster>(
					GLAccountTypeMaster.class), new Object[] { glAccountTypeMaster.getStrGLAccountType() });
			
			System.out.println("AccountTypeChargesDaoImpl.getAccountTypeBasedOnChargeType()"+glAccountTypeMaster1);
			
			 if(glAccountTypeMaster1!=null && glAccountTypeMaster1.size() > 0)
		     {
				 glAccountTypeMaster = glAccountTypeMaster1.get(0);
				 return glAccountTypeMaster;
		     }
		} 	
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//added by sunil Y ,
	@Override
	public GLAccountTypeMaster getGLAccountNumberObjectOnAccountType(GLAccountTypeMaster glAccountTypeMaster) {
		try {	
			String sql = "SELECT account_number AS strAccountNumber , account_type AS strGLAccountType , closing_balance AS strClosingBalance "
					+ " closing_balance as strClosingBalance"
					+ " FROM gl_account_type_master"
					+ " WHERE account_type = ?";

			List<GLAccountTypeMaster> query = this.jdbcTemplate.query(sql, 
						(RowMapper<GLAccountTypeMaster>) new BeanPropertyRowMapper<GLAccountTypeMaster>(GLAccountTypeMaster.class),
					new Object[]
					{
							glAccountTypeMaster.getStrGLAccountType()
					}
			);
			
			return query.get(0);
		} catch (Exception e) {
			System.out.println("getGLAccountList::"+e);
			e.printStackTrace();}
		return null;
	}

	@Override
	public int[] updatesBatchEntryOfLinkedGLAccount(List<GLAccountTypeMaster> glAccountTypeMasters) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE gl_account_type_master glatm SET glatm.closing_balance = ? ");
			updateQueryBuilder.append("WHERE glatm.account_number = ? AND glatm.account_type = ?");
			
			return this.jdbcTemplate.batchUpdate(updateQueryBuilder.toString(), new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, glAccountTypeMasters.get(i).getStrClosingBalance());
					psmt.setString(2, glAccountTypeMasters.get(i).getStrAccountNumber());
					psmt.setString(3, glAccountTypeMasters.get(i).getStrGLAccountType());
				}
				
				@Override
				public int getBatchSize() 
				{
					return glAccountTypeMasters.size();
				}
			});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}
}
