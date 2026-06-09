package ams.cms.api.dao.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.NubanTypeConfigDao;
import ams.cms.api.model.NubanTypeConfig;
import ams.cms.dao.generic.AbstractGenericDao;

@Repository
public class NubanTypeConfigDaoImpl extends AbstractGenericDao<NubanTypeConfig> implements NubanTypeConfigDao{

	@Autowired
	JdbcTemplate jdbcTemplate;

	@SuppressWarnings({ "deprecation" })
	@Override
	public NubanTypeConfig findDescriptionByType(NubanTypeConfig nubanTypeConfig) {
		String sql= "select nuban_type_description as strNubanTypeDescription from nuban_type_config where nuban_type = ?";
		NubanTypeConfig nubanTypeConfigs = jdbcTemplate.queryForObject(sql,
				new Object[] {nubanTypeConfig.getStrNubanType()}, 
				new BeanPropertyRowMapper<>(NubanTypeConfig.class));
		return nubanTypeConfigs;
	}
	
}
