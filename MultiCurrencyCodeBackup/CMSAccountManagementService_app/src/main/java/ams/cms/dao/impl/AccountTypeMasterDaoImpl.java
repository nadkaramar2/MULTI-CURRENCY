package ams.cms.dao.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.api.model.AccountMaster;
import ams.cms.dao.AccountTypeMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountTypeMaster;

@Repository
public class AccountTypeMasterDaoImpl extends AbstractGenericDao<AccountTypeMaster> implements AccountTypeMasterDao
{	
	private AMSLogger amsLogger = AMSLogger.getInstance(AccountTypeMasterDaoImpl.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unchecked")
	@Override
	public AccountTypeMaster getAccountTypeObject(AccountTypeMaster accountTypeMster)
	{
		try
		{
			boolean isCriteriaRun = false;
			
			Criteria criteria = createEntityCriteria();			
			if (accountTypeMster.getStrParticipantId()!=null && accountTypeMster.getStrParticipantId().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strParticipantId", accountTypeMster.getStrParticipantId().trim()));
				isCriteriaRun = true;
			}
			if (accountTypeMster.getStrAccountType()!=null && accountTypeMster.getStrAccountType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountType", accountTypeMster.getStrAccountType().trim()));
				isCriteriaRun = true;
			}
			
			if (isCriteriaRun)
			{
				List<AccountTypeMaster> accountTypelist = (List<AccountTypeMaster>) criteria.list();
				if (accountTypelist != null && accountTypelist.size() > 0) 
				{
					return accountTypelist.get(0);
				}
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeInfoLog("Exception in getAccountInformationListByTypes::"+e);
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public List<AccountTypeMaster> getNonCreditAccounTypeObject(AccountTypeMaster accountTypeMster) 
	{
		try 
		{
			String sql = "SELECT account_type AS strAccountType, description AS strDescription "
					+ "FROM account_type_master "
					+ "WHERE is_credit_type = 'N'";
			List<AccountTypeMaster> accountTypeMasterList = this.jdbcTemplate.query(sql,
					new Object[] {},
					(RowMapper<AccountTypeMaster>) new BeanPropertyRowMapper<AccountTypeMaster>(AccountTypeMaster.class));
			amsLogger.writeInfoLog("getNonCreditAccounTypeObject::::" + accountTypeMasterList);
			
			return accountTypeMasterList;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int updateLastAccountNumber(AccountTypeMaster accountTypeMster)
	{
		try 
		{
			int count = 0;
			count = this.jdbcTemplate.update("UPDATE account_type_master "
					+ "SET last_account_number = ? " + "WHERE participant_id = ? and account_type = ?",
					new Object[] { accountTypeMster.getStrLastAccNumber(), 
							accountTypeMster.getStrParticipantId(), 
							accountTypeMster.getStrAccountType() });
			
			amsLogger.writeInfoLog("count::[" + count + "]");
			return count;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}	       
		return 0;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public String getLastAccounNumberBasedOnAccountType(String accountType) {
		try {

			String lastAccNumber = this.jdbcTemplate.queryForObject("select last_account_number from account_type_master"
					+ " where account_type = ?",
					new Object[] { accountType }, String.class);
			amsLogger.writeInfoLog("ConfigurationDaoImpl.getAccounNumberBasedOnAccountType()" + lastAccNumber);
			amsLogger.writeInfoLog("lastAccNumber::" + lastAccNumber);
			return lastAccNumber;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountType;

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AccountTypeMaster> getAccountTypeMastersByParticipantWise(String participantId) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();
			criteria.add(Restrictions.eq("strParticipantId", participantId));
			
			List<AccountTypeMaster> accountTypelist = (List<AccountTypeMaster>) criteria.list();
			if (accountTypelist !=null && accountTypelist.size() > 0) 
			{
				return accountTypelist;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean isAccounTypeAlreadyExist(AccountTypeMaster accountTypeMster)
	{
		try
		{
			String sql = "SELECT count(*) FROM account_type_master WHERE account_type = ?";
			int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { accountTypeMster.getStrAccountType() });
			return count > 0;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	//Abhishek T-Start
	@Override
	public List<AccountTypeMaster> getYCreditAccounTypeObject(AccountTypeMaster accountTypeMaster) {
		try 
		{
			String sql = "SELECT account_type AS strAccountType, description AS strDescription FROM account_type_master WHERE is_credit_type = 'Y'";
			List<AccountTypeMaster> accountTypeMasterList = this.jdbcTemplate.query(sql, (RowMapper<AccountTypeMaster>) new BeanPropertyRowMapper<AccountTypeMaster>(AccountTypeMaster.class), new Object[] {});
			amsLogger.writeInfoLog("getYCreditAccounTypeObject::::" + accountTypeMasterList);
			
			return accountTypeMasterList;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	//Abhishek T-End
	
	@SuppressWarnings("deprecation")
	@Override
	public List<AccountTypeMaster> getCreditAccounTypeObject(AccountTypeMaster accountTypeMaster) {
		try 
		{
			String sql = "SELECT account_type AS strAccountType, description AS strDescription "
					+ "FROM account_type_master "
					+ "WHERE is_credit_type = ? ";
			List<AccountTypeMaster> accountTypeMasterList = this.jdbcTemplate.query(sql,
					new Object[] {accountTypeMaster.getStrIsCreditType()},
					(RowMapper<AccountTypeMaster>) new BeanPropertyRowMapper<AccountTypeMaster>(AccountTypeMaster.class));
			amsLogger.writeInfoLog("getCreditAccounTypeObject::::" + accountTypeMasterList);
			
			return accountTypeMasterList;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int updateIsRevolvingCreditFromMcc(AccountTypeMaster accountTypeMaster) {
		try 
		{
			int count = this.jdbcTemplate.update(
					"UPDATE account_type_master SET is_revolving_credit = 'N' "
					+ "WHERE " + "account_type = ?",
					new Object[] 
					{ 
						accountTypeMaster.getStrAccountType()
							
					});
			return count;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int updateIsRevolvingCredit(AccountTypeMaster accountTypeMaster) {
		try 
		{
			int count = this.jdbcTemplate.update(
					"UPDATE account_type_master SET is_revolving_credit = 'Y' "
					+ "WHERE " + "account_type = ?",
					new Object[] 
					{ 
						accountTypeMaster.getStrAccountType()
							
					});
			return count;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String getisRevolingCredit(String accountType) {
		try {

			String is_revolving_credit = this.jdbcTemplate.queryForObject("select is_revolving_credit from account_type_master where account_type = ?",
					new Object[] { accountType }, String.class);
			amsLogger.writeInfoLog("is_revolving_credit::" + is_revolving_credit);
			return is_revolving_credit;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//created by ankit
	@Override
	public List<AccountTypeMaster> getAccountDescription(AccountTypeMaster accountTypeMaster) {
		try {

			List<AccountTypeMaster> query = this.jdbcTemplate.query("select description as strDescription from account_type_master where account_type = ?",
					(RowMapper<AccountTypeMaster>) new BeanPropertyRowMapper<AccountTypeMaster>(AccountTypeMaster.class),
					new Object[] { accountTypeMaster.getStrAccountType() });
			
			return query;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//created by ankit
	//created by ankit on 18-04-2023
	@Override
	public List<AccountTypeMaster> getOnlyAccountStatus(AccountTypeMaster accountTypeMaster) {
		try {

			List<AccountTypeMaster> query = this.jdbcTemplate.query("select status as strStatus from account_type_master"
					+ " where account_type = ?",
					(RowMapper<AccountTypeMaster>) new BeanPropertyRowMapper<AccountTypeMaster>(AccountTypeMaster.class),
					new Object[] { accountTypeMaster.getStrAccountType() });
			
			return query;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//created by ankit on 18-04-2023

	
	//created by ankit on 19-04-2023
	@Override
	public List<AccountTypeMaster> getGLAccountNoAndType(AccountTypeMaster accountTypeMaster) {
		try {

			List<AccountTypeMaster> query = this.jdbcTemplate.query(
					"select gl_account_type as strGLAccountType,"
					+ " gl_account_no as strGLAccountNumber"
					+ " from account_type_master"
					+ " where account_type = ?",
					(RowMapper<AccountTypeMaster>) new BeanPropertyRowMapper<AccountTypeMaster>(AccountTypeMaster.class),
					new Object[] { accountTypeMaster.getStrAccountType() });
			return query;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//created by ankit on 19-04-2023

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AccountTypeMaster getGLAccTypeAccNoOnAccType(AccountTypeMaster accountTypeMaster) {
		try
		{
	        List<AccountTypeMaster> accountTypeMasters = this.jdbcTemplate.query(
					"SELECT gl_account_type AS strGLAccountType , gl_account_no AS strGLAccountNumber "
					+ "FROM account_type_master "
					+ "WHERE account_type = ? ",
					(RowMapper) new BeanPropertyRowMapper(AccountTypeMaster.class),
					new Object[] 
					{
						accountTypeMaster.getStrAccountType()
					}
					);
					
	        if(accountTypeMasters!=null && accountTypeMasters.size() > 0)
	        {
	        	accountTypeMaster = accountTypeMasters.get(0);
	        }
	        return accountTypeMaster;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
     return null;
  }
	
	@Override
	public boolean isGLAccountTypeAlreadyExist(AccountTypeMaster accountTypeMaster) {
		try
		{
			String sql = "SELECT COUNT(gl_account_no) AS strGLAccountNumber FROM account_type_master atm "
					+" WHERE atm.gl_account_type = ? AND atm.gl_account_no = ? ";
			int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] {accountTypeMaster.getStrGLAccountType(), accountTypeMaster.getStrGLAccountNumber() });
			return count > 0;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AccountTypeMaster> getAccountTypeMasterDetailsBasedOnAccountType(AccountTypeMaster accountTypeMaster) {
		try 
		{
			String accountType = accountTypeMaster.getStrAccountType();
			String accNoc = accountTypeMaster.getStrLastAccNumber();
			
			Criteria criteria = createEntityCriteria();
			if (accountTypeMaster.getStrAccountType() != null && accountTypeMaster.getStrAccountType().trim().length() > 0) 
			{
				criteria.add(Restrictions.eq("strAccountType", accountTypeMaster.getStrAccountType()));
			}
			if (accountTypeMaster.getStrLastAccNumber() != null && accountTypeMaster.getStrLastAccNumber().trim().length() > 0) 
			{
				criteria.add(Restrictions.eq("strLastAccNumber", accountTypeMaster.getStrLastAccNumber()));
			}
			List<AccountTypeMaster> listData = (List<AccountTypeMaster>) criteria.list();
			
			if (listData !=null && listData.size() > 0) 
			{
				return listData;
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeInfoLog("AccountTypeMasterDaoImpl.getAccountTypeMasterDetailsBasedOnAccountType()"+e);
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public int updateAccountTypeDetails(AccountTypeMaster accountTypeMaster) {
		try {
			int count = this.jdbcTemplate.update("UPDATE account_type_master "
											+ "SET "
											+ "status = ? , "
											+ "tax_type = ? , "
											+ "tax_value = ? "
											+ "WHERE "
											+ "participant_id = ? and "
											+ "account_type = ? ",
											new Object[] {
													accountTypeMaster.getStrStatus(),
													accountTypeMaster.getStrTaxType(),
													accountTypeMaster.getStrTaxVal(),
													accountTypeMaster.getStrParticipantId(),
													accountTypeMaster.getStrAccountType()
											});
					amsLogger.writeInfoLog("AccountTypeMasterDaoImpl.updateAccountTypeDetails()"+accountTypeMaster);
					return count;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AccountTypeMaster getAccountTypeMasterBasedOnAccountType(AccountMaster accountMaster) 
	{
		try
		{
	        List<AccountTypeMaster> accountTypeMasters = this.jdbcTemplate.query(
					"SELECT gl_account_type AS strGLAccountType , gl_account_no AS strGLAccountNumber "
					+ "FROM account_type_master "
					+ "WHERE account_type = ? ",
					(RowMapper) new BeanPropertyRowMapper(AccountTypeMaster.class),
					new Object[] 
					{
						accountMaster.getStrAccountType()
					}
					);
					
	        AccountTypeMaster accountTypeMaster = null;
			if(accountTypeMasters!=null && accountTypeMasters.size() > 0)
	        {
	        	accountTypeMaster = accountTypeMasters.get(0);
	        }
	        return accountTypeMaster;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
     return null;
  }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AccountTypeMaster getAccountTypeCode(AccountTypeMaster accountTypeMaster) 
	{
		try
		{
	        List<AccountTypeMaster> accountTypeMasters = this.jdbcTemplate.query(
					"SELECT account_type_code AS strAccountTypeCode , account_type AS strAccountType "
					+ "FROM account_type_master "
					+ "WHERE account_type = ? ",
					(RowMapper) new BeanPropertyRowMapper(AccountTypeMaster.class),
					new Object[] 
					{
						accountTypeMaster.getStrAccountType()
					}
					);
					
	        if(accountTypeMasters!=null && accountTypeMasters.size() > 0)
	        {
	        	accountTypeMaster = accountTypeMasters.get(0);
	        }
	        return accountTypeMaster;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	 return null;
	}

	@Override
	public void updateNubanSerialNumberByAccountType(AccountTypeMaster accountTypeMaster) 
	{
		try 
		{
			String sql = "UPDATE account_type_master SET nuban_serial_number = ? WHERE account_type = ?";
			int update = jdbcTemplate.update(sql, 
					new Object[] {accountTypeMaster.getStrNubanSerialNumber(),
							accountTypeMaster.getStrAccountType()}
			);
			//return update;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isCreditAccounType(AccountTypeMaster accountTypeMster) 
	{
		try
		{
			String sql = "SELECT COUNT(*) AS cnt FROM account_type_master atm WHERE atm.account_type = ? AND atm.account_type_category = 'C'";
			int count = jdbcTemplate.queryForObject(sql, Integer.class, 
			new Object[] 
			{ 
				accountTypeMster.getStrAccountType()
			});
			return count > 0;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return false;
	}
	
}
