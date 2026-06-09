package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.AccountTypeCharges;
import ams.cms.services.ChargeMasterService;


@RestController
@RequestMapping("/accountType")
public class ChargeMasterAccountController {

	
	@Autowired
	ChargeMasterService chargeMasterService;
	
	
	@RequestMapping(value = "/getSelectedChargesAccountTypeWise", method = RequestMethod.POST)
	public ResponseEntity<?> getSelectedChargesAccountTypeWise(@RequestBody AccountTypeCharges accountTypeCharges)
	{
		String result = null;
		try 
		{
			List<AccountTypeCharges> accTypeCharges = chargeMasterService.getSelectedChargesAccountTypeWise(accountTypeCharges);
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
