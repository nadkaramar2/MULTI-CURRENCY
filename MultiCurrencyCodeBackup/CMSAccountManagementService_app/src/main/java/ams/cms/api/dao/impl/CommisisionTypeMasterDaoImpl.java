package ams.cms.api.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.CommisisionTypeMasterDao;
import ams.cms.api.model.CommisisionTypeMaster;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;

@Repository
public class CommisisionTypeMasterDaoImpl extends AbstractGenericDao<CommisisionTypeMaster> implements CommisisionTypeMasterDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(CommisisionTypeMasterDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<CommisisionTypeMaster> getGLAccountInfoBasedOnCommisionType(CommisisionTypeMaster commisisionTypeMaster) 
	{
		try 
		{
			StringBuilder selectQuerySb = new StringBuilder("SELECT ctm.commision_type AS commissionType, vtm.vat_type AS vatType, ");
			selectQuerySb.append("glatm.account_type AS glAccountType, glatm.account_number AS glAccountNo, ");
			selectQuerySb.append("glatm.account_description AS glAccountRef, glatm.closing_balance AS glAccountBalance ");
			selectQuerySb.append("FROM gl_account_type_master glatm ");
			selectQuerySb.append("LEFT JOIN commision_type_master ctm ON ctm.commision_type = glatm.account_type ");
			selectQuerySb.append("LEFT JOIN vat_type_master vtm ON vtm.vat_type = glatm.account_type ");
			selectQuerySb.append("WHERE glatm.account_type IN('"+commisisionTypeMaster.getCommisionType()+"',(SELECT ctm.vat_type FROM commision_type_master ctm WHERE ctm.commision_type = '"+commisisionTypeMaster.getCommisionType()+"'))");
			
			amsLogger.writeInfoLog("Inside getGLAccountInfoBasedOnFeeType Select Query is:: "+selectQuerySb.toString());
			List<CommisisionTypeMaster> glAccountList = this.jdbcTemplate.query(selectQuerySb.toString(), (RowMapper<CommisisionTypeMaster>) new BeanPropertyRowMapper<CommisisionTypeMaster>(CommisisionTypeMaster.class), new Object[] {});
			if (glAccountList!=null && glAccountList.size() > 0) 
			{
				return glAccountList;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	
	}

}
