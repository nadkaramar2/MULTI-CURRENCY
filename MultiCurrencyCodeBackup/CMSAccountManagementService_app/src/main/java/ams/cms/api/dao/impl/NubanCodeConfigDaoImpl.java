package ams.cms.api.dao.impl;
import java.util.List;
import java.util.Optional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.NubanCodeConfigDao;
import ams.cms.api.model.NUBANAccountDto;
import ams.cms.api.model.NubanCodeConfig;
import ams.cms.api.model.NubanTypeConfig;
import ams.cms.dao.generic.AbstractGenericDao;

@Repository
public class NubanCodeConfigDaoImpl extends AbstractGenericDao<NubanCodeConfig> implements NubanCodeConfigDao
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public int addNewNubanCodeConfig(NubanCodeConfig nubanCodeConfig) {
		
		String sql = "INSERT INTO nuban_code_config (participant_id, nuban_type, nuban_code) VALUES (?, ?, ?)";
		int update = jdbcTemplate.update(sql,
				new Object[] {
						nubanCodeConfig.getStrParticipantId(),
						nubanCodeConfig.getStrNubanType(), 
						nubanCodeConfig.getStrNubanCode()},
				Integer.class);
		return update;
	}

	@Override
	public List<NubanCodeConfig> findSerialNoByUser(NubanCodeConfig nubanCodeConfig) {
		String sql = "SELECT participant_id, nuban_type, nuban_code, nuban_serial_no, created_date FROM nuban_code_config WHERE nuban_code = ?";
		Object[] params = new Object[] { nubanCodeConfig.getStrNubanCode() };
		List<NubanCodeConfig> result = jdbcTemplate.query(sql,
				params,
				new BeanPropertyRowMapper<>(NubanCodeConfig.class));
		Optional<NubanCodeConfig> optionalNubanCodeConfig = result.stream().findFirst();
		return result;
	}

	@Override
	public boolean isNubanCodeExists(NubanCodeConfig nubanCodeConfig) {
		String sql = "SELECT COUNT(*) FROM nuban_code_config WHERE nuban_code = ? AND nuban_type = ?";
		Object[] params = new Object[] { nubanCodeConfig.getStrNubanCode(),nubanCodeConfig.getStrNubanType() };
		int count = jdbcTemplate.queryForObject(sql, params, Integer.class);
		if(count>0) {
			return true;
		}
		return false;
	}

	@Override
	public void addNewNubanCodeConfig() {
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<NubanCodeConfig> getNubanCodeConfig(NubanCodeConfig nubanCodeConfig) {
		String sql = "SELECT id as strId, participant_id AS strParticipantId, nuban_type AS str_nuban_type, nuban_code AS strNubanCode, nuban_serial_no AS strNubanSerialNo, created_date AS strCreatedDate FROM nuban_code_config WHERE nuban_type = ? AND participant_id = ?";
		Object[] params = new Object[] { nubanCodeConfig.getStrNubanCode() };
		List<NubanCodeConfig> result = jdbcTemplate.query(sql,
				params,
				new BeanPropertyRowMapper<>(NubanCodeConfig.class));
		
		NubanTypeConfig nubanTypeConfigs = jdbcTemplate.queryForObject(sql,
				new Object[] {nubanCodeConfig.getStrNubanType()}, 
				new BeanPropertyRowMapper<>(NubanTypeConfig.class));
		 
		 return result;
	}
	

	@SuppressWarnings("deprecation")
	@Override
	public NubanCodeConfig getNubanCodeConfigObject(NubanCodeConfig nubanCodeConfig) 
	{
		String sql = "SELECT id as strId, participant_id AS strParticipantId, nuban_type AS str_nuban_type, nuban_code AS strNubanCode, nuban_serial_no AS strNubanSerialNo, created_date AS strCreatedDate FROM nuban_code_config WHERE nuban_type = ? AND participant_id = ?";
		Object[] params = new Object[] { nubanCodeConfig.getStrNubanType(),nubanCodeConfig.getStrParticipantId() };
		List<NubanCodeConfig> result = jdbcTemplate.query(sql,
				params,
				new BeanPropertyRowMapper<>(NubanCodeConfig.class)
				);
		
		 if(result!=null && result.size() > 0)
	        {
			 NubanCodeConfig nubanTypeConfigResult = result.get(0);
			 return nubanTypeConfigResult;
	        }
		 return null;
	}

	@Override
	public int updateNubanSerialNoByNubanCode(NubanCodeConfig nubanCodeConfig) {
		String sql = "UPDATE nuban_code_config SET nuban_serial_no = ? WHERE nuban_code = ?";
		int update = jdbcTemplate.update(sql, 
				new Object[] {nubanCodeConfig.getStrNubanSerialNo(),
						nubanCodeConfig.getStrNubanCode()}
		);
		return update;
	}
	
	@Override
	public NubanCodeConfig getNubanCodeConfigObjectByNubanCode(NubanCodeConfig nubanCodeConfig) {
		String sql = "SELECT id as strId, participant_id AS strParticipantId, nuban_type AS str_nuban_type, nuban_code AS strNubanCode, nuban_serial_no AS strNubanSerialNo, created_date AS strCreatedDate FROM nuban_code_config WHERE nuban_code = ?";
		Object[] params = new Object[] { nubanCodeConfig.getStrNubanType(),nubanCodeConfig.getStrParticipantId() };
		List<NubanCodeConfig> result = jdbcTemplate.query(sql,
				params,
				new BeanPropertyRowMapper<>(NubanCodeConfig.class)
				);
		
		 if(result!=null && result.size() > 0)
	        {
			 NubanCodeConfig nubanTypeConfigResult = result.get(0);
			 return nubanTypeConfigResult;
	        }
		 return null;
	}

	@Override
	public void isConfigCodeExists(NUBANAccountDto nUBANAccountDto) {
	}

	@Override
	public NubanCodeConfig getNubanConfigObjectBasedOnParameter(NubanCodeConfig nubanCodeConfig) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();
			
			if (nubanCodeConfig.getStrParticipantId()!=null && nubanCodeConfig.getStrParticipantId().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strParticipantId", nubanCodeConfig.getStrParticipantId().trim()));
			}
			if (nubanCodeConfig.getStrNubanType()!=null && nubanCodeConfig.getStrNubanType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strNubanType", nubanCodeConfig.getStrNubanType().trim()));
			}
			if (nubanCodeConfig.getStrNubanCode()!=null && nubanCodeConfig.getStrNubanCode().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strNubanCode", nubanCodeConfig.getStrNubanCode().trim()));
			}
			
			@SuppressWarnings("unchecked")
			List<NubanCodeConfig> nubanCodeConfigList = (List<NubanCodeConfig>) criteria.list();
			if (nubanCodeConfigList !=null && nubanCodeConfigList.size() > 0) 
			{
				return nubanCodeConfigList.get(0);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	

}
