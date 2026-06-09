package ams.cms.services;

import java.util.List;

import ams.cms.model.IdentityProofDocumentTypeMaster;

public interface IdentityProofDocumentTypeMasterServices 
{
	List<IdentityProofDocumentTypeMaster> getIdentityProofDocumentTypeMasters(IdentityProofDocumentTypeMaster identityProofDocumentTypeMaster) throws Exception;
	
	IdentityProofDocumentTypeMaster getIdentityProofDocumentTypeMasterObject(IdentityProofDocumentTypeMaster identityProofDocumentTypeMaster) throws Exception;
}
