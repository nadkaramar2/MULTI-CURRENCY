package ams.cms.api.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.VatTypeMasterDao;
import ams.cms.api.model.FeeTypeMaster;
import ams.cms.api.model.VatTypeMaster;
import ams.cms.api.service.impl.FeeTypeServiceImpl;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;

@Repository
public class VatTypeMasterDaoImpl extends AbstractGenericDao<VatTypeMaster> implements VatTypeMasterDao {

	private AMSLogger amsLogger = AMSLogger.getInstance(VatTypeMasterDaoImpl.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<VatTypeMaster> getVatCollectedBalance(VatTypeMaster vatTypeMaster) 
	{
		try 
		{
			StringBuilder selectQuerySb = new StringBuilder(" SELECT '"+vatTypeMaster.getDate()+"' AS date, '"+vatTypeMaster.getTime()+"' AS time, vtm.vat_type AS vatType, vtm.vat_description AS vatDescription,");
			selectQuerySb.append("gltm.account_number AS glAccountNo, gltm.closing_balance AS glAccountBalance ");
			selectQuerySb.append("FROM vat_type_master vtm ");
			selectQuerySb.append("INNER JOIN gl_account_type_master gltm ");
			selectQuerySb.append("ON vtm.vat_type = gltm.account_type ");
			
			amsLogger.writeInfoLog("Inside getFeeCollectedBalance Select Query is:: "+selectQuerySb.toString());
			List<VatTypeMaster> vatTypeCollectedList = this.jdbcTemplate.query(selectQuerySb.toString(), (RowMapper<VatTypeMaster>) new BeanPropertyRowMapper<VatTypeMaster>(VatTypeMaster.class), new Object[] {});
			if (vatTypeCollectedList!=null && vatTypeCollectedList.size() > 0) 
			{
				return vatTypeCollectedList;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

}
