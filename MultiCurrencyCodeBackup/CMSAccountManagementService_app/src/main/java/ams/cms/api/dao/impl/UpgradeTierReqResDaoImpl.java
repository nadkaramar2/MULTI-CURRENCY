package ams.cms.api.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.UpgradeTierReqResDao;
import ams.cms.api.model.UpgradeTierReqRes;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;

//created by ankit on 12-05-2023
@Repository
public class UpgradeTierReqResDaoImpl extends AbstractGenericDao<UpgradeTierReqRes> implements UpgradeTierReqResDao 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(UpgradeTierReqResDaoImpl.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<UpgradeTierReqRes> getDetails(UpgradeTierReqRes upgradeTierReqRes) 
	{
		try 
		{
			StringBuilder selectSb = new StringBuilder("SELECT id AS strId , cust_id AS strCustId, tier_type AS strTierType ,req_status AS strReqStatus,");
			selectSb.append("req_date_time AS strReqDateTime, res_date_time AS strResDateTime FROM upgrade_tier_req_res ");
			selectSb.append("WHERE cust_id = ? AND tier_type = ?");
						
			List<UpgradeTierReqRes> upgradeTierReqResData = jdbcTemplate.query(selectSb.toString(), new BeanPropertyRowMapper<UpgradeTierReqRes>(UpgradeTierReqRes.class),
			new Object[] 
			{
				upgradeTierReqRes.getStrCustId(), 
				upgradeTierReqRes.getStrTierType()
			});
			return upgradeTierReqResData;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	
	@Override
	public int addEntryInUpgradeTierReqRes(UpgradeTierReqRes upgradeTierReqRes)
	{
		String sql = "INSERT INTO upgrade_tier_req_res (cust_id, tier_type, req_status, req_date_time) " +
	               "VALUES (?, ?, ?, ?)";
		int count =	jdbcTemplate.update(sql,new Object[] {
			upgradeTierReqRes.getStrCustId(),
			upgradeTierReqRes.getStrTierType(),
			upgradeTierReqRes.getStrReqStatus(),
			upgradeTierReqRes.getStrReqDateTime()
		});	
		return count;
	}
	
	public int updateUpgradeRequest(UpgradeTierReqRes upgradeTierReqRes) 
	{
		int count = 0;
		try
		{
			List<Object> objectList = new ArrayList<Object>();
			
			StringBuilder queryBuilder = new StringBuilder("UPDATE upgrade_tier_req_res SET ");
			
			if (upgradeTierReqRes.getStrReqStatus() != null && upgradeTierReqRes.getStrReqStatus().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("req_status = ").append("?");
				objectList.add(upgradeTierReqRes.getStrReqStatus());
			}
			if (upgradeTierReqRes.getStrRejectedReason() != null )
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("rejetcted_reason = ").append("?");
				objectList.add(upgradeTierReqRes.getStrRejectedReason());
			}
			if (upgradeTierReqRes.getStrResDateTime()!=null)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("res_date_time = ").append("?");
				objectList.add(upgradeTierReqRes.getStrResDateTime());
			}
			
			queryBuilder.append(" WHERE cust_id = ").append("?");
			objectList.add(upgradeTierReqRes.getStrCustId());
			
			queryBuilder.append(" AND tier_type = ").append("?");
			objectList.add(upgradeTierReqRes.getStrTierType());
			
			Object[] object = objectList.toArray();
			
			amsLogger.writeInfoLog("Update Request for Tier Upgrade::"+queryBuilder);
			count = this.jdbcTemplate.update(queryBuilder.toString(), object);
			return count;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return count;
	}

	/*
	@Override
	public int updateUpgradeRequest(UpgradeTierReqRes upgradeTierReqRes) 
	{
		try 
		{
			StringBuilder updateQuery = new StringBuilder();
			
			
			String sql = "UPDATE upgrade_tier_req_res SET req_status = ? where cust_id = ? AND tier_type = ?";
			
			
			int update = this.jdbcTemplate.update(sql,
			new Object[] 
			{ 
				upgradeTierReqRes.getStrReqStatus(),
				upgradeTierReqRes.getStrCustId(),
				upgradeTierReqRes.getStrTierType()
			});
			return update;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	*/
}
