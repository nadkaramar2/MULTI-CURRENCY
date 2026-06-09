package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.AddressProofDocumentTypeMaster;
import ams.cms.services.AddressProofDocumentTypeMasterService;

@RestController
@RequestMapping("/address_proof_document_type")
public class AddressProofDocumentTypeMasterController
{
	@Autowired
	AddressProofDocumentTypeMasterService addressProofDocumentTypeMasterService;
	
	@RequestMapping(value = "/documentList", method = RequestMethod.POST)
	public ResponseEntity<?> getAddressProofDocumentTypeList(@RequestBody AddressProofDocumentTypeMaster addressProofDocumentTypeMaster)
	{
		String result = null;
		try 
		{
			List<AddressProofDocumentTypeMaster> addressProofDocumentTypeMasters = addressProofDocumentTypeMasterService.getAddressProofDocumentTypeMasterList(addressProofDocumentTypeMaster);
			if (addressProofDocumentTypeMasters != null && addressProofDocumentTypeMasters.size() > 0)
			{
				return ResponseEntity.ok(addressProofDocumentTypeMasters);
			}			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
}
