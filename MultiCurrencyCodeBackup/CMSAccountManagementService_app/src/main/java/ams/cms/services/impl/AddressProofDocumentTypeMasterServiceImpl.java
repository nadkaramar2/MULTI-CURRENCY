package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.AddressProofDocumentTypeMasterDao;
import ams.cms.model.AddressProofDocumentTypeMaster;
import ams.cms.services.AddressProofDocumentTypeMasterService;

@Transactional
@Service
public class AddressProofDocumentTypeMasterServiceImpl implements AddressProofDocumentTypeMasterService
{
	@Autowired
	AddressProofDocumentTypeMasterDao addressProofDocumentTypeMasterDao;

	@Override
	public List<AddressProofDocumentTypeMaster> getAddressProofDocumentTypeMasterList(AddressProofDocumentTypeMaster addressProofDocumentTypeMaster) throws Exception 
	{
		return addressProofDocumentTypeMasterDao.getAddressProofDocumentTypeMasterList(addressProofDocumentTypeMaster);
	}

	@Override
	public AddressProofDocumentTypeMaster getAddressProofDocumentTypeMaster(AddressProofDocumentTypeMaster addressProofDocumentTypeMaster) throws Exception {
		return addressProofDocumentTypeMasterDao.getAddressProofDocumentTypeMaster(addressProofDocumentTypeMaster);
	}
	
	
}
