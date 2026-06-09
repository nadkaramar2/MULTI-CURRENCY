package ams.cms.dao.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.dao.GLAccountCreationDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.GLAccountCreation;

@Repository
public class GLAccountCreationDaoImpl extends AbstractGenericDao<GLAccountCreation> implements GLAccountCreationDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(GLAccountCreationDaoImpl.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public boolean isGLAccountTypeAlreadyExist(GLAccountCreation glAccountCreation)
	{
		try
		{
			String sql = "SELECT COUNT(*) AS cnt FROM gl_account_type_master WHERE account_type = ?";
			int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { glAccountCreation.getStrGLAccountType() });
			return count > 0;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isGLAccountAccountNumberAlreadyExist(GLAccountCreation glAccountCreation) {
		try
		{
			String sql = "SELECT COUNT(*) AS cnt FROM gl_account_type_master WHERE account_number = ?";
			int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { glAccountCreation.getStrGLAccountNumber() });
			return count > 0;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<GLAccountCreation> getGlAccTypeAccNumber(GLAccountCreation glAccountCreation)
	{
		try 
		{
			String sql = "SELECT account_type AS strGLAccountType , account_number AS strGLAccountNumber "
					+ "FROM gl_account_type_master WHERE third_party_allow = 'N' ";
			List<GLAccountCreation> list = this.jdbcTemplate.query(sql, (RowMapper) new BeanPropertyRowMapper(GLAccountCreation.class), new Object[] {});
			System.out.println("GLAccountCreationDaoImpl.getGlAccTypeAccNumber()"+list);
			return list;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public int updateClosingBalance(GLAccountCreation glAccountCreation) 
	{
		String sqlUpdateQuery = "UPDATE gl_account_type_master SET closing_balance = ? where account_type = ? AND account_number = ?";
		try 
		{
			int count = this.jdbcTemplate.update(sqlUpdateQuery, new Object[]
			{ 
				glAccountCreation.getStrClosingBalance(),
				glAccountCreation.getStrGLAccountType(), 
				glAccountCreation.getStrGLAccountNumber() 
			}
			);
			return count;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public GLAccountCreation isGLAccountTypeExist(GLAccountCreation glAccountCreation) 
	{
		try
		{
			String sql = "SELECT account_number as strGLAccountNumber, closing_balance as strClosingBalance, account_description as strGLDescription FROM gl_account_type_master WHERE account_type = ?";
			List<GLAccountCreation> gLAccountCreationResp= jdbcTemplate.query(sql, (RowMapper) new BeanPropertyRowMapper(GLAccountCreation.class), new Object[] { glAccountCreation.getStrGLAccountType() });
			 if(gLAccountCreationResp != null && gLAccountCreationResp.size() > 0)
			 {
					return gLAccountCreationResp.get(0);
			 }
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	return null;
	}

	@Override
	public String getAllGLTogetherBalance() 
	{
		try 
		{
			String sql = "SELECT SUM(glatm.closing_balance) AS strControlAccountBalance FROM gl_account_type_master glatm";
			String strControlAccountBalance = this.jdbcTemplate.queryForObject(sql, String.class, new Object[] {} );
			if (strControlAccountBalance!=null && strControlAccountBalance.trim().length() > 0) 
			{
				strControlAccountBalance = strControlAccountBalance.trim();
			}
			return strControlAccountBalance;
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<GLAccountCreation> getGlAccTypeAccNumberList(GLAccountCreation glAccountCreation) 
	{
			try 
			{
				String sql = "SELECT account_type AS strGLAccountType , account_number AS strGLAccountNumber "
						+ "FROM gl_account_type_master WHERE third_party_allow = 'Y' ";
				List<GLAccountCreation> list = this.jdbcTemplate.query(sql, (RowMapper) new BeanPropertyRowMapper(GLAccountCreation.class), new Object[] {});
				System.out.println("GLAccountCreationDaoImpl.getGlAccTypeAccNumberList()"+list);
				return list;
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				System.out.println("GLAccountCreationDaoImpl.getGlAccTypeAccNumberList()"+e);
			}
			return null;
		
	}
}
