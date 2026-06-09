package ams.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.ChargeRelatedMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.ChargeRelatedMaster;


@Repository
public class ChargeRelatedMasterDaoImpl extends AbstractGenericDao<ChargeRelatedMaster> implements ChargeRelatedMasterDao{

	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<ChargeRelatedMaster> getChargeRelatedList(ChargeRelatedMaster chargeRelatedMaster) {
		{
			try 
			{
				Criteria criteria = createEntityCriteria();
				if (chargeRelatedMaster.getStrChargeRelated()!=null && chargeRelatedMaster.getStrChargeRelated().trim().length() > 0)
				{
					criteria.add(Restrictions.eq("strChargeRelated", chargeRelatedMaster.getStrChargeRelated()));
				}
				if (chargeRelatedMaster.getStrChargeRelatedDescription()!=null && chargeRelatedMaster.getStrChargeRelatedDescription().trim().length() > 0)
				{
					criteria.add(Restrictions.eq("strChargeRelatedDescription", chargeRelatedMaster.getStrChargeRelatedDescription()));
				}
				List<ChargeRelatedMaster> listData = (List<ChargeRelatedMaster>) criteria.list();
				return listData;
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			return null;
		}
	}

	
	
	@Override
	public String getChargeRelatedDescription(ChargeRelatedMaster chargeRelatedMaster) {
		try {	
			String sql = "SELECT charge_related_description AS strChargeRelatedDescription FROM charge_related_master WHERE charge_related = ? ";
			
			System.out.println("ChargeRelatedMasterDaoImpl.getChargeRelatedDescription()"+sql);
			String chargeDesc = this.jdbcTemplate.queryForObject(sql, String.class,
					new Object[]
					{chargeRelatedMaster.getStrChargeRelated()}
			);
			return chargeDesc;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}



	@Override
	public Boolean validateChargeRelated(ChargeRelatedMaster chargeRelatedMaster) {
		try {	
			String sql = "SELECT COUNT(*) FROM charge_related_master "
					+ "where charge_related = ? ";

			String validateCount = this.jdbcTemplate.queryForObject(sql, String.class,
					new Object[]
					{
						chargeRelatedMaster.getStrChargeRelated(),
					}
			);
			if(Integer.valueOf(validateCount) > 0)
			{
				return true;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
		
	}



	
	
}
	
	