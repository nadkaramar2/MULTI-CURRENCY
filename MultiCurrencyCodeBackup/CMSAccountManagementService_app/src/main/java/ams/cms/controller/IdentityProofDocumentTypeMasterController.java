package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.IdentityProofDocumentTypeMaster;
import ams.cms.services.IdentityProofDocumentTypeMasterServices;

@RestController
@RequestMapping("/identity_proof_document_type")
public class IdentityProofDocumentTypeMasterController 
{
	@Autowired
	IdentityProofDocumentTypeMasterServices identityProofDocumentTypeMasterServices;
	
	@RequestMapping(value = "/documentList", method = RequestMethod.POST)
	public ResponseEntity<?> getAddressProofDocumentTypeList(@RequestBody IdentityProofDocumentTypeMaster identityProofDocumentTypeMaster)
	{
		String result = null;
		try 
		{
			List<IdentityProofDocumentTypeMaster> identityProofDocumentTypeMasters = identityProofDocumentTypeMasterServices.getIdentityProofDocumentTypeMasters(identityProofDocumentTypeMaster);
			if (identityProofDocumentTypeMasters != null && identityProofDocumentTypeMasters.size() > 0)
			{
				return ResponseEntity.ok(identityProofDocumentTypeMasters);
			}			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
}
