package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.AccountInterestMaster;
import ams.cms.services.AccountInterestMasterService;

@RestController
@RequestMapping("/account-wise-credit-interest")
public class AccountInterestMasterController 
{
	@Autowired
	private AccountInterestMasterService accountInterestMasterService;
	
	@RequestMapping(value = "/getOutStandingInterest", method = RequestMethod.POST)
	public ResponseEntity<?> getOutStandingInterestList(@RequestBody AccountInterestMaster accountInterestMaster)
	{
		try 
		{
			List<AccountInterestMaster> accountInterestMasters = accountInterestMasterService.getOutStandingInterestList(accountInterestMaster);
			return ResponseEntity.ok(accountInterestMasters);			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
}
