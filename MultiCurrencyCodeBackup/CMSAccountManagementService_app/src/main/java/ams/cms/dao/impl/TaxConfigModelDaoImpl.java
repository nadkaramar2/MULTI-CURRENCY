package ams.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ams.cms.dao.TaxConfigModelDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.TaxConfigModel;

@Repository
public class TaxConfigModelDaoImpl extends AbstractGenericDao<TaxConfigModel> implements TaxConfigModelDao
{
	@SuppressWarnings("unchecked")
	@Override
	public List<TaxConfigModel> getTaxTypeConfigModelsList(TaxConfigModel taxConfigModel)
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (taxConfigModel.getStrParticipantId()!=null && taxConfigModel.getStrParticipantId().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strParticipantID", taxConfigModel.getStrParticipantId()));
			}
			
			List<TaxConfigModel> listData = (List<TaxConfigModel>) criteria.list();
			return listData;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
}
