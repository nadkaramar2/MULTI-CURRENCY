package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.IdentityProofDocumentTypeMasterDao;
import ams.cms.model.IdentityProofDocumentTypeMaster;
import ams.cms.services.IdentityProofDocumentTypeMasterServices;

@Transactional
@Service
public class IdentityProofDocumentTypeMasterServicesImpl implements IdentityProofDocumentTypeMasterServices
{
	@Autowired
	IdentityProofDocumentTypeMasterDao identityProofDocumentTypeMasterDao;
	
	@Override
	public List<IdentityProofDocumentTypeMaster> getIdentityProofDocumentTypeMasters(IdentityProofDocumentTypeMaster identityProofDocumentTypeMaster) throws Exception 
	{
		return identityProofDocumentTypeMasterDao.getIdentityProofDocumentTypeMasters(identityProofDocumentTypeMaster);
	}

	@Override
	public IdentityProofDocumentTypeMaster getIdentityProofDocumentTypeMasterObject(IdentityProofDocumentTypeMaster identityProofDocumentTypeMaster) throws Exception 
	{
		return identityProofDocumentTypeMasterDao.getIdentityProofDocumentTypeMasterObject(identityProofDocumentTypeMaster);
	}
	
}
