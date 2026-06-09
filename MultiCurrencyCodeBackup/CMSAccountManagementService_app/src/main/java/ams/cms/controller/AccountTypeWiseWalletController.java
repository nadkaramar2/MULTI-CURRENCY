package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.AccountTypeWiseWalletMaster;
import ams.cms.services.AccountTypeWiseWalletService;

@RestController
@RequestMapping("/account_type_wallet")
public class AccountTypeWiseWalletController 
{
	@Autowired
	AccountTypeWiseWalletService accountTypeWiseWalletService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addAccountTypeWiseWallet(@RequestBody AccountTypeWiseWalletMaster accountTypeWiseWalletMaster)
	{
		String result = null;
		try 
		{
			accountTypeWiseWalletMaster = accountTypeWiseWalletService.addAccountTypeWiseWallet(accountTypeWiseWalletMaster);
			if (accountTypeWiseWalletMaster.getStrID() != null)
			{
				return ResponseEntity.ok(accountTypeWiseWalletMaster);
			}			
		}
		catch (Exception e) {
			System.out.println("Exception in addAccountTypeWiseWallet::"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/accountTypeBasedWallet", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountTypeBasedWallet(@RequestBody AccountTypeWiseWalletMaster accountTypeWiseWalletMaster)
	{
		String result = null;
		try 
		{
			List<AccountTypeWiseWalletMaster> accountTypeWiseWalletMasters = accountTypeWiseWalletService.getAccountTypeWiseWalletMasterList(accountTypeWiseWalletMaster);
			if (accountTypeWiseWalletMasters != null && accountTypeWiseWalletMasters.size() > 0)
			{
				return ResponseEntity.ok(accountTypeWiseWalletMasters);
			}			
		}
		catch (Exception e) {
			System.out.println("Exception in addAccountTypeWiseWallet::"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
}
