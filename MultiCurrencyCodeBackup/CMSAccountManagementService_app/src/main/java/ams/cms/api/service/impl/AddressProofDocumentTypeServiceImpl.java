package ams.cms.api.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.AddressProofDocumentTypeDao;
import ams.cms.api.service.AddressProofDocumentTypeService;

@Transactional
@Service
public class AddressProofDocumentTypeServiceImpl implements AddressProofDocumentTypeService
{
	@Autowired
	private AddressProofDocumentTypeDao addressProofDocumentTypeDao;
	
	@Override
	public List<String> getAddressDocumentType() {
		List<String> addressDocumentTypes = addressProofDocumentTypeDao.getAddressDocumentTypes();
		return addressDocumentTypes;
	}
	
	@Override
	public String getPrefixOfDocumentType(String addressProofDocumentDescription) {
		String prefixOfDocumentType = addressProofDocumentTypeDao.getPrefixOfDocumentType(addressProofDocumentDescription);
		return prefixOfDocumentType;
	}

	
}
