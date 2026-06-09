package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.api.model.PreSubAccountMaster;
import ams.cms.api.service.PreSubAccountMasterService;

@RestController
@RequestMapping("/preSubAccountMaster")
public class PreSubAccountMasterController
{
	@Autowired
	PreSubAccountMasterService preSubAccountMasterService;
	
	@RequestMapping(value = "/updateIsAccountNoCreatedField", method = RequestMethod.POST)
	public ResponseEntity<?> updateIsAccountNoCreatedField(@RequestBody PreSubAccountMaster preSubAccountMaster)
	{
		int resultCount = 0;
		try 
		{
			resultCount = preSubAccountMasterService.updateIsAccountNoCreatedField(preSubAccountMaster);
			return ResponseEntity.ok(resultCount);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(resultCount);
	}
	
	@RequestMapping(value = "/getPendingRegCustWithLinkAccount", method = RequestMethod.POST)
	public ResponseEntity<?> getPendingRegCustWithLinkAccount(@RequestBody PreSubAccountMaster preSubAccountMaster)
	{
		String result = null;
		try 
		{
			List<PreSubAccountMaster> accountInfoList = preSubAccountMasterService.getPendingRegCustWithLinkAccount(preSubAccountMaster);
			return ResponseEntity.ok(accountInfoList);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
}
