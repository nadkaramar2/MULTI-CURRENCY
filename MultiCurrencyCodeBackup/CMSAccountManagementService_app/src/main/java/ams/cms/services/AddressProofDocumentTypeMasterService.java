package ams.cms.services;

import java.util.List;

import ams.cms.model.AddressProofDocumentTypeMaster;

public interface AddressProofDocumentTypeMasterService
{
	List<AddressProofDocumentTypeMaster> getAddressProofDocumentTypeMasterList(AddressProofDocumentTypeMaster addressProofDocumentTypeMaster) throws Exception;
	
	AddressProofDocumentTypeMaster getAddressProofDocumentTypeMaster(AddressProofDocumentTypeMaster addressProofDocumentTypeMaster) throws Exception;
}
