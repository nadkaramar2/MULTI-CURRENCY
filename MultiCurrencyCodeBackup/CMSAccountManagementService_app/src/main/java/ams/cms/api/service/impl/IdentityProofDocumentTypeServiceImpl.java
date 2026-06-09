package ams.cms.api.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.IdentityProofDocumentTypeDao;
import ams.cms.api.service.IdentityProofDocumentTypeService;

@Transactional
@Service
public class IdentityProofDocumentTypeServiceImpl implements IdentityProofDocumentTypeService{

	@Autowired
	private IdentityProofDocumentTypeDao identityProofDocumentTypeDao;
	
	@Override
	public List<String> getIdentityDocumentType() {
		List<String> identityDocumentTypes = identityProofDocumentTypeDao.getIdentityDocumentTypes();
		return identityDocumentTypes;
	}
	
	@Override
	public String getPrefixOfDocumentType(String identityProofDocumentDescription) {
		String prefixOfDocumentType = identityProofDocumentTypeDao.getPrefixOfDocumentType(identityProofDocumentDescription);
		return prefixOfDocumentType;
	}

	
	
}
