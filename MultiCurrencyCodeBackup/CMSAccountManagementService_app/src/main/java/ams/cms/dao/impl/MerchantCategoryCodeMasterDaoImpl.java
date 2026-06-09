package ams.cms.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.dao.MerchantCategoryCodeMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.MerchantCategoryCodeMaster;

@Repository
public class MerchantCategoryCodeMasterDaoImpl extends AbstractGenericDao<MerchantCategoryCodeMaster> implements MerchantCategoryCodeMasterDao
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	@Override
	public List<MerchantCategoryCodeMaster> getSelectedMccAndDescrList(MerchantCategoryCodeMaster merchantCategoryCodeMaster) 
	{
		try 
		{
			List<MerchantCategoryCodeMaster> merchantCategoryCodeMasters = this.jdbcTemplate.query(
					"SELECT pwwm.mcc_code AS strMccCode, mccm.mcc_desc AS strMccCodeDesc "
					+ "FROM participant_wise_wallet_master pwwm "
					+ "INNER JOIN merchant_category_code_master mccm "
					+ "ON pwwm.mcc_code = mccm.mcc_code WHERE pwwm.participant_id= ?",
					new Object[] { merchantCategoryCodeMaster.getStrParticipantID() },
					(RowMapper) new BeanPropertyRowMapper(MerchantCategoryCodeMaster.class));
			
			return merchantCategoryCodeMasters;
		}
		catch (Exception e) {
		}
		return null;
	}
	
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	@Override
	public List<MerchantCategoryCodeMaster> getUnSelectedMccAndDescrList(MerchantCategoryCodeMaster merchantCategoryCodeMaster) 
	{
		try 
		{
			List<MerchantCategoryCodeMaster> merchantCategoryCodeMasters = this.jdbcTemplate.query(
					"SELECT mcc_code AS strMccCode, mcc_desc AS strMccCodeDesc "
					+ "FROM merchant_category_code_master AS mc WHERE "
					+ "NOT EXISTS ( SELECT pwm.mcc_code FROM participant_wise_wallet_master AS "
					+ "pwm WHERE pwm.mcc_code = mc.mcc_code AND pwm.participant_id = ?)",
					new Object[] { merchantCategoryCodeMaster.getStrParticipantID() },
					(RowMapper) new BeanPropertyRowMapper(MerchantCategoryCodeMaster.class));
			
			return merchantCategoryCodeMasters;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
