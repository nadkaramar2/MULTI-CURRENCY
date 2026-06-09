package ams.cms.dao;

import java.util.List;

import ams.cms.model.AddressProofDocumentTypeMaster;

public interface AddressProofDocumentTypeMasterDao extends GenericDao<AddressProofDocumentTypeMaster>
{
	List<AddressProofDocumentTypeMaster> getAddressProofDocumentTypeMasterList(AddressProofDocumentTypeMaster addressProofDocumentTypeMaster);
	
	AddressProofDocumentTypeMaster getAddressProofDocumentTypeMaster(AddressProofDocumentTypeMaster addressProofDocumentTypeMaster);
}
