package ams.cms.api.dao.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.MiddleWareBankRequestDao;
import ams.cms.api.model.MiddleWareBankRequestModel;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.utility.MiddleWareRequestModel;

@Repository
public class MiddleWareBankRequestDaoImpl extends AbstractGenericDao<MiddleWareRequestModel> implements MiddleWareBankRequestDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(MiddleWareBankRequestDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;	

	@Override
	public MiddleWareBankRequestModel getMiddleWareBankRequestByParticipantId(MiddleWareBankRequestModel middleWareBankRequestModel) 
	{
		try
		{
			StringBuilder queryBuilder = new StringBuilder("Select map.app_id as appId, map.channel_code as channelCode, map.user_loginid as userLoginId,");
			queryBuilder.append("map.audit_id as auditId from middleware_app_info map where participant_id = ? ");
		
			List<MiddleWareBankRequestModel> pullAccountlist  = jdbcTemplate.query(queryBuilder.toString(), new BeanPropertyRowMapper<MiddleWareBankRequestModel>(MiddleWareBankRequestModel.class), new Object[]  
		    {
		    	middleWareBankRequestModel.getParticipantId()
		    });
	
			if (pullAccountlist!=null && pullAccountlist.size() > 0) 
			{
				return pullAccountlist.get(0);
			}			
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int updateMiddleWareAppInfo(MiddleWareBankRequestModel middleWareBankRequestModel) 
	{
		try 
		{
			StringBuilder updateSbQuery = new StringBuilder("UPDATE middleware_app_info AS mai SET mai.year = ?, mai.julian_date = ?,");
			updateSbQuery.append("mai.last_audit_ser_no = ?, mai.audit_id = ? ");
			updateSbQuery.append("WHERE mai.participant_id = ?");
			
			return this.jdbcTemplate.update(updateSbQuery.toString(), new Object[] 
			{ 
				middleWareBankRequestModel.getStrYear(),
				middleWareBankRequestModel.getStrJulianDate(),
				middleWareBankRequestModel.getStrLastAuditSerNo(),
				middleWareBankRequestModel.getAuditId(),
				middleWareBankRequestModel.getParticipantId()
			});
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return 0;
	}

}
