package ams.cms.api.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.DenominationValuesMasterDao;
import ams.cms.api.model.DenominationValuesMaster;
import ams.cms.dao.generic.AbstractGenericDao;

@Repository
public class DenominationValuesMasterDaoImpl extends AbstractGenericDao<DenominationValuesMaster> implements DenominationValuesMasterDao
{
		@Autowired
		private JdbcTemplate jdbcTemplate;

		@SuppressWarnings("unchecked")
		@Override
		public DenominationValuesMaster getDenominationValuesMasterBasedOnParameters(	DenominationValuesMaster denominationValuesMaster) 
		{
			try 
			{
				Criteria criteria = createEntityCriteria();
				if (denominationValuesMaster.getStrCurrencyCode()!=null && denominationValuesMaster.getStrCurrencyCode().trim().length() > 0)
				{
					criteria.add(Restrictions.eq("strCurrencyCode", denominationValuesMaster.getStrCurrencyCode()));
				}
				if (denominationValuesMaster.getStrCountry()!=null && denominationValuesMaster.getStrCountry().trim().length() > 0)
				{
					criteria.add(Restrictions.eq("strCountry", denominationValuesMaster.getStrCountry()));
				}
				
				List<DenominationValuesMaster> denominationValuesList = (List<DenominationValuesMaster>) criteria.list();
				if (denominationValuesList !=null && denominationValuesList.size() > 0) 
				{
					return denominationValuesList.get(0);
				}
			}
			catch (Exception e) 
			{
				System.out.println("Exception in getAccountInformationListById::"+e);
				e.printStackTrace();
			}
			return null;		
		}
	
		
	
}
