package ams.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.handler.AccountWiseChargesHandler;
import ams.cms.model.AccountWiseCharges;

@RestController
@RequestMapping("/accountWiseCharge") 
public class AccountWiseChargesController 
{
	@Autowired
	AccountWiseChargesHandler accountWiseChargesHandler;
	
	@RequestMapping(value = "/processToCharge", method = RequestMethod.POST) 
	public ResponseEntity<?> processToCharge(@RequestBody AccountWiseCharges accountWiseCharges) 
	{ 
		try
		{ 
			AccountWiseCharges accountWiseChargesReq = accountWiseChargesHandler.processToCharge(accountWiseCharges);	
			return ResponseEntity.ok(accountWiseChargesReq);
		}
		catch (Exception e) 
		{
			System.out.println("Exception in processToCharge::"+e);
			e.printStackTrace(); 
		} 
		return ResponseEntity.ok(null); 
	}
	
	
	
}
