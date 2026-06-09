package ams.cms.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.dao.ParticipantMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.ParticipantMaster;

@Repository
public class ParticipantMasterDaoImpl extends AbstractGenericDao<ParticipantMaster> implements ParticipantMasterDao
{
	@Autowired
	private	JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<ParticipantMaster> getAllParticipantList(ParticipantMaster participantMaster) {
		try 
		{
			StringBuilder sql = new StringBuilder("SELECT participant_name as strParticipantName, participant_id as strParticipantID FROM "
					+ "");
		
			@SuppressWarnings("deprecation")
			List<ParticipantMaster> participantList = this.jdbcTemplate.query(sql.toString(),
					new Object[] {},
					(RowMapper) new BeanPropertyRowMapper(ParticipantMaster.class));
			
			if (participantList !=null && participantList.size() > 0) 
			{
				return participantList;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getApiKeyBasedOnParticipant(ParticipantMaster participantMaster) 
	{
		try 
		{
			String participantId = participantMaster.getStrParticipantID().trim();
			String sql = "SELECT apikey as strApikey FROM participant_master_table pat where pat.participant_id = ?";
			String apikey = this.jdbcTemplate.queryForObject(sql, String.class, new Object[]
			{
				participantId
			});
			return apikey;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
