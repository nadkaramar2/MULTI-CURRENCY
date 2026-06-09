package ams.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.AccountTransactionLimitation;
import ams.cms.services.AccountTransactionLimitationService;

@RestController
@RequestMapping("/account-txn-limit")
public class AccountTransactionLimitationController 
{
	@Autowired
	AccountTransactionLimitationService accountTransactionLimitationService;
	
	@RequestMapping(value = "/getAccountTxnLimit", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountTxnLimitBasedOnParam(@RequestBody AccountTransactionLimitation accountTransactionLimitation) 
	{
		String result = null;
		try 
		{
			AccountTransactionLimitation accountTransactionLimitationObj = accountTransactionLimitationService.getAccountTxnLimitBasedOnParam(accountTransactionLimitation);
			return ResponseEntity.ok(accountTransactionLimitationObj);
		} 
		catch (Exception e) {
			System.out.println("Exception in getAccountTxnLimitBasedOnParam::" + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
}
