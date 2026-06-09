package ams.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.RevolvingCreditInterestTxn;
import ams.cms.services.RevolvingCreditInterestTxnService;

@RestController
@RequestMapping("/revolvingCreditInterestTxn")
public class RevolvingCreditInterestTxnController {
	
	@Autowired
	RevolvingCreditInterestTxnService revolvingCreditInterestTxnService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addAccountStatementData(@RequestBody RevolvingCreditInterestTxn revolvingCreditInterestTxn) 
	{
		String result = null;
		try 
		{	
			revolvingCreditInterestTxn = revolvingCreditInterestTxnService.addAccountTransactionData(revolvingCreditInterestTxn);
			return ResponseEntity.ok(revolvingCreditInterestTxn);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}


}
