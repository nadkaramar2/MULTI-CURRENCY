package ams.cms.api.dao;

import java.util.List;

public interface IdentityProofDocumentTypeDao {

	List<String> getIdentityDocumentTypes();

	String getPrefixOfDocumentType(String documentType);
}
