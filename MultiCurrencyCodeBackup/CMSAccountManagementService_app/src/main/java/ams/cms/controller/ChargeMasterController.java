package ams.cms.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.AccountTypeCharges;
import ams.cms.model.ChargeMaster;
import ams.cms.services.ChargeMasterService;

@RestController
@RequestMapping("/getChargeMasterList")
public class ChargeMasterController
{
	@Autowired
	ChargeMasterService chargeMasterService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addChargingConfigData(@RequestBody AccountTypeCharges accountTypeCharges)
	{
		String result = null;
		try {
			 accountTypeCharges = chargeMasterService.addChargingConfigData(accountTypeCharges);
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
	
	@RequestMapping(value = "/addChargeType", method = RequestMethod.POST)
	public ResponseEntity<?> addChargeType(@RequestBody ChargeMaster chargeMaster)
	{
		String result = null;
		try {
			chargeMaster.setStrDateOfCreation(new Date());
			System.out.println("chargeMaster::"+chargeMaster);
			chargeMaster = chargeMasterService.addChargeType(chargeMaster);
			if(chargeMaster.getStrID() != null)
			{
				return ResponseEntity.ok(chargeMaster.getStrID());  
			}
			
		}catch (Exception e) {
			System.out.println("ChargeMasterController.addChargeType()"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/validateChargeType", method = RequestMethod.POST)
	public ResponseEntity<?> validateChargeType(@RequestBody ChargeMaster chargeMaster)
	{
		String result = null;
		try 
		{
			Boolean test = chargeMasterService.validateChargeType(chargeMaster);
			System.out.println("test:::"+test);
			return ResponseEntity.ok(test);
		}
		catch (Exception e) 
		{
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
	
	@RequestMapping(value = "/getChargesMasterList", method = RequestMethod.POST)
	public ResponseEntity<?> getChargeMasterList(@RequestBody ChargeMaster chargeMaster)
	{
		String result = null;
		try 
		{
			List<ChargeMaster> chargeMasters = chargeMasterService.getChargeMasterList(chargeMaster);
			if (chargeMasters != null && chargeMasters.size()>0)
			{
				return ResponseEntity.ok(chargeMasters);
			}			
		}
		catch (Exception e) {
			System.out.println("ChargeMasterController.getChargeMasterList()"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getTransactionChargeList", method = RequestMethod.POST)
	public ResponseEntity<?> getTransactionChargeList(@RequestBody ChargeMaster chargeMaster)
	{
		String result = null;
		try 
		{
			List<ChargeMaster> chargeMasters = chargeMasterService.getTransactionChargeList(chargeMaster);
			if (chargeMasters != null && chargeMasters.size()>0)
			{
				return ResponseEntity.ok(chargeMasters);
			}			
		}
		catch (Exception e) {
			System.out.println("ChargeMasterController.getTransactionChargeList()"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	
	@RequestMapping(value = "/getFuelChargeList", method = RequestMethod.POST)
	public ResponseEntity<?> getFuelChargeList(@RequestBody ChargeMaster chargeMaster)
	{
		String result = null;
		try 
		{
			List<ChargeMaster> chargeMasters = chargeMasterService.getFuelChargeList(chargeMaster);
			if (chargeMasters != null && chargeMasters.size()>0)
			{
				return ResponseEntity.ok(chargeMasters);
			}			
		}
		catch (Exception e) {
			System.out.println("ChargeMasterController.getFuelChargeList()"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
}
