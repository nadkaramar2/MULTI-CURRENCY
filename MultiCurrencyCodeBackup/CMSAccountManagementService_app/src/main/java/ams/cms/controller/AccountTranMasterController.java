package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.AccountTranMaster;
import ams.cms.services.AccountTranMasterService;

@RestController
@RequestMapping("/accountTxnMaster")
public class AccountTranMasterController 
{
	@Autowired
	private AccountTranMasterService accountTranMasterService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addAccountTransactionMasterData(@RequestBody AccountTranMaster accountTranMaster) 
	{
		String result = null;
		try 
		{
			accountTranMaster = accountTranMasterService.addAccountTransactionData(accountTranMaster);
			return ResponseEntity.ok(accountTranMaster.getStrID());
		} 
		catch (Exception e) {
			System.out.println("Exception in addAccountTypeWiseWallet::" + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/searchTransactionByTxnId", method = RequestMethod.POST)
	public ResponseEntity<?> getTransactionByTxnId(@RequestBody AccountTranMaster accountTranMaster) 
	{
		String result = null;
		try 
		{
			List<AccountTranMaster> txnbyId = accountTranMasterService.getTransactionByTxnId(accountTranMaster);
			return ResponseEntity.ok(txnbyId);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
}
