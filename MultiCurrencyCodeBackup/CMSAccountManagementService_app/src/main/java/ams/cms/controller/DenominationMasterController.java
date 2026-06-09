package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.api.model.DenominationMaster;
import ams.cms.api.service.DenominationMasterService;


@RestController
@RequestMapping("/denomination_master")
public class DenominationMasterController
{
	@Autowired
	DenominationMasterService denominationMasterService;
	
	@RequestMapping(value = "/getTxnDenominationDetails", method = RequestMethod.POST)
	public ResponseEntity<?> getTxnDenominationDetails(@RequestBody DenominationMaster denominationMaster)
	{
		String result = null;
		try 
		{
			List<DenominationMaster> denominationMasterResp = denominationMasterService.getTxnDenominationDetails(denominationMaster);
			if (denominationMasterResp!=null && denominationMasterResp.size() > 0)
			{
				return ResponseEntity.ok(denominationMasterResp);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	
	
	@RequestMapping(value = "/getTxnDenominationDetailsbyAgent", method = RequestMethod.POST)
	public ResponseEntity<?> getTxnDenominationDetailsbyAgent(@RequestBody DenominationMaster denominationMaster)
	{
		String result = null;
		try 
		{
			List<DenominationMaster> denominationMasterResp = denominationMasterService.getTxnDenominationDetailsbyAgent(denominationMaster);
			if (denominationMasterResp!=null && denominationMasterResp.size() > 0)
			{
				return ResponseEntity.ok(denominationMasterResp);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
}
