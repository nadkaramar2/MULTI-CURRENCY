package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.AccountTypeCharges;
import ams.cms.services.AccountTypeChargesService;

@RestController
@RequestMapping("/accountTypeCharges")
public class AccountTypeChargesController 
{
	@Autowired
	AccountTypeChargesService accountTypeChargesService;
	
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addChargingConfigData(@RequestBody AccountTypeCharges accountTypeCharges)
	{
		String result = null;
		try {
			accountTypeCharges = accountTypeChargesService.addChargingConfigData(accountTypeCharges);
			if(accountTypeCharges.getStrId() != null)
			{
				return ResponseEntity.ok(accountTypeCharges);
			}
			
		}catch (Exception e) {
			System.out.println("ChargeMasterController.addChargingConfigData()"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getSelectedChargesAccountTypeWise", method = RequestMethod.POST)
	public ResponseEntity<?> getSelectedChargesAccountTypeWise(@RequestBody AccountTypeCharges accountTypeCharges)
	{
		String result = null;
		try 
		{
			List<AccountTypeCharges> accTypeCharges = accountTypeChargesService.getSelectedChargesAccountTypeWise(accountTypeCharges);
			if (accTypeCharges != null && accTypeCharges.size()>0)
			{
				return ResponseEntity.ok(accTypeCharges);
			}			
		}
		catch (Exception e) {
			System.out.println("ChargeMasterController.getSelectedChargesAccountTypeWise()"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}	
	
}
