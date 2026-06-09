package ams.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.AddressProofDocumentTypeMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.AddressProofDocumentTypeMaster;

@Repository
public class AddressProofDocumentTypeMasterDaoImpl extends AbstractGenericDao<AddressProofDocumentTypeMaster> implements AddressProofDocumentTypeMasterDao
{
	@Autowired
	JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<AddressProofDocumentTypeMaster> getAddressProofDocumentTypeMasterList(AddressProofDocumentTypeMaster addressProofDocumentTypeMaster) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();			
			if (addressProofDocumentTypeMaster.getStrParticipantId()!=null && addressProofDocumentTypeMaster.getStrParticipantId().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strParticipantId", addressProofDocumentTypeMaster.getStrParticipantId()));
			}			
			List<AddressProofDocumentTypeMaster> addressProofDocumentTypeMasters = (List<AddressProofDocumentTypeMaster>) criteria.list();
			return addressProofDocumentTypeMasters;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AddressProofDocumentTypeMaster getAddressProofDocumentTypeMaster(AddressProofDocumentTypeMaster addressProofDocumentTypeMaster) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();
			
			if (addressProofDocumentTypeMaster.getStrDocumentType()!=null && addressProofDocumentTypeMaster.getStrDocumentType().trim().length() > 0) 
			{
				criteria.add(Restrictions.eq("strDocumentType", addressProofDocumentTypeMaster.getStrDocumentType()));
			}
			List<AddressProofDocumentTypeMaster> addressProofDocumentTypeMasters = (List<AddressProofDocumentTypeMaster>) criteria.list();
			
			if(addressProofDocumentTypeMasters!=null && addressProofDocumentTypeMasters.size() > 0)
			{
				return addressProofDocumentTypeMasters.get(0);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
