package ams.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.AccountLoadMaster;
import ams.cms.services.AccountLoadMasterService;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountTranMasterService;

@RestController
@RequestMapping("/account-load")
public class AccountLoadMasterController 
{
	@Autowired
	AccountLoadMasterService accountLoadMasterService;
	
	@Autowired
	AccountMasterService accountMasterService;
	
	@Autowired
	AccountTranMasterService accountTranMasterService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addAccountLoadData(@RequestBody AccountLoadMaster accountLoadMaster) 
	{
		String result = null;
		try 
		{
			accountLoadMaster = accountLoadMasterService.saveAccountLoadMasterInfo(accountLoadMaster);
			return ResponseEntity.ok(accountLoadMaster);
		} 
		catch (Exception e) {
			System.out.println("Exception in addAccountTypeWiseWallet::" + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/loadBalance", method = RequestMethod.POST)
	public ResponseEntity<?> loadAccountData(@RequestBody AccountLoadMaster accountLoadMaster) 
	{
		String result = null;
		try 
		{
			accountLoadMaster = accountLoadMasterService.saveAccountLoadMasterInfo(accountLoadMaster);
			return ResponseEntity.ok(accountLoadMaster);
		} 
		catch (Exception e) {
			System.out.println("Exception in addAccountTypeWiseWallet::" + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
}
