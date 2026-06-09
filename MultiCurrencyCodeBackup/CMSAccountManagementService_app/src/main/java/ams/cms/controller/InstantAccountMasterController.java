package ams.cms.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.InstantAccountCreation;
import ams.cms.services.InstantAccountMasterService;

@RestController
@RequestMapping("/instanceAccount")
public class InstantAccountMasterController 
{
	@Autowired
	InstantAccountMasterService instantAccountMasterService;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> createInstanceAccounts(@RequestBody InstantAccountCreation instantAccountCreation)
	{
		String result = null;
		try 
		{
			instantAccountCreation.setStrCreationDate(new Date());
			
			int res = instantAccountMasterService.createInstantAccount(instantAccountCreation);
			
			if (res > 0)
			{
				result = "success";
			}			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			result = "Exception in createInstanceAccounts::"+e.getMessage();
		}
		return ResponseEntity.ok(result);
	}
	
}
