package ams.cms.controller;

//Created by Prashant Tayde for Charge Related Master Data

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.ChargeRelatedMaster;
import ams.cms.services.ChargeRelatedMasterService;

@RestController
@RequestMapping("/chargeRelatedMaster")
public class ChargeRelatedMasterController {

	
	@Autowired
	ChargeRelatedMasterService chargeRelatedService;

	
	//changes added by prashant for getting chargeRelated List Data
	
	@RequestMapping(value = "/getchargeRelatedList", method = RequestMethod.POST)
	public ResponseEntity<?> getchargeRelatedList(@RequestBody ChargeRelatedMaster chargeRelatedMaster)
	{
		try 
		{
			List<ChargeRelatedMaster> chargeRelatedList = chargeRelatedService.getChargeRelatedList(chargeRelatedMaster);
			return ResponseEntity.ok(chargeRelatedList);				
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	
	
	@RequestMapping(value = "/getChargeRelatedDescription", method = RequestMethod.POST)
	public ResponseEntity<?> getChargeRelatedDescription(@RequestBody ChargeRelatedMaster chargeRelatedMaster)
	{
		String result = null;
		try 
		{
			result = chargeRelatedService.getChargeRelatedDescription(chargeRelatedMaster);
			return ResponseEntity.ok(result);				
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/addChargeRelated", method = RequestMethod.POST)
	public ResponseEntity<?> addChargeRelated(@RequestBody ChargeRelatedMaster chargeRelatedMaster)
	{
		String result = null;
		try {
			
			
			chargeRelatedMaster = chargeRelatedService.addChargeRelated(chargeRelatedMaster);
			if(chargeRelatedMaster.getStrID() != null)
			{
				return ResponseEntity.ok(chargeRelatedMaster.getStrID());  
			}
			
		}catch (Exception e) {
			System.out.println("ChargeRelatedMasterController.addChargeRelated()"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	
	
	
	@RequestMapping(value = "/validateChargeRelated", method = RequestMethod.POST)
	public ResponseEntity<?> validateChargeRelated(@RequestBody ChargeRelatedMaster chargeRelatedMaster)
	{
		String result = null;
		try 
		{
			Boolean validateChargeRelated = chargeRelatedService.validateChargeRelated(chargeRelatedMaster);
			System.out.println("validateChargeRelated:::"+validateChargeRelated);
			return ResponseEntity.ok(validateChargeRelated);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}

	
	
}
