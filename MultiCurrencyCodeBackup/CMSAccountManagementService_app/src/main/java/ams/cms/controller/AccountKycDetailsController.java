package ams.cms.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.AccountKycDetails;
import ams.cms.services.AccountKycDetailsService;

@RestController
@RequestMapping("/account-wise-kyc-details")
public class AccountKycDetailsController 
{
	@Autowired
	AccountKycDetailsService accountKycDetailsService;
	
	@RequestMapping(value = "/getSingleAccountKycDetail", method = RequestMethod.POST)
	public ResponseEntity<?> getSingleAccountKycDetail(@RequestBody AccountKycDetails accountKycDetails)
	{
		try 
		{
			accountKycDetails = accountKycDetailsService.getSingleAccountKycDetail(accountKycDetails);
			return ResponseEntity.ok(accountKycDetails);			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
}
