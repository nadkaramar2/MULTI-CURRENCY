package ams.cms.api.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.FeeTypeMasterDao;
import ams.cms.api.model.FeeTypeMaster;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;

@Repository
public class FeeTypeMasterDaoImpl extends AbstractGenericDao<FeeTypeMaster> implements FeeTypeMasterDao 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(FeeTypeMasterDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<FeeTypeMaster> getGLAccountInfoBasedOnFeeType(FeeTypeMaster feeTypeMaster) 
	{
		try 
		{
			StringBuilder selectQuerySb = new StringBuilder("SELECT ftm.fee_type AS feeType, vtm.vat_type AS vatType, ");
			selectQuerySb.append("glatm.account_type AS glAccountType, glatm.account_number AS glAccountNo, ");
			selectQuerySb.append("glatm.account_description AS glAccountRef, glatm.closing_balance AS glAccountBalance ");
			selectQuerySb.append("FROM gl_account_type_master glatm ");
			selectQuerySb.append("LEFT JOIN fee_type_master ftm ON ftm.fee_type = glatm.account_type ");
			selectQuerySb.append("LEFT JOIN vat_type_master vtm ON vtm.vat_type = glatm.account_type ");
			selectQuerySb.append("WHERE glatm.account_type IN('"+feeTypeMaster.getFeeType()+"',(SELECT ftm.vat_type FROM fee_type_master ftm WHERE ftm.fee_type = '"+feeTypeMaster.getFeeType()+"'))");
			
			amsLogger.writeInfoLog("Inside getGLAccountInfoBasedOnFeeType Select Query is:: "+selectQuerySb.toString());
			List<FeeTypeMaster> glAccountList = this.jdbcTemplate.query(selectQuerySb.toString(), (RowMapper<FeeTypeMaster>) new BeanPropertyRowMapper<FeeTypeMaster>(FeeTypeMaster.class), new Object[] {});
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

	@Override
	public List<FeeTypeMaster> getFeeCollectedBalance(FeeTypeMaster feeTypeMaster) {
		try 
		{
			StringBuilder selectQuerySb = new StringBuilder(" SELECT '"+feeTypeMaster.getDate()+"' AS date, '"+feeTypeMaster.getTime()+"' AS time, ftm.fee_type AS feeType, ftm.fee_description AS feeDescription, ");
			selectQuerySb.append("gltm.account_number AS glAccountNo, gltm.closing_balance AS glAccountBalance ");
			selectQuerySb.append("FROM fee_type_master ftm ");
			selectQuerySb.append("INNER JOIN gl_account_type_master gltm ");
			selectQuerySb.append("ON ftm.fee_type = gltm.account_type ");
			
			amsLogger.writeInfoLog("Inside getFeeCollectedBalance Select Query is:: "+selectQuerySb.toString());
			List<FeeTypeMaster> feeTypeCollectedList = this.jdbcTemplate.query(selectQuerySb.toString(), (RowMapper<FeeTypeMaster>) new BeanPropertyRowMapper<FeeTypeMaster>(FeeTypeMaster.class), new Object[] {});
			if (feeTypeCollectedList!=null && feeTypeCollectedList.size() > 0) 
			{
				return feeTypeCollectedList;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
}
