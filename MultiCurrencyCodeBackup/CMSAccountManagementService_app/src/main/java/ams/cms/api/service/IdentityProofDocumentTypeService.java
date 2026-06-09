package ams.cms.api.service;

import java.util.List;

public interface IdentityProofDocumentTypeService {
	public List<String> getIdentityDocumentType();
	public String getPrefixOfDocumentType(String identityProofDocumentDescription);
}
