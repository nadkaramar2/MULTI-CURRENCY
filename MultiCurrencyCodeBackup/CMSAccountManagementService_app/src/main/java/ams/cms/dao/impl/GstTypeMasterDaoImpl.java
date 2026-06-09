package ams.cms.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.GstTypeMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.GstTypeMaster;

@Repository
public class GstTypeMasterDaoImpl extends AbstractGenericDao<GstTypeMaster> implements GstTypeMasterDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public GstTypeMaster getGstTypeMasterByGstType(GstTypeMaster gstTypeMaster) {
		try 
		{	
			StringBuilder sql = new StringBuilder(" SELECT gtm.gl_account_type AS glAccountType , gtm.gl_account_number AS glAccountNumber , gtm.gst_percentage AS gstPercentage ");
			sql.append( " FROM gst_type_master gtm WHERE gtm.gst_type = '"+gstTypeMaster.getGstType().trim()+"'  ");
		
			List<GstTypeMaster> gstTypeMasters  = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<GstTypeMaster>(GstTypeMaster.class), new Object[]  {});
			if (gstTypeMasters!=null && gstTypeMasters.size() > 0) 
			{
				return gstTypeMasters.get(0);
			}	
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

}
