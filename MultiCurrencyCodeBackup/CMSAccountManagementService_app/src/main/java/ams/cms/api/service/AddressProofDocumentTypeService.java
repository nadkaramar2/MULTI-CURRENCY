package ams.cms.api.service;

import java.util.List;

public interface AddressProofDocumentTypeService {

	public List<String> getAddressDocumentType();

	String getPrefixOfDocumentType(String addressProofDocumentDescription);
	
}
