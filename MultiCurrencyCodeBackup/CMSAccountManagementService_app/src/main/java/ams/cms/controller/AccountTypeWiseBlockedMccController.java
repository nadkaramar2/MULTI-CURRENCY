package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.AccountTypeWiseBlockedMccMaster;
import ams.cms.services.AccountTypeWiseBlockedMccService;

@RestController
@RequestMapping("/blocked_mcc_account_type_wise")
public class AccountTypeWiseBlockedMccController 
{
	@Autowired
	AccountTypeWiseBlockedMccService accountTypeWiseBlockedMccService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addAccountTypeWiseBlockMccCodes(
			@RequestBody AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMaster) {
		String result = null;
		try {
			accountTypeWiseBlockedMccMaster = accountTypeWiseBlockedMccService.addAccountTypeWiseBlockedMcc(accountTypeWiseBlockedMccMaster);
			if (accountTypeWiseBlockedMccMaster.getStrID() != null) {
				return ResponseEntity.ok(accountTypeWiseBlockedMccMaster);
			}
		} catch (Exception e) {
			System.out.println("Exception in addAccountTypeWiseWallet::" + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/getUnblockMCC", method = RequestMethod.POST)
	public ResponseEntity<?> getBlockMccData(
			@RequestBody AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMaster) {
		String result = null;
		try {
			List<AccountTypeWiseBlockedMccMaster> accountTypeWiseBlockedMccMasters = accountTypeWiseBlockedMccService.getAccountTypeWiseUnBlockedMccMaster(accountTypeWiseBlockedMccMaster);

			if (accountTypeWiseBlockedMccMasters != null && accountTypeWiseBlockedMccMasters.size() > 0) {
				return ResponseEntity.ok(accountTypeWiseBlockedMccMasters);
			}
		} catch (Exception e) {
			System.out.println("Exception in addAccountTypeWiseWallet::" + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/getBlockMccAccountTypeWise", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountTypeWiseUnBlockedMccMaster(
			@RequestBody AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMaster) {
		String result = null;
		try {
			List<AccountTypeWiseBlockedMccMaster> accountTypeWiseBlockedMccMasters = accountTypeWiseBlockedMccService
					.getAccountTypeWiseUnBlockedMccMaster(accountTypeWiseBlockedMccMaster);

			if (accountTypeWiseBlockedMccMasters != null && accountTypeWiseBlockedMccMasters.size() > 0) {
				return ResponseEntity.ok(accountTypeWiseBlockedMccMasters);
			}
		} catch (Exception e) {
			System.out.println("Exception in ::::" + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}

	
	
	
	// Prashant
	@RequestMapping(value = "/getBlockMccListAccountTypeWise", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountTypeWiseBlockMccMaster(
			@RequestBody AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMaster) {
		String result = null;
		try {
			List<AccountTypeWiseBlockedMccMaster> accountTypeWiseBlockedMcc = accountTypeWiseBlockedMccService.getBlockMccListAccountTypeWise(accountTypeWiseBlockedMccMaster);

			if (accountTypeWiseBlockedMcc != null && accountTypeWiseBlockedMcc.size() > 0) {
				return ResponseEntity.ok(accountTypeWiseBlockedMcc);
			}
		} catch (Exception e) {
			System.out.println("Exception in ::::" + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);

	}

}
