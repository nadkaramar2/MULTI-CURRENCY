package ams.cms.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.TcsTypeMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.TcsTypeMaster;

@Repository
public class TcsTypeMasterDaoImpl extends AbstractGenericDao<TcsTypeMaster> implements TcsTypeMasterDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public TcsTypeMaster getTcsTypeMasterObj(TcsTypeMaster tcsTypeMaster) {
		try 
		{	
			StringBuilder sql = new StringBuilder(" SELECT ttm.gl_account_type AS glAccountType , ttm.gl_account_number AS glAccountNumber ");
			sql.append( " FROM tcs_type_master ttm WHERE ttm.tcs_type = '"+tcsTypeMaster.getTcsType().trim()+"'  ");
		
			List<TcsTypeMaster> tcsTypeMasters  = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<TcsTypeMaster>(TcsTypeMaster.class), new Object[]  {});
			if (tcsTypeMasters!=null && tcsTypeMasters.size() > 0) 
			{
				return tcsTypeMasters.get(0);
			}	
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

}
