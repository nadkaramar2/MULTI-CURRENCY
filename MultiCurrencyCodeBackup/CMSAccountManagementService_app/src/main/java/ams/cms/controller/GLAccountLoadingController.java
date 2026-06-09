package ams.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.GLAccountLoadingMaster;
import ams.cms.services.GLAccountLoadingMasterService;

@RestController
@RequestMapping("/gl-account-load")
public class GLAccountLoadingController 
{
	@Autowired
	GLAccountLoadingMasterService glAccountLoadingMasterService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> saveGLAccountLoadMasterInfo(@RequestBody GLAccountLoadingMaster glAccountLoadingMaster) 
	{
		String result = null;
		try 
		{
			glAccountLoadingMaster = glAccountLoadingMasterService.saveGLAccountLoadMasterInfo(glAccountLoadingMaster);
			return ResponseEntity.ok(glAccountLoadingMaster.getStrId());
		} 
		catch (Exception e) {
			System.out.println("Exception in addAccountTypeWiseWallet::" + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
}
