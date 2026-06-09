package ams.cms.api.dao;

import java.util.List;

public interface AddressProofDocumentTypeDao 
{
	List<String> getAddressDocumentTypes();

	String getPrefixOfDocumentType(String documentType);	
}
