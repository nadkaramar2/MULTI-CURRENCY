package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.api.model.UpgradeTierReqRes;
import ams.cms.api.service.UpgradeTierReqResService;
import ams.cms.model.ApproveUpgradeTier;
import ams.cms.model.CustomerIdCreation;
import ams.cms.services.ApproveUpgradeTierService;
import ams.cms.services.CustomerIDCreationService;
import ams.cms.utility.Utils;

@RestController
@RequestMapping("/approve_upgrade_tier")
public class ApproveUpgradeTierController
{
	@Autowired
	ApproveUpgradeTierService approveUpgradeTierService;
	
	@Autowired
	UpgradeTierReqResService upgradeTierReqResService;
	
	@Autowired
	CustomerIDCreationService customerIDCreationService;
	
	@RequestMapping(value = "/getUpgradeTierType", method = RequestMethod.POST)
	public ResponseEntity<?> getIssuedAccountBasedOnCardLinked(@RequestBody ApproveUpgradeTier approveUpgradeTier)
	{
		try 
		{
			List<ApproveUpgradeTier> approveUpgradeList = approveUpgradeTierService.getUpgradeTierType(approveUpgradeTier);
			return ResponseEntity.ok(approveUpgradeList);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}

	@RequestMapping(value = "/approve", method = RequestMethod.POST)
	public ResponseEntity<?> approveTier(@RequestBody UpgradeTierReqRes upgradeTierReqRes)
	{
		try 
		{
			upgradeTierReqRes.setStrRejectedReason("");
			upgradeTierReqRes.setStrReqStatus("approved");
			upgradeTierReqRes.setStrResDateTime(Utils.getCurrentDate());
			upgradeTierReqRes.setStrRejectedReason("");
			
			upgradeTierReqResService.updateUpgradeRequest(upgradeTierReqRes);
			
			CustomerIdCreation customerIdCreation = new CustomerIdCreation();
			customerIdCreation.setStrCustId(upgradeTierReqRes.getStrCustId());
			customerIdCreation.setStrActiveTier(upgradeTierReqRes.getStrTierType());
			
			customerIDCreationService.updateCustomerAccountTier(customerIdCreation);
			
			return ResponseEntity.ok("approved");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("failed");
	}
	
	@RequestMapping(value = "/reject", method = RequestMethod.POST)
	public ResponseEntity<?> rejectTier(@RequestBody UpgradeTierReqRes upgradeTierReqRes)
	{
		try 
		{
			upgradeTierReqRes.setStrReqStatus("rejected");
			upgradeTierReqRes.setStrResDateTime(Utils.getCurrentDate());
			
			upgradeTierReqResService.updateUpgradeRequest(upgradeTierReqRes);
			return ResponseEntity.ok("rejected");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("failed");
	}

	
}
