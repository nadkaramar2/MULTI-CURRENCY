package ams.cms.dao.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.MontraAccountMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.MontraAccountMaster;

@Repository
public class MontraAccountMasterDaoImpl extends AbstractGenericDao<MontraAccountMaster> implements MontraAccountMasterDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(AccountMasterDaoImpl.class);
	
	@Autowired
	private	JdbcTemplate jdbcTemplate;
	
	@Override
	public MontraAccountMaster findMontraAccountMasterByCustIdAndMontraId(MontraAccountMaster montraRequestModel)
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT mam.cust_id AS strCustId, mam.cid AS cid, mam.bid AS bid ");
			queryBuilder.append("FROM montra_account_master AS mam ");
			queryBuilder.append("WHERE mam.cust_id = '" + montraRequestModel.getStrCustId().trim() + "' ");
			
			if (montraRequestModel.getCid() != null && montraRequestModel.getCid().trim().length() > 0) 
			{
				queryBuilder.append("AND mam.cid = '"+ montraRequestModel.getCid().trim() +"' ");
			}
			if(montraRequestModel.getBid()!=null && montraRequestModel.getBid().trim().length() > 0) 
			{
				queryBuilder.append("AND mam.bid = '"+ montraRequestModel.getBid().trim() +"' ");
			}
			amsLogger.writeInfoLog("Query:: "+queryBuilder.toString());
			
			List<MontraAccountMaster> montraAccountMasters  = jdbcTemplate.query(queryBuilder.toString(), new BeanPropertyRowMapper<MontraAccountMaster>(MontraAccountMaster.class), new Object[] {});
			if (montraAccountMasters!=null && montraAccountMasters.size() > 0) 
			{
				return montraAccountMasters.get(0);
			}	
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}	

	@Override
	public MontraAccountMaster findByMontraId(MontraAccountMaster montraAccountMaster) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT mam.cust_id AS strCustId , mam.cid AS cid From montra_account_master AS mam WHERE mam.cid = '" + montraAccountMaster.getCid() + "' ");
												
			amsLogger.writeInfoLog(queryBuilder.toString());
			
			List<MontraAccountMaster> montraAccountMasters  = jdbcTemplate.query(queryBuilder.toString(), new BeanPropertyRowMapper<MontraAccountMaster>(MontraAccountMaster.class),new Object[]{});			
			if (montraAccountMasters!=null && montraAccountMasters.size() > 0) 
			{
				return montraAccountMasters.get(0);
			}	
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public MontraAccountMaster getMontraAccountMasterInstance(MontraAccountMaster montraAccountMaster) 
	{
		try 
		{
			StringBuilder selectQuerySb = new StringBuilder("SELECT mam.cust_id AS strCustId, mam.cid AS cid, cm.mobile_no AS mobileNo, cm.active_tier AS activeTier ");
			selectQuerySb.append("From montra_account_master AS mam INNER JOIN customer_master AS cm ON cm.cust_id = mam.cust_id ");
			//selectQuerySb.append("WHERE mam.cid = ? AND mam.cust_id = ?");
			selectQuerySb.append("WHERE mam.cust_id = ?");
			
			List<MontraAccountMaster> montraAccountMasters  = jdbcTemplate.query(selectQuerySb.toString(), new BeanPropertyRowMapper<MontraAccountMaster>(MontraAccountMaster.class),
			new Object[] 
			{
				//montraAccountMaster.getCid().trim(), 
				montraAccountMaster.getStrCustId().trim()
			});			
				
			if (montraAccountMasters!=null && montraAccountMasters.size() > 0) 
			{
					return montraAccountMasters.get(0);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured in getMontraAccountMasterInstance::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public MontraAccountMaster validateBid(MontraAccountMaster montraAccountMaster) 
	{
		return null;
	}
	
	@Override
	public MontraAccountMaster findByBid(MontraAccountMaster montraAccountMaster) {
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT mam.cust_id AS strCustId , mam.cid AS cid , mam.bid AS bid From montra_account_master AS mam  WHERE mam.bid = '" + montraAccountMaster.getBid() + "' ");
			queryBuilder.append("AND mam.account_type = '"+montraAccountMaster.getStrAccountType().trim()+"'");
			
			amsLogger.writeInfoLog(queryBuilder.toString());
			
			List<MontraAccountMaster> montraAccountMasters  = jdbcTemplate.query(queryBuilder.toString(), new BeanPropertyRowMapper<MontraAccountMaster>(MontraAccountMaster.class),
			     new Object[]  { 
			});			
			if (montraAccountMasters!=null && montraAccountMasters.size() > 0) 
			{
				return montraAccountMasters.get(0);
			}	
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public MontraAccountMaster findByMontraCid(MontraAccountMaster montraAccountMaster) {
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT mam.cust_id AS strCustId , mam.cid AS cid, mam.bid AS bid, mam.mcc_code as mccCode From montra_account_master AS mam  WHERE mam.cid = '" + montraAccountMaster.getCid() + "' ");
												
			amsLogger.writeInfoLog(queryBuilder.toString());
			
			List<MontraAccountMaster> montraAccountMasters  = jdbcTemplate.query(queryBuilder.toString(), new BeanPropertyRowMapper<MontraAccountMaster>(MontraAccountMaster.class), new Object[] {});			
			if (montraAccountMasters!=null && montraAccountMasters.size() > 0) 
			{
				return montraAccountMasters.get(0);
			}	
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public MontraAccountMaster findMontraAccountMasterByCustIdAccountTypeAndMontraId(MontraAccountMaster montraAccountMaster) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT mam.cust_id AS strCustId , mam.cid AS cid From montra_account_master AS mam WHERE mam.cid = '" + montraAccountMaster.getCid() + "' And  mam.cust_id = '"+ montraAccountMaster.getStrCustId() +"' AND mam.account_type = '"+ montraAccountMaster.getAccountType() +"' ");
												
			List<MontraAccountMaster> montraAccountMasters  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<MontraAccountMaster>(MontraAccountMaster.class),
			     new Object[]  
			 { 
			    	
			});
			amsLogger.writeInfoLog(queryBuilder.toString());
			if (montraAccountMasters!=null && montraAccountMasters.size() > 0) 
			{
				return montraAccountMasters.get(0);
			}	
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public MontraAccountMaster validateMCCAgainstAccountType(MontraAccountMaster montraAccountMaster) 
	{
		MontraAccountMaster resMontraAccountMaster = null;
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT mam.mcc_code AS mccCode from montra_account_master AS mam ");
			queryBuilder.append("WHERE cid = '"+montraAccountMaster.getCid()+"' AND cust_id = '"+montraAccountMaster.getStrCustId()+"' ");
			queryBuilder.append("AND account_type = '"+montraAccountMaster.getStrAccountType()+"' AND bid = '"+montraAccountMaster.getBid()+"' AND mccCode = '"+montraAccountMaster.getMccCode()+"' ");
			amsLogger.writeInfoLog(queryBuilder.toString());
			
			List<MontraAccountMaster> montraAccountMasters  = jdbcTemplate.query(queryBuilder.toString(), new BeanPropertyRowMapper<MontraAccountMaster>(MontraAccountMaster.class),new Object[]
					{});
			if (montraAccountMasters!=null && montraAccountMasters.size() > 0) 
			{
				resMontraAccountMaster = montraAccountMasters.get(0);
				return resMontraAccountMaster;
			}	
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return resMontraAccountMaster;
	}
	
	@Override
	public MontraAccountMaster accountInfoByMontraCid(MontraAccountMaster montraAccountMaster) {
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT mam.cust_id AS strCustId , mam.cid AS cid, mam.bid AS bid, mam.mcc_code as mccCode, mam.account_type AS accountType From montra_account_master AS mam  WHERE mam.cid = '" + montraAccountMaster.getCid() + "' and mam.cust_id = '" + montraAccountMaster.getStrCustId() + "' ");
			
			amsLogger.writeInfoLog(queryBuilder.toString());
			
			List<MontraAccountMaster> montraAccountMasters  = jdbcTemplate.query(queryBuilder.toString(), new BeanPropertyRowMapper<MontraAccountMaster>(MontraAccountMaster.class), new Object[] {});			
			if (montraAccountMasters!=null && montraAccountMasters.size() > 0) 
			{
				return montraAccountMasters.get(0);
			}	
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
}
