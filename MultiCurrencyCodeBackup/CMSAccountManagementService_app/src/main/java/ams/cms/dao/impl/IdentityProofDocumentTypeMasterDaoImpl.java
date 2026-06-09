package ams.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.IdentityProofDocumentTypeMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.IdentityProofDocumentTypeMaster;

@Repository
public class IdentityProofDocumentTypeMasterDaoImpl extends AbstractGenericDao<IdentityProofDocumentTypeMaster> implements IdentityProofDocumentTypeMasterDao
{
	@Autowired
	JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<IdentityProofDocumentTypeMaster> getIdentityProofDocumentTypeMasters(IdentityProofDocumentTypeMaster identityProofDocumentTypeMaster) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();			
			if (identityProofDocumentTypeMaster.getStrParticipantId()!=null && identityProofDocumentTypeMaster.getStrParticipantId().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strParticipantId", identityProofDocumentTypeMaster.getStrParticipantId()));
			}			
			List<IdentityProofDocumentTypeMaster> identityProofDocumentTypeMasters = (List<IdentityProofDocumentTypeMaster>) criteria.list();
			return identityProofDocumentTypeMasters;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IdentityProofDocumentTypeMaster getIdentityProofDocumentTypeMasterObject(IdentityProofDocumentTypeMaster identityProofDocumentTypeMaster) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();			
			if (identityProofDocumentTypeMaster.getStrDocumentType()!=null && identityProofDocumentTypeMaster.getStrDocumentType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strDocumentType", identityProofDocumentTypeMaster.getStrDocumentType()));
			}			
			List<IdentityProofDocumentTypeMaster> identityProofDocumentTypeMasters = (List<IdentityProofDocumentTypeMaster>) criteria.list();
			if (identityProofDocumentTypeMasters!=null && identityProofDocumentTypeMasters.size() > 0) 
			{
				return identityProofDocumentTypeMasters.get(0);				
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
}
