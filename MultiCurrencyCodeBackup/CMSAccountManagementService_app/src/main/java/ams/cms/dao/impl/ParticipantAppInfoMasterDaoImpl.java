package ams.cms.dao.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.ParticipantAppInfoMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.ParticipantAppInfoMaster;

@Repository
public class ParticipantAppInfoMasterDaoImpl extends AbstractGenericDao<ParticipantAppInfoMaster> implements ParticipantAppInfoMasterDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(ParticipantAppInfoMasterDaoImpl.class);
	
	@Autowired
	private	JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public ParticipantAppInfoMaster getParticipantAppInfoObject(ParticipantAppInfoMaster participantAppInfoMaster)
	{
		boolean isDataCheck = false;
		try 
		{
			Criteria criteria = createEntityCriteria();			
			if (participantAppInfoMaster.getStrParticipantId()!=null && participantAppInfoMaster.getStrParticipantId().trim().length()>0) 
			{
				criteria.add(Restrictions.eq("strParticipantId", participantAppInfoMaster.getStrParticipantId().trim()));
				isDataCheck = true;
			}
			if (isDataCheck) 
			{
				List<ParticipantAppInfoMaster> listData = (List<ParticipantAppInfoMaster>) criteria.list();
				if (listData !=null && listData.size() > 0) 
				{
					return listData.get(0);
				}
			}			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;		
	}

	@Override
	public int updateParticipantAppInfo(ParticipantAppInfoMaster participantAppInfoMaster)throws Exception 
	{
		try 
		{
			StringBuilder updateQuery = new StringBuilder("UPDATE participant_app_info as pai set pai.secret_key = ?, pai.api_key = ?, ");
			updateQuery.append("pai.iv = ?, pai.phrase = ?, pai.salt = ?, pai.created_date = ? ");
			updateQuery.append("WHERE pai.participant_id = ?");
			int count = this.jdbcTemplate.update(updateQuery.toString(), new Object[] 
			{
				participantAppInfoMaster.getStrSecretKey(),
				participantAppInfoMaster.getStrApiKey(),
				participantAppInfoMaster.getStrIv(),
				participantAppInfoMaster.getStrPhrase(),
				participantAppInfoMaster.getStrSalt(),
				participantAppInfoMaster.getStrCreatedDate(),
				participantAppInfoMaster.getStrParticipantId()
			});
			
			return count;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	
	
}
